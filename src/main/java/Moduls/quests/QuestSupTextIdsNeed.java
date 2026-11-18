package Moduls.quests;

import java.io.DataInputStream;

public final class QuestSupTextIdsNeed {
   public String name;
   public short count;
   public String[] maps = new String[0];
   public short[][] mapsWorldCoords = new short[0][];

   public QuestSupTextIdsNeed(DataInputStream dataInputStream) {
      this.loadFromStream(dataInputStream);
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.count = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.maps = new String[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];
      this.mapsWorldCoords = new short[this.maps.length][];

      for(short i = 0; i < this.maps.length; ++i) {
         this.maps[i] = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
         if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
            this.mapsWorldCoords[i] = new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
         } else {
            this.mapsWorldCoords[i] = null;
         }
      }

   }
}
