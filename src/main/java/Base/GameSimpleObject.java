package Base;

import Moduls.DrawOrderElement;

public abstract class GameSimpleObject extends DrawOrderElement {
   public GameSimpleObject(short rx, short ry, short sortInd) {
      super(rx, ry, sortInd);
   }

   public abstract boolean update(int deltaTime);
}
