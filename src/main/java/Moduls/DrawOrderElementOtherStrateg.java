package Moduls;

import Base.ImagesGlobal;
import Base.Com;
import Engine.MystFont;
import Engine.Mystery;
import Moduls.otherheroes.HeroOnMap;
import Moduls.otherheroes.HeroesOnMapManager;
import battlepck.Battle;
import desktop.graphics.GameGraphics;

public final class DrawOrderElementOtherStrateg extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new DrawOrderElementOtherStrateg((short)-1, (short)-1, (short)-1, -1));
   public int ind = -1;

   private DrawOrderElementOtherStrateg(short rx, short ry, short sortInd, int ind2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      if (HeroesOnMapManager.instance.forMapInd == Com.currentGameMap.serverIndex) {
         HeroOnMap heroOnMap;
         if ((heroOnMap = (HeroOnMap)HeroesOnMapManager.instance.heroes.elementAt(this.ind)) != null) {
            int cx2 = heroOnMap.cx;
            int cy2 = heroOnMap.cy;
            int petB2 = heroOnMap.petB;
            int petH2 = heroOnMap.petH;
            int unitInfoVerticalOffset;
            int maxHeight;
            int petDirection;
            if (petB2 >= 0 && petH2 >= 0) {
               unitInfoVerticalOffset = heroOnMap.petX;
               maxHeight = heroOnMap.petY;
               petDirection = heroOnMap.petDirect;
               if (unitInfoVerticalOffset >= 0 && maxHeight <= cy2) {
                  if (heroOnMap.animationState < StrategAnimGroup.ANIMATION_STATE_WALK) {
                     Legionery.draw(gameGraphics2, unitInfoVerticalOffset, maxHeight, petB2, petH2, petDirection, Com.petOffBody, 0);
                  } else {
                     Legionery.draw(gameGraphics2, unitInfoVerticalOffset, maxHeight, petB2, petH2, petDirection, 0, Com.petOffHead);
                  }
               }
            }

            if ((heroOnMap.addHighlighFlags & 4) != 0) {
               ImagesGlobal.addingAnimSprites[14].drawCenter(gameGraphics2, cx2, cy2, 0);
            }

            if (heroOnMap.swords != 0 && heroOnMap.fightingWithMobInd >= 0 && heroOnMap.fightingWithMobInd < Com.EnsMap.Enemys.length) {
               Enemy enemy2 = Com.EnsMap.Enemys[heroOnMap.fightingWithMobInd];
               maxHeight = (int)((Mystery.currentTimeMillis - heroOnMap.visibleSinceTime) % (long)(Battle.c_all_apply_atacks_time << 2));
               if (enemy2.Enable) {
                  if (maxHeight < Battle.c_all_apply_atacks_time) {
                     heroOnMap.strategGraphic.draw(gameGraphics2, cx2, cy2, heroOnMap.watchDirection, StrategAnimGroup.ANIMATION_STATE_ATACK, (long)maxHeight, false, (heroOnMap.addHighlighFlags & 2) != 0);
                  } else {
                     heroOnMap.strategGraphic.draw(gameGraphics2, cx2, cy2, heroOnMap.watchDirection, heroOnMap.animationState, heroOnMap.animationTime, true, (heroOnMap.addHighlighFlags & 2) != 0);
                  }
               } else {
                  heroOnMap.strategGraphic.draw(gameGraphics2, cx2, cy2, heroOnMap.watchDirection, heroOnMap.animationState, heroOnMap.animationTime, true, (heroOnMap.addHighlighFlags & 2) != 0);
               }
            } else {
               heroOnMap.strategGraphic.draw(gameGraphics2, cx2, cy2, heroOnMap.watchDirection, heroOnMap.animationState, heroOnMap.animationTime, true, (heroOnMap.addHighlighFlags & 2) != 0);
            }

            unitInfoVerticalOffset = Legionery.getUnitInfoVerticalOffset(1, -1, -1, heroOnMap.strategGraphic);
            if (heroOnMap.swords != 0) {
               byte combatIconOffsetX = 0;
               if (Com.cameraWorld.isActiveLongStay()) {
                  combatIconOffsetX = 4;
               }

               if (heroOnMap.swords == 1) {
                  ImagesGlobal.addingAnimSprites[25].drawHCenterBottom(gameGraphics2, cx2 + 3 + combatIconOffsetX, cy2 + unitInfoVerticalOffset - 7 - 2 + 11, 0);
               } else {
                  ImagesGlobal.addingAnimSprites[24].drawHCenterBottom(gameGraphics2, cx2 + 3 + combatIconOffsetX, cy2 + unitInfoVerticalOffset - 7 - 2 + 11, 0);
               }
            }

            int petDirect;
            if (Com.cameraWorld.isActiveLongStay()) {
               String name2 = heroOnMap.name;
               String clan2 = heroOnMap.clan;
               if (name2.length() > 8) {
                  name2 = name2.substring(0, 8);
               }

               petDirect = cx2 - MystFont.getStringLengthSpecSymb(name2, Com.fontSmall) * Com.c_font_width / 2;
               Com.fontSmall.writeText(gameGraphics2, name2, petDirect, cy2 + 2);
               if (clan2 != null && !clan2.equals("")) {
                  Com.fontClan.writeText(gameGraphics2, clan2, petDirect - clan2.length() * Com.c_font_width, cy2 + 2);
               }

               if (heroOnMap.headMessage != null) {
                  ImagesGlobal.addingAnimSprites[5].drawHCenterBottom(gameGraphics2, cx2, cy2 + unitInfoVerticalOffset + 2, 0);
                  maxHeight = heroOnMap.headMessage.headText.getMaxHeight(Com.fontSmall);
                  heroOnMap.headMessage.headText.draw(gameGraphics2, cx2 - 63, cy2 + unitInfoVerticalOffset + 2 - ImagesGlobal.addingAnimSprites[5].rh + 1 + (18 - maxHeight) / 2, 124, 18, 0, 0, Com.fontSmall);
               } else {
                  byte levelTextOffsetX = 0;
                  if (heroOnMap.swords != 0) {
                     levelTextOffsetX = -6;
                  }

                  if (heroOnMap.level > 0) {
                     ImagesGlobal.addingAnimSprites[10].draw(gameGraphics2, cx2 - 12 + levelTextOffsetX, cy2 + unitInfoVerticalOffset - 19 + 4, 0);
                     Com.fontSmall.writeText(gameGraphics2, String.valueOf(heroOnMap.level), cx2 - Integer.toString(heroOnMap.level).length() * Com.c_font_width / 2 + levelTextOffsetX, cy2 + unitInfoVerticalOffset - 19 + 4);
                  }
               }
            } else if (heroOnMap.headMessage != null) {
               ImagesGlobal.addingAnimSprites[4].drawHCenterBottom(gameGraphics2, cx2 + 3, cy2 + unitInfoVerticalOffset - 7 - 2 + 11, 0);
            }

            if (petB2 >= 0 && petH2 >= 0) {
               maxHeight = heroOnMap.petX;
               petDirection = heroOnMap.petY;
               petDirect = heroOnMap.petDirect;
               if (maxHeight >= 0 && petDirection > cy2) {
                  if (heroOnMap.animationState == StrategAnimGroup.ANIMATION_STATE_WALK) {
                     Legionery.draw(gameGraphics2, maxHeight, petDirection, petB2, petH2, petDirect, Com.petOffBody, 0);
                     return;
                  }

                  Legionery.draw(gameGraphics2, maxHeight, petDirection, petB2, petH2, petDirect, 0, Com.petOffHead);
               }
            }

         }
      }
   }

   public final DrawOrderElement newInstance() {
      return new DrawOrderElementOtherStrateg((short)-1, (short)-1, (short)-1, -1);
   }

   public final void returnInstance() {
      factory.returnInstance(this);
   }

   public static DrawOrderElementOtherStrateg getInstance(short rx, short ry, short sortInd, int ind2) {
      DrawOrderElementOtherStrateg drawOrderElementOtherStrateg;
      (drawOrderElementOtherStrateg = (DrawOrderElementOtherStrateg)factory.getInstance()).rx = rx;
      drawOrderElementOtherStrateg.ry = ry;
      drawOrderElementOtherStrateg.sortInd = sortInd;
      drawOrderElementOtherStrateg.ind = ind2;
      return drawOrderElementOtherStrateg;
   }
}
