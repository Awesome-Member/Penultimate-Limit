package battlepck.client;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Engine.Pair;
import desktop.PlatformRuntime;
import Moduls.chat.Chat;
import battlepck.Battle;
import battlepck.BattleEvent;
import battlepck.BattleManager;
import battlepck.BattleOrder;
import battlepck.BattleQuantumEvent;
import java.io.DataInputStream;
import java.io.EOFException;
import java.util.Vector;

public final class BattleNetTh implements Runnable {
   private static Thread btNetThThread = null;
   public static BattleNetTh btNetTh = null;
   public boolean active = false;
   private static Object lock = new Object();
   private Pair indirectNetworkCheckTriger = null;

   public static void waitOldDone() {
      while(true) {
         synchronized(lock) {
            if (btNetTh == null) {
               return;
            }

            btNetTh.active = false;
         }

         Thread.yield();

         try {
            Thread.sleep(10L);
         } catch (Exception exception) {
         }
      }
   }

   public static void runNewWithWaitOldDone() {
      waitOldDone();
      synchronized(lock) {
         btNetTh = new BattleNetTh();
         (btNetThThread = PlatformRuntime.withBackgroundPriority(new Thread(btNetTh))).start();
      }
   }

   private static void writeOrders(NetRequest netRequest, BattleOrder[] orders) {
      try {
         netRequest.request.writeInt(orders.length);

         for(int orderIndex = 0; orderIndex < orders.length; ++orderIndex) {
            BattleOrder battleOrder = orders[orderIndex];
            netRequest.request.writeInt(battleOrder.unitInd);
            netRequest.request.writeInt(battleOrder.targetInd);
            netRequest.request.writeInt(battleOrder.targetX);
            netRequest.request.writeInt(battleOrder.targetY);
            netRequest.request.writeByte(battleOrder.orderType);
            netRequest.request.writeInt(battleOrder.bushInd);
            netRequest.request.writeInt(battleOrder.extraData);
         }

      } catch (Exception exception) {
         Com.SendMistace("Can't send orders " + exception.toString(), true, true, exception);
      }
   }

   public final void run() {
      PlatformRuntime.initializeThread();
      this.active = true;

      while(this.active) {
         BattleNetTh battleNetTh = this;
         byte processingStage = 0;

         try {
            boolean unusedProgressMarker = true;
            if (!BattleTh.realBattle && IndirectNetworkHandler.instance.isDoneWithDataFromSecondThreadByMainThread(battleNetTh.indirectNetworkCheckTriger)) {
               battleNetTh.indirectNetworkCheckTriger = null;
               NetRequest netRequest;
               (netRequest = new NetRequest()).writeHeader(4622000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4622001) {
                  battleNetTh.indirectNetworkCheckTriger = IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response, false, false);
               }

               if (netRequest.requestBuffer != null) {
                  netRequest.requestBuffer.reset();
                  netRequest.requestBuffer.close();
                  netRequest.requestBuffer = null;
               }

               if (netRequest.request != null) {
                  netRequest.request.close();
                  netRequest.request = null;
               }

               netRequest.responseBytes = null;
               if (netRequest.responseBuffer != null) {
                  netRequest.responseBuffer.close();
               }

               if (netRequest.response != null) {
                  netRequest.response.close();
               }

               netRequest.responseBuffer = null;
               netRequest.response = null;
            }

            battleNetTh.processEventsAction();
            Com.fixLag();
            Thread.yield();
         } catch (Exception exception) {
            this.active = false;
            Com.SendMistace("Error in battleTh " + processingStage + " " + exception.toString(), true, true, exception);
         }
      }

      synchronized(lock) {
         if (btNetTh == this) {
            btNetTh = null;
            btNetThThread = null;
         }

      }
   }

   public static void setActive() {
      synchronized(lock) {
         if (btNetTh != null) {
            btNetTh.active = false;
         }

      }
   }

   private void processEventsAction() {
      if (this.active) {
         Chat.instance.processHeadMessages();
         if (BattleManager.getCurrentBattle() != null) {
            boolean requestSucceeded = false;
            boolean eventsReceived = false;
            DataInputStream dataInputStream = null;
            int integerCode;
            if (BattleTh.realBattle) {
               if (IndirectNetworkHandler.instance.isDoneWithDataFromSecondThreadByMainThread(this.indirectNetworkCheckTriger)) {
                  BattleOrder[] orders = BattleManager.getCurrentBattle().pullAccumOrders();
                  this.indirectNetworkCheckTriger = null;
                  NetRequest netRequest;
                  (netRequest = new NetRequest()).writeHeader(1300100);
                  writeOrders(netRequest, orders);
                  Com.sendRequest(netRequest);
                  requestSucceeded = netRequest.success;
                  dataInputStream = netRequest.response;
               } else {
                  eventsReceived = true;
               }
            } else {
               BattleManager.getCurrentBattle().pullAccumOrders();
               dataInputStream = BattleTh.viewBattleDIS;
               if (BattleManager.getCurrentBattle().getLeftQuantumEventsCount() <= 10) {
                  integerCode = 0;
                  BattleQuantumEvent[] battleQuantumEvents = new BattleQuantumEvent[10];

                  int battleQuantumEventIndex;
                  for(int retryIndex = 0; retryIndex < 10 && this.active && integerCode < battleQuantumEvents.length; ++retryIndex) {
                     if (BattleTh.viewBattleNextReadnQuantumEvent == null) {
                        battleQuantumEventIndex = -1;
                        Vector vector3 = new Vector();

                        while(BattleTh.viewBattleWillEndTick < 0) {
                           try {
                              if (BattleTh.viewBattleNextPacketSize < 0) {
                                 BattleTh.viewBattleNextPacketSize = dataInputStream.readInt();
                                 BattleTh.viewBattleNextPacketId = dataInputStream.readInt();
                                 BattleTh.viewBattleNextPacketTickInd = dataInputStream.readInt();
                              }

                              if (battleQuantumEventIndex != -1 && battleQuantumEventIndex != BattleTh.viewBattleNextPacketTickInd) {
                                 break;
                              }

                              battleQuantumEventIndex = BattleTh.viewBattleNextPacketTickInd;
                              switch (BattleTh.viewBattleNextPacketId) {
                                 case 1600:
                                    BattleEvent battleEvent = new BattleEvent(dataInputStream);
                                    vector3.addElement(battleEvent);
                                    break;
                                 case 1700:
                                    BattleTh.viewBattleWillEndTick = battleQuantumEventIndex;
                              }

                              BattleTh.viewBattleNextPacketSize = -1;
                           } catch (EOFException exception) {
                              if (BattleTh.viewBattleWillEndTick < 0) {
                                 BattleTh.viewBattleWillEndTick = BattleTh.viewBattleLastAddedEventsTillTick + integerCode;
                              }
                              break;
                           } catch (Exception exception2) {
                              this.active = false;
                              BattleTh.viewBattleWillEndTick = BattleTh.viewBattleLastAddedEventsTillTick + integerCode;
                              BattleManager.addBattleFinishEventToActualBattle();
                              break;
                           }
                        }

                        if (battleQuantumEventIndex >= 0) {
                           BattleEvent[] battleEvents = new BattleEvent[vector3.size()];

                           for(int sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
                              battleEvents[sizeIndex] = (BattleEvent)vector3.elementAt(sizeIndex);
                           }

                           BattleTh.viewBattleNextReadnQuantumEvent = new BattleQuantumEvent(battleEvents);
                           BattleTh.viewBattleNextReadnQuantumEventTick = battleQuantumEventIndex;
                        }
                     }

                     if (BattleTh.viewBattleNextReadnQuantumEvent != null) {
                        if (BattleTh.viewBattleNextReadnQuantumEventTick == BattleTh.viewBattleLastAddedEventsTillTick + integerCode) {
                           battleQuantumEvents[integerCode++] = BattleTh.viewBattleNextReadnQuantumEvent;
                           BattleTh.viewBattleNextReadnQuantumEvent = null;
                        } else {
                           battleQuantumEvents[integerCode++] = new BattleQuantumEvent(new BattleEvent[0]);
                        }
                     }

                     if (BattleTh.viewBattleWillEndTick >= 0 && !BattleTh.viewBattleWillEndTickApplyed && BattleTh.viewBattleWillEndTick <= BattleTh.viewBattleLastAddedEventsTillTick + integerCode) {
                        BattleTh.viewBattleWillEndTickApplyed = true;
                        if (integerCode == battleQuantumEvents.length) {
                           BattleQuantumEvent[] battleQuantumEvents2 = battleQuantumEvents;
                           battleQuantumEvents = new BattleQuantumEvent[battleQuantumEvents.length + 1];
                           System.arraycopy(battleQuantumEvents2, 0, battleQuantumEvents, 0, battleQuantumEvents2.length);
                        }

                        battleQuantumEvents[integerCode++] = null;
                        this.active = false;
                     }
                  }

                  if (battleQuantumEvents.length != integerCode) {
                     BattleQuantumEvent[] battleQuantumEvents3 = battleQuantumEvents;
                     battleQuantumEvents = new BattleQuantumEvent[integerCode];
                     System.arraycopy(battleQuantumEvents3, 0, battleQuantumEvents, 0, integerCode);
                  }

                  Vector vector4 = new Vector(battleQuantumEvents.length);

                  for(battleQuantumEventIndex = 0; battleQuantumEventIndex < battleQuantumEvents.length; ++battleQuantumEventIndex) {
                     vector4.addElement(battleQuantumEvents[battleQuantumEventIndex]);
                  }

                  BattleManager.addEventsWithoutSpeedCorrectionToActualBattle(vector4);
                  BattleTh.viewBattleLastAddedEventsTillTick += integerCode;
               }
            }

            if (requestSucceeded) {
               try {
                  ++BattleManager.getCurrentBattle().hasUnDoneActSendNum;
                  integerCode = 1300101;
                  if (BattleTh.realBattle) {
                     integerCode = dataInputStream.readInt();
                  }

                  switch (integerCode) {
                     case 1101119:
                        this.active = true;
                        Com.showFatalScreen("Осуществлен вход персонажем с другого устройства");
                        return;
                     case 1300101:
                        if (BattleTh.realBattle) {
                           this.loadFromStream(dataInputStream);
                           this.indirectNetworkCheckTriger = IndirectNetworkHandler.instance.loadFromStreamAndApply(dataInputStream, false, false);
                           return;
                        }
                        break;
                     case 1300110:
                        BattleTh.res_ExitReason = dataInputStream.readInt();
                        this.loadFromStream(dataInputStream);
                        Chat.chatWindow.diactivate();
                        Com.textWrite.totalExit();

                        while(Chat.chatWindow.isActive || Com.textWrite.isActive) {
                           Thread.yield();
                        }

                        this.active = false;
                        BattleManager.addBattleFinishEventToActualBattle();
                        return;
                     case 1300201:
                        return;
                     default:
                        this.active = false;
                        Com.SendMistace("Income wrong packet in battle " + integerCode, true, false, (Exception)null);
                        Com.showFatalScreen("Соединение с сервером потеряно. Перезапустите игру.");
                  }
               } catch (Exception exception3) {
                  Com.SendMistace("Can't get orders " + exception3.toString(), true, true, exception3);
                  return;
               }
            } else if (BattleTh.realBattle && !eventsReceived) {
               this.active = false;
               Com.SendMistace("BattleNetTh cant send req", true, true, (Exception)null);
            }
         }

      }
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.readEvents(dataInputStream, shortCode);
   }

   private void readEvents(DataInputStream dataInputStream, int count) {
      label56:
      while(true) {
         Vector vector3 = new Vector();
         int previousQuantumIndex = -1;
         Vector vector4 = new Vector();

         int byteCode;
         for(int countIndex = 0; countIndex < count; ++countIndex) {
            if ((byteCode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream)) != 0 && byteCode != 2) {
               if (byteCode == 1) {
                  System.out.println("income battle type 1");
                  BattleManager.setReplaceBattle(new Battle(Com.currentBattleMap, dataInputStream, new Object()));
                  count = count - countIndex - 1;
                  continue label56;
               }
            } else {
               int integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
               if (previousQuantumIndex == -1) {
                  previousQuantumIndex = integerCode;
               }

               if (previousQuantumIndex != integerCode) {
                  BattleEvent[] vector4Count = new BattleEvent[vector4.size()];

                  for(int lengthIndex = 0; lengthIndex < vector4Count.length; ++lengthIndex) {
                     vector4Count[lengthIndex] = (BattleEvent)vector4.elementAt(lengthIndex);
                  }

                  BattleQuantumEvent battleQuantumEvent = new BattleQuantumEvent(vector4Count);
                  vector3.addElement(battleQuantumEvent);
                  previousQuantumIndex = integerCode;
                  vector4.removeAllElements();
               }

               if (byteCode == 0) {
                  vector4.addElement(new BattleEvent(dataInputStream));
               }
            }
         }

         if (previousQuantumIndex >= 0) {
            BattleEvent[] vector4Count2 = new BattleEvent[vector4.size()];

            for(byteCode = 0; byteCode < vector4Count2.length; ++byteCode) {
               vector4Count2[byteCode] = (BattleEvent)vector4.elementAt(byteCode);
            }

            BattleQuantumEvent battleQuantumEvent2 = new BattleQuantumEvent(vector4Count2);
            vector3.addElement(battleQuantumEvent2);
            vector4.removeAllElements();
         }

         BattleManager.addEventsWithSpeedCorrectionToActualBattle(vector3);
         return;
      }
   }
}
