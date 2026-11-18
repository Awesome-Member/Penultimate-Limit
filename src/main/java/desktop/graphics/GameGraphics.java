package desktop.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public final class GameGraphics {
   private int translateX = 0;
   private int translateY = 0;
   private Graphics graphics;
   private GameImage image;
   private static HashMap transformedImageCache = new HashMap();
   private static long lastCacheCleanupTime = 0L;

   public GameGraphics(Graphics graphics2, int width, int height) {
      this.image = null;
      this.graphics = graphics2;
      this.graphics.setClip(0, 0, width, height);
   }

   public GameGraphics(GameImage gameImage) {
      this.image = gameImage;
      this.graphics = gameImage.getAwtGraphics();
      this.graphics.setClip(0, 0, gameImage.getWidth(), gameImage.getHeight());
   }

   public final void setColor(int rgb) {
      this.graphics.setColor(new Color(rgb));
   }

   public final void fillRect(int x, int y, int width, int height) {
      this.graphics.fillRect(x, y, width, height);
   }

   public final void setColor(int red, int green, int blue) {
      this.graphics.setColor(new Color(red, green, blue));
   }

   public final void drawImage(GameImage gameImage, int x, int y, int anchor) {
      x = x;
      y = y;
      if ((anchor & 1) > 0) {
         x -= gameImage.getWidth() / 2;
      }

      if ((anchor & 2) > 0) {
         y -= gameImage.getHeight() / 2;
      }

      if ((anchor & 32) > 0) {
         y -= gameImage.getHeight();
      }

      if ((anchor & 8) > 0) {
         x -= gameImage.getWidth();
      }

      while(!this.graphics.drawImage(gameImage.getBufferedImage(), x, y, (ImageObserver)null)) {
      }

   }

   public final void drawRegion(GameImage gameImage, int sourceX, int sourceY, int width2, int height2, int transform, int destinationX, int destinationY) {
      long lastCacheCleanupTime2;
      if ((lastCacheCleanupTime2 = System.currentTimeMillis()) - lastCacheCleanupTime >= 90000L) {
         LinkedList linkedList2 = null;
         Iterator iterator = transformedImageCache.entrySet().iterator();

         while(iterator.hasNext()) {
            Map.Entry entry;
            Iterator iterator2 = ((HashMap)(entry = (Map.Entry)iterator.next()).getValue()).entrySet().iterator();

            while(iterator2.hasNext()) {
               Map.Entry entry2 = (Map.Entry)iterator2.next();
               if (lastCacheCleanupTime2 - (Long)((GraphicsCacheEntry)entry2.getValue()).key >= 180000L) {
                  ((GraphicsCacheEntry)entry2.getValue()).value = null;
                  if (linkedList2 == null) {
                     linkedList2 = new LinkedList();
                  }

                  linkedList2.add(new GraphicsCacheEntry(entry.getKey(), entry2.getKey(), (byte)0));
               }
            }
         }

         if (linkedList2 != null) {
            iterator = linkedList2.iterator();

            while(iterator.hasNext()) {
               GraphicsCacheEntry graphicsCacheEntry = (GraphicsCacheEntry)iterator.next();
               HashMap hashMap;
               (hashMap = (HashMap)transformedImageCache.get(graphicsCacheEntry.key)).remove(graphicsCacheEntry.value);
               if (hashMap.size() <= 0) {
                  transformedImageCache.remove(graphicsCacheEntry.key);
               }
            }
         }

         lastCacheCleanupTime = lastCacheCleanupTime2;
      }

      BufferedImage bufferedImage2 = gameImage.getBufferedImage();
      int width = gameImage.getWidth();
      int height = gameImage.getHeight();
      AffineTransform affineTransform;
      AffineTransformOp affineTransformOp;
      BufferedImage bufferedImage3;
      HashMap hashMap2;
      GraphicsCacheEntry graphicsCacheEntry2;
      switch (transform) {
         case 1:
            if ((hashMap2 = (HashMap)transformedImageCache.get(gameImage)) == null) {
               hashMap2 = new HashMap();
               transformedImageCache.put(gameImage, hashMap2);
            }

            if ((graphicsCacheEntry2 = (GraphicsCacheEntry)hashMap2.get(transform)) == null) {
               (affineTransform = new AffineTransform()).scale(1.0, -1.0);
               affineTransform.translate(0.0, (double)(-height));
               bufferedImage3 = (affineTransformOp = new AffineTransformOp(affineTransform, 1)).createCompatibleDestImage(bufferedImage2, (ColorModel)null);
               affineTransformOp.filter(bufferedImage2, bufferedImage3);
               graphicsCacheEntry2 = new GraphicsCacheEntry(System.currentTimeMillis(), bufferedImage3, (byte)0);
               hashMap2.put(transform, graphicsCacheEntry2);
            }

            graphicsCacheEntry2.key = System.currentTimeMillis();

            while(!this.graphics.drawImage((Image)graphicsCacheEntry2.value, destinationX, destinationY, destinationX + width2, destinationY + height2, sourceX, height - sourceY - height2, sourceX + width2, height - sourceY, (ImageObserver)null)) {
               Thread.yield();
            }

            return;
         case 2:
            if ((hashMap2 = (HashMap)transformedImageCache.get(gameImage)) == null) {
               hashMap2 = new HashMap();
               transformedImageCache.put(gameImage, hashMap2);
            }

            if ((graphicsCacheEntry2 = (GraphicsCacheEntry)hashMap2.get(transform)) == null) {
               (affineTransform = new AffineTransform()).scale(-1.0, 1.0);
               affineTransform.translate((double)(-width), 0.0);
               bufferedImage3 = (affineTransformOp = new AffineTransformOp(affineTransform, 1)).createCompatibleDestImage(bufferedImage2, (ColorModel)null);
               affineTransformOp.filter(bufferedImage2, bufferedImage3);
               graphicsCacheEntry2 = new GraphicsCacheEntry(System.currentTimeMillis(), bufferedImage3, (byte)0);
               hashMap2.put(transform, graphicsCacheEntry2);
            }

            graphicsCacheEntry2.key = System.currentTimeMillis();

            while(!this.graphics.drawImage((Image)graphicsCacheEntry2.value, destinationX, destinationY, destinationX + width2, destinationY + height2, width - sourceX - width2, sourceY, width - sourceX, sourceY + height2, (ImageObserver)null)) {
               Thread.yield();
            }

            return;
         case 3:
            if ((hashMap2 = (HashMap)transformedImageCache.get(gameImage)) == null) {
               hashMap2 = new HashMap();
               transformedImageCache.put(gameImage, hashMap2);
            }

            if ((graphicsCacheEntry2 = (GraphicsCacheEntry)hashMap2.get(transform)) == null) {
               (affineTransform = new AffineTransform()).quadrantRotate(2, (double)((float)width / 2.0F), (double)((float)height / 2.0F));
               bufferedImage3 = (affineTransformOp = new AffineTransformOp(affineTransform, 1)).createCompatibleDestImage(bufferedImage2, (ColorModel)null);
               affineTransformOp.filter(bufferedImage2, bufferedImage3);
               graphicsCacheEntry2 = new GraphicsCacheEntry(System.currentTimeMillis(), bufferedImage3, (byte)0);
               hashMap2.put(transform, graphicsCacheEntry2);
            }

            graphicsCacheEntry2.key = System.currentTimeMillis();

            while(!this.graphics.drawImage((Image)graphicsCacheEntry2.value, destinationX, destinationY, destinationX + width2, destinationY + height2, width - sourceX - width2, height - sourceY - height2, width - sourceX, height - sourceY, (ImageObserver)null)) {
               Thread.yield();
            }

            return;
         case 4:
         default:
            while(!this.graphics.drawImage(bufferedImage2, destinationX, destinationY, destinationX + width2, destinationY + height2, sourceX, sourceY, sourceX + width2, sourceY + height2, (ImageObserver)null)) {
               Thread.yield();
            }

            return;
         case 5:
            if ((hashMap2 = (HashMap)transformedImageCache.get(gameImage)) == null) {
               hashMap2 = new HashMap();
               transformedImageCache.put(gameImage, hashMap2);
            }

            if ((graphicsCacheEntry2 = (GraphicsCacheEntry)hashMap2.get(transform)) == null) {
               (affineTransform = new AffineTransform()).quadrantRotate(1, (double)((float)width / 2.0F), (double)((float)height / 2.0F));
               affineTransform.translate((double)(-((float)height - (float)width) / 2.0F), (double)(-((float)height - (float)width) / 2.0F));
               bufferedImage3 = (affineTransformOp = new AffineTransformOp(affineTransform, 1)).createCompatibleDestImage(bufferedImage2, (ColorModel)null);
               affineTransformOp.filter(bufferedImage2, bufferedImage3);
               graphicsCacheEntry2 = new GraphicsCacheEntry(System.currentTimeMillis(), bufferedImage3, (byte)0);
               hashMap2.put(transform, graphicsCacheEntry2);
            }

            graphicsCacheEntry2.key = System.currentTimeMillis();

            while(!this.graphics.drawImage((Image)graphicsCacheEntry2.value, destinationX, destinationY, destinationX + height2, destinationY + width2, height - sourceY - height2, sourceX, height - sourceY, sourceX + width2, (ImageObserver)null)) {
               Thread.yield();
            }

            return;
         case 6:
            if ((hashMap2 = (HashMap)transformedImageCache.get(gameImage)) == null) {
               hashMap2 = new HashMap();
               transformedImageCache.put(gameImage, hashMap2);
            }

            if ((graphicsCacheEntry2 = (GraphicsCacheEntry)hashMap2.get(transform)) == null) {
               (affineTransform = new AffineTransform()).quadrantRotate(3, (double)((float)width / 2.0F), (double)((float)height / 2.0F));
               affineTransform.translate((double)(((float)height - (float)width) / 2.0F), (double)(((float)height - (float)width) / 2.0F));
               bufferedImage3 = (affineTransformOp = new AffineTransformOp(affineTransform, 1)).createCompatibleDestImage(bufferedImage2, (ColorModel)null);
               affineTransformOp.filter(bufferedImage2, bufferedImage3);
               graphicsCacheEntry2 = new GraphicsCacheEntry(System.currentTimeMillis(), bufferedImage3, (byte)0);
               hashMap2.put(transform, graphicsCacheEntry2);
            }

            graphicsCacheEntry2.key = System.currentTimeMillis();

            while(!this.graphics.drawImage((Image)graphicsCacheEntry2.value, destinationX, destinationY, destinationX + height2, destinationY + width2, sourceY, width - sourceX - width2, sourceY + height2, width - sourceX, (ImageObserver)null)) {
               Thread.yield();
            }

      }
   }

   public final void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
      this.graphics.drawArc(x, y, width, height, 0, 360);
   }

   public final void setClip(int x, int y, int width, int height) {
      this.graphics.setClip(x, y, width, height);
   }

   public final void drawLine(int x1, int y1, int x2, int y2) {
      this.graphics.drawLine(x1, y1, x2, y2);
   }

   public final void translate(int offsetX, int offsetY) {
      this.graphics.translate(offsetX, offsetY);
      this.translateX += offsetX;
      this.translateY += offsetY;
   }

   public final void drawString(String messageText, int x, int y) {
      this.graphics.drawString(messageText, 0, 0 + this.graphics.getFontMetrics().getHeight());
   }

   public final int getTranslateX() {
      return this.translateX;
   }

   public final int getTranslateY() {
      return this.translateY;
   }

   public final int getClipHeight() {
      return this.graphics.getClipBounds().height;
   }

   public final int getClipWidth() {
      return this.graphics.getClipBounds().width;
   }

   public final int getClipX() {
      return this.graphics.getClipBounds().x;
   }

   public final int getClipY() {
      return this.graphics.getClipBounds().y;
   }

   public final Graphics getAwtGraphics() {
      return this.graphics;
   }
}
