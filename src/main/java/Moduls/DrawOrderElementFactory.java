package Moduls;

public final class DrawOrderElementFactory {
   private DrawOrderElement factory;
   private DrawOrderElement first = null;

   public DrawOrderElementFactory(DrawOrderElement drawOrderElement) {
      this.factory = drawOrderElement;
   }

   public final DrawOrderElement getInstance() {
      if (this.first == null) {
         return this.factory.newInstance();
      } else {
         DrawOrderElement first2 = this.first;
         this.first = first2.next;
         first2.next = null;
         first2.prev = null;
         return first2;
      }
   }

   public final void returnInstance(DrawOrderElement drawOrderElement) {
      drawOrderElement.prev = null;
      drawOrderElement.next = this.first;
      this.first = drawOrderElement;
   }
}
