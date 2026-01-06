package app.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database.DBConnection;

public class TransactionModel {

    public void insertTransaction(int userId, int itemId, int amount, boolean isGame) throws SQLException {
        String id;
        if (!isGame) {
            id = "TP_" + String.valueOf(itemId);
        } else {
            id = "GM_" + String.valueOf(itemId);
        }
        String sql = "INSERT INTO tr_transaction (user_id, item_id, amount) VALUES (?, ?, ?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, id);
        ps.setInt(3, amount);

        ps.executeUpdate();
    }

    public List<Integer> getOwnedGameIds(int userId) throws SQLException {
        List<Integer> ownedIds = new ArrayList<>();
        String sql = "SELECT item_id FROM tr_transaction WHERE user_id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String itemId = rs.getString("item_id");
            if (itemId.startsWith("TP_")) {
                continue; // Skip top-up transactions
            }
            int gameId = Integer.parseInt(itemId.substring(3)); // Skip "GM_" prefix
            ownedIds.add(gameId);
        }
        return ownedIds;
    }

    public int getGameDeveloperId(int gameId) throws SQLException {
        String sql = "SELECT developer_id FROM mst_game WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, gameId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("developer_id");
        } else {
            throw new SQLException("Game not found");
        }
    }

    /**
     * Get all owned games with full details for a user
     * Returns: id, name, genre, price, is_played
     */
    public List<Object[]> getOwnedGamesWithDetails(int userId) throws SQLException {
        List<Object[]> games = new ArrayList<>();
        String sql = "SELECT g.id, g.name, ge.name AS genre, g.price, " +
                "COALESCE(t.is_played, FALSE) AS is_played, u.username AS developer " +
                "FROM tr_transaction t " +
                "JOIN mst_game g ON CONCAT('GM_', g.id) = t.item_id " +
                "JOIN ref_genre ge ON g.genre_id = ge.id " +
                "JOIN ref_user u ON g.developer_id = u.id " +
                "WHERE t.user_id = ? AND t.item_id LIKE 'GM_%'";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            games.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("genre"),
                    rs.getString("developer"),
                    rs.getInt("price"),
                    rs.getBoolean("is_played")
            });
        }
        return games;
    }

    /**
     * Remove a game from user's library (delete transaction record)
     */
    public void removeGameFromLibrary(int userId, int gameId) throws SQLException {
        String sql = "DELETE FROM tr_transaction WHERE user_id = ? AND item_id = ?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, "GM_" + gameId);
        ps.executeUpdate();
    }

    /**
     * Check if a game has been played by user
     */
    public boolean isGamePlayed(int userId, int gameId) throws SQLException {
        String sql = "SELECT is_played FROM tr_transaction WHERE user_id = ? AND item_id = ?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, "GM_" + gameId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("is_played");
        }
        return false;
    }

    /**
     * Mark a game as played
     */
    public void markGameAsPlayed(int userId, int gameId) throws SQLException {
        String sql = "UPDATE tr_transaction SET is_played = TRUE WHERE user_id = ? AND item_id = ?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setString(2, "GM_" + gameId);
        ps.executeUpdate();
    }
}
