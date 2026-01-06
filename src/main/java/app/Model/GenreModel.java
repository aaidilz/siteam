package app.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.Database.DBConnection;

public class GenreModel {

    // CREATE
    public void insertGenre(String name) throws SQLException {
        String sql = "INSERT INTO ref_genre (name) VALUES (?)";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        ps.executeUpdate();
    }

    // UPDATE
    public void updateGenre(int id, String name) throws SQLException {
        String sql = "UPDATE ref_genre SET name=? WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    // DELETE
    public void deleteGenre(int id) throws SQLException {
        String sql = "DELETE FROM ref_genre WHERE id=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // READ
    public List<Object[]> getAllGenre() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT id, name FROM ref_genre";
        Statement st = DBConnection.configDB().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            data.add(new Object[] {
                    rs.getInt("id"),
                    rs.getString("name")
            });
        }
        return data;
    }

    // VALIDASI
    public boolean genreExists(String name) throws SQLException {
        String sql = "SELECT name FROM ref_genre WHERE name=?";
        PreparedStatement ps = DBConnection.configDB().prepareStatement(sql);
        ps.setString(1, name);
        return ps.executeQuery().next();
    }
}
