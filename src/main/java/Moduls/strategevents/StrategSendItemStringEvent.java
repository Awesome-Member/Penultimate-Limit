package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.InventoryItem;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendItemStringEvent extends StrategSendEvent {
   private short itemInd;
   private String content;

   protected StrategSendItemStringEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.itemInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.content = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case 121:
            ((InventoryItem)Strategist.instance.items.elementAt(this.itemInd)).description = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
