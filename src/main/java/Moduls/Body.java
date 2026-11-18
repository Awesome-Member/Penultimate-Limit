package Moduls;

import java.io.DataInputStream;
import java.io.IOException;

public final class Body {
   ImageLevelAreaDesc imgDesc;
   private String Name;
   public int HDX0;
   public int HDY0;
   public int shadowInd = 0;
   public int offX;
   public int offY;
   public int verticalOffset = 0;
   public boolean jumping = true;

   public Body() {
   }

   public Body(DataInputStream dataInputStream) {
      try {
         this.Name = dataInputStream.readUTF();
         this.jumping = dataInputStream.readBoolean();
         this.imgDesc = new ImageLevelAreaDesc(dataInputStream);
         this.HDX0 = this.imgDesc.adding[0];
         this.HDY0 = this.imgDesc.adding[1];
         this.verticalOffset = this.imgDesc.adding[2];
         this.shadowInd = this.imgDesc.adding[3];
         this.offX = -this.imgDesc.frW / 2;
         this.offY = -this.imgDesc.rh;
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }
}
