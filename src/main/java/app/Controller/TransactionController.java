package app.Controller;

import app.Model.TransactionModel;
import app.Util.Session;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class TransactionController {
    private TransactionModel model;

    public TransactionController() {
        this.model = new TransactionModel();
    }

    public boolean buyGame(int gameId, int price) {
        if (Session.getInstance().getUser() == null) {
            JOptionPane.showMessageDialog(null, "Anda harus login untuk membeli game!");
            return false;
        }

        int userId = Session.getInstance().getUser().getId();

        try {
            // Check if already owned
            List<Integer> ownedGames = model.getOwnedGameIds(userId);
            if (ownedGames.contains(gameId)) {
                JOptionPane.showMessageDialog(null, "Anda sudah memiliki game ini!");
                return false;
            }

            model.insertTransaction(userId, gameId, price);
            JOptionPane.showMessageDialog(null, "Pembelian berhasil!");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Transaksi gagal: " + e.getMessage());
            return false;
        }
    }

    public boolean isGameOwned(int gameId) {
        if (Session.getInstance().getUser() == null)
            return false;
        int userId = Session.getInstance().getUser().getId();
        try {
            return model.getOwnedGameIds(userId).contains(gameId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
