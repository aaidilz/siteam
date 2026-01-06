package app.view.Admin;

import app.Util.Session;
import app.view.Auth.LoginView;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    // Dark theme colors
    private static final Color BG_DARK = new Color(27, 40, 56);
    private static final Color BG_DARKER = new Color(23, 26, 33);
    private static final Color BG_PANEL = new Color(30, 30, 30);
    // private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_BLUE = new Color(102, 192, 244);
    private static final Color ACCENT_RED = new Color(183, 28, 28);

    public AdminDashboardView() {
        setTitle("SITEAM - Admin Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        // Top Panel
        add(createTopPanel(), BorderLayout.NORTH);

        // Main Panel with Tabs
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARKER);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("SITEAM Admin");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(ACCENT_BLUE);
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(BG_DARKER);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

        JButton btnProfile = createStyledButton("Profile", new Color(66, 133, 244));

        JButton btnLogout = createStyledButton("Logout", ACCENT_RED);

        btnProfile.addActionListener(e -> new app.view.ProfileView().setVisible(true));
        btnLogout.addActionListener(e -> handleLogout());

        rightPanel.add(btnProfile);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rightPanel.add(btnLogout);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_PANEL);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tab Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(BG_PANEL);
        tabbedPane.setForeground(Color.WHITE);

        tabbedPane.addTab("Statistics", new StatistikView());
        tabbedPane.addTab("User Management", new UserManagementView());
        tabbedPane.addTab("Genre Management", new GenreManagementView());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        return mainPanel;
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

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Session.getInstance().logout();
            new LoginView().setVisible(true);
            dispose();
        }
    }
}