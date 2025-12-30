package app.view.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeUserView extends JFrame {

  private JTable table;
  private DefaultTableModel model;

  public HomeUserView() {
    setTitle("Home User");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(createPanel());
  }

  private JPanel createPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    model = new DefaultTableModel(
        new Object[] { "ID", "Nama Game", "Genre", "Harga" }, 0);
    table = new JTable(model);

    model.addRow(new Object[] { 1, "Cyber Adventure", "RPG", 150000 });

    panel.add(new JScrollPane(table), BorderLayout.CENTER);

    JButton btnBuy = new JButton("Beli");
    JButton btnLogout = new JButton("Logout");

    JPanel bottom = new JPanel();
    bottom.add(btnBuy);
    bottom.add(btnLogout);

    panel.add(bottom, BorderLayout.SOUTH);
    return panel;
  }
}
