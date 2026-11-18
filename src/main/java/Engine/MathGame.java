package Engine;

public final class MathGame {
   public static final int[][] direction8DToVect = new int[][]{{1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {0, 0}};

   public static int getOrthoStepBetweenTwoDirections8D(int direction8D1, int direction8D2) {
      return Math.min(Math.abs(direction8D1 - direction8D2), Math.min(Math.abs(direction8D1 + 8 - direction8D2), Math.abs(direction8D1 - 8 - direction8D2)));
   }

   public static int[] direction8DToNormalVector(int direction8D) {
      return direction8DToVect[direction8D];
   }

   public static int vectorToDirection8(int deltaX, int deltaY) {
      byte direction = 8;
      if (deltaX > 0) {
         direction = 1;
      } else if (deltaX < 0) {
         direction = 5;
      }

      if (deltaY > 0) {
         if (direction == 1) {
            direction = 2;
         } else if (direction == 5) {
            direction = 4;
         } else {
            direction = 3;
         }
      } else if (deltaY < 0) {
         if (direction == 1) {
            direction = 0;
         } else if (direction == 5) {
            direction = 6;
         } else {
            direction = 7;
         }
      }

      return direction;
   }

   public static long ceilDivide(long dividend, long divisor) {
      long remainder;
      if ((remainder = dividend % divisor) == 0L) {
         return dividend / divisor;
      } else {
         long quotient;
         return (quotient = dividend / divisor) >= 0L && (quotient != 0L || remainder > 0L) ? quotient + 1L : quotient;
      }
   }

   public static long floorDivide(long dividend, long divisor) {
      long remainder;
      if ((remainder = dividend % divisor) == 0L) {
         return dividend / divisor;
      } else {
         long quotient;
         return (quotient = dividend / divisor) >= 0L && (quotient != 0L || remainder > 0L) ? quotient : quotient - 1L;
      }
   }

   public static long integerSquareRoot(long radicand) {
      long distance = radicand;
      if (radicand <= 0L) {
         return 0L;
      } else {
         long distance2 = (radicand & 4294901760L) != 0L ? ((radicand & 4278190080L) != 0L ? 16383L : 1023L) : ((radicand & 65280L) != 0L ? 63L : (radicand > 4L ? 7L : radicand));

         for(int bitIndex = 0; bitIndex <= 30; distance = distance2) {
            ++bitIndex;
            long nextEstimate;
            distance2 = ((nextEstimate = radicand / distance2 + distance2) >> 1) + (nextEstimate & 1L);
            if (distance <= distance2) {
               if (radicand / distance == distance - 1L && radicand % distance == 0L) {
                  --distance;
               }

               return distance;
            }
         }

         return 0L;
      }
   }

   public static boolean isWithinRadius(long x1, long y1, long x2, long y2, long radius) {
      long deltaX = x2 - x1;
      long deltaY = y2 - y1;
      return deltaX * deltaX + deltaY * deltaY <= radius * radius;
   }
   private MathGame() {
   }

}
