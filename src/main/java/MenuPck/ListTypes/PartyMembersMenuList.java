package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuSupport;
import java.util.Vector;

public final class PartyMembersMenuList extends MenuList {
   public PartyMembersMenuList(int id, int backMenu, String[] list) {
      super(87, 36, (String[])list);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex].aboutMe);
      showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex].strategGraphic);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(7940000);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 7940001) {
            MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

            int otherPlayerIndex;
            for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
               MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
            }

            for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
               vector3.addElement("(" + Integer.toString(MenuSupport.instance.otherPlayers[otherPlayerIndex].level) + ")" + MenuSupport.instance.otherPlayers[otherPlayerIndex].name);
            }
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
      return vector3;
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      MenuSupport instance2 = MenuSupport.instance;
      instance2.curFriend = instance2.otherPlayers[itemIndex].id;
      byte menuId2 = 88;
      MenuManager.instance.showMenuById(menuId2);
   }
}
