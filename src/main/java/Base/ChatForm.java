package Base;

import desktop.ui.Form;

final class ChatForm extends Form {

   private ChatWindow chatWindow;

   ChatForm(ChatWindow chatWindow2, String messageText) {
       super(messageText);
       this.chatWindow = chatWindow2;
   }
}
