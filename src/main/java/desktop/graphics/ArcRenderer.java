package desktop.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import desktop.graphics.GameGraphics;

public final class ArcRenderer {
   private GameGraphics gameGraphics;

   public ArcRenderer(GameGraphics gameGraphics2) {
      this.gameGraphics = gameGraphics2;
   }

   public final void fillArc(int x, int y, int width, int height, int arcAngle) {
      Graphics graphics2;
      (graphics2 = this.gameGraphics.getAwtGraphics()).setColor(new Color(1280102399, true));
      graphics2.fillArc(x, y, width, height, 0, 360);
   }

   static {
      new HashMap();
   }
}
