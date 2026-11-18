package Engine;

public final class AnimSost {
   public int TimeAccum = 0;
   public int CurFrameI = 0;
   private int AnimSourceInd;
   public boolean WasLoop;
   public boolean WasFrame;

   public AnimSost(int AnimSource) {
      this.AnimSourceInd = AnimSource;
      this.WasLoop = false;
      this.WasFrame = false;
   }
}
