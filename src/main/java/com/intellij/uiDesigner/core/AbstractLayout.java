package com.intellij.uiDesigner.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

public abstract class AbstractLayout implements LayoutManager2 {
   public static final int DEFAULT_HGAP = 10;
   public static final int DEFAULT_VGAP = 5;
   protected Component[] myComponents;
   protected GridConstraints[] myConstraints;
   protected Insets myMargin;
   private int myHGap;
   private int myVGap;
   private static final Component[] COMPONENT_EMPTY_ARRAY = new Component[0];

   public AbstractLayout() {
      this.myComponents = COMPONENT_EMPTY_ARRAY;
      this.myConstraints = GridConstraints.EMPTY_ARRAY;
      this.myMargin = new Insets(0, 0, 0, 0);
      this.myHGap = -1;
      this.myVGap = -1;
   }

   public final Insets getMargin() {
      return (Insets)this.myMargin.clone();
   }

   public final int getHGap() {
      return this.myHGap;
   }

   protected static int getHGapImpl(Container parent) {
      if (parent == null) {
         throw new IllegalArgumentException("container cannot be null");
      } else {
         while(parent != null) {
            AbstractLayout layout;
            if (parent.getLayout() instanceof AbstractLayout && (layout = (AbstractLayout)parent.getLayout()).getHGap() != -1) {
               return layout.getHGap();
            }

            parent = parent.getParent();
         }

         return 10;
      }
   }

   public final void setHGap(int hGap) {
      if (hGap < -1) {
         throw new IllegalArgumentException("wrong hGap: " + hGap);
      } else {
         this.myHGap = hGap;
      }
   }

   public final int getVGap() {
      return this.myVGap;
   }

   protected static int getVGapImpl(Container parent) {
      if (parent == null) {
         throw new IllegalArgumentException("container cannot be null");
      } else {
         while(parent != null) {
            AbstractLayout layout;
            if (parent.getLayout() instanceof AbstractLayout && (layout = (AbstractLayout)parent.getLayout()).getVGap() != -1) {
               return layout.getVGap();
            }

            parent = parent.getParent();
         }

         return 5;
      }
   }

   public final void setVGap(int vGap) {
      if (vGap < -1) {
         throw new IllegalArgumentException("wrong vGap: " + vGap);
      } else {
         this.myVGap = vGap;
      }
   }

   public final void setMargin(Insets insets) {
      if (insets == null) {
         throw new IllegalArgumentException("margin cannot be null");
      } else {
         this.myMargin = (Insets)insets.clone();
      }
   }

   final int getComponentCount() {
      return this.myComponents.length;
   }

   final Component getComponent(int componentIndex) {
      return this.myComponents[componentIndex];
   }

   final GridConstraints getConstraints(int componentIndex) {
      return this.myConstraints[componentIndex];
   }

   public void addLayoutComponent(Component component, Object constraints2) {
      if (!(constraints2 instanceof GridConstraints)) {
         throw new IllegalArgumentException("constraints: " + constraints2);
      } else {
         Component[] components = new Component[this.myComponents.length + 1];
         System.arraycopy(this.myComponents, 0, components, 0, this.myComponents.length);
         components[this.myComponents.length] = component;
         this.myComponents = components;
         GridConstraints[] constraints = new GridConstraints[this.myConstraints.length + 1];
         System.arraycopy(this.myConstraints, 0, constraints, 0, this.myConstraints.length);
         constraints[this.myConstraints.length] = (GridConstraints)((GridConstraints)constraints2).clone();
         this.myConstraints = constraints;
      }
   }

   public final void addLayoutComponent(String messageText, Component component) {
      throw new UnsupportedOperationException();
   }

   public final void removeLayoutComponent(Component component) {
      int componentIndex;
      if ((componentIndex = this.getComponentIndex(component)) == -1) {
         throw new IllegalArgumentException("component was not added: " + component);
      } else {
         if (this.myComponents.length == 1) {
            this.myComponents = COMPONENT_EMPTY_ARRAY;
         } else {
            Component[] components = new Component[this.myComponents.length - 1];
            System.arraycopy(this.myComponents, 0, components, 0, componentIndex);
            System.arraycopy(this.myComponents, componentIndex + 1, components, componentIndex, this.myComponents.length - componentIndex - 1);
            this.myComponents = components;
         }

         if (this.myConstraints.length == 1) {
            this.myConstraints = GridConstraints.EMPTY_ARRAY;
         } else {
            GridConstraints[] constraints = new GridConstraints[this.myConstraints.length - 1];
            System.arraycopy(this.myConstraints, 0, constraints, 0, componentIndex);
            System.arraycopy(this.myConstraints, componentIndex + 1, constraints, componentIndex, this.myConstraints.length - componentIndex - 1);
            this.myConstraints = constraints;
         }
      }
   }

   public GridConstraints getConstraintsForComponent(Component component) {
      int componentIndex;
      if ((componentIndex = this.getComponentIndex(component)) == -1) {
         throw new IllegalArgumentException("component was not added: " + component);
      } else {
         return this.myConstraints[componentIndex];
      }
   }

   private int getComponentIndex(Component component) {
      for(int componentIndex = 0; componentIndex < this.myComponents.length; ++componentIndex) {
         if (this.myComponents[componentIndex] == component) {
            return componentIndex;
         }
      }

      return -1;
   }

   public final float getLayoutAlignmentX(Container container) {
      return 0.5F;
   }

   public final float getLayoutAlignmentY(Container container) {
      return 0.5F;
   }

   public abstract Dimension maximumLayoutSize(Container container);

   public abstract void invalidateLayout(Container container);

   public abstract Dimension preferredLayoutSize(Container container);

   public abstract Dimension minimumLayoutSize(Container container);

   public abstract void layoutContainer(Container container);
}
