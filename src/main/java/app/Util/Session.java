package app.Util;

import app.Model.User;

public class Session {
    private static Session instance;
    private User currentUser;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getUser() {
        return currentUser;
    }
}
