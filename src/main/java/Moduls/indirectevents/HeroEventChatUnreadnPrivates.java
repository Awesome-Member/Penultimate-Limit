package Moduls.indirectevents;

import Moduls.chat.Chat;
import java.io.DataInputStream;

public final class HeroEventChatUnreadnPrivates extends HeroEvent {
   private int[] unreadnPrivates;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.unreadnPrivates = new int[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

      for(int i = 0; i < this.unreadnPrivates.length; ++i) {
         this.unreadnPrivates[i] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

   }

   public final void apply() {
      Chat.instance.incomeUnreadnPrivates(this.unreadnPrivates);
   }

   public final void afterApply() {
   }
}
