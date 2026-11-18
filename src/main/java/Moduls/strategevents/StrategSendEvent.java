package Moduls.strategevents;

import Base.IndirectNetworkEvent;
import Moduls.Strategist;

public abstract class StrategSendEvent implements IndirectNetworkEvent {
   public byte elementId;

   protected StrategSendEvent(byte elementId2) {
      this.elementId = elementId2;
   }

   public abstract void applyToStrategist();

   public final void apply() {
      this.applyToStrategist();
      Strategist.instance.onChanged(true);
   }

   public int getApplyOrder() {
      return 1;
   }

   public void afterApply() {
   }
}
