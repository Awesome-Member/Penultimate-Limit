package MenuPck;

import desktop.PlatformRuntime;
import java.util.Enumeration;
import java.util.Vector;
import desktop.graphics.GameGraphics;

public final class UIConteiner {
   private Vector components = new Vector();
   private boolean wasPress = false;
   private int pressStartX = 0;
   private int pressStartY = 0;
   private Vector pressIds = new Vector();
   private int lastDragX = -1;
   private int lastDragY = -1;
   private boolean wasDrag = false;
   private Vector touchFlingPositionsAndTimesX = new Vector();
   private Vector touchFlingPositionsAndTimesY = new Vector();
   private int touchFlingLastDeltaX = 0;
   private int touchFlingLastDeltaY = 0;

   public final void functional(int frameTime) {
      Enumeration enumeration = this.components.elements();

      while(enumeration.hasMoreElements()) {
         UIComponent uIComponent15;
         if ((uIComponent15 = (UIComponent)enumeration.nextElement()).enabled) {
            uIComponent15.updateAnimationsAndKineticDrag();
         }
      }

   }

   public final void paint(GameGraphics gameGraphics2) {
      Enumeration enumeration = this.components.elements();

      while(enumeration.hasMoreElements()) {
         UIComponent uIComponent15;
         if ((uIComponent15 = (UIComponent)enumeration.nextElement()).visible && uIComponent15.enabled) {
            uIComponent15.paint(gameGraphics2);
         }
      }

   }

   public final void clear() {
      this.components.removeAllElements();
   }

   public final boolean containsPressId(int id) {
      for(int i = 0; i < this.pressIds.size(); ++i) {
         if (id == (Integer)this.pressIds.elementAt(i)) {
            return true;
         }
      }

      return false;
   }

   public final void addComponent(UIComponent uIComponent15) {
      int id2 = uIComponent15.id;
      UIConteiner uIConteiner = this;

      for(int componentsCount = this.components.size() - 1; componentsCount >= 0; --componentsCount) {
         if (((UIComponent)uIConteiner.components.elementAt(componentsCount)).id == id2) {
            uIConteiner.components.removeElementAt(componentsCount);
         }
      }

      this.components.addElement(uIComponent15);
   }

   public final UIComponent getComponent(int id) {
      Enumeration enumeration = this.components.elements();

      UIComponent uIComponent15;
      do {
         if (!enumeration.hasMoreElements()) {
            return null;
         }
      } while((uIComponent15 = (UIComponent)enumeration.nextElement()).id != id);

      return uIComponent15;
   }

   public final boolean dragPointer(int x, int y) {
      boolean res = false;

      int touchFlingLastDeltaY2;
      for(touchFlingLastDeltaY2 = this.components.size() - 1; touchFlingLastDeltaY2 >= 0; --touchFlingLastDeltaY2) {
         UIComponent uIComponent15;
         if ((uIComponent15 = (UIComponent)this.components.elementAt(touchFlingLastDeltaY2)).enabled && !uIComponent15.ignoreInTouch && uIComponent15.isContentClick(x, y)) {
            if (uIComponent15.handler != null) {
               uIComponent15.handler.overUIComponent(uIComponent15, x - uIComponent15.xContent, y - uIComponent15.yContent, false);
            }

            res = true;
            if (!uIComponent15.translateTouch) {
               break;
            }
         }
      }

      PlatformRuntime.getInstance();
      if (this.wasPress && Math.abs(this.pressStartX - x) <= 5 && Math.abs(this.pressStartY - y) <= 5) {
         return res;
      } else {
         int touchFlingLastDeltaX2;
         if (this.wasPress) {
            this.wasPress = false;

            for(touchFlingLastDeltaX2 = 0; touchFlingLastDeltaX2 < this.pressIds.size(); ++touchFlingLastDeltaX2) {
               touchFlingLastDeltaY2 = (Integer)this.pressIds.elementAt(touchFlingLastDeltaX2);
               UIComponent uIComponent16;
               if ((uIComponent16 = this.getComponent(touchFlingLastDeltaY2)) != null) {
                  uIComponent16.state = 0;
                  if (uIComponent16.handler != null) {
                     uIComponent16.handler.releasPressedUIComponent2(uIComponent16);
                  }
               }
            }

            this.lastDragX = this.pressStartX;
            this.lastDragY = this.pressStartY;
         }

         if (this.lastDragX >= 0 && this.lastDragY >= 0) {
            this.wasDrag = true;
            touchFlingLastDeltaX2 = x - this.lastDragX;
            touchFlingLastDeltaY2 = y - this.lastDragY;

            for(int sizeIndex = 0; sizeIndex < this.pressIds.size(); ++sizeIndex) {
               int pressIds2 = (Integer)this.pressIds.elementAt(sizeIndex);
               UIComponent uIComponent17;
               if ((uIComponent17 = this.getComponent(pressIds2)) != null) {
                  uIComponent17.stopKineticDrag();
                  if (uIComponent17.handler != null) {
                     uIComponent17.handler.dragUIComponent(uIComponent17, touchFlingLastDeltaX2, touchFlingLastDeltaY2, x - uIComponent17.xContent, y - uIComponent17.yContent);
                  }
               }

               if (this.touchFlingLastDeltaX > 0 && touchFlingLastDeltaX2 < 0 || this.touchFlingLastDeltaX < 0 && touchFlingLastDeltaX2 > 0) {
                  this.touchFlingPositionsAndTimesX.removeAllElements();
               }

               if (this.touchFlingLastDeltaY > 0 && touchFlingLastDeltaY2 < 0 || this.touchFlingLastDeltaY < 0 && touchFlingLastDeltaY2 > 0) {
                  this.touchFlingPositionsAndTimesY.removeAllElements();
               }

               this.touchFlingLastDeltaX = touchFlingLastDeltaX2;
               this.touchFlingLastDeltaY = touchFlingLastDeltaY2;
               this.touchFlingPositionsAndTimesX.addElement(new long[]{(long)x, System.currentTimeMillis()});
               this.touchFlingPositionsAndTimesY.addElement(new long[]{(long)y, System.currentTimeMillis()});
               if (this.touchFlingPositionsAndTimesX.size() > 12) {
                  this.touchFlingPositionsAndTimesX.removeElementAt(0);
               }

               if (this.touchFlingPositionsAndTimesY.size() > 12) {
                  this.touchFlingPositionsAndTimesY.removeElementAt(0);
               }
            }
         }

         this.lastDragX = x;
         this.lastDragY = y;
         return res;
      }
   }

   public final boolean pressPointer(int x, int y) {
      int sizeIndex;
      UIComponent uIComponent15;
      for(sizeIndex = this.components.size() - 1; sizeIndex >= 0; --sizeIndex) {
         if ((uIComponent15 = (UIComponent)this.components.elementAt(sizeIndex)).enabled && !uIComponent15.ignoreInTouch && uIComponent15.isContentClick(x, y)) {
            if (uIComponent15.handler != null) {
               uIComponent15.handler.overUIComponent(uIComponent15, x - uIComponent15.xContent, y - uIComponent15.yContent, true);
            }

            if (!uIComponent15.translateTouch) {
               break;
            }
         }
      }

      if (this.pressIds.size() > 0) {
         for(sizeIndex = 0; sizeIndex < this.pressIds.size(); ++sizeIndex) {
            int pressIds2 = (Integer)this.pressIds.elementAt(sizeIndex);
            if ((uIComponent15 = this.getComponent(pressIds2)) != null) {
               uIComponent15.state = 0;
            }
         }

         this.pressIds.removeAllElements();
      }

      this.wasDrag = false;
      this.wasPress = true;
      this.pressStartX = x;
      this.pressStartY = y;
      this.pressIds.removeAllElements();
      this.touchFlingLastDeltaX = 0;
      this.touchFlingLastDeltaY = 0;
      this.touchFlingPositionsAndTimesX.removeAllElements();
      this.touchFlingPositionsAndTimesY.removeAllElements();
      long time = System.currentTimeMillis();
      this.touchFlingPositionsAndTimesX.addElement(new long[]{(long)x, time});
      this.touchFlingPositionsAndTimesY.addElement(new long[]{(long)y, time});
      boolean res = false;

      for(sizeIndex = this.components.size() - 1; sizeIndex >= 0; --sizeIndex) {
         UIComponent uIComponent16;
         if ((uIComponent16 = (UIComponent)this.components.elementAt(sizeIndex)).enabled && !uIComponent16.ignoreInTouch && uIComponent16.isContentClick(x, y)) {
            uIComponent16.state = 1;
            this.pressIds.addElement(uIComponent16.id);
            uIComponent16.stopKineticDrag();
            if (uIComponent16.handler != null) {
               uIComponent16.handler.pressUIComponent(uIComponent16, x - uIComponent16.xContent, y - uIComponent16.yContent);
            }

            res = true;
            if (!uIComponent16.translateTouch) {
               return true;
            }
         }
      }

      return res;
   }

   public final void releasPointer(int x, int y) {
      int sizeIndex;
      for(sizeIndex = this.components.size() - 1; sizeIndex >= 0; --sizeIndex) {
         UIComponent uIComponent15;
         if ((uIComponent15 = (UIComponent)this.components.elementAt(sizeIndex)).enabled && !uIComponent15.ignoreInTouch && uIComponent15.isContentClick(x, y)) {
            if (uIComponent15.handler != null) {
               uIComponent15.handler.releasPressedUIComponent(uIComponent15);
            }

            if (!uIComponent15.translateTouch) {
               break;
            }
         }
      }

      int pressIds3;
      for(sizeIndex = 0; sizeIndex < this.pressIds.size(); ++sizeIndex) {
         pressIds3 = (Integer)this.pressIds.elementAt(sizeIndex);
         UIComponent uIComponent16;
         if ((uIComponent16 = this.getComponent(pressIds3)) != null) {
            uIComponent16.state = 0;
         }
      }

      Vector pressIds2 = this.pressIds;
      this.pressIds = new Vector();
      int sizeIndex2;
      if (this.wasDrag && pressIds2.size() > 0) {
         pressIds3 = 0;
         sizeIndex2 = 0;
         long releaseTime = System.currentTimeMillis();
         this.touchFlingPositionsAndTimesX.addElement(new long[]{(long)x, releaseTime});
         this.touchFlingPositionsAndTimesY.addElement(new long[]{(long)y, releaseTime});
         long[] timestamps = null;

         int sizeIndex3;
         long[] timestamps2;
         for(sizeIndex3 = this.touchFlingPositionsAndTimesX.size() - 1; sizeIndex3 >= 0 && (timestamps2 = (long[])this.touchFlingPositionsAndTimesX.elementAt(sizeIndex3))[1] >= releaseTime - 250L; --sizeIndex3) {
            timestamps = timestamps2;
         }

         long sampleAge;
         if (timestamps != null) {
            sizeIndex3 = (int)((long)x - timestamps[0]);
            sampleAge = releaseTime - timestamps[1];
            if (sizeIndex3 != 0) {
               pressIds3 = (int)(sampleAge * 100L / (long)sizeIndex3);
            }
         }

         timestamps = null;

         for(sizeIndex3 = this.touchFlingPositionsAndTimesY.size() - 1; sizeIndex3 >= 0 && (timestamps2 = (long[])this.touchFlingPositionsAndTimesY.elementAt(sizeIndex3))[1] >= releaseTime - 250L; --sizeIndex3) {
            timestamps = timestamps2;
         }

         if (timestamps != null) {
            sizeIndex3 = (int)((long)y - timestamps[0]);
            sampleAge = releaseTime - timestamps[1];
            if (sizeIndex3 != 0) {
               sizeIndex2 = (int)(sampleAge * 100L / (long)sizeIndex3);
            }
         }

         if (pressIds3 != 0 || sizeIndex2 != 0) {
            for(sizeIndex3 = 0; sizeIndex3 < pressIds2.size(); ++sizeIndex3) {
               int elementAt = (Integer)pressIds2.elementAt(sizeIndex3);
               UIComponent uIComponent17;
               if ((uIComponent17 = this.getComponent(elementAt)) != null) {
                  uIComponent17.stratKineticDrag(pressIds3, sizeIndex2);
                  if (uIComponent17.handler != null) {
                     uIComponent17.handler.flingUIComponent(uIComponent17, pressIds3, sizeIndex2);
                  }
               }
            }
         }
      }

      this.lastDragX = -1;
      this.lastDragY = -1;
      this.wasDrag = false;
      if (this.wasPress) {
         this.wasPress = false;
         PlatformRuntime.getInstance();
         UIComponent uIComponent18;
         int elementAt2;
         if (Math.abs(this.pressStartX - x) <= 5 && Math.abs(this.pressStartY - y) <= 5) {
            for(sizeIndex2 = 0; sizeIndex2 < pressIds2.size(); ++sizeIndex2) {
               elementAt2 = (Integer)pressIds2.elementAt(sizeIndex2);
               if ((uIComponent18 = this.getComponent(elementAt2)) != null && uIComponent18.handler != null && uIComponent18.enabled) {
                  uIComponent18.handler.clickUIComponent(uIComponent18);
                  if (!uIComponent18.ignoreInTouch && uIComponent18.isContentClick(x, y)) {
                     uIComponent18.handler.clickUIComponent(uIComponent18, x - uIComponent18.xContent, y - uIComponent18.yContent);
                  }
               }

               if (uIComponent18 != null && uIComponent18.handler != null) {
                  uIComponent18.handler.releasPressedUIComponent2(uIComponent18);
               }
            }

         } else {
            for(sizeIndex2 = 0; sizeIndex2 < pressIds2.size(); ++sizeIndex2) {
               elementAt2 = (Integer)pressIds2.elementAt(sizeIndex2);
               if ((uIComponent18 = this.getComponent(elementAt2)) != null && uIComponent18.handler != null) {
                  uIComponent18.handler.releasPressedUIComponent2(uIComponent18);
               }
            }

         }
      }
   }
}
