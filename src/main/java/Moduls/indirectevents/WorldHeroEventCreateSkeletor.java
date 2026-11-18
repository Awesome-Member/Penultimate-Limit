package Moduls.indirectevents;

import Base.Com;
import Moduls.TileMapElement;
import java.io.DataInputStream;

public final class WorldHeroEventCreateSkeletor extends HeroEvent {
   private int mapId;
   private short x;
   private short y;
   private short decorId;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.mapId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.x = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.y = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.decorId = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
   }

   public final void apply() {
      if (Com.currentGameMap.serverIndex == this.mapId) {
         Com.currentGameMap.addFreeDecors(new TileMapElement[]{new TileMapElement(this.x, this.y, this.decorId, (short)0)});
      }

   }

   public final void afterApply() {
   }
}
