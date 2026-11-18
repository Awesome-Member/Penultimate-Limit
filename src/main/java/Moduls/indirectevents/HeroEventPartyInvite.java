package Moduls.indirectevents;

import MenuPck.ListTypes.OtherMenuList;
import java.io.DataInputStream;

public final class HeroEventPartyInvite extends HeroEvent {
   private String leaderName;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.leaderName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void apply() {
      OtherMenuList otherMenuList;
      (otherMenuList = new OtherMenuList()).showConfirmationForMenu(166, "Присоединиться к отряду " + this.leaderName + "?");
      otherMenuList.disableBackgroundClick();
   }

   public final void afterApply() {
   }
}
