package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;

public final class StrategNullablePartyNameEvent extends StrategSendEvent {
   private String payload = null;

   protected StrategNullablePartyNameEvent(byte eventId) {
      super(eventId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.payload = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      }

   }

   public final void applyToStrategist() {
      if (this.payload == null) {
         Strategist.instance.isInClan = false;
         Strategist.instance.clanIcon = "";
      } else {
         Strategist.instance.isInClan = true;
         Strategist.instance.clanIcon = this.payload;
      }
   }
}
