package app.view.Developer;

import javax.swing.*;
import java.awt.*;

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

    panel.add(new JButton("Manajemen Game"));
    panel.add(new JButton("Manajemen Genre"));
    panel.add(new JButton("Manajemen User"));
    panel.add(new JButton("Logout"));

    return panel;
  }
}
