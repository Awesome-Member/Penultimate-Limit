package Moduls.otherheroes;

import Base.GameSimpleObjectHighlight;
import Base.GameWalkPathElement;
import Engine.Mystery;
import Moduls.StrategAnimGroup;
import Moduls.StrategGraphic;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.chat.ChatMessage;
import java.io.DataInputStream;
import java.util.Vector;

public final class HeroOnMap {
   public boolean incomeResetPath = false;
   public boolean incomeIsFull = false;
   public int incomeFlags = 0;
   public String name;
   public String clan;
   public Vector path;
   public int cx;
   public int cy;
   public byte watchDirection = 0;
   public int id;
   public int level;
   public int swords;
   public boolean incomeFightingData = false;
   public short fightingWithMobInd = -1;
   public short fightingAtackMobBushInd = 0;
   public short fightingAtackMobBushBaseX = 0;
   public short fightingAtackMobBushBaseY = 0;
   public int fightingAtackMobBushLatency = 0;
   public long visibleSinceTime;
   public int petH;
   public int petB;
   public int partyId = -1;
   public boolean availAtack = false;
   public StrategGraphic strategGraphic;
   private short walkSpeed = 29;
   public byte addHighlighFlags = 0;
   public int petX = -1;
   public int petY = -1;
   public int petDirect = 1;
   private long noPathStartTime = -1L;
   private boolean setAnimationState = false;
   public long animationTime = 0L;
   public byte animationState;
   private int accPathTime;
   public int onIncomeLeftPathCount;
   public ChatMessage headMessage;

   public HeroOnMap() {
      this.animationState = StrategAnimGroup.ANIMATION_STATE_STAY;
      this.accPathTime = 0;
      this.onIncomeLeftPathCount = 0;
      this.headMessage = null;
      this.visibleSinceTime = Mystery.currentTimeMillis;
   }

   public final void clear() {
      this.incomeFightingData = false;
      this.headMessage = null;
      this.incomeResetPath = false;
      this.incomeIsFull = false;
      this.name = null;
      this.clan = null;
      this.path = null;
      this.cx = -1;
      this.cy = -1;
      this.watchDirection = 0;
      this.id = -1;
      this.level = 0;
      this.swords = 0;
      this.petH = -1;
      this.petB = -1;
      this.partyId = -1;
      this.availAtack = false;
      this.strategGraphic = null;
      this.walkSpeed = 29;
      this.addHighlighFlags = 0;
      this.visibleSinceTime = Mystery.currentTimeMillis;
      this.clearCalculations();
   }

   private void clearCalculations() {
      this.petX = -1;
      this.petY = -1;
      this.petDirect = 1;
      this.animationTime = 0L;
      this.animationState = StrategAnimGroup.ANIMATION_STATE_STAY;
      this.setAnimationState = false;
      this.noPathStartTime = -1L;
   }

   public final void copyFromIncomeToWasForFullIncome(HeroOnMap heroOnMap) {
      this.name = heroOnMap.name;
      this.clan = heroOnMap.clan;
      this.level = heroOnMap.level;
      this.walkSpeed = heroOnMap.walkSpeed;
      this.petB = heroOnMap.petB;
      this.petH = heroOnMap.petH;
      this.strategGraphic = heroOnMap.strategGraphic;
      this.partyId = heroOnMap.partyId;
      this.fightingAtackMobBushInd = heroOnMap.fightingAtackMobBushInd;
      this.fightingAtackMobBushBaseX = heroOnMap.fightingAtackMobBushBaseX;
      this.fightingAtackMobBushBaseY = heroOnMap.fightingAtackMobBushBaseY;
      this.fightingAtackMobBushLatency = heroOnMap.fightingAtackMobBushLatency;
      if (heroOnMap.incomeResetPath) {
         this.cx = heroOnMap.cx;
         this.cy = heroOnMap.cy;
         this.watchDirection = heroOnMap.watchDirection;
         this.clearPath();
      }

   }

   public final void loadFromNet(DataInputStream dataInputStream) {
      this.name = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.clan = decompiledsupport.UncheckedDataInput.readUTF(dataInputStream);
      this.cx = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.cy = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.watchDirection = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.level = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.walkSpeed = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.petB = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.petH = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.fightingAtackMobBushInd = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.fightingAtackMobBushBaseX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.fightingAtackMobBushBaseY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.fightingAtackMobBushLatency = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
      this.strategGraphic = new StrategGraphic(dataInputStream);
      this.partyId = decompiledsupport.UncheckedDataInput.readInt(dataInputStream);
   }

   public final void clearPath() {
      this.accPathTime = 0;
      this.path.removeAllElements();
      this.clearCalculations();
   }

   public final void iterate(int time) {
      this.animationTime += (long)time;
      this.accPathTime += time;
      if (this.onIncomeLeftPathCount > 2) {
         this.accPathTime += time / 2;
         this.animationTime += (long)(time / 2);
      }

      time = this.accPathTime / 50;

      for(int timeIndex = 0; timeIndex < time && this.path.size() > 1; ++timeIndex) {
         this.path.removeElementAt(0);
         --this.onIncomeLeftPathCount;
         if (this.onIncomeLeftPathCount < 0) {
            this.onIncomeLeftPathCount = 0;
         }
      }

      this.accPathTime %= 50;
      GameWalkPathElement gameWalkPathElement;
      if (this.path.size() > 1) {
         this.noPathStartTime = -1L;
         gameWalkPathElement = (GameWalkPathElement)this.path.elementAt(0);
         GameWalkPathElement gameWalkPathElement2;
         if ((gameWalkPathElement2 = (GameWalkPathElement)this.path.elementAt(1)).x >= 0 && gameWalkPathElement2.y >= 0) {
            short x = gameWalkPathElement.x < 0 ? (short)(-gameWalkPathElement.x) : gameWalkPathElement.x;
            short y = gameWalkPathElement.y < 0 ? (short)(-gameWalkPathElement.y) : gameWalkPathElement.y;
            short x2 = gameWalkPathElement2.x < 0 ? (short)(-gameWalkPathElement2.x) : gameWalkPathElement2.x;
            short y2 = gameWalkPathElement2.y < 0 ? (short)(-gameWalkPathElement2.y) : gameWalkPathElement2.y;
            this.cx = (x2 - x) * this.accPathTime / 50 + x;
            this.cy = (y2 - y) * this.accPathTime / 50 + y;
            HeroOnMap heroOnMap;
            int watchDirection2;
            if (x2 == x) {
               heroOnMap = this;
               watchDirection2 = gameWalkPathElement2.watchDirection;
            } else {
               heroOnMap = this;
               watchDirection2 = x2 > x ? 0 : 1;
            }

            heroOnMap.watchDirection = (byte)watchDirection2;
            if (x2 != x || y2 != y) {
               this.setAnimationState = true;
               if (this.animationState != StrategAnimGroup.ANIMATION_STATE_WALK) {
                  this.animationState = StrategAnimGroup.ANIMATION_STATE_WALK;
                  this.animationTime = 0L;
               }

               return;
            }

            this.setStay();
         } else {
            this.cx = -gameWalkPathElement2.x;
            this.cy = -gameWalkPathElement2.y;
            this.watchDirection = gameWalkPathElement2.watchDirection;
            this.setStay();
         }
      } else {
         if (this.path.size() == 1) {
            gameWalkPathElement = (GameWalkPathElement)this.path.elementAt(0);
            this.cx = Math.abs(gameWalkPathElement.x);
            this.cy = Math.abs(gameWalkPathElement.y);
            this.watchDirection = gameWalkPathElement.watchDirection;
            if (this.noPathStartTime < 0L) {
               this.noPathStartTime = Mystery.currentTimeMillis;
            }

            if (!this.setAnimationState) {
               this.setStay();
            }

            if (!this.isStay() && Mystery.currentTimeMillis - this.noPathStartTime >= 1000L) {
               this.setStay();
            }

            return;
         }

         if (this.noPathStartTime < 0L) {
            this.noPathStartTime = Mystery.currentTimeMillis;
         }

         if (!this.setAnimationState) {
            this.setStay();
         }

         if (!this.isStay() && Mystery.currentTimeMillis - this.noPathStartTime >= 1000L) {
            this.setStay();
         }
      }

   }

   private boolean isStay() {
      return this.animationState == StrategAnimGroup.ANIMATION_STATE_STAY;
   }

   private void setStay() {
      this.setAnimationState = true;
      if (this.animationState != StrategAnimGroup.ANIMATION_STATE_STAY) {
         this.animationState = StrategAnimGroup.ANIMATION_STATE_STAY;
         this.animationTime = 0L;
      }

   }

   public final GameSimpleObjectHighlight createGameSimpleObjectHighlight(int type, TileMap tileMap) {
      int[] paddingsNoAura = this.strategGraphic.getBasePaddingsNoAura(this.watchDirection);
      byte highlightType;
      switch (type) {
         case 0:
            highlightType = 2;
            break;
         case 1:
            highlightType = 4;
            break;
         case 2:
            highlightType = 3;
            break;
         default:
            highlightType = 2;
      }

      return new GameSimpleObjectHighlight((short)this.cx, (short)this.cy, (short)(tileMap.heightPx + Tile.height), paddingsNoAura[0] + -2, paddingsNoAura[1] + -2, paddingsNoAura[3] + -2, paddingsNoAura[2] + -2, highlightType);
   }
}
