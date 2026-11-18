package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Delegate;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import Moduls.StrategRace;
import Moduls.Strategist;
import java.util.Vector;

public final class AvatarMenuList extends MenuList {
   public AvatarMenuList(int id, int backMenu, String[] list) {
      super(2, 1, (String[])list);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      this.hints = new Vector();
      this.clearSkips();
      int i;
      StrategRace strategRace;
      if (MenuSupport.instance.isCreateStrategFromServiceMenu) {
         for(i = 0; i < StrategRace.strategRaces.length; ++i) {
            strategRace = StrategRace.strategRaces[i];
            vector3.addElement(strategRace.names[Strategist.instance.GP_Gender]);
            this.hints.addElement(strategRace.descriptions[Strategist.instance.GP_Gender]);
            if (!strategRace.availableForPlayer) {
               this.addSkip(vector3.size() - 1);
            }
         }
      } else {
         for(i = 0; i < StrategRace.strategRaces.length; ++i) {
            strategRace = StrategRace.strategRaces[i];
            vector3.addElement(strategRace.names[0]);
            vector3.addElement(strategRace.names[1]);
            this.hints.addElement(strategRace.descriptions[0]);
            this.hints.addElement(strategRace.descriptions[1]);
            if (!strategRace.availableForPlayer) {
               this.addSkip(vector3.size() - 2);
               this.addSkip(vector3.size() - 1);
            }
         }
      }

      return vector3;
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      setAnimatedTitle(this.hints.elementAt(itemIndex).toString());
      if (MenuSupport.instance.isCreateStrategFromServiceMenu) {
         showAvatarPortrait(StrategRace.strategRaces[itemIndex].getStrategGraphic(Strategist.instance.GP_Gender));
      } else {
         showAvatarPortrait(StrategRace.strategRaces[itemIndex / 2].getStrategGraphic(itemIndex % 2));
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return MenuSupport.instance.isCreateStrategFromServiceMenu ? 37 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      switch (backIndex) {
         case 108:
            if (res) {
               if (Strategist.instance.GP_Crystals < Com.c_serv_class_cost) {
                  ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_serv_class_cost);
                  return;
               }

               Com.PrBarTh.Set(true);
               NetRequest netRequest;
               (netRequest = new NetRequest()).writeHeader(1457200);
               decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, Com.newg_sprite);
               Com.sendRequest(netRequest);

               try {
                  switch (netRequest.response.readInt()) {
                     case 1457201:
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                        break;
                     case 1457202:
                        GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 2000);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            byte menuId = 37;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      if (MenuSupport.instance.isCreateStrategFromServiceMenu) {
         menuId = itemIndex;
         itemIndex = Strategist.instance.GP_Gender;
      } else {
         menuId = itemIndex / 2;
         itemIndex %= 2;
      }

      if (MenuSupport.instance.isCreateStrategFromServiceMenu) {
         if (menuId == Strategist.instance.GP_Race) {
            GameCommons.instance.showFloatText((String)"Это твоя раса", 4000);
         } else {
            Com.newg_sprite = menuId;
            if (Strategist.instance.GP_Crystals < Com.c_serv_class_cost) {
               ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_serv_class_cost);
            } else {
               this.showConfirmationForMenu(108, "Цена " + Integer.toString(Com.c_serv_class_cost) + "ɾɿ. Сменить расу героя?");
            }
         }
      } else {
         Com.newg_sprite = menuId;
         Com.newg_pol = itemIndex;
         (new OtherMenuList()).showWorldMapMenu();
      }
   }
}
