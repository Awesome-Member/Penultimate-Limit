package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendStringEvent extends StrategSendEvent {
   private String content;

   protected StrategSendStringEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -120:
            Strategist.instance.GP_Name = this.content;
            return;
         case -114:
            Strategist.instance.GP_AboutMe = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
