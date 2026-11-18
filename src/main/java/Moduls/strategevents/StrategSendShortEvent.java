package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendShortEvent extends StrategSendEvent {
   private short content;

   protected StrategSendShortEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -122:
            Strategist.instance.GP_MaxTroops = this.content;
            return;
         case -118:
            Strategist.instance.GP_Level = this.content;
            return;
         case -115:
            Strategist.instance.GP_SkillPoints = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
