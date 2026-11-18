package Moduls;

import Engine.MathGame;
import java.util.Vector;

public final class PathfindingGrid {
   private static final int[][][] PARAMS_FOR_CHECK_COLLISION_FOR_DIAGONAL_MOVE = new int[][][]{{{-1, -1, 0, 1, 0, 1, -1}, {1, 1, 1, -1, 0, 1, -1}, {-1, -1, 0, 1, 1, 1, -1}, {1, 1, 1, -1, 1, 1, -1}}, new int[0][], {{-1, 1, 0, 1, 0, 1, 1}, {1, -1, 1, 1, 0, 1, 1}, {1, -1, 1, 1, 1, 1, 1}, {-1, 1, 0, 1, 1, 1, 1}}, new int[0][], {{-1, -1, 1, 1, 0, -1, 1}, {1, 1, 0, -1, 0, -1, 1}, {-1, -1, 1, 1, 1, -1, 1}, {1, 1, 0, -1, 1, -1, 1}}, new int[0][], {{1, -1, 0, -1, 0, -1, -1}, {-1, 1, 1, -1, 0, -1, -1}, {1, -1, 0, -1, 1, -1, -1}, {-1, 1, 1, -1, 1, -1, -1}}, new int[0][], new int[0][]};
   private short[] grid;
   private short[] next;
   private int sizeX;
   private int sizeY;

   public PathfindingGrid(int sizeX2, int sizeY2) {
      this.grid = new short[sizeX2 * sizeY2];
      this.next = new short[sizeX2 * sizeY2 << 1];
      this.sizeX = sizeX2;
      this.sizeY = sizeY2;
   }

   public final synchronized void buildPathPx(Vector toVector, PathfindingGridBlockable blocks, short fromX, short fromY, short toX, short toY, short tileWidth, short tileHeight, short collisionWidth, short collisionHeight) {
      if (toX < 0) {
         toX = 0;
      }

      if (toY < 0) {
         toY = 0;
      }

      if (toX >= tileWidth * this.sizeX) {
         toX = (short)(tileWidth * this.sizeX - 1);
      }

      if (toY >= tileHeight * this.sizeY) {
         toY = (short)(tileHeight * this.sizeY - 1);
      }

      int tileX = (short)((int)MathGame.floorDivide((long)toX, (long)tileWidth));
      short tileY = (short)((int)MathGame.floorDivide((long)toY, (long)tileHeight));
      short fromTileX = (short)((int)MathGame.floorDivide((long)fromX, (long)tileWidth));
      short fromTileY = (short)((int)MathGame.floorDivide((long)fromY, (long)tileHeight));
      short[][] path = this.buildPath((PathfindingGridBlockable)blocks, fromTileX, fromTileY, (short)tileX, tileY);
      boolean successPath = fromTileX == tileX && fromTileY == tileY || path.length > 0 && tileX == path[path.length - 1][0] && tileY == path[path.length - 1][1];
      int tileHalfWidth = tileWidth / 2;
      int tileHalfHeight = tileHeight / 2;
      collisionWidth /= 2;
      collisionHeight /= 2;
      if (!successPath) {
         int tileDelta;
         if (path.length > 0) {
            if ((tileDelta = tileX - path[path.length - 1][0]) > 0) {
               toX = (short)((path[path.length - 1][0] + 1) * tileWidth - collisionWidth - 1);
            } else if (tileDelta < 0) {
               toX = (short)(path[path.length - 1][0] * tileWidth + collisionWidth);
            }

            if ((tileDelta = tileY - path[path.length - 1][1]) > 0) {
               toY = (short)((path[path.length - 1][1] + 1) * tileHeight - collisionHeight - 1);
            } else if (tileDelta < 0) {
               toY = (short)(path[path.length - 1][1] * tileHeight + collisionHeight);
            }
         } else {
            if ((tileDelta = tileX - fromTileX) > 0) {
               toX = (short)((fromTileX + 1) * tileWidth - collisionWidth - 1);
            } else if (tileDelta < 0) {
               toX = (short)(fromTileX * tileWidth + collisionWidth);
            }

            if ((tileDelta = tileY - fromTileY) > 0) {
               toY = (short)((fromTileY + 1) * tileHeight - collisionHeight - 1);
            } else if (tileDelta < 0) {
               toY = (short)(fromTileY * tileHeight + collisionHeight);
            }
         }

         tileX = (short)((int)MathGame.floorDivide((long)toX, (long)tileWidth));
         tileY = (short)((int)MathGame.floorDivide((long)toY, (long)tileHeight));
      }

      short collisionTileX;
      if ((collisionTileX = (short)((int)MathGame.floorDivide((long)(toX + collisionWidth), (long)tileWidth))) != tileX && blocks.isBlockTl(collisionTileX, tileY)) {
         tileX = (short)((int)MathGame.floorDivide((long)(toX = (short)((tileX + 1) * tileWidth - 1 - collisionWidth)), (long)tileWidth));
      }

      if ((collisionTileX = (short)((int)MathGame.floorDivide((long)(toX - collisionWidth), (long)tileWidth))) != tileX && blocks.isBlockTl(collisionTileX, tileY)) {
         tileX = (short)((int)MathGame.floorDivide((long)(toX = (short)(tileX * tileWidth + collisionWidth)), (long)tileWidth));
      }

      if ((collisionTileX = (short)((int)MathGame.floorDivide((long)(toY + collisionHeight), (long)tileHeight))) != tileY && blocks.isBlockTl((short)tileX, collisionTileX)) {
         tileY = (short)((int)MathGame.floorDivide((long)(toY = (short)((tileY + 1) * tileHeight - 1 - collisionHeight)), (long)tileHeight));
      }

      if ((collisionTileX = (short)((int)MathGame.floorDivide((long)(toY - collisionHeight), (long)tileHeight))) != tileY && blocks.isBlockTl((short)tileX, collisionTileX)) {
         tileY = (short)((int)MathGame.floorDivide((long)(toY = (short)(tileY * tileHeight + collisionHeight)), (long)tileHeight));
      }

      collisionTileX = (short)((int)MathGame.floorDivide((long)(toX + collisionWidth), (long)tileWidth));
      short previousPathX = (short)((int)MathGame.floorDivide((long)(toY + collisionHeight), (long)tileHeight));
      if (blocks.isBlockTl(collisionTileX, previousPathX)) {
         if (collisionTileX == tileX) {
            tileY = (short)((int)MathGame.floorDivide((long)(toY = (short)((tileY + 1) * tileHeight - 1 - collisionHeight)), (long)tileHeight));
         } else if (previousPathX == tileY) {
            tileX = (short)((int)MathGame.floorDivide((long)(toX = (short)((tileX + 1) * tileWidth - 1 - collisionWidth)), (long)tileWidth));
         } else {
            collisionTileX = (short)((tileX + 1) * tileWidth - 1 - collisionWidth);
            previousPathX = (short)((tileY + 1) * tileHeight - 1 - collisionHeight);
            if (Math.abs(toX - collisionTileX) <= Math.abs(toY - previousPathX)) {
               toX = collisionTileX;
               tileX = (short)((int)MathGame.floorDivide((long)collisionTileX, (long)tileWidth));
            } else {
               toY = previousPathX;
               tileY = (short)((int)MathGame.floorDivide((long)previousPathX, (long)tileHeight));
            }
         }
      }

      collisionTileX = (short)((int)MathGame.floorDivide((long)(toX - collisionWidth), (long)tileWidth));
      previousPathX = (short)((int)MathGame.floorDivide((long)(toY + collisionHeight), (long)tileHeight));
      if (blocks.isBlockTl(collisionTileX, previousPathX)) {
         if (collisionTileX == tileX) {
            tileY = (short)((int)MathGame.floorDivide((long)(toY = (short)((tileY + 1) * tileHeight - 1 - collisionHeight)), (long)tileHeight));
         } else if (previousPathX == tileY) {
            tileX = (short)((int)MathGame.floorDivide((long)(toX = (short)(tileX * tileWidth + collisionWidth)), (long)tileWidth));
         } else {
            collisionTileX = (short)(tileX * tileWidth + collisionWidth);
            previousPathX = (short)((tileY + 1) * tileHeight - 1 - collisionHeight);
            if (Math.abs(toX - collisionTileX) <= Math.abs(toY - previousPathX)) {
               toX = collisionTileX;
               tileX = (short)((int)MathGame.floorDivide((long)collisionTileX, (long)tileWidth));
            } else {
               toY = previousPathX;
               tileY = (short)((int)MathGame.floorDivide((long)previousPathX, (long)tileHeight));
            }
         }
      }

      collisionTileX = (short)((int)MathGame.floorDivide((long)(toX - collisionWidth), (long)tileWidth));
      previousPathX = (short)((int)MathGame.floorDivide((long)(toY - collisionHeight), (long)tileHeight));
      if (blocks.isBlockTl(collisionTileX, previousPathX)) {
         if (collisionTileX == tileX) {
            tileY = (short)((int)MathGame.floorDivide((long)(toY = (short)(tileY * tileHeight + collisionHeight)), (long)tileHeight));
         } else if (previousPathX == tileY) {
            tileX = (short)((int)MathGame.floorDivide((long)(toX = (short)(tileX * tileWidth + collisionWidth)), (long)tileWidth));
         } else {
            collisionTileX = (short)(tileX * tileWidth + collisionWidth);
            previousPathX = (short)(tileY * tileHeight + collisionHeight);
            if (Math.abs(toX - collisionTileX) <= Math.abs(toY - previousPathX)) {
               toX = collisionTileX;
               tileX = (short)((int)MathGame.floorDivide((long)collisionTileX, (long)tileWidth));
            } else {
               toY = previousPathX;
               tileY = (short)((int)MathGame.floorDivide((long)previousPathX, (long)tileHeight));
            }
         }
      }

      collisionTileX = (short)((int)MathGame.floorDivide((long)(toX + collisionWidth), (long)tileWidth));
      previousPathX = (short)((int)MathGame.floorDivide((long)(toY - collisionHeight), (long)tileHeight));
      if (blocks.isBlockTl(collisionTileX, previousPathX)) {
         if (collisionTileX == tileX) {
            MathGame.floorDivide((long)(toY = (short)(tileY * tileHeight + collisionHeight)), (long)tileHeight);
         } else if (previousPathX == tileY) {
            MathGame.floorDivide((long)(toX = (short)((tileX + 1) * tileWidth - 1 - collisionWidth)), (long)tileWidth);
         } else {
            tileX = (short)((tileX + 1) * tileWidth - 1 - collisionWidth);
            tileY = (short)(tileY * tileHeight + collisionHeight);
            if (Math.abs(toX - tileX) <= Math.abs(toY - tileY)) {
               toX = (short)tileX;
               MathGame.floorDivide((long)tileX, (long)tileWidth);
            } else {
               toY = tileY;
               MathGame.floorDivide((long)tileY, (long)tileHeight);
            }
         }
      }

      tileX = -1;
      tileY = fromTileX;
      collisionTileX = fromTileY;
      previousPathX = fromX;
      short previousPathY = fromY;
      short previousTileX = fromTileX;
      short previousTileY = fromTileY;
      boolean wasWallRight = false;
      boolean wasWallLeft = false;
      boolean wasWallDown = false;
      boolean wasWallUp = false;

      short pathElementX;
      int pathElementY;
      short collisionOffsetY;
      for(int lengthIndex = 0; lengthIndex < path.length; ++lengthIndex) {
         pathElementX = path[lengthIndex][0];
         pathElementY = path[lengthIndex][1];
         int nextDirection = MathGame.vectorToDirection8(pathElementX - previousTileX, pathElementY - previousTileY);
         int collisionCheckIndex;
         int offsetPixelsX;
         int collisionPixelsX;
         int collisionPixelsY;
         if (tileX != -1 && nextDirection != tileX) {
            label524:
            switch (tileX) {
               case 0:
               case 2:
               case 4:
               case 6:
                  collisionCheckIndex = previousPathX;
                  offsetPixelsX = previousPathY;
                  previousPathX = (short)(previousPathX + (previousTileX - tileY) * tileWidth);
                  previousPathY = (short)(previousPathY + (previousTileY - collisionTileX) * tileHeight);
                  collisionPixelsX = previousTileX * tileWidth;
                  collisionPixelsY = previousTileY * tileHeight;
                  int[] directionVector = MathGame.direction8DToNormalVector(tileX);
                  short[] correctedPoint;
                  previousPathX = (correctedPoint = cutLineByPoint((short)(collisionPixelsX + tileHalfWidth - (tileHalfWidth - collisionWidth - (directionVector[0] < 0 ? 1 : 0)) * directionVector[0]), (short)(collisionPixelsY + tileHalfHeight - (tileHalfHeight - collisionHeight - (directionVector[1] < 0 ? 1 : 0)) * directionVector[1]), collisionCheckIndex, offsetPixelsX, previousPathX, previousPathY, nextDirection == 7 || nextDirection == 3, nextDirection == 5 || nextDirection == 1, collisionPixelsX + collisionWidth, collisionPixelsY + collisionHeight, collisionPixelsX + tileWidth - collisionWidth - 1, collisionPixelsY + tileHeight - collisionHeight - 1))[0];
                  previousPathY = correctedPoint[1];
                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                  break;
               case 1:
                  switch (nextDirection) {
                     case 0:
                     case 2:
                        previousPathX = (short)(previousTileX * tileWidth + collisionWidth);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                     case 1:
                     case 4:
                     case 5:
                     case 6:
                     default:
                        break label524;
                     case 3:
                        previousPathX = (short)(previousTileX * tileWidth + collisionWidth);
                        previousPathY = (short)((previousTileY + 1) * tileHeight - 1 - collisionHeight);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                        break label524;
                     case 7:
                        previousPathX = (short)(previousTileX * tileWidth + collisionWidth);
                        previousPathY = (short)(previousTileY * tileHeight + collisionHeight);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                        break label524;
                  }
               case 3:
                  switch (nextDirection) {
                     case 1:
                        previousPathX = (short)((previousTileX + 1) * tileWidth - 1 - collisionWidth);
                     case 2:
                     case 4:
                        break;
                     case 3:
                     default:
                        break label524;
                     case 5:
                        previousPathX = (short)(previousTileX * tileWidth + collisionWidth);
                  }

                  previousPathY = (short)(previousTileY * tileHeight + collisionHeight);
                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                  break;
               case 5:
                  switch (nextDirection) {
                     case 3:
                        previousPathX = (short)((previousTileX + 1) * tileWidth - 1 - collisionWidth);
                        previousPathY = (short)((previousTileY + 1) * tileHeight - 1 - collisionHeight);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                        break label524;
                     case 4:
                     case 6:
                        previousPathX = (short)((previousTileX + 1) * tileWidth - 1 - collisionWidth);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                     case 5:
                     default:
                        break label524;
                     case 7:
                        previousPathX = (short)((previousTileX + 1) * tileWidth - 1 - collisionWidth);
                        previousPathY = (short)(previousTileY * tileHeight + collisionHeight);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                        break label524;
                  }
               case 7:
                  label516: {
                     switch (nextDirection) {
                        case 0:
                        case 6:
                           break;
                        case 1:
                           previousPathX = (short)((previousTileX + 1) * tileWidth - 1 - collisionWidth);
                           break;
                        case 2:
                        case 3:
                        case 4:
                        default:
                           break label516;
                        case 5:
                           previousPathX = (short)(previousTileX * tileWidth + collisionWidth);
                     }

                     previousPathY = (short)((previousTileY + 1) * tileHeight - 1 - collisionHeight);
                     this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                  }
            }

            wasWallRight = false;
            wasWallLeft = false;
            wasWallDown = false;
            wasWallUp = false;
            tileY = previousTileX;
            collisionTileX = previousTileY;
         }

         short collisionBaseTileX;
         int collisionDeltaY;
         label534:
         switch (nextDirection) {
            case 0:
            case 2:
            case 4:
            case 6:
               collisionCheckIndex = 0;

               while(true) {
                  if (collisionCheckIndex >= PARAMS_FOR_CHECK_COLLISION_FOR_DIAGONAL_MOVE[nextDirection].length) {
                     break label534;
                  }

                  int[] collisionParameters;
                  collisionBaseTileX = (collisionParameters = PARAMS_FOR_CHECK_COLLISION_FOR_DIAGONAL_MOVE[nextDirection][collisionCheckIndex])[4] == 0 ? previousTileX : pathElementX;
                  short collisionBaseTileY = (short)(collisionParameters[4] == 0 ? previousTileY : pathElementY);
                  tileX = collisionParameters[0];
                  collisionDeltaY = collisionParameters[1];
                  boolean neighborWithinWidth = tileX < 0 ? collisionBaseTileX > 0 : collisionBaseTileX + 1 < this.sizeX;
                  boolean neighborWithinHeight = collisionDeltaY < 0 ? collisionBaseTileY > 0 : collisionBaseTileY + 1 < this.sizeY;
                  if (neighborWithinWidth && neighborWithinHeight && blocks.isBlockTl((short)(collisionBaseTileX + tileX), (short)(collisionBaseTileY + collisionDeltaY))) {
                     collisionPixelsX = (collisionBaseTileX + (tileX < 0 ? 0 : 1)) * tileWidth - (tileX < 0 ? 0 : 1);
                     collisionPixelsY = (collisionBaseTileY + (collisionDeltaY < 0 ? 0 : 1)) * tileHeight - (collisionDeltaY < 0 ? 0 : 1);
                     tileX = previousPathX + collisionWidth * tileX;
                     collisionDeltaY = previousPathY + collisionHeight * collisionDeltaY;
                     tileX = (collisionPixelsX - tileX) * collisionParameters[5];
                     collisionPixelsX = (collisionPixelsY - collisionDeltaY) * collisionParameters[6];
                     if (collisionParameters[2] == 0) {
                        if (tileX >= 0 && tileX > collisionPixelsX) {
                           previousPathX = (short)(previousPathX + (tileX - collisionPixelsX) * collisionParameters[3]);
                           this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                        }
                     } else if (collisionPixelsX >= 0 && collisionPixelsX > tileX) {
                        previousPathY = (short)(previousPathY + (collisionPixelsX - tileX) * collisionParameters[3]);
                        this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
                     }
                  }

                  ++collisionCheckIndex;
               }
            case 1:
            case 5:
               collisionBaseTileX = 0;
               collisionPixelsY = 0;
               if (previousPathY % tileHeight < collisionHeight) {
                  collisionBaseTileX = -1;
                  collisionPixelsY = collisionHeight - previousPathY % tileHeight;
               } else if (tileHeight - previousPathY % tileHeight < collisionHeight + 1) {
                  collisionBaseTileX = 1;
                  collisionPixelsY = tileHeight - previousPathY % tileHeight - (collisionHeight + 1);
               }

               if (collisionBaseTileX != 0 && blocks.isBlockTl(pathElementX, (short)(pathElementY + collisionBaseTileX))) {
                  previousPathY = (short)(previousPathY + collisionPixelsY);
                  tileX = previousPathX + (nextDirection == 1 ? collisionWidth : -collisionWidth);
                  collisionDeltaY = pathElementX * tileWidth + (nextDirection == 1 ? 0 : tileWidth - 1);
                  if (Math.abs(tileX - collisionDeltaY) > Math.abs(collisionPixelsY)) {
                     previousPathX = (short)(previousPathX + (nextDirection == 1 ? Math.abs(collisionPixelsY) : -Math.abs(collisionPixelsY)));
                  }

                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
               }

               if (blocks.isBlockTl(pathElementX, (short)(pathElementY - 1))) {
                  wasWallUp = true;
               }

               if (blocks.isBlockTl(pathElementX, (short)(pathElementY + 1))) {
                  wasWallDown = true;
               }
               break;
            case 3:
            case 7:
               collisionOffsetY = 0;
               offsetPixelsX = 0;
               if (previousPathX % tileWidth < collisionWidth) {
                  collisionOffsetY = -1;
                  offsetPixelsX = collisionWidth - previousPathX % tileWidth;
               } else if (tileWidth - previousPathX % tileWidth < collisionWidth + 1) {
                  collisionOffsetY = 1;
                  offsetPixelsX = tileWidth - previousPathX % tileWidth - (collisionWidth + 1);
               }

               if (collisionOffsetY != 0 && blocks.isBlockTl((short)(pathElementX + collisionOffsetY), (short)pathElementY)) {
                  previousPathX = (short)(previousPathX + offsetPixelsX);
                  collisionPixelsX = previousPathY + (nextDirection == 3 ? collisionHeight : -collisionHeight);
                  collisionPixelsY = pathElementY * tileHeight + (nextDirection == 3 ? 0 : tileHeight - 1);
                  if (Math.abs(collisionPixelsX - collisionPixelsY) > Math.abs(offsetPixelsX)) {
                     previousPathY = (short)(previousPathY + (nextDirection == 3 ? Math.abs(offsetPixelsX) : -Math.abs(offsetPixelsX)));
                  }

                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
               }

               if (blocks.isBlockTl((short)(pathElementX - 1), (short)pathElementY)) {
                  wasWallLeft = true;
               }

               if (blocks.isBlockTl((short)(pathElementX + 1), (short)pathElementY)) {
                  wasWallRight = true;
               }
         }

         tileX = nextDirection;
         previousTileX = pathElementX;
         previousTileY = (short)pathElementY;
      }

      short collisionOffsetX;
      if (path.length > 0) {
         previousTileX = path.length > 1 ? path[path.length - 2][0] : fromTileX;
         previousTileY = path.length > 1 ? path[path.length - 2][1] : fromTileY;
         short lastTileX = path[path.length - 1][0];
         pathElementX = path[path.length - 1][1];
         switch (pathElementY = MathGame.vectorToDirection8(lastTileX - previousTileX, pathElementX - previousTileY)) {
            case 1:
            case 5:
               collisionOffsetY = 0;
               if (toY % tileHeight < collisionHeight) {
                  collisionOffsetY = -1;
               } else if (tileHeight - toX % tileHeight < collisionHeight + 1) {
                  collisionOffsetY = 1;
               }

               if (collisionOffsetY != 0 && blocks.isBlockTl((short)previousTileX, (short)(previousTileY + collisionOffsetY))) {
                  previousPathX = (short)(previousTileX * tileWidth + (pathElementY == 1 ? tileWidth + collisionWidth : -collisionWidth - 1));
                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
               }
            case 2:
            case 4:
            case 6:
            default:
               break;
            case 3:
            case 7:
               collisionOffsetX = 0;
               if (toX % tileWidth < collisionWidth) {
                  collisionOffsetX = -1;
               } else if (tileWidth - toX % tileWidth < collisionWidth + 1) {
                  collisionOffsetX = 1;
               }

               if (collisionOffsetX != 0 && blocks.isBlockTl((short)(previousTileX + collisionOffsetX), (short)previousTileY)) {
                  previousPathY = (short)(previousTileY * tileHeight + (pathElementY == 3 ? tileHeight + collisionHeight : -collisionHeight - 1));
                  this.buildPath(toVector, previousPathX, previousPathY, fromX, fromY);
               }
         }
      }

      if (toVector.size() <= 0) {
         buildPath(toVector, toX, toY, fromX, fromY, true, tileX == 7 || tileX == 3 || tileX == 5 || tileX == 1);
      } else {
         boolean canCutLastSegment = true;
         if (tileX == 7 || tileX == 3) {
            if (toX % tileWidth < collisionWidth && wasWallLeft) {
               canCutLastSegment = false;
            }

            if (tileWidth - toX % tileWidth < collisionWidth + 1 && wasWallRight) {
               canCutLastSegment = false;
            }
         }

         if (tileX == 5 || tileX == 1) {
            if (toY % tileHeight < collisionHeight && wasWallUp) {
               canCutLastSegment = false;
            }

            if (tileHeight - toY % tileHeight < collisionHeight + 1 && wasWallDown) {
               canCutLastSegment = false;
            }
         }

         if (canCutLastSegment) {
            pathElementX = toVector.size() >= 2 ? ((short[])toVector.elementAt(toVector.size() - 2))[0] : fromX;
            short previousPointY = toVector.size() >= 2 ? ((short[])toVector.elementAt(toVector.size() - 2))[1] : fromY;
            collisionOffsetX = ((short[])toVector.elementAt(toVector.size() - 1))[0];
            collisionOffsetY = ((short[])toVector.elementAt(toVector.size() - 1))[1];
            short[] correctedEndpoint = cutLineByPoint(toX, toY, pathElementX, previousPointY, collisionOffsetX, collisionOffsetY, false, false, -1, -1, -1, -1);
            ((short[])toVector.elementAt(toVector.size() - 1))[0] = correctedEndpoint[0];
            ((short[])toVector.elementAt(toVector.size() - 1))[1] = correctedEndpoint[1];
         }

         boolean blockByX = false;
         if (toX % tileWidth < collisionWidth) {
            blockByX = wasWallLeft;
         } else if (tileWidth - toX % tileWidth < collisionWidth + 1) {
            blockByX = wasWallRight;
         }

         boolean blockByY = false;
         if (toY % tileHeight < collisionHeight) {
            blockByY = wasWallUp;
         } else if (tileHeight - toY % tileHeight < collisionHeight + 1) {
            blockByY = wasWallDown;
         }

         if ((tileX == 1 || tileX == 5) && blockByY) {
            buildPath(toVector, toX, toY, fromX, fromY, false, false);
         } else {
            if (tileX != 7 && tileX != 3 || !blockByX) {
               this.buildPath(toVector, toX, toY, fromX, fromY);
               return;
            }

            buildPath(toVector, toX, toY, fromX, fromY, false, false);
         }
      }

   }

   private static short[] cutLineByPoint(short pointX, short pointY, int segmentStartX, int segmentStartY, int segmentEndX, int segmentEndY, boolean constrainX, boolean constrainY, int minX, int minY, int maxX, int maxY) {
      int intersectionCoordinate;
      if (segmentStartX != segmentEndX) {
         if (segmentStartX < segmentEndX) {
            if (segmentStartX <= pointX && segmentEndX >= pointX) {
               intersectionCoordinate = segmentEndY - segmentStartY == 0 ? segmentEndY : segmentEndY - (segmentEndY - segmentStartY > 0 ? segmentEndX - pointX : -(segmentEndX - pointX));
               if (!constrainY || intersectionCoordinate >= minY && intersectionCoordinate < maxY) {
                  segmentEndX = pointX;
                  segmentEndY = intersectionCoordinate;
               }
            }
         } else if (segmentEndX <= pointX && segmentStartX >= pointX) {
            intersectionCoordinate = segmentEndY - segmentStartY == 0 ? segmentEndY : segmentEndY - (segmentEndY - segmentStartY > 0 ? pointX - segmentEndX : -(pointX - segmentEndX));
            if (!constrainY || intersectionCoordinate >= minY && intersectionCoordinate < maxY) {
               segmentEndX = pointX;
               segmentEndY = intersectionCoordinate;
            }
         }
      }

      if (segmentStartY != segmentEndY) {
         if (segmentStartY < segmentEndY) {
            if (segmentStartY <= pointY && segmentEndY >= pointY) {
               intersectionCoordinate = segmentEndX - segmentStartX == 0 ? segmentEndX : segmentEndX - (segmentEndX - segmentStartX > 0 ? segmentEndY - pointY : -(segmentEndY - pointY));
               if (!constrainX || intersectionCoordinate >= minX && intersectionCoordinate < maxX) {
                  segmentEndY = pointY;
                  segmentEndX = intersectionCoordinate;
               }
            }
         } else if (segmentEndY <= pointY && segmentStartY >= pointY) {
            intersectionCoordinate = segmentEndX - segmentStartX == 0 ? segmentEndX : segmentEndX - (segmentEndX - segmentStartX > 0 ? pointY - segmentEndY : -(pointY - segmentEndY));
            if (!constrainX || intersectionCoordinate >= minX && intersectionCoordinate < maxX) {
               segmentEndY = pointY;
               segmentEndX = intersectionCoordinate;
            }
         }
      }

      return new short[]{(short)segmentEndX, (short)segmentEndY};
   }

   private void buildPath(Vector vector3, short targetX, short targetY, short startX, short startY) {
      buildPath(vector3, targetX, targetY, startX, startY, false, false);
   }

   private static void buildPath(Vector vector3, short targetX, short targetY, short startX, short startY, boolean forceDirection, boolean preferStraightSegment) {
      int orthoStepBetweenTwoDirections8D = startX;
      short originalStartY = startY;
      if (vector3.size() > 0) {
         short[] shorts;
         startX = (shorts = (short[])vector3.elementAt(vector3.size() - 1))[0];
         startY = shorts[1];
      }

      int absoluteDistance = Math.abs(targetX - startX);
      int absoluteDistance2 = Math.abs(targetY - startY);
      if (absoluteDistance != absoluteDistance2 && absoluteDistance != 0 && absoluteDistance2 != 0) {
         boolean insertStraightFirst;
         label97: {
            insertStraightFirst = false;
            boolean preferOrthogonal;
            if (forceDirection) {
               preferOrthogonal = preferStraightSegment;
            } else {
               int orthoStepBetweenTwoDirections8D2 = targetX - startX;
               int vectorToDirection8 = targetY - startY;
               int orthogonalDirection;
               if (absoluteDistance > absoluteDistance2) {
                  orthogonalDirection = orthoStepBetweenTwoDirections8D2 > 0 ? 1 : 5;
                  orthoStepBetweenTwoDirections8D2 = vectorToDirection8 > 0 ? (orthogonalDirection == 1 ? 2 : 4) : (orthogonalDirection == 1 ? 0 : 6);
               } else {
                  orthogonalDirection = vectorToDirection8 > 0 ? 3 : 7;
                  orthoStepBetweenTwoDirections8D2 = orthoStepBetweenTwoDirections8D2 > 0 ? (orthogonalDirection == 3 ? 2 : 0) : (orthogonalDirection == 3 ? 4 : 6);
               }

               vectorToDirection8 = -1;
               if (vector3.size() > 0) {
                  if (vector3.size() > 1) {
                     short[] vector3Count = (short[])vector3.elementAt(vector3.size() - 2);
                     vectorToDirection8 = MathGame.vectorToDirection8(startX - vector3Count[0], startY - vector3Count[1]);
                  } else {
                     vectorToDirection8 = MathGame.vectorToDirection8(startX - orthoStepBetweenTwoDirections8D, startY - originalStartY);
                  }
               }

               if (vectorToDirection8 == -1) {
                  break label97;
               }

               orthoStepBetweenTwoDirections8D = MathGame.getOrthoStepBetweenTwoDirections8D(orthogonalDirection, vectorToDirection8);
               orthoStepBetweenTwoDirections8D2 = MathGame.getOrthoStepBetweenTwoDirections8D(orthoStepBetweenTwoDirections8D2, vectorToDirection8);
               preferOrthogonal = orthoStepBetweenTwoDirections8D < orthoStepBetweenTwoDirections8D2;
            }

            insertStraightFirst = preferOrthogonal;
         }

         if (insertStraightFirst) {
            if (absoluteDistance > absoluteDistance2) {
               vector3.addElement(new short[]{(short)(startX + (targetX - startX) - (targetX - startX > 0 ? absoluteDistance2 : -absoluteDistance2)), startY});
            } else {
               vector3.addElement(new short[]{startX, (short)(startY + (targetY - startY) - (targetY - startY > 0 ? absoluteDistance : -absoluteDistance))});
            }
         } else if (absoluteDistance > absoluteDistance2) {
            vector3.addElement(new short[]{(short)(startX + (targetX - startX > 0 ? absoluteDistance2 : -absoluteDistance2)), targetY});
         } else {
            vector3.addElement(new short[]{targetX, (short)(startY + (targetY - startY > 0 ? absoluteDistance : -absoluteDistance))});
         }
      }

      vector3.addElement(new short[]{targetX, targetY});
   }

   private synchronized short[][] buildPath(PathfindingGridBlockable pathfindingGridBlockable, short fromX, short fromY, short toX, short toY) {
      try {
         PathfindingCellState pathfindingCellState;
         if ((pathfindingCellState = this.fillGridForPathBuilding(pathfindingGridBlockable, fromX, (short)fromY, toX, (short)toY)).pathSearchFlag) {
            return new short[0][];
         } else {
            fromX = pathfindingCellState.tileX;
            short tileY2 = pathfindingCellState.tileY;
            short[][] res = new short[fromY = this.grid[this.sizeX * tileY2 + fromX]][];
            if (fromY > 0) {
               res[fromY - 1] = new short[]{fromX, tileY2};
            }

            for(toY = (short)(fromY - 2); toY >= 0; --toY) {
               fromY = (short)(this.sizeX * tileY2 + fromX);
               if (fromX + 1 < this.sizeX && this.grid[fromY + 1] == toY + 1) {
                  ++fromX;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (fromX - 1 >= 0 && this.grid[fromY - 1] == toY + 1) {
                  --fromX;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (tileY2 + 1 < this.sizeY && this.grid[fromY + this.sizeX] == toY + 1) {
                  ++tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (tileY2 - 1 >= 0 && this.grid[fromY - this.sizeX] == toY + 1) {
                  --tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (fromX + 1 < this.sizeX && tileY2 + 1 < this.sizeY && this.grid[fromY + 1 + this.sizeX] == toY + 1) {
                  ++fromX;
                  ++tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (fromX - 1 >= 0 && tileY2 + 1 < this.sizeY && this.grid[fromY - 1 + this.sizeX] == toY + 1) {
                  --fromX;
                  ++tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (fromX + 1 < this.sizeX && tileY2 - 1 >= 0 && this.grid[fromY + 1 - this.sizeX] == toY + 1) {
                  ++fromX;
                  --tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               } else if (fromX - 1 >= 0 && tileY2 - 1 >= 0 && this.grid[fromY - 1 - this.sizeX] == toY + 1) {
                  --fromX;
                  --tileY2;
                  res[toY] = new short[]{fromX, tileY2};
               }
            }

            return res;
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         return new short[0][];
      }
   }

   private synchronized PathfindingCellState fillGridForPathBuilding(PathfindingGridBlockable pathfindingGridBlockable, short fromX, short sizeX2, short toX, short toY) {
      if (fromX == toX && sizeX2 == toY) {
         return new PathfindingCellState(true, true, (short)-1, (short)-1);
      } else {
         try {
            int nextFrom = 0;

            for(int gridCount = this.grid.length - 1; gridCount >= 0; --gridCount) {
               this.grid[gridCount] = -1;
            }

            int nextLength = 1;
            this.next[0] = (short)fromX;
            this.next[1] = (short)sizeX2;
            this.grid[this.sizeX * sizeX2 + fromX] = 0;
            int mAX_VALUE = Integer.MAX_VALUE;
            short closestPointX = -1;
            short closestPointY = -1;
            short currentStep = 0;
            boolean done = false;
            int absoluteDistance = Math.abs(toX - fromX);
            int absoluteDistance2 = Math.abs(toY - sizeX2);
            int minimum = Math.min(absoluteDistance, absoluteDistance2);
            if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < Integer.MAX_VALUE) {
               mAX_VALUE = absoluteDistance;
               closestPointX = (short)fromX;
               closestPointY = (short)sizeX2;
            }

            short next2;
            while(!done && nextLength > 0) {
               ++currentStep;
               int queueIndex = nextFrom;
               int queueEnd = nextFrom + nextLength;
               nextFrom += nextLength;

               for(nextLength = 0; queueIndex < queueEnd; ++queueIndex) {
                  fromX = (short)(queueIndex << 1);
                  short next3 = this.next[fromX];
                  next2 = this.next[fromX + 1];
                  sizeX2 = (short)(this.sizeX * next2 + next3);
                  boolean isBlockTl = true;
                  boolean isBlockTl2 = true;
                  boolean isBlockTl3 = true;
                  boolean isBlockTl4 = true;
                  short neighborRightX = (short)(next3 + 1);
                  short neighborLeftX = (short)(next3 - 1);
                  short neighborDownY = (short)(next2 + 1);
                  short neighborUpY = (short)(next2 - 1);
                  if (neighborRightX < this.sizeX && !(isBlockTl = pathfindingGridBlockable.isBlockTl(neighborRightX, next2)) && this.grid[sizeX2 + 1] == -1) {
                     if (neighborRightX == toX && next2 == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborRightX);
                     absoluteDistance2 = Math.abs(toY - next2);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborRightX;
                        closestPointY = next2;
                     }

                     this.grid[sizeX2 + 1] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborRightX;
                     this.next[absoluteDistance + 1] = next2;
                  }

                  if (neighborLeftX >= 0 && !(isBlockTl2 = pathfindingGridBlockable.isBlockTl(neighborLeftX, next2)) && this.grid[sizeX2 - 1] == -1) {
                     if (neighborLeftX == toX && next2 == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborLeftX);
                     absoluteDistance2 = Math.abs(toY - next2);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborLeftX;
                        closestPointY = next2;
                     }

                     this.grid[sizeX2 - 1] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborLeftX;
                     this.next[absoluteDistance + 1] = next2;
                  }

                  if (neighborDownY < this.sizeY && !(isBlockTl3 = pathfindingGridBlockable.isBlockTl(next3, neighborDownY)) && this.grid[sizeX2 + this.sizeX] == -1) {
                     if (next3 == toX && neighborDownY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - next3);
                     absoluteDistance2 = Math.abs(toY - neighborDownY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = next3;
                        closestPointY = neighborDownY;
                     }

                     this.grid[sizeX2 + this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = next3;
                     this.next[absoluteDistance + 1] = neighborDownY;
                  }

                  if (neighborUpY >= 0 && !(isBlockTl4 = pathfindingGridBlockable.isBlockTl(next3, neighborUpY)) && this.grid[sizeX2 - this.sizeX] == -1) {
                     if (next3 == toX && neighborUpY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - next3);
                     absoluteDistance2 = Math.abs(toY - neighborUpY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = next3;
                        closestPointY = neighborUpY;
                     }

                     this.grid[sizeX2 - this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = next3;
                     this.next[absoluteDistance + 1] = neighborUpY;
                  }

                  if (neighborRightX < this.sizeX && neighborDownY < this.sizeY && this.grid[sizeX2 + 1 + this.sizeX] == -1 && !pathfindingGridBlockable.isBlockTl(neighborRightX, neighborDownY) && !isBlockTl && !isBlockTl3) {
                     if (neighborRightX == toX && neighborDownY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborRightX);
                     absoluteDistance2 = Math.abs(toY - neighborDownY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborRightX;
                        closestPointY = neighborDownY;
                     }

                     this.grid[sizeX2 + 1 + this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborRightX;
                     this.next[absoluteDistance + 1] = neighborDownY;
                  }

                  if (neighborRightX < this.sizeX && neighborUpY >= 0 && this.grid[sizeX2 + 1 - this.sizeX] == -1 && !pathfindingGridBlockable.isBlockTl(neighborRightX, neighborUpY) && !isBlockTl && !isBlockTl4) {
                     if (neighborRightX == toX && neighborUpY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborRightX);
                     absoluteDistance2 = Math.abs(toY - neighborUpY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborRightX;
                        closestPointY = neighborUpY;
                     }

                     this.grid[sizeX2 + 1 - this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborRightX;
                     this.next[absoluteDistance + 1] = neighborUpY;
                  }

                  if (neighborLeftX >= 0 && neighborDownY < this.sizeY && this.grid[sizeX2 - 1 + this.sizeX] == -1 && !pathfindingGridBlockable.isBlockTl(neighborLeftX, neighborDownY) && !isBlockTl2 && !isBlockTl3) {
                     if (neighborLeftX == toX && neighborDownY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborLeftX);
                     absoluteDistance2 = Math.abs(toY - neighborDownY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborLeftX;
                        closestPointY = neighborDownY;
                     }

                     this.grid[sizeX2 - 1 + this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborLeftX;
                     this.next[absoluteDistance + 1] = neighborDownY;
                  }

                  if (neighborLeftX >= 0 && neighborUpY >= 0 && this.grid[sizeX2 - 1 - this.sizeX] == -1 && !pathfindingGridBlockable.isBlockTl(neighborLeftX, neighborUpY) && !isBlockTl2 && !isBlockTl4) {
                     if (neighborLeftX == toX && neighborUpY == toY) {
                        done = true;
                     }

                     absoluteDistance = Math.abs(toX - neighborLeftX);
                     absoluteDistance2 = Math.abs(toY - neighborUpY);
                     minimum = Math.min(absoluteDistance, absoluteDistance2);
                     if ((absoluteDistance = minimum * 141 + 100 * (Math.max(absoluteDistance, absoluteDistance2) - minimum)) < mAX_VALUE) {
                        mAX_VALUE = absoluteDistance;
                        closestPointX = neighborLeftX;
                        closestPointY = neighborUpY;
                     }

                     this.grid[sizeX2 - 1 - this.sizeX] = currentStep;
                     absoluteDistance = nextFrom + nextLength << 1;
                     ++nextLength;
                     this.next[absoluteDistance] = neighborLeftX;
                     this.next[absoluteDistance + 1] = neighborUpY;
                  }
               }
            }

            short resolvedTargetX = toX;
            next2 = toY;
            if (!done) {
               if (mAX_VALUE == Integer.MAX_VALUE) {
                  return new PathfindingCellState(true, false, (short)-1, (short)-1);
               }

               resolvedTargetX = closestPointX;
               next2 = closestPointY;
            }

            return new PathfindingCellState(false, done, resolvedTargetX, next2);
         } catch (Exception exception) {
            exception.printStackTrace();
            return new PathfindingCellState(true, false, (short)-1, (short)-1);
         }
      }
   }

   public final synchronized int getPathLengthInPixelsWithTilesPrecision(PathfindingGridBlockable pathfindingGridBlockable, short fromXpx, short fromYpx, short toXpx, short toYpx, short tileWidth, short tileHeight, int tileDiagonalLength, boolean neagativeIfUnachived) {
      if (toXpx < 0) {
         toXpx = 0;
      }

      if (toYpx < 0) {
         toYpx = 0;
      }

      if (toXpx >= tileWidth * this.sizeX) {
         toXpx = (short)(tileWidth * this.sizeX - 1);
      }

      if (toYpx >= tileHeight * this.sizeY) {
         toYpx = (short)(tileHeight * this.sizeY - 1);
      }

      toXpx = (short)((int)MathGame.floorDivide((long)toXpx, (long)tileWidth));
      toYpx = (short)((int)MathGame.floorDivide((long)toYpx, (long)tileHeight));
      fromXpx = (short)((int)MathGame.floorDivide((long)fromXpx, (long)tileWidth));
      fromYpx = (short)((int)MathGame.floorDivide((long)fromYpx, (long)tileHeight));
      if (fromXpx == toXpx && fromYpx == toYpx) {
         return 0;
      } else {
         try {
            PathfindingCellState pathfindingCellState = this.fillGridForPathBuilding(pathfindingGridBlockable, fromXpx, fromYpx, toXpx, (short)toYpx);
            if (neagativeIfUnachived && !pathfindingCellState.pathStepFlag) {
               return -1;
            } else if (pathfindingCellState.pathSearchFlag) {
               return 0;
            } else {
               toXpx = pathfindingCellState.tileX;
               int tileY2 = pathfindingCellState.tileY;
               toYpx = this.grid[this.sizeX * tileY2 + toXpx];
               int pathLengthInPixelsWithTilesPrecision = 0;

               int absoluteDistance;
               for(absoluteDistance = toYpx - 2; absoluteDistance >= 0; --absoluteDistance) {
                  toYpx = (short)(this.sizeX * tileY2 + toXpx);
                  if (toXpx + 1 < this.sizeX && this.grid[toYpx + 1] == absoluteDistance + 1) {
                     ++toXpx;
                     pathLengthInPixelsWithTilesPrecision += tileWidth;
                  } else if (toXpx - 1 >= 0 && this.grid[toYpx - 1] == absoluteDistance + 1) {
                     --toXpx;
                     pathLengthInPixelsWithTilesPrecision += tileWidth;
                  } else if (tileY2 + 1 < this.sizeY && this.grid[toYpx + this.sizeX] == absoluteDistance + 1) {
                     tileY2 = (short)(tileY2 + 1);
                     pathLengthInPixelsWithTilesPrecision += tileHeight;
                  } else if (tileY2 - 1 >= 0 && this.grid[toYpx - this.sizeX] == absoluteDistance + 1) {
                     tileY2 = (short)(tileY2 - 1);
                     pathLengthInPixelsWithTilesPrecision += tileHeight;
                  } else if (toXpx + 1 < this.sizeX && tileY2 + 1 < this.sizeY && this.grid[toYpx + 1 + this.sizeX] == absoluteDistance + 1) {
                     ++toXpx;
                     tileY2 = (short)(tileY2 + 1);
                     pathLengthInPixelsWithTilesPrecision += tileDiagonalLength;
                  } else if (toXpx - 1 >= 0 && tileY2 + 1 < this.sizeY && this.grid[toYpx - 1 + this.sizeX] == absoluteDistance + 1) {
                     --toXpx;
                     tileY2 = (short)(tileY2 + 1);
                     pathLengthInPixelsWithTilesPrecision += tileDiagonalLength;
                  } else if (toXpx + 1 < this.sizeX && tileY2 - 1 >= 0 && this.grid[toYpx + 1 - this.sizeX] == absoluteDistance + 1) {
                     ++toXpx;
                     tileY2 = (short)(tileY2 - 1);
                     pathLengthInPixelsWithTilesPrecision += tileDiagonalLength;
                  } else if (toXpx - 1 >= 0 && tileY2 - 1 >= 0 && this.grid[toYpx - 1 - this.sizeX] == absoluteDistance + 1) {
                     --toXpx;
                     tileY2 = (short)(tileY2 - 1);
                     pathLengthInPixelsWithTilesPrecision += tileDiagonalLength;
                  }
               }

               absoluteDistance = Math.abs(toXpx - fromXpx);
               tileY2 = Math.abs(tileY2 - fromYpx);
               if (absoluteDistance != 0 || tileY2 != 0) {
                  if (absoluteDistance > tileY2) {
                     pathLengthInPixelsWithTilesPrecision += tileWidth;
                  } else if (tileY2 > absoluteDistance) {
                     pathLengthInPixelsWithTilesPrecision += tileHeight;
                  } else {
                     pathLengthInPixelsWithTilesPrecision += tileDiagonalLength;
                  }
               }

               return pathLengthInPixelsWithTilesPrecision;
            }
         } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
         }
      }
   }
}
