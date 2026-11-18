package bridge.movement;

import Base.Com;
import Moduls.TileMap;
import bridge.ui.ClickGui;

public final class NoClip extends ClickGui.Feature {

    public static final NoClip INSTANCE = new NoClip();
    private boolean applied;

    public interface CollisionMap {
        void entropy$rebuildCollisions();
    }

    private NoClip() {
        super(ClickGui.Category.MOVEMENT,"NoClip","Walk through obstacles");
    }

    public void update() {
        TileMap map = Com.currentGameMap;
        if (map != null && enabled != applied) {
            applied = enabled;
            ((CollisionMap) (Object) map).entropy$rebuildCollisions();
        }
    }
}
