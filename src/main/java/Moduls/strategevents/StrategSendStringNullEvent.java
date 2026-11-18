package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendStringNullEvent extends StrategSendEvent {
   private String content;

   protected StrategSendStringNullEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.content = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      } else {
         this.content = null;
      }
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -83:
            Strategist.instance.isInClan = this.content != null;
            Strategist.instance.clanIcon = this.content == null ? "" : this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
