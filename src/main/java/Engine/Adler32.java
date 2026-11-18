package Engine;

public final class Adler32 {
   private int checksum = 1;

   public final void updateChecksum(byte[] bytes, int offset, int length) {
      int checksum2 = this.checksum & '\uffff';

      int checksum3;
      for(checksum3 = this.checksum >>> 16; length > 0; checksum3 %= 65521) {
         int chunkLength = 3800;
         if (3800 > length) {
            chunkLength = length;
         }

         length -= chunkLength;

         while(true) {
            --chunkLength;
            if (chunkLength < 0) {
               checksum2 %= 65521;
               break;
            }

            checksum2 += bytes[offset++] & 255;
            checksum3 += checksum2;
         }
      }

      this.checksum = checksum3 << 16 | checksum2;
   }

   public final long getValue() {
      return (long)this.checksum & 4294967295L;
   }
}
