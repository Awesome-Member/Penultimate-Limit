package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;

public abstract class DimensionInfo {
   private final int[] myCell;
   private final int[] mySpan;
   protected final LayoutState myLayoutState;
   private final int[] myStretches;
   private final int[] mySpansAfterElimination;
   private final int[] myCellSizePolicies;
   private final int myGap;

   public DimensionInfo(LayoutState layoutState, int gap) {
      if (layoutState == null) {
         throw new IllegalArgumentException("layoutState cannot be null");
      } else if (gap < 0) {
         throw new IllegalArgumentException("invalid gap: " + gap);
      } else {
         this.myLayoutState = layoutState;
         this.myGap = gap;
         this.myCell = new int[layoutState.getComponentCount()];
         this.mySpan = new int[layoutState.getComponentCount()];

         for(gap = 0; gap < layoutState.getComponentCount(); ++gap) {
            GridConstraints constraints = layoutState.getConstraints(gap);
            this.myCell[gap] = this.getOriginalCell(constraints);
            this.mySpan[gap] = this.getOriginalSpan(constraints);
         }

         this.myStretches = new int[this.getCellCount()];

         for(gap = 0; gap < this.myStretches.length; ++gap) {
            this.myStretches[gap] = 1;
         }

         ArrayList arrayList = new ArrayList();
         this.mySpansAfterElimination = (int[])this.mySpan.clone();
         Util.eliminate((int[])this.myCell.clone(), this.mySpansAfterElimination, arrayList);
         this.myCellSizePolicies = new int[this.getCellCount()];

         for(int cellSizePolicieIndex = 0; cellSizePolicieIndex < this.myCellSizePolicies.length; ++cellSizePolicieIndex) {
            this.myCellSizePolicies[cellSizePolicieIndex] = this.getCellSizePolicyImpl(cellSizePolicieIndex, arrayList);
         }

      }
   }

   public final int getComponentCount() {
      return this.myLayoutState.getComponentCount();
   }

   public final Component getComponent(int componentIndex) {
      return this.myLayoutState.getComponent(componentIndex);
   }

   public final GridConstraints getConstraints(int componentIndex) {
      return this.myLayoutState.getConstraints(componentIndex);
   }

   public abstract int getCellCount();

   public abstract int getPreferredWidth(int componentIndex);

   public abstract int getMinimumWidth(int componentIndex);

   public abstract DimensionInfo getDimensionInfo(GridLayoutManager gridLayoutManager);

   public final int getCell(int componentIndex) {
      return this.myCell[componentIndex];
   }

   public final int getSpan(int componentIndex) {
      return this.mySpan[componentIndex];
   }

   public final int getStretch(int cellIndex) {
      return this.myStretches[cellIndex];
   }

   protected abstract int getOriginalCell(GridConstraints gridConstraints);

   protected abstract int getOriginalSpan(GridConstraints gridConstraints);

   abstract int getSizePolicy(int componentIndex);

   abstract int getChildLayoutCellCount(GridLayoutManager gridLayoutManager);

   public final int getGap() {
      return this.myGap;
   }

   public boolean componentBelongsCell(int span, int cellIndex) {
      int cell = this.getCell(span);
      span = this.getSpan(span);
      return cell <= cellIndex && cellIndex < cell + span;
   }

   public final int getCellSizePolicy(int cellIndex) {
      return this.myCellSizePolicies[cellIndex];
   }

   private int getCellSizePolicyImpl(int cellIndex, ArrayList arrayList) {
      int cellSizePolicyFromInheriting;
      if ((cellSizePolicyFromInheriting = this.getCellSizePolicyFromInheriting(cellIndex)) != -1) {
         return cellSizePolicyFromInheriting;
      } else {
         for(cellSizePolicyFromInheriting = arrayList.size() - 1; cellSizePolicyFromInheriting >= 0; --cellSizePolicyFromInheriting) {
            if (cellIndex == (Integer)arrayList.get(cellSizePolicyFromInheriting)) {
               return 1;
            }
         }

         return this.calcCellSizePolicy(cellIndex);
      }
   }

   private int calcCellSizePolicy(int cellIndex) {
      boolean canShrink = true;
      boolean canGrow = false;
      boolean wantGrow = false;
      boolean allCanGrow = true;
      boolean allWantGrow = true;
      int componentCount = 0;

      for(int spansAfterEliminationIndex = 0; spansAfterEliminationIndex < this.getComponentCount(); ++spansAfterEliminationIndex) {
         if (this.componentBelongsCell(spansAfterEliminationIndex, cellIndex)) {
            ++componentCount;
            int sizePolicy;
            boolean componentCanShrink = ((sizePolicy = this.getSizePolicy(spansAfterEliminationIndex)) & 1) != 0;
            boolean componentCanGrow = (sizePolicy & 2) != 0;
            boolean componentWantsGrow = (sizePolicy & 4) != 0;
            if (this.getCell(spansAfterEliminationIndex) == cellIndex && this.mySpansAfterElimination[spansAfterEliminationIndex] == 1) {
               canShrink &= componentCanShrink;
               canGrow |= componentCanGrow;
               wantGrow |= componentWantsGrow;
            }

            if (!componentCanGrow) {
               allCanGrow = false;
            }

            if (!componentWantsGrow) {
               allWantGrow = false;
            }
         }
      }

      return (canShrink ? 1 : 0) | (!canGrow && (componentCount <= 0 || !allCanGrow) ? 0 : 2) | (!wantGrow && (componentCount <= 0 || !allWantGrow) ? 0 : 4);
   }

   private int getCellSizePolicyFromInheriting(int cellIndex) {
      int nonInheritedComponentCount = 0;
      int cellSizePolicyFromInheriting = -1;

      for(int getComponentCount = this.getComponentCount() - 1; getComponentCount >= 0; --getComponentCount) {
         if (this.componentBelongsCell(getComponentCount, cellIndex)) {
            Component component = this.getComponent(getComponentCount);
            GridConstraints constraints = this.getConstraints(getComponentCount);
            Container alignedChild;
            if ((alignedChild = findAlignedChild(component, constraints)) != null) {
               GridLayoutManager layout;
               (layout = (GridLayoutManager)alignedChild.getLayout()).validateInfos(alignedChild);
               int dimensionInfo = this.getDimensionInfo(layout).calcCellSizePolicy(cellIndex - this.getOriginalCell(constraints));
               if (cellSizePolicyFromInheriting == -1) {
                  cellSizePolicyFromInheriting = dimensionInfo;
               } else {
                  cellSizePolicyFromInheriting |= dimensionInfo;
               }
            } else if (this.getOriginalCell(constraints) == cellIndex && this.getOriginalSpan(constraints) == 1 && !(component instanceof Spacer)) {
               ++nonInheritedComponentCount;
            }
         }
      }

      if (nonInheritedComponentCount > 0) {
         return -1;
      } else {
         return cellSizePolicyFromInheriting;
      }
   }

   public static Container findAlignedChild(Component component, GridConstraints gridConstraints) {
      if (gridConstraints.isUseParentLayout() && component instanceof Container) {
         Container component2;
         if ((component2 = (Container)component).getLayout() instanceof GridLayoutManager) {
            return component2;
         }

         if (component2.getComponentCount() == 1 && component2.getComponent(0) instanceof Container && (component2 = (Container)component2.getComponent(0)).getLayout() instanceof GridLayoutManager) {
            return component2;
         }
      }

      return null;
   }

   protected final Dimension getPreferredSize(int componentIndex) {
      Dimension preferredSize;
      if ((preferredSize = this.myLayoutState.myPreferredSizes[componentIndex]) == null) {
         preferredSize = Util.getPreferredSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
         this.myLayoutState.myPreferredSizes[componentIndex] = preferredSize;
      }

      return preferredSize;
   }

   protected final Dimension getMinimumSize(int componentIndex) {
      Dimension minimumSize;
      if ((minimumSize = this.myLayoutState.myMinimumSizes[componentIndex]) == null) {
         minimumSize = Util.getMinimumSize(this.myLayoutState.getComponent(componentIndex), this.myLayoutState.getConstraints(componentIndex), true);
         this.myLayoutState.myMinimumSizes[componentIndex] = minimumSize;
      }

      return minimumSize;
   }
}
