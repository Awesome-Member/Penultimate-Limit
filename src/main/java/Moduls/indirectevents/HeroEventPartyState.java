package Moduls.indirectevents;

import Base.GameCommons;
import Moduls.Strategist;
import java.io.DataInputStream;

public final class HeroEventPartyState extends HeroEvent {
   private String stateInfo;
   private byte partyState;
   private int partyId;

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.partyState = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.partyId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.stateInfo = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
   }

   public final void apply() {
      Strategist.instance.partyState = this.partyState;
      Strategist.instance.partyId = this.partyId;
      GameCommons.instance.showFloatText((String)this.stateInfo, 3000);
   }

   public final void afterApply() {
   }
}
