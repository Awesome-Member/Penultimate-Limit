package bridge.visuals;

import Base.Com;
import Base.NetRequest;
import Moduls.InventoryItem;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.otherheroes.HeroOnMap;
import Moduls.otherheroes.HeroesOnMapManager;
import bridge.ui.ClickGui;
import desktop.graphics.GameGraphics;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ShowStats extends ClickGui.Feature {
    public static final ShowStats INSTANCE = new ShowStats();
    private static final long REFRESH_INTERVAL = 5_000L;
    private static final long REQUEST_INTERVAL = 1_000L;
    private static final Pattern PERCENT = Pattern.compile("([0-9]+)\\s*%");
    private final ConcurrentMap<Integer, String> stats = new ConcurrentHashMap<>();
    private final Map<Integer, Long> lastRequested = new HashMap<>();
    private volatile boolean requestInProgress;
    private final ExecutorService requests = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "visual-st");
        thread.setDaemon(true);
        return thread;
    });
    private long nextRequestTime;
    private volatile int revision;
    private int ownAttack, ownDefense, ownId = -1, mapId = -1;

    private ShowStats() {
        super(ClickGui.Category.VISUALS, "Show Stats", "Show stats under player names");
        onToggle(this::resetStats);
    }

    private synchronized void resetStats() {
        revision++;
        stats.clear();
        lastRequested.clear();
        nextRequestTime = 0;
    }

    public synchronized void update() {
        if (!enabled) return;
        TileMap map = Com.currentGameMap;
        HeroesOnMapManager manager = HeroesOnMapManager.instance;
        Strategist player = Strategist.instance;

        if (ownId != player.id || mapId != map.serverIndex || ownAttack != player.avatarAtack || ownDefense != player.avatarArmor)
        {
            resetStats();
            ownId = player.id;
            mapId = map.serverIndex;
            ownAttack = player.avatarAtack;
            ownDefense = player.avatarArmor;
        }

        long now = System.currentTimeMillis();
        if (now < nextRequestTime || requestInProgress) return;
        if (manager.forMapInd != map.serverIndex || manager.isOldHeroesTilNextLoad()) return;

        int nextPlayer = -1;
        long oldestRequest = Long.MAX_VALUE;
        long refreshBefore = now - REFRESH_INTERVAL;
        for (HeroOnMap hero : (HeroOnMap[])manager.heroes.toArray(new HeroOnMap[0])) {
            int id = hero.id;
            if (id < 0 || id == player.id) continue;

            long requestedAt = lastRequested.getOrDefault(id, Long.MIN_VALUE);
            if (requestedAt <= refreshBefore && requestedAt < oldestRequest) {
                nextPlayer = id;
                oldestRequest = requestedAt;
            }
        }
        if (nextPlayer < 0) return;
        int id = nextPlayer, requestRevision = revision;
        lastRequested.put(id, now);
        nextRequestTime = now + REQUEST_INTERVAL;
        requestInProgress = true;
        requests.execute(() -> loadStats(id, requestRevision));
    }

    private void loadStats(int playerId, int requestRevision) {
        try {
            NetRequest request = new NetRequest();
            request.writeHeader(4630000);
            request.request.writeInt(playerId);
            if (!enabled || requestRevision != revision) return;
            if (!Com.sendRequest(request) || request.response == null) return;
            int responseCode = request.response.readInt();
            if (responseCode != 4630001) return;

            request.response.readUTF();
            request.response.readUTF();
            request.response.readByte();
            for (int i = 0; i < 17; i++) request.response.readShort();
            request.response.readBoolean();
            request.response.readByte();
            request.response.readByte();

            int itemCount = request.response.readInt();
            if (itemCount < 0 || itemCount > 1024) return;
            for (int i = 0; i < itemCount * 3; i++) request.response.readUTF();
            for (int i = 0; i < itemCount; i++) {
                if (request.response.readBoolean()) new InventoryItem().readAdditional(request.response);
            }
            String info = request.response.readUTF();
            Matcher percentages = PERCENT.matcher(info);
            if (!percentages.find()) return;

            String attack = percentages.group(1);
            if (!percentages.find()) return;
            synchronized (this) {
                if (enabled && requestRevision == revision) {
                    stats.put(playerId, "\u026e\u026f" + attack + "% \u0270\u0271" + percentages.group(1) + "%");
                }
            }
        } catch (Exception exception) {
            System.out.println("profile " + playerId + ": " + exception);
        } finally {
            requestInProgress = false;
        }
    }

    public void draw(GameGraphics graphics) {
        if (!enabled) return;
        TileMap map = Com.currentGameMap;
        HeroesOnMapManager manager = HeroesOnMapManager.instance;
        Strategist player = Strategist.instance;
        if (manager.forMapInd != map.serverIndex || manager.isOldHeroesTilNextLoad()) return;

        int cameraX = Com.cameraWorld.getCx();
        int cameraY = Com.cameraWorld.getCy();
        for (Object entry : manager.heroes.toArray()) {
            if (!(entry instanceof HeroOnMap)) continue;
            HeroOnMap hero = (HeroOnMap) entry;
            if (hero.id == player.id) continue;

            String info = stats.get(hero.id);
            if (info == null) continue;
            int y = hero.cy - cameraY + 2 + Com.fontSmall.fontHeight + 2;
            int width = Com.fontBig.getTextWidth(info);
            Com.fontBig.writeText(graphics, info, hero.cx - cameraX - width / 2, y);
        }
    }
}
