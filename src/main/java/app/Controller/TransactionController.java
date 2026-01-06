package app.Controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import app.Model.TransactionModel;
import app.Model.UserModel;
import app.Util.Session;

public class TransactionController {
    private TransactionModel modelTransaction;
    private UserModel modelUser;

    public TransactionController() {
        this.modelTransaction = new TransactionModel();
        this.modelUser = new UserModel();
    }

    public boolean buyGame(int gameId, int price) {
        if (Session.getInstance().getUser() == null) {
            JOptionPane.showMessageDialog(null, "Anda harus login untuk membeli game!");
            return false;
        }

        int userId = Session.getInstance().getUser().getId();

        try {
            // Check if already owned
            List<Integer> ownedGames = modelTransaction.getOwnedGameIds(userId);
            if (ownedGames.contains(gameId)) {
                JOptionPane.showMessageDialog(null, "Anda sudah memiliki game ini!");
                return false;
            }

            // Check saldo
            int currentSaldo = modelUser.getUserSaldo(userId);
            if (currentSaldo < price) {
                JOptionPane.showMessageDialog(null, "Saldo tidak mencukupi!");
                return false;
            }

            modelTransaction.insertTransaction(userId, gameId, price, true);
            modelUser.updateUserSaldo(userId, currentSaldo - price);
            JOptionPane.showMessageDialog(null, "Pembelian berhasil!");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Transaksi gagal: " + e.getMessage());
            return false;
        }
    }

    public boolean buyTopUp(int amount) {
        if (Session.getInstance().getUser() == null) {
            JOptionPane.showMessageDialog(null, "Anda harus login untuk membeli top-up!");
            return false;
        }

        int userId = Session.getInstance().getUser().getId();
        int topUpId = (int) (new Date().getTime() % Integer.MAX_VALUE);

        try {
            int addition = modelUser.getUserSaldo(userId) + amount;
            modelTransaction.insertTransaction(userId, topUpId, addition, false);
            modelUser.updateUserSaldo(userId, addition);
            JOptionPane.showMessageDialog(null, "Pembelian top-up berhasil!");
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
            return modelTransaction.getOwnedGameIds(userId).contains(gameId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserSaldo() {
        if (Session.getInstance().getUser() == null)
            return 0;
        int userId = Session.getInstance().getUser().getId();
        try {
            return modelUser.getUserSaldo(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
