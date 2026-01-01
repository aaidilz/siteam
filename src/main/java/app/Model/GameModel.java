package app.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import app.Database.DBConnection;

public class GameModel {

    // ===== CREATE =====
    public void insertGame(String name, int genreId, int developerId, int price) throws SQLException {
        String sql = "INSERT INTO game (name, genre_id, developer_id, price) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, genreId);
        ps.setInt(3, developerId);
        ps.setInt(4, price);
        ps.executeUpdate();
    }

    // ===== UPDATE =====
    public void updateGame(int id, String name, int genreId, int price) throws SQLException {
        String sql = "UPDATE game SET name=?, genre_id=?, price=? WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, genreId);
        ps.setInt(3, price);
        ps.setInt(4, id);
        ps.executeUpdate();
    }

    // ===== DELETE =====
    public void deleteGame(int id) throws SQLException {
        String sql = "DELETE FROM game WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // ===== READ =====
    public List<Object[]> getAllGame() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT g.id, g.name, ge.name AS genre, g.price, g.genre_id " +
                "FROM game g JOIN genre ge ON g.genre_id = ge.id";

        Statement st = DBConnection.configDB().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            data.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("genre"),
                    rs.getInt("price"),
                    rs.getInt("genre_id")
            });
        }
        return data;
    }

    // ===== VALIDASI =====
    public boolean gameExists(String name) throws SQLException {
        String sql = "SELECT name FROM game WHERE name=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        return ps.executeQuery().next();
    }
}
