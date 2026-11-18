package MenuPck;

import Engine.MystString;
import Engine.MystText;
import Moduls.StrategAnimGroup;
import Moduls.StrategGraphic;
import Moduls.Legionery;
import java.util.Vector;
import desktop.graphics.GameGraphics;

public final class GUIActivityTopText extends GUIBaseActivity implements UIComponentHandler {
   private String text;
   private MystText textProcessed;
   private MystText textAddingProcessed;
   private int[][] textZones = null;
   private boolean drawArrowUp;
   private boolean drawArrowDown;
   private int vertOffset = 0;
   private boolean haveAnimSymbols = false;
   private StrategGraphic decorStrateg = null;
   private boolean decorUnitShow = false;
   private int decorUnitHead = -1;
   private int decorUnitBody = -1;
   private boolean decorUnitsShow = false;
   private int[][] decorUnitsDesc = null;
   private Vector decorUnitsByLines = null;

   public GUIActivityTopText(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, -1, -1, 7, 7, (Object)null);
   }

   protected final void initDecorAvatar(StrategGraphic strategGraphic2) {
      this.decorStrateg = strategGraphic2;
   }

   protected final void hideDecorAvatar() {
      this.decorStrateg = null;
   }

   protected final void initDecorUnit(int head, int body) {
      this.decorUnitShow = true;
      this.decorUnitHead = head;
      this.decorUnitBody = body;
   }

   protected final void hideDecorUnit() {
      this.decorUnitShow = false;
      this.decorUnitHead = -1;
      this.decorUnitBody = -1;
   }

   protected final void initDecorUnits(int[][] units) {
      if (units != null && units.length > 0) {
         this.decorUnitsShow = true;
         this.decorUnitsDesc = units;
      } else {
         this.hideDecorUnits();
      }
   }

   protected final void hideDecorUnits() {
      this.decorUnitsShow = false;
      this.decorUnitsDesc = null;
      this.decorUnitsByLines = null;
   }

   protected final void initText(String messageText) {
      this.text = messageText;
   }

   public final void activate() {
      this.activate(true);
   }

   public final void activate(boolean animHeight) {
      super.activate();
      this.guiHandler.currentTopGUIActivity = this;
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      this.vertOffset = 0;
      this.guiHandler.initWindowsParams(animHeight);
      this.recalcParams(animHeight);
      this.guiHandler.initVisibiblity();
   }

   private int getDecorUnitsHeight() {
      int[] params;
      return this.getTopContentHeight() + (this.decorUnitsShow && this.decorUnitsByLines != null ? (params = (int[])((Vector)this.decorUnitsByLines.elementAt(this.decorUnitsByLines.size() - 1)).elementAt(0))[2] + params[1] : 0) + (this.textAddingProcessed == null ? 0 : getLinesHeight(this.textAddingProcessed.getRowsCount(), this.guiHandler));
   }

   private int getTopContentHeight() {
      return Math.max(this.getUnitPortraitHeight(), Math.max(this.getTextHeight(), this.getAvatarHeight()));
   }

   private int getAvatarHeight() {
      if (this.decorStrateg == null) {
         return 0;
      } else {
         byte ANIMATION_STATE_STAY2 = StrategAnimGroup.ANIMATION_STATE_STAY;
         boolean unusedProgressMarker = false;
         int[] paddings = this.decorStrateg.getPaddings((byte)0, ANIMATION_STATE_STAY2, true);
         return Math.max(GUIHandler.personageBackgroundImage.getHeight() - 56, paddings[3]) + Math.max(56, paddings[2]) + 6;
      }
   }

   private int getAvatarWidth() {
      if (this.decorStrateg == null) {
         return 0;
      } else {
         byte ANIMATION_STATE_STAY2 = StrategAnimGroup.ANIMATION_STATE_STAY;
         boolean unusedProgressMarker = false;
         int[] paddings = this.decorStrateg.getPaddings((byte)0, ANIMATION_STATE_STAY2, true);
         return Math.max(26, paddings[0]) + Math.max(26, paddings[1]) + 6;
      }
   }

   private int getUnitPortraitHeight() {
      if (!this.decorUnitShow) {
         return 0;
      } else {
         int[] paddings = Legionery.getPaddings(this.decorUnitBody, this.decorUnitHead, 0);
         return Math.max(GUIHandler.personageBackgroundImage.getHeight() - 56, paddings[3]) + Math.max(56, paddings[2]) + 6;
      }
   }

   private int getDecorUnitWidth() {
      if (!this.decorUnitShow) {
         return 0;
      } else {
         int[] paddings = Legionery.getPaddings(this.decorUnitBody, this.decorUnitHead, 0);
         return Math.max(26, paddings[0]) + Math.max(26, paddings[1]) + 6;
      }
   }

   private int getTextHeight() {
      return this.textProcessed == null ? 0 : getLinesHeight(this.textProcessed.getRowsCount(), this.guiHandler);
   }

   protected static int getLinesHeight(int linesCount, GUIHandler gUIHandler) {
      return linesCount * gUIHandler.font.fontHeight + (linesCount > 0 ? linesCount - 1 : 0) * gUIHandler.textSpacing;
   }

   private int getWidthForText() {
      return Math.max(1, this.guiHandler.topWindow.widthContent - 22);
   }

   protected final void recalcParams() {
      this.recalcParams(true);
   }

   private void addTextZone(int[] zone) {
      if (this.textZones == null) {
         this.textZones = new int[0][0];
      }

      int[][] textZones2 = this.textZones;
      this.textZones = new int[textZones2.length + 1][];
      System.arraycopy(textZones2, 0, this.textZones, 0, textZones2.length);
      if (this.textZones.length >= 2) {
         zone[0] += this.textZones[this.textZones.length - 2][1];
         zone[1] += this.textZones[this.textZones.length - 2][1];
      }

      this.textZones[this.textZones.length - 1] = zone;
   }

   private void recalcParams(boolean animHeight) {
      this.textZones = null;
      if (this.decorStrateg != null) {
         this.addTextZone(new int[]{0, this.getAvatarHeight(), this.getAvatarWidth(), 0, 0});
      }

      if (this.decorUnitShow) {
         this.addTextZone(new int[]{0, this.getUnitPortraitHeight(), this.getDecorUnitWidth(), 0, 0});
      }

      String messageText;
      int separatorIndex = (messageText = this.text == null ? "" : this.text).indexOf("ကခ");
      String messageText2 = messageText;
      String substring = null;
      if (separatorIndex >= 0) {
         messageText2 = messageText.substring(0, separatorIndex - 1);
         substring = messageText.substring(separatorIndex + 2);
      }

      this.textProcessed = new MystText();
      MystString mystString;
      (mystString = new MystString()).Create(messageText2, this.guiHandler.font);
      this.textProcessed.create(mystString, this.getWidthForText(), this.guiHandler.textSpacing, this.guiHandler.font, this.textZones, 0);
      this.textAddingProcessed = null;
      if (substring != null) {
         this.textAddingProcessed = new MystText();
         (mystString = new MystString()).Create(substring, this.guiHandler.font);
         this.textAddingProcessed.create(mystString, this.getWidthForText(), this.guiHandler.textSpacing, this.guiHandler.font, (int[][])null, 0);
      }

      this.decorUnitsByLines = null;
      if (this.decorUnitsShow) {
         this.decorUnitsByLines = new Vector();
         Vector vector3 = new Vector();
         this.decorUnitsByLines.addElement(vector3);
         int[] integers = new int[4];
         vector3.addElement(integers);
         int rowOffsetY = 0;
         int currentRowWidth = 0;
         int maximum = 0;
         int maximum2 = 0;

         for(int decorUnitsDescIndex = 0; decorUnitsDescIndex < this.decorUnitsDesc.length; ++decorUnitsDescIndex) {
            int decorUnitsDesc2 = this.decorUnitsDesc[decorUnitsDescIndex][0];
            int decorUnitsDesc3;
            int[] paddings = Legionery.getPaddings(decorUnitsDesc3 = this.decorUnitsDesc[decorUnitsDescIndex][1], decorUnitsDesc2, 0);
            int maximum3;
            int unitWidth = (maximum3 = Math.max(26, paddings[0]) + Math.max(26, paddings[1])) + (currentRowWidth > 0 ? 3 : 0);
            int maximum4 = Math.max(56, paddings[2]);
            int maximum5 = Math.max(GUIHandler.personageBackgroundImage.getHeight() - 56, paddings[3]);
            if (currentRowWidth + unitWidth > this.guiHandler.topWindow.widthContent && currentRowWidth > 0) {
               integers[0] = currentRowWidth;
               integers[1] = maximum + maximum2;
               integers[2] = rowOffsetY;
               integers[3] = maximum;
               currentRowWidth = 0;
               rowOffsetY += maximum + maximum2;
               maximum = 0;
               maximum2 = 0;
               unitWidth = maximum3;
               vector3 = new Vector();
               this.decorUnitsByLines.addElement(vector3);
               integers = new int[4];
               vector3.addElement(integers);
            }

            currentRowWidth += unitWidth;
            maximum = Math.max(maximum, maximum4);
            maximum2 = Math.max(maximum2, maximum5);
            vector3.addElement(new int[]{decorUnitsDesc2, decorUnitsDesc3});
         }

         if (currentRowWidth > 0) {
            integers[0] = currentRowWidth;
            integers[1] = maximum + maximum2;
            integers[2] = rowOffsetY;
            integers[3] = maximum;
         }
      }

      this.guiHandler.initWindowsParams(animHeight);
      int decorUnitsHeight = this.getDecorUnitsHeight();
      if (this.guiHandler.topWindow.heightContentByPosition >= decorUnitsHeight) {
         this.vertOffset = -(this.guiHandler.topWindow.heightContentByPosition / 2 - decorUnitsHeight / 2);
      } else {
         if (this.vertOffset > decorUnitsHeight - this.guiHandler.topWindow.heightContentByPosition) {
            this.vertOffset = decorUnitsHeight - this.guiHandler.topWindow.heightContentByPosition;
         }

         if (this.vertOffset < 0) {
            this.vertOffset = 0;
         }
      }

      this.uiConteiner.clear();
      this.uiConteiner.addComponent(this.guiHandler.topWindow);
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 15, 0, false, false));
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 16, 0, false, false));
      this.initArrows();
      this.initArrowsPositions();
      this.guiHandler.topWindow.setHandler((UIComponentHandler)this);
   }

   private void changeVertOffset(int delta) {
      int decorUnitsHeight;
      if ((decorUnitsHeight = this.getDecorUnitsHeight()) > this.guiHandler.topWindow.heightContentByPosition) {
         int vertOffset2 = this.vertOffset;
         this.vertOffset += delta;
         if (this.vertOffset < 0) {
            this.vertOffset = 0;
         }

         if (this.vertOffset > decorUnitsHeight - this.guiHandler.topWindow.heightContentByPosition) {
            this.vertOffset = decorUnitsHeight - this.guiHandler.topWindow.heightContentByPosition;
         }

         if (vertOffset2 != this.vertOffset) {
            this.guiHandler.topWindow.needRedraw = true;
            this.initArrows();
         }
      }

   }

   private void initArrows() {
      int contentHeight = this.getDecorUnitsHeight();
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      if (contentHeight > this.guiHandler.topWindow.heightContentByPosition) {
         if (this.vertOffset > 0) {
            this.drawArrowUp = true;
         }

         if (contentHeight - this.vertOffset > this.guiHandler.topWindow.heightContentByPosition) {
            this.drawArrowDown = true;
         }
      }

   }

   private void initArrowsPositions() {
      UIComponent uIComponent15;
      if ((uIComponent15 = this.uiConteiner.getComponent(15)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.topWindow.yContent - 8, 0, this.guiHandler.topWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

      if ((uIComponent15 = this.uiConteiner.getComponent(16)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.topWindow.yContent + this.guiHandler.topWindow.heightContent - (GUIHandler.arrowImage.getHeight() - 8), 0, this.guiHandler.topWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams(true);
   }

   public final int getPreferContentHeight(int availHeight) {
      return this.getDecorUnitsHeight();
   }

   public final void functional(boolean wasFontAnimation) {
      super.functional(wasFontAnimation);
      if (this.guiHandler.animateMenu && wasFontAnimation && this.haveAnimSymbols) {
         this.guiHandler.topWindow.needRedraw = true;
      }

   }

   public final void realeasPointerAll() {
      super.realeasPointerAll();
      this.releasRightAll();
      this.releasLeftAll();
   }

   public final void horizHoldAction(int delta) {
      super.horizHoldAction(delta);
      this.changeVertOffset(delta);
      this.guiHandler.topWindow.needRedraw = true;
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 15:
            if (this.drawArrowUp) {
               gameGraphics2.drawImage(GUIHandler.arrowImage, uIComponent15.widthContent / 2 - GUIHandler.arrowImage.getWidth() / 2, 0, 0);
               return;
            }
            break;
         case 16:
            if (this.drawArrowDown) {
               gameGraphics2.drawRegion(GUIHandler.arrowImage, 0, 0, GUIHandler.arrowImage.getWidth(), GUIHandler.arrowImage.getHeight(), 1, uIComponent15.widthContent / 2 - GUIHandler.arrowImage.getWidth() / 2, 0);
               return;
            }
            break;
         case 101:
            int portraitOffsetY = 0;
            int topContentHeight;
            boolean showPortrait;
            int[] paddings;
            int widthContent;
            if (this.decorStrateg != null) {
               topContentHeight = StrategAnimGroup.ANIMATION_STATE_STAY;
               boolean unusedProgressMarker = false;
               paddings = this.decorStrateg.getPaddings((byte)0, (byte)topContentHeight, true);
               showPortrait = false;
               if (this.textZones.length > 0 && this.textZones[0][4] == 1) {
                  showPortrait = true;
               }

               widthContent = showPortrait ? uIComponent15.widthContent / 2 : 14 + Math.max(26, paddings[0]);
               portraitOffsetY = Math.max(56, paddings[2]) + 3 - this.vertOffset;
               gameGraphics2.drawImage(GUIHandler.personageBackgroundImage, widthContent - 37, portraitOffsetY - 56, 0);
               this.decorStrateg.draw(gameGraphics2, widthContent, portraitOffsetY, (byte)0, StrategAnimGroup.ANIMATION_STATE_STAY, 0L, true);
               portraitOffsetY = 0 + this.getAvatarHeight();
            }

            if (this.decorUnitShow) {
               paddings = Legionery.getPaddings(this.decorUnitBody, this.decorUnitHead, 0);
               showPortrait = false;
               if (this.textZones.length > 0 && this.textZones[0][4] == 1) {
                  showPortrait = true;
               }

               widthContent = showPortrait ? uIComponent15.widthContent / 2 : 14 + Math.max(26, paddings[0]);
               portraitOffsetY += Math.max(56, paddings[2]) + 3 - this.vertOffset;
               int decorUnitHead2 = this.decorUnitHead;
               int decorUnitBody2 = this.decorUnitBody;
               gameGraphics2.drawImage(GUIHandler.personageBackgroundImage, widthContent - 37, portraitOffsetY - 56, 0);
               Legionery.draw(gameGraphics2, widthContent, portraitOffsetY, decorUnitBody2, decorUnitHead2, 0, 0, 0);
            }

            this.haveAnimSymbols = this.textProcessed.draw(gameGraphics2, 0, 0, uIComponent15.widthContent, uIComponent15.heightContent, -this.vertOffset, 0, this.guiHandler.font, this.textZones, 0);
            topContentHeight = this.getTopContentHeight();
            if (this.decorUnitsShow && this.decorUnitsByLines != null) {
               this.drawUnitsLines(uIComponent15, gameGraphics2, topContentHeight, 0);
               this.drawUnitsLines(uIComponent15, gameGraphics2, topContentHeight, 1);
               int[] decorUnitsByLinesCount = (int[])((Vector)this.decorUnitsByLines.elementAt(this.decorUnitsByLines.size() - 1)).elementAt(0);
               topContentHeight += decorUnitsByLinesCount[2] + decorUnitsByLinesCount[1];
            }

            if (this.textAddingProcessed != null && this.textAddingProcessed.draw(gameGraphics2, 0, 0, uIComponent15.widthContent, uIComponent15.heightContent, -this.vertOffset + topContentHeight, 0, this.guiHandler.font, (int[][])null, 0)) {
               this.haveAnimSymbols = true;
            }
      }

   }

   private void drawUnitsLines(UIComponent uIComponent15, GameGraphics gameGraphics2, int addingOffset, int type) {
      for(int sizeIndex = 0; sizeIndex < this.decorUnitsByLines.size(); ++sizeIndex) {
         Vector vector3;
         int[] integers = (int[])(vector3 = (Vector)this.decorUnitsByLines.elementAt(sizeIndex)).elementAt(0);
         int widthContent = (uIComponent15.widthContent - integers[0]) / 2;
         int vertOffset2 = -this.vertOffset + addingOffset + integers[2] + integers[3];

         for(int sizeIndex2 = 1; sizeIndex2 < vector3.size(); ++sizeIndex2) {
            int[] paddings = Legionery.getPaddings((integers = (int[])vector3.elementAt(sizeIndex2))[1], integers[0], 0);
            int unitX = widthContent + Math.max(26, paddings[0]);
            switch (type) {
               case 0:
                  gameGraphics2.drawImage(GUIHandler.personageBackgroundImage, unitX - 37, vertOffset2 - 56, 0);
                  break;
               case 1:
                  Legionery.draw(gameGraphics2, unitX, vertOffset2, integers[1], integers[0], 0, 0, 0);
            }

            widthContent += Math.max(26, paddings[0]) + Math.max(26, paddings[1]) + 3;
         }
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
      switch (uIComponent15.id) {
         case 101:
            if (dy != 0) {
               this.changeVertOffset(-dy);
               this.guiHandler.topWindow.needRedraw = true;
            }
         default:
      }
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
      switch (uIComponent15.id) {
         case 101:
            if (dy != 0) {
               this.changeVertOffset(-dy);
            }
         default:
      }
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 15:
            if (this.drawArrowUp) {
               this.pressLeft();
               return;
            }
            break;
         case 16:
            if (this.drawArrowDown) {
               this.pressRight();
            }
      }

   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 15:
            if (this.drawArrowUp) {
               this.releasLeftAll();
               return;
            }
            break;
         case 16:
            if (this.drawArrowDown) {
               this.releasRightAll();
            }
      }

   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 101:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 101:
            this.guiHandler.initWindowsParams();
            this.initArrowsPositions();
         default:
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 101:
            if (delta != 0) {
               this.recalcParams(true);
            }
         default:
      }
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 101:
            if (delta != 0) {
               this.recalcParams(true);
            }
         default:
      }
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 101:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }
}
