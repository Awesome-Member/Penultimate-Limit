package MenuPck;

import Engine.Mystery;

public class GUIBaseActivity {
   public UIConteiner uiConteiner = new UIConteiner();
   protected GUIHandler guiHandler;
   private int timeAccumHoldVert = 0;
   private boolean holdDown = false;
   private boolean holdUp = false;
   private int holdVertStartTime = -1;
   private int holdVertRepeatTime = -1;
   private int timeAccumHoldHoriz = 0;
   private boolean holdLeft = false;
   private boolean holdRight = false;
   private int holdHorizStartTime = -1;
   private int holdHorizRepeatTime = -1;
   protected Object context = null;

   public GUIBaseActivity(GUIHandler gUIHandler, Object context2) {
      this.guiHandler = gUIHandler;
      this.context = context2;
   }

   public GUIBaseActivity(GUIHandler gUIHandler, int holdVertStartTime2, int holdVertRepeatTime2, int holdHorizStartTime2, int holdHorizRepeatTime2, Object context2) {
      this.holdVertStartTime = holdVertStartTime2;
      this.holdVertRepeatTime = holdVertRepeatTime2;
      this.holdHorizStartTime = holdHorizStartTime2;
      this.holdHorizRepeatTime = holdHorizRepeatTime2;
      this.guiHandler = gUIHandler;
      this.context = context2;
   }

   public void activate() {
      this.holdDown = false;
      this.holdLeft = false;
      this.holdRight = false;
      this.holdUp = false;
   }

   protected void recalcParams() {
   }

   public void setScreenSize(int screenWidth, int screenHeight) {
   }

   public boolean a_() {
      return true;
   }

   public int getBotWindowAddingOffset() {
      return GUIHandler.getSoftkeyHeight(this.guiHandler.font, this.guiHandler.softSpacing) + 8;
   }

   public int getPreferContentHeight(int availHeight) {
      return 0;
   }

   public int getPreferContentWidth() {
      int w;
      if ((w = this.guiHandler.screenWidth - (GUIDecorator.SNAKE_TOP_OFF_X << 1)) > 240) {
         w = 240;
      }

      if (w < 188) {
         w = Math.min(188, this.guiHandler.screenWidth - (GUIDecorator.SNAKE_BORDER_SIZE_X << 1));
      }

      return w;
   }

   public void functional(boolean wasFontAnimation) {
      int count;
      if (this.holdVertRepeatTime > 0 && (this.holdDown || this.holdUp)) {
         this.timeAccumHoldVert = (int)((long)this.timeAccumHoldVert + Mystery.FrameTimeI);
         if (this.timeAccumHoldVert >= this.holdVertRepeatTime) {
            count = this.timeAccumHoldVert / this.holdVertRepeatTime;
            this.timeAccumHoldVert %= this.holdVertRepeatTime;
            if (this.holdUp) {
               this.vertHoldAction(-count);
            }

            if (this.holdDown) {
               this.vertHoldAction(count);
            }
         }
      }

      if (this.holdHorizRepeatTime > 0 && (this.holdLeft || this.holdRight)) {
         this.timeAccumHoldHoriz = (int)((long)this.timeAccumHoldHoriz + Mystery.FrameTimeI);
         if (this.timeAccumHoldHoriz >= this.holdHorizRepeatTime) {
            count = this.timeAccumHoldHoriz / this.holdHorizRepeatTime;
            this.timeAccumHoldHoriz %= this.holdHorizRepeatTime;
            if (this.holdLeft) {
               this.horizHoldAction(-count);
            }

            if (this.holdRight) {
               this.horizHoldAction(count);
            }
         }
      }

   }

   public void realeasPointerAll() {
   }

   public void clickLeftSoft() {
   }

   public void clickRightSoft(boolean byBackgroundClick) {
   }

   public final void pressUp() {
      if (this.holdVertRepeatTime > 0 && !this.holdUp) {
         this.holdUp = true;
         this.timeAccumHoldVert = this.holdVertRepeatTime - this.holdVertStartTime;
         this.vertHoldAction(-1);
      }

   }

   public final void pressDown() {
      if (this.holdVertRepeatTime > 0 && !this.holdDown) {
         this.holdDown = true;
         this.timeAccumHoldVert = this.holdVertRepeatTime - this.holdVertStartTime;
         this.vertHoldAction(1);
      }

   }

   public final void pressLeft() {
      if (this.holdHorizRepeatTime > 0 && !this.holdLeft) {
         this.holdLeft = true;
         this.timeAccumHoldHoriz = this.holdHorizRepeatTime - this.holdHorizStartTime;
         this.horizHoldAction(-1);
      }

   }

   public final void pressRight() {
      if (this.holdHorizRepeatTime > 0 && !this.holdRight) {
         this.holdRight = true;
         this.timeAccumHoldHoriz = this.holdHorizRepeatTime - this.holdHorizStartTime;
         this.horizHoldAction(1);
      }

   }

   public void pressFire() {
   }

   public void realeasFire() {
   }

   public final void releasUpAll() {
      this.holdUp = false;
   }

   public final void releasDownAll() {
      this.holdDown = false;
   }

   public final void releasRightAll() {
      this.holdRight = false;
   }

   public final void releasLeftAll() {
      this.holdLeft = false;
   }

   public void horizHoldAction(int delta) {
   }

   public void vertHoldAction(int delta) {
   }

   public void releasAll() {
      this.holdLeft = false;
      this.holdRight = false;
      this.holdUp = false;
      this.holdDown = false;
   }
}
