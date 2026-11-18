package Moduls.quests;

import java.io.DataInputStream;

public final class QuestSupMapWithCoords {
   public String map;
   public short[] mapWorldCoord = null;
   public int[][] coords = new int[0][2];

   public QuestSupMapWithCoords(DataInputStream dataInputStream) {
      this.loadForStream(dataInputStream);
   }

   private void loadForStream(DataInputStream dataInputStream) {
      this.map = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.mapWorldCoord = new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
      } else {
         this.mapWorldCoord = null;
      }

      this.coords = new int[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)][];

      for(int i = 0; i < this.coords.length; ++i) {
         this.coords[i] = new int[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
      }

   }
}
