package Base;

public final class GameWalkPathElement {
   public short x;
   public short y;
   public byte watchDirection;

   public GameWalkPathElement(short x2, short y2, byte watchDirection2) {
      this.x = x2;
      this.y = y2;
      this.watchDirection = watchDirection2;
   }
}
