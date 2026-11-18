package MenuPck.ListTypes;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Delegate;
import Engine.Mystery;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSnapshotDelegate;
import Moduls.Strategist;
import Moduls.InventoryItem;
import java.util.Vector;

public final class MarketMenuList extends MenuList {
   private static String search = "";
   public static long searchId = -1L;
   public static int searchNeedCount = 0;
   private String sells = "";
   private String backs = "";
   private String buys = "";
   private static MarketItemDescription[] marketItemDescriptionEntries;
   private MarketItemDescription marketItemDescription;
   private int prevMarketMenuId;

   public MarketMenuList(int id, int backMenu, String list) {
      super(id, backMenu, list);
      switch (id) {
         case 100:
            this.hints = Mystery.vectorFromString("Посмотри на новые товары|Список того что ты выставил на продажу. Тут ты можешь забрать вещь|Твои покупки, продажи, и возвраты|Введи часть названия товара (например ВЕКС, ПРЕД, или СУН)");
         default:
      }
   }

   protected final void goBack() {
      if (this.curMenuId == 104 && searchId >= 0L) {
         ServicesMenuList.suggestBuyItemResult(searchNeedCount <= 0);
      } else {
         super.goBack();
      }
   }

   protected final int onGenerateBackIdEvent(int menuId) {
      return menuId == 100 && GameCommons.instance.isFromGameMenu ? -2 : super.onGenerateBackIdEvent(menuId);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      super.onGenerateHintEvent(menuId, itemIndex);
      switch (menuId) {
         case 101:
         case 102:
         case 104:
            if (marketItemDescriptionEntries.length == 0) {
               setAnimatedTitle("Ничего не найдено");
               return;
            }

            MarketItemDescription marketItemDescription2;
            setAnimatedTitle((marketItemDescription2 = marketItemDescriptionEntries[itemIndex]).string2);
            if (marketItemDescription2.inventoryItem.drawMob) {
               showUnitPortrait(marketItemDescription2.inventoryItem.drawMobHead, marketItemDescription2.inventoryItem.drawMobBody);
            }

            if (marketItemDescription2.inventoryItem.hasStrategGraphic()) {
               showAvatarPortrait(marketItemDescription2.inventoryItem.createStrategGraphic(Strategist.instance.GP_Race, Strategist.instance.GP_Gender));
            }
            break;
         case 103:
            switch (itemIndex) {
               case 0:
                  setAnimatedTitle(this.sells);
                  break;
               case 1:
                  setAnimatedTitle(this.backs);
                  break;
               case 2:
                  setAnimatedTitle(this.buys);
               default:
                  return;
            }
      }

   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
      NetRequest netRequest;
      switch (menuId) {
         case 101:
         case 102:
         case 104:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               int marketItemDescriptionEntrieIndex = 0;
               if (menuId == 102) {
                  netRequest.writeHeader(3110100);
                  marketItemDescriptionEntrieIndex = 3110101;
               }

               if (menuId == 104) {
                  netRequest.writeHeader(3110300);
                  if (searchId >= 0L) {
                     netRequest.request.writeBoolean(true);
                     netRequest.request.writeLong(searchId);
                  } else {
                     netRequest.request.writeBoolean(false);
                     netRequest.request.writeUTF(search);
                  }

                  marketItemDescriptionEntrieIndex = 3110301;
               }

               if (menuId == 101) {
                  netRequest.writeHeader(3110000);
                  marketItemDescriptionEntrieIndex = 3110001;
               }

               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == marketItemDescriptionEntrieIndex) {
                  marketItemDescriptionEntries = new MarketItemDescription[netRequest.response.readInt()];

                  for(marketItemDescriptionEntrieIndex = 0; marketItemDescriptionEntrieIndex < marketItemDescriptionEntries.length; ++marketItemDescriptionEntrieIndex) {
                     InventoryItem inventoryItem2 = new InventoryItem();
                     String messageText = netRequest.response.readUTF();
                     String messageText2 = netRequest.response.readUTF();
                     inventoryItem2.readAdditional(netRequest.response);
                     inventoryItem2.unweight = netRequest.response.readBoolean();
                     marketItemDescriptionEntries[marketItemDescriptionEntrieIndex] = new MarketItemDescription(messageText, messageText2, netRequest.response.readInt(), netRequest.response.readInt(), netRequest.response.readInt(), inventoryItem2);
                     vector3.addElement(marketItemDescriptionEntries[marketItemDescriptionEntrieIndex].string);
                  }

                  if (marketItemDescriptionEntries.length == 0) {
                     vector3.addElement("Ничего не найдено");
                     if (menuId == 104 && searchId >= 0L) {
                        showFloatMessageS("Ничего не найдено");
                        ServicesMenuList.suggestBuyItemResult(searchNeedCount <= 0);
                     } else {
                        this.showMenuMessage(205, "Ничего не найдено");
                     }

                     vector3 = LIST_IGNORE;
                  }
               } else {
                  if (menuId == 104 && searchId >= 0L) {
                     ServicesMenuList.suggestBuyItemResult(searchNeedCount <= 0);
                  } else {
                     byte menuId2 = 100;
                     MenuManager.instance.showMenuById(menuId2);
                  }

                  vector3 = LIST_IGNORE;
               }
            } catch (Exception exception) {
               exception.printStackTrace();
            }

            Com.PrBarTh.Stop();
            break;
         case 103:
            Com.PrBarTh.Set(true);
            netRequest = new NetRequest();

            try {
               netRequest.writeHeader(3110200);
               Com.sendRequest(netRequest);
               if (netRequest.response.readInt() == 3110201) {
                  this.sells = netRequest.response.readUTF();
                  this.backs = netRequest.response.readUTF();
                  this.buys = netRequest.response.readUTF();
               }
            } catch (Exception exception2) {
               exception2.printStackTrace();
            }

            Com.PrBarTh.Stop();
      }

      return vector3;
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 100:
            byte menuId2;
            switch (itemIndex) {
               case 0:
                  menuId2 = 101;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 1:
                  menuId2 = 102;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 2:
                  menuId2 = 103;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               case 3:
                  this.showTextEnter(141, "Введи часть названия товара (например ВЕКС, ПРЕД, или СУН)", search);
               default:
                  return;
            }
         case 101:
         case 102:
         case 104:
            if (marketItemDescriptionEntries.length != 0) {
               if (menuId == 102) {
                  this.marketItemDescription = marketItemDescriptionEntries[itemIndex];
                  this.showConfirmationForMenu(143, Mystery.stringFormat("Вернуть $?", '$', new String[]{this.marketItemDescription.string}));
               }

               if (menuId == 104 || menuId == 101) {
                  this.marketItemDescription = marketItemDescriptionEntries[itemIndex];
                  this.prevMarketMenuId = menuId;
                  if (this.marketItemDescription.crystalPrice > Strategist.instance.GP_Money) {
                     ServicesMenuList.confirmGoldPurchaseWithMessage(new Delegate(this, 1), new Delegate(new MenuSnapshotDelegate(this), 0), (long)this.marketItemDescription.crystalPrice, (String)null);
                     return;
                  }

                  this.executeDelegated(1);
               }
            }
         case 103:
      }

   }

   public final void executeDelegated(int context) {
      switch (context) {
         case 1:
            this.showConfirmationForMenu(142, Mystery.stringFormat("Купить $ за $ɼɽ?", '$', new String[]{this.marketItemDescription.string, String.valueOf(this.marketItemDescription.crystalPrice)}));
         default:
      }
   }

   protected final void onTextEnterEvent(int backIndex, boolean res, String search2) {
      switch (backIndex) {
         case 141:
            byte menuId;
            if (res) {
               search = search2;
               searchId = -1L;
               setLastSelectedListElForced(104, -1);
               menuId = 104;
               MenuManager.instance.showMenuById(menuId);
               return;
            } else {
               menuId = 100;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
      }
   }

   protected final void onOKMenuEvent(int backIndex) {
      switch (backIndex) {
         case 205:
            byte menuId = 100;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      NetRequest netRequest;
      switch (backIndex) {
         case 142:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(3110500);
                  netRequest.request.writeInt(this.marketItemDescription.quantity);
                  netRequest.request.writeInt(this.marketItemDescription.goldPrice);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 3110501) {
                     Com.applyRewardEvents(netRequest);
                     if (searchId >= 0L) {
                        searchNeedCount -= this.marketItemDescription.inventoryItem.count;
                     }

                     showFloatMessageS("Успех!");
                  } else {
                     showFloatMessageS("Провал!");
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               backIndex = this.prevMarketMenuId;
               MenuManager.instance.showMenuById(backIndex);
               return;
            }

            backIndex = this.prevMarketMenuId;
            MenuManager.instance.showMenuById(backIndex);
            return;
         case 143:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(3110600);
                  netRequest.request.writeInt(this.marketItemDescription.quantity);
                  netRequest.request.writeInt(this.marketItemDescription.goldPrice);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 3110601) {
                     Com.applyRewardEvents(netRequest);
                     showFloatMessageS("Успех!");
                  } else {
                     showFloatMessageS("Провал!");
                  }
               } catch (Exception exception2) {
                  exception2.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            byte menuId = 102;
            MenuManager.instance.showMenuById(menuId);
         default:
      }
   }
}
