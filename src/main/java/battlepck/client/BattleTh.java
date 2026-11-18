package battlepck.client;

import Base.GlobalMapTh;
import Base.ImagesGlobal;
import Base.IndirectNetworkHandler;
import Base.NetRequest;
import Base.ClientState;
import Base.Com;
import Base.GameCommons;
import Base.GameCommonsExtender;
import Base.GameEffects;
import Base.GameMapObjectsController;
import Engine.Camera;
import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import MenuPck.GUIDecorator;
import MenuPck.GUIHandler;
import MenuPck.MenuManager;
import MenuPck.UIComponent;
import MenuPck.UIComponentHandler;
import MenuPck.ListTypes.OtherMenuList;
import desktop.PlatformRuntime;
import Moduls.PFter;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.BattleInfo;
import Moduls.DrawOrderElement;
import Moduls.DrawOrderList;
import Moduls.chat.Chat;
import Moduls.chat.ChatMessage;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestSupMonsterTarget;
import Resources.StringResources;
import battlepck.Battle;
import battlepck.BattleBottle;
import battlepck.BattleManager;
import battlepck.BattleMember;
import battlepck.BattleMemberSmall;
import battlepck.BattleOrder;
import battlepck.BattleQuantumEvent;
import battlepck.BattleUnit;
import java.io.DataInputStream;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class BattleTh implements GameCommonsExtender, UIComponentHandler, PFter {
   private static String[] HOT_KEYS = new String[]{"x", "c", "v"};
   public static int pressFireStep = 0;
   private int uiScreenHeight = 0;
   private int uiScreenWidth = 0;
   private UIComponent uiOverlay;
   private UIComponent uiMountLatency;
   private UIComponent uiMountTimer;
   private UIComponent[] uiSlots;
   private UIComponent uiTopWindow;
   private UIComponent uiHealthBarMy;
   private UIComponent uiHealthBarEnemy;
   private UIComponent uiChatIcon;
   private UIComponent uiChatText;
   private UIComponent uiRunIcon;
   private UIComponent uiFriendsIcons;
   private UIComponent uiEnemysIcons;
   private long uiHealthEnemyVal;
   private long uiHealthEnemyMax;
   private long uiHealthMyVal;
   private long uiHealthMyMax;
   private long uiLatencyVal;
   private static Vector ordersArray = new Vector();
   private DrawOrderList drawOrderList;
   private int topWindowState;
   private boolean topTextDraw;
   private String topTextCurrent;
   private MystText topTextMyst;
   private boolean chatTextDraw;
   private String chatTextCurrent;
   private MystText chatTextMyst;
   private long chatTextCountdown;
   private Vector skillCircles;
   private Vector offscreenIcons;
   private int pressedSlot;
   private int overSlot;
   private long overSlotStartTime;
   private boolean activePressInTouch;
   private long activePressInTouchSinceTime;
   private int activePressInTouchCoordX;
   private int activePressInTouchCoordY;
   private long cameraMoveAccumTime;
   private int[] cursorPosition;
   private int[] cursorWorldPosition;
   private long cursorMoveAccumTime;
   private boolean[] cursorMoveDirections;
   private int cursorType;
   private int cursorSate;
   public static int currentChoosedUnit = -1;
   private int choosedBottle;
   private boolean chooseModeEnemys;
   private boolean chooseModeFriends;
   private boolean chooseModeSelf;
   private boolean chooseModeGround;
   private long endBattleWatchSinceTime;
   private static boolean workBattle;
   public static boolean realBattle = true;
   public static DataInputStream viewBattleDIS = null;
   public static int viewBattleLastAddedEventsTillTick = 0;
   public static int viewBattleNextReadnQuantumEventTick = -1;
   public static BattleQuantumEvent viewBattleNextReadnQuantumEvent = null;
   public static int viewBattleNextPacketSize = -1;
   public static int viewBattleNextPacketId = -1;
   public static int viewBattleNextPacketTickInd = -1;
   public static int viewBattleWillEndTick = -1;
   public static boolean viewBattleWillEndTickApplyed = false;
   private static boolean res_FastFinish;
   public static int res_ExitReason;
   public static int res_WinPrisoners;
   public static int res_WinMoney;
   public static int res_WinPrisons;
   public static int res_ChangeFire;
   public static int res_ChangeIce;
   public static int res_ChangeHero;
   private static int res_TimerTime;
   public static int res_WinItem;
   private static String res_TrophyText;
   public static int res_ChangeLife;
   private static QuestInPlayer[] res_GiveQuests;
   public static BattleInfo CurBattleInf = new BattleInfo();
   private boolean centerBattle;
   private boolean fillBackground;
   private boolean wasPressRSoft;
   private boolean wasPress;
   private boolean activateCursorAfterAnalize;
   private boolean firstFunc;
   private boolean wasPress1;
   private boolean wasPress3;
   private boolean wasPress7;
   private boolean wasPress9;
   private boolean wasPressLeftSoft;
   public static int funcStep = 0;
   private int shooBottlePhase;
   private String shooBottlePhaseInfo;
   private int bChooPhase;

   public BattleTh() {
      this.uiOverlay = new UIComponent(this, GUIDecorator.instance, 10006, 0, false, false);
      this.uiMountLatency = new UIComponent(this, GUIDecorator.instance, 10001, 5, false, false, true);
      this.uiMountTimer = new UIComponent(this, GUIDecorator.instance, 10008, 4, false, false, true);
      this.uiSlots = new UIComponent[]{new UIComponent(this, GUIDecorator.instance, 11000, 8, false), new UIComponent(this, GUIDecorator.instance, 11001, 8, false), new UIComponent(this, GUIDecorator.instance, 11002, 8, false), new UIComponent(this, GUIDecorator.instance, 11003, 8, false), new UIComponent(this, GUIDecorator.instance, 11004, 8, false)};
      this.uiTopWindow = new UIComponent(this, GUIDecorator.instance, 10002, 8, false, false, true);
      this.uiHealthBarMy = new UIComponent(this, GUIDecorator.instance, 10003, 8, false, false, true);
      this.uiHealthBarEnemy = new UIComponent(this, GUIDecorator.instance, 10004, 8, false, false, true);
      this.uiChatIcon = new UIComponent(this, GUIDecorator.instance, 10007, 0, false, false);
      this.uiChatText = new UIComponent(this, GUIDecorator.instance, 10005, 0, false, false);
      this.uiRunIcon = new UIComponent(this, GUIDecorator.instance, 10009, 0, false, false, false);
      this.uiFriendsIcons = new UIComponent(this, GUIDecorator.instance, 10010, 0, false, false, true);
      this.uiEnemysIcons = new UIComponent(this, GUIDecorator.instance, 10011, 0, false, false, true);
      this.drawOrderList = new DrawOrderList();
      this.topWindowState = 0;
      this.topTextDraw = false;
      this.topTextCurrent = "";
      this.topTextMyst = new MystText();
      this.chatTextDraw = false;
      this.chatTextCurrent = "";
      this.chatTextMyst = new MystText();
      this.chatTextCountdown = 0L;
      this.skillCircles = new Vector();
      this.offscreenIcons = new Vector();
      this.pressedSlot = -1;
      this.overSlot = -1;
      this.overSlotStartTime = -1L;
      this.activePressInTouch = false;
      this.activePressInTouchSinceTime = 0L;
      this.activePressInTouchCoordX = -1;
      this.activePressInTouchCoordY = -1;
      this.cameraMoveAccumTime = 0L;
      this.cursorPosition = new int[2];
      this.cursorWorldPosition = null;
      this.cursorMoveAccumTime = 0L;
      this.cursorMoveDirections = new boolean[4];
      this.cursorType = 1;
      this.cursorSate = 0;
      this.endBattleWatchSinceTime = -1L;
      this.centerBattle = false;
      this.fillBackground = false;
      this.wasPressRSoft = false;
      this.wasPress = false;
      this.activateCursorAfterAnalize = false;
      this.firstFunc = false;
      this.wasPress1 = false;
      this.wasPress3 = false;
      this.wasPress7 = false;
      this.wasPress9 = false;
      this.wasPressLeftSoft = false;
      this.shooBottlePhase = 0;
      this.shooBottlePhaseInfo = "";
      this.bChooPhase = 0;
      this.uiMountLatency.ignoreInTouch = true;
      this.uiMountTimer.ignoreInTouch = true;
      this.uiTopWindow.ignoreInTouch = true;
      this.uiHealthBarEnemy.ignoreInTouch = true;
   }

   private void Set() {
      GlobalMapTh.gmTh.pauseAndWait(8800001);
      MenuManager.instance.guiHandler.hideAllWindows();
      Com.cameraBattle.setLimit(-Tile.width, -Tile.height, Com.currentBattleMap.widthPx + (Tile.width << 1), Com.currentBattleMap.heightPx + (Tile.height << 1));
      centerCamera(Strategist.instance.cx, Strategist.instance.cy);
      this.uiTopWindow.setUseBuffer(!Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1);
      this.recalcParams(Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
      this.topTextDraw = false;
      Com.mainTh.setStopPaint();
      Com.mainTh.setPFter1(this);
      Chat.instance.pullForBattleMessage();
      GameCommons.instance.onActiveCameraChanged(Com.cameraBattle, Com.currentBattleMap);
      if (Com.touchControlType != 4) {
         Com.showDefaultCursor();
      } else {
         Com.showGameCursor();
      }

      BattleTh battleTh2 = this;
      this.wasPressRSoft = false;
      this.activateCursorAfterAnalize = false;
      this.wasPress = false;
      this.firstFunc = true;
      this.wasPress1 = false;
      this.wasPress3 = false;
      this.wasPress7 = false;
      this.wasPress9 = false;
      this.wasPressLeftSoft = false;
      this.choosedBottle = -1;
      this.pressedSlot = -1;
      this.overSlot = -1;
      this.cursorWorldPosition = null;
      this.cursorPosition[0] = Com.cameraScreen.getCw() / 2;
      this.cursorPosition[1] = Com.cameraScreen.getCh() / 2;
      this.cursorMoveAccumTime = 0L;

      for(int cursorMoveDirectionIndex = 0; cursorMoveDirectionIndex < battleTh2.cursorMoveDirections.length; ++cursorMoveDirectionIndex) {
         battleTh2.cursorMoveDirections[cursorMoveDirectionIndex] = false;
      }

      battleTh2.cursorType = 3;
      GameCommons.instance.mapBufferSetMarkUnblockedTiles(false);
      currentChoosedUnit = -1;
      this.centerBattle = true;
      this.fillBackground = true;
      this.setCursor(1);
   }

   public final void onChangeResolution(int screenWidth, int screenHeight) {
      if (Com.mainTh.getPFter1() == this) {
         GameCommons.instance.onActiveCameraChanged(Com.cameraBattle, Com.currentBattleMap);
         this.recalcParams(screenWidth, screenHeight);
         this.fixCursor();
      }
   }

   private void recalcParams(int screenWidth, int screenHeight) {
      this.uiScreenWidth = screenWidth;
      this.uiScreenHeight = screenHeight;
      this.initTopText();
      this.initChatText();
      GameCommons.instance.uiConteiner.clear();
      GameCommons.instance.uiConteiner.addComponent(this.uiOverlay);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountLatency);
      GameCommons.instance.uiConteiner.addComponent(this.uiMountTimer);
      GameCommons.instance.uiConteiner.addComponent(this.uiChatText);
      GameCommons.instance.uiConteiner.addComponent(this.uiChatIcon);
      GameCommons.instance.uiConteiner.addComponent(this.uiRunIcon);
      GameCommons.instance.uiConteiner.addComponent(this.uiFriendsIcons);
      GameCommons.instance.uiConteiner.addComponent(this.uiEnemysIcons);
      GameCommons.instance.uiConteiner.addComponent(this.uiHealthBarMy);
      GameCommons.instance.uiConteiner.addComponent(this.uiHealthBarEnemy);
      GameCommons.instance.uiConteiner.addComponent(this.uiTopWindow);

      for(screenWidth = 0; screenWidth < this.uiSlots.length; ++screenWidth) {
         GameCommons.instance.uiConteiner.addComponent(this.uiSlots[screenWidth]);
      }

      GUIHandler.positionateSoftButtons(false, GameCommons.instance.uiConteiner, this, this.uiScreenWidth, this.uiScreenHeight, Com.fontBig, GameCommons.instance.guiSoftSpacing);
      this.initUI();
   }

   private void chooseBottle(int slotInd) {
      this.shooBottlePhase = 10;
      if (BattleManager.getCurrentBattle() != null) {
         this.shooBottlePhase = 20;
         if (BattleManager.getCurrentBattle().getPlayerMember() != null) {
            this.shooBottlePhase = 30;
            BattleBottle battleBottle;
            if ((battleBottle = (BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)slotInd))) != null) {
               this.shooBottlePhaseInfo = "bottle is " + battleBottle.itemTypeId + " " + battleBottle.slotInd;
               BattleMemberSmall battleMemberSmall = BattleManager.getCurrentBattle().getPlayerMemberSmall();
               boolean isStoped = false;

               int fastTargetIndexForBottle;
               for(fastTargetIndexForBottle = 0; fastTargetIndexForBottle < battleMemberSmall.stopedItems.size(); ++fastTargetIndexForBottle) {
                  int[] els = (int[])battleMemberSmall.stopedItems.elementAt(fastTargetIndexForBottle);

                  for(int lengthIndex = 1; lengthIndex < els.length; ++lengthIndex) {
                     if (els[lengthIndex] == battleBottle.itemTypeId) {
                        isStoped = true;
                        break;
                     }
                  }

                  if (isStoped) {
                     break;
                  }
               }

               this.shooBottlePhase = 40;
               if (battleBottle.count > 0 && !isStoped && BattleManager.getCurrentBattle().getPlayerMember().leftUsesCount != 0 && battleBottle.cooldownTimeCur <= 0) {
                  if (battleBottle.effect.appGround) {
                     this.shooBottlePhase = 45;
                     fastTargetIndexForBottle = -4;
                  } else {
                     this.shooBottlePhase = 50;
                     fastTargetIndexForBottle = this.getFastTargetIndexForBottle(battleBottle);
                  }

                  this.shooBottlePhase = 120;
                  if (fastTargetIndexForBottle != -3 && fastTargetIndexForBottle != -2) {
                     if (fastTargetIndexForBottle >= 0) {
                        this.shooBottlePhase = 150;
                        issuePlayerOrder(fastTargetIndexForBottle, (byte)2, slotInd, -1, -1);
                        MenuManager.instance.guiHandler.hideAllWindows();
                        Com.battleTh.setCursor(1);
                        this.shooBottlePhase = 160;
                     } else {
                        this.shooBottlePhase = 170;
                        this.chooseModeEnemys = battleBottle.effect.appEnemys;
                        this.chooseModeFriends = battleBottle.effect.appFriends;
                        this.chooseModeSelf = battleBottle.effect.appSelf;
                        this.chooseModeGround = battleBottle.effect.appGround;
                        this.shooBottlePhase = 180;
                        this.choosedBottle = slotInd;
                        if (battleBottle.effect.appGround) {
                           Com.battleTh.setCursor(11);
                        } else {
                           Com.battleTh.setCursor(10);
                        }

                        this.shooBottlePhase = 190;
                     }
                  } else {
                     this.shooBottlePhase = 130;
                     MenuManager.instance.guiHandler.hideAllWindows();
                     Com.battleTh.setCursor(1);
                     this.shooBottlePhase = 140;
                  }

                  this.shooBottlePhase = 200;
               } else {
                  this.shooBottlePhase = 210;
                  if (battleBottle.cooldownTimeCur > 0) {
                     GameCommons.instance.showFloatText((String)"Перезарядка!", 2000);
                  } else if (isStoped) {
                     GameCommons.instance.showFloatText((String)"Прием заблокирован", 2000);
                  } else {
                     GameCommons.instance.showFloatText((String)"Предметы закончились", 2000);
                  }

                  this.shooBottlePhase = 220;
               }

               this.shooBottlePhase = 230;
            }
         }
      }
   }

   private int getFastTargetIndexForBottle(BattleBottle battleBottle) {
      int playerArmyValue = -3;
      if (battleBottle.effect.appFriends || battleBottle.effect.appSelf) {
         playerArmyValue = getPlayerArmyValue(battleBottle.effect.appFriends, battleBottle.effect.appSelf);
         if (battleBottle.effect.appFriends && playerArmyValue >= 0) {
            playerArmyValue = -1;
         }
      }

      int enemyArmyValue = -3;
      if (battleBottle.effect.appEnemys && (enemyArmyValue = getEnemyArmyValue()) >= 0) {
         enemyArmyValue = -1;
      }

      if (enemyArmyValue == -3) {
         return playerArmyValue;
      } else if (playerArmyValue == -3) {
         return enemyArmyValue;
      } else if (enemyArmyValue == -2 && playerArmyValue == -2) {
         return -2;
      } else if (playerArmyValue == -2) {
         return enemyArmyValue;
      } else {
         return enemyArmyValue == -2 ? playerArmyValue : -1;
      }
   }

   public static void addDrawOrder(boolean forMyUnit, int tragetInd, int targetX, int targetY) {
      byte drawOrderType;
      if (forMyUnit) {
         if (tragetInd == -1) {
            drawOrderType = 8;
         } else {
            drawOrderType = 9;
         }
      } else if (tragetInd == -1) {
         drawOrderType = 12;
      } else {
         drawOrderType = 13;
      }

      synchronized(ordersArray) {
         ordersArray.addElement(new BattleDrawOrder(drawOrderType, tragetInd, targetX, targetY));
      }
   }

   private static void animateDrawOrders(int doubleTime, DrawOrderList drawOrderList2) {
      for(int i = ordersArray.size() - 1; i >= 0; --i) {
         BattleDrawOrder battleDrawOrder = (BattleDrawOrder)ordersArray.elementAt(i);
         ImagesGlobal.animClientSprites[battleDrawOrder.sprite].advanceAnimationLoop(battleDrawOrder.aSost, doubleTime);
         if (battleDrawOrder.aSost.WasLoop) {
            ordersArray.removeElementAt(i);
         } else {
            BattleUnit battleUnit;
            if (battleDrawOrder.targetInd >= 0 && (battleUnit = BattleManager.getCurrentBattleUnit(battleDrawOrder.targetInd)) != null) {
               short cy2 = battleUnit.cy;
               short cx2 = battleUnit.cx;
               battleDrawOrder.rx = cx2;
               battleDrawOrder.ry = cy2;
               battleDrawOrder.sortInd = (short)(cy2 - 1);
            }

            drawOrderList2.addAndSortFromEnd2(battleDrawOrder);
         }
      }

   }

   private void showTopText(String topTextCurrent2) {
      if (this.topTextCurrent.length() == topTextCurrent2.length() && this.topTextCurrent.equals(topTextCurrent2)) {
         this.topTextDraw = this.topTextCurrent.length() > 0;
      } else {
         this.topTextCurrent = topTextCurrent2;
         this.topTextDraw = this.topTextCurrent.length() > 0;
         this.initTopText();
      }
   }

   private void initTopText() {
      if (this.topTextDraw) {
         MystString mystString;
         (mystString = new MystString()).Create(this.topTextCurrent, Com.fontBig);
         this.topTextMyst.create(mystString, this.uiScreenWidth - 20, 1, Com.fontBig);
         this.uiTopWindow.needRedraw = true;
      }
   }

   private void initChatText() {
      if (this.chatTextDraw) {
         MystString mystString;
         (mystString = new MystString()).Create(this.chatTextCurrent, Com.fontBig);
         this.chatTextMyst.create(mystString, this.uiScreenWidth - 60, 2, 'ə', 1, Com.fontBig);
      }
   }

   public static void resultBattleClear() {
      res_WinItem = 0;
      res_WinMoney = 0;
      res_ChangeFire = 0;
      res_ChangeIce = 0;
      res_ChangeHero = 0;
      res_ChangeLife = 0;
   }

   public static void appleBattleRes() {
      GameCommons.instance.stopTimer();
      Com.PrBarTh.Set(true);
      NetRequest netRequest;
      (netRequest = new NetRequest()).writeHeader(1600000);
      Com.sendRequest(netRequest);
      int integerCode = -1;
      int integerCode2 = -1;
      int GP_Level2 = Strategist.instance.GP_Level;

      int integerCode3;
      try {
         if ((integerCode3 = netRequest.response.readInt()) == 1600001) {
            integerCode = netRequest.response.readInt();
            integerCode2 = netRequest.response.readInt();
            Com.applyRewardEvents(netRequest);
            Strategist.instance.cx = (short)netRequest.response.readInt();
            Strategist.instance.cy = (short)netRequest.response.readInt();
            Com.cameraWorld.resetSmooth();
         } else if (integerCode3 == 1600002 || integerCode3 == 1600003) {
            integerCode = netRequest.response.readInt();
            integerCode2 = netRequest.response.readInt();
            Com.applyRewardEvents(netRequest);
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      MenuManager.instance.guiHandler.hideAllWindows();
      Com.GameTh.Set();
      ClientState.instance.set(4);
      BattleManager.setCurrentBattle((Battle)null);
      if (res_ExitReason == 4) {
         if (integerCode == 1) {
            GameMapObjectsController.destroyMapObject2(integerCode2);
         } else if (integerCode == 0) {
            Com.EnsMap.Enemys[integerCode2].Enable = false;
            GameEffects instance2 = GameEffects.instance;
            GameEffects.spawnClientEffect4(Com.EnsMap.Enemys[integerCode2].cx, Com.EnsMap.Enemys[integerCode2].cy);
            String nameId2 = Com.currentGameMap.nameId;
            String nameId3 = Com.EnsMap.Enemys[integerCode2].nameId;
            QuestStrateg instance3 = QuestStrateg.instance;

            for(integerCode3 = 0; integerCode3 < instance3.quests.size(); ++integerCode3) {
               QuestInPlayer questInPlayer;
               QuestSupMonsterTarget[] needMonsters2 = (questInPlayer = (QuestInPlayer)instance3.quests.elementAt(integerCode3)).questSingle.needMonsters;

               for(int needMonsters2Index = 0; needMonsters2Index < needMonsters2.length; ++needMonsters2Index) {
                  QuestSupMonsterTarget questSupMonsterTarget;
                  if ((questSupMonsterTarget = needMonsters2[needMonsters2Index]).monsterName.equals(nameId3) && (questSupMonsterTarget.maps.length == 0 || Mystery.isContainsStringInArray(questSupMonsterTarget.maps, nameId2))) {
                     if ((questInPlayer = questInPlayer).killedMonsters.containsKey(nameId3)) {
                        needMonsters2Index = (Integer)questInPlayer.killedMonsters.get(nameId3) + 1;
                        questInPlayer.killedMonsters.put(nameId3, needMonsters2Index);
                     } else {
                        questInPlayer.killedMonsters.put(nameId3, 1);
                        boolean unusedProgressMarker = true;
                     }

                     Com.onQuestsChanged(true);
                     break;
                  }
               }
            }
         } else if (integerCode == 2) {
            QuestStrateg.instance.tryAddKilledPlayer(integerCode2);
         }
      } else if (res_ExitReason != 6 && res_ExitReason != 2 && integerCode == 2) {
         QuestStrateg.instance.removeHuntingQuestsForTarget(integerCode2);
      }

      QuestInPlayer questInPlayer2 = null;
      if (res_GiveQuests != null) {
         for(int res_GiveQuestIndex = 0; res_GiveQuestIndex < res_GiveQuests.length; ++res_GiveQuestIndex) {
            QuestStrateg.instance.quests.addElement(res_GiveQuests[res_GiveQuestIndex]);
            questInPlayer2 = res_GiveQuests[res_GiveQuestIndex];
         }
      }

      if (questInPlayer2 != null) {
         Com.onQuestsChanged(true);
         QuestStrateg.instance.changeCompassedQuest(questInPlayer2);
      }

      Com.PrBarTh.Stop();
      if (GP_Level2 != Strategist.instance.GP_Level) {
         GameCommons.instance.showFloatText((String)"Ты получил уровень!", 5000);
      }

      Com.GameTh.trophyStart(res_ChangeLife, res_WinPrisons, res_WinMoney, res_WinItem, res_ChangeFire, res_ChangeIce, res_ChangeHero);
      res_GiveQuests = null;
   }

   private static void initBattleRes() {
      NetRequest netRequest;
      (netRequest = new NetRequest()).writeHeader(1500000);

      try {
         netRequest.request.writeBoolean(false);
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      Com.sendRequest(netRequest);

      try {
         if (netRequest.response.readInt() == 1500001) {
            res_FastFinish = netRequest.response.readBoolean();
            res_ExitReason = netRequest.response.readInt();
            netRequest.response.readInt();
            netRequest.response.readInt();
            res_WinMoney = netRequest.response.readInt();
            netRequest.response.readInt();
            res_WinPrisons = netRequest.response.readInt();
            netRequest.response.readInt();
            res_ChangeFire = netRequest.response.readInt();
            res_ChangeIce = netRequest.response.readInt();
            res_ChangeHero = netRequest.response.readInt();
            res_TrophyText = netRequest.response.readUTF();
            netRequest.response.readInt();
            netRequest.response.readInt();
            res_TimerTime = netRequest.response.readInt();
            res_GiveQuests = new QuestInPlayer[netRequest.response.readInt()];

            for(int res_GiveQuestIndex = 0; res_GiveQuestIndex < res_GiveQuests.length; ++res_GiveQuestIndex) {
               res_GiveQuests[res_GiveQuestIndex] = new QuestInPlayer(netRequest.response);
            }

            res_ChangeLife = 0;
         }

      } catch (Exception exception2) {
         Com.SendMistace("Can't initBattleRes " + exception2.toString(), true, true, exception2);
      }
   }

   public static void finishRealBattle() {
      initBattleRes();
      Com.GameTh.Set();
      ClientState.instance.set(8);
      GameCommons.instance.redrawMapBuffer();
      if (!res_FastFinish) {
         GameCommons.instance.startTimer(res_TimerTime, 4);
         if (res_ExitReason == 4) {
            GameEffects.instance.stopSkullsFall();
            (new OtherMenuList()).showMenuMessage(24, "Ярость и Удача!\n" + CurBattleInf.WinAutro + res_TrophyText);
         } else if (res_ExitReason == 2) {
            (new OtherMenuList()).showMenuMessage(25, "Отступление\nТы удачно отступил из боя" + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : ""));
         } else if (res_ExitReason == 6) {
            (new OtherMenuList()).showMenuMessage(25, "Ничья\nНикто не выирал и не проиграл" + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : ""));
         } else {
            (new OtherMenuList()).showMenuMessage(25, "Проклятье...\nТы потерпел поражение." + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : ""));
         }
      } else {
         if (res_ExitReason == 4) {
            if (res_TrophyText.length() > 0) {
               GameCommons.instance.showFloatText((String)res_TrophyText, 3000, 250, 1000, 0);
            }
         } else if (res_ExitReason == 2) {
            GameCommons.instance.showFloatText((String)("Отступление" + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : "")), 3000, 250, 1000, 0);
         } else if (res_ExitReason == 6) {
            GameCommons.instance.showFloatText((String)("Ничья" + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : "")), 3000, 250, 1000, 0);
         } else {
            GameCommons.instance.showFloatText((String)("Ты потерпел\nпоражение..." + (res_TrophyText.length() > 0 ? "\n" + res_TrophyText : "")), 3000, 250, 1000, 0);
         }

         appleBattleRes();
      }

      Com.clanLastCheckArenaTime = Mystery.currentTimeMillis;
   }

   private static int findUnitAtPixel(int cX, int cY) {
      BattleUnit[] currentBattleUnits = BattleManager.getCurrentBattleUnits();
      BattleUnit battleUnit = null;

      for(int lengthIndex = 0; lengthIndex < currentBattleUnits.length; ++lengthIndex) {
         BattleUnit battleUnit2;
         if (!(battleUnit2 = currentBattleUnits[lengthIndex]).Dead && (battleUnit == null || battleUnit.cy < battleUnit2.cy)) {
            int[] paddings = battleUnit2.getPaddings();
            if (cX >= battleUnit2.cx - paddings[0] && cX < battleUnit2.cx + paddings[1] && cY >= battleUnit2.cy - paddings[2] && cY < battleUnit2.cy + paddings[3] && (cX >= battleUnit2.cx - Tile.width2_6 && cX <= battleUnit2.cx + Tile.width2_6 && cY >= battleUnit2.cy - Tile.height2_3 && cY <= battleUnit2.cy || !battleUnit2.isPixelTransporant(cX - battleUnit2.cx, cY - battleUnit2.cy))) {
               battleUnit = battleUnit2;
            }
         }
      }

      if (battleUnit == null) {
         return -1;
      } else {
         return battleUnit.hisInd;
      }
   }

   private static int findNearestTargetUnit(int cX, int cY, boolean chooEnemys, boolean chooFriends) {
      if (BattleManager.getCurrentBattle() == null) {
         return -1;
      } else if (BattleManager.getCurrentBattle().getPlayerMember() == null) {
         return -1;
      } else {
         BattleMember battleMember = BattleManager.getCurrentBattle().getPlayerMember();
         int nearestTargetUnit = -1;
         long mAX_VALUE = Long.MAX_VALUE;
         BattleUnit[] currentBattleUnits = BattleManager.getCurrentBattleUnits();

         for(int lengthIndex = 0; lengthIndex < currentBattleUnits.length; ++lengthIndex) {
            long deltaX;
            BattleUnit battleUnit;
            if (((battleUnit = currentBattleUnits[lengthIndex]).side == battleMember.side && chooFriends || battleUnit.side != battleMember.side && chooEnemys) && !battleUnit.Dead && (deltaX = (long)((battleUnit.cx - cX) * (battleUnit.cx - cX) + (battleUnit.cy - cY) * (battleUnit.cy - cY))) < mAX_VALUE) {
               mAX_VALUE = deltaX;
               nearestTargetUnit = battleUnit.hisInd;
            }
         }

         return nearestTargetUnit;
      }
   }

   public static void startBattle(NetRequest netRequest, boolean viewBattle) {
      ClientState.instance.set(viewBattle ? 10 : 7);
      BattleNetTh.waitOldDone();
      Com.skipGameLoop = true;
      Com.GameTh.isDrawTrophys = false;
      Com.battleTh.endBattleWatchSinceTime = -1L;
      realBattle = true;
      DataInputStream response2 = netRequest.response;
      viewBattleNextReadnQuantumEvent = null;
      viewBattleNextPacketSize = -1;
      viewBattleNextPacketId = -1;
      viewBattleLastAddedEventsTillTick = 0;
      viewBattleWillEndTick = -1;
      viewBattleWillEndTickApplyed = false;
      if (viewBattle) {
         realBattle = false;
         response2 = viewBattleDIS;

         int integerCode;
         try {
            do {
               int integerCode2 = response2.readInt();
               if ((integerCode = response2.readInt()) != 1500) {
                  response2.skip((long)(integerCode2 - 4));
               }
            } while(integerCode != 1500);
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }

      try {
         BattleManager.setCurrentBattle(new Battle(Com.currentBattleMap, response2, new Object()));
      } catch (Exception exception2) {
         Com.SendMistace("Error in startBattle in new Battle() real", true, true, exception2);
         ClientState.instance.set(4);
         return;
      }

      Com.battleTh.Set();
      ordersArray.removeAllElements();
      workBattle = true;
      GameEffects.instance.stopRain();
      Com.firstMapLoadPending = false;
      GameCommons.instance.redrawMapBuffer();
      GameCommons.instance.stopTimer();
      Com.PrBarTh.Stop();
      MenuManager.instance.guiHandler.hideAllWindows();
      BattleNetTh.runNewWithWaitOldDone();
      Mystery.InitFPS();
   }

   public final void releaseDown() {
      this.cursorMoveDirections[3] = false;
   }

   public final void releaseUp() {
      this.cursorMoveDirections[2] = false;
   }

   public final void releaseLeft() {
      this.cursorMoveDirections[0] = false;
   }

   public final void releaseRight() {
      this.cursorMoveDirections[1] = false;
   }

   public final void releaseFire() {
      this.releaseCursor();
   }

   public final void pressDown() {
      this.cursorMoveDirections[3] = true;
   }

   public final void pressUp() {
      this.cursorMoveDirections[2] = true;
   }

   public final void pressLeft() {
      this.cursorMoveDirections[0] = true;
   }

   public final void pressRight() {
      this.cursorMoveDirections[1] = true;
   }

   public final void pressFire() {
      this.pressCursor();
   }

   private static void centerCamera(int px, int py) {
      Com.cameraBattle.centerAt(px, py);
   }

   private static void smoothMoveCamera(int px, int py) {
      Com.cameraBattle.smoothMoveTo(px, py);
   }

   public final void functionate() {
      IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
      if ((Com.currentGameMap.serverIndex < 0 || Com.currentGameMap.serverIndex != Strategist.instance.curMapInd) && Strategist.instance.curMapInd >= 0) {
         Com.startGameMap(true);
      }

   }

   public final void functionateUnderDrawProgress() {
      this.functionate();
      if (workBattle) {
         BattleOffscreenIcon.OBJECTS_POOL.clear(this.offscreenIcons);
         this.skillCircles.removeAllElements();
         ChatMessage chatMessage;
         if ((chatMessage = Chat.instance.pullForBattleMessage()) != null) {
            String chatTextCurrent2 = "";
            if (chatMessage.playerGender >= 0 && chatMessage.playerRace >= 0 && chatMessage.playerLevel >= 0) {
               chatTextCurrent2 = chatTextCurrent2 + "[" + StringResources.c_chat_sex_symb[chatMessage.playerGender] + StringResources.c_chat_class_symb[chatMessage.playerRace] + chatMessage.playerLevel + "]";
            }

            chatTextCurrent2 = Mystery.stringFormat(Mystery.stringFormat(chatTextCurrent2 + chatMessage.playerName + ">>" + chatMessage.message, '♂', new String[]{"̾̿"}), '♀', new String[]{"̼̽"});
            this.chatTextCountdown = 3000L;
            if (this.chatTextCurrent.length() == chatTextCurrent2.length() && this.chatTextCurrent.equals(chatTextCurrent2)) {
               this.chatTextDraw = this.chatTextCurrent.length() > 0;
            } else {
               this.chatTextCurrent = chatTextCurrent2;
               this.chatTextDraw = this.chatTextCurrent.length() > 0;
               this.initChatText();
            }
         }

         GameCommons.instance.uiConteiner.functional((int)Mystery.FrameTimeI);
         this.drawOrderList.clear();
         GameCommons.instance.functionateDraw(this.drawOrderList);
         funcStep = 100;
         Battle battle2;
         if ((battle2 = BattleManager.getCurrentBattle()) != null && battle2.isWasEnd()) {
            if (this.endBattleWatchSinceTime < 0L) {
               this.endBattleWatchSinceTime = Mystery.currentTimeMillis;
            }

            if (Mystery.currentTimeMillis - this.endBattleWatchSinceTime >= 1500L) {
               if (realBattle) {
                  finishRealBattle();
                  return;
               }

               GameCommons.instance.redrawMapBuffer();

               try {
                  viewBattleDIS.close();
               } catch (Exception exception) {
                  exception.printStackTrace();
               }

               viewBattleDIS = null;
               GameCommons.instance.stopTimer();
               MenuManager.instance.guiHandler.hideAllWindows();
               Com.GameTh.Set();
               ClientState.instance.set(4);
               BattleManager.setCurrentBattle((Battle)null);
               return;
            }
         }

         if (this.chatTextDraw) {
            this.chatTextCountdown -= Mystery.FrameTimeI;
            if (this.chatTextCountdown <= 0L) {
               this.chatTextDraw = false;
            }
         }

         funcStep = 400;
         GameCommons.instance.functionateLocal(Com.cameraBattle);
         funcStep = 500;
         funcStep = 600;
         switch (Com.touchControlType) {
            default:
               this.cursorAnimateMove(Mystery.FrameTimeI);
            case 4:
               funcStep = 700;
               ImagesGlobal.animClientSprites[0].advanceAnimation(ImagesGlobal.GSprtsSost[0], (int)Mystery.FrameTimeI);
               funcStep = 800;

               try {
                  animateDrawOrders((int)(Mystery.FrameTimeI + (BattleManager.getCurrentBattle().getSpeedUpForQuantsCount() > 0 ? Mystery.FrameTimeI : 0L)), this.drawOrderList);
               } catch (Exception exception2) {
                  Com.SendMistace("Error battle animateDrawOrders", true, false, exception2);
               }

               funcStep = 900;
               BattleManager.processReplaceBattle();
               if (BattleManager.getCurrentBattle() != null) {
                  BattleManager.getCurrentBattle().processActiveState();
               }

               this.cursorSate = 0;
               currentChoosedUnit = -1;
               switch (Com.touchControlType) {
                  case 4:
                     if (this.activePressInTouch) {
                        this.cursorWorldPosition = cursorToRealCoord(this.activePressInTouchCoordX, this.activePressInTouchCoordY);
                        this.analizeCursorStateByType();
                     }
                     break;
                  default:
                     this.cameraMoveAccumTime += Mystery.FrameTimeI;
                     int cameraMoveAccumTime2 = (int)(this.cameraMoveAccumTime / 12L);
                     this.cameraMoveAccumTime %= 12L;
                     if (cameraMoveAccumTime2 > 0) {
                        Camera camera;
                        if (this.cursorPosition[0] <= 16) {
                           camera = Com.cameraBattle;
                           camera.setCx(camera.getCx() - cameraMoveAccumTime2);
                           this.cursorSate = 6;
                        }

                        if (this.cursorPosition[0] >= Com.cameraScreen.getCw() - 16) {
                           camera = Com.cameraBattle;
                           camera.setCx(camera.getCx() + cameraMoveAccumTime2);
                           this.cursorSate = 7;
                        }

                        if (this.cursorPosition[1] <= 16) {
                           camera = Com.cameraBattle;
                           camera.setCy(camera.getCy() - cameraMoveAccumTime2);
                           this.cursorSate = this.cursorSate == 7 ? 11 : (this.cursorSate == 6 ? 10 : 8);
                        }

                        if (this.cursorPosition[1] >= Com.cameraScreen.getCh() - 16) {
                           camera = Com.cameraBattle;
                           camera.setCy(camera.getCy() + cameraMoveAccumTime2);
                           this.cursorSate = this.cursorSate == 7 ? 13 : (this.cursorSate == 6 ? 12 : 9);
                        }
                     }

                     if (this.cursorWorldPosition != null && this.cursorSate == 0) {
                        this.analizeCursorStateByType();
                     }
               }

               BattleUnit battleUnit;
               if (this.centerBattle) {
                  this.centerBattle = false;
                  if (BattleManager.getCurrentBattle() != null && (battleUnit = BattleManager.getCurrentBattle().getPlayerAvatarUnit()) != null) {
                     centerCamera(battleUnit.cx, battleUnit.cy);
                  }
               }

               funcStep = 3000;
               BattleSkillCircle battleSkillCircle;
               BattleUnit battleUnit2;
               BattleUnit battleUnit3;
               BattleUnit battleUnit4;
               BattleBottle battleBottle;
               BattleSkillCircle battleSkillCircle2;
               if (this.overSlot >= 0 && Mystery.currentTimeMillis - this.overSlotStartTime >= 200L) {
                  if (this.overSlot == 0) {
                     if ((battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                        battleSkillCircle = new BattleSkillCircle(0, battleUnit.cx, battleUnit.cy, (short)((int)Battle.percentLogic((long)battleUnit.params.hitRangePix, battleUnit.changeRange)), battleUnit.hisInd, battleUnit.side, true, false, false);
                        this.skillCircles.addElement(battleSkillCircle);
                     }
                  } else if (this.overSlot == BattleManager.getCurrentBattle().getPlayerMember().maxSlotInd + 2) {
                     if (Battle.c_rage_distance > 0 && (battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                        battleSkillCircle = new BattleSkillCircle(0, battleUnit.cx, battleUnit.cy, (short)Battle.c_rage_distance, battleUnit.hisInd, battleUnit.side, true, false, false);
                        this.skillCircles.addElement(battleSkillCircle);
                     }
                  } else {
                     int fastTargetIndexForBottle;
                     if ((battleBottle = (BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)(this.overSlot - 1)))) != null && battleBottle.effect.range > 0 && (fastTargetIndexForBottle = this.getFastTargetIndexForBottle(battleBottle)) >= 0) {
                        battleUnit2 = BattleManager.getCurrentBattleUnit(fastTargetIndexForBottle);
                        battleUnit3 = BattleManager.getCurrentBattlePlayerAvatar();
                        if (battleUnit2 != null && battleUnit3 != null) {
                           BattleSkillCircle battleSkillCircle3 = new BattleSkillCircle(!battleBottle.effect.appSelf && !battleBottle.effect.appFriends ? 0 : 1, battleUnit2.cx, battleUnit2.cy, (short)battleBottle.effect.range, battleUnit3.hisInd, battleUnit3.side, battleBottle.effect.enemys, battleBottle.effect.friends, battleBottle.effect.self);
                           this.skillCircles.addElement(battleSkillCircle3);
                        }
                     }

                     if (battleBottle != null && battleBottle.effect.distance > 0 && (battleUnit4 = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                        battleSkillCircle2 = new BattleSkillCircle(2, battleUnit4.cx, battleUnit4.cy, (short)battleBottle.effect.distance, battleUnit4.hisInd, battleUnit4.side, false, false, false);
                        this.skillCircles.addElement(battleSkillCircle2);
                     }
                  }
               }

               switch (this.cursorType) {
                  case 9:
                     if (Battle.c_rage_distance > 0 && (battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                        battleSkillCircle = new BattleSkillCircle(2, battleUnit.cx, battleUnit.cy, (short)Battle.c_rage_distance, battleUnit.hisInd, battleUnit.side, false, false, false);
                        this.skillCircles.addElement(battleSkillCircle);
                     }
                     break;
                  case 10:
                  case 11:
                     if ((battleBottle = (BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)this.choosedBottle))) != null && battleBottle.effect.range > 0 && (Com.touchControlType != 4 || this.activePressInTouch && Mystery.currentTimeMillis - this.activePressInTouchSinceTime >= 200L)) {
                        if (currentChoosedUnit >= 0) {
                           battleUnit4 = BattleManager.getCurrentBattleUnit(currentChoosedUnit);
                           battleUnit2 = BattleManager.getCurrentBattlePlayerAvatar();
                           if (battleUnit4 != null && battleUnit2 != null) {
                              BattleSkillCircle battleSkillCircle4 = new BattleSkillCircle(!battleBottle.effect.appSelf && !battleBottle.effect.appFriends ? 0 : 1, battleUnit4.cx, battleUnit4.cy, (short)battleBottle.effect.range, battleUnit2.hisInd, battleUnit2.side, battleBottle.effect.enemys, battleBottle.effect.friends, battleBottle.effect.self);
                              this.skillCircles.addElement(battleSkillCircle4);
                           }
                        } else if (this.cursorType == 11 && (battleUnit4 = BattleManager.getCurrentBattlePlayerAvatar()) != null && this.cursorWorldPosition != null) {
                           battleSkillCircle2 = new BattleSkillCircle(!battleBottle.effect.appSelf && !battleBottle.effect.appFriends ? 0 : 1, (short)this.cursorWorldPosition[0], (short)this.cursorWorldPosition[1], (short)battleBottle.effect.range, battleUnit4.hisInd, battleUnit4.side, battleBottle.effect.enemys, battleBottle.effect.friends, battleBottle.effect.self);
                           this.skillCircles.addElement(battleSkillCircle2);
                        }
                     }

                     if (battleBottle != null && battleBottle.effect.distance > 0 && (battleUnit4 = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                        battleSkillCircle2 = new BattleSkillCircle(2, battleUnit4.cx, battleUnit4.cy, (short)battleBottle.effect.distance, battleUnit4.hisInd, battleUnit4.side, false, false, false);
                        this.skillCircles.addElement(battleSkillCircle2);
                     }
               }

               this.topTextDraw = false;
               boolean showTargetHighlight = false;
               String messageText = "";
               switch (this.cursorType) {
                  case 4:
                  case 9:
                  case 10:
                  case 11:
                     messageText = "Укажи цель\n";
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                  default:
                     this.uiHealthEnemyMax = -1L;
                     if (this.overSlot >= 0) {
                        if (this.overSlot == 0) {
                           this.showTopText(messageText + "Выбери цель атаки");
                           showTargetHighlight = true;
                        } else if (this.overSlot == BattleManager.getCurrentBattle().getPlayerMember().maxSlotInd + 2) {
                           long rageDamage;
                           long rageDamageMaximum = (rageDamage = calculateRageDamage(BattleManager.getCurrentBattle().getPlayerMemberSmall(), BattleManager.getCurrentBattlePlayerAvatar())) * (long)Battle.c_battle_action_random_min / 1000L;
                           long c_battle_action_random_max = rageDamage * (long)Battle.c_battle_action_random_max / 1000L;
                           this.showTopText(messageText + "Ярость " + (rageDamageMaximum == c_battle_action_random_max ? String.valueOf(rageDamageMaximum) : rageDamageMaximum + "ə" + c_battle_action_random_max) + "ɲɳ");
                           showTargetHighlight = true;
                        } else {
                           BattleBottle battleBottle2;
                           if ((battleBottle2 = (BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)(this.overSlot - 1)))) != null) {
                              this.showTopText(messageText + battleBottle2.descr);
                              showTargetHighlight = true;
                           } else {
                              this.showTopText(messageText + "Пустой слот");
                              showTargetHighlight = true;
                           }
                        }
                     }

                     label274: {
                        if (currentChoosedUnit >= 0) {
                           battleUnit2 = BattleManager.getCurrentBattlePlayerAvatar();
                           if ((battleUnit3 = BattleManager.getCurrentBattle().getUnit(currentChoosedUnit)) != null) {
                              this.uiHealthEnemyVal = battleUnit3.CurCount;
                              this.uiHealthEnemyMax = battleUnit3.curHealthMaxCount;
                              if (battleUnit2 == null) {
                                 battleUnit2 = battleUnit3;
                              }

                              if (battleUnit3.HigherLegi) {
                                 this.showTopText(messageText + "ɮɯ" + Battle.percentLogic((long)battleUnit3.params.atackValue, battleUnit3.changeAtack) * 100L / Battle.percentLogic((long)battleUnit2.params.atackValue, battleUnit2.changeAtack) + "% ɰɱ" + Battle.percentLogic((long)battleUnit3.params.defenceValue, battleUnit3.changeDefence) * 100L / Battle.percentLogic((long)battleUnit2.params.defenceValue, battleUnit2.changeDefence) + "% (" + battleUnit3.Level + ")" + battleUnit3.Name);
                              } else {
                                 this.showTopText(messageText + battleUnit3.Name);
                              }
                              break label274;
                           }
                        } else if ((battleUnit2 = BattleManager.getCurrentBattlePlayerAvatar()) != null && (battleUnit3 = BattleManager.getCurrentBattle().getUnit(battleUnit2.ai_targetAtackUnit)) != null) {
                           this.uiHealthEnemyVal = battleUnit3.CurCount;
                           this.uiHealthEnemyMax = battleUnit3.curHealthMaxCount;
                        }

                        if (!showTargetHighlight) {
                           switch (this.cursorType) {
                              case 4:
                              case 9:
                              case 10:
                              case 11:
                                 this.showTopText("Укажи цель");
                              case 5:
                              case 6:
                              case 7:
                              case 8:
                           }
                        }
                     }

                     if (BattleManager.getCurrentBattle().hasUnDoneAct) {
                        BattleUnit battleUnit5;
                        if ((battleUnit5 = BattleManager.getCurrentBattle().getUnit(BattleManager.getCurrentBattle().unDoneActTarget)) != null && battleUnit5.Dead) {
                           BattleManager.getCurrentBattle().drawUnDoneAct = -1;
                           BattleManager.getCurrentBattle().hasUnDoneAct = false;
                        }

                        if (BattleManager.getCurrentBattle().hasUnDoneActSendNum >= 3) {
                           BattleManager.getCurrentBattle().drawUnDoneAct = -1;
                           BattleManager.getCurrentBattle().hasUnDoneAct = false;
                        }
                     }

                     funcStep = 1000;
                     DrawOrderList drawOrderList2 = new DrawOrderList();
                     if (BattleManager.getCurrentBattle() != null) {
                        showTargetHighlight = false;
                        boolean highlightFriends = false;
                        boolean highlightEnemies = false;
                        switch (this.cursorType) {
                           case 4:
                           case 9:
                              highlightEnemies = true;
                              break;
                           case 10:
                              highlightEnemies = this.chooseModeEnemys;
                              highlightFriends = this.chooseModeFriends;
                              showTargetHighlight = this.chooseModeSelf;
                        }

                        BattleManager.getCurrentBattle().iterateGraphicUnits(Com.cameraBattle, this.skillCircles, drawOrderList2, showTargetHighlight, highlightFriends, highlightEnemies, this.offscreenIcons);
                     }

                     Com.currentBattleMap.fillDecorsDrawOrder(Com.cameraBattle, this.drawOrderList, drawOrderList2);
                     this.drawOrderList.mergeWith(drawOrderList2);
                     drawOrderList2.clear();
                     this.addSkillCirclesToDraw();
                     switch (Com.touchControlType) {
                        default:
                           if (this.activateCursorAfterAnalize) {
                              this.cursorWorldActivate();
                           }

                           this.activateCursorAfterAnalize = false;
                        case 4:
                           this.initUI();
                           if (this.firstFunc) {
                              this.firstFunc = false;
                              this.dragCursor();
                           }

                           GameCommons instance2 = GameCommons.instance;
                           GameCommons.animateCamera(Com.cameraBattle);
                     }
               }
         }
      }
   }

   private void analizeCursorStateByType() {
      int cursorWorldPosition2;
      int cursorWorldPosition3;
      boolean chooseEnemies;
      boolean chooseFriends;
      label38: {
         switch (this.cursorType) {
            case 1:
               this.cursorSate = 2;
               int currentChoosedUnit2;
               BattleUnit battleUnit;
               if ((currentChoosedUnit2 = findUnitAtPixel(this.cursorWorldPosition[0], this.cursorWorldPosition[1])) >= 0 && (battleUnit = BattleManager.getCurrentBattle().getUnit(currentChoosedUnit2)) != null && battleUnit.side != BattleManager.getCurrentBattle().getPlayerMember().side) {
                  this.cursorSate = 1;
               }

               currentChoosedUnit = currentChoosedUnit2;
               return;
            case 2:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
               return;
            case 3:
               this.cursorSate = 2;
               return;
            case 4:
               this.cursorSate = 1;
               cursorWorldPosition2 = this.cursorWorldPosition[0];
               cursorWorldPosition3 = this.cursorWorldPosition[1];
               chooseEnemies = true;
               break;
            case 9:
               this.cursorSate = 3;
               cursorWorldPosition2 = this.cursorWorldPosition[0];
               cursorWorldPosition3 = this.cursorWorldPosition[1];
               chooseEnemies = true;
               break;
            case 10:
            case 11:
               this.cursorSate = 4;
               cursorWorldPosition2 = this.cursorWorldPosition[0];
               cursorWorldPosition3 = this.cursorWorldPosition[1];
               chooseEnemies = this.chooseModeEnemys;
               if (this.chooseModeFriends || this.chooseModeSelf) {
                  chooseFriends = true;
                  break label38;
               }
         }

         chooseFriends = false;
      }

      currentChoosedUnit = findNearestTargetUnit(cursorWorldPosition2, cursorWorldPosition3, chooseEnemies, chooseFriends);
   }

   private void addSkillCirclesToDraw() {
      label33:
      for(int i = 0; i < this.skillCircles.size(); ++i) {
         BattleSkillCircle battleSkillCircle = (BattleSkillCircle)this.skillCircles.elementAt(i);
         DrawOrderList drawOrderList2 = this.drawOrderList;
         BattleSkillCircle battleSkillCircle2 = battleSkillCircle;
         DrawOrderList drawOrderList3 = drawOrderList2;
         if (drawOrderList2.first != null && !drawOrderList3.first.isGreater((DrawOrderElement)battleSkillCircle)) {
            battleSkillCircle.next = null;
            battleSkillCircle.prev = null;
            DrawOrderElement first2 = drawOrderList3.first;

            do {
               if ((first2 = first2.next) == null) {
                  drawOrderList3.addAndSortFromEnd((DrawOrderElement)battleSkillCircle2);
                  continue label33;
               }
            } while(!first2.isGreater((DrawOrderElement)battleSkillCircle2));

            DrawOrderElement prev2 = first2.prev;
            first2.prev = battleSkillCircle2;
            battleSkillCircle2.next = first2;
            battleSkillCircle2.prev = prev2;
            if (prev2 == null) {
               drawOrderList3.first = battleSkillCircle2;
            } else {
               prev2.next = battleSkillCircle2;
            }

            ++drawOrderList3.length;
         } else {
            drawOrderList3.addAndSortFromBegining(battleSkillCircle);
         }
      }

   }

   public final void paint(GameGraphics gameGraphics2, boolean justPaint) {
      if (workBattle) {
         if (ClientState.instance.get() == 7 || ClientState.instance.get() == 10) {
            if (this.fillBackground) {
               gameGraphics2.setColor(263172);
               gameGraphics2.fillRect(0, 0, Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
            }

            this.fillBackground = false;
            GameCommons.instance.paintInScreenCoord(gameGraphics2, Com.cameraBattle, Com.currentBattleMap);
            if (BattleManager.getCurrentBattle() != null) {
               if (BattleManager.getCurrentBattle().getPlayerMember() != null) {
                  gameGraphics2.translate(-gameGraphics2.getTranslateX() - Com.cameraBattle.getCx(), -gameGraphics2.getTranslateY() - Com.cameraBattle.getCy());
                  if (this.drawOrderList.first != null) {
                     DrawOrderElement first2 = this.drawOrderList.first;

                     do {
                        first2.draw(gameGraphics2);
                     } while((first2 = first2.next) != null);
                  }

                  BattleManager.getCurrentBattle().drawUnits(gameGraphics2);
                  BattleManager.getCurrentBattle().drawDistPunchs(gameGraphics2);
                  GameCommons instance2 = GameCommons.instance;
                  GameCommons.draw(gameGraphics2, Com.cameraBattle);
                  gameGraphics2.translate(Com.cameraBattle.getCx(), Com.cameraBattle.getCy());
                  GameCommons.instance.uiConteiner.paint(gameGraphics2);

                  for(int sizeIndex = 0; sizeIndex < this.offscreenIcons.size(); ++sizeIndex) {
                     BattleOffscreenIcon battleOffscreenIcon = (BattleOffscreenIcon)this.offscreenIcons.elementAt(sizeIndex);
                     gameGraphics2.drawImage(battleOffscreenIcon.getImage(), battleOffscreenIcon.getScreenX(), battleOffscreenIcon.getScreenY(), battleOffscreenIcon.getAnchor());
                  }

                  switch (Com.touchControlType) {
                     case 4:
                        return;
                     default:
                        GameImage gameImage;
                        switch (this.cursorSate) {
                           case 0:
                              gameImage = ImagesGlobal.Cursors[0];
                              break;
                           case 1:
                              gameImage = ImagesGlobal.Cursors[2];
                              break;
                           case 2:
                              gameImage = ImagesGlobal.Cursors[3];
                              break;
                           case 3:
                              gameImage = ImagesGlobal.Cursors[7];
                              break;
                           case 4:
                              gameImage = ImagesGlobal.Cursors[8];
                              break;
                           default:
                              gameImage = null;
                        }

                        GameImage gameImage2 = gameImage;
                        if (gameImage != null) {
                           gameGraphics2.drawImage(gameImage2, this.cursorPosition[0], this.cursorPosition[1], 0);
                        } else {
                           byte pointerTransform = 0;
                           int pointerOffsetY = 0;
                           int pointerOffsetX = 0;
                           switch (this.cursorSate) {
                              case 6:
                                 gameImage2 = ImagesGlobal.Cursors[9];
                                 pointerTransform = 6;
                                 pointerOffsetX = -gameImage2.getWidth() / 2;
                                 break;
                              case 7:
                                 gameImage2 = ImagesGlobal.Cursors[9];
                                 pointerTransform = 5;
                                 pointerOffsetY = -gameImage2.getHeight();
                                 pointerOffsetX = -gameImage2.getWidth() / 2;
                                 break;
                              case 8:
                                 gameImage2 = ImagesGlobal.Cursors[9];
                                 pointerTransform = 0;
                                 pointerOffsetY = -gameImage2.getWidth() / 2;
                                 break;
                              case 9:
                                 gameImage2 = ImagesGlobal.Cursors[9];
                                 pointerTransform = 3;
                                 pointerOffsetY = -gameImage2.getWidth() / 2;
                                 pointerOffsetX = -gameImage2.getHeight();
                                 break;
                              case 10:
                                 gameImage2 = ImagesGlobal.Cursors[10];
                                 pointerTransform = 6;
                                 break;
                              case 11:
                                 gameImage2 = ImagesGlobal.Cursors[10];
                                 pointerTransform = 0;
                                 pointerOffsetY = -gameImage2.getWidth();
                                 break;
                              case 12:
                                 gameImage2 = ImagesGlobal.Cursors[10];
                                 pointerTransform = 3;
                                 pointerOffsetX = -gameImage2.getHeight();
                                 break;
                              case 13:
                                 gameImage2 = ImagesGlobal.Cursors[10];
                                 pointerTransform = 5;
                                 pointerOffsetY = -gameImage2.getHeight();
                                 pointerOffsetX = -gameImage2.getWidth();
                           }

                           if (gameImage2 != null) {
                              gameGraphics2.drawRegion(gameImage2, 0, 0, gameImage2.getWidth(), gameImage2.getHeight(), pointerTransform, this.cursorPosition[0] + pointerOffsetY, this.cursorPosition[1] + pointerOffsetX);
                           }

                        }
                  }
               }
            }
         }
      }
   }

   public final void onAfterHardLoad() {
      if (Com.touchControlType != 4) {
         Com.showDefaultCursor();
      } else {
         Com.showGameCursor();
      }
   }

   public final void pressStar() {
      if (BattleManager.getCurrentBattle() != null) {
         BattleUnit[] units = BattleManager.getPlayerUnits();

         for(int i = 0; i < units.length; ++i) {
            BattleUnit battleUnit;
            if ((battleUnit = units[i]).playersAvatar) {
               centerCamera(battleUnit.cx, battleUnit.cy);
               this.setCursorPosition(battleUnit.cx - Com.cameraBattle.getCx(), battleUnit.cy - Com.cameraBattle.getCy());
               this.dragCursor();
               return;
            }
         }

      }
   }

   private static int getPlayerArmyValue(boolean friends, boolean self) {
      int unitCount = 0;
      int playerArmyValue = 0;
      BattleUnit[] currentBattleUnits = BattleManager.getCurrentBattleUnits();
      int side2 = BattleManager.getCurrentBattle().getPlayerMember().side;
      int id2 = BattleManager.getCurrentBattle().getPlayerMember().id;

      for(int lengthIndex = 0; lengthIndex < currentBattleUnits.length; ++lengthIndex) {
         BattleUnit battleUnit;
         if ((battleUnit = currentBattleUnits[lengthIndex]).side == side2 && !battleUnit.Dead && (self && battleUnit.hisBattleMemberId == id2 && battleUnit.playersAvatar || friends && (battleUnit.hisBattleMemberId != id2 || !battleUnit.playersAvatar))) {
            playerArmyValue = battleUnit.hisInd;
            ++unitCount;
         }
      }

      if (unitCount == 1) {
         return playerArmyValue;
      } else if (unitCount == 0) {
         return -2;
      } else {
         return -1;
      }
   }

   private static int getEnemyArmyValue() {
      int unitCount = 0;
      int enemyArmyValue = 0;
      BattleUnit[] currentBattleUnits = BattleManager.getCurrentBattleUnits();
      int enemySide = Battle.ENEMY_SIDE_FAST[BattleManager.getCurrentBattle().getPlayerMember().side];

      for(int lengthIndex = 0; lengthIndex < currentBattleUnits.length; ++lengthIndex) {
         BattleUnit battleUnit;
         if ((battleUnit = currentBattleUnits[lengthIndex]).side == enemySide && !battleUnit.Dead) {
            enemyArmyValue = battleUnit.hisInd;
            ++unitCount;
         }
      }

      if (unitCount == 1) {
         return enemyArmyValue;
      } else if (unitCount == 0) {
         return -2;
      } else {
         return -1;
      }
   }

   private static void issuePlayerOrder(int targInd, byte OrderType, int bushInd, int groundX, int groundY) {
      if (!BattleManager.getCurrentBattle().hasUnDoneAct) {
         BattleUnit[] playerUnits = BattleManager.getPlayerUnits();

         for(int lengthIndex = 0; lengthIndex < playerUnits.length; ++lengthIndex) {
            BattleUnit battleUnit;
            if ((battleUnit = playerUnits[lengthIndex]).playersAvatar) {
               BattleManager.getCurrentBattle().hasUnDoneAct = true;
               BattleManager.getCurrentBattle().hasUnDoneActSendNum = 0;
               BattleManager.getCurrentBattle().unDoneActTarget = targInd;
               if (OrderType == 2) {
                  BattleManager.getCurrentBattle().drawUnDoneAct = ((BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)bushInd))).effect.bushIndex;
               } else if (OrderType == 1) {
                  BattleManager.getCurrentBattle().drawUnDoneAct = 9;
               }

               BattleManager.getCurrentBattle().giveOrder(new BattleOrder(battleUnit.hisInd, targInd, groundX, groundY, OrderType, bushInd, 0));
            }
         }

      }
   }

   private static void issueMoveOrder(int toX, int toY) {
      BattleUnit[] playerUnits = BattleManager.getPlayerUnits();

      for(int lengthIndex = 0; lengthIndex < playerUnits.length; ++lengthIndex) {
         BattleUnit battleUnit;
         if ((battleUnit = playerUnits[lengthIndex]).playersAvatar) {
            BattleManager.getCurrentBattle().giveOrder(new BattleOrder(battleUnit.hisInd, -1, toX, toY, (byte)0, 0, 0));
         }
      }

   }

   private void tryActivateAtack() {
      if (this.cursorType == 4) {
         this.setCursor(1);
      } else {
         this.setCursor(4);
         BattleUnit battleUnit;
         if ((battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
            smoothMoveCamera(battleUnit.cx, battleUnit.cy);
         }

      }
   }

   private boolean tryActivateRage() {
      if (this.cursorType == 9) {
         this.setCursor(1);
         return false;
      } else if (BattleManager.getCurrentBattle().getPlayerMemberSmall().getRageLevel() > 0) {
         int ind;
         if ((ind = getEnemyArmyValue()) >= 0) {
            ind = -1;
         }

         if (ind == -3) {
            return true;
         } else if (ind != -1) {
            issuePlayerOrder(ind, (byte)1, 0, -1, -1);
            return true;
         } else {
            Com.battleTh.setCursor(9);
            return true;
         }
      } else {
         GameCommons.instance.showFloatText((String)"Мало ярости!", 300);
         return false;
      }
   }

   private void tryChooseBottle(int ind) {
      try {
         this.bChooPhase = 10;
         if (BattleManager.getCurrentBattle() != null) {
            this.bChooPhase = 20;
            if (BattleManager.getCurrentBattle().getPlayerMember() != null) {
               this.bChooPhase = 30;
               if (ind <= BattleManager.getCurrentBattle().getPlayerMember().maxSlotInd) {
                  this.bChooPhase = 40;
                  if ((this.cursorType == 10 || this.cursorType == 11) && this.choosedBottle == ind) {
                     this.bChooPhase = 50;
                     this.setCursor(1);
                     return;
                  }

                  this.bChooPhase = 60;
                  BattleBottle battleBottle = (BattleBottle)BattleManager.getCurrentBattle().getPlayerMember().bottlesBySlot.get(new Byte((byte)ind));
                  this.bChooPhase = 70;
                  if (battleBottle == null) {
                     return;
                  }

                  this.bChooPhase = 90;
                  this.chooseBottle(ind);
                  this.bChooPhase = 100;
               }

            }
         }
      } catch (Exception exception) {
         Com.SendMistace("Error in choose bottle " + this.shooBottlePhase + " -- " + this.bChooPhase + " -- " + this.shooBottlePhaseInfo, true, true, exception);
      }
   }

   public final void pressSharp() {
      boolean unusedProgressMarker;
      if (BattleManager.getCurrentBattle() == null) {
         unusedProgressMarker = false;
      } else if (BattleManager.getCurrentBattle().getPlayerMember() == null) {
         unusedProgressMarker = false;
      } else {
         BattleMember battleMember = BattleManager.getCurrentBattle().getPlayerMember();
         if (BattleManager.getCurrentBattle().getPlayerMemberSmall().getRageLevel() > 0) {
            BattleUnit battleUnit;
            if ((battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null && (battleUnit = BattleManager.getCurrentBattleUnit(battleUnit.ai_targetAtackUnit)) != null) {
               issuePlayerOrder(battleUnit.hisInd, (byte)1, 0, -1, -1);
               unusedProgressMarker = true;
            } else if (currentChoosedUnit >= 0 && BattleManager.getCurrentBattle().getUnit(currentChoosedUnit) != null && BattleManager.getCurrentBattle().getUnit(currentChoosedUnit).side != battleMember.side) {
               issuePlayerOrder(currentChoosedUnit, (byte)1, 0, -1, -1);
               unusedProgressMarker = true;
            } else {
               int hisInd = -1;
               int mAX_VALUE = Integer.MAX_VALUE;
               int enemySide = Battle.ENEMY_SIDE_FAST[battleMember.side];
               int targetX;
               int targetY;
               if (this.cursorWorldPosition == null) {
                  BattleUnit battleUnit2;
                  if ((battleUnit2 = BattleManager.getCurrentBattlePlayerAvatar()) == null) {
                     unusedProgressMarker = false;
                     return;
                  }

                  targetX = battleUnit2.cx;
                  targetY = battleUnit2.cy;
               } else {
                  targetX = this.cursorWorldPosition[0];
                  targetY = this.cursorWorldPosition[1];
               }

               BattleUnit[] currentBattleUnits = BattleManager.getCurrentBattleUnits();

               for(int lengthIndex = 0; lengthIndex < currentBattleUnits.length; ++lengthIndex) {
                  int deltaX;
                  BattleUnit battleUnit3;
                  if ((battleUnit3 = currentBattleUnits[lengthIndex]).side == enemySide && !battleUnit3.Dead && (deltaX = (battleUnit3.cx - targetX) * (battleUnit3.cx - targetX) + (battleUnit3.cy - targetY) * (battleUnit3.cy - targetY)) < mAX_VALUE) {
                     mAX_VALUE = deltaX;
                     hisInd = battleUnit3.hisInd;
                  }
               }

               if (hisInd < 0) {
                  unusedProgressMarker = false;
               } else {
                  issuePlayerOrder(hisInd, (byte)1, 0, -1, -1);
                  unusedProgressMarker = true;
               }
            }
         } else {
            GameCommons.instance.showFloatText((String)"Мало ярости!", 300);
            unusedProgressMarker = false;
         }
      }

   }

   public final void press1() {
      this.pressedSlot = 1;
      this.wasPress1 = true;
      this.trySetOverSlotValue(1);
   }

   public final void press3() {
      this.pressedSlot = 2;
      this.wasPress3 = true;
      this.trySetOverSlotValue(2);
   }

   public final void press7() {
      this.pressedSlot = 3;
      this.wasPress7 = true;
      this.trySetOverSlotValue(3);
   }

   public final void press9() {
      this.pressedSlot = 4;
      this.wasPress9 = true;
      this.trySetOverSlotValue(4);
   }

   public final void pressLeftSoft() {
      this.pressedSlot = 0;
      this.wasPressLeftSoft = true;
      this.trySetOverSlotValue(0);
   }

   public final void pressRightSoft() {
      this.wasPressRSoft = true;
   }

   public final void releaseLeftSoft() {
      if (this.pressedSlot == 0) {
         this.pressedSlot = -1;
      }

      this.trySetOverSlotValue(-1);
      if (this.wasPressLeftSoft) {
         this.wasPressLeftSoft = false;
         this.tryActivateAtack();
      }

   }

   public final void releaseRightSoft() {
      if (this.wasPressRSoft) {
         this.tryActivateRage();
      }

      this.wasPressRSoft = false;
   }

   public final void press0() {
      Battle battle2;
      if ((battle2 = BattleManager.getCurrentBattle()) == null || !battle2.isWasEnd()) {
         if (Strategist.instance.partyState != 2) {
            Chat.instance.setUnreadnParty();
         }

      }
   }

   public final void release1() {
      if (this.pressedSlot == 1) {
         this.pressedSlot = -1;
      }

      this.trySetOverSlotValue(-1);
      if (this.wasPress1) {
         this.wasPress1 = false;
         this.tryChooseBottle(0);
      }

   }

   public final void release3() {
      if (this.pressedSlot == 2) {
         this.pressedSlot = -1;
      }

      this.trySetOverSlotValue(-1);
      if (this.wasPress3) {
         this.wasPress3 = false;
         this.tryChooseBottle(1);
      }

   }

   public final void release7() {
      if (this.pressedSlot == 3) {
         this.pressedSlot = -1;
      }

      this.trySetOverSlotValue(-1);
      if (this.wasPress7) {
         this.wasPress7 = false;
         this.tryChooseBottle(2);
      }

   }

   public final void release9() {
      if (this.pressedSlot == 4) {
         this.pressedSlot = -1;
      }

      this.trySetOverSlotValue(-1);
      if (this.wasPress9) {
         this.wasPress9 = false;
         this.tryActivateRage();
      }

   }

   public final void releaseAllEvent() {
      this.wasPress = false;
      this.wasPress1 = false;
      this.wasPress3 = false;
      this.wasPress7 = false;
      this.wasPress9 = false;
      this.wasPressLeftSoft = false;

      for(int i = 0; i < this.cursorMoveDirections.length; ++i) {
         this.cursorMoveDirections[i] = false;
      }

      this.wasPressRSoft = false;
   }

   public final boolean isReplaceable() {
      return true;
   }

   public final void wheelRot(int num) {
   }

   public final void pressPointer(int x, int y) {
      this.wasPress = true;
      switch (Com.touchControlType) {
         case 4:
            if (GameCommons.instance.uiConteiner.pressPointer(x, y)) {
               return;
            }
            break;
         default:
            this.setCursorPosition(x, y);
            this.pressCursor();
      }

   }

   public final void movePointer(int x, int y) {
      switch (Com.touchControlType) {
         case 4:
            return;
         default:
            this.setCursorPosition(x, y);
            this.dragCursor();
      }
   }

   public final void dragPointer(int x, int y) {
      switch (Com.touchControlType) {
         case 4:
            GameCommons.instance.uiConteiner.dragPointer(x, y);
            return;
         default:
            this.setCursorPosition(x, y);
            this.dragCursor();
      }
   }

   public final void releasePointer(int x, int y) {
      if (this.wasPress) {
         this.wasPress = false;
         switch (Com.touchControlType) {
            case 4:
               GameCommons.instance.uiConteiner.releasPointer(x, y);
               this.trySetOverSlotValue(-2);
               return;
            default:
               this.setCursorPosition(x, y);
               this.releaseCursor();
         }
      }
   }

   private static long calculateRageDamage(BattleMemberSmall battleMemberSmall, BattleUnit battleUnit) {
      if (battleMemberSmall != null && battleUnit != null) {
         int rageLevel = battleMemberSmall.getRageLevel();

         long rageDamage;
         try {
            rageDamage = Battle.percentLogic((long)battleUnit.params.hitPower0P, battleUnit.changeDamage) * (long)Battle.c_rage_Coofs_int[rageLevel] * (long)(10000 + battleMemberSmall.gpEffects[11]) / 10000L / 1000L;
         } catch (Exception exception) {
            rageDamage = 0L;
         }

         return rageDamage;
      } else {
         return 0L;
      }
   }

   private static String getC_player_block_timeout() {
      Battle battle2;
      long battleTime = (battle2 = BattleManager.getCurrentBattle()) == null ? 0L : (long)battle2.getBattleTime();
      return String.valueOf(((long)(Com.c_player_block_timeout * 1000) - battleTime) / 1000L);
   }

   private String getMountLatencyAddText() {
      return this.uiLatencyVal >= 8000L ? "ʂʃ" : "";
   }

   private void initUI() {
      this.uiHealthMyMax = -1L;
      BattleUnit battleUnit;
      if ((battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
         this.uiHealthMyVal = battleUnit.CurCount;
         this.uiHealthMyMax = battleUnit.curHealthMaxCount;
      }

      BattleMember battleMember = BattleManager.getCurrentBattlePlayerMember();
      BattleMemberSmall battleMemberSmall = BattleManager.getCurrentBattlePlayerMemberSmall();
      this.uiLatencyVal = Mystery.currentTimeMillis - Com.lagTimeLast;
      if (this.uiLatencyVal < 8000L) {
         this.uiLatencyVal = Com.lagShowTime;
      }

      int topWindowState2 = this.topWindowState;
      if (this.topTextDraw) {
         if (GameCommons.instance.guiAnimate) {
            switch (this.topWindowState) {
               case 0:
               case 2:
                  this.topWindowState = 3;
            }
         } else {
            this.topWindowState = 1;
         }
      } else if (GameCommons.instance.guiAnimate) {
         switch (this.topWindowState) {
            case 1:
            case 3:
               this.topWindowState = 2;
         }
      } else {
         this.topWindowState = 0;
      }

      this.uiRunIcon.enabled = true;
      this.uiFriendsIcons.enabled = battleMember != null;
      this.uiEnemysIcons.enabled = battleMember != null;
      this.uiChatIcon.enabled = Strategist.instance.partyState != 2;
      this.uiChatText.enabled = this.chatTextDraw;
      this.uiTopWindow.enabled = this.topWindowState != 0;
      this.uiHealthBarEnemy.enabled = this.uiHealthEnemyMax > 0L;
      this.uiHealthBarMy.enabled = this.uiHealthMyMax > 0L;
      this.uiMountLatency.enabled = true;
      this.uiMountTimer.enabled = true;
      this.uiOverlay.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 4, 0, 0, Com.cameraBattle.getCw(), 0, Com.cameraBattle.getCh(), true);
      int textWidth = Com.fontDigitGame.getTextWidth(String.valueOf(this.uiLatencyVal));
      int textWidth2 = Com.fontBig.getTextWidth(this.getMountLatencyAddText());
      this.uiMountLatency.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, 4, 0, textWidth + (textWidth2 > 0 ? 1 : 0) + 2 + textWidth2, 0, Com.fontDigitGame.fontHeight, true);
      if (this.uiRunIcon.enabled) {
         this.uiRunIcon.setPosition(this.uiScreenWidth, this.uiScreenHeight, 2, 0, 8, this.uiMountLatency.y + this.uiMountLatency.height + 4, 0, 35, 0, 30, true);
      }

      textWidth = Com.fontDigitGame.getTextWidth(getC_player_block_timeout());
      textWidth2 = Com.fontBig.getTextWidth("ˤ˥");
      UIComponent uIComponent15 = this.uiMountTimer.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, 4, 0, textWidth + 1 + 2 + textWidth2, 0, Com.fontDigitGame.fontHeight, true);
      if (this.uiChatIcon.enabled) {
         this.uiChatIcon.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, 0, 8, this.uiMountTimer.y + this.uiMountTimer.height + 4, 0, 35, 0, 25, true);
      }

      if (this.uiChatText.enabled) {
         this.uiChatText.setPosition(this.uiScreenWidth, this.uiScreenHeight, 6, (this.uiChatIcon.enabled ? this.uiChatIcon.xByPosition + this.uiChatIcon.widthContentByPosition : 0) + 10, 8, this.uiMountTimer.y + this.uiMountTimer.height + 4 + 3, 0, this.chatTextMyst.getMaxWidth(), 0, this.chatTextMyst.getMaxHeight(Com.fontBig), true);
      }

      textWidth2 = this.uiScreenWidth / this.uiSlots.length;
      int slotHeight = getSlotHeight();
      int uiSlotsCount = (this.uiScreenWidth - textWidth2 * this.uiSlots.length) / 2;
      int softkeyHeight = GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 4;

      int uiSlotIndex;
      for(uiSlotIndex = 0; uiSlotIndex < this.uiSlots.length; ++uiSlotIndex) {
         uIComponent15 = this.uiSlots[uiSlotIndex].setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, uiSlotsCount, 9, softkeyHeight, 1, textWidth2, 0, slotHeight, true);
         this.uiSlots[uiSlotIndex].overBackGroundColor = 3214603;
         this.uiSlots[uiSlotIndex].setDecorType(8);
         uiSlotsCount += textWidth2;
      }

      if (this.uiHealthBarMy.enabled) {
         this.uiHealthBarMy.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 5, this.uiScreenHeight - uIComponent15.y + 2, 0, 127, 0, 10, true);
      }

      if (this.uiFriendsIcons.enabled && battleMember != null) {
         if (battleMember.avatarFriendsCount > 3) {
            uiSlotIndex = 1;
            textWidth = battleMember.avatarFriendsCount;
         } else {
            uiSlotIndex = battleMember.avatarFriendsCount;
            textWidth = 0;
         }

         if ((textWidth2 = uiSlotIndex * (ImagesGlobal.iconBattleFriends.getWidth() + 1)) > 0) {
            --textWidth2;
         }

         if (textWidth > 0) {
            textWidth2 += 1 + Com.fontGreen.getTextWidth("x" + textWidth);
         }

         this.uiFriendsIcons.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, this.uiHealthBarMy.xByPosition - textWidth2 - 1, 4, this.uiHealthBarMy.yByPosition + (this.uiHealthBarMy.height - ImagesGlobal.iconBattleFriends.getHeight()) / 2, 0, textWidth2, 0, ImagesGlobal.iconBattleFriends.getHeight(), true);
      }

      if (this.uiEnemysIcons.enabled && battleMember != null) {
         if (battleMember.avatarEnemysCount > 3) {
            uiSlotIndex = 1;
            textWidth = battleMember.avatarEnemysCount;
         } else {
            uiSlotIndex = battleMember.avatarEnemysCount;
            textWidth = 0;
         }

         if ((textWidth2 = uiSlotIndex * (ImagesGlobal.iconBattleEnemys.getWidth() + 1)) > 0) {
            --textWidth2;
         }

         if (textWidth > 0) {
            textWidth2 += 1 + Com.fontRed.getTextWidth("x" + textWidth);
         }

         this.uiEnemysIcons.setPosition(this.uiScreenWidth, this.uiScreenHeight, 0, this.uiHealthBarMy.xByPosition + this.uiHealthBarMy.width + 1, 4, this.uiHealthBarMy.yByPosition + (this.uiHealthBarMy.height - ImagesGlobal.iconBattleEnemys.getHeight()) / 2, 0, textWidth2, 0, ImagesGlobal.iconBattleEnemys.getHeight(), true);
      }

      uIComponent15 = null;
      if (this.uiTopWindow.enabled) {
         uIComponent15 = this.uiTopWindow.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 8, 4, 0, this.topTextMyst.getMaxWidth() + 2, 0, this.topTextMyst.getMaxHeight(Com.fontBig) + 2, false, true, this.topWindowState == 3 || this.topWindowState == 2, this.topWindowState == 3 || this.topWindowState == 2, 0L, 140L, true);
         if (GameCommons.instance.guiAnimate) {
            switch (this.topWindowState) {
               case 2:
                  if (topWindowState2 == 2) {
                     if (this.uiTopWindow.animateY.needValue != -this.uiTopWindow.height) {
                        this.uiTopWindow.clearAnimationY();
                        this.uiTopWindow.startAnimationY(this.uiTopWindow.y, -this.uiTopWindow.height, Math.max(0L, 140L - this.uiTopWindow.animateY.accumTime));
                     }
                  } else if (topWindowState2 != this.topWindowState) {
                     this.uiTopWindow.clearAnimationY();
                     this.uiTopWindow.startAnimationY(this.uiTopWindow.y, -this.uiTopWindow.height, 140L);
                  }
                  break;
               case 3:
                  if (topWindowState2 == 3) {
                     if (this.uiTopWindow.animateY.needValue != this.uiTopWindow.yByPosition) {
                        this.uiTopWindow.clearAnimationY();
                        this.uiTopWindow.startAnimationY(this.uiTopWindow.y, this.uiTopWindow.yByPosition, Math.max(0L, 140L - this.uiTopWindow.animateY.accumTime));
                     }
                  } else if (topWindowState2 != this.topWindowState) {
                     this.uiTopWindow.clearAnimationY();
                     this.uiTopWindow.startAnimationY(this.uiTopWindow.y, this.uiTopWindow.yByPosition, 140L);
                  }
            }
         }
      }

      if (this.uiHealthBarEnemy.enabled) {
         this.uiHealthBarEnemy.setPosition(this.uiScreenWidth, this.uiScreenHeight, 3, 0, 4, uIComponent15 == null ? 4 : uIComponent15.y + uIComponent15.height + 2, 0, 127, 0, 10, true);
      }

      if (this.cursorType == 4) {
         this.uiSlots[0].overBackGroundColor = -1;
         this.uiSlots[0].setDecorType(10);
      } else {
         this.uiSlots[0].overBackGroundColor = 282884;
         this.uiSlots[0].setDecorType(8);
      }

      if (battleMember != null && battleMemberSmall != null) {
         if (battleMemberSmall.getRageLevel() == 0) {
            this.uiSlots[this.uiSlots.length - 1].overBackGroundColor = 3214603;
            this.uiSlots[this.uiSlots.length - 1].setDecorType(8);
         } else if (this.cursorType == 9) {
            this.uiSlots[this.uiSlots.length - 1].overBackGroundColor = -1;
            this.uiSlots[this.uiSlots.length - 1].setDecorType(10);
         } else {
            this.uiSlots[this.uiSlots.length - 1].overBackGroundColor = -1;
            this.uiSlots[this.uiSlots.length - 1].setDecorType(8);
         }

         for(textWidth = 1; textWidth < this.uiSlots.length - 1; ++textWidth) {
            textWidth2 = textWidth - 1;
            BattleBottle battleBottle = (BattleBottle)battleMember.bottlesBySlot.get(new Byte((byte)textWidth2));
            topWindowState2 = 0;
            if (battleBottle != null) {
               topWindowState2 = (this.cursorType == 10 || this.cursorType == 11) && this.choosedBottle == textWidth2 ? 2 : 1;
            }

            switch (topWindowState2) {
               case 0:
                  this.uiSlots[textWidth].overBackGroundColor = 3214603;
                  this.uiSlots[textWidth].setDecorType(8);
                  break;
               case 1:
                  this.uiSlots[textWidth].overBackGroundColor = -1;
                  if (battleBottle.cooldownTimeCur <= 0) {
                     this.uiSlots[textWidth].overBackGroundColor = 282884;
                  }

                  this.uiSlots[textWidth].setDecorType(8);
                  break;
               case 2:
                  this.uiSlots[textWidth].overBackGroundColor = -1;
                  this.uiSlots[textWidth].setDecorType(10);
            }
         }

         if (this.pressedSlot >= 0) {
            this.uiSlots[this.pressedSlot].setDecorType(11);
         }

      }
   }

   public static int getSlotHeight() {
      return Com.fontBig.fontHeight + (GameCommons.instance.guiLineSpacing << 1) + 2;
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 10001:
            GameCommons.paintSymbolOnMount(gameGraphics2, uIComponent15, this.getMountLatencyAddText(), 1, false);
            return;
         case 10003:
            GameCommons.paintComplexSymbolOnMount(uIComponent15, gameGraphics2, String.valueOf(this.uiHealthMyVal), "ʎʏ");
            return;
         case 10004:
            GameCommons.paintComplexSymbolOnMount(this.uiHealthBarEnemy, gameGraphics2, String.valueOf(this.uiHealthEnemyVal), "ʎʏ");
         case 10002:
         case 10005:
         case 10006:
         case 10007:
         default:
            return;
         case 10008:
            GameCommons.paintSymbolOnMount(gameGraphics2, uIComponent15, "ˤ˥", 1, false);
      }
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      int rageLevel;
      int textWidth;
      int textWidth2;
      BattleMember battleMember;
      int cooldownHeight;
      if (uIComponent15.id >= 11000) {
         if (uIComponent15.id == 11000) {
            gameGraphics2.drawImage(ImagesGlobal.iconAtack, (uIComponent15.widthContent - ImagesGlobal.iconAtack.getWidth()) / 2, (uIComponent15.heightContent - ImagesGlobal.iconAtack.getHeight()) / 2, 0);
         } else if (uIComponent15.id == 11000 + this.uiSlots.length - 1) {
            BattleMemberSmall battleMemberSmall;
            if ((battleMemberSmall = BattleManager.getCurrentBattle().getPlayerMemberSmall()) == null) {
               return;
            }

            rageLevel = battleMemberSmall.getRageLevel();
            gameGraphics2.setColor(Battle.c_rage_Colors[rageLevel]);
            int rageHeightNumerator = battleMemberSmall.rage * uIComponent15.heightContent;
            int[] c_rage_Levels2 = Battle.c_rage_Levels;
            textWidth = rageHeightNumerator / c_rage_Levels2[c_rage_Levels2.length - 1];
            gameGraphics2.fillRect(0, uIComponent15.heightContent - textWidth, uIComponent15.widthContent, textWidth);
            GameImage gameImage = ImagesGlobal.ragesIcons[rageLevel];
            gameGraphics2.drawImage(gameImage, (uIComponent15.widthContent - gameImage.getWidth()) / 2, (uIComponent15.heightContent - gameImage.getHeight()) / 2, 0);
            Com.fontDigitGameRed.writeText(gameGraphics2, "b", 0, 0);
         } else if (uIComponent15.id >= 11001 && uIComponent15.id <= 11000 + this.uiSlots.length - 2) {
            if ((battleMember = BattleManager.getCurrentBattle().getPlayerMember()) == null) {
               return;
            }

            rageLevel = uIComponent15.id - 11001;
            BattleBottle battleBottle;
            if ((battleBottle = (BattleBottle)battleMember.bottlesBySlot.get(new Byte((byte)rageLevel))) != null) {
               if (battleBottle.cooldownTimeCur > 0) {
                  gameGraphics2.setColor(3881787);
                  cooldownHeight = (battleBottle.cooldownTimeBase - battleBottle.cooldownTimeCur) * uIComponent15.heightContent / battleBottle.cooldownTimeBase;
                  gameGraphics2.fillRect(0, uIComponent15.heightContent - cooldownHeight, uIComponent15.widthContent, cooldownHeight);
               }

               String icon2 = battleBottle.icon;
               textWidth2 = Com.fontBig.getTextWidth(icon2);
               Com.fontBig.writeText(gameGraphics2, icon2, (uIComponent15.widthContent - textWidth2) / 2, (uIComponent15.heightContent - Com.fontBig.fontHeight) / 2);
               icon2 = String.valueOf(battleBottle.count);
               textWidth = Com.fontBig.getTextWidth(icon2);
               Com.fontDigitGame.writeText(gameGraphics2, icon2, uIComponent15.widthContent - textWidth, uIComponent15.heightContent - Com.fontDigitGame.fontHeight);
            }

            if (rageLevel < 3) {
               Com.fontDigitGameRed.writeText(gameGraphics2, HOT_KEYS[rageLevel], 0, 0);
            }
         }
      }

      switch (uIComponent15.id) {
         case 10001:
            if (this.uiLatencyVal >= 8000L) {
               Com.fontDigitGameRed.writeText(gameGraphics2, String.valueOf(this.uiLatencyVal), 1, 0);
               return;
            }

            Com.fontDigitGame.writeText(gameGraphics2, String.valueOf(this.uiLatencyVal), 1, 0);
            return;
         case 10002:
            this.topTextMyst.draw(gameGraphics2, 1, 1, uIComponent15.widthContent - 2, uIComponent15.heightContent - 2, 0, 0, Com.fontBig);
            return;
         case 10003:
            Com.drawLifeBar(gameGraphics2, 1, 1, uIComponent15.widthContent - 2, uIComponent15.heightContent - 2, this.uiHealthMyVal, this.uiHealthMyMax);
            return;
         case 10004:
            Com.drawLifeBar(gameGraphics2, 1, 1, uIComponent15.widthContent - 2, uIComponent15.heightContent - 2, this.uiHealthEnemyVal, this.uiHealthEnemyMax);
            return;
         case 10005:
            this.chatTextMyst.draw(gameGraphics2, 0, 0, uIComponent15.widthContent, uIComponent15.heightContent, 0, 1, Com.fontBig);
            return;
         case 10006:
         default:
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, "Атака", "Ярость!", "", Com.fontBig, GameCommons.instance.guiSoftSpacing);
            break;
         case 10007:
            gameGraphics2.drawImage(ImagesGlobal.uiSideArrow, 0, 0, 0);
            gameGraphics2.drawImage(ImagesGlobal.iconChatParty, (uIComponent15.width - ImagesGlobal.iconChatParty.getWidth()) / 2, (uIComponent15.height - ImagesGlobal.iconChatParty.getHeight()) / 2, 0);
            return;
         case 10008:
            Com.fontDigitGame.writeText(gameGraphics2, getC_player_block_timeout(), 1, 0);
            return;
         case 10009:
            if ((battleMember = BattleManager.getCurrentBattle().getPlayerMember()) != null) {
               if (battleMember.isAllowStepBack()) {
                  gameGraphics2.drawImage(ImagesGlobal.iconStepBack, (uIComponent15.widthContent - ImagesGlobal.iconStepBack.getWidth()) / 2, (uIComponent15.heightContent - ImagesGlobal.iconStepBack.getHeight()) / 2, 0);
                  return;
               }

               gameGraphics2.drawImage(ImagesGlobal.iconSurrender, (uIComponent15.widthContent - ImagesGlobal.iconSurrender.getWidth()) / 2, (uIComponent15.heightContent - ImagesGlobal.iconSurrender.getHeight()) / 2, 0);
               return;
            }
            break;
         case 10010:
            if ((battleMember = BattleManager.getCurrentBattlePlayerMember()) != null) {
               rageLevel = 0;
               if (battleMember.avatarFriendsCount > 3) {
                  textWidth = 1;
                  cooldownHeight = battleMember.avatarFriendsCount;
               } else {
                  textWidth = battleMember.avatarFriendsCount;
                  cooldownHeight = 0;
               }

               for(textWidth2 = 0; textWidth2 < textWidth; rageLevel += ImagesGlobal.iconBattleFriends.getWidth() + 1) {
                  gameGraphics2.drawImage(ImagesGlobal.iconBattleFriends, rageLevel, 0, 0);
                  ++textWidth2;
               }

               if (cooldownHeight > 0) {
                  Com.fontGreen.writeText(gameGraphics2, "x" + cooldownHeight, rageLevel, (uIComponent15.heightContent - Com.fontGreen.fontHeight) / 2);
               }

               return;
            }
            break;
         case 10011:
            if ((battleMember = BattleManager.getCurrentBattlePlayerMember()) != null) {
               rageLevel = 0;
               if (battleMember.avatarEnemysCount > 3) {
                  textWidth = 1;
                  cooldownHeight = battleMember.avatarEnemysCount;
               } else {
                  textWidth = battleMember.avatarEnemysCount;
                  cooldownHeight = 0;
               }

               for(textWidth2 = 0; textWidth2 < textWidth; rageLevel += ImagesGlobal.iconBattleEnemys.getWidth() + 1) {
                  gameGraphics2.drawImage(ImagesGlobal.iconBattleEnemys, rageLevel, 0, 0);
                  ++textWidth2;
               }

               if (cooldownHeight > 0) {
                  Com.fontRed.writeText(gameGraphics2, "x" + cooldownHeight, rageLevel, (uIComponent15.heightContent - Com.fontRed.fontHeight) / 2);
               }

               return;
            }
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      if (uIComponent15.id >= 11000) {
         if (uIComponent15.id == 11000) {
            this.tryActivateAtack();
         } else if (uIComponent15.id == 11000 + this.uiSlots.length - 1) {
            this.tryActivateRage();
         } else if (uIComponent15.id >= 11001 && uIComponent15.id <= 11000 + this.uiSlots.length - 2) {
            this.tryChooseBottle(uIComponent15.id - 11001);
         }
      }

      switch (uIComponent15.id) {
         case 10:
            this.pressLeftSoft();
            this.releaseLeftSoft();
            return;
         case 11:
            this.wasPressRSoft = true;
            this.releaseRightSoft();
            break;
         case 10005:
         case 10007:
            this.press0();
            return;
         case 10009:
            Battle battle2;
            if ((battle2 = BattleManager.getCurrentBattle()) != null) {
               BattleMember battleMember;
               if ((battleMember = battle2.getPlayerMember()) == null) {
                  return;
               }

               if (!battleMember.isAllowStepBack()) {
                  (new OtherMenuList()).showConfirmationForMenu(204, "Сдаться?\n(считается как смерть)");
                  return;
               }

               if (battleMember.stepBackType == 3) {
                  (new OtherMenuList()).showConfirmationForMenu(249, "Отступить из боя и потратить 1x͎͏Знак мира?\n(ничего не будет потеряно)");
               } else {
                  (new OtherMenuList()).showConfirmationForMenu(249, "Отступить из боя?\n(ничего не будет потеряно)");
               }
            }
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 10006:
            for(int i = this.offscreenIcons.size() - 1; i >= 0; --i) {
               BattleOffscreenIcon battleOffscreenIcon = (BattleOffscreenIcon)this.offscreenIcons.elementAt(i);
               if (x >= battleOffscreenIcon.left && x < battleOffscreenIcon.bottom && y >= battleOffscreenIcon.right && y < battleOffscreenIcon.top) {
                  smoothMoveCamera(battleOffscreenIcon.unitX, battleOffscreenIcon.unitY);
                  return;
               }
            }

            this.cursorWorldPosition = cursorToRealCoord(x, y);
            this.analizeCursorStateByType();
            this.cursorWorldActivate();
         default:
            this.cursorWorldPosition = null;
      }
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
      label17:
      switch (uIComponent15.id) {
         case 10006:
            switch (Com.touchControlType) {
               case 4:
                  break label17;
               default:
                  this.cursorWorldPosition = cursorToRealCoord(x, y);
                  break label17;
            }
         default:
            this.cursorWorldPosition = null;
      }

      if (uIComponent15.id >= 11000) {
         this.trySetOverSlotValue(uIComponent15.id - 11000);
      } else {
         this.trySetOverSlotValue(-1);
      }
   }

   private void trySetOverSlotValue(int setSlotIndex) {
      if (this.overSlot != setSlotIndex) {
         this.overSlot = setSlotIndex;
         this.overSlotStartTime = Mystery.currentTimeMillis;
      }

   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
      switch (uIComponent15.id) {
         case 10006:
            switch (Com.touchControlType) {
               case 4:
                  Camera cameraBattle2 = Com.cameraBattle;
                  cameraBattle2.centerAt(cameraBattle2.getCenterX() - dx, Com.cameraBattle.getCenterY() - dy);
                  this.cursorWorldPosition = null;
            }
         default:
      }
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
      switch (uIComponent15.id) {
         case 10006:
            switch (Com.touchControlType) {
               case 4:
                  Camera cameraBattle2 = Com.cameraBattle;
                  cameraBattle2.centerAt(cameraBattle2.getCenterX() - dx, Com.cameraBattle.getCenterY() - dy);
            }
         default:
      }
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 10006:
            switch (Com.touchControlType) {
               case 4:
                  this.activePressInTouch = true;
                  this.activePressInTouchSinceTime = Mystery.currentTimeMillis;
                  this.activePressInTouchCoordX = x;
                  this.activePressInTouchCoordY = y;
               default:
                  this.cursorWorldPosition = cursorToRealCoord(x, y);
            }
         default:
            if (uIComponent15.id >= 11000) {
               this.pressedSlot = uIComponent15.id - 11000;
            }

      }
   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10006:
            this.activateCursorAfterAnalize = true;
         default:
      }
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      switch (Com.touchControlType) {
         case 4:
            this.activePressInTouch = false;
            this.cursorWorldPosition = null;
         default:
            if (uIComponent15.id >= 11000 && this.pressedSlot == uIComponent15.id - 11000) {
               this.pressedSlot = -1;
            }

      }
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
      switch (uIComponent15.id) {
         case 10002:
            switch (this.topWindowState) {
               case 2:
                  this.topWindowState = 0;
                  return;
               case 3:
                  this.topWindowState = 1;
            }
         default:
      }
   }

   private void setCursor(int setType) {
      if (this.cursorType != setType) {
         GameCommons.instance.mapBufferSetMarkUnblockedTiles(setType == 11);
      }

      this.cursorType = setType;
      if (Com.touchControlType != 4) {
         Com.showDefaultCursor();
      }

   }

   private void fixCursor() {
      if (this.cursorPosition[0] < 0) {
         this.cursorPosition[0] = 0;
      }

      if (this.cursorPosition[0] > Com.cameraScreen.getCw() - 1) {
         this.cursorPosition[0] = Com.cameraScreen.getCw() - 1;
      }

      if (this.cursorPosition[1] < 0) {
         this.cursorPosition[1] = 0;
      }

      if (this.cursorPosition[1] > Com.cameraScreen.getCh() - 1) {
         this.cursorPosition[1] = Com.cameraScreen.getCh() - 1;
      }

   }

   private void setCursorPosition(int x, int y) {
      this.cursorPosition[0] = x;
      this.cursorPosition[1] = y;
      this.fixCursor();
   }

   private void pressCursor() {
      int cursorPosition2 = this.cursorPosition[0];
      int cursorPosition3 = this.cursorPosition[1];
      if (!GameCommons.instance.uiConteiner.pressPointer(cursorPosition2, cursorPosition3)) {
         this.wasPress = true;
      }
   }

   private void releaseCursor() {
      int cursorPosition2 = this.cursorPosition[0];
      int cursorPosition3 = this.cursorPosition[1];
      GameCommons.instance.uiConteiner.releasPointer(cursorPosition2, cursorPosition3);
      this.wasPress = false;
   }

   private void dragCursor() {
      int cursorPosition2 = this.cursorPosition[0];
      int cursorPosition3 = this.cursorPosition[1];
      GameCommons.instance.uiConteiner.dragPointer(cursorPosition2, cursorPosition3);
   }

   private void cursorAnimateMove(long time) {
      this.cursorMoveAccumTime += time;
      int cursorMoveAccumTime2 = (int)(this.cursorMoveAccumTime / 7L);
      this.cursorMoveAccumTime %= 7L;
      if (cursorMoveAccumTime2 > 0) {
         int cursorPosition2 = this.cursorPosition[0];
         int cursorPosition3 = this.cursorPosition[1];

         for(int cursorMoveDirectionIndex = 0; cursorMoveDirectionIndex < this.cursorMoveDirections.length; ++cursorMoveDirectionIndex) {
            if (this.cursorMoveDirections[cursorMoveDirectionIndex]) {
               int[] integers;
               switch (cursorMoveDirectionIndex) {
                  case 0:
                     integers = this.cursorPosition;
                     integers[0] -= cursorMoveAccumTime2;
                     break;
                  case 1:
                     integers = this.cursorPosition;
                     integers[0] += cursorMoveAccumTime2;
                     break;
                  case 2:
                     integers = this.cursorPosition;
                     integers[1] -= cursorMoveAccumTime2;
                     break;
                  case 3:
                     integers = this.cursorPosition;
                     integers[1] += cursorMoveAccumTime2;
               }
            }
         }

         this.fixCursor();
         if (cursorPosition2 != this.cursorPosition[0] || cursorPosition3 != this.cursorPosition[1]) {
            this.dragCursor();
         }

      }
   }

   private void cursorWorldActivate() {
      pressFireStep = 100;
      if (!realBattle) {
         pressFireStep = 200;
         (new OtherMenuList()).showConfirmationForMenu(165, "Покинуть просмотр боя?");
      } else if (this.cursorWorldPosition != null) {
         pressFireStep = 300;
         switch (this.cursorSate) {
            case 1:
               if (currentChoosedUnit >= 0) {
                  pressFireStep = 580;
                  BattleUnit battleUnit;
                  if ((battleUnit = BattleManager.getCurrentBattlePlayerAvatar()) != null) {
                     pressFireStep = 590;
                     BattleManager.getCurrentBattle().giveOrder(new BattleOrder(battleUnit.hisInd, currentChoosedUnit, 1, 1, (byte)0, 0, 0));
                     pressFireStep = 5100;
                  }
               }
               break;
            case 2:
               issueMoveOrder(this.cursorWorldPosition[0], this.cursorWorldPosition[1]);
               break;
            case 3:
               if (currentChoosedUnit >= 0) {
                  issuePlayerOrder(currentChoosedUnit, (byte)1, 0, -1, -1);
               }
               break;
            case 4:
               boolean appli = false;
               int targetX = -1;
               int targetY = -1;
               if (currentChoosedUnit >= 0) {
                  BattleUnit battleUnit2 = BattleManager.getCurrentBattleUnit(currentChoosedUnit);
                  BattleMember battleMember = BattleManager.getCurrentBattle() == null ? null : BattleManager.getCurrentBattle().getPlayerMember();
                  if (battleUnit2 != null && battleMember != null) {
                     if (this.chooseModeEnemys && battleUnit2.side != battleMember.side || this.chooseModeSelf && battleUnit2.hisBattleMemberId == battleMember.id && battleUnit2.playersAvatar || this.chooseModeFriends && (battleUnit2.side == battleMember.side && battleUnit2.hisBattleMemberId != battleMember.id || battleUnit2.hisBattleMemberId == battleMember.id && !battleUnit2.playersAvatar)) {
                        appli = true;
                     }

                     if (appli && ((BattleBottle)battleMember.bottlesBySlot.get(new Byte((byte)this.choosedBottle))).cooldownTimeCur > 0) {
                        appli = false;
                     }

                     if (appli && this.chooseModeGround) {
                        targetX = battleUnit2.cx;
                        targetY = battleUnit2.cy;
                     }
                  }
               } else if (this.chooseModeGround) {
                  appli = true;
                  targetX = this.cursorWorldPosition[0];
                  targetY = this.cursorWorldPosition[1];
               }

               if (appli) {
                  issuePlayerOrder(currentChoosedUnit, (byte)2, this.choosedBottle, targetX, targetY);
               }
         }

         this.setCursor(1);
         pressFireStep = 1100;
      }
   }

   private static int[] cursorToRealCoord(int cursorX, int cursorY) {
      return new int[]{Com.cameraBattle.getCx() + cursorX, Com.cameraBattle.getCy() + cursorY};
   }

   public final void getFloatScreenSpace(int[] result) {
      result[0] = 2;
      result[1] = (this.uiHealthBarMy.enabled ? this.uiHealthBarMy.yByPosition : Com.cameraScreen.getCh() - (GUIHandler.getSoftkeyHeight(Com.fontBig, GameCommons.instance.guiSoftSpacing) + 2) - (getSlotHeight() + 2)) - 2;
   }
}
