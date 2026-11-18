package Base;

import Moduls.Strategist;
import Moduls.Enemy;
import Moduls.EnemysMapListener;
import Moduls.otherheroes.HeroOnMap;

public final class GameControllAnalogData implements EnemysMapListener {
   public boolean checkMoveToNextMap = false;
   public long lastActivateOtherPlayers = 0L;
   public long lastCheckPartyJoinTime = 0L;
   public boolean[] playerActiveMoves = new boolean[8];
   public int playerActiveMoveDirectResult = 8;
   public int activeForAtackPlayerId = -1;
   public HeroOnMap activeForAtackPlayerObj = null;
   public int closestEnemyForAtack = -1;
   public long closestEnemyForAtackDistance = -1L;

   public final void disableAtackToPlayer() {
      this.activeForAtackPlayerId = -1;
      this.activeForAtackPlayerObj = null;
   }

   public final void disableControll() {
      for(int i = 0; i < this.playerActiveMoves.length; ++i) {
         this.playerActiveMoves[i] = false;
      }

      this.playerActiveMoveDirectResult = 8;
   }

   public final void initMoveDirectionResult() {
      this.playerActiveMoveDirectResult = 8;
      if (this.playerActiveMoves[7] && this.playerActiveMoves[1]) {
         this.playerActiveMoveDirectResult = 0;
      } else if (this.playerActiveMoves[1] && this.playerActiveMoves[3]) {
         this.playerActiveMoveDirectResult = 2;
      } else if (this.playerActiveMoves[3] && this.playerActiveMoves[5]) {
         this.playerActiveMoveDirectResult = 4;
      } else if (this.playerActiveMoves[5] && this.playerActiveMoves[7]) {
         this.playerActiveMoveDirectResult = 6;
      } else {
         for(int i = 0; i < 8; ++i) {
            if (this.playerActiveMoves[i]) {
               this.playerActiveMoveDirectResult = i;
            }
         }

      }
   }

   public final void processEnemy(int ind, Enemy enemy2) {
      int deltaX = Strategist.instance.cx - enemy2.cx;
      int deltaY = Strategist.instance.cy - enemy2.cy;
      long distance;
      if ((distance = (long)(deltaX * deltaX + deltaY * deltaY)) <= (long)(Strategist.instance.maxIceLvl * Strategist.instance.maxIceLvl) && enemy2.isFightiable() && (this.closestEnemyForAtack < 0 || distance < this.closestEnemyForAtackDistance)) {
         this.closestEnemyForAtack = ind;
         this.closestEnemyForAtackDistance = distance;
      }

   }
}
