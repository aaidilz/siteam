package app.view.User;

import app.Controller.GameController;
import app.Controller.TransactionController;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeUserView extends JFrame {

  private JTable table;
  private DefaultTableModel model;
  private GameController gameController;
  private TransactionController transactionController;

  public HomeUserView() {
    this.gameController = new GameController();
    this.transactionController = new TransactionController();

    setTitle("Home User - Game Store");
    setSize(800, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(createPanel());
    refreshData();
  }

  private JPanel createPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    model = new DefaultTableModel(
        new Object[] { "ID", "Nama Game", "Genre", "Harga" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table = new JTable(model);

    panel.add(new JScrollPane(table), BorderLayout.CENTER);

    JButton btnBuy = new JButton("Beli");
    JButton btnLogout = new JButton("Logout");

    btnBuy.addActionListener(e -> {
      int selectedRow = table.getSelectedRow();
      if (selectedRow >= 0) {
        int gameId = (int) model.getValueAt(selectedRow, 0);
        int price = (int) model.getValueAt(selectedRow, 3);

        // Execute Purchase
        if (transactionController.buyGame(gameId, price)) {
          // Determine visuals if needed (e.g. mark as owned).
          // ideally refresh table to show owned status if we had that column.
        }
      } else {
        JOptionPane.showMessageDialog(this, "Pilih game yang ingin dibeli");
      }
    });

    btnLogout.addActionListener(e -> {
      this.dispose();
      new app.view.Auth.LoginView().setVisible(true);
    });

    JPanel bottom = new JPanel();
    bottom.add(btnBuy);
    bottom.add(btnLogout);

    panel.add(bottom, BorderLayout.SOUTH);
    return panel;
  }

  private void refreshData() {
    model.setRowCount(0);
    List<Object[]> games = gameController.getAllGames();
    if (games != null) {
      for (Object[] game : games) {
        // game: id, name, genre, price, genre_id
        // We only need first 4
        model.addRow(new Object[] { game[0], game[1], game[2], game[3] });
      }
    }
  }
}
