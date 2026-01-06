package app.View.Admin;

import app.Controller.UserController;
import app.Util.Session;
import app.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private UserController controller;

    public UserManagementView() {
        this.controller = new UserController();
        initUI();
        refreshData();
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


        model = new DefaultTableModel(new Object[] { "ID", "Username", "Role" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> refreshData());
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

    private void onAdd() {
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[] { "USER", "DEVELOPER", "ADMIN" });

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.add(new JLabel("Username:")); p.add(txtUsername);
        p.add(new JLabel("Password:")); p.add(txtPassword);
        p.add(new JLabel("Role:")); p.add(cmbRole);

        int ok = JOptionPane.showConfirmDialog(this, p, "Tambah User", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String role = (String) cmbRole.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi");
                return;
            }
            controller.addUser(username, password, role);
            refreshData();
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

        
        if (current != null && "ADMIN".equalsIgnoreCase(current.getRole()) && current.getId() == id) {
            JOptionPane.showMessageDialog(this, "Admin tidak dapat menghapus akun yang sedang login");
            return;
        }
        
        controller.deleteUser(id);
        refreshData();
    }
}