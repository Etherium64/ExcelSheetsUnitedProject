package project.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {

    private Connection connection;
    public UserDAO() {
        connection = DatabaseConnection.getConnection();
        createTable();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "user_id INTEGER PRIMARY KEY,"
                            + "username VARCHAR,"
                            + "password VARCHAR NOT NULL,"
                            + "salt BYTES,"
                            + "registered BIT NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(User user) {
        try {
            PreparedStatement insertUser = connection.prepareStatement(
                    "INSERT INTO users (username, password, salt, registered) VALUES (?, ?, ?, ?)"
            );
            insertUser.setString(1, user.getUsername());
            insertUser.setString(2, user.getPassword());
            insertUser.setBytes(3, user.getSalt());
            insertUser.setBoolean(4, user.getRegistered());
            insertUser.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(User user) {
        try {
            PreparedStatement updateUser = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, salt = ?, registered = ? WHERE user_id = ?"
            );
            updateUser.setString(1, user.getUsername());
            updateUser.setString(2, user.getPassword());
            updateUser.setBytes(3, user.getSalt());
            updateUser.setBoolean(4, user.getRegistered());
            updateUser.setInt(5, user.getUser_id());
            updateUser.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet resultset = getAll.executeQuery("SELECT * FROM users LIMIT 15");
            while (resultset.next()) {
                users.add(
                        new User(
                                resultset.getInt("user_id"),
                                resultset.getString("username"),
                                resultset.getString("password"),
                                resultset.getBytes("salt"),
                                resultset.getBoolean("registered")
                        )
                );
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return users;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

}
