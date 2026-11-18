package MenuPck;

import desktop.graphics.GameGraphics;

public final class GUIActivityOK extends GUIBaseActivity implements UIComponentHandler {
   public GUIActivityOK(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, context);
   }

   public final void activate() {
      super.activate();
      this.guiHandler.hideTopWindow();
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 18, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 18, (Object)null);
      }

      this.guiHandler.initWindowsParams();
      this.recalcParams();
      this.guiHandler.initVisibiblity();
   }

   protected final void recalcParams() {
      this.uiConteiner.clear();
      GUIHandler.positionateSoftButtons(true, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
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
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 17, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 17, (Object)null);
      }

   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      this.clickLeftSoft();
   }

   public final void realeasFire() {
      super.realeasFire();
      this.clickLeftSoft();
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      String val;
      if ((val = this.guiHandler.currentEventListener == null ? "" : this.guiHandler.currentEventListener.getSoftPrefix()) == null) {
         val = "";
      }

      GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, "", "", val + "ʒʓOK", this.guiHandler.font, this.guiHandler.softSpacing);
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 12:
            this.clickLeftSoft();
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
