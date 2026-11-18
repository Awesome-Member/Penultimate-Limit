package MenuPck;

import Engine.Mystery;
import desktop.PlatformRuntime;
import Resources.StringResources;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public class GUIActivityKeyboard extends GUIBaseActivity implements UIComponentHandler {
   protected String currentText;
   private int curKeyboardInd;
   private boolean isPress;
   private boolean isHold;
   private int curSymbX;
   private int curSymbY;
   private GameImage curButtonImage;
   private GameImage curButtonSelectImage;
   private GameImage curButtonPressImage;
   private int paddingForBorder;
   private String[][][] symbolsStrings;
   private int[][][] symbolsWidths;
   private int symbolChangeKeyboardX;
   private int symbolChangeKeyboardY;
   private int symbolDelX;
   private int symbolDelY;
   private int symbolYesX;
   private int symbolYesY;
   private int symbolNoX;
   private int symbolNoY;
   private UIComponent holdButtonCoverComponent;

   public GUIActivityKeyboard(GUIHandler gUIHandler, String[][][] symbolsStrings2, int symbolChangeKeyboardX2, int symbolChangeKeyboardY2, int symbolDelX2, int symbolDelY2, int symbolYesX2, int symbolYesY2, int symbolNoX2, int symbolNoY2, Object context) {
      super(gUIHandler, 300, 300, 300, 300, context);
      this.curKeyboardInd = 0;
      this.isPress = false;
      this.isHold = false;
      this.curSymbX = 0;
      this.curSymbY = 0;
      this.curButtonImage = null;
      this.curButtonSelectImage = null;
      this.curButtonPressImage = null;
      this.symbolsStrings = null;
      this.symbolsWidths = null;
      this.symbolChangeKeyboardX = -1;
      this.symbolChangeKeyboardY = -1;
      this.symbolDelX = -1;
      this.symbolDelY = -1;
      this.symbolYesX = -1;
      this.symbolYesY = -1;
      this.symbolNoX = -1;
      this.symbolNoY = -1;
      this.holdButtonCoverComponent = new UIComponent(this, GUIHandler.menuDecorator, 500, 0, false);
      this.symbolsStrings = symbolsStrings2;
      this.symbolChangeKeyboardX = symbolChangeKeyboardX2;
      this.symbolChangeKeyboardY = symbolChangeKeyboardY2;
      this.symbolDelX = symbolDelX2;
      this.symbolDelY = symbolDelY2;
      this.symbolYesX = symbolYesX2;
      this.symbolYesY = symbolYesY2;
      this.symbolNoX = symbolNoX2;
      this.symbolNoY = symbolNoY2;
      this.holdButtonCoverComponent.ignoreInTouch = true;
   }

   public GUIActivityKeyboard(GUIHandler gUIHandler, Object context) {
      this(gUIHandler, StringResources.insSymbols, StringResources.insSymbols[0][0].length - 4, StringResources.insSymbols[0].length - 1, StringResources.insSymbols[0][0].length - 3, StringResources.insSymbols[0].length - 1, StringResources.insSymbols[0][0].length - 2, StringResources.insSymbols[0].length - 1, StringResources.insSymbols[0][0].length - 1, StringResources.insSymbols[0].length - 1, context);
   }
   protected final void init(String startText, int startKeyboard) {
      this.currentText = startText;
      this.curKeyboardInd = startKeyboard;
   }

   public void activate() {
      super.activate();
      this.symbolsWidths = new int[this.symbolsStrings.length][][];

      for(int symbolsWidthIndex = 0; symbolsWidthIndex < this.symbolsStrings.length; ++symbolsWidthIndex) {
         this.symbolsWidths[symbolsWidthIndex] = new int[this.symbolsStrings[symbolsWidthIndex].length][];

         for(int symbolsStringsIndex = 0; symbolsStringsIndex < this.symbolsStrings[symbolsWidthIndex].length; ++symbolsStringsIndex) {
            this.symbolsWidths[symbolsWidthIndex][symbolsStringsIndex] = new int[this.symbolsStrings[symbolsWidthIndex][symbolsStringsIndex].length];

            for(int symbolsStringsIndex2 = 0; symbolsStringsIndex2 < this.symbolsStrings[symbolsWidthIndex][symbolsStringsIndex].length; ++symbolsStringsIndex2) {
               this.symbolsWidths[symbolsWidthIndex][symbolsStringsIndex][symbolsStringsIndex2] = this.guiHandler.font.getTextWidth(this.symbolsStrings[symbolsWidthIndex][symbolsStringsIndex][symbolsStringsIndex2]);
            }
         }
      }

      this.paddingForBorder = GUIDecorator.toothBorderImageWidth;
      this.guiHandler.hideTopWindow();
      this.guiHandler.initWindowsParams();
      this.recalcParams();
      this.currentText = this.onFixText(this.currentText);
      this.onTextChanged(this.currentText);
      this.guiHandler.initVisibiblity();
   }

   protected int getKeyboardTopPadding() {
      return 0;
   }

   protected String onFixText(String messageText) {
      return messageText;
   }

   protected void onTextChanged(String messageText) {
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 12, messageText);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 12, messageText);
      }

   }

   protected void onTextSelected(String messageText) {
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 13, messageText);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 13, messageText);
      }

   }

   protected void onTextCancel() {
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 14, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 14, (Object)null);
      }

   }

   protected void recalcParams() {
      this.uiConteiner.clear();
      GUIHandler.positionateSoftButtons(false, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
      this.uiConteiner.addComponent(this.guiHandler.botWindow);
      this.uiConteiner.addComponent(this.holdButtonCoverComponent);
      this.initHoldButtonCoverComponent();
      this.guiHandler.botWindow.setHandler((UIComponentHandler)this);
   }

   private void initHoldButtonCoverComponent() {
      int[] buttonsParams = this.getButtonsParams();
      this.holdButtonCoverComponent.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 0, this.guiHandler.botWindow.xContent + buttonsParams[0] + this.curSymbX * buttonsParams[2] - 3, 4, this.guiHandler.botWindow.yContent + buttonsParams[1] + this.curSymbY * buttonsParams[3] - buttonsParams[3] - 3 - 11, 1, buttonsParams[2] + 6, 1, buttonsParams[3] + 6 + (GUIDecorator.toothArrowPressImageHeight - GUIDecorator.toothBorderPressImageWidth));
      this.holdButtonCoverComponent.needRedraw = true;
   }

   protected int[] getButtonsParams() {
      int maximum = Math.max(1, this.guiHandler.botWindow.heightContent - this.getKeyboardTopPadding());
      int maximum2 = Math.max(1, this.guiHandler.botWindow.widthContent / this.getKeyboardColumnCount());
      int maximum3 = Math.max(1, maximum / this.getKeyboardRowCount());
      maximum = this.getKeyboardTopPadding() + (maximum - maximum3 * this.getKeyboardRowCount()) / 2;
      int keyboardOffsetX = (this.guiHandler.botWindow.widthContent - maximum2 * this.getKeyboardColumnCount()) / 2;
      return new int[]{keyboardOffsetX, maximum, maximum2, maximum3};
   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams();
   }

   public int getBotWindowAddingOffset() {
      return 0;
   }

   public final int getPreferContentHeight(int availHeight) {
      int availableKeyboardHeight = availHeight - GUIActivityTopText.getLinesHeight(2, this.guiHandler);
      int keyboardRowCount = this.getKeyboardRowCount();
      return Math.min(availableKeyboardHeight, keyboardRowCount * (this.guiHandler.getOneLineHeight() + (this.paddingForBorder << 1)) + this.getKeyboardTopPadding());
   }

   public final int getPreferContentWidth() {
      return (this.guiHandler.screenWidth - (GUIDecorator.CLAW_CORNER_OFF_X << 1)) / this.getKeyboardColumnCount() * this.getKeyboardColumnCount();
   }

   private int getKeyboardRowCount() {
      return this.symbolsStrings[this.curKeyboardInd].length;
   }

   private int getKeyboardColumnCount() {
      return this.symbolsStrings[this.curKeyboardInd][0].length;
   }

   public final void realeasPointerAll() {
      super.realeasPointerAll();
      if (this.isHold) {
         this.realeasFire();
      }

      this.isHold = false;
      this.isPress = false;
   }

   public final void clickLeftSoft() {
      super.clickLeftSoft();
      this.onTextSelected(this.currentText);
   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      if (!byBackgroundClick) {
         this.onTextCancel();
      }

   }

   public final void realeasFire() {
      super.realeasFire();
      if (this.isPress) {
         this.isPress = false;
         this.guiHandler.botWindow.needRedraw = true;
         boolean wasSpec = false;
         if (this.curSymbX == this.symbolChangeKeyboardX && this.curSymbY == this.symbolChangeKeyboardY) {
            wasSpec = true;
            this.curKeyboardInd = Mystery.positiveModulo(this.curKeyboardInd + 1, this.symbolsStrings.length);
            this.guiHandler.botWindow.needRedraw = true;
         }

         if (this.curSymbX == this.symbolNoX && this.curSymbY == this.symbolNoY) {
            wasSpec = true;
            this.clickRightSoft(false);
         }

         if (this.curSymbX == this.symbolYesX && this.curSymbY == this.symbolYesY) {
            wasSpec = true;
            this.clickLeftSoft();
         }

         if (this.curSymbX == this.symbolDelX && this.curSymbY == this.symbolDelY) {
            wasSpec = true;
            if (this.currentText.length() > 0) {
               this.currentText = this.currentText.substring(0, this.currentText.length() - 1);
               this.currentText = this.onFixText(this.currentText);
               this.onTextChanged(this.currentText);
            }
         }

         if (!wasSpec) {
            String symb = this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX];
            this.currentText = this.currentText + symb;
            this.currentText = this.onFixText(this.currentText);
            this.onTextChanged(this.currentText);
         }

      }
   }

   public final void pressFire() {
      super.pressFire();
      this.isPress = true;
      this.guiHandler.botWindow.needRedraw = true;
   }

   public final void horizHoldAction(int delta) {
      super.horizHoldAction(delta);
      if (delta != 0) {
         String curSymb = this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX];
         int keyboardColumnCount = this.getKeyboardColumnCount();
         this.curSymbX = Mystery.positiveModulo(this.curSymbX + delta, keyboardColumnCount);

         for(int keyboardColumnIndex = 0; this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX].equals(curSymb) && keyboardColumnIndex <= keyboardColumnCount; ++keyboardColumnIndex) {
            this.curSymbX = Mystery.positiveModulo(this.curSymbX + (delta > 0 ? 1 : -1), keyboardColumnCount);
         }

         this.guiHandler.botWindow.needRedraw = true;
      }
   }

   public final void vertHoldAction(int delta) {
      super.vertHoldAction(delta);
      if (delta != 0) {
         String curSymb = this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX];
         int keyboardRowCount = this.getKeyboardRowCount();
         this.curSymbY = Mystery.positiveModulo(this.curSymbY + delta, keyboardRowCount);

         for(int keyboardRowIndex = 0; this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX].equals(curSymb) && keyboardRowIndex <= keyboardRowCount; ++keyboardRowIndex) {
            this.curSymbY = Mystery.positiveModulo(this.curSymbY + (delta > 0 ? 1 : -1), keyboardRowCount);
         }

         this.guiHandler.botWindow.needRedraw = true;
      }
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      int[] buttonsParams;
      int getButtonsParams = (buttonsParams = this.getButtonsParams())[2];
      int buttonHeight = buttonsParams[3];
      String messageText;
      int columnCount;
      int borderWidth;
      switch (uIComponent15.id) {
         case 100:
            if (PlatformRuntime.getInstance().getPlatformType() == 1 && (this.curButtonImage == null || this.curButtonImage.getWidth() != getButtonsParams || this.curButtonImage.getHeight() != buttonHeight)) {
               this.curButtonImage = GameImage.createImage(getButtonsParams, buttonHeight);
               drawBaseButton(getButtonsParams, buttonHeight, this.curButtonImage.getGraphics(), 0, 0);
               this.curButtonSelectImage = GameImage.createImage(getButtonsParams, buttonHeight);
               drawSelectButton(getButtonsParams, buttonHeight, this.curButtonSelectImage.getGraphics(), 0, 0);
               this.curButtonPressImage = GameImage.createImage(getButtonsParams, buttonHeight);
               draw(getButtonsParams, buttonHeight, this.curButtonPressImage.getGraphics(), 0, 0);
            }

            String messageText2 = this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX];
            int buttonY = buttonsParams[1];
            int rowCount = this.symbolsStrings[this.curKeyboardInd].length;

            for(int rowIndex = 0; rowIndex < rowCount; ++rowIndex) {
               int buttonX = buttonsParams[0];
               columnCount = this.symbolsStrings[this.curKeyboardInd][rowIndex].length;

               for(int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
                  String messageText3 = this.symbolsStrings[this.curKeyboardInd][rowIndex][columnIndex];
                  boolean[] bordersForKey = this.getBordersForKey(columnIndex, rowIndex, messageText3);
                  if (messageText3.equals(messageText2)) {
                     if (bordersForKey[0] && bordersForKey[1] && bordersForKey[2] && bordersForKey[3]) {
                        if (PlatformRuntime.getInstance().getPlatformType() == 1) {
                           gameGraphics2.drawImage(this.isPress ? this.curButtonPressImage : this.curButtonSelectImage, buttonX, buttonY, 0);
                        } else {
                           this.draw(this.isPress ? 2 : 1, getButtonsParams, buttonHeight, gameGraphics2, buttonX, buttonY);
                        }
                     } else if (this.isPress) {
                        borderWidth = GUIDecorator.toothBorderPressImageWidth;
                        UIComponent.drawBordersInner(gameGraphics2, buttonX, buttonY, getButtonsParams, buttonHeight, GUIDecorator.toothCornerPressImage, GUIDecorator.toothBorderPressImage, GUIDecorator.toothCornerPressImageWidth, GUIDecorator.toothCornerPressImageHeight, borderWidth, borderWidth, GUIDecorator.toothBorderPressImageWidth, GUIDecorator.toothBorderPressImageHeight, bordersForKey[0], bordersForKey[1], bordersForKey[2], bordersForKey[3], 2119968);
                     } else {
                        borderWidth = GUIDecorator.toothBorderSelectImageWidth;
                        UIComponent.drawBordersInner(gameGraphics2, buttonX, buttonY, getButtonsParams, buttonHeight, GUIDecorator.toothCornerSelectImage, GUIDecorator.toothBorderSelectImage, GUIDecorator.toothCornerSelectImageWidth, GUIDecorator.toothCornerSelectImageHeight, borderWidth, borderWidth, GUIDecorator.toothBorderSelectImageWidth, GUIDecorator.toothBorderSelectImageHeight, bordersForKey[0], bordersForKey[1], bordersForKey[2], bordersForKey[3], 4539700);
                     }
                  } else if (bordersForKey[0] && bordersForKey[1] && bordersForKey[2] && bordersForKey[3]) {
                     if (PlatformRuntime.getInstance().getPlatformType() == 1) {
                        gameGraphics2.drawImage(this.curButtonImage, buttonX, buttonY, 0);
                     } else {
                        this.draw(0, getButtonsParams, buttonHeight, gameGraphics2, buttonX, buttonY);
                     }
                  } else {
                     borderWidth = GUIDecorator.toothBorderImageWidth;
                     UIComponent.drawBordersInner(gameGraphics2, buttonX, buttonY, getButtonsParams, buttonHeight, GUIDecorator.toothCornerImage, GUIDecorator.toothBorderImage, GUIDecorator.toothCornerImageWidth, GUIDecorator.toothCornerImageHeight, borderWidth, borderWidth, GUIDecorator.toothBorderImageWidth, GUIDecorator.toothBorderImageHeight, bordersForKey[0], bordersForKey[1], bordersForKey[2], bordersForKey[3], 263172);
                  }

                  int symbolWidth = this.symbolsWidths[this.curKeyboardInd][rowIndex][columnIndex];
                  this.guiHandler.font.writeText(gameGraphics2, messageText3, buttonX + (getButtonsParams - symbolWidth) / 2, buttonY + (buttonHeight - this.guiHandler.font.fontHeight) / 2);
                  buttonX += getButtonsParams;
               }

               buttonY += buttonHeight;
            }

            return;
         case 500:
            if (this.isHold) {
               messageText = this.symbolsStrings[this.curKeyboardInd][this.curSymbY][this.curSymbX];
               boolean[] bordersForKey2;
               if ((bordersForKey2 = this.getBordersForKey(this.curSymbX, this.curSymbY, messageText))[0] && bordersForKey2[1] && bordersForKey2[2] && bordersForKey2[3]) {
                  int panelHeight = uIComponent15.height - (GUIDecorator.toothArrowPressImageHeight - GUIDecorator.toothBorderPressImageWidth);
                  borderWidth = GUIDecorator.toothBorderPressImageWidth;
                  UIComponent.drawBordersInner(gameGraphics2, 0, 0, uIComponent15.width, panelHeight, GUIDecorator.toothCornerPressImage, GUIDecorator.toothBorderPressImage, GUIDecorator.toothCornerPressImageWidth, GUIDecorator.toothCornerPressImageHeight, borderWidth, borderWidth, GUIDecorator.toothBorderPressImageWidth, GUIDecorator.toothBorderPressImageHeight, true, true, true, true, 2119968);
                  gameGraphics2.drawImage(GUIDecorator.toothArrowPressImage, uIComponent15.width / 2 - GUIDecorator.toothArrowPressImageWidth / 2, uIComponent15.height - GUIDecorator.toothArrowPressImageHeight, 0);
                  columnCount = this.symbolsWidths[this.curKeyboardInd][this.curSymbY][this.curSymbX];
                  this.guiHandler.font.writeText(gameGraphics2, messageText, (uIComponent15.widthContent - columnCount) / 2, (buttonHeight - this.guiHandler.font.fontHeight) / 2);
               }

               return;
            }
            break;
         default:
            messageText = this.onLeftSoftText();
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, messageText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + messageText : "", this.guiHandler.currentEventListener.getSoftPrefix() + "ʔʕНет", "", this.guiHandler.font, this.guiHandler.softSpacing);
      }

   }

   private void draw(int buttonState, int width, int height, GameGraphics gameGraphics2, int x, int y) {
      switch (buttonState) {
         case 0:
            drawBaseButton(width, height, gameGraphics2, x, y);
            return;
         case 1:
            drawSelectButton(width, height, gameGraphics2, x, y);
            return;
         case 2:
            draw(width, height, gameGraphics2, x, y);
         default:
      }
   }

   private static void draw(int width, int height, GameGraphics gameGraphics2, int x, int y) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(x, y, width, height);
      gameGraphics2.setColor(2119968);
      gameGraphics2.fillRect(x + GUIDecorator.toothBorderPressImageWidth, y + GUIDecorator.toothBorderPressImageWidth, Math.max(1, width - (GUIDecorator.toothBorderPressImageWidth << 1)), Math.max(1, height - (GUIDecorator.toothBorderPressImageWidth << 1)));
      int toothBorderPressImageWidth2 = GUIDecorator.toothBorderPressImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, x, y, width, height, GUIDecorator.toothCornerPressImage, GUIDecorator.toothBorderPressImage, GUIDecorator.toothCornerPressImageWidth, GUIDecorator.toothCornerPressImageHeight, toothBorderPressImageWidth2, toothBorderPressImageWidth2, GUIDecorator.toothBorderPressImageWidth, GUIDecorator.toothBorderPressImageHeight);
   }

   private static void drawSelectButton(int buttonWidth, int buttonHeight, GameGraphics gameGraphics2, int posX, int posY) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(posX, posY, buttonWidth, buttonHeight);
      gameGraphics2.setColor(4539700);
      gameGraphics2.fillRect(posX + GUIDecorator.toothBorderSelectImageWidth, posY + GUIDecorator.toothBorderSelectImageWidth, Math.max(1, buttonWidth - (GUIDecorator.toothBorderSelectImageWidth << 1)), Math.max(1, buttonHeight - (GUIDecorator.toothBorderSelectImageWidth << 1)));
      int toothBorderSelectImageWidth2 = GUIDecorator.toothBorderSelectImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, posX, posY, buttonWidth, buttonHeight, GUIDecorator.toothCornerSelectImage, GUIDecorator.toothBorderSelectImage, GUIDecorator.toothCornerSelectImageWidth, GUIDecorator.toothCornerSelectImageHeight, toothBorderSelectImageWidth2, toothBorderSelectImageWidth2, GUIDecorator.toothBorderSelectImageWidth, GUIDecorator.toothBorderSelectImageHeight);
   }

   private static void drawBaseButton(int buttonWidth, int buttonHeight, GameGraphics gameGraphics2, int posX, int posY) {
      gameGraphics2.setColor(263172);
      gameGraphics2.fillRect(posX, posY, buttonWidth, buttonHeight);
      int toothBorderImageWidth2 = GUIDecorator.toothBorderImageWidth;
      UIComponent.drawBordersInner(gameGraphics2, posX, posY, buttonWidth, buttonHeight, GUIDecorator.toothCornerImage, GUIDecorator.toothBorderImage, GUIDecorator.toothCornerImageWidth, GUIDecorator.toothCornerImageHeight, toothBorderImageWidth2, toothBorderImageWidth2, GUIDecorator.toothBorderImageWidth, GUIDecorator.toothBorderImageHeight);
   }

   protected String onLeftSoftText() {
      return "ʒʓДа";
   }

   private boolean[] getBordersForKey(int ix, int iy, String compSymb) {
      boolean leftBorder = ix <= 0 || !this.symbolsStrings[this.curKeyboardInd][iy][ix - 1].equals(compSymb);
      boolean rightBorder = ix >= this.symbolsStrings[this.curKeyboardInd][iy].length - 1 || !this.symbolsStrings[this.curKeyboardInd][iy][ix + 1].equals(compSymb);
      boolean topBorder = iy <= 0 || !this.symbolsStrings[this.curKeyboardInd][iy - 1][ix].equals(compSymb);
      boolean bottomBorder = iy >= this.symbolsStrings[this.curKeyboardInd].length - 1 || !this.symbolsStrings[this.curKeyboardInd][iy + 1][ix].equals(compSymb);
      return new boolean[]{leftBorder, rightBorder, topBorder, bottomBorder};
   }

   public void clickUIComponent(UIComponent uIComponent15) {
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
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
      switch (uIComponent15.id) {
         case 100:
            int[] buttonsParams = this.getButtonsParams();
            this.isHold = false;
            this.isPress = false;
            if (x >= buttonsParams[0] && y >= buttonsParams[1] && x < buttonsParams[0] + buttonsParams[2] * this.getKeyboardColumnCount() && y < buttonsParams[1] + buttonsParams[3] * this.getKeyboardRowCount()) {
               this.isHold = true;
               this.isPress = true;
               x = (x - buttonsParams[0]) / buttonsParams[2];
               int curSymbY2 = (y - buttonsParams[1]) / buttonsParams[3];
               this.curSymbX = x;
               this.curSymbY = curSymbY2;
               this.initHoldButtonCoverComponent();
            }

            this.guiHandler.botWindow.needRedraw = true;
         default:
      }
   }

   public final void releasAll() {
      super.releasAll();
      this.isHold = false;
      this.isPress = false;
   }
}
