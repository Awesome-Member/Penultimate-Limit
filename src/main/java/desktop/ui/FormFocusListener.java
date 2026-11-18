package desktop.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;

final class FormFocusListener implements FocusListener {

   private Form form;

   FormFocusListener(Form form2) {
      this.form = form2;
   }

   public final void focusGained(FocusEvent focusEvent) {
      Iterator iterator = Form.getFormItems(this.form).iterator();

      while(iterator.hasNext()) {
         ((FormItem)iterator.next()).requestFocus();
      }

   }

   public final void focusLost(FocusEvent focusEvent) {
   }
}
