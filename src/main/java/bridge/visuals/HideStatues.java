package bridge.visuals;

import Base.Com;
import Moduls.Enemy;
import bridge.ui.ClickGui;
import java.util.ArrayList;
import java.util.List;

public final class HideStatues extends ClickGui.Feature {
    public static final HideStatues INSTANCE = new HideStatues();
    private final List<Enemy> disabled = new ArrayList<>();

    private HideStatues() {
        super(ClickGui.Category.VISUALS, "Hide Statues", "Disable statues on the world map");
    }

    public void restore() {
        for (Enemy enemy : disabled) enemy.Enable = true;
        disabled.clear();
    }

    public void update() {
        if (!enabled) return;
        Enemy[] enemies = Com.EnsMap.Enemys;
        if (enemies == null) return;
        for (Enemy enemy : enemies) {
            if (enemy != null && enemy.level == 99 && enemy.Enable) {
                disabled.add(enemy);
                enemy.Enable = false;
            }
        }
    }
}
