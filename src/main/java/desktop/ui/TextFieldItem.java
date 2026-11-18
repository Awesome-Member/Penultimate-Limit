package desktop.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class TextFieldItem extends FormItem {
   private int maxSize;
   private String string;
   private String text;
   private JPanel jPanel;
   private JTextField jTextField;
   private JLabel jLabel;

   public TextFieldItem(String string2, String messageText, int maximumLength) {
      this.string = string2;
      this.text = messageText;
      this.maxSize = 120;
      this.jPanel = new JPanel();
      this.jPanel.setLayout(new BorderLayout(0, 0));
      this.jLabel = new JLabel();
      this.jLabel.setText(string2);
      this.jPanel.add(this.jLabel, "North");
      this.jTextField = new JTextField();
      this.jTextField.setPreferredSize(new Dimension(100, 40));
      this.jTextField.setText(messageText);
      this.jTextField.addActionListener(new TextFieldActionListener(this, 120));
      this.jTextField.addKeyListener(new TextFieldKeyListener(this));
      this.jPanel.add(this.jTextField, "South");
   }

   public final void requestFocus() {
      this.jTextField.requestFocus();
   }

   public final void setString(String messageText) {
      this.text = messageText;
      this.jTextField.setText(messageText);
   }

   public final String getString() {
      this.text = this.jTextField.getText();
      return this.text;
   }

   public final void setString2(String string2) {
      this.string = string2;
      this.jLabel.setText(string2);
   }

   public final String getString2() {
      return this.string;
   }

   public final Component getComponent() {
      return this.jPanel;
   }


   static JTextField getTextField(TextFieldItem textFieldItem2) {
      return textFieldItem2.jTextField;
   }
}
