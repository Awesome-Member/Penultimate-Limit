package Moduls.strategevents;

import Base.GameCommons;
import Engine.Mystery;
import MenuPck.ListTypes.TradeMenuList;
import Moduls.Strategist;
import Moduls.InventoryItem;
import Moduls.trading.PlayersTrade;
import Moduls.trading.TradeSuggestion;
import java.io.DataInputStream;

public final class StrategSendDifInTradesEvent extends StrategSendEvent {
   private PlayersTrade trade;
   private int tradeId;
   private byte action;

   protected StrategSendDifInTradesEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.action = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      switch (this.action) {
         case 0:
            this.trade = new PlayersTrade(dataInputStream);
            return;
         case 1:
            this.tradeId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         default:
      }
   }

   public final void applyToStrategist() {
      Strategist strategist2;
      switch (this.action) {
         case 0:
            this.trade.haveNew = true;
            PlayersTrade trade2 = this.trade;
            strategist2 = Strategist.instance;
            PlayersTrade playersTrade = null;

            for(int sizeIndex = 0; sizeIndex < strategist2.trades.size(); ++sizeIndex) {
               PlayersTrade playersTrade2;
               if ((playersTrade2 = (PlayersTrade)strategist2.trades.elementAt(sizeIndex)).id == trade2.id) {
                  strategist2.trades.removeElementAt(sizeIndex);
                  playersTrade = playersTrade2;
                  break;
               }
            }

            strategist2.trades.insertElementAt(trade2, 0);
            TradeSuggestion tradeSuggestion3;
            if (playersTrade != null) {
               tradeSuggestion3 = this.trade.findInvertSuggestion(Strategist.instance.id);
               boolean newTrade = false;
               TradeSuggestion tradeSuggestion4 = playersTrade.findInvertSuggestion(Strategist.instance.id);
               TradeSuggestion tradeSuggestion5 = tradeSuggestion3;
               boolean notifyTradeChange;
               if (tradeSuggestion3.gold != tradeSuggestion4.gold) {
                  notifyTradeChange = true;
               } else if (tradeSuggestion3.items.length != tradeSuggestion4.items.length) {
                  notifyTradeChange = true;
               } else {
                  int itemIndex = 0;

                  while(true) {
                     if (!(itemIndex < tradeSuggestion5.items.length)) {
                        notifyTradeChange = false;
                        break;
                     }

                     InventoryItem inventoryItem2 = tradeSuggestion5.items[itemIndex];
                     InventoryItem inventoryItem3 = tradeSuggestion4.items[itemIndex];
                     if (inventoryItem2.id != inventoryItem3.id || inventoryItem2.count != inventoryItem3.count) {
                        notifyTradeChange = true;
                        break;
                     }

                     ++itemIndex;
                  }
               }

               if (notifyTradeChange) {
                  tradeSuggestion3.markNew();
                  newTrade = true;
               }

               if (newTrade && playersTrade.findSuggestion(Strategist.instance.id).isAccept && !this.trade.findSuggestion(Strategist.instance.id).isAccept) {
                  GameCommons.instance.showFloatText(Mystery.stringFormat("Условия обмена\nс % изменились", tradeSuggestion3.playerName));
               }

               TradeMenuList.instance.onTradeChanged(this.trade.id);
               return;
            }

            if (this.trade.tradeSuggestion2.playerId == Strategist.instance.id) {
               if ((tradeSuggestion3 = this.trade.findInvertSuggestion(Strategist.instance.id)).gold != 0L || tradeSuggestion3.items.length != 0) {
                  tradeSuggestion3.markNew();
               }

               GameCommons.instance.showFloatText(Mystery.stringFormat("Новая сделка\nс %\n(МЕНЮ-Мир-Сделки)", tradeSuggestion3 == null ? "ERROR" : tradeSuggestion3.playerName));
               return;
            }
            break;
         case 1:
            int tradeId2 = this.tradeId;
            strategist2 = Strategist.instance;
            int tradeIndex = 0;

            while(true) {
               if (tradeIndex >= strategist2.trades.size()) {
                  Object unusedTradeContext = null;
                  break;
               }

               PlayersTrade playersTrade3;
               if ((playersTrade3 = (PlayersTrade)strategist2.trades.elementAt(tradeIndex)).id == tradeId2) {
                  strategist2.trades.removeElementAt(tradeIndex);
                  break;
               }

               ++tradeIndex;
            }

            TradeMenuList.instance.returnToTradeMenu(this.tradeId);
      }

   }
}
