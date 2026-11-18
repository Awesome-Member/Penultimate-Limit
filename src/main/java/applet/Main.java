package applet;

import Base.Com;
import Base.FeastOfWar;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import desktop.ui.Displayable;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main extends JApplet {
   public static Main instance;
   private static boolean initialized = false;
   private static Cursor cursor;
   private static Cursor cursor2;
   private Component component = null;
   private FeastOfWar feastOfWar = null;
   public boolean standalone = false;

   public static void main(String[] arguments) {
      SwingUtilities.invokeLater(new DesktopWindowLauncher());
   }

   public void start() {
      this.feastOfWar.startApp();
   }

   public void stop() {
      super.stop();
      FeastOfWar.resumePainting();
   }

   public void destroy() {
      try {
         this.feastOfWar.quit();
         FeastOfWar feastOfWar2 = this.feastOfWar;
         Com.flgs_MidletStart = false;
         this.feastOfWar = null;
      } catch (Exception exception) {
      }

      super.destroy();
      Runtime.getRuntime().exit(0);
   }

   public void init() {
      this.initializeClient(false);
   }

   private void initializeClient(boolean standalone2) {
      instance = this;
      this.standalone = standalone2;
      this.setBackground(Color.black);
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      this.addComponentListener(new AppletResizeListener(this));
      if (initialized) {
         JPanel jPanel4;
         (jPanel4 = new JPanel()).setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
         jPanel4.setBackground(new Color(-16777216));
         JLabel jLabel2;
         (jLabel2 = new JLabel()).setFont(new Font(jLabel2.getFont().getName(), 1, 24));
         jLabel2.setForeground(new Color(-32));
         jLabel2.setHorizontalAlignment(0);
         jLabel2.setHorizontalTextPosition(0);
         jLabel2.setText("Перезапусти браузер");
         jPanel4.add(jLabel2, new GridConstraints(0, 0, 1, 1, 0, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
         jPanel4.setSize(this.getWidth(), this.getHeight());
         this.getContentPane().add(jPanel4, "Center");
      } else {
         initialized = true;
         this.feastOfWar = new FeastOfWar();

         try {
            if ((FeastOfWar.browserPrefControl = this.getParameter("browserPrefControl")) == null) {
               FeastOfWar.browserPrefControl = "";
            }

            FeastOfWar.platform = this.getParameter("platform");
            FeastOfWar.profiles = this.getParameter("profiles");
            FeastOfWar.configuration = this.getParameter("configuration");
            FeastOfWar.string = this.getDocumentBase().toString();
         } catch (Exception exception) {
            FeastOfWar.browserPrefControl = "";
            FeastOfWar.platform = "standalone";
            FeastOfWar.profiles = "standalone";
            FeastOfWar.configuration = "";
            FeastOfWar.string = "http://win.lastlimit.ru";
         }

         Image image2;
         try {
            image2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Data/All/empty.png"));

            while(image2.getWidth((ImageObserver)null) == -1) {
               Thread.yield();
            }

            cursor = Toolkit.getDefaultToolkit().createCustomCursor(image2, new Point(0, 0), "LLEmptyCur");
         } catch (Exception exception2) {
            cursor = null;
         }

         try {
            image2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Data/All/Cursors/Hand.png"));

            while(image2.getWidth((ImageObserver)null) == -1) {
               Thread.yield();
            }

            cursor2 = Toolkit.getDefaultToolkit().createCustomCursor(image2, new Point(0, 0), "LLArrowCur");
         } catch (Exception exception3) {
            cursor2 = null;
         }

         showGameCursor();
      }
   }

   public static void showGameCursor() {
      if (cursor2 != null && cursor != null) {
         instance.setCursor(cursor2);
      }

   }

   public static void showDefaultCursor() {
      if (cursor2 != null && cursor != null) {
         instance.setCursor(cursor);
      }

   }

   public final void setDisplayable(Displayable displayable2) {
      if (this.component != null) {
         this.getContentPane().remove(this.component);
         this.component = null;
      }

      Component component2;
      (component2 = displayable2.getComponent()).setSize(this.getWidth(), this.getHeight());
      this.getContentPane().add(component2, "Center");
      component2.requestFocus();
      this.component = component2;
      component2.invalidate();
      component2.validate();
      component2.doLayout();
      this.getContentPane().doLayout();
      this.repaint();
      SwingUtilities.invokeLater(new UpdateSwingUiTask(this, component2));
   }


   static void initializeClient(Main main, boolean unusedProgressMarker) {
      main.initializeClient(true);
   }


   static Component getGameComponent(Main main) {
      return main.component;
   }
}
