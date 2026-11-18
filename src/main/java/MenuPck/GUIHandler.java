package MenuPck;

import Base.Com;
import Base.GameCommonsExtender;
import Engine.MystFont;
import Engine.Mystery;
import desktop.PlatformRuntime;
import Moduls.PFter;
import Moduls.StrategGraphic;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class GUIHandler implements GameCommonsExtender, UIComponentHandler, PFter {
   protected static GUIDecorator menuDecorator;
   public static GameImage arrowImage;
   public static GameImage personageBackgroundImage;
   public static GameImage[] slotImages;
   private boolean wasActive = false;
   protected UIComponent botWindow;
   protected UIComponent topWindow;
   private int botWindowState;
   private int topWindowAnimateYDirection;
   private boolean topWindowAnimateYDirectionConfirm;
   private AnimateProperty topPrevAnimation;
   private int topPrevWindowAnimateYDirection;
   private boolean topPrevWindowAnimateYDirectionConfirm;
   private UIConteiner innerUIContainer;
   private Object lockObject;
   protected GUIHandlerEvents commonEvents;
   protected GUIActivityTopText currentTopGUIActivity;
   private GUIActivityTopText willTopGUIActivity;
   private GUIBaseActivity currentBottomGUIActivity;
   private GUIBaseActivity willBottomGUIActivity;
   protected GUIHandlerEvents currentEventListener;
   private GUIHandlerEvents willEventListener;
   private boolean isFastOkAnim;
   private Object actionEventsLock;
   private Vector actionEvents;
   protected MystFont font;
   protected MystFont fontDigit;
   protected int textSpacing;
   protected int lineSpacing;
   protected int softSpacing;
   protected int dividerSize;
   protected boolean animateMenu;
   protected int screenWidth;
   protected int screenHeight;
   private long disableBotAfterTime;
   private boolean lockReplace;

   public GUIHandler() {
      this.botWindow = new UIComponent((UIComponentHandler)null, menuDecorator, 100, 7, !Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1);
      this.topWindow = new UIComponent((UIComponentHandler)null, menuDecorator, 101, 6, !Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1);
      this.botWindowState = 0;
      this.topWindowAnimateYDirection = 0;
      this.topWindowAnimateYDirectionConfirm = false;
      this.topPrevAnimation = null;
      this.topPrevWindowAnimateYDirection = 0;
      this.topPrevWindowAnimateYDirectionConfirm = false;
      this.innerUIContainer = new UIConteiner();
      this.lockObject = new Object();
      this.currentTopGUIActivity = null;
      this.willTopGUIActivity = null;
      this.currentBottomGUIActivity = null;
      this.willBottomGUIActivity = null;
      this.currentEventListener = null;
      this.willEventListener = null;
      this.isFastOkAnim = false;
      this.actionEventsLock = new Object();
      this.actionEvents = new Vector();
      this.dividerSize = 1;
      this.disableBotAfterTime = -1L;
      this.lockReplace = false;
   }

   public static void loadResources() throws java.io.IOException {
      arrowImage = GameImage.createImage("/Data/All/itrfc/arrow_up.png");
      personageBackgroundImage = GameImage.createImage("/Data/All/personage_background.png");
      slotImages = new GameImage[16];

      for(int i = 0; i < 16; ++i) {
         slotImages[i] = GameImage.createImage("/Data/All/itrfc/inv_slot_" + i + ".png");
      }

   }

   public static void addForAtlasing(Vector vector3) {
      vector3.addElement(arrowImage);
      vector3.addElement(personageBackgroundImage);

      for(int i = 0; i < slotImages.length; ++i) {
         vector3.addElement(slotImages[i]);
      }

   }

   public final void init(MystFont mystFont, MystFont mystFont2, boolean animateMenu2, int lineSpacing2, int softSpacing2, int textSpacing2, int w, int h, GUIHandlerEvents gUIHandlerEvents) {
      this.font = mystFont;
      this.fontDigit = mystFont2;
      this.lineSpacing = lineSpacing2;
      this.softSpacing = softSpacing2;
      this.textSpacing = textSpacing2;
      this.commonEvents = gUIHandlerEvents;
      this.animateMenu = animateMenu2;
      this.screenWidth = w;
      this.screenHeight = h;
      this.botWindow.setUseBuffer(!Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1);
      this.topWindow.setUseBuffer(!Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1);
      this.botWindow.animationSecondHandler = this;
      this.topWindow.animationSecondHandler = this;
   }

   public final void onChangeResolution(int w, int h, boolean justPaint) {
      this.screenWidth = w;
      this.screenHeight = h;
      this.initWindowsParams(true, justPaint, false);
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null) {
            this.currentBottomGUIActivity.setScreenSize(w, h);
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.setScreenSize(w, h);
         }

      }
   }

   public final void addActionEvent(GUIHandlerEvents gUIHandlerEvents, int action, Object param) {
      synchronized(this.actionEventsLock) {
         this.actionEvents.addElement(new Object[]{gUIHandlerEvents, action, param});
      }
   }

   public final void setLockReplace(boolean lockReplace2) {
      this.lockReplace = lockReplace2;
   }

   private boolean isTopActive() {
      return this.currentTopGUIActivity != null;
   }

   private boolean isBottomActive() {
      return this.botWindowState != 0;
   }

   public final boolean isActive() {
      return this.isBottomActive();
   }

   protected final void initVisibiblity() {
      synchronized(this.lockObject) {
         boolean active;
         if (!(active = this.isBottomActive()) && this.wasActive) {
            Com.mainTh.setPFter2((PFter)null);
            this.wasActive = false;
         } else {
            if (active && Com.mainTh.setPFter2(this)) {
               if (this.currentEventListener != null) {
                  this.currentEventListener.onStartShowMenus();
               }

               if (this.commonEvents != null) {
                  this.commonEvents.onStartShowMenus();
               }
            }

            this.wasActive = active;
         }
      }
   }

   private void notifyMenuContentChanged() {
      if (this.currentEventListener != null) {
         this.currentEventListener.onMenuContentChanged2();
      }

      if (this.commonEvents != null) {
         this.commonEvents.onMenuContentChanged2();
      }

   }

   public final void hideAllWindows() {
      synchronized(this.lockObject) {
         if (!this.lockReplace) {
            this.hideTopWindow();
            this.hideBottomWindow(true);
         }
      }
   }

   public final void hideTopWindow() {
      synchronized(this.lockObject) {
         if (this.currentTopGUIActivity != null) {
            if (this.animateMenu) {
               if (this.topWindow.animateY.needValue != -this.topWindow.height) {
                  if (this.topWindowAnimateYDirectionConfirm && this.topWindowAnimateYDirection != 0) {
                     this.topPrevAnimation = new AnimateProperty();
                     this.topWindow.animateY.copyTo(this.topPrevAnimation);
                     this.topPrevWindowAnimateYDirection = this.topWindowAnimateYDirection;
                     this.topPrevWindowAnimateYDirectionConfirm = this.topWindowAnimateYDirectionConfirm;
                  }

                  this.topWindowAnimateYDirection = -1;
                  this.topWindowAnimateYDirectionConfirm = false;
                  this.topWindow.startAnimationY(this.topWindow.y, -this.topWindow.height, 140L);
               }
            } else {
               this.disableTopWindow();
            }
         } else {
            this.tryActivateTopActivity(false);
         }

         this.initVisibiblity();
      }
   }

   private void disableTopWindow() {
      synchronized(this.lockObject) {
         this.currentTopGUIActivity = null;
         this.topWindow.setHandler((UIComponentHandler)null);
         this.topWindow.clearAnimationY();
         this.tryActivateTopActivity(true);
         this.initVisibiblity();
      }
   }

   private void tryActivateTopActivity(boolean withAnimHeight) {
      synchronized(this.lockObject) {
         if (this.currentTopGUIActivity == null && this.willTopGUIActivity != null) {
            if (this.currentBottomGUIActivity == null && this.willBottomGUIActivity == null) {
               this.willTopGUIActivity = null;
            } else {
               this.willTopGUIActivity.activate(withAnimHeight);
               this.willTopGUIActivity = null;
               if (this.animateMenu && !this.topWindow.isActiveAnimationY()) {
                  this.topWindowAnimateYDirection = 1;
                  this.topWindowAnimateYDirectionConfirm = false;
                  this.topWindow.startAnimationY(-this.topWindow.height, this.topWindow.y, 140L);
               }
            }
         }

      }
   }

   private void hideBottomWindow(boolean forceHide) {
      synchronized(this.lockObject) {
         if (this.animateMenu) {
            if (this.currentBottomGUIActivity != null && !this.currentBottomGUIActivity.a_()) {
               this.botWindowState = 1;
               this.disableBotAfterTime = 140L;
            } else {
               switch (this.botWindowState) {
                  case 0:
                     this.tryActivateBotActivity();
                  case 1:
                  default:
                     break;
                  case 2:
                     if (this.willBottomGUIActivity == null) {
                        this.botWindowState = 1;
                        this.botWindow.startAnimationY(this.botWindow.y, this.screenHeight, 140L);
                     } else if (this.willBottomGUIActivity.a_()) {
                        this.tryActivateBotActivity();
                     } else {
                        this.botWindowState = 1;
                        this.botWindow.startAnimationY(this.botWindow.y, this.screenHeight, 140L);
                     }
                     break;
                  case 3:
                     if (forceHide) {
                        this.botWindowState = 1;
                        this.botWindow.startAnimationY(this.botWindow.y, this.screenHeight, 140L);
                     }
               }
            }
         } else {
            this.disableBotWindow();
         }

         this.initVisibiblity();
      }
   }

   private void disableBotWindow() {
      synchronized(this.lockObject) {
         if (this.currentEventListener != null) {
            this.currentEventListener.onDisableMenu(this.currentBottomGUIActivity != null ? this.currentBottomGUIActivity.context : null, this.willBottomGUIActivity == null ? null : this.willBottomGUIActivity.context);
         }

         if (this.commonEvents != null) {
            this.commonEvents.onDisableMenu(this.currentBottomGUIActivity != null ? this.currentBottomGUIActivity.context : null, this.willBottomGUIActivity == null ? null : this.willBottomGUIActivity.context);
         }

         this.botWindowState = 0;
         this.currentBottomGUIActivity = null;
         this.currentEventListener = null;
         this.botWindow.setHandler((UIComponentHandler)null);
         this.botWindow.animateY.clearAnimation(this.botWindow.y);
         this.tryActivateBotActivity();
         this.initVisibiblity();
      }
   }

   private void tryActivateBotActivity() {
      synchronized(this.lockObject) {
         switch (this.botWindowState) {
            case 0:
            case 2:
               if (this.willBottomGUIActivity != null) {
                  if (this.botWindowState == 2 && this.currentBottomGUIActivity.a_() && !this.willBottomGUIActivity.a_()) {
                     this.hideBottomWindow(false);
                  } else {
                     if (this.currentEventListener != null) {
                        this.currentEventListener.onDisableMenu(this.currentBottomGUIActivity != null ? this.currentBottomGUIActivity.context : null, this.willBottomGUIActivity.context);
                        if (this.commonEvents != null) {
                           this.commonEvents.onDisableMenu(this.currentBottomGUIActivity != null ? this.currentBottomGUIActivity.context : null, this.willBottomGUIActivity.context);
                        }
                     }

                     this.currentEventListener = this.willEventListener;
                     this.currentBottomGUIActivity = this.willBottomGUIActivity;
                     if (this.currentBottomGUIActivity.a_()) {
                        switch (this.botWindowState) {
                           case 0:
                              if (this.animateMenu) {
                                 this.botWindowState = 3;
                              } else {
                                 this.botWindowState = 2;
                              }
                        }
                     } else {
                        this.botWindowState = 2;
                     }

                     this.currentBottomGUIActivity.activate();
                     Com.mainTh.cleareWasPresses();
                     this.clearWillActivity();
                     this.initWindowsParams(true, false, true);
                  }
               }
            default:
         }
      }
   }

   private boolean isBotWindowContorollable() {
      return this.botWindowState != 0 && this.botWindowState != 1;
   }

   protected final int getOneLineHeight() {
      return this.font.fontHeight + (this.lineSpacing << 1);
   }

   public final void initWindowsParams() {
      this.initWindowsParams(true, false, true);
   }

   public final void initWindowsParams(boolean enableTopHeightAnim) {
      this.initWindowsParams(enableTopHeightAnim, false, true);
   }

   public final void initWindowsParams(boolean enableTopHeightAnim, boolean forceXY, boolean withAnimX) {
      synchronized(this.lockObject) {
         this.innerUIContainer.clear();
         this.innerUIContainer.addComponent(new UIComponent(this, menuDecorator, 99, 0, false, false, this.screenWidth, this.screenHeight, 0, 0, 4, 0, 0, this.screenWidth, 0, this.screenHeight));
         int cLAW_CORNER_OFF_Y = this.screenHeight - (this.currentBottomGUIActivity == null ? 0 : this.currentBottomGUIActivity.getBotWindowAddingOffset());
         if (this.isBottomActive() && this.isTopActive()) {
            if (this.currentBottomGUIActivity != null && this.currentBottomGUIActivity.a_()) {
               cLAW_CORNER_OFF_Y = cLAW_CORNER_OFF_Y - GUIDecorator.CLAW_CORNER_OFF_Y - GUIDecorator.CLAW_CORNER_OFF_Y;
            }

            cLAW_CORNER_OFF_Y = cLAW_CORNER_OFF_Y - GUIDecorator.SNAKE_TOP_OFF_Y - (GUIDecorator.SNAKE_BOT_SIZE_Y - GUIDecorator.SNAKE_BOT_OFF_Y);
         } else if (this.isBottomActive()) {
            if (this.currentBottomGUIActivity != null && this.currentBottomGUIActivity.a_()) {
               cLAW_CORNER_OFF_Y = cLAW_CORNER_OFF_Y - GUIDecorator.CLAW_CORNER_OFF_Y - GUIDecorator.CLAW_CORNER_OFF_Y;
            }
         } else if (this.isTopActive()) {
            cLAW_CORNER_OFF_Y = cLAW_CORNER_OFF_Y - GUIDecorator.SNAKE_TOP_OFF_Y - (GUIDecorator.SNAKE_BOT_SIZE_Y - GUIDecorator.SNAKE_BOT_OFF_Y);
         }

         int preferContentHeight;
         if (this.isBottomActive() && this.currentBottomGUIActivity != null && this.currentBottomGUIActivity.a_()) {
            preferContentHeight = this.currentBottomGUIActivity.getPreferContentHeight(cLAW_CORNER_OFF_Y);
            boolean isActiveAnimationX = this.botWindow.isActiveAnimationX();
            boolean isActiveAnimationY = this.botWindow.isActiveAnimationY();
            withAnimX = !withAnimX || !this.animateMenu || this.botWindowState == 3 && !isActiveAnimationX;
            this.botWindow.setPosition(this.screenWidth, this.screenHeight, 3, 0, 5, this.currentBottomGUIActivity.getBotWindowAddingOffset(), 0, this.currentBottomGUIActivity.getPreferContentWidth(), 0, preferContentHeight, !withAnimX, withAnimX, this.animateMenu, !this.animateMenu, withAnimX ? 0L : 140L, this.animateMenu ? 140L : 0L, false);
            if (this.animateMenu && (!this.botWindow.isActiveAnimationX() || this.botWindow.animateX.needValue != this.botWindow.xByPosition)) {
               this.botWindow.clearAnimationX();
               this.botWindow.startAnimationX(this.botWindow.x, this.botWindow.xByPosition, 140L);
            }

            if (this.animateMenu) {
               switch (this.botWindowState) {
                  case 0:
                  case 1:
                     if (!isActiveAnimationY && this.botWindow.y != this.screenHeight || isActiveAnimationY && this.botWindow.animateY.needValue != this.screenHeight) {
                        this.botWindow.clearAnimationY();
                        this.botWindow.startAnimationY(this.botWindow.y, this.screenHeight, 140L);
                     }
                     break;
                  case 2:
                  case 3:
                     if (!isActiveAnimationY && this.botWindow.y != this.botWindow.yByPosition || isActiveAnimationY && this.botWindow.animateY.needValue != this.botWindow.yByPosition) {
                        this.botWindow.clearAnimationY();
                        this.botWindow.startAnimationY(this.botWindowState == 3 && !isActiveAnimationY ? this.screenHeight : this.botWindow.y, this.botWindow.yByPosition, 140L);
                     }
               }
            }
         }

         if (this.isBottomActive() && this.currentBottomGUIActivity != null && this.currentBottomGUIActivity.a_()) {
            cLAW_CORNER_OFF_Y -= this.botWindow.heightContentByPosition;
         }

         if (this.isTopActive() && this.currentTopGUIActivity != null) {
            preferContentHeight = this.currentTopGUIActivity.getPreferContentHeight(cLAW_CORNER_OFF_Y);
            int minimum = Math.min(cLAW_CORNER_OFF_Y, Math.max(preferContentHeight, GUIDecorator.SNAKE_TOP_SIZE_Y - GUIDecorator.SNAKE_TOP_OFF_Y + GUIDecorator.SNAKE_BOT_OFF_Y));
            this.topWindow.setPosition(this.screenWidth, this.screenHeight, 3, 0, 4, 0, 0, this.currentTopGUIActivity.getPreferContentWidth(), 0, minimum, (this.topWindow.isActiveAnimationY() || this.topWindowAnimateYDirection != 0) && !forceXY, !this.topWindow.isActiveAnimationY() && this.topWindowAnimateYDirection == 0, 0L, this.animateMenu && enableTopHeightAnim ? 140L : 0L);
         }
      }

      this.notifyMenuContentChanged();
      synchronized(this.lockObject) {
         if (this.isBottomActive() && this.animateMenu && this.botWindowState == 3 && !this.botWindow.isActiveAnimationY()) {
            this.finishAnimationYUIComponent(this.botWindow);
         }

      }
   }

   public final boolean forceDataBottomWindowTextList(String[] texts, int[] textsIds, int choosedIndex) {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity instanceof GUIActivityTextList) {
            ((GUIActivityTextList)this.currentBottomGUIActivity).forceNewData(texts, textsIds, choosedIndex);
            return true;
         } else {
            return false;
         }
      }
   }

   public final void showBottomWindowTextList(String[] texts, int[] textsIds, String rightSoftText, int choosedIndex, GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityTextList gUIActivityTextList;
         (gUIActivityTextList = new GUIActivityTextList(this, context)).init(texts, textsIds, rightSoftText, choosedIndex);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityTextList);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowWorldMap(int mapX, int mapY, int[][] questsMarks, GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityWorldMap gUIActivityWorldMap;
         (gUIActivityWorldMap = new GUIActivityWorldMap(this, context)).init(mapX, mapY, questsMarks);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityWorldMap);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowInventory(String[] slotsSymbols, int[] slotsNumbers, int curSlot, String leftSoftText, String rightSoftText, String midSoftText, boolean justMid, StrategGraphic strategGraphic2, GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityInventory gUIActivityInventory;
         (gUIActivityInventory = new GUIActivityInventory(this, context)).init(slotsSymbols, slotsNumbers, curSlot, leftSoftText, rightSoftText, midSoftText, justMid, strategGraphic2);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityInventory);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowNumberEnter(int curVal, int minVal, int maxVal, String hintText, GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityNumber gUIActivityNumber;
         (gUIActivityNumber = new GUIActivityNumber(this, context)).init(curVal, minVal, maxVal, hintText);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityNumber);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowTextEnter(String startText, int startKeyboard, GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityKeyboard gUIActivityKeyboard;
         (gUIActivityKeyboard = new GUIActivityKeyboard(this, context)).init(startText, startKeyboard);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityKeyboard);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowYesNo(GUIHandlerEvents gUIHandlerEvents, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityYesNo gUIActivityYesNo = new GUIActivityYesNo(this, context);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityYesNo);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowYesNo(GUIHandlerEvents gUIHandlerEvents, String specYes, String specNo, Object context) {
      synchronized(this.lockObject) {
         this.hideTopWindow();
         GUIActivityYesNo gUIActivityYesNo = new GUIActivityYesNo(this, specYes, specNo, context);
         this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityYesNo);
         this.hideBottomWindow(false);
      }
   }

   public final void showBottomWindowOK(GUIHandlerEvents gUIHandlerEvents, Object context) {
      this.showBottomWindowOK(gUIHandlerEvents, false, context);
   }

   public final void showBottomWindowOK(GUIHandlerEvents gUIHandlerEvents, boolean fastAnim, Object context) {
      synchronized(this.lockObject) {
         this.isFastOkAnim = fastAnim;
         GUIActivityOK gUIActivityOK;
         if (fastAnim) {
            this.disableTopWindow();
            gUIActivityOK = new GUIActivityOK(this, context);
            this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityOK);
            this.disableBotWindow();
         } else {
            this.hideTopWindow();
            gUIActivityOK = new GUIActivityOK(this, context);
            this.setWillActivity((GUIHandlerEvents)gUIHandlerEvents, (GUIBaseActivity)gUIActivityOK);
            this.hideBottomWindow(false);
         }

      }
   }

   private void setWillActivity(GUIHandlerEvents gUIHandlerEvents, GUIBaseActivity gUIBaseActivity) {
      if (this.willEventListener != null) {
         this.willEventListener.onDisableMenu(this.willBottomGUIActivity != null ? this.willBottomGUIActivity.context : null, gUIBaseActivity == null ? null : gUIBaseActivity.context);
      }

      if (this.commonEvents != null) {
         this.commonEvents.onDisableMenu(this.willBottomGUIActivity != null ? this.willBottomGUIActivity.context : null, gUIBaseActivity == null ? null : gUIBaseActivity.context);
      }

      this.willEventListener = gUIHandlerEvents;
      this.willBottomGUIActivity = gUIBaseActivity;
   }

   private void clearWillActivity() {
      this.willEventListener = null;
      this.willBottomGUIActivity = null;
   }

   public final GUIHandlerSnapshot makeSnapshot() {
      synchronized(this.lockObject) {
         return new GUIHandlerSnapshot(this.currentEventListener, this.currentBottomGUIActivity, this.isFastOkAnim);
      }
   }

   public final void restoreSnapshot(GUIHandlerSnapshot gUIHandlerSnapshot) {
      synchronized(this.lockObject) {
         if (gUIHandlerSnapshot.bottomActivity instanceof GUIActivityOK && gUIHandlerSnapshot.fastAnim) {
            this.disableTopWindow();
            this.setWillActivity(gUIHandlerSnapshot.eventListener, gUIHandlerSnapshot.bottomActivity);
            this.disableBotWindow();
         } else {
            this.hideTopWindow();
            this.setWillActivity(gUIHandlerSnapshot.eventListener, gUIHandlerSnapshot.bottomActivity);
            this.hideBottomWindow(false);
         }

      }
   }

   public final void finishAnimationYUIComponent(String messageText) {
      synchronized(this.lockObject) {
         String messageText2 = messageText;
         boolean unusedProgressMarker = true;
         String messageText3 = messageText2;
         GUIHandler gUIHandler = this;
         synchronized(this.lockObject) {
            if (gUIHandler.topWindowAnimateYDirection != 0 && gUIHandler.currentTopGUIActivity != null) {
               if (gUIHandler.topWindowAnimateYDirection < 0) {
                  if (gUIHandler.topWindowAnimateYDirectionConfirm) {
                     gUIHandler.setWillTopActivity(messageText3);
                  } else if (gUIHandler.topPrevAnimation == null) {
                     gUIHandler.setWillTopActivity(messageText3);
                     gUIHandler.disableTopWindow();
                     gUIHandler.topWindow.forceAnimationY();
                  } else {
                     if (gUIHandler.topPrevWindowAnimateYDirection > 0) {
                        gUIHandler.resetCurrentTopActivity(messageText3);
                     } else {
                        gUIHandler.setWillTopActivity(messageText3);
                     }

                     gUIHandler.topPrevAnimation.copyTo(gUIHandler.topWindow.animateY);
                     gUIHandler.topWindowAnimateYDirection = gUIHandler.topPrevWindowAnimateYDirection;
                     gUIHandler.topWindowAnimateYDirectionConfirm = gUIHandler.topPrevWindowAnimateYDirectionConfirm;
                  }
               } else if (gUIHandler.topWindowAnimateYDirection > 0) {
                  gUIHandler.resetCurrentTopActivity(messageText3);
               }
            } else if (gUIHandler.currentTopGUIActivity == null) {
               gUIHandler.setWillTopActivity(messageText3);
               gUIHandler.tryActivateTopActivity(false);
            } else {
               gUIHandler.resetCurrentTopActivity(messageText3);
            }

            gUIHandler.topPrevAnimation = null;
            gUIHandler.initVisibiblity();
            gUIHandler.hideTopWindowDecorAvatar();
            gUIHandler.hideTopWindowDecorUnit();
            gUIHandler.hideTopWindowDecorUnits();
         }

      }
   }

   private void resetCurrentTopActivity(String messageText) {
      synchronized(this.lockObject) {
         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.initText(messageText);
            this.currentTopGUIActivity.activate();
         }
      }
   }

   private void setWillTopActivity(String messageText) {
      synchronized(this.lockObject) {
         GUIActivityTopText gUIActivityTopText;
         (gUIActivityTopText = new GUIActivityTopText(this, (Object)null)).initText(messageText);
         this.willTopGUIActivity = gUIActivityTopText;
      }
   }

   public final void showTopWindowDecorAvatar(StrategGraphic strategGraphic2) {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.initDecorAvatar(strategGraphic2);
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.initDecorAvatar(strategGraphic2);
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   public final void hideTopWindowDecorAvatar() {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.hideDecorAvatar();
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.hideDecorAvatar();
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   public final void showTopWindowDecorUnit(int head, int body) {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.initDecorUnit(head, body);
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.initDecorUnit(head, body);
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   private void hideTopWindowDecorUnit() {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.hideDecorUnit();
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.hideDecorUnit();
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   public final void showTopWindowDecorUnits(int[][] units) {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.initDecorUnits(units);
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.initDecorUnits(units);
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   private void hideTopWindowDecorUnits() {
      synchronized(this.lockObject) {
         if (this.willTopGUIActivity != null) {
            this.willTopGUIActivity.hideDecorUnits();
         } else if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.hideDecorUnits();
            this.currentTopGUIActivity.recalcParams();
         }
      }

      this.initVisibiblity();
   }

   public final void paint(GameGraphics gameGraphics2, boolean justPaint) {
      this.innerUIContainer.paint(gameGraphics2);
      synchronized(this.lockObject) {
         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.uiConteiner.paint(gameGraphics2);
         }

         if (this.currentBottomGUIActivity != null) {
            this.currentBottomGUIActivity.uiConteiner.paint(gameGraphics2);
         }

      }
   }

   public final void onAfterHardLoad() {
      Com.showGameCursor();
   }

   public final void functionate() {
   }

   public final void functionateUnderDrawProgress() {
      boolean animationStep = this.font.wasAnimationStep();
      synchronized(this.lockObject) {
         if (this.disableBotAfterTime >= 0L) {
            this.disableBotAfterTime -= Mystery.FrameTimeI;
            if (this.disableBotAfterTime < 0L) {
               this.disableBotAfterTime = -1L;
               this.disableBotWindow();
            }
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.uiConteiner.functional((int)Mystery.FrameTimeI);
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.functional(animationStep);
         }

         if (this.currentBottomGUIActivity != null) {
            this.currentBottomGUIActivity.uiConteiner.functional((int)Mystery.FrameTimeI);
         }

         if (this.currentBottomGUIActivity != null) {
            this.currentBottomGUIActivity.functional(animationStep);
         }

         if (this.currentEventListener != null) {
            this.currentEventListener.functionate();
         }

         this.innerUIContainer.functional((int)Mystery.FrameTimeI);
      }

      Vector vector3 = null;
      synchronized(this.actionEventsLock) {
         if (this.actionEvents.size() > 0) {
            vector3 = this.actionEvents;
            this.actionEvents = new Vector();
         }
      }

      if (vector3 != null) {
         int vector3Index = 0;

         for(int vector3Count = vector3.size(); vector3Index < vector3Count; ++vector3Index) {
            Object[] elements;
            GUIHandlerEvents gUIHandlerEvents = (GUIHandlerEvents)(elements = (Object[])vector3.elementAt(vector3Index))[0];
            int eventType = (Integer)elements[1];
            Object eventPayload = elements[2];
            switch (eventType) {
               case 0:
                  gUIHandlerEvents.onMenuContentChanged();
                  break;
               case 1:
                  gUIHandlerEvents.onInventoryBackSelected();
                  break;
               case 2:
                  gUIHandlerEvents.onInventoryChanged((Integer)eventPayload);
                  break;
               case 3:
                  gUIHandlerEvents.onInventorySelected((Integer)eventPayload);
                  break;
               case 4:
                  gUIHandlerEvents.onInventoryBackSelected2();
                  break;
               case 5:
                  gUIHandlerEvents.onInventoryFixSelected();
                  break;
               case 6:
                  gUIHandlerEvents.onTextListChanged((Integer)eventPayload);
                  break;
               case 7:
                  gUIHandlerEvents.onTextListSelected((Integer)eventPayload);
                  break;
               case 8:
                  Boolean ignoredCast = (Boolean)eventPayload;
                  gUIHandlerEvents.onInventoryBackSelected3();
                  break;
               case 9:
                  gUIHandlerEvents.onNumberChanged((Integer)eventPayload);
                  break;
               case 10:
                  gUIHandlerEvents.onNumberCancel();
                  break;
               case 11:
                  gUIHandlerEvents.onNumberSelected((Integer)eventPayload);
                  break;
               case 12:
                  gUIHandlerEvents.onTextChanged((String)eventPayload);
                  break;
               case 13:
                  gUIHandlerEvents.onTextSelected((String)eventPayload);
                  break;
               case 14:
                  gUIHandlerEvents.onTextDeclane();
                  break;
               case 15:
                  gUIHandlerEvents.onYesNoSelected(((boolean[])eventPayload)[0], ((boolean[])eventPayload)[1]);
                  break;
               case 16:
                  gUIHandlerEvents.onYesNoChanged();
                  break;
               case 17:
                  gUIHandlerEvents.onOKSelected();
                  break;
               case 18:
                  gUIHandlerEvents.onOKChanged();
            }
         }
      }

   }

   public final void releaseAllEvent() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null) {
            this.currentBottomGUIActivity.releasAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.releasAll();
         }

      }
   }

   public final boolean isReplaceable() {
      return !this.lockReplace;
   }

   public final void wheelRot(int num) {
      while(num > 0) {
         --num;
         this.pressDown();
         this.releaseDown();
      }

      while(num < 0) {
         ++num;
         this.pressUp();
         this.releaseUp();
      }

   }

   public final void movePointer(int x, int y) {
   }

   public final void pressPointer(int x, int y) {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity == null || !this.isBotWindowContorollable() || !this.currentBottomGUIActivity.uiConteiner.pressPointer(x, y)) {
            if (this.currentTopGUIActivity == null || !this.currentTopGUIActivity.uiConteiner.pressPointer(x, y)) {
               this.innerUIContainer.pressPointer(x, y);
            }
         }
      }
   }

   public final void dragPointer(int x, int y) {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.uiConteiner.dragPointer(x, y);
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.uiConteiner.dragPointer(x, y);
         }

         this.innerUIContainer.dragPointer(x, y);
      }
   }

   public final void releasePointer(int x, int y) {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.uiConteiner.releasPointer(x, y);
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.uiConteiner.releasPointer(x, y);
         }

         this.innerUIContainer.releasPointer(x, y);
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.realeasPointerAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.realeasPointerAll();
         }

      }
   }

   public final void pressLeftSoft() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.clickLeftSoft();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.clickLeftSoft();
         }

      }
   }

   public final void pressRightSoft() {
      this.pressRightSoft(false);
   }

   private void pressRightSoft(boolean byBackgroundClick) {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.clickRightSoft(byBackgroundClick);
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.clickRightSoft(byBackgroundClick);
         }

      }
   }

   public final void releaseLeftSoft() {
   }

   public final void releaseRightSoft() {
   }

   public final void pressStar() {
   }

   public final void pressSharp() {
   }

   public final void press1() {
   }

   public final void press3() {
   }

   public final void press7() {
   }

   public final void press9() {
   }

   public final void press0() {
   }

   public final void release1() {
   }

   public final void release3() {
   }

   public final void release7() {
   }

   public final void release9() {
   }

   public final void pressLeft() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.pressLeft();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.pressLeft();
         }

      }
   }

   public final void pressRight() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.pressRight();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.pressRight();
         }

      }
   }

   public final void pressUp() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.pressUp();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.pressUp();
         }

      }
   }

   public final void pressDown() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.pressDown();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.pressDown();
         }

      }
   }

   public final void pressFire() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.pressFire();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.pressFire();
         }

      }
   }

   public final void releaseLeft() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.releasLeftAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.releasLeftAll();
         }

      }
   }

   public final void releaseRight() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.releasRightAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.releasRightAll();
         }

      }
   }

   public final void releaseUp() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.releasUpAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.releasUpAll();
         }

      }
   }

   public final void releaseDown() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.releasDownAll();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.releasDownAll();
         }

      }
   }

   public final void releaseFire() {
      synchronized(this.lockObject) {
         if (this.currentBottomGUIActivity != null && this.isBotWindowContorollable()) {
            this.currentBottomGUIActivity.realeasFire();
         }

         if (this.currentTopGUIActivity != null) {
            this.currentTopGUIActivity.realeasFire();
         }

      }
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 99:
            this.pressRightSoft(true);
         default:
      }
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

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 101:
            if (!this.topWindowAnimateYDirectionConfirm) {
               if (this.topWindowAnimateYDirection < 0 && delta < 0) {
                  this.topWindowAnimateYDirectionConfirm = true;
               }

               if (this.topWindowAnimateYDirection > 0 && delta > 0) {
                  this.topWindowAnimateYDirectionConfirm = true;
               }

               if (this.topWindowAnimateYDirection == 0 && delta == 0) {
                  this.topWindowAnimateYDirectionConfirm = true;
               }
            }
         default:
      }
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
      synchronized(this.lockObject) {
         switch (uIComponent15.id) {
            case 100:
               switch (this.botWindowState) {
                  case 0:
                  case 1:
                     this.disableBotWindow();
                     return;
                  case 2:
                  case 3:
                     this.botWindowState = 2;
                     if (this.willBottomGUIActivity != null) {
                        this.tryActivateBotActivity();
                     }

                     return;
                  default:
                     return;
               }
            case 101:
               this.topPrevAnimation = null;
               int topWindowAnimateYDirection2 = this.topWindowAnimateYDirection;
               this.topWindowAnimateYDirection = 0;
               this.topWindowAnimateYDirectionConfirm = true;
               if (topWindowAnimateYDirection2 > 0) {
                  if (this.willTopGUIActivity != null) {
                     this.disableTopWindow();
                     this.topWindow.forceAnimationY();
                  }
               } else if (topWindowAnimateYDirection2 < 0) {
                  this.disableTopWindow();
               }
         }

      }
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
      synchronized(this.lockObject) {
         switch (uIComponent15.id) {
            case 100:
               if (this.currentBottomGUIActivity != null && delta != 0) {
                  this.currentBottomGUIActivity.recalcParams();
               }
            default:
         }
      }
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
      synchronized(this.lockObject) {
         switch (uIComponent15.id) {
            case 100:
               if (this.currentBottomGUIActivity != null && delta != 0) {
                  this.currentBottomGUIActivity.recalcParams();
               }
            default:
         }
      }
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }

   public static void positionateSoftButtons(boolean justMid, UIConteiner uIConteiner, UIComponentHandler uIComponentHandler, int screenWidth2, int screenHeight2, MystFont mystFont, int lineSpacing2) {
      positionateSoftButtons(justMid ? 0 : 1, uIConteiner, uIComponentHandler, screenWidth2, screenHeight2, mystFont, lineSpacing2);
   }

   public static void positionateSoftButtons(int positionateType, UIConteiner uIConteiner, UIComponentHandler uIComponentHandler, int screenWidth2, int screenHeight2, MystFont mystFont, int lineSpacing2) {
      if (positionateType == 0 || positionateType == 2) {
         uIConteiner.addComponent(new UIComponent(uIComponentHandler, menuDecorator, 12, 3, false, screenWidth2, screenHeight2, 3, 0, 5, 0, 1, screenWidth2, 0, getSoftkeyHeight(mystFont, lineSpacing2)));
      }

      if (positionateType == 1 || positionateType == 2) {
         uIConteiner.addComponent(new UIComponent(uIComponentHandler, menuDecorator, 10, 1, false, screenWidth2, screenHeight2, 0, 0, 5, 0, 1, screenWidth2 / 2, 0, getSoftkeyHeight(mystFont, lineSpacing2)));
         uIConteiner.addComponent(new UIComponent(uIComponentHandler, menuDecorator, 11, 2, false, screenWidth2, screenHeight2, 2, 0, 5, 0, 1, screenWidth2 / 2, 0, getSoftkeyHeight(mystFont, lineSpacing2)));
      }

   }

   public static void paintSoftButtons(UIComponent uIComponent15, GameGraphics gameGraphics2, String left, String right, String mid, MystFont mystFont, int softSpacing2) {
      int textWidth;
      int widthContent;
      switch (uIComponent15.id) {
         case 10:
         case 11:
            mid = "";
            textWidth = 0;
            switch (uIComponent15.id) {
               case 10:
                  mid = left;
                  textWidth = mystFont.getTextWidth(left);
                  break;
               case 11:
                  mid = right;
                  textWidth = mystFont.getTextWidth(right);
            }

            if ((widthContent = uIComponent15.widthContent - 4) >= textWidth) {
               mystFont.writeText(gameGraphics2, mid, 2 + (widthContent - textWidth) / 2, softSpacing2);
               return;
            } else {
               mystFont.writeText(gameGraphics2, mid, 2, softSpacing2, widthContent, 'ə');
            }
         default:
            return;
         case 12:
            textWidth = mystFont.getTextWidth(mid);
            if ((widthContent = uIComponent15.widthContent - 4) >= textWidth) {
               mystFont.writeText(gameGraphics2, mid, 2 + (widthContent - textWidth) / 2, softSpacing2);
            } else {
               mystFont.writeText(gameGraphics2, mid, 2, softSpacing2, widthContent, 'ə');
            }
      }
   }

   public static int getSoftkeyHeight(MystFont mystFont, int softSpacing2) {
      return mystFont.fontHeight + 2 * softSpacing2;
   }

   public final void getFloatScreenSpace(int[] result) {
      if (this.isTopActive()) {
         result[0] = this.topWindow.yByPosition + this.topWindow.heightByPosition + 2;
      } else {
         result[0] = 2;
      }

      if (this.isBottomActive() && this.currentBottomGUIActivity != null && this.currentBottomGUIActivity.a_()) {
         result[1] = this.botWindow.yByPosition - 2;
      } else {
         result[1] = Com.cameraScreen.getCh() - getSoftkeyHeight(this.font, this.softSpacing) - 2;
      }
   }

   static {
      menuDecorator = GUIDecorator.instance;
   }
}
