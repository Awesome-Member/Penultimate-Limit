package bridge.other;

public final class AutoSilk extends AutoGatherRoute {
    public static final AutoSilk INSTANCE = new AutoSilk();

    private AutoSilk() {
        super("Auto Silk", "Wild_E15", "Craft_E15_String", "Ищем нить...", 4);
    }
}
