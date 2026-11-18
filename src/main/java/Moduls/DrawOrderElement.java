package Moduls;

import desktop.graphics.GameGraphics;

public abstract class DrawOrderElement {
   public short rx;
   public short ry;
   public short sortInd;
   public DrawOrderElement next = null;
   public DrawOrderElement prev = null;

   public DrawOrderElement(short rx2, short ry2, short sortInd2) {
      this.rx = rx2;
      this.ry = ry2;
      this.sortInd = sortInd2;
   }

   public final boolean isGreater(DrawOrderElement drawOrderElement) {
      return this.sortInd > drawOrderElement.sortInd || this.sortInd == drawOrderElement.sortInd && this.rx > drawOrderElement.rx;
   }

   public abstract void draw(GameGraphics gameGraphics2);

   public abstract DrawOrderElement newInstance();

   public abstract void returnInstance();

   public boolean isVisible() {
      return false;
   }

   public int getRx() {
      return this.rx;
   }

   public int getRx2() {
      return this.rx;
   }

   public int getVisualFrameTop() {
      return this.ry;
   }

   public int getVisualFrameBottom() {
      return this.ry;
   }
}
