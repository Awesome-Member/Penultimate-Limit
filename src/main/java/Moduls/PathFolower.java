package Moduls;

import java.util.Vector;

public final class PathFolower {
   public Vector activeMovePath = new Vector();
   public int activeMovePathCurIndex = 0;

   public final boolean folow(MoveableUnitDiscretShort moveableUnitDiscretShort, long deltaTime, long walkSpeedTimePerPixel, PathfindingGridBlockable pathfindingGridBlockable) {
      boolean movedSuccessfully = true;

      boolean positionChanged;
      do {
         positionChanged = false;
         if (!this.isMovePathAbsent()) {
            short[] val = (short[])this.activeMovePath.elementAt(this.activeMovePathCurIndex);
            int moveDirectionToTarget = moveableUnitDiscretShort.getMoveDirectionToTarget(val[0], val[1]);
            int animateMovement;
            if ((animateMovement = moveableUnitDiscretShort.animateMovement(deltaTime, walkSpeedTimePerPixel, pathfindingGridBlockable, moveDirectionToTarget, true, val[0], val[1])) != 1 && moveDirectionToTarget != 8) {
               if (animateMovement == 2) {
                  movedSuccessfully = false;
                  this.disableMovePath();
               }
            } else {
               positionChanged = true;
               deltaTime = 0L;
               ++this.activeMovePathCurIndex;
            }
         } else if (deltaTime > 0L) {
            moveableUnitDiscretShort.animateMovement(deltaTime, walkSpeedTimePerPixel, pathfindingGridBlockable, 8, false, -1, -1);
         }
      } while(positionChanged);

      return movedSuccessfully;
   }

   public final boolean isMovePathAbsent() {
      return this.activeMovePath.size() == 0 || this.activeMovePathCurIndex >= this.activeMovePath.size();
   }

   public final void disableMovePath() {
      this.activeMovePathCurIndex = this.activeMovePath.size();
   }

   public final boolean buildPath(PathfindingGrid pathfindingGrid2, PathfindingGridBlockable pathfindingGridBlockable, short fromX, short fromY, short toX, short toY, short collisionWidth, short collisionHeight, short tileWidth, short tileHeight) {
      this.activeMovePath.removeAllElements();
      this.activeMovePathCurIndex = 0;
      pathfindingGrid2.buildPathPx(this.activeMovePath, pathfindingGridBlockable, fromX, fromY, toX, toY, tileWidth, tileHeight, (short)(collisionWidth + 2), (short)(collisionHeight + 2));
      return this.activeMovePath.size() > 0 && ((short[])this.activeMovePath.elementAt(this.activeMovePath.size() - 1))[0] == toX && ((short[])this.activeMovePath.elementAt(this.activeMovePath.size() - 1))[1] == toY;
   }
}
