package Base;

import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import MenuPck.GUIDecorator;
import MenuPck.UIComponent;
import MenuPck.UIComponentHandler;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class TrackBar implements UIComponentHandler {
   private int progress;
   private int type;
   private boolean drawText = false;
   private String printText = "";
   private String textForPrintTextProcessed = "";
   private MystText printTextProcessed = null;
   private long lastFrameTime;
   private long timeAccum = 0L;
   private UIComponent textComponent;

   public TrackBar() {
      this.textComponent = new UIComponent(this, GUIDecorator.instance, 9, 7, false);
   }

   public final void setProgress(int val) {
      synchronized(this) {
         this.progress = val;
      }
   }

   public final void setText(String printText2) {
      if (Com.fontBig != null) {
         synchronized(this) {
            this.drawText = true;
            this.printText = printText2;
         }
      }
   }

   public final void Stop() {
      MainRep.Stop();
      synchronized(this) {
         Com.mainTh.isDrawProgress = false;
      }

      MainTh mainTh2 = Com.mainTh;
      Mystery.InitFPS();
      Mystery.normalizeFPS();
      Com.mainTh.callReleasAllEvent();
      Com.mainTh.stopAllActions();
   }

   private void showProgress(boolean startProgressThread, boolean repaintRequested, int progressType, boolean unusedProgressMarker, String messageText) {
      boolean originalThreadRequest = startProgressThread;
      startProgressThread = false;
      String printText2 = messageText;
      boolean drawText2 = false;
      byte reportType = 0;
      boolean reportImmediately = false;
      repaintRequested = originalThreadRequest;
      TrackBar trackBar = this;
      Com.mainTh.callReleasAllEvent();
      this.drawText = drawText2;
      this.printText = printText2;
      this.timeAccum = 0L;
      this.progress = -1;
      this.lastFrameTime = System.currentTimeMillis();
      synchronized(this) {
         trackBar.type = 0;
         if (!Com.mainTh.isDrawProgress) {
            Com.mainTh.isDrawProgress = true;
            if (repaintRequested) {
               MainRep.Run(reportImmediately, reportType);
            }
         }

      }
   }

   public final void Set(boolean RunAddThread) {
      this.showProgress(true, false, 0, false, "");
   }

   public final void paint(GameGraphics gameGraphics2) {
      synchronized(this) {
         long lastFrameTime2;
         long lastFrameTime3 = (lastFrameTime2 = System.currentTimeMillis()) - this.lastFrameTime;
         this.lastFrameTime = lastFrameTime2;
         int maximum;
         if (this.drawText && Com.fontBig != null) {
            if (!this.textForPrintTextProcessed.equals(this.printText)) {
               this.textForPrintTextProcessed = this.printText;
               MystString mystString;
               (mystString = new MystString()).Create(this.printText, Com.fontBig);
               this.printTextProcessed = new MystText();
               this.printTextProcessed.create(mystString, getCameraScreen(), 1, Com.fontBig);
            }

            int maximum2 = Math.max((Com.fontBig.fontHeight << 1) + 1, this.printTextProcessed.getMaxHeight(Com.fontBig));
            maximum = Math.max(getCameraScreen(), this.printTextProcessed.getMaxWidth());
            this.textComponent.setPosition(Com.cameraScreen.getCw(), Com.cameraScreen.getCh(), 3, 0, 4, 0, 0, maximum + 4, 0, maximum2, true);
            this.textComponent.paint(gameGraphics2);
         }

         this.timeAccum += lastFrameTime3;
         if (this.timeAccum >= 400L || this.drawText || this.progress >= 0) {
            switch (this.type) {
               case 0:
                  GameImage gameImage = (maximum = (int)(this.timeAccum % 960L) / 120) % 2 == 0 ? ImagesGlobal.loadingSmoke0 : ImagesGlobal.loadingSmoke1;
                  maximum /= 2;
                  int textWidth = 0;
                  int imageWidth = gameImage.getWidth();
                  int imageHeight = gameImage.getHeight();
                  switch (maximum) {
                     case 0:
                        textWidth = 0;
                        imageWidth = gameImage.getWidth();
                        imageHeight = gameImage.getHeight();
                        break;
                     case 1:
                        textWidth = 5;
                        imageWidth = gameImage.getHeight();
                        imageHeight = gameImage.getWidth();
                        break;
                     case 2:
                        textWidth = 3;
                        imageWidth = gameImage.getWidth();
                        imageHeight = gameImage.getHeight();
                        break;
                     case 3:
                        textWidth = 6;
                        imageWidth = gameImage.getHeight();
                        imageHeight = gameImage.getWidth();
                  }

                  maximum = -ImagesGlobal.loadingSnake.getHeight() / 2;
                  gameGraphics2.drawRegion(gameImage, 0, 0, gameImage.getWidth(), gameImage.getHeight(), textWidth, Com.cameraScreen.getCw() / 2 - imageWidth / 2, Com.cameraScreen.getCh() / 2 - imageHeight / 2 + maximum);
                  gameGraphics2.drawImage(ImagesGlobal.loadingSnake, Com.cameraScreen.getCw() / 2 - ImagesGlobal.loadingSnake.getWidth() / 2, Com.cameraScreen.getCh() / 2 - ImagesGlobal.loadingSnake.getHeight() / 2 + maximum, 0);
                  if (this.progress >= 0 && Com.fontDigitGame != null) {
                     String t = this.progress + "%";
                     textWidth = Com.fontDigitGame.getTextWidth(t);
                     Com.fontDigitGame.writeText(gameGraphics2, t, Com.cameraScreen.getCw() / 2 - textWidth / 2, Com.cameraScreen.getCh() / 2 - Com.fontDigitGame.fontHeight / 2 + maximum);
                  }
            }
         }

      }
   }

   private static int getCameraScreen() {
      return Math.min(176, Com.cameraScreen.getCw() - 10 - 4);
   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 9:
            this.printTextProcessed.draw(gameGraphics2, 2, (uIComponent15.heightContent - this.printTextProcessed.getMaxHeight(Com.fontBig)) / 2, this.textComponent.widthContent - 4, this.textComponent.heightContent, 0, 0, Com.fontBig);
         default:
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15) {
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
   }
}
