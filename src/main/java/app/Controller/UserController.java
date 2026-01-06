package app.Controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import app.Model.User;
import app.Model.UserModel;
import app.Util.Session;

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

    // === LOGIN ===
    public String login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username dan Password harus diisi");
            return null;
        }

        try {
            User user = model.checkLogin(username, password);
            if (user != null) {
                Session.getInstance().login(user);
                JOptionPane.showMessageDialog(null, "Login Berhasil sebagai " + user.getRole());
                return user.getRole();
            } else {
                JOptionPane.showMessageDialog(null, "Username atau Password salah!");
                return null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal login: " + e.getMessage());
            return null;
        }
    }

    // === REGISTER ===
    public boolean register(String username, String password, String role) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username dan Password harus diisi");
            return false;
        }

        try {
            if (model.usernameExists(username)) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
                return false;
            }

            model.insertUser(username, password, role);
            JOptionPane.showMessageDialog(null, "Registrasi berhasil! Silakan login.");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal registrasi: " + e.getMessage());
            return false;
        }
    }

    // kalau butuh buat CRUD user, panggil aja ini :)
    public void addUser(String username, String password, String role) {
        register(username, password, role);
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