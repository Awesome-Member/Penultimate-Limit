package desktop;

import desktop.graphics.GameImage;

public final class PlatformRuntime {
   private int platformType = 1;
   private final Object lock = new Object();
   private PlatformListener platformListener = null;
   private static PlatformRuntime instance;
   private static final Object lock2 = new Object();

   public static PlatformRuntime getInstance() {
      if (instance != null) {
         return instance;
      } else {
         synchronized(lock2) {
            return instance != null ? instance : (instance = new PlatformRuntime());
         }
      }
   }

   public final void setPlatformListener(PlatformListener platformListener2) {
      synchronized(this.lock) {
         this.platformListener = platformListener2;
      }
   }

   public final int getPlatformType() {
      return this.platformType;
   }

   public final void setPlatformType(int platformType2) {
      if (platformType2 == 1) {
         this.platformType = platformType2;
      }
   }

   public static String getPlatformName() {
      return "BROWSER";
   }

   public static boolean isPixelTransparent(GameImage gameImage, int x, int y) {
      return gameImage.getBufferedImage().getRGB(x, y) >>> 24 == 0;
   }

   public static void initializeThread() {
   }

   public static Thread withBackgroundPriority(Thread thread2) {
      try {
         thread2.setPriority(4);
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      return thread2;
   }

   public static Thread keepThreadPriority(Thread thread2) {
      return thread2;
   }

   public static Thread withLowPriority(Thread thread2) {
      try {
         thread2.setPriority(3);
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      return thread2;
   }
}
