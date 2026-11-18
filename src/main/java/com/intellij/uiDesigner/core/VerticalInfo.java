package com.intellij.uiDesigner.core;

final class VerticalInfo extends DimensionInfo {
   public VerticalInfo(LayoutState layoutState, int gap) {
      super(layoutState, gap);
   }

   protected final int getOriginalCell(GridConstraints gridConstraints) {
      return gridConstraints.getRow();
   }

   protected final int getOriginalSpan(GridConstraints gridConstraints) {
      return gridConstraints.getRowSpan();
   }

   final int getSizePolicy(int componentIndex) {
      return this.myLayoutState.getConstraints(componentIndex).getVSizePolicy();
   }

   final int getChildLayoutCellCount(GridLayoutManager gridLayoutManager) {
      return gridLayoutManager.getRowCount();
   }

   public final int getMinimumWidth(int componentIndex) {
      return this.getMinimumSize(componentIndex).height;
   }

   public final DimensionInfo getDimensionInfo(GridLayoutManager gridLayoutManager) {
      return gridLayoutManager.myVerticalInfo;
   }

   public final int getCellCount() {
      return this.myLayoutState.getRowCount();
   }

   public final int getPreferredWidth(int componentIndex) {
      return this.getPreferredSize(componentIndex).height;
   }
}
