package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendShortArrayEvent extends StrategSendEvent {
   private int[] content;

   protected StrategSendShortArrayEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = new int[decompiledsupport.UncheckedDataInput.readByte(dataInputStream)];

      for(int i = 0; i < this.content.length; ++i) {
         this.content[i] = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      }

   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -109:
            Strategist.instance.GP_SelfSkillsLevels = this.content;
            return;
         case -79:
            Strategist.instance.GP_AddSkillsLevels = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
