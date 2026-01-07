package app.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {

    private static Connection mysqlconfig;

    public static Connection configDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/gamestore?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String pass = ""; // GANTI jika root pakai password

            mysqlconfig = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Koneksi gagal: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        return mysqlconfig;
    }
}
