package Moduls;

import Engine.AnimSprite;
import java.io.DataInputStream;
import java.io.IOException;

public final class Decor {
   private ImageLevelAreaDesc imgDesc;
   ImageLevelAreaDesc imageLevelAreaDesc;
   public AnimSprite Self;
   public boolean Anim;
   public int width;
   public int height;
   public int Offset;
   public int w2;
   public int EncloseCoord;
   public int style;
   private int passCode;
   public int color;
   public boolean GP_Block;
   public boolean GP_UnBlock;

   public Decor() {
      this.Self = null;
   }

   public Decor(DataInputStream dataInputStream) {
      try {
         this.style = dataInputStream.readInt();
         this.passCode = dataInputStream.readInt();
         this.GP_Block = dataInputStream.readBoolean();
         this.GP_UnBlock = dataInputStream.readBoolean();
         this.color = dataInputStream.readInt();
         this.imgDesc = new ImageLevelAreaDesc(dataInputStream);
         if (dataInputStream.readBoolean()) {
            this.imageLevelAreaDesc = new ImageLevelAreaDesc(dataInputStream);
         } else {
            this.imageLevelAreaDesc = null;
         }

         this.Offset = this.imgDesc.adding[0];
         this.EncloseCoord = this.imgDesc.adding[1];
         this.Self = new AnimSprite(this.imgDesc);
         this.Anim = this.imgDesc.numFrames != 1;
      } catch (IOException exception) {
         exception.printStackTrace();
      }

      this.width = this.Self.sw;
      this.height = this.Self.sh;
      this.w2 = this.width / 2;
   }
}
