package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Engine.Delegate;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import java.util.Vector;

public final class ClanMembersMenuList extends MenuList {
   private String clanInfo = "";

   public ClanMembersMenuList(int id, int backMenu, String[] list) {
      super(id, backMenu, list);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      int otherPlayerIndex;
      NetRequest netRequest;
      switch (menuId) {
         case 51:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               if (MenuSupport.instance.isMembersForClanRequest) {
                  netRequest.writeHeader(4470000);
               } else {
                  netRequest.writeHeader(4440000);
               }

               Com.sendRequest(netRequest);
               int integerCode;
               if ((integerCode = netRequest.response.readInt()) == 4440001 || integerCode == 4470001) {
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                  }

                  if (MenuSupport.instance.isMembersForClanRequest) {
                     vector3.addElement("Заявки:");
                  } else {
                     vector3.addElement("Друзья:");
                  }

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     vector3.addElement("(" + Integer.toString(MenuSupport.instance.otherPlayers[otherPlayerIndex].level) + ")" + MenuSupport.instance.otherPlayers[otherPlayerIndex].name);
                  }
               }

               if (integerCode == 4341002) {
                  vector3.addElement("Назад");
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 81:
            this.clearSkips();
            if (Strategist.instance.isInClan) {
               this.addSkip(0);
            }

            vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(6500000);
               netRequest.request.writeUTF(MenuSupport.instance.curSelectedClanName);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() != 6500001) {
                  MenuManager.instance.guiHandler.hideAllWindows();
               } else {
                  this.clanInfo = netRequest.response.readUTF();
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                  }

                  vector3.addElement("ɾɿДать заявку");
                  vector3.addElement("Всего " + MenuSupport.instance.otherPlayers.length + " человек:");

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     vector3.addElement("(" + Integer.toString(MenuSupport.instance.otherPlayers[otherPlayerIndex].level) + ")" + MenuSupport.instance.otherPlayers[otherPlayerIndex].name);
                  }
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
      }

      return vector3;
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 51:
            hideAvatarPortrait();
            if (itemIndex == 0) {
               if (MenuSupport.instance.isMembersForClanRequest) {
                  if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanRecruiter) {
                     setAnimatedTitle("У тебя недостаточно прав, чтобы принимать заявки, ты можешь только смотреть кто просится в клан");
                     return;
                  }

                  setAnimatedTitle("Для действий, выбери заявку из списка");
                  return;
               }

               setAnimatedTitle("Для действий, выбери героя из списка");
               return;
            }

            setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
            showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
            return;
         case 81:
            hideAvatarPortrait();
            if (itemIndex == 0) {
               String messageText = "Подай заявку на вступление в клан. Цена " + Com.c_clan_join_cost;
               setAnimatedTitle(messageText + "ɾɿ\rПреимущества клана:\n- низкие налоги\n- бонусы от цитадели\n- общий клановый приват\n- призыв друга к себе\n- помощь при нападении\n- клановая иконка");
               return;
            } else if (itemIndex == 1) {
               setAnimatedTitle(this.clanInfo);
               return;
            } else {
               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 2].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 2].strategGraphic);
            }
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      switch (backIndex) {
         case 113:
            if (res) {
               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4430000);
                  netRequest.request.writeUTF(MenuSupport.instance.curSelectedClanName);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4430001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            byte menuId = 81;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 51:
            if (itemIndex > 0) {
               MenuSupport instance2 = MenuSupport.instance;
               instance2.curFriend = instance2.otherPlayers[itemIndex - 1].id;
               if (!MenuSupport.instance.isMembersForClanRequest) {
                  byte menuId2 = 55;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               }

               if (MenuSupport.instance.isMembersForClanRequest && (ClanMenuList.isClanLeader || ClanMenuList.isClanRecruiter)) {
                  (new OtherMenuList()).showConfirmationForMenu(53, "Принять этого героя в клан?");
                  return;
               }
            }
            break;
         case 81:
            if (itemIndex == 0) {
               if (Strategist.instance.GP_Level < Com.c_clan_join_lvl) {
                  showFloatMessageS("Нужно быть уровня " + Com.c_clan_join_lvl);
                  return;
               }

               if (Strategist.instance.GP_Crystals < Com.c_clan_join_cost) {
                  ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_clan_join_cost);
                  return;
               }

               this.showConfirmationForMenu(113, "Уверен что хочешь подать заявку в клан " + MenuSupport.instance.curSelectedClanName + "? Это будет стоить " + Com.c_clan_join_cost + "ɾɿ");
            }
      }

   }
}
