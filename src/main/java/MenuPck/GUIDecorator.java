package MenuPck;

import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class GUIDecorator implements UIComponentDecorator {
   private static GameImage toothImage;
   private static int toothImageWidth;
   private static int toothImageHeight;
   public static GameImage toothCornerImage;
   public static int toothCornerImageWidth;
   public static int toothCornerImageHeight;
   public static GameImage toothBorderImage;
   public static int toothBorderImageWidth;
   public static int toothBorderImageHeight;
   public static GameImage toothCornerSelectImage;
   public static int toothCornerSelectImageWidth;
   public static int toothCornerSelectImageHeight;
   public static GameImage toothBorderSelectImage;
   public static int toothBorderSelectImageWidth;
   public static int toothBorderSelectImageHeight;
   public static GameImage toothCornerPressImage;
   public static int toothCornerPressImageWidth;
   public static int toothCornerPressImageHeight;
   public static GameImage toothArrowPressImage;
   public static int toothArrowPressImageWidth;
   public static int toothArrowPressImageHeight;
   public static GameImage toothBorderPressImage;
   public static int toothBorderPressImageWidth;
   public static int toothBorderPressImageHeight;
   private static GameImage clawCornerTopImage;
   private static GameImage clawCornerImage;
   private static GameImage clawBorderImage;
   private static GameImage snakeCornerImage;
   private static GameImage snakeBorderImage;
   private static GameImage topSnakeImage;
   private static GameImage botSnakeImage;
   private static int CLAW_CORNER_TOP_OFF_Y = 12;
   private static int CLAW_CORNER_TOP_SIZE_X = 8;
   private static int CLAW_CORNER_TOP_SIZE_Y = 8;
   public static int CLAW_CORNER_OFF_X = 8;
   public static int CLAW_CORNER_OFF_Y = 8;
   private static int CLAW_CORNER_SIZE_X = 8;
   private static int CLAW_CORNER_SIZE_Y = 8;
   private static int CLAW_BORDER_SIZE_X = 8;
   private static int CLAW_BORDER_SIZE_Y = 12;
   private static int SNAKE_CORNER_OFF_X = 8;
   private static int SNAKE_CORNER_OFF_Y = 8;
   private static int SNAKE_CORNER_SIZE_X = 8;
   private static int SNAKE_CORNER_SIZE_Y = 8;
   public static int SNAKE_BORDER_SIZE_X = 8;
   private static int SNAKE_BORDER_SIZE_Y = 12;
   public static int SNAKE_TOP_OFF_X = 41;
   public static int SNAKE_TOP_OFF_Y = 13;
   private static int SNAKE_TOP_SIZE_X = 41;
   public static int SNAKE_TOP_SIZE_Y = 13;
   private static int SNAKE_BOT_OFF_X = 20;
   public static int SNAKE_BOT_OFF_Y = 8;
   private static int SNAKE_BOT_SIZE_X = 20;
   public static int SNAKE_BOT_SIZE_Y = 8;
   public static GUIDecorator instance = new GUIDecorator();
   private static boolean drawSnakes = true;

   public static void loadResources(boolean drawSnakes2) {
      drawSnakes = drawSnakes2;
      toothImageWidth = (toothImage = GameImage.createImage("/Data/All/itrfc/tooth.png")).getWidth();
      toothImageHeight = toothImage.getHeight();
      toothCornerImageWidth = (toothCornerImage = GameImage.createImage("/Data/All/itrfc/tooth_corner.png")).getWidth();
      toothCornerImageHeight = toothCornerImage.getHeight();
      toothBorderImageWidth = (toothBorderImage = GameImage.createImage("/Data/All/itrfc/tooth_border.png")).getWidth();
      toothBorderImageHeight = toothBorderImage.getHeight();
      toothCornerSelectImageWidth = (toothCornerSelectImage = GameImage.createImage("/Data/All/itrfc/tooth_corner_select.png")).getWidth();
      toothCornerSelectImageHeight = toothCornerSelectImage.getHeight();
      toothBorderSelectImageWidth = (toothBorderSelectImage = GameImage.createImage("/Data/All/itrfc/tooth_border_select.png")).getWidth();
      toothBorderSelectImageHeight = toothBorderSelectImage.getHeight();
      toothCornerPressImageWidth = (toothCornerPressImage = GameImage.createImage("/Data/All/itrfc/tooth_corner_press.png")).getWidth();
      toothCornerPressImageHeight = toothCornerPressImage.getHeight();
      toothArrowPressImageWidth = (toothArrowPressImage = GameImage.createImage("/Data/All/itrfc/tooth_arrow_press.png")).getWidth();
      toothArrowPressImageHeight = toothArrowPressImage.getHeight();
      toothBorderPressImageWidth = (toothBorderPressImage = GameImage.createImage("/Data/All/itrfc/tooth_border_press.png")).getWidth();
      toothBorderPressImageHeight = toothBorderPressImage.getHeight();
      CLAW_CORNER_TOP_SIZE_X = (clawCornerTopImage = GameImage.createImage("/Data/All/itrfc/window_claw_corner_top.png")).getWidth();
      CLAW_CORNER_TOP_SIZE_Y = clawCornerTopImage.getHeight();
      CLAW_CORNER_SIZE_X = (clawCornerImage = GameImage.createImage("/Data/All/itrfc/window_claw_corner.png")).getWidth();
      CLAW_CORNER_SIZE_Y = clawCornerImage.getHeight();
      CLAW_BORDER_SIZE_X = (clawBorderImage = GameImage.createImage("/Data/All/itrfc/window_claw_border.png")).getWidth();
      CLAW_BORDER_SIZE_Y = clawBorderImage.getHeight();
      SNAKE_CORNER_SIZE_X = (snakeCornerImage = GameImage.createImage("/Data/All/itrfc/window_snake_corner.png")).getWidth();
      SNAKE_CORNER_SIZE_Y = snakeCornerImage.getHeight();
      SNAKE_BORDER_SIZE_X = (snakeBorderImage = GameImage.createImage("/Data/All/itrfc/window_snake_border.png")).getWidth();
      SNAKE_BORDER_SIZE_Y = snakeBorderImage.getHeight();
      if (drawSnakes2) {
         SNAKE_TOP_SIZE_X = (topSnakeImage = GameImage.createImage("/Data/All/itrfc/window_snake_top.png")).getWidth();
         SNAKE_TOP_SIZE_Y = topSnakeImage.getHeight();
         SNAKE_BOT_SIZE_X = (botSnakeImage = GameImage.createImage("/Data/All/itrfc/window_snake_bottom.png")).getWidth();
         SNAKE_BOT_SIZE_Y = botSnakeImage.getHeight();
      }

   }

   public static void addForAtlasing(Vector vector3) {
      vector3.addElement(toothImage);
      vector3.addElement(toothCornerImage);
      vector3.addElement(toothBorderImage);
      vector3.addElement(toothCornerSelectImage);
      vector3.addElement(toothBorderSelectImage);
      vector3.addElement(toothCornerPressImage);
      vector3.addElement(toothArrowPressImage);
      vector3.addElement(toothBorderPressImage);
      vector3.addElement(clawCornerTopImage);
      vector3.addElement(clawCornerImage);
      vector3.addElement(clawBorderImage);
      vector3.addElement(snakeCornerImage);
      vector3.addElement(snakeBorderImage);
      vector3.addElement(topSnakeImage);
      vector3.addElement(botSnakeImage);
   }

   public final void initPaddings(UIComponent uIComponent15) {
      switch (uIComponent15.decorType) {
         case 0:
            uIComponent15.paddingLeft = 0;
            uIComponent15.paddingRight = 0;
            uIComponent15.paddingTop = 0;
            uIComponent15.paddingBottom = 0;
            return;
         case 1:
            uIComponent15.paddingLeft = toothImageWidth;
            uIComponent15.paddingRight = toothBorderImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = 0;
            return;
         case 2:
            uIComponent15.paddingLeft = toothBorderImageWidth;
            uIComponent15.paddingRight = toothImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = 0;
            return;
         case 3:
            uIComponent15.paddingLeft = toothImageWidth;
            uIComponent15.paddingRight = toothImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = 0;
            return;
         case 4:
            uIComponent15.paddingLeft = toothImageWidth;
            uIComponent15.paddingRight = toothBorderImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = toothBorderImageWidth;
            return;
         case 5:
            uIComponent15.paddingLeft = toothBorderImageWidth;
            uIComponent15.paddingRight = toothImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = toothBorderImageWidth;
            return;
         case 6:
            uIComponent15.paddingLeft = Math.max(SNAKE_TOP_OFF_X, Math.max(SNAKE_BOT_OFF_X, SNAKE_BORDER_SIZE_X));
            uIComponent15.paddingRight = Math.max(SNAKE_TOP_OFF_X, Math.max(SNAKE_BOT_OFF_X, SNAKE_BORDER_SIZE_X));
            uIComponent15.paddingTop = Math.max(SNAKE_TOP_OFF_Y, SNAKE_BORDER_SIZE_X);
            uIComponent15.paddingBottom = Math.max(SNAKE_BOT_OFF_Y, SNAKE_BORDER_SIZE_X);
            return;
         case 7:
            uIComponent15.paddingLeft = CLAW_CORNER_OFF_X;
            uIComponent15.paddingRight = CLAW_CORNER_OFF_X;
            uIComponent15.paddingTop = CLAW_CORNER_OFF_Y;
            uIComponent15.paddingBottom = CLAW_CORNER_OFF_Y;
            return;
         case 8:
            uIComponent15.paddingLeft = toothBorderImageWidth;
            uIComponent15.paddingRight = toothBorderImageWidth;
            uIComponent15.paddingTop = toothBorderImageWidth;
            uIComponent15.paddingBottom = toothBorderImageWidth;
            return;
         case 9:
            uIComponent15.paddingLeft = CLAW_CORNER_OFF_X;
            uIComponent15.paddingRight = CLAW_CORNER_OFF_X;
            uIComponent15.paddingTop = CLAW_CORNER_TOP_OFF_Y;
            uIComponent15.paddingBottom = CLAW_CORNER_OFF_Y;
            return;
         case 10:
            uIComponent15.paddingLeft = toothBorderSelectImageWidth;
            uIComponent15.paddingRight = toothBorderSelectImageWidth;
            uIComponent15.paddingTop = toothBorderSelectImageWidth;
            uIComponent15.paddingBottom = toothBorderSelectImageWidth;
            return;
         case 11:
            uIComponent15.paddingLeft = toothBorderPressImageWidth;
            uIComponent15.paddingRight = toothBorderPressImageWidth;
            uIComponent15.paddingTop = toothBorderPressImageWidth;
            uIComponent15.paddingBottom = toothBorderPressImageWidth;
            return;
         default:
            uIComponent15.paddingLeft = 0;
            uIComponent15.paddingRight = 0;
            uIComponent15.paddingTop = 0;
            uIComponent15.paddingBottom = 0;
      }
   }

   public final void correctPaddingsFinal(UIComponent uIComponent15) {
      switch (uIComponent15.decorType) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            if (uIComponent15.height < toothImageHeight) {
               uIComponent15.paddingTop += toothImageHeight - uIComponent15.height;
               uIComponent15.height = toothImageHeight;
            }
         default:
      }
   }

   public final void fillBackground(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      GameGraphics gameGraphics3;
      int backgroundColor;
      switch (uIComponent15.decorType) {
         case 6:
         case 7:
         case 9:
            gameGraphics2.setColor(uIComponent15.overBackGroundColor >= 0 ? uIComponent15.overBackGroundColor : 263172);
            gameGraphics2.fillRect(0, 0, uIComponent15.widthContent, uIComponent15.heightContent);
            return;
         case 8:
            gameGraphics3 = gameGraphics2;
            backgroundColor = getBackgroundColor(uIComponent15);
            break;
         case 10:
            gameGraphics2.setColor(uIComponent15.overBackGroundColor >= 0 ? uIComponent15.overBackGroundColor : 4539700);
            gameGraphics2.fillRect(0, 0, uIComponent15.widthContent, uIComponent15.heightContent);
            return;
         case 11:
            gameGraphics3 = gameGraphics2;
            backgroundColor = uIComponent15.overBackGroundColor >= 0 ? uIComponent15.overBackGroundColor : 2119968;
            break;
         case 12:
            if (uIComponent15.state == 1) {
               gameGraphics3 = gameGraphics2;
               backgroundColor = 4210752;
               break;
            }

            return;
         default:
            return;
      }

      gameGraphics3.setColor(backgroundColor);
      gameGraphics2.fillRect(0, 0, uIComponent15.widthContent, uIComponent15.heightContent);
   }

   public final void draw(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      int leftBorderPadding;
      int rightBorderPadding;
      int x;
      int contentY;
      int width;
      int contentHeight;
      int borderWidth;
      switch (uIComponent15.decorType) {
         case 1:
         case 2:
         case 3:
            leftBorderPadding = toothBorderImageWidth + (uIComponent15.decorType != 1 && uIComponent15.decorType != 3 ? 0 : 3);
            rightBorderPadding = toothBorderImageWidth + (uIComponent15.decorType != 2 && uIComponent15.decorType != 3 ? 0 : 3);
            gameGraphics2.setColor(getBackgroundColor(uIComponent15));
            gameGraphics2.fillRect(uIComponent15.x, uIComponent15.y + uIComponent15.paddingTop, uIComponent15.width, uIComponent15.height - uIComponent15.paddingTop - uIComponent15.paddingBottom);
            x = uIComponent15.x + leftBorderPadding;
            contentY = uIComponent15.y + uIComponent15.paddingTop;
            width = uIComponent15.width - leftBorderPadding - rightBorderPadding;
            contentHeight = uIComponent15.height - uIComponent15.paddingTop - uIComponent15.paddingBottom;
            borderWidth = toothBorderImageWidth;
            UIComponent.drawBorders(gameGraphics2, x, contentY, width, contentHeight, toothCornerImage, toothBorderImage, toothCornerImageWidth, toothCornerImageHeight, borderWidth, borderWidth, toothBorderImageWidth, toothBorderImageHeight, true, true, true, false);
            return;
         case 4:
         case 5:
            leftBorderPadding = toothBorderImageWidth + (uIComponent15.decorType == 4 ? 3 : 0);
            rightBorderPadding = toothBorderImageWidth + (uIComponent15.decorType == 5 ? 3 : 0);
            gameGraphics2.setColor(getBackgroundColor(uIComponent15));
            gameGraphics2.fillRect(uIComponent15.x + leftBorderPadding, uIComponent15.y + uIComponent15.paddingTop, uIComponent15.width - leftBorderPadding - rightBorderPadding, uIComponent15.height - uIComponent15.paddingTop - uIComponent15.paddingBottom);
            x = uIComponent15.x + leftBorderPadding;
            contentY = uIComponent15.y + uIComponent15.paddingTop;
            width = uIComponent15.width - leftBorderPadding - rightBorderPadding;
            contentHeight = uIComponent15.height - uIComponent15.paddingTop - uIComponent15.paddingBottom;
            borderWidth = toothBorderImageWidth;
            UIComponent.drawBorders(gameGraphics2, x, contentY, width, contentHeight, toothCornerImage, toothBorderImage, toothCornerImageWidth, toothCornerImageHeight, borderWidth, borderWidth, toothBorderImageWidth, toothBorderImageHeight);
            return;
         case 6:
            UIComponent.drawBorders(gameGraphics2, uIComponent15.xContent, uIComponent15.yContent, uIComponent15.widthContent, uIComponent15.heightContent, snakeCornerImage, snakeBorderImage, SNAKE_CORNER_SIZE_X, SNAKE_CORNER_SIZE_Y, SNAKE_CORNER_OFF_X, SNAKE_CORNER_OFF_Y, SNAKE_BORDER_SIZE_X, SNAKE_BORDER_SIZE_Y);
            return;
         case 7:
            UIComponent.drawBorders(gameGraphics2, uIComponent15.xContent, uIComponent15.yContent, uIComponent15.widthContent, uIComponent15.heightContent, clawCornerImage, clawBorderImage, CLAW_CORNER_SIZE_X, CLAW_CORNER_SIZE_Y, CLAW_CORNER_OFF_X, CLAW_CORNER_OFF_Y, CLAW_BORDER_SIZE_X, CLAW_BORDER_SIZE_Y);
            return;
         case 8:
            borderWidth = toothBorderImageWidth;
            UIComponent.drawBorders(gameGraphics2, uIComponent15.xContent, uIComponent15.yContent, uIComponent15.widthContent, uIComponent15.heightContent, toothCornerImage, toothBorderImage, toothCornerImageWidth, toothCornerImageHeight, borderWidth, borderWidth, toothBorderImageWidth, toothBorderImageHeight);
            return;
         case 9:
            GameGraphics gameGraphics3 = gameGraphics2;
            int CLAW_BORDER_SIZE_Y2 = CLAW_BORDER_SIZE_Y;
            int CLAW_BORDER_SIZE_X2 = CLAW_BORDER_SIZE_X;
            int CLAW_CORNER_OFF_Y2 = CLAW_CORNER_OFF_Y;
            int CLAW_CORNER_TOP_OFF_Y2 = CLAW_CORNER_TOP_OFF_Y;
            int CLAW_CORNER_OFF_X2 = CLAW_CORNER_OFF_X;
            int CLAW_CORNER_SIZE_Y2 = CLAW_CORNER_SIZE_Y;
            int CLAW_CORNER_TOP_SIZE_Y2 = CLAW_CORNER_TOP_SIZE_Y;
            int CLAW_CORNER_SIZE_X2 = CLAW_CORNER_SIZE_X;
            int CLAW_CORNER_TOP_SIZE_X2 = CLAW_CORNER_TOP_SIZE_X;
            GameImage clawBorderImage2 = clawBorderImage;
            GameImage clawCornerImage2 = clawCornerImage;
            GameImage clawCornerTopImage2 = clawCornerTopImage;
            int heightContent2 = uIComponent15.heightContent;
            rightBorderPadding = uIComponent15.widthContent;
            leftBorderPadding = uIComponent15.yContent;
            int xContent2 = uIComponent15.xContent;
            boolean unusedProgressMarker = true;
            unusedProgressMarker = true;
            unusedProgressMarker = true;
            unusedProgressMarker = true;
            UIComponent.drawBorders(gameGraphics3, xContent2, leftBorderPadding, rightBorderPadding, heightContent2, clawCornerTopImage2, clawCornerImage2, clawBorderImage2, CLAW_CORNER_TOP_SIZE_X2, CLAW_CORNER_SIZE_X2, CLAW_CORNER_TOP_SIZE_Y2, CLAW_CORNER_SIZE_Y2, CLAW_CORNER_OFF_X2, CLAW_CORNER_TOP_OFF_Y2, CLAW_CORNER_OFF_Y2, CLAW_BORDER_SIZE_X2, CLAW_BORDER_SIZE_Y2, true, true, true, true, -1);
         default:
            return;
         case 10:
            borderWidth = toothBorderSelectImageWidth;
            UIComponent.drawBorders(gameGraphics2, uIComponent15.xContent, uIComponent15.yContent, uIComponent15.widthContent, uIComponent15.heightContent, toothCornerSelectImage, toothBorderSelectImage, toothCornerSelectImageWidth, toothCornerSelectImageHeight, borderWidth, borderWidth, toothBorderSelectImageWidth, toothBorderSelectImageHeight);
            return;
         case 11:
            borderWidth = toothBorderPressImageWidth;
            UIComponent.drawBorders(gameGraphics2, uIComponent15.xContent, uIComponent15.yContent, uIComponent15.widthContent, uIComponent15.heightContent, toothCornerPressImage, toothBorderPressImage, toothCornerPressImageWidth, toothCornerPressImageHeight, borderWidth, borderWidth, toothBorderPressImageWidth, toothBorderPressImageHeight);
      }
   }

   private static int getBackgroundColor(UIComponent uIComponent15) {
      if (uIComponent15.state == 1) {
         return 4210752;
      } else {
         return uIComponent15.overBackGroundColor >= 0 ? uIComponent15.overBackGroundColor : 263172;
      }
   }

   public final void paintBordersPhase2(UIComponent uIComponent15, GameGraphics gameGraphics2) {
      switch (uIComponent15.decorType) {
         case 1:
         case 4:
            gameGraphics2.drawImage(toothImage, uIComponent15.x, uIComponent15.y + uIComponent15.height - toothImageHeight, 0);
            return;
         case 2:
         case 5:
            gameGraphics2.drawRegion(toothImage, 0, 0, toothImageWidth, toothImageHeight, 2, uIComponent15.x + uIComponent15.width - toothImageWidth, uIComponent15.y + uIComponent15.height - toothImageHeight);
            return;
         case 3:
            gameGraphics2.drawImage(toothImage, uIComponent15.x, uIComponent15.y + uIComponent15.height - toothImageHeight, 0);
            gameGraphics2.drawRegion(toothImage, 0, 0, toothImageWidth, toothImageHeight, 2, uIComponent15.x + uIComponent15.width - toothImageWidth, uIComponent15.y + uIComponent15.height - toothImageHeight);
            return;
         case 6:
            if (drawSnakes) {
               if (SNAKE_TOP_SIZE_Y - SNAKE_TOP_OFF_Y <= uIComponent15.heightContent) {
                  gameGraphics2.drawRegion(topSnakeImage, 0, 0, SNAKE_TOP_SIZE_X, SNAKE_TOP_SIZE_Y, 0, uIComponent15.xContent - SNAKE_TOP_OFF_X, uIComponent15.yContent - SNAKE_TOP_OFF_Y);
                  gameGraphics2.drawRegion(topSnakeImage, 0, 0, SNAKE_TOP_SIZE_X, SNAKE_TOP_SIZE_Y, 2, uIComponent15.xContent + uIComponent15.widthContent - (SNAKE_TOP_SIZE_X - SNAKE_TOP_OFF_X), uIComponent15.yContent - SNAKE_TOP_OFF_Y);
               }

               if (uIComponent15.heightContent - (SNAKE_TOP_SIZE_Y - SNAKE_TOP_OFF_Y) >= SNAKE_BOT_OFF_Y) {
                  gameGraphics2.drawRegion(botSnakeImage, 0, 0, SNAKE_BOT_SIZE_X, SNAKE_BOT_SIZE_Y, 0, uIComponent15.xContent - SNAKE_BOT_OFF_X, uIComponent15.yContent + uIComponent15.heightContent - SNAKE_BOT_OFF_Y);
                  gameGraphics2.drawRegion(botSnakeImage, 0, 0, SNAKE_BOT_SIZE_X, SNAKE_BOT_SIZE_Y, 2, uIComponent15.xContent + uIComponent15.widthContent - (SNAKE_BOT_SIZE_X - SNAKE_BOT_OFF_X), uIComponent15.yContent + uIComponent15.heightContent - SNAKE_BOT_OFF_Y);
               }
            }
         default:
      }
   }
}
