package desktop.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;

public final class CommandItem extends FormItem {
   private String label;
   private JButton jButton;

   public CommandItem(String label2) {
      this.label = label2;
      this.jButton = new JButton();
      this.jButton.setText(label2);
      this.jButton.setMinimumSize(new Dimension(100, 30));
   }

   public final Component getComponent() {
      return this.jButton;
   }

   public final void requestFocus() {
   }
}
