package Engine;

public final class Camera {
   private int longStayTimeDelay = 1000;
   private int longStayTimeAccum = 0;
   private boolean activeLongStay = true;
   private boolean onActiveLongStay = true;
   private int cx = 0;
   private int cy = 0;
   private int cw = 200;
   private int ch = 250;
   private int centerX;
   private int centerY;
   private int halfW;
   private int halfH;
   private int limitX = 0;
   private int limitY = 0;
   private int limitW = 0;
   private int limitH = 0;
   private boolean activeSmooth = false;
   private boolean smoothPhaseAllowed = false;
   private int smoothPhaseFromX = 0;
   private int smoothPhaseFromY = 0;
   private int targetCenterX = 0;
   private int targetCenterY = 0;
   private int smoothByTime = 0;
   private long smoothPhaseTimeAcc = 0L;
   private int lastOnAnimateCenterX = -1;
   private int lastOnAnimateCenterY = -1;

   public Camera(int cX, int cY, int cW, int cH, int limitX2, int limitY2, int limitW2, int limitH2, int smoothByTime2) {
      this.smoothByTime = 700;
      this.limitX = 0;
      this.limitY = 0;
      this.limitW = 1;
      this.limitH = 1;
      this.halfW = this.cw / 2;
      this.halfH = this.ch / 2;
      this.centerX = this.cx + this.halfW;
      this.centerY = this.cy + this.halfH;
      this.fixPosition();
   }

   public final void resetStayTimer() {
      this.longStayTimeAccum = 0;
      this.activeLongStay = false;
      this.onActiveLongStay = false;
   }

   public final boolean isActiveLongStay() {
      return this.activeLongStay;
   }

   public final boolean isOnActiveLongStay() {
      return this.onActiveLongStay;
   }

   public final boolean isPointVisible(int px, int py, int borderX, int borderY) {
      return px >= this.cx - 11 && px < this.cx + this.cw + 11 && py >= this.cy - 36 && py < this.cy + this.ch + 36;
   }

   public final boolean isPointVisible(int px, int py) {
      return px >= this.cx && px < this.cx + this.cw && py >= this.cy && py < this.cy + this.ch;
   }

   public final boolean framePoint(int px, int py, int[] res) {
      boolean ret = false;
      px -= this.cx;
      py -= this.cy;
      int outcode;
      if (px < 0) {
         px = 0;
         outcode = 4;
         ret = true;
      } else if (px >= this.cw) {
         px = this.cw - 1;
         outcode = 8;
         ret = true;
      } else {
         outcode = 1;
      }

      if (py < 0) {
         py = 0;
         outcode |= 16;
         ret = true;
      } else if (py >= this.ch) {
         py = this.ch - 1;
         outcode |= 32;
         ret = true;
      } else {
         outcode |= 2;
      }

      res[0] = px;
      res[1] = py;
      res[2] = outcode;
      return ret;
   }

   private void fixPosition() {
      if (this.limitW <= this.cw) {
         this.cx = this.limitX + (this.limitW - this.cw) / 2;
         this.centerX = this.cx + this.halfW;
      } else {
         if (this.cx < this.limitX) {
            this.cx = this.limitX;
            this.centerX = this.cx + this.halfW;
         }

         if (this.cx + this.cw > this.limitX + this.limitW) {
            this.cx = this.limitX + this.limitW - this.cw;
            this.centerX = this.cx + this.halfW;
         }
      }

      if (this.limitH <= this.ch) {
         this.cy = this.limitY + (this.limitH - this.ch) / 2;
         this.centerY = this.cy + this.halfH;
      } else {
         if (this.cy < this.limitY) {
            this.cy = this.limitY;
            this.centerY = this.cy + this.halfH;
         }

         if (this.cy + this.ch > this.limitY + this.limitH) {
            this.cy = this.limitY + this.limitH - this.ch;
            this.centerY = this.cy + this.halfH;
         }

      }
   }

   public final int getCenterX() {
      return this.centerX;
   }

   public final int getCenterY() {
      return this.centerY;
   }

   public final void resetSmooth() {
      this.activeSmooth = false;
   }

   public final void smoothMoveTo(int x, int y) {
      if (!this.activeSmooth) {
         this.smoothPhaseAllowed = true;
         this.smoothPhaseFromX = this.centerX;
         this.smoothPhaseFromY = this.centerY;
         this.smoothPhaseTimeAcc = 0L;
      }

      this.targetCenterX = x;
      this.targetCenterY = y;
      this.activeSmooth = true;
   }

   public final void animate() {
      if (this.activeSmooth) {
         int centerX2;
         int centerY2;
         if (this.smoothPhaseAllowed) {
            this.smoothPhaseTimeAcc += Mystery.FrameTimeI;
            this.smoothPhaseTimeAcc = Math.min(this.smoothPhaseTimeAcc, (long)this.smoothByTime);
            centerX2 = (int)((long)(this.targetCenterX - this.smoothPhaseFromX) * this.smoothPhaseTimeAcc / (long)this.smoothByTime + (long)this.smoothPhaseFromX);
            centerY2 = (int)((long)(this.targetCenterY - this.smoothPhaseFromY) * this.smoothPhaseTimeAcc / (long)this.smoothByTime + (long)this.smoothPhaseFromY);
            if (this.smoothPhaseTimeAcc == (long)this.smoothByTime) {
               this.smoothPhaseAllowed = false;
               this.smoothPhaseTimeAcc = 0L;
            }
         } else {
            centerX2 = this.targetCenterX;
            centerY2 = this.targetCenterY;
         }

         if (this.centerX != centerX2 || this.centerY != centerY2) {
            this.centerX = centerX2;
            this.cx = this.centerX - this.halfW;
            this.centerY = centerY2;
            this.cy = this.centerY - this.halfH;
            this.fixPosition();
         }
      }

      if (this.lastOnAnimateCenterX != this.centerX || this.lastOnAnimateCenterY != this.centerY) {
         this.longStayTimeAccum = 0;
         this.activeLongStay = false;
      }

      this.onActiveLongStay = false;
      boolean activeLongStay2 = this.activeLongStay;
      if (!this.activeLongStay) {
         this.longStayTimeAccum = (int)((long)this.longStayTimeAccum + Mystery.FrameTimeI);
         if (this.longStayTimeAccum >= this.longStayTimeDelay) {
            this.activeLongStay = true;
         }
      }

      if (!activeLongStay2 && this.activeLongStay) {
         this.onActiveLongStay = true;
      }

      this.lastOnAnimateCenterX = this.centerX;
      this.lastOnAnimateCenterY = this.centerY;
   }

   public final void centerAt(int x, int y) {
      if (this.centerX != x || this.centerY != y) {
         this.centerX = x;
         this.cx = this.centerX - this.halfW;
         this.centerY = y;
         this.cy = this.centerY - this.halfH;
         this.fixPosition();
         this.activeSmooth = false;
      }
   }

   public final int getCx() {
      return this.cx;
   }

   public final void setCx(int cx2) {
      if (this.cx != cx2) {
         this.cx = cx2;
         this.centerX = cx2 + this.halfW;
         this.fixPosition();
         this.activeSmooth = false;
      }
   }

   public final int getCy() {
      return this.cy;
   }

   public final void setCy(int cy2) {
      if (this.cy != cy2) {
         this.cy = cy2;
         this.centerY = cy2 + this.halfH;
         this.fixPosition();
         this.activeSmooth = false;
      }
   }

   public final int getCw() {
      return this.cw;
   }

   public final void setCw(int cw2) {
      if (this.cw != cw2) {
         this.cw = cw2;
         this.halfW = cw2 / 2;
         this.cx = this.centerX - this.halfW;
         this.fixPosition();
      }
   }

   public final int getCh() {
      return this.ch;
   }

   public final void setCh(int ch2) {
      if (this.ch != ch2) {
         this.ch = ch2;
         this.halfH = ch2 / 2;
         this.cy = this.centerY - this.halfH;
         this.fixPosition();
      }
   }

   public final void setLimit(int x, int y, int w, int h) {
      this.limitX = x;
      this.limitY = y;
      this.limitH = h;
      this.limitW = w;
      this.fixPosition();
   }
}
