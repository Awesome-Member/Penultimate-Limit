package desktop.ui;

import applet.Main;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

final class CanvasMouseListener implements MouseListener {

   private GameCanvas gameCanvas;

   CanvasMouseListener(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void mouseClicked(MouseEvent mouseEvent) {
   }

   public final void mouseEntered(MouseEvent mouseEvent) {
   }

   public final void mouseExited(MouseEvent mouseEvent) {
   }

   public final void mousePressed(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == 1) {
         GameCanvas gameCanvas2 = this.gameCanvas;
         float x = (float)mouseEvent.getX();
         Main instance2 = Main.instance;
         int scaledX = (int)(x / 2.0F);
         float y = (float)mouseEvent.getY();
         Main instance3 = Main.instance;
         gameCanvas2.pointerPressed(scaledX, (int)(y / 2.0F));
      }

      if (mouseEvent.getButton() == 3) {
         this.gameCanvas.keyPressed(-7);
      }

   }

   public final void mouseReleased(MouseEvent mouseEvent) {
      if (mouseEvent.getButton() == 1) {
         GameCanvas gameCanvas2 = this.gameCanvas;
         float x = (float)mouseEvent.getX();
         Main instance2 = Main.instance;
         int scaledX = (int)(x / 2.0F);
         float y = (float)mouseEvent.getY();
         Main instance3 = Main.instance;
         gameCanvas2.pointerReleased(scaledX, (int)(y / 2.0F));
      }

      if (mouseEvent.getButton() == 3) {
         this.gameCanvas.keyReleased(-7);
      }

   }
}
