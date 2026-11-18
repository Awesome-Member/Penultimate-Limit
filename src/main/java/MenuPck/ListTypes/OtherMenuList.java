package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Base.GameEffects;
import Base.GameMapObjectsController;
import Engine.Delegate;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuPlayerDescription;
import MenuPck.MenuShowListDelegate;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import desktop.PlatformRuntime;
import Moduls.StrategGraphic;
import Moduls.Strategist;
import Base.Black;
import Moduls.InventoryItem;
import Moduls.ItemSimple;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestSingle;
import Moduls.quests.QuestSingleIdSmall;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestSupItemNeed;
import Moduls.quests.QuestsOnMap;
import Resources.StringResources;
import battlepck.BattleManager;
import battlepck.client.BattleNetTh;
import battlepck.client.BattleTh;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import desktop.io.FileConnectionAdapter;
import desktop.graphics.GameImage;

public final class OtherMenuList extends MenuList {
   private static char[][] NAME_CHECK_ALPHABITS = new char[][]{{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'}, {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}};
   private Vector dirsList;
   private int itemsSellerReBuyItemInd;
   private int itemsSellerBuyItemInd;
   private int itemsSellerBuyItemCount;
   private static int questMyBackMenuInd = -1;
   public static int questGiverInMapInd = -1;
   private static QuestSingle delegateTakeQuest;
   private static QuestSingle questSelected;
   private static QuestInPlayer questSelectedInPlayer;
   private Vector vector;
   private Vector vector2;

   public OtherMenuList() {
      this(106, -2, "");
   }

   public OtherMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      this.dirsList = new Vector();
      this.itemsSellerReBuyItemInd = -1;
      this.itemsSellerBuyItemInd = -1;
      this.itemsSellerBuyItemCount = 1;
      switch (id) {
         case 60:
            this.hints = Mystery.vectorFromString("Выбери срок наказания\r(грубость, хамство, ссоры, капс, флуд, обман новичков, хула на игру без причины)|Выбери срок наказания\r(мат, нецензурщина, оскорбления)|Выбери срок наказания\r(неуместная реклама, пропаганда запретных тем, повторные нарушения)|Выбери срок наказания\r(самые злостные и циничные нарушения правил)");
            return;
         case 69:
            this.hints = Mystery.vectorFromString("Посмотреть фотографию|Переименовать фотографию|Удалить фотографию");
            return;
         case 235:
            this.hints = Mystery.vectorFromString("Наблюдать за этим квестом, не заходя в меню|Отказаться от задачи");
         default:
      }
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      QuestInPlayer questInPlayer;
      switch (menuId) {
         case 19:
            setAnimatedTitle(MenuSupport.instance.helpHintsList[itemIndex]);
            return;
         case 52:
         case 67:
            if (itemIndex == this.getCurrentMenuListsCount() - 1 && menuId == 67) {
               setAnimatedTitle("Добавить фотографию");
            } else if (itemIndex == 0) {
               setAnimatedTitle("Выбери фотографию");
            } else if (menuId == 67) {
               setAnimatedTitle("Нажми, чтобы редактировать это фото");
            } else {
               setAnimatedTitle("Нажми, чтобы посмотреть это фото");
            }
            break;
         case 61:
            setAnimatedTitle("Выбери, чтобы сменить размер шрифта в чате");
            break;
         case 66:
            if (itemIndex == 0) {
               setAnimatedTitle("Выбери себе уникальную ауру");
            } else if (itemIndex == 1) {
               setAnimatedTitle("Убери ауру");
            } else {
               setAnimatedTitle(Com.AurasTypes[itemIndex - 2].description);
               StrategGraphic strategGraphic2;
               (strategGraphic2 = Strategist.instance.avatarGraphic.makeClone()).auraInd = (byte)(itemIndex - 2);
               showAvatarPortrait(strategGraphic2);
            }
            break;
         case 68:
         case 215:
            if (itemIndex == 0 && !MenuSupport.instance.menuCurDir.equals("")) {
               int i;
               String substring;
               if ((i = MenuSupport.instance.menuCurDir.lastIndexOf(47, MenuSupport.instance.menuCurDir.length() - 2)) != -1) {
                  substring = MenuSupport.instance.menuCurDir.substring(0, i + 1);
               } else {
                  substring = "корневой каталог";
               }

               setAnimatedTitle("Перейти в\n" + substring);
            } else if (((String)this.dirsList.elementAt(itemIndex)).endsWith("/")) {
               setAnimatedTitle("Перейти в\n" + MenuSupport.instance.menuCurDir + this.dirsList.elementAt(itemIndex));
            } else {
               setAnimatedTitle("Загрузить\n" + MenuSupport.instance.menuCurDir + this.dirsList.elementAt(itemIndex));
            }
            break;
         case 190:
            if (itemIndex == 0) {
               setAnimatedTitle(MenuSupport.instance.string6);
            } else if (itemIndex == MenuSupport.instance.itemsSellerItems.length + 1) {
               setAnimatedTitle("Сбытое");
            } else {
               MenuSupport.instance.itemsSellerItems[itemIndex - 1].showInMenu((MenuList)this);
            }
            break;
         case 191:
            MenuSupport.instance.itemsSellerSoldItems[itemIndex].showInMenu((MenuList)this);
            break;
         case 196:
            if (itemIndex == 0) {
               setAnimatedTitle(Com.currentGameMap.questers[questGiverInMapInd].startText);
            } else if (itemIndex - 1 < this.vector.size()) {
               QuestSingle questSingle2;
               setAnimatedTitle((questSingle2 = (QuestSingle)this.vector.elementAt(itemIndex - 1)).getDescription(Strategist.instance, (QuestInPlayer)null));
               showUnitPortraits(questSingle2.getMobsToDraw());
               showAvatarPortrait(questSingle2.getStrategToDraw((QuestInPlayer)null));
            } else {
               setAnimatedTitle((questInPlayer = (QuestInPlayer)this.vector2.elementAt(itemIndex - 1 - this.vector.size())).questSingle.getDescription(Strategist.instance, questInPlayer));
               showUnitPortraits(questInPlayer.questSingle.getMobsToDraw());
               showAvatarPortrait(questInPlayer.questSingle.getStrategToDraw(questInPlayer));
            }
            break;
         case 199:
         case 200:
            setAnimatedTitle((questInPlayer = (QuestInPlayer)QuestStrateg.instance.quests.elementAt(itemIndex)).questSingle.getDescription(Strategist.instance, questInPlayer));
            showUnitPortraits(questInPlayer.questSingle.getMobsToDraw());
            showAvatarPortrait(questInPlayer.questSingle.getStrategToDraw(questInPlayer));
            break;
         case 206:
            setAnimatedTitle("Выбери режим показа миникарты, золота, счетчиков, и т.п.");
            break;
         case 209:
            setAnimatedTitle("Выбери, что показывать в потоке общего чата");
            break;
         case 226:
            setAnimatedTitle("Пожалуйста, оцените нашу игру.\n͓͓͓͓͓͒͒͒͒͒\nКруглое число пятерок - это повод устроить праздник всей игре.\nСпасибо!");
            break;
         case 227:
            setAnimatedTitle("Прими поздравления! Ты получил уровень!");
            showAvatarPortrait(Strategist.instance.avatarGraphic);
            break;
         case 236:
            setAnimatedTitle("Выбери режим показа компаса");
      }

      super.onGenerateHintEvent(menuId, itemIndex);
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      if (menuId == 235) {
         return questMyBackMenuInd;
      } else {
         return GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      FileConnectionAdapter fileConnectionAdapter = null;
      Vector vector3;
      int integerCode;
      switch (menuId) {
         case 19:
            vector3 = new Vector();

            for(integerCode = 0; integerCode < MenuSupport.instance.helpMenuList.length; ++integerCode) {
               vector3.addElement(MenuSupport.instance.helpMenuList[integerCode]);
            }

            return vector3;
         case 52:
         case 67:
            vector3 = new Vector();
            Com.PrBarTh.Set(true);
            NetRequest netRequest = new NetRequest();

            try {
               netRequest.writeHeader(5300000);
               netRequest.request.writeInt(MenuSupport.instance.tempStartegId);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 5300001) {
                  integerCode = netRequest.response.readInt();
                  vector3.addElement("Фотографии:");
                  MenuSupport.instance.otherPlayers = new MenuPlayerDescription[integerCode];

                  for(integerCode = 0; integerCode < MenuSupport.instance.otherPlayers.length; ++integerCode) {
                     String messageText = netRequest.response.readUTF();
                     MenuSupport.instance.otherPlayers[integerCode] = new MenuPlayerDescription(netRequest.response.readUTF(), netRequest.response.readUTF(), 0, new StrategGraphic());
                     MenuSupport.instance.otherPlayers[integerCode].time = messageText;
                     vector3.addElement(MenuSupport.instance.otherPlayers[integerCode].name);
                  }

                  if (menuId == 67) {
                     vector3.addElement("Добавить фото");
                  }
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            return vector3;
         case 66:
            (vector3 = new Vector()).addElement("Ауры:");
            vector3.addElement("Убрать ауру");

            for(integerCode = 0; integerCode < Com.AurasTypes.length; ++integerCode) {
               vector3.addElement(Com.AurasTypes[integerCode].name);
            }

            return vector3;
         case 68:
         case 215:
            vector3 = new Vector();
            this.dirsList = new Vector();

            try {
               Enumeration enumeration;
               if (MenuSupport.instance.menuCurDir.equals("")) {
                  enumeration = Black.listFileRoots();
               } else {
                  enumeration = (fileConnectionAdapter = (FileConnectionAdapter)Black.openConnection("file://localhost/" + MenuSupport.instance.menuCurDir)).list();
                  this.dirsList.addElement("..");
                  vector3.addElement("..");
               }

               while(true) {
                  while(enumeration.hasMoreElements()) {
                     String element;
                     if ((element = (String)enumeration.nextElement()).charAt(element.length() - 1) == '/') {
                        this.dirsList.addElement(element);
                        vector3.addElement("ʈʉ" + element);
                     } else if (element.toLowerCase().endsWith(".png") || menuId == 68 && (element.toLowerCase().endsWith(".jpg") || element.toLowerCase().endsWith(".jpeg"))) {
                        this.dirsList.addElement(element);
                        vector3.addElement("ʊʋ" + element);
                     }
                  }

                  if (fileConnectionAdapter != null) {
                     fileConnectionAdapter.close();
                  }
                  break;
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            return vector3;
         case 190:
            (vector3 = new Vector()).addElement("Товары");

            for(integerCode = 0; integerCode < MenuSupport.instance.itemsSellerItems.length; ++integerCode) {
               vector3.addElement(MenuSupport.instance.itemsSellerItems[integerCode].getNameForMenu());
            }

            vector3.addElement("Сбытое");
            return vector3;
         case 191:
            vector3 = new Vector();

            for(integerCode = 0; integerCode < MenuSupport.instance.itemsSellerSoldItems.length; ++integerCode) {
               vector3.addElement(MenuSupport.instance.itemsSellerSoldItems[integerCode].getNameForMenu());
            }

            return vector3;
         case 196:
            vector3 = new Vector();
            QuestsOnMap questsOnMap = Com.currentGameMap.questers[questGiverInMapInd];
            vector3.addElement(questsOnMap.clientHeaderLabel);
            this.vector = questsOnMap.getQuestsForTake(Strategist.instance, QuestStrateg.instance);
            this.vector2 = QuestsOnMap.getQuestsInProgress(Com.currentGameMap, questGiverInMapInd, QuestStrateg.instance);

            int sizeIndex;
            for(sizeIndex = 0; sizeIndex < this.vector.size(); ++sizeIndex) {
               QuestSingle questSingle2 = (QuestSingle)this.vector.elementAt(sizeIndex);
               vector3.addElement("Ѝgr͙͘" + questSingle2.name + "Џ");
            }

            for(sizeIndex = 0; sizeIndex < this.vector2.size(); ++sizeIndex) {
               QuestInPlayer questInPlayer = (QuestInPlayer)this.vector2.elementAt(sizeIndex);
               vector3.addElement("Ѝye" + (questInPlayer.questSingle.isDone(Strategist.instance, questInPlayer) ? "͔͕" : "͖͗") + questInPlayer.questSingle.name + "Џ");
            }

            return vector3;
         case 199:
         case 200:
            vector3 = new Vector();
            QuestInPlayer questInPlayer2 = QuestStrateg.instance.getCompassedQuest();

            for(integerCode = 0; integerCode < QuestStrateg.instance.quests.size(); ++integerCode) {
               QuestInPlayer questInPlayer3 = (QuestInPlayer)QuestStrateg.instance.quests.elementAt(integerCode);
               vector3.addElement((questInPlayer2 != null && questInPlayer3.questSingle.equalsById((QuestSingleIdSmall)questInPlayer2.questSingle) ? "͟͞" : "") + (questInPlayer3.questSingle.isDone(Strategist.instance, questInPlayer3) ? "͔͕" : "͖͗") + questInPlayer3.questSingle.name);
            }

            return vector3;
         default:
            return super.onGenerateItemsAndSkipsEvent(menuId);
      }
   }

   public final void onNumberChangedEvent(int backIndex, int inputNumber) {
      switch (backIndex) {
         case 193:
            String messageText = MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].getItemDisplayName();
            int costCrys2 = MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costCrys;
            int costGold2 = MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costGold;
            String messageText2 = messageText;
            setAnimatedTitle(inputNumber <= 0 ? "Ничего не покупать" : Mystery.stringFormat("Стоимость % за % шт %?", '%', new String[]{(costGold2 > 0 ? costGold2 * inputNumber + "ɼɽ" + (costCrys2 > 0 ? " " : "") : "") + (costCrys2 > 0 ? costCrys2 * inputNumber + "ɾɿ" : ""), String.valueOf(inputNumber), messageText2}));
         default:
      }
   }

   public final void onNumberEnterEvent(int backIndex, int itemsSellerBuyItemCount2) {
      switch (backIndex) {
         case 193:
            if (itemsSellerBuyItemCount2 <= 0) {
               short menuId = 190;
               MenuManager.instance.showMenuById(menuId);
               return;
            } else {
               this.itemsSellerBuyItemCount = itemsSellerBuyItemCount2;
               this.showConfirmationForMenu(192, this.getPurchaseConfirmation(MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].getItemDisplayName(), itemsSellerBuyItemCount2, MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costGold, MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costCrys));
            }
         default:
      }
   }

   protected final void onNumberCancelEvent(int backIndex) {
      switch (backIndex) {
         case 193:
            short menuId = 190;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   private String getPurchaseConfirmation(String messageText, int quantity, int goldCost, int crystalCost) {
      return getPurchaseConfirmation(messageText, quantity, goldCost, crystalCost, false);
   }

   private static String getPurchaseConfirmation(String messageText, int quantity, int goldCost, int crystalCost, boolean pricePerItem) {
      return goldCost <= 0 && crystalCost <= 0 ? Mystery.stringFormat("Взять % шт %?", '%', new String[]{String.valueOf(quantity), messageText}) : Mystery.stringFormat("Потратить % и купить % шт %?", '%', new String[]{(goldCost > 0 ? goldCost * (pricePerItem ? 1 : quantity) + "ɼɽ" + (crystalCost > 0 ? " " : "") : "") + (crystalCost > 0 ? crystalCost * (pricePerItem ? 1 : quantity) + "ɾɿ" : ""), String.valueOf(quantity), messageText});
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      String messageText3;
      NetRequest netRequest;
      short menuId2;
      String messageText4;
      int lastMatchOffset;
      byte menuId3;
      MenuSupport menuSupport;
      switch (menuId) {
         case 19:
            this.showMenuMessage(176, MenuSupport.instance.helpTexts[itemIndex]);
            break;
         case 52:
         case 67:
            if (itemIndex == this.getCurrentMenuListsCount() - 1 && menuId == 67) {
               if (MenuSupport.instance.otherPlayers.length >= Com.c_photo_max_number_for_player) {
                  GameCommons.instance.showFloatText((String)"Предел\rкартинок!", 4000);
                  return;
               }

               PlatformRuntime.getInstance();
               MenuSupport.instance.menuCurDir = "";
               menuId3 = 68;
               MenuManager.instance.showMenuById(menuId3);
               return;
            }

            if (itemIndex > 0) {
               if (menuId == 67) {
                  menuSupport = MenuSupport.instance;
                  menuSupport.string3 = menuSupport.otherPlayers[itemIndex - 1].time;
                  menuSupport = MenuSupport.instance;
                  menuSupport.string2 = menuSupport.otherPlayers[itemIndex - 1].name;
                  menuSupport = MenuSupport.instance;
                  menuSupport.string4 = menuSupport.otherPlayers[itemIndex - 1].aboutMe;
                  menuId3 = 69;
                  MenuManager.instance.showMenuById(menuId3);
                  return;
               }

               try {
                  Com.midlet.openBrowser(MenuSupport.instance.otherPlayers[itemIndex - 1].aboutMe);
                  return;
               } catch (Exception exception) {
                  exception.printStackTrace();
                  return;
               }
            }
            break;
         case 60:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4530000);
               netRequest.request.writeInt(MenuSupport.instance.curFriend);
               netRequest.request.writeInt(MenuSupport.SILENCE_TIMES[itemIndex]);
               Com.sendRequest(netRequest);
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId3 = 59;
            MenuManager.instance.showMenuById(menuId3);
            return;
         case 61:
            byte chatFlags = 0;
            if (itemIndex == 0) {
               chatFlags = 8;
            }

            if (itemIndex == 2) {
               chatFlags = 16;
            }

            Com.changeFontSize(chatFlags);
            menuId3 = 71;
            MenuManager.instance.showMenuById(menuId3);
            return;
         case 66:
            if (itemIndex == 1) {
               this.showConfirmationForMenu(167, "Убрать ауру?");
               return;
            }

            if (itemIndex > 1) {
               MenuSupport.instance.menuAuraInd = itemIndex - 2;
               if (Com.AurasTypes[MenuSupport.instance.menuAuraInd].cost > Strategist.instance.GP_Crystals) {
                  ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), Com.AurasTypes[MenuSupport.instance.menuAuraInd].cost);
                  return;
               }

               this.showConfirmationForMenu(168, "Купить ауру " + Com.AurasTypes[MenuSupport.instance.menuAuraInd].name + " за " + Com.AurasTypes[MenuSupport.instance.menuAuraInd].cost + "ɾɿ?");
               return;
            }
            break;
         case 68:
         case 215:
            if (itemIndex == 0 && !MenuSupport.instance.menuCurDir.equals("")) {
               if ((lastMatchOffset = MenuSupport.instance.menuCurDir.lastIndexOf(47, MenuSupport.instance.menuCurDir.length() - 2)) != -1) {
                  menuSupport = MenuSupport.instance;
                  menuSupport.menuCurDir = menuSupport.menuCurDir.substring(0, lastMatchOffset + 1);
               } else {
                  MenuSupport.instance.menuCurDir = "";
               }

               MenuManager.instance.showMenuById(menuId);
               return;
            }

            if ((messageText3 = (String)this.dirsList.elementAt(itemIndex)).endsWith("/")) {
               MenuSupport.instance.menuCurDir = MenuSupport.instance.menuCurDir + messageText3;
               MenuManager.instance.showMenuById(menuId);
            } else {
               try {
                  FileConnectionAdapter fileConnectionAdapter;
                  if ((fileConnectionAdapter = (FileConnectionAdapter)Black.openConnection("file://localhost/" + MenuSupport.instance.menuCurDir + messageText3)).exists()) {
                     String messageText = MenuSupport.instance.menuCurDir + messageText3;
                     FileConnectionAdapter fileConnectionAdapter2 = fileConnectionAdapter;
                     messageText3 = "";
                     netRequest = null;
                     switch (menuId) {
                        case 68:
                           if (fileConnectionAdapter.fileSize() <= (long)Com.c_photo_max_size) {
                              MenuSupport.instance.photoData = null;
                              MenuSupport.instance.string = messageText3;
                              MenuSupport.instance.photoPath = messageText;
                              this.showTextEnter(180, "Напиши название фотографии", "");
                           } else {
                              GameCommons.instance.showFloatText((String)("Размер больше\r" + Com.c_photo_max_size / 1000 + "кб"), 3000);
                           }
                           break;
                        case 215:
                           Com.PrBarTh.Set(true);

                           try {
                              netRequest = null;
                              InputStream inputStream;
                              GameImage gameImage = GameImage.createImage(inputStream = fileConnectionAdapter2.openInputStream());
                              inputStream.close();
                              if (gameImage.getWidth() == 8 && gameImage.getHeight() == 8) {
                                 (netRequest = new NetRequest()).request.writeInt(4406000);
                                 sendFile(fileConnectionAdapter2, netRequest);
                                 Com.sendRequest(netRequest);
                                 if (netRequest.response.readInt() == 4406001) {
                                    showFloatMessageS2("Успех! Иконка изменится завтра");
                                    menuId3 = 48;
                                    MenuManager.instance.showMenuById(menuId3);
                                 } else {
                                    showFloatMessageS2("Ошибка");
                                 }
                              } else {
                                 showFloatMessageS2("Размер должен быть 8 на 8 пикселей");
                              }
                           } finally {
                              Com.PrBarTh.Stop();
                           }
                     }
                  } else {
                     GameCommons.instance.showFloatText((String)"Файл не найден", 3000);
                  }

                  fileConnectionAdapter.close();
               } catch (Exception exception3) {
                  exception3.printStackTrace();
                  return;
               }
            }
            break;
         case 69:
            switch (itemIndex) {
               case 0:
                  try {
                     Com.midlet.openBrowser(MenuSupport.instance.string4);
                  } catch (Exception exception4) {
                     exception4.printStackTrace();
                  }

                  return;
               case 1:
                  this.showTextEnter(179, "Напиши название фотографии", MenuSupport.instance.string2);
                  return;
               case 2:
                  Com.PrBarTh.Set(true);
                  netRequest = new NetRequest();

                  try {
                     netRequest.request.writeInt(5410000);
                     netRequest.request.writeUTF(MenuSupport.instance.string3);
                     Com.sendRequest(netRequest);
                     if (netRequest.response.readInt() == 5410001) {
                        GameCommons.instance.showFloatText((String)"Фото\rудалено", 3000);
                     }
                  } catch (Exception exception5) {
                     exception5.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
                  menuId3 = 67;
                  MenuManager.instance.showMenuById(menuId3);
               default:
                  return;
            }
         case 190:
            if (itemIndex != 0) {
               if (itemIndex == MenuSupport.instance.itemsSellerItems.length + 1) {
                  menuId2 = 191;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               }

               this.itemsSellerBuyItemInd = itemIndex - 1;
               InventoryItem inventoryItem2;
               if (!checkPurchaseRequirements((inventoryItem2 = MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd]).count, inventoryItem2.costGold, inventoryItem2.costCrys, this, new Delegate(this, 2), inventoryItem2.unweight)) {
                  this.executeDelegated(2);
                  return;
               }
            }
            break;
         case 191:
            this.itemsSellerReBuyItemInd = itemIndex;
            InventoryItem inventoryItem3;
            if (!checkPurchaseRequirements((inventoryItem3 = MenuSupport.instance.itemsSellerSoldItems[itemIndex]).count, inventoryItem3.costGold, inventoryItem3.costCrys, this, new Delegate(this, 1), inventoryItem3.unweight)) {
               this.executeDelegated(1);
               return;
            }
            break;
         case 196:
            if (itemIndex == 0) {
               break;
            }

            if (itemIndex - 1 >= this.vector.size()) {
               QuestInPlayer questInPlayer;
               questSelected = (questInPlayer = (QuestInPlayer)this.vector2.elementAt(itemIndex - 1 - this.vector.size())).questSingle;
               this.showConfirmationWithPortrait(198, "Отказаться от задачи?\n" + questInPlayer.questSingle.name, false, (StrategGraphic)null, false, -1, -1, false, (int[][])null);
               return;
            }

            if (QuestStrateg.instance.quests.size() >= Com.c_quests_max_count) {
               this.showMenuMessage(203, "Слишком много задач");
               return;
            }

            QuestSingle questSingle2 = (QuestSingle)this.vector.elementAt(itemIndex - 1);
            ItemSimple[] itemsCount;
            if ((itemsCount = Strategist.instance.isEnoughtItemsCount(questSingle2.takeItemsOnAccept, true)).length != 0) {
               messageText4 = "";

               for(lastMatchOffset = 0; lastMatchOffset < itemsCount.length; ++lastMatchOffset) {
                  ItemSimple itemSimple = itemsCount[lastMatchOffset];
                  messageText4 = messageText4 + (messageText4.length() > 0 ? "\n" : "") + InventoryItem.getNameForMenu(itemSimple.count, "", itemSimple.nameVis);
               }

               this.showMenuMessage(203, Mystery.stringFormat("Требуется:\n%", new String[]{messageText4}));
               return;
            }

            if (questSingle2.takeOnlyIfFinishable && (!questSingle2.isDone(Strategist.instance, (QuestInPlayer)null) || questSingle2.isAvailableByReward(Strategist.instance) >= 0)) {
               if (!questSingle2.takeOnlyIfFinishable) {
                  return;
               }

               if (!questSingle2.isDone(Strategist.instance, (QuestInPlayer)null)) {
                  if (questSingle2.isNeedJustCrystals(Strategist.instance, (QuestInPlayer)null)) {
                     ServicesMenuList.suggestBuyCrystals((Delegate)null, new Delegate(new MenuShowListDelegate(196), 0), questSingle2.needCrys, "Не хватает кристаллов.");
                  } else if (questSingle2.isNeedJustGold(Strategist.instance, (QuestInPlayer)null)) {
                     delegateTakeQuest = questSingle2;
                     ServicesMenuList.confirmGoldPurchaseWithMessage(new Delegate(this, 3), new Delegate(new MenuShowListDelegate(196), 0), (long)questSingle2.needMoney, "Не хватает %needvalɼɽ.");
                  } else {
                     QuestSupItemNeed questSupItemNeed;
                     if ((questSupItemNeed = questSingle2.isNeedJustSelleableItem((Strategist)Strategist.instance, (QuestInPlayer)null)) != null) {
                        delegateTakeQuest = questSingle2;
                        ServicesMenuList.suggestBuyItem(new Delegate(this, 3), new Delegate(new MenuShowListDelegate(196), 0), questSupItemNeed, "Не хватает вещей.");
                     } else {
                        showFloatMessageS("Нет нужных вещей");
                     }
                  }
               }

               if ((menuId = questSingle2.isAvailableByReward(Strategist.instance)) >= 0) {
                  if (menuId == 0) {
                     showFloatMessageS("Не получается взять награду, нет места");
                     return;
                  }

                  showFloatMessageS("Не получается взять награду, активен другой героизм");
               }
               break;
            }

            delegateTakeQuest = questSingle2;
            this.executeDelegated(3);
            break;
         case 199:
         case 200:
            questMyBackMenuInd = menuId;
            questSelectedInPlayer = (QuestInPlayer)QuestStrateg.instance.quests.elementAt(itemIndex);
            menuId2 = 235;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 206:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(1458000);
               netRequest.request.writeInt(4);
               switch (itemIndex) {
                  case 0:
                     netRequest.request.writeInt(2);
                     break;
                  case 1:
                     netRequest.request.writeInt(1);
                     break;
                  case 2:
                     netRequest.request.writeInt(0);
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 1458001) {
                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  showFloatMessageS("Успех!");
               }
            } catch (Exception exception6) {
               exception6.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId2 = 71;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 209:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(1458000);
               netRequest.request.writeInt(8);
               switch (itemIndex) {
                  case 0:
                     netRequest.request.writeInt(0);
                     break;
                  case 1:
                     netRequest.request.writeInt(1);
                     break;
                  case 2:
                     netRequest.request.writeInt(2);
                     break;
                  case 3:
                     netRequest.request.writeInt(3);
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 1458001) {
                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  showFloatMessageS("Успех!");
               }
            } catch (Exception exception7) {
               exception7.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId2 = 71;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 226:
            if (itemIndex == 0) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1461000);
                  Com.sendRequest(netRequest);
               } catch (Exception exception8) {
                  exception8.printStackTrace();
               }

               Com.PrBarTh.Stop();
               PlatformRuntime.getInstance();
            }

            GameEffects.instance.stopSkullsFall2();
            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 227:
            if (itemIndex == 0) {
               try {
                  messageText4 = Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(StringResources.SHARE_DESCRIPTIONS_LIST[Mystery.RandInt(1)], "%title", "Последний Предел"), "%img", Com.c_share_vk_image_url), "%id", String.valueOf(Strategist.instance.id)), "%nameLiteral", String.valueOf(Strategist.instance.GP_Name)), "%level", String.valueOf(Strategist.instance.GP_Level));
                  messageText3 = "http://www.LastLimit.ru/client/%id/";
                  messageText3 = Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace("http://www.LastLimit.ru/client/%id/", "%img", Mystery.encodeURL(Com.c_share_vk_image_url)), "%title", Mystery.encodeURL("Последний Предел")), "%desc", Mystery.encodeURL(messageText4)), "%id", Mystery.encodeURL(String.valueOf(Strategist.instance.id))), "%nameLiteral", Mystery.encodeURL(String.valueOf(Strategist.instance.GP_Name))), "%level", Mystery.encodeURL(String.valueOf(Strategist.instance.GP_Level)));
                  String messageText2 = "http://vk.com/share.php?url=%url&title=%title&description=%desc&image=%img";
                  messageText2 = Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace(Mystery.stringReplace("http://vk.com/share.php?url=%url&title=%title&description=%desc&image=%img", "%url", Mystery.encodeURL(messageText3)), "%title", Mystery.encodeURL("Последний Предел")), "%img", Mystery.encodeURL(Com.c_share_vk_image_url)), "%desc", Mystery.encodeURL(messageText4)), "%id", Mystery.encodeURL(String.valueOf(Strategist.instance.id))), "%nameLiteral", Mystery.encodeURL(String.valueOf(Strategist.instance.GP_Name))), "%level", Mystery.encodeURL(String.valueOf(Strategist.instance.GP_Level)));
                  Com.midlet.openBrowser(messageText2);
               } catch (Exception exception9) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }
            }

            GameEffects.instance.stopSkullsFall2();
            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 235:
            switch (itemIndex) {
               case 0:
                  QuestStrateg.instance.changeCompassedQuest(questSelectedInPlayer);
                  menuId = questMyBackMenuInd;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               case 1:
                  this.showConfirmationForMenu(201, "Отказаться от этой задачи?");
               default:
                  return;
            }
         case 236:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(1458000);
               netRequest.request.writeInt(6);
               switch (itemIndex) {
                  case 0:
                     netRequest.request.writeInt(3);
                     break;
                  case 1:
                     netRequest.request.writeInt(1);
                     break;
                  case 2:
                     netRequest.request.writeInt(2);
                     break;
                  case 3:
                     netRequest.request.writeInt(0);
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 1458001) {
                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  showFloatMessageS("Успех!");
               }
            } catch (Exception exception10) {
               exception10.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId2 = 71;
            MenuManager.instance.showMenuById(menuId2);
            return;
      }

   }

   private static void sendFile(FileConnectionAdapter fileConnectionAdapter, NetRequest netRequest) {
      InputStream inputStream = fileConnectionAdapter.openInputStream();
      byte[] bytes = new byte[1024];
      long fileSize = fileConnectionAdapter.fileSize();
      long bytesSent = 0L;
      decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, (int)fileSize);

      while(bytesSent < fileSize) {
         int uncheckedDataInput = decompiledsupport.UncheckedDataInput.read(inputStream, bytes, 0, (int)Math.min(1024L, fileSize - bytesSent));
         decompiledsupport.UncheckedDataInput.write(netRequest.request, bytes, 0, uncheckedDataInput);
         bytesSent += (long)uncheckedDataInput;
      }

      decompiledsupport.UncheckedDataInput.close(inputStream);
   }

   public final void executeDelegated(int context) {
      switch (context) {
         case 1:
            this.showConfirmationForMenu(194, getPurchaseConfirmation(MenuSupport.instance.itemsSellerSoldItems[this.itemsSellerReBuyItemInd].getItemDisplayName(), MenuSupport.instance.itemsSellerSoldItems[this.itemsSellerReBuyItemInd].count, MenuSupport.instance.itemsSellerSoldItems[this.itemsSellerReBuyItemInd].costGold, MenuSupport.instance.itemsSellerSoldItems[this.itemsSellerReBuyItemInd].costCrys, true));
            return;
         case 2:
            context = MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].count;
            if (MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costGold > 0) {
               context = Math.min(context, Strategist.instance.GP_Money / MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costGold);
            }

            if (!MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].unweight) {
               int itemsWeight = Strategist.instance.getItemsWeight();
               if (context / MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].count > Strategist.instance.getMaxInventorySize() - itemsWeight) {
                  context = (Strategist.instance.getMaxInventorySize() - itemsWeight) * MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].count;
               }
            }

            if (MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costCrys > 0) {
               context = Math.min(context, Strategist.instance.GP_Crystals / MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costCrys);
            }

            if (context > 1) {
               this.showNumberEnter(193, 0, context, "");
               return;
            } else {
               this.itemsSellerBuyItemCount = context;
               this.showConfirmationForMenu(192, this.getPurchaseConfirmation(MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].getItemDisplayName(), context, MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costGold, MenuSupport.instance.itemsSellerItems[this.itemsSellerBuyItemInd].costCrys));
            }
         default:
            return;
         case 3:
            if (delegateTakeQuest.showYesNo && !delegateTakeQuest.isFreeForDone()) {
               questSelected = delegateTakeQuest;
               this.showConfirmationWithPortrait(197, delegateTakeQuest.overrideTextOnYesNo != null && delegateTakeQuest.overrideTextOnYesNo.length() > 0 ? delegateTakeQuest.overrideTextOnYesNo : "Взять задачу?\n" + delegateTakeQuest.name, false, (StrategGraphic)null, false, -1, -1, false, (int[][])null);
            } else {
               this.takeQuestAction(true, delegateTakeQuest);
            }
      }
   }

   private static boolean checkPurchaseRequirements(int count, int money, int crys, MenuList menuList2, Delegate delegate, boolean unweight) {
      if (!unweight && Strategist.instance.getItemsWeight() >= Strategist.instance.getMaxInventorySize()) {
         GameCommons.instance.showFloatText("Инвентарь полон!");
         return true;
      } else if (count <= 0) {
         GameCommons.instance.showFloatText("Вещи закончились");
         return true;
      } else if (Strategist.instance.GP_Money < money) {
         ServicesMenuList.confirmGoldPurchaseWithMessage(delegate, new Delegate(new MenuSnapshotDelegate(menuList2), 0), (long)money, (String)null);
         return true;
      } else if (Strategist.instance.GP_Crystals < crys) {
         ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(menuList2), 0), crys);
         return true;
      } else {
         return false;
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      NetRequest netRequest;
      boolean actionSucceeded;
      int response;
      QuestStrateg questStrateg;
      int integerCode;
      short menuId;
      byte menuId2;
      GameMapObjectsController gameMapObjectsController;
      switch (backIndex) {
         case 53:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4403000);
               netRequest.request.writeInt(MenuSupport.instance.curFriend);
               netRequest.request.writeInt(res ? 0 : 1);
               Com.sendRequest(netRequest);
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            MenuSupport.instance.isMembersForClanRequest = true;
            menuId2 = 51;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 165:
            if (res) {
               BattleNetTh btNetTh2 = BattleNetTh.btNetTh;
               if (!BattleTh.realBattle && (btNetTh2 == null || !btNetTh2.active)) {
                  BattleManager.addBattleFinishEventToActualBattle();
               } else if (BattleTh.viewBattleWillEndTick < 0) {
                  BattleTh.viewBattleWillEndTick = BattleTh.viewBattleLastAddedEventsTillTick;
               }
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 166:
            netRequest = new NetRequest();

            try {
               if (res) {
                  netRequest.writeHeader(7910000);
                  netRequest.request.writeBoolean(true);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 7910001) {
                     Strategist.instance.partyState = 0;
                     Strategist.instance.partyId = netRequest.response.readInt();
                  } else if (integerCode == 7910002) {
                     GameCommons.instance.showFloatText((String)"Провал!", 3000);
                  }
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 167:
         case 168:
            if (res) {
               actionSucceeded = true;
               if (backIndex == 168 && Com.AurasTypes[MenuSupport.instance.menuAuraInd].cost > Strategist.instance.GP_Crystals) {
                  GameCommons.instance.showFloatText((String)"Не хватает кристаллов!", 3000);
                  actionSucceeded = false;
               }

               if (actionSucceeded) {
                  netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(4640000);
                     if (backIndex == 167) {
                        netRequest.request.writeInt(-1);
                     } else {
                        netRequest.request.writeInt(MenuSupport.instance.menuAuraInd);
                     }

                     Com.sendRequest(netRequest);
                     if ((response = netRequest.response.readInt()) == 4640001) {
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                        GameCommons.instance.showFloatText((String)"Удачно!", 4000);
                     } else if (response == 4640002) {
                        GameCommons.instance.showFloatText((String)"Провал!", 4000);
                     }
                  } catch (Exception exception3) {
                     exception3.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               }
            }

            menuId2 = 66;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 169:
            if (res) {
               Com.CoverTh.Set(3);
               return;
            }

            menuId2 = 1;
            MenuManager.instance.showMenuById(menuId2);
            break;
         case 186:
            MenuManager.instance.guiHandler.hideAllWindows();
            if (res) {
               gameMapObjectsController = GameMapObjectsController.instance;
               GameMapObjectsController.actionMapObjectMapPortalUse(MenuSupport.instance.curActiveTownInd);
               return;
            }
            break;
         case 187:
            MenuManager.instance.guiHandler.hideAllWindows();
            if (res) {
               gameMapObjectsController = GameMapObjectsController.instance;
               GameMapObjectsController.actionMapObjectDirectPortalUse(MenuSupport.instance.curActiveTownInd);
               return;
            }
            break;
         case 188:
            MenuManager.instance.guiHandler.hideAllWindows();
            if (res) {
               gameMapObjectsController = GameMapObjectsController.instance;
               GameMapObjectsController.destroyMapObject(MenuSupport.instance.curActiveTownInd);
               return;
            }
            break;
         case 189:
            MenuManager.instance.guiHandler.hideAllWindows();
            if (res) {
               gameMapObjectsController = GameMapObjectsController.instance;
               GameMapObjectsController.actionMapObjectMonsterSellerUse(MenuSupport.instance.curActiveTownInd);
               return;
            }
            break;
         case 192:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140230);
                  netRequest.request.writeUTF(MenuSupport.instance.string5);
                  netRequest.request.writeInt(this.itemsSellerBuyItemInd);
                  netRequest.request.writeInt(this.itemsSellerBuyItemCount);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 4140231) {
                     GameMapObjectsController.loadItemsSellerItemsData(netRequest);
                     Com.applyRewardEvents(netRequest);
                  } else if (integerCode == 4140232) {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  } else if (integerCode == 4140004) {
                     GameMapObjectsController.brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception4) {
                  exception4.printStackTrace();
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
            }

            menuId = 190;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 194:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4140220);
                  netRequest.request.writeUTF(MenuSupport.instance.string5);
                  netRequest.request.writeInt(this.itemsSellerReBuyItemInd);
                  Com.sendRequest(netRequest);
                  if ((integerCode = netRequest.response.readInt()) == 4140221) {
                     GameMapObjectsController.loadItemsSellerSoldData(netRequest);
                     Com.applyRewardEvents(netRequest);
                  } else if (integerCode == 4140222) {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  } else if (integerCode == 4140004) {
                     GameMapObjectsController.brokeMapObjectsOrder();
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception5) {
                  Com.SendMistace("Error in load for items seller", true, false, exception5);
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
            }

            menuId = 191;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 197:
         case 198:
            if (backIndex == 198) {
               actionSucceeded = true;
               if (res) {
                  Com.PrBarTh.Set(true);
                  netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(4141010);
                     questSelected.saveToStreamId((DataOutputStream)netRequest.request);
                     Com.sendRequest(netRequest);
                     if (netRequest.response.readInt() == 4141011) {
                        response = netRequest.response.readShort();
                        if (netRequest.response.readBoolean()) {
                           Com.applyRewardEvents(netRequest);
                        }

                        questStrateg = QuestStrateg.instance;
                        QuestStrateg.removeQuest(response, true);
                        actionSucceeded = false;
                        if (!GameMapObjectsController.instance.actionMapObjectQuestGiverForDelegate(true, true, false)) {
                           MenuManager.instance.guiHandler.hideAllWindows();
                        }
                     } else {
                        GameCommons.instance.showFloatText("Произошла ошибка!");
                     }
                  } catch (Exception exception6) {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }

                  Com.PrBarTh.Stop();
               }

               if (actionSucceeded) {
                  menuId = 196;
                  MenuManager.instance.showMenuById(menuId);
               }
            }

            if (backIndex == 197) {
               this.takeQuestAction(res, questSelected);
               return;
            }
            break;
         case 201:
            int menuId3;
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4141010);
                  questSelectedInPlayer.questSingle.saveToStreamId((DataOutputStream)netRequest.request);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4141011) {
                     response = netRequest.response.readShort();
                     if (netRequest.response.readBoolean()) {
                        Com.applyRewardEvents(netRequest);
                     }

                     questStrateg = QuestStrateg.instance;
                     QuestStrateg.removeQuest(response, true);
                  } else {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception7) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
               menuId3 = questMyBackMenuInd;
               MenuManager.instance.showMenuById(menuId3);
               return;
            }

            menuId3 = 235;
            MenuManager.instance.showMenuById(menuId3);
            return;
         case 204:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1300200);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() != 1300201) {
                     GameCommons.instance.showFloatText("Произошла ошибка!");
                  }
               } catch (Exception exception8) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 234:
            if (res) {
               GameMapObjectsController.instance.executeDelegated(4);
               return;
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 247:
            if (!res) {
               MenuManager.instance.guiHandler.hideAllWindows();
               return;
            }

            GameMapObjectsController instance2 = GameMapObjectsController.instance;
            GameCommons.instance.showFloatText(instance2.curFloatInfo);
            if (instance2.curNeedGold > 0L) {
               ServicesMenuList.confirmGoldPurchase(new Delegate(instance2, 3), new Delegate(instance2, 5), instance2.curNeedGold);
            } else {
               if (instance2.curNeedCrys <= 0) {
                  if (instance2.curNeedItem != null) {
                     ServicesMenuList.suggestBuyItem(new Delegate(instance2, 3), new Delegate(instance2, 5), instance2.curNeedItem);
                  }

                  return;
               }

               ServicesMenuList.suggestBuyCrystals((Delegate)null, new Delegate(instance2, 5), instance2.curNeedCrys);
            }
            break;
         case 249:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1300210);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() != 1300211) {
                     GameCommons.instance.showFloatText("Не удалось отступить");
                  }
               } catch (Exception exception9) {
                  GameCommons.instance.showFloatText("Произошла ошибка!");
               }

               Com.PrBarTh.Stop();
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            return;
      }

   }

   private void takeQuestAction(boolean isTake, QuestSingle questSingle2) {
      boolean toMenu = true;
      if (isTake) {
         Com.PrBarTh.Set(true);
         NetRequest netRequest = new NetRequest();

         try {
            netRequest.writeHeader(4140920);
            questSingle2.saveToStreamId((DataOutputStream)netRequest.request);
            Com.sendRequest(netRequest);
            int integerCode;
            if ((integerCode = netRequest.response.readInt()) == 4140921) {
               QuestInPlayer questInPlayer = new QuestInPlayer(netRequest.response);
               if (netRequest.response.readBoolean()) {
                  String messageText = netRequest.response.readUTF();
                  Com.applyRewardEvents(netRequest);
                  showFloatMessageS(messageText);
               }

               QuestStrateg.instance.quests.addElement(questInPlayer);
               Com.onQuestsChanged(true);
               if (questInPlayer.questSingle.switchCompass) {
                  QuestStrateg.instance.changeCompassedQuest(questInPlayer);
               }

               toMenu = false;
               if (!GameMapObjectsController.instance.actionMapObjectQuestGiverForDelegate(true, true, true)) {
                  MenuManager.instance.guiHandler.hideAllWindows();
               }
            } else if (integerCode == 4140923) {
               toMenu = false;
               integerCode = netRequest.response.readInt();
               this.showOKMenuWithBackMenu(196, Mystery.stringFormat("Для взятия квеста нужно % мест в рюкзаке", '%', new String[]{String.valueOf(integerCode)}));
            } else {
               GameCommons.instance.showFloatText("Произошла ошибка!");
            }
         } catch (Exception exception) {
            GameCommons.instance.showFloatText("Произошла ошибка!");
         }

         Com.PrBarTh.Stop();
      }

      if (toMenu) {
         short menuId = 196;
         MenuManager.instance.showMenuById(menuId);
      }

   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String messageText) {
      byte menuId;
      NetRequest netRequest;
      switch (backIndex) {
         case 49:
            if (!res) {
               menuId = 48;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4420000);
               decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
               Com.sendRequest(netRequest);
               if (decompiledsupport.UncheckedDataInput.readInt(netRequest.response) == 4420001) {
                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  menuId = 48;
                  MenuManager.instance.showMenuById(menuId);
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            return;
         case 107:
            if (!res) {
               this.showWorldMapMenu();
               return;
            }

            if (messageText.indexOf(" ") >= 0 && MenuSupport.instance.newGameMode == 1) {
               GameCommons.instance.showFloatText("Нельзя использовать пробел в пароле");
               return;
            }

            if ((messageText.length() < Com.c_pass_min_len || messageText.length() > Com.c_pass_max_len) && MenuSupport.instance.newGameMode == 1) {
               GameCommons.instance.showFloatText("Неверная длина пароля");
               return;
            }

            if ((messageText.length() < 3 || messageText.length() > 15) && MenuSupport.instance.newGameMode == 0) {
               GameCommons.instance.showFloatText("Неверная длина пароля");
               return;
            }

            if (MenuSupport.isPasswordHaveJustLetters(messageText) && MenuSupport.instance.newGameMode == 1) {
               GameCommons.instance.showFloatText("Нужны буквы и символы!");
               return;
            }

            Com.newg_password = messageText;
            Com.savedPassword = messageText;
            MenuManager.instance.guiHandler.hideAllWindows();
            if (MenuSupport.instance.newGameMode == 1) {
               Com.newg_contact = "9999999";
               Com.tryRegistration();
               return;
            }

            Com.loginFromMenu((String)null);
            return;
         case 177:
            Com.newg_contact = messageText;
            if (res) {
               Com.tryRegistration();
               return;
            }

            this.showHeroMenu();
            return;
         case 178:
            if (!res) {
               messageText = "";
            }

            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.request.writeInt(7600000);
               decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
            } catch (IOException exception2) {
               exception2.printStackTrace();
            }

            Com.sendRequest(netRequest);
            Com.PrBarTh.Stop();
            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 179:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(5420000);
                  netRequest.request.writeUTF(MenuSupport.instance.string3);
                  decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
                  Com.sendRequest(netRequest);
               } catch (Exception exception3) {
                  exception3.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId = 69;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 180:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  FileConnectionAdapter fileConnectionAdapter = null;
                  if (MenuSupport.instance.photoData == null) {
                     fileConnectionAdapter = (FileConnectionAdapter)Black.openConnection("file://localhost/" + MenuSupport.instance.photoPath);
                  }

                  if (fileConnectionAdapter != null && !fileConnectionAdapter.exists()) {
                     GameCommons.instance.showFloatText((String)"Файл не найден", 3000);
                  } else {
                     try {
                        netRequest.request.writeInt(5400000);
                        decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
                        if (MenuSupport.instance.photoData == null) {
                           if (MenuSupport.instance.photoPath.toLowerCase().endsWith(".png")) {
                              netRequest.request.writeUTF("png");
                           } else if (MenuSupport.instance.photoPath.toLowerCase().endsWith(".jpg")) {
                              netRequest.request.writeUTF("jpg");
                           } else if (MenuSupport.instance.photoPath.toLowerCase().endsWith(".jpeg")) {
                              netRequest.request.writeUTF("jpeg");
                           }

                           sendFile(fileConnectionAdapter, netRequest);
                        } else {
                           netRequest.request.writeUTF(MenuSupport.instance.string.toLowerCase());
                           netRequest.request.writeInt(MenuSupport.instance.photoData.length);
                           netRequest.request.write(MenuSupport.instance.photoData);
                           MenuSupport.instance.photoData = null;
                        }

                        Com.sendRequest(netRequest);
                        GameCommons.instance.showFloatText((String)"Фото\nдобавлено", 3000);
                     } catch (Exception exception4) {
                        exception4.printStackTrace();
                     }
                  }

                  if (fileConnectionAdapter != null) {
                     fileConnectionAdapter.close();
                  }
               } catch (Exception exception5) {
                  exception5.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            PlatformRuntime.getInstance();
            menuId = 67;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 181:
            int spaceCount = 0;
            boolean startsWithSpace = messageText.startsWith(" ");
            boolean endsWithSpace = messageText.endsWith(" ");
            boolean hasDoubleSpace = messageText.indexOf("  ") >= 0;
            boolean hasInvalidCharacter = false;
            int nAME_CHECK_ALPHABITSIndex2;
            if (messageText.length() > 3) {
               for(nAME_CHECK_ALPHABITSIndex2 = 3; nAME_CHECK_ALPHABITSIndex2 < messageText.length(); ++nAME_CHECK_ALPHABITSIndex2) {
                  if (messageText.charAt(nAME_CHECK_ALPHABITSIndex2) == messageText.charAt(nAME_CHECK_ALPHABITSIndex2 - 1) && messageText.charAt(nAME_CHECK_ALPHABITSIndex2) == messageText.charAt(nAME_CHECK_ALPHABITSIndex2 - 2)) {
                     hasInvalidCharacter = true;
                     break;
                  }
               }
            }

            for(nAME_CHECK_ALPHABITSIndex2 = 0; nAME_CHECK_ALPHABITSIndex2 < 2; ++nAME_CHECK_ALPHABITSIndex2) {
               for(int nAME_CHECK_ALPHABITSIndex = 0; nAME_CHECK_ALPHABITSIndex < NAME_CHECK_ALPHABITS[nAME_CHECK_ALPHABITSIndex2].length; ++nAME_CHECK_ALPHABITSIndex) {
                  if (messageText.indexOf(NAME_CHECK_ALPHABITS[nAME_CHECK_ALPHABITSIndex2][nAME_CHECK_ALPHABITSIndex]) >= 0) {
                     ++spaceCount;
                     break;
                  }
               }
            }

            if (!res) {
               if (MenuSupport.instance.newGameMode == 2) {
                  menuId = 1;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               }

               if (MenuSupport.instance.newGameMode == 1) {
                  MenuSupport.instance.isCreateStrategFromServiceMenu = false;
                  menuId = 2;
                  MenuManager.instance.showMenuById(menuId);
                  return;
               }

               menuId = 1;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            if (MenuSupport.instance.newGameMode == 2) {
               Com.PrBarTh.Set(true);

               try {
                  (netRequest = new NetRequest()).writeHeader(1100030);
                  decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
                  Com.sendRequest(netRequest);
                  switch (decompiledsupport.UncheckedDataInput.readInt(netRequest.response)) {
                     case 1100031:
                        this.showMenuMessage(214, Mystery.stringFormat("Пароль от персонажа % выслан на указанную ранее почту. Проверьте папки ВХОДЯЩИЕ и СПАМ", '%', new String[]{messageText}));
                        return;
                     case 1100032:
                     default:
                        return;
                     case 1100033:
                        showFloatMessageS("Персонаж не найден");
                        return;
                     case 1100034:
                        this.showMenuMessage(213, "Слишком частый запрос пароля, попробуйте позже");
                        return;
                     case 1100035:
                        this.showMenuMessage(213, Mystery.stringFormat("Для персонажа % не была задана почта: она задается по достижении героем высокого уровня. См также ПОМОЩЬ на сайте www.LastLimit.ru", '%', new String[]{messageText}));
                  }
               } finally {
                  Com.PrBarTh.Stop();
               }
            } else {
               if (MenuSupport.instance.newGameMode == 1) {
                  if (spaceCount > 1) {
                     GameCommons.instance.showFloatText("В имени возможен только один язык");
                     return;
                  }

                  if (startsWithSpace) {
                     GameCommons.instance.showFloatText("Пробел в начале запрещен");
                     return;
                  }

                  if (endsWithSpace) {
                     GameCommons.instance.showFloatText("Пробел в конце запрещен");
                     return;
                  }

                  if (hasDoubleSpace) {
                     GameCommons.instance.showFloatText("Два пробела подряд запрещено");
                     return;
                  }

                  if (hasInvalidCharacter) {
                     GameCommons.instance.showFloatText("Три одинаковых символа подряд");
                     return;
                  }
               }

               if (messageText.length() >= Com.c_name_min_len && messageText.length() <= Com.c_name_max_len) {
                  Com.newg_name = messageText;
                  this.showHeroMenu();
                  return;
               }

               GameCommons.instance.showFloatText("Имя неверной длины!");
            }
            break;
         case 210:
            if (!res) {
               this.showWorldMapMenu();
               return;
            }

            Com.PrBarTh.Set(true);

            try {
               (netRequest = new NetRequest()).writeHeader(1100020);
               decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, Com.newg_name);
               decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, Com.newg_password);
               decompiledsupport.UncheckedDataInput.writeUTF(netRequest.request, messageText);
               Com.sendRequest(netRequest);
               switch (decompiledsupport.UncheckedDataInput.readInt(netRequest.response)) {
                  case 1100021:
                     this.showMenuMessage(212, Mystery.stringFormat("На почту % отправлено письмо со ссылкой для подтверждения. Вам нужно зайти в свой почтовый ящик, получить письмо от Последнего Предела и перейти по ссылке, указанной в этом письме. Если письма нет в ящике, загляните в почтовую папку СПАМ. Затем вы сможете зайти в игру героем %", '%', new String[]{messageText, Com.newg_name}));
                     break;
                  case 1100022:
                     showFloatMessageS("Персонаж не найден");
                     this.showWorldMapMenu();
                     break;
                  case 1100023:
                     showFloatMessageS("Ошибка!\nНеверный формат почты");
                     break;
                  case 1100024:
                     this.showMenuMessage(211, "Вы превысили количество попыток задать почту, попробуйте повторить попытку позже. А пока, попробуйте зайти в игру другим героем.");
               }
            } finally {
               Com.PrBarTh.Stop();
            }
      }

   }

   public final void onOKMenuEvent(int backIndex) {
      short menuId;
      byte menuId2;
      switch (backIndex) {
         case 24:
         case 25:
            GameEffects.instance.stopSkullsFall2();
            BattleTh.appleBattleRes();
            return;
         case 172:
            MenuManager.instance.guiHandler.setLockReplace(false);
            if (Com.BlackTh != null) {
               Com.BlackTh.setLockReplace(false);
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            Com.CoverTh.Set(2);
            return;
         case 173:
            GameCommons.instance.stopTimer();
            menuId2 = 1;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 174:
            if (MenuSupport.instance.curActiveTownInd >= 0) {
               Com.currentGameMap.towns[MenuSupport.instance.curActiveTownInd].activedAtTime = Mystery.currentTimeMillis;
            }

            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 175:
            MenuManager.instance.guiHandler.hideAllWindows();
            return;
         case 176:
            menuId2 = 19;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 184:
            menuId2 = 67;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 185:
            MenuManager.instance.guiHandler.hideAllWindows();
            break;
         case 195:
            MenuManager.instance.guiHandler.hideAllWindows();
            if (MenuSupport.instance.questSuccessInfoMap.equals(Com.currentGameMap.nameId) && MenuSupport.instance.curActiveTownInd >= 0) {
               GameMapObjectsController.instance.actionMapObjectQuestGiver(MenuSupport.instance.curActiveTownInd);
               return;
            }
            break;
         case 203:
            menuId = 196;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 211:
            this.showWorldMapMenu();
            return;
         case 212:
         case 214:
            menuId = 1;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 213:
            this.showWorldMapMenu();
            return;
      }

   }

   public static void showQuestsMenu(boolean fromGame) {
      MenuManager.instance.showMenuById(fromGame ? 199 : 200);
   }

   public final void showWorldMapMenu() {
      this.showTextEnter(181, "Введи имя (" + Com.c_name_min_len + "ə" + Com.c_name_max_len + " символов)", Com.newg_name);
   }

   public final void showHeroMenu() {
      this.showTextEnter(107, "Введи пароль (" + Com.c_pass_min_len + "ə" + Com.c_pass_max_len + " символов)", "");
   }

   public final void onTextChanged(String messageText) {
      super.onTextChanged(messageText);
      switch (this.curMenuId) {
         case 107:
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

   protected final void onCloseMenuEvent(int backIndex) {
      super.onCloseMenuEvent(backIndex);
      switch (backIndex) {
         case 174:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 195:
         case 196:
         case 234:
            if ((backIndex != 195 || MenuSupport.instance.questSuccessInfoMap.equals(Com.currentGameMap.nameId)) && MenuSupport.instance.curActiveTownInd >= 0 && MenuSupport.instance.curActiveTownInd < Com.currentGameMap.towns.length) {
               Com.currentGameMap.towns[MenuSupport.instance.curActiveTownInd].activedAtTime = Mystery.currentTimeMillis;
            }
         default:
            return;
         case 226:
         case 227:
            GameEffects.instance.stopSkullsFall2();
      }
   }
}
