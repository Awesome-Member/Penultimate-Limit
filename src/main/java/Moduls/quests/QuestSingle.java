package Moduls.quests;

import Base.Com;
import Engine.MathGame;
import Engine.Mystery;
import Moduls.StrategGraphic;
import Moduls.Strategist;
import Moduls.ItemSimple;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class QuestSingle extends QuestSingleIdSmall {
   private int reqMinFireLevel = 0;
   private int reqMaxFireLevel = 99999;
   private int reqMinIceLevel = 0;
   private int reqMaxIceLevel = 99999;
   private int reqMinHeroLevel = 0;
   private int reqMaxHeroLevel = 99999;
   public int reqMinLevelLevel = 0;
   private int reqMaxLevelLevel = 99999;
   private int[] reqStrategs = new int[0];
   private int[][] reqSkill = new int[0][2];
   public String name = "";
   private String description = "";
   public int time = -1;
   private boolean manyTimes = false;
   public QuestSupStartDropItem[] startDropItems = new QuestSupStartDropItem[0];
   public boolean onMapDone = false;
   private ItemSimple[] giveItemsOnAccept = new ItemSimple[0];
   public boolean isNeedToKillPlayer = false;
   private int needKillPlayerMaxDifInLevels = 2;
   public QuestSupMonsterTarget[] needMonsters = new QuestSupMonsterTarget[0];
   public int needMoney = 0;
   public int needCrys = 0;
   private boolean needItemsCostOrPass = true;
   public QuestSupItemNeed[] needItems = new QuestSupItemNeed[0];
   public QuestSupTextIdsNeed[] needDecorators = new QuestSupTextIdsNeed[0];
   public QuestSupTextIdsNeed[] needQuesters = new QuestSupTextIdsNeed[0];
   private int rewCrys = 0;
   private int rewMinMoney = 0;
   private int rewMinExp = 0;
   private int rewMaxMoney = 0;
   private int rewMaxExp = 0;
   private QuestSupItemRew[] rewItems = new QuestSupItemRew[0];
   private int rewSkillEffectInd = -1;
   private boolean rewResetSkills = false;
   private long rewProtectionSecs = 0L;
   public int rewVIPType = 0;
   public long rewVIPSecs = 0L;
   public int rewBagSize = 0;
   public QuestSupTextIdsNeed[] needChests = new QuestSupTextIdsNeed[0];
   public ItemSimple[] takeItemsOnAccept = new ItemSimple[0];
   private String rewTeleportToMapLiteral = "";
   public String rewTeleportToMap = "";
   private int rewTeleportToX = -1;
   private int rewTeleportToY = -1;
   private int rewGiveAuraInd = -1;
   private boolean rewRemoveAura = false;
   public String backToOtherQuester = "";
   private String requerAnotherQuestName = "";
   public boolean switchCompass = true;
   private boolean showOnlyIfFinishable = false;
   public boolean takeOnlyIfFinishable = false;
   private boolean showRewardWord = true;
   public boolean showYesNo = true;
   public boolean showInFloat = false;
   private boolean showCountInDescription = false;
   public boolean showRewardWindow = true;
   public String overrideTextOnYesNo = "";
   public short[] questerMapWorldCoord = null;

   public QuestSingle(DataInputStream dataInputStream) {
      this.loadFromStream(dataInputStream);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      super.loadFromStream(dataInputStream);
      this.reqMinFireLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMaxFireLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMinIceLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMaxIceLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMinHeroLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMaxHeroLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMinLevelLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqMaxLevelLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.reqStrategs = new int[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      int i;
      for(i = 0; i < this.reqStrategs.length; ++i) {
         this.reqStrategs[i] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      }

      this.reqSkill = new int[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)][];

      for(i = 0; i < this.reqSkill.length; ++i) {
         this.reqSkill[i] = new int[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
      }

      this.name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.description = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.time = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.manyTimes = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.startDropItems = new QuestSupStartDropItem[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.startDropItems.length; ++i) {
         this.startDropItems[i] = new QuestSupStartDropItem(dataInputStream);
      }

      this.onMapDone = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.giveItemsOnAccept = new ItemSimple[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.giveItemsOnAccept.length; ++i) {
         this.giveItemsOnAccept[i] = new ItemSimple(dataInputStream);
      }

      this.isNeedToKillPlayer = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.needKillPlayerMaxDifInLevels = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.needMonsters = new QuestSupMonsterTarget[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.needMonsters.length; ++i) {
         this.needMonsters[i] = new QuestSupMonsterTarget(dataInputStream);
      }

      this.needMoney = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.needCrys = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.needItemsCostOrPass = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.needItems = new QuestSupItemNeed[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.needItems.length; ++i) {
         this.needItems[i] = new QuestSupItemNeed(dataInputStream);
      }

      this.needDecorators = new QuestSupTextIdsNeed[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.needDecorators.length; ++i) {
         this.needDecorators[i] = new QuestSupTextIdsNeed(dataInputStream);
      }

      this.needQuesters = new QuestSupTextIdsNeed[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.needQuesters.length; ++i) {
         this.needQuesters[i] = new QuestSupTextIdsNeed(dataInputStream);
      }

      this.rewCrys = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rewMinMoney = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rewMinExp = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rewMaxMoney = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rewMaxExp = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rewItems = new QuestSupItemRew[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.rewItems.length; ++i) {
         this.rewItems[i] = new QuestSupItemRew(dataInputStream);
      }

      this.rewSkillEffectInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.rewResetSkills = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.rewProtectionSecs = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.rewVIPType = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.rewVIPSecs = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.rewBagSize = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.needChests = new QuestSupTextIdsNeed[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.needChests.length; ++i) {
         this.needChests[i] = new QuestSupTextIdsNeed(dataInputStream);
      }

      this.takeItemsOnAccept = new ItemSimple[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(i = 0; i < this.takeItemsOnAccept.length; ++i) {
         this.takeItemsOnAccept[i] = new ItemSimple(dataInputStream);
      }

      this.rewTeleportToMap = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.rewTeleportToMapLiteral = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.rewTeleportToX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.rewTeleportToY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.rewGiveAuraInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.rewRemoveAura = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.backToOtherQuester = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.questerMapWorldCoord = new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
      } else {
         this.questerMapWorldCoord = null;
      }

      this.requerAnotherQuestName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.overrideTextOnYesNo = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.showRewardWindow = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.showRewardWord = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.showYesNo = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.showInFloat = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.showCountInDescription = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.takeOnlyIfFinishable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.switchCompass = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.showOnlyIfFinishable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
   }

   public final boolean isAvailableByDone(QuestStrateg questStrateg) {
      return this.manyTimes || (QuestSingleIdSmall)questStrateg.questsDone.get(this.createStringIdSmall()) == null;
   }

   public final boolean isAvailable(Strategist strategist2, QuestStrateg questStrateg) {
      return this.isAvailableByDone(questStrateg) && (strategist2.GP_Level <= this.reqMaxLevelLevel && strategist2.GP_Level >= this.reqMinLevelLevel ? this.isCanTakeCommonPart(strategist2, questStrateg) : false) && questStrateg.findGetnQuest((QuestSingleIdSmall)this) == null;
   }

   public final boolean isCanTakeAver(Strategist strategist2, QuestStrateg questStrateg) {
      return strategist2.GP_Level > this.reqMaxLevelLevel ? false : this.isCanTakeCommonPart(strategist2, questStrateg);
   }

   private boolean isCanTakeCommonPart(Strategist strategist2, QuestStrateg questStrateg) {
      boolean res = true;
      if (strategist2.GP_FireLevel <= this.reqMaxFireLevel && strategist2.GP_FireLevel >= this.reqMinFireLevel) {
         if (strategist2.GP_IceLevel <= this.reqMaxIceLevel && strategist2.GP_IceLevel >= this.reqMinIceLevel) {
            if (strategist2.GP_HeroLevel <= this.reqMaxHeroLevel && strategist2.GP_HeroLevel >= this.reqMinHeroLevel) {
               int reqStrategIndex;
               if (this.reqStrategs.length > 0) {
                  res = false;

                  for(reqStrategIndex = 0; reqStrategIndex < this.reqStrategs.length; ++reqStrategIndex) {
                     int reqStrategs2 = this.reqStrategs[reqStrategIndex];
                     if (strategist2.GP_Race == reqStrategs2) {
                        res = true;
                     }
                  }
               }

               if (!res) {
                  return false;
               } else {
                  if (this.reqSkill.length > 0) {
                     res = false;

                     for(reqStrategIndex = 0; reqStrategIndex < this.reqSkill.length; ++reqStrategIndex) {
                        int[] aReqSkill = this.reqSkill[reqStrategIndex];
                        if (strategist2.GP_TotalSkillsLevels[aReqSkill[0]] >= aReqSkill[1]) {
                           res = true;
                        }
                     }
                  }

                  if (!res) {
                     return false;
                  } else if (this.requerAnotherQuestName != null && this.requerAnotherQuestName.length() > 0 && questStrateg.findGetnQuestByName(this.requerAnotherQuestName) == null) {
                     return false;
                  } else {
                     return !this.showOnlyIfFinishable || this.isDone(strategist2, (QuestInPlayer)null);
                  }
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean isFreeForDone() {
      return this.giveItemsOnAccept.length == 0 && !this.isNeedToKillPlayer && this.needMonsters.length == 0 && this.needMoney <= 0 && this.needCrys <= 0 && (!this.needItemsCostOrPass || this.needItems.length == 0) && this.needDecorators.length == 0 && this.needQuesters.length == 0;
   }

   public final int isAvailableByReward(Strategist strategist2) {
      if (this.getRewItemsWeightCount() - (strategist2.getMaxInventorySize() + Math.max(this.rewBagSize, 0) - strategist2.getItemsWeight()) > 0) {
         return 0;
      } else {
         return Strategist.instance.vipStatus >= 0 && this.rewVIPSecs > 0L && Strategist.instance.vipStatus != this.rewVIPType ? 1 : -1;
      }
   }

   public final QuestSupItemNeed isNeedJustSelleableItem(Strategist strategist2, QuestInPlayer questInPlayer) {
      QuestSupItemNeed questSupItemNeed = strategist2.isEnoughtItemsJustFromMarket(this.needItems, true);
      return strategist2.GP_Money >= this.needMoney && strategist2.GP_Crystals >= this.needCrys && (!this.isNeedToKillPlayer || questInPlayer != null && questInPlayer.isTargetPlayerKilled) && questSupItemNeed != null && this.isAllMonstersKilled(questInPlayer) && isAllObjectsUsed(this.needDecorators, questInPlayer == null ? null : questInPlayer.usedDecorators) && isAllObjectsUsed(this.needChests, questInPlayer == null ? null : questInPlayer.usedChests) && isAllObjectsUsed(this.needQuesters, questInPlayer == null ? null : questInPlayer.usedQuesters) ? questSupItemNeed : null;
   }

   public final boolean isNeedJustGold(Strategist strategist2, QuestInPlayer questInPlayer) {
      return strategist2.GP_Money < this.needMoney && strategist2.GP_Crystals >= this.needCrys && (!this.isNeedToKillPlayer || questInPlayer != null && questInPlayer.isTargetPlayerKilled) && strategist2.isEnoughtItems((ItemSimple[])this.needItems, true) && this.isAllMonstersKilled(questInPlayer) && isAllObjectsUsed(this.needDecorators, questInPlayer == null ? null : questInPlayer.usedDecorators) && isAllObjectsUsed(this.needChests, questInPlayer == null ? null : questInPlayer.usedChests) && isAllObjectsUsed(this.needQuesters, questInPlayer == null ? null : questInPlayer.usedQuesters);
   }

   public final boolean isNeedJustCrystals(Strategist strategist2, QuestInPlayer questInPlayer) {
      return strategist2.GP_Money >= this.needMoney && strategist2.GP_Crystals < this.needCrys && (!this.isNeedToKillPlayer || questInPlayer != null && questInPlayer.isTargetPlayerKilled) && strategist2.isEnoughtItems((ItemSimple[])this.needItems, true) && this.isAllMonstersKilled(questInPlayer) && isAllObjectsUsed(this.needDecorators, questInPlayer == null ? null : questInPlayer.usedDecorators) && isAllObjectsUsed(this.needChests, questInPlayer == null ? null : questInPlayer.usedChests) && isAllObjectsUsed(this.needQuesters, questInPlayer == null ? null : questInPlayer.usedQuesters);
   }

   public final boolean isDone(Strategist strategist2, QuestInPlayer questInPlayer) {
      return strategist2.GP_Money >= this.needMoney && strategist2.GP_Crystals >= this.needCrys && (!this.isNeedToKillPlayer || questInPlayer != null && questInPlayer.isTargetPlayerKilled) && strategist2.isEnoughtItems((ItemSimple[])this.needItems, true) && this.isAllMonstersKilled(questInPlayer) && isAllObjectsUsed(this.needDecorators, questInPlayer == null ? null : questInPlayer.usedDecorators) && isAllObjectsUsed(this.needChests, questInPlayer == null ? null : questInPlayer.usedChests) && isAllObjectsUsed(this.needQuesters, questInPlayer == null ? null : questInPlayer.usedQuesters);
   }

   private boolean isAllMonstersKilled(QuestInPlayer questInPlayer) {
      for(int needMonsterIndex = 0; needMonsterIndex < this.needMonsters.length; ++needMonsterIndex) {
         QuestSupMonsterTarget questSupMonsterTarget = this.needMonsters[needMonsterIndex];
         int killedCount = 0;
         Integer integer = questInPlayer == null ? null : (Integer)questInPlayer.killedMonsters.get(questSupMonsterTarget.monsterName);
         Integer integer2 = integer;
         if (integer != null) {
            killedCount = 0 + integer2;
         }

         if (killedCount < questSupMonsterTarget.count) {
            return false;
         }
      }

      return true;
   }

   private static boolean isAllObjectsUsed(QuestSupTextIdsNeed[] needObjects, Hashtable hashtable) {
      for(int needObjectIndex = 0; needObjectIndex < needObjects.length; ++needObjectIndex) {
         QuestSupTextIdsNeed questSupTextIdsNeed = needObjects[needObjectIndex];
         int usedCount = 0;
         Integer integer = hashtable == null ? null : (Integer)hashtable.get(questSupTextIdsNeed.name);
         Integer integer2 = integer;
         if (integer != null) {
            usedCount = 0 + integer2;
         }

         if (usedCount < questSupTextIdsNeed.count && questSupTextIdsNeed.count != -1) {
            return false;
         }
      }

      return true;
   }

   public final Vector getPreviewQuestTexts(Strategist strategist2, QuestInPlayer questInPlayer) {
      Vector vector3 = new Vector();
      if (this.isNeedToKillPlayer) {
         if (questInPlayer.isTargetPlayerKilled) {
            vector3.addElement(Mystery.stringFormat("Убит %", questInPlayer.needToKillPlayer.needToKillPlayerName));
         } else if (questInPlayer.needToKillPlayer.needToKillPlayerId >= 0) {
            vector3.addElement(Mystery.stringFormat("Убить %", questInPlayer.needToKillPlayer.needToKillPlayerName));
         } else {
            vector3.addElement("Поиск цели");
         }
      }

      String messageText;
      int needMonsterIndex;
      int fullCountForNameMapCountMap;
      if (this.needMonsters.length > 0) {
         messageText = "";

         for(needMonsterIndex = 0; needMonsterIndex < this.needMonsters.length; ++needMonsterIndex) {
            QuestSupMonsterTarget questSupMonsterTarget;
            fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap((questSupMonsterTarget = this.needMonsters[needMonsterIndex]).monsterName, questInPlayer.killedMonsters);
            messageText = messageText + Math.min(fullCountForNameMapCountMap, questSupMonsterTarget.count) + "/" + questSupMonsterTarget.count + " ";
         }

         if (messageText.length() > 0) {
            messageText = "ͦ" + messageText.substring(0, messageText.length() - 1);
         }

         vector3.addElement(messageText);
      }

      QuestSupTextIdsNeed questSupTextIdsNeed;
      if (this.needDecorators.length > 0) {
         messageText = "";

         for(needMonsterIndex = 0; needMonsterIndex < this.needDecorators.length; ++needMonsterIndex) {
            if ((questSupTextIdsNeed = this.needDecorators[needMonsterIndex]).count != -1) {
               fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedDecorators);
               messageText = messageText + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + " ";
            }
         }

         if (messageText.length() > 0) {
            messageText = "ͧ" + messageText.substring(0, messageText.length() - 1);
         }

         if (messageText.length() > 0) {
            vector3.addElement(messageText);
         }
      }

      if (this.needChests.length > 0) {
         messageText = "";

         for(needMonsterIndex = 0; needMonsterIndex < this.needChests.length; ++needMonsterIndex) {
            if ((questSupTextIdsNeed = this.needChests[needMonsterIndex]).count != -1) {
               fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedChests);
               messageText = messageText + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + " ";
            }
         }

         if (messageText.length() > 0) {
            messageText = "ʜʝ" + messageText.substring(0, messageText.length() - 1);
         }

         if (messageText.length() > 0) {
            vector3.addElement(messageText);
         }
      }

      if (this.needQuesters.length > 0) {
         messageText = "";

         for(needMonsterIndex = 0; needMonsterIndex < this.needQuesters.length; ++needMonsterIndex) {
            if ((questSupTextIdsNeed = this.needQuesters[needMonsterIndex]).count != -1) {
               fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedQuesters);
               messageText = messageText + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + " ";
            }
         }

         if (messageText.length() > 0) {
            messageText = "ͩ" + messageText.substring(0, messageText.length() - 1);
         }

         if (messageText.length() > 0) {
            vector3.addElement(messageText);
         }
      }

      try {
         ItemSimple[] dd = strategist2.isEnoughtItemsCount(this.needItems, true);
         String messageText2 = "";
         Hashtable hashtable = new Hashtable();

         int maximum;
         int ddIndex;
         QuestSupStartDropItem questSupStartDropItem;
         for(fullCountForNameMapCountMap = 0; fullCountForNameMapCountMap < this.needItems.length; ++fullCountForNameMapCountMap) {
            QuestSupItemNeed questSupItemNeed;
            maximum = (questSupItemNeed = this.needItems[fullCountForNameMapCountMap]).count;

            for(ddIndex = 0; ddIndex < dd.length; ++ddIndex) {
               ItemSimple itemSimple = dd[ddIndex];
               if (questSupItemNeed.typeId == itemSimple.typeId) {
                  maximum = Math.max(0, maximum - itemSimple.count);
               }
            }

            messageText2 = messageText2 + maximum + "/" + questSupItemNeed.count + " ";

            for(ddIndex = 0; ddIndex < this.startDropItems.length; ++ddIndex) {
               if ((questSupStartDropItem = this.startDropItems[ddIndex]).typeId == questSupItemNeed.typeId) {
                  hashtable.put(questSupStartDropItem, new Object());
               }
            }
         }

         for(fullCountForNameMapCountMap = 0; fullCountForNameMapCountMap < this.startDropItems.length; ++fullCountForNameMapCountMap) {
            QuestSupStartDropItem questSupStartDropItem2 = this.startDropItems[fullCountForNameMapCountMap];
            if (hashtable.get(questSupStartDropItem2) == null) {
               maximum = questSupStartDropItem2.count;

               for(ddIndex = fullCountForNameMapCountMap + 1; ddIndex < this.startDropItems.length; ++ddIndex) {
                  questSupStartDropItem = this.startDropItems[ddIndex];
                  if (hashtable.get(questSupStartDropItem) == null && questSupStartDropItem.typeId == questSupStartDropItem2.typeId) {
                     maximum = Math.max(maximum, questSupStartDropItem.count);
                  }
               }

               if (maximum > 0) {
                  messageText2 = messageText2 + strategist2.getItemsTotalCount(questSupStartDropItem2.typeId) + "/" + maximum + " ";
               } else {
                  messageText2 = messageText2 + strategist2.getItemsTotalCount(questSupStartDropItem2.typeId) + " ";
               }
            }
         }

         if (messageText2.length() > 0) {
            messageText2 = "ͨ" + messageText2.substring(0, messageText2.length() - 1);
            vector3.addElement(messageText2);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      messageText = "";
      if (this.needMoney > 0) {
         messageText = messageText + (messageText.length() > 0 ? " и " : "") + "ɼɽ" + Math.min(strategist2.GP_Money, this.needMoney) + "/" + this.needMoney;
      }

      if (this.needCrys > 0) {
         messageText = messageText + (messageText.length() > 0 ? " и " : "") + "ɾɿ" + Math.min(strategist2.GP_Crystals, this.needCrys) + "/" + this.needCrys;
      }

      if (messageText.length() > 0) {
         vector3.addElement(messageText);
      }

      return vector3;
   }

   public final String getDescription(Strategist strategist2, QuestInPlayer questInPlayer) {
      String messageText = "";
      if (questInPlayer != null && this.isDone(strategist2, questInPlayer)) {
         messageText = messageText + "Квест завершен, вернись за наградой!\n\n";
      }

      messageText = messageText + this.description + "\n\n";
      String substring;
      int takeItemsOnAcceptIndex;
      ItemSimple itemSimple;
      if (questInPlayer == null && this.takeItemsOnAccept.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.takeItemsOnAccept.length; ++takeItemsOnAcceptIndex) {
            itemSimple = this.takeItemsOnAccept[takeItemsOnAcceptIndex];
            substring = substring + (substring.length() > 0 ? ", " : "") + this.getCountTextForDescription(strategist2, itemSimple, itemSimple.count) + "͜" + itemSimple.nameVis;
         }

         if (substring.length() > 0) {
            messageText = messageText + Mystery.stringFormat("Отдать %\n", substring);
         }
      }

      if (questInPlayer == null && this.giveItemsOnAccept.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.giveItemsOnAccept.length; ++takeItemsOnAcceptIndex) {
            itemSimple = this.giveItemsOnAccept[takeItemsOnAcceptIndex];
            substring = substring + (substring.length() > 0 ? ", " : "") + itemSimple.count + "͜" + itemSimple.nameVis;
         }

         if (substring.length() > 0) {
            messageText = messageText + Mystery.stringFormat("Получить %\n", substring);
         }
      }

      if (this.time >= 0) {
         if (questInPlayer == null) {
            messageText = messageText + Mystery.stringFormat("за % минут\n", String.valueOf(this.time / '\uea60'));
         } else {
            messageText = messageText + Mystery.stringFormat("Осталось % минут\n", String.valueOf((questInPlayer.timeLeft - (Mystery.currentTimeMillis - questInPlayer.timeOnLoad)) / 60000L));
         }
      }

      if (this.isNeedToKillPlayer) {
         if (questInPlayer == null) {
            messageText = messageText + Mystery.stringFormat("Победить героя %ə% уровня\n", new String[]{String.valueOf(Math.max(1, strategist2.GP_Level - this.needKillPlayerMaxDifInLevels)), String.valueOf(strategist2.GP_Level + this.needKillPlayerMaxDifInLevels)});
         } else if (questInPlayer.isTargetPlayerKilled) {
            messageText = messageText + Mystery.stringFormat("% побежден\n", questInPlayer.needToKillPlayer.needToKillPlayerName);
         } else if (questInPlayer.needToKillPlayer.needToKillPlayerId >= 0) {
            messageText = messageText + Mystery.stringFormat("Победить %\n", "(" + questInPlayer.needToKillPlayer.needToKillPlayerLevel + ")" + questInPlayer.needToKillPlayer.needToKillPlayerName);
            if (!questInPlayer.needToKillPlayer.needToKillPlayerOnline) {
               messageText = messageText + "Сейчас вне игры, скоро будет найдена новая цель\n";
            }
         } else {
            messageText = messageText + "Поиск противника...\n";
         }
      }

      int fullCountForNameMapCountMap;
      if (this.needMonsters.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.needMonsters.length; ++takeItemsOnAcceptIndex) {
            QuestSupMonsterTarget questSupMonsterTarget = this.needMonsters[takeItemsOnAcceptIndex];
            if (questInPlayer == null) {
               substring = substring + questSupMonsterTarget.count + "͜" + questSupMonsterTarget.monsterVis + ", ";
            } else {
               fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupMonsterTarget.monsterName, questInPlayer.killedMonsters);
               substring = substring + Math.min(fullCountForNameMapCountMap, questSupMonsterTarget.count) + "/" + questSupMonsterTarget.count + "͜" + questSupMonsterTarget.monsterVis + ", ";
            }
         }

         if (substring.length() >= 2) {
            substring = substring.substring(0, substring.length() - 2);
         }

         messageText = messageText + Mystery.stringFormat("Убить %\n", substring);
      }

      if (this.needMonsters.length > 0 || this.startDropItems.length > 0) {
         messageText = messageText + "ကခ";
      }

      QuestSupTextIdsNeed questSupTextIdsNeed;
      if (this.needDecorators.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.needDecorators.length; ++takeItemsOnAcceptIndex) {
            if ((questSupTextIdsNeed = this.needDecorators[takeItemsOnAcceptIndex]).count != -1) {
               if (questInPlayer == null) {
                  substring = substring + (questSupTextIdsNeed.count == 1 ? "" : questSupTextIdsNeed.count + "͜") + questSupTextIdsNeed.name + ", ";
               } else {
                  fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedDecorators);
                  if (questSupTextIdsNeed.count == 1) {
                     substring = substring + (Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) == 1 ? "ʒʓ" : "") + questSupTextIdsNeed.name + ", ";
                  } else {
                     substring = substring + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + "͜" + questSupTextIdsNeed.name + ", ";
                  }
               }
            }
         }

         if (substring.length() >= 2) {
            substring = substring.substring(0, substring.length() - 2);
         }

         if (substring.length() > 0) {
            messageText = messageText + Mystery.stringFormat("%\n", substring);
         }
      }

      if (this.needChests.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.needChests.length; ++takeItemsOnAcceptIndex) {
            if ((questSupTextIdsNeed = this.needChests[takeItemsOnAcceptIndex]).count != -1) {
               if (questInPlayer == null) {
                  substring = substring + (questSupTextIdsNeed.count == 1 ? "" : questSupTextIdsNeed.count + "͜") + questSupTextIdsNeed.name + ", ";
               } else {
                  fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedChests);
                  if (questSupTextIdsNeed.count == 1) {
                     substring = substring + (Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) == 1 ? "ʒʓ" : "") + questSupTextIdsNeed.name + ", ";
                  } else {
                     substring = substring + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + "͜" + questSupTextIdsNeed.name + ", ";
                  }
               }
            }
         }

         if (substring.length() >= 2) {
            substring = substring.substring(0, substring.length() - 2);
         }

         if (substring.length() > 0) {
            messageText = messageText + Mystery.stringFormat("%\n", substring);
         }
      }

      if (this.needQuesters.length > 0) {
         substring = "";

         for(takeItemsOnAcceptIndex = 0; takeItemsOnAcceptIndex < this.needQuesters.length; ++takeItemsOnAcceptIndex) {
            if ((questSupTextIdsNeed = this.needQuesters[takeItemsOnAcceptIndex]).count != -1) {
               if (questInPlayer == null) {
                  substring = substring + (questSupTextIdsNeed.count == 1 ? "" : questSupTextIdsNeed.count + "͜") + questSupTextIdsNeed.name + ", ";
               } else {
                  fullCountForNameMapCountMap = QuestInPlayer.getFullCountForNameMapCountMap(questSupTextIdsNeed.name, questInPlayer.usedQuesters);
                  if (questSupTextIdsNeed.count == 1) {
                     substring = substring + (Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) == 1 ? "ʒʓ" : "") + questSupTextIdsNeed.name + ", ";
                  } else {
                     substring = substring + Math.min(fullCountForNameMapCountMap, questSupTextIdsNeed.count) + "/" + questSupTextIdsNeed.count + "͜" + questSupTextIdsNeed.name + ", ";
                  }
               }
            }
         }

         if (substring.length() >= 2) {
            substring = substring.substring(0, substring.length() - 2);
         }

         if (substring.length() > 0) {
            messageText = messageText + Mystery.stringFormat("Посетить %\n", substring);
         }
      }

      int ddIndex;
      String messageText2;
      try {
         ItemSimple[] dd = strategist2.isEnoughtItemsCount(this.needItems, true);
         messageText2 = "";
         Hashtable hashtable = new Hashtable();

         int maximum;
         for(fullCountForNameMapCountMap = 0; fullCountForNameMapCountMap < this.needItems.length; ++fullCountForNameMapCountMap) {
            QuestSupItemNeed questSupItemNeed;
            maximum = (questSupItemNeed = this.needItems[fullCountForNameMapCountMap]).count;

            for(ddIndex = 0; ddIndex < dd.length; ++ddIndex) {
               ItemSimple itemSimple2 = dd[ddIndex];
               if (questSupItemNeed.typeId == itemSimple2.typeId) {
                  maximum = Math.max(0, maximum - itemSimple2.count);
               }
            }

            if (questInPlayer == null) {
               messageText2 = messageText2 + this.getCountTextForDescription(strategist2, questSupItemNeed, questSupItemNeed.count) + "͜" + questSupItemNeed.nameVis;
            } else {
               messageText2 = messageText2 + maximum + "/" + questSupItemNeed.count + "͜" + questSupItemNeed.nameVis;
            }

            boolean requireAllTargets = true;

            for(int startDropItemIndex = 0; startDropItemIndex < this.startDropItems.length; ++startDropItemIndex) {
               QuestSupStartDropItem questSupStartDropItem;
               if ((questSupStartDropItem = this.startDropItems[startDropItemIndex]).typeId == questSupItemNeed.typeId) {
                  hashtable.put(questSupStartDropItem, new Object());
                  messageText2 = messageText2 + Mystery.stringFormat(requireAllTargets ? " из %" : " или %", questSupStartDropItem.string);
                  requireAllTargets = false;
                  if (questSupStartDropItem.chance < 100000) {
                     messageText2 = messageText2 + Mystery.stringFormat("($%)", '$', new String[]{Mystery.decimalToString((long)questSupStartDropItem.chance, 3)});
                  }
               }
            }

            messageText2 = messageText2 + ", ";
         }

         for(fullCountForNameMapCountMap = 0; fullCountForNameMapCountMap < this.startDropItems.length; ++fullCountForNameMapCountMap) {
            QuestSupStartDropItem questSupStartDropItem2 = this.startDropItems[fullCountForNameMapCountMap];
            if (hashtable.get(questSupStartDropItem2) == null) {
               maximum = questSupStartDropItem2.count;

               QuestSupStartDropItem questSupStartDropItem3;
               for(ddIndex = fullCountForNameMapCountMap + 1; ddIndex < this.startDropItems.length; ++ddIndex) {
                  questSupStartDropItem3 = this.startDropItems[ddIndex];
                  if (hashtable.get(questSupStartDropItem3) == null && questSupStartDropItem3.typeId == questSupStartDropItem2.typeId) {
                     maximum = Math.max(maximum, questSupStartDropItem3.count);
                  }
               }

               if (maximum > 0) {
                  if (questInPlayer == null) {
                     messageText2 = messageText2 + this.getCountTextForDescription(strategist2, questSupStartDropItem2, maximum) + "͜" + questSupStartDropItem2.nameVis;
                  } else {
                     messageText2 = messageText2 + strategist2.getItemsTotalCount(questSupStartDropItem2.typeId) + "/" + maximum + "͜" + questSupStartDropItem2.nameVis;
                  }
               } else if (questInPlayer == null) {
                  messageText2 = messageText2 + questSupStartDropItem2.nameVis;
               } else {
                  messageText2 = messageText2 + strategist2.getItemsTotalCount(questSupStartDropItem2.typeId) + "͜" + questSupStartDropItem2.nameVis;
               }

               messageText2 = messageText2 + Mystery.stringFormat(" из %", questSupStartDropItem2.string);
               if (questSupStartDropItem2.chance < 100000) {
                  messageText2 = messageText2 + Mystery.stringFormat("($%)", '$', new String[]{Mystery.decimalToString((long)questSupStartDropItem2.chance, 3)});
               }

               for(ddIndex = fullCountForNameMapCountMap + 1; ddIndex < this.startDropItems.length; ++ddIndex) {
                  questSupStartDropItem3 = this.startDropItems[ddIndex];
                  if (hashtable.get(questSupStartDropItem3) == null && questSupStartDropItem3.typeId == questSupStartDropItem2.typeId) {
                     messageText2 = messageText2 + Mystery.stringFormat(" или %", questSupStartDropItem3.string);
                     if (questSupStartDropItem3.chance < 100000) {
                        messageText2 = messageText2 + Mystery.stringFormat("($%)", '$', new String[]{Mystery.decimalToString((long)questSupStartDropItem3.chance, 3)});
                     }

                     hashtable.put(questSupStartDropItem3, new Object());
                  }
               }

               messageText2 = messageText2 + ", ";
            }
         }

         if (!messageText2.equals("")) {
            messageText2 = messageText2.substring(0, messageText2.length() - 2);
            messageText = messageText + Mystery.stringFormat("Добыть %\n", messageText2);
         }
      } catch (Exception exception) {
         messageText = messageText + exception.toString() + "\r";
      }

      substring = "";
      if (this.needMoney > 0) {
         if (questInPlayer == null) {
            substring = substring + (substring.length() > 0 ? " и " : "") + this.getCountTextForDescription(strategist2.GP_Money, this.needMoney) + "ɼɽ";
         } else {
            substring = substring + (substring.length() > 0 ? " и " : "") + Math.min(strategist2.GP_Money, this.needMoney) + "/" + this.needMoney + "ɼɽ";
         }
      }

      if (this.needCrys > 0) {
         if (questInPlayer == null) {
            substring = substring + (substring.length() > 0 ? " и " : "") + this.getCountTextForDescription(strategist2.GP_Crystals, this.needCrys) + "ɾɿ";
         } else {
            substring = substring + (substring.length() > 0 ? " и " : "") + Math.min(strategist2.GP_Crystals, this.needCrys) + "/" + this.needCrys + "ɾɿ";
         }
      }

      if (substring.length() > 0) {
         if (questInPlayer == null) {
            messageText = messageText + Mystery.stringFormat("Собрать %\n", substring);
         } else {
            messageText = messageText + Mystery.stringFormat("Собрано %\n", substring);
         }
      }

      messageText2 = "";
      if (this.rewTeleportToMap.length() > 0) {
         messageText2 = messageText2 + Mystery.stringFormat("Перенесет в %\n", this.rewTeleportToMapLiteral);
      }

      boolean appendSeparator = false;
      if (this.rewMaxMoney > 0) {
         messageText2 = messageText2 + (this.rewMaxMoney == this.rewMinMoney ? String.valueOf(this.rewMaxMoney) : this.rewMinMoney + "ə" + this.rewMaxMoney) + "ɼɽ";
         appendSeparator = true;
      }

      if (this.rewCrys > 0) {
         messageText2 = messageText2 + (appendSeparator ? " " : "") + this.rewCrys + "ɾɿ";
         appendSeparator = true;
      }

      if (this.rewMaxExp > 0) {
         messageText2 = messageText2 + (appendSeparator ? " " : "") + (this.rewMaxExp == this.rewMinExp ? String.valueOf(this.rewMaxExp) : this.rewMinExp + "ə" + this.rewMaxExp) + "ɺɻ";
         appendSeparator = true;
      }

      if (appendSeparator) {
         messageText2 = messageText2 + "\n";
      }

      fullCountForNameMapCountMap = 0;
      if (this.rewSkillEffectInd >= 0) {
         fullCountForNameMapCountMap = strategist2.GP_Effects[this.rewSkillEffectInd];
      }

      long mult = (long)(fullCountForNameMapCountMap + 100);

      for(ddIndex = 0; ddIndex < this.rewItems.length; ++ddIndex) {
         QuestSupItemRew questSupItemRew;
         if ((questSupItemRew = this.rewItems[ddIndex]).chance >= 100000) {
            messageText2 = messageText2 + questSupItemRew.getNameWithCount() + "\n";
         } else {
            messageText2 = messageText2 + Mystery.stringFormat("$($%)\n", '$', new String[]{questSupItemRew.getNameWithCount(), Mystery.decimalToString(mult * (long)questSupItemRew.chance, 5)});
         }
      }

      if (this.rewGiveAuraInd >= 0) {
         messageText2 = messageText2 + Mystery.stringFormat("Даст ауру %\n", Com.AurasTypes[this.rewGiveAuraInd].name);
      }

      if (this.rewRemoveAura) {
         messageText2 = messageText2 + "Уберет ауру\n";
      }

      if (this.rewResetSkills) {
         messageText2 = messageText2 + "Переобучение\n";
      }

      if (this.rewProtectionSecs > 0L) {
         messageText2 = messageText2 + Mystery.stringFormat("̎̏Безопасность на %\n", Mystery.milisecondsToTime(this.rewProtectionSecs * 1000L));
      }

      if (this.rewVIPSecs > 0L) {
         messageText2 = messageText2 + Mystery.stringFormat("Героизм x% на %\n", new String[]{Com.c_vip_exp_coof[this.rewVIPType], Mystery.milisecondsToTime(this.rewVIPSecs * 1000L)});
      }

      if (this.rewBagSize > 0) {
         messageText2 = messageText2 + Mystery.stringFormat("Вместимость +%\n", String.valueOf(this.rewBagSize));
      }

      if (messageText2.length() > 0) {
         if (this.showRewardWord) {
            messageText = messageText + "Награда:\n" + messageText2;
         } else {
            messageText = messageText + messageText2;
         }
      }

      for(messageText = messageText.trim(); messageText.endsWith(",") || messageText.endsWith("\n") || messageText.endsWith("\r"); messageText = messageText.substring(0, messageText.length() - 1)) {
      }

      return messageText;
   }

   private String getCountTextForDescription(int val, int need) {
      return (this.showCountInDescription ? Math.min(val, need) + "/" : "") + need;
   }

   private String getCountTextForDescription(Strategist strategist2, ItemSimple itemSimple, int overCount) {
      return (this.showCountInDescription ? Math.min(overCount, strategist2.getItemsTotalCount(itemSimple.typeId)) + "/" : "") + overCount;
   }

   public final int[][] getMobsToDraw() {
      Hashtable hashtable = new Hashtable();

      int startDropItemIndex;
      for(startDropItemIndex = 0; startDropItemIndex < this.startDropItems.length; ++startDropItemIndex) {
         QuestSupStartDropItem questSupStartDropItem = this.startDropItems[startDropItemIndex];
         if (!hashtable.containsKey(questSupStartDropItem.string2) && (questSupStartDropItem.headInd >= 0 || questSupStartDropItem.bodyInd >= 0)) {
            hashtable.put(questSupStartDropItem.string2, new int[]{questSupStartDropItem.headInd, questSupStartDropItem.bodyInd});
         }
      }

      for(startDropItemIndex = 0; startDropItemIndex < this.needMonsters.length; ++startDropItemIndex) {
         QuestSupMonsterTarget questSupMonsterTarget = this.needMonsters[startDropItemIndex];
         if (!hashtable.containsKey(questSupMonsterTarget.monsterName) && (questSupMonsterTarget.headInd >= 0 || questSupMonsterTarget.bodyInd >= 0)) {
            hashtable.put(questSupMonsterTarget.monsterName, new int[]{questSupMonsterTarget.headInd, questSupMonsterTarget.bodyInd});
         }
      }

      if (hashtable.size() == 0) {
         return null;
      } else {
         int[][] res = new int[hashtable.size()][];
         int reIndex = 0;

         for(Enumeration iter = hashtable.keys(); iter.hasMoreElements(); ++reIndex) {
            String key = (String)iter.nextElement();
            int[] el = (int[])hashtable.get(key);
            res[reIndex] = el;
         }

         return res;
      }
   }

   public final StrategGraphic getStrategToDraw(QuestInPlayer questInPlayer) {
      StrategGraphic strategGraphic2;
      if (this.rewGiveAuraInd >= 0) {
         (strategGraphic2 = Strategist.instance.avatarGraphic.makeClone()).auraInd = (byte)this.rewGiveAuraInd;
         return strategGraphic2;
      } else if (this.rewRemoveAura) {
         (strategGraphic2 = Strategist.instance.avatarGraphic.makeClone()).auraInd = -1;
         return strategGraphic2;
      } else {
         return questInPlayer != null && this.isNeedToKillPlayer && questInPlayer.needToKillPlayer != null && questInPlayer.needToKillPlayer.needToKillPlayerGraphics != null ? questInPlayer.needToKillPlayer.needToKillPlayerGraphics : null;
      }
   }

   public final int getRewItemsWeightCount() {
      int rewItemsWeightCount = 0;

      for(int rewItemIndex = 0; rewItemIndex < this.rewItems.length; ++rewItemIndex) {
         QuestSupItemRew questSupItemRew;
         if (!(questSupItemRew = this.rewItems[rewItemIndex]).unweight) {
            rewItemsWeightCount = (int)((long)rewItemsWeightCount + MathGame.ceilDivide((long)questSupItemRew.maxCount, (long)questSupItemRew.stackMaxCount));
         }
      }

      return rewItemsWeightCount;
   }
}
