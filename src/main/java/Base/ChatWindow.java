package Base;

import MenuPck.MenuManager;
import Moduls.chat.Chat;
import desktop.ui.TextFieldItem;
import desktop.ui.CommandItem;
import desktop.ui.CommandListener;
import desktop.ui.LegacyFont;
import desktop.ui.Form;
import desktop.ui.FormItem;
import desktop.ui.StringItem;

public final class ChatWindow implements CommandListener {
   public boolean isActive = false;
   private int channelType;
   private int channelId;
   public int mode = 1;
   private CommandItem exitCommand = new CommandItem("Выйти");
   private CommandItem okCommand = new CommandItem("Сказать");
   Form form = new ChatForm(this, "Сообщение");
   public TextFieldItem textInput = new TextFieldItem("", "", 120);
   private StringItem textArea = new StringItem("", "");
   boolean done = false;

   public ChatWindow() {
      this.form.appendItem((FormItem)this.textInput);
      this.form.appendItem((FormItem)this.textArea);
      this.form.addCommand(this.okCommand);
      this.form.addCommand(this.exitCommand);
      this.form.setCommandListener((CommandListener)this);
   }

   public final void onHeaderDependentDataChanged() {
      String header = Chat.instance.createHeader(this.channelType);
      if (!this.textInput.getString2().equals(header)) {
         this.textInput.setString2(header);
      }

   }

   public final void onVisibleChatContentChanged() {
      this.textArea.setText(Chat.instance.createChatFlowText(this.channelType));
   }

   public final int getChannelType() {
      return this.channelType;
   }

   public final int getChannelId() {
      return this.channelId;
   }

   public final void activate(int wMode, int channelType2, int channelId2) {
      this.channelType = channelType2;
      this.channelId = channelId2;
      this.isActive = true;
      this.textInput.setString2(Chat.instance.createHeader(channelType2));
      channelId2 = Com.chatFontSize;
      this.textArea.setFont(new LegacyFont(channelId2));
      this.textArea.setText(Chat.instance.createChatFlowText(channelType2));
      this.mode = wMode;
      this.textInput.setString("");
      Com.mainTh.onOutFromCurrentDisplay();
      Com.display.setCurrent(this.form);

      while(this.done) {
         Thread.yield();
      }

      this.done = true;
      ChatSendTask chatSendTask;
      (chatSendTask = new ChatSendTask(this)).action = 3;
      (new Thread(chatSendTask)).start();
      Com.showGameCursor();
   }

   public final void diactivate() {
      if (this.isActive) {
         if (this.mode != 3) {
            if (GameCommons.instance.isFromGameMenu) {
               MenuManager.instance.guiHandler.hideAllWindows();
            }
         } else {
            Com.showDefaultCursor();
         }

         Com.mainTh.onInToCurrentDisplay();
         Com.cht_Moder = false;
         Com.display.setCurrent(Com.mainTh);
         this.isActive = false;
         Chat.instance.onCloseChatWindow();
      }
   }

   public final void startThread(CommandItem commandItem2) {
      ChatSendTask chatSendTask;
      if (commandItem2 == this.okCommand && !this.done) {
         this.done = true;
         (chatSendTask = new ChatSendTask(this)).action = 1;
         (new Thread(chatSendTask)).start();
      }

      if (commandItem2 == this.exitCommand && !this.done) {
         this.done = true;
         (chatSendTask = new ChatSendTask(this)).action = 2;
         (new Thread(chatSendTask)).start();
      }

   }


   static int getChannelType(ChatWindow chatWindow2) {
      return chatWindow2.channelType;
   }


   static int getChannelId(ChatWindow chatWindow2) {
      return chatWindow2.channelId;
   }
}
