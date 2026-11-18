package Moduls;

import java.io.DataInputStream;

public class ItemSimple {
   public long typeId = -1L;
   public int count = 1;
   public String nameVis;

   public ItemSimple() {
   }

   public ItemSimple(DataInputStream dataInputStream) {
      this.read(dataInputStream);
   }

   public ItemSimple(long typeId2, int count2, String nameVis2) {
      this.typeId = typeId2;
      this.count = count2;
      this.nameVis = nameVis2;
   }

   public final void read(DataInputStream dataInputStream) {
      this.typeId = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.count = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.nameVis = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public static ItemSimple readInstance(DataInputStream dataInputStream) {
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         ItemSimple itemSimple;
         (itemSimple = new ItemSimple()).read(dataInputStream);
         return itemSimple;
      } else {
         return null;
      }
   }

   public final ItemSimple copy() {
      return new ItemSimple(this.typeId, this.count, this.nameVis);
   }
}
