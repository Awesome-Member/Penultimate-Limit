package bridge.other;

public final class AutoOre extends AutoGather {
    public static final AutoOre INSTANCE = new AutoOre();
    private static final String OUTSIDE = "Wild_B5", INSIDE = "Craft_B5_Ore";

    private AutoOre() {
        super("Auto Ore", "Collect every available attempt per run", OUTSIDE, INSIDE, "Ищем руду...", route(5), 6);
    }

    private static Step[] route(int jumpsBefore) {
        Step[] steps = new Step[11];
        steps[0] = new Step(OUTSIDE, "sp0", 404, 0, 0);
        steps[jumpsBefore + 1] = new Step(INSIDE, "rque0", 409, 0, 0);
        steps[10] = new Step(INSIDE, "sp0", 404, 0, 0);
        for (int jump = 0; jump < 8; jump++) {
            steps[jump + (jump < jumpsBefore ? 1 : 2)] = new Step(INSIDE, "dp" + jump, 405, -1, -1);
        }
        return steps;
    }
}
