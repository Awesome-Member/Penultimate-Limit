package Moduls.quests;

import java.io.DataInputStream;

public final class QuestSupMonsterTarget {
   public String monsterName;
   public String monsterVis;
   public short count;
   public int headInd;
   public int bodyInd;
   public String[] maps = new String[0];
   public short[][] mapsWorldCoords = new short[0][];

   public QuestSupMonsterTarget(DataInputStream dataInputStream) {
      this.loadFromStream(dataInputStream);
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.monsterName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.monsterVis = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.count = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.headInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.bodyInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
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
