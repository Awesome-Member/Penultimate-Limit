package Base;

import Moduls.Enemy;
import Moduls.otherheroes.HeroOnMap;

public final class GameCrossingsWorldData {
   public int crossWithTownInd = -1;
   public int nextMapMoveDirect = -1;
   public HeroOnMap crossWithStrategForJoin = null;
   public HeroOnMap crossWithStrategFromParty = null;
   public HeroOnMap crossWithStrategForQuest = null;
   public Enemy crossWithEnemy = null;
   public int crossWithEnemyInd = -1;
}
