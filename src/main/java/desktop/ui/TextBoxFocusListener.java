package desktop.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

final class TextBoxFocusListener implements FocusListener {

   private TextBox textBox;

   TextBoxFocusListener(TextBox textBox2) {
      this.textBox = textBox2;
   }

   public final void focusGained(FocusEvent focusEvent) {
      TextBox.getTextArea(this.textBox).requestFocus();
   }

   public final void focusLost(FocusEvent focusEvent) {
   }
}
