package Moduls;

import Base.NetRequest;
import Base.Com;
import Base.GameCommons;
import Engine.Camera;
import Engine.MathGame;
import Engine.MystFont;
import Engine.MystString;
import Engine.MystText;
import Engine.Mystery;
import Moduls.quests.QuestsOnMap;
import java.io.DataInputStream;
import java.util.Vector;
import desktop.graphics.GameImage;

public final class TileMap implements PathfindingGridBlockable {
   public TileMapCell[][] cells;
   public MystText[] textsInGameFormat;
   public int[][] textsCoords = new int[2][];
   public Vector textsVisible = new Vector();
   public Town[] towns;
   private int[][] modifedDecorsTempArray;
   private TileMapElement[] addingFreeDecor;
   public QuestsOnMap[] questers;
   public PathfindingGrid pathfindingGrid = null;
   public int widthTl;
   public int heightTl;
   public int widthPx;
   public int heightPx;
   public int cfg_RainChance = 1;
   public int cfg_RainWalkTime = 30000;
   public int cfg_TryRainTime = 3000;
   public int cfg_RainType;
   private int cfg_TempertureBalanse;
   public boolean[] moveToMapsZones = new boolean[4];
   public boolean[] moveToMapsByTileTop;
   public boolean[] moveToMapsByTileBot;
   public boolean[] moveToMapsByTileLeft;
   public boolean[] moveToMapsByTileRight;
   public String nameId;
   public short serverIndex = -1;
   public String nameLiteral;
   public boolean home;
   public boolean peacefull;
   public boolean battleReject;
   public GameImage miniMapBack;
   public int[] miniMapBackSize;
   private static int[][] c_Offs = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

   private boolean isInMap(int xTl, int yTl) {
      return xTl >= 0 && yTl >= 0 && xTl < this.widthTl && yTl < this.heightTl;
   }

   public final int crossWithTown(int cx, int cy, boolean ignoreActivationTime) {
      int tileX = (int)MathGame.floorDivide((long)cx, (long)Tile.width);
      int tileY = (int)MathGame.floorDivide((long)cy, (long)Tile.height);
      byte townResult = -1;

      for(int c_OffIndex = 0; c_OffIndex < 9; ++c_OffIndex) {
         int neighborTileX = tileX + c_Offs[c_OffIndex][0];
         int towns2 = tileY + c_Offs[c_OffIndex][1];
         short cells2;
         if (this.isInMap(neighborTileX, towns2) && (cells2 = this.cells[neighborTileX][towns2].townInd) >= 0) {
            Town town;
            towns2 = (town = this.towns[cells2]).dx - cx;
            int dy = town.dy - cy;
            if (Math.abs(towns2) < town.SensRad && Math.abs(dy) < town.SensRad) {
               if (town.isActivateable(this) && (ignoreActivationTime || Mystery.currentTimeMillis - town.activedAtTime >= 10000L)) {
                  return cells2;
               }

               townResult = -2;
            }
         }
      }

      return townResult;
   }

   public final int findMapMoveUnderCoord(int xPx, int yPx) {
      xPx = (int)MathGame.floorDivide((long)xPx, (long)Tile.width);
      yPx = (int)MathGame.floorDivide((long)yPx, (long)Tile.height);
      if (xPx <= 0 && this.moveToMapsZones[2] && yPx >= 0 && yPx < this.moveToMapsByTileLeft.length && this.moveToMapsByTileLeft[yPx]) {
         return 2;
      } else if (xPx >= this.widthTl - 1 && this.moveToMapsZones[0] && yPx >= 0 && yPx < this.moveToMapsByTileRight.length && this.moveToMapsByTileRight[yPx]) {
         return 0;
      } else if (yPx <= 0 && this.moveToMapsZones[3] && xPx >= 0 && xPx < this.moveToMapsByTileTop.length && this.moveToMapsByTileTop[xPx]) {
         return 3;
      } else {
         return yPx >= this.heightTl - 1 && this.moveToMapsZones[1] && xPx >= 0 && xPx < this.moveToMapsByTileBot.length && this.moveToMapsByTileBot[xPx] ? 1 : -1;
      }
   }

   public final int findTownUnderCoord(int xPx, int yPx, boolean ignoreIndirect) {
      int tileX = (int)MathGame.floorDivide((long)xPx, (long)Tile.width);
      int tileY = (int)MathGame.floorDivide((long)yPx, (long)Tile.height);
      short townUnderCoord = -1;

      for(int c_OffIndex = 0; c_OffIndex < 9; ++c_OffIndex) {
         int neighborTileX = tileX + c_Offs[c_OffIndex][0];
         int neighborTileY = tileY + c_Offs[c_OffIndex][1];
         short townUnderCoord2;
         if (this.isInMap(neighborTileX, neighborTileY) && (townUnderCoord2 = this.cells[neighborTileX][neighborTileY].townInd) >= 0 && this.towns[townUnderCoord2].isCrossCoord(xPx, yPx, true, this)) {
            townUnderCoord = townUnderCoord2;
         }
      }

      return townUnderCoord;
   }

   private static TileMapElement[] decorsComArrayTryRemove(TileMapElement[] array, TileMapElement tileMapElement) {
      int elementIndex = -1;
      int arrayIndex = 0;

      for(int length = array.length; arrayIndex < length; ++arrayIndex) {
         TileMapElement tileMapElement2;
         if ((tileMapElement2 = array[arrayIndex]).x == tileMapElement.x && tileMapElement2.y == tileMapElement.y && tileMapElement2.sprite == tileMapElement.sprite && tileMapElement2.addHeight == tileMapElement.addHeight) {
            elementIndex = arrayIndex;
            break;
         }
      }

      if (elementIndex >= 0) {
         TileMapElement[] arrayCount = new TileMapElement[array.length - 1];
         if (elementIndex > 0) {
            System.arraycopy(array, 0, arrayCount, 0, elementIndex);
         }

         if (arrayCount.length - elementIndex > 0) {
            System.arraycopy(array, elementIndex + 1, arrayCount, elementIndex, arrayCount.length - elementIndex);
         }

         return arrayCount;
      } else {
         return array;
      }
   }

   private boolean isBlockInCoordCalculate(int tx, int ty) {
      boolean blockingDecor = false;
      boolean unblockingDecor = false;
      TileMapElement[] decors2 = this.cells[tx][ty].decors;
      int decors2Index = 0;

      for(int length = decors2.length; decors2Index < length; ++decors2Index) {
         TileMapElement tileMapElement;
         if ((tileMapElement = decors2[decors2Index]).sprite >= 0) {
            if (Com.Decors[tileMapElement.sprite].GP_Block) {
               blockingDecor = true;
            }

            if (Com.Decors[tileMapElement.sprite].GP_UnBlock) {
               unblockingDecor = true;
            }
         }
      }

      return Com.Tiles[this.cells[tx][ty].tileInd].GP_Block && !unblockingDecor || blockingDecor;
   }

   public final void addDecors(int[][] addDs, boolean graphicCalculation) {
      for(int addDIndex = 0; addDIndex < addDs.length; ++addDIndex) {
         int tileX = addDs[addDIndex][0];
         int tileY = addDs[addDIndex][1];
         this.decorsClear(tileX, tileY);
         this.decorAddModifi(new TileMapElement((short)(tileX * Tile.width + Tile.width / 2), (short)(tileY * Tile.height + Tile.height / 2), (short)addDs[addDIndex][2], (short)0));
         this.cells[tileX][tileY].townInd = -1;
      }

      if (graphicCalculation) {
         GameCommons.instance.redrawMapBuffer();
         this.initMiniMap();
      }

   }

   public final void addFreeDecors(TileMapElement[] addDs) {
      for(int i = 0; i < addDs.length; ++i) {
         this.decorAddModifi(addDs[i]);
      }

      GameCommons.instance.redrawMapBuffer();
      this.initMiniMap();
   }

   private TileMapElement[] decorsClear(int tX, int tY) {
      TileMapElement[] was;
      if ((was = this.cells[tX][tY].decors).length <= 0) {
         return was;
      } else {
         this.cells[tX][tY].decors = new TileMapElement[0];
         int waIndex = 0;

         for(int length = was.length; waIndex < length; ++waIndex) {
            TileMapElement tileMapElement;
            short width = (short)((tileMapElement = was[waIndex]).x / Tile.width);
            short tileY = (short)(tileMapElement.y / Tile.height);
            this.cells[width][tileY].decorsDraw = decorsComArrayTryRemove(this.cells[width][tileY].decorsDraw, tileMapElement);
         }

         this.cells[tX][tY].block = this.isBlockInCoordCalculate(tX, tY);
         return was;
      }
   }

   private void decorAddModifi(TileMapElement tileMapElement) {
      int tileX = tileMapElement.x / Tile.width;
      int tileY = (tileMapElement.y + tileMapElement.addHeight) / Tile.height;
      TileMapElement[] tileMapElements;
      TileMapElement[] tileMapElements2 = new TileMapElement[(tileMapElements = this.cells[tileX][tileY].decors).length + 1];
      System.arraycopy(tileMapElements, 0, tileMapElements2, 0, tileMapElements.length);
      tileMapElements2[tileMapElements2.length - 1] = tileMapElement;
      this.cells[tileX][tileY].decors = tileMapElements2;
      this.cells[tileX][tileY].block = this.isBlockInCoordCalculate(tileX, tileY);
      tileX = tileMapElement.x / Tile.width;
      tileY = tileMapElement.y / Tile.height;
      tileMapElements2 = new TileMapElement[(tileMapElements = this.cells[tileX][tileY].decorsDraw).length + 1];
      System.arraycopy(tileMapElements, 0, tileMapElements2, 0, tileMapElements.length);
      tileMapElements2[tileMapElements2.length - 1] = tileMapElement;
      this.cells[tileX][tileY].decorsDraw = tileMapElements2;
   }

   public final void decorModifiNoBack(int sX, int sY, int setDecInd, short addingHeight) {
      this.decorsClear(sX, (sY * Tile.height + Tile.height / 2 + addingHeight) / Tile.height);
      this.decorAddModifi(new TileMapElement((short)(sX * Tile.width + Tile.width / 2), (short)(sY * Tile.height + Tile.height / 2), (short)setDecInd, addingHeight));
   }

   private void AddTowns() {
      for(short i = 0; i < this.towns.length; ++i) {
         this.cells[this.towns[i].cx][this.towns[i].cy].townInd = i;
         if (this.towns[i].state == 0 && this.towns[i].SafeSpriteInd >= 0) {
            this.decorModifiNoBack(this.towns[i].cx, this.towns[i].cy, this.towns[i].SafeSpriteInd, this.towns[i].addingHeight);
         }

         if (this.towns[i].state == 1) {
            this.decorModifiNoBack(this.towns[i].cx, this.towns[i].cy, this.towns[i].RuinSpriteInd, this.towns[i].addingHeight);
         }

         if (this.towns[i].state == 2) {
            this.decorsClear(this.towns[i].cx, (this.towns[i].dy + this.towns[i].addingHeight) / Tile.height);
            this.cells[this.towns[i].cx][this.towns[i].cy].townInd = -1;
         }
      }

   }

   private void AddTexts() {
      for(short i = 0; i < this.textsInGameFormat.length; this.cells[this.textsCoords[0][i]][this.textsCoords[1][i]].textInd = i++) {
      }

   }

   public final void initVisibleTexts(Camera camera) {
      int tileX = (int)MathGame.floorDivide((long)camera.getCx(), (long)Tile.width);
      int tileY = (int)MathGame.floorDivide((long)camera.getCy(), (long)Tile.height);
      int tileX2 = (int)MathGame.floorDivide((long)(camera.getCx() + camera.getCw()), (long)Tile.width);
      int tileY2 = (int)MathGame.floorDivide((long)(camera.getCy() + camera.getCh()), (long)Tile.height);
      if (tileX < 0) {
         tileX = 0;
      }

      if (tileX > this.widthTl - 1) {
         tileX = this.widthTl - 1;
      }

      if (tileY < 0) {
         tileY = 0;
      }

      if (tileY > this.heightTl - 1) {
         tileY = this.heightTl - 1;
      }

      if (tileX2 < 0) {
         tileX2 = 0;
      }

      if (tileX2 > this.widthTl - 1) {
         tileX2 = this.widthTl - 1;
      }

      if (tileY2 < 0) {
         tileY2 = 0;
      }

      if (tileY2 > this.heightTl - 1) {
         tileY2 = this.heightTl - 1;
      }

      this.textsVisible.removeAllElements();

      for(tileX = tileX; tileX <= tileX2; ++tileX) {
         for(int tileY3 = tileY; tileY3 <= tileY2; ++tileY3) {
            if (this.cells[tileX][tileY3].textInd >= 0) {
               this.textsVisible.addElement(new Integer(this.cells[tileX][tileY3].textInd));
            }
         }
      }

   }

   public final boolean getBlockTl(int cX, int cY) {
      return cX < 0 || cY < 0 || cX >= this.widthTl || cY >= this.heightTl || this.cells[cX][cY].block;
   }

   public final void initMiniMap() {
      this.miniMapBackSize = new int[2];
      this.miniMapBackSize[0] = this.widthTl << 1;
      this.miniMapBackSize[1] = this.heightTl << 1;
      this.miniMapBack = null;
   }

   public static int pxToTl(int px, int orientation2D) {
      return (int)MathGame.floorDivide((long)px, orientation2D == 0 ? (long)Tile.height : (long)Tile.width);
   }

   public final void buildPath(Vector vector3, short fromX, short fromY, short toX, short toY, short collisionWidht, short collisionHeight) {
      if (this.pathfindingGrid != null) {
         this.pathfindingGrid.buildPathPx(vector3, this, fromX, fromY, toX, toY, (short)Tile.width, (short)Tile.height, (short)(collisionWidht + 2), (short)(collisionHeight + 2));
      }
   }

   public final int getPathLengthInPixelsWithTilesPrecision(short fromXpx, short fromYpx, short toXpx, short toYpx, boolean neagativeIfUnachived) {
      return this.pathfindingGrid == null ? -1 : this.pathfindingGrid.getPathLengthInPixelsWithTilesPrecision(this, fromXpx, fromYpx, toXpx, toYpx, (short)Tile.width, (short)Tile.height, Tile.tileDiagonalLength, true);
   }

   public final void applyVirginFromMap(TileMap tileMap, boolean calcPathfind) {
      this.textsVisible = null;
      this.widthTl = tileMap.widthTl;
      this.heightTl = tileMap.heightTl;
      this.widthPx = tileMap.widthPx;
      this.heightPx = tileMap.heightPx;
      int heightTlIndex;
      int cellIndex;
      if (this.cells != null && this.cells.length == this.widthTl && this.cells[0].length == this.heightTl) {
         for(cellIndex = 0; cellIndex < this.widthTl; ++cellIndex) {
            for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
               this.cells[cellIndex][heightTlIndex].reset();
            }
         }
      } else {
         this.cells = new TileMapCell[this.widthTl][];

         for(cellIndex = 0; cellIndex < this.widthTl; ++cellIndex) {
            this.cells[cellIndex] = new TileMapCell[this.heightTl];

            for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
               this.cells[cellIndex][heightTlIndex] = new TileMapCell();
            }
         }
      }

      for(cellIndex = 0; cellIndex < this.widthTl; ++cellIndex) {
         for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
            this.cells[cellIndex][heightTlIndex].tileInd = tileMap.cells[cellIndex][heightTlIndex].tileInd;
            this.cells[cellIndex][heightTlIndex].decorTri = tileMap.cells[cellIndex][heightTlIndex].decorTri;
            this.cells[cellIndex][heightTlIndex].borderV0 = tileMap.cells[cellIndex][heightTlIndex].borderV0;
            this.cells[cellIndex][heightTlIndex].borderV1 = tileMap.cells[cellIndex][heightTlIndex].borderV1;
            this.cells[cellIndex][heightTlIndex].borderH0 = tileMap.cells[cellIndex][heightTlIndex].borderH0;
            this.cells[cellIndex][heightTlIndex].borderH1 = tileMap.cells[cellIndex][heightTlIndex].borderH1;
         }
      }

      this.pathfindingGrid = new PathfindingGrid(this.widthTl, this.heightTl);
      Vector[][] tempDrawArray = new Vector[this.widthTl][];

      for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
         tempDrawArray[heightTlIndex] = new Vector[this.heightTl];
      }

      int heightTlIndex2;
      for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
         for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
            tempDrawArray[heightTlIndex][heightTlIndex2] = new Vector();
         }
      }

      int cells2;
      for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
         for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
            TileMapElement[] decorsFrom;
            cells2 = (decorsFrom = tileMap.cells[heightTlIndex][heightTlIndex2].decors).length;
            this.cells[heightTlIndex][heightTlIndex2].decors = new TileMapElement[cells2];

            for(int decorsFromIndex = 0; decorsFromIndex < cells2; ++decorsFromIndex) {
               TileMapElement tileMapElement = decorsFrom[decorsFromIndex];
               this.cells[heightTlIndex][heightTlIndex2].decors[decorsFromIndex] = new TileMapElement(tileMapElement.x, tileMapElement.y, tileMapElement.sprite, tileMapElement.addHeight);
               tileMapElement = this.cells[heightTlIndex][heightTlIndex2].decors[decorsFromIndex];
               tempDrawArray[tileMapElement.x / Tile.width][tileMapElement.y / Tile.height].addElement(tileMapElement);
            }

            this.cells[heightTlIndex][heightTlIndex2].block = tileMap.cells[heightTlIndex][heightTlIndex2].block;
         }
      }

      for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
         for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
            this.cells[heightTlIndex][heightTlIndex2].decorsDraw = new TileMapElement[tempDrawArray[heightTlIndex][heightTlIndex2].size()];
            int cellY = 0;

            for(cells2 = this.cells[heightTlIndex][heightTlIndex2].decorsDraw.length; cellY < cells2; ++cellY) {
               this.cells[heightTlIndex][heightTlIndex2].decorsDraw[cellY] = (TileMapElement)tempDrawArray[heightTlIndex][heightTlIndex2].elementAt(cellY);
            }
         }
      }

   }

   public final void loadFromStream(NetRequest netRequest, DataInputStream dataInputStream, MystFont mystFont, int etap) {
      try {
         int integerCode;
         int moveToMapsZoneIndex;
         if (etap == 1) {
            this.textsVisible.removeAllElements();
            this.cfg_RainChance = dataInputStream.readInt();
            this.cfg_RainWalkTime = dataInputStream.readInt();
            this.cfg_TryRainTime = dataInputStream.readInt();
            this.cfg_RainType = dataInputStream.readInt();
            this.cfg_TempertureBalanse = dataInputStream.readInt();
            MystFont mystFont2 = mystFont;
            DataInputStream dataInputStream2 = dataInputStream;
            TileMap tileMap = this;
            this.textsInGameFormat = null;
            integerCode = dataInputStream.readInt();
            MystString mystString = new MystString();
            this.textsInGameFormat = new MystText[integerCode];

            int textsInGameFormatIndex;
            for(textsInGameFormatIndex = 0; textsInGameFormatIndex < integerCode; ++textsInGameFormatIndex) {
               tileMap.textsInGameFormat[textsInGameFormatIndex] = new MystText();
               String messageText = dataInputStream2.readUTF();
               mystString.Create(messageText, Com.fontSmall);
               tileMap.textsInGameFormat[textsInGameFormatIndex].create(mystString, 72, 1, mystFont2);
            }

            integerCode = dataInputStream2.readInt();
            tileMap.textsCoords[0] = new int[integerCode];
            tileMap.textsCoords[1] = new int[integerCode];

            for(textsInGameFormatIndex = 0; textsInGameFormatIndex < integerCode; ++textsInGameFormatIndex) {
               tileMap.textsCoords[0][textsInGameFormatIndex] = dataInputStream2.readInt();
               tileMap.textsCoords[1][textsInGameFormatIndex] = dataInputStream2.readInt();
            }

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.moveToMapsZones.length; ++moveToMapsZoneIndex) {
               this.moveToMapsZones[moveToMapsZoneIndex] = dataInputStream.readBoolean();
            }
         }

         int heightTlIndex;
         if (etap == 2) {
            this.widthTl = dataInputStream.readInt();
            this.heightTl = dataInputStream.readInt();
            this.widthPx = this.widthTl * Tile.width;
            this.heightPx = this.heightTl * Tile.height;
            if (this.cells != null && this.cells.length == this.widthTl && this.cells[0].length == this.heightTl) {
               for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
                  for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                     this.cells[moveToMapsZoneIndex][heightTlIndex].reset();
                  }
               }
            } else {
               this.cells = new TileMapCell[this.widthTl][];

               for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
                  this.cells[moveToMapsZoneIndex] = new TileMapCell[this.heightTl];

                  for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                     this.cells[moveToMapsZoneIndex][heightTlIndex] = new TileMapCell();
                  }
               }
            }

            dataInputStream.readInt();

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
               dataInputStream.readInt();

               for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                  this.cells[moveToMapsZoneIndex][heightTlIndex].tileInd = dataInputStream.readShort();
               }
            }

            dataInputStream.readInt();

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
               dataInputStream.readInt();

               for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                  this.cells[moveToMapsZoneIndex][heightTlIndex].decorTri = dataInputStream.readShort();
               }
            }
         }

         if (etap == 3) {
            dataInputStream.readInt();

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
               dataInputStream.readInt();

               for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                  dataInputStream.readInt();
                  this.cells[moveToMapsZoneIndex][heightTlIndex].borderV0 = dataInputStream.readShort();
                  this.cells[moveToMapsZoneIndex][heightTlIndex].borderV1 = dataInputStream.readShort();
               }
            }

            dataInputStream.readInt();

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.widthTl; ++moveToMapsZoneIndex) {
               dataInputStream.readInt();

               for(heightTlIndex = 0; heightTlIndex < this.heightTl; ++heightTlIndex) {
                  dataInputStream.readInt();
                  this.cells[moveToMapsZoneIndex][heightTlIndex].borderH0 = dataInputStream.readShort();
                  this.cells[moveToMapsZoneIndex][heightTlIndex].borderH1 = dataInputStream.readShort();
               }
            }
         }

         if (etap == 4) {
            Vector[][] tempDrawArray = new Vector[this.widthTl][];

            for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
               tempDrawArray[heightTlIndex] = new Vector[this.heightTl];
            }

            this.pathfindingGrid = new PathfindingGrid(this.widthTl, this.heightTl);

            int heightTlIndex2;
            for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
               for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
                  tempDrawArray[heightTlIndex][heightTlIndex2] = new Vector();
               }
            }

            int countIndex;
            for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
               for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
                  short count = dataInputStream.readShort();
                  this.cells[heightTlIndex][heightTlIndex2].decors = new TileMapElement[count];

                  for(countIndex = 0; countIndex < count; ++countIndex) {
                     this.cells[heightTlIndex][heightTlIndex2].decors[countIndex] = new TileMapElement(dataInputStream.readShort(), dataInputStream.readShort(), dataInputStream.readShort(), dataInputStream.readShort());
                     TileMapElement tileMapElement = this.cells[heightTlIndex][heightTlIndex2].decors[countIndex];
                     tempDrawArray[tileMapElement.x / Tile.width][tileMapElement.y / Tile.height].addElement(tileMapElement);
                  }

                  this.cells[heightTlIndex][heightTlIndex2].block = this.isBlockInCoordCalculate(heightTlIndex, heightTlIndex2);
               }
            }

            for(heightTlIndex = 0; heightTlIndex < this.widthTl; ++heightTlIndex) {
               for(heightTlIndex2 = 0; heightTlIndex2 < this.heightTl; ++heightTlIndex2) {
                  this.cells[heightTlIndex][heightTlIndex2].decorsDraw = new TileMapElement[tempDrawArray[heightTlIndex][heightTlIndex2].size()];
                  integerCode = 0;

                  for(countIndex = this.cells[heightTlIndex][heightTlIndex2].decorsDraw.length; integerCode < countIndex; ++integerCode) {
                     this.cells[heightTlIndex][heightTlIndex2].decorsDraw[integerCode] = (TileMapElement)tempDrawArray[heightTlIndex][heightTlIndex2].elementAt(integerCode);
                  }
               }
            }

            this.towns = null;
            this.towns = new Town[dataInputStream.readInt()];

            for(heightTlIndex = 0; heightTlIndex < this.towns.length; ++heightTlIndex) {
               this.towns[heightTlIndex] = new Town(heightTlIndex);
               this.towns[heightTlIndex].LoadFromStream(dataInputStream);
            }
         }

         if (etap == 16) {
            moveToMapsZoneIndex = dataInputStream.readInt();

            for(heightTlIndex = 0; heightTlIndex < moveToMapsZoneIndex; ++heightTlIndex) {
               this.towns[heightTlIndex].state = dataInputStream.readInt();
            }
         }

         if (etap == 5) {
            Com.EnsMap.Enemys = null;
            Com.EnsMap.Enemys = new Enemy[dataInputStream.readInt()];

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < Com.EnsMap.Enemys.length; ++moveToMapsZoneIndex) {
               Com.EnsMap.Enemys[moveToMapsZoneIndex] = new Enemy();
               Com.EnsMap.Enemys[moveToMapsZoneIndex].LoadFromStream(dataInputStream, true, true);
            }
         }

         if (etap == 6) {
            this.modifedDecorsTempArray = new int[netRequest.response.readInt()][];

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.modifedDecorsTempArray.length; ++moveToMapsZoneIndex) {
               this.modifedDecorsTempArray[moveToMapsZoneIndex] = new int[3];
               this.modifedDecorsTempArray[moveToMapsZoneIndex][0] = netRequest.response.readInt();
               this.modifedDecorsTempArray[moveToMapsZoneIndex][1] = netRequest.response.readInt();
               this.modifedDecorsTempArray[moveToMapsZoneIndex][2] = netRequest.response.readInt();
            }

            this.addingFreeDecor = new TileMapElement[netRequest.response.readShort()];

            for(moveToMapsZoneIndex = 0; moveToMapsZoneIndex < this.addingFreeDecor.length; ++moveToMapsZoneIndex) {
               this.addingFreeDecor[moveToMapsZoneIndex] = new TileMapElement(netRequest.response.readShort(), netRequest.response.readShort(), netRequest.response.readShort(), (short)0);
            }

            this.nameLiteral = netRequest.response.readUTF();
            this.peacefull = netRequest.response.readBoolean();
            this.home = netRequest.response.readBoolean();
            this.battleReject = netRequest.response.readBoolean();
         }

         if (etap == 7) {
            if (dataInputStream.readInt() != Com.EnsMap.Enemys.length) {
               Com.showFatalScreen("Ошибка на этапе 7.2");
            }

            for(heightTlIndex = 0; heightTlIndex < Com.EnsMap.Enemys.length; ++heightTlIndex) {
               Com.EnsMap.Enemys[heightTlIndex].Enable = dataInputStream.readBoolean();
               Com.EnsMap.Enemys[heightTlIndex].Agression = dataInputStream.readInt();
               Com.EnsMap.Enemys[heightTlIndex].Atacking = dataInputStream.readBoolean();
               Com.EnsMap.Enemys[heightTlIndex].enemySpeechStateEntries[0].position[0] = dataInputStream.readInt();
               Com.EnsMap.Enemys[heightTlIndex].enemySpeechStateEntries[0].position[1] = dataInputStream.readInt();
            }
         }

         if (etap == 18) {
            this.questers = new QuestsOnMap[dataInputStream.readShort()];
            int[] townsInds = new int[this.questers.length];

            for(heightTlIndex = 0; heightTlIndex < this.towns.length; ++heightTlIndex) {
               if (this.towns[heightTlIndex].type == 409) {
                  townsInds[this.towns[heightTlIndex].typeInd] = heightTlIndex;
               }
            }

            for(heightTlIndex = 0; heightTlIndex < this.questers.length; ++heightTlIndex) {
               this.questers[heightTlIndex] = new QuestsOnMap(dataInputStream, townsInds[heightTlIndex]);
            }

            this.moveToMapsByTileTop = new boolean[this.widthTl];

            for(heightTlIndex = 0; heightTlIndex < this.moveToMapsByTileTop.length; ++heightTlIndex) {
               this.moveToMapsByTileTop[heightTlIndex] = dataInputStream.readBoolean();
            }

            this.moveToMapsByTileBot = new boolean[this.widthTl];

            for(heightTlIndex = 0; heightTlIndex < this.moveToMapsByTileBot.length; ++heightTlIndex) {
               this.moveToMapsByTileBot[heightTlIndex] = dataInputStream.readBoolean();
            }

            this.moveToMapsByTileLeft = new boolean[this.heightTl];

            for(heightTlIndex = 0; heightTlIndex < this.moveToMapsByTileLeft.length; ++heightTlIndex) {
               this.moveToMapsByTileLeft[heightTlIndex] = dataInputStream.readBoolean();
            }

            this.moveToMapsByTileRight = new boolean[this.heightTl];

            for(heightTlIndex = 0; heightTlIndex < this.moveToMapsByTileRight.length; ++heightTlIndex) {
               this.moveToMapsByTileRight[heightTlIndex] = dataInputStream.readBoolean();
            }
         }

      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public final void initAfterLoad() {
      EnemysMap enemysMap;
      (enemysMap = Com.EnsMap).Width = this.widthPx / 660 + 1;
      enemysMap.Height = this.heightPx / 660 + 1;
      enemysMap.SlotW = this.widthPx / enemysMap.Width;
      enemysMap.SlotH = this.heightPx / enemysMap.Height;
      enemysMap.Map = new Vector[enemysMap.Width][enemysMap.Height];

      for(int mapIndex = 0; mapIndex < enemysMap.Width; ++mapIndex) {
         for(int heightIndex = 0; heightIndex < enemysMap.Height; ++heightIndex) {
            enemysMap.Map[mapIndex][heightIndex] = new Vector();
         }
      }

      Com.EnsMap.AssociateEnemys();
      this.AddTexts();
      this.AddTowns();
      this.addDecors(this.modifedDecorsTempArray, true);
      this.addFreeDecors(this.addingFreeDecor);
      this.modifedDecorsTempArray = null;
      this.initMiniMap();
   }

   public final void fillDecorsDrawOrder(Camera camera, DrawOrderList drawOrderList2, DrawOrderList drawOrderList3) {
      int tileX = (int)MathGame.floorDivide((long)(camera.getCx() - Com.decorMaxRight), (long)Tile.width);
      int tileY = (int)MathGame.floorDivide((long)(camera.getCy() - Com.decorMaxBottom), (long)Tile.height);
      int tileX2 = (int)MathGame.floorDivide((long)(camera.getCx() + camera.getCw() + Com.decorMaxLeft), (long)Tile.width);
      int tileY2 = (int)MathGame.floorDivide((long)(camera.getCy() + camera.getCh() + Com.decorMaxTop), (long)Tile.height);
      if (tileX < 0) {
         tileX = 0;
      }

      if (tileX > this.widthTl - 1) {
         tileX = this.widthTl - 1;
      }

      if (tileY < 0) {
         tileY = 0;
      }

      if (tileY > this.heightTl - 1) {
         tileY = this.heightTl - 1;
      }

      if (tileX2 < 0) {
         tileX2 = 0;
      }

      if (tileX2 > this.widthTl - 1) {
         tileX2 = this.widthTl - 1;
      }

      if (tileY2 < 0) {
         tileY2 = 0;
      }

      if (tileY2 > this.heightTl - 1) {
         tileY2 = this.heightTl - 1;
      }

      for(int tileY3 = tileY; tileY3 <= tileY2; ++tileY3) {
         for(int cellIndex = tileX; cellIndex <= tileX2; ++cellIndex) {
            TileMapElement[] decorsDraw2 = this.cells[cellIndex][tileY3].decorsDraw;
            int decorsDraw2Index = 0;

            for(int length = decorsDraw2.length; decorsDraw2Index < length; ++decorsDraw2Index) {
               TileMapElement tileMapElement;
               if ((tileMapElement = decorsDraw2[decorsDraw2Index]).sprite >= 0) {
                  Decor decor2 = Com.Decors[tileMapElement.sprite];
                  DrawOrderElementDecor drawOrderElementDecor = DrawOrderElementDecor.getInstance(tileMapElement.x, tileMapElement.y, (short)(tileMapElement.y + tileMapElement.addHeight + decor2.EncloseCoord), decor2, false);
                  drawOrderList2.addAndSortFromEnd2(drawOrderElementDecor);
                  if (decor2.imageLevelAreaDesc != null) {
                     boolean needTransporant = false;

                     for(DrawOrderElement first2 = drawOrderList3.first; first2 != null && !first2.isGreater((DrawOrderElement)drawOrderElementDecor); first2 = first2.next) {
                        if (first2.isVisible() && first2.getRx2() >= drawOrderElementDecor.getRx() && first2.getRx() <= drawOrderElementDecor.getRx2() && first2.getVisualFrameBottom() >= drawOrderElementDecor.getVisualFrameTop() && first2.getVisualFrameTop() <= drawOrderElementDecor.getVisualFrameBottom()) {
                           needTransporant = true;
                           break;
                        }
                     }

                     if (needTransporant) {
                        drawOrderElementDecor.setDrawTransporant(true);
                     }
                  }
               }
            }

            short cells2;
            DrawOrderElement[] addDraw;
            if ((cells2 = this.cells[cellIndex][tileY3].townInd) >= 0 && (addDraw = this.towns[cells2].getAddDecorToMap(this)) != null) {
               for(decorsDraw2Index = 0; decorsDraw2Index < addDraw.length; ++decorsDraw2Index) {
                  drawOrderList2.addAndSortFromEnd2(addDraw[decorsDraw2Index]);
               }
            }
         }
      }

   }

   public final boolean isBlockTl(short x, short y) {
      return this.getBlockTl((int)x, (int)y);
   }

   public final boolean isPathCellBlocked(short pixelX, short pixelY) {
      return this.getBlockTl((int)MathGame.floorDivide((long)pixelX, (long)Tile.width), (int)MathGame.floorDivide((long)pixelY, (long)Tile.height));
   }
}
