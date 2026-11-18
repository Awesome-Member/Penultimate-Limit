package Base;

import Engine.AnimSprite;
import Engine.Camera;
import Engine.Mystery;
import desktop.PlatformRuntime;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class GameEffects {
   public static final GameEffects instance = new GameEffects();
   private static final int[] LIGHT_COLOR = new int[]{16777215, 16777215, 16777215, 16777215, 16777215, 16777215};
   private boolean drawFallSkulls = false;
   private Vector fallSkulls = new Vector();
   private int fallSkullsAccumTime;
   private int fallSkullsCreateAccumTime;
   private boolean drawMirage = false;
   private GameEffectFrame[] gameEffectFrameEntries;
   private int mirageW = 22;
   private int mirageH = 22;
   private GameImage mirageImage;
   private GameGraphics mirageBuffer;
   private boolean drawQuake = false;
   private boolean drawLightning = false;
   private Vector lightings = new Vector();
   private boolean drawRain = false;
   private int rainType;
   private int[][] rainBits;
   private int rainAccumTime;
   private int rainAllTime;

   public GameEffects() {
      boolean unusedProgressMarker = true;
      unusedProgressMarker = true;
      if (PlatformRuntime.getInstance().getPlatformType() == 1) {
         this.mirageW = 22;
         this.mirageH = 22;
         this.mirageImage = GameImage.createImage(22, 22);
         this.mirageBuffer = this.mirageImage.getGraphics();
      }

      this.clear();
   }

   public final void clear() {
      this.drawFallSkulls = false;
      this.drawQuake = false;
      this.drawLightning = false;
      this.drawRain = false;
   }

   public final void drawRain(GameGraphics gameGraphics2, Camera camera) {
      GameGraphics gameGraphics3 = gameGraphics2;
      GameEffects gameEffects = this;
      if (this.drawRain) {
         if (this.rainType == 1) {
            if (Mystery.BuffImage != null) {
               Camera camera2 = camera;
               GameImage BuffImage2 = Mystery.BuffImage;
               gameGraphics3 = gameGraphics2;
               gameEffects = this;
               if (PlatformRuntime.getInstance().getPlatformType() == 1 && this.drawMirage) {
                  for(int gameEffectFrameEntrieIndex = 0; gameEffectFrameEntrieIndex < gameEffects.gameEffectFrameEntries.length; ++gameEffectFrameEntrieIndex) {
                     gameEffects.mirageBuffer.drawImage(BuffImage2, -gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].x, -gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].y, 0);
                     gameGraphics3.drawImage(gameEffects.mirageImage, 1 + gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].x + camera2.getCx(), gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].y + camera2.getCy(), 0);
                  }
               }
            }
         } else {
            int rainBitIndex;
            if (this.rainType == 0) {
               gameGraphics2.setColor(2930687);

               for(rainBitIndex = 0; rainBitIndex < gameEffects.rainBits.length; ++rainBitIndex) {
                  gameGraphics3.drawLine(gameEffects.rainBits[rainBitIndex][0], gameEffects.rainBits[rainBitIndex][1], gameEffects.rainBits[rainBitIndex][0] + 5, gameEffects.rainBits[rainBitIndex][1] - 10);
               }
            } else if (this.rainType == 2) {
               gameGraphics2.setColor(11590141);

               for(rainBitIndex = 0; rainBitIndex < gameEffects.rainBits.length; ++rainBitIndex) {
                  gameGraphics3.drawLine(gameEffects.rainBits[rainBitIndex][0], gameEffects.rainBits[rainBitIndex][1], gameEffects.rainBits[rainBitIndex][0] + 10, gameEffects.rainBits[rainBitIndex][1] + 2);
               }
            }
         }
      }

   }

   public final void drawSkullsFall(GameGraphics gameGraphics2) {
      GameGraphics gameGraphics3 = gameGraphics2;
      GameEffects gameEffects = this;
      if (this.drawFallSkulls) {
         try {
            for(int i = gameEffects.fallSkulls.size() - 1; i >= 0; --i) {
               FloatingEffectPosition floatingEffectPosition = (FloatingEffectPosition)gameEffects.fallSkulls.elementAt(i);
               ImagesGlobal.animClientSprites[floatingEffectPosition.spriteIndex].draw(gameGraphics3, ImagesGlobal.GSprtsSost[floatingEffectPosition.spriteIndex], floatingEffectPosition.x, floatingEffectPosition.y);
            }

            return;
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }

   }

   public final void animateRain(Camera camera) {
      int animClientSpriteIndex;
      GameEffects gameEffects;
      if (this.drawFallSkulls) {
         for(animClientSpriteIndex = 10; animClientSpriteIndex < ImagesGlobal.animClientSprites.length && animClientSpriteIndex <= 11; ++animClientSpriteIndex) {
            ImagesGlobal.animClientSprites[animClientSpriteIndex].advanceAnimation(ImagesGlobal.GSprtsSost[animClientSpriteIndex], (int)Mystery.FrameTimeI);
         }

         gameEffects = this;

         for(this.fallSkullsCreateAccumTime = (int)((long)this.fallSkullsCreateAccumTime + Mystery.FrameTimeI); gameEffects.fallSkullsCreateAccumTime >= 260; gameEffects.fallSkullsCreateAccumTime -= 260) {
            gameEffects.fallSkulls.addElement(new FloatingEffectPosition(Mystery.RandInt(Com.cameraScreen.getCw()), -12, Mystery.RandInt(2) + 10));
         }

         for(gameEffects.fallSkullsAccumTime = (int)((long)gameEffects.fallSkullsAccumTime + Mystery.FrameTimeI); gameEffects.fallSkullsAccumTime >= 50; gameEffects.fallSkullsAccumTime -= 50) {
            for(int fallSkullsCount = gameEffects.fallSkulls.size() - 1; fallSkullsCount >= 0; --fallSkullsCount) {
               FloatingEffectPosition floatingEffectPosition = (FloatingEffectPosition)gameEffects.fallSkulls.elementAt(fallSkullsCount);
               floatingEffectPosition.y += 3;
               if (((FloatingEffectPosition)gameEffects.fallSkulls.elementAt(fallSkullsCount)).y > Com.cameraScreen.getCh()) {
                  gameEffects.fallSkulls.removeElementAt(fallSkullsCount);
               }
            }
         }
      }

      if (this.drawRain) {
         Camera camera2 = camera;
         gameEffects = this;
         this.rainAllTime = (int)((long)this.rainAllTime - Mystery.FrameTimeI);
         if (this.rainAllTime <= 0) {
            this.drawRain = false;
            this.rainBits = null;
            return;
         }

         if (this.rainType == 1) {
            int frameTimeI = (int)Mystery.FrameTimeI;
            camera2 = camera;
            animClientSpriteIndex = frameTimeI;
            GameEffects gameEffects2 = this;
            if (PlatformRuntime.getInstance().getPlatformType() == 1 && this.drawMirage) {
               for(int gameEffectFrameEntrieIndex = 0; gameEffectFrameEntrieIndex < gameEffects2.gameEffectFrameEntries.length; ++gameEffectFrameEntrieIndex) {
                  GameEffectFrame gameEffectFrame = gameEffects2.gameEffectFrameEntries[gameEffectFrameEntrieIndex];
                  gameEffectFrame.remainingTime -= animClientSpriteIndex;
                  if (gameEffects2.gameEffectFrameEntries[gameEffectFrameEntrieIndex].remainingTime <= 0) {
                     gameEffects2.gameEffectFrameEntries[gameEffectFrameEntrieIndex].remainingTime = 600 + Mystery.RandInt(600);
                     gameEffects2.gameEffectFrameEntries[gameEffectFrameEntrieIndex].x = Mystery.RandInt(camera2.getCw() - gameEffects2.mirageW);
                     gameEffects2.gameEffectFrameEntries[gameEffectFrameEntrieIndex].y = Mystery.RandInt(camera2.getCh() - gameEffects2.mirageH);
                  }
               }
            }

            return;
         }

         if (this.rainType == 0 || this.rainType == 2) {
            this.rainAccumTime = (int)((long)this.rainAccumTime + Mystery.FrameTimeI);
            if (this.rainAccumTime >= 100) {
               this.rainAccumTime %= 100;

               for(int rainBitIndex = 0; rainBitIndex < gameEffects.rainBits.length; ++rainBitIndex) {
                  gameEffects.rainBits[rainBitIndex][0] = Mystery.RandInt(camera2.getCw()) + camera2.getCx();
                  gameEffects.rainBits[rainBitIndex][1] = Mystery.RandInt(camera2.getCh()) + camera2.getCy();
               }
            }
         }
      }

   }

   public final void startRain(int Time, int type, Camera camera) {
      this.drawRain = true;
      this.rainType = type;
      this.rainAllTime = Time;
      this.rainAccumTime = 0;
      if (this.rainType == 0) {
         this.rainBits = new int[camera.getCw() * camera.getCh() / 600][2];
      } else if (this.rainType == 2) {
         this.rainBits = new int[camera.getCw() * camera.getCh() / 600][2];
      } else {
         if (this.rainType == 1) {
            Camera camera2 = camera;
            boolean unusedProgressMarker = true;
            GameEffects gameEffects = this;
            if (PlatformRuntime.getInstance().getPlatformType() == 1) {
               this.drawMirage = true;
               this.gameEffectFrameEntries = new GameEffectFrame[(camera.getCw() * camera.getCh() << 2) / 128 / 128];

               for(int gameEffectFrameEntrieIndex = 0; gameEffectFrameEntrieIndex < gameEffects.gameEffectFrameEntries.length; ++gameEffectFrameEntrieIndex) {
                  gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex] = new GameEffectFrame();
                  gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].remainingTime = 600 + Mystery.RandInt(600);
                  gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].x = Mystery.RandInt(camera2.getCw() - gameEffects.mirageW);
                  gameEffects.gameEffectFrameEntries[gameEffectFrameEntrieIndex].y = Mystery.RandInt(camera2.getCh() - gameEffects.mirageH);
               }
            }
         }

      }
   }

   public final void stopRain() {
      this.drawRain = false;
   }

   public final boolean isDrawRain() {
      return this.drawRain;
   }

   public static void spawnClientEffect6(int x, int y) {
      AnimSprite animSprite = ImagesGlobal.animClientSprites[6];
      GameCommons.instance.addGameSimpleObject((GameSimpleObject)(new GameSimpleObjectAnimate((short)(x - animSprite.swHalf), (short)(y - animSprite.sh), (short)y, animSprite)));
   }

   public static void spawnClientEffect4(int x, int y) {
      AnimSprite animSprite = ImagesGlobal.animClientSprites[4];
      GameCommons.instance.addGameSimpleObject((GameSimpleObject)(new GameSimpleObjectAnimate((short)(x - animSprite.swHalf), (short)(y - animSprite.sh), (short)y, animSprite)));
   }

   public final void stopSkullsFall() {
      this.drawFallSkulls = true;
      this.fallSkulls.removeAllElements();
      this.fallSkullsAccumTime = 0;
      this.fallSkullsCreateAccumTime = 0;
   }

   public final void stopSkullsFall2() {
      this.drawFallSkulls = false;
      this.fallSkulls.removeAllElements();
   }
}
