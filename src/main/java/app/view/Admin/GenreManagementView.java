package app.view.Admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.Controller.GenreController;

public class GenreManagementView extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private GenreController controller;

    public GenreManagementView() {
        this.controller = new GenreController();

        setTitle("Manajemen Genre");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table
        String[] columns = { "ID", "Nama Genre" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Ubah");
        JButton btnDelete = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteGenre());
        btnRefresh.addActionListener(e -> loadData());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Object[]> genres = controller.getAllGenres();
        if (genres != null) {
            for (Object[] genre : genres) {
                model.addRow(genre);
            }
        }
    }

    private void showAddDialog() {
        String name = JOptionPane.showInputDialog(this, "Masukkan Nama Genre Baru:");
        if (name != null) {
            controller.addGenre(name);
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih genre yang ingin diubah!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        String currentName = (String) model.getValueAt(selectedRow, 1);

        String newName = JOptionPane.showInputDialog(this, "Ubah Nama Genre:", currentName);
        if (newName != null && !newName.trim().isEmpty()) {
            controller.updateGenre(id, newName);
            loadData();
        }
    }

    private void deleteGenre() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih genre yang ingin dihapus!");
            return;
        }

        int id = (int) model.getValueAt(selectedRow, 0);
        controller.deleteGenre(id);
        loadData();
    }
}
