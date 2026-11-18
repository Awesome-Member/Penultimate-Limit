package MenuPck.ListTypes;

import Base.GameCommons;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;
import Moduls.Strategist;

public final class StrategMenuList extends MenuList {
   public StrategMenuList(int id, int backMenu, String list) {
      super(4, 0, (String)list);
      this.hints = Mystery.vectorFromString("Твои качества|Посмотри свои умения|Твои квесты|Фотографии");
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      byte menuId2;
      switch (itemIndex) {
         case 0:
            (new MainMenuList()).showHeroMenu();
            return;
         case 1:
            menuId2 = 7;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 2:
            OtherMenuList.showQuestsMenu(false);
            return;
         case 3:
            MenuSupport.instance.tempStartegId = Strategist.instance.id;
            menuId2 = 67;
            MenuManager.instance.showMenuById(menuId2);
         default:
      }
   }
}
