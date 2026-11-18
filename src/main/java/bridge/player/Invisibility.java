package bridge.player;

import Moduls.Strategist;
import bridge.ui.ClickGui;

public final class Invisibility extends ClickGui.Feature {

    public static final Invisibility INSTANCE = new Invisibility();
    private Byte originalDirection;

    private Invisibility() {
        super(ClickGui.Category.PLAYER,"Invisibility","Hide player model");
    }

    public void update() {
        Strategist player = Strategist.instance;
        if (enabled) {
            if (originalDirection == null) originalDirection = player.watchDirection;
            player.watchDirection = 2;
        } else if (originalDirection != null) {
            player.watchDirection = originalDirection;
            originalDirection = null;
        }
    }
}
