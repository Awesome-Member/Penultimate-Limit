package Moduls.indirectevents;

import Base.IndirectNetworkEvent;

public abstract class HeroEvent implements IndirectNetworkEvent {
   public final int getApplyOrder() {
      return 1;
   }
}
