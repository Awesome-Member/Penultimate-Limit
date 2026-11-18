package applet;

import java.awt.Component;
import javax.swing.SwingUtilities;

final class UpdateSwingUiTask implements Runnable {

   private Component component;

   private Main applet;

   UpdateSwingUiTask(Main main, Component component2) {
      this.applet = main;
      this.component = component2;
   }

   public final void run() {
      try {
         SwingUtilities.updateComponentTreeUI(this.component);
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }
}
