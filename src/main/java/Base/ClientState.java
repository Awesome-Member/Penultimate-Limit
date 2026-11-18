package Base;

import Moduls.chat.Chat;

public final class ClientState {
   private int currentState = 0;
   private int prevState = -1;
   public static ClientState instance = new ClientState();

   private ClientState() {
   }

   private static void applyStateTransition(int previousGameState, int currentGameState) {
      switch (previousGameState) {
         case 4:
            if (currentGameState != 4) {
               Com.stopGlobalMap(previousGameState * 100 + currentGameState);
               Com.GameTh.setMyPathActive2();
            }
         default:
            switch (currentGameState) {
               case 4:
                  Com.GameTh.myPathReset();
                  Com.resumeGlobalMap();
                  Com.GameTh.setMyPathActive();
                  return;
               case 7:
               case 8:
                  Chat.chatWindow.diactivate();
                  Com.textWrite.totalExit();
               case 5:
               case 6:
               default:
            }
      }
   }

   public final void revert() {
      if (this.prevState >= 0) {
         this.set(this.prevState);
         this.prevState = -1;
      }

   }

   public final void set(int newGameState) {
      if (newGameState != this.currentState) {
         int currentState2 = this.currentState;
         this.prevState = currentState2;
         this.currentState = newGameState;
         applyStateTransition(currentState2, newGameState);
      }
   }

   public final int get() {
      return this.currentState;
   }
}
