package Moduls;

import Base.Com;
import desktop.graphics.GameGraphics;

public final class DrawOrderElementEnemy extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementEnemy((short)-1, (short)-1, (short)-1, (short)-1));
   public short ind = -1;

   private DrawOrderElementEnemy(short rx, short ry, short sortInd, short ind2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      Enemy[] enemies = Com.EnsMap.Enemys;
      int ind2 = this.ind;
      if (ind2 < 0 || ind2 >= enemies.length) return;
      Enemy enemy = enemies[ind2];
      if (enemy != null) enemy.draw(gameGraphics2, Com.cameraWorld.isActiveLongStay());
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementEnemy((short)-1, (short)-1, (short)-1, (short)-1);
   }

   public final void returnInstance() {
      factory.returnInstance(this);
   }

   public static DrawOrderElementEnemy getInstance(short rx, short ry, short sortInd, short ind2) {
      DrawOrderElementEnemy drawOrderElementEnemy;
      (drawOrderElementEnemy = (DrawOrderElementEnemy)factory.getInstance()).rx = rx;
      drawOrderElementEnemy.ry = ry;
      drawOrderElementEnemy.sortInd = sortInd;
      drawOrderElementEnemy.ind = ind2;
      return drawOrderElementEnemy;
   }
}
