package Moduls;

import Base.NetRequest;
import Base.Com;
import java.io.IOException;

public final class Skill {
   public String Name;
   private String ListInfo;
   public String nowInfo;
   public String willInfo;
   public String Suffiks;
   public int[] NeedLevels;
   public int[] valueBySkillLvl;
   public int effectInd;
   public static int MaxSkills = 19;
   public static Skill[] Skills;
   private static int[] skillsValues;
   public int divider;

   public static void LoadSkills(NetRequest netRequest) {
      try {
         int integerCode;
         skillsValues = new int[integerCode = netRequest.response.readInt()];

         int skillsValueIndex;
         for(skillsValueIndex = 0; skillsValueIndex < integerCode; ++skillsValueIndex) {
            skillsValues[skillsValueIndex] = netRequest.response.readInt();
         }

         Skills = new Skill[MaxSkills = netRequest.response.readInt()];

         for(skillsValueIndex = 0; skillsValueIndex < MaxSkills; ++skillsValueIndex) {
            Com.PrBarTh.setText("Навык\r" + skillsValueIndex + "/" + MaxSkills);
            Skills[skillsValueIndex] = new Skill();
            Skills[skillsValueIndex].Name = netRequest.response.readUTF();
            Skills[skillsValueIndex].ListInfo = netRequest.response.readUTF();
            Skills[skillsValueIndex].nowInfo = netRequest.response.readUTF();
            Skills[skillsValueIndex].willInfo = netRequest.response.readUTF();
            Skills[skillsValueIndex].Suffiks = netRequest.response.readUTF();
            Skills[skillsValueIndex].NeedLevels = new int[netRequest.response.readInt()];

            for(integerCode = 0; integerCode < Skills[skillsValueIndex].NeedLevels.length; ++integerCode) {
               Skills[skillsValueIndex].NeedLevels[integerCode] = netRequest.response.readInt();
            }

            Skills[skillsValueIndex].effectInd = netRequest.response.readInt();
            Skills[skillsValueIndex].divider = netRequest.response.readInt();
            Skills[skillsValueIndex].valueBySkillLvl = new int[netRequest.response.readInt()];

            for(integerCode = 0; integerCode < Skills[skillsValueIndex].valueBySkillLvl.length; ++integerCode) {
               Skills[skillsValueIndex].valueBySkillLvl[integerCode] = netRequest.response.readInt();
            }
         }

      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }
}
