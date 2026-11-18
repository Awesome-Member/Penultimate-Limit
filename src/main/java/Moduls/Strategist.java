package Moduls;

import Base.ClientState;
import Base.Com;
import Engine.Mystery;
import Moduls.quests.QuestSupItemNeed;
import Moduls.trading.PlayersTrade;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class Strategist extends MoveableUnitDiscretShort {
   public static final Strategist instance = new Strategist(240, 240, 0, 0);
   public Vector trades = new Vector();
   public static String accountEmail = null;
   public int commonChatShowTypes = 0;
   public boolean isAutoFixItems = true;
   public boolean isAutoCancelTrade = false;
   public boolean isAutoCancelFights = false;
   public boolean isAutoCancelParty = false;
   public int compassMode = 3;
   public int guiMode = 1;
   public boolean guiDrawMiniMap = true;
   public int curMapInd = -1;
   public String curMapId = null;
   public boolean isInClan = false;
   public String clanIcon = "";
   public boolean isModer = false;
   public int id = 0;
   public byte partyState;
   public int partyId;
   public String[][] partyInfo = new String[0][];
   public final Object partyInfoLock = new Object();
   public Vector items = new Vector();
   private int TimeAccum2 = 0;
   private int LifeAcc = 0;
   public boolean hasMaster;
   public int petB = -1;
   public int petH = -1;
   public int petX = -1;
   public int petY = -1;
   public int petDirection = 1;
   public int GP_FireLevel;
   public int GP_IceLevel;
   public int GP_HeroLevel;
   public boolean protectionStatus = false;
   public long protectionStatusEndTime = 0L;
   public byte vipStatus = -1;
   public long vipStatusEndTime = 0L;
   public int buyedBagSize = 0;
   public int GP_MaxTroops = 0;
   public String GP_Name;
   public String GP_AboutMe;
   public int unstableCount = 0;
   public int GP_Gender;
   public int GP_Level;
   public int GP_Money;
   public int GP_Crystals;
   public int GP_Race;
   public byte watchDirection = 0;
   public long animationTime = 0L;
   private int animationLastStateType = -1;
   public int avatarWalkSpeed = 1000;
   public StrategGraphic avatarGraphic = new StrategGraphic();
   public int atackSprite = -2;
   public int atackBaseX = 0;
   public int atackBaseY = -13;
   public int atackLatency = 0;
   public int avatarRegeneration;
   public int avatarArmor;
   public int avatarAtack;
   public int avatarLife;
   public int avatarHit;
   public int avatarSpeed;
   public int avatarDmg;
   public int avatarLifeCur;
   public int avatarHitCur;
   public int avatarSpeedCur;
   public int avatarDmgCur;
   public int maxIceLvl;
   public int avatarAtackDistance;
   public int maxFireLvl;
   public int GP_Temperature;
   public int GP_NextLevelExp;
   public int GP_TemperTime;
   public int GP_AddTemperature;
   public int GP_Life;
   public int GP_Prisoners;
   public int GP_Heads = 0;
   public int[] GP_SelfSkillsLevels;
   public int[] GP_AddSkillsLevels;
   public int[] GP_TotalSkillsLevels;
   public int GP_Exp;
   public int GP_SkillPoints;
   public int[] GP_Effects = new int[20];
   public Vector GP_Army = new Vector();
   public Vector GP_Highers = new Vector();
   private boolean was_deactive_highs;
   public static int cd_wi;
   public static int cd_hi;
   public int forcePower = 0;

   public Strategist(int cX, int cY, int cdW, int cdH) {
      super((short)cX, (short)cY, (short)0, (short)0);
      this.items.removeAllElements();
      this.GP_SelfSkillsLevels = new int[Skill.MaxSkills];
      this.GP_AddSkillsLevels = new int[Skill.MaxSkills];
      this.GP_TotalSkillsLevels = new int[Skill.MaxSkills];
   }

   public final PlayersTrade tradeFindWithPlayerId(int playerId) {
      for(int i = 0; i < this.trades.size(); ++i) {
         PlayersTrade playersTrade;
         if ((playersTrade = (PlayersTrade)this.trades.elementAt(i)).tradeSuggestion.playerId == playerId) {
            return playersTrade;
         }

         if (playersTrade.tradeSuggestion2.playerId == playerId) {
            return playersTrade;
         }
      }

      return null;
   }

   public final PlayersTrade tradeFind(int id2) {
      for(int i = 0; i < this.trades.size(); ++i) {
         PlayersTrade playersTrade;
         if ((playersTrade = (PlayersTrade)this.trades.elementAt(i)).id == id2) {
            return playersTrade;
         }
      }

      return null;
   }

   public final int getItemsTotalCount(long typeId) {
      int res = 0;
      Enumeration enumeration = this.items.elements();

      while(enumeration.hasMoreElements()) {
         InventoryItem inventoryItem2;
         if ((inventoryItem2 = (InventoryItem)enumeration.nextElement()).typeId == typeId) {
            res += inventoryItem2.count;
         }
      }

      return res;
   }

   public final boolean isEnoughtItems(ItemSimple[] need, boolean checkDurability) {
      return this.isEnoughtItemsTempStep(need, true).size() <= 0;
   }

   public final ItemSimple[] isEnoughtItemsCount(ItemSimple[] need, boolean checkDurability) {
      Hashtable hashtable;
      ItemSimple[] res = new ItemSimple[(hashtable = this.isEnoughtItemsTempStep(need, true)).size()];
      int i = 0;

      for(Enumeration enu = hashtable.keys(); enu.hasMoreElements(); ++i) {
         Long longValue = (Long)enu.nextElement();
         res[i] = (ItemSimple)hashtable.get(longValue);
      }

      return res;
   }

   private Hashtable isEnoughtItemsTempStep(ItemSimple[] need, boolean checkDurability) {
      Hashtable hashtable = new Hashtable();

      Long longValue;
      ItemSimple itemSimple;
      for(int needCount = need.length - 1; needCount >= 0; --needCount) {
         ItemSimple itemSimple2 = need[needCount];
         longValue = itemSimple2.typeId;
         if ((itemSimple = (ItemSimple)hashtable.get(longValue)) == null) {
            hashtable.put(longValue, itemSimple2.copy());
         } else {
            itemSimple.count += itemSimple2.count;
         }
      }

      Enumeration enumeration = this.items.elements();

      while(true) {
         InventoryItem inventoryItem2;
         do {
            if (!enumeration.hasMoreElements()) {
               return hashtable;
            }

            inventoryItem2 = (InventoryItem)enumeration.nextElement();
         } while(checkDurability && inventoryItem2.durability < inventoryItem2.maxDurability);

         longValue = inventoryItem2.typeId;
         if ((itemSimple = (ItemSimple)hashtable.get(longValue)) != null) {
            int count;
            if ((count = itemSimple.count - inventoryItem2.count) <= 0) {
               hashtable.remove(longValue);
            } else {
               itemSimple.count = count;
            }
         }
      }
   }

   public final QuestSupItemNeed isEnoughtItemsJustFromMarket(QuestSupItemNeed[] need, boolean checkDurability) {
      Hashtable hashtable = this.isEnoughtItemsTempStep(need, true);
      QuestSupItemNeed questSupItemNeed = null;
      Enumeration enumeration = hashtable.keys();

      while(enumeration.hasMoreElements()) {
         long key = (Long)enumeration.nextElement();

         for(int i = 0; i < need.length; ++i) {
            if (need[i].typeId == key) {
               if (!need[i].availableInMarket) {
                  return null;
               }

               if (questSupItemNeed != null) {
                  return questSupItemNeed;
               }

               questSupItemNeed = need[i];
            }
         }
      }

      return questSupItemNeed;
   }

   public final boolean isEnoughtItems(long itemTypeId, int count, boolean checkDurability) {
      Enumeration enumeration = this.items.elements();

      while(enumeration.hasMoreElements()) {
         InventoryItem inventoryItem2;
         if ((inventoryItem2 = (InventoryItem)enumeration.nextElement()).typeId == itemTypeId) {
            count -= inventoryItem2.count;
         }
      }

      if (count <= 0) {
         return true;
      } else {
         return false;
      }
   }

   public final int getMaxInventorySize() {
      return Com.c_base_bag_size + this.GP_HeroLevel + this.buyedBagSize;
   }

   public final int getItemsWeight() {
      return this.items.size() - this.getUnweightItemsCount();
   }

   private int getUnweightItemsCount() {
      int unweightItemsCount = 0;

      for(int itemsCount = this.items.size() - 1; itemsCount >= 0; --itemsCount) {
         if (((InventoryItem)this.items.elementAt(itemsCount)).unweight) {
            ++unweightItemsCount;
         }
      }

      return unweightItemsCount;
   }

   public final void CalculateSkills() {
      for(int gP_EffectIndex = 0; gP_EffectIndex < 20; ++gP_EffectIndex) {
         this.GP_Effects[gP_EffectIndex] = 0;
      }

      if (this.GP_TotalSkillsLevels.length != this.GP_SelfSkillsLevels.length) {
         this.GP_TotalSkillsLevels = new int[this.GP_SelfSkillsLevels.length];
      }

      int[] integers = new int[20];

      for(int gP_TotalSkillsLevelIndex = 0; gP_TotalSkillsLevelIndex < Skill.MaxSkills; ++gP_TotalSkillsLevelIndex) {
         this.GP_TotalSkillsLevels[gP_TotalSkillsLevelIndex] = this.GP_SelfSkillsLevels[gP_TotalSkillsLevelIndex] + this.GP_AddSkillsLevels[gP_TotalSkillsLevelIndex];
         int[] GP_TotalSkillsLevels2 = this.GP_TotalSkillsLevels;
         GP_TotalSkillsLevels2[gP_TotalSkillsLevelIndex] = Math.min(GP_TotalSkillsLevels2[gP_TotalSkillsLevelIndex], Skill.Skills[gP_TotalSkillsLevelIndex].valueBySkillLvl.length - 1);
         Skill skill = Skill.Skills[gP_TotalSkillsLevelIndex];
         int gP_TotalSkillsLevels = this.GP_TotalSkillsLevels[gP_TotalSkillsLevelIndex];
         Skill skill2 = skill;
         integers[skill2.effectInd] = skill2.valueBySkillLvl[gP_TotalSkillsLevels];

         for(int gP_Effects2Index = 0; gP_Effects2Index < 20; ++gP_Effects2Index) {
            int[] GP_Effects2 = this.GP_Effects;
            GP_Effects2[gP_Effects2Index] += integers[gP_Effects2Index];
            integers[gP_Effects2Index] = 0;
         }
      }

   }

   public final void animate() {
      switch (this.moveLastDirection8D) {
         case 0:
         case 1:
         case 2:
            this.watchDirection = 0;
         case 3:
         default:
            break;
         case 4:
         case 5:
         case 6:
            this.watchDirection = 1;
      }

      if (ClientState.instance.get() == 4) {
         this.TimeAccum2 = (int)((long)this.TimeAccum2 + Mystery.FrameTimeI);
      }

      int deltaX;
      for(deltaX = this.GP_Life; this.TimeAccum2 >= 100; this.TimeAccum2 -= 100) {
         this.LifeAcc += (int)((long)this.avatarLife * (long)this.avatarRegeneration / 60L);
      }

      if (this.LifeAcc / 1000 > 0) {
         this.GP_Life += this.LifeAcc / 1000;
         this.LifeAcc %= 1000;
      }

      int avatarLife2 = this.avatarLife;
      if (this.GP_Life > avatarLife2) {
         this.GP_Life = this.GP_Life < avatarLife2 ? deltaX : avatarLife2;
      }

      boolean b = false;
      if (this.GP_Money <= 0) {
         this.GP_Money = 0;
         if (!this.was_deactive_highs) {
            this.was_deactive_highs = true;
            b = true;
            this.GP_Money = 0;

            for(avatarLife2 = 0; avatarLife2 < this.GP_Highers.size(); ++avatarLife2) {
               if (((Higher)this.GP_Highers.elementAt(avatarLife2)).curCost != 0) {
                  ((Higher)this.GP_Highers.elementAt(avatarLife2)).Active = false;
               }
            }
         }
      } else if (this.was_deactive_highs) {
         this.was_deactive_highs = false;
         b = true;

         for(avatarLife2 = 0; avatarLife2 < this.GP_Highers.size(); ++avatarLife2) {
            ((Higher)this.GP_Highers.elementAt(avatarLife2)).Active = true;
         }
      }

      if (b) {
         this.CalculateSkills();
      }

      if (this.vipStatus >= 0) {
         this.vipStatusEndTime -= Mystery.FrameTimeI;
         if (this.vipStatusEndTime <= 0L) {
            this.vipStatusEndTime = 0L;
            this.vipStatus = -1;
         }
      }

      if (this.protectionStatus) {
         this.protectionStatusEndTime -= Mystery.FrameTimeI;
         if (this.protectionStatusEndTime <= 0L) {
            this.protectionStatusEndTime = 0L;
            this.protectionStatus = false;
         }
      }

      byte animationLastStateType2 = this.isMoveLastDirection8D() ? StrategAnimGroup.ANIMATION_STATE_WALK : StrategAnimGroup.ANIMATION_STATE_STAY;
      if (this.animationLastStateType != animationLastStateType2) {
         this.animationTime = 0L;
         this.animationLastStateType = animationLastStateType2;
      } else {
         this.animationTime += Mystery.FrameTimeI;
      }

      if (this.petB >= 0 && this.petH >= 0) {
         deltaX = this.cx - this.petX;

         for(avatarLife2 = this.cy - this.petY; deltaX * deltaX + avatarLife2 * avatarLife2 > Com.c_pet_off_base_y; avatarLife2 = this.cy - this.petY) {
            if (avatarLife2 < 0) {
               if (deltaX < Com.c_pet_off_base_x) {
                  --this.petX;
                  this.petDirection = 1;
               }

               if (deltaX > Com.c_pet_off_base_x) {
                  ++this.petX;
                  this.petDirection = 0;
               }

               --this.petY;
            } else {
               if (deltaX < 0) {
                  --this.petX;
                  this.petDirection = 1;
               }

               if (deltaX > 0) {
                  ++this.petX;
                  this.petDirection = 0;
               }

               if (avatarLife2 > 0) {
                  ++this.petY;
               }
            }

            deltaX = this.cx - this.petX;
         }
      }

   }

   public final void onChanged(boolean mainThread) {
      int petH2 = this.petH;
      int petB2 = this.petB;
      this.petH = -1;
      this.petB = -1;
      if (this.GP_Highers.size() > 0) {
         this.petH = ((Higher)this.GP_Highers.firstElement()).HeadInd;
         this.petB = ((Higher)this.GP_Highers.firstElement()).BodyInd;
      }

      if (this.petH != petH2 || this.petB != petB2) {
         this.petX = this.cx + Com.c_pet_off_x_up;
         this.petY = this.cy + Com.c_master_avail_max_lvl;
      }

      this.CalculateSkills();
      Com.onStrategChanged(true);
   }

   public final void stableAllItems() {
      for(int i = 0; i < this.items.size(); ++i) {
         ((InventoryItem)this.items.elementAt(i)).isStable = true;
      }

   }

   public static InventoryItem loadIndventoryItem(DataInputStream dataInputStream) {
      InventoryItem inventoryItem2;
      (inventoryItem2 = new InventoryItem()).id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.typeId = (long)decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.count = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      inventoryItem2.state = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.durability = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.upgradeLevel = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.canUse = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      byte count = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.upgrades.clear();

      for(int i3 = 0; i3 < count; ++i3) {
         inventoryItem2.upgrades.put(new Byte(decompiledsupport.UncheckedDataInput.readByte(dataInputStream)), new Integer(decompiledsupport.UncheckedDataInput.readInt(dataInputStream)));
      }

      inventoryItem2.isCanUpgradeCheap = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.openState = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      if (inventoryItem2.openState == 2) {
         inventoryItem2.openItem = ItemSimple.readInstance(dataInputStream);
      }

      if (inventoryItem2.openState > 0) {
         inventoryItem2.openInfo = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      }

      inventoryItem2.forgeAvail = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      if (inventoryItem2.forgeAvail) {
         inventoryItem2.forgeInfo = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
         inventoryItem2.forgeNeedCrys = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         inventoryItem2.forgeNeedGold = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
         inventoryItem2.forgeCan = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      }

      inventoryItem2.maxUpgrade = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.icon = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      inventoryItem2.name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      inventoryItem2.description = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      inventoryItem2.readAdditional(dataInputStream);
      inventoryItem2.useType = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.costGold = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.crysSell = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.costCrys = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.resell = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.justInClan = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.unsoldeable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.unweight = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.repairCost = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.maxDurability = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.useable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.putonable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.canMutateInd = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.canBeMutatedInd = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.isStable = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      inventoryItem2.battleSlot = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      inventoryItem2.isCanSpawn = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      if (inventoryItem2.isCanSpawn) {
         inventoryItem2.spawnDescription = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
         inventoryItem2.upgradeCheapSpeed = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapPull = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapAnswer = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapFriends = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapParalize = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapPoison = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapHitDist = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         inventoryItem2.upgradeCheapHealth = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      }

      inventoryItem2.changeHitPowerBase = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeLifeBase = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeAtackBase = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeDefenceBase = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeHitPowerNow = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeLifeNow = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeAtackNow = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      inventoryItem2.changeDefenceNow = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      return inventoryItem2;
   }
}
