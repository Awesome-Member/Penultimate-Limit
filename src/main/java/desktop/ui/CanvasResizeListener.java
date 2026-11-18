package desktop.ui;

import applet.Main;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

final class CanvasResizeListener implements ComponentListener {

   private GameCanvas gameCanvas;

   CanvasResizeListener(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void componentResized(ComponentEvent componentEvent) {
      GameCanvas gameCanvas2 = this.gameCanvas;
      float component = (float)componentEvent.getComponent().getWidth();
      Main instance2 = Main.instance;
      int scaledWidth = (int)(component / 2.0F);
      float component2 = (float)componentEvent.getComponent().getHeight();
      Main instance3 = Main.instance;
      gameCanvas2.sizeChanged(scaledWidth, (int)(component2 / 2.0F));
   }

   public final void componentMoved(ComponentEvent componentEvent) {
   }

   public final void componentShown(ComponentEvent componentEvent) {
   }

   public final void componentHidden(ComponentEvent componentEvent) {
   }
}
