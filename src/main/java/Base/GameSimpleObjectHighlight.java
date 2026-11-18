package Base;

import Moduls.DrawOrderElement;
import Moduls.ImageLevelAreaDesc;
import desktop.graphics.GameGraphics;

public final class GameSimpleObjectHighlight extends GameSimpleObject {
   private int type = 2;
   private int paddingLeft = 0;
   private int paddingRight = 0;
   private int paddingBottom = 0;
   private int paddingTop = 0;

   public GameSimpleObjectHighlight(short rx, short ry, short sortInd, int paddingLeft2, int paddingRight2, int paddingBottom2, int paddingTop2, int type2) {
      super(rx, ry, sortInd);
      this.paddingLeft = paddingLeft2;
      this.paddingRight = paddingRight2;
      this.paddingBottom = paddingBottom2;
      this.paddingTop = paddingTop2;
      this.type = type2;
   }

   public final boolean update(int deltaTime) {
      return false;
   }

   public final void draw(GameGraphics gameGraphics2) {
      ImageLevelAreaDesc imageLevelAreaDesc2;
      ImageLevelAreaDesc imageLevelAreaDesc3;
      switch (this.type) {
         case 2:
            imageLevelAreaDesc2 = ImagesGlobal.addingAnimSprites[35];
            imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[38];
            break;
         case 3:
            imageLevelAreaDesc2 = ImagesGlobal.addingAnimSprites[34];
            imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[37];
            break;
         case 4:
            imageLevelAreaDesc2 = ImagesGlobal.addingAnimSprites[33];
            imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[36];
            break;
         default:
            imageLevelAreaDesc2 = ImagesGlobal.addingAnimSprites[35];
            imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[38];
      }

      imageLevelAreaDesc2.draw(gameGraphics2, this.rx - imageLevelAreaDesc2.getAdding(0) - this.paddingLeft, this.ry - this.paddingTop - imageLevelAreaDesc2.getAdding(1), 0);
      imageLevelAreaDesc2.draw(gameGraphics2, this.rx - (imageLevelAreaDesc2.frW - imageLevelAreaDesc2.getAdding(0)) + this.paddingRight, this.ry - this.paddingTop - imageLevelAreaDesc2.getAdding(1), 2);
      imageLevelAreaDesc3.draw(gameGraphics2, this.rx - imageLevelAreaDesc3.getAdding(0) - this.paddingLeft, this.ry + this.paddingBottom - imageLevelAreaDesc3.getAdding(1), 0);
      imageLevelAreaDesc3.draw(gameGraphics2, this.rx - (imageLevelAreaDesc3.frW - imageLevelAreaDesc3.getAdding(0)) + this.paddingRight, this.ry + this.paddingBottom - imageLevelAreaDesc3.getAdding(1), 2);
   }

   public final DrawOrderElement newInstance() {
      return null;
   }

   public final void returnInstance() {
   }
}
