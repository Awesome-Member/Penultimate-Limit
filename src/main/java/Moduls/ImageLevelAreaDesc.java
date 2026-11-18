package Moduls;

import Base.ImagesCach;
import desktop.PlatformRuntime;
import java.io.DataInputStream;
import java.io.IOException;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class ImageLevelAreaDesc {
   public String imgName;
   public int frW;
   public GameImage img;
   public int rx;
   public int ry;
   public int rw;
   public int rh;
   public int numFrames;
   public int frameTime;
   public short centerOffX;
   public short centerOffY;
   public int[] adding;

   public ImageLevelAreaDesc(DataInputStream dataInputStream) {
      try {
      this.imgName = dataInputStream.readUTF();
      this.img = ImagesCach.getImage(this.imgName);
      this.rx = dataInputStream.readInt();
      this.ry = dataInputStream.readInt();
      this.rw = dataInputStream.readInt();
      this.rh = dataInputStream.readInt();
      this.numFrames = dataInputStream.readInt();
      this.frameTime = dataInputStream.readInt();
      if (this.rx + this.rw > this.img.getWidth()) {
         throw new IOException(this.imgName + " image is wrong width params rx = " + this.rx + " rw = " + this.rw + " img.getWidth() = " + this.img.getWidth());
      } else if (this.ry + this.rh > this.img.getHeight()) {
         throw new IOException(this.imgName + " image is wrong height params ry = " + this.ry + " rh = " + this.rh + " img.getHeight() = " + this.img.getHeight());
      } else {
         this.frW = this.rw / this.numFrames;
         this.adding = new int[dataInputStream.readInt()];

         for(int i = 0; i < this.adding.length; ++i) {
            this.adding[i] = dataInputStream.readInt();
         }

         this.centerOffX = (short)(this.frW / 2);
         this.centerOffY = (short)(this.rh / 2);
      }
   
      } catch (IOException exception) {
         ImageLevelAreaDesc.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public final void draw(GameGraphics gameGraphics2, int x, int y, int transform) {
      gameGraphics2.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, transform, x, y);
   }

   public final void drawCenter(GameGraphics g, int x, int y, int transform) {
      g.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, 0, x - this.centerOffX, y - this.centerOffY);
   }

   public final void drawVCenterLeft(GameGraphics g, int x, int y, int transform) {
      g.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, 0, x, y - this.centerOffY);
   }

   public final void drawVCenterRight(GameGraphics g, int x, int y, int transform) {
      g.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, 0, x - this.frW, y - this.centerOffY);
   }

   public final void drawHCenterBottom(GameGraphics g, int x, int y, int transform) {
      g.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, 0, x - this.centerOffX, y - this.rh);
   }

   public final void drawHCenterTop(GameGraphics g, int x, int y, int transform) {
      g.drawRegion(this.img, this.rx, this.ry, this.frW, this.rh, 0, x - this.centerOffX, y);
   }

   public final int getAdding(int ind) {
      return this.adding.length - 1 >= ind ? this.adding[ind] : 0;
   }

   public final boolean isTransporant(int x, int y, int frame, boolean isMirror) {
      if (x >= 0 && y >= 0 && x < this.frW && y < this.rh) {
         try {
            PlatformRuntime.getInstance();
            return PlatformRuntime.isPixelTransparent(this.img, this.rx + 0 * this.frW + (isMirror ? this.frW - 1 - x : x), this.ry + y);
         } catch (Exception exception) {
            exception.printStackTrace();
            return true;
         }
      } else {
         return true;
      }
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
}
