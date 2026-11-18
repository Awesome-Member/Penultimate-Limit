package MenuPck;

import desktop.PlatformRuntime;
import Moduls.StrategAnimGroup;
import Moduls.StrategGraphic;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class GUIActivityInventory extends GUIBaseActivity implements UIComponentHandler {
   private static int[] SLOTS_ORDER_NEXT_KEYBOARD = new int[]{6, 5, 13, 4, 7, 2, 1, 9, 10, 11, 12, 14, 15, 3, 8, 0};
   private static int[] SLOTS_ORDER_PREV_KEYBOARD = new int[]{15, 6, 5, 13, 3, 1, 0, 4, 14, 7, 8, 9, 10, 2, 11, 12};
   private boolean isPress = false;
   private int curSlotInd = 0;
   private boolean preventClick = false;
   private String leftSoftText = "";
   private String rightSoftText = "";
   private String midSoftText = "";
   private boolean justMid = false;
   private GameImage curButtonImage = null;
   private GameImage curButtonSelectImage = null;
   private GameImage curButtonPressImage = null;
   private int paddingForBorder;
   private int slotSize = -1;
   private int personageCenterX = -1;
   private int personageCenterY = -1;
   private int[][] slotsPositions = new int[16][2];
   private String[] slotsSymbols = new String[16];
   private int[] slotsNumbers = new int[16];
   private StrategGraphic strategGraphic = null;

   public GUIActivityInventory(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, 300, 300, -1, -1, context);
   }

   protected final void init(String[] slotsSymbols2, int[] slotsNumbers2, int curSlot, String leftSoftText2, String rightSoftText2, String midSoftText2, boolean justMid2, StrategGraphic strategGraphic2) {
      this.slotsSymbols = slotsSymbols2;
      this.slotsNumbers = slotsNumbers2;
      this.curSlotInd = curSlot;
      this.leftSoftText = leftSoftText2;
      this.rightSoftText = rightSoftText2;
      this.midSoftText = midSoftText2;
      this.justMid = justMid2;
      this.strategGraphic = strategGraphic2;
      if (this.curSlotInd < 0) {
         this.curSlotInd = 0;
      }

      if (this.curSlotInd > 15) {
         this.curSlotInd = 15;
      }

   }

   public final void activate() {
      super.activate();
      this.paddingForBorder = GUIDecorator.toothBorderImageWidth;
      this.guiHandler.hideTopWindow();
      this.setCurSlotInd(this.curSlotInd, true);
      this.guiHandler.initWindowsParams();
      this.recalcParams();
      this.guiHandler.initVisibiblity();
   }

   private void setCurSlotInd(int curSlot, boolean force) {
      if (curSlot < 0) {
         curSlot = 0;
      }

      if (curSlot > 15) {
         curSlot = 15;
      }

      if (this.curSlotInd != curSlot || force) {
         this.curSlotInd = curSlot;
         if (this.curSlotInd >= 0) {
            if (this.guiHandler.currentEventListener != null) {
               this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 2, this.curSlotInd);
            }

            if (this.guiHandler.commonEvents != null) {
               this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 2, this.curSlotInd);
            }
         }

         this.guiHandler.botWindow.needRedraw = true;
      }

   }

   private static int getSlotSizeForHeight(int availableHeight) {
      int maximumSlotSize = (availableHeight - 15) / 4;
      return Math.min((int)((353553L * (long)availableHeight - 3000000L) / 1353553L), maximumSlotSize);
   }

   private static int getSlotSizeForWidth(int availableWidth) {
      return (int)((353553L * (long)availableWidth - 5121318L) / 2060659L);
   }

   private static int getSlotDiagonalOffset(int slotSize2) {
      return (int)(1000000L * (long)(slotSize2 + 3) / 707106L + 1L);
   }

   protected final void recalcParams() {
      int slotSize2 = getSlotSizeForHeight(this.guiHandler.botWindow.heightContent);
      int slotSizeForWidth = getSlotSizeForWidth(this.guiHandler.botWindow.widthContent);
      this.slotSize = Math.min(slotSize2, slotSizeForWidth);
      if (this.slotSize % 2 == 1) {
         --this.slotSize;
      }

      if (this.slotSize <= 1) {
         this.slotSize = 2;
      }

      slotSizeForWidth = ((slotSize2 = getSlotDiagonalOffset(this.slotSize)) << 1) + this.slotSize + 6 + 2 * this.slotSize;
      int slotSize3 = (slotSize2 << 1) + this.slotSize;
      slotSizeForWidth = this.guiHandler.botWindow.widthContent / 2 - slotSizeForWidth / 2;
      slotSize3 = this.guiHandler.botWindow.heightContent / 2 - slotSize3 / 2;
      this.personageCenterX = slotSizeForWidth + slotSize2 + this.slotSize / 2;
      this.personageCenterY = slotSize3 + slotSize2 + this.slotSize / 2;
      this.slotsPositions[0][0] = this.personageCenterX - this.slotSize / 2;
      this.slotsPositions[0][1] = this.personageCenterY - slotSize2 - this.slotSize / 2;
      this.slotsPositions[1][0] = (int)((long)this.personageCenterX + 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[1][1] = (int)((long)this.personageCenterY - 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[2][0] = this.personageCenterX + slotSize2 - this.slotSize / 2;
      this.slotsPositions[2][1] = this.personageCenterY - this.slotSize / 2;
      this.slotsPositions[3][0] = (int)((long)this.personageCenterX + 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[3][1] = (int)((long)this.personageCenterY + 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[4][0] = this.personageCenterX - this.slotSize / 2;
      this.slotsPositions[4][1] = this.personageCenterY + slotSize2 - this.slotSize / 2;
      this.slotsPositions[5][0] = this.personageCenterX - slotSize2 - this.slotSize / 2;
      this.slotsPositions[5][1] = this.personageCenterY - this.slotSize / 2;
      this.slotsPositions[6][0] = (int)((long)this.personageCenterX - 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[6][1] = (int)((long)this.personageCenterY - 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[7][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3;
      this.slotsPositions[7][1] = this.personageCenterY - this.slotSize - this.slotSize - 3 - 1;
      this.slotsPositions[8][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3 + this.slotSize + 3;
      this.slotsPositions[8][1] = this.personageCenterY - this.slotSize - this.slotSize - 3 - 1;
      this.slotsPositions[9][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3;
      this.slotsPositions[9][1] = this.personageCenterY - this.slotSize - 1;
      this.slotsPositions[10][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3 + this.slotSize + 3;
      this.slotsPositions[10][1] = this.personageCenterY - this.slotSize - 1;
      this.slotsPositions[11][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3;
      this.slotsPositions[11][1] = this.personageCenterY + 1;
      this.slotsPositions[12][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3 + this.slotSize + 3;
      this.slotsPositions[12][1] = this.personageCenterY + 1;
      this.slotsPositions[13][0] = (int)((long)this.personageCenterX - 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[13][1] = (int)((long)this.personageCenterY + 707106L * (long)slotSize2 / 1000000L - (long)(this.slotSize / 2));
      this.slotsPositions[14][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3;
      this.slotsPositions[14][1] = this.personageCenterY + 1 + this.slotSize + 3;
      this.slotsPositions[15][0] = this.personageCenterX + slotSize2 + this.slotSize / 2 + 3 + this.slotSize + 3;
      this.slotsPositions[15][1] = this.personageCenterY + 1 + this.slotSize + 3;
      this.uiConteiner.clear();
      GUIHandler.positionateSoftButtons(this.justMid, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
      this.uiConteiner.addComponent(this.guiHandler.botWindow);
      this.guiHandler.botWindow.setHandler((UIComponentHandler)this);
   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams();
   }

   public final int getPreferContentHeight(int availHeight) {
      availHeight = getSlotSizeForHeight((availHeight << 1) / 3);
      int getPreferContentWidth = getSlotSizeForWidth(this.getPreferContentWidth());
      if ((availHeight = Math.min(availHeight, getPreferContentWidth)) % 2 == 1) {
         --availHeight;
      }

      getPreferContentWidth = getSlotDiagonalOffset(availHeight);
      return 2 * getPreferContentWidth + availHeight;
   }

   public final void realeasPointerAll() {
      super.realeasPointerAll();
      this.isPress = false;
   }

   public final void clickLeftSoft() {
      super.clickLeftSoft();
      if (this.justMid) {
         this.clickRightSoft(false);
      } else {
         if (this.guiHandler.currentEventListener != null) {
            this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 5, (Object)null);
         }

         if (this.guiHandler.commonEvents != null) {
            this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 5, (Object)null);
         }

      }
   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 4, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 4, (Object)null);
      }

   }

   public final void realeasFire() {
      super.realeasFire();
      this.isPress = false;
      this.guiHandler.botWindow.needRedraw = true;
      if (this.curSlotInd >= 0) {
         if (this.guiHandler.currentEventListener != null) {
            this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 3, this.curSlotInd);
         }

         if (this.guiHandler.commonEvents != null) {
            this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 3, this.curSlotInd);
         }
      }

   }

   public final void pressFire() {
      super.pressFire();
      this.isPress = true;
      this.guiHandler.botWindow.needRedraw = true;
   }

   public final void vertHoldAction(int delta) {
      super.vertHoldAction(delta);
      if (delta != 0) {
         int curSlotInd2;
         while(delta > 0) {
            --delta;
            curSlotInd2 = SLOTS_ORDER_NEXT_KEYBOARD[this.curSlotInd];
            this.setCurSlotInd(curSlotInd2, false);
         }

         while(delta < 0) {
            ++delta;
            curSlotInd2 = SLOTS_ORDER_PREV_KEYBOARD[this.curSlotInd];
            this.setCurSlotInd(curSlotInd2, false);
         }

         this.guiHandler.botWindow.needRedraw = true;
      }
   }

   public final void functional(boolean wasFontAnimation) {
      super.functional(wasFontAnimation);
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 100:
            if (this.slotSize < 0) {
               return;
            } else {
               if (PlatformRuntime.getInstance().getPlatformType() == 1 && (this.curButtonImage == null || this.curButtonImage.getWidth() != this.slotSize || this.curButtonImage.getHeight() != this.slotSize)) {
                  this.curButtonImage = GameImage.createImage(this.slotSize, this.slotSize);
                  this.drawBaseButton((GameGraphics)this.curButtonImage.getGraphics(), 0, 0);
                  this.curButtonSelectImage = GameImage.createImage(this.slotSize, this.slotSize);
                  this.drawSelectButton((GameGraphics)this.curButtonSelectImage.getGraphics(), 0, 0);
                  this.curButtonPressImage = GameImage.createImage(this.slotSize, this.slotSize);
                  this.drawPressButton((GameGraphics)this.curButtonPressImage.getGraphics(), 0, 0);
               }

               int textWidth2;
               if (this.strategGraphic != null) {
                  int ANIMATION_STATE_STAY2 = StrategAnimGroup.ANIMATION_STATE_STAY;
                  boolean unusedProgressMarker = false;
                  int[] paddings = this.strategGraphic.getPaddings((byte)0, (byte)ANIMATION_STATE_STAY2, true);
                  ANIMATION_STATE_STAY2 = this.personageCenterX;
                  textWidth2 = this.personageCenterY + paddings[2] - (paddings[3] + paddings[2]) / 2;
                  gameGraphics2.drawImage(GUIHandler.personageBackgroundImage, ANIMATION_STATE_STAY2 - 37, textWidth2 - 56, 0);
                  this.strategGraphic.draw(gameGraphics2, ANIMATION_STATE_STAY2, textWidth2, (byte)0, StrategAnimGroup.ANIMATION_STATE_STAY, 0L, false);
               }

               for(int slotsPositionIndex = 0; slotsPositionIndex < 16; ++slotsPositionIndex) {
                  int[] integers = this.slotsPositions[slotsPositionIndex];
                  if (PlatformRuntime.getInstance().getPlatformType() == 1) {
                     gameGraphics2.drawImage(this.curSlotInd == slotsPositionIndex ? (this.isPress ? this.curButtonPressImage : this.curButtonSelectImage) : this.curButtonImage, integers[0], integers[1], 0);
                  } else {
                     int slotState = this.curSlotInd == slotsPositionIndex ? (this.isPress ? 2 : 1) : 0;
                     int slotX = integers[0];
                     int slotY = integers[1];
                     int buttonX = slotX;
                     int buttonState = slotState;
                     switch (buttonState) {
                        case 0:
                           this.drawBaseButton(gameGraphics2, buttonX, slotY);
                           break;
                        case 1:
                           this.drawSelectButton(gameGraphics2, buttonX, slotY);
                           break;
                        case 2:
                           this.drawPressButton(gameGraphics2, buttonX, slotY);
                     }
                  }

                  String symb;
                  int textWidth;
                  if ((symb = this.slotsSymbols[slotsPositionIndex]) == null) {
                     GameImage gameImage = GUIHandler.slotImages[slotsPositionIndex];
                     gameGraphics2.drawImage(gameImage, integers[0] + this.slotSize / 2 - gameImage.getWidth() / 2, integers[1] + this.slotSize / 2 - gameImage.getHeight() / 2, 0);
                  } else {
                     textWidth = this.guiHandler.font.getTextWidth(symb);
                     this.guiHandler.font.writeText(gameGraphics2, symb, integers[0] + this.slotSize / 2 - textWidth / 2, integers[1] + this.slotSize / 2 - this.guiHandler.font.fontHeight / 2);
                  }

                  if (this.slotsNumbers != null) {
                     if ((textWidth = this.slotsNumbers[slotsPositionIndex]) > 0) {
                        textWidth2 = this.guiHandler.fontDigit.getTextWidth(String.valueOf(textWidth));
                        this.guiHandler.fontDigit.writeText(gameGraphics2, String.valueOf(textWidth), integers[0] + this.slotSize - textWidth2 - 1, integers[1] + this.slotSize - this.guiHandler.fontDigit.fontHeight - 1);
                     } else if (textWidth == -1) {
                        textWidth2 = this.guiHandler.fontDigit.getTextWidth("-");
                        this.guiHandler.fontDigit.writeText(gameGraphics2, "-", integers[0] + this.slotSize - textWidth2 - 1, integers[1] + this.slotSize - this.guiHandler.fontDigit.fontHeight - 1);
                     }
                  }
               }

               return;
            }
         default:
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, this.leftSoftText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + this.leftSoftText : "", this.rightSoftText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + this.rightSoftText : "", this.midSoftText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + this.midSoftText : "", this.guiHandler.font, this.guiHandler.softSpacing);
      }
   }

   private void drawPressButton(GameGraphics gameGraphics2, int posX, int posY) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(posX, posY, this.slotSize, this.slotSize);
      gameGraphics2.setColor(2119968);
      gameGraphics2.fillRect(posX + GUIDecorator.toothBorderPressImageWidth, posY + GUIDecorator.toothBorderPressImageWidth, Math.max(1, this.slotSize - (GUIDecorator.toothBorderPressImageWidth << 1)), Math.max(1, this.slotSize - (GUIDecorator.toothBorderPressImageWidth << 1)));
      int toothBorderPressImageWidth2 = GUIDecorator.toothBorderPressImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, posX, posY, this.slotSize, this.slotSize, GUIDecorator.toothCornerPressImage, GUIDecorator.toothBorderPressImage, GUIDecorator.toothCornerPressImageWidth, GUIDecorator.toothCornerPressImageHeight, toothBorderPressImageWidth2, toothBorderPressImageWidth2, GUIDecorator.toothBorderPressImageWidth, GUIDecorator.toothBorderPressImageHeight);
   }

   private void drawSelectButton(GameGraphics gameGraphics2, int posX, int posY) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(posX, posY, this.slotSize, this.slotSize);
      gameGraphics2.setColor(4539700);
      gameGraphics2.fillRect(posX + GUIDecorator.toothBorderSelectImageWidth, posY + GUIDecorator.toothBorderSelectImageWidth, Math.max(1, this.slotSize - (GUIDecorator.toothBorderSelectImageWidth << 1)), Math.max(1, this.slotSize - (GUIDecorator.toothBorderSelectImageWidth << 1)));
      int toothBorderSelectImageWidth2 = GUIDecorator.toothBorderSelectImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, posX, posY, this.slotSize, this.slotSize, GUIDecorator.toothCornerSelectImage, GUIDecorator.toothBorderSelectImage, GUIDecorator.toothCornerSelectImageWidth, GUIDecorator.toothCornerSelectImageHeight, toothBorderSelectImageWidth2, toothBorderSelectImageWidth2, GUIDecorator.toothBorderSelectImageWidth, GUIDecorator.toothBorderSelectImageHeight);
   }

   private void drawBaseButton(GameGraphics gameGraphics2, int posX, int posY) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(posX, posY, this.slotSize, this.slotSize);
      int toothBorderImageWidth2 = GUIDecorator.toothBorderImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, posX, posY, this.slotSize, this.slotSize, GUIDecorator.toothCornerImage, GUIDecorator.toothBorderImage, GUIDecorator.toothCornerImageWidth, GUIDecorator.toothCornerImageHeight, toothBorderImageWidth2, toothBorderImageWidth2, GUIDecorator.toothBorderImageWidth, GUIDecorator.toothBorderImageHeight);
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 11:
            this.clickRightSoft(false);
         default:
            return;
         case 12:
            this.clickRightSoft(false);
         case 10:
            this.clickLeftSoft();
      }
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            int slot = this.getSlotUnderCoord(x, y);
            this.isPress = false;
            if (slot >= 0 && !this.preventClick && slot == this.curSlotInd) {
               this.isPress = true;
            }
         default:
      }
   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 100:
            this.isPress = false;
         default:
      }
   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            int slot;
            if ((slot = this.getSlotUnderCoord(x, y)) >= 0) {
               if (slot == this.curSlotInd) {
                  if (!this.preventClick) {
                     this.pressFire();
                     this.realeasFire();
                     return;
                  }
               } else {
                  this.setCurSlotInd(slot, false);
               }
            }
         default:
      }
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
      switch (uIComponent15.id) {
         case 100:
            if (this.uiConteiner.containsPressId(uIComponent15.id) && !first) {
               return;
            } else {
               int slot = this.getSlotUnderCoord(x, y);
               if (first) {
                  this.preventClick = slot != this.curSlotInd;
               }

               if (slot >= 0) {
                  this.setCurSlotInd(slot, false);
               }
            }
         default:
      }
   }

   private int getSlotUnderCoord(int x, int y) {
      for(int i = 0; i < this.slotsPositions.length; ++i) {
         int[] pos = this.slotsPositions[i];
         if (x >= pos[0] && x < pos[0] + this.slotSize && y >= pos[1] && y < pos[1] + this.slotSize) {
            return i;
         }
      }

      return -1;
   }

   public final void releasAll() {
      super.releasAll();
      this.isPress = false;
   }
}
