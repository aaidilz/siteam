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
}
