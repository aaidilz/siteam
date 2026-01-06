package app.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.Database.DBConnection;

public class UserModel {

    // ===== CREATE =====
    public void insertUser(String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO ref_user (username, password, role) VALUES (?, ?, ?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, role);
        ps.executeUpdate();
    }

    // ===== UPDATE =====
    public void updateUser(int id, String username, String role) throws SQLException {
        String sql = "UPDATE ref_user SET username=?, saldo=?, role=? WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(3, role);
        ps.setInt(4, id);
        ps.executeUpdate();
    }

    // ===== DELETE =====
    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM ref_user WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // ===== READ (JTable) =====
    public List<Object[]> getAllUser() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT id, username, role FROM ref_user";
        Statement st = DBConnection.configDB().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            data.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role")
            });
        }
        return data;
    }

    // ===== VALIDASI =====
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT username FROM ref_user WHERE username=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        return ps.executeQuery().next();
    }

    public User checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM ref_user WHERE username=? AND password=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
        }
        return null;
    }

    public int getUserSaldo(int userId) throws SQLException {
        String sql = "SELECT saldo FROM ref_user WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("saldo");
        }
        return 0;
    }

    public void updateUserSaldo(int userId, int newSaldo) throws SQLException {
        String sql = "UPDATE ref_user SET saldo=? WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, newSaldo);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }
}
