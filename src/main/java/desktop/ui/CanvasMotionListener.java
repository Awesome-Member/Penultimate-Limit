package desktop.ui;

import applet.Main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

final class CanvasMotionListener implements MouseMotionListener {

   private GameCanvas gameCanvas;

   CanvasMotionListener(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void mouseDragged(MouseEvent mouseEvent) {
      GameCanvas gameCanvas2 = this.gameCanvas;
      float x = (float)mouseEvent.getX();
      Main instance2 = Main.instance;
      int scaledX = (int)(x / 2.0F);
      float y = (float)mouseEvent.getY();
      Main instance3 = Main.instance;
      gameCanvas2.enqueuePointerDragged(scaledX, (int)(y / 2.0F));
   }

   public final void mouseMoved(MouseEvent mouseEvent) {
      GameCanvas gameCanvas2 = this.gameCanvas;
      float x = (float)mouseEvent.getX();
      Main instance2 = Main.instance;
      int scaledX = (int)(x / 2.0F);
      float y = (float)mouseEvent.getY();
      Main instance3 = Main.instance;
      gameCanvas2.enqueuePointerPressed(scaledX, (int)(y / 2.0F));
   }
}
