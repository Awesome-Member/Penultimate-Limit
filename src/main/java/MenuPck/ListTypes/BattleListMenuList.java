package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import MenuPck.MenuList;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuSupport;
import Moduls.StrategGraphic;
import battlepck.BattleManager;
import java.util.Vector;

public final class BattleListMenuList extends MenuList {
   public BattleListMenuList(int id, int backMenu, String[] list) {
      super(64, 36, (String[])list);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      hideAvatarPortrait();
      if (itemIndex == 0) {
         setAnimatedTitle("Чтобы посмотреть бой, выбери его из списка");
      } else {
         setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
         showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4623000);
         Com.sendRequest(netRequest);
         int integerCode;
         if ((integerCode = netRequest.response.readInt()) != 4623001 && integerCode != 4624001) {
            vector3.addElement("Назад");
         } else {
            MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

            for(integerCode = 0; integerCode < MenuSupport.instance.otherPlayers.length; ++integerCode) {
               MenuSupport.instance.otherPlayers[integerCode] = new MenuPlayerDescription(netRequest.response.readUTF(), netRequest.response.readUTF(), 0, new StrategGraphic(netRequest.response));
               MenuSupport.instance.otherPlayers[integerCode].id = netRequest.response.readInt();
               MenuSupport.instance.otherPlayers[integerCode].time = netRequest.response.readUTF();
               MenuSupport.instance.otherPlayers[integerCode].logId = netRequest.response.readUTF();
            }

            vector3.addElement("Бои:");

            for(integerCode = 0; integerCode < MenuSupport.instance.otherPlayers.length; ++integerCode) {
               vector3.addElement(MenuSupport.instance.otherPlayers[integerCode].name + " " + MenuSupport.instance.otherPlayers[integerCode].time);
            }
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
      return vector3;
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      if (itemIndex > 0) {
         BattleManager.startWatchBattle(MenuSupport.instance.otherPlayers[itemIndex - 1].logId);
      }

   }
}
