package MenuPck;

import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import desktop.graphics.GameGraphics;

public final class GUIActivityNumber extends GUIActivityKeyboard implements UIComponentHandler {
   private int minValue;
   private int maxValue;
   private String hintText;
   private MystText infoText = new MystText();

   public GUIActivityNumber(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, new String[][][]{{{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "̺̻"}}}, -1, -1, 10, 0, -1, -1, -1, -1, context);
   }

   protected final void init(int curVal, int minVal, int maxVal, String hintText2) {
      this.minValue = minVal;
      this.maxValue = maxVal;
      this.hintText = hintText2;
      this.currentText = String.valueOf(curVal);
   }

   public final void activate() {
      super.activate();
   }

   protected final void recalcParams() {
      String messageText = Mystery.stringFormat("от % до %:", '%', new String[]{String.valueOf(this.minValue), String.valueOf(this.maxValue)});
      this.infoText = new MystText();
      MystString mystString;
      (mystString = new MystString()).Create(messageText, this.guiHandler.font);
      this.infoText.create(mystString, this.getWidthForText(), this.guiHandler.textSpacing, this.guiHandler.font);
      this.guiHandler.initWindowsParams();
      super.recalcParams();
   }

   private int getWidthForText() {
      return Math.max(1, this.guiHandler.botWindow.widthContent - 4);
   }

   protected final int[] getButtonsParams() {
      return super.getButtonsParams();
   }

   public final int getBotWindowAddingOffset() {
      return GUIHandler.getSoftkeyHeight(this.guiHandler.font, this.guiHandler.softSpacing) + 8;
   }

   protected final int getKeyboardTopPadding() {
      return (this.infoText.getRowsCount() + 1) * (this.guiHandler.font.fontHeight + this.guiHandler.textSpacing) + 2;
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      super.paintUIComponent(uIComponent15, gameGraphics2);
      switch (uIComponent15.id) {
         case 100:
            this.infoText.draw(gameGraphics2, 2, 2, this.getWidthForText(), this.getKeyboardTopPadding(), 0, 0, this.guiHandler.font);
            String messageText = this.currentText + this.hintText;
            int w = this.guiHandler.font.getTextWidth(messageText);
            this.guiHandler.font.writeText(gameGraphics2, messageText, (uIComponent15.widthContent - w) / 2, this.infoText.getRowsCount() * (this.guiHandler.font.fontHeight + this.guiHandler.textSpacing) + 2);
         default:
      }
   }

   protected final String onLeftSoftText() {
      return this.isGoodText(this.currentText) ? super.onLeftSoftText() : "̀́В рамки";
   }

   protected final String onFixText(String substring) {
      while(substring.length() > 1 && substring.charAt(0) == '0') {
         substring = substring.substring(1);
      }

      if (substring.length() == 0) {
         substring = "0";
      }

      for(String s = String.valueOf(this.maxValue); substring.length() > s.length() + 1; substring = substring.substring(0, substring.length() - 1)) {
      }

      return substring;
   }

   protected final void onTextChanged(String messageText) {
      this.guiHandler.botWindow.needRedraw = true;
      int val = this.textToVal(messageText);
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 9, val);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 9, val);
      }

   }

   protected final void onTextSelected(String messageText) {
      int val;
      if (this.isGoodText(messageText)) {
         val = this.textToVal(messageText);
         if (this.guiHandler.currentEventListener != null) {
            this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 11, val);
         }

         if (this.guiHandler.commonEvents != null) {
            this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 11, val);
         }

      } else {
         val = this.textToVal(messageText);
         this.currentText = String.valueOf(val);
         this.currentText = this.onFixText(this.currentText);
         this.onTextChanged(this.currentText);
      }
   }

   protected final void onTextCancel() {
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 10, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 10, (Object)null);
      }

   }

   private boolean isGoodText(String messageText) {
      int val;
      try {
         val = Integer.parseInt(messageText);
      } catch (Exception exception) {
         return false;
      }

      return val >= this.minValue && val <= this.maxValue;
   }

   private int textToVal(String messageText) {
      int val;
      try {
         val = Integer.parseInt(messageText);
      } catch (Exception exception) {
         val = this.minValue;
      }

      if (val < this.minValue) {
         return this.minValue;
      } else {
         return val > this.maxValue ? this.maxValue : val;
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      super.clickUIComponent(uIComponent15);
      switch (uIComponent15.id) {
         case 10:
            this.clickLeftSoft();
            return;
         case 11:
            this.clickRightSoft(false);
         default:
      }
   }
}
