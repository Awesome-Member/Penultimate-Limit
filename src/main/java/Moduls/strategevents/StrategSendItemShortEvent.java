package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendItemShortEvent extends StrategSendEvent {
   private short itemInd;
   private short content;

   protected StrategSendItemShortEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.itemInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.content = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case 111:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).count = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
