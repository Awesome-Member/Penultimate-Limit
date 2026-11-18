package desktop.ui;

import applet.Main;

public final class Display {
   private static Display display = new Display();
   private Displayable displayable;

   private Display() {
   }

   public final void setCurrent(Displayable displayable2) {
      if (this.displayable != displayable2) {
         this.displayable = displayable2;
         Main.instance.setDisplayable(displayable2);
      }
   }

   public static Display getDisplay() {
      return display;
   }
}
