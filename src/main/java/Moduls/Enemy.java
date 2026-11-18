package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Engine.Camera;
import Engine.MystFont;
import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import java.io.DataInputStream;
import java.io.IOException;
import desktop.graphics.GameGraphics;

public final class Enemy extends MoveableUnitDiscretShort {
   public short level = 0;
   private int AniTimeAccum;
   private int CurAnim;
   private boolean AnimDirect;
   private boolean Walk;
   private int MovTimeAccum;
   private int CurMoveMode;
   private int CurWayPoint;
   private int CurMovPattern;
   private int[] MovSpeeds = new int[10];
   private int SensRadSQR;
   public int CurMapSlotX;
   public int CurMapSlotY;
   public boolean Drawed;
   private int BackX;
   private int BackY;
   private int BackMM;
   private boolean BackWalk;
   private boolean drawText = false;
   private MystText text = new MystText();
   private int textTime = 0;
   private int stayTime = 0;
   public String nameId;
   public String Name;
   public String GPFile;
   public EnemySpeechState[] enemySpeechStateEntries;
   private int WayMode;
   public int BodyInd;
   public int HeadInd;
   private int WalkSpeed;
   private int SensRad;
   public int Agression;
   public int gameID;
   public boolean fightable = false;
   public int direction = 1;
   public boolean Atacking;
   public boolean Enable;
   private boolean StratInteres;
   public int respaunTime;
   public short fightingBushInd = 0;
   public short fightingBushBaseX = 0;
   public short fightingBushBaseY = 0;
   public short fightingBushLatency = 0;
   public static int c_AwaySensRadCoof;
   private static int[] c_AniMaxVal = new int[]{1, 1};
   private static int[] c_AniSpeed = new int[]{333, 100};
   public static int c_StartBattleDist;
   public static int c_StartInteresDist;
   public static int[][][] c_MovPatterns;
   private boolean FromRespaun = false;
   private boolean RespaunEffectStage;
   private long LastRespaunEffect;
   public boolean drawQuestCompas = false;

   public final void beginRespaun() {
      this.FromRespaun = true;
      this.RespaunEffectStage = false;
      this.LastRespaunEffect = Mystery.currentTimeMillis;
   }

   public final void startRespawnEffect() {
      this.FromRespaun = false;
      this.RespaunEffectStage = false;
   }

   public Enemy() {
      super((short)0, (short)0, (short)10, (short)5);
      this.ResetAnimation();
      this.MovTimeAccum = 0;
      if (this.WayMode == 2) {
         this.CurWayPoint = -1;
         this.CurMovPattern = Mystery.RandInt(c_MovPatterns.length);
      } else {
         this.CurWayPoint = 0;
         this.CurMovPattern = 0;
      }

      this.Walk = false;
      this.Drawed = true;
      this.StratInteres = true;
      this.direction = Mystery.RandInt(2) == 0 ? 1 : 0;
   }

   private void SetMoveMode(int SetMode) {
      this.CurMoveMode = SetMode;
      boolean Walk2 = this.Walk;
      this.Walk = this.CurMoveMode != 0;
      this.MovTimeAccum = 0;
      if (Walk2 != this.Walk) {
         this.ResetAnimation();
      }

   }

   public final void draw(GameGraphics gameGraphics2, boolean drawPower) {
      if (this.FromRespaun) {
         if (Mystery.currentTimeMillis - this.LastRespaunEffect > 200L) {
            this.LastRespaunEffect = Mystery.currentTimeMillis;
            this.RespaunEffectStage = !this.RespaunEffectStage;
         }

         if (!this.RespaunEffectStage) {
            return;
         }
      }

      if (this.drawQuestCompas) {
         ImagesGlobal.addingAnimSprites[26].drawCenter(gameGraphics2, this.cx, this.cy, 0);
      }

      int unitInfoVerticalOffset = 0;
      int curAnim = 0;
      if (this.Walk) {
         curAnim = -this.CurAnim;
      } else {
         unitInfoVerticalOffset = this.CurAnim;
      }

      Legionery.draw(gameGraphics2, this.cx, this.cy, this.BodyInd, this.HeadInd, this.direction, curAnim, unitInfoVerticalOffset);
      unitInfoVerticalOffset = Legionery.getUnitInfoVerticalOffset(0, this.BodyInd, this.HeadInd, (StrategGraphic)null);
      int maxHeight;
      if (this.drawText) {
         ImagesGlobal.addingAnimSprites[5].drawHCenterBottom(gameGraphics2, this.cx, this.cy + unitInfoVerticalOffset, 0);
         maxHeight = this.text.getMaxHeight(Com.fontSmall);
         this.text.draw(gameGraphics2, this.cx - 63, this.cy + unitInfoVerticalOffset - ImagesGlobal.addingAnimSprites[5].rh + 1 + (18 - maxHeight) / 2, 124, 18, 0, 0, Com.fontSmall);
      } else {
         if (this.level > 0 && drawPower) {
            maxHeight = 9;
            if ((curAnim = this.level - Strategist.instance.GP_Level) >= 0 && curAnim >= Com.c_enemy_levels_dif_strong) {
               maxHeight = 43;
            }

            if (curAnim < 0 && -curAnim >= Com.c_enemy_levels_dif_weak) {
               maxHeight = 7;
            }

            ImagesGlobal.addingAnimSprites[maxHeight].draw(gameGraphics2, this.cx - 12, this.cy + unitInfoVerticalOffset - 19 + 4, 0);
            String levelText = String.valueOf(this.level);
            Com.fontSmall.writeText(gameGraphics2, levelText, this.cx - MystFont.getStringLengthSpecSymb(levelText, Com.fontSmall) * Com.c_font_width / 2, this.cy + unitInfoVerticalOffset - 19 + 4);
         }

      }
   }

   private void ResetAnimation() {
      this.AniTimeAccum = 0;
      this.AnimDirect = true;
      this.CurAnim = 0;
   }

   private void LoadBack() {
      this.cx = (short)this.BackX;
      this.cy = (short)this.BackY;
      this.SetMoveMode(this.BackMM);
      this.Walk = this.BackWalk;
   }

   public final boolean isFightiable() {
      return !this.FromRespaun && this.StratInteres && this.Atacking && this.fightable;
   }

   public final boolean iteration(Camera camera, Strategist strategist2, TileMap tileMap) {
      this.drawQuestCompas = false;
      this.AniTimeAccum = (int)((long)this.AniTimeAccum + Mystery.FrameTimeI);
      int deltaX = 0;
      if (this.Walk) {
         deltaX = 1;
      }

      for(; this.AniTimeAccum >= c_AniSpeed[deltaX]; this.AniTimeAccum -= c_AniSpeed[deltaX]) {
         if (this.AnimDirect) {
            ++this.CurAnim;
            if (this.CurAnim == c_AniMaxVal[deltaX]) {
               this.AnimDirect = false;
            }
         } else {
            --this.CurAnim;
            if (this.CurAnim == 0) {
               this.AnimDirect = true;
            }
         }
      }

      deltaX = this.cx - strategist2.cx;
      int deltaX3 = this.cy - strategist2.cy;
      deltaX = deltaX * deltaX + deltaX3 * deltaX3;
      if (!this.FromRespaun && deltaX <= c_StartBattleDist && this.StratInteres && this.Atacking) {
         return true;
      } else {
         if (!this.StratInteres) {
            this.StratInteres = deltaX >= c_StartInteresDist;
         }

         if (this.CurMoveMode != 3 && this.CurMoveMode != 4) {
            if (deltaX <= this.SensRadSQR && this.StratInteres) {
               if (this.CurMoveMode != 5) {
                  this.BackX = this.cx;
                  this.BackY = this.cy;
                  this.BackMM = this.CurMoveMode;
                  this.BackWalk = this.Walk;
               }

               if (this.Agression == 0) {
                  this.SetMoveMode(3);
               } else if (this.Agression == 2) {
                  this.SetMoveMode(4);
               }
            }
         } else if (deltaX >= this.SensRadSQR * c_AwaySensRadCoof / 10 || !this.StratInteres) {
            this.SetMoveMode(5);
         }

         this.MovTimeAccum = (int)((long)this.MovTimeAccum + Mystery.FrameTimeI);
         this.stayTime = (int)((long)this.stayTime - Mystery.FrameTimeI);
         if (this.drawText) {
            this.textTime = (int)((long)this.textTime - Mystery.FrameTimeI);
            if (this.textTime <= 0) {
               this.textTime = 0;
               this.drawText = false;
            }
         }

         deltaX3 = this.cx;
         int cy2 = this.cy;
         if (this.CurMoveMode == 1) {
            if (this.stayTime <= 0) {
               this.Walk = true;
               this.stayTime = 0;
               if ((deltaX = this.CurWayPoint + 1) >= this.enemySpeechStateEntries.length) {
                  deltaX = 0;
               }

               deltaX3 = this.enemySpeechStateEntries[deltaX].position[0];
               cy2 = this.enemySpeechStateEntries[deltaX].position[1];
            } else {
               this.Walk = false;
            }
         } else if (this.CurMoveMode == 2) {
            if ((deltaX = this.CurWayPoint + 1) >= c_MovPatterns[this.CurMovPattern].length) {
               deltaX = 0;
            }

            if (!this.Walk) {
               if (this.MovTimeAccum >= c_MovPatterns[this.CurMovPattern][deltaX][2]) {
                  this.ResetAnimation();
                  this.Walk = true;
                  this.MovTimeAccum = 0;
                  ++this.CurWayPoint;
                  if (this.CurWayPoint >= c_MovPatterns[this.CurMovPattern].length - 1) {
                     this.CurWayPoint = -1;
                     this.CurMovPattern = Mystery.RandInt(c_MovPatterns.length);
                  }

                  deltaX3 = this.enemySpeechStateEntries[0].position[0] + c_MovPatterns[this.CurMovPattern][this.CurWayPoint + 1][0];
                  cy2 = this.enemySpeechStateEntries[0].position[1] + c_MovPatterns[this.CurMovPattern][this.CurWayPoint + 1][1];
               }
            } else {
               deltaX3 = this.enemySpeechStateEntries[0].position[0] + c_MovPatterns[this.CurMovPattern][deltaX][0];
               cy2 = this.enemySpeechStateEntries[0].position[1] + c_MovPatterns[this.CurMovPattern][deltaX][1];
            }
         } else if (this.CurMoveMode == 3) {
            deltaX3 = this.cx + this.cx - strategist2.cx;
            cy2 = this.cy + this.cy - strategist2.cy;
            if (deltaX >= this.SensRadSQR * c_AwaySensRadCoof * 9 / 100) {
               deltaX3 = this.cx;
               cy2 = this.cy;
            }
         } else if (this.CurMoveMode == 4) {
            deltaX3 = strategist2.cx;
            cy2 = strategist2.cy;
         } else if (this.CurMoveMode == 5) {
            deltaX3 = this.BackX;
            cy2 = this.BackY;
            if (!this.Drawed && !camera.isPointVisible(this.BackX, this.BackY, 11, 36)) {
               this.LoadBack();
            }
         }

         int moveDirectionToTarget;
         if ((moveDirectionToTarget = this.getMoveDirectionToTarget(deltaX3, cy2)) >= 8 && this.CurMoveMode != 0 && this.CurMoveMode != 2) {
            this.MovTimeAccum = 0;
         }

         if (this.CurMoveMode == 0) {
            int deltaX2;
            if ((deltaX2 = strategist2.cx - this.cx) < 0) {
               this.direction = 1;
            } else if (deltaX2 > 0) {
               this.direction = 0;
            }
         }

         if (moveDirectionToTarget < 8) {
            for(boolean b = true; this.MovTimeAccum >= this.MovSpeeds[moveDirectionToTarget] && b; this.MovTimeAccum -= this.MovSpeeds[moveDirectionToTarget]) {
               short cx2 = this.cx;
               short cy3 = this.cy;
               this.collidMoveStep(moveDirectionToTarget, tileMap);
               if (this.cx - cx2 < 0) {
                  this.direction = 1;
               } else if (this.cx - cx2 > 0) {
                  this.direction = 0;
               }

               moveDirectionToTarget = this.getMoveDirectionToTarget(deltaX3, cy2);
               if (this.CurMoveMode == 2 && moveDirectionToTarget < 8 && cx2 == this.cx && cy3 == this.cy) {
                  ++this.CurWayPoint;
                  if (this.CurWayPoint >= c_MovPatterns[this.CurMovPattern].length - 1) {
                     this.CurWayPoint = -1;
                     this.CurMovPattern = Mystery.RandInt(c_MovPatterns.length);
                  }
               }

               if (moveDirectionToTarget >= 8) {
                  b = false;
                  if (this.CurMoveMode == 1) {
                     do {
                        ++this.CurWayPoint;
                        if (this.CurWayPoint >= this.enemySpeechStateEntries.length) {
                           this.CurWayPoint = 0;
                        }

                        this.stayTime = this.enemySpeechStateEntries[this.CurWayPoint].elapsedTime;
                        this.drawText = this.enemySpeechStateEntries[this.CurWayPoint].active;
                        if (this.drawText) {
                           MystString mystString;
                           (mystString = new MystString()).Create(this.enemySpeechStateEntries[this.CurWayPoint].string, Com.fontSmall);
                           this.text.create(mystString, 110, 2, '\u0000', 1, Com.fontSmall);
                           this.textTime = this.enemySpeechStateEntries[this.CurWayPoint].duration;
                        }

                        if (this.enemySpeechStateEntries[this.CurWayPoint].displayed) {
                           if ((deltaX = this.CurWayPoint + 1) >= this.enemySpeechStateEntries.length) {
                              deltaX = 0;
                           }

                           this.cx = (short)this.enemySpeechStateEntries[deltaX].position[0];
                           this.cy = (short)this.enemySpeechStateEntries[deltaX].position[1];
                        }
                     } while(this.enemySpeechStateEntries[this.CurWayPoint].displayed);
                  } else if (this.CurMoveMode == 2) {
                     this.ResetAnimation();
                     this.Walk = false;
                  } else if (this.CurMoveMode == 5) {
                     this.getMoveDirectionToTarget(deltaX3, cy2);
                     this.LoadBack();
                  }
               }
            }
         }

         return false;
      }
   }

   public final void LoadFromStream(DataInputStream dataInputStream, boolean loadFull, boolean initParams) {
      try {
         this.nameId = dataInputStream.readUTF();
         this.Name = dataInputStream.readUTF();
         this.GPFile = dataInputStream.readUTF();
         this.fightable = !this.GPFile.equals("no");
         if (loadFull) {
            int integerCode = dataInputStream.readInt();
            this.enemySpeechStateEntries = new EnemySpeechState[integerCode];

            for(int enemySpeechStateEntrieIndex = 0; enemySpeechStateEntrieIndex < integerCode; ++enemySpeechStateEntrieIndex) {
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex] = new EnemySpeechState();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].position[0] = dataInputStream.readInt();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].position[1] = dataInputStream.readInt();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].elapsedTime = dataInputStream.readInt();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].duration = dataInputStream.readInt();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].active = dataInputStream.readBoolean();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].string = dataInputStream.readUTF();
               this.enemySpeechStateEntries[enemySpeechStateEntrieIndex].displayed = dataInputStream.readBoolean();
            }
         }

         this.Atacking = dataInputStream.readBoolean();
         if (loadFull) {
            this.WayMode = dataInputStream.readInt();
         }

         this.BodyInd = dataInputStream.readInt();
         this.HeadInd = dataInputStream.readInt();
         if (loadFull) {
            this.WalkSpeed = dataInputStream.readInt();
            this.SensRad = dataInputStream.readInt();
         }

         this.Agression = dataInputStream.readInt();
         this.gameID = dataInputStream.readInt();
         this.level = dataInputStream.readShort();
         if (!this.fightable) {
            this.level = 0;
         }

         this.Enable = dataInputStream.readBoolean();
         this.respaunTime = dataInputStream.readInt();
         this.fightingBushInd = dataInputStream.readShort();
         this.fightingBushBaseX = dataInputStream.readShort();
         this.fightingBushBaseY = dataInputStream.readShort();
         this.fightingBushLatency = dataInputStream.readShort();
      } catch (IOException exception) {
         Com.SendMistace("Can't load enemy", false, true, exception);
      }

      if (initParams) {
         this.reinitParams(this.enemySpeechStateEntries[0].position[0], this.enemySpeechStateEntries[0].position[1]);
      }

   }

   public final void reinitParams(int cx, int cy) {
      this.setGridPosition((short)cx, (short)cy);
      if (this.WayMode == 1) {
         this.SetMoveMode(0);
      } else if (this.WayMode == 0) {
         this.SetMoveMode(1);
      } else {
         this.SetMoveMode(this.WayMode);
      }

      for(cx = 1; cx < 10; cx += 2) {
         this.MovSpeeds[cx] = this.WalkSpeed;
      }

      for(cx = 0; cx < 10; cx += 2) {
         this.MovSpeeds[cx] = this.WalkSpeed * 1404 / 1000;
      }

      if (this.WayMode == 2) {
         this.CurWayPoint = -1;
         this.CurMovPattern = Mystery.RandInt(c_MovPatterns.length);
      } else {
         this.CurWayPoint = 0;
      }

      this.SensRadSQR = this.SensRad * this.SensRad;
   }
}
