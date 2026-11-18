package Moduls;

import Base.Com;
import java.io.DataInputStream;

public final class StrategProgramAnim {
   public static StrategProgramAnim[] strategProgramAnims;
   public byte[][][] animSlotsInds = new byte[3][][];
   public short[][] animFrameTimes = new short[3][];
   public CoordShort[][][] animOffsets = new CoordShort[3][][];

   private void readGroup(DataInputStream dataInputStream, byte animationInd) {
      this.animSlotsInds[animationInd] = new byte[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)][];
      this.animFrameTimes[animationInd] = new short[this.animSlotsInds[animationInd].length];
      this.animOffsets[animationInd] = new CoordShort[this.animSlotsInds[animationInd].length][];
      loadFromStream(dataInputStream, this.animSlotsInds[animationInd], this.animFrameTimes[animationInd], this.animOffsets[animationInd]);
   }

   private static void loadFromStream(DataInputStream dataInputStream, byte[][] bytesGrid, short[] shorts, CoordShort[][] coordShortsGrid) {
      for(int lengthIndex = 0; lengthIndex < bytesGrid.length; ++lengthIndex) {
         bytesGrid[lengthIndex] = new byte[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

         int bytesGridIndex;
         for(bytesGridIndex = 0; bytesGridIndex < bytesGrid[lengthIndex].length; ++bytesGridIndex) {
            bytesGrid[lengthIndex][bytesGridIndex] = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
         }

         shorts[lengthIndex] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         coordShortsGrid[lengthIndex] = new CoordShort[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

         for(bytesGridIndex = 0; bytesGridIndex < coordShortsGrid[lengthIndex].length; ++bytesGridIndex) {
            coordShortsGrid[lengthIndex][bytesGridIndex] = new CoordShort(decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream));
         }
      }

   }

   public static void loadAllFromStream(DataInputStream dataInputStream) {
      strategProgramAnims = new StrategProgramAnim[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(int i = 0; i < strategProgramAnims.length; ++i) {
         Com.PrBarTh.setText("Программная анимация " + (i + 1) + "/" + strategProgramAnims.length);
         StrategProgramAnim strategProgramAnim = new StrategProgramAnim();
         strategProgramAnims[i] = strategProgramAnim;
         (strategProgramAnim = strategProgramAnim).readGroup(dataInputStream, StrategAnimGroup.ANIMATION_STATE_STAY);
         strategProgramAnim.readGroup(dataInputStream, StrategAnimGroup.ANIMATION_STATE_WALK);
         strategProgramAnim.readGroup(dataInputStream, StrategAnimGroup.ANIMATION_STATE_ATACK);
      }

   }
}
