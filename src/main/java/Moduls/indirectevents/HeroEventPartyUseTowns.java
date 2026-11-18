package Moduls.indirectevents;

import Base.Com;
import Moduls.Town;
import Moduls.quests.QuestStrateg;
import java.io.DataInputStream;

public final class HeroEventPartyUseTowns extends HeroEvent {
   private int[] townsList;
   private int[][] creatingDecsList;

   public final void loadFromStream(DataInputStream dataInputStream) {
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.townsList = new int[shortCode];

      int townsListIndex;
      for(townsListIndex = 0; townsListIndex < shortCode; ++townsListIndex) {
         this.townsList[townsListIndex] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      }

      shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.creatingDecsList = new int[shortCode][];

      for(townsListIndex = 0; townsListIndex < shortCode; ++townsListIndex) {
         this.creatingDecsList[townsListIndex] = new int[3];
         this.creatingDecsList[townsListIndex][0] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         this.creatingDecsList[townsListIndex][1] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         this.creatingDecsList[townsListIndex][2] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      }

   }

   public final void apply() {
      for(int townsListIndex = 0; townsListIndex < this.townsList.length; ++townsListIndex) {
         int townsList2;
         Town town;
         if ((townsList2 = this.townsList[townsListIndex]) >= 0 && townsList2 < Com.currentGameMap.towns.length && (town = Com.currentGameMap.towns[townsList2]).type == 406 && town.state == 0) {
            town.state = 1;
            QuestStrateg.instance.recordDecoratorUse(Com.currentGameMap.nameId, (short)townsList2, town.typeIdName);
         }
      }

      Com.currentGameMap.addDecors(this.creatingDecsList, true);
   }

   public final void afterApply() {
   }
}
