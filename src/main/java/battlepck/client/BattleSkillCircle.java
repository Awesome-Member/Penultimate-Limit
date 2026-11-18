package battlepck.client;

import desktop.graphics.ArcRenderer;
import Moduls.DrawOrderElement;
import desktop.graphics.GameGraphics;

public final class BattleSkillCircle extends DrawOrderElement {
   private int type;
   public short cx;
   public short cy;
   public short radius;
   public int ovenrUnit;
   public int ovnerSide;
   public boolean markEnemys;
   public boolean markFriends;
   public boolean markSelf;

   public BattleSkillCircle(int type2, short cx2, short cy2, short radius2, int ovenrUnit2, int ovnerSide2, boolean markEnemys2, boolean markFriends2, boolean markSelf2) {
      super(cx2, cy2, (short)32767);
      this.type = type2;
      this.cx = cx2;
      this.cy = cy2;
      this.radius = radius2;
      this.ovenrUnit = ovenrUnit2;
      this.ovnerSide = ovnerSide2;
      this.markEnemys = markEnemys2;
      this.markFriends = markFriends2;
      this.markSelf = markSelf2;
   }

   public final void draw(GameGraphics gameGraphics2) {
      ArcRenderer arcRenderer = new ArcRenderer(gameGraphics2);
      int vRadius = this.radius * 87 / 100;
      arcRenderer.fillArc(this.cx - this.radius, this.cy - vRadius, this.radius << 1, vRadius << 1, 1280102399);
      gameGraphics2.setColor(5033983);
      gameGraphics2.drawArc(this.cx - this.radius, this.cy - vRadius, this.radius << 1, vRadius << 1, 0, 360);
      gameGraphics2.drawArc(this.cx - this.radius + 1, this.cy - vRadius, this.radius << 1, vRadius << 1, 0, 360);
   }

   public final DrawOrderElement newInstance() {
      return null;
   }

   public final void returnInstance() {
   }
}
