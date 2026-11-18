package Moduls.quests;

import java.io.DataInputStream;

public final class QuestInPlayerKillPlayerPosition {
   public short posX = -1;
   public short posY = -1;
   public int[] ways = new int[0];
   public short[] mapWorldCoord = null;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.posX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.posY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.ways = QuestInPlayer.loadFromStream(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.mapWorldCoord = new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)};
      } else {
         this.mapWorldCoord = null;
      }
   }
}
