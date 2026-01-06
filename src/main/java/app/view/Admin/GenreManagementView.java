package app.view.Admin;

import app.Controller.GenreController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GenreManagementView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private GenreController controller;

    // Dark theme colors
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_TABLE = new Color(45, 45, 45);
    private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    // private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_ORANGE = new Color(255, 152, 0);
    private static final Color ACCENT_RED = new Color(244, 67, 54);
    private static final Color BTN_BLUE = new Color(66, 133, 244);
    private static final Color BTN_GRAY = new Color(100, 100, 100);

    public GenreManagementView() {
        this.controller = new GenreController();

        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BG_PANEL);

        // Toolbar
        add(createToolbar(), BorderLayout.NORTH);

        // Table
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnAdd = createStyledButton("Tambah", ACCENT_GREEN);
        JButton btnEdit = createStyledButton("Edit", ACCENT_ORANGE);
        JButton btnDelete = createStyledButton("Hapus", ACCENT_RED);
        JButton btnRefresh = createStyledButton("Refresh", BTN_GRAY);

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteGenre());
        btnRefresh.addActionListener(e -> loadData());

        panel.add(btnAdd);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnEdit);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnDelete);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(btnRefresh);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "ID", "Nama Genre" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(BG_TABLE);
        table.setForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(BTN_BLUE);
        table.setSelectionForeground(Color.WHITE);

        // Style header
        table.getTableHeader().setBackground(BG_TABLE_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_TABLE);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        return scrollPane;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
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
        if (name != null && !name.trim().isEmpty()) {
            controller.addGenre(name.trim());
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
            controller.updateGenre(id, newName.trim());
            loadData();
        }
    }

    private void deleteGenre() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih genre yang ingin dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus genre ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(selectedRow, 0);
            controller.deleteGenre(id);
            loadData();
        }
    }
}
