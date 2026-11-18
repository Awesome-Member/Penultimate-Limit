package Moduls;

import MenuPck.MenuList;
import java.io.DataInputStream;
import java.util.Hashtable;
import java.util.Vector;

public final class InventoryItem {
   public int id = -1;
   public long typeId = -1L;
   public int count = 1;
   public int state = 0;
   public boolean forgeAvail = false;
   public boolean forgeCan = false;
   public String forgeInfo = null;
   public int forgeNeedCrys = 0;
   public long forgeNeedGold = 0L;
   public Hashtable upgrades = new Hashtable();
   public boolean upgradeCheapSpeed;
   public boolean upgradeCheapPull;
   public boolean upgradeCheapAnswer;
   public boolean upgradeCheapFriends;
   public boolean upgradeCheapParalize;
   public boolean upgradeCheapPoison;
   public boolean upgradeCheapHitDist;
   public boolean upgradeCheapHealth;
   public int upgradeLevel;
   public int maxUpgrade;
   public boolean isCanUpgradeCheap = false;
   public int durability;
   public int maxDurability;
   public int repairCost;
   public boolean useable;
   public boolean isStable;
   public int costGold;
   public int costCrys;
   public int crysSell;
   public int useType;
   public String icon;
   public String name;
   public String description;
   public boolean resell;
   public boolean justInClan;
   public boolean unsoldeable = false;
   public boolean unweight = false;
   public byte battleSlot;
   public boolean isCanSpawn;
   public String spawnDescription;
   public byte canMutateInd = -1;
   public byte canBeMutatedInd = -1;
   public boolean putonable = true;
   public boolean canUse = true;
   public boolean drawMob = false;
   public int drawMobHead = -1;
   public int drawMobBody = -1;
   public byte openState;
   public String openInfo;
   public ItemSimple openItem = null;
   public byte[] setSpritesSlot = new byte[0];
   public short[][] setSpritesSprite = new short[0][0];
   public byte[][] setSpritesOtherSlots = new byte[0][2];
   public int changeHitPowerBase;
   public int changeLifeBase;
   public int changeAtackBase;
   public int changeDefenceBase;
   public int changeHitPowerNow;
   public int changeLifeNow;
   public int changeAtackNow;
   public int changeDefenceNow;

   public final int getUpgradeByKey(byte key) {
      return !this.upgrades.containsKey(key) ? 0 : (Integer)this.upgrades.get(key);
   }

   public final String getItemDisplayName() {
      return this.icon + this.name;
   }

   public final String getNameForMenu() {
      return getNameForMenu(this.count, this.icon, this.name);
   }

   public static String getNameForMenu(int count2, String icon2, String name2) {
      return count2 == 1 ? icon2 + name2 : count2 + "x" + icon2 + name2;
   }

   public final void readAdditional(DataInputStream dataInputStream) {
      this.drawMob = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      if (this.drawMob) {
         this.drawMobHead = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.drawMobBody = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

      byte c = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.setSpritesSlot = new byte[c];
      this.setSpritesSprite = new short[c][];

      int setSpritesSlotIndex;
      for(setSpritesSlotIndex = 0; setSpritesSlotIndex < c; ++setSpritesSlotIndex) {
         this.setSpritesSlot[setSpritesSlotIndex] = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
         this.setSpritesSprite[setSpritesSlotIndex] = new short[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

         for(int setSpritesSpriteIndex = 0; setSpritesSpriteIndex < this.setSpritesSprite[setSpritesSlotIndex].length; ++setSpritesSpriteIndex) {
            this.setSpritesSprite[setSpritesSlotIndex][setSpritesSpriteIndex] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         }
      }

      this.setSpritesOtherSlots = new byte[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)][];

      for(setSpritesSlotIndex = 0; setSpritesSlotIndex < this.setSpritesOtherSlots.length; ++setSpritesSlotIndex) {
         this.setSpritesOtherSlots[setSpritesSlotIndex] = new byte[2];
         this.setSpritesOtherSlots[setSpritesSlotIndex][0] = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
         this.setSpritesOtherSlots[setSpritesSlotIndex][1] = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      }

   }

   public final boolean hasStrategGraphic() {
      return this.setSpritesSlot.length > 0;
   }

   public final StrategGraphic createStrategGraphic(int raceId, int gender) {
      if (this.setSpritesSlot.length <= 0) {
         return null;
      } else {
         try {
            StrategGraphic strategGraphic2 = new StrategGraphic();
            Vector[] temp = new Vector[17];
            byte[] orders = new byte[17];

            for(int tempIndex = 0; tempIndex < 17; ++tempIndex) {
               temp[tempIndex] = new Vector();
               orders[tempIndex] = 0;
            }

            StrategRace strategRace = StrategRace.strategRaces[raceId];

            int setSpritesOtherSlotIndex;
            for(raceId = 0; raceId < strategRace.spritesSetSlot.length; ++raceId) {
               temp[strategRace.spritesSetSlot[raceId]].removeAllElements();

               for(setSpritesOtherSlotIndex = 0; setSpritesOtherSlotIndex < strategRace.spritesSetSpritesByGender[raceId].length; ++setSpritesOtherSlotIndex) {
                  temp[strategRace.spritesSetSlot[raceId]].addElement(new Short(strategRace.spritesSetSpritesByGender[raceId][setSpritesOtherSlotIndex][gender]));
               }
            }

            for(raceId = 0; raceId < this.setSpritesSlot.length; ++raceId) {
               temp[this.setSpritesSlot[raceId]].removeAllElements();

               for(setSpritesOtherSlotIndex = 0; setSpritesOtherSlotIndex < this.setSpritesSprite[raceId].length; ++setSpritesOtherSlotIndex) {
                  temp[this.setSpritesSlot[raceId]].addElement(new Short(this.setSpritesSprite[raceId][setSpritesOtherSlotIndex]));
               }

               for(setSpritesOtherSlotIndex = 0; setSpritesOtherSlotIndex < this.setSpritesOtherSlots.length; ++setSpritesOtherSlotIndex) {
                  if (this.setSpritesOtherSlots[setSpritesOtherSlotIndex][1] > orders[this.setSpritesOtherSlots[setSpritesOtherSlotIndex][0]]) {
                     orders[this.setSpritesOtherSlots[setSpritesOtherSlotIndex][0]] = this.setSpritesOtherSlots[setSpritesOtherSlotIndex][1];
                  }
               }
            }

            for(raceId = 0; raceId < 17; ++raceId) {
               if (temp[raceId].size() > 0) {
                  strategGraphic2.itemsSprites[raceId] = (Short)temp[raceId].elementAt(orders[raceId]);
               } else {
                  strategGraphic2.itemsSprites[raceId] = -1;
               }
            }

            return strategGraphic2;
         } catch (Exception exception) {
            exception.printStackTrace();
            return null;
         }
      }
   }

   public final void showInMenu(MenuList menuList2) {
      MenuList.setAnimatedTitle(this.description);
      if (this.drawMob) {
         MenuList.showUnitPortrait(this.drawMobHead, this.drawMobBody);
      }

      if (this.hasStrategGraphic()) {
         MenuList.showAvatarPortrait(this.createStrategGraphic(Strategist.instance.GP_Race, Strategist.instance.GP_Gender));
      }

   }
}
