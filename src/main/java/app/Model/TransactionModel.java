package app.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database.DBConnection;

public class TransactionModel {

    public void insertTransaction(int userId, int gameId, int amount) throws SQLException {
        String sql = "INSERT INTO transaction (user_id, game_id, amount) VALUES (?, ?, ?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, gameId);
        ps.setInt(3, amount);
        ps.executeUpdate();
    }

    public List<Integer> getOwnedGameIds(int userId) throws SQLException {
        List<Integer> ownedIds = new ArrayList<>();
        String sql = "SELECT game_id FROM transaction WHERE user_id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ownedIds.add(rs.getInt("game_id"));
        }
        return ownedIds;
    }
}
