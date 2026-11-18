package Moduls.indirectevents;

import Base.Com;
import Moduls.chat.Chat;
import java.io.DataInputStream;

public final class HeroEventChatCounting extends HeroEvent {
   private int roomType;
   private int countPlayers = 0;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.roomType = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.countPlayers = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void apply() {
      switch (this.roomType) {
         case 0:
            Com.playersInChat = this.countPlayers;
            break;
         case 1:
            Com.playersInClanChat = this.countPlayers;
      }

      Chat instance2 = Chat.instance;
      Chat.refreshChatHeader();
   }

   public final void afterApply() {
   }
}
