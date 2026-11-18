package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Delegate;
import Engine.MathGame;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import MenuPck.MenuSupport;
import Moduls.StrategGraphic;
import Moduls.Strategist;
import Moduls.InventoryItem;
import Moduls.ItemSimple;
import Resources.StringResources;
import java.util.Vector;

public final class ItemsMenuList extends MenuList {
   private static int[] ITEMS_TYPES_BY_SLOT = new int[]{0, 3, 6, 1, 7, 4, 2, 8, 9, 8, 12, 8, 13, 11, 5, 10};
   private static String[] INFOS_BY_SLOT;
   private static Vector itemsForMutate;
   private static InventoryItem forMutateItem;
   private static int _curItemType;
   private static int _curItem;
   private static int _sellCount;
   private static int _sellType;
   private static byte selectPetUpgradeKey;
   private static boolean[] petsUpCans;
   private static String[] bySlotInfos;
   private static String[] bySlotSymbs;
   private static int[] bySlotCount;
   private static int[] bySlotCount2;
   private static int[][] bySlotDrawMob;
   private static StrategGraphic[] bySlotStrategGraphic;
   private static int toMarketValue;
   private static int delegateOpenItemInd;
   private static int delegateUpgradePetItemInd;
   private static int delegateUpgradePetKeyInd;
   private static int delegateUpgradeItemInd;

   public ItemsMenuList() {
      this(106, -1, "");
   }

   public ItemsMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      switch (id) {
         case 11:
            this.hints = Mystery.vectorFromString("Надеть выбраный предмет|Снять выбраный предмет|Использовать выбранный предмет прямой сейчас|Улучшить качества вещи за чистый пределий или кристаллы|Сделать из вещи новую вещь|Открыть|Открыть максимально возможное количество раз? (сколько хватит места и ключей)|Призвать|Улучшить характеристики питомца, обучив его|Выбери, чтобы сменить внешний вид другой вещи такого же типа, образец будет уничтожен|Починить выбранный предмет|Отправь вещь на рынок, за $% от выбранной цены|Моментально продать предмет за государственную цену");
         default:
            return;
         case 97:
      }
   }

   protected final void goBack() {
      if (this.curMenuId == 10) {
         this.showMyItemsMenu(false, false);
      } else {
         super.goBack();
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      switch (menuId) {
         case 5:
            if (GameCommons.instance.isFromGameMenu) {
               return -2;
            }

            return 0;
         case 74:
            return 63;
         default:
            return super.onGenerateBackIdEvent(menuId);
      }
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      InventoryItem inventoryItem2;
      switch (menuId) {
         case 10:
            ((InventoryItem)Strategist.instance.items.elementAt(MenuSupport.instance.itemsEquealsIds[itemIndex])).showInMenu((MenuList)this);
            return;
         case 11:
            if (itemIndex == 3) {
               setAnimatedTitle((inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).upgradeLevel < inventoryItem2.maxUpgrade ? Mystery.stringFormat("Улучшить предмет до [+%]", String.valueOf(inventoryItem2.upgradeLevel + 1)) : "Предельная заточка");
               return;
            } else if (itemIndex == 4) {
               setAnimatedTitle(((InventoryItem)Strategist.instance.items.elementAt(_curItem)).forgeInfo);
               return;
            } else if (itemIndex == 7) {
               setAnimatedTitle(((InventoryItem)Strategist.instance.items.elementAt(_curItem)).spawnDescription);
               return;
            } else if (itemIndex == 5) {
               setAnimatedTitle(((InventoryItem)Strategist.instance.items.elementAt(_curItem)).openInfo);
               return;
            } else if (itemIndex == 11) {
               setAnimatedTitle(Mystery.stringFormat("Отправь вещь на рынок, за $% от выбранной цены", '$', new String[]{String.valueOf(Com.c_market_place_cost_percent)}));
               return;
            } else if (itemIndex == 12) {
               setAnimatedTitle("Моментально продать предмет за " + Com.c_trade_to_gos_cost_procent + "% от цены");
               return;
            } else {
               super.onGenerateHintEvent(menuId, itemIndex);
            }
         default:
            return;
         case 97:
            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            setAnimatedTitle(getPetTrainingDescription(itemIndex, inventoryItem2));
            return;
         case 207:
            ((InventoryItem)itemsForMutate.elementAt(itemIndex)).showInMenu((MenuList)this);
      }
   }

   protected final void onInventoryChangedEvent(int backIndex, int slotIndex) {
      int itemCount;
      String messageText;
      switch (backIndex) {
         case 5:
            messageText = bySlotInfos[slotIndex];
            boolean drawMob = bySlotDrawMob[slotIndex] != null;
            int bodyIndex = 0;
            int headIndex = 0;
            if (drawMob) {
               bodyIndex = bySlotDrawMob[slotIndex][0];
               headIndex = bySlotDrawMob[slotIndex][1];
            }

            itemCount = bySlotCount[slotIndex];
            if (messageText != null) {
               messageText = messageText;
            } else {
               messageText = Mystery.stringFormat(INFOS_BY_SLOT[slotIndex], '%', new String[]{String.valueOf(itemCount)});
            }

            setAnimatedTitle(messageText);
            if (drawMob) {
               showUnitPortrait(bodyIndex, headIndex);
            }

            if (bySlotStrategGraphic[slotIndex] != null) {
               showAvatarPortrait(bySlotStrategGraphic[slotIndex]);
            }
            break;
         case 74:
            messageText = bySlotInfos[slotIndex];
            itemCount = bySlotCount[slotIndex];
            if (messageText != null) {
               messageText = messageText;
            } else {
               messageText = Mystery.stringFormat(INFOS_BY_SLOT[slotIndex], '%', new String[]{String.valueOf(itemCount)});
            }

            setAnimatedTitle(messageText);
            if (bySlotStrategGraphic[slotIndex] != null) {
               showAvatarPortrait(bySlotStrategGraphic[slotIndex]);
               return;
            }
      }

   }

   protected final void onInventorySelectEvent(int backIndex, int slotIndex) {
      switch (backIndex) {
         case 5:
            if (bySlotCount[slotIndex] > 0) {
               _curItemType = ITEMS_TYPES_BY_SLOT[slotIndex];
               byte menuId = 10;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
            return;
         case 74:
      }
   }

   protected final void onInventoryFixEvent(int backIndex) {
      switch (backIndex) {
         case 5:
            this.showConfirmationForMenu(121, "Починить все надетые вещи");
         default:
            return;
         case 74:
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      InventoryItem inventoryItem2;
      int sizeIndex;
      switch (menuId) {
         case 5:
            this.showMyItemsMenu(false, false);
            return LIST_IGNORE;
         case 10:
            sizeIndex = 0;
            int matchingItemCount = 0;

            for(menuId = 0; menuId < Strategist.instance.items.size(); ++menuId) {
               if (((InventoryItem)Strategist.instance.items.elementAt(menuId)).useType == _curItemType) {
                  if (((InventoryItem)Strategist.instance.items.elementAt(menuId)).state == 1) {
                     ++sizeIndex;
                  }

                  ++matchingItemCount;
               }
            }

            if (matchingItemCount == 0) {
               this.goBack();
               return null;
            }

            MenuSupport.instance.itemsEquealsIds = new int[matchingItemCount];
            matchingItemCount = -1;
            int itemsEquealsIdIndex = -1;

            for(menuId = 0; menuId < Strategist.instance.items.size(); ++menuId) {
               InventoryItem inventoryItem3;
               if ((inventoryItem3 = (InventoryItem)Strategist.instance.items.elementAt(menuId)).useType == _curItemType) {
                  if (inventoryItem3.state == 1) {
                     ++itemsEquealsIdIndex;
                     MenuSupport.instance.itemsEquealsIds[itemsEquealsIdIndex] = menuId;
                     vector3.insertElementAt("ɞɟ" + inventoryItem3.getNameForMenu(), itemsEquealsIdIndex);
                  } else {
                     ++matchingItemCount;
                     MenuSupport.instance.itemsEquealsIds[matchingItemCount + sizeIndex] = menuId;
                     vector3.addElement(inventoryItem3.getNameForMenu());
                  }
               }
            }

            return vector3;
         case 11:
            if (_curItem < 0 || _curItem >= Strategist.instance.items.size()) {
               byte menuId2 = 10;
               MenuManager.instance.showMenuById(menuId2);
               return LIST_IGNORE;
            }

            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            this.clearSkips();
            if ((Com.c_items_get_on_count[inventoryItem2.useType] > 0 || inventoryItem2.useType == 8 && inventoryItem2.battleSlot >= 0) && inventoryItem2.putonable) {
               if (inventoryItem2.state != 0) {
                  this.addSkip(0);
               } else {
                  this.addSkip(1);
               }
            } else {
               this.addSkip(0);
               this.addSkip(1);
            }

            if (!inventoryItem2.useable) {
               this.addSkip(2);
            }

            if (inventoryItem2.maxUpgrade <= 0) {
               this.addSkip(3);
            }

            if (!inventoryItem2.forgeAvail || inventoryItem2.upgradeLevel > 0) {
               this.addSkip(4);
            }

            if (inventoryItem2.openState == 0) {
               this.addSkip(5);
               this.addSkip(6);
            }

            if (!inventoryItem2.isCanSpawn) {
               this.addSkip(7);
               this.addSkip(8);
            }

            if (inventoryItem2.canMutateInd < 0) {
               this.addSkip(9);
            }

            if (inventoryItem2.repairCost < 0 || inventoryItem2.durability >= inventoryItem2.maxDurability || inventoryItem2.maxDurability <= 0) {
               this.addSkip(10);
            }

            if (inventoryItem2.unsoldeable) {
               this.addSkip(11);
               this.addSkip(12);
            } else if (!inventoryItem2.resell || inventoryItem2.justInClan) {
               this.addSkip(11);
            }

            vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            break;
         case 97:
            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            petsUpCans = new boolean[]{inventoryItem2.getUpgradeByKey((byte)0) < Com.c_items_pet_speed_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)1) < Com.c_items_pet_pull_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)2) < Com.c_items_pet_answer_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)3) < Com.c_items_pet_friends_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)4) < Com.c_items_pet_paralize_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)5) < Com.c_items_pet_poison_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)6) < Com.c_items_pet_hit_dist_inc.length - 1, inventoryItem2.getUpgradeByKey((byte)7) < Com.c_items_pet_health_inc.length - 1};
            InventoryItem inventoryItem4 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);

            for(sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
               vector3.setElementAt("(" + inventoryItem4.getUpgradeByKey((byte)sizeIndex) + ")" + vector3.elementAt(sizeIndex), sizeIndex);
            }

            return vector3;
         case 207:
            for(menuId = 0; menuId < itemsForMutate.size(); ++menuId) {
               inventoryItem2 = (InventoryItem)itemsForMutate.elementAt(menuId);
               vector3.addElement(inventoryItem2.getNameForMenu());
            }
      }

      return vector3;
   }

   protected final void onNumberCancelEvent(int backIndex) {
      byte menuId;
      switch (backIndex) {
         case 123:
            menuId = 11;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 124:
            menuId = 11;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   public final void onNumberEnterEvent(int backIndex, int inputNumber) {
      byte menuId;
      switch (backIndex) {
         case 123:
            if (_sellType == 6) {
               toMarketValue = inputNumber;
               int needMoney = inputNumber * Com.c_market_place_cost_percent / 100;
               if (Strategist.instance.GP_Money < needMoney) {
                  ServicesMenuList.confirmGoldPurchaseWithMessage(new Delegate(this, 5), new Delegate(new MenuSnapshotDelegate(this), -1), (long)needMoney, "Надо %needvalɼɽ за отправку на рынок.");
                  return;
               }

               this.executeDelegated(5);
               return;
            }

            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 124:
            InventoryItem inventoryItem2;
            if (_sellType == 0) {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
               _sellCount = inputNumber;
               String messageText = "Продать за";
               if (inventoryItem2.costGold > 0 || inventoryItem2.crysSell <= 0) {
                  messageText = messageText + " " + _sellCount * inventoryItem2.costGold * Com.c_trade_to_gos_cost_procent / 100 + "ɼɽ";
               }

               if (inventoryItem2.crysSell > 0) {
                  messageText = messageText + " " + _sellCount * inventoryItem2.crysSell + "ɾɿ";
               }

               this.showConfirmationForMenu(119, messageText);
               return;
            } else if (_sellType == 6) {
               _sellCount = inputNumber;
               InventoryItem inventoryItem3 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
               long sellCount = (long)_sellCount;
               inventoryItem2 = inventoryItem3;
               long basePrice;
               long minimumPrice = (basePrice = (long)(inventoryItem3.costGold + inventoryItem2.costCrys * Com.c_serv_gold_add) * sellCount) * (long)Com.c_market_min_cost_percent / 100L;
               long c_market_max_cost_percent = basePrice * (long)Com.c_market_max_cost_percent / 100L;
               this.showNumberEnter(123, (int)minimumPrice, (int)c_market_max_cost_percent, "ɼɽ", Mystery.stringFormat("По какой цене? Цена указывается за все вещи. Стоимость размещения на рынке $% от указанной цены.", '$', new String[]{String.valueOf(Com.c_market_place_cost_percent)}));
               return;
            } else {
               menuId = 10;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   public final void onNumberChangedEvent(int backIndex, int inputNumber) {
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      InventoryItem inventoryItem2;
      byte menuId2;
      switch (menuId) {
         case 10:
            _curItem = MenuSupport.instance.itemsEquealsIds[itemIndex];
            if (!((InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
               showFloatMessageS("Вещь еще нестабильна\nВернись в город");
               return;
            }

            menuId2 = 11;
            MenuManager.instance.showMenuById(menuId2);
            return;
         case 11:
            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            NetRequest netRequest;
            int sizeIndex;
            switch (itemIndex) {
               case 0:
               case 1:
               case 2:
                  if ((itemIndex == 0 || itemIndex == 2) && !inventoryItem2.isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  }

                  Com.PrBarTh.Set(true);
                  netRequest = new NetRequest();

                  try {
                     if (itemIndex == 0) {
                        netRequest.writeHeader(4310000);
                     } else if (itemIndex == 1) {
                        netRequest.writeHeader(4320000);
                     } else {
                        netRequest.writeHeader(4332000);
                     }

                     netRequest.request.writeInt(_curItem);
                     netRequest.request.writeLong(inventoryItem2.typeId);
                     Com.sendRequest(netRequest);
                     if ((sizeIndex = netRequest.response.readInt()) != 4310001 && sizeIndex != 4320001 && sizeIndex != 4332001) {
                        if (itemIndex == 0) {
                           showFloatMessageS("Нельзя надеть!");
                        } else if (itemIndex == 1) {
                           showFloatMessageS("Нельзя снять!");
                        } else {
                           showFloatMessageS("Нельзя использовать!");
                        }
                     } else {
                        Com.applyRewardEvents(netRequest);
                        if (itemIndex == 0) {
                           showFloatMessageS("Надето!");
                        } else if (itemIndex == 1) {
                           showFloatMessageS("Снято!");
                        } else {
                           showFloatMessageS("Использовано!");
                        }
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
                  menuId2 = 10;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 3:
                  if ((inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).upgradeLevel >= inventoryItem2.maxUpgrade) {
                     showFloatMessageS("Достигнут предел");
                     return;
                  }

                  sizeIndex = Com.findValueIndex(Com.c_items_upgrade_power_inc, inventoryItem2.upgradeLevel + 1);
                  this.showConfirmationForMenu(237, "Нужно " + Com.findValueIndex(Com.c_items_upgrade_cost_chip, inventoryItem2.upgradeLevel) + Com.c_items_upgrade_chip_item_symb + " " + Com.findValueIndex(Com.c_items_upgrade_cost_money, inventoryItem2.upgradeLevel) + "ɼɽ\nШанс успеха " + Com.findValueIndex(Com.c_items_upgrade_chance, inventoryItem2.upgradeLevel) + "%" + (inventoryItem2.changeHitPowerBase == 0 ? "" : "\n" + inventoryItem2.changeHitPowerNow + "ɲɳ >> " + (long)inventoryItem2.changeHitPowerBase * (long)(sizeIndex + 100) / 100L + "ɲɳ") + (inventoryItem2.changeLifeBase == 0 ? "" : "\n" + (inventoryItem2.changeLifeBase > 0 ? "+" : "") + inventoryItem2.changeLifeNow + "ʎʏ >> " + (inventoryItem2.changeLifeBase > 0 ? "+" : "") + (long)inventoryItem2.changeLifeBase * (long)(sizeIndex + 100) / 100L + "ʎʏ") + (inventoryItem2.changeAtackBase == 0 ? "" : "\n" + inventoryItem2.changeAtackNow + "ɮɯ >> " + (long)inventoryItem2.changeAtackBase * (long)(sizeIndex + 100) / 100L + "ɮɯ") + (inventoryItem2.changeDefenceBase == 0 ? "" : "\n" + inventoryItem2.changeDefenceNow + "ɰɱ >> " + (long)inventoryItem2.changeDefenceBase * (long)(sizeIndex + 100) / 100L + "ɰɱ"));
                  return;
               case 4:
                  this.executeDelegated(6);
                  return;
               case 5:
                  inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
                  delegateOpenItemInd = _curItem;
                  if (!inventoryItem2.isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  } else if (inventoryItem2.openState == 3) {
                     showFloatMessageS("Прочность на нуле");
                     return;
                  } else {
                     if (inventoryItem2.openState != 1) {
                        if (inventoryItem2.openItem != null) {
                           ServicesMenuList.suggestBuyItem(new Delegate(this, 9), new Delegate(new MenuSnapshotDelegate(this), 0), inventoryItem2.openItem);
                           return;
                        }

                        showFloatMessageS("Не хватает\rвещей!");
                        return;
                     }

                     this.executeDelegated(9);
                     return;
                  }
               case 6:
                  if (!(inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  } else if (inventoryItem2.openState == 3) {
                     showFloatMessageS("Прочность на нуле");
                     return;
                  } else {
                     if (inventoryItem2.openState != 1) {
                        showFloatMessageS("Не хватает\rвещей!");
                        return;
                     }

                     boolean notGo = false;
                     Com.PrBarTh.Set(true);
                     netRequest = new NetRequest();

                     try {
                        netRequest.writeHeader(4339000);
                        netRequest.request.writeInt(_curItem);
                        netRequest.request.writeLong(inventoryItem2.typeId);
                        Com.sendRequest(netRequest);
                        if (netRequest.response.readInt() == 4339001) {
                           String messageText = netRequest.response.readUTF();
                           this.showMenuMessage(117, messageText);
                           notGo = true;
                        } else {
                           showFloatMessageS("Неудача");
                        }

                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     } catch (Exception exception2) {
                        exception2.printStackTrace();
                     }

                     Com.PrBarTh.Stop();
                     if (!notGo) {
                        menuId2 = 10;
                        MenuManager.instance.showMenuById(menuId2);
                     }

                     return;
                  }
               case 7:
                  if (!(inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  } else {
                     if (inventoryItem2.durability <= 0) {
                        showFloatMessageS("Прочность на нуле");
                        return;
                     }

                     Com.PrBarTh.Set(true);
                     netRequest = new NetRequest();

                     try {
                        netRequest.writeHeader(4337000);
                        netRequest.request.writeInt(_curItem);
                        netRequest.request.writeLong(inventoryItem2.typeId);
                        Com.sendRequest(netRequest);
                        if (netRequest.response.readInt() == 4337001) {
                           showFloatMessageS("Призыв удачен");
                        } else {
                           showFloatMessageS("Призыв неудачен");
                        }

                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     } catch (Exception exception3) {
                        exception3.printStackTrace();
                     }

                     Com.PrBarTh.Stop();
                     menuId2 = 10;
                     MenuManager.instance.showMenuById(menuId2);
                     return;
                  }
               case 8:
                  menuId2 = 97;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 9:
                  if (inventoryItem2.canUse) {
                     itemsForMutate.removeAllElements();

                     for(sizeIndex = 0; sizeIndex < Strategist.instance.items.size(); ++sizeIndex) {
                        InventoryItem inventoryItem3;
                        if ((inventoryItem3 = (InventoryItem)Strategist.instance.items.elementAt(sizeIndex)).canBeMutatedInd == inventoryItem2.canMutateInd && inventoryItem3.canBeMutatedInd >= 0 && sizeIndex != _curItem) {
                           itemsForMutate.addElement(inventoryItem3);
                        }
                     }

                     if (itemsForMutate.size() <= 0) {
                        showFloatMessageS("Нет подходящих вещей");
                        return;
                     }

                     short menuId3 = 207;
                     MenuManager.instance.showMenuById(menuId3);
                     return;
                  }

                  showFloatMessageS("Нельзя использовать!");
                  return;
               case 10:
                  _sellType = 0;
                  inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
                  if ((long)Strategist.instance.GP_Money < MathGame.ceilDivide((long)inventoryItem2.repairCost, (long)inventoryItem2.maxDurability)) {
                     ServicesMenuList.confirmPurchaseWithCurrencies(new Delegate(this, 4), new Delegate(new MenuSnapshotDelegate(this), -1), MathGame.ceilDivide((long)inventoryItem2.repairCost, (long)inventoryItem2.maxDurability), MathGame.ceilDivide((long)(inventoryItem2.repairCost * (inventoryItem2.maxDurability - inventoryItem2.durability)), (long)inventoryItem2.maxDurability), (String)null, (String)null, (String)null);
                     return;
                  }

                  this.executeDelegated(4);
                  return;
               case 11:
                  if (!(inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  } else if (inventoryItem2.unsoldeable) {
                     showFloatMessageS("Вещь нельзя сбыть");
                     return;
                  } else {
                     _sellType = 6;
                     if (inventoryItem2.count == 1) {
                        this.onNumberEnterEvent(124, 1);
                        return;
                     }

                     this.showNumberEnter(124, 1, inventoryItem2.count, "", "Сколько штук продать?");
                     return;
                  }
               case 12:
                  if (!(inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
                     showFloatMessageS("Вещь еще нестабильна\nВернись в город");
                     return;
                  } else if (inventoryItem2.unsoldeable) {
                     showFloatMessageS("Вещь нельзя сбыть");
                     return;
                  } else {
                     _sellType = 0;
                     if (inventoryItem2.count == 1) {
                        this.onNumberEnterEvent(124, 1);
                        return;
                     } else {
                        this.showNumberEnter(124, 1, inventoryItem2.count, "", "Сколько штук продать?");
                     }
                  }
            }
         default:
            return;
         case 97:
            if (petsUpCans[itemIndex]) {
               String petTrainingDescription = getPetTrainingDescription(selectPetUpgradeKey = (byte)itemIndex, (InventoryItem)Strategist.instance.items.elementAt(_curItem));
               this.showConfirmationForMenu(238, petTrainingDescription);
               return;
            }

            showFloatMessageS("Достигнут предел");
            return;
         case 207:
            forMutateItem = (InventoryItem)itemsForMutate.elementAt(itemIndex);
            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            this.showConfirmationForMenu(208, Mystery.stringFormat("Уверен что хочешь, чтобы % стал выглядеть как %?\nОбразец будет уничтожен", '%', new String[]{forMutateItem.getItemDisplayName(), inventoryItem2.getItemDisplayName()}));
      }
   }

   private static String getPetTrainingDescription(int upgradeKey, InventoryItem inventoryItem2) {
      String messageText = "";
      int upgradeByKey = 0;
      String messageText2 = "";
      switch (upgradeKey) {
         case 0:
            messageText = Com.c_items_pet_speed_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)0);
            messageText2 = Mystery.stringFormat("Сейчас бег питомца увеличен на $%\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_speed_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_speed_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения бег станет больше на $%", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_speed_inc, upgradeByKey + 1))}));
            break;
         case 1:
            messageText = Com.c_items_pet_pull_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)1);
            messageText2 = Mystery.stringFormat("Сейчас притягивание питомца $\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_pull_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_pull_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения притягивание станет равно $", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_pull_inc, upgradeByKey + 1))}));
            break;
         case 2:
            messageText = Com.c_items_pet_answer_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)2);
            messageText2 = Mystery.stringFormat("Сейчас отдача питомца $%\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_answer_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_answer_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения отдача станет $%", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_answer_inc, upgradeByKey + 1))}));
            break;
         case 3:
            messageText = Com.c_items_pet_friends_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)3);
            messageText2 = Mystery.stringFormat("Сейчас усиление от союзников у питомца $%\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_friends_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_friends_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения усиление от союзников станет $%", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_friends_inc, upgradeByKey + 1))}));
            break;
         case 4:
            messageText = Com.c_items_pet_paralize_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)4);
            messageText2 = Mystery.stringFormat("Сейчас питомец замораживает на $ с\n", '$', new String[]{Mystery.floatToString(Com.findValueIndex(Com.c_items_pet_paralize_inc, upgradeByKey), 1000)}) + (upgradeByKey >= Com.c_items_pet_paralize_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения питомец станет замораживать на $ с", '$', new String[]{Mystery.floatToString(Com.findValueIndex(Com.c_items_pet_paralize_inc, upgradeByKey + 1), 1000)}));
            break;
         case 5:
            messageText = Com.c_items_pet_poison_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)5);
            messageText2 = Mystery.stringFormat("Сейчас поджог питомца $% от силы удара\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_poison_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_poison_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения поджог станет $% от силы удара питомца", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_poison_inc, upgradeByKey + 1))}));
            break;
         case 6:
            messageText = Com.c_items_pet_hit_dist_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)6);
            messageText2 = Mystery.stringFormat("Сейчас дальность удара питомца увеличена на $%\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_hit_dist_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_hit_dist_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения дальность удара станет больше на $%", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_hit_dist_inc, upgradeByKey + 1))}));
            break;
         case 7:
            messageText = Com.c_items_pet_health_chip_item_symb;
            upgradeByKey = inventoryItem2.getUpgradeByKey((byte)7);
            messageText2 = Mystery.stringFormat("Сейчас здоровье питомца увеличено на $%\n", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_health_inc, upgradeByKey))}) + (upgradeByKey >= Com.c_items_pet_health_inc.length - 1 ? "Максимально." : Mystery.stringFormat("После обучения здоровье станет больше на $%", '$', new String[]{Integer.toString(Com.findValueIndex(Com.c_items_pet_health_inc, upgradeByKey + 1))}));
      }

      return "Нужно " + Com.findValueIndex(Com.c_items_pet_cost_chip, upgradeByKey) + messageText + " " + Com.findValueIndex(Com.c_items_pet_cost_money, upgradeByKey) + "ɼɽ\n" + "Шанс успеха " + Com.findValueIndex(Com.c_items_pet_chance_success, upgradeByKey) + "%\n" + messageText2;
   }

   public final void onOKMenuEvent(int backIndex) {
      switch (backIndex) {
         case 117:
            byte menuId = 10;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      int _curItem2 = _curItem;
      byte menuId;
      InventoryItem inventoryItem2;
      NetRequest netRequest;
      switch (backIndex) {
         case 118:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);

               try {
                  netRequest.writeHeader(4335000);
                  netRequest.request.writeInt(_curItem);
                  netRequest.request.writeLong(inventoryItem2.typeId);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4335001) {
                     showFloatMessageS("+" + netRequest.response.readUTF());
                  } else {
                     showFloatMessageS("Неудача");
                  }

                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               menuId = 10;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            menuId = 11;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 119:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem2);

               try {
                  netRequest.writeHeader(4330000);
                  netRequest.request.writeInt(_curItem2);
                  netRequest.request.writeLong(inventoryItem2.typeId);
                  netRequest.request.writeInt(_sellCount);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4330001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     showFloatMessageS("Продано!");
                  } else {
                     showFloatMessageS("Не продано!");
                  }
               } catch (Exception exception2) {
                  exception2.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 120:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem2);

               try {
                  netRequest.writeHeader(4331000);
                  netRequest.request.writeInt(_curItem2);
                  netRequest.request.writeLong(inventoryItem2.typeId);
                  netRequest.request.writeInt(_sellCount);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4331001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     showFloatMessageS("Отремонтировано!");
                  } else {
                     showFloatMessageS("Не отремонтировано!");
                  }
               } catch (Exception exception3) {
                  exception3.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 121:
            if (res) {
               this.executeDelegated(2);
               return;
            }

            this.executeDelegated(1);
            break;
         case 208:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);

               try {
                  netRequest.writeHeader(4350000);
                  netRequest.request.writeInt(_curItem);
                  netRequest.request.writeLong(inventoryItem2.typeId);
                  netRequest.request.writeInt(Strategist.instance.items.indexOf(forMutateItem));
                  netRequest.request.writeLong(forMutateItem.typeId);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4350001) {
                     showFloatMessageS("Успех!");
                  } else {
                     showFloatMessageS("Неудача");
                  }

                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               } catch (Exception exception4) {
                  exception4.printStackTrace();
               }

               Com.PrBarTh.Stop();
               menuId = 10;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            menuId = 11;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 237:
            if (!res) {
               menuId = 11;
               MenuManager.instance.showMenuById(menuId);
               return;
            }

            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            delegateUpgradePetItemInd = _curItem;
            res = true;
            if (inventoryItem2.isCanUpgradeCheap) {
               int c_items_upgrade_cost_moneyCount = Com.c_items_upgrade_cost_money[Math.min(inventoryItem2.upgradeLevel, Com.c_items_upgrade_cost_money.length - 1)];
               if (Strategist.instance.GP_Money < c_items_upgrade_cost_moneyCount) {
                  Delegate delegate = new Delegate(new MenuSnapshotDelegate(this), 0);
                  ServicesMenuList.confirmGoldPurchase(new Delegate(this, 7), delegate, (long)c_items_upgrade_cost_moneyCount);
                  res = false;
               }
            } else {
               if (!Strategist.instance.isEnoughtItems(Com.c_items_upgrade_chip_item_id, Com.findValueIndex(Com.c_items_upgrade_cost_chip, inventoryItem2.upgradeLevel), false) && Com.c_items_upgrade_chip_item_on_market) {
                  ServicesMenuList.suggestBuyItem(new Delegate(this, 7), new Delegate(new MenuSnapshotDelegate(this), 0), new ItemSimple(Com.c_items_upgrade_chip_item_id, Com.findValueIndex(Com.c_items_upgrade_cost_chip, inventoryItem2.upgradeLevel), Com.c_items_upgrade_chip_item_symb));
               } else {
                  showFloatMessageS("Не хватает\rвещей!");
               }

               res = false;
            }

            if (res) {
               this.executeDelegated(7);
               return;
            }
            break;
         case 238:
            if (res) {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
               delegateUpgradePetKeyInd = _curItem;
               delegateUpgradeItemInd = selectPetUpgradeKey;
               boolean cheapUpgradeAvailable = false;
               _curItem2 = 0;
               int upgradeChipId = 0;
               String cheapItemSymb = "";
               boolean chipSoldOnMarket = false;
               switch (selectPetUpgradeKey) {
                  case 0:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapSpeed;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)0);
                     upgradeChipId = Com.c_items_pet_speed_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_speed_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_speed_chip_item_on_market;
                     break;
                  case 1:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapPull;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)1);
                     upgradeChipId = Com.c_items_pet_pull_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_pull_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_pull_chip_item_on_market;
                     break;
                  case 2:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapAnswer;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)2);
                     upgradeChipId = Com.c_items_pet_answer_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_answer_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_answer_chip_item_on_market;
                     break;
                  case 3:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapFriends;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)3);
                     upgradeChipId = Com.c_items_pet_friends_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_friends_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_friends_chip_item_on_market;
                     break;
                  case 4:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapParalize;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)4);
                     upgradeChipId = Com.c_items_pet_paralize_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_paralize_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_paralize_chip_item_on_market;
                     break;
                  case 5:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapPoison;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)5);
                     upgradeChipId = Com.c_items_pet_poison_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_poison_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_poison_chip_item_on_market;
                     break;
                  case 6:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapHitDist;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)6);
                     upgradeChipId = Com.c_items_pet_hit_dist_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_hit_dist_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_hit_dist_chip_item_on_market;
                     break;
                  case 7:
                     cheapUpgradeAvailable = inventoryItem2.upgradeCheapHealth;
                     _curItem2 = inventoryItem2.getUpgradeByKey((byte)7);
                     upgradeChipId = Com.c_items_pet_health_chip_item_id;
                     cheapItemSymb = Com.c_items_pet_health_chip_item_symb;
                     chipSoldOnMarket = Com.c_items_pet_health_chip_item_on_market;
               }

               res = true;
               if (!cheapUpgradeAvailable) {
                  if (!Strategist.instance.isEnoughtItems((long)upgradeChipId, Com.findValueIndex(Com.c_items_pet_cost_chip, _curItem2), false) && chipSoldOnMarket) {
                     ServicesMenuList.suggestBuyItem(new Delegate(this, 8), new Delegate(new MenuSnapshotDelegate(this), 0), new ItemSimple((long)upgradeChipId, Com.findValueIndex(Com.c_items_pet_cost_chip, _curItem2), cheapItemSymb));
                  } else {
                     showFloatMessageS("Не хватает\rвещей!");
                  }

                  res = false;
               } else {
                  int[] c_items_pet_cost_money2 = Com.c_items_pet_cost_money;
                  backIndex = c_items_pet_cost_money2[Math.min(_curItem2, c_items_pet_cost_money2.length - 1)];
                  if (Strategist.instance.GP_Money < backIndex) {
                     ServicesMenuList.confirmGoldPurchase(new Delegate(this, 8), new Delegate(new MenuSnapshotDelegate(this), 0), (long)backIndex);
                     res = false;
                  }
               }

               if (res) {
                  this.executeDelegated(8);
               }

               return;
            }

            menuId = 97;
            MenuManager.instance.showMenuById(menuId);
            return;
      }

   }

   public final void executeDelegated(int context) {
      InventoryItem inventoryItem2;
      NetRequest netRequest;
      byte menuId;
      int integerCode;
      int response;
      switch (context) {
         case 1:
            this.showMyItemsMenu(false, false);
         case 3:
         default:
            return;
         case 2:
            long needMore = 0L;
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(4333000);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4333001) {
                  integerCode = netRequest.response.readInt();
                  needMore = netRequest.response.readLong();
                  IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  showFloatMessageS("Починено на " + integerCode + "ɼɽ");
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            if (needMore > 0L) {
               ServicesMenuList.confirmGoldPurchaseWithMessage(new Delegate(this, 2), new Delegate(this, 1), needMore, "Для полной починки нужно еще %needvalɼɽ.");
               return;
            }

            this.showMyItemsMenu(false, false);
            return;
         case 4:
            inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
            _sellCount = response = (int)Math.min((long)Strategist.instance.GP_Money * (long)inventoryItem2.maxDurability / (long)inventoryItem2.repairCost, (long)(inventoryItem2.maxDurability - inventoryItem2.durability));
            this.showConfirmationForMenu(120, "Починить за " + MathGame.ceilDivide((long)(response * inventoryItem2.repairCost), (long)inventoryItem2.maxDurability) + "ɼɽ? Прочность станет " + (inventoryItem2.durability + response) + "/" + inventoryItem2.maxDurability);
            return;
         case 5:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem);
               netRequest.writeHeader(3110400);
               netRequest.request.writeInt(_curItem);
               netRequest.request.writeLong(inventoryItem2.typeId);
               netRequest.request.writeInt(_sellCount);
               netRequest.request.writeInt(toMarketValue);
               Com.sendRequest(netRequest);
               if ((response = netRequest.response.readInt()) == 3110401) {
                  Com.applyRewardEvents(netRequest);
                  showFloatMessageS("Отправлено на рынок");
               } else if (response == 3110402) {
                  showFloatMessageS("Провал!");
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 6:
            if (!(inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(_curItem)).isStable) {
               showFloatMessageS("Вещь еще нестабильна\nВернись в город");
               return;
            } else {
               if (!inventoryItem2.forgeCan) {
                  if (inventoryItem2.forgeNeedCrys > Strategist.instance.GP_Crystals) {
                     ServicesMenuList.suggestBuyCrystals(new Delegate(new MenuSnapshotDelegate(this), 0), inventoryItem2.forgeNeedCrys);
                     return;
                  }

                  if (inventoryItem2.forgeNeedGold > (long)Strategist.instance.GP_Money) {
                     ServicesMenuList.confirmGoldPurchase(new Delegate(this, 6), new Delegate(new MenuSnapshotDelegate(this), 0), inventoryItem2.forgeNeedGold);
                     return;
                  }

                  showFloatMessageS("Не хватает\rресурсов!");
                  return;
               }

               this.showConfirmationForMenu(118, inventoryItem2.forgeInfo);
               return;
            }
         case 7:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(delegateUpgradePetItemInd);
               netRequest.writeHeader(4334000);
               netRequest.request.writeInt(delegateUpgradePetItemInd);
               netRequest.request.writeLong(inventoryItem2.typeId);
               Com.sendRequest(netRequest);
               integerCode = netRequest.response.readInt();
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               if (integerCode == 4334001) {
                  showFloatMessageS("Заточка успешна!");
               } else {
                  showFloatMessageS("Заточка не удалась");
               }
            } catch (Exception exception3) {
               exception3.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 8:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(delegateUpgradePetKeyInd);
               netRequest.writeHeader(4338000);
               netRequest.request.writeByte(delegateUpgradeItemInd);
               netRequest.request.writeInt(delegateUpgradePetKeyInd);
               netRequest.request.writeLong(inventoryItem2.typeId);
               Com.sendRequest(netRequest);
               integerCode = netRequest.response.readInt();
               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
               if (integerCode == 4338001) {
                  showFloatMessageS("Обучение успешно");
               } else {
                  showFloatMessageS("Обучение не удалось");
               }
            } catch (Exception exception4) {
               exception4.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
            return;
         case 9:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(delegateOpenItemInd);
               netRequest.writeHeader(4336000);
               netRequest.request.writeInt(delegateOpenItemInd);
               netRequest.request.writeLong(inventoryItem2.typeId);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 4336001) {
                  String nameRes = netRequest.response.readUTF();
                  showFloatMessageS("+" + nameRes);
               } else {
                  showFloatMessageS("Неудача");
               }

               IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
            } catch (Exception exception5) {
               exception5.printStackTrace();
            }

            Com.PrBarTh.Stop();
            menuId = 10;
            MenuManager.instance.showMenuById(menuId);
      }
   }

   private static int findEquipmentSlot(int type) {
      for(int iTEMS_TYPES_BY_SLOTIndex = 0; iTEMS_TYPES_BY_SLOTIndex < 16; ++iTEMS_TYPES_BY_SLOTIndex) {
         if (ITEMS_TYPES_BY_SLOT[iTEMS_TYPES_BY_SLOTIndex] == type) {
            return iTEMS_TYPES_BY_SLOTIndex;
         }
      }

      return -1;
   }

   private static Vector getSlotsByItemType(int type) {
      Vector vector3 = new Vector();

      for(int iTEMS_TYPES_BY_SLOTIndex = 0; iTEMS_TYPES_BY_SLOTIndex < 16; ++iTEMS_TYPES_BY_SLOTIndex) {
         if (ITEMS_TYPES_BY_SLOT[iTEMS_TYPES_BY_SLOTIndex] == type) {
            vector3.addElement(iTEMS_TYPES_BY_SLOTIndex);
         }
      }

      return vector3;
   }

   public final void showMyItemsMenu(boolean setFromGame, boolean fromGame) {
      if (setFromGame) {
         GameCommons.instance.isFromGameMenu = fromGame;
      }

      int bySlotCountIndex;
      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotCount.length; ++bySlotCountIndex) {
         bySlotCount[bySlotCountIndex] = 0;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotCount2.length; ++bySlotCountIndex) {
         bySlotCount2[bySlotCountIndex] = 0;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotInfos.length; ++bySlotCountIndex) {
         bySlotInfos[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotDrawMob.length; ++bySlotCountIndex) {
         bySlotDrawMob[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotSymbs.length; ++bySlotCountIndex) {
         bySlotSymbs[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotStrategGraphic.length; ++bySlotCountIndex) {
         bySlotStrategGraphic[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < Strategist.instance.items.size(); ++bySlotCountIndex) {
         InventoryItem inventoryItem2;
         int equipmentSlot = findEquipmentSlot((inventoryItem2 = (InventoryItem)Strategist.instance.items.elementAt(bySlotCountIndex)).useType);
         Vector vector3 = getSlotsByItemType(inventoryItem2.useType);
         if (equipmentSlot >= 0) {
            int sizeIndex;
            for(sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
               int elementAt = (Integer)vector3.elementAt(sizeIndex);
               int unusedCountIncrement = bySlotCount[elementAt]++;
               if (sizeIndex == 0) {
                  unusedCountIncrement = bySlotCount2[elementAt]++;
               } else {
                  bySlotCount2[elementAt] = -1;
               }
            }

            if (inventoryItem2.state == 1) {
               for(sizeIndex = 0; bySlotSymbs[equipmentSlot] != null && sizeIndex <= vector3.size() - 1; ++sizeIndex) {
                  equipmentSlot = (Integer)vector3.elementAt(sizeIndex);
               }

               if (equipmentSlot >= 0 && equipmentSlot <= 15) {
                  bySlotSymbs[equipmentSlot] = inventoryItem2.icon;
                  bySlotInfos[equipmentSlot] = inventoryItem2.description;
                  if (inventoryItem2.drawMob) {
                     bySlotDrawMob[equipmentSlot] = new int[]{inventoryItem2.drawMobHead, inventoryItem2.drawMobBody};
                  }

                  if (inventoryItem2.hasStrategGraphic()) {
                     bySlotStrategGraphic[equipmentSlot] = inventoryItem2.createStrategGraphic(Strategist.instance.GP_Race, Strategist.instance.GP_Gender);
                  }
               }
            }
         }
      }

      this.showInventoryMenu(5, bySlotSymbs, bySlotCount2, false, Strategist.instance.avatarGraphic);
   }

   public final void showOtherItemsMenu(int race, int gender, String[] icons, String[] names, String[] infos, InventoryItem[] inventoryItems, StrategGraphic strategGraphic2) {
      int bySlotCountIndex;
      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotCount.length; ++bySlotCountIndex) {
         bySlotCount[bySlotCountIndex] = 0;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotCount2.length; ++bySlotCountIndex) {
         bySlotCount2[bySlotCountIndex] = 0;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotInfos.length; ++bySlotCountIndex) {
         bySlotInfos[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotDrawMob.length; ++bySlotCountIndex) {
         bySlotDrawMob[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotSymbs.length; ++bySlotCountIndex) {
         bySlotSymbs[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < bySlotStrategGraphic.length; ++bySlotCountIndex) {
         bySlotStrategGraphic[bySlotCountIndex] = null;
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < names.length; ++bySlotCountIndex) {
         int equipmentSlot = findEquipmentSlot(bySlotCountIndex);
         bySlotInfos[equipmentSlot] = infos[bySlotCountIndex] != null && infos[bySlotCountIndex].length() > 0 ? infos[bySlotCountIndex] : null;
         bySlotSymbs[equipmentSlot] = icons[bySlotCountIndex] != null && icons[bySlotCountIndex].length() > 0 ? icons[bySlotCountIndex] : null;
         if (inventoryItems[bySlotCountIndex] != null && inventoryItems[bySlotCountIndex].hasStrategGraphic()) {
            bySlotStrategGraphic[equipmentSlot] = inventoryItems[bySlotCountIndex].createStrategGraphic(race, gender);
         }
      }

      for(bySlotCountIndex = 0; bySlotCountIndex < 16; ++bySlotCountIndex) {
         if (ITEMS_TYPES_BY_SLOT[bySlotCountIndex] >= names.length) {
            bySlotSymbs[bySlotCountIndex] = "?";
         }
      }

      this.showInventoryMenu(74, bySlotSymbs, (int[])null, true, strategGraphic2);
   }

   static {
      INFOS_BY_SLOT = StringResources.ITEMS_SLOTS;
      itemsForMutate = new Vector();
      forMutateItem = null;
      petsUpCans = new boolean[8];
      bySlotInfos = new String[16];
      bySlotSymbs = new String[16];
      bySlotCount = new int[16];
      bySlotCount2 = new int[16];
      bySlotDrawMob = new int[16][];
      bySlotStrategGraphic = new StrategGraphic[16];
      toMarketValue = 0;
   }
}
