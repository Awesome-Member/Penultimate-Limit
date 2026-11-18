package Moduls.strategevents;

import Moduls.Strategist;
import Moduls.chat.Chat;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendByteEvent extends StrategSendEvent {
   private byte content;

   protected StrategSendByteEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -119:
            Strategist.instance.GP_Race = this.content;
            return;
         case -104:
            Strategist.instance.GP_Gender = this.content;
            return;
         case -103:
            Strategist.instance.vipStatus = this.content;
            return;
         case -82:
            Strategist.instance.partyState = this.content;
            return;
         case -72:
            Strategist.instance.guiMode = this.content;
            return;
         case -69:
            Strategist.instance.compassMode = this.content;
            return;
         case -61:
            Strategist.instance.commonChatShowTypes = this.content;
            Chat.instance.setShowInCommonFlags(this.content == 0 || this.content == 1, this.content == 0 || this.content == 2, this.content == 0 || this.content == 1 || this.content == 2);
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
