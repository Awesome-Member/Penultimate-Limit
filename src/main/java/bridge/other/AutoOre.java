package bridge.other;

public final class AutoOre extends AutoGatherRoute {
    public static final AutoOre INSTANCE = new AutoOre();

    private AutoOre() {
        super("Auto Ore", "Wild_B5", "Craft_B5_Ore", "Ищем руду...", 5);
    }
}
