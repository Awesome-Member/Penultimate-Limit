package Base;

import Engine.MathGame;
import Moduls.PathFolower;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.Enemy;
import Moduls.EnemysMapListener;
import Moduls.otherheroes.HeroOnMap;

public final class GameControllDirectData implements EnemysMapListener {
   public GameSelectionStructure selections = null;
   public GameSelectionStructure moveTarget = null;
   public int moveTargetLastMovingX = 0;
   public int moveTargetLastMovingY = 0;
   public boolean moveTargetPeacefull = false;
   private boolean moveTargetIsAchivable = false;
   public long lastCheckPartyJoinTime = 0L;
   private int waitForJointToPlayerProcess = -1;
   private int waitForAtackToPlayerProcess = -1;
   public short[][] curPathFindingKeys = new short[0][];
   public PathFolower pathFolower = new PathFolower();
   public int prevStrategCoordX = -1;
   public int prevStrategCoordY = -1;

   public final void processEnemy(int ind, Enemy enemy2) {
      if (this.moveTarget != null && ind == this.moveTarget.selectedEnemyInd && (this.pathFolower.isMovePathAbsent() && this.moveTargetIsAchivable || MathGame.floorDivide((long)this.moveTargetLastMovingX, (long)Tile.width) != MathGame.floorDivide((long)enemy2.cx, (long)Tile.width) || MathGame.floorDivide((long)this.moveTargetLastMovingY, (long)Tile.height) != MathGame.floorDivide((long)enemy2.cy, (long)Tile.height))) {
         this.buildPath(enemy2.cx, enemy2.cy);
         this.moveTargetLastMovingX = enemy2.cx;
         this.moveTargetLastMovingY = enemy2.cy;
      }

   }

   public final void processStrategOnMap(HeroOnMap heroOnMap) {
      if (this.waitForJointToPlayerProcess >= 0 && heroOnMap.id == this.waitForJointToPlayerProcess) {
         this.disableControll();
         this.moveTarget = new GameSelectionStructure();
         this.moveTarget.selectedOtherStrategObj = heroOnMap;
         this.moveTarget.selectedOtherStrategId = heroOnMap.id;
         this.moveTargetPeacefull = true;
         this.moveTargetLastMovingX = heroOnMap.cx;
         this.moveTargetLastMovingY = heroOnMap.cy;
         this.buildPath((short)this.moveTargetLastMovingX, (short)this.moveTargetLastMovingY);
      }

      if (this.waitForAtackToPlayerProcess >= 0 && heroOnMap.id == this.waitForAtackToPlayerProcess) {
         this.disableControll();
         this.moveTarget = new GameSelectionStructure();
         this.moveTarget.selectedOtherStrategObj = heroOnMap;
         this.moveTarget.selectedOtherStrategId = heroOnMap.id;
         this.moveTargetPeacefull = false;
         this.moveTargetLastMovingX = heroOnMap.cx;
         this.moveTargetLastMovingY = heroOnMap.cy;
         this.buildPath((short)this.moveTargetLastMovingX, (short)this.moveTargetLastMovingY);
      }

      if (this.selections != null && this.selections.selectedOtherStrategId == heroOnMap.id) {
         this.selections.selectedOtherStrategObj = heroOnMap;
      }

      if (this.moveTarget != null && this.moveTarget.selectedOtherStrategId == heroOnMap.id) {
         this.moveTarget.selectedOtherStrategObj = heroOnMap;
         if (this.pathFolower.isMovePathAbsent() && (this.moveTargetLastMovingX != heroOnMap.cx || this.moveTargetLastMovingY != heroOnMap.cy)) {
            this.buildPath((short)heroOnMap.cx, (short)heroOnMap.cy);
            this.moveTargetLastMovingX = heroOnMap.cx;
            this.moveTargetLastMovingY = heroOnMap.cy;
            return;
         }

         if (MathGame.floorDivide((long)this.moveTargetLastMovingX, (long)Tile.width) != MathGame.floorDivide((long)heroOnMap.cx, (long)Tile.width) || MathGame.floorDivide((long)this.moveTargetLastMovingY, (long)Tile.height) != MathGame.floorDivide((long)heroOnMap.cy, (long)Tile.height)) {
            this.buildPath((short)heroOnMap.cx, (short)heroOnMap.cy);
            this.moveTargetLastMovingX = heroOnMap.cx;
            this.moveTargetLastMovingY = heroOnMap.cy;
         }
      }

   }

   public final void disableControll() {
      this.pathFolower.disableMovePath();
      this.moveTarget = null;
      this.waitForJointToPlayerProcess = -1;
      this.waitForAtackToPlayerProcess = -1;
   }

   public final void activateAtackToPlayer(int playerId) {
      this.disableControll();
      this.waitForAtackToPlayerProcess = playerId;
   }

   public final void activateJoinToPlayer(int playerId) {
      this.disableControll();
      this.waitForJointToPlayerProcess = playerId;
   }

   public final void buildPath(short toX, short toY) {
      this.moveTargetIsAchivable = this.pathFolower.buildPath(Com.currentGameMap.pathfindingGrid, Com.currentGameMap, Strategist.instance.cx, Strategist.instance.cy, toX, toY, Strategist.instance.cdW, Strategist.instance.cdH, (short)Tile.width, (short)Tile.height);
      this.curPathFindingKeys = new short[][]{{Strategist.instance.cx, Strategist.instance.cy}, {toX, toY}};
   }

   public static void updateSelection(GameSelectionStructure gameSelectionStructure, GameSelectionStructure gameSelectionStructure2) {
      gameSelectionStructure.reset();
      if (gameSelectionStructure2.selectedTown >= 0) {
         gameSelectionStructure.selectedTown = gameSelectionStructure2.selectedTown;
      } else if (gameSelectionStructure2.selectedMapMove >= 0) {
         gameSelectionStructure.selectedMapMove = gameSelectionStructure2.selectedMapMove;
         gameSelectionStructure.selectedMapMoveX = gameSelectionStructure2.selectedMapMoveX;
         gameSelectionStructure.selectedMapMoveY = gameSelectionStructure2.selectedMapMoveY;
      } else if (gameSelectionStructure2.selectedEnemyInd >= 0) {
         gameSelectionStructure.selectedEnemyInd = gameSelectionStructure2.selectedEnemyInd;
      } else if (gameSelectionStructure2.selectedOtherStrategId >= 0) {
         gameSelectionStructure.selectedOtherStrategId = gameSelectionStructure2.selectedOtherStrategId;
         gameSelectionStructure.selectedOtherStrategObj = gameSelectionStructure2.selectedOtherStrategObj;
      } else {
         if (gameSelectionStructure2.selectStrateg) {
            gameSelectionStructure.selectStrateg = true;
         }

      }
   }
}
