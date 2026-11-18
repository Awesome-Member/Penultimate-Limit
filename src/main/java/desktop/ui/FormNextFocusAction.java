package desktop.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

final class FormNextFocusAction extends AbstractAction {

   private Form form;

   FormNextFocusAction(Form form2) {
      this.form = form2;
   }

   public final void actionPerformed(ActionEvent actionEvent) {
      this.form.handleKeyCommand("ESCAPE");
   }
}
