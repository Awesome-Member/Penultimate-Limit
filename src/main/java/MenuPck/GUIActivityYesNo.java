package MenuPck;

import desktop.graphics.GameGraphics;

public final class GUIActivityYesNo extends GUIBaseActivity implements UIComponentHandler {
   private String specYes = null;
   private String specNo = null;

   public GUIActivityYesNo(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, context);
   }

   public GUIActivityYesNo(GUIHandler gUIHandler, String specYes2, String specNo2, Object context) {
      super(gUIHandler, context);
      this.specYes = specYes2;
      this.specNo = specNo2;
   }

   public final void activate() {
      super.activate();
      this.guiHandler.hideTopWindow();
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 16, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 16, (Object)null);
      }

      this.guiHandler.initWindowsParams();
      this.recalcParams();
      this.guiHandler.initVisibiblity();
   }

   protected final void recalcParams() {
      this.uiConteiner.clear();
      GUIHandler.positionateSoftButtons(false, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
      this.guiHandler.botWindow.setHandler((UIComponentHandler)this);
   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams();
   }

   public final int getPreferContentHeight(int availHeight) {
      return 0;
   }

   public final boolean a_() {
      return false;
   }

   public final void clickLeftSoft() {
      super.clickLeftSoft();
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 15, new boolean[]{true, false});
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 15, new boolean[]{true, false});
      }

   }

   public final void realeasFire() {
      super.realeasFire();
      this.clickLeftSoft();
   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 15, new boolean[]{false, byBackgroundClick});
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 15, new boolean[]{false, byBackgroundClick});
      }

   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      String messageText = this.specYes == null ? "ʒʓДа" : this.specYes;
      String messageText2 = this.specNo == null ? "ʔʕНет" : this.specNo;
      GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, messageText.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + messageText : "", messageText2.length() > 0 ? this.guiHandler.currentEventListener.getSoftPrefix() + messageText2 : "", "", this.guiHandler.font, this.guiHandler.softSpacing);
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 10:
            this.clickLeftSoft();
            return;
         case 11:
            this.clickRightSoft(false);
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
   }
}
