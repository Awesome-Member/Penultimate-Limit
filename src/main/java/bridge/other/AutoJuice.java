package bridge.other;

public final class AutoJuice extends AutoGather {
    public static final AutoJuice INSTANCE = new AutoJuice();

    private AutoJuice() {
        super("Auto Juice", "Collect every available attempt per run",
                "Wild_E5", "Craft_E5_Juice", null, new Step[] {
                new Step("Wild_E5", "sp0", 404, 0, 0),
                new Step("Craft_E5_Juice", "dp0", 405, 54, 594),
                new Step("Craft_E5_Juice", "dp1", 405, 450, 558),
                new Step("Craft_E5_Juice", "dp2", 405, 738, 594),
                new Step("Craft_E5_Juice", "dp3", 405, 702, 558),
                new Step("Craft_E5_Juice", "dp4", 405, 630, 342),
                new Step("Craft_E5_Juice", "rque0", 409, 0, 0),
                new Step("Craft_E5_Juice", "dp5", 405, 666, 126),
                new Step("Craft_E5_Juice", "dp6", 405, 90, 54),
                new Step("Craft_E5_Juice", "sp0", 404, 0, 0)
        }, 6);
    }
}
