package MenuPck;

import Base.ImagesGlobal;
import Base.Com;
import Engine.Mystery;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class GUIActivityWorldMap extends GUIBaseActivity implements UIComponentHandler {
   private boolean drawArrowUp;
   private boolean drawArrowDown;
   private boolean drawArrowLeft;
   private boolean drawArrowRight;
   private int vertOffset;
   private int horizOffset;
   private int velocityX = 0;
   private int velocityXTimeAccum = 0;
   private int velocityY = 0;
   private int velocityYTimeAccum = 0;
   private int mapX = -1;
   private int mapY = -1;
   private int[][] questsMarks = new int[0][];
   private int mapImageWidth;
   private int mapImageHeight;
   private long timeCounter = 0L;
   private boolean isBlinkPlayerPosition = false;

   public GUIActivityWorldMap(GUIHandler gUIHandler, Object context) {
      super(gUIHandler, 7, 7, 7, 7, context);
   }

   protected final void init(int mapX2, int mapY2, int[][] questsMarks2) {
      this.mapImageWidth = Com.c_world_map_image.getWidth();
      this.mapImageHeight = Com.c_world_map_image.getHeight();
      this.mapX = mapX2;
      this.mapY = mapY2;
      this.questsMarks = questsMarks2;
   }

   public final void activate() {
      super.activate();
      this.guiHandler.hideTopWindow();
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      this.drawArrowLeft = false;
      this.drawArrowRight = false;
      this.velocityX = 0;
      this.velocityY = 0;
      this.vertOffset = 0;
      this.horizOffset = 0;
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 0, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 0, (Object)null);
      }

      this.guiHandler.initWindowsParams();
      int mapCenterX = Com.c_world_map_zero_pos_x + this.mapX * Com.c_world_map_step_x;
      int mapCenterY = Com.c_world_map_zero_pos_y + this.mapY * Com.c_world_map_step_y;
      this.vertOffset = mapCenterY - this.guiHandler.botWindow.heightContent / 2;
      this.horizOffset = mapCenterX - this.guiHandler.botWindow.widthContent / 2;
      this.fixOffsets();
      this.velocityX = 0;
      this.velocityY = 0;
      this.recalcParams();
      this.guiHandler.initVisibiblity();
   }

   protected final void recalcParams() {
      this.fixOffsets();
      this.uiConteiner.clear();
      this.uiConteiner.addComponent(this.guiHandler.botWindow);
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 130, 0, false, false));
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 131, 0, false, false));
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 132, 0, false, false));
      this.uiConteiner.addComponent(new UIComponent(this, GUIHandler.menuDecorator, 133, 0, false, false));
      GUIHandler.positionateSoftButtons(true, this.uiConteiner, this, this.guiHandler.screenWidth, this.guiHandler.screenHeight, this.guiHandler.font, this.guiHandler.softSpacing);
      this.initArrows();
      this.initArrowsPositions();
      this.guiHandler.botWindow.setHandler((UIComponentHandler)this);
   }

   private void changeVertOffset(int delta, boolean resetVelocity) {
      if (this.mapImageHeight > this.guiHandler.botWindow.heightContent) {
         int vertOffset2 = this.vertOffset;
         this.vertOffset += delta;
         if (this.vertOffset < 0) {
            this.vertOffset = 0;
         }

         if (this.vertOffset > this.mapImageHeight - this.guiHandler.botWindow.heightContent) {
            this.vertOffset = this.mapImageHeight - this.guiHandler.botWindow.heightContent;
         }

         if (vertOffset2 != this.vertOffset) {
            this.guiHandler.botWindow.needRedraw = true;
            this.initArrows();
         }
      }

      if (resetVelocity) {
         this.velocityY = 0;
      }

   }

   private void changeHorizOffset(int delta, boolean resetVelocity) {
      if (this.mapImageWidth > this.guiHandler.botWindow.widthContent) {
         int horizOffset2 = this.horizOffset;
         this.horizOffset += delta;
         if (this.horizOffset < 0) {
            this.horizOffset = 0;
         }

         if (this.horizOffset > this.mapImageWidth - this.guiHandler.botWindow.widthContent) {
            this.horizOffset = this.mapImageWidth - this.guiHandler.botWindow.widthContent;
         }

         if (horizOffset2 != this.horizOffset) {
            this.guiHandler.botWindow.needRedraw = true;
            this.initArrows();
         }
      }

      if (resetVelocity) {
         this.velocityX = 0;
      }

   }

   private void initArrows() {
      this.drawArrowUp = false;
      this.drawArrowDown = false;
      this.drawArrowLeft = false;
      this.drawArrowRight = false;
      if (this.mapImageHeight > this.guiHandler.botWindow.heightContent) {
         if (this.vertOffset > 0) {
            this.drawArrowUp = true;
         }

         if (this.mapImageHeight - this.vertOffset > this.guiHandler.botWindow.heightContent) {
            this.drawArrowDown = true;
         }
      }

      if (this.mapImageWidth > this.guiHandler.botWindow.widthContent) {
         if (this.horizOffset > 0) {
            this.drawArrowLeft = true;
         }

         if (this.mapImageWidth - this.horizOffset > this.guiHandler.botWindow.widthContent) {
            this.drawArrowRight = true;
         }
      }

   }

   private void initArrowsPositions() {
      UIComponent uIComponent15;
      if ((uIComponent15 = this.uiConteiner.getComponent(130)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.botWindow.yContent - 8, 0, this.guiHandler.botWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

      if ((uIComponent15 = this.uiConteiner.getComponent(131)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 3, 0, 4, this.guiHandler.botWindow.yContent + this.guiHandler.botWindow.heightContent - (GUIHandler.arrowImage.getHeight() - 8), 0, this.guiHandler.botWindow.widthContent, 0, GUIHandler.arrowImage.getHeight());
      }

      if ((uIComponent15 = this.uiConteiner.getComponent(132)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 0, this.guiHandler.botWindow.xContent - 8, 3, this.guiHandler.botWindow.yContent + this.guiHandler.botWindow.heightContent / 2 - this.guiHandler.screenHeight / 2, 0, GUIHandler.arrowImage.getHeight(), 0, this.guiHandler.botWindow.heightContent);
      }

      if ((uIComponent15 = this.uiConteiner.getComponent(133)) != null) {
         uIComponent15.setPosition(this.guiHandler.screenWidth, this.guiHandler.screenHeight, 0, this.guiHandler.botWindow.xContent + this.guiHandler.botWindow.widthContent - (GUIHandler.arrowImage.getHeight() - 8), 3, this.guiHandler.botWindow.yContent + this.guiHandler.botWindow.heightContent / 2 - this.guiHandler.screenHeight / 2, 0, GUIHandler.arrowImage.getHeight(), 0, this.guiHandler.botWindow.heightContent);
      }

   }

   private void fixOffsets() {
      if (this.guiHandler.botWindow.heightContent >= this.mapImageHeight) {
         this.vertOffset = -(this.guiHandler.botWindow.heightContent - this.mapImageHeight) / 2;
      } else {
         if (this.vertOffset < 0) {
            this.vertOffset = 0;
         }

         if (this.mapImageHeight - this.vertOffset < this.guiHandler.botWindow.heightContent) {
            this.vertOffset = this.mapImageHeight - this.guiHandler.botWindow.heightContent;
         }
      }

      if (this.guiHandler.botWindow.widthContent >= this.mapImageWidth) {
         this.horizOffset = -(this.guiHandler.botWindow.widthContent - this.mapImageWidth) / 2;
      } else {
         if (this.horizOffset < 0) {
            this.horizOffset = 0;
         }

         if (this.mapImageWidth - this.horizOffset < this.guiHandler.botWindow.widthContent) {
            this.horizOffset = this.mapImageWidth - this.guiHandler.botWindow.widthContent;
         }

      }
   }

   public final void setScreenSize(int screenWidth, int screenHeight) {
      super.setScreenSize(screenWidth, screenHeight);
      this.recalcParams();
   }

   public final int getPreferContentHeight(int availHeight) {
      return Math.min((availHeight << 1) / 3, this.mapImageHeight);
   }

   public final int getPreferContentWidth() {
      return Math.min(this.guiHandler.screenWidth - (GUIDecorator.CLAW_CORNER_OFF_X << 1), this.mapImageWidth);
   }

   public final void functional(boolean wasFontAnimation) {
      super.functional(wasFontAnimation);
      this.timeCounter += Mystery.FrameTimeI;
      wasFontAnimation = this.timeCounter % 1200L > 700L;
      if (this.isBlinkPlayerPosition != wasFontAnimation) {
         this.isBlinkPlayerPosition = wasFontAnimation;
         this.guiHandler.botWindow.needRedraw = true;
      }

      int count;
      if (this.velocityX != 0) {
         this.velocityXTimeAccum = (int)((long)this.velocityXTimeAccum + Mystery.FrameTimeI * 100L);
         if (this.velocityXTimeAccum >= this.velocityX) {
            count = this.velocityXTimeAccum / this.velocityX;
            this.velocityXTimeAccum %= this.velocityX;
            this.changeHorizOffset(count, false);
            this.guiHandler.botWindow.needRedraw = true;
         }

         this.velocityX = (int)((long)this.velocityX + (long)this.velocityX * 200L * Mystery.FrameTimeI / 100000L);
         if (Math.abs(this.velocityX) >= 5000) {
            this.velocityX = 0;
         }
      }

      if (this.velocityY != 0) {
         this.velocityYTimeAccum = (int)((long)this.velocityYTimeAccum + Mystery.FrameTimeI * 100L);
         if (this.velocityYTimeAccum >= this.velocityY) {
            count = this.velocityYTimeAccum / this.velocityY;
            this.velocityYTimeAccum %= this.velocityY;
            this.changeVertOffset(count, false);
            this.guiHandler.botWindow.needRedraw = true;
         }

         this.velocityY = (int)((long)this.velocityY + (long)this.velocityY * 200L * Mystery.FrameTimeI / 100000L);
         if (Math.abs(this.velocityY) >= 5000) {
            this.velocityY = 0;
         }
      }

   }

   public final void realeasPointerAll() {
      super.realeasPointerAll();
   }

   public final void clickLeftSoft() {
      super.clickLeftSoft();
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 1, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 1, (Object)null);
      }

   }

   public final void clickRightSoft(boolean byBackgroundClick) {
      super.clickRightSoft(byBackgroundClick);
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 1, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 1, (Object)null);
      }

   }

   public final void vertHoldAction(int delta) {
      super.vertHoldAction(delta);
      this.changeVertOffset(delta, true);
   }

   public final void horizHoldAction(int delta) {
      super.horizHoldAction(delta);
      this.changeHorizOffset(delta, true);
   }

   public final void pressFire() {
      super.pressFire();
      if (this.guiHandler.currentEventListener != null) {
         this.guiHandler.addActionEvent(this.guiHandler.currentEventListener, 1, (Object)null);
      }

      if (this.guiHandler.commonEvents != null) {
         this.guiHandler.addActionEvent(this.guiHandler.commonEvents, 1, (Object)null);
      }

   }

   public final void paintUIComponentOver(UIComponent uIComponent15, GameGraphics gameGraphics2) {
   }

   public final void paintUIComponent(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.id) {
         case 100:
            int horizOffset2 = -this.horizOffset;
            int vertOffset2 = -this.vertOffset;
            gameGraphics2.drawImage(Com.c_world_map_image, horizOffset2, vertOffset2, 0);
            if (!this.isBlinkPlayerPosition) {
               int playerMarkerX = horizOffset2 + Com.c_world_map_zero_pos_x + this.mapX * Com.c_world_map_step_x;
               int playerMarkerY = vertOffset2 + Com.c_world_map_zero_pos_y + this.mapY * Com.c_world_map_step_y;
               GameImage worldMapPlayerPosition2 = ImagesGlobal.worldMapPlayerPosition;
               gameGraphics2.drawImage(worldMapPlayerPosition2, playerMarkerX - worldMapPlayerPosition2.getWidth() / 2, playerMarkerY - ImagesGlobal.worldMapPlayerPosition.getHeight() / 2, 0);

               for(int questsMarkIndex = 0; questsMarkIndex < this.questsMarks.length; ++questsMarkIndex) {
                  int[] el = this.questsMarks[questsMarkIndex];
                  playerMarkerX = horizOffset2 + Com.c_world_map_zero_pos_x + el[0] * Com.c_world_map_step_x;
                  playerMarkerY = vertOffset2 + Com.c_world_map_zero_pos_y + el[1] * Com.c_world_map_step_y;
                  worldMapPlayerPosition2 = ImagesGlobal.worldMapQuest;
                  gameGraphics2.drawImage(worldMapPlayerPosition2, playerMarkerX - worldMapPlayerPosition2.getWidth() / 2, playerMarkerY - ImagesGlobal.worldMapQuest.getHeight() / 2, 0);
               }

               return;
            }
            break;
         case 130:
            if (this.drawArrowUp) {
               gameGraphics2.drawImage(GUIHandler.arrowImage, uIComponent15.widthContent / 2 - GUIHandler.arrowImage.getWidth() / 2, 0, 0);
               return;
            }
            break;
         case 131:
            if (this.drawArrowDown) {
               gameGraphics2.drawRegion(GUIHandler.arrowImage, 0, 0, GUIHandler.arrowImage.getWidth(), GUIHandler.arrowImage.getHeight(), 1, uIComponent15.widthContent / 2 - GUIHandler.arrowImage.getWidth() / 2, 0);
               return;
            }
            break;
         case 132:
            if (this.drawArrowLeft) {
               gameGraphics2.drawRegion(GUIHandler.arrowImage, 0, 0, GUIHandler.arrowImage.getWidth(), GUIHandler.arrowImage.getHeight(), 6, 0, uIComponent15.heightContent / 2 - GUIHandler.arrowImage.getWidth() / 2);
               return;
            }
            break;
         case 133:
            if (this.drawArrowRight) {
               gameGraphics2.drawRegion(GUIHandler.arrowImage, 0, 0, GUIHandler.arrowImage.getWidth(), GUIHandler.arrowImage.getHeight(), 5, 0, uIComponent15.heightContent / 2 - GUIHandler.arrowImage.getWidth() / 2);
               return;
            }
            break;
         default:
            GUIHandler.paintSoftButtons(uIComponent15, gameGraphics2, "", "", this.guiHandler.currentEventListener.getSoftPrefix() + "ʒʓOK", this.guiHandler.font, this.guiHandler.softSpacing);
      }

   }

   public final void clickUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 12:
            this.clickLeftSoft();
         default:
      }
   }

   public final void clickUIComponent(UIComponent uIComponent15, int x, int y) {
   }

   public final void kineticDragUIComponent(UIComponent uIComponent15, int dx, int dy) {
   }

   public final void dragUIComponent(UIComponent uIComponent15, int dx, int dy, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            if (dx != 0) {
               this.changeHorizOffset(-dx, true);
            }

            if (dy != 0) {
               this.changeVertOffset(-dy, true);
            }
         default:
      }
   }

   public final void pressUIComponent(UIComponent uIComponent15, int x, int y) {
      switch (uIComponent15.id) {
         case 100:
            this.velocityX = 0;
            this.velocityY = 0;
            return;
         case 130:
            if (this.drawArrowUp) {
               this.pressUp();
               return;
            }
            break;
         case 131:
            if (this.drawArrowDown) {
               this.pressDown();
               return;
            }
            break;
         case 132:
            if (this.drawArrowLeft) {
               this.pressLeft();
               return;
            }
            break;
         case 133:
            if (this.drawArrowRight) {
               this.pressRight();
            }
      }

   }

   public final void releasPressedUIComponent(UIComponent uIComponent15) {
   }

   public final void releasPressedUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 130:
            this.releasUpAll();
            return;
         case 131:
            this.releasDownAll();
            return;
         case 132:
            this.releasLeftAll();
            return;
         case 133:
            this.releasRightAll();
         default:
      }
   }

   public final void flingUIComponent(UIComponent uIComponent15, int velX, int velY) {
      switch (uIComponent15.id) {
         case 100:
            if (velX != 0) {
               this.velocityX = -velX;
               this.velocityXTimeAccum = 0;
            } else {
               this.velocityX = 0;
               this.velocityXTimeAccum = 0;
            }

            if (velY != 0) {
               this.velocityY = -velY;
               this.velocityYTimeAccum = 0;
               return;
            } else {
               this.velocityY = 0;
               this.velocityYTimeAccum = 0;
            }
         default:
      }
   }

   public final void animateYUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationYUIComponent(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 100:
            this.guiHandler.initWindowsParams();
            this.initArrowsPositions();
         default:
      }
   }

   public final void animateWidthUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            if (delta != 0) {
               this.initArrowsPositions();
            }
         default:
      }
   }

   public final void clickUIComponent2(UIComponent uIComponent15) {
      switch (uIComponent15.id) {
         case 100:
            this.initArrowsPositions();
         default:
      }
   }

   public final void finishAnimationXUIComponent(UIComponent uIComponent15) {
   }

   public final void animateHeightUIComponent(UIComponent uIComponent15, int delta) {
      switch (uIComponent15.id) {
         case 100:
            if (delta != 0) {
               this.initArrowsPositions();
            }
         default:
      }
   }

   public final void overUIComponent(UIComponent uIComponent15, int x, int y, boolean first) {
   }
}
