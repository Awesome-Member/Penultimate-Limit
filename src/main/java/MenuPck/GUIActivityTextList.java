package MenuPck;

import Base.Com;
import Engine.Mystery;
import desktop.graphics.GameGraphics;

public final class GUIActivityTextList extends GUIBaseActivity implements UIComponentHandler {
   private String[] texts;
   private int[] ids;
   private int[] linesWidths;
   private String rightSoftText;
   private int preinitChoosedInd;
   private boolean drawArrowUp;
   private boolean drawArrowDown;
   private AnimateProperty choosedLineHorizOffset = new AnimateProperty();
   private boolean choosedLineHorizOffsetDirection;
   private AnimateProperty vertOffset = new AnimateProperty();
   private int targetVertOffset;
   private int choosedInd;
   private AnimateProperty chooserVertOffset = new AnimateProperty();
   private int timeAccumMouseHold = 0;
   private boolean mouseHoldDown = false;
   private boolean mouseHoldUp = false;
   private boolean haveAnimSymbols = false;

   public GUIActivityTextList(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, 600, 300, -1, -1, context);
   }

   protected final void init(String[] texts2, int[] textsIds, String rightSoftText2, int choosedIndex) {
      this.preinitChoosedInd = choosedIndex;
      this.texts = texts2;
      this.ids = textsIds;
      this.rightSoftText = rightSoftText2;
   }

   public final void activate() {
      super.activate();
      this.guiHandler.hideTopWindow();
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      this.mouseHoldDown = false;
      this.mouseHoldUp = false;
      this.vertOffset.value = 0;
      this.targetVertOffset = 0;
      this.setChoosedInd(this.preinitChoosedInd >= 0 ? this.preinitChoosedInd : (this.texts.length > 0 ? 0 : -1));
      this.linesWidths = new int[this.texts.length];

      for(int i = 0; i < this.texts.length; ++i) {
         try {
            this.linesWidths[i] = this.guiHandler.font.getTextWidth(this.texts[i]);
         } catch (Exception exception) {
            Com.SendMistace("Error in calc line width in menu", true, false, exception);
         }
      }

      this.guiHandler.initWindowsParams();
      this.initTargetVertOffsetByChoosedInd();
      this.vertOffset.value = this.targetVertOffset;
      if (this.choosedInd < 0) {
         this.chooserVertOffset.value = -1;
      } else {
         this.chooserVertOffset.value = this.getRowsHeightWithTrailingDivider(this.choosedInd);
      }

      this.recalcParams();
      this.guiHandler.initVisibiblity();
   }

   public final void forceNewData(String[] texts2, int[] textsIds, int preferChoosedInd) {
      this.texts = texts2;
      this.ids = textsIds;
      if (preferChoosedInd < 0) {
         preferChoosedInd = 0;
      }

      if (preferChoosedInd >= texts2.length) {
         preferChoosedInd = texts2.length - 1;
      }

      if (this.choosedInd != preferChoosedInd) {
         this.setChoosedInd(preferChoosedInd);
      }

      this.linesWidths = new int[this.texts.length];

      for(int i = 0; i < this.texts.length; ++i) {
         try {
            this.linesWidths[i] = this.guiHandler.font.getTextWidth(this.texts[i]);
         } catch (Exception exception) {
            Com.SendMistace("Error in calc line width in menu", true, false, exception);
         }
      }

      this.guiHandler.initWindowsParams(false, true, false);
      this.initHorizontalScroll();
      this.fitVerticalOffset();
      this.initArrows();
      this.initArrowsPositions();
   }

   private int getRowsHeightWithoutTrailingDivider(int rowCount) {
      return rowCount * this.guiHandler.getOneLineHeight() + (rowCount > 1 ? (rowCount - 1) * this.guiHandler.dividerSize : 0);
   }

   private int getRowsHeightWithTrailingDivider(int rowCount) {
      return rowCount * (this.guiHandler.getOneLineHeight() + this.guiHandler.dividerSize);
   }

   protected final void recalcParams() {
      this.initHorizontalScroll();
      this.fitVerticalOffset();
      this.uiConteiner.clear();
      this.uiConteiner.addComponent(this.guiHandler.botWindow);
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 13, 0, false, false));
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 14, 0, false, false));
      this.initArrows();
      this.initArrowsPositions();
      GUIHandler.positionateSoftButtons(this.rightSoftText.length() <= 0, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
      this.guiHandler.botWindow.setHandler((UIComponentHandler)this);
   }

   private void initHorizontalScroll() {
      if (this.choosedInd >= 0) {
         int availWidth = this.getAvailWidth() - 4;
         if (this.linesWidths[this.choosedInd] > availWidth && this.choosedLineHorizOffset.value < -(this.linesWidths[this.choosedInd] - availWidth)) {
            this.choosedLineHorizOffset.value = -(this.linesWidths[this.choosedInd] - availWidth);
            this.choosedLineHorizOffsetDirection = false;
         }
      }

   }

   private void fitVerticalOffset() {
      int listHeight = this.getRowsHeightWithoutTrailingDivider(this.texts.length);
      if (this.guiHandler.botWindow.heightContent >= listHeight) {
         this.targetVertOffset = -((this.guiHandler.botWindow.heightContent - listHeight) / 2);
         this.vertOffset.value = this.targetVertOffset;
      } else {
         if (this.targetVertOffset > listHeight - this.guiHandler.botWindow.heightContent) {
            this.targetVertOffset = listHeight - this.guiHandler.botWindow.heightContent;
            this.vertOffset.value = this.targetVertOffset;
         }

         if (this.targetVertOffset < 0) {
            this.targetVertOffset = 0;
            this.vertOffset.value = this.targetVertOffset;
         }

      }
   }

   private void changeVertOffset(int delta) {
      int rowsHeightWithoutTrailingDivider;
      if ((rowsHeightWithoutTrailingDivider = this.getRowsHeightWithoutTrailingDivider(this.texts.length)) > this.guiHandler.botWindow.heightContent) {
         int vertOffset3 = this.vertOffset.value;
         AnimateProperty vertOffset2 = this.vertOffset;
         vertOffset2.value += delta;
         if (this.vertOffset.value < 0) {
            this.vertOffset.value = 0;
         }

         if (this.vertOffset.value > rowsHeightWithoutTrailingDivider - this.guiHandler.botWindow.heightContent) {
            this.vertOffset.value = rowsHeightWithoutTrailingDivider - this.guiHandler.botWindow.heightContent;
         }

         this.targetVertOffset = this.vertOffset.value;
         if (vertOffset3 != this.vertOffset.value) {
            this.guiHandler.botWindow.needRedraw = true;
            this.initArrows();
         }
      }

   }

   private void initArrows() {
      int listHeight = this.getRowsHeightWithoutTrailingDivider(this.texts.length);
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      if (listHeight > this.guiHandler.botWindow.heightContent) {
         if (this.vertOffset.value > 0) {
            this.drawArrowUp = true;
         }

         if (listHeight - this.vertOffset.value > this.guiHandler.botWindow.heightContent) {
            this.drawArrowDown = true;
         }
      }

   }

   private void initArrowsPositions() {
      UIComponent uIComponent15;
      if ((uIComponent15 = this.uiConteiner.getComponent(13)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.botWindow.yContent - 8, 0, this.guiHandler.botWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

      if ((uIComponent15 = this.uiConteiner.getComponent(14)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.botWindow.yContent + this.guiHandler.botWindow.heightContent - (GUIHandler.arrowImage.getHeight() - 8), 0, this.guiHandler.botWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

   }

   private int getAvailWidth() {
      return this.guiHandler.botWindow.widthContent;
   }

   private void setChoosedInd(int choosedIndex) {
      if (choosedIndex >= this.texts.length) {
         choosedIndex = 0;
      }

      if (choosedIndex < 0) {
         choosedIndex = -1;
      }

      if (this.texts.length <= 0) {
         choosedIndex = -1;
      }

      this.choosedInd = choosedIndex;
      this.choosedLineHorizOffset.value = 0;
      this.choosedLineHorizOffsetDirection = true;
      if (this.choosedInd >= 0) {
         if (this.guiHandler.currentEventListener != null) {
            this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 6, new Integer(this.ids[this.choosedInd]));
         }

         if (this.guiHandler.commonEvents != null) {
            this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 6, new Integer(this.ids[this.choosedInd]));
         }
      }

   }

   private void initTargetVertOffsetByChoosedInd() {
      int oneLineHeight = this.guiHandler.getOneLineHeight();
      int textsCount = this.getRowsHeightWithoutTrailingDivider(this.texts.length);
      if (this.guiHandler.botWindow.heightContent >= textsCount) {
         this.targetVertOffset = -((this.guiHandler.botWindow.heightContent - textsCount) / 2);
      } else {
         if (this.choosedInd < 0) {
            return;
         }

         int rowsHeightWithTrailingDivider;
         oneLineHeight += rowsHeightWithTrailingDivider = this.getRowsHeightWithTrailingDivider(this.choosedInd);
         if (rowsHeightWithTrailingDivider - this.getRowsHeightWithTrailingDivider(1) < this.vertOffset.value) {
            this.targetVertOffset = Math.max(0, rowsHeightWithTrailingDivider - this.getRowsHeightWithTrailingDivider(1));
         }

         if (oneLineHeight + this.getRowsHeightWithTrailingDivider(1) > this.vertOffset.value + this.guiHandler.botWindow.heightContent) {
            this.targetVertOffset = Math.min(textsCount - this.guiHandler.botWindow.heightContent, oneLineHeight + this.getRowsHeightWithTrailingDivider(1) - this.guiHandler.botWindow.heightContent);
         }
      }

      this.guiHandler.botWindow.stopKineticDrag();
   }

   private void switchChoosedInd(int setInd) {
      this.setChoosedInd(setInd);
      this.initTargetVertOffsetByChoosedInd();
   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams();
   }

   public final int getPreferContentHeight(int availHeight) {
      return Math.min((availHeight << 1) / 3, this.getRowsHeightWithoutTrailingDivider(Math.min(6, this.texts.length)));
   }

   public final void functional(boolean wasFontAnimation) {
      super.functional(wasFontAnimation);
      int availWidth;
      if (this.mouseHoldDown || this.mouseHoldUp) {
         this.timeAccumMouseHold = (int)((long)this.timeAccumMouseHold + Mystery.FrameTimeI);
         if (this.timeAccumMouseHold >= 7) {
            availWidth = this.timeAccumMouseHold / 7;
            this.timeAccumMouseHold %= 7;
            if (this.mouseHoldDown) {
               this.changeVertOffset(availWidth);
            }

            if (this.mouseHoldUp) {
               this.changeVertOffset(-availWidth);
            }

            this.guiHandler.botWindow.needRedraw = true;
         }
      }

      availWidth = this.choosedInd >= 0 ? this.getRowsHeightWithTrailingDivider(this.choosedInd) : -1;
      if (this.chooserVertOffset.animateFixTime(availWidth, !this.guiHandler.animateMenu, 140L)) {
         this.guiHandler.botWindow.needRedraw = true;
      }

      if (this.vertOffset.animateFixTime(this.targetVertOffset, !this.guiHandler.animateMenu, 140L)) {
         this.guiHandler.botWindow.needRedraw = true;
         this.initArrows();
      }

      availWidth = this.getAvailWidth();
      if (this.choosedInd >= 0 && this.linesWidths[this.choosedInd] > availWidth) {
         availWidth = this.choosedLineHorizOffsetDirection ? -(this.linesWidths[this.choosedInd] - availWidth) : 0;
         long marqueeDelay = 2500L;
         boolean unusedProgressMarker = false;
         AnimateProperty choosedLineHorizOffset2 = this.choosedLineHorizOffset;
         boolean unusedProgressMarker2 = false;
         if (choosedLineHorizOffset2.value != availWidth) {
            label107: {
               if (choosedLineHorizOffset2.fastAnimate) {
                  choosedLineHorizOffset2.value = availWidth;
               } else {
                  choosedLineHorizOffset2.accumTime += Mystery.FrameTimeI * 100L;
                  if (2500L <= 0L || choosedLineHorizOffset2.accumTime < 2500L) {
                     break label107;
                  }

                  long accumTime = choosedLineHorizOffset2.accumTime / 2500L;
                  choosedLineHorizOffset2.accumTime %= 2500L;
                  if (availWidth > choosedLineHorizOffset2.value) {
                     choosedLineHorizOffset2.value = (int)((long)choosedLineHorizOffset2.value + Math.min((long)(availWidth - choosedLineHorizOffset2.value), accumTime));
                  } else {
                     choosedLineHorizOffset2.value = (int)((long)choosedLineHorizOffset2.value - Math.min((long)(choosedLineHorizOffset2.value - availWidth), accumTime));
                  }

                  if (availWidth == choosedLineHorizOffset2.value) {
                     choosedLineHorizOffset2.accumTime = 0L;
                  }
               }

               unusedProgressMarker2 = true;
            }
         }

         choosedLineHorizOffset2.fastAnimate = false;
         if (this.choosedLineHorizOffset.value == availWidth) {
            this.choosedLineHorizOffsetDirection = !this.choosedLineHorizOffsetDirection;
         }

         this.guiHandler.botWindow.needRedraw = true;
      }

      if (this.guiHandler.animateMenu && wasFontAnimation && this.haveAnimSymbols) {
         this.guiHandler.topWindow.needRedraw = true;
      }

   }

   public final void realeasPointerAll() {
      super.realeasPointerAll();
      this.mouseHoldUp = false;
      this.mouseHoldDown = false;
   }

   public final void clickLeftSoft() {
      super.clickLeftSoft();
      this.guiHandler.pressFire();
      this.guiHandler.releaseFire();
   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 8, byBackgroundClick ? Boolean.TRUE : Boolean.FALSE);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 8, byBackgroundClick ? Boolean.TRUE : Boolean.FALSE);
      }

   }

   public final void vertHoldAction(int delta) {
      super.vertHoldAction(delta);
      if (this.texts.length > 0) {
         this.choosedInd += delta;
         this.choosedInd %= this.texts.length;
         if (this.choosedInd < 0) {
            this.choosedInd += this.texts.length;
         }

         this.switchChoosedInd(this.choosedInd);
      }

   }

   public final void pressFire() {
      super.pressFire();
      if (this.choosedInd >= 0) {
         if (this.guiHandler.currentEventListener != null) {
            this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 7, new Integer(this.ids[this.choosedInd]));
         }

         if (this.guiHandler.commonEvents != null) {
            this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 7, new Integer(this.ids[this.choosedInd]));
         }
      }

   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 13:
            if (this.drawArrowUp) {
               gameGraphics2.drawImage(GUIHandler.arrowImage, (uIComponent15.widthContent - GUIHandler.arrowImage.getWidth()) / 2, 0, 0);
               return;
            }
            break;
         case 14:
            if (this.drawArrowDown) {
               gameGraphics2.drawRegion(GUIHandler.arrowImage, 0, 0, GUIHandler.arrowImage.getWidth(), GUIHandler.arrowImage.getHeight(), 1, (uIComponent15.widthContent - GUIHandler.arrowImage.getWidth()) / 2, 0);
               return;
            }
            break;
         case 100:
            int availWidth = this.getAvailWidth();
            int oneLineHeight = this.guiHandler.getOneLineHeight();
            int rowsHeightWithTrailingDivider = this.getRowsHeightWithTrailingDivider(1);
            int maximum = Math.max(0, this.vertOffset.value / rowsHeightWithTrailingDivider);
            int textsCount = Math.min(this.texts.length - 1, (this.vertOffset.value + this.guiHandler.botWindow.heightContent) / rowsHeightWithTrailingDivider);
            int availableRowWidth = availWidth - 4;
            int vertOffset2 = -this.vertOffset.value + maximum * rowsHeightWithTrailingDivider;
            int rowY;
            if (this.guiHandler.dividerSize > 0) {
               rowY = vertOffset2;

               for(int lengthIndex = maximum; lengthIndex <= textsCount; ++lengthIndex) {
                  if (lengthIndex < this.texts.length - 1) {
                     gameGraphics2.setColor(1580832);
                     gameGraphics2.fillRect(0, rowY + oneLineHeight, availWidth, this.guiHandler.dividerSize);
                  }

                  rowY += rowsHeightWithTrailingDivider;
               }
            }

            if (this.chooserVertOffset.value >= 0) {
               gameGraphics2.setColor(5263440);
               gameGraphics2.fillRect(0, this.chooserVertOffset.value - this.vertOffset.value, availWidth, oneLineHeight);
            }

            boolean haveAnim = false;
            rowY = vertOffset2;

            for(oneLineHeight = maximum; oneLineHeight <= textsCount; ++oneLineHeight) {
               maximum = this.linesWidths[oneLineHeight];
               if (oneLineHeight == this.choosedInd) {
                  if (this.guiHandler.font.writeText(gameGraphics2, this.texts[oneLineHeight], 2 + (maximum > availableRowWidth ? this.choosedLineHorizOffset.value : (availableRowWidth - maximum) / 2), rowY + this.guiHandler.lineSpacing)) {
                     haveAnim = true;
                  }
               } else if (this.guiHandler.font.writeText(gameGraphics2, this.texts[oneLineHeight], 2 + (maximum > availableRowWidth ? 0 : (availableRowWidth - maximum) / 2), rowY + this.guiHandler.lineSpacing, availWidth, 'ə')) {
                  haveAnim = true;
               }

               rowY += rowsHeightWithTrailingDivider;
            }

            this.haveAnimSymbols = haveAnim;
            return;
         default:
            String messageText = "Далее";
            String rightSoftText2 = this.rightSoftText;
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, messageText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + messageText : "", rightSoftText2.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + rightSoftText2 : "", messageText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + messageText : "", this.guiHandler.font, this.guiHandler.softSpacing);
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10:
            this.clickLeftSoft();
            return;
         case 11:
            this.clickRightSoft(false);
         default:
            return;
         case 12:
            this.clickLeftSoft();
      }
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            if (dy != 0) {
               this.changeVertOffset(-dy);
            }
         default:
      }
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 13:
            if (this.drawArrowUp && !this.mouseHoldUp) {
               this.mouseHoldUp = true;
               this.timeAccumMouseHold = 7;
               return;
            }
            break;
         case 14:
            if (this.drawArrowDown && !this.mouseHoldDown) {
               this.mouseHoldDown = true;
               this.timeAccumMouseHold = 7;
            }
      }

   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      int rowIndex;
      switch (uIComponent15.id) {
         case 13:
            if (this.drawArrowUp && this.getRowsHeightWithoutTrailingDivider(this.texts.length) > this.guiHandler.botWindow.heightContent && this.mouseHoldUp) {
               rowIndex = this.vertOffset.value / this.getRowsHeightWithTrailingDivider(1);
               this.targetVertOffset = this.getRowsHeightWithTrailingDivider(rowIndex);
               return;
            }
            break;
         case 14:
            if (this.drawArrowDown && this.getRowsHeightWithoutTrailingDivider(this.texts.length) > this.guiHandler.botWindow.heightContent && this.mouseHoldDown) {
               rowIndex = (this.vertOffset.value + this.guiHandler.botWindow.heightContent) / this.getRowsHeightWithTrailingDivider(1);
               this.targetVertOffset = this.getRowsHeightWithoutTrailingDivider(rowIndex + 1) - this.guiHandler.botWindow.heightContent;
            }
      }

   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
      switch (uIComponent15.id) {
         case 100:
            if (dy != 0) {
               this.changeVertOffset(-dy);
               this.guiHandler.botWindow.needRedraw = true;
            }
         default:
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            int vertOffset2;
            x = (vertOffset2 = y + this.vertOffset.value) / this.getRowsHeightWithTrailingDivider(1);
            if (vertOffset2 <= this.getRowsHeightWithoutTrailingDivider(x + 1) && x >= 0 && x < this.texts.length) {
               if (x == this.choosedInd) {
                  this.guiHandler.pressFire();
                  this.guiHandler.releaseFire();
                  return;
               } else {
                  this.switchChoosedInd(x);
               }
            }
         default:
      }
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 100:
            this.guiHandler.initWindowsParams();
            this.initArrowsPositions();
         default:
      }
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }

   public final void releasAll() {
      super.releasAll();
      this.mouseHoldDown = false;
      this.mouseHoldUp = false;
   }
}
