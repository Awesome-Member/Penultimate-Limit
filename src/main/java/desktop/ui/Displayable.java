package desktop.ui;

import applet.Main;
import java.awt.Component;

public abstract class Displayable {
   public static int getWidth() {
      float width = (float)Main.instance.getWidth();
      Main instance2 = Main.instance;
      return (int)(width / 2.0F);
   }

   public static int getHeight() {
      float height = (float)Main.instance.getHeight();
      Main instance2 = Main.instance;
      return (int)(height / 2.0F);
   }

   public abstract Component getComponent();
}
