package Base;

public final class TextWriteTask implements Runnable {
   public int action;

   private TextWrite textWrite;

   public TextWriteTask(TextWrite textWrite2) {
      this.textWrite = textWrite2;
      this.action = 0;
   }

   public final void run() {
      String messageText;
      if (this.action == 1) {
         messageText = TextWrite.getTextBox(this.textWrite).getString();
         TextWrite.getTextBox(this.textWrite).setString("Отправка...");
         this.textWrite.diactivate(true, messageText);
      } else if (this.action == 2) {
         messageText = TextWrite.getTextBox(this.textWrite).getString();
         TextWrite.getTextBox(this.textWrite).setString("Выход...");
         this.textWrite.diactivate(false, messageText);
      }

      TextWrite.markDone(this.textWrite, true);
   }
}
