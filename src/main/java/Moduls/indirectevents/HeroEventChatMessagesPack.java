package Moduls.indirectevents;

import Moduls.chat.Chat;
import Moduls.chat.ChatMessage;
import java.io.DataInputStream;

public final class HeroEventChatMessagesPack extends HeroEvent {
   private ChatMessage[] messages;

   public final void loadFromStream(DataInputStream dataInputStream) {
      boolean newest = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.messages = new ChatMessage[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

      for(int i = 0; i < this.messages.length; ++i) {
         ChatMessage chatMessage;
         if ((chatMessage = new ChatMessage(dataInputStream)).playerId >= 0) {
            Chat.instance.addLastWriterFromUncommonThread(chatMessage.playerId);
            if (newest) {
               Chat.instance.addHeadMessage(chatMessage);
            }
         }

         this.messages[i] = chatMessage;
      }

   }

   public final void apply() {
      for(int i = 0; i < this.messages.length; ++i) {
         ChatMessage chatMessage = this.messages[i];
         Chat.instance.incomeMessage(chatMessage);
      }

   }

   public final void afterApply() {
   }
}
