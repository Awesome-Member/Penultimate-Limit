package bridge.visuals;

import Base.Com;
import Base.MainTh;
import MenuPck.MenuManager;
import bridge.ui.ClickGui;
import java.awt.Component;

public final class Zoom extends ClickGui.Feature {
    public static final Zoom INSTANCE = new Zoom();
    private volatile float zoomScale = 2.0f;

    private Zoom() {
        super(ClickGui.Category.VISUALS, "Zoom", "Mouse wheel to zoom in and out");
    }

    public float scale() {
        MainTh thread = Com.mainTh;
        return enabled && thread != null ? scaledValue(thread.getComponent(), 1.0) : 2.0f;
    }

    private float scaledValue(Component canvas, double multiplier) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double pixels = Math.min(16_777_216.0, Runtime.getRuntime().maxMemory() / 32.0);
        double minimum = Math.max(Math.sqrt(width * height / pixels), Math.max(width, height) / 8192.0);
        double maximum = Math.max(minimum, Math.min(width, height) / 16.0);

        float current = (float)Math.max(minimum, Math.min(maximum, zoomScale));
        return (float)Math.max(minimum, Math.min(maximum, current * multiplier));
    }

    public boolean scroll(int rotation) {
        MainTh thread = Com.mainTh;
        if (!enabled || thread == null || MenuManager.instance.guiHandler.isActive()) return false;
        zoomScale = scaledValue(thread.getComponent(), Math.pow(1.12, -rotation));
        return true;
    }
}
