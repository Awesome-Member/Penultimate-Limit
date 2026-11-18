package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.MathGame;
import Engine.Mystery;
import Engine.Pair;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import Moduls.Strategist;
import Moduls.InventoryItem;
import Moduls.trading.PlayersTrade;
import Moduls.trading.TradeSuggestion;
import java.util.Vector;

public final class TradeMenuList extends MenuList {
   public static final TradeMenuList instance = new TradeMenuList();
   private static final Object TAG_TRADE_MY_HEADER = new Object();
   private static final Object TAG_TRADE_MY_GOLD = new Object();
   private static final Object TAG_TRADE_MY_ADD_ITEM = new Object();
   private static final Object TAG_TRADE_MY_ITEM = new Object();
   private static final Object TAG_TRADE_AGREE = new Object();
   private static final Object TAG_TRADE_DECLINE = new Object();
   private static final Object TAG_TRADE_HIS_HEADER = new Object();
   private static final Object TAG_TRADE_HIS_GOLD = new Object();
   private static final Object TAG_TRADE_HIS_ITEM = new Object();
   private int tradeMenuWorkTradeId = -1;
   private int tradeMenuCreateWithPlayerId = -1;
   private int tradeMenuBackMenu = -1;
   private int tradeMenuCurItemId = -1;
   private Vector tradesListMenuCach = new Vector();
   private Vector tradeMenuTags = new Vector();
   private Vector tradeMenuItemsCach = new Vector();
   private int curVisibleProtectionTimeDelta = 0;

   private TradeMenuList() {
      super(new int[]{239, 243, 245}, -2, "");
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 239:
            PlayersTrade playersTrade;
            if ((playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) == null) {
               return;
            }

            TradeSuggestion tradeSuggestion3 = playersTrade.findSuggestion(Strategist.instance.id);
            TradeSuggestion tradeSuggestion4 = playersTrade.findInvertSuggestion(Strategist.instance.id);
            Object el;
            if ((el = this.tradeMenuTags.elementAt(itemIndex)) == TAG_TRADE_MY_HEADER) {
               setAnimatedTitle(Mystery.stringFormat("Твои предложения\nдля %", tradeSuggestion4.playerName));
               return;
            }

            if (el == TAG_TRADE_MY_GOLD) {
               this.showHintForMyGoldTrade(tradeSuggestion3.gold, playersTrade);
               return;
            }

            if (el == TAG_TRADE_MY_ADD_ITEM) {
               setAnimatedTitle("Нажми, чтобы добавить вещи к обмену");
               return;
            }

            if (el == TAG_TRADE_HIS_HEADER) {
               setAnimatedTitle(Mystery.stringFormat("%\nдаст взамен", tradeSuggestion4.playerName));
               return;
            }

            if (el == TAG_TRADE_HIS_GOLD) {
               setAnimatedTitle(Mystery.stringFormat("Ты получишь $ɼɽ", '$', new String[]{String.valueOf(tradeSuggestion4.gold)}));
               return;
            }

            if (el == TAG_TRADE_AGREE) {
               setAnimatedTitle(tradeSuggestion3.isAccept ? "Ты согласился на обмен на этих условиях.\nНажми, чтобы убрать согласие." : "Нажми, чтобы подтвердить обмен на этих условиях");
               return;
            }

            if (el == TAG_TRADE_DECLINE) {
               setAnimatedTitle("Нажми, чтобы полностью закрыть сделку");
               return;
            }

            if (el instanceof Pair) {
               Pair pair;
               ((InventoryItem)(pair = (Pair)el).second).showInMenu((MenuList)this);
               return;
            }
            break;
         case 243:
            ((InventoryItem)this.tradeMenuItemsCach.elementAt(itemIndex)).showInMenu((MenuList)this);
            return;
         case 245:
            setAnimatedTitle(Mystery.stringFormat("Нажми для редактирования сделки с %", ((PlayersTrade)this.tradesListMenuCach.elementAt(itemIndex)).findInvertSuggestion(Strategist.instance.id).playerName));
            return;
         default:
            super.onGenerateHintEvent(menuId, itemIndex);
      }

   }

   protected final int onGenerateBackIdEvent(int menuId) {
      switch (menuId) {
         case 239:
            return this.tradeMenuBackMenu;
         case 243:
            return 239;
         case 245:
         case 250:
            if (GameCommons.instance.isFromGameMenu) {
               return -2;
            }

            return 36;
         default:
            return super.onGenerateBackIdEvent(menuId);
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      PlayersTrade playersTrade;
      int sizeIndex;
      Vector vector3;
      switch (menuId) {
         case 239:
            if (this.tradeMenuWorkTradeId < 0) {
               PlayersTrade playersTrade2;
               if ((playersTrade2 = Strategist.instance.tradeFindWithPlayerId(this.tradeMenuCreateWithPlayerId)) == null) {
                  Com.PrBarTh.Set(true);
                  Com.PrBarTh.Set(true);
                  NetRequest netRequest = new NetRequest();

                  try {
                     netRequest.writeHeader(4360010);
                     netRequest.request.writeInt(this.tradeMenuCreateWithPlayerId);
                     Com.sendRequest(netRequest);
                     if ((sizeIndex = netRequest.response.readInt()) == 4360011) {
                        this.tradeMenuWorkTradeId = netRequest.response.readInt();
                        IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                     } else if (sizeIndex == 4360017) {
                        showFloatMessageS("Автоотказ от торговли!");
                     } else {
                        showFloatMessageS("Сделка невозможна");
                     }
                  } catch (Exception exception) {
                     exception.printStackTrace();
                  }

                  Com.PrBarTh.Stop();
               } else {
                  this.tradeMenuWorkTradeId = playersTrade2.id;
               }
            }

            sizeIndex = this.getLastSelectedListEl();
            Object wasTag = null;
            if (sizeIndex >= 0 && sizeIndex < this.tradeMenuTags.size()) {
               wasTag = this.tradeMenuTags.elementAt(sizeIndex);
            }

            vector3 = new Vector();
            this.tradeMenuTags.removeAllElements();
            if ((playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) != null) {
               sizeIndex = sizeIndex;
               GameCommons.instance.startTimer((int)MathGame.ceilDivide(Com.c_trading_expires_time - (System.currentTimeMillis() - playersTrade.loadTime + playersTrade.expireTimeOnLoad), 1000L), -1);
               TradeSuggestion tradeSuggestion3 = playersTrade.findSuggestion(Strategist.instance.id);
               TradeSuggestion tradeSuggestion4;
               (tradeSuggestion4 = playersTrade.findInvertSuggestion(Strategist.instance.id)).haveNew = false;
               vector3.addElement("Ты отдашь:");
               this.tradeMenuTags.addElement(TAG_TRADE_MY_HEADER);
               if (wasTag == TAG_TRADE_MY_HEADER) {
                  sizeIndex = vector3.size() - 1;
               }

               vector3.addElement(Mystery.stringFormat("Золото %ɼɽ", new String[]{String.valueOf(tradeSuggestion3.gold)}));
               this.tradeMenuTags.addElement(TAG_TRADE_MY_GOLD);
               if (wasTag == TAG_TRADE_MY_GOLD) {
                  sizeIndex = vector3.size() - 1;
               }

               InventoryItem inventoryItem2;
               int curVisibleProtectionTimeDelta2;
               for(curVisibleProtectionTimeDelta2 = 0; curVisibleProtectionTimeDelta2 < tradeSuggestion3.items.length; ++curVisibleProtectionTimeDelta2) {
                  inventoryItem2 = tradeSuggestion3.items[curVisibleProtectionTimeDelta2];
                  vector3.addElement(inventoryItem2.getNameForMenu());
                  this.tradeMenuTags.addElement(new Pair(TAG_TRADE_MY_ITEM, inventoryItem2));
                  if (wasTag instanceof Pair && ((Pair)wasTag).first == TAG_TRADE_MY_ITEM && ((InventoryItem)((Pair)wasTag).second).id == inventoryItem2.id) {
                     sizeIndex = vector3.size() - 1;
                  }
               }

               vector3.addElement("Добавить вещь");
               this.tradeMenuTags.addElement(TAG_TRADE_MY_ADD_ITEM);
               if (wasTag == TAG_TRADE_MY_ADD_ITEM) {
                  sizeIndex = vector3.size() - 1;
               }

               vector3.addElement("Ты получишь:");
               this.tradeMenuTags.addElement(TAG_TRADE_HIS_HEADER);
               if (wasTag == TAG_TRADE_HIS_HEADER) {
                  sizeIndex = vector3.size() - 1;
               }

               vector3.addElement(Mystery.stringFormat("Золото %ɼɽ", new String[]{String.valueOf(tradeSuggestion4.gold)}));
               this.tradeMenuTags.addElement(TAG_TRADE_HIS_GOLD);
               if (wasTag == TAG_TRADE_HIS_GOLD) {
                  sizeIndex = vector3.size() - 1;
               }

               for(curVisibleProtectionTimeDelta2 = 0; curVisibleProtectionTimeDelta2 < tradeSuggestion4.items.length; ++curVisibleProtectionTimeDelta2) {
                  inventoryItem2 = tradeSuggestion4.items[curVisibleProtectionTimeDelta2];
                  vector3.addElement(inventoryItem2.getNameForMenu());
                  this.tradeMenuTags.addElement(new Pair(TAG_TRADE_HIS_ITEM, inventoryItem2));
                  if (wasTag instanceof Pair && ((Pair)wasTag).first == TAG_TRADE_HIS_ITEM && ((InventoryItem)((Pair)wasTag).second).id == inventoryItem2.id) {
                     sizeIndex = vector3.size() - 1;
                  }
               }

               curVisibleProtectionTimeDelta2 = (int)MathGame.ceilDivide(Math.max(0L, 3000L - (System.currentTimeMillis() - tradeSuggestion4.lastChangeTime)), 1000L);
               this.curVisibleProtectionTimeDelta = curVisibleProtectionTimeDelta2;
               vector3.addElement(tradeSuggestion3.isAccept ? "Согласился" : (curVisibleProtectionTimeDelta2 > 0 ? "(" + curVisibleProtectionTimeDelta2 + ")" : "") + "Согласен?");
               this.tradeMenuTags.addElement(TAG_TRADE_AGREE);
               if (wasTag == TAG_TRADE_AGREE) {
                  sizeIndex = vector3.size() - 1;
               }

               vector3.addElement("Отказаться");
               this.tradeMenuTags.addElement(TAG_TRADE_DECLINE);
               if (wasTag == TAG_TRADE_DECLINE) {
                  sizeIndex = vector3.size() - 1;
               }

               this.setLastSelectedListElForced(sizeIndex);
            } else {
               vector3 = LIST_IGNORE;
               this.tradeMenuBack();
            }

            return vector3;
         case 243:
            this.tradeMenuItemsCach.removeAllElements();
            vector3 = new Vector();
            playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId);

            for(sizeIndex = 0; sizeIndex < Strategist.instance.items.size(); ++sizeIndex) {
               InventoryItem inventoryItem3;
               InventoryItem inventoryItem4 = inventoryItem3 = (InventoryItem)Strategist.instance.items.elementAt(sizeIndex);
               boolean hasPlayersTrade = playersTrade != null && playersTrade.inClan;
               InventoryItem inventoryItem5 = inventoryItem4;
               if (!inventoryItem4.unsoldeable && inventoryItem5.resell && inventoryItem5.isStable && (!inventoryItem5.justInClan || hasPlayersTrade) && inventoryItem3.state == 0) {
                  this.tradeMenuItemsCach.addElement(inventoryItem3);
                  vector3.addElement(inventoryItem3.getNameForMenu());
               }
            }

            return vector3;
         case 245:
            this.tradesListMenuCach.removeAllElements();
            vector3 = new Vector();

            for(sizeIndex = 0; sizeIndex < Strategist.instance.trades.size(); ++sizeIndex) {
               playersTrade = (PlayersTrade)Strategist.instance.trades.elementAt(sizeIndex);
               this.tradesListMenuCach.addElement(playersTrade);
               TradeSuggestion tradeSuggestion5 = playersTrade.findInvertSuggestion(Strategist.instance.id);
               vector3.addElement((tradeSuggestion5.haveNew ? "ͪͫ" : "") + tradeSuggestion5.playerName);
            }

            return vector3;
         default:
            return super.onGenerateItemsAndSkipsEvent(menuId);
      }
   }

   private void showHintForMyGoldTrade(long goldAmount, PlayersTrade playersTrade) {
      setAnimatedTitle(Mystery.stringFormat("Ты отдашь $ɼɽ и налог $ɼɽ ($%)", '$', new String[]{String.valueOf(goldAmount), String.valueOf(MathGame.ceilDivide(goldAmount * (long)playersTrade.getTax(), 100L)), String.valueOf(playersTrade.getTax())}));
   }

   public final void onNumberChangedEvent(int backIndex, int inputNumber) {
      switch (backIndex) {
         case 240:
            PlayersTrade playersTrade;
            if ((playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) == null) {
               return;
            } else {
               this.showHintForMyGoldTrade((long)inputNumber, playersTrade);
            }
         default:
      }
   }

   public final void onNumberEnterEvent(int backIndex, int inputNumber) {
      switch (backIndex) {
         case 240:
            PlayersTrade playersTrade;
            TradeSuggestion tradeSuggestion3 = (playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) == null ? null : playersTrade.findSuggestion(Strategist.instance.id);
            TradeSuggestion tradeSuggestion4 = tradeSuggestion3;
            if (tradeSuggestion3 == null || tradeSuggestion4.gold != (long)inputNumber) {
               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4360020);
                  netRequest.request.writeInt(this.tradeMenuWorkTradeId);
                  netRequest.request.writeLong((long)inputNumber);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 4360021) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
            }

            short menuId = 239;
            MenuManager.instance.showMenuById(menuId);
         default:
            return;
         case 244:
            this.tradePostItemToTrade(inputNumber);
      }
   }

   protected final void onNumberCancelEvent(int backIndex) {
      short menuId;
      switch (backIndex) {
         case 240:
         case 243:
            menuId = 239;
            MenuManager.instance.showMenuById(menuId);
         case 241:
         case 242:
         default:
            return;
         case 244:
            menuId = 243;
            MenuManager.instance.showMenuById(menuId);
      }
   }

   protected final void onFunctionate(int menuId) {
      switch (menuId) {
         case 239:
            PlayersTrade playersTrade;
            if ((playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) == null) {
               return;
            } else {
               TradeSuggestion tradeSuggestion3 = playersTrade.findInvertSuggestion(Strategist.instance.id);
               if ((int)MathGame.ceilDivide(Math.max(0L, 3000L - (System.currentTimeMillis() - tradeSuggestion3.lastChangeTime)), 1000L) != this.curVisibleProtectionTimeDelta) {
                  this.showListMenuInner(239, true);
               }
            }
         default:
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 239:
            PlayersTrade playersTrade;
            if ((playersTrade = Strategist.instance.tradeFind(this.tradeMenuWorkTradeId)) == null) {
               return;
            } else {
               TradeSuggestion tradeSuggestion3 = playersTrade.findSuggestion(Strategist.instance.id);
               TradeSuggestion tradeSuggestion4 = playersTrade.findInvertSuggestion(Strategist.instance.id);
               Object el;
               int maximumGoldAfterTax;
               if ((el = this.tradeMenuTags.elementAt(itemIndex)) == TAG_TRADE_MY_GOLD) {
                  maximumGoldAfterTax = (int)((long)Strategist.instance.GP_Money * 100L / (long)(100 + playersTrade.getTax()));
                  this.showNumberEnter(240, (int)tradeSuggestion3.gold, 0, maximumGoldAfterTax, "");
                  return;
               } else if (el == TAG_TRADE_MY_ADD_ITEM) {
                  if (Com.c_trading_items_max_count > 0 && tradeSuggestion3.items.length >= Com.c_trading_items_max_count) {
                     showFloatMessageS(Mystery.stringFormat("Не больше %\nвещей в сделке", String.valueOf(Com.c_trading_items_max_count)));
                     return;
                  }

                  short menuId2 = 243;
                  MenuManager.instance.showMenuById(menuId2);
                  return;
               } else if (el == TAG_TRADE_AGREE) {
                  if ((maximumGoldAfterTax = (int)MathGame.ceilDivide(Math.max(0L, 3000L - (System.currentTimeMillis() - tradeSuggestion4.lastChangeTime)), 1000L)) <= 0) {
                     Com.PrBarTh.Set(true);
                     NetRequest netRequest = new NetRequest();

                     try {
                        netRequest.writeHeader(4360040);
                        decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, this.tradeMenuWorkTradeId);
                        decompiledsupport.UncheckedDataInput.writeBoolean(netRequest.request, !tradeSuggestion3.isAccept);
                        Com.sendRequest(netRequest);
                        int integerCode;
                        if ((integerCode = decompiledsupport.UncheckedDataInput.readInt(netRequest.response)) == 4360041) {
                           IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                           if (decompiledsupport.UncheckedDataInput.readBoolean(netRequest.response)) {
                              this.showOKMenuWithBackMenu(this.tradeMenuBackMenu, decompiledsupport.UncheckedDataInput.readUTF(netRequest.response));
                           }
                        } else if (integerCode == 4360043) {
                           IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                        } else if (integerCode == 4360045) {
                           long deltaTime = decompiledsupport.UncheckedDataInput.readLong(netRequest.response);
                           showFloatMessageS(Mystery.stringFormat("Защитный интервал\nеще % сек", '%', new String[]{String.valueOf((int)MathGame.ceilDivide(Math.max(0L, 3000L - deltaTime), 1000L))}));
                        }
                     } finally {
                        Com.PrBarTh.Stop();
                     }

                     this.onGenerateHintEvent(menuId, itemIndex);
                     return;
                  } else {
                     showFloatMessageS(Mystery.stringFormat("Защитный интервал\nеще % сек", '%', new String[]{String.valueOf(maximumGoldAfterTax)}));
                  }
               } else if (el == TAG_TRADE_DECLINE) {
                  this.showConfirmationForMenu(241, "Уверен, что хочешь полностью отклонить сделку?");
                  return;
               } else {
                  Pair pair;
                  if (el instanceof Pair && (pair = (Pair)el).first == TAG_TRADE_MY_ITEM) {
                     InventoryItem inventoryItem2 = (InventoryItem)pair.second;
                     this.tradeMenuCurItemId = inventoryItem2.id;
                     this.showConfirmationForMenu(242, Mystery.stringFormat("Убрать %?", inventoryItem2.getNameForMenu()));
                  }
               }
            }
         default:
            return;
         case 243:
            InventoryItem inventoryItem3 = (InventoryItem)this.tradeMenuItemsCach.elementAt(itemIndex);
            this.tradeMenuCurItemId = inventoryItem3.id;
            if (inventoryItem3.count > 1) {
               this.showNumberEnter(244, 0, 1, inventoryItem3.count, "Сколько штук продать?");
               return;
            }

            this.tradePostItemToTrade(1);
            return;
         case 245:
            this.tradeMenuStartNew(((PlayersTrade)this.tradesListMenuCach.elementAt(itemIndex)).id, -1, 245);
      }
   }

   private void tradePostItemToTrade(int count) {
      Com.PrBarTh.Set(true);
      NetRequest netRequest = new NetRequest();

      try {
         netRequest.writeHeader(4360030);
         netRequest.request.writeInt(this.tradeMenuWorkTradeId);
         netRequest.request.writeInt(this.tradeMenuCurItemId);
         netRequest.request.writeInt(count);
         Com.sendRequest(netRequest);
         if (netRequest.response.readInt() == 4360031) {
            IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      } finally {
         Com.PrBarTh.Stop();
      }

      short menuId = 239;
      MenuManager.instance.showMenuById(menuId);
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      NetRequest netRequest;
      short menuId;
      switch (backIndex) {
         case 241:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4360050);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, this.tradeMenuWorkTradeId);
                  Com.sendRequest(netRequest);
                  if (decompiledsupport.UncheckedDataInput.readInt(netRequest.response) == 4360051) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  }
               } finally {
                  Com.PrBarTh.Stop();
               }

               this.tradeMenuBack();
               return;
            } else {
               menuId = 239;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
            return;
         case 242:
            if (res) {
               Com.PrBarTh.Set(true);
               netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(4360030);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, this.tradeMenuWorkTradeId);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, this.tradeMenuCurItemId);
                  decompiledsupport.UncheckedDataInput.writeInt(netRequest.request, 0);
                  Com.sendRequest(netRequest);
                  if (decompiledsupport.UncheckedDataInput.readInt(netRequest.response) == 4360031) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  }
               } finally {
                  Com.PrBarTh.Stop();
               }
            }

            menuId = 239;
            MenuManager.instance.showMenuById(menuId);
      }
   }

   protected final void onCloseMenuEvent(int backIndex) {
      super.onCloseMenuEvent(backIndex);
      switch (backIndex) {
         case 239:
            this.tradeMenuTags.removeAllElements();
            GameCommons.instance.stopTimer();
            return;
         case 243:
            this.tradeMenuItemsCach.removeAllElements();
            return;
         case 245:
            this.tradesListMenuCach.removeAllElements();
         default:
      }
   }

   public final void tradeMenuStartNew(int tradeId, int withPlayerId, int backToMenu) {
      this.tradeMenuBackMenu = backToMenu;
      this.tradeMenuWorkTradeId = tradeId;
      this.tradeMenuCreateWithPlayerId = withPlayerId;
      MenuManager.instance.showMenuById(239);
   }

   public final void onTradeChanged(int id) {
      if (MenuManager.instance.guiHandler.isActive()) {
         switch (MenuManager.instance.getCurMenuList().getCurMenuId()) {
            case 239:
               if (this.tradeMenuWorkTradeId == id) {
                  this.showListMenuInner(239, true);
               }
         }
      }

   }

   public final void returnToTradeMenu(int tradeId) {
      if (MenuManager.instance.guiHandler.isActive()) {
         switch (MenuManager.instance.getCurMenuList().getCurMenuId()) {
            case 239:
               if (this.tradeMenuWorkTradeId == tradeId) {
                  this.tradeMenuBack();
               }
         }
      }

   }

   private void tradeMenuBack() {
      if (this.tradeMenuBackMenu >= 0) {
         int tradeMenuBackMenu2 = this.tradeMenuBackMenu;
         MenuManager.instance.showMenuById(tradeMenuBackMenu2);
      } else {
         MenuManager.instance.guiHandler.hideAllWindows();
      }
   }
}
