package bridge.other;

import Base.Com;
import Base.Game;
import Base.GameCommons;
import Moduls.Enemy;
import Moduls.EnemysMap;
import Moduls.Strategist;
import Moduls.TileMap;
import applet.Main;
import bridge.ui.ClickGui;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public final class MobGather extends ClickGui.Feature {
    public static final MobGather INSTANCE = new MobGather();
    private final Path config = Paths.get("config", "entropy", "mob-gather.properties");
    private volatile List<Rule> rules = Collections.emptyList();
    private final ConcurrentLinkedQueue<Request> requests = new ConcurrentLinkedQueue<>();
    private TileMap previousMap;
    private Enemy[] previousEnemies;
    private short previousMapId = -1;
    private boolean loading;
    private Editor window;
    private volatile String status = "";

    private MobGather() {
        super(ClickGui.Category.OTHER, "Mob Gather", "Map rules for mob positions");
        button("Edit rules", this::open);
        onToggle(() -> { if (enabled) open(); });
        load();
    }

    public void update(Game game) {
        if (GameCommons.instance.loadMap) { loading = true; return; }
        TileMap map = Com.currentGameMap;
        Strategist player = Strategist.instance;
        EnemysMap enemies = Com.EnsMap;
        if (map == null || player == null || enemies == null || enemies.Enemys == null
                || map.cells == null || map.serverIndex != player.curMapInd
                || Com.mainTh == null || Com.mainTh.getTopPFter() != game) return;
        boolean entered = loading || map != previousMap || map.serverIndex != previousMapId
                || enemies.Enemys != previousEnemies;
        loading = false;
        previousMap = map; previousMapId = map.serverIndex; previousEnemies = enemies.Enemys;
        if (enabled && entered) for (Rule rule : rules) {
            if (rule.enabled && rule.onEntry && rule.matches(map)) apply(rule, game, map, enemies, player);
        }
        Request request;
        while ((request = requests.poll()) != null) {
            if (request.map != map || request.mapId != map.serverIndex || request.enemies != enemies.Enemys) {
                report("Map changed.");
            } else if (!request.rule.matches(map)) {
                report("Rule does not match this map.");
            } else {
                apply(request.rule, game, map, enemies, player);
            }
        }
    }

    private void apply(Rule rule, Game game, TileMap map, EnemysMap enemies, Strategist player) {
        try {
            applyPositions(rule, game, map, enemies, player);
        } catch (IllegalArgumentException exception) {
            report("Mob Gather: " + exception.getMessage());
        }
    }

    private static void applyPositions(Rule rule, Game game, TileMap map, EnemysMap enemies, Strategist player) {
        short[] playerPoint = point(rule.playerPoint);
        short[] mobPoint = point(rule.mobPoint);
        if (playerPoint != null) checkPoint(playerPoint, map);
        if (mobPoint == null) mobPoint = playerPoint == null
                ? new short[]{player.cx, player.cy} : playerPoint;
        if (rule.count > 0) checkPoint(mobPoint, map);
        if (rule.count > 0 && (enemies.Map == null || enemies.SlotW <= 0 || enemies.SlotH <= 0))
            throw new IllegalArgumentException("Enemy grid is not loaded.");
        if (rule.count > 0) {
            int sx = mobPoint[0] / enemies.SlotW, sy = mobPoint[1] / enemies.SlotH;
            if (sx >= enemies.Map.length || enemies.Map[sx] == null || sy >= enemies.Map[sx].length
                    || enemies.Map[sx][sy] == null) throw new IllegalArgumentException("Target is outside the enemy grid.");
        }
        if (playerPoint != null) {
            game.gameControllDirectData.disableControll();
            game.gameControllAnalogData.disableControll();
            player.setGridPosition(playerPoint[0], playerPoint[1]);
        }
        moveEnemies(enemies, rule.count, mobPoint[0], mobPoint[1]);
    }

    private static void moveEnemies(EnemysMap map, int count, short x, short y) {
        int moved = 0;
        for (int id = 0; id < map.Enemys.length && moved < count; id++) {
            Enemy enemy = map.Enemys[id];
            if (isCombatMob(enemy) && enemy.Enable && moveEnemy(map, id, x, y)) moved++;
        }
    }

    public static boolean isCombatMob(Enemy enemy) {
        return enemy != null && enemy.fightable && enemy.level > 0 && enemy.level != 99;
    }

    public static boolean moveEnemy(EnemysMap map, int id, short x, short y) {
        if (map == null || map.Enemys == null || id < 0 || id >= map.Enemys.length || map.Enemys[id] == null
                || x < 0 || y < 0 || map.Map == null || map.SlotW <= 0 || map.SlotH <= 0) return false;
        int slotX = x / map.SlotW, slotY = y / map.SlotH;
        if (slotX >= map.Map.length || map.Map[slotX] == null || slotY >= map.Map[slotX].length
                || map.Map[slotX][slotY] == null) return false;
        Enemy enemy = map.Enemys[id];
        if (enemy.CurMapSlotX >= 0 && enemy.CurMapSlotX < map.Map.length
                && map.Map[enemy.CurMapSlotX] != null && enemy.CurMapSlotY >= 0
                && enemy.CurMapSlotY < map.Map[enemy.CurMapSlotX].length && map.Map[enemy.CurMapSlotX][enemy.CurMapSlotY] != null)
            map.Map[enemy.CurMapSlotX][enemy.CurMapSlotY].removeElement(Integer.valueOf(id));
        enemy.setGridPosition(x, y); enemy.CurMapSlotX = slotX; enemy.CurMapSlotY = slotY;
        map.Map[slotX][slotY].addElement(Integer.valueOf(id));
        return true;
    }

    private static short[] point(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String[] parts = text.split(";", -1);
        try {
            if (parts.length == 2) return new short[]{Short.parseShort(parts[0].trim()), Short.parseShort(parts[1].trim())};
        } catch (NumberFormatException ignored) { }
        throw new IllegalArgumentException("Enter coordinates as X;Y, e.g. 120;180.");
    }

    private static void checkPoint(short[] point, TileMap map) {
        if (point[0] < 0 || point[1] < 0 || point[0] >= map.widthPx || point[1] >= map.heightPx)
            throw new IllegalArgumentException("Coordinates are outside the map.");
    }

    private void report(String text) {

    }

    private void open() {
        if (!SwingUtilities.isEventDispatchThread()) { SwingUtilities.invokeLater(this::open); return; }
        if (window == null || !window.isDisplayable()) window = new Editor();
        window.setVisible(true); window.toFront();
    }

    private static final class Rule {
        final boolean enabled, onEntry;
        final String location, mobPoint, playerPoint;
        final int count;
        Rule(boolean enabled, String location, int count, String mobPoint, String playerPoint, boolean onEntry) {
            if (location.trim().isEmpty()) throw new IllegalArgumentException("Map is required.");
            if (count < 0) throw new IllegalArgumentException("Mob count must be zero or greater.");
            point(mobPoint); point(playerPoint);
            this.enabled = enabled; this.location = location.trim(); this.count = count;
            this.mobPoint = mobPoint.trim(); this.playerPoint = playerPoint.trim(); this.onEntry = onEntry;
        }
        boolean matches(TileMap map) {
            return location.equalsIgnoreCase(map.nameId) || location.equalsIgnoreCase(map.nameLiteral)
                    || location.equals(Short.toString(map.serverIndex));
        }
    }

    private static final class Request {
        final Rule rule;
        final TileMap map;
        final short mapId;
        final Enemy[] enemies;
        Request(Rule rule) {
            this.rule = rule; map = Com.currentGameMap;
            mapId = map == null ? -1 : map.serverIndex;
            enemies = Com.EnsMap == null ? null : Com.EnsMap.Enemys;
        }
    }

    private void load() {
        if (!Files.exists(config)) return;
        try (Reader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8)) {
            Properties data = new Properties(); data.load(reader);
            List<Rule> loaded = new ArrayList<>();
            int count = Integer.parseInt(data.getProperty("rules", "0"));
            for (int i = 0; i < count; i++) {
                String key = i + ".";
                loaded.add(new Rule(Boolean.parseBoolean(data.getProperty(key + "enabled", "true")),
                        data.getProperty(key + "map", ""), Integer.parseInt(data.getProperty(key + "count", "2")),
                        data.getProperty(key + "mobs", ""), data.getProperty(key + "player", ""),
                        Boolean.parseBoolean(data.getProperty(key + "entry", "true"))));
            }
            rules = Collections.unmodifiableList(loaded);
        } catch (IOException | IllegalArgumentException exception) { status = "Could not load rules: " + exception.getMessage(); }
    }

    private void save(List<Rule> updated) throws IOException {
        Properties data = new Properties(); data.setProperty("rules", Integer.toString(updated.size()));
        for (int i = 0; i < updated.size(); i++) {
            Rule rule = updated.get(i); String key = i + ".";
            data.setProperty(key + "enabled", Boolean.toString(rule.enabled));
            data.setProperty(key + "map", rule.location); data.setProperty(key + "count", Integer.toString(rule.count));
            data.setProperty(key + "mobs", rule.mobPoint); data.setProperty(key + "player", rule.playerPoint);
            data.setProperty(key + "entry", Boolean.toString(rule.onEntry));
        }
        Files.createDirectories(config.getParent());
        Path temporary = config.resolveSibling(config.getFileName() + ".tmp");
        try {
            try (Writer writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) { data.store(writer, "Mob Gather"); }
            Files.move(temporary, config, StandardCopyOption.REPLACE_EXISTING);
        } finally { Files.deleteIfExists(temporary); }
        rules = Collections.unmodifiableList(new ArrayList<>(updated));
    }

    private final class Editor extends JDialog {
        private final DefaultTableModel model = new DefaultTableModel(
                new String[]{"Enabled", "Map", "Count", "Mob X;Y", "Player X;Y", "Trigger"}, 0) {
            @Override public Class<?> getColumnClass(int column) { return column == 0 ? Boolean.class : String.class; }
        };
        private final JTable table = new JTable(model);
        private final JLabel result = new JLabel(" ");
        private final javax.swing.Timer refresh = new javax.swing.Timer(250, event -> result.setText(status));

        Editor() {
            super(SwingUtilities.getWindowAncestor(Main.instance), "Mob Gather Rules", Dialog.ModalityType.MODELESS);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(830, 350)); setSize(980, 480); setLocationRelativeTo(Main.instance);
            JPanel root = new JPanel(new BorderLayout(10, 12));
            root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16)); setContentPane(root);
            Color background = new Color(0x202320), card = new Color(0x292d29), text = new Color(0xd8d8d8);
            root.setBackground(background);
            JLabel help = new JLabel("<html>Use the map name or ID. Leave Mob X;Y blank to use the player's position.<br>Leave Player X;Y blank to keep the player in place. Entry rules run from top to bottom.</html>");
            help.setForeground(text); root.add(help, BorderLayout.NORTH);
            table.setRowHeight(30); table.setBackground(card); table.setForeground(text);
            table.setGridColor(background); table.setSelectionBackground(new Color(0x303e65)); table.setSelectionForeground(Color.WHITE);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13)); table.putClientProperty("terminateEditOnFocusLost", true);
            table.getColumnModel().getColumn(0).setPreferredWidth(45);
            table.getColumnModel().getColumn(1).setPreferredWidth(230);
            table.getColumnModel().getColumn(2).setPreferredWidth(60);
            table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox<>(new String[]{"On Entry", "Manual"})));
            JScrollPane scroll = new JScrollPane(table); scroll.getViewport().setBackground(card); root.add(scroll, BorderLayout.CENTER);
            JPanel bottom = new JPanel(new BorderLayout(8, 8)); bottom.setOpaque(false);
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0)); buttons.setOpaque(false);
            addButton(buttons, "Add", () -> {
                TileMap map = Com.currentGameMap;
                model.addRow(new Object[]{true, map == null ? "" : map.nameId, "2", "", "", "On Entry"});
                table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
            });
            addButton(buttons, "Remove", () -> { finishEdit(); int row = table.getSelectedRow(); if (row >= 0) model.removeRow(row); });
            addButton(buttons, "Use Current Map", () -> {
                finishEdit(); int row = table.getSelectedRow(); TileMap map = Com.currentGameMap;
                if (row >= 0 && map != null) model.setValueAt(map.nameId, row, 1);
            });
            addButton(buttons, "Use Player Position", () -> {
                finishEdit(); int row = table.getSelectedRow(); Strategist player = Strategist.instance;
                if (row >= 0 && player != null) model.setValueAt(player.cx + ";" + player.cy, row, 4);
            });
            addButton(buttons, "Save", () -> {
                try {
                    finishEdit(); List<Rule> updated = new ArrayList<>();
                    for (int row = 0; row < model.getRowCount(); row++) updated.add(readRule(row));
                    save(updated); status = "Rules saved. Entry rules: " + (enabled ? "enabled." : "disabled.");
                } catch (IOException | IllegalArgumentException exception) { status = exception.getMessage(); }
            });
            addButton(buttons, "Apply Now", () -> {
                try {
                    finishEdit(); int row = table.getSelectedRow();
                    if (row < 0) throw new IllegalArgumentException("Select a rule.");
                    Rule rule = readRule(row); TileMap map = Com.currentGameMap;
                    if (map == null || Strategist.instance == null || !rule.matches(map)
                            || Com.mainTh == null || !(Com.mainTh.getTopPFter() instanceof Game) || GameCommons.instance.loadMap)
                        throw new IllegalArgumentException("Rule does not match this map.");
                    requests.add(new Request(rule)); status = "Rule queued.";
                } catch (IllegalArgumentException exception) { status = exception.getMessage(); }
            });
            bottom.add(buttons, BorderLayout.CENTER); result.setForeground(text); bottom.add(result, BorderLayout.SOUTH);
            root.add(bottom, BorderLayout.SOUTH);
            for (Rule rule : rules) model.addRow(new Object[]{rule.enabled, rule.location, Integer.toString(rule.count), rule.mobPoint, rule.playerPoint, rule.onEntry ? "On Entry" : "Manual"});
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override public void windowClosed(java.awt.event.WindowEvent event) { refresh.stop(); }
            });
            refresh.start();
        }

        private void finishEdit() { if (table.isEditing()) table.getCellEditor().stopCellEditing(); }
        private Rule readRule(int row) {
            try {
                return new Rule(Boolean.TRUE.equals(model.getValueAt(row, 0)), value(row, 1), Integer.parseInt(value(row, 2).trim()),
                        value(row, 3), value(row, 4), "On Entry".equals(value(row, 5)));
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Row " + (row + 1) + ": " + exception.getMessage());
            }
        }
        private String value(int row, int column) { return Objects.toString(model.getValueAt(row, column), ""); }
        private void addButton(JPanel panel, String title, Runnable action) {
            JButton button = new JButton(title); button.setFocusPainted(false);
            button.setBackground(new Color(0x303630)); button.setForeground(new Color(0xd8d8d8));
            button.addActionListener(event -> action.run()); panel.add(button);
        }
    }
}
