package app.Controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import app.Model.GameModel;

public class GameController {
    private GameModel model;

    public GameController() {
        this.model = new GameModel();
    }

    // ===== READ =====
    public List<Object[]> getAllGames() {
        try {
            return model.getAllGame();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat data game: " + e.getMessage());
            return null;
        }
    }

    // ===== CREATE =====
    public void addGame(String name, int genreId, int developerId, int price) {
        // Validasi nama dan harga
        if (name.isEmpty() || price <= 0) {
            JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong dan harga harus > 0");
            return;
        }

        try {
            model.insertGame(name, genreId, developerId, price);
            JOptionPane.showMessageDialog(null, "Game berhasil ditambahkan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah game: " + e.getMessage());
        }
    }

    // ===== UPDATE =====
    public void updateGame(int id, String name, int genreId, int price) {
        if (id <= 0 || name.isEmpty() || price <= 0) {
            JOptionPane.showMessageDialog(null, "Data tidak valid untuk update");
            return;
        }

        try {
            model.updateGame(id, name, genreId, price);
            JOptionPane.showMessageDialog(null, "Game berhasil diupdate!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update game: " + e.getMessage());
        }
    }

    // ===== DELETE =====
    public void deleteGame(int id) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, "Pilih game yang ingin dihapus");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteGame(id);
                JOptionPane.showMessageDialog(null, "Game berhasil dihapus!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menghapus game: " + e.getMessage());
            }
        }
    }
}