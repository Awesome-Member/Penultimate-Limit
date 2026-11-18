package MenuPck;

import desktop.graphics.GameGraphics;

public interface UIComponentHandler {
   void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2);

   void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2);

   void clickUIComponent(UIComponent uIComponent15);

   void clickUIComponent(UIComponent uIComponent15, int x, int y);

   void overUIComponent(UIComponent uIComponent15, int x, int y, boolean unusedProgressMarker);

   void dragUIComponent(UIComponent uIComponent15, int pointerY, int pointerY2, int pointerY3, int pointerY4);

   void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy);

   void pressUIComponent(UIComponent uIComponent15, int pointerY, int pointerY2);

   void releasPressedUIComponent(UIComponent uIComponent15);

   void releasPressedUIComponent2(UIComponent uIComponent15);

   void flingUIComponent(UIComponent uIComponent15, int velX, int velY);

   void animateHeightUIComponent(UIComponent uIComponent15, int delta);

   void animateWidthUIComponent(UIComponent uIComponent15, int delta);

   void clickUIComponent2(UIComponent uIComponent15);

   void finishAnimationXUIComponent(UIComponent uIComponent15);

   void animateYUIComponent(UIComponent uIComponent15, int delta);

   void finishAnimationYUIComponent(UIComponent uIComponent15);
}
