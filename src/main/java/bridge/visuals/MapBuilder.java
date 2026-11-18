package bridge.visuals;

import Base.Com;
import Base.GameCommons;
import Engine.AnimSprite;
import Moduls.Decor;
import Moduls.Tile;
import Moduls.TileMap;
import Moduls.TileMapCell;
import Moduls.TileMapElement;
import applet.Main;
import bridge.ui.ClickGui;
import desktop.graphics.GameGraphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class MapBuilder extends ClickGui.Feature {
    public static final MapBuilder INSTANCE = new MapBuilder();
    private Editor window;
    private final AtomicReference<Runnable> pending = new AtomicReference<>();
    private TileMap editedMap;
    private short editedMapIndex;
    private TileMapCell[][] editedCells;
    private final Map<Long, Original> originals = new HashMap<>();

    private MapBuilder() {
        super(ClickGui.Category.VISUALS, "Map Builder", "Build your own local map");
        button("Open editor", this::open);
        onToggle(() -> { if (enabled) open(); else pending.set(this::restore); });
    }

    public void update() {
        if (editedMap != null && (editedMap != Com.currentGameMap || editedMap.serverIndex != editedMapIndex || editedMap.cells != editedCells)) discardMap(editedMap);
        Runnable task = pending.getAndSet(null);
        if (task != null) task.run();
    }

    public void discardMap(TileMap map) {
        if (editedMap == map) { originals.clear(); editedMap = null; editedCells = null; }
    }

    private void open() {
        if (!SwingUtilities.isEventDispatchThread()) { SwingUtilities.invokeLater(this::open); return; }
        if (Com.currentGameMap == null || Com.currentGameMap.cells == null || Com.Tiles == null) {
            JOptionPane.showMessageDialog(Main.instance, "Load a map first.", "Map Builder", JOptionPane.INFORMATION_MESSAGE); return;
        }
        if (window == null || !window.isDisplayable()) window = new Editor();
        window.setVisible(true); window.toFront();
    }

    private static long key(int x, int y) { return (long)x << 32 | y & 0xffffffffL; }

    private static final class Original {
        final TileMapCell cell;
        final short tile, decor, v0, v1, h0, h1;
        final boolean blocked;
        Original(TileMapCell cell) {
            this.cell = cell; tile = cell.tileInd; decor = cell.decorTri;
            v0 = cell.borderV0; v1 = cell.borderV1; h0 = cell.borderH0; h1 = cell.borderH1; blocked = cell.block;
        }
        void restore() {
            cell.tileInd = tile; cell.decorTri = decor; cell.borderV0 = v0; cell.borderV1 = v1;
            cell.borderH0 = h0; cell.borderH1 = h1; cell.block = blocked;
        }
    }

    private void restore() {
        if (editedMap != null && editedMap == Com.currentGameMap && editedMap.serverIndex == editedMapIndex
                && editedMap.cells == editedCells) {
            for (Original original : originals.values()) original.restore();
            editedMap.initMiniMap();
            GameCommons.instance.redrawMapBuffer();
        }
        discardMap(editedMap);
    }

    private void apply(Draft draft, TileMap target, short mapIndex, int originX, int originY, Editor editor) {
        if (target == null || Com.currentGameMap != target || target.serverIndex != mapIndex || target.cells == null) {
            editor.status("Map changed before the draft could be applied."); return;
        }
        if (originX < 0 || originY < 0 || originX + draft.width > target.widthTl || originY + draft.height > target.heightTl) {
            editor.status("Draft does not fit at this position."); return;
        }
        for (int x = 0; x < draft.width; x++) for (int y = 0; y < draft.height; y++) {
            int i = x + y * draft.width;
            if (draft.tiles[i] < 0 || draft.tiles[i] >= Com.Tiles.length || draft.decors[i] < -1
                    || draft.decors[i] >= Com.Decors.length) {
                editor.status("Draft uses unavailable map assets."); return;
            }
        }
        restore(); editedMap = target; editedMapIndex = target.serverIndex; editedCells = target.cells;
        for (int x = 0; x < draft.width; x++) for (int y = 0; y < draft.height; y++) {
            int i = x + y * draft.width;
            TileMapCell cell = target.cells[originX + x][originY + y];
            originals.put(key(originX + x, originY + y), new Original(cell));
            cell.tileInd = draft.tiles[i]; cell.decorTri = draft.decors[i];
            cell.borderV0 = cell.borderV1 = cell.borderH0 = cell.borderH1 = -1;
            cell.block = draft.blocked[i];
        }
        enabled = true;
        target.initMiniMap(); GameCommons.instance.redrawMapBuffer();
        editor.status("");
    }

    private static final class Draft {
        final int width, height;
        final short[] tiles, decors;
        final boolean[] blocked;
        final List<TileMapElement> foreground = new ArrayList<>();
        Draft(int width, int height, short tile) {
            this.width = width; this.height = height;
            tiles = new short[width * height]; java.util.Arrays.fill(tiles, tile);
            decors = new short[width * height]; java.util.Arrays.fill(decors, (short)-1);
            blocked = new boolean[width * height];
            java.util.Arrays.fill(blocked, Com.Tiles[tile].GP_Block);
        }
        Draft copy() {
            Draft copy = new Draft(width, height, (short)0);
            System.arraycopy(tiles, 0, copy.tiles, 0, tiles.length);
            System.arraycopy(decors, 0, copy.decors, 0, decors.length);
            System.arraycopy(blocked, 0, copy.blocked, 0, blocked.length);
            copy.foreground.addAll(foreground);
            return copy;
        }
    }

    private final class Editor extends JDialog {
        private Draft draft;
        private final ArrayDeque<Draft> undo = new ArrayDeque<>(), redo = new ArrayDeque<>();
        private final Color background = new Color(0x202320), card = new Color(0x2b302b);
        private final JComboBox<String> layer = new JComboBox<>(new String[]{"Terrain", "Decor", "Collision"});
        private final JComboBox<String> tool = new JComboBox<>(new String[]{"Brush", "Fill", "Eraser"});
        private final JSpinner width = number(32, 1, 512), height = number(32, 1, 512);
        private final JSpinner originX = number(0, 0, 2048), originY = number(0, 0, 2048);
        private final JSpinner brush = number(1, 1, 12), zoom = number(32, 8, 96);
        private final JCheckBox grid = new JCheckBox("Grid", true), collision = new JCheckBox("Blocked Tiles", false);
        private final JLabel message = new JLabel("Left click to paint · Right click to sample · Scroll to zoom");
        private final DefaultListModel<Integer> paletteModel = new DefaultListModel<>();
        private final JList<Integer> palette = new JList<>(paletteModel);
        private final Map<String, Icon> icons = new HashMap<>();
        private int selectedTile, selectedDecor = -1;
        private final Canvas canvas = new Canvas();
        private final JPanel minimap = new JPanel() {
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (draft == null) return;
                double factor = Math.min(getWidth() / (double)draft.width, getHeight() / (double)draft.height);
                Graphics2D drawing = (Graphics2D)graphics.create();
                try {
                    drawing.scale(factor / value(zoom), factor / value(zoom));
                    canvas.paintMap(drawing, false);
                } finally { drawing.dispose(); }
            }
        };

        Editor() {
            super(SwingUtilities.getWindowAncestor(Main.instance), "Map Builder", Dialog.ModalityType.MODELESS);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); setSize(1180, 780);
            setMinimumSize(new Dimension(900, 580)); setLocationRelativeTo(Main.instance);
            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14)); setContentPane(root);
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
            toolbar.add(new JLabel("Size")); toolbar.add(width); toolbar.add(new JLabel("×")); toolbar.add(height);
            addButton(toolbar, "New", () -> { history(); draft = new Draft(value(width), value(height), (short)selectedTile); changed(); });
            addButton(toolbar, "Import Current Map", this::importCurrent);
            addButton(toolbar, "Open", this::load);
            addButton(toolbar, "Save", this::save);
            addButton(toolbar, "PNG", this::export);
            root.add(toolbar, BorderLayout.NORTH);

            JPanel left = new JPanel(new BorderLayout(6, 8)); left.setPreferredSize(new Dimension(240, 0));
            left.add(layer, BorderLayout.NORTH);
            palette.setLayoutOrientation(JList.HORIZONTAL_WRAP); palette.setVisibleRowCount(-1);
            palette.setFixedCellWidth(106); palette.setFixedCellHeight(98);
            palette.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            palette.setCellRenderer((list, id, index, selected, focused) -> {
                JLabel label = new JLabel(id < 0 ? "No Decor" : "#" + id, icon(id), SwingConstants.CENTER);
                label.setHorizontalTextPosition(SwingConstants.CENTER); label.setVerticalTextPosition(SwingConstants.BOTTOM);
                label.setOpaque(true); label.setBackground(selected ? new Color(0x303e65) : card);
                label.setForeground(new Color(0xd8d8d8)); label.setBorder(BorderFactory.createLineBorder(background, 3));
                return label;
            });
            palette.addListSelectionListener(event -> {
                Integer id = palette.getSelectedValue();
                if (id != null) { if (layer.getSelectedIndex() == 0) selectedTile = id; else selectedDecor = id; }
            });
            layer.addActionListener(event -> populate());
            left.add(new JScrollPane(palette), BorderLayout.CENTER); root.add(left, BorderLayout.WEST);
            JScrollPane viewport = new JScrollPane(canvas);
            viewport.setWheelScrollingEnabled(false); root.add(viewport, BorderLayout.CENTER);

            JPanel right = new JPanel(new BorderLayout(8, 12)); right.setPreferredSize(new Dimension(220, 0));
            JPanel settings = new JPanel(new GridLayout(0, 2, 6, 10));
            settings.add(new JLabel("Tool")); settings.add(tool);
            settings.add(new JLabel("Brush Size")); settings.add(brush);
            settings.add(new JLabel("Zoom")); settings.add(zoom);
            settings.add(grid); settings.add(collision);
            settings.add(new JLabel("Origin X")); settings.add(originX);
            settings.add(new JLabel("Origin Y")); settings.add(originY);
            addButton(settings, "Undo", () -> { if (!undo.isEmpty()) { redo.push(draft.copy()); draft = undo.pop(); changed(); } });
            addButton(settings, "Redo", () -> { if (!redo.isEmpty()) { undo.push(draft.copy()); draft = redo.pop(); changed(); } });
            right.add(settings, BorderLayout.NORTH);
            minimap.setPreferredSize(new Dimension(200, 160)); right.add(minimap, BorderLayout.CENTER);
            JPanel actions = new JPanel(new GridLayout(0, 1, 6, 8));
            addButton(actions, "Apply to Map", () -> {
                Draft snapshot = draft.copy(); TileMap target = Com.currentGameMap;
                short mapIndex = target == null ? -1 : target.serverIndex;
                int x = value(originX), y = value(originY);
                pending.set(() -> apply(snapshot, target, mapIndex, x, y, this));
                status("");
            });
            addButton(actions, "Restore Original", () -> pending.set(() -> { restore(); status(""); }));
            actions.add(new JLabel("Local changes only", SwingConstants.CENTER));
            right.add(actions, BorderLayout.SOUTH); root.add(right, BorderLayout.EAST);
            root.add(message, BorderLayout.SOUTH);
            zoom.addChangeListener(event -> changed()); grid.addActionListener(event -> canvas.repaint());
            collision.addActionListener(event -> canvas.repaint());
            theme(root); populate(); importCurrent();
        }

        private void status(String text) { SwingUtilities.invokeLater(() -> message.setText(text)); }
        private JSpinner number(int initial, int min, int max) { return new JSpinner(new SpinnerNumberModel(initial, min, max, 1)); }
        private int value(JSpinner spinner) { return ((Number)spinner.getValue()).intValue(); }
        private void addButton(JPanel panel, String text, Runnable action) {
            JButton button = new JButton(text); button.addActionListener(event -> action.run()); panel.add(button);
        }
        private void theme(Component component) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 12)); component.setForeground(new Color(0xd8d8d8));
            if (component instanceof JPanel || component instanceof JList || component instanceof JCheckBox) component.setBackground(background);
            if (component instanceof JButton || component instanceof JComboBox || component instanceof JTextField
                    || component instanceof JSpinner) component.setBackground(card);
            if (component instanceof Container) for (Component child : ((Container)component).getComponents()) theme(child);
        }
        private void history() {
            if (draft != null) { undo.push(draft.copy()); if (undo.size() > 20) undo.removeLast(); }
            redo.clear();
        }
        private void changed() {
            if (draft == null) return;
            width.setValue(draft.width); height.setValue(draft.height);
            canvas.setPreferredSize(new Dimension(draft.width * value(zoom), draft.height * value(zoom)));
            canvas.revalidate(); canvas.repaint(); minimap.repaint();
        }
        private void importCurrent() {
            TileMap map = Com.currentGameMap;
            if (map == null || map.cells == null) return;
            history(); draft = new Draft(Math.min(512, map.widthTl), Math.min(512, map.heightTl), (short)0);
            for (int y = 0; y < draft.height; y++) for (int x = 0; x < draft.width; x++) {
                TileMapCell cell = map.cells[x][y]; int i = x + y * draft.width;
                draft.tiles[i] = cell.tileInd; draft.decors[i] = cell.decorTri; draft.blocked[i] = cell.block;
                if (cell.decorsDraw != null) for (TileMapElement decor : cell.decorsDraw) {
                    if (decor != null && decor.sprite >= 0 && decor.sprite < Com.Decors.length) {
                        draft.foreground.add(new TileMapElement(decor.x, decor.y, decor.sprite, decor.addHeight));
                    }
                }
            }
            draft.foreground.sort(Comparator.comparingInt(decor -> decor.y + decor.addHeight + Com.Decors[decor.sprite].EncloseCoord));
            originX.setValue(0); originY.setValue(0); changed();
        }
        private void populate() {
            paletteModel.clear(); int mode = layer.getSelectedIndex();
            if (mode == 1) paletteModel.addElement(-1);
            int count = mode == 0 ? Com.Tiles.length : mode == 1 ? Com.Decors.length : 0;
            for (int i = 0; i < count && i <= Short.MAX_VALUE; i++) paletteModel.addElement(i);
        }
        private Icon icon(int id) {
            String key = layer.getSelectedIndex() + ":" + id;
            Icon cached = icons.get(key); if (cached != null) return cached;
            BufferedImage image = new BufferedImage(96, 68, BufferedImage.TYPE_INT_ARGB);
            if (id >= 0) {
                AnimSprite sprite = layer.getSelectedIndex() == 0 ? Com.Tiles[id].Self : Com.Decors[id].Self;
                Graphics2D graphics = image.createGraphics();
                try {
                    double scale = Math.min(2, Math.min(92.0 / sprite.sw, 64.0 / sprite.sh));
                    graphics.translate((96 - sprite.sw * scale) / 2, (68 - sprite.sh * scale) / 2); graphics.scale(scale, scale);
                    sprite.draw(new GameGraphics(graphics, sprite.sw, sprite.sh), 0, 0, 0);
                } finally { graphics.dispose(); }
            }
            cached = new ImageIcon(image); icons.put(key, cached); return cached;
        }

        private void recalculate(int index) {
            boolean blocked = Com.Tiles[draft.tiles[index]].GP_Block;
            int decorId = draft.decors[index];
            if (decorId >= 0) {
                Decor decor = Com.Decors[decorId];
                blocked = blocked && !decor.GP_UnBlock || decor.GP_Block;
            }
            draft.blocked[index] = blocked;
        }
        private void setCell(int x, int y) {
            int i = x + y * draft.width, mode = layer.getSelectedIndex();
            boolean erase = tool.getSelectedIndex() == 2;
            if (mode == 0) { draft.tiles[i] = (short)(erase ? 0 : selectedTile); recalculate(i); }
            else if (mode == 1) { draft.decors[i] = (short)(erase ? -1 : selectedDecor); recalculate(i); }
            else draft.blocked[i] = !erase;
        }
        private int cellValue(int index) {
            return layer.getSelectedIndex() == 0 ? draft.tiles[index] : layer.getSelectedIndex() == 1 ? draft.decors[index] : draft.blocked[index] ? 1 : 0;
        }
        private void fill(int startX, int startY) {
            int old = cellValue(startX + startY * draft.width);
            boolean[] visited = new boolean[draft.tiles.length]; ArrayDeque<Integer> queue = new ArrayDeque<>();
            queue.add(startX + startY * draft.width);
            while (!queue.isEmpty()) {
                int i = queue.removeFirst(); if (visited[i]) continue; visited[i] = true;
                if (cellValue(i) != old) continue;
                int x = i % draft.width, y = i / draft.width; setCell(x, y);
                if (x > 0) queue.add(i - 1); if (x + 1 < draft.width) queue.add(i + 1);
                if (y > 0) queue.add(i - draft.width); if (y + 1 < draft.height) queue.add(i + draft.width);
            }
        }

        private final class Canvas extends JPanel {
            private Point last;
            Canvas() {
                MouseAdapter input = new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent event) {
                        Point p = cell(event); if (p == null) return;
                        if (SwingUtilities.isRightMouseButton(event)) {
                            int id = cellValue(p.x + p.y * draft.width);
                            if (layer.getSelectedIndex() < 2) palette.setSelectedValue(id, true);
                            return;
                        }
                        if (!SwingUtilities.isLeftMouseButton(event)) return;
                        history(); last = p;
                        if (tool.getSelectedIndex() == 1) fill(p.x, p.y); else stroke(p, p);
                        repaint(); minimap.repaint();
                    }
                    @Override public void mouseDragged(MouseEvent event) {
                        Point p = cell(event); if (p == null || last == null || tool.getSelectedIndex() == 1) return;
                        stroke(last, p); last = p; repaint(); minimap.repaint();
                    }
                    @Override public void mouseReleased(MouseEvent event) { last = null; }
                    @Override public void mouseMoved(MouseEvent event) {
                        Point p = cell(event); if (p != null) message.setText("Tile " + p.x + ";" + p.y + " · Right click to sample");
                    }
                    @Override public void mouseWheelMoved(MouseWheelEvent event) {
                        zoom.setValue(Math.max(8, Math.min(96, value(zoom) - event.getWheelRotation() * 4)));
                    }
                };
                addMouseListener(input); addMouseMotionListener(input); addMouseWheelListener(input);
            }
            private Point cell(MouseEvent event) {
                if (draft == null) return null;
                int x = event.getX() / value(zoom), y = event.getY() / value(zoom);
                return x >= 0 && y >= 0 && x < draft.width && y < draft.height ? new Point(x, y) : null;
            }
            private void stroke(Point from, Point to) {
                int steps = Math.max(Math.abs(to.x - from.x), Math.abs(to.y - from.y));
                for (int step = 0; step <= steps; step++) {
                    int x = steps == 0 ? to.x : from.x + (to.x - from.x) * step / steps;
                    int y = steps == 0 ? to.y : from.y + (to.y - from.y) * step / steps;
                    for (int bx = 0; bx < value(brush) && x + bx < draft.width; bx++)
                        for (int by = 0; by < value(brush) && y + by < draft.height; by++) setCell(x + bx, y + by);
                }
            }
            @Override protected void paintComponent(Graphics drawing) {
                super.paintComponent(drawing);
                paintMap(drawing, true);
            }
            private void paintMap(Graphics drawing, boolean guides) {
                if (draft == null) return;
                int size = value(zoom); Rectangle clip = drawing.getClipBounds();
                int x0 = Math.max(0, clip.x / size), y0 = Math.max(0, clip.y / size);
                int x1 = Math.min(draft.width, (clip.x + clip.width) / size + 1);
                int y1 = Math.min(draft.height, (clip.y + clip.height) / size + 1);
                for (int y = y0; y < y1; y++) for (int x = x0; x < x1; x++) {
                    int i = x + y * draft.width;
                    Graphics2D graphics = (Graphics2D)drawing.create();
                    try {
                        graphics.translate(x * size, y * size); graphics.scale(size / (double)Tile.width, size / (double)Tile.height);
                        GameGraphics surface = new GameGraphics(graphics, Tile.width, Tile.height);
                        Com.Tiles[draft.tiles[i]].Self.draw(surface, 0, 0, 0);
                    } finally { graphics.dispose(); }
                }
                for (int y = Math.max(0, y0 - 8); y < Math.min(draft.height, y1 + 8); y++)
                    for (int x = Math.max(0, x0 - 8); x < Math.min(draft.width, x1 + 8); x++) {
                        int id = draft.decors[x + y * draft.width]; if (id < 0) continue;
                        Decor decor = Com.Decors[id]; Graphics2D graphics = (Graphics2D)drawing.create();
                        try {
                            graphics.translate(x * size, y * size); graphics.scale(size / (double)Tile.width, size / (double)Tile.height);
                            graphics.translate(Tile.widthHalf - decor.w2, Tile.heightHalf - decor.Offset);
                            decor.Self.draw(new GameGraphics(graphics, decor.width, decor.height), 0, 0, 0);
                        } finally { graphics.dispose(); }
                    }
                double scaleX = size / (double)Tile.width, scaleY = size / (double)Tile.height;
                for (TileMapElement element : draft.foreground) {
                    Decor decor = Com.Decors[element.sprite];
                    double left = (element.x - decor.w2) * scaleX;
                    double top = (element.y - decor.Offset) * scaleY;
                    if (!clip.intersects(left, top, decor.width * scaleX, decor.height * scaleY)) continue;
                    Graphics2D graphics = (Graphics2D)drawing.create();
                    try {
                        graphics.translate(left, top); graphics.scale(scaleX, scaleY);
                        decor.Self.draw(new GameGraphics(graphics, decor.width, decor.height), 0, 0, 0);
                    } finally { graphics.dispose(); }
                }
                if (!guides) return;
                for (int y = y0; y < y1; y++) for (int x = x0; x < x1; x++) {
                    if ((collision.isSelected() || layer.getSelectedIndex() == 2) && draft.blocked[x + y * draft.width]) {
                        drawing.setColor(new Color(255, 85, 105, 85)); drawing.fillRect(x * size, y * size, size, size);
                    }
                    if (grid.isSelected()) { drawing.setColor(new Color(0, 0, 0, 65)); drawing.drawRect(x * size, y * size, size, size); }
                }
            }
        }

        private JFileChooser chooser() {
            JFileChooser chooser = new JFileChooser(); chooser.setFileFilter(new FileNameExtensionFilter("Last Limit Map (*.llmap)", "llmap")); return chooser;
        }
        private void save() {
            JFileChooser chooser = chooser(); if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            File file = chooser.getSelectedFile(); if (!file.getName().toLowerCase().endsWith(".llmap")) file = new File(file.getPath() + ".llmap");
            try (DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                output.writeInt(0x4c4c4d31); output.writeInt(draft.width); output.writeInt(draft.height);
                for (int i = 0; i < draft.tiles.length; i++) { output.writeShort(draft.tiles[i]); output.writeShort(draft.decors[i]); output.writeBoolean(draft.blocked[i]); }
                status("Saved: " + file.getName());
            } catch (IOException error) { status("Could not save: " + error.getMessage()); }
        }
        private void load() {
            JFileChooser chooser = chooser(); if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            try (DataInputStream input = new DataInputStream(new BufferedInputStream(new FileInputStream(chooser.getSelectedFile())))) {
                if (input.readInt() != 0x4c4c4d31) throw new IOException("Invalid map format");
                int w = input.readInt(), h = input.readInt();
                if (w < 1 || h < 1 || w > 512 || h > 512) throw new IOException("Map dimensions must be between 1 and 512");
                Draft loaded = new Draft(w, h, (short)0);
                for (int i = 0; i < loaded.tiles.length; i++) {
                    loaded.tiles[i] = input.readShort(); loaded.decors[i] = input.readShort(); loaded.blocked[i] = input.readBoolean();
                    if (loaded.tiles[i] < 0 || loaded.tiles[i] >= Com.Tiles.length || loaded.decors[i] < -1 || loaded.decors[i] >= Com.Decors.length)
                        throw new IOException("Map assets are incompatible with this game version");
                }
                history(); draft = loaded; changed(); status("Map opened.");
            } catch (IOException error) { status("Could not open: " + error.getMessage()); }
        }
        private void export() {
            JFileChooser chooser = new JFileChooser(); chooser.setSelectedFile(new File("map-preview.png"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            int oldZoom = value(zoom), exportZoom = Math.max(1, Math.min(32, 4096 / Math.max(draft.width, draft.height)));
            try {
                zoom.setValue(Math.max(8, exportZoom));
                int size = value(zoom); BufferedImage image = new BufferedImage(draft.width * size, draft.height * size, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                try { graphics.setClip(0, 0, image.getWidth(), image.getHeight()); canvas.paintComponent(graphics); }
                finally { graphics.dispose(); }
                javax.imageio.ImageIO.write(image, "png", chooser.getSelectedFile()); status("Preview saved.");
            } catch (IOException error) { status("Could not save PNG: " + error.getMessage()); }
            finally { zoom.setValue(oldZoom); }
        }
    }
}
