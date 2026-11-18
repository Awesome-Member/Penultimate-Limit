package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import Engine.Delegate;
import Engine.MathGame;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import java.util.Vector;

public final class LiederActionsMenuList extends MenuList {
   private static boolean isClanGeneral;
   private static boolean isClanHerald;
   private static boolean isClanInquirer;
   private static boolean isClanPathfinder;
   private static boolean isClanRecruiter;
   private static boolean isClanStudent;
   private static boolean isClanTreasurer;
   private int goldCountToClaner = 0;
   private static String clanTrespasses = "";
   private static String clanPvPFights = "";
   private static String clanPvEFights = "";

   public LiederActionsMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      switch (menuId) {
         case 55:
            actualizeFriendInfo();
            this.clearSkips();
            int countSkips = 0;
            if (MenuSupport.instance.curFriend == Strategist.instance.id) {
               this.addSkip(0);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanTreasurer) {
               this.addSkip(1);
               ++countSkips;
            }

            if (MenuSupport.instance.curFriend == Strategist.instance.id) {
               this.addSkip(2);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanRecruiter || MenuSupport.instance.curFriend == Strategist.instance.id) {
               this.addSkip(3);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanGeneral) {
               this.addSkip(4);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanInquirer) {
               this.addSkip(5);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader && !ClanMenuList.isClanPathfinder) {
               this.addSkip(6);
               ++countSkips;
               this.addSkip(7);
               ++countSkips;
            }

            if (!ClanMenuList.isClanLeader || MenuSupport.instance.curFriend == Strategist.instance.id) {
               this.addSkip(8);
               ++countSkips;
            }

            if (countSkips < 9) {
               this.addSkip(9);
            }

            return super.onGenerateItemsAndSkipsEvent(menuId);
         case 57:
            Vector vector3 = new Vector();
            this.clearSkips();
            if (!ClanMenuList.isClanLeader) {
               this.addSkip(0);
            }

            vector3.addElement(isClanGeneral ? "Отнять Генерала" : "Сделать Генералом");
            vector3.addElement(isClanStudent ? "Отнять Курсанта" : "Сделать Курсантом");
            vector3.addElement(isClanRecruiter ? "Отнять Вербовщика" : "Сделать Вербовщиком");
            vector3.addElement(isClanHerald ? "Отнять Герольда" : "Сделать Герольдом");
            vector3.addElement(isClanTreasurer ? "Отнять Казначея" : "Сделать Казначеем");
            vector3.addElement(isClanInquirer ? "Отнять Дознавателя" : "Сделать Дознавателем");
            vector3.addElement(isClanPathfinder ? "Отнять Следопыта" : "Сделать Следопытом");
            return vector3;
         default:
            return super.onGenerateItemsAndSkipsEvent(menuId);
      }
   }

   private static void actualizeFriendInfo() {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.request.writeInt(4490030);
         netRequest.request.writeInt(MenuSupport.instance.curFriend);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4490031) {
            netRequest.response.readBoolean();
            isClanGeneral = netRequest.response.readBoolean();
            isClanStudent = netRequest.response.readBoolean();
            isClanRecruiter = netRequest.response.readBoolean();
            isClanHerald = netRequest.response.readBoolean();
            isClanTreasurer = netRequest.response.readBoolean();
            isClanInquirer = netRequest.response.readBoolean();
            isClanPathfinder = netRequest.response.readBoolean();
            clanTrespasses = netRequest.response.readUTF();
            clanPvPFights = netRequest.response.readUTF();
            clanPvEFights = netRequest.response.readUTF();
         } else {
            MenuManager.instance.guiHandler.hideAllWindows();
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.PrBarTh.Stop();
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 55:
            switch (itemIndex) {
               case 0:
                  setAnimatedTitle("Переместиться к другу. Цена " + Com.c_clan_teleport_cost + "ɾɿ");
                  return;
               case 1:
                  if (ClanMenuList.isClanLeader) {
                     setAnimatedTitle("Дать любую сумму из казны этому соклановцу");
                  } else {
                     setAnimatedTitle(Mystery.stringFormat("Дать любую сумму из казны этому соклановцу, но не больше $% в день от казны в сумме на все переводы", '$', new String[]{Integer.toString(Com.c_clan_treasure_max_percents_per_day)}));
                  }

                  return;
               case 2:
                  setAnimatedTitle("Обменяйся золотом и вещами");
                  return;
               case 3:
                  setAnimatedTitle("Исключи его из клана");
                  return;
               case 4:
                  setAnimatedTitle("Забрать или дать звание");
                  return;
               case 5:
                  if (clanTrespasses.length() > 0) {
                     setAnimatedTitle("Совершенные переходы:\n" + clanTrespasses);
                  } else {
                     setAnimatedTitle("Переходов не было");
                  }

                  return;
               case 6:
                  if (clanPvEFights.length() > 0) {
                     setAnimatedTitle("Совершенные бои:\n" + clanPvEFights);
                  } else {
                     setAnimatedTitle("Боев не было");
                  }

                  return;
               case 7:
                  if (clanPvPFights.length() > 0) {
                     setAnimatedTitle("Совершенные бои:\n" + clanPvPFights);
                  } else {
                     setAnimatedTitle("Боев не было");
                  }

                  return;
               case 8:
                  setAnimatedTitle("Отдать ему власть над кланом, ты перестанешь быть лидером");
                  return;
               case 9:
                  setAnimatedTitle("Это ты");
               default:
                  return;
            }
         case 57:
            setAnimatedTitle("Назначь или отними звание");
            return;
         default:
            super.onGenerateHintEvent(menuId, itemIndex);
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      byte menuId;
      switch (backIndex) {
         case 126:
            if (res) {
               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.request.writeInt(4490040);
                  netRequest.request.writeInt(MenuSupport.instance.curFriend);
                  netRequest.request.writeInt(this.goldCountToClaner);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4490041) {
                     int integerCode = netRequest.response.readInt();
                     ClanMenuList.clanTreasure = netRequest.response.readInt();
                     showFloatMessageS(Mystery.processTemplate("Переведено %ɼɽ", integerCode));
                  } else {
                     showFloatMessageS("Запрещено");
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId = 55;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 127:
         case 128:
            if (res) {
               Com.ProcessClanActions(backIndex);
            }

            MenuSupport.instance.isMembersForClanRequest = false;
            menuId = 51;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 129:
            if (res) {
               Com.ProcessClanActions(backIndex);
            }

            menuId = 48;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   public final void onNumberEnterEvent(int backIndex, int goldCountToClaner2) {
      switch (backIndex) {
         case 125:
            this.goldCountToClaner = goldCountToClaner2;
            this.showConfirmationForMenu(126, Mystery.stringFormat("Перевести соклановцу $ɼɽ из казны клана?", '$', new String[]{String.valueOf(this.goldCountToClaner)}));
         default:
      }
   }

   protected final void onNumberCancelEvent(int backIndex) {
      switch (backIndex) {
         case 125:
            byte menuId = 55;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      byte menuId2;
      switch (menuId) {
         case 55:
            switch (itemIndex) {
               case 0:
                  if (Strategist.instance.GP_Crystals < Com.c_clan_teleport_cost) {
                     ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_clan_teleport_cost);
                     return;
                  }

                  this.showConfirmationForMenu(127, "Переместиться к другу. Цена " + Com.c_clan_teleport_cost + "ɾɿ");
                  return;
               case 1:
                  menuId = (int)MathGame.ceilDivide((long)Com.c_clan_give_add * (long)ClanMenuList.shareMoney * (long)Com.c_clan_give_cost, 1000L);
                  if (ClanMenuList.clanTreasure > 0) {
                     if (ClanMenuList.clanTreasure <= menuId && Com.serverProtocolVersion >= 11) {
                        showFloatMessageS("Казна минимальна");
                        return;
                     }

                     this.showNumberEnter(125, 1, ClanMenuList.clanTreasure - menuId, "ɼɽ", "Сколько золота из казны дать соклановцу");
                     return;
                  }

                  showFloatMessageS("Казна пуста!");
                  return;
               case 2:
                  TradeMenuList.instance.tradeMenuStartNew(-1, MenuSupport.instance.curFriend, 55);
                  return;
               case 3:
                  this.showConfirmationForMenu(128, "Выгнать из клана?");
                  return;
               case 4:
                  menuId2 = 57;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 5:
               case 6:
               case 7:
               default:
                  break;
               case 8:
                  this.showConfirmationForMenu(129, "Ты действительно хочешь отдать власть над своим кланом другому игроку???");
                  return;
               case 9:
                  menuId2 = 51;
                  MenuManager.instance.showMenuById(menuId2);
            }
         default:
            return;
         case 57:
            Com.PrBarTh.Set(true);
            NetRequest netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(4490010);
               netRequest.request.writeInt(MenuSupport.instance.curFriend);
               netRequest.request.writeInt(itemIndex);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4490011) {
                  showFloatMessageS("Успех!");
               } else {
                  showFloatMessageS("Запрещено");
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            actualizeFriendInfo();
            menuId2 = 57;
            MenuManager.instance.showMenuById(menuId2);
      }
   }
}
