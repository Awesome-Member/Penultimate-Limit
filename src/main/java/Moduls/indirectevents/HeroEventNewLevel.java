package Moduls.indirectevents;

import Base.GameCommons;
import desktop.PlatformRuntime;
import java.io.DataInputStream;

public final class HeroEventNewLevel extends HeroEvent {
   private boolean isRateGame;
   private boolean sugestRate;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.sugestRate = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.isRateGame = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
   }

   public final void apply() {
      if (this.sugestRate) {
         PlatformRuntime.getInstance();
         GameCommons.instance.startShareMenu();
      }

   }

   public final void afterApply() {
   }
}
