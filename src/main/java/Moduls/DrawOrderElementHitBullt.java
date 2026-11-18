package Moduls;

import Base.Com;
import Moduls.otherheroes.HeroOnMap;
import Moduls.otherheroes.HeroesOnMapManager;
import battlepck.Battle;
import battlepck.BattleUnit;
import desktop.graphics.GameGraphics;

public final class DrawOrderElementHitBullt extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementHitBullt((short)-1, (short)-1, (short)-1, -1, -1, -1));
   private int heroInd = -1;
   private int enemyInd = -1;
   private int time = -1;

   private DrawOrderElementHitBullt(short rx, short ry, short sortInd, int heroInd2, int enemyInd2, int time2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex) {
         HeroOnMap heroOnMap;
         if ((heroOnMap = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(this.heroInd)) != null) {
            if (this.enemyInd >= 0 && this.enemyInd < Com.EnsMap.Enemys.length) {
               Enemy enemy2 = Com.EnsMap.Enemys[this.enemyInd];
               Enemy enemy3 = enemy2;
               int time2 = this.time;
               int cy2 = heroOnMap.cy;
               int cx2 = heroOnMap.cx;
               if (time2 >= 0 && time2 < 0 + Battle.c_all_apply_atacks_time) {
                  BattleUnit.drawAniBush(gameGraphics2, time2, heroOnMap.fightingAtackMobBushInd, heroOnMap.fightingAtackMobBushBaseX, heroOnMap.fightingAtackMobBushBaseY, heroOnMap.fightingAtackMobBushLatency, cx2, cy2, heroOnMap.watchDirection, enemy3.cx, enemy3.cy, Legionery.getUnitInfoVerticalOffset(0, enemy3.BodyInd, enemy3.HeadInd, (StrategGraphic)null));
               }

               int c_all_apply_atacks_time2 = Battle.c_all_apply_atacks_time;
               if (time2 >= c_all_apply_atacks_time2 && time2 < c_all_apply_atacks_time2 + Battle.c_all_apply_atacks_time) {
                  BattleUnit.drawAniBush(gameGraphics2, time2 - c_all_apply_atacks_time2, enemy3.fightingBushInd, enemy3.fightingBushBaseX, enemy3.fightingBushBaseY, enemy3.fightingBushLatency, enemy3.cx, enemy3.cy, enemy3.direction, cx2, cy2, Legionery.getUnitInfoVerticalOffset(1, -1, -1, heroOnMap.strategGraphic));
               }

            }
         }
      }
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementHitBullt((short)-1, (short)-1, (short)-1, -1, -1, -1);
   }

   public final void returnInstance() {
      factory.returnInstance(this);
   }

   public static DrawOrderElementHitBullt getInstance(short rx, short ry, short sortInd, int heroInd2, int enemyInd2, int time2) {
      DrawOrderElementHitBullt drawOrderElementHitBullt;
      (drawOrderElementHitBullt = (DrawOrderElementHitBullt)factory.getInstance()).rx = rx;
      drawOrderElementHitBullt.ry = ry;
      drawOrderElementHitBullt.sortInd = 32767;
      drawOrderElementHitBullt.heroInd = heroInd2;
      drawOrderElementHitBullt.enemyInd = enemyInd2;
      drawOrderElementHitBullt.time = time2;
      return drawOrderElementHitBullt;
   }
}
