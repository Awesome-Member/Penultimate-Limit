package Moduls;

final class PathfindingCellState {
   public boolean pathSearchFlag;
   public boolean pathStepFlag;
   public short tileX;
   public short tileY;

   public PathfindingCellState(boolean pathSearchFlag2, boolean pathStepFlag2, short tileX2, short tileY2) {
      this.pathSearchFlag = pathSearchFlag2;
      this.pathStepFlag = pathStepFlag2;
      this.tileX = tileX2;
      this.tileY = tileY2;
   }
}
