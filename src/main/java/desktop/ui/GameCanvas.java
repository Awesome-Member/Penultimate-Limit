package desktop.ui;
import desktop.graphics.GameGraphics;

import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public abstract class GameCanvas extends Displayable {
   private JPanel jPanel = new CanvasPanel(this);

   public GameCanvas() {
      this.jPanel.setFocusable(true);
      this.jPanel.addComponentListener(new CanvasResizeListener(this));
      this.jPanel.addKeyListener(new CanvasKeyListener(this));
      this.jPanel.addMouseListener(new CanvasMouseListener(this));
      this.jPanel.addMouseWheelListener(new CanvasWheelListener(this));
      this.jPanel.addMouseMotionListener(new CanvasMotionListener(this));
   }

   public final Component getComponent() {
      return this.jPanel;
   }

   public final void repaint() {
      this.jPanel.repaint();
   }

   public abstract void paint(GameGraphics gameGraphics2);

   public void wheelRotate(int num) {
   }

   public void pointerPressed(int x, int y) {
   }

   public void pointerReleased(int x, int y) {
   }

   public void enqueuePointerDragged(int pointerX, int pointerY) {
   }

   public void enqueuePointerPressed(int pointerX, int pointerY) {
   }

   public void keyReleased(int keyOrPressCode) {
   }

   public void keyPressed(int keyOrActionCode) {
   }

   public static int getGameAction(int keyCode) {
      if (keyCode == 9) {
         return 2;
      } else if (keyCode == 8) {
         return 1;
      } else if (keyCode == 10) {
         return 3;
      } else if (keyCode == 11) {
         return 4;
      } else {
         return keyCode == 12 ? 5 : -1;
      }
   }

   protected void sizeChanged(int width, int height) {
   }


   static int getKeyCode(GameCanvas gameCanvas2, KeyEvent keyEvent) {
      switch (keyEvent.getKeyCode()) {
         case 10:
         case 32:
         case 53:
            return 12;
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 28:
         case 29:
         case 30:
         case 31:
         case 33:
         case 34:
         case 35:
         case 36:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 82:
         case 84:
         case 85:
         case 89:
         default:
            return -1;
         case 27:
         case 69:
            return -7;
         case 37:
         case 52:
         case 65:
            return 10;
         case 38:
         case 56:
         case 87:
            return 8;
         case 39:
         case 54:
         case 68:
            return 11;
         case 40:
         case 50:
         case 83:
            return 9;
         case 48:
            return 48;
         case 49:
         case 88:
            return 49;
         case 51:
         case 67:
            return 51;
         case 55:
         case 86:
            return 55;
         case 57:
         case 66:
            return 57;
         case 81:
         case 90:
            return -6;
      }
   }
}
