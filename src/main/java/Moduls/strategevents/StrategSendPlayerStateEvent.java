package Moduls.strategevents;

import Moduls.PlayerGameState;
import java.io.DataInputStream;

public final class StrategSendPlayerStateEvent extends StrategSendEvent {
   private byte content;

   protected StrategSendPlayerStateEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
   }

   public final void applyToStrategist() {
   }

   public final int getApplyOrder() {
      return 2;
   }

   public final void afterApply() {
      PlayerGameState.instance.set(this.content);
   }
}
