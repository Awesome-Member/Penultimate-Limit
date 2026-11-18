package battlepck;

import java.io.DataInputStream;

public final class BattleQuantumEvent {
   public final BattleEvent[] battleEvents;

   public BattleQuantumEvent(BattleEvent[] battleEvents2) {
      this.battleEvents = battleEvents2;
   }

   public final BattleQuantumEvent cloneTo() {
      BattleEvent[] battleEventsCount = new BattleEvent[this.battleEvents.length];

      for(int battleEventIndex = 0; battleEventIndex < battleEventsCount.length; ++battleEventIndex) {
         BattleEvent battleEvent = this.battleEvents[battleEventIndex];
         BattleEvent battleEvent2;
         (battleEvent2 = new BattleEvent((DataInputStream)null)).eventType = battleEvent.eventType;
         battleEvent2.setTargetUnitInd = battleEvent.setTargetUnitInd;
         battleEvent2.setTargetTargetInd = battleEvent.setTargetTargetInd;
         battleEvent2.setTargetTargetX = battleEvent.setTargetTargetX;
         battleEvent2.setTargetTargetY = battleEvent.setTargetTargetY;
         if (battleEvent.addPunchPunch == null) {
            battleEvent2.addPunchPunch = null;
         } else {
            battleEvent2.addPunchPunch = battleEvent.addPunchPunch.cloneTo();
         }

         battleEvent2.removeMemberId = battleEvent.removeMemberId;
         if (battleEvent.removeMemberUnits == null) {
            battleEvent2.removeMemberUnits = null;
         } else {
            battleEvent2.removeMemberUnits = new int[battleEvent.removeMemberUnits.length];
            System.arraycopy(battleEvent.removeMemberUnits, 0, battleEvent2.removeMemberUnits, 0, battleEvent.removeMemberUnits.length);
         }

         battleEvent2.addMemberId = battleEvent.addMemberId;
         if (battleEvent.addMemberMember == null) {
            battleEvent2.addMemberMember = null;
         } else {
            battleEvent2.addMemberMember = battleEvent.addMemberMember.clone((Battle)null);
         }

         if (battleEvent.addUnitUnit == null) {
            battleEvent2.addUnitUnit = null;
         } else {
            battleEvent2.addUnitUnit = battleEvent.addUnitUnit.cloneTo(new BattleUnit((DataInputStream)null));
         }

         battleEvent2.addBottleMemberId = battleEvent.addBottleMemberId;
         if (battleEvent.addBottleBottle == null) {
            battleEvent2.addBottleBottle = null;
         } else {
            battleEvent2.addBottleBottle = battleEvent.addBottleBottle.cloneTo();
         }

         battleEventsCount[battleEventIndex] = battleEvent2;
      }

      return new BattleQuantumEvent(battleEventsCount);
   }

   public final void applyEvents(Battle battle2, int phase) {
      for(int i = 0; i < this.battleEvents.length; ++i) {
         byte cooldownChange;
         BattleEvent battleEvent;
         switch ((battleEvent = this.battleEvents[i]).eventType) {
            case 0:
            case 1:
               cooldownChange = 1;
               break;
            case 2:
            case 3:
            case 4:
            case 8:
            case 9:
            case 10:
            case 11:
            default:
               cooldownChange = -1;
               break;
            case 5:
            case 12:
               cooldownChange = 3;
               break;
            case 6:
            case 7:
               cooldownChange = 0;
         }

         if (cooldownChange == phase) {
            battle2.applyEvent(battleEvent);
         }
      }

   }
}
