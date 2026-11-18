package battlepck.calls;

import Base.GameCommons;
import Engine.Mystery;
import Moduls.indirectevents.HeroEvent;
import java.io.DataInputStream;

public final class HeroEventAddBattleCall extends HeroEvent {
   private boolean withNotify;
   private boolean initiator;
   private int withPlayerId;
   private String withPlayerName;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.withNotify = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.initiator = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.withPlayerId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.withPlayerName = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void apply() {
      BattleCallsHero.instance.addBattleCall(new BattleCall(this.initiator, this.withPlayerId, this.withPlayerName));
      if (this.withNotify) {
         GameCommons.instance.showFloatText(Mystery.stringFormat("Предложение дуэли\n%", this.withPlayerName));
      }

   }

   public final void afterApply() {
   }
}
