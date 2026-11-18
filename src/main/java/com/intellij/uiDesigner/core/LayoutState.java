package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

public final class LayoutState {
   private final Component[] myComponents;
   private final GridConstraints[] myConstraints;
   private final int myColumnCount;
   private final int myRowCount;
   final Dimension[] myPreferredSizes;
   final Dimension[] myMinimumSizes;

   public LayoutState(GridLayoutManager gridLayoutManager, boolean ignoreInvisibleComponents) {
      ArrayList arrayList = new ArrayList(gridLayoutManager.getComponentCount());
      ArrayList arrayList2 = new ArrayList(gridLayoutManager.getComponentCount());

      for(int getComponentIndex = 0; getComponentIndex < gridLayoutManager.getComponentCount(); ++getComponentIndex) {
         Component component = gridLayoutManager.getComponent(getComponentIndex);
         if (!ignoreInvisibleComponents || component.isVisible()) {
            arrayList.add(component);
            GridConstraints constraints = gridLayoutManager.getConstraints(getComponentIndex);
            arrayList2.add(constraints);
         }
      }

      this.myComponents = (Component[])arrayList.toArray(new Component[arrayList.size()]);
      this.myConstraints = (GridConstraints[])arrayList2.toArray(new GridConstraints[arrayList2.size()]);
      this.myMinimumSizes = new Dimension[this.myComponents.length];
      this.myPreferredSizes = new Dimension[this.myComponents.length];
      this.myColumnCount = gridLayoutManager.getColumnCount();
      this.myRowCount = gridLayoutManager.getRowCount();
   }

   public final int getComponentCount() {
      return this.myComponents.length;
   }

   public final Component getComponent(int componentIndex) {
      return this.myComponents[componentIndex];
   }

   public final GridConstraints getConstraints(int componentIndex) {
      return this.myConstraints[componentIndex];
   }

   public final int getColumnCount() {
      return this.myColumnCount;
   }

   public final int getRowCount() {
      return this.myRowCount;
   }
}
