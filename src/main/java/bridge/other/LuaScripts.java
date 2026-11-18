package bridge.other;

import applet.Main;
import bridge.ui.ClickGui;
import bridge.scripting.LuaRuntime;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

public final class LuaScripts extends ClickGui.Feature {
    public static final LuaScripts INSTANCE = new LuaScripts();
    private Editor window;

    private LuaScripts() {
        super(ClickGui.Category.OTHER, "Lua Scripts", "Lua script editor");
        button("Open editor", this::open);
        action(this::open);
    }

    private void open() {
        if (!SwingUtilities.isEventDispatchThread()) { SwingUtilities.invokeLater(this::open); return; }
        if (window == null || !window.isDisplayable()) window = new Editor();
        window.setVisible(true); window.toFront(); window.code.requestFocusInWindow();
    }

    private static final class Editor extends JDialog {
        private final Path directory = Paths.get("Entropy", "scripts").toAbsolutePath();
        private final RSyntaxTextArea code = new RSyntaxTextArea();
        private final JLabel status = new JLabel("Lua · Stop");
        private final JTextArea console = new JTextArea();
        private final JLabel position = new JLabel("1:1");
        private Path file;
        private boolean modified;
        private int runId;

        Editor() {
            super(Main.instance == null ? null : SwingUtilities.getWindowAncestor(Main.instance),
                    "Lua Scripts", Dialog.ModalityType.MODELESS);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setMinimumSize(new Dimension(660, 420)); setSize(940, 640); setLocationRelativeTo(Main.instance);
            JPanel root = new JPanel(new BorderLayout(0, 12));
            root.setBackground(new Color(0x202320));
            root.setBorder(BorderFactory.createEmptyBorder(14, 14, 12, 14)); setContentPane(root);
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); toolbar.setOpaque(false);
            button(toolbar, "New", this::newFile);
            button(toolbar, "Open", this::openFile);
            button(toolbar, "Save", this::saveFile);
            button(toolbar, "Run", () -> {
                console.setText("");
                status.setText("Running");
                int run = ++runId;
                LuaRuntime.INSTANCE.start(code.getText(), file == null ? "editor.lua" : file.toString(),
                        message -> log(run, message), state -> SwingUtilities.invokeLater(() -> {
                            if (run == runId) status.setText(state);
                        }));
            });
            button(toolbar, "Stop", () -> { LuaRuntime.INSTANCE.stop(); status.setText("Stopped"); });
            root.add(toolbar, BorderLayout.NORTH);

            code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
            code.setCodeFoldingEnabled(true);
            code.setAutoIndentEnabled(true);
            code.setTabSize(4); code.setTabsEmulated(true);
            code.setBracketMatchingEnabled(true);
            code.setAntiAliasingEnabled(true);
            try (InputStream theme = RSyntaxTextArea.class.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml")) {
                if (theme != null) Theme.load(theme).apply(code);
            } catch (IOException exception) { status.setText("Could not load theme: " + exception.getMessage()); }
            code.setFont(new Font("Consolas", Font.PLAIN, 15));
            code.setBackground(new Color(0x252925));
            code.setCurrentLineHighlightColor(new Color(0x303630));
            RTextScrollPane scroll = new RTextScrollPane(code);
            scroll.setLineNumbersEnabled(true);
            scroll.setFoldIndicatorEnabled(true);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(0x383e38)));
            scroll.getGutter().setBackground(new Color(0x202320));
            console.setEditable(false); console.setFont(new Font("Consolas", Font.PLAIN, 12));
            console.setBackground(new Color(0x1c201c)); console.setForeground(new Color(0xc3ccc3));
            console.setMargin(new Insets(8, 8, 8, 8));
            JScrollPane consoleScroll = new JScrollPane(console);
            consoleScroll.setBorder(BorderFactory.createLineBorder(new Color(0x383e38)));
            JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, consoleScroll);
            split.setBorder(null); split.setResizeWeight(0.8); split.setDividerLocation(370);
            root.add(split, BorderLayout.CENTER);

            JPanel footer = new JPanel(new BorderLayout(12, 0)); footer.setOpaque(false);
            status.setForeground(new Color(0xa6b0a6)); position.setForeground(new Color(0xa6b0a6));
            footer.add(status, BorderLayout.CENTER); footer.add(position, BorderLayout.EAST); root.add(footer, BorderLayout.SOUTH);
            code.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent event) { changed(); }
                public void removeUpdate(DocumentEvent event) { changed(); }
                public void changedUpdate(DocumentEvent event) { }
                private void changed() { modified = true; updateTitle(); }
            });
            code.addCaretListener(event -> {
                try {
                    int line = code.getLineOfOffset(code.getCaretPosition());
                    position.setText((line + 1) + ":" + (code.getCaretPosition() - code.getLineStartOffset(line) + 1));
                } catch (javax.swing.text.BadLocationException ignored) { }
            });
            shortcut("control N", "new", this::newFile);
            shortcut("control O", "open", this::openFile);
            shortcut("control S", "save", this::saveFile);
        }

        private void log(int run, String message) {
            SwingUtilities.invokeLater(() -> {
                if (run != runId) return;
                console.append(message + "\n");
                if (console.getDocument().getLength() > 30000) console.replaceRange("", 0, console.getDocument().getLength() - 20000);
                console.setCaretPosition(console.getDocument().getLength());
            });
        }

        private void updateTitle() {
            setTitle("Lua Scripts — " + (file == null ? "untitled.lua" : file.getFileName()) + (modified ? " *" : ""));
        }

        private void setDocument(Path path, String text) {
            file = path; code.setText(text); code.setCaretPosition(0); code.discardAllEdits();
            modified = false; updateTitle();
        }

        private boolean canReplace() {
            if (!modified) return true;
            int answer = JOptionPane.showConfirmDialog(this, "Save changes?", "Lua Scripts", JOptionPane.YES_NO_CANCEL_OPTION);
            return answer == JOptionPane.NO_OPTION || answer == JOptionPane.YES_OPTION && saveFile();
        }

        private void newFile() {
            if (canReplace()) { setDocument(null, ""); status.setText("New Lua file"); }
        }

        private JFileChooser chooser() throws IOException {
            Files.createDirectories(directory);
            JFileChooser chooser = new JFileChooser(directory.toFile());
            chooser.setFileFilter(new FileNameExtensionFilter("Lua scripts (*.lua)", "lua"));
            return chooser;
        }

        private void openFile() {
            if (!canReplace()) return;
            try {
                JFileChooser chooser = chooser();
                if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
                Path selected = chooser.getSelectedFile().toPath();
                String text = new String(Files.readAllBytes(selected), StandardCharsets.UTF_8);
                if (text.startsWith("\uFEFF")) text = text.substring(1);
                setDocument(selected, text); status.setText("Opened: " + file);
            } catch (IOException exception) { status.setText("Could not open: " + exception.getMessage()); }
        }

        private boolean saveFile() {
            try {
                Path destination = file;
                if (destination == null) {
                    JFileChooser chooser = chooser(); chooser.setSelectedFile(directory.resolve("script.lua").toFile());
                    if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return false;
                    destination = chooser.getSelectedFile().toPath();
                    if (!destination.getFileName().toString().toLowerCase(java.util.Locale.ROOT).endsWith(".lua"))
                        destination = destination.resolveSibling(destination.getFileName() + ".lua");
                    if (Files.exists(destination) && JOptionPane.showConfirmDialog(this, "Replace file " + destination.getFileName() + "?",
                            "Lua Scripts", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return false;
                }
                Path temporary = Files.createTempFile(destination.toAbsolutePath().getParent(), ".entropy-lua-", ".tmp");
                try {
                    Files.write(temporary, code.getText().getBytes(StandardCharsets.UTF_8));
                    Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
                } finally { Files.deleteIfExists(temporary); }
                file = destination; modified = false; updateTitle(); status.setText("Saved: " + file); return true;
            } catch (IOException exception) { status.setText("Could not save: " + exception.getMessage()); return false; }
        }

        private void shortcut(String key, String name, Runnable action) {
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), name);
            getRootPane().getActionMap().put(name, new AbstractAction() {
                public void actionPerformed(ActionEvent event) { action.run(); }
            });
        }

        private void button(JPanel panel, String name, Runnable action) {
            JButton button = new JButton(name); button.setFocusPainted(false);
            button.setBackground(new Color(0x303630)); button.setForeground(new Color(0xd8d8d8));
            button.addActionListener(event -> action.run()); panel.add(button);
        }
    }
}
