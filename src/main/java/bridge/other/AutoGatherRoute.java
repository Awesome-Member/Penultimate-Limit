package bridge.other;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Base.GameControllDirectData;
import Base.GameMapObjectsController;
import MenuPck.MenuList;
import MenuPck.MenuManager;
import MenuPck.MenuSupport;
import Moduls.Strategist;
import Moduls.TileMap;
import Moduls.Town;
import bridge.movement.QuestFollower;
import bridge.ui.ClickGui;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AutoGatherRoute extends ClickGui.Feature {
    private static final class Routes {
        static final List<AutoGatherRoute> ALL = Collections.unmodifiableList(Arrays.asList(AutoSilk.INSTANCE, AutoOre.INSTANCE));
    }

    public static List<AutoGatherRoute> routes() {
        return Routes.ALL;
    }

    private final String outside, inside, questName;
    private final String[] route = new String[11];
    private final int collectStage;

    private volatile int iterations = 1;
    private int stage = -1, runs, runLimit, requestedTown = -1;
    private int portalMap, portalX, portalY;
    private boolean portalStarted, accepting, rewardConfirmed;
    private long deadline;
    private short[][] path;
    private MenuList menu;
    private int menuId;

    AutoGatherRoute(String title, String outside, String inside, String questName, int jumpsBefore) {
        super(ClickGui.Category.OTHER, title, "Collect every available attempt per run");
        this.outside = outside;
        this.inside = inside;
        this.questName = questName;
        collectStage = jumpsBefore + 1;
        route[0] = route[route.length - 1] = "sp0";
        route[collectStage] = "rque0";
        for (int jump = 0; jump < 8; jump++) {
            route[jump + (jump < jumpsBefore ? 1 : 2)] = "dp" + jump;
        }
        textInput("Iterations", () -> Integer.toString(iterations), value -> iterations = Integer.parseInt(value.trim()),
                value -> {
                    try {
                        int count = Integer.parseInt(value.trim());
                        return count >= 1;
                    } catch (NumberFormatException exception) {
                        return false;
                    }
                });
    }

    public void stop(Game game) {
        enabled = false;
        reset(game);
    }

    private void reset(Game game) {
        releasePath(game.gameControllDirectData);
        stage = -1;
        clearStep();
    }

    private void clearStep() {
        requestedTown = -1;
        deadline = 0;
        menu = null;
        portalStarted = accepting = rewardConfirmed = false;
    }

    private void advance() {
        stage++;
        clearStep();
    }

    public void update(Game game) {
        if (!enabled) {
            if (stage >= 0) reset(game);
            return;
        }
        TileMap map = Com.currentGameMap;
        Strategist player = Strategist.instance;
        if (Com.mainTh == null || Com.mainTh.getPFter1() != game || map == null || map.towns == null || GameCommons.instance.loadMap || player.curMapInd != map.serverIndex) return;

        boolean dialog = MenuManager.instance.guiHandler.isActive();
        if (Com.mainTh.getTopPFter() != game && !(dialog && requestedTown >= 0)) return;
        if (stage < 0) {
            if (!outside.equals(map.nameId)) {
                stop(game);
                return;
            }
            if (QuestFollower.INSTANCE.enabled) QuestFollower.INSTANCE.stop(game);
            game.gameControllDirectData.disableControll();
            game.gameControllAnalogData.disableControll();
            runLimit = iterations;
            runs = 0;
            stage = 0;
        }
        if (stage == route.length) {
            if (!outside.equals(map.nameId) || ++runs >= runLimit) {
                stop(game);
            } else {
                stage = 0;
            }
            return;
        }
        if (!(stage == 0 ? outside : inside).equals(map.nameId)) {
            stop(game);
            return;
        }
        if (deadline != 0 && System.currentTimeMillis() > deadline) {
            stop(game);
            return;
        }
        if (requestedTown >= 0) {
            if (stage == collectStage) updateCollection(game, map, dialog);
            return;
        }
        if (dialog || Com.dontFightAndAction || Com.mainTh.stopPaint) return;

        int townIndex = findTown(map, route[stage], stage == collectStage ? 409 : stage == 0 || stage == route.length - 1 ? 404 : 405);
        if (townIndex < 0) {
            stop(game);
            return;
        }
        Town town = map.towns[townIndex];
        GameControllDirectData controls = game.gameControllDirectData;
        if (Math.abs(player.cx - town.dx) > town.SensRad + 1 || Math.abs(player.cy - town.dy) > town.SensRad + 1) {
            if (path == null) {
                if (map.pathfindingGrid == null) return;
                controls.buildPath(town.dx, town.dy);
                path = controls.curPathFindingKeys;
                deadline = System.currentTimeMillis() + 15000;
            }
            return;
        }
        releasePath(controls);
        requestedTown = townIndex;
        deadline = System.currentTimeMillis() + 15000;
        GameMapObjectsController.instance.analisMapObject(townIndex, stage != collectStage);
    }

    private void releasePath(GameControllDirectData controls) {
        if (path != null && controls.curPathFindingKeys == path) controls.disableControll();
        path = null;
    }

    private static int findTown(TileMap map, String key, int type) {
        for (int i = 0; i < map.towns.length; i++) {
            Town town = map.towns[i];
            if (town != null && town.state == 0 && town.type == type && key.equals(town.GPFile)) return i;
        }
        return -1;
    }

    public void beforePortal(int townIndex) {
        if (!enabled || stage < 0 || stage == collectStage || requestedTown != townIndex) return;
        Strategist player = Strategist.instance;
        portalMap = player.curMapInd;
        portalX = player.cx;
        portalY = player.cy;
        portalStarted = true;
    }

    public void afterPortal() {
        if (!enabled || !portalStarted) return;
        portalStarted = false;
        Strategist player = Strategist.instance;
        boolean mapPortal = stage == 0 || stage == route.length - 1;
        if (mapPortal ? player.curMapInd != portalMap
                : player.curMapInd == portalMap && (player.cx != portalX || player.cy != portalY)) advance();
    }

    public void observeMenu(MenuList current, int id) {
        if (enabled && stage == collectStage && requestedTown >= 0
                && MenuSupport.instance.curActiveTownInd == requestedTown) {
            menu = current;
            menuId = id;
        }
    }

    public void onReward(TileMap map, int giver, String[] result) {
        if (enabled && stage == collectStage && requestedTown >= 0 && map != null && inside.equals(map.nameId)
                && giver == 0 && GatherCollector.rewardConfirmed(result)) {
            rewardConfirmed = true;
        }
    }

    private void updateCollection(Game game, TileMap map, boolean dialog) {
        int giver = map.towns[requestedTown].typeInd;
        int choice = giver == 0 ? GatherCollector.collectionChoice(
                GatherCollector.availableQuests(map, giver), inside, giver, questName) : -1;
        if (!dialog) {
            if (accepting && !rewardConfirmed) {
                stop(game);
            } else if (choice < 0) {
                advance();
            } else {
                clearStep();
            }
            return;
        }
        if (menu == null || menu != MenuManager.instance.getCurMenuList()) return;
        MenuList current = menu;
        menu = null;
        switch (menuId) {
            case 196:
                if (accepting && !rewardConfirmed) {
                    stop(game);
                } else if (choice < 0) {
                    current.activateEl(0);
                    advance();
                } else {
                    accepting = true;
                    rewardConfirmed = false;
                    deadline = System.currentTimeMillis() + 15000;
                    current.activateEl(choice + 1);
                }
                break;
            case 197:
                if (accepting) current.activateEl(0);
                break;
            case 185:
            case 195:
                if (rewardConfirmed) current.activateEl(0);
                else stop(game);
                break;
            default:
                stop(game);
                break;
        }
    }

}
