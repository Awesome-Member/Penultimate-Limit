package Moduls.trading;

import Engine.Mystery;
import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;

public final class TradeSuggestion {
   public int playerId;
   public String playerName;
   public long gold = 0L;
   public InventoryItem[] items = new InventoryItem[0];
   public boolean isAccept = false;
   public long lastChangeTime = 0L;
   public boolean haveNew = false;
   private long haveNewSinceTime = 0L;

   protected TradeSuggestion(DataInputStream dataInputStream) {
      this.readForClient(dataInputStream);
   }

   private void readForClient(DataInputStream dataInputStream) {
      this.playerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.playerName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.gold = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.isAccept = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.lastChangeTime = System.currentTimeMillis() - decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.items = new InventoryItem[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      for(int i = 0; i < this.items.length; ++i) {
         this.items[i] = Strategist.loadIndventoryItem(dataInputStream);
      }

   }

   public final String getTitleForFastInfo() {
      return (this.haveNew && Mystery.currentTimeMillis - this.haveNewSinceTime < 1800L && (Mystery.currentTimeMillis - this.haveNewSinceTime) % 600L >= 300L ? "  " : "ͪͫ") + this.playerName;
   }

   public final void markNew() {
      this.haveNew = true;
      this.haveNewSinceTime = Mystery.currentTimeMillis;
   }
}
