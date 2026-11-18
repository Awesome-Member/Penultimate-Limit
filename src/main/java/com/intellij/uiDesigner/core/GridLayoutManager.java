package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.JComponent;

public final class GridLayoutManager extends AbstractLayout {
   private int myMinCellSize;
   private final int[] myRowStretches;
   private final int[] myColumnStretches;
   private final int[] myYs;
   private final int[] myHeights;
   private final int[] myXs;
   private final int[] myWidths;
   private LayoutState myLayoutState;
   DimensionInfo myHorizontalInfo;
   DimensionInfo myVerticalInfo;
   private boolean mySameSizeHorizontally;
   private boolean mySameSizeVertically;
   public static Object DESIGN_TIME_INSETS = new Object();
   private static final int SKIP_ROW = 1;
   private static final int SKIP_COL = 2;

   public GridLayoutManager(int sizeX2, int sizeY2) {
      this.myMinCellSize = 20;
      if (sizeY2 <= 0) {
         throw new IllegalArgumentException("wrong columnCount: " + sizeY2);
      } else if (sizeX2 <= 0) {
         throw new IllegalArgumentException("wrong rowCount: " + sizeX2);
      } else {
         this.myRowStretches = new int[sizeX2];

         int rowStretcheIndex;
         for(rowStretcheIndex = 0; rowStretcheIndex < sizeX2; ++rowStretcheIndex) {
            this.myRowStretches[rowStretcheIndex] = 1;
         }

         this.myColumnStretches = new int[sizeY2];

         for(rowStretcheIndex = 0; rowStretcheIndex < sizeY2; ++rowStretcheIndex) {
            this.myColumnStretches[rowStretcheIndex] = 1;
         }

         this.myXs = new int[sizeY2];
         this.myWidths = new int[sizeY2];
         this.myYs = new int[sizeX2];
         this.myHeights = new int[sizeX2];
      }
   }

   public GridLayoutManager(int rowCount, int columnCount, Insets insets, int horizontalGap, int verticalGap) {
      this(rowCount, columnCount);
      this.setMargin(insets);
      this.setHGap(horizontalGap);
      this.setVGap(verticalGap);
      this.myMinCellSize = 0;
   }

   public GridLayoutManager(int rowCount, int columnCount, Insets insets, int horizontalGap, int verticalGap, boolean sameSizeHorizontally, boolean sameSizeVertically) {
      this(rowCount, columnCount, insets, horizontalGap, verticalGap);
      this.mySameSizeHorizontally = sameSizeHorizontally;
      this.mySameSizeVertically = sameSizeVertically;
   }

   public final void addLayoutComponent(Component component, Object constraints) {
      GridConstraints gridConstraints;
      int column = (gridConstraints = (GridConstraints)constraints).getRow();
      int rowSpan = gridConstraints.getRowSpan();
      int rowCount = this.getRowCount();
      if (column >= 0 && column < rowCount) {
         if (column + rowSpan - 1 >= rowCount) {
            throw new IllegalArgumentException("wrong row span: " + rowSpan + "; row=" + column + " rowCount=" + rowCount);
         } else {
            column = gridConstraints.getColumn();
            int colSpan = gridConstraints.getColSpan();
            rowSpan = this.getColumnCount();
            if (column >= 0 && column < rowSpan) {
               if (column + colSpan - 1 >= rowSpan) {
                  throw new IllegalArgumentException("wrong col span: " + colSpan + "; column=" + column + " columnCount=" + rowSpan);
               } else {
                  super.addLayoutComponent(component, constraints);
               }
            } else {
               throw new IllegalArgumentException("wrong column: " + column);
            }
         }
      } else {
         throw new IllegalArgumentException("wrong row: " + column);
      }
   }

   public final int getRowCount() {
      return this.myRowStretches.length;
   }

   public final int getColumnCount() {
      return this.myColumnStretches.length;
   }

   public final int getRowStretch(int rowIndex) {
      return this.myRowStretches[rowIndex];
   }

   public final void setRowStretch(int rowIndex, int stretch) {
      if (stretch <= 0) {
         throw new IllegalArgumentException("wrong stretch: " + stretch);
      } else {
         this.myRowStretches[rowIndex] = stretch;
      }
   }

   public final int getColumnStretch(int columnIndex) {
      return this.myColumnStretches[columnIndex];
   }

   public final void setColumnStretch(int columnIndex, int stretch) {
      if (stretch <= 0) {
         throw new IllegalArgumentException("wrong stretch: " + stretch);
      } else {
         this.myColumnStretches[columnIndex] = stretch;
      }
   }

   public final Dimension maximumLayoutSize(Container container) {
      return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
   }

   public final Dimension minimumLayoutSize(Container container) {
      this.validateInfos(container);
      DimensionInfo horizontalInfo = this.myHorizontalInfo;
      DimensionInfo verticalInfo = this.myVerticalInfo;
      Dimension totalGap = this.getTotalGap(container, horizontalInfo, verticalInfo);
      int[] minSizes = this.getMinSizes(horizontalInfo);
      if (this.mySameSizeHorizontally) {
         makeSameSizes(minSizes);
      }

      totalGap.width += sum(minSizes);
      minSizes = this.getMinSizes(verticalInfo);
      if (this.mySameSizeVertically) {
         makeSameSizes(minSizes);
      }

      totalGap.height += sum(minSizes);
      return totalGap;
   }

   private static void makeSameSizes(int[] integers) {
      int maximum = integers[0];

      int integerIndex;
      for(integerIndex = 0; integerIndex < integers.length; ++integerIndex) {
         maximum = Math.max(integers[integerIndex], maximum);
      }

      for(integerIndex = 0; integerIndex < integers.length; ++integerIndex) {
         integers[integerIndex] = maximum;
      }

   }

   private static int[] getSameSizes(DimensionInfo dimensionInfo, int availableSize) {
      int[] sameSizes = new int[dimensionInfo.getCellCount()];
      int sameSizesCount = availableSize / sameSizes.length;
      availableSize %= sameSizes.length;

      for(int lengthIndex = 0; lengthIndex < sameSizes.length; ++lengthIndex) {
         sameSizes[lengthIndex] = sameSizesCount;
         if (availableSize > 0) {
            int unusedIncrementResult = sameSizes[lengthIndex]++;
            --availableSize;
         }
      }

      return sameSizes;
   }

   public final Dimension preferredLayoutSize(Container container) {
      this.validateInfos(container);
      DimensionInfo horizontalInfo = this.myHorizontalInfo;
      DimensionInfo verticalInfo = this.myVerticalInfo;
      Dimension totalGap = this.getTotalGap(container, horizontalInfo, verticalInfo);
      int[] prefSizes = this.getPrefSizes(horizontalInfo);
      if (this.mySameSizeHorizontally) {
         makeSameSizes(prefSizes);
      }

      totalGap.width += sum(prefSizes);
      prefSizes = this.getPrefSizes(verticalInfo);
      if (this.mySameSizeVertically) {
         makeSameSizes(prefSizes);
      }

      totalGap.height += sum(prefSizes);
      return totalGap;
   }

   private static int sum(int[] integers) {
      int total = 0;

      for(int integersCount = integers.length - 1; integersCount >= 0; --integersCount) {
         total += integers[integersCount];
      }

      return total;
   }

   private Dimension getTotalGap(Container container, DimensionInfo dimensionInfo, DimensionInfo dimensionInfo2) {
      Insets insets = getInsets(container);
      return new Dimension(insets.left + insets.right + countGap(dimensionInfo, 0, dimensionInfo.getCellCount()) + this.myMargin.left + this.myMargin.right, insets.top + insets.bottom + countGap(dimensionInfo2, 0, dimensionInfo2.getCellCount()) + this.myMargin.top + this.myMargin.bottom);
   }

   private static int getDesignTimeInsets(Container parent) {
      while(parent != null) {
         Integer designTimeInsets;
         if (parent instanceof JComponent && (designTimeInsets = (Integer)((JComponent)parent).getClientProperty(DESIGN_TIME_INSETS)) != null) {
            return designTimeInsets;
         }

         parent = parent.getParent();
      }

      return 0;
   }

   private static Insets getInsets(Container container) {
      Insets insets = container.getInsets();
      int designTimeInsets;
      return (designTimeInsets = getDesignTimeInsets(container)) != 0 ? new Insets(insets.top + designTimeInsets, insets.left + designTimeInsets, insets.bottom + designTimeInsets, insets.right + designTimeInsets) : insets;
   }

   private static int countGap(DimensionInfo dimensionInfo, int startCell, int cellCount) {
      int gapCount = 0;

      for(cellCount = startCell + cellCount - 2; cellCount >= startCell; --cellCount) {
         if (shouldAddGapAfterCell(dimensionInfo, cellCount)) {
            ++gapCount;
         }
      }

      return gapCount * dimensionInfo.getGap();
   }

   private static boolean shouldAddGapAfterCell(DimensionInfo dimensionInfo, int cellIndex) {
      if (cellIndex >= 0 && cellIndex < dimensionInfo.getCellCount()) {
         boolean hasComponentEndingHere = false;
         boolean hasComponentStartingNext = false;
         int nextNonEmptyCell = -1;

         int getCellIndex;
         for(getCellIndex = cellIndex + 1; getCellIndex < dimensionInfo.getCellCount(); ++getCellIndex) {
            if (!isCellEmpty(dimensionInfo, getCellIndex)) {
               nextNonEmptyCell = getCellIndex;
               break;
            }
         }

         for(getCellIndex = 0; getCellIndex < dimensionInfo.getComponentCount(); ++getCellIndex) {
            Component component;
            if (!((component = dimensionInfo.getComponent(getCellIndex)) instanceof Spacer)) {
               if (dimensionInfo.componentBelongsCell(getCellIndex, cellIndex) && DimensionInfo.findAlignedChild(component, dimensionInfo.getConstraints(getCellIndex)) != null) {
                  return true;
               }

               if (dimensionInfo.getCell(getCellIndex) == nextNonEmptyCell) {
                  hasComponentStartingNext = true;
               }

               if (dimensionInfo.getCell(getCellIndex) + dimensionInfo.getSpan(getCellIndex) - 1 == cellIndex) {
                  hasComponentEndingHere = true;
               }
            }
         }

         if (hasComponentStartingNext && hasComponentEndingHere) {
            return true;
         } else {
            return false;
         }
      } else {
         throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + dimensionInfo.getCellCount());
      }
   }

   private static boolean isCellEmpty(DimensionInfo dimensionInfo, int cellIndex) {
      if (cellIndex >= 0 && cellIndex < dimensionInfo.getCellCount()) {
         for(int getComponentIndex = 0; getComponentIndex < dimensionInfo.getComponentCount(); ++getComponentIndex) {
            Component component = dimensionInfo.getComponent(getComponentIndex);
            if (dimensionInfo.getCell(getComponentIndex) == cellIndex && !(component instanceof Spacer)) {
               return false;
            }
         }

         return true;
      } else {
         throw new IllegalArgumentException("wrong cellIndex: " + cellIndex + "; cellCount=" + dimensionInfo.getCellCount());
      }
   }

   public final void layoutContainer(Container container) {
      this.validateInfos(container);
      LayoutState layoutState = this.myLayoutState;
      DimensionInfo horizontalInfo = this.myHorizontalInfo;
      DimensionInfo verticalInfo = this.myVerticalInfo;
      Insets insets = getInsets(container);
      int cell = this.checkSetSizesFromParent(container, insets);
      Dimension totalGap = this.getTotalGap(container, horizontalInfo, verticalInfo);
      Dimension size;
      Dimension dimension = size = container.getSize();
      dimension.width -= totalGap.width;
      size.height -= totalGap.height;
      Dimension dimension2;
      dimension = dimension2 = this.preferredLayoutSize(container);
      dimension.width -= totalGap.width;
      dimension2.height -= totalGap.height;
      Dimension dimension3;
      dimension = dimension3 = this.minimumLayoutSize(container);
      dimension.width -= totalGap.width;
      dimension3.height -= totalGap.height;
      int currentY;
      int yIndex;
      int[] integers;
      if ((cell & 1) == 0) {
         if (this.mySameSizeVertically) {
            integers = getSameSizes(verticalInfo, Math.max(size.height, dimension3.height));
         } else if (size.height < dimension2.height) {
            integers = this.getMinSizes(verticalInfo);
            this.new_doIt(integers, 0, verticalInfo.getCellCount(), size.height, verticalInfo, true);
         } else {
            integers = this.getPrefSizes(verticalInfo);
            this.new_doIt(integers, 0, verticalInfo.getCellCount(), size.height, verticalInfo, false);
         }

         currentY = insets.top + this.myMargin.top;

         for(yIndex = 0; yIndex < integers.length; ++yIndex) {
            this.myYs[yIndex] = currentY;
            this.myHeights[yIndex] = integers[yIndex];
            currentY += integers[yIndex];
            if (shouldAddGapAfterCell(verticalInfo, yIndex)) {
               currentY += verticalInfo.getGap();
            }
         }
      }

      if ((cell & 2) == 0) {
         if (this.mySameSizeHorizontally) {
            integers = getSameSizes(horizontalInfo, Math.max(size.width, dimension3.width));
         } else if (size.width < dimension2.width) {
            integers = this.getMinSizes(horizontalInfo);
            this.new_doIt(integers, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, true);
         } else {
            integers = this.getPrefSizes(horizontalInfo);
            this.new_doIt(integers, 0, horizontalInfo.getCellCount(), size.width, horizontalInfo, false);
         }

         currentY = insets.left + this.myMargin.left;

         for(yIndex = 0; yIndex < integers.length; ++yIndex) {
            this.myXs[yIndex] = currentY;
            this.myWidths[yIndex] = integers[yIndex];
            currentY += integers[yIndex];
            if (shouldAddGapAfterCell(horizontalInfo, yIndex)) {
               currentY += horizontalInfo.getGap();
            }
         }
      }

      for(int getComponentIndex = 0; getComponentIndex < layoutState.getComponentCount(); ++getComponentIndex) {
         GridConstraints constraints = layoutState.getConstraints(getComponentIndex);
         Component component = layoutState.getComponent(getComponentIndex);
         int cell2 = horizontalInfo.getCell(getComponentIndex);
         int span = horizontalInfo.getSpan(getComponentIndex);
         cell = verticalInfo.getCell(getComponentIndex);
         int span2 = verticalInfo.getSpan(getComponentIndex);
         span = this.myXs[cell2 + span - 1] + this.myWidths[cell2 + span - 1] - this.myXs[cell2];
         span2 = this.myYs[cell + span2 - 1] + this.myHeights[cell + span2 - 1] - this.myYs[cell];
         dimension2 = new Dimension(span, span2);
         if ((constraints.getFill() & 1) == 0) {
            dimension2.width = Math.min(dimension2.width, horizontalInfo.getPreferredWidth(getComponentIndex));
         }

         if ((constraints.getFill() & 2) == 0) {
            dimension2.height = Math.min(dimension2.height, verticalInfo.getPreferredWidth(getComponentIndex));
         }

         Util.adjustSize(component, constraints, dimension2);
         int width = 0;
         int height = 0;
         if ((constraints.getAnchor() & 4) != 0) {
            width = span - dimension2.width;
         } else if ((constraints.getAnchor() & 8) == 0) {
            width = (span - dimension2.width) / 2;
         }

         if ((constraints.getAnchor() & 2) != 0) {
            height = span2 - dimension2.height;
         } else if ((constraints.getAnchor() & 1) == 0) {
            height = (span2 - dimension2.height) / 2;
         }

         span = 10 * constraints.getIndent();
         dimension2.width -= span;
         width += span;
         component.setBounds(this.myXs[cell2] + width, this.myYs[cell] + height, dimension2.width, dimension2.height);
      }

   }

   private int checkSetSizesFromParent(Container parent, Insets insets) {
      int inheritedAxes = 0;
      GridLayoutManager layout = null;
      GridConstraints layout2 = null;
      Container parent2;
      if ((parent2 = parent.getParent()) != null) {
         if (parent2.getLayout() instanceof GridLayoutManager) {
            layout2 = (layout = (GridLayoutManager)parent2.getLayout()).getConstraintsForComponent(parent);
         } else if ((parent = parent2.getParent()) != null && parent.getLayout() instanceof GridLayoutManager) {
            layout2 = (layout = (GridLayoutManager)parent.getLayout()).getConstraintsForComponent(parent2);
         }
      }

      if (layout != null && layout2.isUseParentLayout()) {
         int[] integers;
         int lastRow;
         int parentRow;
         int yIndex;
         if (this.myRowStretches.length == layout2.getRowSpan()) {
            parentRow = layout2.getRow();
            this.myYs[0] = insets.top + this.myMargin.top;
            this.myHeights[0] = layout.myHeights[parentRow] - this.myYs[0];

            for(yIndex = 1; yIndex < this.myRowStretches.length; ++yIndex) {
               this.myYs[yIndex] = layout.myYs[yIndex + parentRow] - layout.myYs[parentRow];
               this.myHeights[yIndex] = layout.myHeights[yIndex + parentRow];
            }

            integers = this.myHeights;
            lastRow = this.myRowStretches.length - 1;
            integers[lastRow] -= insets.bottom + this.myMargin.bottom;
            inheritedAxes = 1;
         }

         if (this.myColumnStretches.length == layout2.getColSpan()) {
            parentRow = layout2.getColumn();
            this.myXs[0] = insets.left + this.myMargin.left;
            this.myWidths[0] = layout.myWidths[parentRow] - this.myXs[0];

            for(yIndex = 1; yIndex < this.myColumnStretches.length; ++yIndex) {
               this.myXs[yIndex] = layout.myXs[yIndex + parentRow] - layout.myXs[parentRow];
               this.myWidths[yIndex] = layout.myWidths[yIndex + parentRow];
            }

            integers = this.myWidths;
            lastRow = this.myColumnStretches.length - 1;
            integers[lastRow] -= insets.right + this.myMargin.right;
            inheritedAxes |= 2;
         }
      }

      return inheritedAxes;
   }

   public final void invalidateLayout(Container container) {
      this.myLayoutState = null;
      this.myHorizontalInfo = null;
      this.myVerticalInfo = null;
   }

   final void validateInfos(Container container) {
      if (this.myLayoutState == null) {
         this.myLayoutState = new LayoutState(this, getDesignTimeInsets(container) == 0);
         this.myHorizontalInfo = new HorizontalInfo(this.myLayoutState, getHGapImpl(container));
         this.myVerticalInfo = new VerticalInfo(this.myLayoutState, getVGapImpl(container));
      }

   }

   public final int[] getXs() {
      return this.myXs;
   }

   public final int[] getWidths() {
      return this.myWidths;
   }

   public final int[] getYs() {
      return this.myYs;
   }

   public final int[] getHeights() {
      return this.myHeights;
   }

   public final int[] getCoords(boolean isRow) {
      return isRow ? this.myYs : this.myXs;
   }

   public final int[] getSizes(boolean isRow) {
      return isRow ? this.myHeights : this.myWidths;
   }

   private int[] getMinSizes(DimensionInfo dimensionInfo) {
      return this.getMinOrPrefSizes(dimensionInfo, true);
   }

   private int[] getPrefSizes(DimensionInfo dimensionInfo) {
      return this.getMinOrPrefSizes(dimensionInfo, false);
   }

   private int[] getMinOrPrefSizes(DimensionInfo dimensionInfo, boolean minimum) {
      int[] minOrPrefSizes = new int[dimensionInfo.getCellCount()];

      int minOrPrefSizeIndex;
      for(minOrPrefSizeIndex = 0; minOrPrefSizeIndex < minOrPrefSizes.length; ++minOrPrefSizeIndex) {
         minOrPrefSizes[minOrPrefSizeIndex] = this.myMinCellSize;
      }

      int maximum;
      int getCell;
      int getSpan;
      for(minOrPrefSizeIndex = dimensionInfo.getComponentCount() - 1; minOrPrefSizeIndex >= 0; --minOrPrefSizeIndex) {
         if (dimensionInfo.getSpan(minOrPrefSizeIndex) == 1) {
            maximum = minimum ? getMin2(dimensionInfo, minOrPrefSizeIndex) : Math.max(dimensionInfo.getMinimumWidth(minOrPrefSizeIndex), dimensionInfo.getPreferredWidth(minOrPrefSizeIndex));
            getCell = dimensionInfo.getCell(minOrPrefSizeIndex);
            getSpan = countGap(dimensionInfo, getCell, dimensionInfo.getSpan(minOrPrefSizeIndex));
            maximum = Math.max(maximum - getSpan, 0);
            minOrPrefSizes[getCell] = Math.max(minOrPrefSizes[getCell], maximum);
         }
      }

      updateSizesFromChildren(dimensionInfo, minimum, minOrPrefSizes);
      boolean[] states = new boolean[dimensionInfo.getCellCount()];

      for(maximum = dimensionInfo.getComponentCount() - 1; maximum >= 0; --maximum) {
         getCell = minimum ? getMin2(dimensionInfo, maximum) : Math.max(dimensionInfo.getMinimumWidth(maximum), dimensionInfo.getPreferredWidth(maximum));
         getSpan = dimensionInfo.getSpan(maximum);
         int cell = dimensionInfo.getCell(maximum);
         int gap = countGap(dimensionInfo, cell, getSpan);
         getCell = Math.max(getCell - gap, 0);
         Arrays.fill(states, false);
         gap = 0;

         for(int spanIndex = 0; spanIndex < getSpan; ++spanIndex) {
            gap += minOrPrefSizes[spanIndex + cell];
            states[spanIndex + cell] = true;
         }

         if (gap < getCell) {
            boolean[] states2 = new boolean[states.length];
            this.getCellsWithHigherPriorities(dimensionInfo, states, states2, false, minOrPrefSizes);
            distribute(states2, dimensionInfo, getCell - gap, minOrPrefSizes);
         }
      }

      return minOrPrefSizes;
   }

   private static void updateSizesFromChildren(DimensionInfo dimensionInfo, boolean minimum, int[] integers) {
      for(int getComponentCount = dimensionInfo.getComponentCount() - 1; getComponentCount >= 0; --getComponentCount) {
         Component component = dimensionInfo.getComponent(getComponentCount);
         if (dimensionInfo.getConstraints(getComponentCount).isUseParentLayout() && component instanceof Container) {
            Container component2;
            if ((component2 = (Container)component).getLayout() instanceof GridLayoutManager) {
               updateSizesFromChild(dimensionInfo, minimum, integers, component2, getComponentCount);
            } else if (component2.getComponentCount() == 1 && component2.getComponent(0) instanceof Container && (component2 = (Container)component2.getComponent(0)).getLayout() instanceof GridLayoutManager) {
               updateSizesFromChild(dimensionInfo, minimum, integers, component2, getComponentCount);
            }
         }
      }

   }

   private static void updateSizesFromChild(DimensionInfo dimensionInfo, boolean minimum, int[] integers, Container container, int componentIndex) {
      GridLayoutManager layout = (GridLayoutManager)container.getLayout();
      if (dimensionInfo.getSpan(componentIndex) == dimensionInfo.getChildLayoutCellCount(layout)) {
         layout.validateInfos(container);
         DimensionInfo dimensionInfo2 = dimensionInfo instanceof HorizontalInfo ? layout.myHorizontalInfo : layout.myVerticalInfo;
         int[] minOrPrefSizes = layout.getMinOrPrefSizes(dimensionInfo2, minimum);
         int cell = dimensionInfo.getCell(componentIndex);

         for(int lengthIndex = 0; lengthIndex < minOrPrefSizes.length; ++lengthIndex) {
            integers[cell + lengthIndex] = Math.max(integers[cell + lengthIndex], minOrPrefSizes[lengthIndex]);
         }
      }

   }

   private static int getMin2(DimensionInfo dimensionInfo, int componentIndex) {
      int min2;
      if ((dimensionInfo.getSizePolicy(componentIndex) & 1) != 0) {
         min2 = dimensionInfo.getMinimumWidth(componentIndex);
      } else {
         min2 = Math.max(dimensionInfo.getMinimumWidth(componentIndex), dimensionInfo.getPreferredWidth(componentIndex));
      }

      return min2;
   }

   private void new_doIt(int[] integers, int startCell, int cellCount, int availableSize, DimensionInfo dimensionInfo, boolean checkPreferredSize) {
      availableSize = availableSize;

      for(int integerIndex = startCell; integerIndex < startCell + cellCount; ++integerIndex) {
         availableSize -= integers[integerIndex];
      }

      if (availableSize > 0) {
         boolean[] states = new boolean[dimensionInfo.getCellCount()];

         for(int stateIndex = startCell; stateIndex < startCell + cellCount; ++stateIndex) {
            states[stateIndex] = true;
         }

         boolean[] states2 = new boolean[dimensionInfo.getCellCount()];
         this.getCellsWithHigherPriorities(dimensionInfo, states, states2, checkPreferredSize, integers);
         distribute(states2, dimensionInfo, availableSize, integers);
      }
   }

   private static void distribute(boolean[] states, DimensionInfo dimensionInfo, int pixelsToDistribute, int[] integers) {
      int totalStretch = 0;

      int stateIndex;
      for(stateIndex = 0; stateIndex < dimensionInfo.getCellCount(); ++stateIndex) {
         if (states[stateIndex]) {
            totalStretch += dimensionInfo.getStretch(stateIndex);
         }
      }

      stateIndex = pixelsToDistribute;

      for(int getCellIndex = 0; getCellIndex < dimensionInfo.getCellCount(); ++getCellIndex) {
         if (states[getCellIndex]) {
            int getStretch = stateIndex * dimensionInfo.getStretch(getCellIndex) / totalStretch;
            integers[getCellIndex] += getStretch;
            pixelsToDistribute -= getStretch;
         }
      }

      if (pixelsToDistribute != 0) {
         for(stateIndex = 0; stateIndex < dimensionInfo.getCellCount(); ++stateIndex) {
            if (states[stateIndex]) {
               int unusedIncrementResult = integers[stateIndex]++;
               --pixelsToDistribute;
               if (pixelsToDistribute == 0) {
                  break;
               }
            }
         }
      }

      if (pixelsToDistribute != 0) {
         throw new IllegalStateException("toDistribute = " + pixelsToDistribute);
      }
   }

   private void getCellsWithHigherPriorities(DimensionInfo dimensionInfo, boolean[] states, boolean[] states2, boolean checkPreferredSize, int[] integers) {
      Arrays.fill(states2, false);
      int candidateCount = 0;
      if (checkPreferredSize) {
         int[] minOrPrefSizes = this.getMinOrPrefSizes(dimensionInfo, false);

         for(int lengthIndex = 0; lengthIndex < states.length; ++lengthIndex) {
            if (states[lengthIndex] && !isCellEmpty(dimensionInfo, lengthIndex) && minOrPrefSizes[lengthIndex] > integers[lengthIndex]) {
               states2[lengthIndex] = true;
               ++candidateCount;
            }
         }

         if (candidateCount > 0) {
            return;
         }
      }

      int stateIndex;
      for(stateIndex = 0; stateIndex < states.length; ++stateIndex) {
         if (states[stateIndex] && (dimensionInfo.getCellSizePolicy(stateIndex) & 4) != 0) {
            states2[stateIndex] = true;
            ++candidateCount;
         }
      }

      if (candidateCount <= 0) {
         for(stateIndex = 0; stateIndex < states.length; ++stateIndex) {
            if (states[stateIndex] && (dimensionInfo.getCellSizePolicy(stateIndex) & 2) != 0) {
               states2[stateIndex] = true;
               ++candidateCount;
            }
         }

         if (candidateCount <= 0) {
            for(stateIndex = 0; stateIndex < states.length; ++stateIndex) {
               if (states[stateIndex] && !isCellEmpty(dimensionInfo, stateIndex)) {
                  states2[stateIndex] = true;
                  ++candidateCount;
               }
            }

            if (candidateCount <= 0) {
               for(stateIndex = 0; stateIndex < states.length; ++stateIndex) {
                  if (states[stateIndex]) {
                     states2[stateIndex] = true;
                  }
               }

            }
         }
      }
   }

   public final boolean isSameSizeHorizontally() {
      return this.mySameSizeHorizontally;
   }

   public final boolean isSameSizeVertically() {
      return this.mySameSizeVertically;
   }

   public final void setSameSizeHorizontally(boolean sameSizeHorizontally) {
      this.mySameSizeHorizontally = sameSizeHorizontally;
   }

   public final void setSameSizeVertically(boolean sameSizeVertically) {
      this.mySameSizeVertically = sameSizeVertically;
   }

   public final int[] getHorizontalGridLines() {
      int[] horizontalGridLines;
      (horizontalGridLines = new int[this.myYs.length + 1])[0] = this.myYs[0];

      for(int yIndex = 0; yIndex < this.myYs.length - 1; ++yIndex) {
         horizontalGridLines[yIndex + 1] = (this.myYs[yIndex] + this.myHeights[yIndex] + this.myYs[yIndex + 1]) / 2;
      }

      horizontalGridLines[this.myYs.length] = this.myYs[this.myYs.length - 1] + this.myHeights[this.myYs.length - 1];
      return horizontalGridLines;
   }

   public final int[] getVerticalGridLines() {
      int[] verticalGridLines;
      (verticalGridLines = new int[this.myXs.length + 1])[0] = this.myXs[0];

      for(int xIndex = 0; xIndex < this.myXs.length - 1; ++xIndex) {
         verticalGridLines[xIndex + 1] = (this.myXs[xIndex] + this.myWidths[xIndex] + this.myXs[xIndex + 1]) / 2;
      }

      verticalGridLines[this.myXs.length] = this.myXs[this.myXs.length - 1] + this.myWidths[this.myXs.length - 1];
      return verticalGridLines;
   }

   public final int getCellCount(boolean isRow) {
      return isRow ? this.getRowCount() : this.getColumnCount();
   }

   public final int getCellSizePolicy(boolean isRow, int cellIndex) {
      DimensionInfo dimensionInfo;
      return (dimensionInfo = isRow ? this.myVerticalInfo : this.myHorizontalInfo) == null ? 0 : dimensionInfo.getCellSizePolicy(cellIndex);
   }
}
