package app.view.Developer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.Controller.GameController;
import app.Controller.GenreController;
import app.Util.Session;

public class GameView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private GameController gameController;
    private GenreController genreController;

    // Search
    private JTextField txtSearch;
    private List<Object[]> gameCache = new ArrayList<>();

    // Dark theme colors
    private static final Color BG_DARK = new Color(27, 40, 56);
    private static final Color BG_DARKER = new Color(23, 26, 33);
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_TABLE = new Color(45, 45, 45);
    private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_BLUE = new Color(102, 192, 244);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_RED = new Color(183, 28, 28);
    private static final Color ACCENT_ORANGE = new Color(255, 152, 0);
    private static final Color BTN_BLUE = new Color(66, 133, 244);
    private static final Color BTN_GRAY = new Color(100, 100, 100);

    // Helper class genre
    private static class GenreItem {
        int id;
        String name;

        GenreItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public GameView() {
        gameController = new GameController();
        genreController = new GenreController();

        setTitle("SITEAM - Game Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARKER);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Game Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(ACCENT_BLUE);

        JButton btnLogout = createStyledButton("Logout", ACCENT_RED);
        btnLogout.addActionListener(e -> handleLogout());

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(btnLogout, BorderLayout.EAST);
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(createToolbar(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    // ================= TOOLBAR =================
    private JPanel createToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);

        JButton btnAdd = createStyledButton("Tambah", ACCENT_GREEN);
        JButton btnEdit = createStyledButton("Edit", ACCENT_ORANGE);
        JButton btnDelete = createStyledButton("Hapus", ACCENT_RED);
        JButton btnRefresh = createStyledButton("Refresh", BTN_GRAY);
        JButton btnAddGenre = createStyledButton("Tambah Genre", BTN_BLUE);

        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshData());
        btnAddGenre.addActionListener(e -> addGenre());

        panel.add(btnAdd);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnEdit);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnDelete);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnRefresh);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnAddGenre);

        // ===== SEARCH (UI FIX TOTAL) =====
        panel.add(Box.createHorizontalGlue());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(BG_PANEL);
        searchPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setForeground(TEXT_PRIMARY);
        lblSearch.setAlignmentY(Component.CENTER_ALIGNMENT);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 30));
        txtSearch.setMaximumSize(new Dimension(220, 30));
        txtSearch.setMinimumSize(new Dimension(220, 30));
        txtSearch.setBackground(BG_TABLE);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setAlignmentY(Component.CENTER_ALIGNMENT);

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterGameTable(txtSearch.getText());
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        searchPanel.add(txtSearch);

        panel.add(searchPanel);
        // =================================

        return panel;
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Nama", "Genre", "Harga", "GenreID" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setBackground(BG_TABLE);
        table.setForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(BTN_BLUE);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(BG_TABLE_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);

        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(formatCurrency((Integer) v));
                return super.getTableCellRendererComponent(t, getText(), sel, foc, r, c);
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_TABLE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return scroll;
    }

    // ================= LOGIC =================
    private void refreshData() {
        tableModel.setRowCount(0);
        gameCache = gameController.getGamesByCurrentDeveloper();
        for (Object[] g : gameCache) tableModel.addRow(g);
        if (!txtSearch.getText().isEmpty()) filterGameTable(txtSearch.getText());
    }

    private void filterGameTable(String keyword) {
        tableModel.setRowCount(0);
        String q = keyword.toLowerCase().trim();
        for (Object[] g : gameCache) {
            if (g[1].toString().toLowerCase().contains(q) ||
                g[2].toString().toLowerCase().contains(q)) {
                tableModel.addRow(g);
            }
        }
    }

    private void editSelected() {
        int r = table.getSelectedRow();
        if (r >= 0)
            showForm(new Object[] {
                tableModel.getValueAt(r, 0),
                tableModel.getValueAt(r, 1),
                tableModel.getValueAt(r, 4),
                tableModel.getValueAt(r, 3)
            });
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            gameController.deleteGame((int) tableModel.getValueAt(r, 0));
            refreshData();
        }
    }

    private void addGenre() {
        String name = JOptionPane.showInputDialog(this, "Nama genre:");
        if (name != null && !name.trim().isEmpty())
            genreController.addGenre(name.trim());
    }

    // ================= FORM =================
    private void showForm(Object[] data) {
        JDialog d = new JDialog(this, data == null ? "Tambah Game" : "Edit Game", true);
        d.setSize(420, 320);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(BG_PANEL);

        JLabel l1 = label("Nama Game");
        JLabel l2 = label("Genre");
        JLabel l3 = label("Harga");

        JTextField name = field();
        JTextField price = field();
        JComboBox<GenreItem> genre = new JComboBox<>();

        for (Object[] g : genreController.getAllGenres())
            genre.addItem(new GenreItem((int) g[0], (String) g[1]));

        if (data != null) {
            name.setText(data[1].toString());
            price.setText(data[3].toString());
        }

        p.add(l1); p.add(name);
        p.add(l2); p.add(genre);
        p.add(l3); p.add(price);

        JButton save = createStyledButton("Simpan", ACCENT_GREEN);
        save.addActionListener(e -> {
            try {
                int devId = Session.getInstance().getUser().getId();
                GenreItem g = (GenreItem) genre.getSelectedItem();
                if (data == null)
                    gameController.addGame(name.getText(), g.id, devId, Integer.parseInt(price.getText()));
                else
                    gameController.updateGame((int) data[0], name.getText(), g.id, Integer.parseInt(price.getText()));
                d.dispose();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Input tidak valid");
            }
        });

        d.add(p, BorderLayout.CENTER);
        d.add(save, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // ================= UTIL =================
    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(BG_TABLE);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        return f;
    }

    private JButton createStyledButton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        return b;
    }

    private void handleLogout() {
        dispose();
        new app.view.Auth.LoginView().setVisible(true);
    }

    private String formatCurrency(int a) {
        return String.format("Rp %,d", a);
    }
}
