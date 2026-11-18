package battlepck;

import Base.ImagesGlobal;
import Engine.AnimSprite;
import Moduls.DrawOrderElement;
import java.io.DataInputStream;
import desktop.graphics.GameGraphics;
import desktop.graphics.GameImage;

public final class BattleDeadman extends DrawOrderElement {
   private short cx;
   private short cy;
   private boolean playersAvatar;
   private byte watchDirection;
   public boolean drawDying = true;
   public int dyingTime = 0;

   public BattleDeadman(short cx2, short cy2, boolean playersAvatar2, byte watchDirection2, boolean drawDying2) {
      super(cx2, cy2, cy2);
      this.cx = cx2;
      this.cy = cy2;
      this.playersAvatar = playersAvatar2;
      this.watchDirection = watchDirection2;
      this.drawDying = drawDying2;
   }

   public BattleDeadman(DataInputStream dataInputStream) {
      super((short)0, (short)0, (short)0);
      this.cx = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.cy = decompiledsupport.UncheckedDataInput.readShort(dataInputStream);
      this.playersAvatar = decompiledsupport.UncheckedDataInput.readBoolean(dataInputStream);
      this.watchDirection = decompiledsupport.UncheckedDataInput.readByte(dataInputStream);
      this.rx = this.cx;
      this.ry = this.cy;
      this.sortInd = this.cy;
      this.drawDying = false;
   }

   public final BattleDeadman cloneTo() {
      BattleDeadman battleDeadman;
      (battleDeadman = new BattleDeadman(this.cx, this.cy, this.playersAvatar, this.watchDirection, this.drawDying)).dyingTime = this.dyingTime;
      return battleDeadman;
   }

   public final int getSpriteForDying() {
      return this.playersAvatar ? 35 : 4;
   }

   public final void draw(GameGraphics gameGraphics2) {
      short cx2;
      short cy2;
      if (this.drawDying) {
         cy2 = this.cy;
         cx2 = this.cx;
         ImagesGlobal.addingAnimSprites[1].drawCenter(gameGraphics2, cx2, cy2, 0);
         AnimSprite animSprite;
         (animSprite = ImagesGlobal.animClientSprites[this.getSpriteForDying()]).draw(gameGraphics2, (long)this.dyingTime, false, cx2 - animSprite.swHalf, cy2 - animSprite.sh + 9, this.watchDirection == 0);
      } else {
         cy2 = this.cy;
         cx2 = this.cx;
         GameImage gameImage = this.playersAvatar ? ImagesGlobal.deadHero : ImagesGlobal.deadMob;
         gameGraphics2.drawRegion(gameImage, 0, 0, gameImage.getWidth(), gameImage.getHeight(), this.watchDirection == 0 ? 2 : 0, cx2 - gameImage.getWidth() / 2, cy2 - gameImage.getHeight() + 9);
      }
   }

   public final DrawOrderElement newInstance() {
      return null;
   }

   public final void returnInstance() {
   }
}
