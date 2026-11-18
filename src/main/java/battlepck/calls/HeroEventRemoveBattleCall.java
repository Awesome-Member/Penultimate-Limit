package battlepck.calls;

import Base.GameCommons;
import Engine.Mystery;
import Moduls.indirectevents.HeroEvent;
import java.io.DataInputStream;

public final class HeroEventRemoveBattleCall extends HeroEvent {
   private boolean withNotify;
   private int withPlayerId;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.withNotify = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.withPlayerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void apply() {
      BattleCall battleCall = BattleCallsHero.instance.removeBattleCall(this.withPlayerId);
      if (this.withNotify && battleCall != null) {
         GameCommons.instance.showFloatText(Mystery.stringFormat("Дуэль отклонена\n%", battleCall.withPlayerName));
      }

   }

   public final void afterApply() {
   }
}
