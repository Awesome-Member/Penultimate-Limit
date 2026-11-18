package battlepck.client;

import Moduls.DrawOrderElement;
import Moduls.DrawOrderElementFactory;
import battlepck.Battle;
import battlepck.BattleUnit;
import desktop.graphics.GameGraphics;

public final class BattleDrawUnitOrderElement extends DrawOrderElement {
   private static final DrawOrderElementFactory factory = new DrawOrderElementFactory(new BattleDrawUnitOrderElement((short)-1, (short)-1, (short)-1, (BattleUnit)null, (Battle)null));
   private BattleUnit unit = null;
   private int[] paddings = null;
   private Battle battle = null;

   private BattleDrawUnitOrderElement(short rx, short ry, short sortInd, BattleUnit battleUnit, Battle battle2) {
      super((short)-1, (short)-1, (short)-1);
   }

   public final void draw(GameGraphics gameGraphics2) {
      this.unit.draw(gameGraphics2, this.battle.drawUnDoneAct, this.battle);
   }

   public final boolean isVisible() {
      return true;
   }

   public final int getRx() {
      return this.rx - this.paddings[0];
   }

   public final int getRx2() {
      return this.rx + this.paddings[1];
   }

   public final int getVisualFrameTop() {
      return this.ry - this.paddings[2];
   }

   public final int getVisualFrameBottom() {
      return this.ry + this.paddings[3];
   }

   public final DrawOrderElement newInstance() {
      return new BattleDrawUnitOrderElement((short)-1, (short)-1, (short)-1, (BattleUnit)null, (Battle)null);
   }

   public final void returnInstance() {
      this.battle = null;
      this.unit = null;
      this.paddings = null;
      factory.returnInstance(this);
   }

   public static BattleDrawUnitOrderElement getInstance(short rx, short ry, short sortInd, BattleUnit battleUnit, Battle battle2) {
      BattleDrawUnitOrderElement battleDrawUnitOrderElement;
      (battleDrawUnitOrderElement = (BattleDrawUnitOrderElement)factory.getInstance()).rx = rx;
      battleDrawUnitOrderElement.ry = ry;
      battleDrawUnitOrderElement.sortInd = sortInd;
      battleDrawUnitOrderElement.unit = battleUnit;
      battleDrawUnitOrderElement.paddings = battleUnit.getPaddings();
      battleDrawUnitOrderElement.battle = battle2;
      return battleDrawUnitOrderElement;
   }
}
