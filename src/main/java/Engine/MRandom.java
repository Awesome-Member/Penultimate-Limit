package Engine;

public final class MRandom {
   private long x;

   public MRandom(long startX) {
      this.x = startX;
   }

   public final long nextInt(long n) {
      long x2;
      if ((x2 = 48271L * (this.x % 44488L) - 3399L * (this.x / 44488L)) > 0L) {
         this.x = x2;
      } else {
         this.x = x2 + 2147483647L;
      }

      return this.x * n / 2147483647L;
   }

   public final MRandom cloneTo() {
      return new MRandom(this.x);
   }
}
