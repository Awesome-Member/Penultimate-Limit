package MenuPck.ListTypes;

import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;

public final class FirstMenuList extends MenuList {
   public FirstMenuList(int id, int backMenu, String list) {
      super(1, -1, (String)list);
      this.hints = Mystery.vectorFromString("Выбери и начни новую игру|Продолжи игру с места последнего выхода|Выбери для получения пароля на почту|Выйти из игры");
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (itemIndex) {
         case 0:
            MenuSupport.instance.newGameMode = 1;
            MenuSupport.instance.isCreateStrategFromServiceMenu = false;
            byte menuId2 = 2;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 1:
            MenuSupport.instance.newGameMode = 0;
            (new OtherMenuList()).showWorldMapMenu();
            return;
         case 2:
            MenuSupport.instance.newGameMode = 2;
            (new OtherMenuList()).showWorldMapMenu();
            return;
         case 3:
            (new OtherMenuList()).showConfirmationForMenu(169, "Выйти из игры?");
         default:
      }
   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String messageText) {
   }
}
