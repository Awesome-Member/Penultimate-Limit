package desktop.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

final class TextBoxDocument extends PlainDocument {

   private TextBox textBox;

   TextBoxDocument(TextBox textBox2) {
      this.textBox = textBox2;
   }

   public final void insertString(int offset, String messageText, AttributeSet attributeSet) throws BadLocationException {
      if (messageText != null && this.getLength() + messageText.length() <= TextBox.getMaximumLength(this.textBox)) {
         super.insertString(offset, messageText, attributeSet);
      }
   }
}
