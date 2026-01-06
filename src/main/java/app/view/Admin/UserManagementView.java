package app.view.Admin;

import app.Controller.UserController;
import app.Util.Session;
import app.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementView extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private UserController controller;

    public UserManagementView() {
        this.controller = new UserController();
        setTitle("User Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(createPanel());
        refreshData();
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        model = new DefaultTableModel(new Object[] { "ID", "Username", "Role" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> refreshData());
        btnClose.addActionListener(e -> this.dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnEdit);
        bottom.add(btnDelete);
        bottom.add(btnRefresh);
        bottom.add(btnClose);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshData() {
        model.setRowCount(0);
        try {
            List<Object[]> users = controller.getAllUsers();
            if (users != null) {
                for (Object[] u : users) {
                    model.addRow(new Object[] { u[0], u[1], u[2] });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat user: " + ex.getMessage());
        }
    }

    private void onEdit() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih user untuk diedit"); return; }
        int id = (int) model.getValueAt(r, 0);
        String username = (String) model.getValueAt(r, 1);
        String role = (String) model.getValueAt(r, 2);

        JTextField txtUsername = new JTextField(username);
        JComboBox<String> cmbRole = new JComboBox<>(new String[] { "USER", "DEVELOPER", "ADMIN" });
        cmbRole.setSelectedItem(role);

        JPanel p = new JPanel(new GridLayout(2,2,5,5));
        p.add(new JLabel("Username:")); p.add(txtUsername);
        p.add(new JLabel("Role:")); p.add(cmbRole);

        int ok = JOptionPane.showConfirmDialog(this, p, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String newUsername = txtUsername.getText().trim();
            String newRole = (String) cmbRole.getSelectedItem();
            if (newUsername.isEmpty()) { JOptionPane.showMessageDialog(this, "Username tidak boleh kosong"); return; }
            controller.updateUser(id, newUsername, newRole);
            refreshData();
        }
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih user untuk dihapus"); return; }
        int id = (int) model.getValueAt(r, 0);
        User current = Session.getInstance().getUser();

        // Proteksi: Admin tidak bisa menghapus akunnya sendiri yang sedang login
        if (current != null && "ADMIN".equalsIgnoreCase(current.getRole()) && current.getId() == id) {
            JOptionPane.showMessageDialog(this, "Admin tidak dapat menghapus akun yang sedang login");
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this, "Hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            controller.deleteUser(id);
            refreshData();
        }
    }

    // Untuk membuka view ini langsung (opsional)
    public static void open() {
        SwingUtilities.invokeLater(() -> new UserManagementView().setVisible(true));
    }
}