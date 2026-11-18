package battlepck;

import java.io.DataInputStream;
import java.io.IOException;

public final class BattleBottle {
   private static String readUTFUnchecked(DataInputStream input) {
      try { return input.readUTF(); }
      catch (IOException exception) {
         BattleBottle.<RuntimeException>throwUnchecked(exception);
         return null;
      }
   }

   private static int readIntUnchecked(DataInputStream input) {
      try { return input.readInt(); }
      catch (IOException exception) {
         BattleBottle.<RuntimeException>throwUnchecked(exception);
         return 0;
      }
   }

   private static byte readByteUnchecked(DataInputStream input) {
      try { return input.readByte(); }
      catch (IOException exception) {
         BattleBottle.<RuntimeException>throwUnchecked(exception);
         return 0;
      }
   }

   @SuppressWarnings("unchecked")
   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T { throw (T)throwable; }

   public String icon;
   private String name;
   public String descr;
   public int count;
   public BottleEffect effect;
   public int itemTypeId;
   private int wasCount;
   public byte slotInd;
   public int cooldownTimeCur = 0;
   public int cooldownTimeBase = 0;

   public BattleBottle(DataInputStream dataInputStream) {
      if (dataInputStream != null) {
         this.icon = readUTFUnchecked(dataInputStream);
         this.name = readUTFUnchecked(dataInputStream);
         this.descr = readUTFUnchecked(dataInputStream);
         this.count = readIntUnchecked(dataInputStream);
         this.effect = new BottleEffect(dataInputStream);
         this.itemTypeId = readIntUnchecked(dataInputStream);
         this.wasCount = readIntUnchecked(dataInputStream);
         this.slotInd = readByteUnchecked(dataInputStream);
         this.cooldownTimeCur = readIntUnchecked(dataInputStream);
         this.cooldownTimeBase = readIntUnchecked(dataInputStream);
      }

   }

   public final BattleBottle cloneTo() {
      BattleBottle battleBottle;
      (battleBottle = new BattleBottle((DataInputStream)null)).icon = this.icon;
      battleBottle.name = this.name;
      battleBottle.descr = this.descr;
      battleBottle.count = this.count;
      battleBottle.itemTypeId = this.itemTypeId;
      battleBottle.wasCount = this.wasCount;
      battleBottle.slotInd = this.slotInd;
      battleBottle.cooldownTimeCur = this.cooldownTimeCur;
      battleBottle.cooldownTimeBase = this.cooldownTimeBase;
      battleBottle.effect = this.effect.cloneTo();
      return battleBottle;
   }
}
