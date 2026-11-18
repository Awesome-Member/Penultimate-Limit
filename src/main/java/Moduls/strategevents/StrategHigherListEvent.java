package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.Higher;
import java.io.DataInputStream;
import java.util.Vector;

public final class StrategHigherListEvent extends StrategSendEvent {
   private Vector payloadEntries = new Vector();

   protected StrategHigherListEvent(byte eventId) {
      super(eventId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      byte byteCode = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.payloadEntries.removeAllElements();

      for(int byteCodeIndex = 0; byteCodeIndex < byteCode; ++byteCodeIndex) {
         this.payloadEntries.addElement(new Higher());
         ((Higher)this.payloadEntries.lastElement()).LoadFromStream(dataInputStream);
      }

   }

   public final void applyToStrategist() {
      Strategist.instance.GP_Army.removeAllElements();
      Strategist.instance.GP_Highers = this.payloadEntries;
   }
}
