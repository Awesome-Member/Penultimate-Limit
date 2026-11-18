package Base;

import Engine.Pair;
import desktop.PlatformRuntime;
import Moduls.Strategist;
import Moduls.chat.Chat;
import Moduls.otherheroes.HeroesOnMapManager;
import Moduls.quests.QuestInPlayerKillPlayerDescExtend;
import Moduls.quests.QuestInPlayerKillPlayerPositionExtend;
import Moduls.quests.QuestStrateg;
import java.util.Vector;

public final class GlobalMapTh implements Runnable {
   private boolean isActiveWorkLoop = false;
   private boolean isInProcessPhase = false;
   private boolean isProcessPhasePause = true;
   private final Object lockPauseResume = new Object();
   private long loopsCount = 0L;
   private long lastSendTime = -1L;
   private static Object lock = new Object();
   private static Thread gmThThread;
   public static GlobalMapTh gmTh = null;
   private final Vector usedTownsList = new Vector();

   public GlobalMapTh() {
      this.lastSendTime = System.currentTimeMillis();
   }

   public final void clientDeltaAntichitReset() {
      this.lastSendTime = System.currentTimeMillis();
   }

   public static void startThread() {
      synchronized(lock) {
         if (gmTh == null) {
            gmThThread = null;
            gmTh = null;
            (gmTh = new GlobalMapTh()).isActiveWorkLoop = true;
            gmTh.isProcessPhasePause = true;
            (gmThThread = PlatformRuntime.withBackgroundPriority(new Thread(gmTh))).start();
         }
      }
   }

   public static void suspend() {
      synchronized(lock) {
         if (gmTh != null) {
            gmTh.isActiveWorkLoop = false;
         }

      }
   }

   public final void addUsedTown(short townInd) {
      synchronized(this.usedTownsList) {
         this.usedTownsList.addElement(townInd);
      }
   }

   private void process() {
      if (GameCommons.instance.isGameLoopAvailable()) {
         Chat.instance.processHeadMessages();

         try {
            byte processingStage = 0;

            try {
               boolean unusedProgressMarker = true;
               if (!HeroesOnMapManager.instance.isFreeForLoad()) {
                  return;
               }

               unusedProgressMarker = true;
               Pair pair = Com.GameTh.myPathPick();
               NetRequest netRequest = new NetRequest();
               unusedProgressMarker = true;
               netRequest.writeHeader(1400000);
               byte byteCode = 0;
               if (QuestStrateg.instance.isNeedSendCompassedQuest()) {
                  byteCode = 1;
               }

               synchronized(this.usedTownsList) {
                  if (this.usedTownsList.size() > 0) {
                     byteCode = (byte)(byteCode | 2);
                  }
               }

               int[] markPrivatesList;
               if ((markPrivatesList = Chat.instance.pullMarkPrivatesList()) != null) {
                  byteCode = (byte)(byteCode | 4);
               }

               netRequest.request.writeByte(byteCode);
               if ((byteCode & 1) > 0) {
                  QuestStrateg.instance.trySendCompassedQuest(netRequest.request);
               }

               if ((byteCode & 2) > 0) {
                  synchronized(this.usedTownsList) {
                     netRequest.request.writeShort(this.usedTownsList.size());

                     for(int sizeIndex = 0; sizeIndex < this.usedTownsList.size(); ++sizeIndex) {
                        netRequest.request.writeShort((Short)this.usedTownsList.elementAt(sizeIndex));
                     }

                     this.usedTownsList.removeAllElements();
                  }
               }

               if ((byteCode & 4) > 0) {
                  netRequest.request.writeShort(markPrivatesList.length);

                  for(int lengthIndex = 0; lengthIndex < markPrivatesList.length; ++lengthIndex) {
                     netRequest.request.writeInt(markPrivatesList[lengthIndex]);
                  }
               }

               long lastSendTime3;
               long lastSendTime2;
               if ((lastSendTime3 = (lastSendTime2 = System.currentTimeMillis()) - this.lastSendTime) >= 1500L) {
                  this.lastSendTime = lastSendTime2;
                  netRequest.request.writeBoolean(true);
                  netRequest.request.writeLong(lastSendTime3);
               } else {
                  netRequest.request.writeBoolean(false);
               }

               unusedProgressMarker = true;
               netRequest.request.writeShort((Short)pair.first);
               netRequest.request.writeShort(((Vector)pair.second).size());

               int integerCode;
               for(integerCode = 0; integerCode < ((Vector)pair.second).size(); ++integerCode) {
                  GameWalkPathElement gameWalkPathElement = (GameWalkPathElement)((Vector)pair.second).elementAt(integerCode);
                  netRequest.request.writeShort(gameWalkPathElement.x);
                  netRequest.request.writeShort(gameWalkPathElement.y);
                  netRequest.request.writeByte(gameWalkPathElement.watchDirection);
               }

               unusedProgressMarker = true;
               if (Com.sendRequest(netRequest)) {
                  unusedProgressMarker = true;
                  integerCode = netRequest.response.readInt();
                  unusedProgressMarker = true;
                  switch (integerCode) {
                     case 1101119:
                        this.isProcessPhasePause = true;
                        Com.showFatalScreen("Осуществлен вход персонажем с другого устройства");
                        break;
                     case 1400001:
                        if (!netRequest.response.readBoolean()) {
                           Strategist.instance.cx = (short)netRequest.response.readInt();
                           Strategist.instance.cy = (short)netRequest.response.readInt();
                        }

                        Com.dontFightAndAction = false;
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response, false, true);
                        unusedProgressMarker = true;
                        HeroesOnMapManager.instance.loadHeroes(netRequest.response);
                        unusedProgressMarker = true;
                        byte byteCode2;
                        if (((byteCode2 = netRequest.response.readByte()) & 1) > 0) {
                           byteCode = netRequest.response.readByte();

                           for(integerCode = 0; integerCode < byteCode; ++integerCode) {
                              QuestStrateg.instance.addUpdateKillPlayers(new QuestInPlayerKillPlayerDescExtend(netRequest.response));
                           }
                        }

                        if ((byteCode2 & 2) > 0) {
                           byteCode = netRequest.response.readByte();

                           for(integerCode = 0; integerCode < byteCode; ++integerCode) {
                              QuestStrateg.instance.addUpdateKillPlayersPos(new QuestInPlayerKillPlayerPositionExtend(netRequest.response));
                           }
                        }

                        Com.nearbyPlayerCount = netRequest.response.readByte();
                        break;
                     case 1400002:
                        unusedProgressMarker = true;
                        break;
                     case 1400112:
                        unusedProgressMarker = true;
                        GameCommons.instance.showFloatText((String)"Клиент игры\nускорен", 4000);
                        this.isProcessPhasePause = true;
                        Com.showFatalScreen("Клиент игры\nускорен");
                        break;
                     case 1400113:
                        unusedProgressMarker = true;
                        Strategist.instance.cx = (short)netRequest.response.readInt();
                        Strategist.instance.cy = (short)netRequest.response.readInt();
                        break;
                     default:
                        if (integerCode != 1200000) {
                           Com.SendMistace("in GlobalMapTh income incorrect packet " + integerCode, true, false, (Exception)null);
                        }
                  }
               } else {
                  this.isActiveWorkLoop = false;
                  Com.SendMistace("ERROR in GlobalMapTh can't send req " + 6, true, true, (Exception)null);
               }
            } catch (Exception exception) {
               this.isActiveWorkLoop = false;
               Com.SendMistace("ERROR in GlobalMapTh " + processingStage + " " + exception.toString(), true, true, exception);
            }

            Com.fixLag();
         } catch (Exception exception2) {
            this.isActiveWorkLoop = false;
            Com.SendMistace("ERROR in GlobalMapTh BIG " + exception2.toString(), true, true, exception2);
         }
      }
   }

   public final void pauseAndWait(int enter) {
      synchronized(this.lockPauseResume) {
         if (!this.isProcessPhasePause) {
            this.isProcessPhasePause = true;
            long lastWaitCheck = System.currentTimeMillis();
            long waitStart = System.currentTimeMillis();
            this.loopsCount = 0L;

            do {
               IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
               Thread.yield();
               if (System.currentTimeMillis() - lastWaitCheck >= 60000L) {
                  lastWaitCheck = System.currentTimeMillis();
               }
            } while(this.isInProcessPhase);

         }
      }
   }

   public final void resumeNoWait() {
      synchronized(this.lockPauseResume) {
         if (this.isProcessPhasePause) {
            this.isProcessPhasePause = false;
         }
      }
   }

   public final void run() {
      PlatformRuntime.initializeThread();

      while(this.isActiveWorkLoop) {
         GlobalMapTh globalMapTh = this;

         try {
            ++globalMapTh.loopsCount;
            if (!globalMapTh.isProcessPhasePause && !Game.incomeBlockChangeMap) {
               globalMapTh.isInProcessPhase = true;
               globalMapTh.process();
            } else {
               globalMapTh.isInProcessPhase = false;
            }

            try {
               if (System.currentTimeMillis() - Com.lastSendRequestTime >= 8000L && !Com.isActiveRequest) {
                  NetRequest netRequest;
                  (netRequest = new NetRequest()).writeHeader(6700000);
                  Com.sendRequest(netRequest);
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            try {
               Thread.sleep(10L);
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Thread.yield();
         } catch (Exception exception3) {
            exception3.printStackTrace();
         }
      }

      this.isInProcessPhase = false;
      synchronized(lock) {
         if (gmTh == this) {
            gmTh = null;
            gmThThread = null;
         }

      }
   }
}
