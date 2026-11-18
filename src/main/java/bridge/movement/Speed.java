package bridge.movement;

import Moduls.Strategist;
import bridge.ui.ClickGui;

public final class Speed extends ClickGui.Feature {

    public static final Speed INSTANCE = new Speed();

    public volatile int interval;
    private Integer originalInterval;

    private Speed() {
        super(ClickGui.Category.MOVEMENT,"Speed","Walk step interval");
        enabled = true;
        slider("Interval",0,100,()->interval,value->interval=value);
    }

    public void update() {
        Strategist player = Strategist.instance;

        if (enabled) {
            if (originalInterval == null) {
                originalInterval = player.avatarWalkSpeed;
            }
            player.avatarWalkSpeed = interval;
        } else if (originalInterval != null) {
            player.avatarWalkSpeed = originalInterval;
            originalInterval = null;
        }
    }
}
