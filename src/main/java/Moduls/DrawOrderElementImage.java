package Moduls;

import desktop.graphics.GameGraphics;

public final class DrawOrderElementImage extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementImage((short)-1, (short)-1, (short)-1, (ImageLevelAreaDesc)null));
   private ImageLevelAreaDesc image = null;

   private DrawOrderElementImage(short rx, short ry, short sortInd, ImageLevelAreaDesc imageLevelAreaDesc2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      this.image.drawHCenterBottom(gameGraphics2, this.rx, this.ry, 0);
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementImage((short)-1, (short)-1, (short)-1, (ImageLevelAreaDesc)null);
   }

   public final void returnInstance() {
      factory.returnInstance(this);
   }

   public static DrawOrderElementImage getInstance(short rx, short ry, short sortInd, ImageLevelAreaDesc imageLevelAreaDesc2) {
      DrawOrderElementImage drawOrderElementImage;
      (drawOrderElementImage = (DrawOrderElementImage)factory.getInstance()).rx = rx;
      drawOrderElementImage.ry = ry;
      drawOrderElementImage.sortInd = sortInd;
      drawOrderElementImage.image = imageLevelAreaDesc2;
      return drawOrderElementImage;
   }
}
