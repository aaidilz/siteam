package app.view.Developer;

import app.Controller.GameController;
import app.Controller.GenreController;
import app.Util.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GameView extends JFrame {
  private JTable table;
  private DefaultTableModel tableModel;
  private GameController gameController;
  private GenreController genreController;

  private static class GenreItem {
    int id;
    String name;

    GenreItem(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }

  public GameView() {
    this.gameController = new GameController();
    this.genreController = new GenreController();

    setTitle("Manajemen Game");
    setSize(800, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    add(createToolbar(), BorderLayout.NORTH);
    add(createTable(), BorderLayout.CENTER);

    refreshData();
  }

  private JToolBar createToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.setFloatable(false);

    JButton btnAdd = new JButton("Tambah");
    JButton btnEdit = new JButton("Edit");
    JButton btnDelete = new JButton("Hapus");
    JButton btnRefresh = new JButton("Refresh");
    JButton btnAddGenre = new JButton("Tambah Genre");
    JButton btnLogout = new JButton("Logout");

    btnAdd.addActionListener(e -> showForm(null));
    btnEdit.addActionListener(e -> {
      int selectedRow = table.getSelectedRow();
      if (selectedRow >= 0) {
        // Get data from table
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        int genreId = (int) tableModel.getValueAt(selectedRow, 4); // Added column
        int price = (int) tableModel.getValueAt(selectedRow, 3);

        showForm(new Object[] { id, name, genreId, price });
      } else {
        JOptionPane.showMessageDialog(this, "Pilih game yang akan diedit");
      }
    });
    btnDelete.addActionListener(e -> {
      int selectedRow = table.getSelectedRow();
      if (selectedRow >= 0) {
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        gameController.deleteGame(id);
        refreshData();
      } else {
        JOptionPane.showMessageDialog(this, "Pilih game yang akan dihapus");
      }
    });
    btnRefresh.addActionListener(e -> refreshData());

    btnAddGenre.addActionListener(e -> {
      String genreName = JOptionPane.showInputDialog(this, "Masukkan Nama Genre Baru:");
      if (genreName != null && !genreName.trim().isEmpty()) {
        genreController.addGenre(genreName.trim());
      }
    });

    btnLogout.addActionListener(e -> {
      this.dispose();
      // Ensure to close all windows to return cleanly to login
      for (Window w : Window.getWindows()) {
        w.dispose();
      }
      new app.view.Auth.LoginView().setVisible(true);
    });

    toolbar.add(btnAdd);
    toolbar.add(btnEdit);
    toolbar.add(btnDelete);
    toolbar.add(btnRefresh);
    toolbar.add(btnAddGenre);
    toolbar.add(Box.createHorizontalGlue());
    toolbar.add(btnLogout);

    return toolbar;
  }

  private JScrollPane createTable() {
    // ID, Name, Genre, Price, GenreID (Hidden)
    tableModel = new DefaultTableModel(new Object[] { "ID", "Nama", "Genre", "Harga", "GenreID" }, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    table = new JTable(tableModel);

    // Hide GenreID column
    table.getColumnModel().getColumn(4).setMinWidth(0);
    table.getColumnModel().getColumn(4).setMaxWidth(0);
    table.getColumnModel().getColumn(4).setWidth(0);

    return new JScrollPane(table);
  }

  private void refreshData() {
    tableModel.setRowCount(0);
    List<Object[]> games = gameController.getAllGames();
    if (games != null) {
      for (Object[] game : games) {
        // game[]: id, name, genre(string), price, genre_id
        tableModel.addRow(game);
      }
    }
  }

  private void showForm(Object[] existingData) {
    JDialog dialog = new JDialog(this, existingData == null ? "Tambah Game" : "Edit Game", true);
    dialog.setSize(400, 300);
    dialog.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JTextField txtName = new JTextField();
    JComboBox<GenreItem> cmbGenre = new JComboBox<>();
    JTextField txtPrice = new JTextField();

    // Populate genres
    List<Object[]> genres = genreController.getAllGenres();
    if (genres != null) {
      for (Object[] g : genres) {
        GenreItem item = new GenreItem((int) g[0], (String) g[1]);
        cmbGenre.addItem(item);
        if (existingData != null && item.id == (int) existingData[2]) {
          cmbGenre.setSelectedItem(item);
        }
      }
    }

    if (existingData != null) {
      txtName.setText((String) existingData[1]);
      txtPrice.setText(String.valueOf(existingData[3]));
    }

    panel.add(new JLabel("Nama Game:"));
    panel.add(txtName);
    panel.add(new JLabel("Genre:"));
    panel.add(cmbGenre);
    panel.add(new JLabel("Harga:"));
    panel.add(txtPrice);

    JButton btnSave = new JButton("Simpan");
    btnSave.addActionListener(e -> {
      String name = txtName.getText();
      String priceStr = txtPrice.getText();
      GenreItem selectedGenre = (GenreItem) cmbGenre.getSelectedItem();

      if (selectedGenre == null) {
        JOptionPane.showMessageDialog(dialog, "Pilih genre!");
        return;
      }

      try {
        int price = Integer.parseInt(priceStr);

        // Get Current User ID from Session
        int developerId = 0;
        if (Session.getInstance().getUser() != null) {
          developerId = Session.getInstance().getUser().getId();
        }

        if (existingData == null) {
          gameController.addGame(name, selectedGenre.id, developerId, price);
        } else {
          int id = (int) existingData[0];
          gameController.updateGame(id, name, selectedGenre.id, price);
        }

        dialog.dispose();
        refreshData();

      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(dialog, "Harga harus angka!");
      }
    });

    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(btnSave, BorderLayout.SOUTH);
    dialog.setVisible(true);
  }
}
