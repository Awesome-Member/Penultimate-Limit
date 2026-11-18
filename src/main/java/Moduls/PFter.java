package Moduls;

import desktop.graphics.GameGraphics;

public interface PFter {
   void functionate();

   void functionateUnderDrawProgress();

   void paint(GameGraphics gameGraphics2, boolean unusedProgressMarker);

   void onAfterHardLoad();

   void wheelRot(int num);

   void movePointer(int pointerX, int pointerY);

   void pressPointer(int pointerX, int pointerY);

   void dragPointer(int pointerX, int pointerY);

   void releasePointer(int pointerX, int pointerY);

   void pressLeftSoft();

   void pressRightSoft();

   void releaseLeftSoft();

   void releaseRightSoft();

   void pressStar();

   void pressSharp();

   void press1();

   void press3();

   void press7();

   void press9();

   void press0();

   void release1();

   void release3();

   void release7();

   void release9();

   void pressLeft();

   void pressRight();

   void pressUp();

   void pressDown();

   void pressFire();

   void releaseLeft();

   void releaseRight();

   void releaseUp();

   void releaseDown();

   void releaseFire();

   void releaseAllEvent();

   boolean isReplaceable();
}
