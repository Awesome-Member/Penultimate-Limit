package bridge.other;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Base.GameControllDirectData;
import Base.GameMapObjectsController;
import MenuPck.MenuManager;
import bridge.ui.ClickGui;

public final class ObjectInteract extends ClickGui.Feature {
    public static final ObjectInteract INSTANCE = new ObjectInteract();

    private ObjectInteract() {
        super(ClickGui.Category.OTHER, "Object Interact", "Interact with objects anywhere");
    }

    public boolean blocksApproach(GameControllDirectData controls) {
        return enabled && controls.moveTarget != null && controls.moveTarget.selectedTown >= 0;
    }

    public void update(Game game) {
        if (!enabled || GameCommons.instance.loadMap || Com.dontFightAndAction || Com.mainTh.stopPaint || MenuManager.instance.guiHandler.isActive()) return;

        GameControllDirectData controls = game.gameControllDirectData;
        if (!blocksApproach(controls)) return;
        int objectIndex = controls.moveTarget.selectedTown;
        controls.disableControll();
        if (Com.currentGameMap != null && Com.currentGameMap.towns != null && objectIndex < Com.currentGameMap.towns.length && Com.currentGameMap.towns[objectIndex] != null) {
            GameMapObjectsController.instance.analisMapObject(objectIndex, false);
        }
    }
}
