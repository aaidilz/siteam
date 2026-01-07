package app.view.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

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
import javax.swing.JTextField;
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

  // Search
  private JTextField txtSearch;
  private List<Object[]> storeCache = new ArrayList<>();

  // Dark theme colors
  private static final Color BG_DARK = new Color(27, 40, 56);
  private static final Color BG_DARKER = new Color(23, 26, 33);
  private static final Color BG_PANEL = new Color(30, 30, 30);
  private static final Color BG_TABLE = new Color(45, 45, 45);
  private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
  private static final Color BORDER_COLOR = new Color(60, 60, 60);
  private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
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

    add(createTopPanel(), BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
    tabbedPane.setBackground(BG_DARK);
    tabbedPane.setForeground(Color.WHITE);

    tabbedPane.addTab("STORE", createStorePanel());

    libraryView = new LibraryView();
    libraryView.setOnDataChanged(this::refreshStoreData);
    tabbedPane.addTab("LIBRARY", libraryView);

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

    JLabel lblTitle = new JLabel("SITEAM");
    lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
    lblTitle.setForeground(ACCENT_BLUE);
    panel.add(lblTitle, BorderLayout.WEST);

    JPanel right = new JPanel();
    right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
    right.setBackground(BG_DARKER);

    lblSaldo = new JLabel("Saldo: " + formatCurrency(transactionController.getUserSaldo()));
    lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
    lblSaldo.setForeground(ACCENT_GREEN);

    JButton btnTopUp = createButton("Top Up", ACCENT_GREEN);
    JButton btnProfile = createButton("Profile", BTN_BLUE);
    JButton btnLogout = createButton("Logout", ACCENT_RED);

    btnTopUp.addActionListener(e -> handleTopUp());
    btnProfile.addActionListener(e -> new app.view.ProfileView().setVisible(true));
    btnLogout.addActionListener(e -> handleLogout());

    right.add(lblSaldo);
    right.add(Box.createRigidArea(new Dimension(15, 0)));
    right.add(btnTopUp);
    right.add(Box.createRigidArea(new Dimension(10, 0)));
    right.add(btnProfile);
    right.add(Box.createRigidArea(new Dimension(10, 0)));
    right.add(btnLogout);

    panel.add(right, BorderLayout.EAST);
    return panel;
  }

  private JPanel createStorePanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(BG_PANEL);
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Header + Search
    JPanel header = new JPanel(new BorderLayout(10, 0));
    header.setBackground(BG_PANEL);

    JLabel lbl = new JLabel("GAME STORE - Browse and Buy Games");
    lbl.setFont(new Font("Arial", Font.BOLD, 18));
    lbl.setForeground(TEXT_PRIMARY);
    header.add(lbl, BorderLayout.WEST);

    txtSearch = new JTextField();
    txtSearch.setPreferredSize(new Dimension(250, 30));
    txtSearch.setBackground(BG_TABLE);
    txtSearch.setForeground(Color.WHITE);
    txtSearch.setCaretColor(Color.WHITE);
    txtSearch.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        filterStoreTable(txtSearch.getText());
      }
    });

    JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
    searchPanel.setBackground(BG_PANEL);
    JLabel lblSearch = new JLabel("Cari:");
    lblSearch.setForeground(TEXT_PRIMARY);
    searchPanel.add(lblSearch, BorderLayout.WEST);
    searchPanel.add(txtSearch, BorderLayout.CENTER);

    header.add(searchPanel, BorderLayout.EAST);
    panel.add(header, BorderLayout.NORTH);

    // Table
    String[] cols = { "ID", "Nama Game", "Genre", "Developer", "Harga", "Status" };
    storeModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int r, int c) {
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

    storeTable.getTableHeader().setBackground(BG_TABLE_HEADER);
    storeTable.getTableHeader().setForeground(Color.WHITE);

    storeTable.getColumnModel().getColumn(0).setMinWidth(0);
    storeTable.getColumnModel().getColumn(0).setMaxWidth(0);

    JScrollPane scroll = new JScrollPane(storeTable);
    scroll.getViewport().setBackground(BG_TABLE);
    scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
    panel.add(scroll, BorderLayout.CENTER);

    // Bottom
    JPanel bottom = new JPanel();
    bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
    bottom.setBackground(BG_PANEL);

    JButton btnBuy = createButton("Buy Game", BTN_BLUE);
    JButton btnRefresh = createButton("Refresh", BTN_GRAY);

    btnBuy.addActionListener(e -> handleBuyGame());
    btnRefresh.addActionListener(e -> refreshStoreData());

    bottom.add(btnBuy);
    bottom.add(Box.createRigidArea(new Dimension(10, 0)));
    bottom.add(btnRefresh);
    bottom.add(Box.createHorizontalGlue());

    panel.add(bottom, BorderLayout.SOUTH);

    return panel;
  }

  private void filterStoreTable(String keyword) {
    storeModel.setRowCount(0);
    String q = keyword.toLowerCase().trim();

    for (Object[] g : storeCache) {
      String name = ((String) g[1]).toLowerCase();
      String genre = ((String) g[2]).toLowerCase();
      String dev = ((String) g[5]).toLowerCase();

      if (q.isEmpty() || name.contains(q) || genre.contains(q) || dev.contains(q)) {
        int id = (int) g[0];
        int price = (int) g[3];
        boolean owned = transactionController.isGameOwned(id);
        storeModel.addRow(new Object[] {
            id, g[1], g[2], g[5], price, owned ? "Owned" : "Available"
        });
      }
    }
  }

  private void refreshStoreData() {
    updateSaldoDisplay();
    storeModel.setRowCount(0);

    storeCache = gameController.getAllGames();
    if (storeCache != null) {
      for (Object[] g : storeCache) {
        int id = (int) g[0];
        boolean owned = transactionController.isGameOwned(id);
        storeModel.addRow(new Object[] {
            id, g[1], g[2], g[5], g[3], owned ? "Owned" : "Available"
        });
      }
    }

    if (!txtSearch.getText().trim().isEmpty()) {
      filterStoreTable(txtSearch.getText());
    }
  }

  private JButton createButton(String text, Color bg) {
    JButton b = new JButton(text);
    b.setBackground(bg);
    b.setForeground(Color.WHITE);
    b.setFont(new Font("Arial", Font.BOLD, 12));
    b.setFocusPainted(false);
    b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return b;
  }

  private void updateSaldoDisplay() {
    lblSaldo.setText("Saldo: " + formatCurrency(transactionController.getUserSaldo()));
  }

  private void handleTopUp() {
    String in = JOptionPane.showInputDialog(this, "Masukkan jumlah top-up:");
    if (in == null) return;
    try {
      int amt = Integer.parseInt(in);
      if (amt > 0 && transactionController.buyTopUp(amt)) {
        updateSaldoDisplay();
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Input tidak valid");
    }
  }

  private void handleBuyGame() {
    int r = storeTable.getSelectedRow();
    if (r < 0) return;

    int id = (int) storeModel.getValueAt(r, 0);
    int price = (int) storeModel.getValueAt(r, 4);

    if (transactionController.buyGame(id, price)) {
      refreshStoreData();
    }
  }

  private void handleLogout() {
    if (JOptionPane.showConfirmDialog(this, "Logout?", "Confirm",
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      dispose();
      new app.view.Auth.LoginView().setVisible(true);
    }
  }

  private String formatCurrency(int amount) {
    return String.format("Rp %,d", amount);
  }
}
