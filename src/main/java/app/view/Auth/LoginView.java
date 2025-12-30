package app.view.Auth;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JComboBox<String> cmbRole;

  public LoginView() {
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
    cmbRole = new JComboBox<>(new String[] { "USER", "DEVELOPER" });

    JButton btnLogin = new JButton("Login");
    JButton btnReset = new JButton("Reset");

    panel.add(new JLabel("Username"));
    panel.add(txtUsername);
    panel.add(new JLabel("Password"));
    panel.add(txtPassword);
    panel.add(new JLabel("Role"));
    panel.add(cmbRole);
    panel.add(btnLogin);
    panel.add(btnReset);

    btnLogin.addActionListener(e -> prosesLogin());
    btnReset.addActionListener(e -> resetForm());

    return panel;
  }

  private void prosesLogin() {
    if (txtUsername.getText().length() < 3) {
      JOptionPane.showMessageDialog(this, "Username minimal 3 karakter");
      return;
    }
    JOptionPane.showMessageDialog(this, "Login berhasil (simulasi)");
  }

  private void resetForm() {
    txtUsername.setText("");
    txtPassword.setText("");
    cmbRole.setSelectedIndex(0);
  }
}
