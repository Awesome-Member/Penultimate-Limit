package battlepck.client;

import Base.ImagesGlobal;
import Engine.AnimSost;
import Engine.AnimSprite;
import Moduls.DrawOrderElement;
import desktop.graphics.GameGraphics;

public final class BattleDrawOrder extends DrawOrderElement {
   public AnimSost aSost = new AnimSost(0);
   public int sprite;
   public int targetInd;

   public BattleDrawOrder(int sprite2, int targetind, int targX, int targY) {
      super((short)targX, (short)targY, (short)(targY - 1000));
      this.sprite = sprite2;
      this.targetInd = targetind;
   }

   public final void draw(GameGraphics gameGraphics2) {
      AnimSprite animSprite;
      (animSprite = ImagesGlobal.animClientSprites[this.sprite]).draw(gameGraphics2, this.aSost, this.rx - animSprite.swHalf, this.ry - animSprite.shHalf);
   }

   public final DrawOrderElement newInstance() {
      return null;
   }

   public final void returnInstance() {
   }
}
