package Base;

import Engine.AnimSprite;
import Engine.Camera;
import Engine.Delegate;
import Engine.MathGame;
import Engine.MystFont;
import Engine.Mystery;
import Engine.Pair;
import MenuPck.GUIDecorator;
import MenuPck.GUIHandler;
import MenuPck.MenuManager;
import MenuPck.UIComponent;
import MenuPck.UIComponentHandler;
import MenuPck.ListTypes.ItemsMenuList;
import MenuPck.ListTypes.MainMenuList;
import MenuPck.ListTypes.NeighborsMenuList;
import MenuPck.ListTypes.OtherMenuList;
import MenuPck.ListTypes.ServicesMenuList;
import MenuPck.ListTypes.TradeMenuList;
import Moduls.PFter;
import Moduls.StrategAnimGroup;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.TileMapElement;
import Moduls.Town;
import Moduls.TownAvailabilityFlag;
import Moduls.Decor;
import Moduls.DrawOrderElement;
import Moduls.DrawOrderElementEnemy;
import Moduls.DrawOrderElementHitBullt;
import Moduls.DrawOrderElementOtherStrateg;
import Moduls.DrawOrderElementStrateg;
import Moduls.DrawOrderList;
import Moduls.Enemy;
import Moduls.EnemysMapListener;
import Moduls.Legionery;
import Moduls.chat.Chat;
import Moduls.otherheroes.HeroOnMap;
import Moduls.otherheroes.HeroesOnMapManager;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestsOnMap;
import Moduls.trading.PlayersTrade;
import Moduls.trading.TradeSuggestion;
import Resources.StringResources;
import battlepck.Battle;
import battlepck.BattleManager;
import battlepck.calls.BattleCall;
import battlepck.calls.BattleCallsHero;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class Game implements GameCommonsExtender, UIComponentHandler, PFter {
   private static int[][] TROPHYS_OFFSETS = new int[][]{{-1, -2}, {-1, -2}, {-1, -2}, {-1, -2}, {-1, -2}, {-1, -2}, {-1, -2}, {-1, -2}};
   public static boolean incomeBlockChangeMap = false;
   public boolean waitChangeMap = false;
   private boolean prevLoadMap = false;
   public boolean sendWhatInBlock = false;
   private boolean myPathActive = false;
   private Vector myPathAccum = new Vector();
   private int myPathAccumTime = 0;
   private final Object myPathLock = new Object();
   private int uiScreenHeight = 0;
   private int uiScreenWidth = 0;
   private long latencyVal = -1L;
   private long fpsLastFixTime;
   private GameSelectionStructure highlights = new GameSelectionStructure();
   private int tryRainAccum;
   private int post1State = 0;
   private int post2State = 0;
   private int post3State = 0;
   private int post4State = 0;
   private int post1BlinkTime = 0;
   private int post2BlinkTime = 0;
   private int post3BlinkTime = 0;
   private int post4BlinkTime = 0;
   public boolean isHavePartyForFastFight = false;
   public boolean isDrawTrophys = false;
   private int trophysDelay = 0;
   private int trophysTime = 0;
   private int trophysPhase = 0;
   private int trophysCount = 0;
   private int trophysCurInd = 0;
   private int[][] trophysFrom = new int[8][2];
   private int[][] trophysTo = new int[8][2];
   private int[] trophysTextsWidth = new int[8];
   private String[] trophysTexts = new String[8];
   private String[] trophysSymbs = new String[8];
   private boolean[] trophysColor = new boolean[8];
   private DrawOrderList drawOrderList = new DrawOrderList();
   private boolean joystickActive = false;
   private boolean drawJoystick = false;
   private int joystickCenterX = 0;
   private int joystickCenterY = 0;
   private int joystickStickX = 0;
   private int joystickStickY = 0;
   private int joystickRadius = 48;
   private int joystickDeathZone = 20;
   private boolean fixU = false;
   private boolean fixD = false;
   private boolean fixL = false;
   private boolean fixR = false;
   private boolean autoMove = false;
   private int autoMoveX = 0;
   private int autoMoveY = 0;
   private boolean wasPress = false;
   private boolean dragOverMinimap = false;
   private boolean controllReleasCorrectTimeWasActiveGame = true;
   private GameCrossingsWorldData gameCrossingsWorldData = new GameCrossingsWorldData();
   public GameControllAnalogData gameControllAnalogData = new GameControllAnalogData();
   public GameControllDirectData gameControllDirectData = new GameControllDirectData();
   private UIComponent uiOverlay;
   private UIComponent uiJoystickZone;
   private UIComponent uiMountName;
   private UIComponent uIComponent;
   private UIComponent uiMountGold;
   private UIComponent uiMountCrys;
   private UIComponent uiMountVip;
   private UIComponent uiMountProtection;
   private UIComponent uIComponent2;
   private UIComponent uIComponent3;
   private UIComponent uIComponent4;
   private UIComponent uIComponent5;
   private UIComponent uIComponent6;
   private UIComponent uIComponent7;
   private UIComponent uIComponent8;
   private UIComponent uIComponent9;
   private UIComponent uIComponent10;
   private UIComponent uIComponent11;
   private UIComponent uiMountMiniMap;
   private UIComponent uiMountLifeBar;
   private UIComponent uIComponent12;
   private UIComponent uIComponent13;
   private UIComponent uIComponent14;
   private boolean curVisSetn;
   private boolean curVisGold;
   private boolean curVisCrys;
   private boolean curVisVip;
   private boolean curVisProtection;
   private boolean curVisMiniMap;
   private int[] uiMountTradeTextsWidths;
   private int[] uiMountDuelsTextsWidths;
   private int uiYEndRight;

   public Game() {
      this.uiOverlay = new UIComponent(this, GUIDecorator.instance, 10017, 0, false, false);
      this.uiJoystickZone = new UIComponent(this, GUIDecorator.instance, 10019, 0, false, false);
      this.uiMountName = new UIComponent(this, GUIDecorator.instance, 10001, 4, false, false);
      this.uIComponent = new UIComponent(this, GUIDecorator.instance, 10002, 4, false, false);
      this.uiMountGold = new UIComponent(this, GUIDecorator.instance, 10003, 8, false, false);
      this.uiMountCrys = new UIComponent(this, GUIDecorator.instance, 10004, 8, false, false);
      this.uiMountVip = new UIComponent(this, GUIDecorator.instance, 10005, 8, false, false);
      this.uiMountProtection = new UIComponent(this, GUIDecorator.instance, 10006, 8, false, false);
      this.uIComponent2 = new UIComponent(this, GUIDecorator.instance, 10007, 5, false, false);
      this.uIComponent3 = new UIComponent(this, GUIDecorator.instance, 10008, 5, false, false);
      this.uIComponent4 = new UIComponent(this, GUIDecorator.instance, 10010, 0, false, false);
      this.uIComponent5 = new UIComponent(this, GUIDecorator.instance, 10011, 0, false, false);
      this.uIComponent6 = new UIComponent(this, GUIDecorator.instance, 10012, 0, false, false);
      this.uIComponent7 = new UIComponent(this, GUIDecorator.instance, 10013, 0, false, false);
      this.uIComponent8 = new UIComponent(this, GUIDecorator.instance, 10020, 0, false, false);
      this.uIComponent9 = new UIComponent(this, GUIDecorator.instance, 10014, 0, false, false);
      this.uIComponent10 = new UIComponent(this, GUIDecorator.instance, 10021, 0, false, false);
      this.uIComponent11 = new UIComponent(this, GUIDecorator.instance, 10022, 0, false, false);
      this.uiMountMiniMap = new UIComponent(this, GUIDecorator.instance, 10015, 8, false, false);
      this.uiMountLifeBar = new UIComponent(this, GUIDecorator.instance, 10016, 9, false, false, true);
      this.uIComponent12 = null;
      this.uIComponent13 = null;
      this.uIComponent14 = null;
      this.curVisSetn = false;
      this.curVisGold = false;
      this.curVisCrys = false;
      this.curVisVip = false;
      this.curVisProtection = false;
      this.curVisMiniMap = false;
      this.uiMountTradeTextsWidths = new int[3];
      this.uiMountDuelsTextsWidths = new int[3];
      this.uiYEndRight = 0;
   }

   public final void Set() {
      Com.tryCloseMenuWithNecessaryCheck();
      this.recalcParams(Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
      Com.mainTh.setPFter1(this);
      this.controllReleasCorrectTimeWasActiveGame = true;
      this.playerControllDisableAll();
      this.gameControllAnalogData.disableAtackToPlayer();
      this.curVisSetn = false;
      this.uiMountProtection.enabled = false;
      this.uiMountVip.enabled = false;
      this.highlights.reset();
      GameCommons.instance.onActiveCameraChanged(Com.cameraWorld, Com.currentGameMap);
      Com.cameraWorld.resetStayTimer();
      Com.cameraWorld.centerAt(Strategist.instance.cx, Strategist.instance.cy - 24);
      GameCommons.instance.mapBufferSetMarkUnblockedTiles(false);
      Com.mainTh.setStopPaint();
      this.initPostsState();
   }

   public final void Reset() {
      Com.skipGameLoop = false;
      this.resetOnSetHandling();
      incomeBlockChangeMap = false;
      Com.flgs_GameRuned = true;
      this.dragOverMinimap = false;
      GameCommons.instance.clear();
      GameEffects.instance.clear();
      this.tryRainAccum = 0;
   }

   public final void onChangeResolution(int screenWidth, int screenHeight) {
      if (Com.mainTh.getPFter1() == this) {
         GameCommons.instance.onActiveCameraChanged(Com.cameraWorld, Com.currentGameMap);
         this.recalcParams(screenWidth, screenHeight);
      }
   }

   private void recalcParams(int screenWidth, int screenHeight) {
      this.uiScreenWidth = screenWidth;
      this.uiScreenHeight = screenHeight;
      GameCommons.instance.uiConteiner.clear();
      GameCommons.instance.uiConteiner.addComponent(this.uiOverlay);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountName);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountGold);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountCrys);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountVip);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountProtection);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent2);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent3);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent4);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent5);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent6);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent7);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent8);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent9);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent10);
      GameCommons.instance.uiConteiner.addComponent(this.uIComponent11);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountMiniMap);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountLifeBar);
      GameCommons.instance.uiConteiner.addComponent(this.uiJoystickZone);
      GUIHandler.positionateSoftButtons(2, GameCommons.instance.uiConteiner, this, this.uiScreenWidth, this.uiScreenHeight, Com.fontBig, GameCommons.instance.guiSoftSpacing);
      this.uIComponent12 = GameCommons.instance.uiConteiner.getComponent(12);
      this.uIComponent13 = GameCommons.instance.uiConteiner.getComponent(10);
      this.uIComponent14 = GameCommons.instance.uiConteiner.getComponent(11);
      this.initUI();
      Com.currentGameMap.initMiniMap();
   }

   private void resetOnSetHandling() {
      this.autoMove = false;
      this.fixU = false;
      this.fixD = false;
      this.fixL = false;
      this.fixR = false;
      this.drawJoystick = false;
      this.joystickActive = false;
   }

   private void initPostsState() {
      this.post1State = 1;
      this.post2State = 1;
      this.post3State = Strategist.instance.isInClan ? 1 : 0;
      this.post4State = Strategist.instance.partyState != 2 ? 1 : 0;
      if (Chat.instance.isUnreadnCommon()) {
         this.post1State = 2;
      }

      if (Chat.instance.isUnreadnPrivates()) {
         this.post2State = 2;
      }

      if (Chat.instance.isUnreadnClan() && this.post3State == 1) {
         this.post3State = 2;
      }

      if (Chat.instance.isUnreadnParty() && this.post4State == 1) {
         this.post4State = 2;
      }

   }

   public final void postBlinkCommon() {
      if (this.post1BlinkTime <= 0) {
         this.post1BlinkTime = 2400;
      }

   }

   public final void postBlinkParty() {
      if (this.post4BlinkTime <= 0) {
         this.post4BlinkTime = 2400;
      }

   }

   public final void postBlinkClan() {
      if (this.post3BlinkTime <= 0) {
         this.post3BlinkTime = 2400;
      }

   }

   public final void postBlinkPrivates() {
      if (this.post2BlinkTime <= 0) {
         this.post2BlinkTime = 2400;
      }

   }

   public final void trophyStart(int health, int money, int exp, int items, int fire, int ice, int hero) {
      this.trophyStart(health, money, exp, items, fire, ice, hero, 0);
   }

   public final void trophyStart(int health, int money, int exp, int items, int fire, int ice, int hero, int delay) {
      int[] paddings = Strategist.instance.avatarGraphic.getBasePaddingsNoAuraSimetric();
      this.trophysFrom = new int[][]{{0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}, {0, -paddings[2] * 3 / 4}};
      this.trophysTo = new int[][]{{0, -paddings[2] - 3}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1)}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1 << 1)}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1) * 3}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1 << 2)}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1) * 5}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1) * 6}, {0, -paddings[2] - 3 - (Com.fontSmall.fontHeight + 1) * 7}};
      int parameterIndex = -1;
      if (health != 0) {
         ++parameterIndex;
      }

      if (money != 0) {
         ++parameterIndex;
      }

      if (exp != 0) {
         ++parameterIndex;
      }

      if (items != 0) {
         ++parameterIndex;
      }

      if (fire != 0) {
         ++parameterIndex;
      }

      if (ice != 0) {
         ++parameterIndex;
      }

      if (hero != 0) {
         ++parameterIndex;
      }

      if ((health = (health = (health = (health = (health = (health = (health = 0 + this.trophysAdd(health, parameterIndex, "ɣ")) + this.trophysAdd(exp, parameterIndex - health, "ə")) + this.trophysAdd(money, parameterIndex - health, "ɚ")) + this.trophysAdd(items, parameterIndex - health, "ʁ")) + this.trophysAdd(fire, parameterIndex - health, "ɮ")) + this.trophysAdd(ice, parameterIndex - health, "ɭ")) + this.trophysAdd(hero, parameterIndex - health, "ɯ")) > 0) {
         this.trophysCurInd = 0;
         this.trophysTime = 0;
         this.trophysPhase = 0;
         this.trophysCount = health;
         this.trophysDelay = delay;
         this.isDrawTrophys = true;
      }

   }

   private int trophysAdd(int textWidth, int count, String symbol) {
      if (textWidth == 0) {
         return 0;
      } else {
         this.trophysColor[count] = textWidth > 0;
         this.trophysTexts[count] = (textWidth > 0 ? "+" : "") + textWidth;
         this.trophysSymbs[count] = symbol;
         textWidth = Com.fontGreen.getTextWidth(this.trophysTexts[count]);
         this.trophysTextsWidth[count] = textWidth;
         textWidth = (textWidth + Com.fontSmall.getTextWidth(this.trophysSymbs[count])) * TROPHYS_OFFSETS[count][0] / 2;
         int fontSmall = Com.fontSmall.fontHeight * TROPHYS_OFFSETS[count][1] / 2;
         int[] integers = this.trophysFrom[count];
         integers[0] += textWidth;
         integers = this.trophysFrom[count];
         integers[1] += fontSmall;
         integers = this.trophysTo[count];
         integers[0] += textWidth;
         integers = this.trophysTo[count];
         integers[1] += fontSmall;
         return 1;
      }
   }

   public final void myPathReset() {
      synchronized(this.myPathLock) {
         this.myPathAccum = new Vector();
         this.myPathAccumTime = -1;
      }
   }

   public final void setMyPathActive() {
      synchronized(this.myPathLock) {
         if (!this.myPathActive) {
            this.myPathReset();
            this.myPathActive = true;
         }
      }
   }

   public final void setMyPathActive2() {
      synchronized(this.myPathLock) {
         if (this.myPathActive) {
            this.myPathReset();
            this.myPathActive = false;
         }
      }
   }

   private void myPathIterate(long deltaTime) {
      synchronized(this.myPathLock) {
         if (this.myPathActive) {
            if (this.myPathAccumTime < 0) {
               this.myPathAccumTime = 0;
            }

            this.myPathAccumTime = (int)((long)this.myPathAccumTime + deltaTime);
            int pathAccumTime;
            if ((pathAccumTime = this.myPathAccumTime / 50) > 0) {
               this.myPathAccumTime %= 50;

               for(int stepIndex = 0; stepIndex < pathAccumTime; ++stepIndex) {
                  this.myPathAccum.addElement(new GameWalkPathElement(Strategist.instance.cx, Strategist.instance.cy, Strategist.instance.watchDirection));
               }
            }

         }
      }
   }

   public final Pair myPathPick() {
      synchronized(this.myPathLock) {
         Vector myPathAccum2 = this.myPathAccum;
         this.myPathAccum = new Vector();
         return new Pair(new Short(Com.currentGameMap.serverIndex), myPathAccum2);
      }
   }

   public final void paint(GameGraphics gameGraphics2, boolean justPaint) {
      if (Mystery.currentTimeMillis - this.fpsLastFixTime >= 1000L) {
         this.fpsLastFixTime = Mystery.currentTimeMillis;
      }

      if (GameCommons.instance.loadMap) {
         if (!this.prevLoadMap) {
            this.prevLoadMap = true;
            Com.CoverTh.config(0);
         }

         Com.CoverTh.paint(gameGraphics2, justPaint);
      } else {
         this.prevLoadMap = false;
         QuestInPlayer questInPlayer;
         if ((questInPlayer = QuestStrateg.instance.getCompassedQuest()) == null || Com.currentGameMap.towns == null || Com.EnsMap == null || (Strategist.instance.compassMode & 2) == 0) {
            questInPlayer = null;
         }

         try {
            GameCommons.instance.paintInScreenCoord(gameGraphics2, Com.cameraWorld, Com.currentGameMap);
            gameGraphics2.translate(-gameGraphics2.getTranslateX() - Com.cameraWorld.getCx(), -gameGraphics2.getTranslateY() - Com.cameraWorld.getCy());
            Strategist instance2 = Strategist.instance;
            instance2.forcePower = instance2.GP_Level;
            if (this.drawOrderList.first != null) {
               DrawOrderElement first2 = this.drawOrderList.first;

               do {
                  first2.draw(gameGraphics2);
               } while((first2 = first2.next) != null);
            }

            GameGraphics gameGraphics3;
            int elementCount;
            int calcWaysDirectIndex;
            int trophysToIndex;
            int visibleTextIndex;
            int elementIndex;
            if (Com.cameraWorld.isActiveLongStay()) {
               TileMap currentGameMap2 = Com.currentGameMap;
               MystFont fontSmall2 = Com.fontSmall;
               gameGraphics3 = gameGraphics2;
               TileMap tileMap = currentGameMap2;
               elementCount = currentGameMap2.textsVisible.size();
               calcWaysDirectIndex = Tile.width / 2 - 36;
               trophysToIndex = Tile.height / 2 + -12;

               for(elementIndex = 0; elementIndex < elementCount; ++elementIndex) {
                  visibleTextIndex = (Integer)tileMap.textsVisible.elementAt(elementIndex);
                  tileMap.textsInGameFormat[visibleTextIndex].draw(gameGraphics3, tileMap.textsCoords[0][visibleTextIndex] * Tile.width + calcWaysDirectIndex, tileMap.textsCoords[1][visibleTextIndex] * Tile.height + trophysToIndex, 72, 100, 0, 0, fontSmall2);
               }
            }

            short cy2 = Strategist.instance.cy;
            int cx2 = Strategist.instance.cx;
            gameGraphics3 = gameGraphics2;
            Game game = this;
            if (this.isDrawTrophys && this.trophysDelay <= 0) {
               int trophysCount2;
               int[] integers;
               int[] integers2;
               label214:
               switch (this.trophysPhase) {
                  case 0:
                     for(trophysToIndex = 0; trophysToIndex < game.trophysCurInd; ++trophysToIndex) {
                        trophysCount2 = game.trophysCount - 1 - trophysToIndex;
                        visibleTextIndex = (integers = game.trophysTo[trophysCount2])[0];
                        elementIndex = integers[1];
                        (game.trophysColor[trophysCount2] ? Com.fontGreen : Com.fontRed).writeText(gameGraphics3, game.trophysTexts[trophysCount2], cx2 + visibleTextIndex, cy2 + elementIndex);
                        Com.fontSmall.writeText(gameGraphics3, game.trophysSymbs[trophysCount2], cx2 + visibleTextIndex + game.trophysTextsWidth[trophysCount2], cy2 + elementIndex);
                     }

                     trophysCount2 = game.trophysCount - 1 - game.trophysCurInd;
                     integers = game.trophysTo[trophysCount2];
                     integers2 = game.trophysFrom[trophysCount2];
                     visibleTextIndex = (integers[0] - integers2[0]) * game.trophysTime / 300 + integers2[0];
                     elementIndex = (integers[1] - integers2[1]) * game.trophysTime / 300 + integers2[1];
                     (game.trophysColor[trophysCount2] ? Com.fontGreen : Com.fontRed).writeText(gameGraphics3, game.trophysTexts[trophysCount2], cx2 + visibleTextIndex, cy2 + elementIndex);
                     Com.fontSmall.writeText(gameGraphics3, game.trophysSymbs[trophysCount2], cx2 + visibleTextIndex + game.trophysTextsWidth[trophysCount2], cy2 + elementIndex);
                     break;
                  case 1:
                     trophysToIndex = 0;

                     while(true) {
                        if (trophysToIndex >= game.trophysCount) {
                           break label214;
                        }

                        visibleTextIndex = (integers = game.trophysTo[trophysToIndex])[0];
                        elementIndex = integers[1];
                        (game.trophysColor[trophysToIndex] ? Com.fontGreen : Com.fontRed).writeText(gameGraphics3, game.trophysTexts[trophysToIndex], cx2 + visibleTextIndex, cy2 + elementIndex);
                        Com.fontSmall.writeText(gameGraphics3, game.trophysSymbs[trophysToIndex], cx2 + visibleTextIndex + game.trophysTextsWidth[trophysToIndex], cy2 + elementIndex);
                        ++trophysToIndex;
                     }
                  case 2:
                     for(trophysToIndex = this.trophysCurInd + 1; trophysToIndex < game.trophysCount; ++trophysToIndex) {
                        trophysCount2 = game.trophysCount - 1 - trophysToIndex;
                        visibleTextIndex = (integers = game.trophysTo[trophysCount2])[0];
                        elementIndex = integers[1];
                        (game.trophysColor[trophysCount2] ? Com.fontGreen : Com.fontRed).writeText(gameGraphics3, game.trophysTexts[trophysCount2], cx2 + visibleTextIndex, cy2 + elementIndex);
                        Com.fontSmall.writeText(gameGraphics3, game.trophysSymbs[trophysCount2], cx2 + visibleTextIndex + game.trophysTextsWidth[trophysCount2], cy2 + elementIndex);
                     }

                     trophysCount2 = game.trophysCount - 1 - game.trophysCurInd;
                     visibleTextIndex = (integers2 = game.trophysTo[trophysCount2])[0];
                     elementIndex = (-Com.cameraWorld.getCh() - integers2[1]) * game.trophysTime / 300 + integers2[1];
                     (game.trophysColor[trophysCount2] ? Com.fontGreen : Com.fontRed).writeText(gameGraphics3, game.trophysTexts[trophysCount2], cx2 + visibleTextIndex, cy2 + elementIndex);
                     Com.fontSmall.writeText(gameGraphics3, game.trophysSymbs[trophysCount2], cx2 + visibleTextIndex + game.trophysTextsWidth[trophysCount2], cy2 + elementIndex);
               }
            }

            GameCommons instance3 = GameCommons.instance;
            GameCommons.draw(gameGraphics2, Com.cameraWorld);
            gameGraphics2.translate(Com.cameraWorld.getCx(), Com.cameraWorld.getCy());
            GameCommons.instance.uiConteiner.paint(gameGraphics2);
            if (this.uIComponent3.enabled) {
               if (this.latencyVal < 0L) {
                  this.latencyVal = Mystery.currentTimeMillis - Com.lagTimeLast;
               }

               if (this.latencyVal >= 8000L) {
                  cx2 = Com.fontBig.getTextWidth("ʂʃ");
                  elementCount = this.uIComponent3.yContent + (this.uIComponent3.heightContent - Com.fontBig.fontHeight) / 2;
                  calcWaysDirectIndex = this.uIComponent3.xContent + this.uIComponent3.widthContent - cx2;
                  Com.fontBig.writeText(gameGraphics2, "ʂʃ", calcWaysDirectIndex, elementCount);
               }
            }

            QuestInPlayer questInPlayer2 = questInPlayer;
            GameGraphics gameGraphics4 = gameGraphics2;
            if (questInPlayer != null) {
               int[] integers3 = null;

               for(calcWaysDirectIndex = 0; calcWaysDirectIndex < questInPlayer2.calcWaysDirects.length; ++calcWaysDirectIndex) {
                  switch (questInPlayer2.calcWaysDirects[calcWaysDirectIndex]) {
                     case 1:
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, Com.cameraWorld.getCw() / 2, 0, 17);
                        break;
                     case 2:
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, Com.cameraWorld.getCw() / 2, Com.cameraWorld.getCh(), 33);
                        break;
                     case 3:
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, 0, Com.cameraWorld.getCh() / 2, 6);
                        break;
                     case 4:
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, Com.cameraWorld.getCw(), Com.cameraWorld.getCh() / 2, 10);
                  }
               }

               if (questInPlayer2.questSingle.isNeedToKillPlayer && !questInPlayer2.isTargetPlayerKilled && questInPlayer2.needToKillPlayer.needToKillPlayerId >= 0 && questInPlayer2.needToKillPlayer.needToKillPlayerOnline && !questInPlayer2.calcWaysPlayerVisibleClient && questInPlayer2.calcWaysPlayerId >= 0) {
                  calcWaysDirectIndex = questInPlayer2.calcWaysPlayerPosXClient >= 0 ? questInPlayer2.calcWaysPlayerPosXClient : questInPlayer2.calcWaysPlayerPosXServer;
                  trophysToIndex = questInPlayer2.calcWaysPlayerPosYClient >= 0 ? questInPlayer2.calcWaysPlayerPosYClient : questInPlayer2.calcWaysPlayerPosYServer;
                  integers3 = new int[3];
                  if (Com.cameraWorld.framePoint(calcWaysDirectIndex, trophysToIndex, integers3)) {
                     gameGraphics4.drawImage(ImagesGlobal.questCompass, integers3[0], integers3[1], integers3[2]);
                  }
               }

               for(calcWaysDirectIndex = 0; calcWaysDirectIndex < questInPlayer2.calcWaysEnemys.length; ++calcWaysDirectIndex) {
                  Enemy enemy2;
                  if ((trophysToIndex = questInPlayer2.calcWaysEnemys[calcWaysDirectIndex]) >= 0 && trophysToIndex < Com.EnsMap.Enemys.length && (enemy2 = Com.EnsMap.Enemys[trophysToIndex]).Enable) {
                     short cx3 = enemy2.cx;
                     short cy3 = enemy2.cy;
                     if (integers3 == null) {
                        integers3 = new int[3];
                     }

                     if (Com.cameraWorld.framePoint(cx3, cy3, integers3)) {
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, integers3[0], integers3[1], integers3[2]);
                     }
                  }
               }

               for(calcWaysDirectIndex = 0; calcWaysDirectIndex < questInPlayer2.calcWaysTowns.length; ++calcWaysDirectIndex) {
                  Town town;
                  if ((trophysToIndex = questInPlayer2.calcWaysTowns[calcWaysDirectIndex]) >= 0 && trophysToIndex < Com.currentGameMap.towns.length && (town = Com.currentGameMap.towns[trophysToIndex]).state == 0) {
                     elementIndex = town.dy;
                     if (!town.calcCompasUnderFit && town.SafeSpriteInd >= 0) {
                        Decor decor2 = Com.Decors[town.SafeSpriteInd];
                        elementIndex -= decor2.Offset - decor2.Self.sh / 2;
                     }

                     if (!town.calcCompasUnderFit || !Com.cameraWorld.isPointVisible(town.dx, town.dy)) {
                        if (integers3 == null) {
                           integers3 = new int[3];
                        }

                        Com.cameraWorld.framePoint(town.dx, elementIndex, integers3);
                        gameGraphics4.drawImage(ImagesGlobal.questCompass, integers3[0], integers3[1], integers3[2]);
                     }
                  }
               }
            }

            if (this.drawJoystick) {
               draw(gameGraphics2, this.joystickCenterX, this.joystickCenterY, this.joystickRadius, this.joystickStickX, this.joystickStickY);
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         if (this.waitChangeMap) {
            Com.PrBarTh.paint(gameGraphics2);
         }

      }
   }

   private static void setDrawQuestCompas(QuestInPlayer questInPlayer) {
      if (questInPlayer != null) {
         for(int calcWaysEnemyIndex = 0; calcWaysEnemyIndex < questInPlayer.calcWaysEnemys.length; ++calcWaysEnemyIndex) {
            int calcWaysEnemys;
            if ((calcWaysEnemys = questInPlayer.calcWaysEnemys[calcWaysEnemyIndex]) >= 0 && calcWaysEnemys < Com.EnsMap.Enemys.length) {
               Enemy enemy2 = Com.EnsMap.Enemys[calcWaysEnemys];
               if (Com.cameraWorld.isPointVisible(enemy2.cx, enemy2.cy)) {
                  enemy2.drawQuestCompas = true;
               }
            }
         }

      }
   }

   private static void draw(GameGraphics gameGraphics2, int centerX, int centerY, int radius, int stickX, int stickY) {
      gameGraphics2.setColor(15987688);
      gameGraphics2.drawArc(centerX - radius, centerY - radius, radius << 1, radius << 1, 0, 360);
      gameGraphics2.setColor(1710617);
      gameGraphics2.drawArc(centerX - radius + 1, centerY - radius + 1, (radius << 1) - 2, (radius << 1) - 2, 0, 360);
      gameGraphics2.drawArc(centerX - radius - 1, centerY - radius - 1, (radius << 1) + 2, (radius << 1) + 2, 0, 360);
      gameGraphics2.drawImage(ImagesGlobal.joystickStick, stickX, stickY, 3);
   }

   public final void onAfterHardLoad() {
      Com.showGameCursor();
      this.resetOnSetHandling();
   }

   public final void playerControllDisableAll() {
      this.gameControllAnalogData.disableControll();
      this.gameControllDirectData.disableControll();
   }

   public final void functionate() {
      IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
      if ((Com.currentGameMap.serverIndex < 0 || Com.currentGameMap.serverIndex != Strategist.instance.curMapInd) && Strategist.instance.curMapInd >= 0) {
         this.waitChangeMap = false;
         Com.startGameMap(false);
      }

   }

   public final void functionateUnderDrawProgress() {
      this.functionate();
      GameCommons.instance.uiConteiner.functional((int)Mystery.FrameTimeI);
      boolean canMove;
      if (!Com.skipGameLoop && GameCommons.instance.isGameLoopAvailable()) {
         HeroesOnMapManager.instance.applyHeroes();
         if (this.waitChangeMap) {
            canMove = true;
         } else {
            QuestStrateg.instance.tryCalculateWays(Com.currentGameMap, Com.EnsMap, Strategist.instance);
            QuestStrateg.instance.processUpdateKillPlayers(Strategist.instance);
            canMove = false;
         }
      } else {
         Com.skipGameLoop = false;
         canMove = true;
      }

      if (!canMove) {
         Game game = this;
         if (this.isDrawTrophys) {
            label568: {
               if (this.trophysDelay > 0) {
                  this.trophysDelay = (int)((long)this.trophysDelay - Mystery.FrameTimeI);
                  if (this.trophysDelay > 0) {
                     break label568;
                  }
               }

               this.trophysTime = (int)((long)this.trophysTime + Mystery.FrameTimeI);
               switch (this.trophysPhase) {
                  case 0:
                     if (this.trophysTime > 300) {
                        this.trophysTime %= 300;
                        ++this.trophysCurInd;
                        if (this.trophysCurInd >= this.trophysCount) {
                           this.trophysPhase = 1;
                           this.trophysTime = 0;
                           this.trophysCurInd = 0;
                        }
                     }
                     break;
                  case 1:
                     if (this.trophysTime > 1000) {
                        this.trophysPhase = 2;
                        this.trophysTime = 0;
                     }
                     break;
                  case 2:
                     if (this.trophysTime > 300) {
                        this.trophysTime %= 300;
                        ++this.trophysCurInd;
                        if (this.trophysCurInd >= this.trophysCount) {
                           this.trophysPhase = 0;
                           this.trophysTime = 0;
                           this.trophysCurInd = 0;
                           this.isDrawTrophys = false;
                        }
                     }
               }
            }
         }

         this.initPostsState();
         if (this.post1BlinkTime > 0) {
            this.post1BlinkTime = (int)((long)this.post1BlinkTime - Mystery.FrameTimeI);
            if (this.post1BlinkTime < 0) {
               this.post1BlinkTime = 0;
            }
         }

         if (this.post2BlinkTime > 0) {
            this.post2BlinkTime = (int)((long)this.post2BlinkTime - Mystery.FrameTimeI);
            if (this.post2BlinkTime < 0) {
               this.post2BlinkTime = 0;
            }
         }

         if (this.post3BlinkTime > 0) {
            this.post3BlinkTime = (int)((long)this.post3BlinkTime - Mystery.FrameTimeI);
            if (this.post3BlinkTime < 0) {
               this.post3BlinkTime = 0;
            }
         }

         if (this.post4BlinkTime > 0) {
            this.post4BlinkTime = (int)((long)this.post4BlinkTime - Mystery.FrameTimeI);
            if (this.post4BlinkTime < 0) {
               this.post4BlinkTime = 0;
            }
         }

         if (ClientState.instance.get() == 4 && !GameEffects.instance.isDrawRain()) {
            this.tryRainAccum = (int)((long)this.tryRainAccum + Mystery.FrameTimeI);
            if (Com.currentGameMap.cfg_TryRainTime > 0) {
               for(; game.tryRainAccum >= Com.currentGameMap.cfg_TryRainTime; game.tryRainAccum -= Com.currentGameMap.cfg_TryRainTime) {
                  if (Mystery.RandInt(100) < Com.currentGameMap.cfg_RainChance) {
                     GameEffects.instance.startRain(Com.currentGameMap.cfg_RainWalkTime, Com.currentGameMap.cfg_RainType, Com.cameraWorld);
                  }
               }
            }
         }

         boolean blinkPhase;
         Com.petOffBody = (blinkPhase = (Com.playersInGame = (int)((long)Com.playersInGame + Mystery.FrameTimeI)) / 333 % 2 == 0) ? -2 : 0;
         Com.petOffHead = blinkPhase ? 1 : 0;
         this.isHavePartyForFastFight = false;
         QuestInPlayer questInPlayer;
         if ((questInPlayer = QuestStrateg.instance.getCompassedQuest()) == null || Com.currentGameMap.towns == null || Com.EnsMap == null || (Strategist.instance.compassMode & 2) == 0) {
            questInPlayer = null;
         }

         int calcWaysPlayerId = -1;
         if (questInPlayer != null) {
            calcWaysPlayerId = questInPlayer.calcWaysPlayerId;
         }

         if (questInPlayer != null && questInPlayer.calcWaysPlayerId >= 0) {
            questInPlayer.calcWaysPlayerPosXClient = -1;
            questInPlayer.calcWaysPlayerPosYClient = -1;
            questInPlayer.calcWaysPlayerVisibleClient = false;
         }

         GameCommons.instance.functionateLocal(Com.cameraWorld);
         GameControllDirectData gameControllDirectData2;
         if ((gameControllDirectData2 = this.gameControllDirectData).moveTarget != null) {
            gameControllDirectData2.moveTarget.selectedOtherStrategObj = null;
         }

         if (gameControllDirectData2.selections != null) {
            gameControllDirectData2.selections.selectedOtherStrategObj = null;
         }

         GameControllAnalogData gameControllAnalogData2;
         (gameControllAnalogData2 = this.gameControllAnalogData).activeForAtackPlayerObj = null;
         gameControllAnalogData2.closestEnemyForAtack = -1;
         gameControllAnalogData2.closestEnemyForAtackDistance = -1L;
         this.highlights.selectedOtherStrategObj = null;
         game = this;
         int joystickDeltaY;
         switch (Com.touchControlType) {
            case 0:
            case 2:
            case 3:
               this.gameControllAnalogData.disableControll();
               if (this.drawJoystick || this.joystickActive) {
                  int joystickDeltaX = this.joystickStickX - this.joystickCenterX;
                  joystickDeltaY = this.joystickStickY - this.joystickCenterY;
                  int joystickDistanceSquared;
                  if ((joystickDistanceSquared = joystickDeltaX * joystickDeltaX + joystickDeltaY * joystickDeltaY) > this.joystickDeathZone * this.joystickDeathZone) {
                     int joystickAxisThreshold = joystickDistanceSquared / 4;
                     if (joystickDeltaY * joystickDeltaY > joystickAxisThreshold) {
                        if (joystickDeltaY < 0) {
                           this.gameControllAnalogData.playerActiveMoves[7] = true;
                        } else {
                           this.gameControllAnalogData.playerActiveMoves[3] = true;
                        }
                     }

                     if (joystickDeltaX * joystickDeltaX > joystickAxisThreshold) {
                        if (joystickDeltaX < 0) {
                           this.gameControllAnalogData.playerActiveMoves[5] = true;
                        } else {
                           this.gameControllAnalogData.playerActiveMoves[1] = true;
                        }
                     }
                  }
               }
               break;
            case 1:
               if ((ClientState.instance.get() != 4 || MenuManager.instance.guiHandler.isActive()) && this.autoMove) {
                  this.autoMove = false;
               }

               if (!this.autoMove) {
                  if (this.fixU) {
                     this.fixU = false;
                     this.releaseUp();
                  }

                  if (this.fixD) {
                     this.fixD = false;
                     this.releaseDown();
                  }

                  if (this.fixL) {
                     this.fixL = false;
                     this.releaseLeft();
                  }

                  if (this.fixR) {
                     this.fixR = false;
                     this.releaseRight();
                  }
               }

               if (this.autoMove) {
                  blinkPhase = this.fixU;
                  boolean fixD2 = this.fixD;
                  boolean fixL2 = this.fixL;
                  boolean fixR2 = this.fixR;
                  this.fixU = false;
                  this.fixD = false;
                  this.fixL = false;
                  this.fixR = false;
                  int autoMoveDeltaX = Com.cameraWorld.getCx() + this.autoMoveX - Strategist.instance.cx;
                  int autoMoveDeltaY = Com.cameraWorld.getCy() + this.autoMoveY - Strategist.instance.cy;
                  int autoMoveAxisThreshold = (autoMoveDeltaX * autoMoveDeltaX + autoMoveDeltaY * autoMoveDeltaY) / 4;
                  if (autoMoveDeltaY * autoMoveDeltaY > autoMoveAxisThreshold) {
                     if (autoMoveDeltaY < 0) {
                        this.pressUp();
                        this.fixU = true;
                     } else {
                        this.pressDown();
                        this.fixD = true;
                     }
                  }

                  if (autoMoveDeltaX * autoMoveDeltaX > autoMoveAxisThreshold) {
                     if (autoMoveDeltaX < 0) {
                        this.pressLeft();
                        this.fixL = true;
                     } else {
                        this.pressRight();
                        this.fixR = true;
                     }
                  }

                  if (blinkPhase != this.fixU && blinkPhase) {
                     this.releaseUp();
                  }

                  if (fixD2 != this.fixD && fixD2) {
                     this.releaseDown();
                  }

                  if (fixL2 != this.fixL && fixL2) {
                     this.releaseLeft();
                  }

                  if (fixR2 != this.fixR && fixR2) {
                     this.releaseRight();
                  }
               }
         }

         long elapsedTime;
         for(long FrameTimeI2 = Mystery.FrameTimeI; FrameTimeI2 >= 0L; game.myPathIterate(elapsedTime)) {
            if (FrameTimeI2 > 50L) {
               elapsedTime = 50L;
               FrameTimeI2 -= 50L;
            } else {
               elapsedTime = FrameTimeI2;
               FrameTimeI2 = -1L;
            }

            switch (Com.touchControlType) {
               case 4:
                  if (!game.gameControllDirectData.pathFolower.folow(Strategist.instance, elapsedTime, (long)Strategist.instance.avatarWalkSpeed, Com.currentGameMap)) {
                     game.gameControllDirectData.disableControll();

                     try {
                        System.out.println("WARNING Can't achive animateMovement target: on " + Com.currentGameMap.nameId + " " + game.gameControllDirectData.curPathFindingKeys[0][0] + "x" + game.gameControllDirectData.curPathFindingKeys[0][1] + "->" + game.gameControllDirectData.curPathFindingKeys[1][0] + "x" + game.gameControllDirectData.curPathFindingKeys[1][1]);
                     } catch (Exception exception) {
                        exception.printStackTrace();
                     }
                  }
                  break;
               default:
                  game.gameControllAnalogData.initMoveDirectionResult();
                  Strategist.instance.animateMovement(elapsedTime, (long)Strategist.instance.avatarWalkSpeed, Com.currentGameMap, game.gameControllAnalogData.playerActiveMoveDirectResult, false, -1, -1);
            }
         }

         Strategist.instance.animate();
         int prevStrategCoordX;
         switch (Com.touchControlType) {
            case 4:
               GameControllDirectData gameControllDirectData3 = game.gameControllDirectData;
               prevStrategCoordX = Strategist.instance.cx;
               int prevStrategCoordY = Strategist.instance.cy - 24;
               if (gameControllDirectData3.prevStrategCoordX != prevStrategCoordX || gameControllDirectData3.prevStrategCoordY != prevStrategCoordY) {
                  Com.cameraWorld.smoothMoveTo(prevStrategCoordX, prevStrategCoordY);
               }

               gameControllDirectData3.prevStrategCoordX = prevStrategCoordX;
               gameControllDirectData3.prevStrategCoordY = prevStrategCoordY;
               break;
            default:
               Com.cameraWorld.smoothMoveTo(Strategist.instance.cx, Strategist.instance.cy - 24);
         }

         if (Com.currentGameMap.getBlockTl((int)MathGame.floorDivide((long)Strategist.instance.cx, (long)Tile.width), (int)MathGame.floorDivide((long)Strategist.instance.cy, (long)Tile.height))) {
            Com.sendWhatInBlock();
         }

         DrawOrderList drawOrderList2;
         (drawOrderList2 = new DrawOrderList()).addAndSortFromEnd2(DrawOrderElementStrateg.getInstance(Strategist.instance.cx, Strategist.instance.cy, Strategist.instance.cy));
         if (Com.cameraWorld.isOnActiveLongStay()) {
            Com.currentGameMap.initVisibleTexts(Com.cameraWorld);
         }

         this.gameCrossingsWorldData.crossWithEnemy = null;
         this.gameCrossingsWorldData.crossWithEnemyInd = -1;
         this.gameCrossingsWorldData.crossWithStrategForJoin = null;
         this.gameCrossingsWorldData.crossWithStrategFromParty = null;
         this.gameCrossingsWorldData.crossWithStrategForQuest = null;
         this.gameCrossingsWorldData.crossWithTownInd = Com.currentGameMap.crossWithTown(Strategist.instance.cx, Strategist.instance.cy, this.gameCrossingsWorldData.crossWithTownInd == -1);
         this.gameCrossingsWorldData.nextMapMoveDirect = -1;
         if (Strategist.instance.cx < Tile.width && Com.currentGameMap.moveToMapsZones[2] && (prevStrategCoordX = (int)MathGame.floorDivide((long)Strategist.instance.cy, (long)Tile.height)) >= 0 && prevStrategCoordX < Com.currentGameMap.moveToMapsByTileLeft.length && Com.currentGameMap.moveToMapsByTileLeft[prevStrategCoordX]) {
            this.gameCrossingsWorldData.nextMapMoveDirect = 2;
         } else if (Strategist.instance.cx > Com.currentGameMap.widthPx - Tile.width && Com.currentGameMap.moveToMapsZones[0] && (prevStrategCoordX = (int)MathGame.floorDivide((long)Strategist.instance.cy, (long)Tile.height)) >= 0 && prevStrategCoordX < Com.currentGameMap.moveToMapsByTileRight.length && Com.currentGameMap.moveToMapsByTileRight[prevStrategCoordX]) {
            this.gameCrossingsWorldData.nextMapMoveDirect = 0;
         } else if (Strategist.instance.cy < Tile.height && Com.currentGameMap.moveToMapsZones[3] && (prevStrategCoordX = (int)MathGame.floorDivide((long)Strategist.instance.cx, (long)Tile.width)) >= 0 && prevStrategCoordX < Com.currentGameMap.moveToMapsByTileTop.length && Com.currentGameMap.moveToMapsByTileTop[prevStrategCoordX]) {
            this.gameCrossingsWorldData.nextMapMoveDirect = 3;
         } else if (Strategist.instance.cy > Com.currentGameMap.heightPx - Tile.height && Com.currentGameMap.moveToMapsZones[1] && (prevStrategCoordX = (int)MathGame.floorDivide((long)Strategist.instance.cx, (long)Tile.width)) >= 0 && prevStrategCoordX < Com.currentGameMap.moveToMapsByTileBot.length && Com.currentGameMap.moveToMapsByTileBot[prevStrategCoordX]) {
            this.gameCrossingsWorldData.nextMapMoveDirect = 1;
         }

         this.collectVisibleMapObjects(drawOrderList2, calcWaysPlayerId, questInPlayer);
         Object controlData;
         switch (Com.touchControlType) {
            case 4:
               controlData = this.gameControllDirectData;
               break;
            default:
               controlData = this.gameControllAnalogData;
         }

         if ((calcWaysPlayerId = Com.EnsMap.iterateCloseEnemys(Com.cameraWorld, Strategist.instance, Com.currentGameMap, drawOrderList2, (EnemysMapListener)controlData)) >= 0) {
            this.gameCrossingsWorldData.crossWithEnemyInd = calcWaysPlayerId;
            this.gameCrossingsWorldData.crossWithEnemy = Com.EnsMap.Enemys[calcWaysPlayerId];
         }

         this.drawOrderList.clear();
         GameCommons.instance.functionateDraw(this.drawOrderList);
         GameSimpleObjectHighlight gameSimpleObjectHighlight;
         if ((gameSimpleObjectHighlight = createHighlightObjectForSelection(this.highlights, false, false)) != null) {
            this.drawOrderList.addAndSortFromEnd2(gameSimpleObjectHighlight);
         }

         GameControllDirectData gameControllDirectData4 = this.gameControllDirectData;
         DrawOrderList drawOrderList3 = this.drawOrderList;
         gameControllDirectData2 = gameControllDirectData4;
         if (gameControllDirectData4.selections != null) {
            gameControllDirectData2.selections.onEndFunctionate();
         }

         if (gameControllDirectData2.moveTarget != null && gameControllDirectData2.moveTarget.selectedOtherStrategId >= 0 && gameControllDirectData2.moveTarget.selectedOtherStrategObj == null) {
            gameControllDirectData2.moveTarget.selectedOtherStrategId = -1;
            gameControllDirectData2.disableControll();
         }

         GameSimpleObjectHighlight gameSimpleObjectHighlight2;
         if (gameControllDirectData2.moveTarget != null && (gameSimpleObjectHighlight2 = createHighlightObjectForSelection(gameControllDirectData2.moveTarget, true, !gameControllDirectData2.moveTargetPeacefull)) != null) {
            drawOrderList3.addAndSortFromEnd2(gameSimpleObjectHighlight2);
         }

         if (gameControllDirectData2.moveTarget != null) {
            gameControllDirectData2.moveTarget.onEndFunctionate();
         }

         GameControllAnalogData gameControllAnalogData3 = this.gameControllAnalogData;
         drawOrderList3 = this.drawOrderList;
         gameControllAnalogData2 = gameControllAnalogData3;
         if (gameControllAnalogData3.activeForAtackPlayerId >= 0 && gameControllAnalogData2.activeForAtackPlayerObj == null) {
            gameControllAnalogData2.activeForAtackPlayerObj = null;
            gameControllAnalogData2.disableAtackToPlayer();
         }

         if (gameControllAnalogData2.activeForAtackPlayerObj != null) {
            drawOrderList3.addAndSortFromEnd2(gameControllAnalogData2.activeForAtackPlayerObj.createGameSimpleObjectHighlight(1, Com.currentGameMap));
         }

         if (gameControllAnalogData2.closestEnemyForAtack >= 0) {
            GameSelectionStructure gameSelectionStructure;
            (gameSelectionStructure = new GameSelectionStructure()).selectedEnemyInd = gameControllAnalogData2.closestEnemyForAtack;
            GameSimpleObjectHighlight gameSimpleObjectHighlight3;
            if ((gameSimpleObjectHighlight3 = createHighlightObjectForSelection(gameSelectionStructure, true, false)) != null) {
               drawOrderList3.addAndSortFromEnd2(gameSimpleObjectHighlight3);
            }
         }

         this.highlights.onEndFunctionate();
         DrawOrderList drawOrderList4;
         (drawOrderList4 = new DrawOrderList()).addAndSortFromEnd2(DrawOrderElementStrateg.getInstance(Strategist.instance.cx, Strategist.instance.cy, Strategist.instance.cy));
         Com.currentGameMap.fillDecorsDrawOrder(Com.cameraWorld, this.drawOrderList, drawOrderList4);
         this.drawOrderList.mergeWith(drawOrderList2);
         drawOrderList2.clear();
         if (questInPlayer != null && this.gameCrossingsWorldData.crossWithStrategForQuest != null) {
            questInPlayer.calcWaysPlayerPosXClient = this.gameCrossingsWorldData.crossWithStrategForQuest.cx;
            questInPlayer.calcWaysPlayerPosYClient = this.gameCrossingsWorldData.crossWithStrategForQuest.cy;
         }

         setDrawQuestCompas(questInPlayer);
         if (this.gameCrossingsWorldData.crossWithEnemy != null && !Com.dontFightAndAction) {
            if (!this.gameCrossingsWorldData.crossWithEnemy.fightable) {
               this.gameCrossingsWorldData.crossWithEnemy.Enable = false;
               GameEffects instance2 = GameEffects.instance;
               GameEffects.spawnClientEffect6(this.gameCrossingsWorldData.crossWithEnemy.cx, this.gameCrossingsWorldData.crossWithEnemy.cy);
               this.gameCrossingsWorldData.crossWithEnemy = null;
            } else {
               BattleManager.createFightWithMob(this.gameCrossingsWorldData.crossWithEnemyInd);
            }
         }

         switch (Com.touchControlType) {
            case 4:
               if (!Com.dontFightAndAction) {
                  if (this.gameCrossingsWorldData.crossWithEnemy != null) {
                     this.gameControllDirectData.disableControll();
                  } else if (!MenuManager.instance.guiHandler.isActive() && !Com.mainTh.stopPaint) {
                     Enemy enemy2;
                     if (this.gameControllDirectData.moveTarget != null && this.gameControllDirectData.moveTarget.selectedEnemyInd >= 0 && isWithinPathDistance((enemy2 = Com.EnsMap.Enemys[this.gameControllDirectData.moveTarget.selectedEnemyInd]).cx, enemy2.cy)) {
                        BattleManager.createFightWithMob(this.gameControllDirectData.moveTarget.selectedEnemyInd);
                        this.gameControllDirectData.disableControll();
                     } else {
                        Town town;
                        if (this.gameCrossingsWorldData.crossWithTownInd >= 0 && ((town = Com.currentGameMap.towns[this.gameCrossingsWorldData.crossWithTownInd]).type == 407 || town.type == 408 || town.type == 401 || town.type == 404 && ((TownAvailabilityFlag)town.additional).available || town.type == 405 && ((TownAvailabilityFlag)town.additional).available) && GameMapObjectsController.instance.analisMapObject(this.gameCrossingsWorldData.crossWithTownInd, false)) {
                           this.gameControllDirectData.disableControll();
                        } else {
                           label619: {
                              if (this.gameControllDirectData.moveTarget != null) {
                                 if (this.gameControllDirectData.moveTarget.selectedMapMove >= 0) {
                                    if (this.gameCrossingsWorldData.nextMapMoveDirect == this.gameControllDirectData.moveTarget.selectedMapMove && MathGame.floorDivide((long)Strategist.instance.cx, (long)Tile.width) == (long)this.gameControllDirectData.moveTarget.selectedMapMoveX && MathGame.floorDivide((long)Strategist.instance.cy, (long)Tile.height) == (long)this.gameControllDirectData.moveTarget.selectedMapMoveY) {
                                       this.gameControllDirectData.disableControll();
                                       if (Com.tryMoveToNextMap(this.gameCrossingsWorldData.nextMapMoveDirect)) {
                                          this.waitChangeMap = true;
                                          break label619;
                                       }
                                    }
                                 } else if (this.gameControllDirectData.moveTarget.selectedTown >= 0) {
                                    Town town2 = Com.currentGameMap.towns[this.gameControllDirectData.moveTarget.selectedTown];
                                    boolean unusedProgressMarker = true;
                                    boolean unusedProgressMarker2 = true;
                                    short cy2 = Strategist.instance.cy;
                                    short cx2 = Strategist.instance.cx;
                                    town = town2;
                                    if (cx2 >= town.dx - (town.SensRad + 1) && cx2 <= town.dx + town.SensRad + 1 && cy2 >= town.dy - (town.SensRad + 1) && cy2 <= town.dy + town.SensRad + 1) {
                                       joystickDeltaY = this.gameControllDirectData.moveTarget.selectedTown;
                                       this.gameControllDirectData.disableControll();
                                       if (GameMapObjectsController.instance.analisMapObject(joystickDeltaY, false)) {
                                          break label619;
                                       }
                                    }
                                 } else if (this.gameControllDirectData.moveTarget.selectedOtherStrategId >= 0 && this.gameControllDirectData.moveTarget.selectedOtherStrategObj != null && isWithinPathDistance((short)this.gameControllDirectData.moveTarget.selectedOtherStrategObj.cx, (short)this.gameControllDirectData.moveTarget.selectedOtherStrategObj.cy)) {
                                    if (this.gameControllDirectData.moveTargetPeacefull) {
                                       BattleManager.createFightJoinToPlayer(this.gameControllDirectData.moveTarget.selectedOtherStrategObj.id);
                                       this.gameControllDirectData.disableControll();
                                    } else if (this.gameControllDirectData.moveTarget.selectedOtherStrategObj.availAtack) {
                                       BattleManager.createFightWithPlayer(this.gameControllDirectData.moveTarget.selectedOtherStrategObj.id);
                                       this.gameControllDirectData.disableControll();
                                    } else {
                                       this.gameControllDirectData.pathFolower.disableMovePath();
                                    }
                                 }
                              }

                              if (this.gameCrossingsWorldData.crossWithStrategFromParty != null && Mystery.currentTimeMillis - this.gameControllDirectData.lastCheckPartyJoinTime >= 6000L && !HeroesOnMapManager.instance.isOldHeroesTilNextLoad()) {
                                 BattleManager.createFightJoinToPlayer(this.gameCrossingsWorldData.crossWithStrategFromParty.id);
                                 this.gameControllDirectData.lastCheckPartyJoinTime = Mystery.currentTimeMillis;
                                 this.gameControllDirectData.disableControll();
                              }
                           }
                        }
                     }
                  }
               }
               break;
            default:
               if (!Com.dontFightAndAction && this.gameCrossingsWorldData.crossWithEnemy == null) {
                  label415: {
                     if (this.gameCrossingsWorldData.nextMapMoveDirect != -1) {
                        if (this.gameControllAnalogData.checkMoveToNextMap) {
                           this.gameControllAnalogData.checkMoveToNextMap = false;
                           if (Com.tryMoveToNextMap(this.gameCrossingsWorldData.nextMapMoveDirect)) {
                              this.waitChangeMap = true;
                           }
                           break label415;
                        }
                     } else {
                        this.gameControllAnalogData.checkMoveToNextMap = true;
                     }

                     if (!MenuManager.instance.guiHandler.isActive() && !Com.mainTh.stopPaint && (this.gameCrossingsWorldData.crossWithTownInd < 0 || !GameMapObjectsController.instance.analisMapObject(this.gameCrossingsWorldData.crossWithTownInd, false))) {
                        if (this.gameControllAnalogData.activeForAtackPlayerId >= 0 && this.gameControllAnalogData.activeForAtackPlayerObj != null && this.gameControllAnalogData.activeForAtackPlayerObj.availAtack && isWithinPathDistance((short)this.gameControllAnalogData.activeForAtackPlayerObj.cx, (short)this.gameControllAnalogData.activeForAtackPlayerObj.cy)) {
                           BattleManager.createFightWithPlayer(this.gameControllAnalogData.activeForAtackPlayerId);
                           this.gameControllAnalogData.disableAtackToPlayer();
                           this.gameControllAnalogData.disableControll();
                        }

                        if (this.gameCrossingsWorldData.crossWithStrategForJoin != null && Mystery.currentTimeMillis - this.gameControllAnalogData.lastActivateOtherPlayers >= 6000L) {
                           BattleManager.showBattleInfoByPlayer(this.gameCrossingsWorldData.crossWithStrategForJoin.id);
                           this.gameControllAnalogData.lastActivateOtherPlayers = Mystery.currentTimeMillis;
                        } else if (this.gameCrossingsWorldData.crossWithStrategFromParty != null && Mystery.currentTimeMillis - this.gameControllAnalogData.lastCheckPartyJoinTime >= 6000L && !HeroesOnMapManager.instance.isOldHeroesTilNextLoad()) {
                           BattleManager.createFightJoinToPlayer(this.gameCrossingsWorldData.crossWithStrategFromParty.id);
                           this.gameControllAnalogData.lastCheckPartyJoinTime = Mystery.currentTimeMillis;
                        }
                     }
                  }
               }
         }

         this.initUI();
         GameCommons instance3 = GameCommons.instance;
         GameCommons.animateCamera(Com.cameraWorld);
         if (Com.mainTh.isPFterController((PFter)this)) {
            this.controllReleasCorrectTimeWasActiveGame = true;
         } else {
            if (this.controllReleasCorrectTimeWasActiveGame) {
               switch (Com.touchControlType) {
                  case 4:
                     this.playerControllDisableAll();
                  default:
                     this.controllReleasCorrectTimeWasActiveGame = false;
               }
            }

         }
      }
   }

   private void collectVisibleMapObjects(DrawOrderList drawOrderList2, int questPlayerId, QuestInPlayer questInPlayer) {
      if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex) {
         int sizeIndex = 0;

         while(sizeIndex < HeroesOnMapManager.instance.heroes.size()) {
            HeroOnMap heroOnMap;
            (heroOnMap = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(sizeIndex)).iterate((int)Mystery.FrameTimeI);
            heroOnMap.addHighlighFlags = 0;
            if (heroOnMap.id == questPlayerId) {
               heroOnMap.addHighlighFlags = (byte)(heroOnMap.addHighlighFlags | 2);
            }

            if (heroOnMap.id == this.highlights.selectedOtherStrategId) {
               this.highlights.selectedOtherStrategObj = heroOnMap;
            }

            if (heroOnMap.partyId >= 0 && heroOnMap.partyId == Strategist.instance.partyId && Math.abs(heroOnMap.cx - Strategist.instance.cx) <= Com.c_fast_fight_party_join_for_client_pixels_x && Math.abs(heroOnMap.cy - Strategist.instance.cy) <= Com.c_fast_fight_party_join_for_client_pixels_y) {
               heroOnMap.addHighlighFlags = (byte)(heroOnMap.addHighlighFlags | 4);
               this.isHavePartyForFastFight = true;
            }

            int deltaX;
            int deltaY;
            if (heroOnMap.petH >= 0 && heroOnMap.petB >= 0) {
               if (heroOnMap.petX >= 0 && heroOnMap.petY >= 0) {
                  deltaX = heroOnMap.cx - heroOnMap.petX;

                  for(deltaY = heroOnMap.cy - heroOnMap.petY; deltaX * deltaX + deltaY * deltaY > Com.c_pet_off_base_y; deltaY = heroOnMap.cy - heroOnMap.petY) {
                     if (deltaY < 0) {
                        if (deltaX < Com.c_pet_off_base_x) {
                           --heroOnMap.petX;
                           heroOnMap.petDirect = 1;
                        }

                        if (deltaX > Com.c_pet_off_base_x) {
                           ++heroOnMap.petX;
                           heroOnMap.petDirect = 0;
                        }

                        --heroOnMap.petY;
                     } else {
                        if (deltaX < 0) {
                           --heroOnMap.petX;
                           heroOnMap.petDirect = 1;
                        }

                        if (deltaX > 0) {
                           ++heroOnMap.petX;
                           heroOnMap.petDirect = 0;
                        }

                        if (deltaY > 0) {
                           ++heroOnMap.petY;
                        }
                     }

                     deltaX = heroOnMap.cx - heroOnMap.petX;
                  }
               } else {
                  heroOnMap.petX = heroOnMap.cx + Com.c_pet_off_x_up;
                  heroOnMap.petY = heroOnMap.cy + Com.c_master_avail_max_lvl;
                  heroOnMap.petDirect = Mystery.RandInt(2) == 0 ? 1 : 0;
               }
            }

            deltaX = (int)MathGame.floorDivide((long)heroOnMap.cx, (long)Tile.width);
            deltaY = (int)MathGame.floorDivide((long)heroOnMap.cy, (long)Tile.height);
            if (!Com.currentGameMap.getBlockTl(deltaX, deltaY)) {
               DrawOrderElementHitBullt drawOrderElementHitBullt;
               label142: {
                  DrawOrderElementOtherStrateg drawOrderElementOtherStrateg = DrawOrderElementOtherStrateg.getInstance((short)heroOnMap.cx, (short)heroOnMap.cy, (short)heroOnMap.cy, sizeIndex);
                  drawOrderList2.addAndSortFromEnd2(drawOrderElementOtherStrateg);
                  HeroOnMap heroOnMap2;
                  if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex && (heroOnMap2 = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(drawOrderElementOtherStrateg.ind)) != null && heroOnMap2.swords != 0 && heroOnMap2.fightingWithMobInd >= 0 && heroOnMap2.fightingWithMobInd < Com.EnsMap.Enemys.length) {
                     Enemy enemy2 = Com.EnsMap.Enemys[heroOnMap2.fightingWithMobInd];
                     int attackAnimationTime = (int)((Mystery.currentTimeMillis - heroOnMap2.visibleSinceTime) % (long)(Battle.c_all_apply_atacks_time << 2));
                     if (enemy2.Enable) {
                        drawOrderElementHitBullt = DrawOrderElementHitBullt.getInstance(drawOrderElementOtherStrateg.rx, drawOrderElementOtherStrateg.ry, (short)32767, drawOrderElementOtherStrateg.ind, heroOnMap2.fightingWithMobInd, attackAnimationTime);
                        break label142;
                     }
                  }

                  drawOrderElementHitBullt = null;
               }

               DrawOrderElementHitBullt drawOrderElementHitBullt2 = drawOrderElementHitBullt;
               if (drawOrderElementHitBullt != null) {
                  drawOrderList2.addAndSortFromEnd2(drawOrderElementHitBullt2);
               }
            }

            switch (heroOnMap.swords) {
               case 1:
                  if (this.gameCrossingsWorldData.crossWithStrategForJoin == null && Math.abs(heroOnMap.cx - Strategist.instance.cx) <= 24 && Math.abs(heroOnMap.cy - Strategist.instance.cy) <= 24) {
                     this.gameCrossingsWorldData.crossWithStrategForJoin = heroOnMap;
                  }

                  if (this.gameCrossingsWorldData.crossWithStrategFromParty == null && Strategist.instance.partyState != 2 && heroOnMap.partyId == Strategist.instance.partyId && Math.abs(heroOnMap.cx - Strategist.instance.cx) <= Com.c_party_fight_tiles * Tile.width && Math.abs(heroOnMap.cy - Strategist.instance.cy) <= Com.c_party_fight_tiles * Tile.height) {
                     this.gameCrossingsWorldData.crossWithStrategFromParty = heroOnMap;
                  }
               default:
                  if (heroOnMap.id == questPlayerId) {
                     this.gameCrossingsWorldData.crossWithStrategForQuest = heroOnMap;
                  }

                  if (heroOnMap.id == questPlayerId && questInPlayer != null && Com.cameraWorld.isPointVisible(heroOnMap.cx, heroOnMap.cy)) {
                     questInPlayer.calcWaysPlayerPosXClient = heroOnMap.cx;
                     questInPlayer.calcWaysPlayerPosYClient = heroOnMap.cy;
                     questInPlayer.calcWaysPlayerVisibleClient = true;
                  }

                  this.gameControllDirectData.processStrategOnMap(heroOnMap);
                  GameControllAnalogData gameControllAnalogData2;
                  if ((gameControllAnalogData2 = this.gameControllAnalogData).activeForAtackPlayerId >= 0 && heroOnMap.id == gameControllAnalogData2.activeForAtackPlayerId) {
                     gameControllAnalogData2.activeForAtackPlayerObj = heroOnMap;
                  }

                  ++sizeIndex;
            }
         }

      }
   }

   private static boolean isWithinPathDistance(short targetX, short targetY) {
      int deltaX = Strategist.instance.cx - targetX;
      int deltaY = Strategist.instance.cy - targetY;
      return deltaX * deltaX + deltaY * deltaY <= Strategist.instance.maxIceLvl * Strategist.instance.maxIceLvl && (targetX = (short)Com.currentGameMap.getPathLengthInPixelsWithTilesPrecision(Strategist.instance.cx, Strategist.instance.cy, (short)targetX, targetY, true)) >= 0 && targetX - Strategist.instance.maxIceLvl <= Tile.tileDiagonalLength;
   }

   private void initUI() {
      String[][] partyInfo;
      synchronized(Strategist.instance.partyInfoLock) {
         partyInfo = Strategist.instance.partyInfo;
      }

      boolean curVisSetn2 = this.curVisSetn;
      this.curVisSetn = true;
      boolean curVisGold2 = this.curVisGold;
      boolean curVisCrys2 = this.curVisCrys;
      boolean curVisVip2 = this.curVisVip;
      boolean curVisProtection2 = this.curVisProtection;
      boolean curVisMiniMap2 = this.curVisMiniMap;
      if (!curVisSetn2) {
         curVisGold2 = this.uiMountGold.enabled;
         curVisCrys2 = this.uiMountCrys.enabled;
         curVisVip2 = this.uiMountVip.enabled;
         curVisProtection2 = this.uiMountProtection.enabled;
         curVisMiniMap2 = this.uiMountMiniMap.enabled;
      }

      switch (Com.touchControlType) {
         case 4:
            if (this.uIComponent12 != null) {
               this.uIComponent12.enabled = true;
            }

            if (this.uIComponent13 != null) {
               this.uIComponent13.enabled = false;
            }

            if (this.uIComponent14 != null) {
               this.uIComponent14.enabled = false;
            }
            break;
         default:
            if (this.gameControllAnalogData.closestEnemyForAtack < 0) {
               if (this.uIComponent12 != null) {
                  this.uIComponent12.enabled = true;
               }

               if (this.uIComponent13 != null) {
                  this.uIComponent13.enabled = false;
               }

               if (this.uIComponent14 != null) {
                  this.uIComponent14.enabled = false;
               }
            } else {
               if (this.uIComponent12 != null) {
                  this.uIComponent12.enabled = false;
               }

               if (this.uIComponent13 != null) {
                  this.uIComponent13.enabled = true;
               }

               if (this.uIComponent14 != null) {
                  this.uIComponent14.enabled = true;
               }
            }
      }

      UIComponent uIComponent15;
      boolean enabled;
      label231: {
         this.uiMountName.enabled = true;
         this.uIComponent.enabled = true;
         this.curVisGold = (Com.cameraWorld.isActiveLongStay() || Strategist.instance.guiMode == 2) && Strategist.instance.guiMode != 0;
         this.curVisCrys = (Com.cameraWorld.isActiveLongStay() || Strategist.instance.guiMode == 2) && Strategist.instance.guiMode != 0;
         this.curVisVip = Strategist.instance.vipStatus >= 0 && (Com.cameraWorld.isActiveLongStay() || Strategist.instance.guiMode == 2) && Strategist.instance.guiMode != 0;
         this.curVisProtection = Strategist.instance.protectionStatus && (Com.cameraWorld.isActiveLongStay() || Strategist.instance.guiMode == 2) && Strategist.instance.guiMode != 0;
         this.uIComponent2.enabled = true;
         this.uIComponent3.enabled = true;
         this.uIComponent4.enabled = true;
         this.uIComponent5.enabled = true;
         this.uIComponent6.enabled = true;
         this.uIComponent7.enabled = true;
         this.uIComponent9.enabled = partyInfo.length > 0;
         this.uIComponent10.enabled = Strategist.instance.trades.size() > 0;
         this.uIComponent11.enabled = BattleCallsHero.instance.getCallsCount() > 0;
         uIComponent15 = this.uIComponent8;
         if ((Strategist.instance.compassMode & 1) != 0) {
            QuestStrateg instance2 = QuestStrateg.instance;
            if (QuestStrateg.hasTrackedQuestInfo()) {
               enabled = true;
               break label231;
            }
         }

         enabled = false;
      }

      uIComponent15.enabled = enabled;
      this.curVisMiniMap = (Com.cameraWorld.isActiveLongStay() || Strategist.instance.guiMode == 2 || this.dragOverMinimap) && Strategist.instance.guiDrawMiniMap && Strategist.instance.guiMode != 0;
      this.uiMountLifeBar.enabled = true;
      this.latencyVal = -1L;
      this.uIComponent2.overBackGroundColor = -1;
      if (Com.currentGameMap.home) {
         this.uIComponent2.overBackGroundColor = 684039;
      } else if (!Com.currentGameMap.peacefull) {
         if (Com.currentGameMap.battleReject) {
            this.uIComponent2.overBackGroundColor = 7960953;
         } else {
            this.uIComponent2.overBackGroundColor = 9765121;
         }
      }

      int textWidth = Com.fontClan.getTextWidth(Strategist.instance.clanIcon);
      int instance = Com.fontBig.getTextWidth(Strategist.instance.GP_Name);
      this.uiOverlay.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 4, 0, 0, Com.cameraWorld.getCw(), 0, Com.cameraWorld.getCh(), true);
      switch (Com.touchControlType) {
         case 2:
         case 3:
            this.uiJoystickZone.enabled = !MenuManager.instance.guiHandler.isActive();
            this.uiJoystickZone.setPosition(this.uiScreenWidth, this.uiScreenHeight, Com.touchControlType == 2 ? 0 : 2, ImagesGlobal.joystickStick.getWidth() / 2, 5, GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 4 + ImagesGlobal.joystickStick.getHeight() / 2, 0, (this.joystickRadius << 1) + 2, 0, (this.joystickRadius << 1) + 2, true);
            this.joystickCenterX = this.uiJoystickZone.widthContentByPosition / 2;
            this.joystickCenterY = this.uiJoystickZone.heightContentByPosition / 2;
            break;
         default:
            this.uiJoystickZone.enabled = false;
      }

      UIComponent uIComponent16 = this.uiMountName.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, 4, 0, textWidth + instance + 2, 0, Com.fontBig.fontHeight, true);
      uIComponent16 = this.uIComponent.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.fontDigitGame.getTextWidth(getLevelProgressText()) + 2, 0, Com.fontDigitGame.fontHeight, true);
      uIComponent16 = this.uiMountGold.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.fontDigitGame.getTextWidth(getGoldCountText()) + Com.fontBig.getTextWidth("ɼɽ") + 2, 0, Com.fontDigitGame.fontHeight, true);
      uIComponent16 = this.uiMountCrys.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.fontDigitGame.getTextWidth(getCrystalCountText()) + Com.fontBig.getTextWidth("ɾɿ") + 2, 0, Com.fontDigitGame.fontHeight, true);
      if (this.uiMountVip.enabled || this.curVisVip) {
         uIComponent16 = this.uiMountVip.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.fontDigitGame.getTextWidth(getVipTimeText()) + Com.fontBig.getTextWidth("˦˧") + 2, 0, Com.fontDigitGame.fontHeight, true);
      }

      if (this.uiMountProtection.enabled || this.curVisProtection) {
         uIComponent16 = this.uiMountProtection.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.fontDigitGame.getTextWidth(getProtectionTimeText()) + Com.fontBig.getTextWidth("̎̏") + 2, 0, Com.fontDigitGame.fontHeight, true);
      }

      if (this.uIComponent4.enabled) {
         uIComponent16 = this.uIComponent4.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 5, GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 25, 0, 35, 0, 25, true);
      }

      if (this.uIComponent5.enabled) {
         uIComponent16 = this.uIComponent5.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 5, this.uiScreenHeight - uIComponent16.y, 0, 35, 0, 25, true);
      }

      if (this.uIComponent6.enabled) {
         uIComponent16 = this.uIComponent6.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 5, this.uiScreenHeight - uIComponent16.y, 0, 35, 0, 25, true);
      }

      if (this.uIComponent7.enabled) {
         this.uIComponent7.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 5, this.uiScreenHeight - uIComponent16.y, 0, 35, 0, 25, true);
      }

      uIComponent16 = this.uIComponent2.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, 4, 0, Com.fontBig.getTextWidth(getMapNameText()) + 2, 0, Com.fontBig.fontHeight, true);
      instance = Com.fontDigitGame.getTextWidth(Com.getNearbyPlayerCountText());
      int fontBig = Com.fontDigitGame.getTextWidth(this.getMountLatencyText());
      int partyInfoIndex = Com.fontBig.getTextWidth(this.getMountLatencyAddText());
      uIComponent16 = this.uIComponent3.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, instance + fontBig + 2 + 2 + ImagesGlobal.playersIcon.getWidth() + partyInfoIndex, 0, Com.fontDigitGame.fontHeight, true);
      if (this.uiMountMiniMap.enabled || this.curVisMiniMap) {
         uIComponent16 = this.uiMountMiniMap.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Com.currentGameMap.widthTl << 1, 0, Com.currentGameMap.heightTl << 1, true);
      }

      if (this.uIComponent8.enabled) {
         uIComponent16 = this.uIComponent8.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, uIComponent16.y + uIComponent16.height + 4, 0, Math.min(QuestStrateg.instance.compassQuestInfoMaxWidth, 8 * Com.fontSmall.fontWidth) + 4, 0, Mystery.getSpacedLinesHeight((QuestStrateg.instance.getCompassedQuestTimerInfo() >= 0 ? 1 : 0) + 1 + (QuestStrateg.instance.compassQuestInfoTextsWidth == null ? 0 : QuestStrateg.instance.compassQuestInfoTextsWidth.length), Com.fontSmall.fontHeight, 1), true);
      }

      this.uiYEndRight = uIComponent16.yByPosition + uIComponent16.paddingBottom + uIComponent16.paddingTop + uIComponent16.heightContentByPosition;
      uIComponent16 = this.uiMountLifeBar.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 5, GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 4, 0, 125, 0, 8, true);
      if (this.uIComponent9.enabled) {
         instance = partyInfo.length * 25;
         fontBig = 0;

         for(partyInfoIndex = 0; partyInfoIndex < partyInfo.length; ++partyInfoIndex) {
            fontBig = Math.max(fontBig, Com.fontBig.getTextWidth(partyInfo[partyInfoIndex][0]));
         }

         this.uIComponent9.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 1, 5, GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 25, 0, fontBig, 0, instance, true);
      }

      int textWidth2;
      if (this.uIComponent11.enabled) {
         fontBig = (instance = Math.min(3, BattleCallsHero.instance.getCallsCount())) * (Com.fontBig.fontHeight + 1);
         partyInfoIndex = 0;
         Vector vector3 = BattleCallsHero.instance.getCalls();

         for(int uiMountDuelsTextsWidthIndex = 0; uiMountDuelsTextsWidthIndex < instance; ++uiMountDuelsTextsWidthIndex) {
            BattleCall battleCall = (BattleCall)vector3.elementAt(uiMountDuelsTextsWidthIndex);
            textWidth2 = Com.fontBig.getTextWidth(battleCall.getTitleForFastInfo());
            partyInfoIndex = Math.max(partyInfoIndex, textWidth2);
            this.uiMountDuelsTextsWidths[uiMountDuelsTextsWidthIndex] = textWidth2;
         }

         uIComponent16 = this.uIComponent11.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 10, uIComponent16.yByPosition, 0, partyInfoIndex, 0, fontBig, true);
      }

      if (this.uIComponent10.enabled) {
         fontBig = (instance = Math.min(3, Strategist.instance.trades.size())) * (Com.fontBig.fontHeight + 1);
         partyInfoIndex = 0;

         for(int uiMountTradeTextsWidthIndex = 0; uiMountTradeTextsWidthIndex < instance; ++uiMountTradeTextsWidthIndex) {
            TradeSuggestion tradeSuggestion3 = ((PlayersTrade)Strategist.instance.trades.elementAt(uiMountTradeTextsWidthIndex)).findInvertSuggestion(Strategist.instance.id);
            textWidth2 = Com.fontBig.getTextWidth(tradeSuggestion3.getTitleForFastInfo());
            partyInfoIndex = Math.max(partyInfoIndex, textWidth2);
            this.uiMountTradeTextsWidths[uiMountTradeTextsWidthIndex] = textWidth2;
         }

         this.uIComponent10.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 10, uIComponent16.yByPosition, 0, partyInfoIndex, 0, fontBig, true);
      }

      animateHudVisibility(this.uiMountGold, this.curVisGold, curVisGold2);
      animateHudVisibility(this.uiMountCrys, this.curVisCrys, curVisCrys2);
      animateHudVisibility(this.uiMountVip, this.curVisVip, curVisVip2);
      animateHudVisibility(this.uiMountProtection, this.curVisProtection, curVisProtection2);
      this.tryAnimateMountRight(this.uiMountMiniMap, this.curVisMiniMap, curVisMiniMap2);
   }

   private static void animateHudVisibility(UIComponent uIComponent15, boolean curVis, boolean prevVis) {
      if (GameCommons.instance.guiAnimate) {
         if (curVis != prevVis) {
            uIComponent15.enabled = true;
            if (curVis) {
               uIComponent15.startAnimationX(-uIComponent15.width, uIComponent15.xByPosition, 140L);
               return;
            }

            uIComponent15.startAnimationX(uIComponent15.xByPosition, -uIComponent15.width, 140L);
            return;
         }
      } else {
         uIComponent15.enabled = curVis;
      }

   }

   private void tryAnimateMountRight(UIComponent uIComponent15, boolean curVis, boolean prevVis) {
      if (GameCommons.instance.guiAnimate) {
         if (curVis != prevVis) {
            uIComponent15.enabled = true;
            if (curVis) {
               uIComponent15.startAnimationX(this.uiScreenWidth, uIComponent15.xByPosition, 140L);
               return;
            }

            uIComponent15.startAnimationX(uIComponent15.xByPosition, this.uiScreenWidth, 140L);
            return;
         }
      } else {
         uIComponent15.enabled = curVis;
      }

   }

   private static String getLevelProgressText() {
      long instance = (long)Strategist.instance.GP_Exp * 10000L / (long)Strategist.instance.GP_NextLevelExp;
      return Strategist.instance.GP_Level + "ур " + Long.toString(instance / 100L) + "." + Mystery.fillString(String.valueOf(instance % 100L), '0', 2) + "%";
   }

   private static String getCrystalCountText() {
      return String.valueOf(Strategist.instance.GP_Crystals);
   }

   private static String getGoldCountText() {
      return String.valueOf(Strategist.instance.GP_Money);
   }

   private static String getVipTimeText() {
      long instance;
      int instance2 = (int)((instance = Strategist.instance.vipStatusEndTime / 1000L) % 60L);
      int minutes = (int)(instance /= 60L);
      return (minutes < 10 ? "0" + minutes : "" + minutes) + ":" + (instance2 < 10 ? "0" + instance2 : "" + instance2);
   }

   private static String getProtectionTimeText() {
      long instance;
      int instance2 = (int)((instance = Strategist.instance.protectionStatusEndTime / 1000L) % 60L);
      int minutes = (int)(instance /= 60L);
      return (minutes < 10 ? "0" + minutes : "" + minutes) + ":" + (instance2 < 10 ? "0" + instance2 : "" + instance2);
   }

   private static String getMapNameText() {
      return MystFont.substringSpecialString(16, Com.currentGameMap.nameLiteral, "ə");
   }

   private String getMountLatencyText() {
      if (this.latencyVal < 0L) {
         this.latencyVal = Mystery.currentTimeMillis - Com.lagTimeLast;
      }

      return this.latencyVal >= 8000L ? String.valueOf(this.latencyVal) : String.valueOf(Com.lagShowTime);
   }

   private String getMountLatencyAddText() {
      if (this.latencyVal < 0L) {
         this.latencyVal = Mystery.currentTimeMillis - Com.lagTimeLast;
      }

      return this.latencyVal >= 8000L ? "ʂʃ" : "";
   }

   private static void draw(GameGraphics gameGraphics2, UIComponent uIComponent15, String messageText) {
      GameCommons.paintSymbolOnMount(gameGraphics2, uIComponent15, messageText, 1, false);
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 10003:
            draw(gameGraphics2, this.uiMountGold, "ɼɽ");
            return;
         case 10004:
            draw(gameGraphics2, this.uiMountCrys, "ɾɿ");
            return;
         case 10005:
            draw(gameGraphics2, this.uiMountVip, "˦˧");
            return;
         case 10006:
            draw(gameGraphics2, this.uiMountProtection, "̎̏");
            return;
         case 10020:
            QuestStrateg instance2 = QuestStrateg.instance;
            if (QuestStrateg.hasTrackedQuestInfo()) {
               if (uIComponent15.state == 1) {
                  gameGraphics2.setColor(4210752);
                  gameGraphics2.fillRect(uIComponent15.xContent + uIComponent15.widthContent - 4 - QuestStrateg.instance.compassQuestInfoMaxWidth, uIComponent15.yContent, QuestStrateg.instance.compassQuestInfoMaxWidth + 4, uIComponent15.heightContent);
               }

               Com.fontSmall.writeText(gameGraphics2, QuestStrateg.instance.compassQuestInfoName, uIComponent15.xContent + uIComponent15.widthContent - 4 - QuestStrateg.instance.compassQuestInfoNameWidth, 0 + uIComponent15.yContent, StringResources.FONT_ACCORDINGS);
               int fontSmall = 0 + Com.fontSmall.fontHeight + 1;
               int compassedQuestTimerInfo;
               if ((compassedQuestTimerInfo = QuestStrateg.instance.getCompassedQuestTimerInfo()) >= 0) {
                  String messageText = "͝" + compassedQuestTimerInfo;
                  Com.fontSmall.writeText(gameGraphics2, messageText, uIComponent15.xContent + uIComponent15.widthContent - 4 - Com.fontSmall.getTextWidth(messageText), fontSmall + uIComponent15.yContent, StringResources.FONT_ACCORDINGS);
                  fontSmall += Com.fontSmall.fontHeight + 1;
               }

               for(compassedQuestTimerInfo = 0; compassedQuestTimerInfo < QuestStrateg.instance.compassQuestInfoText.length; ++compassedQuestTimerInfo) {
                  Com.fontSmall.writeText(gameGraphics2, QuestStrateg.instance.compassQuestInfoText[compassedQuestTimerInfo], uIComponent15.xContent + uIComponent15.widthContent - 4 - QuestStrateg.instance.compassQuestInfoTextsWidth[compassedQuestTimerInfo], fontSmall + uIComponent15.yContent, StringResources.FONT_ACCORDINGS);
                  fontSmall += Com.fontSmall.fontHeight + 1;
               }
            }
         default:
      }
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      int cellIndex;
      int uiMountTradeTextsWidthIndex;
      int partyInfoIndex;
      int textWidth;
      GameImage gameImage;
      switch (uIComponent15.id) {
         case 10001:
            cellIndex = Com.fontClan.getTextWidth(Strategist.instance.clanIcon);
            Com.fontClan.writeText(gameGraphics2, Strategist.instance.clanIcon, 1, (uIComponent15.heightContent - Com.fontClan.fontHeight) / 2);
            Com.fontBig.writeText(gameGraphics2, Strategist.instance.GP_Name, cellIndex + 1, 0);
            return;
         case 10002:
            Com.fontDigitGame.writeText(gameGraphics2, getLevelProgressText(), 1, 0);
            return;
         case 10003:
            Com.fontDigitGame.writeText(gameGraphics2, getGoldCountText(), 1, 0);
            return;
         case 10004:
            Com.fontDigitGame.writeText(gameGraphics2, getCrystalCountText(), 1, 0);
            return;
         case 10005:
            Com.fontDigitGame.writeText(gameGraphics2, getVipTimeText(), 1, 0);
            return;
         case 10006:
            Com.fontDigitGame.writeText(gameGraphics2, getProtectionTimeText(), 1, 0);
            return;
         case 10007:
            Com.fontBig.writeText(gameGraphics2, getMapNameText(), 1, 0);
            return;
         case 10008:
            String s = Com.getNearbyPlayerCountText();
            textWidth = Com.fontDigitGame.getTextWidth(s);
            Com.fontDigitGame.writeText(gameGraphics2, s, 1, 0);
            gameGraphics2.drawImage(ImagesGlobal.playersIcon, textWidth + 1 + 1, (uIComponent15.heightContent - ImagesGlobal.playersIcon.getHeight()) / 2, 0);
            textWidth = textWidth + 1 + 1 + ImagesGlobal.playersIcon.getWidth() + 1;
            if (this.latencyVal < 0L) {
               this.latencyVal = Mystery.currentTimeMillis - Com.lagTimeLast;
            }

            String s2;
            if (this.latencyVal < 8000L) {
               s2 = String.valueOf(Com.lagShowTime);
               Com.fontDigitGame.writeText(gameGraphics2, s2, textWidth + 1, 0);
               return;
            }

            s2 = String.valueOf(this.latencyVal);
            Com.fontDigitGameRed.writeText(gameGraphics2, s2, textWidth + 1, 0);
            break;
         case 10009:
         case 10017:
         case 10018:
         case 10020:
         default:
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, "Меню", "Напасть", "Меню", Com.fontBig, GameCommons.instance.guiSoftSpacing);
            break;
         case 10010:
            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            if (this.post1State <= 0) {
               gameGraphics2.drawImage(ImagesGlobal.iconChatDisable, (uIComponent15.width - ImagesGlobal.iconChatDisable.getWidth()) / 2, (uIComponent15.height - ImagesGlobal.iconChatDisable.getHeight()) / 2, 0);
               return;
            }

            if (this.post1BlinkTime <= 0 || this.post1BlinkTime % 800 >= 300) {
               gameImage = this.post1State == 2 ? ImagesGlobal.iconChatCommonSelect : ImagesGlobal.iconChatCommon;
               gameGraphics2.drawImage(gameImage, (uIComponent15.width - gameImage.getWidth()) / 2, (uIComponent15.height - gameImage.getHeight()) / 2, 0);
               return;
            }
            break;
         case 10011:
            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            if (this.post2State <= 0) {
               gameGraphics2.drawImage(ImagesGlobal.iconChatDisable, (uIComponent15.width - ImagesGlobal.iconChatDisable.getWidth()) / 2, (uIComponent15.height - ImagesGlobal.iconChatDisable.getHeight()) / 2, 0);
               return;
            }

            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            if (this.post2BlinkTime <= 0 || this.post2BlinkTime % 800 >= 300) {
               gameImage = this.post2State == 2 ? ImagesGlobal.iconChatPrivateSelect : ImagesGlobal.iconChatPrivate;
               gameGraphics2.drawImage(gameImage, (uIComponent15.width - gameImage.getWidth()) / 2, (uIComponent15.height - gameImage.getHeight()) / 2, 0);
               return;
            }
            break;
         case 10012:
            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            if (this.post3State <= 0) {
               gameGraphics2.drawImage(ImagesGlobal.iconChatDisable, (uIComponent15.width - ImagesGlobal.iconChatDisable.getWidth()) / 2, (uIComponent15.height - ImagesGlobal.iconChatDisable.getHeight()) / 2, 0);
               return;
            }

            if (this.post3BlinkTime <= 0 || this.post3BlinkTime % 800 >= 300) {
               gameImage = this.post3State == 2 ? ImagesGlobal.iconChatClanSelect : ImagesGlobal.iconChatClan;
               gameGraphics2.drawImage(gameImage, (uIComponent15.width - gameImage.getWidth()) / 2, (uIComponent15.height - gameImage.getHeight()) / 2, 0);
               return;
            }
            break;
         case 10013:
            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            if (this.post4State <= 0) {
               gameGraphics2.drawImage(ImagesGlobal.iconChatDisable, (uIComponent15.width - ImagesGlobal.iconChatDisable.getWidth()) / 2, (uIComponent15.height - ImagesGlobal.iconChatDisable.getHeight()) / 2, 0);
               return;
            }

            if (this.post4BlinkTime > 0 && this.post4BlinkTime % 800 < 300) {
               break;
            }

            gameImage = this.post4State == 2 ? ImagesGlobal.iconChatPartySelect : ImagesGlobal.iconChatParty;
            gameGraphics2.drawImage(gameImage, (uIComponent15.width - gameImage.getWidth()) / 2, (uIComponent15.height - gameImage.getHeight()) / 2, 0);
            return;
         case 10014:
            String[][] partyInfo;
            synchronized(Strategist.instance.partyInfoLock) {
               partyInfo = Strategist.instance.partyInfo;
            }

            textWidth = 0;

            for(partyInfoIndex = 0; partyInfoIndex < partyInfo.length; ++partyInfoIndex) {
               uiMountTradeTextsWidthIndex = textWidth + 12 - Com.fontBig.fontHeight / 2;
               Com.fontBig.writeText(gameGraphics2, partyInfo[partyInfoIndex][0], 0, uiMountTradeTextsWidthIndex);
               Com.drawLifeBar(gameGraphics2, 2, uiMountTradeTextsWidthIndex + Com.fontBig.fontHeight + 1, 40, 2, (long)Integer.parseInt(partyInfo[partyInfoIndex][1]), (long)Integer.parseInt(partyInfo[partyInfoIndex][2]), true);
               textWidth += 25;
            }

            return;
         case 10015:
            TileMap tileMap;
            int miniMapColor;
            if ((tileMap = Com.currentGameMap).miniMapBack == null && tileMap.miniMapBackSize != null) {
               tileMap.miniMapBack = GameImage.createImage(tileMap.miniMapBackSize[0], tileMap.miniMapBackSize[1]);
               GameGraphics gameGraphics3 = tileMap.miniMapBack.getGraphics();
               GameGraphics gameGraphics4 = gameGraphics3;
               TileMap tileMap2 = tileMap;

               for(cellIndex = 0; cellIndex < tileMap2.widthTl; ++cellIndex) {
                  for(uiMountTradeTextsWidthIndex = 0; uiMountTradeTextsWidthIndex < tileMap2.heightTl; ++uiMountTradeTextsWidthIndex) {
                     TileMapElement[] decorsDraw2 = tileMap2.cells[cellIndex][uiMountTradeTextsWidthIndex].decorsDraw;
                     miniMapColor = Com.Tiles[tileMap2.cells[cellIndex][uiMountTradeTextsWidthIndex].tileInd].color;
                     int decorsDraw2Index = 0;

                     for(int length = decorsDraw2.length; decorsDraw2Index < length; ++decorsDraw2Index) {
                        TileMapElement tileMapElement;
                        if ((tileMapElement = decorsDraw2[decorsDraw2Index]).sprite >= 0 && Com.Decors[tileMapElement.sprite].color >= 0) {
                           miniMapColor = Com.Decors[tileMapElement.sprite].color;
                        }
                     }

                     gameGraphics4.setColor(miniMapColor);
                     gameGraphics4.fillRect(cellIndex << 1, uiMountTradeTextsWidthIndex << 1, 2, 2);
                  }
               }
            }

            gameGraphics2.drawImage(Com.currentGameMap.miniMapBack, 0, 0, 0);

            int enemyIndex;
            for(enemyIndex = 0; enemyIndex < Com.EnsMap.Enemys.length; ++enemyIndex) {
               Enemy enemy2;
               if ((enemy2 = Com.EnsMap.Enemys[enemyIndex]).Enable) {
                  partyInfoIndex = enemy2.cx / Tile.width << 1;
                  miniMapColor = enemy2.cy / Tile.height << 1;
                  if (enemy2.level == 0) {
                     gameGraphics2.setColor(2651681);
                  } else {
                     int enemyColor = 11865875;
                     int enemyLevelDifference;
                     if ((enemyLevelDifference = enemy2.level - Strategist.instance.GP_Level) >= 0 && enemyLevelDifference >= Com.c_enemy_levels_dif_strong) {
                        enemyColor = 9842175;
                     }

                     if (enemyLevelDifference < 0 && -enemyLevelDifference >= Com.c_enemy_levels_dif_weak) {
                        enemyColor = 14660356;
                     }

                     gameGraphics2.setColor(enemyColor);
                  }

                  gameGraphics2.fillRect(partyInfoIndex, miniMapColor, 2, 2);
               }
            }

            gameGraphics2.setColor(35524);
            if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex) {
               for(enemyIndex = 0; enemyIndex < HeroesOnMapManager.instance.heroes.size(); ++enemyIndex) {
                  HeroOnMap heroOnMap;
                  if ((heroOnMap = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(enemyIndex)).cx >= 0) {
                     gameGraphics2.fillRect(heroOnMap.cx / Tile.width << 1, heroOnMap.cy / Tile.height << 1, 2, 2);
                  }
               }
            }

            partyInfoIndex = Strategist.instance.cx / Tile.width << 1;
            miniMapColor = Strategist.instance.cy / Tile.height << 1;
            gameGraphics2.setColor(16772352);
            gameGraphics2.fillRect(partyInfoIndex - 2, miniMapColor - 2, 5, 5);
            gameGraphics2.setColor(16711680);
            gameGraphics2.drawLine(partyInfoIndex, miniMapColor - 2, partyInfoIndex, miniMapColor + 2);
            gameGraphics2.drawLine(partyInfoIndex - 2, miniMapColor, partyInfoIndex + 2, miniMapColor);
            long iconAnimationTime = Mystery.currentTimeMillis % 2700L;
            byte levelArrowDirection = 0;
            if ((iconAnimationTime -= 1900L) < 0L) {
               levelArrowDirection = 0;
            } else if ((iconAnimationTime -= 100L) < 0L) {
               levelArrowDirection = -1;
            } else if ((iconAnimationTime -= 200L) < 0L) {
               levelArrowDirection = -2;
            } else if ((iconAnimationTime -= 100L) < 0L) {
               levelArrowDirection = -1;
            } else if ((iconAnimationTime -= 200L) < 0L) {
               levelArrowDirection = 0;
            } else if (iconAnimationTime - 200L < 0L) {
               levelArrowDirection = -1;
            }

            for(cellIndex = 0; cellIndex < Com.currentGameMap.questers.length; ++cellIndex) {
               QuestsOnMap questsOnMap;
               if ((questsOnMap = Com.currentGameMap.questers[cellIndex]).drawSignType == 4 || questsOnMap.drawSignType == 2) {
                  Town town;
                  partyInfoIndex = ((town = Com.currentGameMap.towns[questsOnMap.hisTownInd]).cx << 1) + 1;
                  uiMountTradeTextsWidthIndex = (town.cy << 1) + 1;
                  gameGraphics2.drawImage(questsOnMap.drawSignType == 4 ? ImagesGlobal.questMinimapHave : ImagesGlobal.questMinimapDone, partyInfoIndex, uiMountTradeTextsWidthIndex + (questsOnMap.drawSignType == 4 ? levelArrowDirection : 0), 3);
               }
            }

            return;
         case 10016:
            Com.drawLifeBar(gameGraphics2, 0, 0, 125, 8, (long)Strategist.instance.GP_Life, (long)Strategist.instance.avatarLife);
            return;
         case 10019:
            draw(gameGraphics2, this.joystickCenterX, this.joystickCenterY, this.joystickRadius, this.joystickActive ? this.joystickStickX : this.joystickCenterX, this.joystickActive ? this.joystickStickY : this.joystickCenterY);
            return;
         case 10021:
            try {
               cellIndex = Math.min(3, Strategist.instance.trades.size());
               textWidth = 0;

               for(uiMountTradeTextsWidthIndex = 0; uiMountTradeTextsWidthIndex < cellIndex; ++uiMountTradeTextsWidthIndex) {
                  PlayersTrade playersTrade;
                  if ((playersTrade = (PlayersTrade)Strategist.instance.trades.elementAt(uiMountTradeTextsWidthIndex)).tradeSuggestion.isAccept || playersTrade.tradeSuggestion2.isAccept) {
                     gameGraphics2.setColor(2651681);
                     gameGraphics2.fillRect(0, textWidth, uIComponent15.widthContent, Com.fontBig.fontHeight);
                  }

                  TradeSuggestion tradeSuggestion3 = playersTrade.findInvertSuggestion(Strategist.instance.id);
                  Com.fontBig.writeText(gameGraphics2, tradeSuggestion3.getTitleForFastInfo(), (uIComponent15.widthContent - this.uiMountTradeTextsWidths[uiMountTradeTextsWidthIndex]) / 2, textWidth);
                  textWidth += Com.fontBig.fontHeight + 1;
               }

               return;
            } catch (Exception exception) {
               exception.printStackTrace();
               return;
            }
         case 10022:
            try {
               cellIndex = Math.min(3, BattleCallsHero.instance.getCallsCount());
               Vector vector3 = BattleCallsHero.instance.getCalls();
               uiMountTradeTextsWidthIndex = 0;

               for(partyInfoIndex = 0; partyInfoIndex < cellIndex; ++partyInfoIndex) {
                  BattleCall battleCall;
                  if (!(battleCall = (BattleCall)vector3.elementAt(partyInfoIndex)).initiator) {
                     gameGraphics2.setColor(11865875);
                     gameGraphics2.fillRect(0, uiMountTradeTextsWidthIndex, uIComponent15.widthContent, Com.fontBig.fontHeight);
                  }

                  Com.fontBig.writeText(gameGraphics2, battleCall.getTitleForFastInfo(), (uIComponent15.widthContent - this.uiMountDuelsTextsWidths[partyInfoIndex]) / 2, uiMountTradeTextsWidthIndex);
                  uiMountTradeTextsWidthIndex += Com.fontBig.fontHeight + 1;
               }

               return;
            } catch (Exception exception2) {
               exception2.printStackTrace();
               return;
            }
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10:
         case 12:
            this.pressLeftSoft();
            break;
         case 11:
            this.pressRightSoft();
            return;
         case 10001:
         case 10002:
            GameCommons.instance.isFromGameMenu = true;
            (new MainMenuList()).showHeroMenu();
            return;
         case 10003:
            if (Strategist.instance.GP_Crystals <= 0) {
               ServicesMenuList.suggestBuyCrystals((Delegate)null, (Delegate)null, 1);
               return;
            }

            (new ServicesMenuList(106, -2, "1|1")).showCountChangeCrystals(true);
            return;
         case 10004:
            GameCommons.instance.isFromGameMenu = true;
            MenuManager.instance.showMenuById(37);
            return;
         case 10005:
            GameCommons.instance.isFromGameMenu = true;
            MenuManager.instance.showMenuById(92);
            return;
         case 10006:
            return;
         case 10007:
            GameCommons.instance.isFromGameMenu = true;
            (new MainMenuList()).showWorldMapMenu();
            return;
         case 10008:
            return;
         case 10010:
            if (this.post1State > 0) {
               this.pressSharp();
               return;
            }
            break;
         case 10011:
            if (this.post2State > 0) {
               GameCommons.instance.isFromGameMenu = true;
               MenuManager.instance.showMenuById(59);
               return;
            }
            break;
         case 10012:
            if (this.post3State > 0) {
               GameCommons.instance.isFromGameMenu = true;
               Chat.instance.startClanChat();
               return;
            }
            break;
         case 10013:
            if (this.post4State > 0) {
               GameCommons.instance.isFromGameMenu = true;
               Chat.instance.setUnreadnParty2();
               return;
            }
            break;
         case 10014:
            GameCommons.instance.isFromGameMenu = true;
            MenuManager.instance.showMenuById(87);
            return;
         case 10015:
            switch (Com.touchControlType) {
               case 4:
                  return;
               default:
                  GameCommons.instance.isFromGameMenu = true;
                  (new MainMenuList()).showWorldMapMenu();
                  return;
            }
         case 10016:
            return;
         case 10020:
            GameCommons.instance.isFromGameMenu = true;
            MenuManager.instance.showMenuById(199);
            return;
         case 10021:
            try {
               if (Strategist.instance.trades.size() > 0) {
                  if (Strategist.instance.trades.size() == 1) {
                     PlayersTrade playersTrade = (PlayersTrade)Strategist.instance.trades.elementAt(0);
                     TradeMenuList.instance.tradeMenuStartNew(playersTrade.id, -1, -2);
                     break;
                  }

                  GameCommons.instance.isFromGameMenu = true;
                  MenuManager.instance.showMenuById(245);
               }

               return;
            } catch (Exception exception) {
               exception.printStackTrace();
               return;
            }
         case 10022:
            try {
               if (BattleCallsHero.instance.getCallsCount() > 0) {
                  if (BattleCallsHero.instance.getCallsCount() == 1) {
                     BattleCall battleCall = (BattleCall)BattleCallsHero.instance.getCalls().elementAt(0);
                     GameCommons.instance.isFromGameMenu = true;
                     (new NeighborsMenuList(-1, -1, "")).showOneDuelAction(battleCall.withPlayerId, battleCall.withPlayerName, battleCall.initiator, false);
                     return;
                  }

                  GameCommons.instance.isFromGameMenu = true;
                  NeighborsMenuList.showDuelsMenu();
               }

               return;
            } catch (Exception exception2) {
               exception2.printStackTrace();
               return;
            }
      }

   }

   public final void pressDown() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[3] = true;
      }
   }

   public final void pressLeft() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[5] = true;
      }
   }

   public final void pressRight() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[1] = true;
      }
   }

   public final void pressUp() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[7] = true;
      }
   }

   public final void pressFire() {
      switch (Com.touchControlType) {
         case 4:
            this.playerControllDisableAll();
            GameCommons.instance.isFromGameMenu = false;
            MenuManager.instance.showMenuById(0);
            return;
         default:
            if (Strategist.instance.isMoveLastDirection8D2()) {
               this.playerControllDisableAll();
               this.gameControllAnalogData.disableAtackToPlayer();
               GameCommons.instance.isFromGameMenu = false;
               MenuManager.instance.showMenuById(0);
            }

      }
   }

   public final void releaseDown() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[3] = false;
      }
   }

   public final void releaseLeft() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[5] = false;
      }
   }

   public final void releaseRight() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[1] = false;
      }
   }

   public final void releaseUp() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.gameControllAnalogData.playerActiveMoves[7] = false;
      }
   }

   public final void releaseFire() {
   }

   public final void pressLeftSoft() {
      switch (Com.touchControlType) {
         case 4:
            this.playerControllDisableAll();
            GameCommons.instance.isFromGameMenu = false;
            MenuManager.instance.showMenuById(0);
            return;
         default:
            if (Strategist.instance.isMoveLastDirection8D2()) {
               this.playerControllDisableAll();
               this.gameControllAnalogData.disableAtackToPlayer();
               GameCommons.instance.isFromGameMenu = false;
               MenuManager.instance.showMenuById(0);
            }

      }
   }

   public final void pressRightSoft() {
      if (this.gameControllAnalogData.closestEnemyForAtack >= 0) {
         int cx2 = Com.EnsMap.Enemys[this.gameControllAnalogData.closestEnemyForAtack].cx;
         short cy2 = Com.EnsMap.Enemys[this.gameControllAnalogData.closestEnemyForAtack].cy;
         if ((cx2 = Com.currentGameMap.getPathLengthInPixelsWithTilesPrecision(Strategist.instance.cx, Strategist.instance.cy, (short)cx2, cy2, true)) < 0 || cx2 - Strategist.instance.maxIceLvl > Tile.tileDiagonalLength) {
            GameCommons.instance.showFloatText((String)"Далеко, подойдите ближе", 3000);
            return;
         }

         BattleManager.createFightWithMob(this.gameControllAnalogData.closestEnemyForAtack);
      } else {
         this.pressLeftSoft();
      }

   }

   public final void pressStar() {
      GameCommons.instance.isFromGameMenu = true;
      MenuManager.instance.showMenuById(37);
   }

   public final void pressSharp() {
      if (Strategist.instance.GP_Level < 2) {
         GameCommons.instance.showFloatText((String)"Нужно быть уровня 2", 3000);
      } else {
         GameCommons.instance.isFromGameMenu = true;
         Chat.instance.startCommonChat();
      }
   }

   public final void press1() {
      GameCommons.instance.isFromGameMenu = true;
      MenuManager.instance.showMenuById(70);
   }

   public final void press3() {
      OtherMenuList.showQuestsMenu(true);
   }

   public final void press7() {
      (new ItemsMenuList()).showMyItemsMenu(true, true);
   }

   public final void press9() {
      GameCommons.instance.isFromGameMenu = true;
      MenuManager.instance.showMenuById(4);
   }

   public final void press0() {
      GameCommons.instance.isFromGameMenu = true;
      MenuManager.instance.showMenuById(58);
   }

   public final void release1() {
   }

   public final void release3() {
   }

   public final void release7() {
   }

   public final void release9() {
   }

   public final void releaseAllEvent() {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.playerControllDisableAll();
            this.autoMove = false;
            this.drawJoystick = false;
            this.joystickActive = false;
      }
   }

   public final boolean isReplaceable() {
      return true;
   }

   public final void wheelRot(int num) {
   }

   public final void movePointer(int x, int y) {
   }

   public final void pressPointer(int x, int y) {
      if (!GameCommons.instance.uiConteiner.pressPointer(x, y)) {
         ;
      }
   }

   public final void dragPointer(int x, int y) {
      GameCommons.instance.uiConteiner.dragPointer(x, y);
   }

   public final void releaseLeftSoft() {
   }

   public final void releaseRightSoft() {
   }

   public final void releasePointer(int x, int y) {
      GameCommons.instance.uiConteiner.releasPointer(x, y);
      switch (Com.touchControlType) {
         case 0:
         case 1:
         case 2:
         case 3:
            if (this.drawJoystick || this.joystickActive) {
               this.drawJoystick = false;
               this.joystickActive = false;
               this.playerControllDisableAll();
            }

            this.wasPress = false;
            this.autoMove = false;
         default:
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 10017:
            switch (Com.touchControlType) {
               case 0:
               case 1:
               case 2:
               case 3:
                  this.checkClickOnPlayer(x, y);
                  return;
               case 4:
                  GameControllDirectData gameControllDirectData2 = this.gameControllDirectData;
                  short cameraWorld = (short)(Com.cameraWorld.getCx() + x);
                  short moveTargetLastMovingY2 = (short)(Com.cameraWorld.getCy() + y);
                  short moveTargetLastMovingY = cameraWorld;
                  GameControllDirectData gameControllDirectData3 = gameControllDirectData2;
                  gameControllDirectData2.disableControll();
                  GameSelectionStructure gameSelectionStructure;
                  if (gameControllDirectData3.selections != null && ((gameSelectionStructure = gameControllDirectData3.selections).selectedOtherStrategId != -1 || gameSelectionStructure.selectedEnemyInd != -1 || gameSelectionStructure.selectedTown != -1 || gameSelectionStructure.selectedMapMove != -1)) {
                     gameControllDirectData3.moveTarget = new GameSelectionStructure();
                     GameControllDirectData.updateSelection(gameControllDirectData3.moveTarget, gameControllDirectData3.selections);
                     short moveTargetLastMovingX = moveTargetLastMovingY;
                     moveTargetLastMovingY = moveTargetLastMovingY2;
                     if (gameControllDirectData3.moveTarget.selectedTown >= 0) {
                        moveTargetLastMovingX = Com.currentGameMap.towns[gameControllDirectData3.moveTarget.selectedTown].dx;
                        moveTargetLastMovingY = Com.currentGameMap.towns[gameControllDirectData3.moveTarget.selectedTown].dy;
                     } else if (gameControllDirectData3.moveTarget.selectedEnemyInd >= 0) {
                        Enemy enemy2;
                        moveTargetLastMovingX = (enemy2 = Com.EnsMap.Enemys[gameControllDirectData3.moveTarget.selectedEnemyInd]).cx;
                        moveTargetLastMovingY = enemy2.cy;
                        gameControllDirectData3.moveTargetLastMovingX = moveTargetLastMovingX;
                        gameControllDirectData3.moveTargetLastMovingY = moveTargetLastMovingY;
                     } else if (gameControllDirectData3.moveTarget.selectedOtherStrategId >= 0 && gameControllDirectData3.moveTarget.selectedOtherStrategObj != null) {
                        try {
                           if (gameControllDirectData3.moveTarget.selectedOtherStrategObj.swords == 1) {
                              BattleManager.showBattleInfoByPlayer(gameControllDirectData3.moveTarget.selectedOtherStrategId);
                           } else {
                              Com.startOnPlayerClick(gameControllDirectData3.moveTarget.selectedOtherStrategId, 0);
                           }
                        } catch (Exception exception) {
                           GameCommons.instance.showFloatText("Произошла ошибка!");
                           exception.printStackTrace();
                        }

                        gameControllDirectData3.moveTarget = null;
                     } else if (gameControllDirectData3.moveTarget.selectedMapMove >= 0) {
                        moveTargetLastMovingX = (short)(gameControllDirectData3.moveTarget.selectedMapMoveX * Tile.width + Tile.widthHalf);
                        moveTargetLastMovingY = (short)(gameControllDirectData3.moveTarget.selectedMapMoveY * Tile.height + Tile.heightHalf);
                     }

                     if (gameControllDirectData3.moveTarget != null) {
                        gameControllDirectData3.buildPath(moveTargetLastMovingX, moveTargetLastMovingY);
                     }
                  } else {
                     gameControllDirectData3.buildPath(moveTargetLastMovingY, moveTargetLastMovingY2);
                     AnimSprite animSprite = ImagesGlobal.animClientSprites[8];
                     short[] activeMovePathCount = gameControllDirectData3.pathFolower.activeMovePath.size() > 0 ? (short[])gameControllDirectData3.pathFolower.activeMovePath.elementAt(gameControllDirectData3.pathFolower.activeMovePath.size() - 1) : new short[]{moveTargetLastMovingY, moveTargetLastMovingY2};
                     GameCommons.instance.addGameSimpleObject((GameSimpleObject)(new GameSimpleObjectAnimate((short)(activeMovePathCount[0] - animSprite.swHalf), (short)(activeMovePathCount[1] - animSprite.shHalf), activeMovePathCount[1], animSprite)));
                  }

                  gameControllDirectData3.selections = null;
            }
         default:
      }
   }

   private boolean checkClickOnPlayer(int x, int y) {
      if ((x = this.getSelectionsUnderCoord(Com.cameraWorld.getCx() + x, Com.cameraWorld.getCy() + y).selectedOtherStrategId) >= 0) {
         try {
            Com.startOnPlayerClick(x, 0);
         } catch (Exception exception) {
            exception.printStackTrace();
         }

         return true;
      } else {
         return false;
      }
   }

   private GameSelectionStructure getSelectionsUnderCoord(int clickX, int clickY) {
      GameSelectionStructure gameSelectionStructure = new GameSelectionStructure();
      if (this.drawOrderList.first != null) {
         DrawOrderElement first2 = this.drawOrderList.first;

         do {
            int selectedEnemyInd;
            short objectX;
            int[] paddings;
            short ry;
            if (first2 instanceof DrawOrderElementOtherStrateg) {
               if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex) {
                  selectedEnemyInd = ((DrawOrderElementOtherStrateg)first2).ind;
                  objectX = first2.rx;
                  ry = first2.ry;
                  HeroOnMap heroOnMap;
                  paddings = (heroOnMap = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(selectedEnemyInd)).strategGraphic.getBasePaddingsNoAura(heroOnMap.watchDirection);
                  if (clickX > objectX - paddings[0] && clickX < objectX + paddings[1] && clickY > ry - paddings[2] && clickY < ry + paddings[3] && !heroOnMap.strategGraphic.isPixelTransporant(clickX - objectX, clickY - ry, heroOnMap.watchDirection, heroOnMap.animationState, heroOnMap.animationTime, true, false, 0, false)) {
                     gameSelectionStructure.selectedOtherStrategId = heroOnMap.id;
                     gameSelectionStructure.selectedOtherStrategObj = heroOnMap;
                  }
               }
            } else if (first2 instanceof DrawOrderElementEnemy) {
               selectedEnemyInd = ((DrawOrderElementEnemy)first2).ind;
               objectX = first2.rx;
               ry = first2.ry;
               Enemy enemy2;
               if ((enemy2 = Com.EnsMap.Enemys[selectedEnemyInd]).isFightiable()) {
                  paddings = Legionery.getPaddings(enemy2.BodyInd, enemy2.HeadInd, enemy2.direction);
                  if (clickX >= objectX - paddings[0] && clickX <= objectX + paddings[1] && clickY >= ry - paddings[2] && clickY <= ry + paddings[3] && (clickX >= objectX - Tile.width2_6 && clickX <= objectX + Tile.width2_6 && clickY >= ry - Tile.height2_3 && clickY <= ry || !Legionery.isPixelTransporant(clickX - objectX, clickY - ry, enemy2.BodyInd, enemy2.HeadInd, enemy2.direction, 0, 0, false))) {
                     gameSelectionStructure.selectedEnemyInd = selectedEnemyInd;
                  }
               }
            } else if (first2 instanceof DrawOrderElementStrateg) {
               selectedEnemyInd = first2.rx;
               objectX = first2.ry;
               int[] basePaddingsNoAura = Strategist.instance.avatarGraphic.getBasePaddingsNoAura(Strategist.instance.watchDirection);
               if (clickX >= selectedEnemyInd - basePaddingsNoAura[0] && clickX <= selectedEnemyInd + basePaddingsNoAura[1] && clickY >= objectX - basePaddingsNoAura[2] && clickY <= objectX + basePaddingsNoAura[3] && (clickX >= selectedEnemyInd - Tile.width2_6 && clickX <= selectedEnemyInd + Tile.width2_6 && clickY >= objectX - Tile.height2_3 && clickY <= objectX || !Strategist.instance.avatarGraphic.isPixelTransporant(clickX - selectedEnemyInd, clickY - objectX, Strategist.instance.watchDirection, Strategist.instance.isMoveLastDirection8D() ? StrategAnimGroup.ANIMATION_STATE_WALK : StrategAnimGroup.ANIMATION_STATE_STAY, Strategist.instance.animationTime, true, false, 0, false))) {
                  gameSelectionStructure.selectStrateg = true;
               }
            }
         } while((first2 = first2.next) != null);
      }

      gameSelectionStructure.selectedTown = Com.currentGameMap.findTownUnderCoord(clickX, clickY, true);
      gameSelectionStructure.selectedMapMove = Com.currentGameMap.findMapMoveUnderCoord(clickX, clickY);
      gameSelectionStructure.selectedMapMoveX = Math.min(Math.max(0, (int)MathGame.floorDivide((long)clickX, (long)Tile.width)), Com.currentGameMap.widthTl - 1);
      gameSelectionStructure.selectedMapMoveY = Math.min(Math.max(0, (int)MathGame.floorDivide((long)clickY, (long)Tile.height)), Com.currentGameMap.heightTl - 1);
      return gameSelectionStructure;
   }

   public static GameSimpleObjectHighlight createHighlightObjectForSelection(GameSelectionStructure gameSelectionStructure, boolean targeting, boolean agressive) {
      int[] paddings;
      if (gameSelectionStructure.selectedTown != -1 && gameSelectionStructure.selectedTown < Com.currentGameMap.towns.length) {
         Town town;
         paddings = (town = Com.currentGameMap.towns[gameSelectionStructure.selectedTown]).getPaddings(Com.currentGameMap);
         return new GameSimpleObjectHighlight(town.dx, (short)(town.dy + town.addingHeight), (short)(Com.currentGameMap.heightPx + Tile.height), paddings[0] + -2, paddings[1] + -2, paddings[3] + -2, paddings[2] + -2, targeting ? 3 : 2);
      } else if (gameSelectionStructure.selectedMapMove != -1) {
         return new GameSimpleObjectHighlight((short)(gameSelectionStructure.selectedMapMoveX * Tile.width + Tile.widthHalf), (short)(gameSelectionStructure.selectedMapMoveY * Tile.height + Tile.heightHalf), (short)(Com.currentGameMap.heightPx + Tile.height), Tile.widthHalf + -2, Tile.widthHalf + -2, Tile.heightHalf + -2, Tile.heightHalf + -2, targeting ? 3 : 2);
      } else {
         Enemy enemy2;
         if (gameSelectionStructure.selectedEnemyInd != -1 && gameSelectionStructure.selectedEnemyInd < Com.EnsMap.Enemys.length && (enemy2 = Com.EnsMap.Enemys[gameSelectionStructure.selectedEnemyInd]) != null) {
            paddings = Legionery.getPaddings(enemy2.BodyInd, enemy2.HeadInd, enemy2.direction);
            return new GameSimpleObjectHighlight(enemy2.cx, enemy2.cy, (short)(Com.currentGameMap.heightPx + Tile.height), paddings[0] + -2, paddings[1] + -2, paddings[3] + -2, paddings[2] + -2, targeting ? 4 : 2);
         } else {
            return gameSelectionStructure.selectedOtherStrategObj != null ? gameSelectionStructure.selectedOtherStrategObj.createGameSimpleObjectHighlight(!targeting ? 0 : (agressive ? 1 : 2), Com.currentGameMap) : null;
         }
      }
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
      switch (uIComponent15.id) {
         case 10015:
            switch (Com.touchControlType) {
               case 4:
                  Com.cameraWorld.centerAt(x * Tile.width / 2 + Tile.widthHalf, y * Tile.height / 2 + Tile.heightHalf);
                  this.gameControllDirectData.disableControll();
            }
         default:
            return;
         case 10019:
            this.joystickActive = true;
            this.joystickStickX = x;
            this.joystickStickY = y;
            this.tryFixJoystick();
      }
   }

   private void tryFixJoystick() {
      int deltaX = this.joystickStickX - this.joystickCenterX;
      int deltaY = this.joystickStickY - this.joystickCenterY;
      int distance = deltaX * deltaX + deltaY * deltaY;
      int joystickRadius2 = this.joystickRadius * this.joystickRadius;
      if (distance > joystickRadius2) {
         distance = (int)MathGame.integerSquareRoot((long)distance);
         joystickRadius2 = this.joystickRadius;
         this.joystickStickX = this.joystickCenterX + deltaX * joystickRadius2 / distance;
         this.joystickStickY = this.joystickCenterY + deltaY * joystickRadius2 / distance;
      }

   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
      switch (uIComponent15.id) {
         case 10017:
            switch (Com.touchControlType) {
               case 0:
                  if (!this.drawJoystick) {
                     this.drawJoystick = true;
                  }

                  this.joystickStickX = x;
                  this.joystickStickY = y;
                  dx = (this.joystickStickX - this.joystickCenterX) * (this.joystickStickX - this.joystickCenterX) + (this.joystickStickY - this.joystickCenterY) * (this.joystickStickY - this.joystickCenterY);
                  dy = this.joystickRadius * this.joystickRadius;
                  if (dx > dy) {
                     this.joystickCenterX = (int)((long)(this.joystickStickX - this.joystickCenterX) * (long)(dx - dy) / (long)dx + (long)this.joystickCenterX);
                     this.joystickCenterY = (int)((long)(this.joystickStickY - this.joystickCenterY) * (long)(dx - dy) / (long)dx + (long)this.joystickCenterY);
                  }

                  return;
               case 1:
                  if (this.wasPress) {
                     this.autoMove = true;
                     this.autoMoveX = x;
                     this.autoMoveY = y;
                     return;
                  }

                  return;
               case 2:
               case 3:
               default:
                  return;
               case 4:
                  Camera cameraWorld2 = Com.cameraWorld;
                  cameraWorld2.centerAt(cameraWorld2.getCenterX() - dx, Com.cameraWorld.getCenterY() - dy);
                  this.gameControllDirectData.disableControll();
                  return;
            }
         case 10019:
            this.joystickActive = true;
            this.joystickStickX = x;
            this.joystickStickY = y;
            this.tryFixJoystick();
      }

   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
      switch (uIComponent15.id) {
         case 10017:
            switch (Com.touchControlType) {
               case 4:
                  if (Strategist.instance.isMoveLastDirection8D2()) {
                     Camera cameraWorld2 = Com.cameraWorld;
                     cameraWorld2.centerAt(cameraWorld2.getCenterX() - dx, Com.cameraWorld.getCenterY() - dy);
                     return;
                  } else {
                     uIComponent15.stopKineticDrag();
                  }
            }
         default:
      }
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 10015:
            switch (Com.touchControlType) {
               case 4:
                  this.dragOverMinimap = true;
                  return;
               default:
                  return;
            }
         case 10017:
            switch (Com.touchControlType) {
               case 0:
                  this.highlights.reset();
                  this.highlights.selectedOtherStrategId = this.getSelectionsUnderCoord(Com.cameraWorld.getCx() + x, Com.cameraWorld.getCy() + y).selectedOtherStrategId;
                  this.joystickCenterX = x;
                  this.joystickCenterY = y;
                  return;
               case 1:
                  this.highlights.reset();
                  this.highlights.selectedOtherStrategId = this.getSelectionsUnderCoord(Com.cameraWorld.getCx() + x, Com.cameraWorld.getCy() + y).selectedOtherStrategId;
                  if (this.highlights.selectedOtherStrategId < 0) {
                     this.wasPress = true;
                     this.autoMove = true;
                     this.autoMoveX = x;
                     this.autoMoveY = y;
                     break;
                  }

                  return;
               case 2:
               case 3:
               default:
                  return;
               case 4:
                  this.gameControllDirectData.selections = this.getSelectionsUnderCoord(Com.cameraWorld.getCx() + x, Com.cameraWorld.getCy() + y);
                  GameControllDirectData.updateSelection(this.highlights, this.gameControllDirectData.selections);
            }
      }

   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10015:
            switch (Com.touchControlType) {
               case 4:
                  this.dragOverMinimap = false;
            }
         default:
            return;
         case 10017:
            int touchControlType2 = Com.touchControlType;
            this.highlights.reset();
      }
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10017:
            switch (Com.touchControlType) {
               case 4:
                  this.highlights.reset();
                  this.gameControllDirectData.selections = null;
                  return;
               default:
                  this.highlights.reset();
            }
         default:
      }
   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
      switch (uIComponent15.id) {
         case 10017:
            switch (Com.touchControlType) {
               case 4:
                  this.highlights.reset();
                  this.gameControllDirectData.selections = null;
                  return;
               default:
                  this.highlights.reset();
            }
         default:
      }
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10003:
            this.uiMountGold.enabled = this.curVisGold;
            return;
         case 10004:
            this.uiMountCrys.enabled = this.curVisCrys;
            return;
         case 10005:
            this.uiMountVip.enabled = this.curVisVip;
            return;
         case 10006:
            this.uiMountProtection.enabled = this.curVisProtection;
            return;
         case 10015:
            this.uiMountMiniMap.enabled = this.curVisMiniMap;
         case 10007:
         case 10008:
         case 10009:
         case 10010:
         case 10011:
         case 10012:
         case 10013:
         case 10014:
         default:
      }
   }

   public final void getFloatScreenSpace(int[] result) {
      result[0] = 2;
      result[1] = (this.uiMountLifeBar.enabled ? this.uiMountLifeBar.yByPosition : Com.cameraScreen.getCh() - GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing)) - 2;
   }
}
