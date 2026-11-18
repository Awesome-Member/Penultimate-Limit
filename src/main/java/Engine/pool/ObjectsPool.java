package Engine.pool;

import java.util.Vector;

public abstract class ObjectsPool {
   private PooleableObject topObject = null;
   private final boolean enabled = true;

   public final PooleableObject peekPooleableObject() {
      PooleableObject pooleableObject;
      if (this.topObject == null) {
         (pooleableObject = this.createPooleableObject()).setPooleableObjectInPool(false);
         return pooleableObject;
      } else {
         (pooleableObject = this.topObject).setPooleableObjectInPool(false);
         this.topObject = this.topObject.getNextPooleableObject();
         return pooleableObject;
      }
   }

   public final void returnPooleableObject(PooleableObject pooleableObject) {
      if (pooleableObject.isPooleableObjectInPool()) {
         throw new RuntimeException("PooleableObject allready mark in Pool");
      } else {
         pooleableObject.setPooleableObjectInPool(true);
         pooleableObject.cleanPooleableObject();
         pooleableObject.setNextPooleableObject(this.topObject);
         this.topObject = pooleableObject;
      }
   }

   public final void clear(Vector vector3) {
      if (vector3.size() != 0) {
         for(int sizeIndex = 0; sizeIndex < vector3.size(); ++sizeIndex) {
            this.returnPooleableObject((PooleableObject)vector3.elementAt(sizeIndex));
         }

         vector3.removeAllElements();
      }
   }

   public abstract PooleableObject createPooleableObject();
}
