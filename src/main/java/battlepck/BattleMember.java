package battlepck;

import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class BattleMember {
   private Battle hisBattle;
   private int automation;
   public int id;
   public int side;
   public Vector hisUnits = new Vector();
   public int leftUsesCount;
   public int maxSlotInd = 0;
   public Hashtable bottlesBySlot = new Hashtable();
   public int stepBackType = 0;
   public int avatarFriendsCount = 0;
   public int avatarEnemysCount = 0;

   public BattleMember(Battle battle2, DataInputStream dataInputStream) {
      this.hisBattle = battle2;
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }

   }

   public final boolean isAllowStepBack() {
      return this.stepBackType != 0 && this.hisBattle.getBattleTime() < Battle.c_step_back_allow_time;
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.maxSlotInd = 2;
      this.bottlesBySlot.clear();
      this.automation = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.side = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      int integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      int integerCode2;
      for(integerCode2 = 0; integerCode2 < integerCode; ++integerCode2) {
         this.hisUnits.addElement(new Integer(decompiledsupport.UncheckedDataInput.readInt(dataInputStream)));
      }

      for(integerCode2 = 0; integerCode2 < 20; ++integerCode2) {
         decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

      this.leftUsesCount = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      integerCode2 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      for(integerCode = 0; integerCode < integerCode2; ++integerCode) {
         this.readBottle(new BattleBottle(dataInputStream));
      }

      decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.stepBackType = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);

      for(integerCode2 = 0; integerCode2 < shortCode; ++integerCode2) {
         int[] arr = new int[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

         for(int lengthIndex = 0; lengthIndex < arr.length; ++lengthIndex) {
            arr[lengthIndex] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         }
      }

   }

   public final BattleMember clone(Battle battle2) {
      BattleMember battleMember;
      (battleMember = new BattleMember(battle2, (DataInputStream)null)).id = this.id;
      battleMember.side = this.side;
      battleMember.automation = this.automation;
      battleMember.leftUsesCount = this.leftUsesCount;
      battleMember.maxSlotInd = this.maxSlotInd;

      for(int i = 0; i < this.hisUnits.size(); ++i) {
         battleMember.hisUnits.addElement(this.hisUnits.elementAt(i));
      }

      Enumeration enumeration = this.bottlesBySlot.elements();

      while(enumeration.hasMoreElements()) {
         BattleBottle battleBottle = (BattleBottle)enumeration.nextElement();
         battleMember.bottlesBySlot.put(battleBottle.slotInd, battleBottle.cloneTo());
      }

      battleMember.stepBackType = this.stepBackType;
      return battleMember;
   }

   public final void readBottle(BattleBottle battleBottle) {
      this.maxSlotInd = Math.max(this.maxSlotInd, battleBottle.slotInd);
      this.bottlesBySlot.put(battleBottle.slotInd, battleBottle);
   }
}
