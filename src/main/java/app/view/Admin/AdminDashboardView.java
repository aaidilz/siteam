package app.View.Admin;

import app.Util.Session;
import app.View.Auth.LoginView;
import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    public AdminDashboardView() {
        setTitle("Dashboard Admin - Game Store");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setBackground(new Color(50, 60, 70)); 

        JLabel lblTitle = new JLabel("Admin Dashboard");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            Session.getInstance().logout();
            new LoginView().setVisible(true);
            dispose();
        });

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("User Management", createUserManagementPanel());
        tabbedPane.addTab("Game Management", createGameManagementPanel());
        tabbedPane.addTab("Transaction History", createTransactionHistoryPanel());
        tabbedPane.addTab("Statistics", createStatisticsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createUserManagementPanel() {
        return new UserManagementView();
    }

    private JPanel createGameManagementPanel() {
        return new GenreManagementView();
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Transaction History Module (Coming Soon)"));
        return panel;
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Statistics Module (Coming Soon)"));
        return panel;
    }
}
