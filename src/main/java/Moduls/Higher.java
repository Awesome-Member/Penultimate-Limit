package Moduls;

import java.io.DataInputStream;
import java.io.IOException;

public final class Higher {
   private String Name;
   public int BodyInd;
   public int HeadInd;
   private boolean[] Abbils = new boolean[4];
   private int[] Params = new int[18];
   private int curHP;
   public int curCost;
   private int Type;
   private boolean Eatable;
   private boolean Immortal;
   public boolean Active;
   private int PayTimeAccum;
   private int SpendMoneyTime;
   private int sx;
   private int sy;
   private int Level;
   private int[] SkillsLvl;

   public Higher() {
      this.SkillsLvl = new int[Skill.MaxSkills];
   }

   public final void LoadFromStream(DataInputStream dataInputStream) {
      try {
         this.Name = dataInputStream.readUTF();
         this.BodyInd = dataInputStream.readShort();
         this.HeadInd = dataInputStream.readShort();
         this.curHP = dataInputStream.readInt();
         this.curCost = dataInputStream.readInt();
         this.Type = dataInputStream.readByte();
         this.Level = dataInputStream.readShort();
         this.SpendMoneyTime = dataInputStream.readInt();
         this.sx = dataInputStream.readShort();
         this.sy = dataInputStream.readShort();
         this.Eatable = dataInputStream.readBoolean();
         this.Immortal = dataInputStream.readBoolean();
         int byteCode = dataInputStream.readByte();
         this.Params = new int[byteCode];

         for(byteCode = 0; byteCode < this.Params.length; ++byteCode) {
            this.Params[byteCode] = dataInputStream.readInt();
         }

         short abilityCount = dataInputStream.readByte();
         this.Abbils = new boolean[abilityCount];

         for(byteCode = 0; byteCode < this.Abbils.length; ++byteCode) {
            this.Abbils[byteCode] = dataInputStream.readBoolean();
         }

         abilityCount = dataInputStream.readShort();
         this.SkillsLvl = new int[abilityCount];

         for(byteCode = 0; byteCode < this.SkillsLvl.length; ++byteCode) {
            this.SkillsLvl[byteCode] = dataInputStream.readInt();
         }
      } catch (IOException exception) {
      }

      this.PayTimeAccum = 0;
      this.Active = true;
      this.sx = -1;
      this.sy = -1;
   }
}
