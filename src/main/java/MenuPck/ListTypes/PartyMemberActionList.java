package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import java.util.Vector;

public final class PartyMemberActionList extends MenuList {
   public PartyMemberActionList(int id, int backMenu, String list) {
      super(88, 87, (String)list);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (itemIndex) {
         case 0:
            setAnimatedTitle("Выгнать из отряда");
            return;
         case 1:
            setAnimatedTitle("Сделать лидером отряда");
            return;
         case 2:
            setAnimatedTitle("Покинуть отряд");
         default:
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      this.clearSkips();
      if (Strategist.instance.partyState != 1 || MenuSupport.instance.curFriend == Strategist.instance.id) {
         this.addSkip(0);
         this.addSkip(1);
      }

      return super.onGenerateItemsAndSkipsEvent(menuId);
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      int curFriend2 = MenuSupport.instance.curFriend;
      NetRequest netRequest;
      byte menuId;
      switch (backIndex) {
         case 146:
         case 148:
            if (backIndex == 148) {
               curFriend2 = Strategist.instance.id;
            }

            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(7920000);
                  netRequest.request.writeInt(curFriend2);
                  Com.sendRequest(netRequest);
                  if ((backIndex = netRequest.response.readInt()) == 7920001) {
                     if (curFriend2 == Strategist.instance.id) {
                        Strategist.instance.partyState = 2;
                        Strategist.instance.partyId = -1;
                     }
                  } else if (backIndex == 7920002) {
                     GameCommons.instance.showFloatText((String)"Провал!", 3000);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               menuId = 36;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            menuId = 87;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 147:
            if (res && curFriend2 != Strategist.instance.id) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(7930000);
                  netRequest.request.writeInt(curFriend2);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 7930002) {
                     GameCommons.instance.showFloatText((String)"Провал!", 3000);
                  }
               } catch (Exception exception2) {
                  exception2.printStackTrace();
               }

               Com.PrBarTh.Stop();
               menuId = 87;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (itemIndex) {
         case 0:
            this.showConfirmationForMenu(146, "Выгнать из отряда?");
            return;
         case 1:
            this.showConfirmationForMenu(147, "Сделать лидером отряда?");
            return;
         case 2:
            this.showConfirmationForMenu(148, "Покинуть отряд?");
         default:
      }
   }
}
