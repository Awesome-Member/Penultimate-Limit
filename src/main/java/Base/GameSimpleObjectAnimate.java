package Base;

import Engine.AnimSprite;
import Moduls.DrawOrderElement;
import desktop.graphics.GameGraphics;

public final class GameSimpleObjectAnimate extends GameSimpleObject {
   private AnimSprite sprite;
   private int accTime = 0;

   public GameSimpleObjectAnimate(short rx, short ry, short sortInd, AnimSprite animSprite) {
      super(rx, ry, sortInd);
      this.sprite = animSprite;
   }

   public final boolean update(int deltaTime) {
      if (this.sprite.isLastFrameByTime(this.accTime)) {
         return true;
      } else {
         this.accTime += deltaTime;
         return false;
      }
   }

   public final void draw(GameGraphics gameGraphics2) {
      this.sprite.draw(gameGraphics2, (long)this.accTime, false, this.rx, this.ry, false);
   }

   public final DrawOrderElement newInstance() {
      return new GameSimpleObjectAnimate((short)-1, (short)-1, (short)-1, (AnimSprite)null);
   }

   public final void returnInstance() {
   }
}
