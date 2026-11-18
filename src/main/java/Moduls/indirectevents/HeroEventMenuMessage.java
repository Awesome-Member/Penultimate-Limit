package Moduls.indirectevents;

import MenuPck.ListTypes.OtherMenuList;
import java.io.DataInputStream;

public final class HeroEventMenuMessage extends HeroEvent {
   private String message;
   private int time;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.message = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.time = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void apply() {
      OtherMenuList otherMenuList;
      (otherMenuList = new OtherMenuList()).showMenuMessage(175, this.message);
      otherMenuList.ignoreClickDeltaTime((long)this.time);
   }

   public final void afterApply() {
   }
}
