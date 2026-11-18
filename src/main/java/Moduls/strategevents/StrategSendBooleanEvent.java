package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendBooleanEvent extends StrategSendEvent {
   private boolean content;

   protected StrategSendBooleanEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -101:
            Strategist.instance.protectionStatus = this.content;
            return;
         case -86:
            Strategist.instance.hasMaster = this.content;
            return;
         case -80:
            Strategist.instance.isModer = this.content;
            return;
         case -76:
            Strategist.instance.isAutoCancelTrade = this.content;
            return;
         case -75:
            Strategist.instance.isAutoCancelFights = this.content;
            return;
         case -74:
            Strategist.instance.isAutoCancelParty = this.content;
            return;
         case -71:
            Strategist.instance.guiDrawMiniMap = this.content;
            return;
         case -62:
            Strategist.instance.isAutoFixItems = this.content;
            return;
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
      }
   }
}
