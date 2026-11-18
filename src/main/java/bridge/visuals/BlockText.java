package bridge.visuals;

import bridge.ui.ClickGui;

public final class BlockText extends ClickGui.Feature {
    public static final BlockText INSTANCE = new BlockText();

    private BlockText() {
        super(ClickGui.Category.VISUALS,"Block Text","Keep map labels visible while dragging");
    }
}
