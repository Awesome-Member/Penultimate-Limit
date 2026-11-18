package Engine.pool;

public interface PooleableObject {
   void setPooleableObjectInPool(boolean unusedProgressMarker);

   boolean isPooleableObjectInPool();

   void setNextPooleableObject(PooleableObject pooleableObject);

   PooleableObject getNextPooleableObject();

   void cleanPooleableObject();
}
