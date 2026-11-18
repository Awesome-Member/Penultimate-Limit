package desktop.ui;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

final class FormScrollListener implements AdjustmentListener {

   private Form form;

   FormScrollListener(Form form2) {
      this.form = form2;
   }

   public final void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
      if (adjustmentEvent.getValueIsAdjusting()) {
         Form.setFocusNavigationEnabled(this.form, adjustmentEvent.getValue() == 0);
      } else {
         if (Form.isFocusNavigationEnabled(this.form)) {
            Form.getScrollPane(this.form).getVerticalScrollBar().setValue(0);
         }

      }
   }
}
