package Moduls;

import Base.Com;
import Moduls.otherheroes.HeroesOnMapManager;
import battlepck.BattleManager;
import battlepck.calls.BattleCallsHero;
import battlepck.client.BattleTh;

public final class PlayerGameState {
   private int state = -1;
   private int prevState = -2;
   public static PlayerGameState instance = new PlayerGameState();

   private PlayerGameState() {
      this.reset();
   }

   public final void reset() {
      this.state = -1;
      this.prevState = -2;
      BattleCallsHero.instance.reset();
   }

   public final void set(int newState) {
      if (newState != this.state) {
         int state2 = this.state;
         this.prevState = state2;
         this.state = newState;
         int newState2 = state2;
         state2 = newState;
         newState = newState2;
         if (state2 == 0) {
            HeroesOnMapManager.instance.markHeroesAsOldTilNextLoad();
         }

         if (state2 == 1 && newState != 1) {
            if (newState == -1) {
               Com.startGameMap(false);
            }

            BattleManager.appearBattle();
         }

         if (state2 == 0 && newState == -1) {
            Com.startGameMap(false);
         }

         if (state2 == 2) {
            if (newState == -1) {
               Com.startGameMap(false);
            }

            BattleTh.finishRealBattle();
         }

      }
   }
}
