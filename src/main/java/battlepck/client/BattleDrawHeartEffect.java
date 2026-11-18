package battlepck.client;

import Engine.Camera;
import Engine.MystFont;
import battlepck.BattleUnit;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class BattleDrawHeartEffect {
   private byte color;
   private String text;
   private GameImage postfix1Image;
   private GameImage postfix2Image;
   private int fromYOffset;
   public int toYOffset;
   private boolean wasOffsetReport;
   private int calcMaxHeight;
   private int calcXOffset0;
   private int calcXOffset1;
   private int calcXOffset2;
   public int curTime;
   private MystFont colorFont;

   public BattleDrawHeartEffect(byte color2, String messageText, GameImage gameImage, GameImage gameImage2, int fromYOffset2, int toYOffset2) {
      this(color2, messageText, gameImage, gameImage2, fromYOffset2, toYOffset2, true);
   }

   private BattleDrawHeartEffect(byte color2, String messageText, GameImage gameImage, GameImage gameImage2, int fromYOffset2, int toYOffset2, boolean calculate) {
      this.wasOffsetReport = false;
      this.curTime = 0;
      this.color = (byte)color2;
      this.text = messageText;
      this.postfix1Image = gameImage;
      this.postfix2Image = gameImage2;
      this.fromYOffset = fromYOffset2;
      this.toYOffset = toYOffset2;
      this.wasOffsetReport = false;
      if (calculate) {
         switch (color2) {
            case 0:
               this.colorFont = BattleUnit.dRFont;
               break;
            case 1:
               this.colorFont = BattleUnit.dGFont;
               break;
            case 2:
               this.colorFont = BattleUnit.mFont;
               break;
            default:
               this.colorFont = BattleUnit.mFont;
         }

         color2 = (byte)(messageText == null ? 0 : this.colorFont.getTextWidth(messageText));
         fromYOffset2 = gameImage == null ? 0 : gameImage.getWidth();
         toYOffset2 = gameImage2 == null ? 0 : gameImage2.getWidth();
         this.calcXOffset0 = -(color2 + fromYOffset2 + toYOffset2) / 2;
         this.calcXOffset1 = this.calcXOffset0 + color2;
         this.calcXOffset2 = this.calcXOffset1 + fromYOffset2;
         this.calcMaxHeight = 0;
         if (messageText != null) {
            this.calcMaxHeight = Math.max(this.calcMaxHeight, this.colorFont.fontHeight);
         }

         if (gameImage != null) {
            this.calcMaxHeight = Math.max(this.calcMaxHeight, gameImage.getHeight());
         }

         if (gameImage2 != null) {
            this.calcMaxHeight = Math.max(this.calcMaxHeight, gameImage2.getHeight());
         }
      }

   }

   public final BattleDrawHeartEffect cloneTo() {
      BattleDrawHeartEffect battleDrawHeartEffect;
      (battleDrawHeartEffect = new BattleDrawHeartEffect(this.color, this.text, this.postfix1Image, this.postfix2Image, this.fromYOffset, this.toYOffset, false)).curTime = this.curTime;
      battleDrawHeartEffect.calcXOffset0 = this.calcXOffset0;
      battleDrawHeartEffect.calcXOffset1 = this.calcXOffset1;
      battleDrawHeartEffect.calcXOffset2 = this.calcXOffset2;
      battleDrawHeartEffect.colorFont = this.colorFont;
      battleDrawHeartEffect.calcMaxHeight = this.calcMaxHeight;
      battleDrawHeartEffect.wasOffsetReport = this.wasOffsetReport;
      return battleDrawHeartEffect;
   }

   public final int draw(Camera camera, GameGraphics gameGraphics2, int cx, int cy) {
      int calcMaxHeight2 = 0;
      long tTime = (long)this.curTime;
      int offsetY = -camera.getCh();
      if (tTime <= 300L) {
         offsetY = (int)(tTime * (long)(this.toYOffset - this.fromYOffset) / 300L) + this.fromYOffset;
         if (!this.wasOffsetReport && Math.abs(this.toYOffset - offsetY) <= this.calcMaxHeight) {
            calcMaxHeight2 = -this.calcMaxHeight;
            this.wasOffsetReport = true;
         }
      } else if ((tTime -= 300L) <= 700L) {
         offsetY = this.toYOffset;
         if (!this.wasOffsetReport) {
            calcMaxHeight2 = -this.calcMaxHeight;
            this.wasOffsetReport = true;
         }
      } else if ((tTime -= 700L) <= 400L) {
         offsetY = -((int)(tTime * (long)camera.getCh() / 400L)) + this.toYOffset;
         if (!this.wasOffsetReport) {
            calcMaxHeight2 = -this.calcMaxHeight;
            this.wasOffsetReport = true;
         }
      }

      if (this.text != null) {
         this.colorFont.writeText(gameGraphics2, this.text, cx + this.calcXOffset0, cy + offsetY - this.colorFont.fontHeight);
      }

      if (this.postfix1Image != null) {
         gameGraphics2.drawImage(this.postfix1Image, cx + this.calcXOffset1, cy + offsetY, 36);
      }

      if (this.postfix2Image != null) {
         gameGraphics2.drawImage(this.postfix2Image, cx + this.calcXOffset2, cy + offsetY, 36);
      }

      return calcMaxHeight2;
   }
}
