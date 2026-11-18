package Moduls;

public interface PathfindingGridBlockable {
   boolean isBlockTl(short x, short y);

   boolean isPathCellBlocked(short pixelX, short pixelY);
}
