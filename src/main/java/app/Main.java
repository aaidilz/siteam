package app;

import javax.swing.SwingUtilities;
import app.view.Auth.LoginView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}