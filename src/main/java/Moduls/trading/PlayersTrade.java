package Moduls.trading;

import Base.Com;
import java.io.DataInputStream;

public final class PlayersTrade {
   public int id;
   public TradeSuggestion tradeSuggestion;
   public TradeSuggestion tradeSuggestion2;
   public boolean inClan = false;
   public long loadTime;
   public long expireTimeOnLoad;
   public boolean haveNew = false;

   public PlayersTrade(DataInputStream dataInputStream) {
      this.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.tradeSuggestion = new TradeSuggestion(dataInputStream);
      this.tradeSuggestion2 = new TradeSuggestion(dataInputStream);
      this.inClan = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.expireTimeOnLoad = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.loadTime = System.currentTimeMillis();
   }

   public final TradeSuggestion findSuggestion(int playerId) {
      if (this.tradeSuggestion.playerId == playerId) {
         return this.tradeSuggestion;
      } else {
         return this.tradeSuggestion2.playerId == playerId ? this.tradeSuggestion2 : null;
      }
   }

   public final TradeSuggestion findInvertSuggestion(int playerId) {
      if (this.tradeSuggestion.playerId == playerId) {
         return this.tradeSuggestion2;
      } else {
         return this.tradeSuggestion2.playerId == playerId ? this.tradeSuggestion : null;
      }
   }

   public final int getTax() {
      return this.inClan ? Com.c_trade_offer_tax_clan : Com.c_trade_offer_tax;
   }
}
