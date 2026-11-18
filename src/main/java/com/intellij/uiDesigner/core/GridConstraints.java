package com.intellij.uiDesigner.core;

import java.awt.Dimension;

public final class GridConstraints implements Cloneable {
   public static final GridConstraints[] EMPTY_ARRAY = new GridConstraints[0];
   public static final int FILL_NONE = 0;
   public static final int FILL_HORIZONTAL = 1;
   public static final int FILL_VERTICAL = 2;
   public static final int FILL_BOTH = 3;
   public static final int ANCHOR_CENTER = 0;
   public static final int ANCHOR_NORTH = 1;
   public static final int ANCHOR_SOUTH = 2;
   public static final int ANCHOR_EAST = 4;
   public static final int ANCHOR_WEST = 8;
   public static final int ANCHOR_NORTHEAST = 5;
   public static final int ANCHOR_SOUTHEAST = 6;
   public static final int ANCHOR_SOUTHWEST = 10;
   public static final int ANCHOR_NORTHWEST = 9;
   public static final int SIZEPOLICY_FIXED = 0;
   public static final int SIZEPOLICY_CAN_SHRINK = 1;
   public static final int SIZEPOLICY_CAN_GROW = 2;
   public static final int SIZEPOLICY_WANT_GROW = 4;
   public static final int ALIGN_LEFT = 0;
   public static final int ALIGN_CENTER = 1;
   public static final int ALIGN_RIGHT = 2;
   public static final int ALIGN_FILL = 3;
   private int myRow;
   private int myColumn;
   private int myRowSpan;
   private int myColSpan;
   private int myVSizePolicy;
   private int myHSizePolicy;
   private int myFill;
   private int myAnchor;
   public final Dimension myMinimumSize;
   public final Dimension myPreferredSize;
   public final Dimension myMaximumSize;
   private int myIndent;
   private boolean myUseParentLayout;

   public GridConstraints() {
      this.myRowSpan = 1;
      this.myColSpan = 1;
      this.myVSizePolicy = 3;
      this.myHSizePolicy = 3;
      this.myFill = 0;
      this.myAnchor = 0;
      this.myMinimumSize = new Dimension(-1, -1);
      this.myPreferredSize = new Dimension(-1, -1);
      this.myMaximumSize = new Dimension(-1, -1);
      this.myIndent = 0;
   }

   public GridConstraints(int row, int column, int rowSpan, int colSpan, int anchor, int fill, int hSizePolicy, int vSizePolicy, Dimension dimension, Dimension dimension2, Dimension dimension3) {
      this.myRow = row;
      this.myColumn = column;
      this.myRowSpan = rowSpan;
      this.myColSpan = colSpan;
      this.myAnchor = anchor;
      this.myFill = fill;
      this.myHSizePolicy = hSizePolicy;
      this.myVSizePolicy = vSizePolicy;
      this.myMinimumSize = dimension != null ? new Dimension(dimension) : new Dimension(-1, -1);
      this.myPreferredSize = dimension2 != null ? new Dimension(dimension2) : new Dimension(-1, -1);
      this.myMaximumSize = dimension3 != null ? new Dimension(dimension3) : new Dimension(-1, -1);
      this.myIndent = 0;
   }

   public GridConstraints(int row, int column, int rowSpan, int columnSpan, int anchor, int fill, int horizontalSizePolicy, int verticalSizePolicy, Dimension dimension, Dimension dimension2, Dimension dimension3, int indent) {
      this(row, column, rowSpan, columnSpan, anchor, fill, horizontalSizePolicy, verticalSizePolicy, dimension, dimension2, dimension3);
      this.myIndent = indent;
   }

   public GridConstraints(int row, int column, int rowSpan, int columnSpan, int anchor, int fill, int horizontalSizePolicy, int verticalSizePolicy, Dimension dimension, Dimension dimension2, Dimension dimension3, int indent, boolean useParentLayout) {
      this(row, column, rowSpan, columnSpan, anchor, fill, horizontalSizePolicy, verticalSizePolicy, dimension, dimension2, dimension3, indent);
      this.myUseParentLayout = useParentLayout;
   }

   public final Object clone() {
      return new GridConstraints(this.myRow, this.myColumn, this.myRowSpan, this.myColSpan, this.myAnchor, this.myFill, this.myHSizePolicy, this.myVSizePolicy, new Dimension(this.myMinimumSize), new Dimension(this.myPreferredSize), new Dimension(this.myMaximumSize), this.myIndent, this.myUseParentLayout);
   }

   public final int getColumn() {
      return this.myColumn;
   }

   public final void setColumn(int column) {
      if (column < 0) {
         throw new IllegalArgumentException("wrong column: " + column);
      } else {
         this.myColumn = column;
      }
   }

   public final int getRow() {
      return this.myRow;
   }

   public final void setRow(int row) {
      if (row < 0) {
         throw new IllegalArgumentException("wrong row: " + row);
      } else {
         this.myRow = row;
      }
   }

   public final int getRowSpan() {
      return this.myRowSpan;
   }

   public final void setRowSpan(int rowSpan) {
      if (rowSpan <= 0) {
         throw new IllegalArgumentException("wrong rowSpan: " + rowSpan);
      } else {
         this.myRowSpan = rowSpan;
      }
   }

   public final int getColSpan() {
      return this.myColSpan;
   }

   public final void setColSpan(int colSpan) {
      if (colSpan <= 0) {
         throw new IllegalArgumentException("wrong colSpan: " + colSpan);
      } else {
         this.myColSpan = colSpan;
      }
   }

   public final int getHSizePolicy() {
      return this.myHSizePolicy;
   }

   public final void setHSizePolicy(int hSizePolicy) {
      if (hSizePolicy >= 0 && hSizePolicy <= 7) {
         this.myHSizePolicy = hSizePolicy;
      } else {
         throw new IllegalArgumentException("invalid sizePolicy: " + hSizePolicy);
      }
   }

   public final int getVSizePolicy() {
      return this.myVSizePolicy;
   }

   public final void setVSizePolicy(int vSizePolicy) {
      if (vSizePolicy >= 0 && vSizePolicy <= 7) {
         this.myVSizePolicy = vSizePolicy;
      } else {
         throw new IllegalArgumentException("invalid sizePolicy: " + vSizePolicy);
      }
   }

   public final int getAnchor() {
      return this.myAnchor;
   }

   public final void setAnchor(int anchor) {
      if (anchor >= 0 && anchor <= 15) {
         this.myAnchor = anchor;
      } else {
         throw new IllegalArgumentException("invalid anchor: " + anchor);
      }
   }

   public final int getFill() {
      return this.myFill;
   }

   public final void setFill(int fill) {
      if (fill != 0 && fill != 1 && fill != 2 && fill != 3) {
         throw new IllegalArgumentException("invalid fill: " + fill);
      } else {
         this.myFill = fill;
      }
   }

   public final int getIndent() {
      return this.myIndent;
   }

   public final void setIndent(int indent) {
      this.myIndent = indent;
   }

   public final boolean isUseParentLayout() {
      return this.myUseParentLayout;
   }

   public final void setUseParentLayout(boolean useParentLayout) {
      this.myUseParentLayout = useParentLayout;
   }

   public final GridConstraints store() {
      GridConstraints gridConstraints;
      (gridConstraints = new GridConstraints()).setRow(this.myRow);
      gridConstraints.setColumn(this.myColumn);
      gridConstraints.setColSpan(this.myColSpan);
      gridConstraints.setRowSpan(this.myRowSpan);
      gridConstraints.setVSizePolicy(this.myVSizePolicy);
      gridConstraints.setHSizePolicy(this.myHSizePolicy);
      gridConstraints.setFill(this.myFill);
      gridConstraints.setAnchor(this.myAnchor);
      gridConstraints.setIndent(this.myIndent);
      gridConstraints.setUseParentLayout(this.myUseParentLayout);
      gridConstraints.myMinimumSize.setSize(this.myMinimumSize);
      gridConstraints.myPreferredSize.setSize(this.myPreferredSize);
      gridConstraints.myMaximumSize.setSize(this.myMaximumSize);
      return gridConstraints;
   }

   public final void restore(GridConstraints gridConstraints) {
      this.myRow = gridConstraints.myRow;
      this.myColumn = gridConstraints.myColumn;
      this.myRowSpan = gridConstraints.myRowSpan;
      this.myColSpan = gridConstraints.myColSpan;
      this.myHSizePolicy = gridConstraints.myHSizePolicy;
      this.myVSizePolicy = gridConstraints.myVSizePolicy;
      this.myFill = gridConstraints.myFill;
      this.myAnchor = gridConstraints.myAnchor;
      this.myIndent = gridConstraints.myIndent;
      this.myUseParentLayout = gridConstraints.myUseParentLayout;
      this.myMinimumSize.setSize(gridConstraints.myMinimumSize);
      this.myPreferredSize.setSize(gridConstraints.myPreferredSize);
      this.myMaximumSize.setSize(gridConstraints.myMaximumSize);
   }

   public final boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof GridConstraints)) {
         return false;
      } else {
         GridConstraints gridConstraints = (GridConstraints)other;
         if (this.myAnchor != gridConstraints.myAnchor) {
            return false;
         } else if (this.myColSpan != gridConstraints.myColSpan) {
            return false;
         } else if (this.myColumn != gridConstraints.myColumn) {
            return false;
         } else if (this.myFill != gridConstraints.myFill) {
            return false;
         } else if (this.myHSizePolicy != gridConstraints.myHSizePolicy) {
            return false;
         } else if (this.myRow != gridConstraints.myRow) {
            return false;
         } else if (this.myRowSpan != gridConstraints.myRowSpan) {
            return false;
         } else if (this.myVSizePolicy != gridConstraints.myVSizePolicy) {
            return false;
         } else {
            label72: {
               if (this.myMaximumSize != null) {
                  if (this.myMaximumSize.equals(gridConstraints.myMaximumSize)) {
                     break label72;
                  }
               } else if (gridConstraints.myMaximumSize == null) {
                  break label72;
               }

               return false;
            }

            label65: {
               if (this.myMinimumSize != null) {
                  if (this.myMinimumSize.equals(gridConstraints.myMinimumSize)) {
                     break label65;
                  }
               } else if (gridConstraints.myMinimumSize == null) {
                  break label65;
               }

               return false;
            }

            if (this.myPreferredSize != null) {
               if (!this.myPreferredSize.equals(gridConstraints.myPreferredSize)) {
                  return false;
               }
            } else if (gridConstraints.myPreferredSize != null) {
               return false;
            }

            if (this.myIndent != gridConstraints.myIndent) {
               return false;
            } else if (this.myUseParentLayout != gridConstraints.myUseParentLayout) {
               return false;
            } else {
               return true;
            }
         }
      }
   }

   public final int hashCode() {
      int row = this.myRow;
      row = row * 29 + this.myColumn;
      row = row * 29 + this.myRowSpan;
      row = row * 29 + this.myColSpan;
      row = row * 29 + this.myVSizePolicy;
      row = row * 29 + this.myHSizePolicy;
      row = row * 29 + this.myFill;
      row = row * 29 + this.myAnchor;
      row = row * 29 + (this.myMinimumSize != null ? this.myMinimumSize.hashCode() : 0);
      row = row * 29 + (this.myPreferredSize != null ? this.myPreferredSize.hashCode() : 0);
      row = row * 29 + (this.myMaximumSize != null ? this.myMaximumSize.hashCode() : 0);
      row = row * 29 + this.myIndent;
      return row * 29 + (this.myUseParentLayout ? 1 : 0);
   }

   public final int getCell(boolean isRow) {
      return isRow ? this.getRow() : this.getColumn();
   }

   public final void setCell(boolean isRow, int cellIndex) {
      if (isRow) {
         this.setRow(cellIndex);
      } else {
         this.setColumn(cellIndex);
      }
   }

   public final int getSpan(boolean isRow) {
      return isRow ? this.getRowSpan() : this.getColSpan();
   }

   public final void setSpan(boolean isRow, int span) {
      if (isRow) {
         this.setRowSpan(span);
      } else {
         this.setColSpan(span);
      }
   }

   public final boolean contains(boolean isRow, int cellIndex) {
      if (isRow) {
         return cellIndex >= this.myRow && cellIndex < this.myRow + this.myRowSpan;
      } else {
         return cellIndex >= this.myColumn && cellIndex < this.myColumn + this.myColSpan;
      }
   }

   public final String toString() {
      return "GridConstraints (row=" + this.myRow + ", col=" + this.myColumn + ", rowspan=" + this.myRowSpan + ", colspan=" + this.myColSpan + ")";
   }
}
