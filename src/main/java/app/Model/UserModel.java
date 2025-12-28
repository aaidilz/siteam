package app.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import app.Database.DBConnection;

public class UserModel {

    // ===== CREATE =====
    public void insertUser(String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, role);
        ps.executeUpdate();
    }

    // ===== UPDATE =====
    public void updateUser(int id, String username, String role) throws SQLException {
        String sql = "UPDATE user SET username=?, role=? WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, role);
        ps.setInt(3, id);
        ps.executeUpdate();
    }

    // ===== DELETE =====
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // ===== READ (JTable) =====
    public List<Object[]> getAllUser() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT id, username, role FROM user";
        Statement st = DBConnection.configDB().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            data.add(new Object[]{
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("role")
            });
        }
        return data;
    }

    // ===== VALIDASI =====
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT username FROM user WHERE username=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        return ps.executeQuery().next();
    }
}
