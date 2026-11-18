package battlepck.client;

import desktop.graphics.GameImage;

public final class BattleDrawBuffEffect {
   public GameImage image;
   public int baseYOffset;
   public int baseXOffset;
   public int animVertOffset;
   public int reqTime;
   public int curTime;
   public int type = 0;

   public BattleDrawBuffEffect(GameImage gameImage, int baseXOffset2, int baseYOffset2) {
      this.image = gameImage;
      this.baseYOffset = baseYOffset2;
      this.baseXOffset = baseXOffset2;
      this.animVertOffset = 8;
      this.reqTime = 1200;
      this.curTime = 0;
   }
}
