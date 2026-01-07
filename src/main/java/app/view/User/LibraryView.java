package app.view.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.Controller.TransactionController;

public class LibraryView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TransactionController transactionController;
    private Runnable onDataChanged;

    // Search
    private JTextField txtSearch;
    private List<Object[]> libraryCache = new ArrayList<>();

    // Dark theme colors
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_TABLE = new Color(45, 45, 45);
    private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color TEXT_SECONDARY = new Color(150, 150, 150);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_YELLOW = new Color(255, 193, 7);
    private static final Color ACCENT_RED = new Color(244, 67, 54);
    private static final Color BTN_BLUE = new Color(66, 133, 244);

    public LibraryView() {
        this.transactionController = new TransactionController();
        initUI();
        refreshData();
    }

    public void setOnDataChanged(Runnable callback) {
        this.onDataChanged = callback;
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_PANEL);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("MY GAME LIBRARY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        panel.add(lblTitle, BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 30));
        txtSearch.setBackground(BG_TABLE);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterLibraryTable(txtSearch.getText());
            }
        });

        JPanel right = new JPanel(new BorderLayout(5, 0));
        right.setBackground(BG_PANEL);

        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setForeground(TEXT_PRIMARY);

        JLabel lblCount = new JLabel();
        lblCount.setName("gameCount");
        lblCount.setForeground(TEXT_SECONDARY);

        JPanel searchWrap = new JPanel(new BorderLayout(5, 0));
        searchWrap.setBackground(BG_PANEL);
        searchWrap.add(lblSearch, BorderLayout.WEST);
        searchWrap.add(txtSearch, BorderLayout.CENTER);

        right.add(searchWrap, BorderLayout.NORTH);
        right.add(lblCount, BorderLayout.SOUTH);

        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "ID", "Nama Game", "Genre", "Developer", "Harga", "Status" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(BG_TABLE);
        table.setForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(BTN_BLUE);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(BG_TABLE_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // Status renderer
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    comp.setForeground("Played".equals(v) ? ACCENT_GREEN : ACCENT_YELLOW);
                    comp.setBackground(BG_TABLE);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        });

        // Price renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setText(formatCurrency((Integer) v));
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (!sel) {
                    comp.setForeground(Color.WHITE);
                    comp.setBackground(BG_TABLE);
                }
                return comp;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_TABLE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return scroll;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);

        JButton btnPlay = createButton("Play Game", ACCENT_GREEN);
        JButton btnRemove = createButton("Remove", ACCENT_RED);
        JButton btnRefresh = createButton("Refresh", BTN_BLUE);

        btnPlay.addActionListener(e -> playSelectedGame());
        btnRemove.addActionListener(e -> removeSelectedGame());
        btnRefresh.addActionListener(e -> refreshData());

        panel.add(btnPlay);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnRemove);
        panel.add(Box.createHorizontalGlue());
        panel.add(btnRefresh);

        return panel;
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void filterLibraryTable(String keyword) {
        model.setRowCount(0);
        String q = keyword.toLowerCase().trim();

        for (Object[] g : libraryCache) {
            String name = ((String) g[1]).toLowerCase();
            String genre = ((String) g[2]).toLowerCase();
            String dev = ((String) g[3]).toLowerCase();

            if (q.isEmpty() || name.contains(q) || genre.contains(q) || dev.contains(q)) {
                boolean played = (boolean) g[5];
                model.addRow(new Object[] {
                        g[0], g[1], g[2], g[3], g[4], played ? "Played" : "Not Played"
                });
            }
        }
        updateGameCount(model.getRowCount());
    }

    public void refreshData() {
        model.setRowCount(0);
        libraryCache = transactionController.getOwnedGames();

        for (Object[] g : libraryCache) {
            boolean played = (boolean) g[5];
            model.addRow(new Object[] {
                    g[0], g[1], g[2], g[3], g[4], played ? "Played" : "Not Played"
            });
        }
        updateGameCount(libraryCache.size());

        if (!txtSearch.getText().trim().isEmpty()) {
            filterLibraryTable(txtSearch.getText());
        }
    }

    private void updateGameCount(int count) {
        for (Component c : ((JPanel) getComponent(0)).getComponents()) {
            if (c instanceof JPanel) {
                for (Component cc : ((JPanel) c).getComponents()) {
                    if (cc instanceof JLabel && "gameCount".equals(cc.getName())) {
                        ((JLabel) cc).setText(count + " game" + (count != 1 ? "s" : "") + " in library");
                    }
                }
            }
        }
    }

    private void playSelectedGame() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        int id = (int) model.getValueAt(r, 0);
        String name = (String) model.getValueAt(r, 1);

        if (transactionController.playGame(id, name)) {
            refreshData();
        }
    }

    private void removeSelectedGame() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        int id = (int) model.getValueAt(r, 0);
        String name = (String) model.getValueAt(r, 1);

        if (transactionController.removeFromLibrary(id, name)) {
            refreshData();
            if (onDataChanged != null) onDataChanged.run();
        }
    }

    private String formatCurrency(int amount) {
        return String.format("Rp %,d", amount);
    }
}
