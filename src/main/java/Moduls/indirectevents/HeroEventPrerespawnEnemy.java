package Moduls.indirectevents;

import Base.Com;
import java.io.DataInputStream;

public final class HeroEventPrerespawnEnemy extends HeroEvent {
   private String mapName;
   private int enemyInd;
   private int coordX;
   private int coordY;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.mapName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.enemyInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.coordX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.coordY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void apply() {
      if (Com.currentGameMap.nameId.equals(this.mapName)) {
         Com.EnsMap.Enemys[this.enemyInd].Enable = true;
         Com.EnsMap.Enemys[this.enemyInd].Atacking = false;
         Com.EnsMap.Enemys[this.enemyInd].cx = (short)this.coordX;
         Com.EnsMap.Enemys[this.enemyInd].cy = (short)this.coordY;
         Com.EnsMap.Enemys[this.enemyInd].reinitParams(this.coordX, this.coordY);
         Com.EnsMap.Enemys[this.enemyInd].beginRespaun();
      }
   }

   public final void afterApply() {
   }
}
