package Base;

import Moduls.chat.Chat;

public final class ChatSendTask implements Runnable {
   public int action;

   private ChatWindow chatWindow;

   public ChatSendTask(ChatWindow chatWindow2) {
      this.chatWindow = chatWindow2;
      this.action = 0;
   }

   public final void run() {
      if (this.action == 1) {
         String string = this.chatWindow.textInput.getString();
         this.chatWindow.textInput.setString("Отправка...");
         if (!string.equals("")) {
            if (this.chatWindow.mode == 2) {
               string = "ADMMESS " + string;
            }

            Chat instance2 = Chat.instance;
            Chat.sendMessage(string, ChatWindow.getChannelType(this.chatWindow), ChatWindow.getChannelId(this.chatWindow));
         }

         this.chatWindow.textInput.setString("");
         if (this.chatWindow.mode == 2 || this.chatWindow.mode == 3) {
            this.chatWindow.diactivate();
         }
      } else if (this.action == 2) {
         this.chatWindow.textInput.setString("Выход...");
         this.chatWindow.diactivate();
      }

      this.chatWindow.done = false;
   }
}
