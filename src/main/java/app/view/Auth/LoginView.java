package app.view.Auth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import app.Controller.UserController;

public class LoginView extends JFrame {

  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JComboBox<String> cmbRole;
  private JLabel lblRole;
  private UserController controller;
  private boolean isLoginMode = true;
  private JButton btnLogin;
  private JButton btnSwitch;

  // Dark theme colors
  private static final Color BG_DARK = new Color(27, 40, 56);
  private static final Color BG_DARKER = new Color(23, 26, 33);
  private static final Color BG_PANEL = new Color(30, 30, 30);
  private static final Color BG_INPUT = new Color(45, 45, 45);
  private static final Color BORDER_COLOR = new Color(60, 60, 60);
  private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
  private static final Color ACCENT_BLUE = new Color(102, 192, 244);
  private static final Color BTN_BLUE = new Color(66, 133, 244);
  private static final Color BTN_GRAY = new Color(100, 100, 100);

  public LoginView() {
    this.controller = new UserController();
    setTitle("SITEAM - Login");
    setSize(450, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setBackground(BG_DARK);

    initUI();
  }

  private void initUI() {
    setLayout(new BorderLayout());

    // Header
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(BG_DARKER);
    headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

    JLabel lblTitle = new JLabel("SITEAM");
    lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
    lblTitle.setForeground(ACCENT_BLUE);
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    headerPanel.add(lblTitle);

    add(headerPanel, BorderLayout.NORTH);

    // Form Panel
    add(createFormPanel(), BorderLayout.CENTER);
  }

  private JPanel createFormPanel() {
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setBackground(BG_DARK);
    wrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(BG_PANEL);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR),
        BorderFactory.createEmptyBorder(30, 30, 30, 30)));

    // Username
    JLabel lblUsername = new JLabel("Username");
    lblUsername.setForeground(TEXT_PRIMARY);
    lblUsername.setFont(new Font("Arial", Font.BOLD, 12));
    lblUsername.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(lblUsername);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));

    txtUsername = new JTextField();
    txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    txtUsername.setBackground(BG_INPUT);
    txtUsername.setForeground(Color.WHITE);
    txtUsername.setCaretColor(Color.WHITE);
    txtUsername.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    txtUsername.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(txtUsername);
    panel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Password
    JLabel lblPassword = new JLabel("Password");
    lblPassword.setForeground(TEXT_PRIMARY);
    lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
    lblPassword.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(lblPassword);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));

    txtPassword = new JPasswordField();
    txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    txtPassword.setBackground(BG_INPUT);
    txtPassword.setForeground(Color.WHITE);
    txtPassword.setCaretColor(Color.WHITE);
    txtPassword.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    txtPassword.setAlignmentX(LEFT_ALIGNMENT);
    panel.add(txtPassword);
    panel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Role (only visible in register mode) - Only USER and DEVELOPER
    lblRole = new JLabel("Daftar Sebagai");
    lblRole.setForeground(TEXT_PRIMARY);
    lblRole.setFont(new Font("Arial", Font.BOLD, 12));
    lblRole.setAlignmentX(LEFT_ALIGNMENT);
    lblRole.setVisible(false); // Hidden by default (login mode)
    panel.add(lblRole);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Only USER and DEVELOPER options (no ADMIN for public registration)
    cmbRole = new JComboBox<>(new String[] { "User", "Game Developer" });
    cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    cmbRole.setBackground(BG_INPUT);
    cmbRole.setForeground(Color.WHITE);
    cmbRole.setAlignmentX(LEFT_ALIGNMENT);
    cmbRole.setVisible(false); // Hidden by default (login mode)
    panel.add(cmbRole);
    panel.add(Box.createRigidArea(new Dimension(0, 25)));

    // Buttons
    JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
    btnPanel.setBackground(BG_PANEL);
    btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    btnPanel.setAlignmentX(LEFT_ALIGNMENT);

    btnLogin = createStyledButton("Login", BTN_BLUE);
    btnSwitch = createStyledButton("Buat Akun Baru", BTN_GRAY);

    btnLogin.addActionListener(e -> {
      if (isLoginMode)
        prosesLogin();
      else
        prosesRegister();
    });

    btnSwitch.addActionListener(e -> toggleMode());

    btnPanel.add(btnLogin);
    btnPanel.add(btnSwitch);
    panel.add(btnPanel);

    wrapper.add(panel, BorderLayout.CENTER);
    return wrapper;
  }

  private void toggleMode() {
    isLoginMode = !isLoginMode;
    if (isLoginMode) {
      btnLogin.setText("Login");
      btnSwitch.setText("Buat Akun Baru");
      setTitle("SITEAM - Login");
      // Hide role selector in login mode
      lblRole.setVisible(false);
      cmbRole.setVisible(false);
    } else {
      btnLogin.setText("Register");
      btnSwitch.setText("Sudah Punya Akun");
      setTitle("SITEAM - Register");
      // Show role selector in register mode
      lblRole.setVisible(true);
      cmbRole.setVisible(true);
    }
    // Revalidate and repaint to update layout
    revalidate();
    repaint();
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

  private void prosesLogin() {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());

    // Role is determined automatically from database
    String role = controller.login(username, password);
    if (role != null) {
      this.dispose();

      if (role.equalsIgnoreCase("DEVELOPER")) {
        new app.view.Developer.DashboardDevView().setVisible(true);
      } else if (role.equalsIgnoreCase("ADMIN")) {
        new app.view.Admin.AdminDashboardView().setVisible(true);
      } else {
        new app.view.User.HomeUserView().setVisible(true);
      }
    }
  }

  private void prosesRegister() {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());

    // Convert display text to database role
    String selectedRole = (String) cmbRole.getSelectedItem();
    String role;
    if ("Game Developer".equals(selectedRole)) {
      role = "DEVELOPER";
    } else {
      role = "USER";
    }

    if (controller.register(username, password, role)) {
      isLoginMode = true;
      btnLogin.setText("Login");
      btnSwitch.setText("Buat Akun Baru");
      setTitle("SITEAM - Login");
      lblRole.setVisible(false);
      cmbRole.setVisible(false);
      revalidate();
      repaint();
    }
  }
}
