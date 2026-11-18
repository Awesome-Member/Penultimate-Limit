package Moduls.indirectevents;

import Base.GameCommons;
import java.io.DataInputStream;

public final class HeroEventToSold extends HeroEvent {
   public final void loadFromStream(DataInputStream dataInputStream) {
   }

   public final void apply() {
      GameCommons.instance.showFloatText("Ушло в сбытое");
   }

   public final void afterApply() {
   }
}
