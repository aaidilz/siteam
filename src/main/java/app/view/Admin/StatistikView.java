package app.View.Admin;

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

    public StatistikView () {
        initUI();
        refreshStats();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 240, 240));

        // Header
        JLabel lblTitle = new JLabel("Statistik Game Store");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        // Stats Grid (2x2)
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(240, 240, 240));

        lblTotalUsers = createStatCard("👥 Total Users", "0");
        lblTotalTransactions = createStatCard("💳 Total Transactions", "0");
        lblTotalRevenue = createStatCard("💰 Total Revenue", "0");
        lblTotalGames = createStatCard("🎮 Total Games", "0");

        statsPanel.add(createCardPanel(lblTotalUsers));
        statsPanel.add(createCardPanel(lblTotalTransactions));
        statsPanel.add(createCardPanel(lblTotalRevenue));
        statsPanel.add(createCardPanel(lblTotalGames));

        add(statsPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 240, 240));
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> refreshStats());
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createCardPanel(JLabel statLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        card.add(statLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStatCard(String title, String value) {
        JLabel lbl = new JLabel("<html><center><font size='4'>" + title + 
                                "<br><font size='6' color='#0064C8'><b>" + value + 
                                "</b></font></center></html>");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        return lbl;
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
            lblTotalUsers.setText("<html><center><font size='4'>👥 Total Users" + 
                                   "<br><font size='6' color='#0064C8'><b>" + totalUsers + 
                                   "</b></font></center></html>");

            // Total Transactions
            int totalTransactions = executeCountQuery(conn, "SELECT COUNT(*) FROM tr_transaction");
            lblTotalTransactions.setText("<html><center><font size='4'>💳 Total Transactions" + 
                                          "<br><font size='6' color='#28A745'><b>" + totalTransactions + 
                                          "</b></font></center></html>");

            // Total Revenue (hanya game purchases: GM_prefix)
            int totalRevenue = executeSumQuery(conn, 
                "SELECT COALESCE(SUM(amount), 0) FROM tr_transaction WHERE item_id LIKE 'GM_%'");
            lblTotalRevenue.setText("<html><center><font size='4'>💰 Total Revenue" + 
                                    "<br><font size='6' color='#FFC107'><b>" + formatCurrency(totalRevenue) + 
                                    "</b></font></center></html>");

            // Total Games
            int totalGames = executeCountQuery(conn, "SELECT COUNT(*) FROM mst_game");
            lblTotalGames.setText("<html><center><font size='4'>🎮 Total Games" + 
                                  "<br><font size='6' color='#DC3545'><b>" + totalGames + 
                                  "</b></font></center></html>");

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