package app.view.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.Controller.GameController;
import app.Controller.TransactionController;

public class HomeUserView extends JFrame {

  private JTable storeTable;
  private JLabel lblSaldo;
  private DefaultTableModel storeModel;
  private GameController gameController;
  private TransactionController transactionController;
  private LibraryView libraryView;
  private JTabbedPane tabbedPane;

  // Dark theme colors
  private static final Color BG_DARK = new Color(27, 40, 56);
  private static final Color BG_DARKER = new Color(23, 26, 33);
  private static final Color BG_PANEL = new Color(30, 30, 30);
  private static final Color BG_TABLE = new Color(45, 45, 45);
  private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
  private static final Color BORDER_COLOR = new Color(60, 60, 60);
  private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
  // private static final Color TEXT_SECONDARY = new Color(150, 150, 150);
  private static final Color ACCENT_BLUE = new Color(102, 192, 244);
  private static final Color ACCENT_GREEN = new Color(76, 175, 80);
  private static final Color ACCENT_RED = new Color(183, 28, 28);
  private static final Color BTN_BLUE = new Color(66, 133, 244);
  private static final Color BTN_GRAY = new Color(100, 100, 100);

  public HomeUserView() {
    this.gameController = new GameController();
    this.transactionController = new TransactionController();

    setTitle("SITEAM - Game Store");
    setSize(900, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initUI();
    refreshStoreData();
  }

  private void initUI() {
    setLayout(new BorderLayout());
    getContentPane().setBackground(BG_DARK);

    // Top Panel with Saldo and User Info
    add(createTopPanel(), BorderLayout.NORTH);

    // Tabbed Pane for Store and Library
    tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
    tabbedPane.setBackground(BG_DARK);
    tabbedPane.setForeground(Color.WHITE);

    // Store Tab
    JPanel storePanel = createStorePanel();
    tabbedPane.addTab("STORE", storePanel);

    // Library Tab
    libraryView = new LibraryView();
    libraryView.setOnDataChanged(this::refreshStoreData);
    tabbedPane.addTab("LIBRARY", libraryView);

    // Refresh library when switching to library tab
    tabbedPane.addChangeListener(e -> {
      if (tabbedPane.getSelectedIndex() == 1) {
        libraryView.refreshData();
      }
    });

    add(tabbedPane, BorderLayout.CENTER);
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(BG_DARKER);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

    // Left: Logo/Title
    JLabel lblTitle = new JLabel("SITEAM");
    lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitle.setForeground(ACCENT_BLUE);
    panel.add(lblTitle, BorderLayout.WEST);

    // Right: Saldo and Actions
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
    rightPanel.setBackground(BG_DARKER);

    lblSaldo = new JLabel("Saldo: " + formatCurrency(transactionController.getUserSaldo()));
    lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
    lblSaldo.setForeground(ACCENT_GREEN);

    JButton btnTopUp = createStyledButton("Top Up", ACCENT_GREEN);
    JButton btnLogout = createStyledButton("Logout", ACCENT_RED);

    btnTopUp.addActionListener(e -> handleTopUp());
    btnLogout.addActionListener(e -> handleLogout());

    rightPanel.add(lblSaldo);
    rightPanel.add(Box.createRigidArea(new Dimension(15, 0)));
    rightPanel.add(btnTopUp);
    rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    rightPanel.add(btnLogout);

    panel.add(rightPanel, BorderLayout.EAST);

    return panel;
  }

  private JPanel createStorePanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(BG_PANEL);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(BG_PANEL);
    JLabel lblStoreTitle = new JLabel("GAME STORE - Browse and Buy Games");
    lblStoreTitle.setFont(new Font("Arial", Font.BOLD, 18));
    lblStoreTitle.setForeground(TEXT_PRIMARY);
    headerPanel.add(lblStoreTitle, BorderLayout.WEST);
    panel.add(headerPanel, BorderLayout.NORTH);

    // Store Table
    String[] columns = { "ID", "Nama Game", "Genre", "Harga", "Status" };
    storeModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    storeTable = new JTable(storeModel);
    storeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    storeTable.setRowHeight(35);
    storeTable.setFont(new Font("Arial", Font.PLAIN, 14));
    storeTable.setBackground(BG_TABLE);
    storeTable.setForeground(Color.WHITE);
    storeTable.setGridColor(BORDER_COLOR);
    storeTable.setSelectionBackground(BTN_BLUE);
    storeTable.setSelectionForeground(Color.WHITE);

    // Style header
    storeTable.getTableHeader().setBackground(BG_TABLE_HEADER);
    storeTable.getTableHeader().setForeground(Color.WHITE);
    storeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

    // Hide ID column
    storeTable.getColumnModel().getColumn(0).setMinWidth(0);
    storeTable.getColumnModel().getColumn(0).setMaxWidth(0);
    storeTable.getColumnModel().getColumn(0).setWidth(0);

    // Price column renderer
    storeTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
      @Override
      public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(formatCurrency((Integer) value));
        setHorizontalAlignment(SwingConstants.RIGHT);
        if (!isSelected) {
          c.setForeground(Color.WHITE);
          c.setBackground(BG_TABLE);
        }
        return c;
      }
    });

    // Status column renderer (Owned/Available)
    storeTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
      @Override
      public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String status = (String) value;
        if ("Owned".equals(status)) {
          if (!isSelected) {
            c.setForeground(ACCENT_GREEN);
            c.setBackground(BG_TABLE);
          }
        } else {
          if (!isSelected) {
            c.setForeground(BTN_BLUE);
            c.setBackground(BG_TABLE);
          }
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        return c;
      }
    });

    JScrollPane scrollPane = new JScrollPane(storeTable);
    scrollPane.getViewport().setBackground(BG_TABLE);
    scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    panel.add(scrollPane, BorderLayout.CENTER);

    // Bottom Action Panel
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
    bottomPanel.setBackground(BG_PANEL);
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

    JButton btnBuy = createStyledButton("Buy Game", BTN_BLUE);
    JButton btnRefresh = createStyledButton("Refresh", BTN_GRAY);

    btnBuy.addActionListener(e -> handleBuyGame());
    btnRefresh.addActionListener(e -> refreshStoreData());

    bottomPanel.add(btnBuy);
    bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    bottomPanel.add(btnRefresh);
    bottomPanel.add(Box.createHorizontalGlue());

    panel.add(bottomPanel, BorderLayout.SOUTH);

    return panel;
  }

  private JButton createStyledButton(String text, Color bgColor) {
    JButton btn = new JButton(text);
    btn.setBackground(bgColor);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Arial", Font.BOLD, 12));
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    return btn;
  }

  private void handleTopUp() {
    try {
      String input = JOptionPane.showInputDialog(this,
          "Masukkan jumlah top-up (Rp):",
          "Top Up Saldo",
          JOptionPane.PLAIN_MESSAGE);

      if (input == null || input.trim().isEmpty()) {
        return;
      }

      int amount = Integer.parseInt(input.trim());
      if (amount <= 0) {
        JOptionPane.showMessageDialog(this,
            "Jumlah top-up harus lebih dari 0!",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (transactionController.buyTopUp(amount)) {
        updateSaldoDisplay();
      }
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this,
          "Input tidak valid! Masukkan angka.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void handleBuyGame() {
    int selectedRow = storeTable.getSelectedRow();
    if (selectedRow < 0) {
      JOptionPane.showMessageDialog(this,
          "Pilih game yang ingin dibeli!",
          "Pilih Game",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int gameId = (int) storeModel.getValueAt(selectedRow, 0);
    String gameName = (String) storeModel.getValueAt(selectedRow, 1);
    int price = (int) storeModel.getValueAt(selectedRow, 3);
    String status = (String) storeModel.getValueAt(selectedRow, 4);

    // Check if already owned
    if ("Owned".equals(status)) {
      JOptionPane.showMessageDialog(this,
          "Anda sudah memiliki game ini!\n" +
              "Lihat di tab Library untuk memainkannya.",
          "Game Sudah Dimiliki",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    // Check saldo
    int currentSaldo = transactionController.getUserSaldo();
    if (currentSaldo < price) {
      JOptionPane.showMessageDialog(this,
          "Saldo tidak mencukupi!\n\n" +
              "Harga game: " + formatCurrency(price) + "\n" +
              "Saldo Anda: " + formatCurrency(currentSaldo) + "\n\n" +
              "Silakan top up saldo terlebih dahulu.",
          "Saldo Tidak Cukup",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // Confirmation dialog
    int confirm = JOptionPane.showConfirmDialog(this,
        "Konfirmasi Pembelian\n\n" +
            "Game: " + gameName + "\n" +
            "Harga: " + formatCurrency(price) + "\n\n" +
            "Saldo setelah pembelian: " + formatCurrency(currentSaldo - price) + "\n\n" +
            "Lanjutkan pembelian?",
        "Konfirmasi Pembelian",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (confirm == JOptionPane.YES_OPTION) {
      if (transactionController.buyGame(gameId, price)) {
        refreshStoreData();
        updateSaldoDisplay();

        // Ask if user wants to go to library
        int goToLibrary = JOptionPane.showConfirmDialog(this,
            "Game berhasil dibeli!\n\n" +
                "Ingin langsung ke Library untuk memainkannya?",
            "Pembelian Berhasil",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (goToLibrary == JOptionPane.YES_OPTION) {
          tabbedPane.setSelectedIndex(1); // Switch to Library tab
        }
      }
    }
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

  private void updateSaldoDisplay() {
    lblSaldo.setText("Saldo: " + formatCurrency(transactionController.getUserSaldo()));
  }

  private void refreshStoreData() {
    updateSaldoDisplay();
    storeModel.setRowCount(0);

    List<Object[]> games = gameController.getAllGames();
    if (games != null) {
      for (Object[] game : games) {
        // game: id, name, genre, price, genre_id
        int gameId = (int) game[0];
        String name = (String) game[1];
        String genre = (String) game[2];
        int price = (int) game[3];

        // Check if owned
        boolean isOwned = transactionController.isGameOwned(gameId);
        String status = isOwned ? "Owned" : "Available";

        storeModel.addRow(new Object[] { gameId, name, genre, price, status });
      }
    }
  }

  private String formatCurrency(int amount) {
    return String.format("Rp %,d", amount);
  }
}
