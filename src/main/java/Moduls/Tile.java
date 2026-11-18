package Moduls;

import Engine.AnimSprite;
import Engine.MathGame;
import java.io.DataInputStream;
import java.io.IOException;

public final class Tile {
   private ImageLevelAreaDesc imageDescrsTile;
   public ImageLevelAreaDesc[] imageDescrsBorders = new ImageLevelAreaDesc[4];
   public AnimSprite Self;
   public static int width = 36;
   public static int height = 36;
   public static int widthHalf = 18;
   public static int heightHalf = 18;
   public static int height2_3 = 24;
   public static int width2_6 = 12;
   public static int tileDiagonalLength = 51;
   public static int BordOffset;
   public int color;
   private int bordersOrder;
   public boolean GP_Block;

   public Tile() {
      this.Self = null;
   }

   public Tile(DataInputStream dataInputStream) {
      try {
         this.imageDescrsTile = new ImageLevelAreaDesc(dataInputStream);

         for(int imageDescrsBorderIndex = 0; imageDescrsBorderIndex < this.imageDescrsBorders.length; ++imageDescrsBorderIndex) {
            this.imageDescrsBorders[imageDescrsBorderIndex] = new ImageLevelAreaDesc(dataInputStream);
         }

         this.Self = new AnimSprite(this.imageDescrsTile);
         this.GP_Block = dataInputStream.readBoolean();
         this.color = dataInputStream.readInt();
         this.bordersOrder = dataInputStream.readInt();
      } catch (IOException exception) {
      }

      width = this.Self.sw;
      height = this.Self.sh;
      widthHalf = width / 2;
      heightHalf = height / 2;
      height2_3 = (height << 1) / 3;
      width2_6 = (width << 1) / 6;
      BordOffset = this.imageDescrsBorders[0].rh / 2;
   }

   public static void calculateConstants() {
      tileDiagonalLength = (int)MathGame.integerSquareRoot((long)width * (long)width + (long)height * (long)height);
   }
}
