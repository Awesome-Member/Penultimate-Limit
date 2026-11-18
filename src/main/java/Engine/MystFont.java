package Engine;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class MystFont {
   public GameImage src;
   public int[][] LitsCoords;
   private MystFont doubleOfFont;
   public AnimSprite[] animSymbols;
   public int fontWidth;
   public int fontHeight;
   public int rx;
   public int ry;
   private int rw;
   private int rh;
   private boolean isWasAnimationStep;
   private short[] litsAccording;
   public static final Hashtable COLOR_GROUPS = new Hashtable();

   public static String colorizeText(String messageText, String colorGroupName) {
      return "Ѝ" + colorGroupName + messageText + "Џ";
   }

   public static int getStringLengthSpecSymb(String messageText, MystFont mystFont) {
      return getStringLengthSpecSymb(messageText, mystFont, mystFont == null ? null : mystFont.litsAccording);
   }

   private static int getStringLengthSpecSymb(String messageText, MystFont mystFont, short[] litsAccording2) {
      int matchOffset;
      int length;
      if (mystFont != null && litsAccording2 != null) {
         matchOffset = 0;
         char previousCharacter = ' ';
         int length2Index = 0;

         for(int length2 = messageText.length(); length2Index < length2; ++length2Index) {
            char prevChar2;
            if ((length = charToFontPosition(prevChar2 = messageText.charAt(length2Index), mystFont, previousCharacter, litsAccording2)) != -6) {
               if (length == -2) {
                  length2Index += 2;
               } else if (length == -3) {
                  length2Index += 9;
               } else if (length != -4) {
                  ++matchOffset;
               }
            }

            previousCharacter = prevChar2;
         }

         return matchOffset;
      } else {
         matchOffset = -1;

         for(length = messageText.length(); (matchOffset = messageText.indexOf(1038, matchOffset + 1)) >= 0; length -= 11) {
         }

         for(matchOffset = -1; (matchOffset = messageText.indexOf(1037, matchOffset + 1)) >= 0; length -= 4) {
         }

         return length < 0 ? 0 : length;
      }
   }

   public MystFont(MystFont mystFont, short[] litsAccording2) {
      this.doubleOfFont = null;
      this.fontWidth = 8;
      this.fontHeight = 8;
      this.rx = 0;
      this.ry = 0;
      this.rw = 0;
      this.rh = 0;
      this.isWasAnimationStep = false;
      this.litsAccording = null;
      this.src = mystFont.src;
      this.rx = mystFont.rx;
      this.ry = mystFont.ry;
      this.rw = mystFont.rw;
      this.rh = mystFont.rh;
      this.LitsCoords = mystFont.LitsCoords;
      this.animSymbols = mystFont.animSymbols;
      this.fontWidth = mystFont.fontWidth;
      this.fontHeight = mystFont.fontHeight;
      this.doubleOfFont = mystFont;
      this.litsAccording = litsAccording2;
   }

   public MystFont(String FontPic, String animRoot, int fontWidth2, int fontHeight2, boolean digit, int animFontTime, int animFontCount) {
      this(FontPic, animRoot, 8, fontHeight2, digit ? new int[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 11, 13, 88} : null, 200, animFontCount);
   }

   public MystFont(String FontPic, String animRoot, int fontWidth2, int fontHeight2, int[] specLoad, int animFontTime, int animFontCount) {
      this.doubleOfFont = null;
      this.fontWidth = 8;
      this.fontHeight = 8;
      this.rx = 0;
      this.ry = 0;
      this.rw = 0;
      this.rh = 0;
      this.isWasAnimationStep = false;
      this.litsAccording = null;

      try {
         this.src = GameImage.createImage(FontPic);
      } catch (Exception exception) {
         exception.printStackTrace();
      }

      this.init(this.src, 0, 0, this.src.getWidth(), this.src.getHeight(), fontWidth2, fontHeight2, specLoad, animRoot, animFontTime, animFontCount);
   }

   public MystFont(GameImage gameImage, int rx2, int ry2, int rw2, int rh2, int fontWidth2, int fontHeight2, int[] specLoad, String animRoot, int animFontTime, int animFontCount) {
      this.doubleOfFont = null;
      this.fontWidth = 8;
      this.fontHeight = 8;
      this.rx = 0;
      this.ry = 0;
      this.rw = 0;
      this.rh = 0;
      this.isWasAnimationStep = false;
      this.litsAccording = null;
      this.init(gameImage, rx2, ry2, rw2, rh2, 8, 8, (int[])null, animRoot, 200, 1);
   }

   private void init(GameImage gameImage, int rx2, int ry2, int rw2, int rh2, int fontWidth2, int fontHeight2, int[] specLoad, String animRoot, int animFontTime, int animFontCount) {
      this.fontWidth = fontWidth2;
      this.fontHeight = fontHeight2;
      this.src = gameImage;
      this.rx = rx2;
      this.ry = ry2;
      this.rw = rw2;
      this.rh = rh2;
      int animSymbolIndex = 0;
      if (animRoot != null) {
         animSymbolIndex = animFontCount;
      }

      this.animSymbols = new AnimSprite[animSymbolIndex];

      for(animSymbolIndex = 0; animSymbolIndex < this.animSymbols.length; ++animSymbolIndex) {
         this.animSymbols[animSymbolIndex] = new AnimSprite(fontWidth2, animFontTime, animRoot + animSymbolIndex + ".png");
      }

      if (specLoad != null) {
         this.LitsCoords = new int[200][3];

         for(rx2 = 0; rx2 < specLoad.length; ++rx2) {
            ry2 = specLoad[rx2];
            this.LitsCoords[ry2][0] = rx2 * fontWidth2;
            this.LitsCoords[ry2][1] = rx2 * fontWidth2 + fontWidth2 - 1;
            this.LitsCoords[ry2][2] = 0;
         }

      } else {
         animSymbolIndex = rw2 / fontWidth2;
         ry2 = (rx2 = rh2 / fontHeight2) * animSymbolIndex;
         this.LitsCoords = new int[ry2][3];
         ry2 = -1;

         for(rw2 = 0; rw2 < rx2; ++rw2) {
            for(rh2 = 0; rh2 < animSymbolIndex; ++rh2) {
               ++ry2;
               this.LitsCoords[ry2][0] = rh2 * fontWidth2;
               this.LitsCoords[ry2][1] = rh2 * fontWidth2 + fontWidth2 - 1;
               this.LitsCoords[ry2][2] = rw2 * fontHeight2;
            }
         }

      }
   }

   public MystFont(DataInputStream dataInputStream, int fontWidth2) {
      try {
      this.doubleOfFont = null;
      this.fontWidth = 8;
      this.fontHeight = 8;
      this.rx = 0;
      this.ry = 0;
      this.rw = 0;
      this.rh = 0;
      this.isWasAnimationStep = false;
      this.litsAccording = null;
      byte[] pb = new byte[dataInputStream.readInt()];
      dataInputStream.readFully(pb);
      GameImage gameImage = GameImage.createImage(pb, 0, pb.length);
      this.init(gameImage, 0, 0, gameImage.getWidth(), gameImage.getHeight(), 8);
   
      } catch (IOException exception) {
         MystFont.<RuntimeException>throwUnchecked(exception);
         throw new AssertionError(exception);
      }
   }

   public MystFont(GameImage gameImage, int imgx, int imgy, int imgw, int imgh, int fontWidth2) {
      this.doubleOfFont = null;
      this.fontWidth = 8;
      this.fontHeight = 8;
      this.rx = 0;
      this.ry = 0;
      this.rw = 0;
      this.rh = 0;
      this.isWasAnimationStep = false;
      this.litsAccording = null;
      this.init(gameImage, imgx, imgy, imgw, imgh, 8);
   }

   private void init(GameImage gameImage, int imgx, int imgy, int imgw, int imgh, int fontWidth2) {
      this.src = gameImage;
      this.fontWidth = fontWidth2;
      this.fontHeight = fontWidth2;
      this.rx = imgx;
      this.ry = imgy;
      this.rw = imgw;
      this.rh = imgh;
      this.LitsCoords = new int[600][3];
      boolean reachEnd = false;
      int[] cc = new int[]{-fontWidth2, 0};

      for(imgy = 33; imgy <= 58; ++imgy) {
         reachEnd = this.stepSymb(reachEnd, cc);
         this.LitsCoords[imgy][0] = cc[0];
         this.LitsCoords[imgy][1] = this.LitsCoords[imgy][0] + fontWidth2 - 1;
         this.LitsCoords[imgy][2] = cc[1];
      }

      for(imgy = 65; imgy <= 90; ++imgy) {
         reachEnd = this.stepSymb(reachEnd, cc);
         this.LitsCoords[imgy][0] = cc[0];
         this.LitsCoords[imgy][1] = this.LitsCoords[imgy][0] + fontWidth2 - 1;
         this.LitsCoords[imgy][2] = cc[1];
      }

      for(imgy = 95; imgy <= 158; ++imgy) {
         reachEnd = this.stepSymb(reachEnd, cc);
         this.LitsCoords[imgy][0] = cc[0];
         this.LitsCoords[imgy][1] = this.LitsCoords[imgy][0] + fontWidth2 - 1;
         this.LitsCoords[imgy][2] = cc[1];
      }

      for(imgy = 159; imgy < this.LitsCoords.length; ++imgy) {
         reachEnd = this.stepSymb(reachEnd, cc);
         this.LitsCoords[imgy][0] = cc[0];
         this.LitsCoords[imgy][1] = this.LitsCoords[imgy][0] + fontWidth2 - 1;
         this.LitsCoords[imgy][2] = cc[1];
      }

   }

   public final void addForAtlasing(Vector vector3) {
      vector3.addElement(this.src);
      if (this.animSymbols != null) {
         for(int i = 0; i < this.animSymbols.length; ++i) {
            vector3.addElement(this.animSymbols[i].Source);
         }
      }

   }

   private boolean stepSymb(boolean reachEnd, int[] cc) {
      if (!reachEnd) {
         cc[0] += this.fontWidth;
         if (cc[0] >= this.rw || cc[0] + this.fontWidth > this.rw) {
            cc[1] += this.fontHeight;
            cc[0] = 0;
            if (cc[1] >= this.rh || cc[1] + this.fontHeight > this.rh) {
               reachEnd = true;
               cc[1] = 0;
            }
         }
      }

      return reachEnd;
   }

   public final int getTextWidth(String messageText) {
      return this.getTextWidth(messageText, this.litsAccording);
   }

   public final int getTextWidth(String messageText, short[] litsAccording2) {
      return getStringLengthSpecSymb(messageText, this, litsAccording2) * this.fontWidth;
   }

   public static String substringSpecialString(int availSymbs, String messageText, String endSymbol) {
      StringBuffer stringBuffer = new StringBuffer();
      char[] arr = messageText.toCharArray();
      Character character = null;
      int colorNestingDepth = 0;
      int parsedSymbolCount = 0;

      for(int lengthIndex = 0; lengthIndex < arr.length; ++lengthIndex) {
         char c = arr[lengthIndex];
         if (colorNestingDepth > 0) {
            --colorNestingDepth;
         } else if (c == 1038) {
            colorNestingDepth = 9;
         } else if (c == 1037) {
            colorNestingDepth = 2;
         } else if (c != 1039) {
            ++parsedSymbolCount;
            if (parsedSymbolCount > 16) {
               character = null;
               stringBuffer.append(endSymbol);
               break;
            }

            if (parsedSymbolCount == 16) {
               character = c;
            } else {
               stringBuffer.append(c);
            }
         }
      }

      if (character != null) {
         stringBuffer.append(character);
      }

      return stringBuffer.toString();
   }

   public final void animate() {
      if (this.doubleOfFont == null) {
         this.isWasAnimationStep = false;

         for(int i = 0; i < this.animSymbols.length; ++i) {
            AnimSprite animSprite;
            (animSprite = this.animSymbols[i]).NextAnimStep();
            boolean WasFrame2 = animSprite.WasFrame;
            animSprite.WasFrame = false;
            if (WasFrame2) {
               this.isWasAnimationStep = true;
            }
         }

      }
   }

   public final boolean wasAnimationStep() {
      MystFont mystFont = this;
      while (mystFont.doubleOfFont != null) {
         mystFont = mystFont.doubleOfFont;
      }
      return mystFont.isWasAnimationStep;
   }

   private int getFontPositionWidth(int pos) {
      if (pos < 0) {
         return 0;
      } else {
         return pos >= 900 ? this.fontWidth : this.LitsCoords[pos][1] - this.LitsCoords[pos][0] + 1;
      }
   }

   public static int charToFontPositionFix(char ch, MystFont mystFont) {
      int res = charToFontPosition(ch, mystFont, ' ');
      if (res < 0) {
         res = 0;
      }
      return res;
   }

   public static int charToFontPosition(char ch, MystFont mystFont, char prevChar) {
      return charToFontPosition(ch, mystFont, prevChar, mystFont.litsAccording);
   }

   private static int charToFontPosition(char ch, MystFont mystFont, char prevChar, short[] litsAccording2) {
      int b = charToFontIndex(ch, prevChar);
      if (b < 0) {
         return b;
      }
      if (litsAccording2 != null) {
         if (b < litsAccording2.length) {
            if (litsAccording2[b] >= 0) {
               b = litsAccording2[b];
            } else if (litsAccording2[b] == -2) {
               return -6;
            }
         } else if (b < 900) {
            b = -6;
         }
      } else if (b < 900 && b >= mystFont.LitsCoords.length) {
         b = 0;
      }
      return b;
   }

   private static int charToFontIndex(char ch, char prevChar) {
      if (ch == 1025) {
         ch = 1045;
      }

      if ((ch = ch) == 1037) {
         return -2;
      } else if (ch == 1038) {
         return -3;
      } else if (ch == 1039) {
         return -4;
      } else if (ch == 13) {
         return -5;
      } else if (ch == 10) {
         return prevChar == '\r' ? -6 : -5;
      } else {
         if (ch >= 32 && ch <= 126) {
            ch -= 32;
         } else if (ch >= 1040 && ch <= 1103) {
            ch -= 945;
         } else if (ch >= 601 && ch <= 899) {
            ch -= 442;
         } else if (ch < 900 || ch > 1036) {
            if (ch == 1105) {
               ch = 132;
            } else if (ch == 8211) {
               ch = 13;
            } else {
               ch = 0;
            }
         }

         return ch;
      }
   }

   public final boolean writeText(GameGraphics gameGraphics2, String Text, int cx, int cy) {
      return this.writeText(gameGraphics2, Text, cx, cy, this.litsAccording);
   }

   public final boolean writeText(GameGraphics gameGraphics2, String Text, int cx, int cy, short[] litsAccording2) {
      return this.writeText(gameGraphics2, Text, cx, cy, -1, ' ', litsAccording2);
   }

   public final boolean writeText(GameGraphics gameGraphics2, String messageText, int cx, int cy, int limitWidth, char limitSymb) {
      return this.writeText(gameGraphics2, messageText, cx, cy, limitWidth, 'ə', this.litsAccording);
   }

   private boolean writeText(GameGraphics gameGraphics2, String messageText, int cx, int cy, int limitWidth, char limitSymb, short[] litsAccording2) {
      boolean res = false;
      int length = messageText.length();
      int cx2 = cx;
      int cy2 = cy;
      int fontHeight3 = 1 + this.fontHeight;
      int fontHeight2 = this.fontHeight;
      int previousColor = -1;
      int colorNestingDepth = -1;
      char previousCharacter = ' ';

      for(int lengthIndex = 0; lengthIndex < length; ++lengthIndex) {
         char character;
         int charAt = charToFontPosition(character = messageText.charAt(lengthIndex), this, previousCharacter, litsAccording2);
         previousCharacter = character;
         if (charAt != -6) {
            int[] col;
            if (charAt == -2) {
               previousColor = (col = (int[])COLOR_GROUPS.get(String.valueOf(messageText.charAt(lengthIndex + 1)) + messageText.charAt(lengthIndex + 2)))[0];
               cy = col[1];
               charAt = col[2];
               lengthIndex += 2;
               gameGraphics2.setColor(previousColor, cy, charAt);
            } else if (charAt == -3) {
               previousColor = Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 1)) + messageText.charAt(lengthIndex + 2) + messageText.charAt(lengthIndex + 3));
               cy = Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 4)) + messageText.charAt(lengthIndex + 5) + messageText.charAt(lengthIndex + 6));
               charAt = Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 7)) + messageText.charAt(lengthIndex + 8) + messageText.charAt(lengthIndex + 9));
               lengthIndex += 9;
               gameGraphics2.setColor(previousColor, cy, charAt);
            } else if (charAt == -4) {
               previousColor = -1;
               gameGraphics2.setColor(0);
            } else {
               ++colorNestingDepth;
               if (charAt == -5) {
                  cy2 += fontHeight3;
                  cx2 = cx;
               } else {
                  if (charAt >= 900) {
                     if (charAt - 900 > this.animSymbols.length - 1) {
                        continue;
                     }
                  } else if (charAt > this.LitsCoords.length - 1) {
                     continue;
                  }

                  cy = this.getFontPositionWidth(charAt);
                  int glyphOffsetX;
                  if (previousColor >= 0) {
                     glyphOffsetX = (byte)(colorNestingDepth == 0 ? 1 : 0);
                     gameGraphics2.fillRect(cx2 - glyphOffsetX, cy2, cy + 1 + glyphOffsetX, fontHeight2);
                  } else if (lengthIndex <= length - 2) {
                     if (messageText.charAt(lengthIndex + 1) == 1037) {
                        col = (int[])COLOR_GROUPS.get(String.valueOf(messageText.charAt(lengthIndex + 2)) + messageText.charAt(lengthIndex + 3));
                        gameGraphics2.setColor(col[0], col[1], col[2]);
                        gameGraphics2.fillRect(cx2 + cy - 1, cy2, 1, fontHeight2);
                     } else if (charAt == 1038) {
                        gameGraphics2.setColor(Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 2)) + messageText.charAt(lengthIndex + 3) + messageText.charAt(lengthIndex + 4)), Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 5)) + messageText.charAt(lengthIndex + 6) + messageText.charAt(lengthIndex + 7)), Integer.parseInt(String.valueOf(messageText.charAt(lengthIndex + 8)) + messageText.charAt(lengthIndex + 9) + messageText.charAt(lengthIndex + 10)));
                        gameGraphics2.fillRect(cx2 + cy - 1, cy2, 1, fontHeight2);
                     }
                  }

                  if (limitWidth > 0) {
                     glyphOffsetX = cy;
                     if (lengthIndex + 1 < length) {
                        glyphOffsetX = cy + this.getFontPositionWidth(charToFontPosition(messageText.charAt(lengthIndex + 1), this, character, litsAccording2));
                     }

                     if (cx2 - cx + glyphOffsetX >= limitWidth) {
                        int limitGlyphIndex;
                        if ((limitGlyphIndex = charToFontPosition(limitSymb, this, ' ', litsAccording2)) >= 0) {
                           gameGraphics2.drawRegion(this.src, this.rx + this.LitsCoords[limitGlyphIndex][0], this.ry + this.LitsCoords[limitGlyphIndex][2], cy, fontHeight2, 0, cx2, cy2);
                        }
                        break;
                     }
                  }

                  if (charAt >= 900) {
                     res = true;
                     this.animSymbols[charAt - 900].draw(gameGraphics2, cx2, cy2);
                  } else {
                     gameGraphics2.drawRegion(this.src, this.rx + this.LitsCoords[charAt][0], this.ry + this.LitsCoords[charAt][2], cy, fontHeight2, 0, cx2, cy2);
                  }

                  cx2 += cy;
               }
            }
         }
      }

      return res;
   }


   private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
      throw (T)throwable;
   }
}
