package battlepck;

import java.io.DataInputStream;

public final class BattleDistPunch {
   public int fromItemTypeId = -1;
   public int fromBottleInd = -1;
   public int atackerInd;
   public int targetInd;
   public int startX;
   public int startY;
   public int groundX;
   public int groundY;
   public int gameTime;
   public int rageCost;
   public boolean flyDirect;
   public BottleEffect bottleEffect;

   public BattleDistPunch(DataInputStream dataInputStream) {
      if (dataInputStream != null) {
         this.flyDirect = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
         this.atackerInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.targetInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.startX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.startY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.groundX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.groundY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.gameTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.rageCost = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.fromItemTypeId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         this.fromBottleInd = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
         this.bottleEffect = new BottleEffect(dataInputStream);
      }

   }

   public final BattleDistPunch cloneTo() {
      BattleDistPunch battleDistPunch;
      (battleDistPunch = new BattleDistPunch((DataInputStream)null)).atackerInd = this.atackerInd;
      battleDistPunch.targetInd = this.targetInd;
      battleDistPunch.startX = this.startX;
      battleDistPunch.startY = this.startY;
      battleDistPunch.groundX = this.groundX;
      battleDistPunch.groundY = this.groundY;
      battleDistPunch.gameTime = this.gameTime;
      battleDistPunch.rageCost = this.rageCost;
      battleDistPunch.flyDirect = this.flyDirect;
      battleDistPunch.fromItemTypeId = this.fromItemTypeId;
      battleDistPunch.fromBottleInd = this.fromBottleInd;
      battleDistPunch.bottleEffect = this.bottleEffect.cloneTo();
      return battleDistPunch;
   }

   public final int getGPSpriteInd() {
      return Battle.c_atack_images_inds[this.bottleEffect.bushIndex];
   }

   public final int getHeartSpriteInd() {
      return this.bottleEffect.bushIndex < 0 ? -1 : Battle.c_heart_images_inds[this.bottleEffect.bushIndex];
   }
}
