package Moduls;

import desktop.graphics.GameGraphics;

public final class DrawOrderElementDecor extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementDecor((short)-1, (short)-1, (short)-1, (Decor)null, false));
   private Decor decor = null;
   private boolean drawTransporant = false;

   private DrawOrderElementDecor(short rx, short ry, short sortInd, Decor decor2, boolean drawTransporant2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void setDrawTransporant(boolean drawTransporant2) {
      this.drawTransporant = true;
   }

   public final void draw(GameGraphics gameGraphics2) {
      if (this.drawTransporant) {
         GameGraphics gameGraphics3 = gameGraphics2;
         int drawX = this.rx - this.decor.w2;
         int drawY = this.ry - this.decor.Offset;
         int animationFrame = this.decor.Self.CurFrameI < this.decor.imageLevelAreaDesc.numFrames ? this.decor.Self.CurFrameI : 0;
         boolean unusedProgressMarker = false;
         int spriteFrame = animationFrame;
         int spriteY = drawY;
         int spriteX = drawX;
         GameGraphics gameGraphics4 = gameGraphics3;
         ImageLevelAreaDesc imageLevelAreaDesc2 = this.decor.imageLevelAreaDesc;
         gameGraphics4.drawRegion(imageLevelAreaDesc2.img, imageLevelAreaDesc2.rx + spriteFrame * imageLevelAreaDesc2.frW, imageLevelAreaDesc2.ry, imageLevelAreaDesc2.frW, imageLevelAreaDesc2.rh, 0, spriteX, spriteY);
      } else {
         this.decor.Self.draw(gameGraphics2, this.rx - this.decor.w2, this.ry - this.decor.Offset);
      }
   }

   public final boolean isVisible() {
      return true;
   }

   public final int getRx() {
      return this.rx - this.decor.w2;
   }

   public final int getRx2() {
      return this.rx + this.decor.w2;
   }

   public final int getVisualFrameTop() {
      return this.ry - this.decor.Offset;
   }

   public final int getVisualFrameBottom() {
      return this.ry + this.decor.height - this.decor.Offset;
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementDecor((short)-1, (short)-1, (short)-1, (Decor)null, false);
   }

   public final void returnInstance() {
      this.decor = null;
      factory.returnInstance(this);
   }

   public static DrawOrderElementDecor getInstance(short rx, short ry, short sortInd, Decor decor2, boolean drawTransporant2) {
      DrawOrderElementDecor drawOrderElementDecor;
      (drawOrderElementDecor = (DrawOrderElementDecor)factory.getInstance()).rx = rx;
      drawOrderElementDecor.ry = ry;
      drawOrderElementDecor.sortInd = sortInd;
      drawOrderElementDecor.decor = decor2;
      drawOrderElementDecor.drawTransporant = false;
      return drawOrderElementDecor;
   }
}
