package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

public final class Util {
   private static final Dimension MAX_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   public static final int DEFAULT_INDENT = 10;

   public static Dimension getMinimumSize(Component component, GridConstraints gridConstraints, boolean includeIndent) {
      Dimension size = getSize(gridConstraints.myMinimumSize, component.getMinimumSize());
      if (includeIndent) {
         size.width += 10 * gridConstraints.getIndent();
      }

      return size;
   }

   public static Dimension getMaximumSize(Component component, GridConstraints gridConstraints, boolean includeIndent) {
      Dimension size = getSize(gridConstraints.myMaximumSize, MAX_SIZE);
      if (includeIndent && size.width < MAX_SIZE.width) {
         size.width += 10 * gridConstraints.getIndent();
      }

      return size;
   }

   public static Dimension getPreferredSize(Component component, GridConstraints gridConstraints, boolean includeIndent) {
      Dimension size = getSize(gridConstraints.myPreferredSize, component.getPreferredSize());
      if (includeIndent) {
         size.width += 10 * gridConstraints.getIndent();
      }

      return size;
   }

   private static Dimension getSize(Dimension dimension, Dimension dimension2) {
      int width = dimension.width >= 0 ? dimension.width : dimension2.width;
      int height = dimension.height >= 0 ? dimension.height : dimension2.height;
      return new Dimension(width, height);
   }

   public static void adjustSize(Component component, GridConstraints gridConstraints, Dimension dimension) {
      Dimension minimumSize = getMinimumSize(component, gridConstraints, false);
      Dimension maximumSize = getMaximumSize(component, gridConstraints, false);
      dimension.width = Math.max(dimension.width, minimumSize.width);
      dimension.height = Math.max(dimension.height, minimumSize.height);
      dimension.width = Math.min(dimension.width, maximumSize.width);
      dimension.height = Math.min(dimension.height, maximumSize.height);
   }

   public static int eliminate(int[] integers, int[] integers2, ArrayList arrayList) {
      int cellCount;
      if ((cellCount = integers.length) != integers2.length) {
         throw new IllegalArgumentException("size mismatch: " + cellCount + ", " + integers2.length);
      } else if (arrayList != null && arrayList.size() != 0) {
         throw new IllegalArgumentException("eliminated must be empty");
      } else {
         int maximum = 0;

         int integerIndex;
         for(integerIndex = 0; integerIndex < cellCount; ++integerIndex) {
            maximum = Math.max(maximum, integers[integerIndex] + integers2[integerIndex]);
         }

         for(integerIndex = maximum - 1; integerIndex >= 0; --integerIndex) {
            boolean hasCellStart = false;
            boolean hasCellEnd = false;

            int integerIndex2;
            for(integerIndex2 = 0; integerIndex2 < cellCount; ++integerIndex2) {
               if (integers[integerIndex2] == integerIndex) {
                  hasCellStart = true;
               }

               if (integers[integerIndex2] + integers2[integerIndex2] - 1 == integerIndex) {
                  hasCellEnd = true;
               }
            }

            if (!hasCellStart || !hasCellEnd) {
               if (arrayList != null) {
                  arrayList.add(integerIndex);
               }

               for(integerIndex2 = 0; integerIndex2 < cellCount; ++integerIndex2) {
                  hasCellStart = integers[integerIndex2] <= integerIndex && integerIndex < integers[integerIndex2] + integers2[integerIndex2];
                  hasCellEnd = integers[integerIndex2] > integerIndex;
                  int unusedDecrementResult;
                  if (hasCellStart) {
                     unusedDecrementResult = integers2[integerIndex2]--;
                  }

                  if (hasCellEnd) {
                     unusedDecrementResult = integers[integerIndex2]--;
                  }
               }

               --maximum;
            }
         }

         return maximum;
      }
   }
}
