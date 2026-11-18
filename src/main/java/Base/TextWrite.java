package Base;

import MenuPck.MenuManager;
import desktop.ui.CommandItem;
import desktop.ui.CommandListener;
import desktop.ui.TextBox;

public final class TextWrite implements CommandListener {
   private int mode = 1;
   public boolean isActive = false;
   private CommandItem exitCommand = new CommandItem("Отмена");
   private CommandItem okCommand = new CommandItem("Отправить");
   private TextBox form = null;
   private boolean done = true;
   private boolean hideMenu = false;
   private int backMenuId = 0;
   private int backIndex = 0;

   public TextWrite() {
      this.form = new TextBox("", "", 512);
      this.form.addCommand(this.okCommand);
      this.form.addCommand(this.exitCommand);
      this.form.setCommandListener(this);
   }

   public final void initSystemGUI() {
      if (this.form == null) {
         System.out.println("TextWrite fail initSystemGUI");
      }

   }

   public final void activate(int wMode, String headetText, String initText, int backMenuId2, int backIndex2, boolean hideMenu2, int maxLen) {
      this.isActive = true;
      this.hideMenu = false;
      this.backIndex = backIndex2;
      this.backMenuId = backMenuId2;
      this.mode = 0;
      this.form.setTitle2(headetText);
      this.form.setString(initText);
      this.form.setMaxLength(maxLen);
      Com.mainTh.onOutFromCurrentDisplay();
      Com.display.setCurrent(this.form);

      while(!this.done) {
         Thread.yield();
      }

      this.done = true;
   }

   public final void diactivate(boolean res, String messageText) {
      if (this.isActive) {
         Com.mainTh.onInToCurrentDisplay();
         Com.cht_Moder = false;
         this.isActive = false;
         Com.display.setCurrent(Com.mainTh);
         if (this.backMenuId >= 0) {
            MenuManager.instance.textEnterResult(this.backMenuId, this.backIndex, res, messageText);
         }

         if (this.hideMenu) {
            MenuManager.instance.guiHandler.hideAllWindows();
         }

      }
   }

   public final void totalExit() {
      if (this.isActive) {
         MenuManager.instance.guiHandler.hideAllWindows();
         Com.mainTh.onInToCurrentDisplay();
         Com.cht_Moder = false;
         this.isActive = false;
         Com.display.setCurrent(Com.mainTh);
      }
   }

   public final void startThread(CommandItem commandItem2) {
      TextWriteTask textWriteTask;
      if (commandItem2 == this.okCommand && this.done) {
         this.done = false;
         (textWriteTask = new TextWriteTask(this)).action = 1;
         (new Thread(textWriteTask)).start();
      }

      if (commandItem2 == this.exitCommand && this.done) {
         this.done = false;
         (textWriteTask = new TextWriteTask(this)).action = 2;
         (new Thread(textWriteTask)).start();
      }

   }


   static TextBox getTextBox(TextWrite textWrite2) {
      return textWrite2.form;
   }


   static boolean markDone(TextWrite textWrite2, boolean ignoredState) {
      return textWrite2.done = true;
   }
}
