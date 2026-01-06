package app.View.Admin;

import app.Util.Session;
import app.View.Auth.LoginView;
import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    public AdminDashboardView() {
        setTitle("Admin Dashboard - Game Store");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tab Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Statistics", new StatistikView ());
        tabbedPane.addTab("User Management", new UserManagementView());
        tabbedPane.addTab("Genre Management", new GenreManagementView());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom Panel with Logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            Session.getInstance().logout();
            new LoginView().setVisible(true);
            dispose();
        });
        bottomPanel.add(btnLogout);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}