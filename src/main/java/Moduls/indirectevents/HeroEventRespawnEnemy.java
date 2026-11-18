package Moduls.indirectevents;

import Base.Com;
import Moduls.Enemy;
import java.io.DataInputStream;

public final class HeroEventRespawnEnemy extends HeroEvent {
   private String mapName;
   private int enemyInd;
   private Enemy enemy;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.mapName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.enemyInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.enemy = new Enemy();
      this.enemy.LoadFromStream(dataInputStream, false, false);
   }

   public final void apply() {
      if (Com.currentGameMap.nameId.equals(this.mapName)) {
         Enemy enemy2;
         Enemy enemy3 = enemy2 = Com.EnsMap.Enemys[this.enemyInd];
         Enemy enemy4 = this.enemy;
         Enemy enemy5 = enemy3;
         enemy3.nameId = enemy4.nameId;
         enemy5.Name = enemy4.Name;
         enemy5.GPFile = enemy4.GPFile;
         enemy5.Atacking = enemy4.Atacking;
         enemy5.BodyInd = enemy4.BodyInd;
         enemy5.HeadInd = enemy4.HeadInd;
         enemy5.Agression = enemy4.Agression;
         enemy5.gameID = enemy4.gameID;
         enemy5.level = enemy4.level;
         enemy5.Enable = enemy4.Enable;
         enemy5.respaunTime = enemy4.respaunTime;
         enemy5.fightable = enemy4.fightable;
         enemy2.startRespawnEffect();
      }
   }

   public final void afterApply() {
   }
}
