package Moduls.quests;

import Moduls.ItemSimple;
import java.io.DataInputStream;

public final class QuestSupStartDropItem extends ItemSimple {
   public String string;
   public String string2;
   public int chance;
   public int headInd;
   public int bodyInd;
   private String[] maps = new String[0];
   public short[][] mapsWorldCoords = new short[0][];

   public QuestSupStartDropItem(DataInputStream dataInputStream) {
      DataInputStream dataInputStream2 = dataInputStream;
      QuestSupStartDropItem questSupStartDropItem = this;
      this.read(dataInputStream);
      this.string2 = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.string = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.chance = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.headInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.bodyInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.maps = new String[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];
      this.mapsWorldCoords = new short[this.maps.length][];

      for(short mapIndex = 0; mapIndex < questSupStartDropItem.maps.length; ++mapIndex) {
         questSupStartDropItem.maps[mapIndex] = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream2);
         if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream2)) {
            questSupStartDropItem.mapsWorldCoords[mapIndex] = new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream2), decompiledsupport.UncheckedDataInput.readShort(dataInputStream2)};
         } else {
            questSupStartDropItem.mapsWorldCoords[mapIndex] = null;
         }
      }

   }
}
