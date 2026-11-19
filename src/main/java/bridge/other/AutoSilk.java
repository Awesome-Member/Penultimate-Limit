package bridge.other;

public final class AutoSilk extends AutoGather {
    public static final AutoSilk INSTANCE = new AutoSilk();
    private static final String OUTSIDE = "Wild_E15", INSIDE = "Craft_E15_String";

    private AutoSilk() {
        super("Auto Silk", "Collect every available attempt per run", OUTSIDE, INSIDE, "Ищем нить...", route(4), 5);
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
