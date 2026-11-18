package Engine;

import Moduls.ImageLevelAreaDesc;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class AnimSprite {
   private int regx;
   private int regy;
   private int regw;
   private int regh;
   public GameImage Source;
   public int sw;
   public int sh;
   public int AnimTime;
   public int numFrames;
   private int TimeAccum;
   public int CurFrameI;
   private boolean WasLoop;
   public boolean WasFrame;
   private String imgName = "";
   public int swHalf;
   public int shHalf;

   public AnimSprite(int FrameWidth, int animTime, String SourceFile) {
      try {
         this.Source = GameImage.createImage(SourceFile);
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      this.sw = FrameWidth;
      this.sh = this.Source.getHeight();
      this.numFrames = this.Source.getWidth() / FrameWidth;
      this.AnimTime = animTime;
      this.TimeAccum = 0;
      this.CurFrameI = 0;
      this.WasLoop = false;
      this.WasFrame = false;
      this.regx = 0;
      this.regy = 0;
      this.regw = this.Source.getWidth();
      this.regh = this.Source.getHeight();
      this.swHalf = this.sw / 2;
      this.shHalf = this.sh / 2;
   }

   public AnimSprite(ImageLevelAreaDesc imageLevelAreaDesc2) {
      this.Source = imageLevelAreaDesc2.img;
      this.imgName = imageLevelAreaDesc2.imgName;
      this.regx = imageLevelAreaDesc2.rx;
      this.regy = imageLevelAreaDesc2.ry;
      this.regw = imageLevelAreaDesc2.rw;
      this.regh = imageLevelAreaDesc2.rh;
      this.sw = this.regw / imageLevelAreaDesc2.numFrames;
      this.sh = this.regh;
      this.numFrames = imageLevelAreaDesc2.numFrames;
      this.AnimTime = imageLevelAreaDesc2.frameTime;
      this.TimeAccum = 0;
      this.CurFrameI = 0;
      this.WasLoop = false;
      this.WasFrame = false;
      this.swHalf = this.sw / 2;
      this.shHalf = this.sh / 2;
   }

   public static short getFrameIndexByTime(long elapsedTime, boolean loop, int frameCount, int frameDuration) {
      return (short)((int)(loop ? elapsedTime / (long)frameDuration % (long)frameCount : (elapsedTime >= (long)(frameCount * frameDuration) ? (long)(frameCount - 1) : elapsedTime / (long)frameDuration)));
   }

   public final void NextAnimStep() {
      this.NextAnimStep(Mystery.FrameTimeI);
   }

   private void NextAnimStep(long frameTime) {
      try {
         this.WasFrame = false;
         this.WasLoop = false;

         for(this.TimeAccum = (int)((long)this.TimeAccum + frameTime); this.TimeAccum >= this.AnimTime; this.TimeAccum -= this.AnimTime) {
            ++this.CurFrameI;
            this.WasFrame = true;
            if (this.CurFrameI >= this.numFrames) {
               this.WasLoop = true;
               this.CurFrameI -= this.numFrames;
            }
         }

      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final boolean isLastFrameByTime(int time) {
      return time / this.AnimTime >= this.numFrames;
   }

   public final void advanceAnimation(AnimSost animSost, int elapsedTime) {
      try {
         this.WasFrame = false;
         this.WasLoop = false;

         for(animSost.TimeAccum += elapsedTime; animSost.TimeAccum >= this.AnimTime; animSost.TimeAccum -= this.AnimTime) {
            ++animSost.CurFrameI;
            animSost.WasFrame = true;
            if (animSost.CurFrameI >= this.numFrames) {
               animSost.WasLoop = true;
               animSost.CurFrameI -= this.numFrames;
            }
         }

      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final void advanceAnimationLoop(AnimSost animSost, int elapsedTime) {
      try {
         animSost.TimeAccum += elapsedTime;
         if (!animSost.WasLoop) {
            this.WasFrame = false;

            for(this.WasLoop = false; animSost.TimeAccum >= this.AnimTime; animSost.TimeAccum -= this.AnimTime) {
               ++animSost.CurFrameI;
               animSost.WasFrame = true;
               if (animSost.CurFrameI >= this.numFrames) {
                  animSost.WasLoop = true;
                  animSost.CurFrameI = this.numFrames - 1;
               }
            }

         }
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final void draw(GameGraphics gameGraphics2, int cx, int cy) {
      try {
         gameGraphics2.drawRegion(this.Source, this.regx + Math.min(this.numFrames - 1, this.CurFrameI) * this.sw, this.regy, this.sw, this.sh, 0, cx, cy);
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final void draw(GameGraphics gameGraphics2, int Frame, int cx, int cy) {
      this.draw(gameGraphics2, 0, cx, cy, false);
   }

   public final void draw(GameGraphics gameGraphics2, int Frame, int cx, int cy, boolean isMirror) {
      try {
         gameGraphics2.drawRegion(this.Source, this.regx + Math.min(this.numFrames - 1, Frame) * this.sw, this.regy, this.sw, this.sh, isMirror ? 2 : 0, cx, cy);
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final void draw(GameGraphics gameGraphics2, AnimSost animSost, int cx, int cy) {
      this.draw(gameGraphics2, animSost, cx, cy, false);
   }

   private void draw(GameGraphics gameGraphics2, AnimSost animSost, int cx, int cy, boolean isMirror) {
      try {
         gameGraphics2.drawRegion(this.Source, this.regx + Math.min(this.numFrames - 1, animSost.CurFrameI) * this.sw, this.regy, this.sw, this.sh, 0, cx, cy);
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }

   public final void draw(GameGraphics gameGraphics2, long time, boolean loop, int cx, int cy, boolean isMirror) {
      try {
         short frameIndexByTime = getFrameIndexByTime(time, false, this.numFrames, this.AnimTime);
         gameGraphics2.drawRegion(this.Source, this.regx + frameIndexByTime * this.sw, this.regy, this.sw, this.sh, isMirror ? 2 : 0, cx, cy);
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new RuntimeException("Error in AnimSprite " + this.imgName);
      }
   }
}
