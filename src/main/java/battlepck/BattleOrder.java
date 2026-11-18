package battlepck;

public final class BattleOrder {
   public byte orderType;
   public int bushInd;
   public int unitInd;
   public int targetInd;
   public int targetX;
   public int targetY;
   public int extraData;

   public BattleOrder(int unitInd2, int targetInd2, int targetX2, int targetY2, byte orderType2, int bushInd2, int ignoredExtraData) {
      this.unitInd = unitInd2;
      this.targetInd = targetInd2;
      this.targetX = targetX2;
      this.targetY = targetY2;
      this.orderType = orderType2;
      this.bushInd = bushInd2;
      this.extraData = 0;
   }
}
