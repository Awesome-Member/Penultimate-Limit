package applet;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import javax.swing.JFrame;

final class DesktopWindowLauncher implements Runnable {
   public final void run() {
      JFrame jFrame;
      (jFrame = new JFrame("Last Limit")).setDefaultCloseOperation(3);
      jFrame.setResizable(true);
      Main main;
      Main.initializeClient(main = new Main(), true);
      main.setSize(940, 620);
      main.setPreferredSize(new Dimension(940, 620));
      main.getContentPane().setSize(940, 620);
      main.getContentPane().setPreferredSize(new Dimension(940, 620));
      jFrame.getContentPane().add(main);

      try {
         Image image2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Data/All/icon32x32.png"));

         while(image2.getWidth((ImageObserver)null) == -1) {
            Thread.yield();
         }

         jFrame.setIconImage(image2);
      } catch (Exception exception) {
      }

      jFrame.pack();
      jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      jFrame.setVisible(true);
      main.start();
   }
}
