package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Engine.MystFont;
import desktop.graphics.GameGraphics;

public final class DrawOrderElementStrateg extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementStrateg((short)-1, (short)-1, (short)-1));

   private DrawOrderElementStrateg(short rx, short ry, short sortInd) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      if (Com.GameTh.isHavePartyForFastFight) {
         ImagesGlobal.addingAnimSprites[14].drawCenter(gameGraphics2, Strategist.instance.cx, Strategist.instance.cy, 0);
      }

      Strategist instance2 = Strategist.instance;
      short ry2 = this.ry;
      int rx2 = this.rx;
      Strategist strategist2 = instance2;
      boolean hasPet;
      if ((hasPet = instance2.petH >= 0 && strategist2.petB >= 0) && strategist2.petY <= ry2) {
         if (strategist2.isMoveLastDirection8D2()) {
            Legionery.draw(gameGraphics2, strategist2.petX, strategist2.petY, strategist2.petB, strategist2.petH, strategist2.petDirection, 0, Com.petOffHead);
         } else {
            Legionery.draw(gameGraphics2, strategist2.petX, strategist2.petY, strategist2.petB, strategist2.petH, strategist2.petDirection, Com.petOffBody, 0);
         }
      }

      strategist2.avatarGraphic.draw(gameGraphics2, rx2, ry2, strategist2.watchDirection, strategist2.isMoveLastDirection8D() ? StrategAnimGroup.ANIMATION_STATE_WALK : StrategAnimGroup.ANIMATION_STATE_STAY, strategist2.animationTime, true);
      if (hasPet && strategist2.petY > ry2) {
         if (strategist2.isMoveLastDirection8D2()) {
            Legionery.draw(gameGraphics2, strategist2.petX, strategist2.petY, strategist2.petB, strategist2.petH, strategist2.petDirection, 0, Com.petOffHead);
         } else {
            Legionery.draw(gameGraphics2, strategist2.petX, strategist2.petY, strategist2.petB, strategist2.petH, strategist2.petDirection, Com.petOffBody, 0);
         }
      }

      if (Com.cameraWorld.isActiveLongStay()) {
         String name = MystFont.getStringLengthSpecSymb(Strategist.instance.GP_Name, Com.fontSmall) > 8 ? Strategist.instance.GP_Name.substring(0, 8) : Strategist.instance.GP_Name;
         int textWidth = Com.fontClan.getTextWidth(Strategist.instance.clanIcon);
         rx2 = Strategist.instance.cx - (MystFont.getStringLengthSpecSymb(name, Com.fontSmall) * Com.c_font_width + textWidth) / 2;
         Com.fontSmall.writeText(gameGraphics2, name, rx2 + textWidth, Strategist.instance.cy + 2);
         Com.fontClan.writeText(gameGraphics2, Strategist.instance.clanIcon, rx2, Strategist.instance.cy + 2);
      }

   }

   public final boolean isVisible() {
      return true;
   }

   public final int getRx() {
      return this.rx - Strategist.instance.avatarGraphic.basePaddingsSimetric[0];
   }

   public final int getRx2() {
      return this.rx + Strategist.instance.avatarGraphic.basePaddingsSimetric[1];
   }

   public final int getVisualFrameTop() {
      return this.ry - Strategist.instance.avatarGraphic.basePaddingsSimetric[2];
   }

   public final int getVisualFrameBottom() {
      return this.ry + Strategist.instance.avatarGraphic.basePaddingsSimetric[3];
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementStrateg((short)-1, (short)-1, (short)-1);
   }

   public final void returnInstance() {
      factory.returnInstance(this);
   }

   public static DrawOrderElementStrateg getInstance(short rx, short ry, short sortInd) {
      DrawOrderElementStrateg drawOrderElementStrateg;
      (drawOrderElementStrateg = (DrawOrderElementStrateg)factory.getInstance()).rx = rx;
      drawOrderElementStrateg.ry = ry;
      drawOrderElementStrateg.sortInd = sortInd;
      return drawOrderElementStrateg;
   }
}
