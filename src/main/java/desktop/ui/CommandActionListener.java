package desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class CommandActionListener implements ActionListener {

   private CommandItem commandItem;

   private Form form;

   CommandActionListener(Form form2, CommandItem commandItem2) {
      this.form = form2;
      this.commandItem = commandItem2;
   }

   public final void actionPerformed(ActionEvent actionEvent) {
      if (Form.getCommandListener(this.form) != null) {
         Form.getCommandListener(this.form).startThread(this.commandItem);
      }

   }
}
