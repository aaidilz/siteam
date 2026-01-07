package app.view.Admin;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.Util.Session;
import app.view.Auth.LoginView;

public class AdminDashboardView extends JFrame {

    public AdminDashboardView() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblWelcome = new JLabel("Welcome, Admin", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnUserManagement = new JButton("Manajemen User");
        JButton btnGenreManagement = new JButton("Manajemen Genre");
        JButton btnLogout = new JButton("Logout");

        btnUserManagement.addActionListener(e -> {
            new UserManagementView().setVisible(true);
        });

        btnGenreManagement.addActionListener(e -> {
            new GenreManagementView().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            Session.getInstance().logout();
            new LoginView().setVisible(true);
            dispose();
        });

        panel.add(lblWelcome);
        panel.add(btnUserManagement);
        panel.add(btnGenreManagement);
        panel.add(btnLogout);

        add(panel);
    }
}
