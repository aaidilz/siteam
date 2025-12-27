package app;

import java.sql.Connection;
import java.sql.SQLException;

import app.Database.DBConnection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.configDB();
            String sql = "SELECT * FROM game";
            var statement = conn.createStatement();
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("Game ID: " + resultSet.getInt("id") + ", Name: " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}