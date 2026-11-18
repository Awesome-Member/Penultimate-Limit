package bridge.movement;

import Moduls.Strategist;
import bridge.ui.ClickGui;

public final class Teleport extends ClickGui.Feature {
    public static final Teleport INSTANCE = new Teleport();
    private String coordinates = "";

    private Teleport() {
        super(ClickGui.Category.MOVEMENT,"Teleport","Move to X;Y");
        textInput("X;Y",()->{
            Strategist player=Strategist.instance;
            coordinates=player.cx+";"+player.cy;
            return coordinates;
        },value->coordinates=value,Teleport::validCoordinates);
        action(this::teleport);
    }

    private static short[] parse(String text) {
        String[] parts=text.split(";",-1);
        if(parts.length!=2)throw new IllegalArgumentException();
        return new short[]{Short.parseShort(parts[0].trim()),Short.parseShort(parts[1].trim())};
    }

    private static boolean validCoordinates(String text) {
        try { parse(text);return true; }
        catch(IllegalArgumentException exception) { return false; }
    }

    private void teleport() {
        Strategist player=Strategist.instance;
        try {
            short[] point=parse(coordinates);
            player.cx=point[0];
            player.cy=point[1];
        } catch(IllegalArgumentException exception) { }
    }
}
