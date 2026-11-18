package Engine;

public final class MystString {
   private int[] value;
   public int[] colors;

   public final void Create(String s, MystFont mystFont) {
      this.value = null;
      int currentColor = -1;
      char[] sArr;
      int toCharArray = (sArr = s.toCharArray()).length;
      int symbolIndex = 0;
      int[] integers = new int[toCharArray];
      int[] integers2 = new int[toCharArray];
      char previousCharacter = ' ';

      for(int sArrIndex = 0; sArrIndex < toCharArray; ++sArrIndex) {
         int characterCode;
         int charToFontPosition = MystFont.charToFontPosition((char)(characterCode = sArr[sArrIndex]), mystFont, previousCharacter);
         previousCharacter = (char)characterCode;
         if (charToFontPosition != -6) {
            if (charToFontPosition == -2) {
               int[] integers3;
               if ((integers3 = (int[])MystFont.COLOR_GROUPS.get(String.valueOf(sArr[sArrIndex + 1]) + sArr[sArrIndex + 2])) == null) {
                  integers3 = new int[]{255, 255, 255};
               }

               currentColor = (integers3[0] & 255) << 16 | (integers3[1] & 255) << 8 | integers3[2] & 255;
               sArrIndex += 2;
            } else if (charToFontPosition == -3) {
               characterCode = Integer.parseInt(String.valueOf(sArr[sArrIndex + 1]) + sArr[sArrIndex + 2] + sArr[sArrIndex + 3]);
               charToFontPosition = Integer.parseInt(String.valueOf(sArr[sArrIndex + 4]) + sArr[sArrIndex + 5] + sArr[sArrIndex + 6]);
               currentColor = Integer.parseInt(String.valueOf(sArr[sArrIndex + 7]) + sArr[sArrIndex + 8] + sArr[sArrIndex + 9]);
               currentColor = (characterCode & 255) << 16 | (charToFontPosition & 255) << 8 | currentColor & 255;
               sArrIndex += 9;
            } else if (charToFontPosition == -4) {
               currentColor = -1;
            } else {
               if (charToFontPosition >= 900) {
                  if (charToFontPosition - 900 > mystFont.animSymbols.length - 1) {
                     continue;
                  }
               } else if (charToFontPosition > mystFont.LitsCoords.length - 1) {
                  continue;
               }

               ++symbolIndex;
               integers[symbolIndex - 1] = charToFontPosition;
               integers2[symbolIndex - 1] = currentColor;
            }
         }
      }

      this.value = new int[symbolIndex];
      this.colors = new int[symbolIndex];
      System.arraycopy(integers, 0, this.value, 0, symbolIndex);
      System.arraycopy(integers2, 0, this.colors, 0, symbolIndex);
   }

   public final int getLength() {
      return this.value.length;
   }

   public final int[] getValue() {
      return this.value;
   }
}
