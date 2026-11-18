package MenuPck;

import desktop.graphics.GameGraphics;

public interface UIComponentDecorator {
   void initPaddings(UIComponent uIComponent15);

   void correctPaddingsFinal(UIComponent uIComponent15);

   void fillBackground(UIComponent uIComponent15, GameGraphics gameGraphics2);

   void draw(UIComponent uIComponent15, GameGraphics gameGraphics2);

   void paintBordersPhase2(UIComponent uIComponent15, GameGraphics gameGraphics2);
}
