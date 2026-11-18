package battlepck;

import java.io.DataInputStream;

public final class BattleEvent {
   public byte eventType;
   public int setTargetUnitInd;
   public int setTargetTargetInd;
   public int setTargetTargetX;
   public int setTargetTargetY;
   public BattleDistPunch addPunchPunch = null;
   public int removeMemberId;
   public int[] removeMemberUnits = null;
   public int addMemberId;
   public BattleMemberSmall addMemberMember;
   public BattleUnit addUnitUnit = null;
   public int addBottleMemberId;
   public BattleBottle addBottleBottle = null;

   public BattleEvent(DataInputStream dataInputStream) {
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }

   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.eventType = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      switch (this.eventType) {
         case 0:
            this.setTargetUnitInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.setTargetTargetInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.setTargetTargetX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.setTargetTargetY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            return;
         case 1:
            this.addPunchPunch = new BattleDistPunch(dataInputStream);
            return;
         case 5:
            this.removeMemberId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.removeMemberUnits = new int[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

            for(int i = 0; i < this.removeMemberUnits.length; ++i) {
               this.removeMemberUnits[i] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            }

            return;
         case 6:
            this.addMemberId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.addMemberMember = new BattleMemberSmall((Battle)null, dataInputStream);
            return;
         case 7:
            this.addUnitUnit = new BattleUnit(dataInputStream);
            return;
         case 12:
            this.addBottleMemberId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
            this.addBottleBottle = new BattleBottle(dataInputStream);
         case 2:
         case 3:
         case 4:
         case 8:
         case 9:
         case 10:
         case 11:
         default:
      }
   }
}
