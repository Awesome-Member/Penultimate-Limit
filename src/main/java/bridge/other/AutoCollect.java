package bridge.other;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Base.GameMapObjectsController;
import MenuPck.MenuManager;
import Moduls.TileMap;
import Moduls.Town;
import bridge.ui.ClickGui;
import java.util.concurrent.TimeUnit;

public final class AutoCollect extends ClickGui.Feature {
    public static final AutoCollect INSTANCE = new AutoCollect();
    private TileMap previousMap;
    private short previousMapId = -1;
    private Town[] previousTowns;
    private int nextIndex;
    private long nextAction;

    private AutoCollect() {
        super(ClickGui.Category.OTHER, "Auto Collect", "Collect items/treasures on the current map");
        onToggle(this::reset);
    }

    private void reset() { previousMap = null; previousTowns = null; nextIndex = 0; nextAction = 0; }

    public void update(Game game) {
        if (!enabled) {
            reset();
            return;
        }
        TileMap map = Com.currentGameMap;
        if (map.towns == null || !GameCommons.instance.isGameLoopAvailable()
                || Com.mainTh == null || Com.mainTh.getTopPFter() != game
                || Com.dontFightAndAction || Com.mainTh.stopPaint || MenuManager.instance.guiHandler.isActive()) return;
        long now = System.nanoTime();
        int index = nextObject(map, now);
        if (index >= 0) {
            GameMapObjectsController.instance.analisMapObject(index, false);
            if (nextIndex >= map.towns.length) enabled = false;
        }
    }

    private int nextObject(TileMap map, long now) {
        if (previousMap == null) {
            previousMap = map; previousMapId = map.serverIndex; previousTowns = map.towns;
        } else if (map != previousMap || map.serverIndex != previousMapId || map.towns != previousTowns) {
            enabled = false; return -1;
        }
        if (now < nextAction) return -1;
        nextAction = now + TimeUnit.MILLISECONDS.toNanos(400);
        while (nextIndex < map.towns.length) {
            int i = nextIndex++;
            Town town = map.towns[i];
            if (town == null || town.type != 401 || town.state != 0) continue;
            return i;
        }
        enabled = false;
        return -1;
    }
}
