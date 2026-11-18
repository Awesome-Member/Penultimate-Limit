package Engine;

public final class Delegate {
   private Delegateable delegateable;
   private int context;

   public Delegate(Delegateable delegateable2, int context2) {
      this.delegateable = delegateable2;
      this.context = context2;
   }

   public final void execute() {
      this.delegateable.executeDelegated(this.context);
   }
}
