package battlepck;

import Base.ImagesGlobal;
import Base.Com;
import Base.GameSimpleObjectHighlight;
import Engine.AnimSprite;
import Engine.Camera;
import Engine.MRandom;
import Engine.MathGame;
import Engine.Mystery;
import Moduls.MoveableUnitDiscretShort;
import Moduls.PathFolower;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.DrawOrderList;
import battlepck.client.BattleDrawBuffEffect;
import battlepck.client.BattleDrawHeartEffect;
import battlepck.client.BattleDrawUnitOrderElement;
import battlepck.client.BattleOffscreenIcon;
import battlepck.client.BattleSkillCircle;
import battlepck.client.BattleTh;
import java.io.DataInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class Battle {
   public static final int[] ENEMY_SIDE_FAST = new int[]{1, 0};
   public static int c_unit_jump_height = 30;
   public static int c_unit_death_time_before_remove = 1500;
   public static int c_unit_atack_start_cooldown_part_1000 = 900;
   public static int c_step_back_allow_time = 20000;
   public static int c_all_apply_atacks_time = 400;
   public static int c_all_finish_atacks_time = 500;
   public static int[] c_btl_fieldSize;
   public static int[] c_battle_field_margins;
   public static int c_battle_time_between_actions_temporal;
   public static int c_rage_distance;
   public static int c_rage_AddVal;
   public static int c_rage_AddTime;
   public static int c_battle_rage_push_min_rnd_d3;
   public static int c_battle_rage_push_max_rnd_d3;
   public static int c_battle_rage_push_100;
   public static int[] c_rage_Bushs;
   public static int[] c_rage_Coofs_int;
   public static int[] c_rage_Levels;
   public static int[] c_rage_Colors;
   public static int c_rage_Add;
   public static int c_rage_Sub;
   public static int c_rage_cost;
   public static int c_fix_ai_refresh_time;
   public static int c_InertSpeed;
   public static int[][] c_JumpOffsets;
   public static int c_HorrorTime;
   public static int c_OkopTime;
   public static int c_JumpPause;
   public static int c_ClosetsDist;
   public static int c_PoisonTime;
   public static int c_battle_InertSpeed;
   public static int c_battle_min_time_between_hits;
   public static int c_AcidTime;
   public static int c_RegenTime;
   public static int c_battle_weak_fire_new;
   public static int c_DyingTime;
   public static int c_JumpSpeed;
   public static int c_JumpDist;
   public static int c_battle_action_random_min;
   public static int c_battle_action_random_max;
   public static final int[] c_atack_images_inds;
   public static final int[] c_heart_images_inds;
   public static int CD_H;
   public static int CD_W;
   public static int CD_HW;
   public static int CD_HH;
   public static int c_battle_add_power_legioner_vs_higher;
   private boolean wasEnd = false;
   private TileMap currentMap;
   private int centerCorrectCoordX;
   private int centerCorrectCoordY;
   private int lastUnitInd = -1;
   private int battleState = 3;
   private MRandom btlRandom;
   private Hashtable members = new Hashtable();
   private BattleMember playerMember;
   private BattleMemberSmall playerMemberSmall;
   private BattleUnit playerAvatar = null;
   private Hashtable units = new Hashtable();
   private Vector unitsInOrder = new Vector();
   private Vector deadmans = new Vector();
   private int battleTime;
   public long lastTickTime = -1L;
   private int curTickInd = 0;
   private int emptyQuantumEventsCompensationCount = 0;
   private int emptyQuantumEventsCompensationConfirmedEmptyQuantsCount = 0;
   private Battle emptyQuantumEventsCompensationSavedBattleState = null;
   private Vector quantumEventsAccumed = new Vector();
   private Vector distPunchs = new Vector();
   private boolean isCanRunFromBattle = true;
   private final Object totalLock;
   private int speedUpForQuantsCount = 0;
   private int[] lastFewLeftEventsCounters = new int[10];
   private int lastFewLeftEventsCountersIndex = 0;
   private Vector accumOrders = new Vector();
   private BattleOrder accumSecondOrder = null;
   private final Object ordersLock = new Object();
   private long lastApplySecondTime = 0L;
   public int drawUnDoneAct = -1;
   public boolean hasUnDoneAct = false;
   public int hasUnDoneActSendNum = 0;
   public int unDoneActTarget = -1;

   public Battle(TileMap tileMap, DataInputStream dataInputStream, Object totalLock2) {
      this.currentMap = tileMap;
      this.totalLock = totalLock2;
      if (dataInputStream != null) {
         this.loadFromStream(dataInputStream);
      }

   }

   public static long percentLogic(long baseVal, int percentChange) {
      if (baseVal == 0L) {
         return 0L;
      } else if (baseVal > 0L) {
         return percentChange < 0 ? Math.max(1L, baseVal * 100000L / (long)(100000 - percentChange)) : baseVal * (long)(percentChange + 100000) / 100000L;
      } else {
         return percentChange < 0 ? Math.min(-1L, baseVal * 100000L / (long)(100000 - percentChange)) : baseVal * (long)(percentChange + 100000) / 100000L;
      }
   }

   private void addUnitToBattle(BattleMemberSmall battleMemberSmall, BattleUnit battleUnit, int[] coord) {
      ++this.lastUnitInd;
      battleUnit.side = battleMemberSmall.side;
      battleUnit.hisInd = this.lastUnitInd;
      battleUnit.HeadLorR = battleMemberSmall.side == 1;
      battleUnit.LeftRight = battleMemberSmall.side;
      battleUnit.hisBattleMemberId = battleMemberSmall.id;
      battleUnit.btl_freezResist = battleMemberSmall.gpEffects[13];
      battleUnit.ai_AtackTimeSpend = (int)(battleUnit.getRealTimeBetweenHitsMs() * (long)c_unit_atack_start_cooldown_part_1000 / 1000L);
      if (battleUnit.bushInd == -2) {
         battleUnit.bushInd = battleMemberSmall.atackSprite;
         if (battleUnit.bushInd == 0 && battleUnit.params.isArcher) {
            battleUnit.bushInd = 1;
         } else if (battleUnit.bushInd == 0) {
            battleUnit.bushInd = 2;
         }
      }

      battleUnit.watchDirection = (byte)(battleMemberSmall.side % 2 == 0 ? 0 : 1);
      battleUnit.cx = (short)coord[0];
      battleUnit.cy = (short)coord[1];
      if (battleUnit.playersAvatar && battleUnit.hisBattleMemberId == this.playerMember.id) {
         this.playerAvatar = battleUnit;
      }

      this.units.put(battleUnit.hisInd, battleUnit);
      this.unitsInOrder.addElement(battleUnit);
   }

   private void addDeadman(short cx, short cy, boolean playersAvatar, byte watchDirection, boolean animate) {
      this.deadmans.addElement(new BattleDeadman(cx, cy, playersAvatar, watchDirection, animate));
      if (this.deadmans.size() > 10) {
         this.deadmans.removeElementAt(0);
      }

   }

   public final void processActiveState() {
      synchronized(this.totalLock) {
         switch (this.battleState) {
            case 3:
               Battle battle2 = this;
               if (this.lastTickTime == -1L) {
                  this.lastTickTime = Mystery.currentTimeMillis;
               }

               long lastTickTime2 = Mystery.currentTimeMillis - this.lastTickTime;
               int speedUpForQuantsCount2 = this.speedUpForQuantsCount > 0 ? 25 : 50;

               while(lastTickTime2 >= (long)speedUpForQuantsCount2) {
                  battle2.lastTickTime += (long)speedUpForQuantsCount2;
                  lastTickTime2 -= (long)speedUpForQuantsCount2;
                  if (battle2.speedUpForQuantsCount > 0) {
                     --battle2.speedUpForQuantsCount;
                  }

                  if (battle2.advanceBattleTick(50)) {
                  }
               }
            default:
         }
      }
   }

   private boolean advanceBattleTick(int elapsedTime) {
      Battle battle2;
      BattleQuantumEvent battleQuantumEvent;
      if (this.quantumEventsAccumed.size() == 0) {
         if (this.wasEnd) {
            battleQuantumEvent = new BattleQuantumEvent(new BattleEvent[0]);
         } else {
            if (this.emptyQuantumEventsCompensationCount >= 10) {
               return false;
            }

            if (this.emptyQuantumEventsCompensationCount < 0) {
               ++this.emptyQuantumEventsCompensationCount;
               return false;
            }

            boolean successCopy = true;

            try {
               if (this.emptyQuantumEventsCompensationCount == 0) {
                  battle2 = new Battle(this.currentMap, (DataInputStream)null, this.totalLock);
                  this.copyForStoreRestoreTo(battle2);
                  this.emptyQuantumEventsCompensationSavedBattleState = battle2;
                  this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount = 0;
               }
            } catch (Exception exception) {
               exception.printStackTrace();
               successCopy = false;
            }

            if (!successCopy) {
               return false;
            }

            battleQuantumEvent = new BattleQuantumEvent(new BattleEvent[0]);
            ++this.emptyQuantumEventsCompensationCount;
         }
      } else {
         battleQuantumEvent = (BattleQuantumEvent)this.quantumEventsAccumed.elementAt(0);
         this.quantumEventsAccumed.removeElementAt(0);
         if (battleQuantumEvent == null) {
            this.wasEnd = true;
            battleQuantumEvent = new BattleQuantumEvent(new BattleEvent[0]);
         }

         if (this.emptyQuantumEventsCompensationCount <= 0) {
            this.emptyQuantumEventsCompensationCount = 0;
         }
      }

      battleQuantumEvent.applyEvents(this, 0);
      byte realTick = 50;
      Enumeration enumeration = this.members.elements();

      while(enumeration.hasMoreElements()) {
         BattleMemberSmall battleMemberSmall;
         if ((battleMemberSmall = (BattleMemberSmall)enumeration.nextElement()).stopRageTime > 0) {
            battleMemberSmall.stopRageTime -= realTick;
            if (battleMemberSmall.stopRageTime <= 0) {
               battleMemberSmall.stopRageTime = 0;
            }
         }

         for(int stopedItemsCount = battleMemberSmall.stopedItems.size() - 1; stopedItemsCount >= 0; --stopedItemsCount) {
            int[] stoppedItem;
            int[] stoppedItemTimer = stoppedItem = (int[])battleMemberSmall.stopedItems.elementAt(stopedItemsCount);
            stoppedItemTimer[0] -= realTick;
            if (stoppedItem[0] <= 0) {
               battleMemberSmall.stopedItems.removeElementAt(stopedItemsCount);
            }
         }
      }

      enumeration = this.playerMember.bottlesBySlot.elements();

      while(enumeration.hasMoreElements()) {
         BattleBottle battleBottle;
         if ((battleBottle = (BattleBottle)enumeration.nextElement()).cooldownTimeCur > 0) {
            battleBottle.cooldownTimeCur = Math.max(0, battleBottle.cooldownTimeCur - realTick);
         }
      }

      this.battleTime += 50;
      battleQuantumEvent.applyEvents(this, 1);
      realTick = 50;
      battle2 = this;
      this.playerMember.avatarEnemysCount = 0;
      this.playerMember.avatarFriendsCount = 0;
      enumeration = this.unitsInOrder.elements();

      while(true) {
         while(enumeration.hasMoreElements()) {
            final BattleUnit currentUnit = (BattleUnit)enumeration.nextElement();
            Object animatedUnit = currentUnit;
            Object buffUnit = currentUnit;
            int effectTickTime = realTick;
            Object effectsUnit = buffUnit;

            int effectIndex;
            boolean effectExpired;
            for(effectIndex = ((BattleUnit)buffUnit).drawBuffsList.size() - 1; effectIndex >= 0; --effectIndex) {
               BattleDrawBuffEffect battleDrawBuffEffect;
               if ((battleDrawBuffEffect = (BattleDrawBuffEffect)((BattleUnit)effectsUnit).drawBuffsList.elementAt(effectIndex)).curTime >= battleDrawBuffEffect.reqTime) {
                  effectExpired = true;
               } else {
                  battleDrawBuffEffect.curTime += effectTickTime;
                  if (battleDrawBuffEffect.curTime > battleDrawBuffEffect.reqTime) {
                     battleDrawBuffEffect.curTime = battleDrawBuffEffect.reqTime;
                  }

                  effectExpired = false;
               }

               if (effectExpired) {
                  ((BattleUnit)effectsUnit).drawBuffsList.removeElementAt(effectIndex);
               }
            }

            for(effectIndex = ((BattleUnit)effectsUnit).drawHeartEffectsList.size() - 1; effectIndex >= 0; --effectIndex) {
               BattleDrawHeartEffect battleDrawHeartEffect;
               if ((battleDrawHeartEffect = (BattleDrawHeartEffect)((BattleUnit)effectsUnit).drawHeartEffectsList.elementAt(effectIndex)).curTime >= 1400) {
                  effectExpired = true;
               } else {
                  battleDrawHeartEffect.curTime += effectTickTime;
                  if (battleDrawHeartEffect.curTime > 1400) {
                     battleDrawHeartEffect.curTime = 1400;
                  }

                  effectExpired = false;
               }

               if (effectExpired) {
                  ((BattleUnit)effectsUnit).drawHeartEffectsList.removeElementAt(effectIndex);
               }
            }

            if (((BattleUnit)animatedUnit).drawHeartInd >= 0) {
               ((BattleUnit)animatedUnit).drawHeartTime += realTick;
               if (ImagesGlobal.animClientSprites[((BattleUnit)animatedUnit).drawHeartInd].isLastFrameByTime(((BattleUnit)animatedUnit).drawHeartTime)) {
                  ((BattleUnit)animatedUnit).drawHeartInd = -1;
               }
            }

            if (((BattleUnit)animatedUnit).healthAnimFromCount >= 0L) {
               ((BattleUnit)animatedUnit).healthAnimTimeAccum += (long)realTick;
               if (((BattleUnit)animatedUnit).healthAnimTimeAccum >= 1200L) {
                  ((BattleUnit)animatedUnit).healthAnimFromCount = -1L;
               }
            }

            if (!((BattleUnit)animatedUnit).Dead) {
               if (((BattleUnit)animatedUnit).shakeActive) {
                  ((BattleUnit)animatedUnit).shakeTime += (long)realTick;
                  if (((BattleUnit)animatedUnit).shakeTime >= 120L) {
                     ((BattleUnit)animatedUnit).shakeActive = false;
                  }
               }

               ((BattleUnit)animatedUnit).leftOffHitTime -= (long)realTick;
               if (((BattleUnit)animatedUnit).leftOffHitTime <= 0L) {
                  ((BattleUnit)animatedUnit).leftOffHitTime = 0L;
                  ((BattleUnit)animatedUnit).addXOffHit = 0;
                  ((BattleUnit)animatedUnit).addYOffHit = 0;
               }
            }

            Object actingUnit = currentUnit;
            Battle battle3 = battle2;
            int[] enemysFriendsCount;
            if (((BattleUnit)currentUnit).playersAvatar && ((BattleUnit)currentUnit).hisBattleMemberId == battle2.playerMember.id) {
               enemysFriendsCount = battle2.getEnemysFriendsCount((BattleUnit)currentUnit);
               battle2.playerMember.avatarEnemysCount = enemysFriendsCount[0];
               battle2.playerMember.avatarFriendsCount = enemysFriendsCount[1];
            }

            ((BattleUnit)currentUnit).cz = 0;
            ((BattleUnit)currentUnit).iterationStayingForBouncing = false;
            ((BattleUnit)currentUnit).iterationForBouncingCanAtack = false;
            if (((BattleUnit)currentUnit).transfromationActive) {
               ((BattleUnit)currentUnit).transformationLeftTime -= realTick;
               if (((BattleUnit)currentUnit).transformationLeftTime <= 0 && ((BattleUnit)currentUnit).transfromationActive) {
                  ((BattleUnit)currentUnit).transformationLeftTime = 0;
                  ((BattleUnit)currentUnit).transfromationActive = false;
                  ((BattleUnit)currentUnit).transfromToUnit(((BattleUnit)currentUnit).transformationBackUnit, true);
                  ((BattleUnit)currentUnit).transformationBackUnit = null;
               }
            }

            BattleUnit battleUnit;
            BattleUnit battleUnit2;
            BattleMemberSmall battleMemberSmall2;
            if (((BattleUnit)currentUnit).executableDistPunch != null) {
               boolean allow = true;
               if (((BattleUnit)currentUnit).executableDistPunch.bottleEffect.distance > 0) {
                  allow = false;
                  int targetX = ((BattleUnit)currentUnit).executableDistPunch.groundX;
                  int targetY = ((BattleUnit)currentUnit).executableDistPunch.groundY;
                  if (((BattleUnit)currentUnit).executableDistPunch.targetInd >= 0 && (battleUnit2 = battle2.getUnit(((BattleUnit)currentUnit).executableDistPunch.targetInd)) != null) {
                     targetX = battleUnit2.cx;
                     targetY = battleUnit2.cy;
                  }

                  int deltaX = targetX - ((BattleUnit)currentUnit).cx;
                  int deltaY = targetY - ((BattleUnit)currentUnit).cy;
                  if (deltaX * deltaX + deltaY * deltaY <= ((BattleUnit)currentUnit).executableDistPunch.bottleEffect.distance * ((BattleUnit)currentUnit).executableDistPunch.bottleEffect.distance) {
                     allow = true;
                  }
               }

               if (allow) {
                  BattleDistPunch battleDistPunch;
                  if ((battleDistPunch = ((BattleUnit)actingUnit).executableDistPunch) != null) {
                     ((BattleUnit)actingUnit).executableDistPunch = null;
                     battleUnit = battle2.getUnit(battleDistPunch.targetInd);
                     if (battleDistPunch.targetInd < 0 || battleUnit != null) {
                        if (battleUnit != null) {
                           battleDistPunch.groundX = battleUnit.cx;
                           battleDistPunch.groundY = battleUnit.cy;
                        }

                        enemysFriendsCount = battle2.correctCoordToAchivable((BattleUnit)actingUnit, battleDistPunch.groundX, battleDistPunch.groundY);
                        battleDistPunch.groundX = enemysFriendsCount[0];
                        battleDistPunch.groundY = enemysFriendsCount[1];
                        battleDistPunch.flyDirect = battleDistPunch.groundX > ((BattleUnit)actingUnit).cx;
                        int facingDeltaX;
                        if (battleDistPunch.atackerInd != battleDistPunch.targetInd && (facingDeltaX = battleDistPunch.groundX - ((BattleUnit)actingUnit).cx) != 0) {
                           ((BattleUnit)actingUnit).watchDirection = (byte)(facingDeltaX > 0 ? 0 : 1);
                        }

                        if (((BattleUnit)actingUnit).hisBattleMemberId == battle2.playerMember.id && battleDistPunch.fromBottleInd >= 0) {
                           if (battle2.playerMember.leftUsesCount > 0) {
                              --battle2.playerMember.leftUsesCount;
                           }

                           BattleBottle battleBottle2;
                           if ((battleBottle2 = (BattleBottle)battle2.playerMember.bottlesBySlot.get(new Byte((byte)battleDistPunch.fromBottleInd))) != null) {
                              --battleBottle2.count;
                              battleBottle2.cooldownTimeCur = battleBottle2.cooldownTimeBase;
                           }
                        }

                        if (battleDistPunch.rageCost > 0) {
                           int rageLevel;
                           if ((rageLevel = (battleMemberSmall2 = (BattleMemberSmall)battle2.members.get(((BattleUnit)actingUnit).hisBattleMemberId)).getRageLevel()) <= 0) {
                              rageLevel = 1;
                           }

                           if (rageLevel >= c_rage_Levels.length) {
                              rageLevel = c_rage_Levels.length - 1;
                           }

                           battleDistPunch.bottleEffect.bushIndex = c_rage_Bushs[rageLevel];
                           battleDistPunch.bottleEffect.hitPower = (int)(percentLogic((long)((BattleUnit)actingUnit).params.hitPower0P, ((BattleUnit)actingUnit).changeDamage) * (long)c_rage_Coofs_int[rageLevel] * (long)(10000 + battleMemberSmall2.gpEffects[11]) / 10000L);
                           long ragePushMultiplier = 1000L;
                           if (c_battle_rage_push_min_rnd_d3 <= c_battle_rage_push_max_rnd_d3) {
                              long randomValue = battle2.btlRandom.nextInt(10001L);
                              ragePushMultiplier = (long)c_battle_rage_push_min_rnd_d3 + randomValue * (long)(c_battle_rage_push_max_rnd_d3 - c_battle_rage_push_min_rnd_d3) / 10000L;
                           }

                           long rage = (long)battleMemberSmall2.rage * (long)c_battle_rage_push_100 * ragePushMultiplier;
                           int[] c_rage_Levels2 = c_rage_Levels;
                           battleDistPunch.bottleEffect.punchPower = (int)(rage / (long)c_rage_Levels2[c_rage_Levels2.length - 1] / 1000L);
                           battleMemberSmall2.rage = 0;
                        }

                        battle2.distPunchs.addElement(battleDistPunch);
                     }
                  }
               }
            }

            BattleUnit battleUnit3;
            if (((BattleUnit)currentUnit).ai_Atacking) {
               boolean wasPunch = ((BattleUnit)currentUnit).atk_TimeAcc >= c_all_apply_atacks_time;
               ((BattleUnit)currentUnit).atk_TimeAcc += realTick;
               if ((battleUnit3 = battle2.getUnit(((BattleUnit)currentUnit).atk_Target)) != null) {
                  if (battleUnit3.cx - ((BattleUnit)currentUnit).cx > 0) {
                     ((BattleUnit)currentUnit).watchDirection = 0;
                  }

                  if (battleUnit3.cx - ((BattleUnit)currentUnit).cx < 0) {
                     ((BattleUnit)currentUnit).watchDirection = 1;
                  }
               }

               if (!wasPunch && ((BattleUnit)currentUnit).atk_TimeAcc >= c_all_apply_atacks_time) {
                  int atk_Target2 = ((BattleUnit)currentUnit).atk_Target;
                  if ((battleUnit = battle2.getUnit(atk_Target2)) != null && battleUnit.side != ((BattleUnit)actingUnit).side) {
                     BattleMemberSmall battleMemberSmall3 = (BattleMemberSmall)battle2.members.get(((BattleUnit)actingUnit).hisBattleMemberId);
                     battleMemberSmall2 = (BattleMemberSmall)battle2.members.get(battleUnit.hisBattleMemberId);
                     int[] enemysFriendsCount2 = battle2.getEnemysFriendsCount((BattleUnit)actingUnit);
                     int damageModifier = ((atk_Target2 = 0 + ((BattleUnit)actingUnit).params.damageIncForNearFriendsPerc * enemysFriendsCount2[1]) - ((BattleUnit)actingUnit).params.damageDecForNearEnemysPerc * enemysFriendsCount2[0]) * 1000 + ((BattleUnit)actingUnit).changeDamage;
                     Object attackerStats;
                     long attackPower = (long)(((BattleUnit)(attackerStats = actingUnit)).HigherLegi ? ((BattleUnit)attackerStats).params.hitPower0P * 1000 : (int)(((BattleUnit)attackerStats).CurCount * (long)((BattleUnit)attackerStats).params.hitPower0P * 1000L / 100L));
                     int attackRating = (int)Math.max(percentLogic((long)((BattleUnit)actingUnit).params.atackValue, ((BattleUnit)actingUnit).changeAtack), 1L);
                     int defenceRating = (int)Math.max(percentLogic((long)battleUnit.params.defenceValue, battleUnit.changeDefence), 1L);
                     long rawTargetDamage;
                     long rawCounterDamage = (rawTargetDamage = percentLogic(attackPower, damageModifier) * (long)attackRating / (long)defenceRating * (long)((int)battle2.btlRandom.nextInt((long)(c_battle_action_random_max - c_battle_action_random_min)) + c_battle_action_random_min) / 1000L) * (long)battleUnit.params.answerPowerPerc / 100L;
                     byte attackSpecialEffect = 0;
                     boolean criticalHit = battle2.btlRandom.nextInt(10000L) < (long)battleMemberSmall3.gpEffects[14];
                     boolean dodged = battle2.btlRandom.nextInt(1000L) < (long)battleMemberSmall2.gpEffects[16];
                     boolean blocked;
                     if (blocked = battle2.btlRandom.nextInt(1000L) < (long)battleMemberSmall2.gpEffects[15]) {
                        rawTargetDamage = 0L;
                        rawCounterDamage = 0L;
                        attackSpecialEffect = 3;
                     } else if (!(criticalHit & dodged)) {
                        if (criticalHit) {
                           rawTargetDamage *= 3L;
                           rawCounterDamage *= 3L;
                           attackSpecialEffect = 1;
                        } else if (dodged) {
                           rawTargetDamage = MathGame.ceilDivide(rawTargetDamage, 3L);
                           rawCounterDamage = MathGame.ceilDivide(rawCounterDamage, 3L);
                           attackSpecialEffect = 2;
                        }
                     }

                     if (rawTargetDamage < 0L) {
                        rawTargetDamage = 0L;
                     }

                     long targetDamage = calculateUnitDamage(battleUnit, rawTargetDamage);
                     long counterDamage = calculateUnitDamage((BattleUnit)actingUnit, rawCounterDamage);
                     long attackerHealing = 0L * (long)((int)battle2.btlRandom.nextInt((long)(c_battle_action_random_max - c_battle_action_random_min)) + c_battle_action_random_min) / 1000L;
                     GameImage gameImage;
                     switch (attackSpecialEffect) {
                        case 1:
                           gameImage = ImagesGlobal.hitEffectKrit;
                           break;
                        case 2:
                           gameImage = ImagesGlobal.hitEffectBlock;
                           break;
                        case 3:
                           gameImage = ImagesGlobal.hitEffectDodge;
                           break;
                        default:
                           gameImage = null;
                     }

                     battleUnit.hit(battle2, targetDamage, gameImage);
                     ((BattleUnit)actingUnit).hit(battle2, counterDamage, (GameImage)null);
                     ((BattleUnit)actingUnit).heal(attackerHealing, (GameImage)null);
                     int effectPower;
                     if (((BattleUnit)actingUnit).params.poisonPower0P > 0 && !blocked) {
                        if (((BattleUnit)actingUnit).HigherLegi) {
                           effectPower = ((BattleUnit)actingUnit).params.poisonPower0P * 1000;
                        } else {
                           effectPower = (int)(((BattleUnit)actingUnit).CurCount * (long)((BattleUnit)actingUnit).params.poisonPower0P * 1000L / 100L);
                        }

                        effectPower = effectPower * (100 - battleMemberSmall2.gpEffects[3]) / 100;
                        if (!battleUnit.btl_Poisoned || effectPower > battleUnit.btl_PoisPower) {
                           battleUnit.btl_Poisoned = true;
                           battleUnit.btl_PoisPower = effectPower;
                           battleUnit.btl_PoisTimeAcc = c_PoisonTime;
                        }
                     }

                     if (((BattleUnit)actingUnit).params.paralizeTimeMs > 0 && !blocked) {
                        if (((BattleUnit)actingUnit).HigherLegi) {
                           effectPower = ((BattleUnit)actingUnit).params.paralizeTimeMs * (100 - battleMemberSmall2.gpEffects[13]) / 100;
                        } else {
                           effectPower = (int)((long)((BattleUnit)actingUnit).params.paralizeTimeMs * ((BattleUnit)actingUnit).CurCount * (long)(100 - battleMemberSmall2.gpEffects[13]) / 100L);
                        }

                        if (!battleUnit.btl_Paralize || effectPower > battleUnit.btl_ParalTime) {
                           battleUnit.btl_ParalTime = effectPower;
                           battleUnit.btl_Paralize = true;
                        }
                     }

                     long deltaY;
                     long distance;
                     long deltaX;
                     if (((BattleUnit)actingUnit).params.pushPowerPix > 0 && !blocked) {
                        deltaX = (long)(battleUnit.cx - ((BattleUnit)actingUnit).cx);
                        deltaY = (long)(battleUnit.cy - ((BattleUnit)actingUnit).cy);
                        if ((distance = MathGame.integerSquareRoot(deltaX * deltaX + deltaY * deltaY)) == 0L) {
                           distance = 1L;
                        }

                        deltaX = deltaX * (long)Math.abs(((BattleUnit)actingUnit).params.pushPowerPix) / distance;
                        deltaY = deltaY * (long)Math.abs(((BattleUnit)actingUnit).params.pushPowerPix) / distance;
                        battleUnit.addIntertion(new int[]{(int)deltaX, (int)deltaY}, ((BattleUnit)actingUnit).params.pushPowerPix * c_battle_InertSpeed);
                     }

                     if (((BattleUnit)actingUnit).params.pullPowerPix > 0 && !blocked) {
                        deltaX = (long)(((BattleUnit)actingUnit).cx - battleUnit.cx);
                        deltaY = (long)(((BattleUnit)actingUnit).cy - battleUnit.cy);
                        if ((distance = MathGame.integerSquareRoot(deltaX * deltaX + deltaY * deltaY)) == 0L) {
                           distance = 1L;
                        }

                        deltaX = deltaX * (long)Math.abs(((BattleUnit)actingUnit).params.pullPowerPix) / distance;
                        deltaY = deltaY * (long)Math.abs(((BattleUnit)actingUnit).params.pullPowerPix) / distance;
                        battleUnit.addIntertion(new int[]{(int)deltaX, (int)deltaY}, ((BattleUnit)actingUnit).params.pullPowerPix * c_battle_InertSpeed);
                     }

                     BattleMemberSmall battleMemberSmall4;
                     if (((BattleUnit)actingUnit).playersAvatar && (battleMemberSmall4 = battle2.getBattleMember(((BattleUnit)actingUnit).hisBattleMemberId)).stopRageTime <= 0) {
                        battleMemberSmall4.changeRage(c_rage_Add);
                     }

                     if (((BattleUnit)actingUnit).bushInd >= 0) {
                        battleUnit.startDrawHeart(c_heart_images_inds[((BattleUnit)actingUnit).bushInd], (byte)(((BattleUnit)actingUnit).cx - battleUnit.cx >= 0 ? 0 : 1));
                     }

                     if (targetDamage > 0L) {
                        battleUnit.startOffHit(battleUnit.cx - ((BattleUnit)actingUnit).cx < 0);
                     }

                     if (counterDamage > 0L) {
                        ((BattleUnit)actingUnit).startOffHit(((BattleUnit)actingUnit).cx - battleUnit.cx < 0);
                     }

                     battleUnit.shakeActive = true;
                     battleUnit.shakeTime = 0L;
                     if (!battleUnit.playersAvatar && !battleUnit.ai_Atacking) {
                        BattleUnit battleUnit4 = battle2.getUnit(battleUnit.ai_targetMoveUnit);
                        long deltaX2 = 0L;
                        long deltaY2 = 0L;
                        if (battleUnit4 != null) {
                           deltaX2 = (long)(battleUnit4.cx - battleUnit.cx);
                           deltaY2 = (long)(battleUnit4.cy - battleUnit.cy);
                        }

                        if (battleUnit4 == null || battleUnit4.Dead || deltaX2 * deltaX2 + deltaY2 * deltaY2 > Mystery.square(percentLogic((long)battleUnit4.params.hitRangePix, battleUnit4.changeRange)) || !battleUnit4.playersAvatar && ((BattleUnit)actingUnit).playersAvatar && !battleUnit.playersAvatar) {
                           deltaX2 = (long)(battleUnit.cx - ((BattleUnit)actingUnit).cx);
                           deltaY2 = (long)(battleUnit.cy - ((BattleUnit)actingUnit).cy);
                           if (deltaX2 * deltaX2 + deltaY2 * deltaY2 <= Mystery.square(percentLogic((long)battleUnit.params.hitRangePix, battleUnit.changeRange))) {
                              battle2.setTargetByPlayerOrAI(battleUnit, ((BattleUnit)actingUnit).hisInd, 1, 1, false);
                           }
                        }
                     }
                  }
               }

               if (((BattleUnit)currentUnit).atk_TimeAcc >= c_all_finish_atacks_time) {
                  ((BattleUnit)currentUnit).ai_Atacking = false;
                  ((BattleUnit)currentUnit).atk_TimeAcc = 0;
               }

               if (((BattleUnit)currentUnit).ai_Atacking) {
                  battle2.addRage((BattleUnit)currentUnit, realTick);
               }
            }

            if (((BattleUnit)currentUnit).btl_FlyToPointTime > 0) {
               ((BattleUnit)currentUnit).btl_FlyToPointTime -= realTick;
               if (((BattleUnit)currentUnit).btl_FlyToPointTime < 0) {
                  ((BattleUnit)currentUnit).btl_FlyToPointTime = 0;
               }

               ((BattleUnit)currentUnit).cx = (short)(((BattleUnit)currentUnit).btl_FlyToToX + (((BattleUnit)currentUnit).btl_FlyToFromX - ((BattleUnit)currentUnit).btl_FlyToToX) * ((BattleUnit)currentUnit).btl_FlyToPointTime / ((BattleUnit)currentUnit).btl_FlyToPointBaseTime);
               ((BattleUnit)currentUnit).cy = (short)(((BattleUnit)currentUnit).btl_FlyToToY + (((BattleUnit)currentUnit).btl_FlyToFromY - ((BattleUnit)currentUnit).btl_FlyToToY) * ((BattleUnit)currentUnit).btl_FlyToPointTime / ((BattleUnit)currentUnit).btl_FlyToPointBaseTime);
               long fourJumpHeight = 4L * (long)c_unit_jump_height;
               long jumpElapsedTime = (long)(((BattleUnit)currentUnit).btl_FlyToPointBaseTime - ((BattleUnit)currentUnit).btl_FlyToPointTime);
               ((BattleUnit)currentUnit).cz = (short)((int)(fourJumpHeight * jumpElapsedTime / (long)((BattleUnit)currentUnit).btl_FlyToPointBaseTime - fourJumpHeight * jumpElapsedTime / (long)((BattleUnit)currentUnit).btl_FlyToPointBaseTime * jumpElapsedTime / (long)((BattleUnit)currentUnit).btl_FlyToPointBaseTime));
               int flightDeltaX;
               if ((flightDeltaX = ((BattleUnit)currentUnit).btl_FlyToToX - ((BattleUnit)currentUnit).btl_FlyToFromX) != 0) {
                  ((BattleUnit)currentUnit).watchDirection = (byte)(flightDeltaX > 0 ? 0 : 1);
               }
            } else if (((BattleUnit)currentUnit).Dead) {
               ((BattleUnit)currentUnit).deadTimer += realTick;
            } else if (((BattleUnit)currentUnit).btl_Inerted) {
               ((BattleUnit)currentUnit).btl_StayTimeAcc = 0;
               ((BattleUnit)currentUnit).btl_InertTimeAcc += realTick;
               if (((BattleUnit)currentUnit).btl_InertTimeAcc > ((BattleUnit)currentUnit).btl_InertMaxTime) {
                  ((BattleUnit)currentUnit).btl_InertTimeAcc = ((BattleUnit)currentUnit).btl_InertMaxTime;
               }

               if (((BattleUnit)currentUnit).btl_InertMaxTime <= 0) {
                  ((BattleUnit)currentUnit).btl_Inerted = false;
               } else {
                  int inertDeltaX = (((BattleUnit)currentUnit).btl_InertTimeAcc - ((BattleUnit)currentUnit).btl_InertLastTimeX) * ((BattleUnit)currentUnit).btl_InertVect[0] / ((BattleUnit)currentUnit).btl_InertMaxTime;
                  int inertDeltaY = (((BattleUnit)currentUnit).btl_InertTimeAcc - ((BattleUnit)currentUnit).btl_InertLastTimeY) * ((BattleUnit)currentUnit).btl_InertVect[1] / ((BattleUnit)currentUnit).btl_InertMaxTime;
                  if (inertDeltaX != 0 || inertDeltaY != 0) {
                     if (inertDeltaX != 0) {
                        ((BattleUnit)currentUnit).btl_InertLastTimeX = ((BattleUnit)currentUnit).btl_InertTimeAcc;
                     }

                     if (inertDeltaY != 0) {
                        ((BattleUnit)currentUnit).btl_InertLastTimeY = ((BattleUnit)currentUnit).btl_InertTimeAcc;
                     }

                     if (!((BattleUnit)currentUnit).collidMoveOffset(inertDeltaX, inertDeltaY, battle2.currentMap)) {
                        ((BattleUnit)currentUnit).btl_Inerted = false;
                     }
                  }

                  if (((BattleUnit)currentUnit).btl_InertTimeAcc >= ((BattleUnit)currentUnit).btl_InertMaxTime) {
                     ((BattleUnit)currentUnit).btl_Inerted = false;
                  }
               }
            } else {
               if (((BattleUnit)currentUnit).btl_Poisoned) {
                  ((BattleUnit)currentUnit).btl_PoisTimeAcc += realTick;

                  while(((BattleUnit)actingUnit).btl_PoisTimeAcc >= c_PoisonTime) {
                     ((BattleUnit)actingUnit).btl_PoisTimeAcc -= c_PoisonTime;
                     int poisonDamage = (int)calculateUnitDamage((BattleUnit)actingUnit, (long)((BattleUnit)actingUnit).btl_PoisPower);
                     ((BattleUnit)actingUnit).hit(battle3, (long)poisonDamage, ImagesGlobal.iconPoisoned);
                     if (poisonDamage > 0) {
                        ((BattleUnit)actingUnit).startOffHit(!((BattleUnit)actingUnit).HeadLorR);
                     }

                     ((BattleUnit)actingUnit).btl_PoisPower = ((BattleUnit)actingUnit).btl_PoisPower * 1000 / c_battle_weak_fire_new;
                     if (((BattleUnit)actingUnit).btl_PoisPower < 1000) {
                        ((BattleUnit)actingUnit).btl_Poisoned = false;
                     }
                  }
               }

               if (((BattleUnit)actingUnit).btl_AcidPower > 0) {
                  ((BattleUnit)actingUnit).btl_AcidTimeAcc += realTick;

                  while(((BattleUnit)actingUnit).btl_AcidTimeAcc >= c_AcidTime) {
                     ((BattleUnit)actingUnit).btl_AcidTimeAcc -= c_AcidTime;
                     int acidDamage = (int)calculateUnitDamage((BattleUnit)actingUnit, (long)((BattleUnit)actingUnit).btl_AcidPower);
                     ((BattleUnit)actingUnit).hit(battle3, (long)acidDamage, ImagesGlobal.heartAcid);
                     if (acidDamage > 0) {
                        ((BattleUnit)actingUnit).startOffHit(!((BattleUnit)actingUnit).HeadLorR);
                     }
                  }
               }

               if (((BattleUnit)actingUnit).btl_RegenPower > 0) {
                  ((BattleUnit)actingUnit).btl_RegenTimeAcc += realTick;

                  while(((BattleUnit)actingUnit).btl_RegenTimeAcc >= c_RegenTime) {
                     ((BattleUnit)actingUnit).btl_RegenTimeAcc -= c_RegenTime;
                     ((BattleUnit)actingUnit).heal((long)((BattleUnit)actingUnit).btl_RegenPower, ImagesGlobal.heartRegen);
                  }
               }

               if (((BattleUnit)actingUnit).btl_Paralize) {
                  ((BattleUnit)actingUnit).btl_ParalTime -= realTick;
                  if (((BattleUnit)actingUnit).btl_ParalTime <= 0) {
                     ((BattleUnit)actingUnit).btl_ParalTime = 0;
                     ((BattleUnit)actingUnit).btl_Paralize = false;
                  }
               }

               if (((BattleUnit)actingUnit).btl_StandTime > 0) {
                  ((BattleUnit)actingUnit).btl_StandTime -= realTick;
                  if (((BattleUnit)actingUnit).btl_StandTime <= 0) {
                     ((BattleUnit)actingUnit).btl_StandTime = 0;
                  }
               }

               if (((BattleUnit)actingUnit).btl_StopFlyTime > 0) {
                  ((BattleUnit)actingUnit).btl_StopFlyTime -= realTick;
                  if (((BattleUnit)actingUnit).btl_StopFlyTime <= 0) {
                     ((BattleUnit)actingUnit).btl_StopFlyTime = 0;
                  }
               }

               if (((BattleUnit)actingUnit).btl_DesarmTime > 0) {
                  ((BattleUnit)actingUnit).btl_DesarmTime -= realTick;
                  if (((BattleUnit)actingUnit).btl_DesarmTime <= 0) {
                     ((BattleUnit)actingUnit).btl_DesarmTime = 0;
                  }
               }

               if (((BattleUnit)actingUnit).btl_ProtectionTime > 0) {
                  ((BattleUnit)actingUnit).btl_ProtectionTime -= realTick;
                  if (((BattleUnit)actingUnit).btl_ProtectionTime <= 0) {
                     ((BattleUnit)actingUnit).btl_ProtectionTime = 0;
                  }
               }

               if (!((BattleUnit)actingUnit).ai_Atacking) {
                  if (((BattleUnit)actingUnit).btl_Paralize) {
                     ((BattleUnit)actingUnit).iterationStayingForBouncing = true;
                  } else {
                     ((BattleUnit)actingUnit).walkStayAnimationTime += (long)realTick;
                     if (((BattleUnit)actingUnit).ai_FirstTarget) {
                        ((BattleUnit)actingUnit).ai_FirstTarget = false;
                        battle3.defineTarget((BattleUnit)actingUnit);
                     }

                     ((BattleUnit)actingUnit).ai_AtackTimeSpend += realTick;
                     ((BattleUnit)actingUnit).btl_StayTimeAcc += realTick;
                     boolean needRetarget = false;
                     if (((BattleUnit)actingUnit).ai_targetMoveUnit >= 0) {
                        if ((battleUnit3 = battle3.getUnit(((BattleUnit)actingUnit).ai_targetMoveUnit)) == null || battleUnit3.Dead) {
                           needRetarget = true;
                        }
                     } else {
                        long moveDeltaX = (long)(((BattleUnit)actingUnit).ai_targetMoveX - ((BattleUnit)actingUnit).cx);
                        long moveDeltaY = (long)(((BattleUnit)actingUnit).ai_targetMoveY - ((BattleUnit)actingUnit).cy);
                        if (moveDeltaX == 0L && moveDeltaY == 0L && !((BattleUnit)actingUnit).playersAvatar) {
                           needRetarget = true;
                        }
                     }

                     if (needRetarget) {
                        battle3.defineTarget((BattleUnit)actingUnit);
                     }

                     if ((battleUnit3 = battle3.getUnit(((BattleUnit)actingUnit).ai_targetAtackUnit)) == null || battleUnit3.Dead) {
                        ((BattleUnit)actingUnit).ai_targetAtackUnit = (short)battle3.getPreferedTargetUnit((BattleUnit)actingUnit);
                     }

                     boolean canAttack = false;
                     if (((BattleUnit)actingUnit).ai_targetAtackUnit >= 0 && (((BattleUnit)actingUnit).ai_targetMoveUnit != -1 || ((BattleUnit)actingUnit).cx == ((BattleUnit)actingUnit).ai_targetMoveX && ((BattleUnit)actingUnit).cy == ((BattleUnit)actingUnit).ai_targetMoveY) && ((BattleUnit)actingUnit).executableDistPunch == null && (battleUnit2 = battle3.getUnit(((BattleUnit)actingUnit).ai_targetAtackUnit)) != null) {
                        long deltaX3 = (long)(battleUnit2.cx - ((BattleUnit)actingUnit).cx);
                        long deltaY3 = (long)(battleUnit2.cy - ((BattleUnit)actingUnit).cy);
                        long hitRangePix = percentLogic((long)((BattleUnit)actingUnit).params.hitRangePix, ((BattleUnit)actingUnit).changeRange);
                        if (deltaX3 * deltaX3 + deltaY3 * deltaY3 <= hitRangePix * hitRangePix) {
                           canAttack = true;
                           if (deltaX3 > 0L) {
                              ((BattleUnit)actingUnit).watchDirection = 0;
                           } else if (deltaX3 < 0L) {
                              ((BattleUnit)actingUnit).watchDirection = 1;
                           }
                        }
                     }

                     int movementTarget;
                     if (!canAttack) {
                        int targetInd = ((BattleUnit)actingUnit).ai_targetMoveUnit;
                        short ai_targetMoveX2 = ((BattleUnit)actingUnit).ai_targetMoveX;
                        movementTarget = ((BattleUnit)actingUnit).ai_targetMoveY;
                        if (((BattleUnit)actingUnit).executableDistPunch != null) {
                           targetInd = ((BattleUnit)actingUnit).executableDistPunch.targetInd;
                           ai_targetMoveX2 = (short)((BattleUnit)actingUnit).executableDistPunch.groundX;
                           movementTarget = (short)((BattleUnit)actingUnit).executableDistPunch.groundY;
                        }

                        if (targetInd == -1) {
                           PathFolower pathFolower2;
                           short[] lastPathPoint;
                           if ((((BattleUnit)actingUnit).cx != ai_targetMoveX2 || ((BattleUnit)actingUnit).cy != movementTarget) && (((BattleUnit)actingUnit).pathFolower.isMovePathAbsent() || (pathFolower2 = ((BattleUnit)actingUnit).pathFolower).activeMovePath.size() == 0 || (lastPathPoint = (short[])pathFolower2.activeMovePath.elementAt(pathFolower2.activeMovePath.size() - 1))[0] != ai_targetMoveX2 || lastPathPoint[1] != movementTarget) || ((BattleUnit)actingUnit).forceRebuildPath) {
                              ((BattleUnit)actingUnit).forceRebuildPath = false;
                              ((BattleUnit)actingUnit).pathFolower.buildPath(battle3.currentMap.pathfindingGrid, battle3.currentMap, ((BattleUnit)actingUnit).cx, ((BattleUnit)actingUnit).cy, ai_targetMoveX2, (short)movementTarget, ((BattleUnit)actingUnit).cdW, ((BattleUnit)actingUnit).cdH, (short)Tile.width, (short)Tile.height);
                              ((BattleUnit)actingUnit).pathFolowedToTargetTileX = (short)TileMap.pxToTl(ai_targetMoveX2, 1);
                              ((BattleUnit)actingUnit).pathFolowedToTargetTileY = (short)TileMap.pxToTl(movementTarget, 0);
                           }
                        } else {
                           BattleUnit battleUnit5;
                           if (targetInd >= 0 && (battleUnit5 = battle3.getUnit(targetInd)) != null) {
                              short targetTileX = (short)TileMap.pxToTl(battleUnit5.cx, 1);
                              short targetTileY = (short)TileMap.pxToTl(battleUnit5.cy, 0);
                              if (((BattleUnit)actingUnit).pathFolower.isMovePathAbsent() || ((BattleUnit)actingUnit).pathFolowedToTargetTileX != targetTileX || ((BattleUnit)actingUnit).pathFolowedToTargetTileY != targetTileY || ((BattleUnit)actingUnit).forceRebuildPath) {
                                 ((BattleUnit)actingUnit).forceRebuildPath = false;
                                 ((BattleUnit)actingUnit).pathFolower.buildPath(battle3.currentMap.pathfindingGrid, battle3.currentMap, ((BattleUnit)actingUnit).cx, ((BattleUnit)actingUnit).cy, battleUnit5.cx, battleUnit5.cy, ((BattleUnit)actingUnit).cdW, ((BattleUnit)actingUnit).cdH, (short)Tile.width, (short)Tile.height);
                                 ((BattleUnit)actingUnit).pathFolowedToTargetTileX = targetTileX;
                                 ((BattleUnit)actingUnit).pathFolowedToTargetTileY = targetTileY;
                              }
                           }
                        }
                     }

                     battle3.addRage((BattleUnit)actingUnit, realTick);
                     boolean wasWalk = false;
                     if (canAttack) {
                        BattleUnit battleUnit6;
                        if ((battleUnit6 = battle3.getUnit(((BattleUnit)actingUnit).ai_targetAtackUnit)) != null) {
                           ((BattleUnit)actingUnit).iterationForBouncingCanAtack = true;
                           ((BattleUnit)actingUnit).iterationForBouncingCanAtackPointX = battleUnit6.cx;
                           ((BattleUnit)actingUnit).iterationForBouncingCanAtackPointY = battleUnit6.cy;
                           if (battleUnit6.cx - ((BattleUnit)actingUnit).cx > 0) {
                              ((BattleUnit)actingUnit).watchDirection = 0;
                           }

                           if (battleUnit6.cx - ((BattleUnit)actingUnit).cx < 0) {
                              ((BattleUnit)actingUnit).watchDirection = 1;
                           }
                        }

                        if (((BattleUnit)actingUnit).btl_DesarmTime <= 0 && ((BattleUnit)actingUnit).params.isFighter && (long)((BattleUnit)actingUnit).ai_AtackTimeSpend >= ((BattleUnit)actingUnit).getRealTimeBetweenHitsMs()) {
                           ((BattleUnit)actingUnit).ai_AtackTimeSpend = 0;
                           ((BattleUnit)actingUnit).ai_Atacking = true;
                           ((BattleUnit)actingUnit).atk_TimeAcc = 0;
                           ((BattleUnit)actingUnit).atk_Target = ((BattleUnit)actingUnit).ai_targetAtackUnit;
                        }
                     } else {
                        boolean originalUnitTarget = true;
                        movementTarget = ((BattleUnit)actingUnit).ai_targetMoveUnit;
                        if (((BattleUnit)actingUnit).executableDistPunch != null) {
                           originalUnitTarget = false;
                           movementTarget = ((BattleUnit)actingUnit).executableDistPunch.targetInd;
                        }

                        if (((BattleUnit)actingUnit).btl_StandTime <= 0 && movementTarget >= -1) {
                           int movementInterval = (int)percentLogic((long)((BattleUnit)actingUnit).params.speedMsPix2P[0], ((BattleUnit)actingUnit).changeMove);
                           if (movementTarget < 0) {
                              movementInterval = Math.min(movementInterval, ((BattleUnit)actingUnit).params.speedCleanMsPix2P[0]);
                           }

                           movementInterval = 100000 / movementInterval;
                           if (((BattleUnit)actingUnit).pathFolower.folow((MoveableUnitDiscretShort)actingUnit, (long)realTick, (long)movementInterval, battle3.currentMap)) {
                              wasWalk = true;
                              if (((BattleUnit)actingUnit).moveLastDirection8D == 5 || ((BattleUnit)actingUnit).moveLastDirection8D == 4 || ((BattleUnit)actingUnit).moveLastDirection8D == 6) {
                                 ((BattleUnit)actingUnit).watchDirection = 1;
                              }

                              if (((BattleUnit)actingUnit).moveLastDirection8D == 1 || ((BattleUnit)actingUnit).moveLastDirection8D == 2 || ((BattleUnit)actingUnit).moveLastDirection8D == 0) {
                                 ((BattleUnit)actingUnit).watchDirection = 0;
                              }

                              if (((BattleUnit)actingUnit).moveLastDirection8D == 8) {
                                 wasWalk = false;
                              }
                           }

                           if (originalUnitTarget && ((BattleUnit)actingUnit).ai_targetMoveUnit == -1 && ((BattleUnit)actingUnit).ai_targetMoveX == ((BattleUnit)actingUnit).cx && ((BattleUnit)actingUnit).ai_targetMoveY == ((BattleUnit)actingUnit).cy) {
                              ((BattleUnit)actingUnit).ai_targetMoveUnit = -2;
                              ((BattleUnit)actingUnit).pathFolower.disableMovePath();
                           }
                        }
                     }

                     if (wasWalk) {
                        ((BattleUnit)actingUnit).setBouncingPartialLeft(150L);
                        if (!((BattleUnit)actingUnit).isWalkOrStay) {
                           ((BattleUnit)actingUnit).isWalkOrStay = true;
                           ((BattleUnit)actingUnit).walkStayAnimationTime = 0L;
                        }
                     } else {
                        ((BattleUnit)actingUnit).iterationStayingForBouncing = true;
                        if (((BattleUnit)actingUnit).isWalkOrStay) {
                           ((BattleUnit)actingUnit).isWalkOrStay = false;
                           ((BattleUnit)actingUnit).walkStayAnimationTime = 0L;
                        }
                     }
                  }
               }
            }
         }

         BattleUnit battleUnit7;
         for(int unitsInOrderCount = battle2.unitsInOrder.size() - 1; unitsInOrderCount >= 0; --unitsInOrderCount) {
            if ((battleUnit7 = (BattleUnit)battle2.unitsInOrder.elementAt(unitsInOrderCount)).Dead && battleUnit7.deadTimer >= c_unit_death_time_before_remove) {
               battle2.addDeadman(battleUnit7.cx, battleUnit7.cy, battleUnit7.playersAvatar, battleUnit7.watchDirection, false);
               battle2.units.remove(battleUnit7.hisInd);
               battle2.unitsInOrder.removeElementAt(unitsInOrderCount);
            }
         }

         enumeration = battle2.unitsInOrder.elements();

         while(enumeration.hasMoreElements()) {
            battleUnit7 = (BattleUnit)enumeration.nextElement();
            battle2.pullStayingUnitFromOthers(battleUnit7);
         }

         battleQuantumEvent.applyEvents(this, 2);
         this.applyBattleEvents(50);
         battleQuantumEvent.applyEvents(this, 3);
         this.animateSecondaryElements(50);
         ++this.curTickInd;
         return true;
      }
   }

   private void animateSecondaryElements(int realTick) {
      for(int i = 0; i < this.deadmans.size(); ++i) {
         BattleDeadman battleDeadman;
         if ((battleDeadman = (BattleDeadman)this.deadmans.elementAt(i)).drawDying) {
            battleDeadman.dyingTime += realTick;
            if (ImagesGlobal.animClientSprites[battleDeadman.getSpriteForDying()].isLastFrameByTime(battleDeadman.dyingTime)) {
               battleDeadman.drawDying = false;
            }
         }
      }

   }

   private void copyForStoreRestoreTo(Battle battle2) {
      battle2.wasEnd = this.wasEnd;
      battle2.currentMap = this.currentMap;
      battle2.centerCorrectCoordX = this.centerCorrectCoordX;
      battle2.centerCorrectCoordY = this.centerCorrectCoordY;
      battle2.lastUnitInd = this.lastUnitInd;
      battle2.battleState = this.battleState;
      battle2.btlRandom = this.btlRandom.cloneTo();
      battle2.battleTime = this.battleTime;
      battle2.lastTickTime = this.lastTickTime;
      battle2.curTickInd = this.curTickInd;
      battle2.emptyQuantumEventsCompensationCount = this.emptyQuantumEventsCompensationCount;
      battle2.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount = this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount;
      battle2.emptyQuantumEventsCompensationSavedBattleState = this.emptyQuantumEventsCompensationSavedBattleState;
      battle2.isCanRunFromBattle = this.isCanRunFromBattle;
      battle2.speedUpForQuantsCount = this.speedUpForQuantsCount;
      battle2.lastFewLeftEventsCountersIndex = this.lastFewLeftEventsCountersIndex;
      battle2.lastFewLeftEventsCounters = new int[this.lastFewLeftEventsCounters.length];
      System.arraycopy(this.lastFewLeftEventsCounters, 0, battle2.lastFewLeftEventsCounters, 0, this.lastFewLeftEventsCounters.length);
      battle2.playerMemberSmall = null;
      battle2.members.clear();
      Enumeration enumeration = this.members.elements();

      while(enumeration.hasMoreElements()) {
         BattleMemberSmall battleMemberSmall = ((BattleMemberSmall)enumeration.nextElement()).clone(battle2);
         battle2.members.put(battleMemberSmall.id, battleMemberSmall);
         if (this.playerMemberSmall != null && battleMemberSmall.id == this.playerMemberSmall.id) {
            battle2.playerMemberSmall = battleMemberSmall;
         }
      }

      if (this.playerMember == null) {
         battle2.playerMember = null;
      } else {
         battle2.playerMember = this.playerMember.clone(battle2);
      }

      battle2.units.clear();
      battle2.unitsInOrder.removeAllElements();
      enumeration = this.unitsInOrder.elements();

      while(enumeration.hasMoreElements()) {
         BattleUnit battleUnit = ((BattleUnit)enumeration.nextElement()).cloneTo(new BattleUnit((DataInputStream)null));
         battle2.units.put(battleUnit.hisInd, battleUnit);
         battle2.unitsInOrder.addElement(battleUnit);
         if (battle2.playerMember != null && battleUnit.playersAvatar && battleUnit.hisBattleMemberId == battle2.playerMember.id) {
            battle2.playerAvatar = battleUnit;
         }
      }

      battle2.quantumEventsAccumed.removeAllElements();

      int i;
      for(i = 0; i < this.quantumEventsAccumed.size(); ++i) {
         battle2.quantumEventsAccumed.addElement(((BattleQuantumEvent)this.quantumEventsAccumed.elementAt(i)).cloneTo());
      }

      battle2.distPunchs.removeAllElements();

      for(i = 0; i < this.distPunchs.size(); ++i) {
         battle2.distPunchs.addElement(((BattleDistPunch)this.distPunchs.elementAt(i)).cloneTo());
      }

      battle2.deadmans.removeAllElements();

      for(i = 0; i < this.deadmans.size(); ++i) {
         battle2.deadmans.addElement(((BattleDeadman)this.deadmans.elementAt(i)).cloneTo());
      }

   }

   private void loadFromStream(DataInputStream dataInputStream) {
      this.curTickInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.btlRandom = new MRandom(decompiledsupport.UncheckedDataInput.readLong(dataInputStream));
      this.wasEnd = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.playerMember = new BattleMember(this, dataInputStream);
      this.playerMemberSmall = null;
      this.lastUnitInd = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.members = new Hashtable();
      int integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      int integerCode2;
      for(integerCode2 = 0; integerCode2 < integerCode; ++integerCode2) {
         BattleMemberSmall battleMemberSmall = new BattleMemberSmall(this, dataInputStream);
         this.members.put(battleMemberSmall.id, battleMemberSmall);
         if (battleMemberSmall.id == this.playerMember.id) {
            this.playerMemberSmall = battleMemberSmall;
         }
      }

      this.playerAvatar = null;
      this.units = new Hashtable();
      this.unitsInOrder = new Vector();
      integerCode2 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      int integerCode3;
      for(integerCode3 = 0; integerCode3 < integerCode2; ++integerCode3) {
         BattleUnit battleUnit = new BattleUnit(dataInputStream);
         this.units.put(battleUnit.hisInd, battleUnit);
         this.unitsInOrder.addElement(battleUnit);
         if (battleUnit.playersAvatar && battleUnit.hisBattleMemberId == this.playerMember.id) {
            this.playerAvatar = battleUnit;
         }
      }

      this.battleTime = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      integerCode3 = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);

      for(integerCode = 0; integerCode < integerCode3; ++integerCode) {
         BattleDistPunch battleDistPunch = new BattleDistPunch(dataInputStream);
         this.distPunchs.addElement(battleDistPunch);
      }

      integerCode = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.deadmans.removeAllElements();

      for(integerCode2 = 0; integerCode2 < integerCode; ++integerCode2) {
         BattleDeadman battleDeadman = new BattleDeadman(dataInputStream);
         this.deadmans.addElement(battleDeadman);
      }

      this.isCanRunFromBattle = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.centerCorrectCoordX = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.centerCorrectCoordY = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final BattleMember getPlayerMember() {
      return this.playerMember;
   }

   public final BattleMemberSmall getPlayerMemberSmall() {
      return this.playerMemberSmall;
   }

   public final int getBattleTime() {
      return this.battleTime;
   }

   public final BattleUnit getUnit(int ind) {
      synchronized(this.totalLock) {
         return (BattleUnit)this.units.get(ind);
      }
   }

   public final BattleMemberSmall getBattleMember(int memberId) {
      synchronized(this.totalLock) {
         return (BattleMemberSmall)this.members.get(memberId);
      }
   }

   public final BattleUnit getPlayerAvatarUnit() {
      synchronized(this.totalLock) {
         if (this.playerMember == null) {
            return null;
         } else {
            Enumeration enumeration = this.playerMember.hisUnits.elements();

            BattleUnit battleUnit;
            do {
               if (!enumeration.hasMoreElements()) {
                  return null;
               }
            } while((battleUnit = this.getUnit((Integer)enumeration.nextElement())) == null || !battleUnit.playersAvatar);

            return battleUnit;
         }
      }
   }

   public final int getLeftQuantumEventsCount() {
      synchronized(this.totalLock) {
         return this.quantumEventsAccumed.size();
      }
   }

   protected final void addEventsWithSpeedCorrection(Vector vector3) {
      if (vector3.size() > 0) {
         synchronized(this.totalLock) {
            if (!this.analyzeAddingEventsByHavingEmptyQuantumEventsCompensation(vector3)) {
               this.lastFewLeftEventsCounters[this.lastFewLeftEventsCountersIndex] = this.quantumEventsAccumed.size();
               ++this.lastFewLeftEventsCountersIndex;
               if (this.lastFewLeftEventsCountersIndex >= this.lastFewLeftEventsCounters.length) {
                  this.lastFewLeftEventsCountersIndex = 0;
               }

               int mAX_VALUE = Integer.MAX_VALUE;

               int lastFewLeftEventsCounterIndex;
               for(lastFewLeftEventsCounterIndex = 0; lastFewLeftEventsCounterIndex < this.lastFewLeftEventsCounters.length; ++lastFewLeftEventsCounterIndex) {
                  mAX_VALUE = Math.min(mAX_VALUE, this.lastFewLeftEventsCounters[lastFewLeftEventsCounterIndex]);
               }

               this.speedUpForQuantsCount = Math.max(0, (mAX_VALUE == Integer.MAX_VALUE ? 0 : mAX_VALUE) - 1) << 1;

               for(lastFewLeftEventsCounterIndex = 0; lastFewLeftEventsCounterIndex < vector3.size(); ++lastFewLeftEventsCounterIndex) {
                  this.quantumEventsAccumed.addElement(vector3.elementAt(lastFewLeftEventsCounterIndex));
               }

            }
         }
      }
   }

   protected final void addEventsWithoutSpeedCorrection(Vector vector3) {
      if (vector3 != null && vector3.size() > 0) {
         synchronized(this.totalLock) {
            if (!this.analyzeAddingEventsByHavingEmptyQuantumEventsCompensation(vector3)) {
               for(int i = 0; i < vector3.size(); ++i) {
                  this.quantumEventsAccumed.addElement(vector3.elementAt(i));
               }

            }
         }
      }
   }

   private boolean analyzeAddingEventsByHavingEmptyQuantumEventsCompensation(Vector vector3) {
      if (this.emptyQuantumEventsCompensationCount <= 0) {
         return false;
      } else {
         int minimum2Index = 0;

         int elementCount;
         BattleQuantumEvent battleQuantumEvent;
         for(elementCount = 0; elementCount < vector3.size() && this.emptyQuantumEventsCompensationCount > 0 && (battleQuantumEvent = (BattleQuantumEvent)vector3.elementAt(elementCount)) != null && battleQuantumEvent.battleEvents.length == 0; ++elementCount) {
            ++minimum2Index;
            --this.emptyQuantumEventsCompensationCount;
            ++this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount;
         }

         elementCount = vector3.size();

         int minimum;
         for(minimum = 0; minimum < minimum2Index; ++minimum) {
            vector3.removeElementAt(0);
         }

         if (vector3.size() > 0 && this.emptyQuantumEventsCompensationCount > 0) {
            Battle emptyQuantumEventsCompensationSavedBattleState2 = this.emptyQuantumEventsCompensationSavedBattleState;
            this.emptyQuantumEventsCompensationSavedBattleState = null;
            int emptyQuantumEventsCompensationCount2 = this.emptyQuantumEventsCompensationCount;
            int emptyQuantumEventsCompensationConfirmedEmptyQuantsCount2 = this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount;
            long lastTickTime2 = this.lastTickTime;

            try {
               emptyQuantumEventsCompensationSavedBattleState2.copyForStoreRestoreTo(this);
            } catch (Exception exception) {
               Com.SendMistace("Error in restore battle", true, true, exception);
               return true;
            }

            for(minimum = 0; minimum < emptyQuantumEventsCompensationConfirmedEmptyQuantsCount2; ++minimum) {
               this.quantumEventsAccumed.addElement(new BattleQuantumEvent(new BattleEvent[0]));
            }

            for(minimum = 0; minimum < vector3.size(); ++minimum) {
               this.quantumEventsAccumed.addElement(vector3.elementAt(minimum));
            }

            int minimum2;
            if ((minimum2 = minimum2Index + emptyQuantumEventsCompensationCount2) <= elementCount / 2) {
               minimum = minimum2;
            } else if (minimum2 <= elementCount) {
               minimum = Math.min(minimum2, elementCount / 2);
            } else {
               minimum = minimum2 / 2;
            }

            minimum2 = emptyQuantumEventsCompensationConfirmedEmptyQuantsCount2 - minimum2Index + minimum;

            for(minimum2Index = 0; minimum2Index < minimum2; ++minimum2Index) {
               this.advanceBattleTick(50);
            }

            this.speedUpForQuantsCount = 0;
            this.lastTickTime = lastTickTime2;
            return true;
         } else {
            if (this.emptyQuantumEventsCompensationCount <= 0) {
               this.emptyQuantumEventsCompensationSavedBattleState = null;
               this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount = 0;
               this.emptyQuantumEventsCompensationCount = 0;
            } else if (this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount > 3) {
               for(minimum = 0; minimum < this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount; ++minimum) {
                  this.emptyQuantumEventsCompensationSavedBattleState.quantumEventsAccumed.addElement(new BattleQuantumEvent(new BattleEvent[0]));
                  this.emptyQuantumEventsCompensationSavedBattleState.advanceBattleTick(50);
               }

               this.emptyQuantumEventsCompensationConfirmedEmptyQuantsCount = 0;
            }

            return vector3.size() <= 0;
         }
      }
   }

   protected final boolean applyEvent(BattleEvent battleEvent) {
      BattleUnit battleUnit;
      switch (battleEvent.eventType) {
         case 0:
            if ((battleUnit = this.getUnit(battleEvent.setTargetUnitInd)) != null) {
               this.setTargetByPlayerOrAI(battleUnit, battleEvent.setTargetTargetInd, battleEvent.setTargetTargetX, battleEvent.setTargetTargetY, true);
            }
            break;
         case 1:
            BattleDistPunch addPunchPunch2 = battleEvent.addPunchPunch;
            Battle battle2 = this;
            synchronized(this.totalLock) {
               BattleUnit battleUnit2 = battle2.getUnit(addPunchPunch2.atackerInd);
               battle2.getUnit(addPunchPunch2.targetInd);
               if (battleUnit2 != null) {
                  battleUnit2.executableDistPunch = addPunchPunch2;
                  BattleTh.addDrawOrder(battleUnit2.hisBattleMemberId == battle2.playerMember.id, addPunchPunch2.targetInd, addPunchPunch2.groundX, addPunchPunch2.groundY);
               }
            }

            if ((battleUnit = this.getUnit(battleEvent.addPunchPunch.atackerInd)) != null && battleUnit.hisBattleMemberId == this.playerMember.id) {
               this.drawUnDoneAct = -1;
               this.hasUnDoneAct = false;
            }
         case 2:
         case 3:
         case 4:
         case 8:
         case 9:
         case 10:
         case 11:
         default:
            break;
         case 5:
            this.members.remove(battleEvent.removeMemberId);

            for(int removeMemberUnitIndex = 0; removeMemberUnitIndex < battleEvent.removeMemberUnits.length; ++removeMemberUnitIndex) {
               int removeMemberUnits = battleEvent.removeMemberUnits[removeMemberUnitIndex];
               BattleUnit battleUnit3 = (BattleUnit)this.units.remove(removeMemberUnits);
               this.unitsInOrder.removeElement(battleUnit3);
               if (battleUnit3 != null && !battleUnit3.Dead) {
                  this.addDeadman(battleUnit3.cx, battleUnit3.cy, battleUnit3.playersAvatar, battleUnit3.watchDirection, true);
               }

               if (battleUnit3 != null && battleUnit3.playersAvatar && battleUnit3.hisBattleMemberId == this.playerMember.id) {
                  this.playerAvatar = null;
               }
            }

            return true;
         case 6:
            battleEvent.addMemberMember.hisBattle = this;
            this.members.put(new Integer(battleEvent.addMemberMember.id), battleEvent.addMemberMember);
            break;
         case 7:
            BattleMemberSmall battleMemberSmall = (BattleMemberSmall)this.members.get(new Integer(battleEvent.addUnitUnit.hisBattleMemberId));
            this.addUnitToBattle(battleMemberSmall, battleEvent.addUnitUnit, new int[]{battleEvent.addUnitUnit.cx, battleEvent.addUnitUnit.cy});
            break;
         case 12:
            if (this.playerMember.id == battleEvent.addBottleMemberId) {
               this.playerMember.readBottle(battleEvent.addBottleBottle);
            }
      }

      return true;
   }

   private void pullStayingUnitFromOthers(BattleUnit battleUnit) {
      if (!battleUnit.Dead && !battleUnit.playersAvatar) {
         if (battleUnit.iterationStayingForBouncing) {
            if (battleUnit.bouncingPartialLeft > 0L) {
               Enumeration enumeration = this.unitsInOrder.elements();

               while(true) {
                  BattleUnit battleUnit2;
                  int deltaX;
                  int absoluteDistance;
                  int deltaY;
                  int absoluteDistance2;
                  do {
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    if (!enumeration.hasMoreElements()) {
                                       return;
                                    }
                                 } while((battleUnit2 = (BattleUnit)enumeration.nextElement()) == battleUnit);
                              } while(battleUnit2.side != battleUnit.side);
                           } while(battleUnit2.Dead);
                        } while(battleUnit2.playersAvatar);

                        absoluteDistance = Math.abs(deltaX = battleUnit.cx - battleUnit2.cx);
                        absoluteDistance2 = Math.abs(deltaY = battleUnit.cy - battleUnit2.cy);
                     } while(absoluteDistance >= battleUnit.bouncingSenseHWidth + battleUnit2.bouncingSenseHWidth);
                  } while(absoluteDistance2 >= battleUnit.bouncingSenseHWidth + battleUnit2.bouncingSenseHWidth);

                  BattleMovementState battleMovementState = new BattleMovementState(this);
                  long deltaX2;
                  long deltaY2;
                  long realRange;
                  if (!battleUnit.iterationForBouncingCanAtack) {
                     if (absoluteDistance < absoluteDistance2) {
                        if (!battleMovementState.movementPending) {
                           battleMovementState.updateMovement(deltaX < 0 ? -1 : 1, 0, false, 0L, 0L, 0L, absoluteDistance);
                        }
                     } else if (!battleMovementState.movementPending) {
                        battleMovementState.updateMovement(0, deltaY < 0 ? -1 : 1, false, 0L, 0L, 0L, absoluteDistance2);
                     }
                  } else {
                     deltaX2 = (long)(battleUnit.cx - battleUnit.iterationForBouncingCanAtackPointX);
                     deltaY2 = (long)(battleUnit.cy - battleUnit.iterationForBouncingCanAtackPointY);
                     long squaredRange = (realRange = percentLogic((long)battleUnit.params.hitRangePix, battleUnit.changeRange)) * realRange;
                     BattleMovementState battleMovementState2;
                     byte offsetX;
                     byte offsetY;
                     if (absoluteDistance < absoluteDistance2) {
                        if (deltaX == 0) {
                           if (!battleMovementState.movementPending) {
                              battleMovementState.updateMovement(-1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                           }

                           if (!battleMovementState.movementPending) {
                              battleMovementState.updateMovement(1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                           }
                        } else if (!battleMovementState.movementPending) {
                           battleMovementState.updateMovement(deltaX < 0 ? -1 : 1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                        }
                     } else {
                        label179: {
                           label178: {
                              if (deltaY == 0) {
                                 if (!battleMovementState.movementPending) {
                                    battleMovementState.updateMovement(0, -1, true, deltaX2, deltaY2, squaredRange, absoluteDistance2);
                                 }

                                 if (battleMovementState.movementPending) {
                                    break label179;
                                 }

                                 battleMovementState2 = battleMovementState;
                                 offsetX = 0;
                              } else {
                                 if (battleMovementState.movementPending) {
                                    break label179;
                                 }

                                 battleMovementState2 = battleMovementState;
                                 offsetX = 0;
                                 if (deltaY < 0) {
                                    offsetY = -1;
                                    break label178;
                                 }
                              }

                              offsetY = 1;
                           }

                           battleMovementState2.updateMovement(offsetX, offsetY, true, deltaX2, deltaY2, squaredRange, absoluteDistance2);
                        }
                     }

                     if (!battleMovementState.movementPending) {
                        if (absoluteDistance >= absoluteDistance2) {
                           if (deltaX == 0) {
                              if (!battleMovementState.movementPending) {
                                 battleMovementState.updateMovement(-1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                              }

                              if (!battleMovementState.movementPending) {
                                 battleMovementState.updateMovement(1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                              }
                           } else if (!battleMovementState.movementPending) {
                              battleMovementState.updateMovement(deltaX < 0 ? -1 : 1, 0, true, deltaX2, deltaY2, squaredRange, absoluteDistance);
                           }
                        } else {
                           label203: {
                              label166: {
                                 if (deltaY == 0) {
                                    if (!battleMovementState.movementPending) {
                                       battleMovementState.updateMovement(0, -1, true, deltaX2, deltaY2, squaredRange, absoluteDistance2);
                                    }

                                    if (battleMovementState.movementPending) {
                                       break label203;
                                    }

                                    battleMovementState2 = battleMovementState;
                                    offsetX = 0;
                                 } else {
                                    if (battleMovementState.movementPending) {
                                       break label203;
                                    }

                                    battleMovementState2 = battleMovementState;
                                    offsetX = 0;
                                    if (deltaY < 0) {
                                       offsetY = -1;
                                       break label166;
                                    }
                                 }

                                 offsetY = 1;
                              }

                              battleMovementState2.updateMovement(offsetX, offsetY, true, deltaX2, deltaY2, squaredRange, absoluteDistance2);
                           }
                        }
                     }
                  }

                  if (battleMovementState.movementPending) {
                     battleUnit.decBouncingPartialLeft();
                     battleUnit.forceRebuildPath = true;
                     battleUnit.collidMoveOffset(battleMovementState.deltaX, battleMovementState.deltaY, this.currentMap);
                     if (battleUnit.ai_targetMoveUnit < 0) {
                        battleUnit.ai_targetMoveX = battleUnit.cx;
                        battleUnit.ai_targetMoveY = battleUnit.cy;
                     }

                     if (battleMovementState.multipleMoves && battleUnit2.iterationStayingForBouncing && battleUnit2.bouncingPartialLeft > 0L) {
                        if (battleUnit2.iterationForBouncingCanAtack) {
                           deltaX2 = (long)(battleUnit2.cx - battleMovementState.deltaX - battleUnit2.iterationForBouncingCanAtackPointX);
                           deltaY2 = (long)(battleUnit2.cy - battleMovementState.deltaY - battleUnit2.iterationForBouncingCanAtackPointY);
                           realRange = percentLogic((long)battleUnit2.params.hitRangePix, battleUnit2.changeRange);
                           if (deltaX2 * deltaX2 + deltaY2 * deltaY2 <= realRange * realRange) {
                              battleUnit2.collidMoveOffset(-battleMovementState.deltaX, -battleMovementState.deltaY, this.currentMap);
                              battleUnit2.forceRebuildPath = true;
                              battleUnit2.decBouncingPartialLeft();
                              if (battleUnit2.ai_targetMoveUnit < 0) {
                                 battleUnit2.ai_targetMoveX = battleUnit.cx;
                                 battleUnit2.ai_targetMoveY = battleUnit.cy;
                              }
                           }
                        } else {
                           battleUnit2.collidMoveOffset(-battleMovementState.deltaX, -battleMovementState.deltaY, this.currentMap);
                           battleUnit2.forceRebuildPath = true;
                           battleUnit2.decBouncingPartialLeft();
                           if (battleUnit2.ai_targetMoveUnit < 0) {
                              battleUnit2.ai_targetMoveX = battleUnit.cx;
                              battleUnit2.ai_targetMoveY = battleUnit.cy;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private int[] getEnemysFriendsCount(BattleUnit battleUnit) {
      int friendCount = 0;
      int enemyCount = 0;
      Enumeration enumeration = this.unitsInOrder.elements();

      while(enumeration.hasMoreElements()) {
         BattleUnit battleUnit2;
         if ((battleUnit2 = (BattleUnit)enumeration.nextElement()).hisInd != battleUnit.hisInd && !battleUnit2.Dead) {
            long deltaX = (long)(battleUnit2.cx - battleUnit.cx);
            long deltaY = (long)(battleUnit2.cy - battleUnit.cy);
            long squaredDistance = deltaX * deltaX + deltaY * deltaY;
            int c_ClosetsDist2 = c_ClosetsDist;
            if (squaredDistance <= (long)(c_ClosetsDist2 * c_ClosetsDist2)) {
               if (battleUnit.side == battleUnit2.side) {
                  ++friendCount;
               } else {
                  ++enemyCount;
               }
            }
         }
      }

      return new int[]{enemyCount, friendCount};
   }

   private static long calculateUnitDamage(BattleUnit battleUnit, long hitPower) {
      if (battleUnit.btl_ProtectionTime > 0) {
         hitPower = (long)((int)(hitPower * (long)Math.max(0, 100000 - battleUnit.btl_ProtectionPower) / 100000L));
      }

      return hitPower;
   }

   private void addRage(BattleUnit battleUnit, int time) {
      if (battleUnit.playersAvatar) {
         BattleMemberSmall battleMemberSmall;
         BattleMemberSmall battleMemberSmall2 = battleMemberSmall = this.getBattleMember(battleUnit.hisBattleMemberId);
         battleMemberSmall2.rageTimes += time;

         while(battleMemberSmall.rageTimes >= c_rage_AddTime) {
            battleMemberSmall.rageTimes -= c_rage_AddTime;
            if (battleMemberSmall.stopRageTime <= 0) {
               battleMemberSmall.changeRage(c_rage_AddVal);
            }
         }
      }

   }

   private int[] correctCoordToAchivable(int toX, int toY) {
      Vector vector3 = new Vector();
      this.currentMap.buildPath(vector3, (short)this.centerCorrectCoordX, (short)this.centerCorrectCoordY, (short)toX, (short)toY, (short)CD_W, (short)CD_H);
      return vector3.size() == 0 ? new int[]{this.centerCorrectCoordX, this.centerCorrectCoordY} : new int[]{((short[])vector3.elementAt(vector3.size() - 1))[0], ((short[])vector3.elementAt(vector3.size() - 1))[1]};
   }

   private int[] correctCoordToAchivable(BattleUnit battleUnit, int toX, int toY) {
      Vector vector3 = new Vector();
      this.currentMap.buildPath(vector3, battleUnit.cx, battleUnit.cy, (short)toX, (short)toY, battleUnit.cdW, battleUnit.cdH);
      return vector3.size() == 0 ? new int[]{this.centerCorrectCoordX, this.centerCorrectCoordY} : new int[]{((short[])vector3.elementAt(vector3.size() - 1))[0], ((short[])vector3.elementAt(vector3.size() - 1))[1]};
   }

   private void defineTarget(BattleUnit battleUnit) {
      int forSetTarget = this.getPreferedTargetUnit(battleUnit);
      BattleMemberSmall battleMemberSmall = (BattleMemberSmall)this.members.get(battleUnit.hisBattleMemberId);
      this.setTargetByPlayerOrAI(battleUnit, forSetTarget, battleMemberSmall.startCoordX, battleMemberSmall.startCoordY, false);
   }

   private int getPreferedTargetUnit(BattleUnit battleUnit) {
      int forSetTarget = -1;
      long closestSquaredDistance = 2000000000L;
      Enumeration enumeration = this.unitsInOrder.elements();

      while(enumeration.hasMoreElements()) {
         BattleUnit battleUnit2;
         if ((battleUnit2 = (BattleUnit)enumeration.nextElement()).side != battleUnit.side && !battleUnit2.Dead) {
            long deltaX = (long)(battleUnit2.cx - battleUnit.cx);
            long deltaY = (long)(battleUnit2.cy - battleUnit.cy);
            long squaredDistance = deltaX * deltaX + deltaY * deltaY;
            if (Math.abs(closestSquaredDistance - squaredDistance) <= 400L) {
               if (this.btlRandom.nextInt(100L) >= 50L) {
                  closestSquaredDistance = squaredDistance;
                  forSetTarget = battleUnit2.hisInd;
               }
            } else if (squaredDistance < closestSquaredDistance) {
               closestSquaredDistance = squaredDistance;
               forSetTarget = battleUnit2.hisInd;
            }
         }
      }

      return forSetTarget;
   }

   private void setTargetByPlayerOrAI(BattleUnit battleUnit, int targetInd, int targetX, int targetY, boolean byPlayer) {
      if (byPlayer) {
         if (targetInd >= 0) {
            battleUnit.ai_targetAtackUnit = (short)targetInd;
         }

         battleUnit.ai_targetMoveUnit = (short)targetInd;
         battleUnit.ai_targetMoveX = (short)targetX;
         battleUnit.ai_targetMoveY = (short)targetY;
         battleUnit.pathFolower.disableMovePath();
         battleUnit.executableDistPunch = null;
         BattleTh.addDrawOrder(battleUnit.hisBattleMemberId == this.playerMember.id, targetInd, targetX, targetY);
      } else if (targetInd < 0 || battleUnit.ai_targetMoveUnit != targetInd || battleUnit.ai_targetAtackUnit != targetInd) {
         if (targetInd < 0) {
            int[] coordToAchivable;
            targetX = (coordToAchivable = this.correctCoordToAchivable(battleUnit, targetX, targetY))[0];
            targetY = coordToAchivable[1];
         }

         if (targetInd >= 0) {
            battleUnit.ai_targetAtackUnit = (short)targetInd;
         }

         battleUnit.ai_targetMoveUnit = (short)targetInd;
         battleUnit.ai_targetMoveX = (short)targetX;
         battleUnit.ai_targetMoveY = (short)targetY;
         battleUnit.pathFolower.disableMovePath();
      }
   }

   public final void unitDead(int unitInd) {
      BattleUnit battleUnit;
      if ((battleUnit = this.getUnit(unitInd)) != null) {
         if (battleUnit.playersAvatar) {
            Enumeration enumeration = this.unitsInOrder.elements();

            while(enumeration.hasMoreElements()) {
               BattleUnit battleUnit2;
               if ((battleUnit2 = (BattleUnit)enumeration.nextElement()) != battleUnit && battleUnit2.hisBattleMemberId == battleUnit.hisBattleMemberId && !battleUnit2.Dead) {
                  battleUnit2.kill(this, false);
               }
            }
         }

      }
   }

   private void applyBattleEvents(int elapsedTime) {
      for(int sizeIndex = 0; sizeIndex < this.distPunchs.size(); ++sizeIndex) {
         boolean applied;
         label148: {
            BattleDistPunch battleDistPunch = (BattleDistPunch)this.distPunchs.elementAt(sizeIndex);
            Object battleDistPunch3 = battleDistPunch;
            if (this.getUnit(battleDistPunch.atackerInd) != null) {
               if (battleDistPunch.gameTime < battleDistPunch.bottleEffect.timeOfBulletFlight) {
                  boolean wasSelfFlyApplied = battleDistPunch.gameTime >= battleDistPunch.bottleEffect.flySelfToPointApplyTimeOffset && battleDistPunch.gameTime > 0;
                  battleDistPunch.gameTime += elapsedTime;
                  BattleUnit battleUnit;
                  int groundX;
                  int groundY;
                  BattleUnit battleUnit2;
                  if (battleDistPunch.bottleEffect.flySelfToPoint > 0 && !wasSelfFlyApplied && battleDistPunch.gameTime >= battleDistPunch.bottleEffect.flySelfToPointApplyTimeOffset && (battleUnit = this.getUnit(battleDistPunch.atackerInd)) != null) {
                     groundX = battleDistPunch.groundX;
                     groundY = battleDistPunch.groundY;
                     if ((battleUnit2 = this.getUnit(battleDistPunch.targetInd)) != null) {
                        groundX = battleUnit2.cx;
                        groundY = battleUnit2.cy;
                     }

                     BottleEffect bottleEffect2;
                     if ((bottleEffect2 = battleDistPunch.bottleEffect).flySelfToPoint > 0 && battleUnit.btl_StopFlyTime <= 0) {
                        int[] integers = this.correctCoordToAchivable(groundX, groundY);
                        battleUnit.btl_FlyToPointTime = bottleEffect2.flySelfToPoint;
                        battleUnit.btl_FlyToPointBaseTime = bottleEffect2.flySelfToPoint;
                        battleUnit.btl_FlyToFromX = battleUnit.cx;
                        battleUnit.btl_FlyToFromY = battleUnit.cy;
                        battleUnit.btl_FlyToToX = integers[0];
                        battleUnit.btl_FlyToToY = integers[1];
                        if (battleDistPunch.targetInd != battleDistPunch.atackerInd) {
                           if (battleUnit2 != null && battleUnit2.side != battleUnit.side) {
                              battleUnit.ai_targetAtackUnit = (short)battleDistPunch.targetInd;
                              battleUnit.ai_targetMoveUnit = (short)battleDistPunch.targetInd;
                           } else {
                              battleUnit.ai_targetMoveUnit = -1;
                              battleUnit.ai_targetMoveX = (short)integers[0];
                              battleUnit.ai_targetMoveY = (short)integers[1];
                           }
                        }

                        battleUnit.pathFolower.disableMovePath();
                     }
                  }

                  if (battleDistPunch.gameTime >= battleDistPunch.bottleEffect.timeOfBulletFlight) {
                     BattleDistPunch battleDistPunch2 = battleDistPunch;
                     Battle battle2 = this;
                     battleUnit = this.getUnit(battleDistPunch.atackerInd);
                     groundX = battleDistPunch.groundX;
                     groundY = battleDistPunch.groundY;
                     if ((battleUnit2 = this.getUnit(battleDistPunch.targetInd)) != null) {
                        groundX = battleUnit2.cx;
                        groundY = battleUnit2.cy;
                     }

                     if (battleDistPunch.bottleEffect.spawnUnit != null) {
                        int[] integers2 = this.correctCoordToAchivable(groundX, groundY);
                        BattleMemberSmall battleMemberSmall = (BattleMemberSmall)this.members.get(battleUnit.hisBattleMemberId);
                        BattleUnit spawnUnit2 = battleDistPunch.bottleEffect.spawnUnit;
                        this.addUnitToBattle(battleMemberSmall, spawnUnit2, integers2);
                        if (battleDistPunch.bottleEffect.spawnMonsterMaxCount > 0) {
                           battleDistPunch3 = battleDistPunch.fromItemTypeId;
                           Vector vector3;
                           if ((vector3 = (Vector)battleMemberSmall.spawnedMobsByItemType.get(battleDistPunch3)) == null) {
                              vector3 = new Vector();
                              battleMemberSmall.spawnedMobsByItemType.put(battleDistPunch3, vector3);
                           }

                           vector3.addElement(spawnUnit2.hisInd);
                           if (vector3.size() > battleDistPunch.bottleEffect.spawnMonsterMaxCount) {
                              int elementAt = (Integer)vector3.elementAt(0);
                              vector3.removeElementAt(0);
                              if ((spawnUnit2 = this.getUnit(elementAt)) != null) {
                                 spawnUnit2.kill(this, true);
                              }
                           }
                        }
                     }

                     if (((BattleDistPunch)battleDistPunch3).bottleEffect.range <= 0) {
                        if (battleUnit2 == null) {
                           this.applyBottleEffect((BattleUnit)null, (BottleEffect)((BattleDistPunch)battleDistPunch3).bottleEffect, (BattleUnit)battleUnit);
                        } else {
                           this.applyToUnitBottleEffect(((BattleDistPunch)battleDistPunch3).atackerInd, ((BattleDistPunch)battleDistPunch3).startX, ((BattleDistPunch)battleDistPunch3).startY, groundX, groundY, ((BattleDistPunch)battleDistPunch3).bottleEffect, battleUnit2, ((BattleDistPunch)battleDistPunch3).targetInd, ((BattleDistPunch)battleDistPunch3).getHeartSpriteInd());
                           this.applyBottleEffect(battleUnit2, ((BattleDistPunch)battleDistPunch3).bottleEffect, battleUnit);
                        }
                     } else {
                        Enumeration enumeration = this.unitsInOrder.elements();

                        while(enumeration.hasMoreElements()) {
                           BattleUnit battleUnit3 = (BattleUnit)enumeration.nextElement();
                           if (battleUnit.side == battleUnit3.side && battleDistPunch2.bottleEffect.friends || battleUnit.side != battleUnit3.side && battleDistPunch2.bottleEffect.enemys || battleUnit.side == battleUnit3.side && battleDistPunch2.bottleEffect.self && battleUnit3.playersAvatar) {
                              int deltaX = battleUnit3.cx - groundX;
                              int deltaY = battleUnit3.cy - groundY;
                              if (deltaX * deltaX + deltaY * deltaY <= battleDistPunch2.bottleEffect.range * battleDistPunch2.bottleEffect.range) {
                                 battle2.applyToUnitBottleEffect(battleDistPunch2.atackerInd, battleDistPunch2.startX, battleDistPunch2.startY, groundX, groundY, battleDistPunch2.bottleEffect, battleUnit3, battleDistPunch2.targetInd, battleDistPunch2.getHeartSpriteInd());
                                 battle2.applyBottleEffect(battleUnit3, battleDistPunch2.bottleEffect, battleUnit);
                              }
                           }
                        }
                     }
                  }
               } else {
                  battleDistPunch.gameTime += elapsedTime;
                  if (ImagesGlobal.animClientSprites[battleDistPunch.getGPSpriteInd()].isLastFrameByTime(battleDistPunch.gameTime)) {
                     applied = true;
                     break label148;
                  }
               }
            }

            applied = false;
         }

         if (applied) {
            this.distPunchs.removeElementAt(sizeIndex);
            --sizeIndex;
         }
      }

   }

   private void applyToUnitBottleEffect(int atackerInd, int startX, int startY, int groundX, int groundY, BottleEffect bottleEffect2, BattleUnit battleUnit, int targetToUnit, int disPunchHeartInd) {
      BattleUnit battleUnit2 = this.getUnit(atackerInd);
      BattleMemberSmall battleMemberSmall = (BattleMemberSmall)this.members.get(battleUnit2.hisBattleMemberId);
      BattleMemberSmall battleMemberSmall2 = (BattleMemberSmall)this.members.get(battleUnit.hisBattleMemberId);
      if (bottleEffect2.transformToUnit != null) {
         battleUnit.transformWithHystory(bottleEffect2.transformToUnit, bottleEffect2.transformToMonsterTime);
      }

      long hitPower;
      if (bottleEffect2.hitPower > 0) {
         if (bottleEffect2.appArmorAndShip) {
            int maximum = (int)Math.max(percentLogic((long)battleUnit2.params.atackValue, battleUnit2.changeAtack), 1L);
            int maximum2 = (int)Math.max(percentLogic((long)battleUnit.params.defenceValue, battleUnit.changeDefence), 1L);
            hitPower = (long)((int)((long)bottleEffect2.hitPower * (long)((int)this.btlRandom.nextInt((long)(c_battle_action_random_max - c_battle_action_random_min)) + c_battle_action_random_min) / 1000L * (long)maximum / (long)maximum2));
         } else {
            hitPower = (long)((int)((long)bottleEffect2.hitPower * (long)((int)this.btlRandom.nextInt((long)(c_battle_action_random_max - c_battle_action_random_min)) + c_battle_action_random_min) / 1000L));
         }

         hitPower = calculateUnitDamage(battleUnit, hitPower);
         battleUnit.hit(this, hitPower, (GameImage)null);
         if (disPunchHeartInd >= 0) {
            battleUnit.startDrawHeart(disPunchHeartInd, (byte)(battleUnit2.cx - battleUnit.cx >= 0 ? 0 : 1));
         }

         battleUnit.startOffHit(battleUnit.cx - battleUnit2.cx < 0);
      }

      if (bottleEffect2.flyToPoint > 0) {
         int[] integers = this.correctCoordToAchivable(groundX, groundY);
         battleUnit.btl_FlyToPointTime = bottleEffect2.flyToPoint;
         battleUnit.btl_FlyToPointBaseTime = bottleEffect2.flyToPoint;
         battleUnit.btl_FlyToFromX = battleUnit.cx;
         battleUnit.btl_FlyToFromY = battleUnit.cy;
         battleUnit.btl_FlyToToX = integers[0];
         battleUnit.btl_FlyToToY = integers[1];
         battleUnit.ai_targetMoveX = (short)integers[0];
         battleUnit.ai_targetMoveY = (short)integers[1];
         battleUnit.pathFolower.disableMovePath();
      }

      if (bottleEffect2.hitPower < 0) {
         battleUnit.heal((long)((int)((long)(-bottleEffect2.hitPower) * (long)(1000 + battleMemberSmall.gpEffects[12]) * (long)((int)this.btlRandom.nextInt((long)(c_battle_action_random_max - c_battle_action_random_min)) + c_battle_action_random_min) / 1000000L)), (GameImage)null);
      }

      if (bottleEffect2.freezPower > 0) {
         int btl_ParalTime = bottleEffect2.freezPower * (100 - battleUnit.btl_freezResist) / 100;
         if (!battleUnit.btl_Paralize || btl_ParalTime > battleUnit.btl_ParalTime) {
            battleUnit.btl_ParalTime = btl_ParalTime;
            battleUnit.btl_Paralize = true;
         }
      }

      if (bottleEffect2.protectionPower > 0 && (battleUnit.btl_ProtectionTime <= 0 || bottleEffect2.protectionPower * bottleEffect2.protectionTime > battleUnit.btl_ProtectionTime * battleUnit.btl_ProtectionPower)) {
         battleUnit.btl_ProtectionTime = bottleEffect2.protectionTime;
         battleUnit.btl_ProtectionPower = bottleEffect2.protectionPower;
      }

      if (bottleEffect2.standTime > 0 && (battleUnit.btl_StandTime <= 0 || bottleEffect2.standTime > battleUnit.btl_StandTime)) {
         battleUnit.btl_StandTime = bottleEffect2.standTime;
      }

      BattleMemberSmall battleMemberSmall3;
      if (bottleEffect2.rageStop > 0 && battleUnit.playersAvatar && (battleMemberSmall3 = (BattleMemberSmall)this.members.get(battleUnit.hisBattleMemberId)) != null && bottleEffect2.rageStop > battleMemberSmall3.stopRageTime) {
         battleMemberSmall3.stopRageTime = bottleEffect2.rageStop;
      }

      if (bottleEffect2.flyStop > 0 && (battleUnit.btl_StopFlyTime <= 0 || bottleEffect2.flyStop > battleUnit.btl_StopFlyTime)) {
         battleUnit.btl_StopFlyTime = bottleEffect2.flyStop;
      }

      if (bottleEffect2.itemsStopTime > 0 && battleUnit.playersAvatar && (battleMemberSmall3 = (BattleMemberSmall)this.members.get(battleUnit.hisBattleMemberId)) != null) {
         int[] integers2;
         (integers2 = new int[1 + bottleEffect2.itemsStopIds.length])[0] = bottleEffect2.itemsStopTime;
         System.arraycopy(bottleEffect2.itemsStopIds, 0, integers2, 1, bottleEffect2.itemsStopIds.length);
         battleMemberSmall3.stopedItems.addElement(integers2);
      }

      if (bottleEffect2.desarmTime > 0 && (battleUnit.btl_DesarmTime <= 0 || bottleEffect2.desarmTime > battleUnit.btl_DesarmTime)) {
         battleUnit.btl_DesarmTime = bottleEffect2.desarmTime;
      }

      if (bottleEffect2.changeDamage != 0) {
         battleUnit.changeDamage += bottleEffect2.changeDamage;
         if (bottleEffect2.changeDamage < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffDamageDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffDamageUp);
         }
      }

      if (bottleEffect2.changeHealth != 0) {
         if (bottleEffect2.changeHealth < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffHealthDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffHealthUp);
         }

         hitPower = percentLogic(battleUnit.MaxCount, battleUnit.changeHealth);
         battleUnit.changeHealth += bottleEffect2.changeHealth;
         long curHealthMaxCount = percentLogic(battleUnit.MaxCount, battleUnit.changeHealth);
         battleUnit.curHealthMaxCount = curHealthMaxCount;
         battleUnit.healthAnimPrepear();
         battleUnit.CurCount = (long)((int)(curHealthMaxCount * battleUnit.CurCount / hitPower));
         if (battleUnit.CurCount <= 0L) {
            battleUnit.CurCount = 1L;
         }

         if (battleUnit.CurCount > curHealthMaxCount) {
            battleUnit.CurCount = (long)((int)curHealthMaxCount);
         }
      }

      if (bottleEffect2.rageChange != 0 && battleUnit.playersAvatar) {
         this.getBattleMember(battleUnit.hisBattleMemberId).changeRage(bottleEffect2.rageChange);
      }

      if (bottleEffect2.changeAtack != 0) {
         battleUnit.changeAtack += bottleEffect2.changeAtack;
         if (bottleEffect2.changeAtack < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffAtackDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffAtackUp);
         }
      }

      if (bottleEffect2.changeDefence != 0) {
         battleUnit.changeDefence += bottleEffect2.changeDefence;
         if (bottleEffect2.changeDefence < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffDefenceDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffDefenceUp);
         }
      }

      if (bottleEffect2.changeSpeed != 0) {
         battleUnit.changeSpeed += bottleEffect2.changeSpeed;
         if (bottleEffect2.changeSpeed < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffSpeedDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffSpeedUp);
         }
      }

      if (bottleEffect2.changeMove != 0) {
         battleUnit.changeMove += bottleEffect2.changeMove;
         if (bottleEffect2.changeMove < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffMoveDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffMoveUp);
         }
      }

      if (bottleEffect2.changeRange != 0) {
         battleUnit.changeRange += bottleEffect2.changeRange;
         if (bottleEffect2.changeRange < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffRangeDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffRangeUp);
         }
      }

      BattleParams battleParams;
      if (bottleEffect2.changeDamageIncForNearFriendsPerc != 0) {
         battleParams = battleUnit.params;
         battleParams.damageIncForNearFriendsPerc += bottleEffect2.changeDamageIncForNearFriendsPerc;
         if (bottleEffect2.changeDamageIncForNearFriendsPerc < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffFriendsDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffFriendsUp);
         }
      }

      if (bottleEffect2.changeDamageDecForNearEnemysPerc != 0) {
         battleParams = battleUnit.params;
         battleParams.damageDecForNearEnemysPerc += bottleEffect2.changeDamageDecForNearEnemysPerc;
         if (bottleEffect2.changeDamageDecForNearEnemysPerc < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffEnemysDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffEnemysUp);
         }
      }

      if (bottleEffect2.changeAnswerPowerPerc != 0) {
         battleParams = battleUnit.params;
         battleParams.answerPowerPerc += bottleEffect2.changeAnswerPowerPerc;
         if (bottleEffect2.changeAnswerPowerPerc < 0) {
            battleUnit.addDrawBuff(ImagesGlobal.buffAnswerDown);
         } else {
            battleUnit.addDrawBuff(ImagesGlobal.buffAnswerUp);
         }
      }

      if (bottleEffect2.burnPower > 0) {
         hitPower = (long)(bottleEffect2.burnPower * (100 - battleMemberSmall2.gpEffects[3]) / 100);
         if (!battleUnit.btl_Poisoned || hitPower > (long)battleUnit.btl_PoisPower) {
            battleUnit.btl_Poisoned = true;
            battleUnit.btl_PoisPower = (int)hitPower;
            battleUnit.btl_PoisTimeAcc = c_PoisonTime;
            battleUnit.addDrawBuff(ImagesGlobal.buffPoisAdd);
         }
      }

      if (bottleEffect2.acidPower > 0) {
         if (battleUnit.btl_AcidPower <= 0) {
            battleUnit.btl_AcidTimeAcc = c_AcidTime;
         }

         battleUnit.btl_AcidPower += bottleEffect2.acidPower;
         battleUnit.addDrawBuff(ImagesGlobal.buffAcidAdd);
      }

      if (bottleEffect2.regenPower > 0) {
         if (battleUnit.btl_RegenPower <= 0) {
            battleUnit.btl_RegenTimeAcc = c_RegenTime;
         }

         battleUnit.btl_RegenPower += bottleEffect2.regenPower;
         battleUnit.addDrawBuff(ImagesGlobal.buffRegenAdd);
      }

      if (bottleEffect2.removesFreezPower > 0) {
         if (battleUnit.btl_Paralize) {
            battleUnit.btl_ParalTime = Math.max(0, battleUnit.btl_ParalTime - bottleEffect2.removesFreezPower);
         }

         battleUnit.addDrawBuff(ImagesGlobal.buffParalizeRem);
      }

      if (bottleEffect2.removesBurnPower > 0) {
         if (battleUnit.btl_Poisoned) {
            battleUnit.btl_ProtectionPower = Math.max(0, battleUnit.btl_ProtectionPower - bottleEffect2.removesBurnPower);
         }

         battleUnit.addDrawBuff(ImagesGlobal.buffPoisRem);
      }

      if (bottleEffect2.removesAcidPower > 0) {
         if (battleUnit.btl_AcidPower > 0) {
            battleUnit.btl_AcidPower = Math.max(0, battleUnit.btl_AcidPower - bottleEffect2.removesAcidPower);
         }

         battleUnit.addDrawBuff(ImagesGlobal.buffAcidRem);
      }

      long distance;
      long[] pVectF;
      if (bottleEffect2.punchPower > 0) {
         pVectF = new long[2];
         if (targetToUnit >= 0 && battleUnit.hisInd == targetToUnit) {
            pVectF[0] = (long)(battleUnit.cx - startX);
            pVectF[1] = (long)(battleUnit.cy - startY);
         } else if (bottleEffect2.pushFromIniter) {
            pVectF[0] = (long)(battleUnit.cx - battleUnit2.cx);
            pVectF[1] = (long)(battleUnit.cy - battleUnit2.cy);
         } else {
            pVectF[0] = (long)(battleUnit.cx - groundX);
            pVectF[1] = (long)(battleUnit.cy - groundY);
         }

         if ((distance = MathGame.integerSquareRoot(pVectF[0] * pVectF[0] + pVectF[1] * pVectF[1])) == 0L) {
            distance = 1L;
         }

         pVectF[0] = pVectF[0] * (long)bottleEffect2.punchPower / distance;
         pVectF[1] = pVectF[1] * (long)bottleEffect2.punchPower / distance;
         battleUnit.addIntertion(new int[]{(int)pVectF[0], (int)pVectF[1]}, Math.abs(bottleEffect2.punchPower) * c_battle_InertSpeed);
      }

      if (bottleEffect2.punchPower < 0) {
         pVectF = new long[2];
         if (targetToUnit >= 0 && battleUnit.hisInd == targetToUnit) {
            pVectF[0] = (long)(startX - battleUnit.cx);
            pVectF[1] = (long)(startY - battleUnit.cy);
         } else if (bottleEffect2.pushFromIniter) {
            pVectF[0] = (long)(battleUnit2.cx - battleUnit.cx);
            pVectF[1] = (long)(battleUnit2.cy - battleUnit.cy);
         } else {
            pVectF[0] = (long)(groundX - battleUnit.cx);
            pVectF[1] = (long)(groundY - battleUnit.cy);
         }

         if ((distance = MathGame.integerSquareRoot(pVectF[0] * pVectF[0] + pVectF[1] * pVectF[1])) == 0L) {
            distance = 1L;
         }

         pVectF[0] = pVectF[0] * (long)(-bottleEffect2.punchPower) / distance;
         pVectF[1] = pVectF[1] * (long)(-bottleEffect2.punchPower) / distance;
         battleUnit.addIntertion(new int[]{(int)pVectF[0], (int)pVectF[1]}, Math.abs(bottleEffect2.punchPower) * c_battle_InertSpeed);
      }

   }

   private void applyBottleEffect(BattleUnit battleUnit, BottleEffect bottleEffect2, BattleUnit battleUnit2) {
      if (battleUnit != null) {
         if (bottleEffect2.hitPower > 0 && bottleEffect2.appArmorAndShip) {
            battleUnit2.hit(this, calculateUnitDamage(battleUnit2, (long)(bottleEffect2.hitPower * battleUnit.params.answerPowerPerc / 100)), (GameImage)null);
         }

         if (bottleEffect2.vampiricPower > 0) {
            battleUnit2.heal(Math.min((long)bottleEffect2.hitPower, battleUnit.CurCount * 1000L) * (long)bottleEffect2.vampiricPower / 100000L, (GameImage)null);
         }
      }

   }

   public final BattleOrder[] pullAccumOrders() {
      synchronized(this.ordersLock) {
         boolean sendSecond = false;
         long time;
         if (this.accumSecondOrder != null && (time = Mystery.currentTimeMillis) - this.lastApplySecondTime >= (long)c_battle_time_between_actions_temporal) {
            this.lastApplySecondTime = time;
            sendSecond = true;
         }

         BattleOrder[] res = new BattleOrder[this.accumOrders.size() + (sendSecond ? 1 : 0)];
         int i = 0;

         for(Enumeration enu = this.accumOrders.elements(); enu.hasMoreElements(); ++i) {
            BattleOrder battleOrder = (BattleOrder)enu.nextElement();
            res[i] = battleOrder;
         }

         if (sendSecond) {
            res[i] = this.accumSecondOrder;
            this.accumSecondOrder = null;
         }

         this.accumOrders.removeAllElements();
         return res;
      }
   }

   public final void giveOrder(BattleOrder battleOrder) {
      synchronized(this.ordersLock) {
         switch (battleOrder.orderType) {
            case 2:
               this.accumSecondOrder = battleOrder;
               break;
            default:
               this.accumOrders.addElement(battleOrder);
         }

      }
   }

   public final void drawDistPunchs(GameGraphics gameGraphics2) {
      synchronized(this.totalLock) {
         for(int sizeIndex = 0; sizeIndex < this.distPunchs.size(); ++sizeIndex) {
            BattleDistPunch battleDistPunch = (BattleDistPunch)this.distPunchs.elementAt(sizeIndex);
            BattleUnit battleUnit = this.getUnit(battleDistPunch.atackerInd);
            BattleUnit battleUnit2 = this.getUnit(battleDistPunch.targetInd);
            if (battleUnit != null && (battleDistPunch.targetInd < 0 || battleUnit2 != null)) {
               int vertOffsetForEffects;
               int vertOffsetForEffects2 = vertOffsetForEffects = battleUnit.getVertOffsetForEffects(0);
               int groundX2 = battleDistPunch.groundX;
               int groundY2 = battleDistPunch.groundY;
               if (battleUnit2 != null) {
                  groundX2 = battleUnit2.cx;
                  groundY2 = battleUnit2.cy;
                  vertOffsetForEffects2 = battleUnit2.getVertOffsetForEffects(0);
               }

               int cx = groundX2 - battleUnit.cx;
               vertOffsetForEffects2 = groundY2 + vertOffsetForEffects2 / 2 - (battleUnit.cy + vertOffsetForEffects / 2);
               groundX2 = battleDistPunch.bottleEffect.timeOfBulletFlight;
               cx = battleUnit.cx + cx * Math.min(battleDistPunch.gameTime, groundX2) / groundX2;
               vertOffsetForEffects2 = battleUnit.cy + vertOffsetForEffects / 2 + vertOffsetForEffects2 * Math.min(battleDistPunch.gameTime, groundX2) / groundX2;
               AnimSprite animSprite;
               AnimSprite animSprite2 = animSprite = ImagesGlobal.animClientSprites[battleDistPunch.getGPSpriteInd()];
               groundX2 = battleDistPunch.gameTime;
               animSprite2.draw(gameGraphics2, Math.max(0, Math.min(groundX2 / animSprite.AnimTime, animSprite.numFrames - 1)), cx - animSprite.swHalf, vertOffsetForEffects2 - animSprite.shHalf, battleDistPunch.flyDirect);
            }
         }

      }
   }

   public final void drawUnits(GameGraphics gameGraphics2) {
      synchronized(this.totalLock) {
         Enumeration enumeration = this.unitsInOrder.elements();

         while(enumeration.hasMoreElements()) {
            BattleUnit battleUnit;
            BattleUnit battleUnit2;
            if ((battleUnit = (BattleUnit)enumeration.nextElement()).ai_Atacking && (battleUnit2 = this.getUnit(battleUnit.atk_Target)) != null) {
               BattleUnit.drawAniBush(gameGraphics2, battleUnit.atk_TimeAcc, battleUnit.bushInd, battleUnit.bushBaseX, battleUnit.bushBaseY, battleUnit.bushLatency, battleUnit.cx, battleUnit.cy, battleUnit.watchDirection, battleUnit2.cx, battleUnit2.cy, battleUnit2.getVertOffsetForEffects(0));
            }
         }

      }
   }

   public final void iterateGraphicUnits(Camera camera, Vector vector3, DrawOrderList drawOrderList2, boolean highlightSelf, boolean highlightFriends, boolean highlightEnemys, Vector vector4) {
      synchronized(this.totalLock) {
         BattleUnit battleUnit;
         for(Enumeration enu = this.unitsInOrder.elements(); enu.hasMoreElements(); battleUnit.Drawed = true) {
            battleUnit = (BattleUnit)enu.nextElement();
            BattleUnit battleUnit2 = battleUnit;
            Vector vector5 = vector3;
            byte highlightType = -1;
            boolean orderByUnit = true;
            if (BattleTh.currentChoosedUnit == battleUnit.hisInd) {
               highlightType = 2;
               orderByUnit = false;
            } else if (this.playerAvatar != null) {
               if (highlightSelf && this.playerAvatar.hisInd == battleUnit.hisInd) {
                  highlightType = 3;
               } else if (highlightFriends && this.playerAvatar.side == battleUnit.side) {
                  highlightType = 3;
               } else if (highlightEnemys && this.playerAvatar.side != battleUnit.side) {
                  highlightType = 4;
               } else if (this.playerAvatar.ai_targetAtackUnit == battleUnit.hisInd) {
                  highlightType = 4;
               }
            }

            if (vector3.size() > 0) {
               for(int sizeIndex = 0; sizeIndex < vector5.size(); ++sizeIndex) {
                  BattleSkillCircle battleSkillCircle;
                  if ((battleSkillCircle = (BattleSkillCircle)vector5.elementAt(sizeIndex)).markSelf && battleUnit2.hisInd == battleSkillCircle.ovenrUnit || battleSkillCircle.markFriends && battleUnit2.side == battleSkillCircle.ovnerSide && MathGame.isWithinRadius((long)battleUnit2.cx, (long)battleUnit2.cy, (long)battleSkillCircle.cx, (long)battleSkillCircle.cy, (long)battleSkillCircle.radius) || battleSkillCircle.markEnemys && battleUnit2.side != battleSkillCircle.ovnerSide && MathGame.isWithinRadius((long)battleUnit2.cx, (long)battleUnit2.cy, (long)battleSkillCircle.cx, (long)battleSkillCircle.cy, (long)battleSkillCircle.radius)) {
                     highlightType = 2;
                  }
               }
            }

            if (highlightType >= 0) {
               int[] paddings = battleUnit2.getPaddings();
               GameSimpleObjectHighlight gameSimpleObjectHighlight = new GameSimpleObjectHighlight(battleUnit2.cx, battleUnit2.cy, orderByUnit ? battleUnit2.cy : (short)(Com.currentBattleMap.heightPx + Tile.height), paddings[0] + -2, paddings[1] + -2, paddings[3] + -2, paddings[2] + -2, highlightType);
               drawOrderList2.addAndSortFromEnd2(gameSimpleObjectHighlight);
            }

            drawOrderList2.addAndSortFromEnd2(BattleDrawUnitOrderElement.getInstance(battleUnit2.cx, battleUnit2.cy, battleUnit2.cy, battleUnit2, this));
            BattleOffscreenIcon battleOffscreenIcon;
            if ((battleOffscreenIcon = BattleOffscreenIcon.tryCreate(camera, battleUnit2.cx, battleUnit2.cy)) != null) {
               switch (battleUnit2.getCoverType(this)) {
                  case 0:
                     battleOffscreenIcon.setImage(ImagesGlobal.iconBattleOffscreenFriend);
                     break;
                  case 1:
                  default:
                     battleOffscreenIcon.setImage(ImagesGlobal.iconBattleOffscreenEnemy);
                     break;
                  case 2:
                     battleOffscreenIcon.setImage(ImagesGlobal.iconBattleOffscreenMy);
               }

               vector4.addElement(battleOffscreenIcon);
            }
         }

         for(int sizeIndex2 = 0; sizeIndex2 < this.deadmans.size(); ++sizeIndex2) {
            BattleDeadman battleDeadman = (BattleDeadman)this.deadmans.elementAt(sizeIndex2);
            drawOrderList2.addAndSortFromEnd2(battleDeadman);
         }

      }
   }

   public final boolean isWasEnd() {
      return this.wasEnd;
   }

   public final int getSpeedUpForQuantsCount() {
      return this.speedUpForQuantsCount;
   }

   protected final BattleUnit getPlayerAvatar() {
      synchronized(this.totalLock) {
         if (this.playerAvatar != null) {
            return this.playerAvatar;
         } else {
            BattleUnit battleUnit = null;
            BattleUnit[] units2 = this.getPlaterUnitsArray();

            for(int i = 0; i < units2.length && battleUnit == null; ++i) {
               if (units2[i].playersAvatar) {
                  battleUnit = units2[i];
               }
            }

            return battleUnit;
         }
      }
   }

   protected final BattleUnit[] getUnitsArray() {
      synchronized(this.totalLock) {
         BattleUnit[] res = new BattleUnit[this.unitsInOrder.size()];
         int i = 0;

         for(Enumeration enu = this.unitsInOrder.elements(); enu.hasMoreElements(); ++i) {
            res[i] = (BattleUnit)enu.nextElement();
         }

         return res;
      }
   }

   protected final BattleUnit[] getPlaterUnitsArray() {
      if (this.playerMember == null) {
         return new BattleUnit[0];
      } else {
         synchronized(this.totalLock) {
            Vector vector3 = new Vector();
            Enumeration enumeration = this.playerMember.hisUnits.elements();

            while(enumeration.hasMoreElements()) {
               BattleUnit battleUnit;
               if ((battleUnit = this.getUnit((Integer)enumeration.nextElement())) != null) {
                  vector3.addElement(battleUnit);
               }
            }

            BattleUnit[] res = new BattleUnit[vector3.size()];
            int i = 0;

            for(Enumeration enu = vector3.elements(); enu.hasMoreElements(); ++i) {
               res[i] = (BattleUnit)enu.nextElement();
            }

            return res;
         }
      }
   }

   static {
      int[][] unusedJumpOffsets = new int[][]{{-18, -10}, {-17, -25}};
      c_battle_time_between_actions_temporal = 1200;
      c_rage_distance = 50;
      c_rage_AddVal = 50;
      c_rage_AddTime = 3000;
      c_battle_rage_push_min_rnd_d3 = 100;
      c_battle_rage_push_max_rnd_d3 = 1000;
      c_battle_rage_push_100 = 1000;
      c_rage_Bushs = new int[]{0, 9, 10, 11};
      c_rage_Coofs_int = new int[]{100, 200, 300};
      c_rage_Levels = new int[]{100, 200, 300};
      c_rage_Colors = new int[]{8355711, 16776960, 16711680};
      c_rage_Add = 50;
      c_rage_Sub = 10;
      c_battle_action_random_min = 500;
      c_battle_action_random_max = 1500;
      c_atack_images_inds = new int[]{1, 2, 3, 5, 7, 15, 14, 17, 19, 21, 23, 24, 16, 20, 22, 25, 26, 27, 18, 28, 29, 30, 31, 32, 33, 34};
      c_heart_images_inds = new int[]{36, -1, 36, -1, -1, 36, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, 37, -1, -1, -1, -1};
   }
}
