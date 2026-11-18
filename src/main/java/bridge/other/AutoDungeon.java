package bridge.other;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import MenuPck.MenuManager;
import Moduls.Enemy;
import Moduls.EnemysMap;
import Moduls.Strategist;
import bridge.ui.ClickGui;

public final class AutoDungeon extends ClickGui.Feature {
    public static final AutoDungeon INSTANCE = new AutoDungeon();
    private volatile boolean mobs = true;
    private volatile boolean boss;
    private Enemy[] previousEnemies;

    private AutoDungeon() {
        super(ClickGui.Category.OTHER, "Auto Dungeon", "Gather dungeon mobs to yourself");
        checkbox("Mobs", () -> mobs, value -> mobs = value);
        checkbox("Boss", () -> boss, value -> boss = value);
        onToggle(() -> previousEnemies = null);
    }

    public void update(Game game) {
        if (!enabled || (!mobs && !boss)) return;
        if (!GameCommons.instance.isGameLoopAvailable() || Com.mainTh.getTopPFter() != game || Com.dontFightAndAction || Com.mainTh.stopPaint || MenuManager.instance.guiHandler.isActive()) return;

        Strategist player = Strategist.instance;
        EnemysMap enemies = Com.EnsMap;
        if (enemies.Enemys == null || enemies.Enemys == previousEnemies) return;
        previousEnemies = enemies.Enemys;
        gather(enemies, player.cx, player.cy, mobs, boss);
    }

    private static int bossLevel(Enemy[] enemies) {
        int level = 0;
        for (Enemy enemy : enemies) if (MobGather.isCombatMob(enemy)) level = Math.max(level, enemy.level);
        return level;
    }

    private static void gather(EnemysMap map, short x, short y, boolean pullMobs, boolean pullBoss) {
        int boss = bossLevel(map.Enemys);
        for (int id = 0; id < map.Enemys.length; id++) {
            Enemy enemy = map.Enemys[id];
            if (!MobGather.isCombatMob(enemy) || !enemy.Enable) continue;
            boolean isBoss = enemy.level == boss;
            if (isBoss ? !pullBoss : !pullMobs) continue;
            MobGather.moveEnemy(map, id, x, y);
        }
    }
}
