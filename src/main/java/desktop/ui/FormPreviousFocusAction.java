package desktop.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

final class FormPreviousFocusAction extends AbstractAction {

   private Form form;

   FormPreviousFocusAction(Form form2) {
      this.form = form2;
   }

   public final void actionPerformed(ActionEvent actionEvent) {
      this.form.handleKeyCommand("ENTER");
   }
}
