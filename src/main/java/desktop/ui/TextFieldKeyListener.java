package desktop.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

final class TextFieldKeyListener implements KeyListener {

   private TextFieldItem textFieldItem;

   TextFieldKeyListener(TextFieldItem textFieldItem2) {
      this.textFieldItem = textFieldItem2;
   }

   public final void keyTyped(KeyEvent keyEvent) {
   }

   public final void keyPressed(KeyEvent keyEvent) {
      if (keyEvent.getKeyCode() == 10) {
         this.textFieldItem.form.handleKeyCommand("ENTER");
      }

   }

   public final void keyReleased(KeyEvent keyEvent) {
   }
}
