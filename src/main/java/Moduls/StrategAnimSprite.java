package Moduls;

import Base.Com;
import desktop.PlatformRuntime;
import java.io.DataInputStream;
import desktop.graphics.GameImage;

public final class StrategAnimSprite {
   public static StrategAnimSprite[] strategAnimSprites;
   public GameImage image;
   public byte numFrames;
   public short animTime;
   public short baseX;
   public short baseY;
   public short srcX;
   public short srcY;
   private short srcWidth;
   private short srcHeight;
   public short frameWidth;
   public short frameHeight;

   public final boolean isTransporant(int x, int y, int frame, boolean isMirror) {
      if (x >= 0 && y >= 0 && x < this.frameWidth && y < this.frameHeight) {
         try {
            PlatformRuntime.getInstance();
            return PlatformRuntime.isPixelTransparent(this.image, this.srcX + frame * this.frameWidth + (isMirror ? this.frameWidth - 1 - x : x), this.srcY + y);
         } catch (Exception exception) {
            exception.printStackTrace();
            return true;
         }
      } else {
         return true;
      }
   }

   public static void loadAllFromStream(DataInputStream dataInputStream, GameImage[] images) {
      strategAnimSprites = new StrategAnimSprite[decompiledsupport.UncheckedDataInput.readShort(dataInputStream)];

      for(int i = 0; i < strategAnimSprites.length; ++i) {
         Com.PrBarTh.setText("Описание спрайта " + (i + 1) + "/" + strategAnimSprites.length);
         StrategAnimSprite strategAnimSprite = new StrategAnimSprite();
         strategAnimSprites[i] = strategAnimSprite;
         short shortCode = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.image = images[shortCode];
         (strategAnimSprite = strategAnimSprite).numFrames = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
         strategAnimSprite.animTime = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.baseX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.baseY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.srcX = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.srcY = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.srcWidth = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.srcHeight = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
         strategAnimSprite.frameWidth = (short)(strategAnimSprite.srcWidth / strategAnimSprite.numFrames);
         strategAnimSprite.frameHeight = strategAnimSprite.srcHeight;
      }

   }
}
