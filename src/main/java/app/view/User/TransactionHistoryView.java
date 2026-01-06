package app.View.User;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TransactionHistoryView extends JFrame {

  private JTextField txtSearch;
  private JTable table;
  private DefaultTableModel tableModel;

  public TransactionHistoryView() {
    setTitle("Riwayat Transaksi");
    setSize(700, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    setLayout(new BorderLayout());

    add(createTopPanel(), BorderLayout.NORTH);
    add(createTablePanel(), BorderLayout.CENTER);

    loadDummyData(); // sementara
  }

  // PANEL ATAS (SEARCH)
  private JPanel createTopPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel lblSearch = new JLabel("Cari Game:");
    txtSearch = new JTextField();

    txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        filterTable(txtSearch.getText());
      }
    });

    panel.add(lblSearch, BorderLayout.WEST);
    panel.add(txtSearch, BorderLayout.CENTER);

    return panel;
  }

  // TABEL
  private JScrollPane createTablePanel() {
    String[] kolom = { "Nama Game", "Harga", "Tanggal Transaksi" };
    tableModel = new DefaultTableModel(kolom, 0);
    table = new JTable(tableModel);

    return new JScrollPane(table);
  }

  // SEARCH LOGIC (VIEW SIDE)
  private void filterTable(String keyword) {
    tableModel.setRowCount(0);

    for (Object[] row : getDummyData()) {
      if (row[0].toString().toLowerCase().contains(keyword.toLowerCase())) {
        tableModel.addRow(row);
      }
    }
  }

  // DATA SEMENTARA
  private void loadDummyData() {
    for (Object[] row : getDummyData()) {
      tableModel.addRow(row);
    }
  }

  private List<Object[]> getDummyData() {
    List<Object[]> data = new ArrayList<>();
    data.add(new Object[] { "Cyber Adventure", 150000, "2025-01-10" });
    data.add(new Object[] { "Zombie World", 200000, "2025-01-12" });
    data.add(new Object[] { "Racing Pro", 175000, "2025-01-15" });
    return data;
  }
}
