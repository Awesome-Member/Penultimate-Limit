
package Moduls;

import Base.ImagesGlobal;
import Base.NetRequest;
import Base.Com;
import Moduls.StrategGraphic;
import Moduls.Tile;
import Moduls.Body;
import Moduls.Head;
import Moduls.ImageLevelAreaDesc;
import desktop.graphics.GameGraphics;

public final class Legionery {
    private String Name;
    private String Info;
    private int BodyInd;
    private boolean[] Abbils = new boolean[4];
    private int[] Params = new int[18];
    private static Legionery[] c_Legioners;
    private static int c_MaxLegioners;

    public static void LoadTypes(NetRequest request) {
        try {
            c_MaxLegioners = request.response.readInt();
            c_Legioners = new Legionery[c_MaxLegioners];
            int unitIndex = 0;
            while (true) {
                int parameterIndex;
                if (unitIndex >= c_MaxLegioners) {
                    return;
                }
                Com.PrBarTh.setText("\u042e\u043d\u0438\u0442\r" + unitIndex + "/" + c_MaxLegioners);
                Legionery.c_Legioners[unitIndex] = new Legionery();
                Legionery.c_Legioners[unitIndex].Name = request.response.readUTF();
                Legionery.c_Legioners[unitIndex].Info = request.response.readUTF();
                Legionery.c_Legioners[unitIndex].BodyInd = request.response.readInt();
                for (parameterIndex = 0; parameterIndex < Legionery.c_Legioners[unitIndex].Params.length; ++parameterIndex) {
                    Legionery.c_Legioners[unitIndex].Params[parameterIndex] = request.response.readInt();
                }
                for (parameterIndex = 0; parameterIndex < Legionery.c_Legioners[unitIndex].Abbils.length; ++parameterIndex) {
                    Legionery.c_Legioners[unitIndex].Abbils[parameterIndex] = request.response.readBoolean();
                }
                ++unitIndex;
            }
        }
        catch (Exception exception) {
            Exception loadError = exception;
            exception.printStackTrace();
            return;
        }
    }

    public static boolean isPixelTransporant(int offX, int offY, int body, int head, int directionStrGraph, int addBodyY, int addHeadY, boolean withShadow) {
        if (body >= 0) {
            Body bod = Com.Bodys[body];
            addBodyY = 0 + bod.verticalOffset;
            Head headObj = Com.Heads[head];
            if (directionStrGraph == 1) {
                if (!bod.imgDesc.isTransporant(-bod.offX + offX, -bod.offY + offY - addBodyY, 0, false)) {
                    return false;
                }
                if (!headObj.imgDescLeft.isTransporant(offX - bod.HDX0 - headObj.LDXO, offY - bod.HDY0 - headObj.LDYO - addBodyY, 0, false)) {
                    return false;
                }
            } else {
                ImageLevelAreaDesc sprite = bod.imgDesc;
                if (!sprite.isTransporant(offX + (sprite.frW + bod.offX), offY - bod.offY - addBodyY, 0, true)) {
                    return false;
                }
                if (!headObj.imgDescLeft.isTransporant(offX + bod.HDX0 + (headObj.imgDescLeft.frW + headObj.LDXO), offY - bod.HDY0 - headObj.LDYO - addBodyY, 0, true)) {
                    return false;
                }
            }
        } else {
            Head headObj = Com.Heads[head];
            if (directionStrGraph == 1) {
                if (!headObj.imgDescLeft.isTransporant(offX - headObj.LDXO, offY - headObj.LDYO, 0, false)) {
                    return false;
                }
            } else {
                ImageLevelAreaDesc sprite = headObj.imgDescLeft;
                if (!sprite.isTransporant(offX + (sprite.frW + headObj.LDXO), offY - headObj.LDYO, 0, true)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void draw(GameGraphics g, int dx, int dy, int body, int head, int directionStrGraph, int addBodyY, int addHeadY) {
        if (body >= 0) {
            Body bod = Com.Bodys[body];
            ImagesGlobal.addingAnimSprites[bod.shadowInd].drawCenter(g, dx, dy, 0);
            int addY = 0;
            if (bod.jumping) {
                addY = addBodyY + 0;
            }
            addY += bod.verticalOffset;
            if (directionStrGraph != 1) {
                bod.imgDesc.draw(g, dx - (bod.imgDesc.frW + bod.offX), dy + bod.offY + addY, 2);
                Head headObj = Com.Heads[head];
                headObj.imgDescLeft.draw(g, dx - bod.HDX0 - (headObj.imgDescLeft.frW + headObj.LDXO), dy + bod.HDY0 + headObj.LDYO + addHeadY + addY, 2);
                return;
            }
            bod.imgDesc.draw(g, dx + bod.offX, dy + bod.offY + addY, 0);
            Com.Heads[head].imgDescLeft.draw(g, dx + bod.HDX0 + Com.Heads[head].LDXO, dy + bod.HDY0 + Com.Heads[head].LDYO + addHeadY + addY, 0);
        } else {
            ImagesGlobal.addingAnimSprites[2].drawCenter(g, dx, dy, 0);
            Head headObj = Com.Heads[head];
            if (directionStrGraph == 1) {
                headObj.imgDescLeft.draw(g, dx + headObj.LDXO, dy + headObj.LDYO + addHeadY + addBodyY, 0);
                return;
            }
            headObj.imgDescLeft.draw(g, dx - (headObj.imgDescLeft.frW + headObj.LDXO), dy + headObj.LDYO + addHeadY + addBodyY, 2);
        }
    }

    public static int getUnitInfoVerticalOffset(int drawType, int bodyInd, int headInd, StrategGraphic strategGraphic) {
        if (drawType == 1) {
            return -strategGraphic.basePaddingsNoAura[2];
        }
        return Math.min((bodyInd >= 0 ? Com.Bodys[bodyInd].HDY0 : 0) + (headInd >= 0 ? Com.Heads[headInd].LDYO : 0), bodyInd >= 0 ? Com.Bodys[bodyInd].offY + Com.Bodys[bodyInd].verticalOffset : 0);
    }

    public static int getUnitHorizHalf(int drawType, int bodyInd, int headInd, StrategGraphic strategGraphic) {
        if (drawType == 1) {
            return -Math.max(strategGraphic.basePaddingsNoAura[0], strategGraphic.basePaddingsNoAura[1]);
        }
        return -Math.min((bodyInd >= 0 ? Com.Bodys[bodyInd].HDX0 : 0) + (headInd >= 0 ? Math.min(Com.Heads[headInd].LDXO, -(Com.Heads[headInd].imgDescLeft.frW + Com.Heads[headInd].LDXO)) : 0), bodyInd >= 0 ? Com.Bodys[bodyInd].offX : 0);
    }

    public static int[] getPaddings(int bodyInd, int headInd, int direct) {
        int left = Tile.width2_6;
        int right = Tile.width2_6;
        int top = Tile.height2_3;
        int bottom = 0;
        int shadowInd = 2;
        int headOffX = 0;
        int headOffY = 0;
        if (bodyInd >= 0) {
            Body bod = Com.Bodys[bodyInd];
            left = direct == 1 ? Math.max(left, -bod.offX) : Math.max(left, bod.imgDesc.frW + bod.offX);
            right = direct == 1 ? Math.max(right, bod.imgDesc.frW + bod.offX) : Math.max(right, -bod.offX);
            top = Math.max(top, -(bod.offY + bod.verticalOffset));
            bottom = Math.max(0, bod.imgDesc.rh + (bod.offY + bod.verticalOffset));
            shadowInd = bod.shadowInd;
            headOffX = direct == 1 ? bod.HDX0 : -bod.HDX0;
            headOffY = bod.HDY0 + bod.verticalOffset;
        }
        Object shadowOrHead = ImagesGlobal.addingAnimSprites[shadowInd];
        left = Math.max(left, ((ImageLevelAreaDesc)shadowOrHead).centerOffX);
        right = Math.max(right, ((ImageLevelAreaDesc)shadowOrHead).centerOffX);
        top = Math.max(top, ((ImageLevelAreaDesc)shadowOrHead).centerOffY);
        bottom = Math.max(bottom, ((ImageLevelAreaDesc)shadowOrHead).centerOffY);
        if (headInd >= 0) {
            shadowOrHead = Com.Heads[headInd];
            left = Math.max(left, -(headOffX + (direct == 1 ? ((Head)shadowOrHead).LDXO : -(((Head)shadowOrHead).imgDescLeft.frW + ((Head)shadowOrHead).LDXO))));
            right = Math.max(right, ((Head)shadowOrHead).imgDescLeft.frW + (headOffX + (direct == 1 ? ((Head)shadowOrHead).LDXO : -(((Head)shadowOrHead).imgDescLeft.frW + ((Head)shadowOrHead).LDXO))));
            top = Math.max(top, -(headOffY + ((Head)shadowOrHead).LDYO));
            bottom = Math.max(bottom, ((Head)shadowOrHead).imgDescLeft.rh + (headOffY + ((Head)shadowOrHead).LDYO));
        }
        return new int[]{left, right, top, bottom};
    }

    static {
        c_MaxLegioners = 12;
    }
}

