package app.View.Developer;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DashboardDevView extends JFrame {

  public DashboardDevView() {
    setTitle("Dashboard Developer");
    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(createPanel());
  }

  private JPanel createPanel() {
    JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    JButton btnGame = new JButton("Manajemen Game");
    btnGame.addActionListener(e -> new GameView().setVisible(true));
    panel.add(btnGame);

    JButton btnLogout = new JButton("Logout");
    btnLogout.addActionListener(e -> {
      this.dispose();
      new app.View.Auth.LoginView().setVisible(true);
    });
    panel.add(btnLogout);

    return panel;
  }
}
