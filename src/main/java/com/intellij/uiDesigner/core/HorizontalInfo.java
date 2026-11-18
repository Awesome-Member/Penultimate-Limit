package com.intellij.uiDesigner.core;

final class HorizontalInfo extends DimensionInfo {
   public HorizontalInfo(LayoutState layoutState, int gap) {
      super(layoutState, gap);
   }

   protected final int getOriginalCell(GridConstraints gridConstraints) {
      return gridConstraints.getColumn();
   }

   protected final int getOriginalSpan(GridConstraints gridConstraints) {
      return gridConstraints.getColSpan();
   }

   final int getSizePolicy(int componentIndex) {
      return this.myLayoutState.getConstraints(componentIndex).getHSizePolicy();
   }

   final int getChildLayoutCellCount(GridLayoutManager gridLayoutManager) {
      return gridLayoutManager.getColumnCount();
   }

   public final int getMinimumWidth(int componentIndex) {
      return this.getMinimumSize(componentIndex).width;
   }

   public final DimensionInfo getDimensionInfo(GridLayoutManager gridLayoutManager) {
      return gridLayoutManager.myHorizontalInfo;
   }

   public final int getCellCount() {
      return this.myLayoutState.getColumnCount();
   }

   public final int getPreferredWidth(int componentIndex) {
      return this.getPreferredSize(componentIndex).width;
   }
}
