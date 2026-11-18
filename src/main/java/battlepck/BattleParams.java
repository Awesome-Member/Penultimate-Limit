package battlepck;

public final class BattleParams {
   public int[] speedMsPix2P = new int[2];
   public int[] speedCleanMsPix2P = new int[2];
   public int hitPower0P = 0;
   public int timeBetweenHitsMs = 0;
   public int hitRangePix = 0;
   public int damageIncForNearFriendsPerc = 0;
   public int damageDecForNearEnemysPerc = 0;
   public int answerPowerPerc = 0;
   public int pushPowerPix = 0;
   public int pullPowerPix = 0;
   public int atackValue = 0;
   public int defenceValue = 0;
   public int poisonPower0P = 0;
   public int paralizeTimeMs = 0;
   public boolean isArcher = false;
   public boolean isJump = false;
   public boolean isInvisible = false;
   public boolean isFighter = true;

   public final BattleParams cloneTo() {
      BattleParams battleParams;
      (battleParams = new BattleParams()).speedMsPix2P[0] = this.speedMsPix2P[0];
      battleParams.speedMsPix2P[1] = this.speedMsPix2P[1];
      battleParams.speedCleanMsPix2P[0] = this.speedCleanMsPix2P[0];
      battleParams.speedCleanMsPix2P[1] = this.speedCleanMsPix2P[1];
      battleParams.hitPower0P = this.hitPower0P;
      battleParams.timeBetweenHitsMs = this.timeBetweenHitsMs;
      battleParams.hitRangePix = this.hitRangePix;
      battleParams.damageIncForNearFriendsPerc = this.damageIncForNearFriendsPerc;
      battleParams.damageDecForNearEnemysPerc = this.damageDecForNearEnemysPerc;
      battleParams.answerPowerPerc = this.answerPowerPerc;
      battleParams.pushPowerPix = this.pushPowerPix;
      battleParams.pullPowerPix = this.pullPowerPix;
      battleParams.atackValue = this.atackValue;
      battleParams.defenceValue = this.defenceValue;
      battleParams.poisonPower0P = this.poisonPower0P;
      battleParams.paralizeTimeMs = this.paralizeTimeMs;
      battleParams.isArcher = this.isArcher;
      battleParams.isJump = this.isJump;
      battleParams.isInvisible = this.isInvisible;
      battleParams.isFighter = this.isFighter;
      return battleParams;
   }
}
