package Moduls;

public final class DrawOrderList {
   public DrawOrderElement first = null;
   private DrawOrderElement last = null;
   public int length = 0;

   public final void clear() {
      if (this.first != null) {
         DrawOrderElement first2 = this.first;

         DrawOrderElement drawOrderElement;
         do {
            drawOrderElement = first2.next;
            first2.prev = null;
            first2.next = null;
            first2.returnInstance();
            first2 = drawOrderElement;
         } while(drawOrderElement != null);
      }

      this.first = null;
      this.last = null;
      this.length = 0;
   }

   public final void addAndSortFromEnd(DrawOrderElement drawOrderElement) {
      drawOrderElement.next = null;
      drawOrderElement.prev = null;
      if (this.last == null) {
         this.first = drawOrderElement;
         this.last = drawOrderElement;
      } else {
         drawOrderElement.prev = this.last;
         this.last.next = drawOrderElement;
         this.last = drawOrderElement;
      }

      ++this.length;
   }

   public final void addAndSortFromBegining(DrawOrderElement drawOrderElement) {
      drawOrderElement.next = null;
      drawOrderElement.prev = null;
      if (this.first == null) {
         this.first = drawOrderElement;
         this.last = drawOrderElement;
      } else {
         drawOrderElement.next = this.first;
         this.first.prev = drawOrderElement;
         this.first = drawOrderElement;
      }

      ++this.length;
   }

   public final void addAndSortFromEnd2(DrawOrderElement drawOrderElement) {
      if (this.last != null && !drawOrderElement.isGreater(this.last)) {
         drawOrderElement.next = null;
         drawOrderElement.prev = null;
         DrawOrderElement last2 = this.last;

         do {
            if ((last2 = last2.prev) == null) {
               this.addAndSortFromBegining(drawOrderElement);
               return;
            }
         } while(!drawOrderElement.isGreater(last2));

         DrawOrderElement next2 = last2.next;
         last2.next = drawOrderElement;
         drawOrderElement.prev = last2;
         drawOrderElement.next = next2;
         if (next2 == null) {
            this.last = drawOrderElement;
         } else {
            next2.prev = drawOrderElement;
         }

         ++this.length;
      } else {
         this.addAndSortFromEnd(drawOrderElement);
      }
   }

   public final void mergeWith(DrawOrderList drawOrderList2) {
      if (drawOrderList2.first != null) {
         if (this.first == null) {
            this.first = drawOrderList2.first;
            this.last = drawOrderList2.last;
            this.length = drawOrderList2.length;
            drawOrderList2.first = null;
            drawOrderList2.last = null;
            drawOrderList2.length = 0;
         } else {
            DrawOrderElement first2 = drawOrderList2.first;
            DrawOrderElement first3 = this.first;
            int addedLength = 0;

            do {
               if (first3.isGreater(first2)) {
                  DrawOrderElement next2 = first2.next;
                  DrawOrderElement prev2 = first3.prev;
                  first3.prev = first2;
                  first2.next = first3;
                  first2.prev = prev2;
                  if (prev2 == null) {
                     this.first = first2;
                  } else {
                     prev2.next = first2;
                  }

                  ++this.length;
                  ++addedLength;
                  first3 = first2;
                  first2 = next2;
                  if (next2 == null) {
                     break;
                  }
               }
            } while((first3 = first3.next) != null);

            if (first2 != null) {
               first2.prev = this.last;
               this.last.next = first2;
               this.last = drawOrderList2.last;
            }

            this.length += drawOrderList2.length - addedLength;
            drawOrderList2.first = null;
            drawOrderList2.last = null;
            drawOrderList2.length = 0;
         }
      }
   }
}
