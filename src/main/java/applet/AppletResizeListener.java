package applet;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

final class AppletResizeListener implements ComponentListener {

   private Main applet;

   AppletResizeListener(Main main) {
      this.applet = main;
   }

   public final void componentResized(ComponentEvent componentEvent) {
      if (Main.getGameComponent(this.applet) != null) {
         Main.getGameComponent(this.applet).setSize(this.applet.getWidth(), this.applet.getHeight());
      }

   }

   public final void componentMoved(ComponentEvent componentEvent) {
   }

   public final void componentShown(ComponentEvent componentEvent) {
   }

   public final void componentHidden(ComponentEvent componentEvent) {
   }
}
