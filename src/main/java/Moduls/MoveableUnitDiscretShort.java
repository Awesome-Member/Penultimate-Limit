package Moduls;

import Engine.MathGame;

public class MoveableUnitDiscretShort {
   public int moveLastDirection8D = 8;
   public short cx;
   public short cy;
   public short cz = 0;
   public int moveAccumTime;
   public short cdH;
   public short cdW;
   public short cdHW;
   public short cdHH;

   public MoveableUnitDiscretShort(short cX, short cY, short cdW2, short cdH2) {
      this.cx = cX;
      this.cy = cY;
      this.moveAccumTime = 0;
      this.cdH = cdH2;
      this.cdW = cdW2;
      this.cdHW = (short)(cdW2 / 2);
      this.cdHH = (short)(cdH2 / 2);
   }

   public final void setGridPosition(short setX, short setY) {
      this.cx = setX;
      this.cy = setY;
      this.moveAccumTime = 0;
   }

   public final int animateMovement(long deltaTime, long walkSpeedTimePerPixel, PathfindingGridBlockable pathfindingGridBlockable, int direction8D, boolean withTargetPoint, int toX, int toY) {
      byte movementResult = 0;
      this.moveLastDirection8D = direction8D;
      if (direction8D != 8) {
         this.moveAccumTime = (int)((long)this.moveAccumTime + deltaTime);
         long speed = walkSpeedTimePerPixel;
         if (direction8D % 2 == 0) {
            speed = walkSpeedTimePerPixel * 1000L / 707L;
         }

         while((long)this.moveAccumTime >= speed) {
            if (!this.collidMoveStep(direction8D, pathfindingGridBlockable)) {
               movementResult = 2;
            }

            if (withTargetPoint && this.cx == toX && this.cy == toY) {
               movementResult = 1;
               break;
            }

            this.moveAccumTime = (int)((long)this.moveAccumTime - speed);
         }
      } else {
         this.moveAccumTime = 0;
      }

      return movementResult;
   }

   public final boolean collidMoveStep(int stepDirect, PathfindingGridBlockable pathfindingGridBlockable) {
      return this.collidMoveOffset(MathGame.direction8DToVect[stepDirect][0], MathGame.direction8DToVect[stepDirect][1], pathfindingGridBlockable);
   }

   public final boolean collidMoveOffset(int offX, int offY, PathfindingGridBlockable pathfindingGridBlockable) {
      boolean res = true;

      while(true) {
         while(true) {
            short deltaX;
            short deltaX2;
            short bottomY;
            while(offX != 0) {
               if (offX < 0) {
                  ++offX;
                  deltaX = (short)(this.cx - this.cdHW);
                  deltaX2 = (short)(this.cy - this.cdHH);
                  bottomY = (short)(this.cy + this.cdHH);
                  if (!pathfindingGridBlockable.isPathCellBlocked((short)(deltaX - 1), deltaX2) && !pathfindingGridBlockable.isPathCellBlocked((short)(deltaX - 1), bottomY)) {
                     --this.cx;
                  } else {
                     res = false;
                  }
               } else {
                  --offX;
                  deltaX = (short)(this.cx + this.cdHW);
                  deltaX2 = (short)(this.cy - this.cdHH);
                  bottomY = (short)(this.cy + this.cdHH);
                  if (!pathfindingGridBlockable.isPathCellBlocked((short)(deltaX + 1), deltaX2) && !pathfindingGridBlockable.isPathCellBlocked((short)(deltaX + 1), bottomY)) {
                     ++this.cx;
                  } else {
                     res = false;
                  }
               }
            }

            while(true) {
               while(true) {
                  while(offY != 0) {
                     if (offY < 0) {
                        ++offY;
                        deltaX = (short)(this.cy - this.cdHH);
                        deltaX2 = (short)(this.cx - this.cdHW);
                        bottomY = (short)(this.cx + this.cdHW);
                        if (!pathfindingGridBlockable.isPathCellBlocked(deltaX2, (short)(deltaX - 1)) && !pathfindingGridBlockable.isPathCellBlocked(bottomY, (short)(deltaX - 1))) {
                           --this.cy;
                        } else {
                           res = false;
                        }
                     } else {
                        --offY;
                        deltaX = (short)(this.cy + this.cdHH);
                        deltaX2 = (short)(this.cx - this.cdHW);
                        bottomY = (short)(this.cx + this.cdHW);
                        if (!pathfindingGridBlockable.isPathCellBlocked(deltaX2, (short)(deltaX + 1)) && !pathfindingGridBlockable.isPathCellBlocked(bottomY, (short)(deltaX + 1))) {
                           ++this.cy;
                        } else {
                           res = false;
                        }
                     }
                  }

                  return res;
               }
            }
         }
      }
   }

   public final int getMoveDirectionToTarget(int targetX, int targetY) {
      targetX -= this.cx;
      targetY -= this.cy;
      return MathGame.vectorToDirection8(targetX, targetY);
   }

   public final boolean isMoveLastDirection8D() {
      return this.moveLastDirection8D != 8;
   }

   public final boolean isMoveLastDirection8D2() {
      return this.moveLastDirection8D == 8;
   }
}
