package battlepck.calls;

import java.util.Vector;

public final class BattleCallsHero {
   public static final BattleCallsHero instance = new BattleCallsHero();
   private final Vector calls = new Vector();

   private BattleCallsHero() {
   }

   public final void reset() {
      this.calls.removeAllElements();
   }

   public final boolean containsBattleCall(int withPlayerId) {
      for(int i = 0; i < this.calls.size(); ++i) {
         if (((BattleCall)this.calls.elementAt(i)).withPlayerId == withPlayerId) {
            return true;
         }
      }

      return false;
   }

   public final Vector getCalls() {
      return this.calls;
   }

   public final int getCallsCount() {
      return this.calls.size();
   }

   public final void addBattleCall(BattleCall battleCall) {
      for(int i = 0; i < this.calls.size(); ++i) {
         if (((BattleCall)this.calls.elementAt(i)).withPlayerId == battleCall.withPlayerId) {
            this.calls.removeElementAt(i);
            --i;
         }
      }

      this.calls.insertElementAt(battleCall, 0);
   }

   public final BattleCall removeBattleCall(int withPlayerId) {
      BattleCall battleCall = null;

      for(int i = 0; i < this.calls.size(); ++i) {
         if (((BattleCall)this.calls.elementAt(i)).withPlayerId == withPlayerId) {
            battleCall = (BattleCall)this.calls.elementAt(i);
            this.calls.removeElementAt(i);
            --i;
         }
      }

      return battleCall;
   }
}
