package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestsOnMap;
import java.io.DataInputStream;
import java.io.IOException;

public final class Town {
   public int cx;
   public int cy;
   public short dx;
   public short dy;
   public int SafeSpriteInd;
   public int RuinSpriteInd;
   public int state;
   private String Name;
   public String GPFile;
   public int type;
   public int typeInd;
   public String typeIdName;
   public long activedAtTime;
   public int SensRad;
   private int gameID;
   public short addingHeight = 0;
   private int index;
   public boolean calcCompasUnderFit = false;
   public Object additional = null;

   public Town(int townIndex) {
      this.index = townIndex;
   }

   public final void LoadFromStream(DataInputStream dataInputStream) {
      try {
         this.cx = dataInputStream.readShort();
         this.cy = dataInputStream.readShort();
         this.SafeSpriteInd = dataInputStream.readShort();
         this.RuinSpriteInd = dataInputStream.readShort();
         this.addingHeight = dataInputStream.readShort();
         this.SensRad = dataInputStream.readShort();
         this.gameID = dataInputStream.readInt();
         this.type = dataInputStream.readShort();
         this.typeInd = dataInputStream.readShort();
         this.typeIdName = dataInputStream.readUTF();
         this.state = dataInputStream.readShort();
         this.Name = dataInputStream.readUTF();
         this.GPFile = dataInputStream.readUTF();
         switch (this.type) {
            case 1:
               TownSpriteHeight townSpriteHeight;
               (townSpriteHeight = new TownSpriteHeight()).heightOverrideEnabled = dataInputStream.readBoolean();
               townSpriteHeight.height = dataInputStream.readShort();
               this.additional = townSpriteHeight;
               break;
            case 402:
               TownSpriteWidth townSpriteWidth;
               (townSpriteWidth = new TownSpriteWidth()).widthOverrideEnabled = dataInputStream.readBoolean();
               townSpriteWidth.width = dataInputStream.readShort();
               this.additional = townSpriteWidth;
               break;
            case 404:
            case 405:
               TownAvailabilityFlag townAvailabilityFlag;
               (townAvailabilityFlag = new TownAvailabilityFlag()).available = dataInputStream.readBoolean();
               this.additional = townAvailabilityFlag;
               break;
            case 406:
               TownAnimationFrames townAnimationFrames;
               (townAnimationFrames = new TownAnimationFrames()).frames = new int[dataInputStream.readInt()][];

               for(int i = 0; i < townAnimationFrames.frames.length; ++i) {
                  townAnimationFrames.frames[i] = new int[3];
                  townAnimationFrames.frames[i][0] = dataInputStream.readInt();
                  townAnimationFrames.frames[i][1] = dataInputStream.readInt();
                  townAnimationFrames.frames[i][2] = dataInputStream.readInt();
               }

               this.additional = townAnimationFrames;
         }

         this.calcCompasUnderFit = false;
         if (this.SafeSpriteInd >= 0) {
            Decor decor2;
            if ((decor2 = Com.Decors[this.SafeSpriteInd]).Self.sh <= 1 && decor2.Self.sw <= 1) {
               this.calcCompasUnderFit = true;
            }
         } else {
            this.calcCompasUnderFit = true;
         }

         this.dx = (short)(this.cx * Tile.width + Tile.widthHalf);
         this.dy = (short)(this.cy * Tile.height + Tile.heightHalf);
      } catch (IOException exception) {
         exception.printStackTrace();
      }
   }

   public final DrawOrderElement[] getAddDecorToMap(TileMap tileMap) {
      DrawOrderElement[] res = null;
      if (this.state == 0 && this.calcCompasUnderFit) {
         boolean inWays = false;
         QuestInPlayer questInPlayer;
         if ((questInPlayer = QuestStrateg.instance.getCompassedQuest()) != null && (Strategist.instance.compassMode & 2) != 0) {
            for(int i = 0; i < questInPlayer.calcWaysTowns.length; ++i) {
               if (questInPlayer.calcWaysTowns[i] == this.index) {
                  inWays = true;
                  break;
               }
            }
         }

         if (inWays && Com.cameraWorld.isPointVisible(this.dx, this.dy)) {
            ImageLevelAreaDesc imageLevelAreaDesc2 = ImagesGlobal.addingAnimSprites[26];
            res = new DrawOrderElement[]{DrawOrderElementImage.getInstance(this.dx, (short)(this.dy + imageLevelAreaDesc2.rh / 2), (short)(this.dy + this.addingHeight - imageLevelAreaDesc2.rh / 2 - 1), imageLevelAreaDesc2)};
         }
      }

      DrawOrderElementImage drawOrderElementImage;
      switch (this.type) {
         case 1:
            if (!((TownSpriteHeight)this.additional).heightOverrideEnabled) {
               drawOrderElementImage = DrawOrderElementImage.getInstance(this.dx, (short)(this.dy - ((TownSpriteHeight)this.additional).height), (short)(this.dy + this.addingHeight), ImagesGlobal.addingAnimSprites[31]);
               res = addDrawOrderElement(res, drawOrderElementImage);
            }
            break;
         case 402:
            if (!((TownSpriteWidth)this.additional).widthOverrideEnabled) {
               drawOrderElementImage = DrawOrderElementImage.getInstance(this.dx, (short)(this.dy - ((TownSpriteWidth)this.additional).width), (short)(this.dy + this.addingHeight), ImagesGlobal.addingAnimSprites[32]);
               res = addDrawOrderElement(res, drawOrderElementImage);
            }
            break;
         case 409:
            QuestsOnMap questsOnMap;
            if ((questsOnMap = tileMap.questers[this.typeInd]).drawSignType != 0) {
               ImageLevelAreaDesc imageLevelAreaDesc3;
               switch (questsOnMap.drawSignType) {
                  case 1:
                     imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[30];
                     break;
                  case 2:
                     imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[27];
                     break;
                  case 3:
                     imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[29];
                     break;
                  case 4:
                     imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[28];
                     break;
                  default:
                     imageLevelAreaDesc3 = ImagesGlobal.addingAnimSprites[30];
               }

               DrawOrderElementImage drawOrderElementImage2 = DrawOrderElementImage.getInstance(this.dx, (short)(this.dy - questsOnMap.indicatorHeight), (short)(this.dy + this.addingHeight), imageLevelAreaDesc3);
               res = addDrawOrderElement(res, drawOrderElementImage2);
            }
      }

      return res;
   }

   private static DrawOrderElement[] addDrawOrderElement(DrawOrderElement[] drawOrderElement2, DrawOrderElement drawOrderElement) {
      if (drawOrderElement2 == null) {
         drawOrderElement2 = new DrawOrderElement[]{drawOrderElement};
      } else {
         DrawOrderElement[] drawOrderElements = drawOrderElement2;
         drawOrderElement2 = new DrawOrderElement[drawOrderElement2.length + 1];
         System.arraycopy(drawOrderElements, 0, drawOrderElement2, 0, drawOrderElements.length);
         drawOrderElement2[drawOrderElements.length] = drawOrderElement;
      }

      return drawOrderElement2;
   }

   public final boolean isActivateable(TileMap tileMap) {
      return this.state == 0 && (this.type != 409 || tileMap.questers[this.typeInd].questsStateCach != 0);
   }

   public final boolean isCrossCoord(int xPx, int yPx, boolean ignoreIndirect, TileMap tileMap) {
      if (!this.isActivateable(tileMap)) {
         return false;
      } else if (ignoreIndirect && (this.type == 407 || this.type == 408 || this.type == 404 && ((TownAvailabilityFlag)this.additional).available || this.type == 405 && ((TownAvailabilityFlag)this.additional).available)) {
         return false;
      } else {
         int[] paddings = this.getPaddings(tileMap);
         return xPx >= this.dx - paddings[0] && xPx <= this.dx + paddings[1] && yPx >= this.dy + this.addingHeight - paddings[2] && yPx <= this.dy + this.addingHeight + paddings[3];
      }
   }

   public final int[] getPaddings(TileMap tileMap) {
      int width2_62 = Tile.width2_6;
      int width2_63 = Tile.width2_6;
      int maximum = 0;
      int maximum2 = 0;
      Decor decor2;
      if (this.SafeSpriteInd >= 0 && this.state == 0) {
         decor2 = Com.Decors[this.SafeSpriteInd];
         width2_62 = Math.max(width2_62, decor2.w2);
         width2_63 = Math.max(width2_63, decor2.width - decor2.w2);
         maximum = Math.max(0, decor2.Offset);
         maximum2 = Math.max(0, decor2.height - decor2.Offset);
      }

      if (this.RuinSpriteInd >= 0 && this.state == 1) {
         decor2 = Com.Decors[this.RuinSpriteInd];
         width2_62 = Math.max(width2_62, decor2.w2);
         width2_63 = Math.max(width2_63, decor2.width - decor2.w2);
         maximum = Math.max(maximum, decor2.Offset);
         maximum2 = Math.max(maximum2, decor2.height - decor2.Offset);
      }

      if (width2_62 + width2_63 < Tile.widthHalf) {
         switch (this.type) {
            case 402:
            case 403:
            case 409:
               width2_62 = Tile.widthHalf;
               width2_63 = Tile.widthHalf;
               break;
            default:
               width2_62 = Tile.width2_6;
               width2_63 = Tile.width2_6;
         }
      }

      if (maximum + maximum2 < Tile.heightHalf) {
         switch (this.type) {
            case 1:
               maximum = Math.max(maximum, ((TownSpriteHeight)this.additional).height);
               maximum2 = 0;
               break;
            case 402:
               maximum = Math.max(maximum, ((TownSpriteWidth)this.additional).width);
               maximum2 = 0;
               break;
            case 403:
               maximum = Tile.heightHalf;
               maximum2 = Tile.heightHalf;
               break;
            case 409:
               maximum = Math.max(maximum, tileMap.questers[this.typeInd].indicatorHeight);
               maximum2 = 0;
               break;
            default:
               maximum = Tile.heightHalf;
               maximum2 = Tile.heightHalf / 2;
         }
      }

      if (maximum + maximum2 < Tile.heightHalf) {
         maximum = Tile.heightHalf;
         maximum2 = Tile.heightHalf;
      }

      return new int[]{width2_62, width2_63, maximum, maximum2};
   }
}
