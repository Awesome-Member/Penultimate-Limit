package bridge.other;

import Base.Com;
import Base.Black;
import Base.Game;
import Base.GameCommons;
import Base.GameControllDirectData;
import Base.GameMapObjectsController;
import MenuPck.MenuManager;
import Moduls.Strategist;
import Moduls.MapBuffer;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.quests.QuestSingle;
import bridge.movement.QuestFollower;
import bridge.ui.ClickGui;
import Engine.Camera;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public final class AutoJuice extends ClickGui.Feature {
    private static final String OUTSIDE = "Wild_E5", FOREST = "Craft_E5_Juice";
    private static final Step[] ROUTE = {
        new Step(OUTSIDE, "sp0", 404, 0, 0),
        new Step(FOREST, "dp0", 405, 54, 594),
        new Step(FOREST, "dp1", 405, 450, 558),
        new Step(FOREST, "dp2", 405, 738, 594),
        new Step(FOREST, "dp3", 405, 702, 558),
        new Step(FOREST, "dp4", 405, 630, 342),
        new Step(FOREST, "rque0", 409, 0, 0),
        new Step(FOREST, "dp5", 405, 666, 126),
        new Step(FOREST, "dp6", 405, 90, 54),
        new Step(FOREST, "sp0", 404, 0, 0)
    };
    public static final AutoJuice INSTANCE = new AutoJuice();
    private int stage = -1, portalStage = -1;
    private volatile int iterations = 1;
    private int iterationLimit, collected, completedRuns;
    private int portalMap;
    private boolean waiting;
    private long waitingSince;
    private long startedAt;
    private final long[] stepNanos = new long[ROUTE.length];
    private long forestLoadNanos, outsideLoadNanos;
    private long mapRequestNanos, mapCacheReadNanos;
    private int mapRequestCount;
    private boolean forestBufferPending;
    private final ThreadLocal<MapLoad> activeMapLoad = new ThreadLocal<>();

    private AutoJuice() {
        super(ClickGui.Category.OTHER, "Auto Juice", "Iterations count full forest runs; collect every available sap attempt");
        textInput("Iterations", () -> Integer.toString(iterations),
                value -> iterations = Integer.parseInt(value.trim()), value -> {
                    try {
                        int count = Integer.parseInt(value.trim());
                        return count >= 1;
                    } catch (NumberFormatException exception) { return false; }
                });
        onToggle(() -> { stage = -1; });
    }

    public void stop(Game game) {
        enabled = false; stage = -1; waiting = false; portalStage = -1;
        game.gameControllDirectData.disableControll();
    }

    public void prepareMapBuffer(MapBuffer buffer, Camera camera, TileMap map, Tile[] tiles) {
        forestBufferPending = enabled && stage == 1 && FOREST.equals(map.nameId);
        if (!forestBufferPending) buffer.TryRedraw(camera, map, tiles);
    }

    public void prepareMapForPaint() {
        if (!forestBufferPending) return;
        forestBufferPending = false;
        TileMap map = Com.currentGameMap;
        if (map != null && FOREST.equals(map.nameId)) {
            Com.MB.TryRedraw(Com.cameraWorld, map, Com.Tiles);
        }
    }

    public void loadMap(boolean justLoading) {
        int loadStage = stage;
        if (!enabled || (loadStage != 1 && loadStage != ROUTE.length)) {
            Com.startGameMap(justLoading);
            return;
        }
        long start = System.nanoTime();
        MapLoad load = new MapLoad();
        activeMapLoad.set(load);
        try {
            Com.startGameMap(justLoading);
        } finally {
            activeMapLoad.remove();
            long elapsed = System.nanoTime() - start;
            if (loadStage == 1) forestLoadNanos += elapsed;
            else outsideLoadNanos += elapsed;
            mapRequestNanos += load.requestNanos;
            mapCacheReadNanos += load.cacheReadNanos;
            mapRequestCount += load.requestCount;
        }
    }

    public long beginMapRequestTiming() {
        return activeMapLoad.get() == null ? 0 : System.nanoTime();
    }

    public void endMapRequestTiming(long started) {
        if (started == 0) return;
        MapLoad load = activeMapLoad.get();
        if (load == null) return;
        load.requestNanos += System.nanoTime() - started;
        load.requestCount++;
    }

    public byte[] readMapRecord(String store, int index) {
        MapLoad load = activeMapLoad.get();
        if (load == null) return Black.readMapCacheRecord(store, index);

        String key = store + ':' + index;
        byte[] bytes = load.records.get(key);
        if (bytes == null) {
            long start = System.nanoTime();
            bytes = Black.readMapCacheRecord(store, index);
            load.cacheReadNanos += System.nanoTime() - start;
            if (bytes != null) load.records.put(key, bytes);
        }
        return bytes;
    }

    public void update(Game game) {
        for (int actions = 0; actions < 32; actions++) {
            int previousStage = stage, previousCollected = collected, previousRuns = completedRuns;
            long start = enabled ? System.nanoTime() : 0;
            if (enabled && previousStage < 0) startedAt = start;
            updateStep(game);
            if (enabled && previousStage < ROUTE.length) {
                stepNanos[Math.max(0, previousStage)] += System.nanoTime() - start;
            }
            if (!enabled || stage == previousStage && collected == previousCollected && completedRuns == previousRuns) return;
        }
    }

    private void updateStep(Game game) {
        GameControllDirectData controls = game.gameControllDirectData;
        if (!enabled) { stage = -1; return; }
        TileMap map = Com.currentGameMap;
        Strategist player = Strategist.instance;
        if (Com.mainTh == null || Com.mainTh.getPFter1() != game || map == null || map.towns == null || GameCommons.instance.loadMap || map.serverIndex != player.curMapInd) return;
        if (Com.mainTh.getTopPFter() != game) return;
        long now = System.currentTimeMillis();
        if (stage < 0) {
            if (!OUTSIDE.equals(map.nameId)) { stop(game); return; }
            if (QuestFollower.INSTANCE.enabled) QuestFollower.INSTANCE.stop(game);
            controls.disableControll(); game.gameControllAnalogData.disableControll();
            stage = 0; waiting = false;
            iterationLimit = iterations; collected = 0; completedRuns = 0;
            Arrays.fill(stepNanos, 0);
            forestLoadNanos = outsideLoadNanos = 0;
            mapRequestNanos = mapCacheReadNanos = 0;
            mapRequestCount = 0;
            portalStage = -1;
        }
        if (stage == ROUTE.length) {
            if (!OUTSIDE.equals(map.nameId)) { stop(game); return; }
            completedRuns++;
            if (completedRuns < iterationLimit) {
                stage = 0; waiting = false; portalStage = -1;
            } else {
                long total = millis(System.nanoTime() - startedAt);
                System.out.printf("[Auto Juice] %d ms (entry %d, forest load %d, jumps %d, sap %d, exit %d, outside load %d)%n",
                        total, millis(stepNanos[0]), millis(forestLoadNanos), millis(stepNanos[1] + stepNanos[7]),
                        millis(stepNanos[6]), millis(stepNanos[9]), millis(outsideLoadNanos));
                long mapClientNanos = forestLoadNanos + outsideLoadNanos - mapRequestNanos - mapCacheReadNanos;
                System.out.printf("[Auto Juice] Map loads: requests %d ms (%d), cache reads %d ms, other client work %d ms%n",
                        millis(mapRequestNanos), mapRequestCount, millis(mapCacheReadNanos), millis(mapClientNanos));
                stop(game);
            }
            return;
        }
        Step step = ROUTE[stage];
        if (!step.map.equals(map.nameId)) { stop(game); return; }
        if (waiting && now - waitingSince > 15000) { stop(game); return; }
        if (waiting || MenuManager.instance.guiHandler.isActive() || Com.dontFightAndAction || Com.mainTh.stopPaint) return;
        int townIndex = findTown(map, step);
        if (townIndex < 0) { stop(game); return; }
        Town town = map.towns[townIndex];
        QuestSingle quest = null;
        if (stage == 6) {
            Vector<?> available = GatherCollector.availableQuests(map, town.typeInd);
            int choice = GatherCollector.collectionChoice(available, FOREST, town.typeInd, null);
            if (choice < 0) { advance(); return; }
            quest = (QuestSingle)available.elementAt(choice);
        }
        controls.disableControll(); game.gameControllAnalogData.disableControll();
        if (player.cx != town.dx || player.cy != town.dy) {
            player.setGridPosition(town.dx, town.dy);
        }
        waiting = true; waitingSince = now;
        if (stage == 6) {
            try {
                if (!GatherCollector.rewardConfirmed(GatherCollector.collect(map, town.typeInd, quest))) {
                    stop(game);
                    return;
                }
                collected++;
                waiting = false;
            } catch (Exception exception) { stop(game); }
            return;
        }
        if (step.type == 405) {
            int end = stage;
            while (end < ROUTE.length && ROUTE[end].type == 405) end++;
            String[] portals = new String[end - stage];
            for (int i = stage; i < end; i++) {
                int index = findTown(map, ROUTE[i]);
                if (index < 0) { stop(game); return; }
                portals[i - stage] = map.towns[index].GPFile;
            }
            try {
                GatherCollector.jump(portals);
                Step destination = ROUTE[end - 1];
                if (Com.currentGameMap != map || player.curMapInd != map.serverIndex
                        || player.cx != destination.x || player.cy != destination.y) {
                    stop(game); return;
                }
                stage = end; waiting = false;
            } catch (Exception exception) { stop(game); }
        } else GameMapObjectsController.instance.analisMapObject(townIndex, step.type != 409);
    }

    private static long millis(long nanos) {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }

    private static int findTown(TileMap map, Step step) {
        for (int i = 0; i < map.towns.length; i++) {
            Town town = map.towns[i];
            if (town != null && town.state == 0 && town.type == step.type && step.key.equals(town.GPFile)) return i;
        }
        return -1;
    }

    public boolean shouldConfirmPortal(int townIndex, int menuId) {
        if (!enabled || stage < 0 || stage >= ROUTE.length || stage == 6) return false;
        TileMap map = Com.currentGameMap;
        Step step = ROUTE[stage];
        return map != null && step.map.equals(map.nameId) && map.towns != null && townIndex >= 0
                && townIndex < map.towns.length && findTown(map, step) == townIndex
                && (step.type == 404 && menuId == 186 || step.type == 405 && menuId == 187);
    }

    public void beforePortal(int townIndex) {
        portalStage = -1;
        TileMap map = Com.currentGameMap;
        if (!enabled || stage < 0 || stage >= ROUTE.length || stage == 6 || map == null || map.towns == null) return;
        Step step = ROUTE[stage];
        if (step.map.equals(map.nameId) && findTown(map, step) == townIndex) {
            portalStage = stage; portalMap = Strategist.instance.curMapInd;
        }
    }

    public void afterPortal() {
        if (!enabled || portalStage != stage || stage < 0 || stage >= ROUTE.length) return;
        Step step = ROUTE[stage];
        Strategist player = Strategist.instance;
        portalStage = -1;
        if (step.type == 404 ? player.curMapInd != portalMap
                : player.curMapInd == portalMap && player.cx == step.x && player.cy == step.y) advance();
    }

    private void advance() {
        stage++; waiting = false;
    }

    private static final class MapLoad {
        final Map<String, byte[]> records = new HashMap<>();
        long requestNanos, cacheReadNanos;
        int requestCount;
    }

    private static final class Step {
        final String map, key;
        final int type, x, y;
        Step(String map, String key, int type, int x, int y) {
            this.map = map; this.key = key; this.type = type; this.x = x; this.y = y;
        }
    }
}
