package desktop.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public final class StringItem extends FormItem {
   private String bodyText;
   private JPanel jPanel = new JPanel();
   private JTextArea jTextArea;

   public StringItem(String messageText, String messageText2) {
      this.jPanel.setLayout(new BorderLayout(0, 0));
      JLabel jLabel2;
      (jLabel2 = new JLabel()).setText(messageText);
      this.jPanel.add(jLabel2, "North");
      this.jTextArea = new JTextArea();
      this.jTextArea.setBackground(new Color(15658734));
      this.jTextArea.setBorder(new EmptyBorder(0, 0, 0, 0));
      this.jTextArea.setEditable(false);
      this.jTextArea.setFont(new Font(this.jTextArea.getFont().getName(), this.jTextArea.getFont().getStyle(), 14));
      this.jTextArea.setText(messageText2);
      this.jTextArea.setWrapStyleWord(true);
      this.jTextArea.setLineWrap(true);
      this.jTextArea.setPreferredSize((Dimension)null);
      this.jPanel.add(this.jTextArea, "South");
   }

   public final void setText(String bodyText2) {
      this.bodyText = bodyText2;
      this.jTextArea.setText(bodyText2);
   }

   public final void setFont(LegacyFont legacyFont) {
      byte fontSize = 12;
      switch (legacyFont.size) {
         case 0:
            fontSize = 22;
            break;
         case 8:
            fontSize = 14;
            break;
         case 16:
            fontSize = 28;
      }

      Font font2 = this.jTextArea.getFont();
      this.jTextArea.setFont(new Font(font2.getName(), font2.getStyle(), fontSize));
   }

   public final Component getComponent() {
      return this.jPanel;
   }

   public final void requestFocus() {
   }
}
