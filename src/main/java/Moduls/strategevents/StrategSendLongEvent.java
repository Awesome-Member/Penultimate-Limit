package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendLongEvent extends StrategSendEvent {
   private long content;

   protected StrategSendLongEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -102:
            Strategist.instance.vipStatusEndTime = this.content;
            return;
         case -100:
            Strategist.instance.protectionStatusEndTime = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
