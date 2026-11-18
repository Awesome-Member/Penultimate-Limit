package desktop.ui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

final class CanvasWheelListener implements MouseWheelListener {

   private GameCanvas gameCanvas;

   CanvasWheelListener(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
      this.gameCanvas.wheelRotate(mouseWheelEvent.getWheelRotation());
   }
}
