package Moduls.strategevents;

import Moduls.StrategGraphic;
import Moduls.Strategist;
import java.io.DataInputStream;

public final class StrategSendStrategGraphicsEvent extends StrategSendEvent {
   private StrategGraphic content;
   private int atackSprite = -2;
   private int atackBaseX = 0;
   private int atackBaseY = -13;
   private int atackLatency = 0;

   protected StrategSendStrategGraphicsEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = new StrategGraphic();
      this.content.readFromNet(dataInputStream);
      this.atackSprite = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.atackBaseX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.atackBaseY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.atackLatency = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void applyToStrategist() {
      Strategist.instance.avatarGraphic = this.content;
      Strategist.instance.atackSprite = this.atackSprite;
      Strategist.instance.atackBaseX = this.atackBaseX;
      Strategist.instance.atackBaseY = this.atackBaseY;
      Strategist.instance.atackLatency = this.atackLatency;
   }
}
