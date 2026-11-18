package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Delegate;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import desktop.PlatformRuntime;
import Moduls.Strategist;
import java.util.Vector;

public final class ClanMenuList extends MenuList {
   private String aboutAs;
   private static String paysList = "";
   private static String eventsList = "";
   private String fightsList;
   private String[] infos;
   private String aboutCastle;
   private String[][] castelsAbout;
   private static int curCastleId;
   private String string;
   public static int clanTreasure;
   public static int shareMoney;
   private static boolean isInClan;
   public static boolean isClanLeader;
   public static boolean isClanGeneral;
   private static boolean isClanHerald;
   public static boolean isClanInquirer;
   public static boolean isClanPathfinder;
   public static boolean isClanRecruiter;
   private static boolean isClanStudent;
   public static boolean isClanTreasurer;
   private static int goldCountToClan;

   public ClanMenuList(int id, int backMenu, String[] list) {
      super(id, backMenu, list);
   }

   public ClanMenuList(int id, int backMenu, String list) {
      super(48, 36, (String)list);
      this.hints = Mystery.vectorFromString("Создай свой клан и стань его лидером! Цена |О нас|Установи клану новую иконку.\nОбязательно: png файл 8 на 8 пикселей!\nОна изменится завтра.|Для действий, выбери героя из списка|Кто просится в твой клан.|Казна|Переведи часть своего золота в общую казну клана|События|Уйти из клана навсегда|Посмотри список всех кланов Предела");
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 48:
            switch (itemIndex) {
               case 0:
                  String messageText = "Создай свой клан и стань его лидером! Цена " + Com.c_clan_create_cost;
                  setAnimatedTitle(messageText + "ɾɿ\rПреимущества клана:\n- низкие налоги\n- бонусы от цитадели\n- общий клановый приват\n- призыв друга к себе\n- помощь при нападении\n- клановая иконка");
                  return;
               case 1:
                  setAnimatedTitle(this.aboutAs);
                  return;
               case 2:
               case 3:
               case 4:
               case 6:
               default:
                  super.onGenerateHintEvent(menuId, itemIndex);
                  return;
               case 5:
                  setAnimatedTitle(paysList);
                  return;
               case 7:
                  if (eventsList.length() > 0) {
                     setAnimatedTitle("События в клане:\r" + eventsList);
                     return;
                  }

                  setAnimatedTitle("Событий в клане не было");
                  return;
            }
         case 80:
            if (itemIndex == 0) {
               setAnimatedTitle("Список всех кланов Предела");
               return;
            } else {
               setAnimatedTitle(this.infos[itemIndex - 1]);
            }
         default:
            return;
         case 94:
            setAnimatedTitle(this.castelsAbout[itemIndex][3]);
            return;
         case 95:
            if (itemIndex == 0) {
               setAnimatedTitle(this.string);
            } else {
               setAnimatedTitle(this.infos[itemIndex - 1]);
            }
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      NetRequest netRequest;
      int integerCode;
      switch (menuId) {
         case 48:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(4410000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4410001) {
                  this.clearSkips();
                  if (isInClan = netRequest.response.readBoolean()) {
                     this.addSkip(0);
                     isClanLeader = netRequest.response.readBoolean();
                     isClanGeneral = netRequest.response.readBoolean();
                     isClanHerald = netRequest.response.readBoolean();
                     isClanInquirer = netRequest.response.readBoolean();
                     isClanPathfinder = netRequest.response.readBoolean();
                     isClanStudent = netRequest.response.readBoolean();
                     isClanTreasurer = netRequest.response.readBoolean();
                     isClanRecruiter = netRequest.response.readBoolean();
                     if (!isClanHerald && !isClanLeader) {
                        this.addSkip(2);
                     }

                     if (!isClanStudent) {
                        paysList = netRequest.response.readUTF();
                        eventsList = netRequest.response.readUTF();
                     } else {
                        this.addSkip(4);
                        this.addSkip(5);
                        this.addSkip(7);
                     }

                     if (isClanLeader) {
                        this.addSkip(8);
                     }

                     this.fightsList = netRequest.response.readUTF();
                     this.aboutAs = netRequest.response.readUTF();
                     netRequest.response.readInt();
                     netRequest.response.readInt();
                     clanTreasure = netRequest.response.readInt();
                     shareMoney = netRequest.response.readInt();
                     this.aboutCastle = netRequest.response.readUTF();
                  } else {
                     this.addSkip(1);
                     this.addSkip(2);
                     this.addSkip(3);
                     this.addSkip(4);
                     this.addSkip(5);
                     this.addSkip(6);
                     this.addSkip(7);
                     this.addSkip(8);
                  }

                  vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
               } else {
                  MenuManager.instance.guiHandler.hideAllWindows();
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 80:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(6400000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 6400001) {
                  menuId = netRequest.response.readInt();
                  vector3.addElement("Список кланов:");
                  this.infos = new String[menuId];

                  for(integerCode = 0; integerCode < menuId; ++integerCode) {
                     if (netRequest.response.readBoolean()) {
                        vector3.addElement("΄" + netRequest.response.readUTF());
                     } else {
                        vector3.addElement(netRequest.response.readUTF());
                     }

                     this.infos[integerCode] = netRequest.response.readUTF();
                  }
               } else {
                  MenuManager.instance.guiHandler.hideAllWindows();
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 94:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(8210000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 8210001) {
                  menuId = netRequest.response.readInt();
                  this.castelsAbout = new String[menuId][];

                  for(integerCode = 0; integerCode < menuId; ++integerCode) {
                     this.castelsAbout[integerCode] = new String[4];
                     this.castelsAbout[integerCode][0] = String.valueOf(netRequest.response.readInt());
                     this.castelsAbout[integerCode][1] = String.valueOf(netRequest.response.readUTF());
                     this.castelsAbout[integerCode][2] = String.valueOf(netRequest.response.readBoolean());
                     this.castelsAbout[integerCode][3] = String.valueOf(netRequest.response.readUTF());
                     vector3.addElement(this.castelsAbout[integerCode][1]);
                  }
               } else {
                  MenuManager.instance.guiHandler.hideAllWindows();
               }
            } catch (Exception exception3) {
               exception3.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 95:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(8220000);
               netRequest.request.writeInt(curCastleId);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 8220001) {
                  String castleName = netRequest.response.readUTF();
                  this.string = netRequest.response.readUTF();
                  integerCode = netRequest.response.readInt();
                  vector3.addElement(castleName);
                  this.infos = new String[integerCode];

                  for(int infoIndex = 0; infoIndex < integerCode; ++infoIndex) {
                     vector3.addElement(netRequest.response.readUTF());
                     this.infos[infoIndex] = netRequest.response.readUTF();
                  }
               } else {
                  MenuManager.instance.guiHandler.hideAllWindows();
               }
            } catch (Exception exception4) {
               exception4.printStackTrace();
            }

            Com.PrBarTh.Stop();
      }

      return vector3;
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      byte menuId;
      switch (backIndex) {
         case 114:
            if (res) {
               if ((backIndex = goldCountToClan) > Strategist.instance.GP_Money) {
                  GameCommons.instance.showFloatText((String)"Не хватает золота!", 2000);
               } else {
                  Com.PrBarTh.Set(true);
                  NetRequest netRequest;
                  (netRequest = new NetRequest()).writeHeader(4480010);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, backIndex);
                  Com.sendRequest(netRequest);

                  try {
                     if ((backIndex = netRequest.response.readInt()) == 4480011) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                        GameCommons.instance.showFloatText((String)"Золото переведено!", 3000);
                     } else if (backIndex == 4480012) {
                        GameCommons.instance.showFloatText((String)"Не хватает золота!", 2000);
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId = 48;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 116:
            if (res) {
               Com.ProcessClanActions(backIndex);
            }

            menuId = 48;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String messageText) {
      byte menuId;
      if (res) {
         Com.PrBarTh.Set(true);
         NetRequest netRequest = new NetRequest();

         try {
            netRequest.writeHeader(4460000);
            netRequest.request.writeUTF(messageText);
            Com.sendRequest(netRequest);
            if (netRequest.response.readInt() == 4460001) {
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               menuId = 48;
               MenuManager.instance.showMenuById(menuId);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         Com.PrBarTh.Stop();
      } else {
         menuId = 48;
         MenuManager.instance.showMenuById(menuId);
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      short menuId2;
      switch (menuId) {
         case 48:
            switch (itemIndex) {
               case 0:
                  if (Strategist.instance.GP_Level < Com.c_clan_create_lvl) {
                     showFloatMessageS("Нужно быть уровня " + Com.c_clan_create_lvl);
                     return;
                  } else if (Strategist.instance.GP_Crystals < Com.c_clan_create_cost) {
                     ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.c_clan_create_cost);
                     return;
                  } else {
                     (new OtherMenuList()).showTextEnter(49, "Напишите имя клана", "");
                     return;
                  }
               case 1:
                  if (isClanLeader || isClanHerald) {
                     this.showTextEnterSystem(183, "Напиши текст о клане", this.aboutCastle, Com.c_clan_max_description_len);
                     return;
                  }

                  return;
               case 2:
                  PlatformRuntime.getInstance();
                  MenuSupport.instance.menuCurDir = "";
                  menuId2 = 215;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 3:
                  MenuSupport.instance.isMembersForClanRequest = false;
                  menuId2 = 51;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 4:
                  MenuSupport.instance.isMembersForClanRequest = true;
                  menuId2 = 51;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 5:
               case 7:
               default:
                  return;
               case 6:
                  this.showNumberEnter(115, 1, Strategist.instance.GP_Money, "ɼɽ", "Сколько золота перевести в казну клана?");
                  return;
               case 8:
                  this.showConfirmationForMenu(116, "Ты действительно хочешь покинуть клан?");
                  return;
               case 9:
                  menuId2 = 80;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
            }
         case 80:
            if (itemIndex != 0) {
               String clanName;
               if ((clanName = this.curListArray[itemIndex]).startsWith("΄")) {
                  clanName = clanName.substring(1);
               }

               MenuSupport.instance.curSelectedClanName = clanName;
               menuId2 = 81;
               MenuManager.instance.showMenuById(menuId2);
               return;
            }
            break;
         case 94:
            if (this.castelsAbout[itemIndex][2].equals("true")) {
               curCastleId = Integer.parseInt(this.castelsAbout[itemIndex][0]);
               menuId2 = 95;
               MenuManager.instance.showMenuById(menuId2);
               return;
            }
            break;
         case 95:
            return;
      }

   }

   public final void onNumberEnterEvent(int backIndex, int goldCountToClan2) {
      switch (backIndex) {
         case 115:
            goldCountToClan = goldCountToClan2;
            this.showConfirmationForMenu(114, "Перевести " + goldCountToClan2 + "ɼɽ в казну клана?");
         default:
      }
   }

   protected final void onNumberCancelEvent(int backIndex) {
      switch (backIndex) {
         case 115:
            byte menuId = 48;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }
}
