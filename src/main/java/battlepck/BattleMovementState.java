package battlepck;

final class BattleMovementState {
   public boolean movementPending;
   public boolean multipleMoves;
   public int deltaX;
   public int deltaY;

   private Battle battle;

   BattleMovementState(Battle battle2) {
      this.battle = battle2;
      this.movementPending = false;
      this.multipleMoves = false;
      this.deltaX = 0;
      this.deltaY = 0;
   }

   public final void updateMovement(int deltaX2, int deltaY2, boolean constrainRange, long relativeX, long relativeY, long squaredRange, int overlapDepth) {
      this.deltaX = deltaX2;
      this.deltaY = deltaY2;
      if (constrainRange && (relativeX + (long)this.deltaX) * (relativeX + (long)this.deltaX) + (relativeY + (long)this.deltaY) * (relativeY + (long)this.deltaY) > squaredRange) {
         this.deltaX = 0;
         this.deltaY = 0;
      } else {
         this.movementPending = true;
         if (overlapDepth > 1) {
            this.multipleMoves = true;
            return;
         }
      }

   }
}
