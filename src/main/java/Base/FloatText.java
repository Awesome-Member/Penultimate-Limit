package Base;

import Engine.MystText;

public final class FloatText {
   public final MystText text;
   public final int showTime;
   public final int clickProtectionTime;
   public final int clickHoldTime;
   public final int delay;
   public int spendTime = 0;
   public boolean clicked = false;

   public FloatText(MystText mystText, int showTime2, int clickProtectionTime2, int clickHoldTime2, int delay2) {
      this.text = mystText;
      this.showTime = showTime2;
      this.clickProtectionTime = clickProtectionTime2;
      this.clickHoldTime = clickHoldTime2;
      this.delay = delay2;
   }
}
