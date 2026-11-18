package Moduls.quests;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Base.GameMapObjectsController;
import Engine.Mystery;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.EnemysMap;
import Resources.StringResources;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.Vector;

public final class QuestStrateg {
   public static final QuestStrateg instance = new QuestStrateg();
   public Vector quests = new Vector();
   public Hashtable questsDone = new Hashtable();
   private QuestInPlayer compassedQuest = null;
   private Object sendCompassLock = new Object();
   private boolean sendCompassedQuest = false;
   private QuestInPlayer compassedQuestForSend = null;
   private boolean needCalculateWays = false;
   public String compassQuestInfoName = null;
   public String[] compassQuestInfoText = null;
   public int compassQuestInfoMaxWidth = 0;
   public int compassQuestInfoNameWidth = 0;
   public int[] compassQuestInfoTextsWidth = null;
   private final Vector questsUpdateKillPlayers = new Vector();
   private final Vector questsUpdateKillPlayersPos = new Vector();

   public final void addUpdateKillPlayers(QuestInPlayerKillPlayerDescExtend questInPlayerKillPlayerDescExtend) {
      synchronized(this.questsUpdateKillPlayers) {
         this.questsUpdateKillPlayers.addElement(questInPlayerKillPlayerDescExtend);
      }
   }

   public final boolean processUpdateKillPlayers(Strategist strategist2) {
      boolean res = false;
      int updateIndex;
      QuestInPlayer questInPlayer;
      synchronized(this.questsUpdateKillPlayers) {
         updateIndex = this.questsUpdateKillPlayers.size() - 1;

         while(true) {
            if (updateIndex < 0) {
               this.questsUpdateKillPlayers.removeAllElements();
               break;
            }

            QuestInPlayerKillPlayerDescExtend questInPlayerKillPlayerDescExtend = (QuestInPlayerKillPlayerDescExtend)this.questsUpdateKillPlayers.elementAt(updateIndex);
            if ((questInPlayer = this.findGetnQuest(questInPlayerKillPlayerDescExtend.idSmall)) != null) {
               questInPlayer.needToKillPlayer = questInPlayerKillPlayerDescExtend.needToKillPlayer;
               if (questInPlayer == this.compassedQuest) {
                  this.calculateCompassedQuestInfoTexts(strategist2);
               }
            }

            --updateIndex;
         }
      }

      synchronized(this.questsUpdateKillPlayersPos) {
         for(updateIndex = this.questsUpdateKillPlayersPos.size() - 1; updateIndex >= 0; --updateIndex) {
            QuestInPlayerKillPlayerPositionExtend questInPlayerKillPlayerPositionExtend = (QuestInPlayerKillPlayerPositionExtend)this.questsUpdateKillPlayersPos.elementAt(updateIndex);
            int playerId2 = questInPlayerKillPlayerPositionExtend.playerId;
            QuestStrateg questStrateg = this;
            int questIndex = 0;

            QuestInPlayer questInPlayer2;
            while(true) {
               if (questIndex >= questStrateg.quests.size()) {
                  questInPlayer2 = null;
                  break;
               }

               QuestInPlayer questInPlayer3;
               if ((questInPlayer3 = (QuestInPlayer)questStrateg.quests.elementAt(questIndex)).questSingle.isNeedToKillPlayer && questInPlayer3.needToKillPlayer.needToKillPlayerId == playerId2) {
                  questInPlayer2 = questInPlayer3;
                  break;
               }

               ++questIndex;
            }

            questInPlayer = questInPlayer2;
            if (questInPlayer2 != null) {
               questInPlayer.needToKillPlayerPos = questInPlayerKillPlayerPositionExtend.needToKillPlayerPos;
               if (Com.currentGameMap != null && Com.EnsMap != null) {
                  Strategist instance2 = Strategist.instance;
                  questInPlayer.calculateWays(Com.currentGameMap, Com.EnsMap, Strategist.instance);
               }

               res = true;
            }
         }

         this.questsUpdateKillPlayersPos.removeAllElements();
         return res;
      }
   }

   public final void addUpdateKillPlayersPos(QuestInPlayerKillPlayerPositionExtend questInPlayerKillPlayerPositionExtend) {
      synchronized(this.questsUpdateKillPlayersPos) {
         this.questsUpdateKillPlayersPos.addElement(questInPlayerKillPlayerPositionExtend);
      }
   }

   public final QuestInPlayer getCompassedQuest() {
      return this.compassedQuest;
   }

   public final boolean recordDecoratorUse(String mapName, short townInd, String decoratorName) {
      boolean recorded = false;

      for(int sizeIndex = 0; sizeIndex < this.quests.size(); ++sizeIndex) {
         if (((QuestInPlayer)this.quests.elementAt(sizeIndex)).tryAddDecoratorUse(mapName, townInd, decoratorName)) {
            recorded = true;
         }
      }

      if (recorded) {
         Com.onQuestsChanged(true);
      }

      return recorded;
   }

   private void onQuestRemove(QuestInPlayer questInPlayer) {
      if (questInPlayer != null && this.compassedQuest != null) {
         if (this.compassedQuest.questSingle.equalsById((QuestSingleIdSmall)questInPlayer.questSingle)) {
            this.compassedQuest = null;
            questInPlayer = null;

            for(int i = 0; i < this.quests.size(); ++i) {
               QuestInPlayer questInPlayer2;
               if ((questInPlayer2 = (QuestInPlayer)this.quests.elementAt(i)).questSingle.isDone(Strategist.instance, questInPlayer2)) {
                  questInPlayer = questInPlayer2;
                  break;
               }

               if (questInPlayer == null || questInPlayer2.questSingle.reqMinLevelLevel >= questInPlayer.questSingle.reqMinLevelLevel) {
                  questInPlayer = questInPlayer2;
               }
            }

            if (questInPlayer != null) {
               this.changeCompassedQuest(questInPlayer);
               return;
            }

            Strategist instance2 = Strategist.instance;
            this.calculateCompassedQuestInfoTexts(instance2);
         }

      }
   }

   public final void changeCompassedQuest(QuestInPlayer questInPlayer) {
      if (questInPlayer != null) {
         GameCommons.instance.showFloatText(questInPlayer.questSingle.name + "\nотслеживается");
      }

      this.compassedQuest = questInPlayer;
      synchronized(this.sendCompassLock) {
         this.sendCompassedQuest = true;
         this.compassedQuestForSend = questInPlayer;
      }

      this.onCompassedQuestChanged(Strategist.instance);
   }

   public final void trySendCompassedQuest(DataOutputStream dataOutputStream) {
      synchronized(this.sendCompassLock) {
         if (this.sendCompassedQuest) {
            this.sendCompassedQuest = false;
            decompiledsupport.UncheckedDataInput.writeBoolean(dataOutputStream, true);
            if (this.compassedQuestForSend != null) {
               decompiledsupport.UncheckedDataInput.writeBoolean(dataOutputStream, true);
               this.compassedQuestForSend.questSingle.saveToStreamId(dataOutputStream);
            } else {
               decompiledsupport.UncheckedDataInput.writeBoolean(dataOutputStream, false);
            }
         } else {
            decompiledsupport.UncheckedDataInput.writeBoolean(dataOutputStream, false);
         }

      }
   }

   public final boolean isNeedSendCompassedQuest() {
      synchronized(this.sendCompassLock) {
         return this.sendCompassedQuest;
      }
   }

   private void loadCompassedQuest(DataInputStream dataInputStream) {
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         QuestSingleIdSmall questSingleIdSmall = new QuestSingleIdSmall(dataInputStream);
         this.compassedQuest = this.findGetnQuest(questSingleIdSmall);
      } else {
         this.compassedQuest = null;
      }

      Strategist instance2 = Strategist.instance;
      this.calculateCompassedQuestInfoTexts(instance2);
   }

   public final void loadJustWays(DataInputStream dataInputStream) {
      short l = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);

      for(int i = 0; i < Math.min(l, this.quests.size()); ++i) {
         ((QuestInPlayer)this.quests.elementAt(i)).loadWays(dataInputStream);
      }

   }

   public final void loadFromStreamJustQuests(DataInputStream dataInputStream, boolean mainThread) {
      this.quests.removeAllElements();
      int shortCodeIndex = 0;

      for(short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream); shortCodeIndex < shortCode; ++shortCodeIndex) {
         this.quests.addElement(new QuestInPlayer(dataInputStream));
      }

      this.loadCompassedQuest(dataInputStream);
      Com.onQuestsChanged(mainThread);
   }

   public final void loadFromStreamJustDone(DataInputStream dataInputStream, boolean mainThread) {
      this.questsDone.clear();
      int shortCodeIndex = 0;

      for(short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream); shortCodeIndex < shortCode; ++shortCodeIndex) {
         this.questsDone.put(decompiledsupport.UncheckedDataInput.readUTF(dataInputStream), new QuestSingleIdSmall(dataInputStream));
      }

      Com.onQuestsChanged(true);
   }

   public final QuestInPlayer findGetnQuest(QuestSingleIdSmall questSingleIdSmall) {
      for(int i = 0; i < this.quests.size(); ++i) {
         QuestInPlayer questInPlayer;
         if ((questInPlayer = (QuestInPlayer)this.quests.elementAt(i)).questSingle.equalsById((QuestSingleIdSmall)questSingleIdSmall)) {
            return questInPlayer;
         }
      }

      return null;
   }

   public final QuestInPlayer findGetnQuestByName(String name) {
      for(int i = 0; i < this.quests.size(); ++i) {
         QuestInPlayer questInPlayer;
         if ((questInPlayer = (QuestInPlayer)this.quests.elementAt(i)).questSingle.name.equals(name)) {
            return questInPlayer;
         }
      }

      return null;
   }

   public final void tryAddKilledPlayer(int killedPlayerId) {
      for(int i = 0; i < this.quests.size(); ++i) {
         QuestInPlayer questInPlayer;
         if ((questInPlayer = (QuestInPlayer)this.quests.elementAt(i)).questSingle.isNeedToKillPlayer && !questInPlayer.isTargetPlayerKilled && questInPlayer.needToKillPlayer.needToKillPlayerId == killedPlayerId) {
            questInPlayer.isTargetPlayerKilled = true;
            Com.onQuestsChanged(true);
         }
      }

   }

   public final void removeHuntingQuestsForTarget(int targetPlayerId) {
      for(int questsCount = this.quests.size() - 1; questsCount >= 0; --questsCount) {
         QuestInPlayer questInPlayer;
         if ((questInPlayer = (QuestInPlayer)this.quests.elementAt(questsCount)).questSingle.isNeedToKillPlayer && !questInPlayer.isTargetPlayerKilled && questInPlayer.needToKillPlayer.needToKillPlayerId == targetPlayerId) {
            removeQuest(questsCount, true);
         }
      }

   }

   public static QuestInPlayer removeQuest(int ind, boolean mainThread) {
      QuestInPlayer questInPlayer = (QuestInPlayer)instance.quests.elementAt(ind);
      instance.quests.removeElementAt(ind);
      instance.onQuestRemove(questInPlayer);
      Com.onQuestsChanged(true);
      return questInPlayer;
   }

   public final String[] checkGetnQuestsForDone(TileMap tileMap, int qNum, QuestsOnMap questsOnMap, Strategist strategist2, boolean analyzeSpaceJustForLast) {
      Vector vector3 = new Vector();
      Vector vector4 = new Vector();
      QuestInPlayer questInPlayer = null;
      int questId = 0;
      int mAX_VALUE = Integer.MAX_VALUE;

      QuestInPlayer questInPlayer2;
      int mAX_VALUE2;
      for(int ind2 = 0; ind2 < this.quests.size(); ++ind2) {
         QuestSingle questSingle2 = (questInPlayer2 = (QuestInPlayer)this.quests.elementAt(ind2)).questSingle;
         if (questInPlayer2.questSingle.backToOtherQuester.length() > 0) {
            if (questsOnMap == null || !questsOnMap.nameId.equals(questInPlayer2.questSingle.backToOtherQuester)) {
               continue;
            }
         } else if (!questInPlayer2.questSingle.inMap.equals(tileMap.nameId) || questSingle2 == null || qNum >= 0 && questSingle2.inMapQuesterInd != qNum || qNum < 0 && !questSingle2.onMapDone) {
            continue;
         }

         if (questSingle2.isDone(strategist2, questInPlayer2)) {
            if (questInPlayer2.lastTimeTryToFinishWithError >= 0L && Mystery.currentTimeMillis - questInPlayer2.lastTimeTryToFinishWithError < 10000L) {
               System.out.println("Skip quest finish becouse was error");
            } else if ((mAX_VALUE2 = questSingle2.getRewItemsWeightCount() - (strategist2.getMaxInventorySize() + Math.max(questSingle2.rewBagSize, 0) - strategist2.getItemsWeight())) > 0) {
               if (!analyzeSpaceJustForLast || ind2 == this.quests.size()) {
                  vector4.addElement(questInPlayer2);
                  if (mAX_VALUE2 < mAX_VALUE) {
                     mAX_VALUE = mAX_VALUE2;
                  }
               }
            } else {
               if (Strategist.instance.vipStatus < 0 || questSingle2.rewVIPSecs <= 0L || Strategist.instance.vipStatus == questSingle2.rewVIPType) {
                  questInPlayer = questInPlayer2;
                  questId = ind2;
                  break;
               }

               vector3.addElement(questInPlayer2);
            }
         }
      }

      String messageText;
      if (questInPlayer != null) {
         Com.PrBarTh.Set(true);
         NetRequest netRequest = new NetRequest();
         String messageText2 = "SUCCESS";
         if (questInPlayer.questSingle.showRewardWindow) {
            if (questInPlayer.questSingle.showInFloat) {
               messageText2 = "SUCCESS_FLOAT";
            }
         } else {
            messageText2 = "SUCCESS_SILENT";
         }

         try {
            netRequest.writeHeader(4141020);
            questInPlayer.questSingle.saveToStreamId((DataOutputStream)netRequest.request);
            Com.sendRequest(netRequest);
            if ((mAX_VALUE2 = netRequest.response.readInt()) == 4141021) {
               messageText = netRequest.response.readUTF();
               Com.applyRewardEvents(netRequest);
               this.questsDone.put(questInPlayer.questSingle.createStringIdSmall(), new QuestSingleIdSmall(questInPlayer.questSingle.inMap, questInPlayer.questSingle.inMapQuesterInd, questInPlayer.questSingle.inQuesterInd));
               removeQuest(questId, true);
            } else if (mAX_VALUE2 == 4140004) {
               GameMapObjectsController.brokeMapObjectsOrder();
               messageText = "Ошибка при попытке сдать квест";
               messageText2 = "SUCCESS_ERROR";
            } else {
               messageText = "Ошибка при попытке сдать квест";
               messageText2 = "SUCCESS_ERROR";
            }
         } catch (Exception exception) {
            messageText = "Ошибка при попытке сдать квест";
            messageText2 = "SUCCESS_ERROR";
         }

         Com.PrBarTh.Stop();
         if (messageText2 == "SUCCESS_ERROR") {
            questInPlayer.lastTimeTryToFinishWithError = Mystery.currentTimeMillis;
         }

         QuestSingle questSingle3;
         return new String[]{messageText2, messageText, (questSingle3 = questInPlayer.questSingle).rewTeleportToMap != null && questSingle3.rewTeleportToMap.length() > 0 ? "0" : "1"};
      } else {
         String messageText3 = null;
         int sizeIndex;
         QuestInPlayer questInPlayer3;
         if (vector4.size() > 0) {
            if (vector4.size() == 1) {
               questInPlayer2 = (QuestInPlayer)vector4.elementAt(0);
               messageText3 = appendMessage((String)null, Mystery.stringFormat("%\n\nРюкзак полон, нужно еще % мест.\nРасширение рюкзака через МЕНЮ-БОНУСЫ-Вместимость\n+1 место за %ɾɿ", new String[]{questInPlayer2.questSingle.name, String.valueOf(mAX_VALUE), String.valueOf(Com.c_inventory_cost)}));
            } else {
               messageText = "";

               for(sizeIndex = 0; sizeIndex < vector4.size(); ++sizeIndex) {
                  questInPlayer3 = (QuestInPlayer)vector4.elementAt(sizeIndex);
                  messageText = messageText + "\n" + questInPlayer3.questSingle.name;
               }

               messageText = messageText.substring(1);
               messageText3 = appendMessage((String)null, Mystery.stringFormat("%\n\nРюкзак полон, нужно еще % мест.\nРасширение рюкзака через МЕНЮ-БОНУСЫ-Вместимость\n+1 место за %ɾɿ", new String[]{messageText, String.valueOf(mAX_VALUE), String.valueOf(Com.c_inventory_cost)}));
            }
         }

         if (vector3.size() > 0) {
            if (vector3.size() == 1) {
               questInPlayer2 = (QuestInPlayer)vector3.elementAt(0);
               messageText3 = appendMessage(messageText3, Mystery.stringFormat("%\n\nПремиум не совпадает, активен х%, награда х%", new String[]{questInPlayer2.questSingle.name, Com.c_vip_exp_coof[Math.max(0, Strategist.instance.vipStatus)], Com.c_vip_exp_coof[Math.max(0, questInPlayer2.questSingle.rewVIPType)]}));
            } else {
               messageText = "";

               for(sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
                  questInPlayer3 = (QuestInPlayer)vector3.elementAt(sizeIndex);
                  messageText = messageText + "\n" + questInPlayer3.questSingle.name;
               }

               messageText = messageText.substring(1);
               messageText3 = appendMessage(messageText3, Mystery.stringFormat("%\n\nПремиум не совпадает, активен х%", new String[]{messageText, Com.c_vip_exp_coof[Math.max(0, Strategist.instance.vipStatus)]}));
            }
         }

         return messageText3 != null ? new String[]{"CANT_GIVE_REWARD", messageText3} : new String[]{"NOTHING", ""};
      }
   }

   private static String appendMessage(String messageText, String messageText2) {
      return (messageText != null ? messageText + "\n\n" : "") + messageText2;
   }

   public final void needCalculateWays() {
      this.needCalculateWays = true;
   }

   public final void tryCalculateWays(TileMap tileMap, EnemysMap enemysMap, Strategist strategist2) {
      if (this.needCalculateWays) {
         this.needCalculateWays = false;
         this.calculateWays(tileMap, enemysMap, strategist2);
      }
   }

   public final void calculateWays(TileMap tileMap, EnemysMap enemysMap, Strategist strategist2) {
      int questsIndex = 0;

      for(int questsCount = this.quests.size(); questsIndex < questsCount; ++questsIndex) {
         ((QuestInPlayer)this.quests.elementAt(questsIndex)).calculateWays(tileMap, enemysMap, strategist2);
      }

      this.calculateCompassedQuestInfoTexts(strategist2);
   }

   private void onCompassedQuestChanged(Strategist strategist2) {
      this.calculateCompassedQuestInfoTexts(strategist2);
   }

   public final int getCompassedQuestTimerInfo() {
      QuestInPlayer questInPlayer;
      if ((questInPlayer = this.compassedQuest) != null && questInPlayer.questSingle.time > 0) {
         long delta;
         return (delta = Math.max(0L, questInPlayer.timeLeft - (Mystery.currentTimeMillis - questInPlayer.timeOnLoad)) / 1000L) <= 999L ? (int)delta : -1;
      } else {
         return -1;
      }
   }

   private void calculateCompassedQuestInfoTexts(Strategist strategist2) {
      QuestInPlayer questInPlayer;
      if ((questInPlayer = this.compassedQuest) == null) {
         this.compassQuestInfoName = null;
         this.compassQuestInfoText = null;
         this.compassQuestInfoTextsWidth = null;
      } else {
         this.compassQuestInfoName = questInPlayer.questSingle.name;
         if (this.compassQuestInfoName.length() > 16) {
            this.compassQuestInfoName = this.compassQuestInfoName.substring(0, 16);
         }

         this.compassQuestInfoNameWidth = Com.fontSmall.getTextWidth(this.compassQuestInfoName, StringResources.FONT_ACCORDINGS);
         this.compassQuestInfoMaxWidth = Math.max(4, this.compassQuestInfoNameWidth);
         Vector vector3 = questInPlayer.questSingle.getPreviewQuestTexts(strategist2, questInPlayer);
         this.compassQuestInfoText = new String[vector3.size()];
         this.compassQuestInfoTextsWidth = new int[vector3.size()];

         for(int i = 0; i < this.compassQuestInfoText.length; ++i) {
            this.compassQuestInfoText[i] = (String)vector3.elementAt(i);
            this.compassQuestInfoTextsWidth[i] = Com.fontSmall.getTextWidth(this.compassQuestInfoText[i], StringResources.FONT_ACCORDINGS);
            this.compassQuestInfoMaxWidth = Math.max(this.compassQuestInfoMaxWidth, this.compassQuestInfoTextsWidth[i]);
         }

      }
   }

   public static boolean hasTrackedQuestInfo() {
      return instance.compassQuestInfoName != null;
   }
}
