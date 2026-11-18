package desktop.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

public final class TextBox extends Form {
   private JTextArea jTextArea;
   private int maxLength;

   public TextBox(String messageText, String messageText2, int maximumLength) {
      super(messageText);
      this.getComponent().addFocusListener(new TextBoxFocusListener(this));
      this.maxLength = 512;
      this.jTextArea = new JTextArea();
      this.jTextArea.setWrapStyleWord(true);
      this.jTextArea.setLineWrap(true);
      this.jTextArea.setDocument(new TextBoxDocument(this));
      this.jTextArea.setText(messageText2);
      this.jTextArea.setBorder(new BevelBorder(1));
      this.jPanel3.add(this.jTextArea, new GridConstraints(2, 0, 1, 1, 0, 3, 4, 4, (Dimension)null, new Dimension(150, 50), (Dimension)null, 0, false));
   }

   public final void setTitle2(String messageText) {
      this.setTitle(messageText);
   }

   public final void setString(String messageText) {
      this.jTextArea.setText(messageText);
   }

   public final String getString() {
      return this.jTextArea.getText();
   }

   public final void setMaxLength(int maxLength2) {
      this.maxLength = maxLength2;
   }


   static JTextArea getTextArea(TextBox textBox2) {
      return textBox2.jTextArea;
   }


   static int getMaximumLength(TextBox textBox2) {
      return textBox2.maxLength;
   }
}
