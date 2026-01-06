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
            int developerID = modelTransaction.getGameDeveloperId(gameId);
            int developerSaldo = modelUser.getUserSaldo(developerID);
            modelUser.updateUserSaldo(developerID, developerSaldo + price);
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

    /**
     * Get all games owned by current user with details
     * Returns list of: [id, name, genre, price, is_played]
     */
    public List<Object[]> getOwnedGames() {
        if (Session.getInstance().getUser() == null)
            return new java.util.ArrayList<>();
        int userId = Session.getInstance().getUser().getId();
        try {
            return modelTransaction.getOwnedGamesWithDetails(userId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat library: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * Simulate playing a game and mark it as played
     */
    public boolean playGame(int gameId, String gameName) {
        if (Session.getInstance().getUser() == null) {
            JOptionPane.showMessageDialog(null, "Anda harus login!");
            return false;
        }

        int userId = Session.getInstance().getUser().getId();

        try {
            // Mark game as played
            modelTransaction.markGameAsPlayed(userId, gameId);

            // Show "playing" simulation dialog
            JOptionPane.showMessageDialog(null,
                    "Memainkan " + gameName + "...\n\n" +
                            "Game sedang berjalan!\n" +
                            "(Ini adalah simulasi)",
                    "Playing Game",
                    JOptionPane.INFORMATION_MESSAGE);

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memainkan game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove game from library with warning
     * Returns true if game was removed
     */
    public boolean removeFromLibrary(int gameId, String gameName) {
        if (Session.getInstance().getUser() == null) {
            JOptionPane.showMessageDialog(null, "Anda harus login!");
            return false;
        }

        int userId = Session.getInstance().getUser().getId();

        try {
            boolean isPlayed = modelTransaction.isGamePlayed(userId, gameId);

            // Build warning message
            String warningMessage;
            if (isPlayed) {
                warningMessage = "PERINGATAN\n\n" +
                        "Game \"" + gameName + "\" sudah pernah dimainkan.\n\n" +
                        "Jika Anda menghapus game ini dari library:\n" +
                        "- Dana pembelian TIDAK AKAN dikembalikan\n" +
                        "- Anda harus membeli ulang jika ingin memainkan lagi\n\n" +
                        "Yakin ingin menghapus game ini?";
            } else {
                warningMessage = "PERINGATAN\n\n" +
                        "Anda akan menghapus \"" + gameName + "\" dari library.\n\n" +
                        "Meskipun game belum pernah dimainkan:\n" +
                        "- Dana pembelian TIDAK AKAN dikembalikan\n" +
                        "- Anda harus membeli ulang jika ingin memainkan\n\n" +
                        "Yakin ingin menghapus game ini?";
            }

            int confirm = JOptionPane.showConfirmDialog(null,
                    warningMessage,
                    "Konfirmasi Hapus Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                modelTransaction.removeGameFromLibrary(userId, gameId);
                JOptionPane.showMessageDialog(null,
                        "Game \"" + gameName + "\" telah dihapus dari library.",
                        "Game Dihapus",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a specific game has been played
     */
    public boolean isGamePlayed(int gameId) {
        if (Session.getInstance().getUser() == null)
            return false;
        int userId = Session.getInstance().getUser().getId();
        try {
            return modelTransaction.isGamePlayed(userId, gameId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
