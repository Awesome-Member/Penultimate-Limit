package Moduls;

import Base.Com;
import java.io.DataInputStream;

public final class StrategRace {
   public static StrategRace[] strategRaces;
   private int ind;
   public String[] names;
   public boolean availableForPlayer = false;
   private int temperatureCoof;
   private int decoreCode;
   public String[] descriptions;
   private int[] speeds = new int[]{29, 21, 29, 21, 29, 21, 29, 21, 10, 10};
   public byte[] spritesSetSlot;
   public short[][][] spritesSetSpritesByGender;

   public final StrategGraphic getStrategGraphic(int gender) {
      StrategGraphic strategGraphic2 = new StrategGraphic();

      for(int i = 0; i < this.spritesSetSlot.length; ++i) {
         strategGraphic2.itemsSprites[this.spritesSetSlot[i]] = this.spritesSetSpritesByGender[i][0][gender];
      }

      return strategGraphic2;
   }

   public static void loadAllFromStream(DataInputStream dataInputStream) {
      strategRaces = new StrategRace[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

      for(int ind2 = 0; ind2 < strategRaces.length; ++ind2) {
         Com.PrBarTh.setText("Раса " + (ind2 + 1) + "/" + strategRaces.length);
         StrategRace strategRace = new StrategRace();
         strategRaces[ind2] = strategRace;
         strategRace.ind = ind2;
         strategRace.names = new String[]{decompiledsupport.UncheckedDataInput.readUTF(dataInputStream), decompiledsupport.UncheckedDataInput.readUTF(dataInputStream)};
         strategRace.temperatureCoof = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         strategRace.decoreCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         strategRace.speeds = new int[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

         int speedIndex;
         for(speedIndex = 0; speedIndex < strategRace.speeds.length; ++speedIndex) {
            strategRace.speeds[speedIndex] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         }

         strategRace.descriptions = new String[]{decompiledsupport.UncheckedDataInput.readUTF(dataInputStream), decompiledsupport.UncheckedDataInput.readUTF(dataInputStream)};
         strategRace.availableForPlayer = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         strategRace.spritesSetSlot = new byte[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];
         strategRace.spritesSetSpritesByGender = new short[strategRace.spritesSetSlot.length][][];

         for(speedIndex = 0; speedIndex < strategRace.spritesSetSlot.length; ++speedIndex) {
            strategRace.spritesSetSlot[speedIndex] = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
            strategRace.spritesSetSpritesByGender[speedIndex] = new short[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)][];

            for(int spritesSetSpritesByGenderIndex = 0; spritesSetSpritesByGenderIndex < strategRace.spritesSetSpritesByGender[speedIndex].length; ++spritesSetSpritesByGenderIndex) {
               strategRace.spritesSetSpritesByGender[speedIndex][spritesSetSpritesByGenderIndex] = new short[2];
               strategRace.spritesSetSpritesByGender[speedIndex][spritesSetSpritesByGenderIndex][0] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
               strategRace.spritesSetSpritesByGender[speedIndex][spritesSetSpritesByGenderIndex][1] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
            }
         }
      }

   }
}
