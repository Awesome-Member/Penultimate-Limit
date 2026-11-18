package Base;

import Engine.AnimSost;
import Engine.AnimSprite;
import Moduls.ImageLevelAreaDesc;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import desktop.graphics.GameImage;

public final class ImagesGlobal {
   public static int strategGameImagesPhasesCount;
   private static int strategGameImagesPhaseLastInd;
   public static GameImage[] strategGameImages;
   public static ImageLevelAreaDesc[] addingAnimSprites;
   public static GameImage questMinimapHave;
   public static GameImage questMinimapDone;
   public static GameImage questCompass;
   public static GameImage deadMob;
   public static GameImage deadHero;
   public static GameImage iconPoisoned;
   private static GameImage iconHorror;
   public static GameImage iconParalized;
   public static GameImage iconStopSkills;
   public static GameImage buffDamageUp;
   public static GameImage buffDamageDown;
   public static GameImage buffHealthUp;
   public static GameImage buffHealthDown;
   public static GameImage buffAtackUp;
   public static GameImage buffAtackDown;
   public static GameImage buffDefenceUp;
   public static GameImage buffDefenceDown;
   public static GameImage buffSpeedUp;
   public static GameImage buffSpeedDown;
   public static GameImage buffMoveUp;
   public static GameImage buffMoveDown;
   public static GameImage buffRangeUp;
   public static GameImage buffRangeDown;
   public static GameImage buffFriendsUp;
   public static GameImage buffFriendsDown;
   public static GameImage buffEnemysUp;
   public static GameImage buffEnemysDown;
   public static GameImage buffAnswerUp;
   public static GameImage buffAnswerDown;
   private static GameImage lifeBar;
   public static GameImage loadingSnake;
   public static GameImage loadingSmoke0;
   public static GameImage loadingSmoke1;
   public static GameImage buffRegenAdd;
   public static GameImage buffAcidAdd;
   public static GameImage buffPoisAdd;
   public static GameImage buffAcidRem;
   public static GameImage buffPoisRem;
   public static GameImage buffParalizeRem;
   public static GameImage heartAcid;
   public static GameImage heartRegen;
   public static GameImage effectIconStand;
   public static GameImage effectIconDesarm;
   public static GameImage joystickStick;
   public static GameImage effectIconProtection;
   public static GameImage[] Cursors;
   public static AnimSprite[] animServerSprites;
   public static AnimSprite[] animClientSprites;
   public static AnimSost[] GSprtsSost;
   public static GameImage[] ragesIcons;
   public static GameImage[] battleAtacksIconsOverHead;
   private static String[][] c_animSprts_nms = new String[][]{{"Hand", "500", "16"}, {"atkBash", "150", "16"}, {"atkArrows", "10", "16"}, {"atkKlac", "95", "16"}, {"deathMob", "250", "46"}, {"atkSur", "200", "16"}, {"expl", "150", "48"}, {"atkMag", "200", "16"}, {"APM1_", "110", "36"}, {"APH1_", "110", "35"}, {"Dinar", "333", "8"}, {"Skull", "333", "8"}, {"APM2_", "110", "36"}, {"APH2_", "110", "35"}, {"atkAxe", "333", "21"}, {"atkSword", "333", "21"}, {"atkAside", "200", "16"}, {"atkSnow", "200", "16"}, {"atkMage", "200", "16"}, {"atkFire", "200", "16"}, {"atkBomb", "200", "16"}, {"atkRage1", "133", "50"}, {"atkHeal", "200", "16"}, {"atkRage2", "133", "46"}, {"atkRage3", "133", "70"}, {"atkDeadRune", "200", "16"}, {"atkLive", "200", "16"}, {"atkArch", "333", "21"}, {"atkArrow", "10000", "24"}, {"atkMagicBall", "10000", "17"}, {"atkWeapons", "10000", "37"}, {"atkSkill0", "133", "68"}, {"atkSkill1", "133", "32"}, {"atkSkill2", "200", "106"}, {"atkSkill3", "133", "126"}, {"deathHero", "250", "62"}, {"heartSkill", "120", "22"}, {"heartWeapon", "120", "25"}, {"heartMagic", "120", "30"}};
   public static GameImage uiSideArrow;
   public static GameImage playersIcon;
   public static GameImage iconAtack;
   public static GameImage iconChatClan;
   public static GameImage iconChatPrivate;
   public static GameImage iconChatCommon;
   public static GameImage iconChatParty;
   public static GameImage iconChatClanSelect;
   public static GameImage iconChatPrivateSelect;
   public static GameImage iconChatCommonSelect;
   public static GameImage iconChatPartySelect;
   public static GameImage iconChatDisable;
   public static GameImage hitEffectKrit;
   public static GameImage hitEffectDodge;
   public static GameImage hitEffectBlock;
   public static GameImage worldMapPlayerPosition;
   public static GameImage worldMapQuest;
   public static GameImage iconBattleEnemys;
   public static GameImage iconBattleFriends;
   public static GameImage iconStepBack;
   public static GameImage iconSurrender;
   public static GameImage iconBattleOffscreenEnemy;
   public static GameImage iconBattleOffscreenFriend;
   public static GameImage iconBattleOffscreenMy;

   public static void loadCommonImages() {
      try {
         if (!Com.drawCover) {
            c_animSprts_nms[6] = new String[]{"_oldExpl", "200", "32"};
            c_animSprts_nms[4] = new String[]{"_oldDead", "333", "24"};
            c_animSprts_nms[35] = new String[]{"_oldDead", "333", "24"};
            c_animSprts_nms[36] = new String[]{"empty", "10000", "1"};
            c_animSprts_nms[37] = new String[]{"empty", "10000", "1"};
            c_animSprts_nms[38] = new String[]{"empty", "10000", "1"};
            c_animSprts_nms[31] = c_animSprts_nms[20];
            c_animSprts_nms[32] = c_animSprts_nms[22];
            c_animSprts_nms[33] = c_animSprts_nms[20];
            c_animSprts_nms[34] = c_animSprts_nms[26];
         }

         String messageText = "/Data/All/";
         GameImage gameImage = GameImage.createImage(messageText + "atkRage1Icon.png");
         ragesIcons = new GameImage[]{gameImage, gameImage, GameImage.createImage(messageText + "atkRage2Icon.png"), GameImage.createImage(messageText + "atkRage3Icon.png")};
         gameImage = GameImage.createImage(messageText + "atkRageIcon.png");
         battleAtacksIconsOverHead = new GameImage[]{null, null, null, null, null, null, null, null, null, gameImage, gameImage, gameImage, null, null, null, null, null, null, null, null, null, null, GameImage.createImage(messageText + "atkSkill0Icon.png"), GameImage.createImage(messageText + "atkSkill1Icon.png"), GameImage.createImage(messageText + "atkSkill2Icon.png"), GameImage.createImage(messageText + "atkSkill3Icon.png")};
         if (Com.drawCover) {
            deadMob = GameImage.createImage(messageText + "deadMob.png");
            deadHero = GameImage.createImage(messageText + "deadHero.png");
         } else {
            deadHero = deadMob = GameImage.createImage(messageText + "_oldDeadBody.png");
         }

         lifeBar = GameImage.createImage(messageText + "empty.png");
         playersIcon = GameImage.createImage(messageText + "str_head.png");
         iconAtack = GameImage.createImage(messageText + "atack_icon.png");
         uiSideArrow = GameImage.createImage(messageText + "ui_side_arrow_25.png");
         iconChatDisable = GameImage.createImage(messageText + "post_disable.png");
         iconChatCommon = GameImage.createImage(messageText + "post1.png");
         iconChatClan = GameImage.createImage(messageText + "post2.png");
         iconChatPrivate = GameImage.createImage(messageText + "post3.png");
         iconChatParty = GameImage.createImage(messageText + "post4.png");
         iconChatCommonSelect = GameImage.createImage(messageText + "post1_select.png");
         iconChatClanSelect = GameImage.createImage(messageText + "post2_select.png");
         iconChatPrivateSelect = GameImage.createImage(messageText + "post3_select.png");
         iconChatPartySelect = GameImage.createImage(messageText + "post4_select.png");
         iconBattleOffscreenEnemy = GameImage.createImage(messageText + "battle_offscreen_icon_enemy.png");
         iconBattleOffscreenFriend = GameImage.createImage(messageText + "battle_offscreen_icon_friend.png");
         iconBattleOffscreenMy = GameImage.createImage(messageText + "battle_offscreen_icon_my.png");
         hitEffectKrit = GameImage.createImage(messageText + "hit_krit.png");
         hitEffectDodge = GameImage.createImage(messageText + "hit_dodge.png");
         hitEffectBlock = GameImage.createImage(messageText + "hit_block.png");
         worldMapPlayerPosition = GameImage.createImage(messageText + "world_map_player.png");
         worldMapQuest = GameImage.createImage(messageText + "world_map_quest.png");
         iconBattleFriends = GameImage.createImage(messageText + "battle_friends_icon.png");
         iconBattleEnemys = GameImage.createImage(messageText + "battle_enemys_icon.png");
         iconStepBack = GameImage.createImage(messageText + "stepback_icon.png");
         iconSurrender = GameImage.createImage(messageText + "surrender_icon.png");
         (Cursors = new GameImage[11])[0] = GameImage.createImage(messageText + "Cursors/Fist.png");
         Cursors[1] = GameImage.createImage(messageText + "Cursors/Hand.png");
         Cursors[2] = GameImage.createImage(messageText + "Cursors/Sword.png");
         Cursors[3] = GameImage.createImage(messageText + "Cursors/Walk.png");
         Cursors[4] = GameImage.createImage(messageText + "Cursors/Jaw.png");
         Cursors[5] = GameImage.createImage(messageText + "Cursors/NoWalk.png");
         Cursors[6] = GameImage.createImage(messageText + "Cursors/Flag.png");
         Cursors[7] = GameImage.createImage(messageText + "Cursors/Rage.png");
         Cursors[8] = GameImage.createImage(messageText + "Cursors/Item.png");
         Cursors[9] = GameImage.createImage(messageText + "Cursors/ArrowUp.png");
         Cursors[10] = GameImage.createImage(messageText + "Cursors/ArrowUpRight.png");
         iconPoisoned = GameImage.createImage(messageText + "hearts/Poison.png");
         iconParalized = GameImage.createImage(messageText + "hearts/Paralize.png");
         iconStopSkills = GameImage.createImage(messageText + "hearts/stop_skill.png");
         iconHorror = GameImage.createImage(messageText + "Horror.png");
         String messageText2 = messageText + "buffs/";
         buffDamageUp = GameImage.createImage(messageText2 + "buff_dmg_u.png");
         buffDamageDown = GameImage.createImage(messageText2 + "buff_dmg_d.png");
         buffHealthUp = GameImage.createImage(messageText2 + "buff_heal_u.png");
         buffHealthDown = GameImage.createImage(messageText2 + "buff_heal_d.png");
         buffAtackUp = GameImage.createImage(messageText2 + "buff_atack_u.png");
         buffAtackDown = GameImage.createImage(messageText2 + "buff_atack_d.png");
         buffDefenceUp = GameImage.createImage(messageText2 + "buff_def_u.png");
         buffDefenceDown = GameImage.createImage(messageText2 + "buff_def_d.png");
         buffSpeedUp = GameImage.createImage(messageText2 + "buff_spd_u.png");
         buffSpeedDown = GameImage.createImage(messageText2 + "buff_spd_d.png");
         buffMoveUp = GameImage.createImage(messageText2 + "buff_move_u.png");
         buffMoveDown = GameImage.createImage(messageText2 + "buff_move_d.png");
         buffRangeUp = GameImage.createImage(messageText2 + "buff_range_u.png");
         buffRangeDown = GameImage.createImage(messageText2 + "buff_range_d.png");
         buffFriendsUp = GameImage.createImage(messageText2 + "buff_friends_u.png");
         buffFriendsDown = GameImage.createImage(messageText2 + "buff_friends_d.png");
         buffEnemysUp = GameImage.createImage(messageText2 + "buff_enemys_u.png");
         buffEnemysDown = GameImage.createImage(messageText2 + "buff_enemys_d.png");
         buffAnswerUp = GameImage.createImage(messageText2 + "buff_ans_u.png");
         buffAnswerDown = GameImage.createImage(messageText2 + "buff_ans_d.png");
         buffRegenAdd = GameImage.createImage(messageText2 + "buff_regen_add.png");
         buffAcidAdd = GameImage.createImage(messageText2 + "buff_acid_add.png");
         buffPoisAdd = GameImage.createImage(messageText2 + "buff_pois_add.png");
         buffAcidRem = GameImage.createImage(messageText2 + "buff_acid_rem.png");
         buffPoisRem = GameImage.createImage(messageText2 + "buff_pois_rem.png");
         buffParalizeRem = GameImage.createImage(messageText2 + "buff_paralize_rem.png");
         heartAcid = GameImage.createImage(messageText + "hearts/acid.png");
         heartRegen = GameImage.createImage(messageText + "hearts/regen.png");
         effectIconStand = GameImage.createImage(messageText + "hearts/stand.png");
         effectIconDesarm = GameImage.createImage(messageText + "hearts/desarm.png");
         effectIconProtection = GameImage.createImage(messageText + "hearts/protection.png");
         joystickStick = GameImage.createImage(messageText + "itrfc/joystick_stick.png");
         questCompass = GameImage.createImage(messageText + "quest_compass.png");
         questMinimapHave = GameImage.createImage(messageText + "quest_minimap_have.png");
         questMinimapDone = GameImage.createImage(messageText + "quest_minimap_done.png");
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   static void loadAnimSprites() {
      animClientSprites = new AnimSprite[39];
      GSprtsSost = new AnimSost[39];

      for(int i = 0; i < 39; ++i) {
         animClientSprites[i] = new AnimSprite(Integer.parseInt(c_animSprts_nms[i][2]), Integer.parseInt(c_animSprts_nms[i][1]), "/Data/All/" + c_animSprts_nms[i][0] + ".png");
         GSprtsSost[i] = new AnimSost(i);
         AnimSost animSost;
         (animSost = GSprtsSost[i]).TimeAccum = 0;
         animSost.CurFrameI = 0;
         animSost.WasLoop = false;
         animSost.WasFrame = false;
      }

   }

   public static void loadAnimSprites(DataInputStream dataInputStream) {
      try {
      animServerSprites = new AnimSprite[dataInputStream.readInt()];

      for(int i = 0; i < animServerSprites.length; ++i) {
         animServerSprites[i] = new AnimSprite(new ImageLevelAreaDesc(dataInputStream));
      }

   
      } catch (IOException exception) {
         ImagesGlobal.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void loadBaseImages() {
      try {
         String s2 = "/Data/All/";
         if (!Com.drawCover) {
            loadingSnake = GameImage.createImage(s2 + "loading_clock.png");
            loadingSmoke0 = GameImage.createImage(s2 + "loading_clock_0.png");
            loadingSmoke1 = GameImage.createImage(s2 + "loading_clock_1.png");
            return;
         }

         loadingSnake = GameImage.createImage(s2 + "loading_snake.png");
         loadingSmoke0 = GameImage.createImage(s2 + "loading_smoke_0.png");
         loadingSmoke1 = GameImage.createImage(s2 + "loading_smoke_1.png");
      } catch (Exception exception) {
         exception.printStackTrace();
      }

   }

   public static Vector addForAtlasing(Vector vector3) {
      vector3.addElement(deadMob);
      vector3.addElement(deadHero);
      vector3.addElement(loadingSnake);
      vector3.addElement(loadingSmoke0);
      vector3.addElement(loadingSmoke1);

      int i;
      for(i = 0; i < battleAtacksIconsOverHead.length; ++i) {
         if (battleAtacksIconsOverHead[i] != null) {
            vector3.addElement(battleAtacksIconsOverHead[i]);
         }
      }

      for(i = 0; i < ragesIcons.length; ++i) {
         if (ragesIcons[i] != null) {
            vector3.addElement(ragesIcons[i]);
         }
      }

      for(i = 0; i < animClientSprites.length; ++i) {
         vector3.addElement(animClientSprites[i].Source);
      }

      vector3.addElement(lifeBar);
      vector3.addElement(playersIcon);
      vector3.addElement(iconAtack);
      vector3.addElement(uiSideArrow);
      vector3.addElement(iconChatDisable);
      vector3.addElement(iconChatCommon);
      vector3.addElement(iconChatClan);
      vector3.addElement(iconChatPrivate);
      vector3.addElement(iconChatParty);
      vector3.addElement(iconChatCommonSelect);
      vector3.addElement(iconChatClanSelect);
      vector3.addElement(iconChatPrivateSelect);
      vector3.addElement(iconChatPartySelect);
      vector3.addElement(hitEffectKrit);
      vector3.addElement(hitEffectDodge);
      vector3.addElement(hitEffectBlock);
      vector3.addElement(worldMapPlayerPosition);
      vector3.addElement(worldMapQuest);
      vector3.addElement(iconBattleEnemys);
      vector3.addElement(iconBattleFriends);
      vector3.addElement(iconStepBack);
      vector3.addElement(iconSurrender);
      vector3.addElement(iconBattleOffscreenEnemy);
      vector3.addElement(iconBattleOffscreenFriend);
      vector3.addElement(iconBattleOffscreenMy);

      for(i = 0; i < Cursors.length; ++i) {
         vector3.addElement(Cursors[i]);
      }

      vector3.addElement(iconPoisoned);
      vector3.addElement(iconParalized);
      vector3.addElement(iconHorror);
      vector3.addElement(iconStopSkills);
      vector3.addElement(buffDamageUp);
      vector3.addElement(buffDamageDown);
      vector3.addElement(buffHealthUp);
      vector3.addElement(buffHealthDown);
      vector3.addElement(buffAtackUp);
      vector3.addElement(buffAtackDown);
      vector3.addElement(buffDefenceUp);
      vector3.addElement(buffDefenceDown);
      vector3.addElement(buffSpeedUp);
      vector3.addElement(buffSpeedDown);
      vector3.addElement(buffMoveUp);
      vector3.addElement(buffMoveDown);
      vector3.addElement(buffRangeUp);
      vector3.addElement(buffRangeDown);
      vector3.addElement(buffRegenAdd);
      vector3.addElement(buffAcidAdd);
      vector3.addElement(buffPoisAdd);
      vector3.addElement(buffAcidRem);
      vector3.addElement(buffPoisRem);
      vector3.addElement(buffParalizeRem);
      vector3.addElement(buffFriendsUp);
      vector3.addElement(buffFriendsDown);
      vector3.addElement(buffEnemysUp);
      vector3.addElement(buffEnemysDown);
      vector3.addElement(buffAnswerUp);
      vector3.addElement(buffAnswerDown);
      vector3.addElement(heartAcid);
      vector3.addElement(heartRegen);
      vector3.addElement(effectIconStand);
      vector3.addElement(effectIconDesarm);
      vector3.addElement(effectIconProtection);
      vector3.addElement(joystickStick);
      vector3.addElement(questCompass);
      vector3.addElement(questMinimapHave);
      vector3.addElement(questMinimapDone);
      return vector3;
   }

   public static void setStrategGameImage(GameImage gameImage) {
      strategGameImages = new GameImage[]{gameImage};
   }

   public static void loadStrategGameImages(DataInputStream dataInputStream, int phase) {
      try {
      if (phase == 0) {
         strategGameImagesPhaseLastInd = -1;
         strategGameImages = new GameImage[dataInputStream.readShort()];
      }

      short c = dataInputStream.readShort();

      for(int i = 0; i < c; ++i) {
         ++strategGameImagesPhaseLastInd;
         Com.PrBarTh.setText("Спрайт " + (strategGameImagesPhaseLastInd + 1) + "/" + strategGameImages.length);
         byte[] arr = new byte[dataInputStream.readInt()];
         dataInputStream.readFully(arr);
         strategGameImages[strategGameImagesPhaseLastInd] = GameImage.createImage(arr, 0, arr.length);
      }

   
      } catch (IOException exception) {
         ImagesGlobal.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public static void loadAddingAnimSprites(DataInputStream dataInputStream) {
      try {
      addingAnimSprites = new ImageLevelAreaDesc[dataInputStream.readInt()];

      for(int i = 0; i < addingAnimSprites.length; ++i) {
         addingAnimSprites[i] = new ImageLevelAreaDesc(dataInputStream);
      }

   
      } catch (IOException exception) {
         ImagesGlobal.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
   private ImagesGlobal() {
   }

}
