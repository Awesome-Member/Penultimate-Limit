package Engine.pool;

public abstract class DefaultPooleableObject implements PooleableObject {
   private PooleableObject next = null;
   private boolean inPool = false;

   public final void setPooleableObjectInPool(boolean inPool2) {
      this.inPool = inPool2;
   }

   public final boolean isPooleableObjectInPool() {
      return this.inPool;
   }

   public final void setNextPooleableObject(PooleableObject pooleableObject) {
      this.next = pooleableObject;
   }

   public final PooleableObject getNextPooleableObject() {
      return this.next;
   }
}
