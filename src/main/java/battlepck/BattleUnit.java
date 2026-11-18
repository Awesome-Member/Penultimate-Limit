package battlepck;

import Base.ImagesGlobal;
import Base.Com;
import Engine.AnimSprite;
import Engine.MystFont;
import Moduls.MoveableUnitDiscretShort;
import Moduls.PathFolower;
import Moduls.StrategAnimGroup;
import Moduls.StrategGraphic;
import Moduls.Tile;
import Moduls.Legionery;
import battlepck.client.BattleDrawBuffEffect;
import battlepck.client.BattleDrawHeartEffect;
import java.io.DataInputStream;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class BattleUnit extends MoveableUnitDiscretShort {
   public int addXOffHit = 0;
   public int addYOffHit = 0;
   public long leftOffHitTime = 0L;
   private int drawType;
   public boolean playersAvatar = false;
   public int bushInd;
   public int bushBaseX = 0;
   public int bushBaseY = -13;
   public int bushLatency = 0;
   private int BodyInd;
   private int HeadInd;
   public boolean HeadLorR;
   public int LeftRight;
   public int hisBattleMemberId;
   public String Name;
   public boolean HigherLegi;
   private int HigherType;
   public long CurCount = 100L;
   public long MaxCount = 100L;
   public long curHealthMaxCount;
   public int Level = 1;
   public long healthAnimFromCount = -1L;
   public long healthAnimTimeAccum = 0L;
   public BattleParams params = new BattleParams();
   public boolean Dead;
   private boolean Active;
   public boolean Drawed;
   public int deadTimer = 0;
   public long walkStayAnimationTime = 0L;
   public boolean isWalkOrStay = false;
   public byte watchDirection = 0;
   public short ai_targetAtackUnit = -1;
   public short ai_targetMoveUnit = -1;
   public short ai_targetMoveX = 0;
   public short ai_targetMoveY = 0;
   public PathFolower pathFolower = new PathFolower();
   public short pathFolowedToTargetTileX = 0;
   public short pathFolowedToTargetTileY = 0;
   public boolean forceRebuildPath = false;
   public boolean iterationStayingForBouncing = false;
   public boolean iterationForBouncingCanAtack = false;
   public int iterationForBouncingCanAtackPointX = 0;
   public int iterationForBouncingCanAtackPointY = 0;
   public long bouncingPartialLeft;
   public int bouncingSenseHWidth;
   private int bouncingSenseHHeight;
   public BattleDistPunch executableDistPunch = null;
   public boolean ai_Atacking;
   public boolean ai_FirstTarget;
   public int ai_AtackTimeSpend;
   public int atk_Target;
   public int atk_TimeAcc;
   public int btl_freezResist = 0;
   public boolean btl_Paralize;
   public int btl_ParalTime;
   public int btl_StandTime;
   public int btl_DesarmTime;
   public int btl_StopFlyTime;
   public int btl_ProtectionTime;
   public int btl_ProtectionPower;
   public int btl_FlyToPointTime = 0;
   public int btl_FlyToPointBaseTime = 0;
   public int btl_FlyToFromX = 0;
   public int btl_FlyToFromY = 0;
   public int btl_FlyToToX = 0;
   public int btl_FlyToToY = 0;
   public boolean btl_Poisoned;
   public int btl_PoisPower;
   public int btl_PoisTimeAcc;
   public int btl_AcidPower;
   public int btl_AcidTimeAcc;
   public int btl_RegenPower;
   public int btl_RegenTimeAcc;
   public int btl_StayTimeAcc;
   public boolean btl_Inerted;
   public int btl_InertTimeAcc;
   public int btl_InertMaxTime;
   public int btl_InertLastTimeX;
   public int btl_InertLastTimeY;
   public int[] btl_InertVect = new int[2];
   private long btl_HitAccum;
   private long btl_HealAccum;
   public int changeDamage = 0;
   public int changeHealth = 0;
   public int changeAtack = 0;
   public int changeDefence = 0;
   public int changeSpeed = 0;
   public int changeMove = 0;
   public int changeRange = 0;
   public boolean transfromationActive = false;
   public int transformationLeftTime = 0;
   public BattleUnit transformationBackUnit = null;
   public boolean shakeActive = false;
   public long shakeTime = 0L;
   public Vector drawBuffsList = new Vector();
   public Vector drawHeartEffectsList = new Vector();
   public int drawHeartInd = -1;
   public int drawHeartTime = 0;
   private byte drawHeartFromSide = 0;
   public int hisInd;
   public int side;
   private int drawInd;
   private int lastBufPosVert = 0;
   private int lastBufMaxHeight = 0;
   private int lastBufWidth = 0;
   private StrategGraphic strategGraphic = new StrategGraphic();
   public static MystFont mFont;
   public static MystFont dGFont;
   public static MystFont dRFont;
   private int[] paddingsCachLeft = null;
   private int[] paddingsCachRight = null;

   private BattleUnit(short cX, short cY, short cdW, short cdH) {
      super(cX, cY, cdW, cdH);
      this.setBouncingPartialLeft(150L);
   }

   public BattleUnit(DataInputStream dataInputStream) {
      super((short)-1, (short)-1, (short)Battle.CD_W, (short)Battle.CD_H);
      this.setBouncingPartialLeft(150L);
      this.cx = -1;
      this.cy = -1;
      this.Dead = false;
      this.drawType = 0;
      this.ai_AtackTimeSpend = 0;
      this.btl_Paralize = false;
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }

   }

   private void resetHealthAnimation() {
      this.healthAnimFromCount = -1L;
      this.healthAnimTimeAccum = 0L;
   }

   public final void healthAnimPrepear() {
      if (this.healthAnimFromCount < 0L) {
         this.healthAnimFromCount = this.CurCount;
      }

      this.healthAnimTimeAccum = 0L;
   }

   public final void startDrawHeart(int ind, byte fromSide) {
      if (ind >= 0) {
         this.drawHeartInd = ind;
         this.drawHeartTime = 0;
         this.drawHeartFromSide = fromSide;
      }
   }

   public final void decBouncingPartialLeft() {
      if (this.bouncingPartialLeft > 0L) {
         this.setBouncingPartialLeft(this.bouncingPartialLeft - 1L);
      }

   }

   public final void setBouncingPartialLeft(long bouncingPartialLeft2) {
      if (this.bouncingPartialLeft != bouncingPartialLeft2) {
         this.bouncingPartialLeft = bouncingPartialLeft2;
         this.bouncingSenseHWidth = (int)(this.bouncingPartialLeft * (long)Battle.CD_HW / 150L);
         this.bouncingSenseHHeight = (int)(this.bouncingPartialLeft * (long)Battle.CD_HH / 150L);
      }
   }

   public final void transformWithHystory(BattleUnit battleUnit, int time) {
      if (!this.transfromationActive) {
         this.transformationBackUnit = new BattleUnit(this.cx, this.cy, this.cdW, this.cdH);
         this.transformationBackUnit.transfromToUnit(this, true);
      }

      this.transformationLeftTime = time;
      this.transfromationActive = true;
      this.transfromToUnit(battleUnit, false);
   }

   public final void transfromToUnit(BattleUnit battleUnit, boolean fullReplace) {
      this.BodyInd = battleUnit.BodyInd;
      this.HeadInd = battleUnit.HeadInd;
      this.drawType = battleUnit.drawType;
      long wasHealthPart = this.CurCount * 1000L / this.MaxCount;
      if (fullReplace) {
         this.MaxCount = battleUnit.MaxCount;
      } else {
         this.MaxCount += battleUnit.MaxCount;
      }

      this.CurCount = this.MaxCount * wasHealthPart / 1000L;
      if (this.CurCount > this.MaxCount) {
         this.CurCount = this.MaxCount;
      }

      this.resetHealthAnimation();
      BattleParams battleParams = battleUnit.params.cloneTo();
      if (!fullReplace) {
         battleParams.atackValue += this.params.atackValue;
         battleParams.defenceValue += this.params.defenceValue;
      }

      this.params = battleParams;
      this.strategGraphic = battleUnit.strategGraphic.makeClone();
      this.bushInd = battleUnit.bushInd;
   }

   public final int getVertOffsetForEffects(int inc) {
      return Legionery.getUnitInfoVerticalOffset(this.drawType, this.BodyInd, this.HeadInd, this.strategGraphic) + inc;
   }

   private void addHeartEffect(byte color, String messageText, GameImage gameImage, GameImage gameImage2) {
      this.drawHeartEffectsList.addElement(new BattleDrawHeartEffect(color, messageText, gameImage, (GameImage)null, this.getVertOffsetForEffects(0) / 2, this.getVertOffsetForEffects(-8)));
   }

   public final void addDrawBuff(GameImage gameImage) {
      if (this.drawBuffsList.size() == 0) {
         this.lastBufPosVert = this.getVertOffsetForEffects(-3);
         this.lastBufMaxHeight = gameImage.getHeight();
         this.lastBufWidth = gameImage.getWidth();
      } else if (this.drawBuffsList.size() % 3 == 0) {
         this.lastBufPosVert -= this.lastBufMaxHeight;
         this.lastBufMaxHeight = gameImage.getHeight();
         this.lastBufWidth = gameImage.getWidth();
      }

      if (gameImage.getHeight() > this.lastBufMaxHeight) {
         this.lastBufMaxHeight = gameImage.getHeight();
      }

      BattleDrawBuffEffect battleDrawBuffEffect = null;
      switch (this.drawBuffsList.size() % 3) {
         case 0:
            battleDrawBuffEffect = new BattleDrawBuffEffect(gameImage, -this.lastBufWidth / 2, this.lastBufPosVert);
            break;
         case 1:
            battleDrawBuffEffect = new BattleDrawBuffEffect(gameImage, -this.lastBufWidth / 2 - gameImage.getWidth(), this.lastBufPosVert);
            break;
         case 2:
            battleDrawBuffEffect = new BattleDrawBuffEffect(gameImage, this.lastBufWidth / 2, this.lastBufPosVert);
      }

      this.drawBuffsList.addElement(battleDrawBuffEffect);
   }

   public final void startOffHit(boolean leftOfRight) {
      this.leftOffHitTime = 300L;
      this.addYOffHit = -2;
      if (leftOfRight) {
         this.addXOffHit = -1;
      } else {
         this.addXOffHit = 1;
      }
   }

   public final long getRealTimeBetweenHitsMs() {
      return Math.max(Battle.percentLogic((long)this.params.timeBetweenHitsMs, -this.changeSpeed), (long)(Battle.c_battle_min_time_between_hits + Battle.c_all_finish_atacks_time));
   }

   public final void addIntertion(int[] inertVect, int inertTime) {
      this.btl_InertVect[0] = inertVect[0];
      this.btl_InertVect[1] = inertVect[1];
      this.btl_InertMaxTime = inertTime;
      this.btl_InertTimeAcc = 0;
      this.btl_InertLastTimeX = 0;
      this.btl_InertLastTimeY = 0;
      this.btl_Inerted = true;
      this.btl_InertTimeAcc = 0;
      this.btl_InertLastTimeX = 0;
      this.btl_InertLastTimeY = 0;
      if (this.btl_InertMaxTime <= 0) {
         this.btl_Inerted = false;
      }

      this.pathFolower.disableMovePath();
   }

   public final void heal(long HealValue, GameImage gameImage) {
      if (!this.Dead) {
         this.btl_HealAccum += HealValue;
         long btl_HealAccum2;
         if ((btl_HealAccum2 = this.btl_HealAccum / 1000L) > 0L) {
            this.btl_HealAccum %= 1000L;
            this.addHeartEffect((byte)1, String.valueOf(btl_HealAccum2), gameImage, (GameImage)null);
            this.healthAnimPrepear();
            this.CurCount += btl_HealAccum2;
            long curCount = Battle.percentLogic(this.MaxCount, this.changeHealth);
            if (this.CurCount > curCount) {
               this.CurCount = curCount;
            }

         } else {
            if (gameImage != null) {
               this.addHeartEffect((byte)1, (String)null, gameImage, (GameImage)null);
            }

         }
      }
   }

   public final void hit(Battle battle2, long HitValue, GameImage gameImage) {
      if (!this.Dead) {
         this.btl_HitAccum += HitValue;
         if (this.btl_HitAccum < 0L) {
            this.btl_HitAccum = -this.btl_HitAccum;
            this.btl_HitAccum = -this.btl_HitAccum;
         }

         long pr = this.btl_HitAccum / 1000L;
         BattleMemberSmall battleMemberSmall;
         if (this.playersAvatar && HitValue > 0L && (battleMemberSmall = battle2.getBattleMember(this.hisBattleMemberId)).stopRageTime <= 0) {
            battleMemberSmall.changeRage(-Battle.c_rage_Sub);
         }

         if (pr > 0L) {
            this.btl_HitAccum %= 1000L;
            this.addHeartEffect((byte)0, String.valueOf(pr), gameImage, (GameImage)null);
            this.healthAnimPrepear();
            this.CurCount -= pr;
            if (this.CurCount <= 0L) {
               this.CurCount = 0L;
               this.kill(battle2, true);
               return;
            }
         } else if (gameImage != null) {
            this.addHeartEffect((byte)0, (String)null, gameImage, (GameImage)null);
         }

      }
   }

   public final void kill(Battle battle2, boolean notify) {
      this.setDead();
      if (notify && battle2 != null) {
         battle2.unitDead(this.hisInd);
      }

   }

   private void setDead() {
      this.Dead = true;
      this.deadTimer = 0;
   }

   public static void drawAniBush(GameGraphics gameGraphics2, int atackTimeAcc, int bushInd2, int bushBaseX2, int bushBaseY2, int bushLatency2, int fromX, int fromY, int watchDirection2, int toX, int toY, int toTargetHeight) {
      if (bushInd2 >= 0) {
         if (atackTimeAcc >= bushLatency2) {
            int c_all_apply_atacks_time;
            if ((c_all_apply_atacks_time = Battle.c_all_apply_atacks_time - bushLatency2) > 0) {
               atackTimeAcc -= bushLatency2;
               bushLatency2 = 0;
               switch (watchDirection2) {
                  case 0:
                     bushLatency2 = bushBaseX2;
                     break;
                  case 1:
                     bushLatency2 = -bushBaseX2;
               }

               bushBaseX2 = toX - (fromX + bushLatency2);
               watchDirection2 = toY + toTargetHeight / 2 - (fromY + bushBaseY2);
               boolean isRight = bushBaseX2 > 0;
               bushBaseX2 = fromX + bushLatency2 + bushBaseX2 * Math.min(atackTimeAcc, c_all_apply_atacks_time) / c_all_apply_atacks_time;
               watchDirection2 = fromY + bushBaseY2 + watchDirection2 * Math.min(atackTimeAcc, c_all_apply_atacks_time) / c_all_apply_atacks_time;
               AnimSprite animSprite;
               (animSprite = ImagesGlobal.animClientSprites[Battle.c_atack_images_inds[bushInd2]]).draw(gameGraphics2, (long)atackTimeAcc, false, bushBaseX2 - animSprite.swHalf, watchDirection2 - animSprite.shHalf, isRight);
            }
         }
      }
   }

   private void drawEffects(GameGraphics gameGraphics2, int dx, int dy, Battle battle2) {
      int dy2 = dy;
      int dx2 = dx;
      GameGraphics gameGraphics3 = gameGraphics2;
      BattleUnit battleUnit = this;
      int drawHeartEffectsListIndex;
      if (!this.Dead) {
         drawHeartEffectsListIndex = this.drawBuffsList.size() - 1;

         while(drawHeartEffectsListIndex >= 0) {
            BattleDrawBuffEffect battleDrawBuffEffect;
            switch ((battleDrawBuffEffect = (BattleDrawBuffEffect)battleUnit.drawBuffsList.elementAt(drawHeartEffectsListIndex)).type) {
               case 0:
                  gameGraphics3.drawImage(battleDrawBuffEffect.image, dx2 + battleDrawBuffEffect.baseXOffset, dy2 + battleDrawBuffEffect.baseYOffset - (battleDrawBuffEffect.curTime < 1000 ? 0 : battleDrawBuffEffect.animVertOffset * (battleDrawBuffEffect.curTime - 1000) / (battleDrawBuffEffect.reqTime - 1000)), 36);
               default:
                  --drawHeartEffectsListIndex;
            }
         }
      }

      drawHeartEffectsListIndex = 0;

      for(int drawHeartEffectsListCount = battleUnit.drawHeartEffectsList.size(); drawHeartEffectsListIndex < drawHeartEffectsListCount; ++drawHeartEffectsListIndex) {
         int effectDrawType;
         if ((effectDrawType = ((BattleDrawHeartEffect)battleUnit.drawHeartEffectsList.elementAt(drawHeartEffectsListIndex)).draw(Com.cameraBattle, gameGraphics3, dx2, dy2)) != 0) {
            for(int heartEffectIndex = 0; heartEffectIndex < drawHeartEffectsListIndex; ++heartEffectIndex) {
               BattleDrawHeartEffect battleDrawHeartEffect = (BattleDrawHeartEffect)battleUnit.drawHeartEffectsList.elementAt(heartEffectIndex);
               battleDrawHeartEffect.toYOffset += effectDrawType;
            }
         }
      }

      if (!this.Dead) {
         int iconParalized = 0;
         if (this.btl_Paralize) {
            iconParalized = 0 + ImagesGlobal.iconParalized.getHeight();
            ++iconParalized;
         }

         if (this.btl_ProtectionTime > 0) {
            iconParalized += ImagesGlobal.effectIconProtection.getHeight();
            ++iconParalized;
         }

         if (this.btl_StandTime > 0) {
            iconParalized += ImagesGlobal.effectIconStand.getHeight();
            ++iconParalized;
         }

         if (this.btl_DesarmTime > 0) {
            iconParalized += ImagesGlobal.effectIconDesarm.getHeight();
            ++iconParalized;
         }

         if (iconParalized > 0) {
            --iconParalized;
         }

         int unitHorizHalf = Legionery.getUnitHorizHalf(this.drawType, this.BodyInd, this.HeadInd, this.strategGraphic);
         if (-(dx2 = this.getVertOffsetForEffects(3)) < iconParalized) {
            dx2 = -iconParalized;
         }

         if (this.btl_Paralize) {
            gameGraphics2.drawImage(ImagesGlobal.iconParalized, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.iconParalized.getHeight();
         }

         if (this.btl_ProtectionTime > 0) {
            gameGraphics2.drawImage(ImagesGlobal.effectIconProtection, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.effectIconProtection.getHeight();
         }

         if (this.btl_StandTime > 0) {
            gameGraphics2.drawImage(ImagesGlobal.effectIconStand, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.effectIconStand.getHeight();
         }

         if (this.btl_DesarmTime > 0) {
            gameGraphics2.drawImage(ImagesGlobal.effectIconDesarm, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.effectIconDesarm.getHeight();
         }

         if (this.btl_Poisoned) {
            gameGraphics2.drawImage(ImagesGlobal.iconPoisoned, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.iconPoisoned.getHeight();
         }

         if (this.btl_AcidPower != 0) {
            gameGraphics2.drawImage(ImagesGlobal.heartAcid, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.heartAcid.getHeight();
         }

         if (this.btl_RegenPower != 0) {
            gameGraphics2.drawImage(ImagesGlobal.heartRegen, dx + unitHorizHalf, dy + dx2, 0);
            dx2 += ImagesGlobal.heartRegen.getHeight();
         }

         BattleMemberSmall battleMemberSmall;
         if (this.playersAvatar && (battleMemberSmall = battle2.getBattleMember(this.hisBattleMemberId)) != null && battleMemberSmall.stopedItems.size() > 0) {
            gameGraphics2.drawImage(ImagesGlobal.iconStopSkills, dx + unitHorizHalf, dy + dx2, 0);
            ImagesGlobal.iconStopSkills.getHeight();
         }
      }

   }

   private int getSpriteForDying() {
      return this.playersAvatar ? 35 : 4;
   }

   public final int getCoverType(Battle battle2) {
      if (battle2.getPlayerMember() == null) {
         return -1;
      } else if (this.hisBattleMemberId == battle2.getPlayerMember().id) {
         return 2;
      } else {
         return this.side == battle2.getPlayerMember().side ? 0 : 1;
      }
   }

   public final void draw(GameGraphics gameGraphics2, int visBattleDrawUndoneAct, Battle battle2) {
      int drawX = this.cx + this.addXOffHit;
      int drawY = this.cy + this.addYOffHit;
      if (!this.Dead) {
         if (battle2 != null) {
            switch (this.getCoverType(battle2)) {
               case 0:
                  ImagesGlobal.addingAnimSprites[13].drawCenter(gameGraphics2, drawX, drawY, 0);
                  break;
               case 1:
                  ImagesGlobal.addingAnimSprites[12].drawCenter(gameGraphics2, drawX, drawY, 0);
                  break;
               case 2:
                  ImagesGlobal.addingAnimSprites[14].drawCenter(gameGraphics2, drawX, drawY, 0);
            }
         }

         byte horizontalBounce = 0;
         byte verticalBounce = 0;
         if (this.shakeActive) {
            switch (this.watchDirection) {
               case 0:
                  horizontalBounce = -1;
                  verticalBounce = -1;
                  break;
               case 1:
                  horizontalBounce = 1;
                  verticalBounce = -1;
            }
         }

         int topPadding = 0;
         int spriteX;
         int spriteY;
         int maximum;
         int maximum2;
         switch (this.drawType) {
            case 0:
               byte attackOffset = 0;
               spriteY = 0;
               if (this.ai_Atacking) {
                  if (this.atk_TimeAcc / 333 % 2 == 1) {
                     spriteY = 1;
                  }
               } else if (this.isWalkOrStay) {
                  if (this.walkStayAnimationTime / 200L % 2L == 0L) {
                     attackOffset = -2;
                  }
               } else if (this.walkStayAnimationTime / 333L % 2L == 1L) {
                  spriteY = 1;
               }

               Legionery.draw(gameGraphics2, drawX + horizontalBounce, drawY, this.BodyInd, this.HeadInd, this.watchDirection, attackOffset + verticalBounce - this.cz, spriteY);
               topPadding = Legionery.getPaddings(this.BodyInd, this.HeadInd, this.watchDirection)[2];
               break;
            case 1:
               StrategGraphic strategGraphic2 = this.strategGraphic;
               int cx2 = drawX + horizontalBounce;
               byte watchDirection2 = this.watchDirection;
               byte currentAnimationState = this.getCurrentAnimationState();
               long currentAnimationTime = this.getCurrentAnimationTime();
               boolean useIdleAnimation = !this.ai_Atacking;
               maximum2 = (short)(verticalBounce - this.cz);
               boolean idleAnimation = useIdleAnimation;
               long currentAnimationTime2 = currentAnimationTime;
               byte currentAnimationState2 = currentAnimationState;
               maximum = watchDirection2;
               spriteX = cx2;
               StrategGraphic strategGraphic3 = strategGraphic2;
               StrategGraphic.draw(gameGraphics2, strategGraphic3.itemsSprites, strategGraphic3.auraInd, spriteX, drawY, (byte)maximum, currentAnimationState2, currentAnimationTime2, idleAnimation, (short)maximum2, false);
               topPadding = this.strategGraphic.getPaddings(this.watchDirection, StrategAnimGroup.ANIMATION_STATE_STAY, false)[2];
         }

         if (this.drawHeartInd >= 0) {
            AnimSprite animSprite;
            (animSprite = ImagesGlobal.animClientSprites[this.drawHeartInd]).draw(gameGraphics2, (long)this.drawHeartTime, false, drawX - animSprite.swHalf, drawY - topPadding / 2 - animSprite.swHalf, this.drawHeartFromSide == 0);
         }

         boolean hasBattle2 = battle2 != null && this.side != battle2.getPlayerMember().side;
         spriteY = drawY + Legionery.getUnitInfoVerticalOffset(this.drawType, this.BodyInd, this.HeadInd, this.strategGraphic);
         long maximum3 = Math.max(0L, Math.min(this.CurCount, this.curHealthMaxCount));
         if (this.HigherLegi) {
            gameGraphics2.setColor(0);
            int healthBarHeight = hasBattle2 ? 1 : 2;
            gameGraphics2.fillRect(drawX - 10, spriteY - healthBarHeight, 20, healthBarHeight);
            long maximum4 = Math.max(this.curHealthMaxCount, 1L);
            int maximum5;
            if (maximum3 > maximum4 / 2L) {
               maximum2 = 255;
               maximum5 = Math.max(0, (int)(-maximum3 * 255L / maximum4 + 255L));
            } else {
               maximum5 = 255;
               maximum2 = Math.max(0, (int)((maximum3 * 255L << 1) / maximum4));
            }

            gameGraphics2.setColor(maximum5, maximum2, 0);
            maximum = Math.max(0, (int)(1L + 19L * maximum3 / maximum4));
            spriteX = drawX - 10;
            spriteY -= healthBarHeight;
            gameGraphics2.fillRect(spriteX, spriteY, maximum, healthBarHeight);
            if (this.healthAnimFromCount >= 0L) {
               maximum5 = Math.max(0, (int)(1L + 19L * Math.abs(this.healthAnimFromCount - maximum3) / maximum4));
               if (this.healthAnimTimeAccum >= 600L) {
                  maximum5 = (int)((long)maximum5 * (600L - (this.healthAnimTimeAccum - 600L)) / 600L);
               }

               if (this.healthAnimFromCount >= maximum3) {
                  gameGraphics2.setColor(213, 28, 13);
                  gameGraphics2.fillRect(spriteX + maximum, spriteY, maximum5, healthBarHeight);
               } else {
                  gameGraphics2.setColor(18, 121, 15);
                  gameGraphics2.fillRect(spriteX + maximum - maximum5, spriteY, maximum5, healthBarHeight);
               }
            }

            gameGraphics2.setColor(16777215);
            gameGraphics2.fillRect(spriteX + maximum - 1, spriteY - 1, 1, healthBarHeight + 2);
         } else {
            Com.fontDigitGame.writeText(gameGraphics2, String.valueOf(maximum3), drawX + -10, spriteY - Com.c_font_width + 3);
         }

         if (battle2 != null && this.playersAvatar && this.hisBattleMemberId == battle2.getPlayerMember().id && visBattleDrawUndoneAct >= 0) {
            GameImage gameImage;
            if ((gameImage = ImagesGlobal.battleAtacksIconsOverHead[visBattleDrawUndoneAct]) != null) {
               gameGraphics2.drawImage(gameImage, drawX, drawY - topPadding - 2, 33);
            } else {
               visBattleDrawUndoneAct = Battle.c_atack_images_inds[visBattleDrawUndoneAct];
               ImagesGlobal.animClientSprites[visBattleDrawUndoneAct].draw(gameGraphics2, 0, drawX - ImagesGlobal.animClientSprites[visBattleDrawUndoneAct].swHalf, drawY - topPadding - 2 - ImagesGlobal.animClientSprites[visBattleDrawUndoneAct].sh);
            }
         }

         this.drawEffects(gameGraphics2, drawX, drawY, battle2);
      } else {
         if (!ImagesGlobal.animClientSprites[this.getSpriteForDying()].isLastFrameByTime(this.deadTimer)) {
            ImagesGlobal.addingAnimSprites[1].drawCenter(gameGraphics2, this.cx, this.cy, 0);
            AnimSprite animSprite2;
            (animSprite2 = ImagesGlobal.animClientSprites[this.getSpriteForDying()]).draw(gameGraphics2, (long)this.deadTimer, false, this.cx - animSprite2.swHalf, this.cy - animSprite2.sh + 9, this.watchDirection == 0);
         } else {
            GameImage gameImage2 = this.playersAvatar ? ImagesGlobal.deadHero : ImagesGlobal.deadMob;
            gameGraphics2.drawRegion(gameImage2, 0, 0, gameImage2.getWidth(), gameImage2.getHeight(), this.watchDirection == 0 ? 2 : 0, this.cx - gameImage2.getWidth() / 2, this.cy - gameImage2.getHeight() + 9);
         }

         this.drawEffects(gameGraphics2, drawX, drawY, battle2);
      }
   }

   private long getCurrentAnimationTime() {
      return this.ai_Atacking ? (long)this.atk_TimeAcc : this.walkStayAnimationTime;
   }

   private byte getCurrentAnimationState() {
      if (this.ai_Atacking) {
         return StrategAnimGroup.ANIMATION_STATE_ATACK;
      } else {
         return this.isWalkOrStay ? StrategAnimGroup.ANIMATION_STATE_WALK : StrategAnimGroup.ANIMATION_STATE_STAY;
      }
   }

   public final int[] getPaddings() {
      if (this.watchDirection == 1) {
         if (this.paddingsCachLeft == null) {
            switch (this.drawType) {
               case 0:
                  this.paddingsCachLeft = Legionery.getPaddings(this.BodyInd, this.HeadInd, this.watchDirection);
                  break;
               case 1:
                  this.paddingsCachLeft = this.strategGraphic.getPaddings(this.watchDirection, StrategAnimGroup.ANIMATION_STATE_STAY, false);
                  break;
               default:
                  this.paddingsCachLeft = new int[]{Tile.widthHalf, Tile.widthHalf, Tile.height, 0};
            }
         }

         return this.paddingsCachLeft;
      } else if (this.watchDirection == 0) {
         if (this.paddingsCachRight == null) {
            switch (this.drawType) {
               case 0:
                  this.paddingsCachRight = Legionery.getPaddings(this.BodyInd, this.HeadInd, this.watchDirection);
                  break;
               case 1:
                  this.paddingsCachRight = this.strategGraphic.getPaddings(this.watchDirection, StrategAnimGroup.ANIMATION_STATE_STAY, false);
                  break;
               default:
                  this.paddingsCachRight = new int[]{Tile.widthHalf, Tile.widthHalf, Tile.height, 0};
            }
         }

         return this.paddingsCachRight;
      } else {
         return new int[4];
      }
   }

   public final boolean isPixelTransporant(int offX, int offY) {
      switch (this.drawType) {
         case 0:
            return Legionery.isPixelTransporant(offX, offY, this.BodyInd, this.HeadInd, this.watchDirection, 0, 0, false);
         case 1:
            return this.strategGraphic.isPixelTransporant(offX, offY, this.watchDirection, this.getCurrentAnimationState(), this.getCurrentAnimationTime(), !this.ai_Atacking, false, 0, false);
         default:
            return true;
      }
   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.hisInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.playersAvatar = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.side = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.CurCount = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.ai_AtackTimeSpend = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.ai_Atacking = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.ai_FirstTarget = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.moveLastDirection8D = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.moveAccumTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.cdH = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.cdW = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.cdHW = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.cdHW = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.executableDistPunch = new BattleDistPunch(dataInputStream);
      } else {
         this.executableDistPunch = null;
      }

      this.pathFolower.activeMovePathCurIndex = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.pathFolower.activeMovePath.removeAllElements();
      short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);

      for(int shortCodeIndex = 0; shortCodeIndex < shortCode; ++shortCodeIndex) {
         this.pathFolower.activeMovePath.addElement(new short[]{decompiledsupport.UncheckedDataInput.readShort(dataInputStream), decompiledsupport.UncheckedDataInput.readShort(dataInputStream)});
      }

      this.pathFolowedToTargetTileX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.pathFolowedToTargetTileY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.forceRebuildPath = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.iterationStayingForBouncing = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.iterationForBouncingCanAtack = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.iterationForBouncingCanAtackPointX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.iterationForBouncingCanAtackPointY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.bouncingPartialLeft = (long)decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.bouncingSenseHWidth = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.bouncingSenseHHeight = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.ai_targetAtackUnit = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.ai_targetMoveUnit = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.ai_targetMoveX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.ai_targetMoveY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.btl_freezResist = (int)decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.btl_HealAccum = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.btl_HitAccum = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.btl_InertLastTimeX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_InertLastTimeY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_InertMaxTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_InertTimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_Inerted = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.btl_StandTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_DesarmTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_StopFlyTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_ParalTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_Paralize = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.btl_Poisoned = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.btl_PoisPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_PoisTimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_AcidPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_AcidTimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_RegenPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_RegenTimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_StayTimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_InertVect[0] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_InertVect[1] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_ProtectionTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_ProtectionPower = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToPointTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToPointBaseTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToFromX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToFromY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToToX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btl_FlyToToY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.atk_Target = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.atk_TimeAcc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.cx = (short)decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.cy = (short)decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.drawType = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.bushInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.bushBaseX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.bushBaseY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.bushLatency = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.BodyInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.HeadInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.LeftRight = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.hisBattleMemberId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.HigherType = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.CurCount = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.MaxCount = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      BattleParams battleParams;
      (battleParams = this.params).speedMsPix2P[0] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.speedMsPix2P[1] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.speedCleanMsPix2P[0] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.speedCleanMsPix2P[1] = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.timeBetweenHitsMs = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.hitRangePix = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.damageIncForNearFriendsPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.damageDecForNearEnemysPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.hitPower0P = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.answerPowerPerc = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.pushPowerPix = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.atackValue = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.defenceValue = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.pullPowerPix = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.poisonPower0P = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.paralizeTimeMs = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      battleParams.isArcher = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      battleParams.isJump = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      battleParams.isInvisible = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      battleParams.isFighter = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.Level = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.resetHealthAnimation();
      this.changeDamage = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeHealth = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeAtack = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeDefence = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeSpeed = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeMove = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.changeRange = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.Name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.HeadLorR = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.HigherLegi = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.Dead = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.deadTimer = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.Active = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.walkStayAnimationTime = decompiledsupport.UncheckedDataInput.readLong(dataInputStream);
      this.isWalkOrStay = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.watchDirection = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.strategGraphic.readFromNet(dataInputStream);
      this.transfromationActive = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.transformationLeftTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      if (decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream)) {
         this.transformationBackUnit = new BattleUnit(dataInputStream);
      } else {
         this.transformationBackUnit = null;
      }

      this.curHealthMaxCount = Battle.percentLogic(this.MaxCount, this.changeHealth);
      this.drawInd = this.hisInd;
   }

   public final BattleUnit cloneTo(BattleUnit battleUnit) {
      battleUnit.moveAccumTime = super.moveAccumTime;
      battleUnit.moveLastDirection8D = super.moveLastDirection8D;
      battleUnit.cdHW = super.cdHW;
      battleUnit.cdHH = super.cdHH;
      battleUnit.cx = super.cx;
      battleUnit.cy = super.cy;
      battleUnit.cdH = super.cdH;
      battleUnit.cdW = super.cdW;
      battleUnit.cz = super.cz;
      battleUnit.addXOffHit = this.addXOffHit;
      battleUnit.addYOffHit = this.addYOffHit;
      battleUnit.leftOffHitTime = this.leftOffHitTime;
      battleUnit.drawType = this.drawType;
      battleUnit.playersAvatar = this.playersAvatar;
      battleUnit.bushInd = this.bushInd;
      battleUnit.bushBaseX = this.bushBaseX;
      battleUnit.bushBaseY = this.bushBaseY;
      battleUnit.bushLatency = this.bushLatency;
      battleUnit.BodyInd = this.BodyInd;
      battleUnit.HeadInd = this.HeadInd;
      battleUnit.HeadLorR = this.HeadLorR;
      battleUnit.LeftRight = this.LeftRight;
      battleUnit.hisBattleMemberId = this.hisBattleMemberId;
      battleUnit.Name = this.Name;
      battleUnit.HigherLegi = this.HigherLegi;
      battleUnit.HigherType = this.HigherType;
      battleUnit.CurCount = this.CurCount;
      battleUnit.MaxCount = this.MaxCount;
      battleUnit.curHealthMaxCount = this.curHealthMaxCount;
      battleUnit.Level = this.Level;
      battleUnit.healthAnimTimeAccum = this.healthAnimTimeAccum;
      battleUnit.healthAnimFromCount = this.healthAnimFromCount;
      battleUnit.params = this.params.cloneTo();
      battleUnit.Dead = this.Dead;
      battleUnit.deadTimer = this.deadTimer;
      battleUnit.Active = this.Active;
      battleUnit.Drawed = this.Drawed;
      battleUnit.walkStayAnimationTime = this.walkStayAnimationTime;
      battleUnit.isWalkOrStay = this.isWalkOrStay;
      battleUnit.watchDirection = this.watchDirection;
      battleUnit.ai_targetAtackUnit = this.ai_targetAtackUnit;
      battleUnit.ai_targetMoveUnit = this.ai_targetMoveUnit;
      battleUnit.ai_targetMoveX = this.ai_targetMoveX;
      battleUnit.ai_targetMoveY = this.ai_targetMoveY;
      battleUnit.executableDistPunch = this.executableDistPunch == null ? null : this.executableDistPunch.cloneTo();
      PathFolower pathFolower2 = this.pathFolower;
      PathFolower pathFolower3;
      (pathFolower3 = new PathFolower()).activeMovePathCurIndex = pathFolower2.activeMovePathCurIndex;

      int i;
      for(i = 0; i < pathFolower2.activeMovePath.size(); ++i) {
         pathFolower3.activeMovePath.addElement(pathFolower2.activeMovePath.elementAt(i));
      }

      battleUnit.pathFolower = pathFolower3;
      battleUnit.pathFolowedToTargetTileX = this.pathFolowedToTargetTileX;
      battleUnit.pathFolowedToTargetTileY = this.pathFolowedToTargetTileY;
      battleUnit.forceRebuildPath = this.forceRebuildPath;
      battleUnit.iterationStayingForBouncing = this.iterationStayingForBouncing;
      battleUnit.iterationForBouncingCanAtack = this.iterationForBouncingCanAtack;
      battleUnit.iterationForBouncingCanAtackPointX = this.iterationForBouncingCanAtackPointX;
      battleUnit.iterationForBouncingCanAtackPointY = this.iterationForBouncingCanAtackPointY;
      battleUnit.bouncingPartialLeft = this.bouncingPartialLeft;
      battleUnit.bouncingSenseHWidth = this.bouncingSenseHWidth;
      battleUnit.bouncingSenseHHeight = this.bouncingSenseHHeight;
      battleUnit.ai_Atacking = this.ai_Atacking;
      battleUnit.ai_FirstTarget = this.ai_FirstTarget;
      battleUnit.ai_AtackTimeSpend = this.ai_AtackTimeSpend;
      battleUnit.atk_Target = this.atk_Target;
      battleUnit.atk_TimeAcc = this.atk_TimeAcc;
      battleUnit.btl_freezResist = this.btl_freezResist;
      battleUnit.btl_Paralize = this.btl_Paralize;
      battleUnit.btl_ParalTime = this.btl_ParalTime;
      battleUnit.btl_StandTime = this.btl_StandTime;
      battleUnit.btl_DesarmTime = this.btl_DesarmTime;
      battleUnit.btl_StopFlyTime = this.btl_StopFlyTime;
      battleUnit.btl_ProtectionTime = this.btl_ProtectionTime;
      battleUnit.btl_ProtectionPower = this.btl_ProtectionPower;
      battleUnit.btl_FlyToPointTime = this.btl_FlyToPointTime;
      battleUnit.btl_FlyToPointBaseTime = this.btl_FlyToPointBaseTime;
      battleUnit.btl_FlyToFromX = this.btl_FlyToFromX;
      battleUnit.btl_FlyToFromY = this.btl_FlyToFromY;
      battleUnit.btl_FlyToToX = this.btl_FlyToToX;
      battleUnit.btl_FlyToToY = this.btl_FlyToToY;
      battleUnit.btl_Poisoned = this.btl_Poisoned;
      battleUnit.btl_PoisPower = this.btl_PoisPower;
      battleUnit.btl_PoisTimeAcc = this.btl_PoisTimeAcc;
      battleUnit.btl_AcidPower = this.btl_AcidPower;
      battleUnit.btl_AcidTimeAcc = this.btl_AcidTimeAcc;
      battleUnit.btl_RegenPower = this.btl_RegenPower;
      battleUnit.btl_RegenTimeAcc = this.btl_RegenTimeAcc;
      battleUnit.btl_StayTimeAcc = this.btl_StayTimeAcc;
      battleUnit.btl_Inerted = this.btl_Inerted;
      battleUnit.btl_InertTimeAcc = this.btl_InertTimeAcc;
      battleUnit.btl_InertMaxTime = this.btl_InertMaxTime;
      battleUnit.btl_InertLastTimeX = this.btl_InertLastTimeX;
      battleUnit.btl_InertLastTimeY = this.btl_InertLastTimeY;
      battleUnit.btl_InertVect[0] = this.btl_InertVect[0];
      battleUnit.btl_InertVect[1] = this.btl_InertVect[1];
      battleUnit.btl_HitAccum = this.btl_HitAccum;
      battleUnit.btl_HealAccum = this.btl_HealAccum;
      battleUnit.changeDamage = this.changeDamage;
      battleUnit.changeHealth = this.changeHealth;
      battleUnit.changeAtack = this.changeAtack;
      battleUnit.changeDefence = this.changeDefence;
      battleUnit.changeSpeed = this.changeSpeed;
      battleUnit.changeMove = this.changeMove;
      battleUnit.changeRange = this.changeRange;
      battleUnit.transfromationActive = this.transfromationActive;
      battleUnit.transformationLeftTime = this.transformationLeftTime;
      if (this.transformationBackUnit == null) {
         battleUnit.transformationBackUnit = null;
      } else {
         battleUnit.transformationBackUnit = this.transformationBackUnit.cloneTo(new BattleUnit((DataInputStream)null));
      }

      battleUnit.shakeActive = this.shakeActive;
      battleUnit.shakeTime = this.shakeTime;

      for(i = 0; i < this.drawBuffsList.size(); ++i) {
         Vector drawBuffsList2 = battleUnit.drawBuffsList;
         BattleDrawBuffEffect battleDrawBuffEffect = (BattleDrawBuffEffect)this.drawBuffsList.elementAt(i);
         BattleDrawBuffEffect battleDrawBuffEffect2;
         (battleDrawBuffEffect2 = new BattleDrawBuffEffect((GameImage)null, 0, 0)).image = battleDrawBuffEffect.image;
         battleDrawBuffEffect2.baseYOffset = battleDrawBuffEffect.baseYOffset;
         battleDrawBuffEffect2.baseXOffset = battleDrawBuffEffect.baseXOffset;
         battleDrawBuffEffect2.animVertOffset = battleDrawBuffEffect.animVertOffset;
         battleDrawBuffEffect2.reqTime = battleDrawBuffEffect.reqTime;
         battleDrawBuffEffect2.curTime = battleDrawBuffEffect.curTime;
         battleDrawBuffEffect2.type = battleDrawBuffEffect.type;
         drawBuffsList2.addElement(battleDrawBuffEffect2);
      }

      for(i = 0; i < this.drawHeartEffectsList.size(); ++i) {
         battleUnit.drawHeartEffectsList.addElement(((BattleDrawHeartEffect)this.drawHeartEffectsList.elementAt(i)).cloneTo());
      }

      battleUnit.drawHeartInd = this.drawHeartInd;
      battleUnit.drawHeartTime = this.drawHeartTime;
      battleUnit.drawHeartFromSide = this.drawHeartFromSide;
      battleUnit.hisInd = this.hisInd;
      battleUnit.side = this.side;
      battleUnit.drawInd = this.drawInd;
      battleUnit.lastBufPosVert = this.lastBufPosVert;
      battleUnit.lastBufMaxHeight = this.lastBufMaxHeight;
      battleUnit.lastBufWidth = this.lastBufWidth;
      battleUnit.strategGraphic = this.strategGraphic.makeClone();
      if (this.paddingsCachLeft == null) {
         battleUnit.paddingsCachLeft = null;
      } else {
         battleUnit.paddingsCachLeft = new int[this.paddingsCachLeft.length];
         System.arraycopy(this.paddingsCachLeft, 0, battleUnit.paddingsCachLeft, 0, this.paddingsCachLeft.length);
      }

      if (this.paddingsCachRight == null) {
         battleUnit.paddingsCachRight = null;
      } else {
         battleUnit.paddingsCachRight = new int[this.paddingsCachRight.length];
         System.arraycopy(this.paddingsCachRight, 0, battleUnit.paddingsCachRight, 0, this.paddingsCachRight.length);
      }

      return battleUnit;
   }
}
