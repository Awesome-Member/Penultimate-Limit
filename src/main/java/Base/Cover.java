package Base;

import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import MenuPck.MenuManager;
import Moduls.PFter;
import Resources.StringResources;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class Cover implements PFter {
   public GameImage imgCover;
   private GameImage[] images;
   private MystText WriteText;
   private int BackFunc;
   private int TextOff;
   private int showInd = 0;
   private long startTime;
   private boolean showing;
   private boolean waitTime;
   private static int[] c_Times = new int[]{5000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000};
   private boolean blockClick = false;
   private boolean lockReplace = false;

   public final void Set(int back) {
      this.config(back);
      MenuManager.instance.guiHandler.hideAllWindows();
      Com.mainTh.setStopPaint();
      Com.mainTh.setPFter1(this);
   }

   public final void config(int back) {
      this.blockClick = false;
      this.BackFunc = back;
      this.WriteText = new MystText();
      String[] COVER_HINT_TEXTS2 = StringResources.COVER_HINT_TEXTS;
      int pr = Mystery.RandInt(49);
      MystString mystString;
      (mystString = new MystString()).Create(StringResources.COVER_HINT_TEXTS[pr], Com.fontBig);
      this.WriteText.create(mystString, Com.cameraScreen.getCw(), 1, Com.fontBig);
      this.TextOff = this.WriteText.getRowLen().length * this.WriteText.getRowStep();
      this.startTime = Mystery.currentTimeMillis;
      this.waitTime = true;
      this.showing = true;
      if (back != 3 && back != 2) {
         if (this.showInd >= this.images.length) {
            this.startTime = 0L;
         } else {
            this.showInd = 0;
         }
      } else {
         this.showInd = this.images.length;
      }
   }

   public final void loadData() {
      try {
         if (!Com.drawCover) {
            this.imgCover = null;
         } else {
            this.imgCover = GameImage.createImage("/Data/All/Cover.png");
         }

         Vector vector3 = new Vector();

         int imageIndex;
         try {
            imageIndex = 0;

            while(true) {
               String add = "";
               GameImage gameImage = GameImage.createImage("/Data/All/logo" + imageIndex + add + ".png");
               vector3.addElement(gameImage);
               ++imageIndex;
            }
         } catch (Exception exception) {
            this.images = new GameImage[vector3.size()];

            for(imageIndex = 0; imageIndex < vector3.size(); ++imageIndex) {
               this.images[imageIndex] = (GameImage)vector3.elementAt(imageIndex);
            }

         }
      } catch (Exception exception2) {
      }
   }

   private void click() {
      if (!this.blockClick) {
         this.startTime = Mystery.currentTimeMillis;
         ++this.showInd;
         if (this.showInd > this.images.length) {
            this.showInd = this.images.length;
            this.waitTime = false;
            switch (this.BackFunc) {
               case 0:
                  for(int i = 0; i < this.images.length; ++i) {
                     this.images[i] = null;
                  }

                  this.blockClick = true;
                  Com.startMIDlet();
                  return;
               case 1:
               default:
                  break;
               case 2:
                  if (Com.afterFatalLoadClient && !Com.string3.equals("")) {
                     try {
                        Com.midlet.openBrowser(Com.string3);
                     } catch (Exception exception) {
                        exception.printStackTrace();
                     }
                  }

                  Com.midlet.quit();
                  break;
               case 3:
                  Com.midlet.quit();
                  return;
            }
         }

      }
   }

   public final void functionate() {
      IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
   }

   public final void functionateUnderDrawProgress() {
      IndirectNetworkHandler.instance.processEventsInMainThreadWithThrow();
      Com.fontSmall.animate();
      if (Com.fontBig != null) {
         Com.fontBig.animate();
      }

      if (this.waitTime && Mystery.currentTimeMillis - this.startTime >= (long)c_Times[this.showInd]) {
         this.click();
      }

   }

   public final void paint(GameGraphics gameGraphics2, boolean justPaint) {
      if (this.showInd < this.images.length) {
         gameGraphics2.setColor(0);
         gameGraphics2.fillRect(0, 0, Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
         gameGraphics2.drawImage(this.images[this.showInd], Com.cameraScreen.getCw() / 2 - this.images[this.showInd].getWidth() / 2, Com.cameraScreen.getCh() / 2 - this.images[this.showInd].getHeight() / 2, 0);
      } else {
         gameGraphics2.setColor(0);
         gameGraphics2.fillRect(0, 0, Com.cameraScreen.getCw(), Com.cameraScreen.getCh());
         if (this.imgCover != null) {
            gameGraphics2.drawImage(this.imgCover, Com.cameraScreen.getCw() / 2 - this.imgCover.getWidth() / 2, Com.cameraScreen.getCh() / 2 - this.imgCover.getHeight() / 2, 0);
         }

         this.WriteText.draw(gameGraphics2, 0, Com.cameraScreen.getCh() - this.TextOff, Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 0, 0, Com.fontBig);
         gameGraphics2.setColor(16777215);
      }
   }

   public final void onAfterHardLoad() {
      Com.showGameCursor();
   }

   public final void pressDown() {
   }

   public final void pressLeft() {
   }

   public final void pressRight() {
   }

   public final void pressUp() {
   }

   public final void pressFire() {
      this.click();
   }

   public final void releaseDown() {
   }

   public final void releaseLeft() {
   }

   public final void releaseRight() {
   }

   public final void releaseUp() {
   }

   public final void releaseFire() {
   }

   public final void wheelRot(int num) {
   }

   public final void movePointer(int x, int y) {
   }

   public final void pressPointer(int x, int y) {
      this.click();
   }

   public final void dragPointer(int x, int y) {
   }

   public final void releasePointer(int pointerX, int pointerY) {
   }

   public final void pressLeftSoft() {
   }

   public final void pressRightSoft() {
   }

   public final void pressStar() {
   }

   public final void pressSharp() {
   }

   public final void press1() {
   }

   public final void press3() {
   }

   public final void press7() {
   }

   public final void press9() {
   }

   public final void press0() {
   }

   public final void release1() {
   }

   public final void release3() {
   }

   public final void release7() {
   }

   public final void release9() {
   }

   public final void releaseLeftSoft() {
   }

   public final void releaseRightSoft() {
   }

   public final void releaseAllEvent() {
   }

   public final boolean isReplaceable() {
      return true;
   }
}
