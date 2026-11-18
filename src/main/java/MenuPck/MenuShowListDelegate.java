package MenuPck;

import Engine.Delegateable;

public final class MenuShowListDelegate implements Delegateable {
   private int menuId = 196;

   public MenuShowListDelegate(int menuId2) {
   }

   public final void executeDelegated(int context) {
      if (this.menuId >= 0) {
         MenuManager.instance.showMenuById(this.menuId);
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }
   }
}
