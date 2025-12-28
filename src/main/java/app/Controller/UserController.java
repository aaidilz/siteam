package app.Controller;

import app.Model.UserModel;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class UserController {
    private UserModel model;

    public UserController() {
        this.model = new UserModel();
    }

    public List<Object[]> getAllUsers() {
        try {
            return model.getAllUser();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat user: " + e.getMessage());
            return null;
        }
    }

    public void addUser(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username dan Password harus diisi");
            return;
        }

        try {
            if (model.usernameExists(username)) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
                return;
            }
            
            model.insertUser(username, password, role);
            JOptionPane.showMessageDialog(null, "User berhasil ditambahkan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal tambah user: " + e.getMessage());
        }
    }

    public void updateUser(int id, String username, String role) {
        try {
            model.updateUser(id, username, role);
            JOptionPane.showMessageDialog(null, "User berhasil diupdate!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update user: " + e.getMessage());
        }
    }

    public void deleteUser(int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Hapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteUser(id);
                JOptionPane.showMessageDialog(null, "User berhasil dihapus!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal hapus user: " + e.getMessage());
            }
        }
    }
}