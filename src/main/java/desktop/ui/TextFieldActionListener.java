package desktop.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class TextFieldActionListener implements ActionListener {

   private int maximumLength;

   private TextFieldItem textFieldItem;

   TextFieldActionListener(TextFieldItem textFieldItem2, int maximumLength2) {
      this.textFieldItem = textFieldItem2;
      this.maximumLength = maximumLength2;
   }

   public final void actionPerformed(ActionEvent actionEvent) {
      if (TextFieldItem.getTextField(this.textFieldItem).getText().length() > this.maximumLength) {
         TextFieldItem.getTextField(this.textFieldItem).setText(TextFieldItem.getTextField(this.textFieldItem).getText().substring(0, this.maximumLength));
      }

   }
}
