package Moduls.indirectevents;

import Base.GameCommons;
import java.io.DataInputStream;

public final class HeroEventFloatMessage extends HeroEvent {
   private String message;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.message = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void apply() {
      GameCommons.instance.showFloatText(this.message);
   }

   public final void afterApply() {
   }
}
