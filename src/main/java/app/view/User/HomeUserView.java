package app.view.User;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.Controller.GameController;
import app.Controller.TransactionController;

public class HomeUserView extends JFrame {

  private JTable table;
  private JLabel lblSaldo;
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
    this.setLayout(new BorderLayout());
    add(createSaldoPanel(), BorderLayout.NORTH);
    add(createMainPanel(), BorderLayout.CENTER);
    refreshData();
  }

  private JPanel createMainPanel() {
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
          refreshData();
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

  private JPanel createSaldoPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    lblSaldo = new JLabel("Saldo : " + transactionController.getUserSaldo());
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    JButton btnTopUp = new JButton("Top Up Saldo");

    btnTopUp.addActionListener(e -> {
      try {
        int amount = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan jumlah top-up:"));
        if (amount <= 0) {
          JOptionPane.showMessageDialog(this, "Jumlah top-up harus lebih dari 0!");
          return;
        }
        if (amount > 0) {
          transactionController.buyTopUp(amount);
          refreshData();
        } else {
          JOptionPane.showMessageDialog(this, "Jumlah top-up tidak valid!");
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Input tidak valid!");
      }

    });
    panel.add(lblSaldo, BorderLayout.WEST);
    panel.add(btnTopUp, BorderLayout.EAST);
    return panel;
  }

  private void refreshData() {
    lblSaldo.setText("Saldo : " + transactionController.getUserSaldo());
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
