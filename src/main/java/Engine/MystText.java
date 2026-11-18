package Engine;

import java.util.Vector;
import desktop.graphics.GameGraphics;

public final class MystText {
   private int[][] rowSymbols;
   private int[][] rowColors;
   private int[] rowLen;
   private int rowStep;
   private int maxLen;
   private int lineSpacing;

   public final int getRowsCount() {
      return this.rowSymbols == null ? 0 : this.rowSymbols.length;
   }

   public final int getRowStep() {
      return this.rowStep;
   }

   public final int[] getRowLen() {
      return this.rowLen;
   }

   public final int getMaxWidth() {
      return this.maxLen;
   }

   public final int getMaxHeight(MystFont mystFont) {
      int c;
      return (c = this.getRowsCount()) * mystFont.fontHeight + Math.max(0, c - 1) * this.lineSpacing;
   }

   public final void create(MystString mystString, int areaWidth, int lineSpace, MystFont mystFont) {
      this.create(mystString, areaWidth, -1, ' ', lineSpace, mystFont, (int[][])null, 0);
   }

   public final void create(MystString mystString, int areaWidth, int maxLinesCount, char maxLinesSymb, int lineSpace, MystFont mystFont) {
      this.create(mystString, areaWidth, 2, maxLinesSymb, 1, mystFont, (int[][])null, 0);
   }

   public final void create(MystString mystString, int areaWidth, int lineSpace, MystFont mystFont, int[][] zones, int zonesYOffset) {
      this.create(mystString, areaWidth, -1, ' ', lineSpace, mystFont, zones, 0);
   }

   private void create(MystString mystString, int areaWidth, int maxLinesCount, char maxLinesSymb, int lineSpace, MystFont mystFont, int[][] zones, int zonesYOffset) {
      this.maxLen = 0;
      this.lineSpacing = lineSpace;
      if (mystString.getValue().length <= 0) {
         this.rowSymbols = new int[0][0];
         this.rowColors = new int[0][0];
         this.rowLen = new int[0];
         this.rowStep = 0;
      } else {
         this.rowSymbols = null;
         this.rowLen = null;
         this.rowColors = null;
         Vector vector3 = new Vector();
         Vector vector4 = new Vector();
         int charToFontPositionFix = 0;
         int lineWidth = 0;
         int wordWidth = 0;
         byte spaceOffset = 0;
         int lastSpaceIndex = 0;
         boolean hasSpace = false;
         zonesYOffset = zonesYOffset;
         int[] textSymbols = mystString.getValue();
         int[] intValue = getZone(zones, zonesYOffset);
         int availableLineWidth = areaWidth - intValue[2];
         boolean reachedLineLimit = false;
         int symbolIndex = 0;

         int symbolCount;
         for(symbolCount = mystString.getLength(); symbolIndex < symbolCount; ++symbolIndex) {
            if (textSymbols[symbolIndex] == 0) {
               lastSpaceIndex = symbolIndex;
               spaceOffset = 0;
               if (textSymbols[symbolIndex] == 0) {
                  spaceOffset = -1;
               }

               wordWidth = 0;
               hasSpace = true;
            }

            if (availableLineWidth != areaWidth && availableLineWidth / mystFont.fontWidth < 10) {
               intValue[4] = 1;
               vector3.addElement(charToFontPositionFix);
               vector4.addElement(new Integer(charToFontPositionFix - 1));
               if (vector3.size() == maxLinesCount) {
                  reachedLineLimit = true;
                  break;
               }

               zonesYOffset += mystFont.fontHeight + lineSpace;
               intValue = getZone(zones, zonesYOffset);
               availableLineWidth = areaWidth - intValue[2];
               --symbolIndex;
            } else if (textSymbols[symbolIndex] == -5) {
               vector3.addElement(charToFontPositionFix);
               vector4.addElement(new Integer(symbolIndex - 1));
               if (vector3.size() == maxLinesCount) {
                  reachedLineLimit = true;
                  break;
               }

               zonesYOffset += mystFont.fontHeight + lineSpace;
               intValue = getZone(zones, zonesYOffset);
               availableLineWidth = areaWidth - intValue[2];
               charToFontPositionFix = symbolIndex + 1;
               lineWidth = 0;
               wordWidth = 0;
               lastSpaceIndex = symbolIndex;
               spaceOffset = 0;
               hasSpace = true;
            } else {
               if (textSymbols[symbolIndex] >= 900) {
                  lineWidth += mystFont.fontWidth;
                  wordWidth += mystFont.fontWidth;
               } else {
                  lineWidth += mystFont.LitsCoords[textSymbols[symbolIndex]][1] - mystFont.LitsCoords[textSymbols[symbolIndex]][0] + 1;
                  wordWidth += mystFont.LitsCoords[textSymbols[symbolIndex]][1] - mystFont.LitsCoords[textSymbols[symbolIndex]][0] + 1;
               }

               if (lineWidth > availableLineWidth && hasSpace) {
                  vector3.addElement(charToFontPositionFix);
                  vector4.addElement(new Integer(lastSpaceIndex + spaceOffset));
                  if (vector3.size() == maxLinesCount) {
                     reachedLineLimit = true;
                     break;
                  }

                  zonesYOffset += mystFont.fontHeight + lineSpace;
                  intValue = getZone(zones, zonesYOffset);
                  availableLineWidth = areaWidth - intValue[2];
                  charToFontPositionFix = lastSpaceIndex + 1;
                  lineWidth = wordWidth;
                  hasSpace = false;
               } else if (lineWidth > availableLineWidth) {
                  vector3.addElement(charToFontPositionFix);
                  vector4.addElement(new Integer(symbolIndex - 1));
                  if (vector3.size() == maxLinesCount) {
                     reachedLineLimit = true;
                     break;
                  }

                  zonesYOffset += mystFont.fontHeight + lineSpace;
                  intValue = getZone(zones, zonesYOffset);
                  availableLineWidth = areaWidth - intValue[2];
                  charToFontPositionFix = symbolIndex;
                  wordWidth = 0;
                  lineWidth = 0;
                  hasSpace = false;
               }
            }
         }

         if (maxLinesCount > 0 && vector3.size() >= maxLinesCount) {
            if (maxLinesCount > 0) {
               reachedLineLimit = true;
            }
         } else {
            vector3.addElement(charToFontPositionFix);
            vector4.addElement(new Integer(textSymbols.length - 1));
         }

         this.rowSymbols = new int[vector3.size()][];
         this.rowColors = new int[vector3.size()][];
         this.rowLen = new int[vector3.size()];
         this.maxLen = 0;
         symbolIndex = 0;

         for(symbolCount = vector3.size(); symbolIndex < symbolCount; ++symbolIndex) {
            areaWidth = (Integer)vector3.elementAt(symbolIndex);
            maxLinesCount = (Integer)vector4.elementAt(symbolIndex);
            this.rowSymbols[symbolIndex] = new int[maxLinesCount - areaWidth + 1];
            this.rowColors[symbolIndex] = new int[maxLinesCount - areaWidth + 1];
            int rowWidth = 0;

            for(zonesYOffset = areaWidth; zonesYOffset <= maxLinesCount; ++zonesYOffset) {
               charToFontPositionFix = textSymbols[zonesYOffset];
               this.rowSymbols[symbolIndex][zonesYOffset - areaWidth] = charToFontPositionFix;
               this.rowColors[symbolIndex][zonesYOffset - areaWidth] = mystString.colors[zonesYOffset];
               if (reachedLineLimit && symbolIndex == symbolCount - 1 && zonesYOffset == maxLinesCount && maxLinesSymb != 0) {
                  charToFontPositionFix = MystFont.charToFontPositionFix(maxLinesSymb, mystFont);
                  this.rowSymbols[symbolIndex][zonesYOffset - areaWidth] = charToFontPositionFix;
               } else if (charToFontPositionFix >= 900) {
                  rowWidth += mystFont.fontWidth;
                  continue;
               }

               rowWidth += mystFont.LitsCoords[charToFontPositionFix][1] - mystFont.LitsCoords[charToFontPositionFix][0] + 1;
            }

            this.rowLen[symbolIndex] = rowWidth;
            this.maxLen = Math.max(this.maxLen, rowWidth);
         }

         vector3.removeAllElements();
         vector4.removeAllElements();
         this.rowStep = mystFont.fontHeight + lineSpace;
      }
   }

   private static int[] getZone(int[][] integersGrid, int y) {
      if (integersGrid == null) {
         return new int[]{0, 0, 0, 0};
      } else {
         int integersGridIndex = 0;

         for(int length = integersGrid.length; integersGridIndex < length; ++integersGridIndex) {
            int[] intValue = integersGrid[integersGridIndex];
            if (y >= intValue[0] && y <= intValue[1]) {
               return intValue;
            }
         }

         return new int[]{0, 0, 0, 0};
      }
   }

   public final boolean draw(GameGraphics gameGraphics2, int dX, int dY, int dW, int dH, int yOffset, int align, MystFont mystFont) {
      return this.draw(gameGraphics2, dX, dY, dW, dH, 0, align, mystFont, (int[][])null, 0);
   }

   public final boolean draw(GameGraphics gameGraphics2, int dX, int dY, int dW, int dH, int yOffset, int align, MystFont mystFont, int[][] zones, int zonesYOffset) {
      if (this.rowSymbols == null) {
         return false;
      } else {
         boolean res = false;

         try {
            int rowY = dY + yOffset;
            int rowLenCount = this.rowLen.length;
            int fontHeight2 = mystFont.fontHeight;
            int zoneY = 0;
            int[] curZone = getZone(zones, 0);

            for(int rowLenIndex = 0; rowLenIndex < rowLenCount; ++rowLenIndex) {
               if (rowY >= dY + dH) {
                  return res;
               }

               if (rowY + fontHeight2 >= dY) {
                  label83: {
                     yOffset = dX;
                     int rowLen2;
                     int rowLen3;
                     if (align == 0) {
                        rowLen2 = dX + (curZone[3] == 0 ? curZone[2] : 0) + (dW - curZone[2]) / 2;
                        rowLen3 = this.rowLen[rowLenIndex] / 2;
                     } else {
                        if (align == 1) {
                           yOffset = dX + (curZone[3] == 0 ? curZone[2] : 0);
                           break label83;
                        }

                        if (align != 2) {
                           break label83;
                        }

                        rowLen2 = dX + dW - this.rowLen[rowLenIndex];
                        rowLen3 = curZone[3] == 1 ? curZone[2] : 0;
                     }

                     yOffset = rowLen2 - rowLen3;
                  }

                  int rowSymbols2 = this.rowSymbols[rowLenIndex].length;

                  for(int symbolIndex = 0; symbolIndex < rowSymbols2; ++symbolIndex) {
                     int rowSymbols3;
                     int symbolWidth = (rowSymbols3 = this.rowSymbols[rowLenIndex][symbolIndex]) >= 900 ? mystFont.fontWidth : mystFont.LitsCoords[rowSymbols3][1] - mystFont.LitsCoords[rowSymbols3][0] + 1;
                     if (this.rowColors[rowLenIndex][symbolIndex] >= 0) {
                        byte add = (byte)(symbolIndex == 0 ? 1 : 0);
                        gameGraphics2.setColor(this.rowColors[rowLenIndex][symbolIndex]);
                        gameGraphics2.fillRect(yOffset - add, rowY, symbolWidth + 1 + add, fontHeight2);
                     } else if (symbolIndex <= rowSymbols2 - 2 && this.rowColors[rowLenIndex][symbolIndex + 1] >= 0) {
                        gameGraphics2.setColor(this.rowColors[rowLenIndex][symbolIndex + 1]);
                        gameGraphics2.fillRect(yOffset + symbolWidth - 1, rowY, 1, fontHeight2);
                     }

                     if (rowSymbols3 >= 900) {
                        res = true;
                        mystFont.animSymbols[rowSymbols3 - 900].draw(gameGraphics2, yOffset, rowY);
                     } else {
                        gameGraphics2.drawRegion(mystFont.src, mystFont.rx + mystFont.LitsCoords[rowSymbols3][0], mystFont.ry + mystFont.LitsCoords[rowSymbols3][2], symbolWidth, fontHeight2, 0, yOffset, rowY);
                     }

                     yOffset += symbolWidth;
                  }
               }

               rowY += this.rowStep;
               zoneY += this.rowStep;
               curZone = getZone(zones, zoneY);
            }

            return res;
         } catch (Exception exception) {
            return res;
         }
      }
   }
}
