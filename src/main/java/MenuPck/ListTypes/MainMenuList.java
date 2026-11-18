package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;
import Moduls.Skill;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Resources.StringResources;
import battlepck.calls.BattleCallsHero;
import java.util.Vector;

public final class MainMenuList extends MenuList {
   private String newPass;

   public MainMenuList() {
      this(202, -2, "");
   }

   public MainMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      this.newPass = "";
      switch (id) {
         case 0:
            this.hints = Mystery.vectorFromString("Узнай мировые новости|Пообщайся с игроками|Полюбуйся собой|Вещей: % из % возможных.\nНестабильных: %|Получи особые награды!|Настройки, помощь, выход");
            return;
         case 36:
            this.hints = Mystery.vectorFromString("Карта всех локаций игры, со схемой кварталов Арены и Таверны|Твои сделки с другими игроками|Вызовы на дуэль|Игроки рядом с тобой|Мировые цитадели\rДают членам клана владельца бонус к опыту и золоту|Братство героев с общей целью|Отряд|Торговые предложения от других игроков|Посмотреть бои других героев|Выбери, чтобы вернуться в меню");
         default:
      }
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 0:
            if (itemIndex == 3) {
               setAnimatedTitle(Mystery.stringFormat(this.hints.elementAt(3).toString(), new String[]{String.valueOf(Strategist.instance.getItemsWeight()), String.valueOf(Com.c_base_bag_size + Strategist.instance.GP_HeroLevel + Strategist.instance.buyedBagSize), String.valueOf(Strategist.instance.GP_MaxTroops)}));
               return;
            }

            super.onGenerateHintEvent(menuId, itemIndex);
            return;
         case 71:
            switch (itemIndex) {
               case 0:
                  setAnimatedTitle("Прочти помощь по игре");
                  return;
               case 1:
                  setAnimatedTitle((Strategist.accountEmail != null ? Mystery.stringFormat("Почта привязки:\n%\n", Strategist.accountEmail) : "") + "Аватарка:\nhttp://win.lastlimit.ru/\navatars/get.php?id=" + Strategist.instance.id + "\nТрафик за текущий сеанс\rПринято " + Com.getBytes + " байт\rПередано " + Com.sentBytes + " байт\rДлительность сеанса \r" + Mystery.milisecondsToTime(Mystery.currentTimeMillis - Com.lastConnectTime));
                  return;
               case 2:
                  setAnimatedTitle("Задай текст о себе, видный всем игрокам");
                  return;
               case 3:
                  setAnimatedTitle("Выбери, чтобы сменить размер шрифта в чате");
                  return;
               case 4:
                  setAnimatedTitle("Выбери режим показа миникарты, золота, счетчиков, и т.п.");
                  return;
               case 5:
                  setAnimatedTitle("Выбери режим показа компаса");
                  return;
               case 6:
                  setAnimatedTitle("Выбери, что показывать в потоке общего чата");
                  return;
               case 7:
                  setAnimatedTitle("Выбери, показывать миникарту или нет");
                  return;
               case 8:
                  setAnimatedTitle("Выбери, чтобы отклонять предложения о торговле");
                  return;
               case 9:
                  setAnimatedTitle("Выбери, чтобы отказываться от вызовов на бой на тех картах, где это разрешено");
                  return;
               case 10:
                  setAnimatedTitle("Выбери, чтобы отказываться от всех приглашений в отряд");
                  return;
               case 11:
                  setAnimatedTitle("Выбери чтобы чинить надетые вещи после гибели");
                  return;
               case 12:
                  setAnimatedTitle("Выбери, чтобы входить в игру без ввода пароля");
                  return;
               case 13:
                  setAnimatedTitle("Смени свой пароль");
                  return;
               case 14:
                  setAnimatedTitle("Покинуть игру? После выхода в опасной зоне, твой герой находится в игре ещё 60 секунд, и может быть атакован без тебя другими героями. Опасные зоны помечены красным на карте мира (МЕНЮ-Мир-Карта)");
               default:
                  return;
            }
         default:
            super.onGenerateHintEvent(menuId, itemIndex);
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      switch (menuId) {
         case 36:
            this.clearSkips();
            if (Strategist.instance.trades.size() == 0) {
               this.addSkip(1);
            }

            if (BattleCallsHero.instance.getCallsCount() == 0) {
               this.addSkip(2);
            }

            if (Strategist.instance.partyState == 2) {
               this.addSkip(6);
            }
         default:
            return super.onGenerateItemsAndSkipsEvent(menuId);
         case 71:
            Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            int chatFontSize = Com.chatFontSize == 8 ? 0 : (Com.chatFontSize == 0 ? 1 : 2);
            byte platformMode;
            if (Strategist.instance.compassMode == 3) {
               platformMode = 0;
            } else if (Strategist.instance.compassMode == 1) {
               platformMode = 1;
            } else if (Strategist.instance.compassMode == 2) {
               platformMode = 2;
            } else {
               platformMode = 3;
            }

            vector3.setElementAt((String)vector3.elementAt(3) + ": " + StringResources.FONTS_NAMES_IN_OPTIONS[chatFontSize], 3);
            vector3.setElementAt((String)vector3.elementAt(4) + ": " + StringResources.GUI_MODES_NAMES_IN_OPTIONS[Strategist.instance.guiMode], 4);
            vector3.setElementAt((String)vector3.elementAt(5) + ": " + StringResources.COMPASS_MODES_IN_OPTIONS[platformMode], 5);
            vector3.setElementAt((String)vector3.elementAt(6) + ": " + StringResources.CHAT_MODES_IN_OPTIONS[Strategist.instance.commonChatShowTypes], 6);
            vector3.setElementAt((Strategist.instance.guiDrawMiniMap ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(7), 7);
            vector3.setElementAt((Strategist.instance.isAutoCancelTrade ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(8), 8);
            vector3.setElementAt((Strategist.instance.isAutoCancelFights ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(9), 9);
            vector3.setElementAt((Strategist.instance.isAutoCancelParty ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(10), 10);
            vector3.setElementAt((Strategist.instance.isAutoFixItems ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(11), 11);
            vector3.setElementAt((Com.autoGameEnter ? "ʒʓ" : "ʔʕ") + (String)vector3.elementAt(12), 12);
            return vector3;
      }
   }

   public final void onTextChanged(String messageText) {
      super.onTextChanged(messageText);
      switch (this.curMenuId) {
         case 131:
            if (messageText.length() > 0) {
               String t = "";

               for(int i = 0; i < messageText.length() - 1; ++i) {
                  t = t + "*";
               }

               t = t + messageText.charAt(messageText.length() - 1);
               MenuManager.instance.guiHandler.finishAnimationYUIComponent(t);
            }
         default:
      }
   }

   protected final void onListClickEvent(int i, int i2) {
      short menuId;
      byte menuId2;
      switch (i) {
         case 0:
            switch (i2) {
               case 0:
                  menuId2 = 36;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 1:
                  menuId2 = 58;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 2:
                  menuId2 = 4;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 3:
                  (new ItemsMenuList()).showMyItemsMenu(true, false);
                  return;
               case 4:
                  menuId2 = 37;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 5:
                  menuId2 = 71;
                  MenuManager.instance.showMenuById(menuId2);
               default:
                  return;
            }
         case 36:
            switch (i2) {
               case 0:
                  this.showWorldMapMenu();
                  return;
               case 1:
                  menuId = 245;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               case 2:
                  NeighborsMenuList.showDuelsMenu();
                  return;
               case 3:
                  menuId2 = 70;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 4:
                  menuId2 = 94;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 5:
                  menuId2 = 48;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 6:
                  menuId2 = 87;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 7:
                  menuId2 = 100;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 8:
                  menuId2 = 64;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               default:
                  return;
            }
         case 71:
            switch (i2) {
               case 0:
                  menuId2 = 19;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 1:
               default:
                  return;
               case 2:
                  this.showTextEnterSystem(130, "Напиши текст о себе", Strategist.instance.GP_AboutMe, 512);
                  return;
               case 3:
                  menuId2 = 61;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 4:
                  menuId = 206;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               case 5:
                  menuId = 236;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               case 6:
                  menuId = 209;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               case 7:
               case 8:
               case 9:
               case 10:
               case 11:
                  Com.PrBarTh.Set(true);
                  NetRequest netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(1458000);
                     boolean z = false;
                     switch (i2) {
                        case 7:
                           z = Strategist.instance.guiDrawMiniMap;
                           netRequest.request.writeInt(5);
                           break;
                        case 8:
                           z = Strategist.instance.isAutoCancelTrade;
                           netRequest.request.writeInt(0);
                           break;
                        case 9:
                           z = Strategist.instance.isAutoCancelFights;
                           netRequest.request.writeInt(1);
                           break;
                        case 10:
                           z = Strategist.instance.isAutoCancelParty;
                           netRequest.request.writeInt(2);
                           break;
                        case 11:
                           z = Strategist.instance.isAutoFixItems;
                           netRequest.request.writeInt(7);
                     }

                     netRequest.request.writeInt(!z ? 1 : 0);
                     Com.sendRequest(netRequest);
                     if (netRequest.response.readInt() == 1458001) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                        showFloatMessageS("Успех!");
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
                  this.showListMenuInner(71, true);
                  return;
               case 12:
                  Com.autoGameEnter = !Com.autoGameEnter;
                  Com.savePlatformSetting(Com.curSaveCode);
                  this.showListMenuInner(71, true);
                  return;
               case 13:
                  this.showTextEnter(131, "Введи новый пароль (обязательно " + Com.c_pass_min_len + "ə" + Com.c_pass_max_len + " символов, буквы и цифры)", "");
                  return;
               case 14:
                  this.showConfirmationForMenu(134, "Ты уверен?");
            }
      }
   }

   protected final void onOKMenuEvent(int backIndex) {
      switch (backIndex) {
         case 72:
            if (GameCommons.instance.isFromGameMenu) {
               MenuManager.instance.guiHandler.hideAllWindows();
               return;
            } else {
               byte menuId = 4;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      byte menuId;
      switch (backIndex) {
         case 133:
            if (res) {
               Com.PrBarTh.Set(true);

               try {
                  NetRequest netRequest;
                  (netRequest = new NetRequest()).writeHeader(4670000);
                  netRequest.request.writeUTF(this.newPass);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4670001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     Com.newg_password = this.newPass;
                     Com.savedPassword = this.newPass;
                     Com.savePlatformSetting(Com.curSaveCode);
                     if (Strategist.accountEmail != null) {
                        GameCommons.instance.showFloatText((String)Mystery.stringFormat("Пароль сменен\nи отправлен на почту\n%", Strategist.accountEmail), 4000);
                     } else {
                        GameCommons.instance.showFloatText((String)"Пароль сменен!", 4000);
                     }

                     menuId = 71;
                     MenuManager.instance.showMenuById(menuId);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               return;
            }

            menuId = 71;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 134:
            if (res) {
               Com.startExitGame();
               return;
            } else {
               menuId = 71;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String newPass2) {
      NetRequest netRequest;
      byte menuId;
      switch (backIndex) {
         case 65:
            if (!res) {
               menuId = 36;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            MenuSupport.instance.moderBattlesListSaveSearchString = newPass2;
            MenuSupport.instance.adminSaveText = newPass2;
            menuId = 64;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 130:
            if (res) {
               Strategist.instance.GP_AboutMe = newPass2;
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1452000);
                  netRequest.request.writeUTF(newPass2);
                  Com.sendRequest(netRequest);
                  netRequest.response.readInt();
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId = 71;
            MenuManager.instance.showMenuById(menuId);
         default:
            return;
         case 131:
            if (res) {
               if (newPass2.indexOf(" ") >= 0) {
                  GameCommons.instance.showFloatText("Нельзя использовать пробел в пароле");
                  return;
               }

               if (newPass2.length() >= Com.c_pass_min_len && newPass2.length() <= Com.c_pass_max_len) {
                  if (MenuSupport.isPasswordHaveJustLetters(newPass2)) {
                     GameCommons.instance.showFloatText("Нужны буквы и символы!");
                     return;
                  }

                  this.newPass = newPass2;
                  this.showConfirmationForMenu(133, "Сменить пароль?");
                  return;
               }

               GameCommons.instance.showFloatText("Неверная длина пароля");
               return;
            }

            menuId = 71;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 132:
            if (res) {
               boolean b = true;
               if (newPass2.equals("")) {
                  b = false;
               }

               if (b) {
                  Com.PrBarTh.Set(true);
                  netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(1459000);
                     netRequest.request.writeUTF(newPass2);
                     netRequest.request.writeInt(0);
                     Com.sendRequest(netRequest);
                     netRequest.response.readInt();
                  } catch (Exception exception2) {
                     exception2.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId = 71;
            MenuManager.instance.showMenuById(menuId);
      }
   }

   protected final void handleMainMenuAction(int actionIndex) {
      switch (actionIndex) {
         case 96:
            if (GameCommons.instance.isFromGameMenu) {
               MenuManager.instance.guiHandler.hideAllWindows();
               return;
            } else {
               byte menuId = 36;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   public final void showWorldMapMenu() {
      QuestInPlayer questInPlayer;
      int[][] questsMarks;
      if ((questInPlayer = QuestStrateg.instance.getCompassedQuest()) != null) {
         short[][] shorts;
         questsMarks = new int[(shorts = questInPlayer.calcWaysToMaps).length][];

         for(int i = 0; i < shorts.length; ++i) {
            questsMarks[i] = new int[]{shorts[i][0], shorts[i][1]};
         }
      } else {
         questsMarks = new int[0][];
      }

      this.showWorldMapMenu(96, Com.c_world_map_cur_loc_info, questsMarks);
   }

   public final void showHeroMenu() {
      String messageText = Strategist.instance.GP_Name + " (" + Integer.toString(Strategist.instance.GP_Level) + "ур)\n" + Integer.toString(Strategist.instance.GP_Exp) + "ɺɻ из " + Integer.toString(Strategist.instance.GP_NextLevelExp) + "ɺɻ\n" + Integer.toString(Strategist.instance.GP_Money) + "ɼɽ " + Strategist.instance.GP_Crystals + "ɾɿ";
      long instance;
      long instance2;
      if (Strategist.instance.vipStatus >= 0) {
         instance = Strategist.instance.vipStatusEndTime / 1000L / 60L % 60L;
         instance2 = Strategist.instance.vipStatusEndTime / 1000L / 60L / 60L;
         messageText = messageText + "\n˦˧Герой еще " + instance2 + "ч " + instance + "м";
      }

      if (Strategist.instance.protectionStatus) {
         instance = Strategist.instance.protectionStatusEndTime / 1000L / 60L % 60L;
         instance2 = Strategist.instance.protectionStatusEndTime / 1000L / 60L / 60L;
         messageText = messageText + "\n̎̏Безопасность еще " + instance2 + "ч " + instance + "м";
      }

      String messageText2 = "";

      for(int i = 0; i < Skill.MaxSkills; ++i) {
         if (Strategist.instance.GP_TotalSkillsLevels[i] > 0) {
            messageText2 = messageText2 + (messageText2.length() == 0 ? "\n" : ", ") + "(" + Strategist.instance.GP_TotalSkillsLevels[i] + ")" + Skill.Skills[i].Name;
         }
      }

      messageText = messageText + "\n\n" + Strategist.instance.avatarDmg * Com.c_battle_action_random_max / 1000 + "ə" + Strategist.instance.avatarDmg * Com.c_battle_action_random_add / 1000 + "ɲɳ в " + Mystery.floatToString(Strategist.instance.avatarHit, 1000) + " сек\n" + Strategist.instance.GP_Life + "ʎʏ из " + Strategist.instance.avatarLife + "ʎʏ\n" + '\uea60' / Strategist.instance.avatarSpeed / Tile.width + "." + 600000 / Strategist.instance.avatarSpeed / Tile.width % 10 + "ɨɩ " + Strategist.instance.avatarAtack + "ɮɯ " + Strategist.instance.avatarArmor + "ɰɱ\n" + messageText2;
      this.showMenuMessage(72, messageText);
   }
}
