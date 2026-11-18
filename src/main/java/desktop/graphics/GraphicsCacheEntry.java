package desktop.graphics;

final class GraphicsCacheEntry {
   public Object key;
   public Object value;

   private GraphicsCacheEntry(Object key2, Object element) {
      this.key = key2;
      this.value = element;
   }


   GraphicsCacheEntry(Object element, Object element2, byte cacheType) {
      this(element, element2);
   }
}
