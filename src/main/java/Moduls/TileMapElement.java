package Moduls;

public final class TileMapElement {
   public short x;
   public short y;
   public short sprite;
   public short addHeight;

   public TileMapElement() {
   }

   public TileMapElement(short x2, short y2, short sprite2, short addHeight2) {
      this.x = x2;
      this.y = y2;
      this.sprite = sprite2;
      this.addHeight = addHeight2;
   }
}
