package desktop.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

final class CanvasKeyListener implements KeyListener {

   private GameCanvas gameCanvas;

   CanvasKeyListener(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void keyTyped(KeyEvent keyEvent) {
   }

   public final void keyPressed(KeyEvent keyEvent) {
      int keyCode;
      if ((keyCode = GameCanvas.getKeyCode(this.gameCanvas, keyEvent)) != -1) {
         this.gameCanvas.keyPressed(keyCode);
      }

   }

   public final void keyReleased(KeyEvent keyEvent) {
      int keyCode;
      if ((keyCode = GameCanvas.getKeyCode(this.gameCanvas, keyEvent)) != -1) {
         this.gameCanvas.keyReleased(keyCode);
      }

   }
}
