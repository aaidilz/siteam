package app.view.Developer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DashboardDevView extends JFrame {

  // Dark theme colors
  private static final Color BG_DARK = new Color(27, 40, 56);
  private static final Color BG_DARKER = new Color(23, 26, 33);
  private static final Color BG_PANEL = new Color(30, 30, 30);
  private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
  private static final Color ACCENT_BLUE = new Color(102, 192, 244);
  // private static final Color ACCENT_GREEN = new Color(76, 175, 80);
  private static final Color ACCENT_RED = new Color(183, 28, 28);
  private static final Color BTN_BLUE = new Color(66, 133, 244);

  public DashboardDevView() {
    setTitle("SITEAM - Developer Dashboard");
    setSize(800, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initUI();
  }

  private void initUI() {
    setLayout(new BorderLayout());
    getContentPane().setBackground(BG_DARK);

    // Top Panel
    add(createTopPanel(), BorderLayout.NORTH);

    // Main Content
    add(createMainPanel(), BorderLayout.CENTER);
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BG_DARKER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    JLabel lblTitle = new JLabel("SITEAM Developer");
    lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitle.setForeground(ACCENT_BLUE);
    panel.add(lblTitle, BorderLayout.WEST);

    JPanel rightPanel = new JPanel();
    rightPanel.setBackground(BG_DARKER);
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));

    JButton btnProfile = createStyledButton("Profile", BTN_BLUE);

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
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(BG_PANEL);
    panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

    // Welcome message
    JLabel lblWelcome = new JLabel("Developer Dashboard");
    lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
    lblWelcome.setForeground(TEXT_PRIMARY);
    lblWelcome.setAlignmentX(CENTER_ALIGNMENT);
    panel.add(lblWelcome);

    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    JLabel lblSubtitle = new JLabel("Kelola game Anda dari sini");
    lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
    lblSubtitle.setForeground(new Color(150, 150, 150));
    lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);
    panel.add(lblSubtitle);

    panel.add(Box.createRigidArea(new Dimension(0, 40)));

    // Buttons Panel
    JPanel btnPanel = new JPanel();
    btnPanel.setBackground(BG_PANEL);
    btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
    btnPanel.setAlignmentX(CENTER_ALIGNMENT);

    JButton btnGameManagement = createStyledButton("Manajemen Game", BTN_BLUE);
    btnGameManagement.setPreferredSize(new Dimension(200, 50));
    btnGameManagement.setMaximumSize(new Dimension(200, 50));
    btnGameManagement.addActionListener(e -> new GameView().setVisible(true));

    btnPanel.add(Box.createHorizontalGlue());
    btnPanel.add(btnGameManagement);
    btnPanel.add(Box.createHorizontalGlue());

    panel.add(btnPanel);

    return panel;
  }

  private JButton createStyledButton(String text, Color bgColor) {
    JButton btn = new JButton(text);
    btn.setBackground(bgColor);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Arial", Font.BOLD, 14));
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    return btn;
  }

  private void handleLogout() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Yakin ingin logout?",
        "Konfirmasi Logout",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      this.dispose();
      new app.view.Auth.LoginView().setVisible(true);
    }
  }
}
