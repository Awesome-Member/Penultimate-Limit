package Engine;

import desktop.PlatformRuntime;
import desktop.ui.GameCanvas;
import desktop.ui.Display;

public abstract class Threader extends GameCanvas implements Runnable {
   private final Object lock = new Object();
   public boolean isActive = false;
   private Thread thread;

   public final void SetAsCurrent(Display display2) {
      synchronized(this.lock) {
         if (!this.isActive) {
            this.isActive = true;
            display2.setCurrent(this);
            this.thread = null;
            this.thread = PlatformRuntime.keepThreadPriority(new Thread(this));
            this.thread.start();
         }
      }
   }

   public final void StopCurrent() {
      synchronized(this.lock) {
         this.isActive = false;
         this.thread = null;
      }
   }
}
