package MenuPck.ListTypes;

import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.Com;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import Moduls.Skill;
import Moduls.Strategist;
import Resources.StringResources;
import java.util.Vector;

public final class SkillsMenuList extends MenuList {
   private static int curSkill = 0;
   private int[] EqualIDs;

   public SkillsMenuList(int id, int backMenu, String[] list) {
      super(7, 4, (String[])list);
   }

   protected final void onGenerateHintEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 7:
            menuId = this.EqualIDs[itemIndex];
            setAnimatedTitle(Skill.Skills[menuId].nowInfo + getSkillLevelDescription(menuId, 0) + "\n\nСвободных баллов: " + Strategist.instance.GP_SkillPoints);
         default:
      }
   }

   protected final Vector onGenerateItemsAndSkipsEvent(int menuId) {
      switch (menuId) {
         case 7:
            Vector vector3 = super.onGenerateItemsAndSkipsEvent(menuId);
            this.EqualIDs = new int[Skill.MaxSkills];
            int skillIndex2 = 0;
            int skillIndex = 0;

            for(; skillIndex < Skill.MaxSkills; ++skillIndex) {
               if (Skill.Skills[skillIndex].NeedLevels[Strategist.instance.GP_Race] + Strategist.instance.GP_TotalSkillsLevels[skillIndex] <= Strategist.instance.GP_Level || Strategist.instance.GP_TotalSkillsLevels[skillIndex] > 0) {
                  vector3.addElement("(" + Integer.toString(Strategist.instance.GP_TotalSkillsLevels[skillIndex]) + ")" + Skill.Skills[skillIndex].Name);
                  this.EqualIDs[skillIndex2++] = skillIndex;
               }
            }

            return vector3;
         default:
            return null;
      }
   }

   protected final void onListClickEvent(int menuId, int itemIndex) {
      switch (menuId) {
         case 7:
            curSkill = this.EqualIDs[itemIndex];
            if (Strategist.instance.GP_TotalSkillsLevels[curSkill] + 1 >= Skill.Skills[curSkill].valueBySkillLvl.length) {
               showFloatMessageS("Навык\nмаксимального уровня");
               return;
            } else if (Skill.Skills[curSkill].NeedLevels[Strategist.instance.GP_Race] + Strategist.instance.GP_SelfSkillsLevels[curSkill] <= Strategist.instance.GP_Level) {
               this.showConfirmationForMenu(248, "Свободных баллов: " + Strategist.instance.GP_SkillPoints + "\n" + Skill.Skills[curSkill].nowInfo + getSkillLevelDescription(curSkill, 0) + "\n" + Skill.Skills[curSkill].willInfo + getSkillLevelDescription(curSkill, 1) + "\nИзучить?");
            }
         default:
      }
   }

   protected final void onYesNoEnterEvent(int backIndex, boolean res) {
      switch (backIndex) {
         case 248:
            byte menuId;
            if (res) {
               boolean b = true;
               if (Strategist.instance.GP_SkillPoints <= 0) {
                  showFloatMessageS(StringResources.S_TT_EVENTS[2]);
                  b = false;
               }

               if (Skill.Skills[curSkill].NeedLevels[Strategist.instance.GP_Race] + Strategist.instance.GP_SelfSkillsLevels[curSkill] > Strategist.instance.GP_Level) {
                  showFloatMessageS(StringResources.S_TT_EVENTS[4]);
                  b = false;
               }

               if (Strategist.instance.GP_TotalSkillsLevels[curSkill] + 1 > Skill.Skills[curSkill].valueBySkillLvl.length - 1) {
                  showFloatMessageS("Не допускается");
                  b = false;
               }

               if (!b) {
                  break;
               }

               Com.PrBarTh.Set(true);
               NetRequest netRequest = new NetRequest();

               try {
                  netRequest.writeHeader(1402000);
                  netRequest.request.writeInt(curSkill);
                  Com.sendRequest(netRequest);
                  if (netRequest.response.readInt() == 1402001) {
                     IndirectNetworkHandler.instance.loadFromStreamAndApply(netRequest.response);
                  }
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               Com.PrBarTh.Stop();
               showFloatMessageS(StringResources.S_TT_EVENTS[1]);
               if (Strategist.instance.GP_SkillPoints == 0) {
                  menuId = 7;
                  MenuManager.instance.showMenuById(menuId);
               } else if (Strategist.instance.GP_TotalSkillsLevels[curSkill] + 1 < Skill.Skills[curSkill].valueBySkillLvl.length) {
                  this.showConfirmationForMenu(248, "Свободных баллов: " + Strategist.instance.GP_SkillPoints + "\n" + Skill.Skills[curSkill].nowInfo + getSkillLevelDescription(curSkill, 0) + "\n" + Skill.Skills[curSkill].willInfo + getSkillLevelDescription(curSkill, 1) + "\nИзучить?");
                  break;
               } else {
                  menuId = 7;
                  MenuManager.instance.showMenuById(menuId);
               }
            } else {
               menuId = 7;
               MenuManager.instance.showMenuById(menuId);
            }
         default:
            return;
      }

   }

   private static String getSkillLevelDescription(int skill3, int addToLvl) {
      if (Strategist.instance.GP_TotalSkillsLevels[skill3] + addToLvl > Skill.Skills[skill3].valueBySkillLvl.length - 1) {
         return "Навык\nмаксимального уровня";
      } else {
         Skill skill = Skill.Skills[skill3];
         int instance = Strategist.instance.GP_TotalSkillsLevels[skill3] + addToLvl;
         Skill skill2 = skill;
         instance = (addToLvl = instance > skill2.valueBySkillLvl.length - 1 ? skill2.valueBySkillLvl[skill2.valueBySkillLvl.length - 1] : skill2.valueBySkillLvl[instance]) / Skill.Skills[skill3].divider;
         addToLvl = Math.abs(addToLvl % Skill.Skills[skill3].divider);
         int availableLevelCount = 0;
         int addedLevelCount = 0;
         String s = String.valueOf(instance);
         if (addToLvl > 0) {
            int skills;
            for(skills = Skill.Skills[skill3].divider; (skills /= 10) > 0; ++availableLevelCount) {
            }

            for(skills = addToLvl; (skills /= 10) > 0; ++addedLevelCount) {
            }

            ++addedLevelCount;
            s = s + ".";

            for(addedLevelCount = addedLevelCount; addedLevelCount < availableLevelCount; ++addedLevelCount) {
               s = s + "0";
            }

            s = s + addToLvl;
         }

         return s + Skill.Skills[skill3].Suffiks;
      }
   }
}
