package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Engine.AnimSprite;
import java.io.DataInputStream;
import desktop.graphics.GameGraphics;

public final class StrategGraphic {
   public byte auraInd = -1;
   public short[] itemsSprites = new short[17];
   private int[] basePaddings = new int[4];
   public int[] basePaddingsSimetric = new int[4];
   public int[] basePaddingsNoAura = new int[4];

   public StrategGraphic() {
      for(int i = 0; i < this.itemsSprites.length; ++i) {
         this.itemsSprites[i] = -1;
      }

   }

   public StrategGraphic(DataInputStream dataInputStream) {
      this.readFromNet(dataInputStream);
   }

   public final StrategGraphic makeClone() {
      StrategGraphic strategGraphic2;
      (strategGraphic2 = new StrategGraphic()).auraInd = this.auraInd;
      System.arraycopy(this.itemsSprites, 0, strategGraphic2.itemsSprites, 0, 17);
      strategGraphic2.initBasePaddings();
      return strategGraphic2;
   }

   public final void readFromNet(DataInputStream dataInputStream) {
      this.auraInd = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);

      for(int i = 0; i < this.itemsSprites.length; ++i) {
         this.itemsSprites[i] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      }

      this.initBasePaddings();
   }

   private void initBasePaddings() {
      byte ANIMATION_STATE_STAY2 = StrategAnimGroup.ANIMATION_STATE_STAY;
      boolean unusedProgressMarker = false;
      this.basePaddings = this.getPaddings((byte)0, ANIMATION_STATE_STAY2, true);
      ANIMATION_STATE_STAY2 = StrategAnimGroup.ANIMATION_STATE_STAY;
      unusedProgressMarker = false;
      this.basePaddingsSimetric = this.getPaddings((byte)0, ANIMATION_STATE_STAY2, true);
      this.basePaddingsSimetric[0] = this.basePaddingsSimetric[1] = Math.max(this.basePaddingsSimetric[1], this.basePaddingsSimetric[0]);
      this.basePaddingsNoAura = this.getPaddings((byte)0, StrategAnimGroup.ANIMATION_STATE_STAY, false);
   }

   public final int[] getBasePaddingsNoAura(int direction) {
      if (direction == 0) {
         return this.basePaddingsNoAura;
      } else {
         int[] res = new int[this.basePaddingsNoAura.length];
         System.arraycopy(this.basePaddingsNoAura, 0, res, 0, this.basePaddingsNoAura.length);
         int t = res[0];
         res[0] = res[1];
         res[1] = t;
         return res;
      }
   }

   public final int[] getBasePaddingsNoAuraSimetric() {
      int[] res = new int[this.basePaddingsNoAura.length];
      System.arraycopy(this.basePaddingsNoAura, 0, res, 0, this.basePaddingsNoAura.length);
      res[0] = res[1] = Math.max(res[1], res[0]);
      return res;
   }

   public final boolean isPixelTransporant(int offX, int offY, byte direction, byte animationState, long time, boolean loop, boolean withAura, int addOffY, boolean withShadow) {
      short[][] programOffs = getSlotsProgramOffsetsBySprites(this.itemsSprites, animationState, time, loop);

      for(int i = 1; i < this.itemsSprites.length; ++i) {
         StrategAnimSprite strategAnimSprite;
         short frameIndexByTime;
         if ((frameIndexByTime = this.itemsSprites[i]) >= 0 && (strategAnimSprite = StrategAnimGroup.strategAnimGroups[frameIndexByTime].animations[animationState]) != null) {
            frameIndexByTime = AnimSprite.getFrameIndexByTime(time, loop, strategAnimSprite.numFrames, strategAnimSprite.animTime);
            switch (direction) {
               case 0:
                  if (!strategAnimSprite.isTransporant(strategAnimSprite.baseX + offX - programOffs[i][0], strategAnimSprite.baseY + offY - programOffs[i][1], frameIndexByTime, false)) {
                     return false;
                  }
                  break;
               case 1:
                  if (!strategAnimSprite.isTransporant(strategAnimSprite.frameWidth - strategAnimSprite.baseX + offX - programOffs[i][0], strategAnimSprite.baseY + offY - programOffs[i][1], frameIndexByTime, true)) {
                     return false;
                  }
            }
         }
      }

      return true;
   }

   private static short[][] getSlotsProgramOffsetsBySprites(short[] itemsSprites2, byte animationState, long time, boolean loop) {
      short[][] res = new short[17][2];

      int reIndex;
      for(reIndex = 0; reIndex < res.length; ++reIndex) {
         res[reIndex][0] = 0;
         res[reIndex][1] = 0;
      }

      for(int itemsSprites2Index = 0; itemsSprites2Index < itemsSprites2.length; ++itemsSprites2Index) {
         StrategAnimGroup strategAnimGroup;
         short frameIndexByTime;
         if ((frameIndexByTime = itemsSprites2[itemsSprites2Index]) >= 0 && (strategAnimGroup = StrategAnimGroup.strategAnimGroups[frameIndexByTime]).useAnim != null) {
            int animationProgramIndex = 0;

            for(int useAnim = strategAnimGroup.useAnim.animFrameTimes[animationState].length; animationProgramIndex < useAnim; ++animationProgramIndex) {
               reIndex = strategAnimGroup.useAnim.animOffsets[animationState][animationProgramIndex].length;
               int useAnim2 = strategAnimGroup.useAnim.animFrameTimes[animationState][animationProgramIndex];
               frameIndexByTime = AnimSprite.getFrameIndexByTime(time, loop, reIndex, useAnim2);
               CoordShort coordShort = strategAnimGroup.useAnim.animOffsets[animationState][animationProgramIndex][frameIndexByTime];
               useAnim2 = 0;

               for(int useAnim3 = strategAnimGroup.useAnim.animSlotsInds[animationState][animationProgramIndex].length; useAnim2 < useAnim3; ++useAnim2) {
                  byte slot = strategAnimGroup.useAnim.animSlotsInds[animationState][animationProgramIndex][useAnim2];
                  res[slot][0] += coordShort.x;
                  res[slot][1] += coordShort.y;
               }
            }
         }
      }

      return res;
   }

   public final int[] getPaddings(byte direction, byte animationState, boolean withAura) {
      int[] res;
      (res = new int[4])[0] = Tile.width2_6;
      res[1] = Tile.width2_6;
      res[2] = Tile.height2_3;
      if (this.auraInd >= 0 && withAura) {
         res[2] = Math.max(res[2], Com.AurasTypes[this.auraInd].getTopOffset());
      }

      short[][] programOffs = getSlotsProgramOffsetsBySprites(this.itemsSprites, animationState, 0L, true);

      for(int i = 0; i < this.itemsSprites.length; ++i) {
         short ind;
         StrategAnimSprite strategAnimSprite;
         if ((ind = this.itemsSprites[i]) >= 0 && (strategAnimSprite = StrategAnimGroup.strategAnimGroups[ind].animations[animationState]) != null) {
            res[2] = Math.max(res[2], strategAnimSprite.baseY - programOffs[i][1]);
            res[3] = Math.max(res[3], strategAnimSprite.frameHeight - strategAnimSprite.baseY + programOffs[i][1]);
            switch (direction) {
               case 0:
                  res[0] = Math.max(res[0], strategAnimSprite.baseX - programOffs[i][0]);
                  res[1] = Math.max(res[1], strategAnimSprite.frameWidth - strategAnimSprite.baseX + programOffs[i][0]);
                  break;
               case 1:
                  res[1] = Math.max(res[1], strategAnimSprite.baseX - programOffs[i][0]);
                  res[0] = Math.max(res[0], strategAnimSprite.frameWidth - strategAnimSprite.baseX + programOffs[i][0]);
            }
         }
      }

      return res;
   }

   public final void draw(GameGraphics gameGraphics2, int cx, int cy, byte direction, byte animationState, long time, boolean loop) {
      draw(gameGraphics2, this.itemsSprites, this.auraInd, cx, cy, direction, animationState, time, loop, (short)0, false);
   }

   public final void draw(GameGraphics gameGraphics2, int cx, int cy, byte direction, byte animationState, long time, boolean loop, boolean withQuestTarget) {
      draw(gameGraphics2, this.itemsSprites, this.auraInd, cx, cy, direction, animationState, time, loop, (short)0, withQuestTarget);
   }

   public static void draw(GameGraphics gameGraphics2, short[] itemsSprites2, byte auraInd2, int cx, int cy, byte direction, byte animationState, long time, boolean loop, short addOffY, boolean withQuestTarget) {
      if (withQuestTarget) {
         ImagesGlobal.addingAnimSprites[26].drawCenter(gameGraphics2, cx, cy, 0);
      }

      if (auraInd2 >= 0) {
         Com.AurasTypes[auraInd2].draw(gameGraphics2, cx, cy);
      }

      short[][] programOffs = getSlotsProgramOffsetsBySprites(itemsSprites2, animationState, time, loop);
      if (addOffY != 0) {
         for(int lengthIndex = 0; lengthIndex < programOffs.length; ++lengthIndex) {
            if (lengthIndex != 0) {
               programOffs[lengthIndex][1] += addOffY;
            }
         }
      }

      for(int itemsSprites2Index = 0; itemsSprites2Index < itemsSprites2.length; ++itemsSprites2Index) {
         StrategAnimSprite strategAnimSprite;
         if ((addOffY = itemsSprites2[itemsSprites2Index]) >= 0 && (strategAnimSprite = StrategAnimGroup.strategAnimGroups[addOffY].animations[animationState]) != null) {
            short frameIndexByTime = AnimSprite.getFrameIndexByTime(time, loop, strategAnimSprite.numFrames, strategAnimSprite.animTime);
            switch (direction) {
               case 0:
                  gameGraphics2.drawRegion(strategAnimSprite.image, strategAnimSprite.srcX + frameIndexByTime * strategAnimSprite.frameWidth, strategAnimSprite.srcY, strategAnimSprite.frameWidth, strategAnimSprite.frameHeight, 0, cx - strategAnimSprite.baseX + programOffs[itemsSprites2Index][0], cy - strategAnimSprite.baseY + programOffs[itemsSprites2Index][1]);
                  break;
               case 1:
                  gameGraphics2.drawRegion(strategAnimSprite.image, strategAnimSprite.srcX + frameIndexByTime * strategAnimSprite.frameWidth, strategAnimSprite.srcY, strategAnimSprite.frameWidth, strategAnimSprite.frameHeight, 2, cx - strategAnimSprite.frameWidth + strategAnimSprite.baseX - programOffs[itemsSprites2Index][0], cy - strategAnimSprite.baseY + programOffs[itemsSprites2Index][1]);
            }
         }
      }

   }
}
