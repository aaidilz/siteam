package app.Controller;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import app.Model.GenreModel;

public class GenreController {
    private GenreModel model;

    public GenreController() {
        this.model = new GenreModel();
    }

    public List<Object[]> getAllGenres() {
        try {
            return model.getAllGenre();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat genre: " + e.getMessage());
            return null;
        }
    }

    public void addGenre(String name) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama genre tidak boleh kosong");
            return;
        }

        try {
            // Cek duplikasi
            if (model.genreExists(name)) {
                JOptionPane.showMessageDialog(null, "Genre sudah ada!");
                return;
            }

            model.insertGenre(name);
            JOptionPane.showMessageDialog(null, "Genre berhasil ditambahkan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah genre: " + e.getMessage());
        }
    }

    public void updateGenre(int id, String name) {
        try {
            model.updateGenre(id, name);
            JOptionPane.showMessageDialog(null, "Genre berhasil diupdate!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update genre: " + e.getMessage());
        }
    }

    public void deleteGenre(int id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Hapus genre ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.deleteGenre(id);
                JOptionPane.showMessageDialog(null, "Genre berhasil dihapus!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal hapus genre: " + e.getMessage());
            }
        }
    }
}