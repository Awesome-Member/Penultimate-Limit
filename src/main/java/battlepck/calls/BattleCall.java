package battlepck.calls;

import Engine.Mystery;

public final class BattleCall {
   public boolean initiator;
   public int withPlayerId;
   public String withPlayerName;
   private long sinceTime;

   public BattleCall(boolean initiator2, int withPlayerId2, String withPlayerName2) {
      this.initiator = initiator2;
      this.withPlayerId = withPlayerId2;
      this.withPlayerName = withPlayerName2;
      this.sinceTime = Mystery.currentTimeMillis;
   }

   public final String getTitleForFastInfo() {
      return (Mystery.currentTimeMillis - this.sinceTime < 1800L && (Mystery.currentTimeMillis - this.sinceTime) % 600L >= 300L ? "  " : "Ͱͱ") + this.withPlayerName;
   }
}
