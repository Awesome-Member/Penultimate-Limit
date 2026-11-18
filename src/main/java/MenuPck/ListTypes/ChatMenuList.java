package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import Moduls.chat.Chat;
import java.util.Vector;

public final class ChatMenuList extends MenuList {
   private static int menu59Ind;
   private static MenuPlayerDescription curPlayer;
   private static boolean needUpdateFriendsList = true;

   public ChatMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      switch (id) {
         case 58:
            this.hints = Mystery.vectorFromString("Чат локации. Запрещены: мат, грубость, оскорбления, реклама других проектов. Бан за: куплю, продажу, обмен, передачу героев; сделки за реальные деньги.|Общайся со своим кланом|Общайся со своим отрядом|Приватные сообщения|Просмотри список твоих друзей, добавь новых или удали старых|Добавить игрока в игнорируемые или убрать из игнорируемых");
         default:
      }
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 59:
            hideAvatarPortrait();
            if (itemIndex == MenuSupport.instance.otherPlayers.length + 1) {
               setAnimatedTitle("Введи имя того, с кем ты хочешь начать общение в привате.");
               return;
            } else if (itemIndex == 0) {
               setAnimatedTitle("Для привата, выбери героя из списка");
               return;
            } else {
               if (MenuSupport.instance.otherPlayers[itemIndex - 1].id == -1) {
                  setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
                  return;
               }

               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
               return;
            }
         case 90:
            hideAvatarPortrait();
            if (itemIndex == this.getCurrentMenuListsCount() - 2) {
               setAnimatedTitle("Введи имя того, кого ты хочешь игнорировать");
               return;
            } else if (itemIndex == 0) {
               setAnimatedTitle("Для игнорирования, выбери героя из списка");
               return;
            } else {
               if (itemIndex == this.getCurrentMenuListsCount() - 1) {
                  setAnimatedTitle("Выбери, чтобы увидеть список заигнорированных");
                  return;
               }

               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
               return;
            }
         case 91:
            hideAvatarPortrait();
            if (itemIndex == 0) {
               setAnimatedTitle("Для того, чтобы убрать из игнорируемых, выбери героя из списка");
               return;
            }

            setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
            showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
            return;
         case 216:
            hideAvatarPortrait();
            if (itemIndex == this.getCurrentMenuListsCount() - 1) {
               setAnimatedTitle("Выбери, чтобы убрать желаемого героя из списка друзей");
               return;
            } else if (itemIndex == this.getCurrentMenuListsCount() - 2) {
               setAnimatedTitle("Выбери, чтобы добавить желаемого героя в список друзей");
               return;
            } else {
               if (itemIndex == 0) {
                  setAnimatedTitle(Mystery.stringFormat("У тебя в друзьях % героев", '%', new String[]{String.valueOf(MenuSupport.instance.otherPlayers.length)}));
                  return;
               }

               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
               return;
            }
         case 217:
            hideAvatarPortrait();
            if (itemIndex == this.getCurrentMenuListsCount() - 1) {
               setAnimatedTitle("Введи имя того, кого ты хочешь добавить в список друзей");
               return;
            } else {
               if (itemIndex == 0) {
                  setAnimatedTitle("Для добавления в друзья, выбери героя из списка");
                  return;
               }

               setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
               showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex - 1].strategGraphic);
               return;
            }
         case 218:
            hideAvatarPortrait();
            setAnimatedTitle(MenuSupport.instance.otherPlayers[itemIndex].aboutMe);
            showAvatarPortrait(MenuSupport.instance.otherPlayers[itemIndex].strategGraphic);
            return;
         default:
            super.onGenerateHintEvent(menuId, itemIndex);
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = null;
      NetRequest netRequest;
      int[] lastWriters;
      int otherPlayerIndex;
      switch (menuId) {
         case 58:
            this.clearSkips();
            if (!Strategist.instance.isInClan) {
               this.addSkip(1);
            }

            if (Strategist.instance.partyState == 2) {
               this.addSkip(2);
            }

            vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            break;
         case 59:
            vector3 = new Vector();
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4520000);
               netRequest.request.writeInt(0);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() != 4520001) {
                  vector3.addElement("Рядом никого нет!");
               } else {
                  vector3.addElement("Приваты:");
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                     MenuSupport.instance.otherPlayers[otherPlayerIndex].withNew = netRequest.response.readBoolean();
                     vector3.addElement((MenuSupport.instance.otherPlayers[otherPlayerIndex].withNew ? "ʄʅ" : "") + (MenuSupport.instance.otherPlayers[otherPlayerIndex].id == -1 ? MenuSupport.instance.otherPlayers[otherPlayerIndex].name : MenuSupport.instance.otherPlayers[otherPlayerIndex].toLevelNameText()));
                  }

                  vector3.addElement("Введи имя");
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 90:
            vector3 = new Vector();
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               lastWriters = Chat.instance.getLastWriters();
               netRequest.writeHeader(4540000);
               netRequest.request.writeInt(lastWriters.length);

               for(otherPlayerIndex = 0; otherPlayerIndex < lastWriters.length; ++otherPlayerIndex) {
                  netRequest.request.writeInt(lastWriters[otherPlayerIndex]);
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() != 4540001) {
                  vector3.addElement("Ошибка");
               } else {
                  vector3.addElement("Пишущие в чат:");
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(menuId = 0; menuId < MenuSupport.instance.otherPlayers.length; ++menuId) {
                     MenuSupport.instance.otherPlayers[menuId] = new MenuPlayerDescription(netRequest.response);
                     vector3.addElement(MenuSupport.instance.otherPlayers[menuId].toLevelNameText());
                  }

                  vector3.addElement("Введи имя");
                  vector3.addElement("Твои игноры");
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 91:
            vector3 = new Vector();
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4560000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4560001) {
                  vector3.addElement("Игнорируемые:");
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                     MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                     vector3.addElement(MenuSupport.instance.otherPlayers[otherPlayerIndex].toLevelNameText());
                  }
               } else {
                  vector3.addElement("Ошибка");
               }
            } catch (Exception exception3) {
               exception3.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 216:
         case 218:
            if (needUpdateFriendsList) {
               vector3 = new Vector();
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4570000);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() != 4570001) {
                     vector3.addElement("Ошибка");
                  } else {
                     MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                     for(otherPlayerIndex = 0; otherPlayerIndex < MenuSupport.instance.otherPlayers.length; ++otherPlayerIndex) {
                        MenuSupport.instance.otherPlayers[otherPlayerIndex] = new MenuPlayerDescription(netRequest.response);
                     }

                     switch (menuId) {
                        case 216:
                           this.formFriendsList(vector3);
                           break;
                        case 218:
                           vector3 = this.formRemoveFriendsList(vector3);
                     }
                  }
               } catch (Exception exception4) {
                  exception4.printStackTrace();
               }

               Com.PrBarTh.Stop();
            } else {
               switch (menuId) {
                  case 216:
                     this.formFriendsList((Vector)null);
                     return vector3;
                  case 218:
                     vector3 = this.formRemoveFriendsList((Vector)null);
               }
            }
            break;
         case 217:
            vector3 = new Vector();
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               lastWriters = Chat.instance.getLastWriters();
               netRequest.writeHeader(4540000);
               netRequest.request.writeInt(lastWriters.length);

               for(otherPlayerIndex = 0; otherPlayerIndex < lastWriters.length; ++otherPlayerIndex) {
                  netRequest.request.writeInt(lastWriters[otherPlayerIndex]);
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() != 4540001) {
                  vector3.addElement("Ошибка");
               } else {
                  vector3.addElement("Пишущие в чат:");
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[netRequest.response.readInt()];

                  for(menuId = 0; menuId < MenuSupport.instance.otherPlayers.length; ++menuId) {
                     MenuSupport.instance.otherPlayers[menuId] = new MenuPlayerDescription(netRequest.response);
                     vector3.addElement(MenuSupport.instance.otherPlayers[menuId].toLevelNameText());
                  }

                  vector3.addElement("Введи имя");
               }
            } catch (Exception exception5) {
               exception5.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         default:
            vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      }

      return vector3;
   }

   private void formFriendsList(Vector vector3) {
      vector3.addElement("Твои друзья:");

      for(int i = 0; i < MenuSupport.instance.otherPlayers.length; ++i) {
         vector3.addElement(MenuSupport.instance.otherPlayers[i].toLevelNameText());
      }

      vector3.addElement("Добавить в друзья");
      vector3.addElement("Убрать из друзей");
      if (MenuSupport.instance.otherPlayers.length <= 0) {
         this.addSkip(vector3.size() - 1);
      }

   }

   private Vector formRemoveFriendsList(Vector vector3) {
      for(int i = 0; i < MenuSupport.instance.otherPlayers.length; ++i) {
         vector3.addElement(MenuSupport.instance.otherPlayers[i].toLevelNameText());
      }

      if (MenuSupport.instance.otherPlayers.length <= 0) {
         vector3.addElement("Список пуст");
         short menuId = 216;
         MenuManager.instance.showMenuById(menuId);
         return LIST_IGNORE;
      } else {
         return vector3;
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      short menuId;
      byte menuId2;
      switch (backIndex) {
         case 111:
            if (res) {
               setRelation(curPlayer.id, 2);
            }

            menuId2 = 90;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 112:
            if (res) {
               setRelation(curPlayer.id, 0);
            }

            menuId2 = 91;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 219:
            if (res) {
               needUpdateFriendsList = true;
               setRelation(curPlayer.id, 0);
            }

            menuId = 218;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 221:
            if (res) {
               needUpdateFriendsList = true;
               setRelation(curPlayer.id, 1);
            }

            menuId = 217;
            MenuManager.instance.showMenuById(menuId);
            return;
         default:
            if (res) {
               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1459000);
                  netRequest.request.writeUTF("Бардак в чате!");
                  netRequest.request.writeInt(1);
                  Com.sendRequest(netRequest);
                  showFloatMessageS("Сообщение отправлено");
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId2 = 58;
            MenuManager.instance.showMenuById(menuId2);
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return (menuId == 58 || menuId == 59) && GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      short menuId2;
      byte menuId3;
      switch (menuId) {
         case 58:
            switch (itemIndex) {
               case 0:
               case 1:
                  if (itemIndex == 0 && Strategist.instance.GP_Level < 2) {
                     showFloatMessageS("Нужно быть уровня 2");
                     return;
                  }

                  if (itemIndex == 0) {
                     Chat.instance.startCommonChat();
                  }

                  if (itemIndex == 1) {
                     Chat.instance.startClanChat();
                     return;
                  }

                  return;
               case 2:
                  Chat.instance.setUnreadnParty2();
                  return;
               case 3:
                  menu59Ind = 0;
                  menuId3 = 59;
                  MenuManager.instance.showMenuById(menuId3);
                  return;
               case 4:
                  needUpdateFriendsList = true;
                  menuId2 = 216;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 5:
                  menuId3 = 90;
                  MenuManager.instance.showMenuById(menuId3);
                  return;
               default:
                  return;
            }
         case 59:
            if (itemIndex == MenuSupport.instance.otherPlayers.length + 1) {
               this.showTextEnter(110, "Напиши имя", "");
               return;
            }

            if (itemIndex > 0) {
               Chat.instance.startPrivateChat(MenuSupport.instance.otherPlayers[itemIndex - 1].id);
               return;
            }
            break;
         case 90:
            if (itemIndex == this.getCurrentMenuListsCount() - 1) {
               menuId3 = 91;
               MenuManager.instance.showMenuById(menuId3);
               return;
            }

            if (itemIndex == this.getCurrentMenuListsCount() - 2) {
               this.showTextEnter(109, "Введи имя того, кого ты хочешь игнорировать", "");
               return;
            }

            if (itemIndex > 0) {
               curPlayer = MenuSupport.instance.otherPlayers[itemIndex - 1];
               this.showConfirmationForMenu(111, "Добавить игрока " + MenuSupport.instance.otherPlayers[itemIndex - 1].name + " в список игнорируемых?");
               return;
            }
            break;
         case 91:
            if (itemIndex > 0) {
               curPlayer = MenuSupport.instance.otherPlayers[itemIndex - 1];
               this.showConfirmationForMenu(112, "Убрать игрока " + MenuSupport.instance.otherPlayers[itemIndex - 1].name + " из списка игнорируемых?");
               return;
            }
            break;
         case 216:
            if (itemIndex == this.getCurrentMenuListsCount() - 1) {
               menuId2 = 218;
               MenuManager.instance.showMenuById(menuId2);
               return;
            }

            if (itemIndex == this.getCurrentMenuListsCount() - 2) {
               needUpdateFriendsList = true;
               menuId2 = 217;
               MenuManager.instance.showMenuById(menuId2);
               return;
            }

            if (itemIndex > 0) {
               curPlayer = MenuSupport.instance.otherPlayers[itemIndex - 1];
               Chat.instance.startPrivateChat(curPlayer.id);
            }
            break;
         case 217:
            if (itemIndex == this.getCurrentMenuListsCount() - 1) {
               this.showTextEnter(220, "Введи имя того, кого ты хочешь доавить с список друзей", "");
               return;
            }

            if (itemIndex > 0) {
               curPlayer = MenuSupport.instance.otherPlayers[itemIndex - 1];
               this.showConfirmationForMenu(221, Mystery.stringFormat("Добавить % в список друзей?", '%', new String[]{curPlayer.name}));
               return;
            }
            break;
         case 218:
            curPlayer = MenuSupport.instance.otherPlayers[itemIndex];
            this.showConfirmationForMenu(219, Mystery.stringFormat("Уверен что хочешь удалить % из списка друзей?", '%', new String[]{curPlayer.name}));
            return;
      }

   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String messageText) {
      byte menuId;
      switch (backIndex) {
         case 109:
            if (res) {
               setRelation(messageText, 2);
            }

            menuId = 90;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 110:
            if (!res) {
               menuId = 58;
               MenuManager.instance.showMenuById(menuId);
               return;
            } else if (!Chat.instance.startPrivateChat(messageText)) {
               GameCommons.instance.showFloatText((String)"Игрок не найден", 3000);
            }
         default:
            return;
         case 220:
            if (res) {
               setRelation(messageText, 1);
            }

            short menuId2 = 217;
            MenuManager.instance.showMenuById(menuId2);
      }
   }

   private static void setRelation(String playerName, int setState) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4550000);
         netRequest.request.writeInt(setState);
         netRequest.request.writeUTF(playerName);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4550001) {
            GameCommons.instance.showFloatText("Успех!");
         } else {
            GameCommons.instance.showFloatText("Провал!");
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
   }

   private static void setRelation(int playerId, int setState) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4551000);
         netRequest.request.writeInt(setState);
         netRequest.request.writeInt(playerId);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4551001) {
            GameCommons.instance.showFloatText("Успех!");
         } else {
            GameCommons.instance.showFloatText("Провал!");
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
   }
}
