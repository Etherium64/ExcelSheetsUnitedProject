

package project.pomodoro.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static Connection instance = null;

    private DBConnection() {
        String url = "JDBC:sqlite:" + System.getProperty("user.home") +  "/.session/session.db";
        try {
            instance =  DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }
}

