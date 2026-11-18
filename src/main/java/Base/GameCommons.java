package Base;

import Engine.Camera;
import Engine.MathGame;
import Engine.MystFont;
import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import MenuPck.GUIDecorator;
import MenuPck.MenuManager;
import MenuPck.UIComponent;
import MenuPck.UIComponentHandler;
import MenuPck.UIConteiner;
import desktop.PlatformRuntime;
import Moduls.PFter;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.AuraType;
import Moduls.DrawOrderList;
import Moduls.MapBuffer;
import java.util.Vector;
import desktop.graphics.GameGraphics;

public final class GameCommons implements UIComponentHandler {
   public static final GameCommons instance = new GameCommons();
   public boolean loadMap = false;
   public MystFont guiFont;
   public int guiLineSpacing;
   public int guiSoftSpacing;
   public boolean guiAnimate = true;
   public UIConteiner uiConteiner = new UIConteiner();
   private UIComponent floatComponent;
   private UIComponent timerComponent;
   private final Object timerLock;
   private boolean timerEnable;
   private int timerCurTime;
   private int timerTimerMode;
   private int timerTimerAccum;
   private final Object lockFloatText;
   private Vector floatTexts;
   private boolean showRateMenu;
   private boolean showShareMenu;
   private boolean redrawMapBuffer;
   private boolean wasInit;
   public boolean isFromGameMenu;
   private final Vector simpleObjects;

   public GameCommons() {
      this.floatComponent = new UIComponent(this, GUIDecorator.instance, 81, 7, false);
      this.timerComponent = new UIComponent(this, GUIDecorator.instance, 82, 8, false, false);
      this.timerLock = new Object();
      this.lockFloatText = new Object();
      this.floatTexts = new Vector();
      this.showRateMenu = false;
      this.showShareMenu = false;
      this.redrawMapBuffer = true;
      this.wasInit = false;
      this.isFromGameMenu = false;
      this.simpleObjects = new Vector();
   }

   public final void init() {
      this.wasInit = true;
   }

   public final void clear() {
      this.timerEnable = false;
      this.redrawMapBuffer = true;
      synchronized(this.simpleObjects) {
         this.simpleObjects.removeAllElements();
      }
   }

   public final boolean isGameLoopAvailable() {
      return !this.loadMap && Com.currentGameMap.serverIndex >= 0 && Strategist.instance.curMapInd >= 0 && Com.currentGameMap.serverIndex == Strategist.instance.curMapInd;
   }

   public final void addGameSimpleObject(GameSimpleObject gameSimpleObject) {
      synchronized(this.simpleObjects) {
         this.simpleObjects.addElement(gameSimpleObject);
      }
   }

   public final void onChangeResolution() {
      this.repositionateFloatText();
      this.initTimer();
   }

   public final void onActiveCameraChanged(Camera camera, TileMap tileMap) {
      if (Com.flgs_GameRuned && Com.MB.Create(camera, Com.Tiles)) {
         Com.MB.Reset();
         if (!this.loadMap) {
            Com.MB.TryRedraw(camera, tileMap, Com.Tiles);
         }
      }

   }

   public static void paintSymbolOnMount(GameGraphics gameGraphics2, UIComponent uIComponent15, String symbol, int textHorizPadding, boolean left) {
      if (uIComponent15.enabled && uIComponent15.visible) {
         int textY = uIComponent15.yContent + (uIComponent15.heightContent - Com.fontBig.fontHeight) / 2;
         int xContent;
         if (left) {
            xContent = uIComponent15.xContent + textHorizPadding;
         } else {
            int textWidth = Com.fontBig.getTextWidth(symbol);
            xContent = uIComponent15.xContent + uIComponent15.widthContent - textWidth - textHorizPadding;
         }

         Com.fontBig.writeText(gameGraphics2, symbol, xContent, textY);
      }
   }

   public static void paintComplexSymbolOnMount(UIComponent uIComponent15, GameGraphics gameGraphics2, String s1, String s2) {
      if (uIComponent15.enabled && uIComponent15.visible) {
         int textWidth = Com.fontDigitGame.getTextWidth(s1);
         int textWidth2 = Com.fontBig.getTextWidth(s2);
         int digitTextY = uIComponent15.yContent + (uIComponent15.heightContent - Com.fontDigitGame.fontHeight) / 2;
         int labelTextY = uIComponent15.yContent + (uIComponent15.heightContent - Com.fontBig.fontHeight) / 2;
         int textX = uIComponent15.xContent + (uIComponent15.widthContent - (textWidth + textWidth2)) / 2;
         Com.fontDigitGame.writeText(gameGraphics2, s1, textX, digitTextY);
         Com.fontBig.writeText(gameGraphics2, s2, textX + textWidth, labelTextY);
      }
   }

   public final void startShareMenu() {
      this.showShareMenu = true;
   }

   public final void startTimer(int set, int mode) {
      synchronized(this.timerLock) {
         this.timerCurTime = set;
         this.timerTimerMode = mode;
         this.timerTimerAccum = 0;
         this.timerEnable = true;
         this.initTimer();
      }
   }

   private void initTimer() {
      synchronized(this.timerLock) {
         if (this.timerEnable) {
            String s = String.valueOf(this.timerCurTime);
            this.timerComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, 4, 0, 0, Com.fontDigitGame.getTextWidth(s) + 4 + Com.fontBig.getTextWidth("ˤ˥"), 0, Com.fontDigitGame.fontHeight);
         }
      }
   }

   public final void stopTimer() {
      synchronized(this.timerLock) {
         this.timerEnable = false;
      }
   }

   private void floatTextAnimate() {
      synchronized(this.lockFloatText) {
         if (this.floatTexts.size() != 0) {
            FloatText floatText;
            FloatText floatText2 = floatText = (FloatText)this.floatTexts.elementAt(0);
            floatText2.spendTime = (int)((long)floatText2.spendTime + Mystery.FrameTimeI);
            if (floatText.spendTime >= floatText.delay + floatText.showTime || floatText.clicked && floatText.spendTime >= floatText.delay + floatText.clickHoldTime) {
               this.floatTexts.removeElementAt(0);
               this.initFloatText();
            }

         }
      }
   }

   public final void closeFloatTextByClick() {
      synchronized(this.lockFloatText) {
         if (this.floatTexts.size() != 0) {
            FloatText floatText;
            if ((floatText = (FloatText)this.floatTexts.elementAt(0)).spendTime >= floatText.delay + floatText.clickProtectionTime) {
               floatText.clicked = true;
            }

         }
      }
   }

   public final void showFloatText(String messageText) {
      this.showFloatText((String)messageText, 3000);
   }

   public final void showFloatText(String messageText, int textTime) {
      this.showFloatText((String)messageText, textTime, 0, 230, 0);
   }

   public final void showFloatText(String messageText, int textTime, int delay) {
      this.showFloatText((String)messageText, 3000, 0, 230, delay);
   }

   public final void showFloatText(String messageText, int showTime, int clickProtectionTime, int clickHoldTime, int delay) {
      MystText mystText = new MystText();
      MystString mystString;
      (mystString = new MystString()).Create(messageText, Com.fontBig);
      mystText.create(mystString, Math.min(200, Com.cameraScreen.getCw() - 30), 1, Com.fontBig);
      synchronized(this.lockFloatText) {
         this.floatTexts.addElement(new FloatText(mystText, showTime, clickProtectionTime, clickHoldTime, delay));
         this.initFloatText();
      }
   }

   private void initFloatText() {
      synchronized(this.lockFloatText) {
         if (this.floatTexts.size() != 0) {
            FloatText floatText;
            int needHeight = (floatText = (FloatText)this.floatTexts.elementAt(0)).text.getMaxHeight(Com.fontBig);
            PFter pFter;
            if ((pFter = Com.mainTh.getTopPFter()) != null && pFter instanceof GameCommonsExtender) {
               int[] space = new int[2];
               ((GameCommonsExtender)pFter).getFloatScreenSpace(space);
               this.floatComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, space[1] - space[0] >= needHeight ? 10 : 11, space[1] - space[0] >= needHeight ? space[1] : (space[0] + space[1]) / 2, 0, floatText.text.getMaxWidth() + 4, 0, needHeight);
            } else {
               this.floatComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, 3, 0, 0, floatText.text.getMaxWidth() + 4, 0, needHeight);
            }

         }
      }
   }

   public final void repositionateFloatText() {
      synchronized(this.lockFloatText) {
         PFter pFter;
         if ((pFter = Com.mainTh.getTopPFter()) != null && pFter instanceof GameCommonsExtender) {
            int[] space = new int[2];
            ((GameCommonsExtender)pFter).getFloatScreenSpace(space);
            this.floatComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, space[1] - space[0] >= this.floatComponent.heightByPosition ? 10 : 11, space[1] - space[0] >= this.floatComponent.heightByPosition ? space[1] : (space[0] + space[1]) / 2, 0, this.floatComponent.widthContentByPosition, 0, this.floatComponent.heightContentByPosition);
         } else {
            this.floatComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, 3, 0, 0, this.floatComponent.widthContentByPosition, 0, this.floatComponent.heightContentByPosition);
         }

      }
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 81:
            if (this.floatTexts.size() == 0) {
               return;
            }

            FloatText floatText;
            if ((floatText = (FloatText)this.floatTexts.elementAt(0)).spendTime >= floatText.delay) {
               floatText.text.draw(gameGraphics2, 2, 0, uIComponent15.widthContent, uIComponent15.heightContent, 0, 0, Com.fontBig);
               return;
            }
            break;
         case 82:
            if (!this.timerEnable) {
               return;
            }

            int w = Com.fontBig.getTextWidth("ˤ˥");
            Com.fontDigitGame.writeText(gameGraphics2, String.valueOf(this.timerCurTime), w + 2, 0);
      }

   }

   public final void redrawMapBuffer() {
      this.redrawMapBuffer = true;
   }

   public final void mapBufferSetMarkUnblockedTiles(boolean isMark) {
      Com.MB.setMarkUnblockedTiles(isMark);
      this.redrawMapBuffer = true;
   }

   public final void paintInScreenCoord(GameGraphics gameGraphics2, Camera camera, TileMap tileMap) {
      boolean b = false;
      int sY;
      int sX;
      int eX;
      int clipHeight;
      if (!Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1) {
         Com.MB.SetRDs(false);
         sY = 0;

         for(sX = Com.Tiles.length; sY < sX; ++sY) {
            if (Com.Tiles[sY].Self.numFrames > 1 && Com.Tiles[sY].Self.WasFrame) {
               b = true;
               Com.Tiles[sY].Self.WasFrame = false;
               Com.MB.invalidateColumn(sY, tileMap);
            }
         }

         sY = 0;

         for(sX = Com.triangleDecors.length; sY < sX; ++sY) {
            eX = Com.triangleDecors[sY];
            if (Com.Decors[eX].Self.numFrames > 1 && Com.Decors[eX].Self.WasFrame) {
               b = true;
               Com.Decors[eX].Self.WasFrame = false;
               Com.MB.invalidateRow(eX, tileMap);
            }
         }

         if (b) {
            Com.MB.ReDrawAll(tileMap, Com.Tiles);
         }

         Com.MB.SetRDs(true);
         if (this.redrawMapBuffer) {
            this.redrawMapBuffer = false;
            Com.MB.ReDrawAll(tileMap, Com.Tiles);
         }

         sY = gameGraphics2.getClipX();
         sX = gameGraphics2.getClipY();
         eX = gameGraphics2.getClipWidth();
         clipHeight = gameGraphics2.getClipHeight();
         gameGraphics2.setClip(0, 0, camera.getCw(), camera.getCh());
         Com.MB.TryRedraw(camera, tileMap, Com.Tiles);
         int mapOffsetX = camera.getCx() - Com.MB.sX * Tile.width;
         int mapOffsetY = camera.getCy() - Com.MB.sY * Tile.height;
         gameGraphics2.drawImage(Com.MB.fsrc, -mapOffsetX, -mapOffsetY, 0);
         gameGraphics2.setClip(sY, sX, eX, clipHeight);
      } else {
         MapBuffer MB2 = Com.MB;
         sX = (int)MathGame.floorDivide((long)camera.getCx(), (long)Tile.width);
         sY = (int)MathGame.floorDivide((long)camera.getCy(), (long)Tile.height);
         eX = sX + MB2.tW - 1;
         int eY = sY + MB2.tH - 1;
         MB2.sX = sX;
         MB2.sY = sY;
         MB2.eX = eX;
         MB2.eY = eY;
         sY = camera.getCx() - Com.MB.sX * Tile.width;
         sX = camera.getCy() - Com.MB.sY * Tile.height;
         eX = gameGraphics2.getTranslateX();
         clipHeight = gameGraphics2.getTranslateY();
         gameGraphics2.translate(-eX - sY, -clipHeight - sX);

         try {
            Com.MB.Draw(gameGraphics2, tileMap, Com.Tiles, Com.MB.sX, Com.MB.sY, Com.MB.eX, Com.MB.eY);
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         gameGraphics2.translate(eX + sY, clipHeight + sX);
      }
   }

   public static void draw(GameGraphics gameGraphics2, Camera camera) {
      GameEffects.instance.drawRain(gameGraphics2, camera);
   }

   public final void timerDraw(GameGraphics gameGraphics2) {
      if (this.wasInit) {
         GameGraphics gameGraphics3 = gameGraphics2;
         GameCommons gameCommons = this;
         synchronized(this.timerLock) {
            if (gameCommons.timerEnable) {
               gameCommons.timerComponent.paint(gameGraphics3);
               paintSymbolOnMount(gameGraphics3, gameCommons.timerComponent, "ˤ˥", 2, true);
            }
         }

         gameGraphics3 = gameGraphics2;
         gameCommons = this;
         synchronized(this.lockFloatText) {
            if (gameCommons.floatTexts.size() != 0) {
               FloatText floatText;
               if ((floatText = (FloatText)gameCommons.floatTexts.elementAt(0)).spendTime >= floatText.delay) {
                  gameCommons.floatComponent.paint(gameGraphics3);
               }
            }
         }

         GameEffects.instance.drawSkullsFall(gameGraphics2);
      }
   }

   public final void functionateDraw(DrawOrderList drawOrderList2) {
      synchronized(this.simpleObjects) {
         for(int i = this.simpleObjects.size() - 1; i >= 0; --i) {
            GameSimpleObject gameSimpleObject;
            if ((gameSimpleObject = (GameSimpleObject)this.simpleObjects.elementAt(i)).update((int)Mystery.FrameTimeI)) {
               this.simpleObjects.removeElementAt(i);
            }

            drawOrderList2.addAndSortFromEnd2(gameSimpleObject);
         }

      }
   }

   public static void animateCamera(Camera camera) {
      camera.animate();
   }

   public final void functionateLocal(Camera camera) {
      MenuManager menuManager;
      if (this.showShareMenu && !MenuManager.instance.guiHandler.isActive()) {
         this.showShareMenu = false;
         MenuManager.instance.showMenuById(227);
         menuManager = MenuManager.instance;
         MenuManager.getMenuListById(227).disableBackgroundClick();
         GameEffects.instance.stopSkullsFall();
      }

      if (this.showRateMenu && !MenuManager.instance.guiHandler.isActive()) {
         this.showRateMenu = false;
         MenuManager.instance.showMenuById(226);
         menuManager = MenuManager.instance;
         MenuManager.getMenuListById(226).disableBackgroundClick();
         GameEffects.instance.stopSkullsFall();
      }

      Com.fontSmall.animate();
      if (Com.fontBig != null) {
         Com.fontBig.animate();
      }

      int i;
      for(i = 0; i < Com.Tiles.length; ++i) {
         if (Com.Tiles[i].Self.numFrames > 1) {
            Com.Tiles[i].Self.NextAnimStep();
         }
      }

      for(i = 0; i < Com.Decors.length; ++i) {
         if (Com.Decors[i].Anim) {
            Com.Decors[i].Self.NextAnimStep();
         }
      }

      GameEffects.instance.animateRain(camera);

      for(i = 0; i < Com.AurasTypes.length; ++i) {
         AuraType auraType;
         if ((auraType = Com.AurasTypes[i]).drawable) {
            ImagesGlobal.animServerSprites[auraType.sprite].advanceAnimation(auraType.anim, (int)Mystery.FrameTimeI);
         }
      }

   }

   public final void functionalGlobal() {
      if (this.wasInit) {
         try {
            this.floatTextAnimate();
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         try {
            int resMode = -1;
            synchronized(this.timerLock) {
               if (this.timerEnable && MenuManager.instance != null) {
                  for(this.timerTimerAccum = (int)((long)this.timerTimerAccum + Mystery.FrameTimeI); this.timerTimerAccum >= 1000 && this.timerEnable; this.timerTimerAccum -= 1000) {
                     --this.timerCurTime;
                     if (this.timerCurTime < 0) {
                        this.timerCurTime = 0;
                        this.timerEnable = false;
                        resMode = this.timerTimerMode;
                     }
                  }
               }
            }

            if (resMode >= 0) {
               switch (resMode) {
                  case 4:
                     MenuManager.instance.activateListEl(0);
                     return;
                  case 5:
                     MenuManager.instance.activateListEl(2);
                     return;
                  case 6:
                     MenuManager.instance.activateListEl(1);
                     return;
                  case 7:
                  default:
                     break;
                  case 8:
                     Com.loginFromMenu((String)null);
               }
            }

         } catch (Exception exception2) {
            exception2.printStackTrace();
         }
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
   }
}
