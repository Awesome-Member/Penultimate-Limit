package battlepck.client;

import Engine.Camera;
import Engine.pool.DefaultPooleableObject;
import Engine.pool.ObjectsPool;
import Engine.pool.PooleableObject;
import desktop.graphics.GameImage;

public final class BattleOffscreenIcon extends DefaultPooleableObject {
   public static final ObjectsPool OBJECTS_POOL = new BattleOffscreenIconPool();
   public int left;
   public int right;
   public int bottom;
   public int top;
   public int unitX;
   public int unitY;
   private short screenX;
   private short screenY;
   private int anchor;
   private GameImage image;
   private int[] arr = new int[3];

   public final short getScreenX() {
      return this.screenX;
   }

   public final short getScreenY() {
      return this.screenY;
   }

   public final int getAnchor() {
      return this.anchor;
   }

   public final GameImage getImage() {
      return this.image;
   }

   public final void setImage(GameImage gameImage) {
      this.image = gameImage;
      int width = gameImage.getWidth();
      int height = gameImage.getHeight();
      this.left = this.screenX;
      this.right = this.screenY;
      if ((this.anchor & 8) > 0) {
         this.left = this.screenX - width;
      } else if ((this.anchor & 1) > 0) {
         this.left = this.screenX - width / 2;
      }

      if ((this.anchor & 32) > 0) {
         this.right = this.screenY - height;
      } else if ((this.anchor & 2) > 0) {
         this.right = this.screenY - height / 2;
      }

      this.bottom = this.left + width;
      this.top = this.right + height;
   }

   public final void cleanPooleableObject() {
      this.image = null;
   }

   public static BattleOffscreenIcon tryCreate(Camera camera, short unitX2, short unitY2) {
      BattleOffscreenIcon battleOffscreenIcon = (BattleOffscreenIcon)OBJECTS_POOL.peekPooleableObject();
      if (camera.framePoint(unitX2, unitY2, battleOffscreenIcon.arr)) {
         battleOffscreenIcon.unitX = unitX2;
         battleOffscreenIcon.unitY = unitY2;
         battleOffscreenIcon.screenX = (short)battleOffscreenIcon.arr[0];
         battleOffscreenIcon.screenY = (short)battleOffscreenIcon.arr[1];
         battleOffscreenIcon.anchor = battleOffscreenIcon.arr[2];
         return battleOffscreenIcon;
      } else {
         OBJECTS_POOL.returnPooleableObject((PooleableObject)battleOffscreenIcon);
         return null;
      }
   }
}
