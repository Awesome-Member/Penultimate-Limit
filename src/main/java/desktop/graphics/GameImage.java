package desktop.graphics;

import applet.Main;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public final class GameImage {
   private static final AtomicInteger nextImageId = new AtomicInteger(1);
   private int imageId;
   private BufferedImage bufferedImage;

   private GameImage() {
      this.imageId = nextImageId.getAndIncrement();
   }

   public static GameImage createImage(String messageText) {
      GameImage gameImage = new GameImage();
      String substring = messageText;
      if (messageText.charAt(0) == '/') {
         substring = messageText.substring(1);
      }

      URL uRL = ((URLClassLoader)Main.instance.getClass().getClassLoader()).findResource(substring);

      try {
         gameImage.bufferedImage = ImageIO.read(uRL);
      } catch (IOException exception) {
         throw new RuntimeException(exception);
      }

      while(gameImage.bufferedImage.getWidth((ImageObserver)null) == -1) {
         Thread.yield();
      }

      return gameImage;
   }

   public static GameImage createImage(InputStream inputStream) {
      GameImage gameImage = new GameImage();

      try {
         gameImage.bufferedImage = ImageIO.read(inputStream);
      } catch (IOException exception) {
         throw new RuntimeException(exception);
      }

      while(gameImage.bufferedImage.getWidth((ImageObserver)null) == -1) {
         Thread.yield();
      }

      return gameImage;
   }

   public static GameImage createImage(byte[] bytes, int offset, int length) {
      GameImage gameImage = new GameImage();
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes, 0, length);

      try {
         gameImage.bufferedImage = ImageIO.read(byteArrayInputStream);
      } catch (IOException exception) {
         throw new RuntimeException(exception);
      }

      while(gameImage.bufferedImage.getWidth((ImageObserver)null) == -1) {
         Thread.yield();
      }

      return gameImage;
   }

   public static GameImage createImage(int width, int height) {
      GameImage gameImage = new GameImage();

      try {
         gameImage.bufferedImage = new BufferedImage(width, height, 2);
      } catch (Exception exception) {
         throw new UnsupportedOperationException(exception);
      }

      if (gameImage.bufferedImage == null) {
         throw new UnsupportedOperationException("Can't create Image");
      } else {
         while(gameImage.bufferedImage.getWidth((ImageObserver)null) == -1) {
            Thread.yield();
         }

         return gameImage;
      }
   }

   public final Graphics getAwtGraphics() {
      return this.bufferedImage.getGraphics();
   }

   public final BufferedImage getBufferedImage() {
      return this.bufferedImage;
   }

   public final int getWidth() {
      int width;
      while(-1 == (width = this.bufferedImage.getWidth((ImageObserver)null))) {
         Thread.yield();
      }

      return width;
   }

   public final int getHeight() {
      int height;
      while(-1 == (height = this.bufferedImage.getHeight((ImageObserver)null))) {
         Thread.yield();
      }

      return height;
   }

   public final GameGraphics getGraphics() {
      return new GameGraphics(this);
   }
}
