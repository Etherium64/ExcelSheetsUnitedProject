

package project.pomodoro.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static String url = "JDBC:sqlite:" + System.getProperty("user.home") +  "/.session/session.db";

    public static Connection getInstance() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.err.println(ex);
            return null;
        }
    }
}

