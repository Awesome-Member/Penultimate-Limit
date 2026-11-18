package battlepck.client;

import Engine.pool.ObjectsPool;
import Engine.pool.PooleableObject;

final class BattleOffscreenIconPool extends ObjectsPool {
   public final PooleableObject createPooleableObject() {
      return new BattleOffscreenIcon();
   }
}
