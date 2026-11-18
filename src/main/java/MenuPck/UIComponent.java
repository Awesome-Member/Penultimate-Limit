package MenuPck;

import Engine.Mystery;
import desktop.PlatformRuntime;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class UIComponent {
   public int id;
   public int decorType;
   public int overBackGroundColor = -1;
   public int state = 0;
   public int xByPosition;
   public int yByPosition;
   public int heightByPosition;
   private int widthByPosition;
   public int heightContentByPosition;
   public int widthContentByPosition;
   public int x;
   public int y;
   public int width;
   public int height;
   public int xContent;
   public int yContent;
   public int widthContent;
   public int heightContent;
   public int paddingLeft;
   public int paddingRight;
   public int paddingTop;
   public int paddingBottom;
   public AnimateProperty animateX = new AnimateProperty();
   public AnimateProperty animateY = new AnimateProperty();
   private AnimateProperty animateHeight = new AnimateProperty();
   private AnimateProperty animateWidth = new AnimateProperty();
   private boolean useBuffer = false;
   private GameImage bufferImage = null;
   private GameGraphics bufferGraphics = null;
   public boolean needRedraw = false;
   public boolean ignoreInTouch = false;
   public UIComponentHandler animationSecondHandler;
   public UIComponentHandler handler;
   private UIComponentDecorator decorator;
   public boolean visible = true;
   public boolean enabled = true;
   public boolean translateTouch = false;
   private boolean useClip = true;
   private boolean wasSetPosition = false;
   private int lspScreenWidth;
   private int lspScreenHeight;
   private int lspHorizAlign;
   private int lspHorizOffset;
   private int lspVertAlign;
   private int lspVertOffset;
   private int lspWidthDimensionType;
   private int lspNeedWidth;
   private int lspHeightDimensionType;
   private int lspNeedHeight;
   private boolean lspFixX;
   private boolean lspFixY;
   private long lspAnimateHeightTime;
   private int flingVelocityX = 0;
   private int flingVelocityXTimeAccum = 0;
   private int flingVelocityY = 0;
   private int flingVelocityYTimeAccum = 0;

   public UIComponent(UIComponentHandler uIComponentHandler, UIComponentDecorator uIComponentDecorator, int id2, int decorType2, boolean useBuffer2) {
      this.handler = uIComponentHandler;
      this.decorator = uIComponentDecorator;
      this.id = id2;
      this.decorType = decorType2;
      this.useBuffer = useBuffer2;
   }

   public UIComponent(UIComponentHandler uIComponentHandler, UIComponentDecorator uIComponentDecorator, int id2, int decorType2, boolean useBuffer2, boolean useClip2) {
      this.handler = uIComponentHandler;
      this.decorator = uIComponentDecorator;
      this.id = id2;
      this.decorType = decorType2;
      this.useBuffer = false;
      this.useClip = false;
   }

   public UIComponent(UIComponentHandler uIComponentHandler, UIComponentDecorator uIComponentDecorator, int id2, int decorType2, boolean useBuffer2, boolean useClip2, boolean translateTouch2) {
      this.handler = uIComponentHandler;
      this.decorator = uIComponentDecorator;
      this.id = id2;
      this.decorType = decorType2;
      this.useBuffer = false;
      this.useClip = false;
      this.translateTouch = translateTouch2;
   }

   public UIComponent(UIComponentHandler uIComponentHandler, UIComponentDecorator uIComponentDecorator, int id2, int decorType2, boolean useBuffer2, int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int width2, int heightDimensionType, int height2) {
      this.handler = uIComponentHandler;
      this.decorator = uIComponentDecorator;
      this.id = id2;
      this.decorType = decorType2;
      this.useBuffer = false;
      this.setPosition(screenWidth, screenHeight, horizAlign, 0, 5, 0, 1, width2, 0, height2);
   }

   public UIComponent(UIComponentHandler uIComponentHandler, UIComponentDecorator uIComponentDecorator, int id2, int decorType2, boolean useBuffer2, boolean useClip2, int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int width2, int heightDimensionType, int height2) {
      this.handler = uIComponentHandler;
      this.decorator = uIComponentDecorator;
      this.id = 99;
      this.decorType = 0;
      this.useBuffer = false;
      this.useClip = false;
      this.setPosition(screenWidth, screenHeight, 0, 0, 4, 0, 0, width2, 0, height2);
   }

   public final void setHandler(UIComponentHandler uIComponentHandler) {
      this.handler = uIComponentHandler;
      this.needRedraw = true;
   }

   public final void setDecorType(int decorType2) {
      if (this.decorType != decorType2) {
         this.decorType = decorType2;
         this.needRedraw = true;
      }
   }

   public final void setUseBuffer(boolean useBuffer2) {
      this.useBuffer = useBuffer2;
      if (useBuffer2 && PlatformRuntime.getInstance().getPlatformType() == 1) {
         if (this.widthContent > 0 && this.heightContent > 0) {
            this.initBufferGraphics();
         }

         if (this.bufferGraphics != null) {
            this.decorator.fillBackground(this, this.bufferGraphics);
         }

         this.needRedraw = true;
      } else {
         this.bufferGraphics = null;
         this.bufferImage = null;
      }
   }

   public final UIComponent setPosition(int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int needWidth, int heightDimensionType, int needHeight, boolean checkSet) {
      return this.setPosition(screenWidth, screenHeight, horizAlign, horizOffset, vertAlign, vertOffset, widthDimensionType, needWidth, 0, needHeight, false, true, 0L, 0L, true);
   }

   public final UIComponent setPosition(int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int needWidth, int heightDimensionType, int needHeight) {
      return this.setPosition(screenWidth, screenHeight, horizAlign, horizOffset, vertAlign, vertOffset, widthDimensionType, needWidth, heightDimensionType, needHeight, false, true, 0L, 0L, false);
   }

   public final UIComponent setPosition(int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int needWidth, int heightDimensionType, int needHeight, boolean fixXY, boolean clearAnimation, long animateWidthTime, long animateHeightTime) {
      return this.setPosition(screenWidth, screenHeight, 3, 0, 4, 0, 0, needWidth, 0, needHeight, fixXY, clearAnimation, 0L, animateHeightTime, false);
   }

   private UIComponent setPosition(int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int needWidth, int heightDimensionType, int needHeight, boolean fixXY, boolean clearAnimation, long animateWidthTime, long animateHeightTime, boolean checkSet) {
      return this.setPosition(screenWidth, screenHeight, horizAlign, horizOffset, vertAlign, vertOffset, widthDimensionType, needWidth, heightDimensionType, needHeight, fixXY, clearAnimation, fixXY, clearAnimation, animateWidthTime, animateHeightTime, checkSet);
   }

   public final UIComponent setPosition(int screenWidth, int screenHeight, int horizAlign, int horizOffset, int vertAlign, int vertOffset, int widthDimensionType, int needWidth, int heightDimensionType, int needHeight, boolean fixX, boolean clearAnimationX, boolean fixY, boolean clearAnimationY, long animateWidthTime, long animateHeightTime, boolean checkSet) {
      if (this.wasSetPosition && checkSet && this.lspScreenWidth == screenWidth && this.lspScreenHeight == screenHeight && this.lspHorizAlign == horizAlign && this.lspHorizOffset == horizOffset && this.lspVertAlign == vertAlign && this.lspVertOffset == vertOffset && this.lspWidthDimensionType == widthDimensionType && this.lspNeedWidth == needWidth && this.lspHeightDimensionType == heightDimensionType && this.lspNeedHeight == needHeight && this.lspFixX == fixX && this.lspFixY == fixY && this.lspAnimateHeightTime == animateHeightTime) {
         return this;
      } else {
         this.wasSetPosition = true;
         this.lspScreenWidth = screenWidth;
         this.lspScreenHeight = screenHeight;
         this.lspHorizAlign = horizAlign;
         this.lspHorizOffset = horizOffset;
         this.lspVertAlign = vertAlign;
         this.lspVertOffset = vertOffset;
         this.lspWidthDimensionType = widthDimensionType;
         this.lspNeedWidth = needWidth;
         this.lspHeightDimensionType = heightDimensionType;
         this.lspNeedHeight = needHeight;
         this.lspFixX = fixX;
         this.lspFixY = fixY;
         this.lspAnimateHeightTime = animateHeightTime;
         needWidth = Math.max(1, needWidth);
         needHeight = Math.max(1, needHeight);
         this.decorator.initPaddings(this);
         widthDimensionType = widthDimensionType == 1 ? needWidth : needWidth + this.paddingLeft + this.paddingRight;
         needWidth = heightDimensionType == 1 ? needHeight : needHeight + this.paddingTop + this.paddingBottom;
         this.widthContentByPosition = widthDimensionType - (this.paddingLeft + this.paddingRight);
         this.heightContentByPosition = needWidth - (this.paddingTop + this.paddingBottom);
         this.heightByPosition = needWidth;
         this.widthByPosition = widthDimensionType;
         if (animateWidthTime > 0L) {
            if (this.animateWidth.needValue != widthDimensionType || this.animateWidth.value != this.width) {
               this.animateWidth.initAnimation(this.width, widthDimensionType, animateWidthTime);
            }
         } else {
            this.width = widthDimensionType;
            this.animateWidth.clearAnimation(widthDimensionType);
         }

         if (animateHeightTime > 0L) {
            if (this.animateHeight.needValue != needWidth || this.animateHeight.value != this.height) {
               this.animateHeight.initAnimation(this.height, needWidth, animateHeightTime);
            }
         } else {
            this.height = needWidth;
            this.animateHeight.clearAnimation(needWidth);
         }

         heightDimensionType = this.height;
         needHeight = this.width;
         this.decorator.correctPaddingsFinal(this);
         if (heightDimensionType != this.height) {
            needWidth = this.height;
            this.animateHeight.clearAnimation(this.height);
         }

         if (needHeight != this.width) {
            widthDimensionType = this.width;
            this.animateWidth.clearAnimation(this.width);
         }

         switch (horizAlign) {
            case 0:
               this.xByPosition = horizOffset;
            case 1:
            case 4:
            case 5:
            default:
               break;
            case 2:
               this.xByPosition = screenWidth - widthDimensionType - horizOffset;
               break;
            case 3:
               this.xByPosition = (screenWidth - widthDimensionType) / 2 + horizOffset;
               break;
            case 6:
               this.xByPosition = horizOffset - this.paddingLeft;
               break;
            case 7:
               this.xByPosition = screenWidth - widthDimensionType - horizOffset + this.paddingRight;
         }

         switch (vertAlign) {
            case 3:
               this.yByPosition = (screenHeight - needWidth) / 2 + vertOffset;
               break;
            case 4:
               this.yByPosition = vertOffset;
               break;
            case 5:
               this.yByPosition = screenHeight - needWidth - vertOffset;
            case 6:
            case 7:
            default:
               break;
            case 8:
               this.yByPosition = vertOffset - this.paddingTop;
               break;
            case 9:
               this.yByPosition = screenHeight - needWidth - vertOffset + this.paddingBottom;
               break;
            case 10:
               this.yByPosition = vertOffset - needWidth;
               break;
            case 11:
               this.yByPosition = vertOffset - needWidth / 2;
         }

         if (!fixX) {
            this.x = this.xByPosition;
         }

         if (!fixY) {
            this.y = this.yByPosition;
         }

         if (clearAnimationX) {
            this.animateX.clearAnimation(this.x);
         }

         if (clearAnimationY) {
            this.animateY.clearAnimation(this.y);
         }

         this.xContent = this.x + this.paddingLeft;
         this.yContent = this.y + this.paddingTop;
         this.widthContent = this.width - this.paddingLeft - this.paddingRight;
         this.heightContent = this.height - this.paddingTop - this.paddingBottom;
         if (this.useBuffer && PlatformRuntime.getInstance().getPlatformType() == 1) {
            this.initBufferGraphics();
            if (this.bufferGraphics != null) {
               this.decorator.fillBackground(this, this.bufferGraphics);
            }

            this.needRedraw = true;
         }

         return this;
      }
   }

   private void initBufferGraphics() {
      if (this.widthContent > 0 && this.heightContent > 0 && (this.bufferImage == null || this.bufferImage.getWidth() < this.widthContent || this.bufferImage.getHeight() < this.heightContent)) {
         int maximum = Math.max(this.widthContent, this.bufferImage == null ? 0 : this.bufferImage.getWidth());
         int maximum2 = Math.max(this.heightContent, this.bufferImage == null ? 0 : this.bufferImage.getHeight());
         this.bufferImage = GameImage.createImage(maximum, maximum2);
         this.bufferGraphics = this.bufferImage.getGraphics();
      }

   }

   private void updateBuffer() {
      if (this.useBuffer && PlatformRuntime.getInstance().getPlatformType() == 1) {
         if (this.widthContent > 0 && this.heightContent > 0) {
            this.initBufferGraphics();
         }

         if (this.bufferGraphics != null) {
            this.decorator.fillBackground(this, this.bufferGraphics);
         }

         this.needRedraw = true;
      }

   }

   public static void drawBordersInner(GameGraphics gameGraphics2, int x2, int y2, int width2, int height2, GameImage gameImage, GameImage gameImage2, int cornerSizeX, int cornerSizeY, int cornerOffX, int cornerOffY, int borderSizeX, int borderSizeY) {
      drawBordersInner(gameGraphics2, x2, y2, width2, height2, gameImage, gameImage2, cornerSizeX, cornerSizeY, cornerOffX, cornerOffY, borderSizeX, borderSizeY, true, true, true, true);
   }

   private static void drawBordersInner(GameGraphics gameGraphics2, int x2, int y2, int width2, int height2, GameImage gameImage, GameImage gameImage2, int cornerSizeX, int cornerSizeY, int cornerOffX, int cornerOffY, int borderSizeX, int borderSizeY, boolean left, boolean right, boolean top, boolean bot) {
      drawBordersInner(gameGraphics2, x2, y2, width2, height2, gameImage, gameImage2, cornerSizeX, cornerSizeY, cornerOffX, cornerOffY, borderSizeX, borderSizeY, true, true, true, true, -1);
   }

   public static void drawBordersInner(GameGraphics gameGraphics2, int x2, int y2, int width2, int height2, GameImage gameImage, GameImage gameImage2, int cornerSizeX, int cornerSizeY, int cornerOffX, int cornerOffY, int borderSizeX, int borderSizeY, boolean left, boolean right, boolean top, boolean bot, int backgroundColor) {
      drawBorders(gameGraphics2, x2 + (left ? Math.max(cornerOffX, borderSizeX) : 0), y2 + (top ? Math.max(cornerOffY, borderSizeX) : 0), Math.max(0, width2 - (right ? Math.max(cornerOffX, borderSizeX) : 0) - (left ? Math.max(cornerOffX, borderSizeX) : 0)), Math.max(0, height2 - (bot ? Math.max(cornerOffY, borderSizeX) : 0) - (top ? Math.max(cornerOffY, borderSizeX) : 0)), gameImage, gameImage, gameImage2, cornerSizeX, cornerSizeX, cornerSizeY, cornerSizeY, cornerOffX, cornerOffY, cornerOffY, borderSizeX, borderSizeY, left, right, top, bot, backgroundColor);
   }

   public static void drawBorders(GameGraphics g, int x2, int y2, int width2, int height2, GameImage cornerImage, GameImage borderImage, int cornerSizeX, int cornerSizeY, int cornerOffX, int cornerOffY, int borderSizeX, int borderSizeY) {
      drawBorders(g, x2, y2, width2, height2, cornerImage, borderImage, cornerSizeX, cornerSizeY, cornerOffX, cornerOffY, borderSizeX, borderSizeY, true, true, true, true);
   }

   public static void drawBorders(GameGraphics g, int x2, int y2, int width2, int height2, GameImage cornerImage, GameImage borderImage, int cornerSizeX, int cornerSizeY, int cornerOffX, int cornerOffY, int borderSizeX, int borderSizeY, boolean left, boolean right, boolean top, boolean bot) {
      drawBorders(g, x2, y2, width2, height2, cornerImage, cornerImage, borderImage, cornerSizeX, cornerSizeX, cornerSizeY, cornerSizeY, cornerOffX, cornerOffY, cornerOffY, borderSizeX, borderSizeY, true, true, true, bot, -1);
   }

   public static void drawBorders(GameGraphics g, int x2, int y2, int width2, int height2, GameImage cornerImageTop, GameImage cornerImageBot, GameImage borderImage, int cornerTopSizeX, int cornerBotSizeX, int cornerTopSizeY, int cornerBotSizeY, int cornerOffX, int cornerTopOffY, int cornerBotOffY, int borderSizeX, int borderSizeY, boolean left, boolean right, boolean top, boolean bot, int backgroundColor) {
      if (backgroundColor >= 0) {
         g.setColor(backgroundColor);
         g.fillRect(x2, y2, width2, height2);
      }

      backgroundColor = height2 / borderSizeY;
      int borderY = y2;

      int borderSegmentIndex;
      for(borderSegmentIndex = 0; borderSegmentIndex < backgroundColor; borderY += borderSizeY) {
         if (left) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSizeY, 0, x2 - borderSizeX, borderY);
         }

         if (right) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSizeY, 2, x2 + width2, borderY);
         }

         ++borderSegmentIndex;
      }

      if ((borderSegmentIndex = height2 - backgroundColor * borderSizeY) > 0) {
         if (left) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSegmentIndex, 0, x2 - borderSizeX, borderY);
         }

         if (right) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSegmentIndex, 2, x2 + width2, borderY);
         }
      }

      backgroundColor = width2 / borderSizeY;
      borderY = x2;

      for(borderSegmentIndex = 0; borderSegmentIndex < backgroundColor; borderY += borderSizeY) {
         if (top) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSizeY, 5, borderY, y2 - borderSizeX);
         }

         if (bot) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSizeY, 6, borderY, y2 + height2);
         }

         ++borderSegmentIndex;
      }

      if ((borderSegmentIndex = width2 - backgroundColor * borderSizeY) > 0) {
         if (top) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSegmentIndex, 5, borderY, y2 - borderSizeX);
         }

         if (bot) {
            g.drawRegion(borderImage, 0, 0, borderSizeX, borderSegmentIndex, 6, borderY, y2 + height2);
         }
      }

      if (left && top) {
         g.drawRegion(cornerImageTop, 0, 0, cornerTopSizeX, cornerTopSizeY, 0, x2 - cornerOffX, y2 - cornerTopOffY);
      }

      if (right && top) {
         g.drawRegion(cornerImageTop, 0, 0, cornerTopSizeX, cornerTopSizeY, 2, x2 + width2 - (cornerTopSizeX - cornerOffX), y2 - cornerTopOffY);
      }

      if (right && bot) {
         g.drawRegion(cornerImageBot, 0, 0, cornerBotSizeX, cornerBotSizeY, 3, x2 + width2 - (cornerBotSizeX - cornerOffX), y2 + height2 - (cornerBotSizeY - cornerBotOffY));
      }

      if (left && bot) {
         g.drawRegion(cornerImageBot, 0, 0, cornerBotSizeX, cornerBotSizeY, 1, x2 - cornerOffX, y2 + height2 - (cornerBotSizeY - cornerBotOffY));
      }

   }

   public final void paint(GameGraphics gameGraphics2) {
      this.decorator.draw(this, gameGraphics2);
      if (this.handler != null && (this.needRedraw || !this.useBuffer || PlatformRuntime.getInstance().getPlatformType() == 2)) {
         if (this.bufferGraphics == null) {
            int clipX = 0;
            int clipY = 0;
            int clipWidth = 0;
            int clipHeight = 0;
            if (this.useClip) {
               clipX = gameGraphics2.getClipX();
               clipY = gameGraphics2.getClipY();
               clipWidth = gameGraphics2.getClipWidth();
               clipHeight = gameGraphics2.getClipHeight();
            }

            int translateX = gameGraphics2.getTranslateX();
            int translateY = gameGraphics2.getTranslateY();
            if (this.useClip) {
               gameGraphics2.translate(-gameGraphics2.getTranslateX(), -gameGraphics2.getTranslateY());
               gameGraphics2.setClip(this.x + this.paddingLeft, this.y + this.paddingTop, this.width - this.paddingLeft - this.paddingRight, this.height - this.paddingTop - this.paddingBottom);
            }

            gameGraphics2.translate(this.x + this.paddingLeft - gameGraphics2.getTranslateX(), this.y + this.paddingTop - gameGraphics2.getTranslateY());
            this.decorator.fillBackground(this, gameGraphics2);
            this.handler.paintUIComponent(this, gameGraphics2);
            gameGraphics2.translate(translateX - gameGraphics2.getTranslateX(), translateY - gameGraphics2.getTranslateY());
            if (this.useClip) {
               gameGraphics2.setClip(clipX, clipY, clipWidth, clipHeight);
            }
         } else {
            this.needRedraw = false;
            if (this.bufferGraphics.getClipWidth() != this.widthContent || this.bufferGraphics.getClipHeight() != this.heightContent) {
               this.bufferGraphics.setClip(0, 0, this.widthContent, this.heightContent);
            }

            this.decorator.fillBackground(this, this.bufferGraphics);
            this.handler.paintUIComponent(this, this.bufferGraphics);
         }
      }

      if (this.bufferGraphics != null) {
         gameGraphics2.drawRegion(this.bufferImage, 0, 0, this.widthContent, this.heightContent, 0, this.x + this.paddingLeft, this.y + this.paddingTop);
      }

      if (this.handler != null) {
         this.handler.paintUIComponentOver(this, gameGraphics2);
      }

      this.decorator.paintBordersPhase2(this, gameGraphics2);
   }

   public final boolean isContentClick(int cx, int cy) {
      return cx >= this.xContent && cx < this.xContent + this.widthContent && cy >= this.yContent && cy < this.yContent + this.heightContent;
   }

   public final void stratKineticDrag(int velX, int velY) {
      this.flingVelocityX = velX;
      this.flingVelocityXTimeAccum = 0;
      this.flingVelocityY = velY;
      this.flingVelocityYTimeAccum = 0;
   }

   public final void stopKineticDrag() {
      this.flingVelocityX = 0;
      this.flingVelocityY = 0;
   }

   public final void updateAnimationsAndKineticDrag() {
      if (this.isActiveAnimationY()) {
         this.animateY.animateFixTime(false);
         this.checkAnimationY();
      }

      if (this.isActiveAnimationX()) {
         this.animateX.animateFixTime(false);
         this.x = this.animateX.value;
         this.xContent = this.x + this.paddingLeft;
         if (this.animationSecondHandler != null) {
            this.animationSecondHandler.clickUIComponent2(this);
         }

         if (this.handler != null) {
            this.handler.clickUIComponent2(this);
         }

         if (this.animateX.value == this.animateX.needValue) {
            this.notifyAnimationXFinished();
         }
      }

      int previousWidth;
      if (this.animateWidth.needValue != this.width) {
         this.animateWidth.animateFixTime(false);
         previousWidth = this.width;
         this.width = this.animateWidth.value;
         this.widthContent = this.width - this.paddingLeft - this.paddingRight;
         if (this.width != previousWidth) {
            this.updateBuffer();
         }

         if (this.animationSecondHandler != null) {
            this.animationSecondHandler.animateWidthUIComponent(this, this.width - previousWidth);
         }

         if (this.handler != null) {
            this.handler.animateWidthUIComponent(this, this.width - previousWidth);
         }
      }

      if (this.animateHeight.needValue != this.height) {
         this.animateHeight.animateFixTime(false);
         previousWidth = this.height;
         this.height = this.animateHeight.value;
         this.heightContent = this.height - this.paddingTop - this.paddingBottom;
         if (this.height != previousWidth) {
            this.updateBuffer();
         }

         if (this.animationSecondHandler != null) {
            this.animationSecondHandler.animateHeightUIComponent(this, this.height - previousWidth);
         }

         if (this.handler != null) {
            this.handler.animateHeightUIComponent(this, this.height - previousWidth);
         }
      }

      int widthChange = 0;
      if (this.flingVelocityX != 0) {
         this.flingVelocityXTimeAccum = (int)((long)this.flingVelocityXTimeAccum + Mystery.FrameTimeI * 100L);
         if (this.flingVelocityXTimeAccum >= this.flingVelocityX) {
            previousWidth = this.flingVelocityXTimeAccum / this.flingVelocityX;
            this.flingVelocityXTimeAccum %= this.flingVelocityX;
            widthChange = previousWidth;
         }

         this.flingVelocityX = (int)((long)this.flingVelocityX + (long)this.flingVelocityX * 400L * Mystery.FrameTimeI / 100000L);
         if (Math.abs(this.flingVelocityX) >= 5000) {
            this.flingVelocityX = 0;
         }
      }

      previousWidth = 0;
      if (this.flingVelocityY != 0) {
         this.flingVelocityYTimeAccum = (int)((long)this.flingVelocityYTimeAccum + Mystery.FrameTimeI * 100L);
         if (this.flingVelocityYTimeAccum >= this.flingVelocityY) {
            previousWidth = this.flingVelocityYTimeAccum / this.flingVelocityY;
            this.flingVelocityYTimeAccum %= this.flingVelocityY;
            previousWidth = previousWidth;
         }

         this.flingVelocityY = (int)((long)this.flingVelocityY + (long)this.flingVelocityY * 400L * Mystery.FrameTimeI / 100000L);
         if (Math.abs(this.flingVelocityY) >= 5000) {
            this.flingVelocityY = 0;
         }
      }

      if ((widthChange != 0 || previousWidth != 0) && this.handler != null) {
         this.handler.kineticDragUIComponent(this, widthChange, previousWidth);
      }

   }

   public final boolean isActiveAnimationY() {
      return this.animateY.needValue != this.y;
   }

   public final void startAnimationY(int fromY, int toY, long time) {
      this.startAnimationY(fromY, toY, time, 0L);
   }

   private void startAnimationY(int fromY, int toY, long time, long forceProgress) {
      fromY = (int)(0L * (long)(toY - fromY) / 10000L + (long)fromY);
      if ((time = 10000L * time / 10000L) <= 0L) {
         time = 1L;
      }

      if (fromY == toY) {
         this.notifyAnimationYFinished();
      } else {
         this.y = fromY;
         this.yContent = this.y + this.paddingTop;
         this.animateY.initAnimation(fromY, toY, time);
         if (this.animationSecondHandler != null) {
            this.animationSecondHandler.animateYUIComponent(this, 0);
         }

         if (this.handler != null) {
            this.handler.animateYUIComponent(this, 0);
         }

      }
   }

   public final void forceAnimationY() {
      if (this.isActiveAnimationY()) {
         this.animateY.value = this.animateY.needValue;
         this.checkAnimationY();
      }

   }

   private void checkAnimationY() {
      int y2 = this.y;
      this.y = this.animateY.value;
      this.yContent = this.y + this.paddingTop;
      if (this.animationSecondHandler != null) {
         this.animationSecondHandler.animateYUIComponent(this, this.y - y2);
      }

      if (this.handler != null) {
         this.handler.animateYUIComponent(this, this.y - y2);
      }

      if (this.animateY.value == this.animateY.needValue) {
         this.notifyAnimationYFinished();
      }

   }

   private void notifyAnimationYFinished() {
      if (this.animationSecondHandler != null) {
         this.animationSecondHandler.finishAnimationYUIComponent(this);
      }

      if (this.handler != null) {
         this.handler.finishAnimationYUIComponent(this);
      }

   }

   public final void clearAnimationY() {
      this.animateY.clearAnimation(this.y);
   }

   public final boolean isActiveAnimationX() {
      return this.animateX.needValue != this.x;
   }

   public final void startAnimationX(int fromX, int toX, long time) {
      this.startAnimationX(fromX, toX, 140L, 0L);
   }

   private void startAnimationX(int fromX, int toX, long time, long forceProgress) {
      fromX = (int)(0L * (long)(toX - fromX) / 10000L + (long)fromX);
      if ((time = 10000L * time / 10000L) <= 0L) {
         time = 1L;
      }

      if (fromX == toX) {
         this.notifyAnimationXFinished();
      } else {
         this.x = fromX;
         this.xContent = this.x + this.paddingLeft;
         this.animateX.initAnimation(fromX, toX, time);
         if (this.animationSecondHandler != null) {
            this.animationSecondHandler.clickUIComponent2(this);
         }

         if (this.handler != null) {
            this.handler.clickUIComponent2(this);
         }

      }
   }

   private void notifyAnimationXFinished() {
      if (this.animationSecondHandler != null) {
         this.animationSecondHandler.finishAnimationXUIComponent(this);
      }

      if (this.handler != null) {
         this.handler.finishAnimationXUIComponent(this);
      }

   }

   public final void clearAnimationX() {
      this.animateX.clearAnimation(this.x);
   }
}
