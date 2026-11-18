package bridge.movement;

import bridge.ui.ClickGui;

public final class NoStuck extends ClickGui.Feature {
    public static final NoStuck INSTANCE = new NoStuck();

    private NoStuck() {
        super(ClickGui.Category.MOVEMENT,"No Stuck","Skip stuck report");
    }
}
