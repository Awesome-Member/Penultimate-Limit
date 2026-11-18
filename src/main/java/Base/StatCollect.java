package Base;

import java.util.Vector;

public final class StatCollect {
   private static Vector values = new Vector();
   private static long startTime;

   public static void fixMesure(String name) {
      long time = System.currentTimeMillis();
      values.addElement(name + " " + (time - startTime) + " ms");
      startTime = System.currentTimeMillis();
   }
   private StatCollect() {
   }

}
