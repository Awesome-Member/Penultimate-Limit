package MenuPck;

import Engine.Mystery;

public final class AnimateProperty {
   public int value;
   public int needValue;
   private long animationTime;
   private int fromValue;
   private int targetValue;
   public long accumTime;
   public boolean fastAnimate;

   public final void initAnimation(int fromValue2, int toValue, long time) {
      this.fromValue = fromValue2;
      this.value = fromValue2;
      this.targetValue = fromValue2;
      this.needValue = toValue;
      this.animationTime = time;
   }

   public final void copyTo(AnimateProperty animateProperty) {
      animateProperty.value = this.value;
      animateProperty.needValue = this.needValue;
      animateProperty.animationTime = this.animationTime;
      animateProperty.fromValue = this.fromValue;
      animateProperty.targetValue = this.targetValue;
      animateProperty.accumTime = this.accumTime;
      animateProperty.fastAnimate = this.fastAnimate;
   }

   public final void clearAnimation(int initialValue) {
      this.value = initialValue;
      this.needValue = initialValue;
      this.fromValue = initialValue;
      this.targetValue = initialValue;
   }

   public final boolean animateFixTime(boolean fast) {
      return this.animateFixTime(this.needValue, false, this.animationTime);
   }

   public final boolean animateFixTime(int needVal, boolean fast, long timeForFull) {
      boolean res = false;
      if (needVal != this.targetValue) {
         this.fromValue = this.value;
         this.targetValue = needVal;
         this.accumTime = -Mystery.FrameTimeI;
      }

      if (this.value != this.targetValue) {
         if (!fast && !this.fastAnimate) {
            this.accumTime += Mystery.FrameTimeI;
            if (timeForFull <= 0L) {
               timeForFull = 1L;
            }

            if (this.accumTime > timeForFull) {
               this.accumTime = timeForFull;
            }

            needVal = (int)((long)(this.targetValue - this.fromValue) * this.accumTime / timeForFull + (long)this.fromValue);
            if (this.value != needVal) {
               this.value = needVal;
               res = true;
            }
         } else {
            this.value = this.targetValue;
            res = true;
         }
      }

      this.fastAnimate = false;
      return res;
   }
}
