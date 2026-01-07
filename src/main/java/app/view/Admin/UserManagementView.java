package app.view.Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.Controller.UserController;
import app.Model.User;
import app.Util.Session;

public class UserManagementView extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private UserController controller;

    // Dark theme colors
    private static final Color BG_PANEL = new Color(30, 30, 30);
    private static final Color BG_TABLE = new Color(45, 45, 45);
    private static final Color BG_TABLE_HEADER = new Color(35, 35, 35);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_PRIMARY = new Color(200, 200, 200);
    private static final Color ACCENT_GREEN = new Color(76, 175, 80);
    private static final Color ACCENT_ORANGE = new Color(255, 152, 0);
    private static final Color ACCENT_RED = new Color(244, 67, 54);
    private static final Color BTN_BLUE = new Color(66, 133, 244);
    private static final Color BTN_GRAY = new Color(100, 100, 100);

    public UserManagementView() {
        this.controller = new UserController();
        initUI();
        refreshData();
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

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> refreshData());

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
        model = new DefaultTableModel(new Object[] { "ID", "Username", "Role" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
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

        // Role column renderer
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String role = (String) value;
                if (!isSelected) {
                    if ("ADMIN".equals(role)) {
                        c.setForeground(ACCENT_RED);
                    } else if ("DEVELOPER".equals(role)) {
                        c.setForeground(ACCENT_ORANGE);
                    } else {
                        c.setForeground(ACCENT_GREEN);
                    }
                    c.setBackground(BG_TABLE);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

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

        // Style components
        txtUsername.setBackground(BG_TABLE);
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtPassword.setBackground(BG_TABLE);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        cmbRole.setBackground(BG_TABLE);
        cmbRole.setForeground(Color.WHITE);

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBackground(BG_PANEL);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        JLabel lblRole = new JLabel("Role:");
        lblUsername.setForeground(TEXT_PRIMARY);
        lblPassword.setForeground(TEXT_PRIMARY);
        lblRole.setForeground(TEXT_PRIMARY);

        p.add(lblUsername);
        p.add(txtUsername);
        p.add(lblPassword);
        p.add(txtPassword);
        p.add(lblRole);
        p.add(cmbRole);

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
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih user untuk diedit");
            return;
        }
        int id = (int) model.getValueAt(r, 0);
        String currentUsername = (String) model.getValueAt(r, 1);
        String currentRole = (String) model.getValueAt(r, 2);

        // Prevent modifying own account role/status if logged in
        User loggedInUser = Session.getInstance().getUser();
        if (loggedInUser != null && loggedInUser.getId() == id) {
            JOptionPane.showMessageDialog(this,
                    "Anda tidak dapat mengedit akun Anda sendiri dari sini.\nSilakan gunakan menu profil jika tersedia.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField txtUsername = new JTextField(currentUsername);
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[] { "USER", "DEVELOPER", "ADMIN" });
        cmbRole.setSelectedItem(currentRole);

        // Style components
        txtUsername.setBackground(BG_TABLE);
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtPassword.setBackground(BG_TABLE);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        cmbRole.setBackground(BG_TABLE);
        cmbRole.setForeground(Color.WHITE);

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.setBackground(BG_PANEL);

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password (kosong = tidak diubah):");
        JLabel lblRole = new JLabel("Role:");
        lblUsername.setForeground(TEXT_PRIMARY);
        lblPassword.setForeground(TEXT_PRIMARY);
        lblRole.setForeground(TEXT_PRIMARY);

        p.add(lblUsername);
        p.add(txtUsername);
        p.add(lblPassword);
        p.add(txtPassword);
        p.add(lblRole);
        p.add(cmbRole);

        int ok = JOptionPane.showConfirmDialog(this, p, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            String newUsername = txtUsername.getText().trim();
            String newRole = (String) cmbRole.getSelectedItem();

            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong");
                return;
            }

            // Check if username changed and if it conflicts with existing users
            // Note: Ideally controller should have a checkUsernameExists method that
            // excludes current ID
            // For now we assume controller handles basic updates, but we can prevent
            // obvious duplicates if we had the list

            // Attempt to update user details
            boolean success = controller.updateUser(id, newUsername, newRole);

            if (success) {
                // If password is not empty, update password too
                String newPassword = new String(txtPassword.getPassword());
                if (!newPassword.isEmpty()) {
                    controller.updateUserPassword(id, newPassword);
                }
                refreshData();
            }
        }
    }

    private void onDelete() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih user untuk dihapus");
            return;
        }
        int id = (int) model.getValueAt(r, 0);
        User current = Session.getInstance().getUser();

        if (current != null && "ADMIN".equalsIgnoreCase(current.getRole()) && current.getId() == id) {
            JOptionPane.showMessageDialog(this, "Admin tidak dapat menghapus akun yang sedang login");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus user ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteUser(id);
            refreshData();
        }
    }
}