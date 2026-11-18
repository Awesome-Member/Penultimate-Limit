package MenuPck;

import Engine.Delegateable;

public final class MenuSnapshotDelegate implements Delegateable {
   private MenuListSnapshot snapshot = null;

   public MenuSnapshotDelegate() {
   }

   public MenuSnapshotDelegate(MenuList menuList2) {
      this.snapshot = menuList2 == null ? null : menuList2.makeSnapshot();
   }

   public final void executeDelegated(int context) {
      if (this.snapshot != null) {
         this.snapshot.restore();
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }

      this.snapshot = null;
   }
}
