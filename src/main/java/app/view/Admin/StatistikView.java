package app.view.Admin;

import app.Database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatistikView extends JPanel {

    private JLabel lblTotalUsers;
    private JLabel lblTotalTransactions;
    private JLabel lblTotalRevenue;
    private JLabel lblTotalGames;

    // Dark theme colors
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_CARD = new Color(45, 45, 45);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_BLUE = new Color(66, 133, 244);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_YELLOW = new Color(255, 193, 7);
    private static final Color ACCENT_RED = new Color(244, 67, 54);
    private static final Color BTN_GRAY = new Color(100, 100, 100);

    public StatistikView() {
        initUI();
        refreshStats();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(BG_PANEL);

        // Header
        JLabel lblTitle = new JLabel("Statistik Game Store");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_PANEL);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Stats Grid (2x2)
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(BG_PANEL);

        lblTotalUsers = createStatLabel("Total Users", "0", ACCENT_BLUE);
        lblTotalTransactions = createStatLabel("Total Transactions", "0", ACCENT_GREEN);
        lblTotalRevenue = createStatLabel("Total Revenue", "0", ACCENT_YELLOW);
        lblTotalGames = createStatLabel("Total Games", "0", ACCENT_RED);

        statsPanel.add(createCardPanel(lblTotalUsers));
        statsPanel.add(createCardPanel(lblTotalTransactions));
        statsPanel.add(createCardPanel(lblTotalRevenue));
        statsPanel.add(createCardPanel(lblTotalGames));

        add(statsPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(BG_PANEL);
        JButton btnRefresh = createStyledButton("Refresh", BTN_GRAY);
        btnRefresh.addActionListener(e -> refreshStats());
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createCardPanel(JLabel statLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        card.add(statLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStatLabel(String title, String value, Color valueColor) {
        String html = "<html><center>" +
                "<font color='#C8C8C8' size='4'>" + title + "</font><br>" +
                "<font color='"
                + String.format("#%02x%02x%02x", valueColor.getRed(), valueColor.getGreen(), valueColor.getBlue())
                + "' size='6'><b>" + value + "</b></font>" +
                "</center></html>";
        JLabel lbl = new JLabel(html);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private void updateStatLabel(JLabel label, String title, String value, Color valueColor) {
        String html = "<html><center>" +
                "<font color='#C8C8C8' size='4'>" + title + "</font><br>" +
                "<font color='"
                + String.format("#%02x%02x%02x", valueColor.getRed(), valueColor.getGreen(), valueColor.getBlue())
                + "' size='6'><b>" + value + "</b></font>" +
                "</center></html>";
        label.setText(html);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void refreshStats() {
        try (Connection conn = DBConnection.configDB()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi database gagal!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Total Users
            int totalUsers = executeCountQuery(conn, "SELECT COUNT(*) FROM ref_user");
            updateStatLabel(lblTotalUsers, "Total Users", String.valueOf(totalUsers), ACCENT_BLUE);

            // Total Transactions
            int totalTransactions = executeCountQuery(conn, "SELECT COUNT(*) FROM tr_transaction");
            updateStatLabel(lblTotalTransactions, "Total Transactions", String.valueOf(totalTransactions),
                    ACCENT_GREEN);

            // Total Revenue (hanya game purchases: GM_prefix)
            int totalRevenue = executeSumQuery(conn,
                    "SELECT COALESCE(SUM(amount), 0) FROM tr_transaction WHERE item_id LIKE 'GM_%'");
            updateStatLabel(lblTotalRevenue, "Total Revenue", formatCurrency(totalRevenue), ACCENT_YELLOW);

            // Total Games
            int totalGames = executeCountQuery(conn, "SELECT COUNT(*) FROM mst_game");
            updateStatLabel(lblTotalGames, "Total Games", String.valueOf(totalGames), ACCENT_RED);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat statistik: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int executeCountQuery(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private int executeSumQuery(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private String formatCurrency(int amount) {
        return String.format("Rp %,d", amount);
    }
}