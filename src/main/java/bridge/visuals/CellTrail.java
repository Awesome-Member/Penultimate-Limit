package bridge.visuals;

import Base.Com;
import Engine.Camera;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.TileMap;
import bridge.ui.ClickGui;
import desktop.graphics.GameGraphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CellTrail extends ClickGui.Feature {
    public static final CellTrail INSTANCE = new CellTrail();
    private final Map<Long, Long> cells = new LinkedHashMap<>();
    private short previousMapId = -1;
    private static final long FADE_MS = 1200;

    private CellTrail() {
        super(ClickGui.Category.VISUALS, "Cell Trail", "Soft fading trail beneath your feet");
    }

    public void draw(GameGraphics surface) {
        TileMap map = Com.currentGameMap;
        Strategist player = Strategist.instance;
        Camera camera = Com.cameraWorld;
        if (!enabled || map.serverIndex != previousMapId) cells.clear();
        previousMapId = map.serverIndex;
        if (!enabled || Tile.width <= 0 || Tile.height <= 0) return;

        long now = System.nanoTime() / 1_000_000L;
        int cellX = TileMap.pxToTl(player.cx, 1);
        int cellY = TileMap.pxToTl(player.cy, 0);
        long key = (long)cellX << 32 | cellY & 0xffffffffL;
        cells.put(key, now);
        if (cells.size() > 256) cells.remove(cells.keySet().iterator().next());

        Graphics graphics = ((ESP.DrawingSurface)(Object)surface).entropy$createGraphics();
        try {
            graphics.translate(-surface.getTranslateX(), -surface.getTranslateY());
            Rectangle clip = graphics.getClipBounds();
            Iterator<Map.Entry<Long, Long>> iterator = cells.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Long> entry = iterator.next();
                double fade = 1.0 - (now - entry.getValue()) / (double)FADE_MS;
                if (fade <= 0) {
                    iterator.remove();
                    continue;
                }
                fade = fade * fade * (3 - 2 * fade);
                int x = (int)(entry.getKey() >> 32) * Tile.width - camera.getCx();
                int y = (int)(long)entry.getKey() * Tile.height - camera.getCy();
                if (clip != null && !clip.intersects(x, y, Tile.width, Tile.height)) continue;
                graphics.setColor(new Color(98, 165, 255, (int)(30 * fade)));
                graphics.fillRect(x + 1, y + 1, Tile.width - 2, Tile.height - 2);
                graphics.setColor(new Color(120, 180, 255, (int)(75 * fade)));
                graphics.drawRect(x + 1, y + 1, Tile.width - 3, Tile.height - 3);
            }
        } finally {
            graphics.dispose();
        }
    }
}
