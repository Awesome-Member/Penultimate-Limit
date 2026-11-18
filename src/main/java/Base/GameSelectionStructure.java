package Base;

import Moduls.otherheroes.HeroOnMap;

public final class GameSelectionStructure {
   public boolean selectStrateg = false;
   public int selectedOtherStrategId = -1;
   public HeroOnMap selectedOtherStrategObj = null;
   public int selectedEnemyInd = -1;
   public int selectedTown = -1;
   public int selectedMapMove = -1;
   public int selectedMapMoveX = -1;
   public int selectedMapMoveY = -1;

   public final void reset() {
      this.selectStrateg = false;
      this.selectedOtherStrategId = -1;
      this.selectedOtherStrategObj = null;
      this.selectedEnemyInd = -1;
      this.selectedTown = -1;
      this.selectedMapMove = -1;
      this.selectedMapMoveX = -1;
      this.selectedMapMoveY = -1;
   }

   public final void onEndFunctionate() {
      if (this.selectedOtherStrategObj == null) {
         this.selectedOtherStrategId = -1;
      }

   }
}
