
package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Engine.Camera;
import Engine.MathGame;
import desktop.PlatformRuntime;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.TileMapCell;
import Moduls.Decor;
import Moduls.ImageLevelAreaDesc;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class MapBuffer {
    private Object prevCamera = null;
    private boolean dob;
    public GameImage fsrc;
    private GameGraphics tDB;
    private GameImage src1;
    private GameGraphics DB1;
    private GameImage src2;
    private GameGraphics DB2;
    public int tW;
    public int tH;
    public int sX;
    public int sY;
    public int eX;
    public int eY;
    private boolean[][] rdTiles;
    private boolean[][] rdBorders;
    private Tile tileDarkCach = null;
    private boolean markUnblockedTiles = false;

    public final void Reset() {
        this.sX = 10000;
        this.sY = 10000;
        this.eX = 11000;
        this.eY = 11000;
        this.SetRDs(true);
    }

    public final void setMarkUnblockedTiles(boolean enabled) {
        this.markUnblockedTiles = enabled;
    }

    public final boolean Create(Camera cam, Tile[] tiles) {
        this.initTilesCaches(tiles);
        int nW = (int)MathGame.ceilDivide((long)((Camera)cam).getCw(), (long)Tile.width) + 1;
        int nH = (int)MathGame.ceilDivide((long)((Camera)cam).getCh(), (long)Tile.height) + 1;
        if (nW == this.tW && nH == this.tH && this.prevCamera == cam) {
            return false;
        }
        this.prevCamera = cam;
        this.tW = nW;
        this.tH = nH;
        this.src1 = null;
        this.src2 = null;
        if (!Com.safeBuffersDisable && PlatformRuntime.getInstance().getPlatformType() == 1) {
            try {
                this.src1 = GameImage.createImage(this.tW * Tile.width, this.tH * Tile.height);
                this.src2 = GameImage.createImage(this.tW * Tile.width, this.tH * Tile.height);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            this.DB1 = this.src1.getGraphics();
            this.DB2 = this.src2.getGraphics();
            this.tDB = this.DB1;
            this.fsrc = this.src2;
            this.dob = true;
        }
        this.rdTiles = new boolean[this.tW + 2][this.tH + 2];
        this.rdBorders = new boolean[this.tW + 2][this.tH + 2];
        this.SetRDs(true);
        this.sX = 10000;
        this.sY = 10000;
        this.eX = 11000;
        this.eY = 11000;
        return true;
    }

    private void initTilesCaches(Tile[] tiles) {
        if (this.tileDarkCach == null && tiles != null && 23 < tiles.length) {
            this.tileDarkCach = tiles[23];
        }
    }

    public final void ReDrawAll(TileMap map, Tile[] tiles) {
        if (Com.safeBuffersDisable || PlatformRuntime.getInstance().getPlatformType() == 2) {
            return;
        }
        if (this.dob) {
            MapBuffer buffer = this;
            buffer.Draw(buffer.DB2, map, tiles, this.sX, this.sY, this.eX, this.eY);
            return;
        }
        MapBuffer buffer = this;
        buffer.Draw(buffer.DB1, map, tiles, this.sX, this.sY, this.eX, this.eY);
    }

    public final void TryRedraw(Camera cam, TileMap map, Tile[] tiles) {
        if (Com.safeBuffersDisable || PlatformRuntime.getInstance().getPlatformType() == 2) {
            return;
        }
        int stx = (int)MathGame.floorDivide((long)cam.getCx(), (long)Tile.width);
        int sty = (int)MathGame.floorDivide((long)cam.getCy(), (long)Tile.height);
        int etx = stx + this.tW - 1;
        int ety = sty + this.tH - 1;
        if (stx >= this.sX && etx <= this.eX && sty >= this.sY && ety <= this.eY) {
            return;
        }
        if (etx < this.sX || ety < this.sY || stx > this.eX || sty > this.eY) {
            this.sX = stx;
            this.sY = sty;
            this.eX = etx;
            this.eY = ety;
            this.ReDrawAll(map, tiles);
            return;
        }
        int dx = stx - this.sX;
        int dy = sty - this.sY;
        this.tDB.drawRegion(this.fsrc, 0, 0, this.fsrc.getWidth(), this.fsrc.getHeight(), 0, -dx * Tile.width, -dy * Tile.height);
        int xFrom = stx;
        int xCount = 0;
        int yFrom = sty;
        int yCount = 0;
        if (dx > 0) {
            xCount = dx;
            xFrom = etx - (xCount - 1);
        } else if (dx < 0) {
            xCount = -dx;
            xFrom = stx;
        }
        if (dy > 0) {
            yCount = dy;
            yFrom = ety - (yCount - 1);
        } else if (dy < 0) {
            yCount = -dy;
            yFrom = sty;
        }
        MapBuffer buffer = this;
        buffer.Draw(buffer.tDB, map, tiles, stx, sty, etx, ety, xFrom, xCount, yFrom, yCount);
        boolean useFirstBuffer = this.dob = !this.dob;
        if (this.dob) {
            this.tDB = this.DB1;
            this.fsrc = this.src2;
        } else {
            this.tDB = this.DB2;
            this.fsrc = this.src1;
        }
        this.sX = stx;
        this.sY = sty;
        this.eX = etx;
        this.eY = ety;
    }

    public final void SetRDs(boolean setVal) {
        int columnIndex = 0;
        while (columnIndex < this.tW + 2) {
            for (int rowIndex = 0; rowIndex < this.tH + 2; ++rowIndex) {
                this.rdTiles[columnIndex][rowIndex] = setVal;
                this.rdBorders[columnIndex][rowIndex] = setVal;
            }
            ++columnIndex;
        }
        return;
    }

    private void setRedrawTile(int rdX, int rdY) {
        this.rdTiles[rdX][rdY] = true;
        this.rdBorders[rdX - 1][rdY] = true;
        this.rdBorders[rdX][rdY] = true;
        this.rdBorders[rdX][rdY - 1] = true;
    }

    public final void invalidateColumn(int redrawTileInd, TileMap map) {
        if (this.sX == 10000 && this.sY == 10000) {
            return;
        }
        int stx = this.sX;
        int sty = this.sY;
        int etx = this.eX + 1;
        int ety = this.eY + 1;
        if (stx < 0) {
            stx = 0;
        }
        if (sty < 0) {
            sty = 0;
        }
        if (etx >= map.widthTl) {
            etx = map.widthTl - 1;
        }
        if (ety >= map.heightTl) {
            ety = map.heightTl - 1;
        }
        int ix = stx;
        while (ix <= etx) {
            for (int iy = sty; iy <= ety; ++iy) {
                if (map.cells[ix][iy].tileInd != redrawTileInd) continue;
                this.setRedrawTile(ix - stx + 1, iy - sty + 1);
            }
            ++ix;
        }
        return;
    }

    public final void invalidateRow(int redrawTileInd, TileMap map) {
        if (this.sX == 10000 && this.sY == 10000) {
            return;
        }
        int stx = this.sX;
        int sty = this.sY;
        int etx = this.eX + 1;
        int ety = this.eY + 1;
        if (stx < 0) {
            stx = 0;
        }
        if (sty < 0) {
            sty = 0;
        }
        if (etx >= map.widthTl) {
            etx = map.widthTl - 1;
        }
        if (ety >= map.heightTl) {
            ety = map.heightTl - 1;
        }
        int ix = stx;
        while (ix <= etx) {
            for (int iy = sty; iy <= ety; ++iy) {
                if (map.cells[ix][iy].decorTri != redrawTileInd) continue;
                this.setRedrawTile(ix - stx + 1, iy - sty + 1);
            }
            ++ix;
        }
        return;
    }

    public final void Draw(GameGraphics g, TileMap map, Tile[] tiles, int stx, int sty, int etx, int ety) {
        int height;
        int etyIndex;
        int width;
        int rdBorderIndex;
        this.initTilesCaches(tiles);
        ImageLevelAreaDesc markGreen = null;
        if (this.markUnblockedTiles) {
            markGreen = ImagesGlobal.addingAnimSprites[44];
        }
        int stx2 = stx;
        int rdTileIndex = 1;
        int cellIndex = 0;
        while (true) {
            if (stx2 > etx) break;
            rdBorderIndex = sty;
            width = 1;
            etyIndex = 0;
            while (rdBorderIndex <= ety) {
                if (this.rdTiles[rdTileIndex][width]) {
                    if (stx2 < 0 || rdBorderIndex < 0 || stx2 >= map.widthTl || rdBorderIndex >= map.heightTl) {
                        this.drawDarkTileWithBorder(g, map, stx2, cellIndex, rdBorderIndex, etyIndex);
                    } else {
                        int checkedTileY;
                        int checkedTileX;
                        TileMap checkedMap;
                        tiles[map.cells[stx2][rdBorderIndex].tileInd].Self.draw(g, cellIndex, etyIndex);
                        short decorIndex = map.cells[stx2][rdBorderIndex].decorTri;
                        height = decorIndex;
                        if (decorIndex >= 0) {
                            Decor decor = Com.Decors[height];
                            decor.Self.draw(g, cellIndex - decor.w2 + Tile.widthHalf, etyIndex - decor.Offset + Tile.heightHalf);
                        }
                        if (this.markUnblockedTiles && !(checkedMap = map).getBlockTl(checkedTileX = stx2, checkedTileY = rdBorderIndex)) {
                            markGreen.draw(g, cellIndex, etyIndex, 0);
                        }
                    }
                }
                ++rdBorderIndex;
                ++width;
                etyIndex += Tile.height;
            }
            ++stx2;
            ++rdTileIndex;
            cellIndex += Tile.width;
        }
        cellIndex = stx - 1;
        rdBorderIndex = 0;
        width = -Tile.width;
        while (cellIndex <= etx) {
            etyIndex = sty - 1;
            stx = 0;
            height = -Tile.height;
            while (etyIndex <= ety) {
                if (cellIndex >= 0 && etyIndex >= 0 && cellIndex < map.widthTl && etyIndex < map.heightTl && this.rdBorders[rdBorderIndex][stx]) {
                    TileMapCell cell = map.cells[cellIndex][etyIndex];
                    if (cell.borderV0 >= 0) {
                        ImageLevelAreaDesc borderElement = tiles[cell.borderV0].imageDescrsBorders[cell.borderV1];
                        g.drawRegion(borderElement.img, borderElement.rx, borderElement.ry, borderElement.rw, borderElement.rh, 0, width + Tile.width - Tile.BordOffset, height);
                    }
                    if (cell.borderH0 >= 0) {
                        ImageLevelAreaDesc rowBorderElement = tiles[cell.borderH0].imageDescrsBorders[cell.borderH1];
                        g.drawRegion(rowBorderElement.img, rowBorderElement.rx, rowBorderElement.ry, rowBorderElement.rw, rowBorderElement.rh, 0, width, height + Tile.height - Tile.BordOffset);
                    }
                }
                ++etyIndex;
                ++stx;
                height += Tile.height;
            }
            ++cellIndex;
            ++rdBorderIndex;
            width += Tile.width;
        }
        return;
    }

    private void drawDarkTileWithBorder(GameGraphics g, TileMap map, int ix, int tx, int iy, int ty) {
        this.tileDarkCach.Self.draw(g, tx, ty);
        if (map.moveToMapsByTileLeft == null) {
            return;
        }
        if (ix == -1) {
            if (iy >= 0 && iy < map.heightTl && map.moveToMapsByTileLeft[iy]) {
                ImagesGlobal.addingAnimSprites[41].drawVCenterRight(g, tx + Tile.width - 2, ty + Tile.heightHalf, 0);
                return;
            }
        } else if (ix == map.widthTl) {
            if (iy >= 0 && iy < map.heightTl && map.moveToMapsByTileRight[iy]) {
                ImagesGlobal.addingAnimSprites[42].drawVCenterLeft(g, tx + 2, ty + Tile.heightHalf, 0);
                return;
            }
        } else if (iy == -1) {
            if (ix >= 0 && ix < map.widthTl && map.moveToMapsByTileTop[ix]) {
                ImagesGlobal.addingAnimSprites[39].drawHCenterBottom(g, tx + Tile.widthHalf, ty + Tile.height - 2, 0);
                return;
            }
        } else if (iy == map.heightTl && ix >= 0 && ix < map.widthTl && map.moveToMapsByTileBot[ix]) {
            ImagesGlobal.addingAnimSprites[40].drawHCenterTop(g, tx + Tile.widthHalf, ty + 2, 0);
        }
    }

    private void Draw(GameGraphics g, TileMap map, Tile[] tiles, int leftTopCellX, int leftTopCellY, int rightBotCellX, int rightBotCellY, int xFrom, int xCount, int yFrom, int yCount) {
        int checkedTileY;
        int height;
        int rightBotCellYIndex;
        this.initTilesCaches(tiles);
        ImageLevelAreaDesc markGreen = null;
        if (this.markUnblockedTiles) {
            markGreen = ImagesGlobal.addingAnimSprites[44];
        }
        int xFrom2 = xFrom;
        int width = (xFrom - leftTopCellX) * Tile.width;
        while (true) {
            if (xFrom2 < xFrom + xCount) {
                rightBotCellYIndex = leftTopCellY;
                height = 0;
            } else {
                xFrom2 = xFrom - 1;
                width = (xFrom - leftTopCellX) * Tile.width - Tile.width;
                break;
            }
            while (rightBotCellYIndex <= rightBotCellY) {
                if (xFrom2 < 0 || rightBotCellYIndex < 0 || xFrom2 >= map.widthTl || rightBotCellYIndex >= map.heightTl) {
                    this.drawDarkTileWithBorder(g, map, xFrom2, width, rightBotCellYIndex, height);
                } else {
                    int checkedTileX;
                    TileMap checkedMap;
                    tiles[map.cells[xFrom2][rightBotCellYIndex].tileInd].Self.draw(g, width, height);
                    short columnDecorIndex = map.cells[xFrom2][rightBotCellYIndex].decorTri;
                    if (columnDecorIndex >= 0) {
                        Decor decor = Com.Decors[columnDecorIndex];
                        decor.Self.draw(g, width - decor.w2 + Tile.widthHalf, height - decor.Offset + Tile.heightHalf);
                    }
                    if (this.markUnblockedTiles && !(checkedMap = map).getBlockTl(checkedTileX = xFrom2, checkedTileY = rightBotCellYIndex)) {
                        markGreen.draw(g, width, height, 0);
                    }
                }
                ++rightBotCellYIndex;
                height += Tile.height;
            }
            ++xFrom2;
            width += Tile.width;
        }
        while (xFrom2 < xFrom + xCount) {
            rightBotCellYIndex = leftTopCellY - 1;
            height = -Tile.height;
            while (rightBotCellYIndex <= rightBotCellY) {
                if (xFrom2 >= 0 && rightBotCellYIndex >= 0 && xFrom2 < map.widthTl && rightBotCellYIndex < map.heightTl) {
                    ImageLevelAreaDesc borderElement;
                    TileMapCell cell = map.cells[xFrom2][rightBotCellYIndex];
                    if (cell.borderV0 >= 0) {
                        borderElement = tiles[cell.borderV0].imageDescrsBorders[cell.borderV1];
                        g.drawRegion(borderElement.img, borderElement.rx, borderElement.ry, borderElement.rw, borderElement.rh, 0, width + Tile.width - Tile.BordOffset, height);
                    }
                    if (cell.borderH0 >= 0) {
                        borderElement = tiles[cell.borderH0].imageDescrsBorders[cell.borderH1];
                        g.drawRegion(borderElement.img, borderElement.rx, borderElement.ry, borderElement.rw, borderElement.rh, 0, width, height + Tile.height - Tile.BordOffset);
                    }
                }
                ++rightBotCellYIndex;
                height += Tile.height;
            }
            ++xFrom2;
            width += Tile.width;
        }
        xFrom2 = 0;
        width = 0;
        if (xCount > 0) {
            if (xFrom == leftTopCellX) {
                xFrom2 = xCount;
            } else {
                width = xCount;
            }
        }
        rightBotCellYIndex = yFrom;
        height = (yFrom - leftTopCellY) * Tile.height;
        while (true) {
            if (rightBotCellYIndex >= yFrom + yCount) break;
            rightBotCellY = leftTopCellX + xFrom2;
            xFrom = xFrom2 * Tile.width;
            while (rightBotCellY <= rightBotCellX - width) {
                if (rightBotCellY < 0 || rightBotCellYIndex < 0 || rightBotCellY >= map.widthTl || rightBotCellYIndex >= map.heightTl) {
                    this.drawDarkTileWithBorder(g, map, rightBotCellY, xFrom, rightBotCellYIndex, height);
                } else {
                    int checkedNeighborTileX;
                    TileMap checkedRowMap;
                    tiles[map.cells[rightBotCellY][rightBotCellYIndex].tileInd].Self.draw(g, xFrom, height);
                    short rowDecorIndex = map.cells[rightBotCellY][rightBotCellYIndex].decorTri;
                    if (rowDecorIndex >= 0) {
                        Decor decor = Com.Decors[rowDecorIndex];
                        decor.Self.draw(g, xFrom - decor.w2 + Tile.widthHalf, height - decor.Offset + Tile.heightHalf);
                    }
                    if (this.markUnblockedTiles && !(checkedRowMap = map).getBlockTl(checkedNeighborTileX = rightBotCellY, checkedTileY = rightBotCellYIndex)) {
                        markGreen.draw(g, xFrom, height, 0);
                    }
                }
                ++rightBotCellY;
                xFrom += Tile.width;
            }
            ++rightBotCellYIndex;
            height += Tile.height;
        }
        rightBotCellYIndex = yFrom - 1;
        height = (yFrom - leftTopCellY) * Tile.height - Tile.height;
        while (rightBotCellYIndex < yFrom + yCount) {
            rightBotCellY = leftTopCellX - 1 + xFrom2;
            xFrom = -Tile.width + xFrom2 * Tile.width;
            while (rightBotCellY <= rightBotCellX - width) {
                if (rightBotCellY >= 0 && rightBotCellYIndex >= 0 && rightBotCellY < map.widthTl && rightBotCellYIndex < map.heightTl) {
                    ImageLevelAreaDesc rowBorderElement;
                    TileMapCell cell = map.cells[rightBotCellY][rightBotCellYIndex];
                    if (cell.borderV0 >= 0) {
                        rowBorderElement = tiles[cell.borderV0].imageDescrsBorders[cell.borderV1];
                        g.drawRegion(rowBorderElement.img, rowBorderElement.rx, rowBorderElement.ry, rowBorderElement.rw, rowBorderElement.rh, 0, xFrom + Tile.width - Tile.BordOffset, height);
                    }
                    if (cell.borderH0 >= 0) {
                        rowBorderElement = tiles[cell.borderH0].imageDescrsBorders[cell.borderH1];
                        g.drawRegion(rowBorderElement.img, rowBorderElement.rx, rowBorderElement.ry, rowBorderElement.rw, rowBorderElement.rh, 0, xFrom, height + Tile.height - Tile.BordOffset);
                    }
                }
                ++rightBotCellY;
                xFrom += Tile.width;
            }
            ++rightBotCellYIndex;
            height += Tile.height;
        }
        return;
    }
}

