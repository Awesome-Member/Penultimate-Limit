package Moduls.indirectevents;

import Base.Com;
import Base.GameEffects;
import java.io.DataInputStream;

public final class HeroEventPartyKillMobs extends HeroEvent {
   private String[] maps;
   private int[] inds;

   public final void loadFromStream(DataInputStream dataInputStream) {
      int integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.maps = new String[integerCode];
      this.inds = new int[integerCode];

      for(int mapIndex = 0; mapIndex < integerCode; ++mapIndex) {
         this.maps[mapIndex] = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
         this.inds[mapIndex] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

   }

   public final void apply() {
      for(int indIndex = 0; indIndex < this.maps.length; ++indIndex) {
         int inds2 = this.inds[indIndex];
         if (Com.currentGameMap.nameId.equals(this.maps[indIndex])) {
            Com.EnsMap.Enemys[inds2].Enable = false;
            GameEffects instance2 = GameEffects.instance;
            GameEffects.spawnClientEffect4(Com.EnsMap.Enemys[inds2].cx, Com.EnsMap.Enemys[inds2].cy);
         }
      }

   }

   public final void afterApply() {
   }
}
