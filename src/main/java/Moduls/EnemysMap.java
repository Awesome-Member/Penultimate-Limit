package Moduls;

import Base.ClientState;
import Engine.Camera;
import java.util.Vector;

public final class EnemysMap {
   public Enemy[] Enemys;
   public Vector[][] Map;
   public int Width;
   public int Height;
   public int SlotW;
   public int SlotH;
   public static int closeEnsPhase;

   private void AssociateEnemy(int enemyInd) {
      this.Enemys[enemyInd].CurMapSlotX = this.Enemys[enemyInd].cx / this.SlotW;
      this.Enemys[enemyInd].CurMapSlotY = this.Enemys[enemyInd].cy / this.SlotH;
      this.Map[this.Enemys[enemyInd].CurMapSlotX][this.Enemys[enemyInd].CurMapSlotY].addElement(enemyInd);
   }

   public final void AssociateEnemys() {
      for(int i = 0; i < this.Enemys.length; ++i) {
         this.AssociateEnemy(i);
      }

   }

   public final int iterateCloseEnemys(Camera camera, Strategist strategist2, TileMap tileMap, DrawOrderList drawOrderList2, EnemysMapListener enemysMapListener) {
      closeEnsPhase = 10000;
      int selectedEnemyIndex = -1;
      int map;
      int lastSlotX;
      if (this.Width == 1) {
         closeEnsPhase = 10100;
         map = 0;
         lastSlotX = 0;
      } else {
         closeEnsPhase = 10200;
         if (strategist2.cx % this.SlotW < this.SlotW / 2) {
            closeEnsPhase = 10300;
            if ((map = strategist2.cx / this.SlotW - 1) < 0) {
               map += this.Width;
            }

            lastSlotX = map + 1;
         } else {
            closeEnsPhase = 10400;
            lastSlotX = (map = strategist2.cx / this.SlotW) + 1;
         }
      }

      closeEnsPhase = 10500;
      int firstSlotY;
      int lastSlotY;
      if (this.Height == 1) {
         closeEnsPhase = 10600;
         firstSlotY = 0;
         lastSlotY = 0;
      } else if (strategist2.cy % this.SlotH < this.SlotH / 2) {
         closeEnsPhase = 10700;
         if ((firstSlotY = strategist2.cy / this.SlotH - 1) < 0) {
            firstSlotY += this.Height;
         }

         lastSlotY = firstSlotY + 1;
      } else {
         closeEnsPhase = 10800;
         lastSlotY = (firstSlotY = strategist2.cy / this.SlotH) + 1;
      }

      closeEnsPhase = 10900;

      for(int enemyInd2 = map; enemyInd2 <= lastSlotX; ++enemyInd2) {
         closeEnsPhase = enemyInd2 + 11000;
         int enemyInd22 = enemyInd2;
         if (enemyInd2 >= this.Width) {
            enemyInd22 = enemyInd2 % this.Width;
         }

         for(int slotY = firstSlotY; slotY <= lastSlotY; ++slotY) {
            closeEnsPhase = slotY + 12000;
            int height = slotY;
            if (slotY >= this.Height) {
               height = slotY % this.Height;
            }

            closeEnsPhase = slotY + 13000;

            for(int map2 = this.Map[enemyInd22][height].size() - 1; map2 >= 0; --map2) {
               closeEnsPhase = slotY + 14000;
               map = (Integer)this.Map[enemyInd22][height].elementAt(map2);
               closeEnsPhase = slotY + 15000;
               Enemy enemy2;
               if ((enemy2 = this.Enemys[map]).Enable) {
                  closeEnsPhase = slotY + 16000;
                  if (camera.isPointVisible(enemy2.cx, enemy2.cy, 11, 36)) {
                     closeEnsPhase = slotY + 18000;
                     drawOrderList2.addAndSortFromEnd2(DrawOrderElementEnemy.getInstance(enemy2.cx, enemy2.cy, enemy2.cy, (short)map));
                     enemy2.Drawed = true;
                  } else {
                     enemy2.Drawed = false;
                  }

                  closeEnsPhase = slotY + 19000;
                  if (enemy2.iteration(camera, strategist2, tileMap)) {
                     closeEnsPhase = slotY + 20000;
                     if (ClientState.instance.get() == 4) {
                        selectedEnemyIndex = map;
                     }
                  }

                  closeEnsPhase = slotY + 21000;
                  int newSlotX = enemy2.cx / this.SlotW;
                  int newSlotY = enemy2.cy / this.SlotH;
                  closeEnsPhase = slotY + 22000;
                  if (newSlotX != enemy2.CurMapSlotX || newSlotY != enemy2.CurMapSlotY) {
                     closeEnsPhase = slotY + 23000;
                     this.AssociateEnemy(map);
                     this.Map[enemyInd22][height].removeElementAt(map2);
                  }

                  enemysMapListener.processEnemy(map, enemy2);
               }
            }
         }
      }

      return selectedEnemyIndex;
   }
}
