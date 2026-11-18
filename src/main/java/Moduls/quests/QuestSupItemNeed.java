package Moduls.quests;

import Moduls.ItemSimple;
import java.io.DataInputStream;

public final class QuestSupItemNeed extends ItemSimple {
   public boolean availableInMarket = false;
   public QuestSupMapWithCoords[] onMaps = new QuestSupMapWithCoords[0];

   public QuestSupItemNeed(DataInputStream dataInputStream) {
      DataInputStream dataInputStream2 = dataInputStream;
      QuestSupItemNeed questSupItemNeed = this;
      this.read(dataInputStream);
      this.availableInMarket = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.onMaps = new QuestSupMapWithCoords[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(short onMapIndex = 0; onMapIndex < questSupItemNeed.onMaps.length; ++onMapIndex) {
         questSupItemNeed.onMaps[onMapIndex] = new QuestSupMapWithCoords(dataInputStream2);
      }

   }
}
