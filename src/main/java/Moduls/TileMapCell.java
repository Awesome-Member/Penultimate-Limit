package Moduls;

public final class TileMapCell {
   public short textInd = -1;
   public short borderV0 = -1;
   public short borderV1 = -1;
   public short borderH0 = -1;
   public short borderH1 = -1;
   public short decorTri = -1;
   public short townInd = -1;
   public short tileInd = -1;
   public TileMapElement[] decors;
   public TileMapElement[] decorsDraw;
   public boolean block = false;

   public final void reset() {
      this.textInd = -1;
      this.borderV0 = -1;
      this.borderV1 = -1;
      this.borderH0 = -1;
      this.borderH1 = -1;
      this.decorTri = -1;
      this.townInd = -1;
      this.tileInd = -1;
      this.block = false;
      this.decors = null;
      this.decorsDraw = null;
   }
}
