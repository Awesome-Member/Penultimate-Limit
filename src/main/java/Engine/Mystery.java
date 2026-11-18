package Engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class Mystery {
   private static Random rnd = new Random();
   private static long LastSecTime;
   private static long LastFrameTime;
   public static long FrameTimeI;
   public static long currentTimeMillis;
   public static GameImage BuffImage;
   public static GameGraphics GBuffer;
   public static int scrWidth;
   public static int scrHeight;

   public static int RandInt(int n) {
      int nextInt;
      int nextInt2;
      do {
         nextInt2 = (nextInt = rnd.nextInt() >>> 1) % n;
      } while(nextInt - nextInt2 + (n - 1) < 0);

      return nextInt2;
   }

   public static void normalizeFPS() {
      if (FrameTimeI > 250L) {
         FrameTimeI = 250L;
      }

   }

   public static void InitFPS() {
      long Tick;
      if ((Tick = System.currentTimeMillis()) < LastFrameTime) {
         Tick = LastFrameTime;
      }

      FrameTimeI = Tick - LastFrameTime;
      currentTimeMillis = Tick;
      LastFrameTime = Tick;
      if (FrameTimeI != 0L) {
         if (LastFrameTime - LastSecTime >= 1000L) {
            LastSecTime = LastFrameTime;
         }

      }
   }

   public static void initDoubleBufferLow(int width, int height) {
      scrWidth = width;
      scrHeight = height;
   }

   public static void initDoubleBuffer(int width, int height) {
      scrWidth = width;
      scrHeight = height;
      (GBuffer = (BuffImage = GameImage.createImage(width, height)).getGraphics()).setColor(263172);
      GBuffer.fillRect(0, 0, width, height);
      GBuffer.setClip(0, 0, width, height);
   }

   public static void drawBuffer(GameGraphics gameGraphics2) {
      gameGraphics2.drawImage(BuffImage, 0, 0, 0);
   }

   public static boolean isContainsStringInArray(String[] arr, String val) {
      for(int i = 0; i < arr.length; ++i) {
         String el = arr[i];
         if (val.equals(el)) {
            return true;
         }
      }

      return false;
   }

   public static int positiveModulo(int dividend, int modulus) {
      if ((dividend %= modulus) < 0) {
         dividend += modulus;
      }

      return dividend;
   }

   public static String[] cloneArray(String[] messageTexts) {
      String[] string = new String[messageTexts.length];
      int stringIndex = 0;

      for(int length = messageTexts.length; stringIndex < length; ++stringIndex) {
         string[stringIndex] = messageTexts[stringIndex];
      }

      return string;
   }

   public static Vector cloneVector(Vector vector3) {
      if (vector3 == null) {
         return null;
      } else {
         Vector vector4 = new Vector();
         Enumeration enumeration = vector3.elements();

         while(enumeration.hasMoreElements()) {
            vector4.addElement(enumeration.nextElement());
         }

         return vector4;
      }
   }

   public static Vector vectorFromArray(String[] arr) {
      Vector vector3 = new Vector();

      for(int i = 0; i < arr.length; ++i) {
         vector3.addElement(arr[i]);
      }

      return vector3;
   }

   public static Vector vectorFromString(String s) {
      if (s.length() == 0) {
         return new Vector();
      } else {
         Vector vector3 = new Vector();

         int matchOffset;
         int segmentStart;
         for(segmentStart = 0; (matchOffset = s.indexOf(124, segmentStart)) != -1; segmentStart = matchOffset + 1) {
            vector3.addElement(s.substring(segmentStart, matchOffset));
         }

         vector3.addElement(s.substring(segmentStart));
         return vector3;
      }
   }

   public static String decimalToString(long val, int zeroCount) {
      String res = "";
      boolean negative = false;
      if (val < 0L) {
         negative = true;
         val = -val;
      }

      boolean hasDigits = false;

      while(true) {
         while(val > 0L) {
            long r = val % 10L;
            val /= 10L;
            if (zeroCount > 0) {
               if (r > 0L || res.length() > 0) {
                  res = r + res;
               }

               --zeroCount;
               if (zeroCount == 0 && res.length() > 0) {
                  res = "." + res;
               }
            } else {
               res = r + res;
               hasDigits = true;
            }
         }

         if (!hasDigits) {
            res = "0" + res;
         }

         if (negative) {
            res = "-" + res;
         }

         return res;
      }
   }

   private static String processTemplate(Object template, String arg, char repChar) {
      String t = template.toString();
      int p = t.indexOf(repChar);
      return p < 0 ? t : t.substring(0, p) + arg + t.substring(p + 1);
   }

   public static String processTemplate(Object template, int arg) {
      return processTemplate(template, Integer.toString(arg), '%');
   }

   public static String stringFormat(String messageText, String arg) {
      return processTemplate((Object)messageText, arg, '%');
   }

   public static String stringFormat(String messageText, String arg, char replaceChar) {
      return processTemplate((Object)messageText, arg, replaceChar);
   }

   public static String stringFormat(String messageText, String[] args) {
      for(int i = 0; i < args.length; ++i) {
         messageText = processTemplate((Object)messageText, args[i], '%');
      }

      return messageText;
   }

   public static String stringFormat(String messageText, char repChar, String[] args) {
      for(int i = 0; i < args.length; ++i) {
         messageText = processTemplate((Object)messageText, args[i], repChar);
      }

      return messageText;
   }

   public static String stringReplace(String substring, char find, String repl) {
      int p;
      while ((p = substring.indexOf(37)) >= 0) {
         substring = substring.substring(0, p) + repl + substring.substring(p + 1);
      }
      return substring;
   }

   public static String stringReplace(String substring, String find, String repl) {
      int p;
      while((p = substring.indexOf(find)) >= 0) {
         substring = substring.substring(0, p) + repl + substring.substring(p + find.length());
      }

      return substring;
   }

   public static String floatToString(int val, int signesDivider) {
      StringBuffer stringBuffer = new StringBuffer(String.valueOf(val / 1000));
      if ((val = Math.abs(val) % 1000) > 0) {
         String ms = String.valueOf(val);

         int decimalPosition;
         for(decimalPosition = 0; ms.charAt(ms.length() - 1 - decimalPosition) == '0'; ++decimalPosition) {
         }

         if (decimalPosition > 0) {
            ms = ms.substring(0, ms.length() - decimalPosition);
         }

         stringBuffer.append(".").append(fillString(ms, '0', "1000".length() - 1 - decimalPosition));
      }

      return stringBuffer.toString();
   }

   public static String milisecondsToTime(long ms) {
      int totalSeconds;
      int seconds = (totalSeconds = (int)ms / 1000) % 60;
      int hours = totalSeconds / 3600;
      totalSeconds = (totalSeconds % 3600 - seconds) / 60;
      return (hours != 0 ? hours + "ч " : "") + (totalSeconds != 0 ? totalSeconds + "м " : "") + seconds + "с";
   }

   public static String fillString(String messageText, char fillChar, int reqLength) {
      int messageTextCount = reqLength - messageText.length();
      if (messageTextCount <= 0) {
         return messageText;
      }
      StringBuffer stringBuffer = new StringBuffer(messageTextCount);
      for (int messageTextIndex = 0; messageTextIndex < messageTextCount; ++messageTextIndex) {
         stringBuffer.append('0');
      }
      stringBuffer.append(messageText);
      return stringBuffer.toString();
   }

   public static String encodeURL(byte[] bytes) {
      StringBuffer stringBuffer = new StringBuffer();

      for(int byteIndex = 0; byteIndex < bytes.length; ++byteIndex) {
         int unsignedByte = bytes[byteIndex] & 255;
         stringBuffer.append("%");
         if (unsignedByte < 16) {
            stringBuffer.append("0");
         }

         stringBuffer.append(Integer.toHexString(unsignedByte));
      }

      return stringBuffer.toString();
   }

   public static String encodeURL(String s) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
      String ret = "";
      decompiledsupport.UncheckedDataInput.writeUTF(dataOutputStream, s);
      ByteArrayInputStream byteArrayInputStream;
      (byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).read();
      byteArrayInputStream.read();

      for(int c = byteArrayInputStream.read(); c >= 0; c = byteArrayInputStream.read()) {
         if (c == 32) {
            ret = ret + "%20";
         } else if (c <= 127) {
            ret = ret + (char)c;
         } else if (c < 128) {
            ret = appendHex(c, ret);
         } else if (c < 224) {
            ret = appendHex(c, ret);
            ret = appendHex(byteArrayInputStream.read(), ret);
         } else if (c < 240) {
            ret = appendHex(c, ret);
            ret = appendHex(byteArrayInputStream.read(), ret);
            ret = appendHex(byteArrayInputStream.read(), ret);
         }
      }

      return ret;
   }

   private static String appendHex(int arg0, String buff) {
      buff = buff + '%';
      if (arg0 < 16) {
         buff = buff + '0';
      }

      return buff + Integer.toHexString(arg0);
   }

   public static int getSpacedLinesHeight(int lineCount, int lineHeight, int lineSpacing) {
      return lineCount * (lineHeight + 1) - (lineCount > 1 ? 1 : 0);
   }

   public static long square(long operand) {
      return operand * operand;
   }

   static {
      LastFrameTime = LastSecTime = System.currentTimeMillis();
      FrameTimeI = 100L;
      scrWidth = 128;
      scrHeight = 128;
   }
   private Mystery() {
   }

}
