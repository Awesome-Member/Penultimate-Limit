package bridge.visuals;

import Moduls.InventoryItem;
import Moduls.StrategAnimGroup;
import Moduls.StrategGraphic;
import Moduls.StrategRace;
import Moduls.Strategist;
import applet.Main;
import bridge.ui.ClickGui;
import desktop.graphics.GameGraphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public final class SkinChanger extends ClickGui.Feature {
    public static final SkinChanger INSTANCE = new SkinChanger();
    private volatile short[] selected;
    private Wardrobe window;

    private SkinChanger() {
        super(ClickGui.Category.VISUALS, "Skin Changer", "just skin changer");
        button("Open wardrobe", this::open);
        onToggle(() -> { if (enabled) open(); });
    }

    public short[] appearance(short[] original) {
        short[] skin = selected;
        if (!enabled || skin == null) return original;
        Strategist player = Strategist.instance;
        if (player.avatarGraphic != null && original == player.avatarGraphic.itemsSprites) return skin;
        return original;
    }

    private void open() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::open);
            return;
        }
        if (StrategAnimGroup.strategAnimGroups == null) {
            JOptionPane.showMessageDialog(Main.instance, "Load your character first.", "Skin Changer", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (window == null || !window.isDisplayable()) window = new Wardrobe();
        window.setVisible(true);
        window.toFront();
    }

    private static StrategGraphic graphic(short[] sprites) {
        StrategGraphic graphic = new StrategGraphic();
        graphic.itemsSprites = sprites.clone();
        return graphic;
    }

    private static void paintSkin(Graphics2D destination, short[] sprites, int width, int height, long time, byte direction) {
        StrategGraphic graphic = graphic(sprites);
        int[] bounds = graphic.getPaddings(direction, StrategAnimGroup.ANIMATION_STATE_STAY, false);
        int bodyWidth = Math.max(1, bounds[0] + bounds[1]);
        int bodyHeight = Math.max(1, bounds[2] + bounds[3]);
        double scale = Math.min(4, Math.min((width - 18.0) / bodyWidth, (height - 18.0) / bodyHeight));
        Graphics2D drawing = (Graphics2D)destination.create();
        try {
            drawing.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            drawing.translate((width - bodyWidth * scale) / 2, (height - bodyHeight * scale) / 2);
            drawing.scale(scale, scale);
            GameGraphics surface = new GameGraphics(drawing, bodyWidth + 1, bodyHeight + 1);
            StrategGraphic.draw(surface, sprites, (byte)-1, bounds[0], bounds[2], direction,
                    StrategAnimGroup.ANIMATION_STATE_STAY, time, true, (short)0, false);
        } finally { drawing.dispose(); }
    }

    private static final class Look {
        final String name;
        final short[] sprites;
        final boolean[] layers;
        Icon icon;
        Look(String name, short[] sprites, boolean[] layers) {
            this.name = name; this.sprites = sprites.clone(); this.layers = layers;
        }
        short[] apply(short[] base) {
            short[] result = base.clone();
            for (int i = 0; i < result.length; i++) if (layers == null || layers[i]) result[i] = sprites[i];
            return result;
        }
        Icon icon() {
            if (icon == null) {
                BufferedImage image = new BufferedImage(92, 86, BufferedImage.TYPE_INT_ARGB);
                Graphics2D drawing = image.createGraphics();
                try { paintSkin(drawing, sprites, 92, 86, 0, (byte)0); }
                finally { drawing.dispose(); }
                icon = new ImageIcon(image);
            }
            return icon;
        }
    }

    private final class Wardrobe extends JDialog {
        private final Color background = new Color(0x202320), card = new Color(0x292d29), accent = new Color(0x4269f5);
        private final List<Look> catalogue = new ArrayList<>();
        private final DefaultListModel<Look> model = new DefaultListModel<>();
        private final JList<Look> grid = new JList<>(model);
        private final JComboBox<String> category = new JComboBox<>(new String[]{"Looks", "Items", "Parts"});
        private final JComboBox<String> layer = new JComboBox<>();
        private final JTextField search = new JTextField();
        private final JLabel title = new JLabel("Current Look");
        private short[] draft = selected == null ? Strategist.instance.avatarGraphic.itemsSprites.clone() : selected.clone();
        private short[] beforeSelection = draft.clone();
        private byte direction;
        private final JPanel preview = new JPanel() {
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                paintSkin((Graphics2D)graphics, draft, getWidth(), getHeight(), System.nanoTime() / 1_000_000L, direction);
            }
        };
        private final Timer animation = new Timer(80, event -> preview.repaint());

        Wardrobe() {
            super(SwingUtilities.getWindowAncestor(Main.instance), "Skin Changer", Dialog.ModalityType.MODELESS);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(760, 480));
            setSize(960, 620);
            setLocationRelativeTo(Main.instance);
            JPanel root = new JPanel(new BorderLayout(16, 16));
            root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            root.setBackground(background);
            setContentPane(root);
            JPanel toolbar = new JPanel(new BorderLayout(10, 0));
            toolbar.add(category, BorderLayout.WEST);
            search.setToolTipText("Search...");
            toolbar.add(search, BorderLayout.CENTER);
            for (int i = 0; i < 17; i++) layer.addItem("Layer " + (i + 1));
            layer.setVisible(false);
            toolbar.add(layer, BorderLayout.EAST);
            root.add(toolbar, BorderLayout.NORTH);

            grid.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            grid.setVisibleRowCount(-1);
            grid.setFixedCellWidth(120);
            grid.setFixedCellHeight(120);
            grid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            grid.setBackground(background);
            grid.setCellRenderer((list, look, index, isSelected, focus) -> {
                JLabel label = new JLabel(look.name, look.icon(), SwingConstants.CENTER);
                label.setHorizontalTextPosition(SwingConstants.CENTER);
                label.setVerticalTextPosition(SwingConstants.BOTTOM);
                label.setOpaque(true);
                label.setBackground(isSelected ? new Color(0x303e65) : card);
                label.setForeground(new Color(0xd8d8d8));
                label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(isSelected ? accent : background, 3),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));
                label.setToolTipText(look.name);
                return label;
            });
            JScrollPane scroll = new JScrollPane(grid);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            root.add(scroll, BorderLayout.CENTER);

            JPanel right = new JPanel(new BorderLayout(8, 12));
            right.setPreferredSize(new Dimension(260, 0));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            right.add(title, BorderLayout.NORTH);
            preview.setBackground(new Color(0x181b18));
            right.add(preview, BorderLayout.CENTER);
            JPanel buttons = new JPanel(new GridLayout(0, 1, 0, 8));
            JButton rotate = new JButton("Rotate");
            rotate.addActionListener(event -> { direction = (byte)(1 - direction); preview.repaint(); });
            JButton apply = new JButton("Apply");
            apply.addActionListener(event -> {
                selected = draft.clone(); enabled = true; beforeSelection = draft.clone();
                title.setText("Look applied");
            });
            JButton reset = new JButton("Reset Look");
            reset.addActionListener(event -> {
                selected = null;
                draft = Strategist.instance.avatarGraphic.itemsSprites.clone();
                beforeSelection = draft.clone(); grid.clearSelection(); title.setText("Current Look"); preview.repaint();
            });
            JLabel note = new JLabel("Visible only to you", SwingConstants.CENTER);
            buttons.add(rotate); buttons.add(apply); buttons.add(reset); buttons.add(note);
            right.add(buttons, BorderLayout.SOUTH);
            root.add(right, BorderLayout.EAST);
            theme(root);

            grid.addListSelectionListener(event -> {
                if (event.getValueIsAdjusting()) return;
                Look look = grid.getSelectedValue();
                if (look == null) return;
                draft = look.apply(beforeSelection);
                title.setText(look.name);
                preview.repaint();
            });
            category.addActionListener(event -> { beforeSelection = draft.clone(); loadCatalogue(); });
            layer.addActionListener(event -> { beforeSelection = draft.clone(); loadCatalogue(); });
            search.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent event) { filter(); }
                public void removeUpdate(DocumentEvent event) { filter(); }
                public void changedUpdate(DocumentEvent event) { filter(); }
            });
            addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent event) { animation.stop(); }
            });
            loadCatalogue();
            animation.start();
        }

        private void theme(Component component) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            component.setForeground(new Color(0xd8d8d8));
            if (component instanceof JPanel && component != preview) component.setBackground(background);
            if (component instanceof JButton || component instanceof JTextField || component instanceof JComboBox) component.setBackground(card);
            if (component instanceof Container) for (Component child : ((Container)component).getComponents()) theme(child);
        }

        private void loadCatalogue() {
            catalogue.clear();
            int mode = category.getSelectedIndex();
            layer.setVisible(mode == 2);
            Strategist player = Strategist.instance;
            if (mode == 0 && StrategRace.strategRaces != null) {
                for (int i = 0; i < StrategRace.strategRaces.length; i++) {
                    StrategRace race = StrategRace.strategRaces[i];
                    if (race == null) continue;
                    for (int gender = 0; gender < 2; gender++) {
                        catalogue.add(new Look(race.names[gender], race.getStrategGraphic(gender).itemsSprites, null));
                    }
                }
            } else if (mode == 1) {
                for (Object entry : player.items.toArray()) {
                    if (!(entry instanceof InventoryItem)) continue;
                    InventoryItem item = (InventoryItem)entry;
                    StrategGraphic graphic = item.createStrategGraphic(player.GP_Race, player.GP_Gender);
                    if (graphic == null) continue;
                    boolean[] layers = new boolean[17];
                    for (byte slot : item.setSpritesSlot) if (slot >= 0 && slot < 17) layers[slot] = true;
                    catalogue.add(new Look(item.name == null ? "Item " + item.id : item.name, graphic.itemsSprites, layers));
                }
            } else if (mode == 2) {
                int slot = layer.getSelectedIndex();
                boolean[] layers = new boolean[17]; layers[slot] = true;
                short[] blank = new short[17]; Arrays.fill(blank, (short)-1);
                catalogue.add(new Look("Hide Layer", blank, layers));
                StrategAnimGroup[] groups = StrategAnimGroup.strategAnimGroups;
                for (int i = 0; i < groups.length && i <= Short.MAX_VALUE; i++) {
                    if (groups[i] == null || groups[i].animations[StrategAnimGroup.ANIMATION_STATE_STAY] == null) continue;
                    short[] sprites = blank.clone(); sprites[slot] = (short)i;
                    catalogue.add(new Look("Part #" + i, sprites, layers));
                }
            }
            filter();
        }

        private void filter() {
            model.clear();
            String query = search.getText().trim().toLowerCase(java.util.Locale.ROOT);
            for (Look look : catalogue) if (look.name.toLowerCase(java.util.Locale.ROOT).contains(query)) model.addElement(look);
        }
    }
}
