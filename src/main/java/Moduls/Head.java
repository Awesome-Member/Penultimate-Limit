package Moduls;

import java.io.DataInputStream;
import java.io.IOException;

public final class Head {
   private String Name;
   ImageLevelAreaDesc imgDescLeft;
   public int LDXO;
   public int LDYO;

   public Head(DataInputStream dataInputStream) {
      try {
         this.Name = dataInputStream.readUTF();
         this.imgDescLeft = new ImageLevelAreaDesc(dataInputStream);
         this.LDXO = this.imgDescLeft.adding[0];
         this.LDYO = this.imgDescLeft.adding[1];
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }
}
