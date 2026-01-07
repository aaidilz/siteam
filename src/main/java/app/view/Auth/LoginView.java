package app.view.Auth;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.Controller.UserController;

public class LoginView extends JFrame {

  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JComboBox<String> cmbRole;
  private UserController controller;
  private boolean isLoginMode = true;

  public LoginView() {
    this.controller = new UserController();
    setTitle("Game Store - Login");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(createFormPanel());
  }

  private JPanel createFormPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    txtUsername = new JTextField();
    txtPassword = new JPasswordField();
    cmbRole = new JComboBox<>(new String[] { "USER", "DEVELOPER", "ADMIN" });

    JButton btnLogin = new JButton("Login");
    JButton btnSwitch = new JButton("Switch to Register");

    panel.add(new JLabel("Username"));
    panel.add(txtUsername);
    panel.add(new JLabel("Password"));
    panel.add(txtPassword);
    panel.add(new JLabel("Role"));
    panel.add(cmbRole);
    panel.add(btnLogin);
    panel.add(btnSwitch);

    // Initial state
    btnLogin.addActionListener(e -> {
      if (isLoginMode)
        prosesLogin();
      else
        prosesRegister();
    });

    // Toggle Mode
    btnSwitch.addActionListener(e -> {
      isLoginMode = !isLoginMode;
      if (isLoginMode) {
        btnLogin.setText("Login");
        btnSwitch.setText("Switch to Register");
        setTitle("Game Store - Login");
      } else {
        btnLogin.setText("Register");
        btnSwitch.setText("Switch to Login");
        setTitle("Game Store - Register");
      }
    });

    return panel;
  }

  private void prosesLogin() {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());

    String role = controller.login(username, password);
    if (role != null) {
      // Close login window
      this.dispose();

      // Open appropriate view based on role
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
    String role = (String) cmbRole.getSelectedItem();

    if (controller.register(username, password, role)) {
      // Switch back to login mode on success
      isLoginMode = true;
      ((JButton) ((JPanel) getContentPane().getComponent(0)).getComponent(6)).setText("Login");
      ((JButton) ((JPanel) getContentPane().getComponent(0)).getComponent(7)).setText("Switch to Register");
      setTitle("Game Store - Login");
    }
  }

}
