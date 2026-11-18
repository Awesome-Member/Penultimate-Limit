package bridge.movement;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Base.GameControllDirectData;
import Base.GameMapObjectsController;
import Base.GameSelectionStructure;
import MenuPck.MenuManager;
import Moduls.Enemy;
import Moduls.Strategist;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.Town;
import Moduls.quests.QuestInPlayer;
import Moduls.quests.QuestStrateg;
import Moduls.quests.QuestSingleIdSmall;
import bridge.ui.ClickGui;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class QuestFollower extends ClickGui.Feature {
    public static final QuestFollower INSTANCE = new QuestFollower();
    private TileMap previousMap;
    private short previousMapId = -1;
    private QuestInPlayer previousQuest;
    private QuestSingleIdSmall questGiver;
    private String returnQuester;
    private Target goal;
    private short[][] ownedPath;
    private long nextPlan, lastProgress, transitionStarted;
    private int lastX, lastY, retries;
    private String lastMessage;

    private QuestFollower() {
        super(ClickGui.Category.MOVEMENT, "Quest Follower", "Follow the active quest marker");
    }

    public void stop(Game game) {
        enabled = false;
        release(game.gameControllDirectData);
        goal = null;
        previousQuest = null;
        questGiver = null;
        transitionStarted = 0;
        lastMessage = null;
    }

    private void release(GameControllDirectData controls) {
        if (ownedPath != null && controls.curPathFindingKeys == ownedPath) controls.disableControll();
        ownedPath = null;
    }

    private void message(String text) {
        if (!text.equals(lastMessage)) {
            lastMessage = text;
            GameCommons.instance.showFloatText(text, 3000);
        }
    }

    public void update(Game game) {
        GameControllDirectData controls = game.gameControllDirectData;
        if (!enabled) { release(controls); goal = null; previousQuest = null; questGiver = null; transitionStarted = 0; lastMessage = null; return; }
        if (Com.mainTh == null || Com.mainTh.getTopPFter() != game || GameCommons.instance.loadMap
                || MenuManager.instance.guiHandler.isActive()) { release(controls); return; }
        Strategist player = Strategist.instance;
        TileMap map = Com.currentGameMap;
        if (map.cells == null || map.pathfindingGrid == null
                || map.serverIndex != player.curMapInd) { release(controls); return; }
        if (Com.touchControlType != 4) {
            release(controls); message("Quest Follower: Enable click-to-move."); return;
        }
        QuestStrateg quests = QuestStrateg.instance;
        QuestInPlayer quest = quests.getCompassedQuest();
        if (quest == null) { release(controls); goal = null; previousQuest = null; questGiver = null; transitionStarted = 0; message("Quest Follower: Track a quest first."); return; }
        quests.tryCalculateWays(map, Com.EnsMap, player);
        if (quest != previousQuest) rememberQuestGiver(quest);
        if (map != previousMap || map.serverIndex != previousMapId || quest != previousQuest) {
            release(controls); goal = null; retries = 0; nextPlan = 0; transitionStarted = 0; lastProgress = System.currentTimeMillis();
            previousMap = map; previousMapId = map.serverIndex; previousQuest = quest;
        }

        long now = System.currentTimeMillis();
        if (player.cx != lastX || player.cy != lastY) {
            lastX = player.cx; lastY = player.cy; lastProgress = now;
        }
        if (now < nextPlan) return;
        nextPlan = now + 400;
        List<Target> targets = targets(quest, map, player);
        if (targets.isEmpty()) { release(controls); goal = null; message("Quest Follower: Waiting for a quest marker."); return; }
        Target selected = null;
        if (goal != null) for (Target target : targets) if (target.kind == goal.kind && target.id == goal.id) { selected = target; break; }
        if (selected == null) selected = targets.get(0);
        boolean changed = goal == null || goal.kind != selected.kind || goal.id != selected.id
                || goal.x != selected.x || goal.y != selected.y;
        goal = selected;
        if (changed) { release(controls); retries = 0; transitionStarted = 0; lastProgress = now; lastMessage = null; }
        if (transitionStarted != 0) {
            if (goal.kind == 0 && map.towns[goal.id].type == 405 && !reached(goal, player)) return;
            if (now - transitionStarted > 10000) {
                stop(game); message("Quest Follower: Portal transition failed.");
            }
            return;
        }
        if (goal.kind != 3 && reached(goal, player)) {
            release(controls);
            if (isPortal(goal, map)) {
                if (Com.dontFightAndAction || Com.mainTh.stopPaint) return;
                controls.disableControll();
                controls.moveTarget = new GameSelectionStructure();
                controls.moveTarget.selectedTown = goal.id;
                transitionStarted = now;
                message("Using quest portal.");
            } else if (isReturnTarget(quest, map, goal)) {
                if (Com.dontFightAndAction || Com.mainTh.stopPaint) return;
                int townIndex = goal.id;
                stop(game);
                message("Interacting with quest giver.");
                GameMapObjectsController.instance.analisMapObject(townIndex, false);
            } else {
                message("Quest marker reached.");
            }
            return;
        }
        if (ownedPath != null && controls.curPathFindingKeys != ownedPath) { stop(game); return; }
        boolean stuck = now - lastProgress > 2500;
        if (!changed && ownedPath != null && !controls.pathFolower.isMovePathAbsent() && !stuck) return;
        if (++retries > 4) {
            stop(game); message("Quest Follower: Path blocked."); return;
        }
        controls.disableControll();
        if (goal.kind == 3) {
            controls.moveTarget = new GameSelectionStructure();
            controls.moveTarget.selectedMapMove = goal.id;
            controls.moveTarget.selectedMapMoveX = TileMap.pxToTl(goal.x, 1);
            controls.moveTarget.selectedMapMoveY = TileMap.pxToTl(goal.y, 0);
        }
        controls.buildPath(goal.x, goal.y);
        ownedPath = controls.curPathFindingKeys;
        lastProgress = now;
        message("Following quest marker: " + goal.x + ";" + goal.y);
    }

    private static List<Target> targets(QuestInPlayer quest, TileMap map, Strategist player) {
        List<Target> result = new ArrayList<>();
        if (map.towns != null) for (int id : quest.calcWaysTowns) {
            if (id < 0 || id >= map.towns.length) continue;
            Town town = map.towns[id];
            if (town != null && town.state == 0) result.add(new Target(0, id, town.dx, town.dy, town.SensRad + 1));
        }
        if (Com.EnsMap != null && Com.EnsMap.Enemys != null) for (int id : quest.calcWaysEnemys) {
            if (id < 0 || id >= Com.EnsMap.Enemys.length) continue;
            Enemy enemy = Com.EnsMap.Enemys[id];
            if (enemy != null && enemy.Enable) result.add(new Target(1, id, enemy.cx, enemy.cy, 4));
        }
        if (quest.calcWaysPlayerId >= 0 && !quest.isTargetPlayerKilled) {
            int x = quest.calcWaysPlayerPosXClient >= 0 ? quest.calcWaysPlayerPosXClient : quest.calcWaysPlayerPosXServer;
            int y = quest.calcWaysPlayerPosYClient >= 0 ? quest.calcWaysPlayerPosYClient : quest.calcWaysPlayerPosYServer;
            if (x >= 0 && y >= 0 && x <= Short.MAX_VALUE && y <= Short.MAX_VALUE) result.add(new Target(2, quest.calcWaysPlayerId, (short)x, (short)y, 4));
        }
        if (result.isEmpty()) for (int arrow : quest.calcWaysDirects) {
            int direction = arrow == 1 ? 3 : arrow == 2 ? 1 : arrow == 3 ? 2 : arrow == 4 ? 0 : -1;
            if (direction < 0) continue;
            Target target = exit(map, player, direction);
            if (target != null) result.add(target);
        }
        result.sort(Comparator.comparingLong(target -> distance(player.cx, player.cy, target.x, target.y)));
        return result;
    }

    private static Target exit(TileMap map, Strategist player, int direction) {
        if (!map.moveToMapsZones[direction]) return null;
        boolean[] allowed = direction == 0 ? map.moveToMapsByTileRight : direction == 1 ? map.moveToMapsByTileBot
                : direction == 2 ? map.moveToMapsByTileLeft : map.moveToMapsByTileTop;
        if (allowed == null) return null;
        Target nearest = null;
        for (int i = 0; i < allowed.length; i++) {
            if (!allowed[i]) continue;
            int x = direction == 0 ? map.widthTl - 1 : direction == 2 ? 0 : i;
            int y = direction == 1 ? map.heightTl - 1 : direction == 3 ? 0 : i;
            if (map.getBlockTl(x, y)) continue;
            Target candidate = new Target(3, direction, (short)(x * Tile.width + Tile.widthHalf), (short)(y * Tile.height + Tile.heightHalf), 0);
            if (nearest == null || distance(player.cx, player.cy, candidate.x, candidate.y) < distance(player.cx, player.cy, nearest.x, nearest.y)) nearest = candidate;
        }
        return nearest;
    }

    private static long distance(int x1, int y1, int x2, int y2) {
        long x = (long)x1 - x2, y = (long)y1 - y2;
        return x * x + y * y;
    }

    private static boolean reached(Target target, Strategist player) {
        if (target.kind == 0) return Math.abs((int)player.cx - target.x) <= target.radius
                && Math.abs((int)player.cy - target.y) <= target.radius;
        return distance(player.cx, player.cy, target.x, target.y) <= (long)target.radius * target.radius;
    }

    private static boolean isPortal(Target target, TileMap map) {
        return target.kind == 0 && (map.towns[target.id].type == 404 || map.towns[target.id].type == 405);
    }

    private void rememberQuestGiver(QuestInPlayer quest) {
        questGiver = new QuestSingleIdSmall(quest.questSingle.inMap, quest.questSingle.inMapQuesterInd,
                quest.questSingle.inQuesterInd);
        returnQuester = quest.questSingle.backToOtherQuester;
    }

    private boolean isReturnTarget(QuestInPlayer quest, TileMap map, Target target) {
        if (!quest.isDoneCach || questGiver == null || target.kind != 0) return false;
        Town town = map.towns[target.id];
        if (town.type != 409) return false;
        if (returnQuester != null && !returnQuester.isEmpty()) return returnQuester.equals(town.typeIdName);
        return questGiver.inMap.equals(map.nameId) && questGiver.inMapQuesterInd == town.typeInd;
    }

    public boolean shouldConfirmPortal(int townIndex, int menuId) {
        TileMap map = Com.currentGameMap;
        Strategist player = Strategist.instance;
        return enabled && goal != null && goal.kind == 0 && goal.id == townIndex
                && map != null && map == previousMap && map.serverIndex == previousMapId
                && map.towns != null && townIndex >= 0 && townIndex < map.towns.length && map.towns[townIndex] != null
                && player != null && map.serverIndex == player.curMapInd && reached(goal, player)
                && !GameCommons.instance.loadMap && Com.mainTh != null && Com.mainTh.getTopPFter() instanceof Game
                && previousQuest != null && QuestStrateg.instance.getCompassedQuest() == previousQuest
                && (menuId == 186 && map.towns[townIndex].type == 404
                    || menuId == 187 && map.towns[townIndex].type == 405);
    }

    private static final class Target {
        final int kind, id, radius;
        final short x, y;
        Target(int kind, int id, short x, short y, int radius) { this.kind = kind; this.id = id; this.x = x; this.y = y; this.radius = radius; }
    }
}
