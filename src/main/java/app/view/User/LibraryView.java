package app.view.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.Controller.TransactionController;

/**
 * Library View - Game library panel
 * Shows owned games with play and remove functionality
 */
public class LibraryView extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TransactionController transactionController;
    private Runnable onDataChanged;

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

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Game Table
        JScrollPane tableScrollPane = createTablePanel();
        add(tableScrollPane, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("MY GAME LIBRARY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        panel.add(lblTitle, BorderLayout.WEST);

        JLabel lblCount = new JLabel();
        lblCount.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCount.setForeground(TEXT_SECONDARY);
        lblCount.setName("gameCount");
        panel.add(lblCount, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "ID", "Nama Game", "Genre", "Developer", "Harga", "Status" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
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

        // Style header
        table.getTableHeader().setBackground(BG_TABLE_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // Custom renderer for Status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("Played".equals(status)) {
                    if (!isSelected) {
                        c.setForeground(ACCENT_GREEN);
                    }
                } else {
                    if (!isSelected) {
                        c.setForeground(ACCENT_YELLOW);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Price column renderer
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setText(formatCurrency((Integer) value));
                setHorizontalAlignment(SwingConstants.RIGHT);
                if (!isSelected) {
                    c.setForeground(Color.WHITE);
                    c.setBackground(BG_TABLE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_TABLE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        return scrollPane;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnPlay = createStyledButton("Play Game", ACCENT_GREEN);
        JButton btnRemove = createStyledButton("Remove from Library", ACCENT_RED);
        JButton btnRefresh = createStyledButton("Refresh", BTN_BLUE);

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

    private void playSelectedGame() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih game yang ingin dimainkan!",
                    "Pilih Game",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameId = (int) model.getValueAt(selectedRow, 0);
        String gameName = (String) model.getValueAt(selectedRow, 1);

        if (transactionController.playGame(gameId, gameName)) {
            refreshData();
        }
    }

    private void removeSelectedGame() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih game yang ingin dihapus!",
                    "Pilih Game",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameId = (int) model.getValueAt(selectedRow, 0);
        String gameName = (String) model.getValueAt(selectedRow, 1);

        if (transactionController.removeFromLibrary(gameId, gameName)) {
            refreshData();
            if (onDataChanged != null) {
                onDataChanged.run();
            }
        }
    }

    public void refreshData() {
        model.setRowCount(0);
        List<Object[]> games = transactionController.getOwnedGames();

        for (Object[] game : games) {
            int id = (int) game[0];
            String name = (String) game[1];
            String genre = (String) game[2];
            String developer = (String) game[3];
            int price = (int) game[4];
            boolean isPlayed = (boolean) game[5];
            String status = isPlayed ? "Played" : "Not Played";

            model.addRow(new Object[] { id, name, genre, developer, price, status });
        }

        updateGameCount(games.size());
    }

    private void updateGameCount(int count) {
        for (Component c : ((JPanel) getComponent(0)).getComponents()) {
            if (c instanceof JLabel && "gameCount".equals(c.getName())) {
                ((JLabel) c).setText(count + " game" + (count != 1 ? "s" : "") + " in library");
                break;
            }
        }
    }

    private String formatCurrency(int amount) {
        return String.format("Rp %,d", amount);
    }
}
