package app.View.Admin;

import app.Controller.GenreController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GenreManagementView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private GenreController controller;

    public GenreManagementView() {
        this.controller = new GenreController();
        
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbarPanel = new JPanel(new BorderLayout());
        

        JPanel leftBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        
        leftBtnPanel.add(btnAdd);
        leftBtnPanel.add(btnEdit);
        leftBtnPanel.add(btnDelete);
        leftBtnPanel.add(btnRefresh);

        toolbarPanel.add(leftBtnPanel, BorderLayout.WEST);
        
        add(toolbarPanel, BorderLayout.NORTH);


        String[] columns = {"ID", "Nama Genre"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

  
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteGenre());
        btnRefresh.addActionListener(e -> loadData());
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
