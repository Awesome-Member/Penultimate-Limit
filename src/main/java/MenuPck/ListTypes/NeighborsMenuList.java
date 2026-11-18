package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.MystFont;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import Moduls.chat.Chat;
import battlepck.calls.BattleCallsHero;
import java.util.Vector;

public final class NeighborsMenuList extends MenuList {
   private static int duelsPlayerId;
   private static String duelsPlayerName;
   private static boolean duelsPlayerWithOther;

   public NeighborsMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      switch (id) {
         case 63:
            this.hints = Mystery.vectorFromString("Посмотри вооружение героя|Пригласи в отряд|Напади на этого героя|Приватное сообщение|Обменяйся золотом и вещами|Посмотри его фотографии");
         default:
      }
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 63:
            if (itemIndex == 0) {
               setAnimatedTitle(MenuSupport.instance.otherPlayerMenuPlayer.aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayerMenuPlayer.strategGraphic);
               return;
            } else if (itemIndex == 3) {
               setAnimatedTitle("Напади на этого героя:\r" + MenuSupport.instance.otherPlayerMenuAtackInfo);
               return;
            } else {
               super.onGenerateHintEvent(menuId, itemIndex - 1);
            }
         default:
            return;
         case 70:
            hideAvatarPortrait();
            if (itemIndex == 0) {
               setAnimatedTitle("Выбери героя из списка");
               return;
            }

            setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
            showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
            return;
         case 250:
            hideAvatarPortrait();
            if (itemIndex == 0) {
               setAnimatedTitle("Выбери дуэль из списка");
            } else {
               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
            }
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      int integerCode;
      switch (menuId) {
         case 63:
            vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            break;
         case 70:
            Com.PrBarTh.Set(true);
            NetRequest netRequest = new NetRequest();

            try {
               netRequest.writeHeader(5500000);
               Com.sendRequest(netRequest);
               if ((integerCode = netRequest.response.readInt()) != 5500001 && integerCode != 1458101) {
                  vector3.addElement("Рядом никого нет!");
                  MenuSupport.instance.otherPlayers = null;
               } else {
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  int otherPlayerIndex;
                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                  }

                  if (MenuSupport.instance.otherPlayers.length == 0) {
                     showFloatMessageS("Рядом никого нет!");
                  }

                  switch (menuId) {
                     case 70:
                        vector3.addElement("Соседи:");
                        break;
                     default:
                        vector3.addElement("Герои рядом:");
                  }

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuPlayerDescription menuPlayerDescription = MenuSupport.instance.otherPlayers[otherPlayerIndex];
                     String addText = "";
                     if (menuId == 70) {
                        if (Strategist.instance.tradeFindWithPlayerId(menuPlayerDescription.id) != null) {
                           addText = addText + "ͪͫ";
                        }

                        if (BattleCallsHero.instance.containsBattleCall(menuPlayerDescription.id)) {
                           addText = addText + "Ͱͱ";
                        }
                     }

                     vector3.addElement(addText + menuPlayerDescription.toLevelNameText());
                  }
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 250:
            vector3.addElement("Дуэли:");

            for(integerCode = 0; integerCode < MenuSupport.instance.otherPlayers.length; ++integerCode) {
               MenuPlayerDescription menuPlayerDescription2;
               if (!(menuPlayerDescription2 = MenuSupport.instance.otherPlayers[integerCode]).withNew) {
                  vector3.addElement(MystFont.colorizeText(menuPlayerDescription2.toLevelNameText(), "re"));
               } else {
                  vector3.addElement(menuPlayerDescription2.toLevelNameText());
               }
            }
      }

      return vector3;
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      if (menuId == 70 && GameCommons.instance.isFromGameMenu) {
         return -2;
      } else {
         return menuId == 63 && MenuSupport.instance.otherPlayerMenuBack == 0 ? -2 : super.onGenerateBackIdEvent(menuId);
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 63:
            switch (itemIndex) {
               case 1:
                  (new ItemsMenuList()).showOtherItemsMenu(MenuSupport.instance.otherPlayerMenuPlayer.race, MenuSupport.instance.otherPlayerMenuPlayer.gender, MenuSupport.instance.otherPlayerMenuIcons, MenuSupport.instance.otherPlayerMenuItems, MenuSupport.instance.otherPlayerMenuItemsInfos, MenuSupport.instance.otherPlayerMenuItemsCont, MenuSupport.instance.otherPlayerMenuPlayer.strategGraphic);
                  return;
               case 2:
                  Com.PrBarTh.Set(true);
                  NetRequest netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(7900000);
                     netRequest.request.writeInt(MenuSupport.instance.otherPlayerMenuPlayer.id);
                     Com.sendRequest(netRequest);
                     if ((menuId = netRequest.response.readInt()) == 7900001) {
                        GameCommons.instance.showFloatText((String)"Приглашение\rотправлено", 3000);
                     } else if (menuId == 7900004) {
                        GameCommons.instance.showFloatText((String)"Частые приглашения, попробуйте позже", 3000);
                     } else if (menuId == 7900003) {
                        GameCommons.instance.showFloatText((String)"Автоотказ\rот приглашения", 3000);
                     } else {
                        GameCommons.instance.showFloatText((String)"Приглашение\rне удалось", 3000);
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
                  return;
               case 3:
                  if (MenuSupport.instance.otherPlayerMenuPeace) {
                     showFloatMessageS("Здесь нельзя нападать");
                     return;
                  }

                  this.showConfirmationForMenu(144, "Напасть на этого игрока?");
                  return;
               case 4:
                  Chat.instance.startPrivateChat(MenuSupport.instance.otherPlayerMenuPlayer.id);
                  return;
               case 5:
                  TradeMenuList.instance.tradeMenuStartNew(-1, MenuSupport.instance.otherPlayerMenuPlayer.id, 63);
                  return;
               case 6:
                  MenuSupport instance2 = MenuSupport.instance;
                  instance2.tempStartegId = instance2.otherPlayerMenuPlayer.id;
                  byte menuId2 = 52;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               default:
                  return;
            }
         case 70:
            if (itemIndex > 0) {
               Com.startOnPlayerClick(MenuSupport.instance.otherPlayers[itemIndex - 1].id, 1);
               return;
            }
            break;
         case 250:
            if (itemIndex > 0) {
               MenuPlayerDescription menuPlayerDescription = MenuSupport.instance.otherPlayers[itemIndex - 1];
               this.showOneDuelAction(menuPlayerDescription.id, menuPlayerDescription.name, menuPlayerDescription.withNew, true);
               return;
            }
      }

   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      switch (backIndex) {
         case 144:
            this.atackTargetPlayer(res, 63, MenuSupport.instance.otherPlayerMenuPlayer.id);
         default:
            return;
         case 251:
            if (res) {
               this.removeDuel(duelsPlayerId, duelsPlayerWithOther);
               return;
            } else {
               if (duelsPlayerWithOther) {
                  short menuId = 250;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               }

               MenuManager.instance.guiHandler.hideAllWindows();
               return;
            }
         case 252:
            if (res) {
               this.atackTargetPlayer(true, 250, duelsPlayerId);
            } else {
               this.removeDuel(duelsPlayerId, duelsPlayerWithOther);
            }
      }
   }

   private void removeDuel(int playerId, boolean wasOtherPlayers) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(1393000);
         netRequest.request.writeInt(duelsPlayerId);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 1393001) {
            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
            int selectedPlayerId = -1;
            if (wasOtherPlayers) {
               for(int otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                  if (MenuSupport.instance.otherPlayers[otherPlayerIndex].id == playerId) {
                     selectedPlayerId = otherPlayerIndex;
                     break;
                  }
               }
            }

            if (selectedPlayerId >= 0) {
               MenuPlayerDescription[] tmp = new MenuPlayerDescription[MenuSupport.instance.otherPlayers.length - 1];
               System.arraycopy(MenuSupport.instance.otherPlayers, 0, tmp, 0, selectedPlayerId);
               if (tmp.length - selectedPlayerId > 0) {
                  System.arraycopy(MenuSupport.instance.otherPlayers, selectedPlayerId + 1, tmp, selectedPlayerId, tmp.length - selectedPlayerId);
               }

               MenuSupport.instance.otherPlayers = tmp;
            }
         }

         if (wasOtherPlayers) {
            if (MenuSupport.instance.otherPlayers.length > 0) {
               short menuId = 250;
               MenuManager.instance.showMenuById(menuId);
            } else {
               byte menuId2 = 36;
               MenuManager.instance.showMenuById(menuId2);
            }
         } else {
            MenuManager.instance.guiHandler.hideAllWindows();
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
   }

   private void atackTargetPlayer(boolean res, int backMenu, int playerId) {
      if (res) {
         switch (Com.touchControlType) {
            case 4:
               Com.GameTh.gameControllDirectData.activateAtackToPlayer(playerId);
               break;
            default:
               Com.GameTh.gameControllAnalogData.activeForAtackPlayerId = playerId;
               showFloatMessageS("Коснись врага!");
         }

         MenuManager.instance.guiHandler.hideAllWindows();
      } else {
         MenuManager.instance.showMenuById(backMenu);
      }
   }

   public static void showDuelsMenu() {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(1394000);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 1394001) {
            MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

            for(int otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
               MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
               MenuSupport.instance.otherPlayers[otherPlayerIndex].withNew = netRequest.response.readBoolean();
            }
         } else {
            MenuSupport.instance.otherPlayers = new MenuPlayerDescription[0];
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
      MenuManager.instance.showMenuById(250);
   }

   public final void showOneDuelAction(int playerId, String playerName, boolean initiator, boolean isShowFromMenu) {
      duelsPlayerId = playerId;
      duelsPlayerName = playerName;
      duelsPlayerWithOther = isShowFromMenu;
      if (initiator) {
         this.showConfirmationForMenu(251, Mystery.stringFormat("Убрать предложение дуэли\n%", duelsPlayerName));
      } else {
         this.showConfirmationForMenu(252, Mystery.stringFormat("Начать дуэль с \n%", duelsPlayerName));
      }
   }
}
