package Base;

public final class EnterName {
   public String name = "";
   public long date = 0L;

   public EnterName(String name2) {
      this.name = name2;
      this.date = System.currentTimeMillis();
   }

   public EnterName(String name2, long date2) {
      this.name = name2;
      this.date = date2;
   }
}
