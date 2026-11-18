package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.trading.PlayersTrade;
import java.io.DataInputStream;
import java.util.Vector;

public final class StrategTradesFullSynchEvent extends StrategSendEvent {
   private Vector trades = new Vector();

   protected StrategTradesFullSynchEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      short size = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);

      for(int i = 0; i < size; ++i) {
         this.trades.addElement(new PlayersTrade(dataInputStream));
      }

   }

   public final void applyToStrategist() {
      Vector trades2 = this.trades;
      Strategist.instance.trades = trades2;
   }
}
