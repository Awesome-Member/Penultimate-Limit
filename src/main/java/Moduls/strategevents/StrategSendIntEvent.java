package Moduls.strategevents;

import Moduls.Strategist;
import java.io.DataInputStream;
import java.io.IOException;

public final class StrategSendIntEvent extends StrategSendEvent {
   private int content;

   protected StrategSendIntEvent(byte elementId) {
      super(elementId);
   }

   public final void loadFromStream(DataInputStream dataInputStream) {
      this.content = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void applyToStrategist() {
      switch (this.elementId) {
         case -117:
            Strategist.instance.GP_Money = this.content;
            return;
         case -116:
            Strategist.instance.GP_Exp = this.content;
            return;
         case -115:
         case -114:
         case -112:
         case -111:
         case -109:
         case -108:
         case -104:
         case -103:
         case -102:
         case -101:
         case -100:
         case -86:
         case -83:
         case -82:
         case -80:
         case -79:
         case -77:
         case -76:
         case -75:
         case -74:
         case -73:
         case -72:
         case -71:
         default:
            throw decompiledsupport.UncheckedDataInput.unchecked(new IOException("unknown elementId in applyToStrategStream in strateg events " + this.elementId));
         case -113:
            Strategist.instance.GP_Crystals = this.content;
            return;
         case -110:
            Strategist.instance.GP_NextLevelExp = this.content;
            return;
         case -107:
            Strategist.instance.GP_FireLevel = this.content;
            return;
         case -106:
            Strategist.instance.GP_IceLevel = this.content;
            return;
         case -105:
            Strategist.instance.GP_HeroLevel = this.content;
            return;
         case -99:
            Strategist.instance.buyedBagSize = this.content;
            return;
         case -98:
            Strategist.instance.avatarLife = this.content;
            return;
         case -97:
            Strategist.instance.avatarHit = this.content;
            return;
         case -96:
            Strategist.instance.avatarSpeed = this.content;
            return;
         case -95:
            Strategist.instance.avatarDmg = this.content;
            return;
         case -94:
            Strategist.instance.avatarLifeCur = this.content;
            return;
         case -93:
            Strategist.instance.avatarHitCur = this.content;
            return;
         case -92:
            Strategist.instance.avatarSpeedCur = this.content;
            return;
         case -91:
            Strategist.instance.avatarDmgCur = this.content;
            return;
         case -90:
            Strategist.instance.avatarArmor = this.content;
            return;
         case -89:
            Strategist.instance.avatarAtack = this.content;
            return;
         case -88:
            Strategist.instance.avatarWalkSpeed = this.content;
            return;
         case -87:
            Strategist.instance.maxFireLvl = this.content;
            return;
         case -85:
            Strategist.instance.GP_Life = this.content;
            return;
         case -84:
            Strategist.instance.avatarRegeneration = this.content;
            return;
         case -81:
            Strategist.instance.partyId = this.content;
            return;
         case -78:
            Strategist.instance.maxIceLvl = this.content;
            return;
         case -70:
            Strategist.instance.avatarAtackDistance = this.content;
      }
   }
}
