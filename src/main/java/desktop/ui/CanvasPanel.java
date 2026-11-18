package desktop.ui;
import desktop.graphics.GameGraphics;

import applet.Main;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

final class CanvasPanel extends JPanel {

   private GameCanvas gameCanvas;

   CanvasPanel(GameCanvas gameCanvas2) {
      this.gameCanvas = gameCanvas2;
   }

   public final void paint(Graphics graphics2) {
      float width = (float)this.getWidth();
      Main instance2 = Main.instance;
      int bufferWidth = (int)(width / 2.0F);
      float height = (float)this.getHeight();
      Main instance3 = Main.instance;
      BufferedImage bufferedImage2;
      Graphics graphics3 = (bufferedImage2 = new BufferedImage(bufferWidth, (int)(height / 2.0F), 2)).getGraphics();
      GameGraphics gameGraphics2 = new GameGraphics(graphics3, bufferedImage2.getWidth(), bufferedImage2.getHeight());
      this.gameCanvas.paint(gameGraphics2);
      graphics3.dispose();
      graphics2.drawImage(bufferedImage2, 0, 0, this.getWidth(), this.getHeight(), (ImageObserver)null);
   }
}
