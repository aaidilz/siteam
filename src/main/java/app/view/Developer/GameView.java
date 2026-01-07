package app.view.Developer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
        this.gameController = new GameController();
        this.genreController = new GenreController();

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
        panel.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = createStyledButton("Logout", ACCENT_RED);
        btnLogout.addActionListener(e -> handleLogout());
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

    private JPanel createToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnAdd = createStyledButton("Tambah", ACCENT_GREEN);
        JButton btnEdit = createStyledButton("Edit", ACCENT_ORANGE);
        JButton btnDelete = createStyledButton("Hapus", ACCENT_RED);
        JButton btnRefresh = createStyledButton("Refresh", BTN_GRAY);
        JButton btnAddGenre = createStyledButton("Tambah Genre", BTN_BLUE);

        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshData());

        btnAddGenre.addActionListener(e -> {
            String genreName = JOptionPane.showInputDialog(
                    this,
                    "Masukkan nama genre baru:",
                    "Tambah Genre",
                    JOptionPane.PLAIN_MESSAGE);

            if (genreName != null && !genreName.trim().isEmpty()) {
                genreController.addGenre(genreName.trim());
                JOptionPane.showMessageDialog(this, "Genre berhasil ditambahkan!");
            }
        });

        panel.add(btnAdd);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnEdit);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnDelete);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnRefresh);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnAddGenre);

        // ===== SEARCH PANEL (FIX UI) =====
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBackground(BG_PANEL);

        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setForeground(TEXT_PRIMARY);

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 32));
        txtSearch.setMaximumSize(new Dimension(200, 32));
        txtSearch.setBackground(BG_TABLE);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterGameTable(txtSearch.getText());
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        searchPanel.add(txtSearch);

        panel.add(Box.createHorizontalGlue());
        panel.add(searchPanel);
        // ================================

        return panel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Nama", "Genre", "Harga", "GenreID" }, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
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

        // Hide GenreID
        table.getColumnModel().getColumn(4).setMinWidth(0);
        table.getColumnModel().getColumn(4).setMaxWidth(0);

        // Price renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                setText(formatCurrency((Integer) v));
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(t, getText(), sel, foc, r, c);
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_TABLE);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return scroll;
    }

    private void refreshData() {
        tableModel.setRowCount(0);
        gameCache = gameController.getGamesByCurrentDeveloper();

        for (Object[] g : gameCache) {
            tableModel.addRow(g);
        }

        if (!txtSearch.getText().trim().isEmpty()) {
            filterGameTable(txtSearch.getText());
        }
    }

    private void filterGameTable(String keyword) {
        tableModel.setRowCount(0);
        String q = keyword.toLowerCase().trim();

        for (Object[] g : gameCache) {
            String name = ((String) g[1]).toLowerCase();
            String genre = ((String) g[2]).toLowerCase();

            if (q.isEmpty() || name.contains(q) || genre.contains(q)) {
                tableModel.addRow(g);
            }
        }
    }

    private void editSelected() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        int id = (int) tableModel.getValueAt(r, 0);
        String name = (String) tableModel.getValueAt(r, 1);
        int genreId = (int) tableModel.getValueAt(r, 4);
        int price = (int) tableModel.getValueAt(r, 3);

        showForm(new Object[] { id, name, genreId, price });
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        int id = (int) tableModel.getValueAt(r, 0);
        gameController.deleteGame(id);
        refreshData();
    }

    private void showForm(Object[] existingData) {
        JDialog dialog = new JDialog(this,
                existingData == null ? "Tambah Game" : "Edit Game", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtName = new JTextField();
        JTextField txtPrice = new JTextField();
        JComboBox<GenreItem> cmbGenre = new JComboBox<>();

        txtName.setBackground(BG_TABLE);
        txtName.setForeground(Color.WHITE);
        txtPrice.setBackground(BG_TABLE);
        txtPrice.setForeground(Color.WHITE);
        cmbGenre.setBackground(BG_TABLE);
        cmbGenre.setForeground(Color.WHITE);

        List<Object[]> genres = genreController.getAllGenres();
        for (Object[] g : genres) {
            GenreItem item = new GenreItem((int) g[0], (String) g[1]);
            cmbGenre.addItem(item);
            if (existingData != null && item.id == (int) existingData[2]) {
                cmbGenre.setSelectedItem(item);
            }
        }

        if (existingData != null) {
            txtName.setText((String) existingData[1]);
            txtPrice.setText(String.valueOf(existingData[3]));
        }

        panel.add(new JLabel("Nama Game"));
        panel.add(txtName);
        panel.add(new JLabel("Genre"));
        panel.add(cmbGenre);
        panel.add(new JLabel("Harga"));
        panel.add(txtPrice);

        JButton btnSave = createStyledButton("Simpan", ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            try {
                int price = Integer.parseInt(txtPrice.getText());
                int devId = Session.getInstance().getUser().getId();
                GenreItem g = (GenreItem) cmbGenre.getSelectedItem();

                if (existingData == null) {
                    gameController.addGame(txtName.getText(), g.id, devId, price);
                } else {
                    gameController.updateGame(
                            (int) existingData[0], txtName.getText(), g.id, price);
                }

                dialog.dispose();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Input tidak valid");
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            for (Window w : Window.getWindows()) w.dispose();
            new app.view.Auth.LoginView().setVisible(true);
        }
    }

    private String formatCurrency(int amount) {
        return String.format("Rp %,d", amount);
    }
}
