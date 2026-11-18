package battlepck;

import java.io.DataInputStream;

public final class BottleEffect {
   public int bushIndex;
   public int hitPower;
   public int burnPower;
   public int freezPower;
   public int punchPower;
   public int range;
   public int distance;
   public int timeOfBulletFlight = 300;
   public boolean enemys;
   public boolean friends;
   public boolean self;
   public boolean appEnemys;
   public boolean appFriends;
   public boolean appSelf;
   public boolean pushFromIniter = false;
   public boolean appArmorAndShip = false;
   public int changeDamage;
   public int changeHealth;
   public int changeAtack;
   public int changeDefence;
   public int changeSpeed;
   public int changeMove;
   public int changeRange;
   public int changeDamageIncForNearFriendsPerc = 0;
   public int changeDamageDecForNearEnemysPerc = 0;
   public int changeAnswerPowerPerc = 0;
   public int acidPower;
   public int regenPower;
   public int removesFreezPower = 0;
   public int removesBurnPower = 0;
   public int removesAcidPower = 0;
   public int flyToPoint = 0;
   public int flySelfToPoint = 0;
   public int flySelfToPointApplyTimeOffset = 0;
   public boolean appGround = false;
   public int vampiricPower = 0;
   public int standTime = 0;
   public int desarmTime = 0;
   public int protectionPower = 0;
   public int protectionTime = 0;
   public BattleUnit spawnUnit = null;
   public int spawnMonsterMaxCount = 3;
   public BattleUnit transformToUnit = null;
   public int transformToMonsterTime = 0;
   public int rageStop = 0;
   public int rageChange = 0;
   public int flyStop = 0;
   public int[] itemsStopIds = new int[0];
   public int itemsStopTime = 0;

   public BottleEffect(DataInputStream dataInputStream) {
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.bushIndex = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.hitPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.burnPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.freezPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.punchPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.range = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.distance = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.timeOfBulletFlight = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.enemys = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.friends = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.self = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.appEnemys = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.appFriends = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.appSelf = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.pushFromIniter = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.appArmorAndShip = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.changeDamage = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeHealth = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeAtack = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeDefence = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeSpeed = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeMove = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeRange = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeDamageIncForNearFriendsPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeDamageDecForNearEnemysPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeAnswerPowerPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.acidPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.regenPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.removesFreezPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.removesBurnPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.removesAcidPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.flyToPoint = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.flySelfToPoint = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.flySelfToPointApplyTimeOffset = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.appGround = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.vampiricPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.standTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.desarmTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.protectionPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.protectionTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.spawnMonsterMaxCount = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.spawnUnit = new BattleUnit(dataInputStream);
      } else {
         this.spawnUnit = null;
      }

      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.transformToUnit = new BattleUnit(dataInputStream);
      } else {
         this.transformToUnit = null;
      }

      this.transformToMonsterTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rageStop = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rageChange = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.flyStop = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.itemsStopIds = new int[decompiledsupport.UncheckedDataInput.readInt(dataInputStream)];

      for(int i = 0; i < this.itemsStopIds.length; ++i) {
         this.itemsStopIds[i] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

      this.itemsStopTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final BottleEffect cloneTo() {
      BottleEffect bottleEffect2;
      (bottleEffect2 = new BottleEffect((DataInputStream)null)).bushIndex = this.bushIndex;
      bottleEffect2.hitPower = this.hitPower;
      bottleEffect2.burnPower = this.burnPower;
      bottleEffect2.freezPower = this.freezPower;
      bottleEffect2.punchPower = this.punchPower;
      bottleEffect2.range = this.range;
      bottleEffect2.distance = this.distance;
      bottleEffect2.timeOfBulletFlight = this.timeOfBulletFlight;
      bottleEffect2.enemys = this.enemys;
      bottleEffect2.friends = this.friends;
      bottleEffect2.self = this.self;
      bottleEffect2.appEnemys = this.appEnemys;
      bottleEffect2.appFriends = this.appFriends;
      bottleEffect2.appSelf = this.appSelf;
      bottleEffect2.pushFromIniter = this.pushFromIniter;
      bottleEffect2.appArmorAndShip = this.appArmorAndShip;
      bottleEffect2.changeDamage = this.changeDamage;
      bottleEffect2.changeHealth = this.changeHealth;
      bottleEffect2.changeAtack = this.changeAtack;
      bottleEffect2.changeDefence = this.changeDefence;
      bottleEffect2.changeSpeed = this.changeSpeed;
      bottleEffect2.changeMove = this.changeMove;
      bottleEffect2.changeRange = this.changeRange;
      bottleEffect2.changeDamageIncForNearFriendsPerc = this.changeDamageIncForNearFriendsPerc;
      bottleEffect2.changeDamageDecForNearEnemysPerc = this.changeDamageDecForNearEnemysPerc;
      bottleEffect2.changeAnswerPowerPerc = this.changeAnswerPowerPerc;
      bottleEffect2.acidPower = this.acidPower;
      bottleEffect2.regenPower = this.regenPower;
      bottleEffect2.removesFreezPower = this.removesFreezPower;
      bottleEffect2.removesBurnPower = this.removesBurnPower;
      bottleEffect2.removesAcidPower = this.removesAcidPower;
      bottleEffect2.flyToPoint = this.flyToPoint;
      bottleEffect2.flySelfToPoint = this.flySelfToPoint;
      bottleEffect2.flySelfToPointApplyTimeOffset = this.flySelfToPointApplyTimeOffset;
      bottleEffect2.appGround = this.appGround;
      bottleEffect2.vampiricPower = this.vampiricPower;
      bottleEffect2.standTime = this.standTime;
      bottleEffect2.desarmTime = this.desarmTime;
      bottleEffect2.protectionPower = this.protectionPower;
      bottleEffect2.protectionTime = this.protectionTime;
      bottleEffect2.spawnMonsterMaxCount = this.spawnMonsterMaxCount;
      bottleEffect2.spawnUnit = this.spawnUnit == null ? null : this.spawnUnit.cloneTo(new BattleUnit((DataInputStream)null));
      bottleEffect2.transformToUnit = this.transformToUnit == null ? null : this.transformToUnit.cloneTo(new BattleUnit((DataInputStream)null));
      bottleEffect2.transformToMonsterTime = this.transformToMonsterTime;
      bottleEffect2.rageStop = this.rageStop;
      bottleEffect2.rageChange = this.rageChange;
      bottleEffect2.flyStop = this.flyStop;
      bottleEffect2.itemsStopIds = new int[this.itemsStopIds.length];
      System.arraycopy(this.itemsStopIds, 0, bottleEffect2.itemsStopIds, 0, bottleEffect2.itemsStopIds.length);
      bottleEffect2.itemsStopTime = this.itemsStopTime;
      return bottleEffect2;
   }
}
