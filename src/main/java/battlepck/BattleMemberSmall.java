package battlepck;

import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public final class BattleMemberSmall {
   public Battle hisBattle;
   public int id;
   public int side;
   public int atackSprite;
   public Hashtable spawnedMobsByItemType = new Hashtable();
   public int[] gpEffects = new int[20];
   public int rage;
   public int rageTimes = 0;
   public int stopRageTime;
   public Vector stopedItems = new Vector();
   public int startCoordX;
   public int startCoordY;

   public BattleMemberSmall(Battle battle2, DataInputStream dataInputStream) {
      this.hisBattle = battle2;
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }

   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.id = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.side = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.atackSprite = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.startCoordX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.startCoordY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      int integerCode;
      for(integerCode = 0; integerCode < this.gpEffects.length; ++integerCode) {
         this.gpEffects[integerCode] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      }

      this.spawnedMobsByItemType.clear();
      integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      int integerCode2;
      int integerCode2Index;
      for(int integerCodeIndex = 0; integerCodeIndex < integerCode; ++integerCodeIndex) {
         Integer integer = new Integer(decompiledsupport.UncheckedDataInput.readInt(dataInputStream));
         Vector vector3 = new Vector();
         this.spawnedMobsByItemType.put(integer, vector3);
         integerCode2 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

         for(integerCode2Index = 0; integerCode2Index < integerCode2; ++integerCode2Index) {
            vector3.addElement(new Integer(decompiledsupport.UncheckedDataInput.readInt(dataInputStream)));
         }
      }

      this.rage = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.rageTimes = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.stopRageTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);

      for(integerCode2Index = 0; integerCode2Index < shortCode; ++integerCode2Index) {
         int[] arr = new int[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

         for(integerCode2 = 0; integerCode2 < arr.length; ++integerCode2) {
            arr[integerCode2] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
         }

         this.stopedItems.addElement(arr);
      }

   }

   public final BattleMemberSmall clone(Battle battle2) {
      BattleMemberSmall battleMemberSmall;
      (battleMemberSmall = new BattleMemberSmall(battle2, (DataInputStream)null)).id = this.id;
      battleMemberSmall.side = this.side;
      battleMemberSmall.atackSprite = this.atackSprite;
      battleMemberSmall.startCoordX = this.startCoordX;
      battleMemberSmall.startCoordY = this.startCoordY;
      System.arraycopy(this.gpEffects, 0, battleMemberSmall.gpEffects, 0, this.gpEffects.length);
      Enumeration enumeration = this.spawnedMobsByItemType.keys();

      while(enumeration.hasMoreElements()) {
         Integer integer = (Integer)enumeration.nextElement();
         Vector vector3 = (Vector)this.spawnedMobsByItemType.get(integer);
         Vector vector4 = new Vector();
         battleMemberSmall.spawnedMobsByItemType.put(integer, vector4);
         Enumeration enumeration2 = vector3.elements();

         while(enumeration2.hasMoreElements()) {
            vector4.addElement(enumeration2.nextElement());
         }
      }

      battleMemberSmall.rage = this.rage;
      battleMemberSmall.rageTimes = this.rageTimes;
      battleMemberSmall.stopRageTime = this.stopRageTime;

      for(int i = 0; i < this.stopedItems.size(); ++i) {
         int[] integers;
         int[] integers2 = new int[(integers = (int[])this.stopedItems.elementAt(i)).length];
         System.arraycopy(integers, 0, integers2, 0, integers2.length);
         battleMemberSmall.stopedItems.addElement(integers2);
      }

      return battleMemberSmall;
   }

   public final int getRageLevel() {
      int rageLevel = 0;

      for(int c_rage_LevelIndex = 0; c_rage_LevelIndex < Battle.c_rage_Levels.length; ++c_rage_LevelIndex) {
         if (this.rage >= Battle.c_rage_Levels[c_rage_LevelIndex]) {
            rageLevel = c_rage_LevelIndex;
         }
      }

      return rageLevel;
   }

   public final int changeRage(int delta) {
      this.rage += delta;
      if (this.rage < 0) {
         this.rage = 0;
      }

      int[] c_rage_Levels2 = Battle.c_rage_Levels;
      if (this.rage > c_rage_Levels2[c_rage_Levels2.length - 1]) {
         c_rage_Levels2 = Battle.c_rage_Levels;
         this.rage = c_rage_Levels2[c_rage_Levels2.length - 1];
      }

      return this.rage;
   }
}
