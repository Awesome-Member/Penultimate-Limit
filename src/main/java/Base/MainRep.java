package Base;

import desktop.PlatformRuntime;

public final class MainRep implements Runnable {
   private static final Object lockObject = new Object();
   private boolean addBar = false;
   private long addBarStart;
   private int addBarTime;
   private static Thread repainterThread;
   private static MainRep repainter = new MainRep();
   private boolean active = false;
   private boolean work = false;

   public static void Run(boolean autoAdd, int addTime) {
      synchronized(lockObject) {
         if (repainter != null) {
            repainter.stop();
         }

         repainter = null;
         repainter = new MainRep();
         repainterThread = PlatformRuntime.keepThreadPriority(new Thread(repainter));
         repainter.addBar = autoAdd;
         if (autoAdd) {
            repainter.addBarTime = addTime;
         }

         repainter.addBarStart = System.currentTimeMillis();
         MainRep mainRep;
         (mainRep = repainter).active = true;
         mainRep.work = true;
         repainterThread.start();
      }
   }

   public static void setActive() {
      synchronized(lockObject) {
         if (repainter != null) {
            repainter.active = false;
         }
      }
   }

   public static void Stop() {
      synchronized(lockObject) {
         if (repainter != null) {
            repainter.active = false;
            repainter.stop();
         }
      }
   }

   private void stop() {
      this.active = false;

      while(this.work) {
         Thread.yield();
      }

   }

   public final void run() {
      PlatformRuntime.initializeThread();

      for(; this.active; Thread.yield()) {
         if (this.addBar) {
            int progressPercent;
            if ((progressPercent = (int)((System.currentTimeMillis() - this.addBarStart) * 100L / (long)this.addBarTime)) > 100) {
               progressPercent = 100;
            }

            Com.PrBarTh.setProgress(progressPercent);
         }

         Com.mainTh.paintProgress();
         Com.mainTh.repaint();

         try {
            Thread.sleep(50L);
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }

      this.work = false;
   }
}
