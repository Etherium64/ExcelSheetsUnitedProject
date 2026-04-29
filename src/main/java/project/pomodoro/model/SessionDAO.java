package project.pomodoro.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SessionDAO {
    private Connection connection;

    public SessionDAO() {
        connection = DBConnection.getConnection();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS sessions ("
                            + "id INTEGER PRIMARY KEY,"
                            + "timestamp TIMESTAMP NOT NULL,"
                            + "sessionType VARCHAR NOT NULL, "
                            + "sessionTask VARCHAR NOT NULL,"
                            + "timeSpent VARCHAR NOT NULL,"
                            + "completion BIT NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(Session session) {
        try {
            PreparedStatement insertSession = connection.prepareStatement(
                    "INSERT INTO sessions (timestamp, sessionType, sessionTask, timeSpent, completion) VALUES (?, ?, ?, ?, ?)"
            );
            insertSession.setTimestamp(1, session.getTimestamp());
            insertSession.setString(2, session.getSessionType());
            insertSession.setString(3, session.getSessionTask());
            insertSession.setString(4, session.getTimeSpent());
            insertSession.setBoolean(5, session.getCompletion());
            insertSession.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void update(Session session) {
        try {
            PreparedStatement updateSession = connection.prepareStatement(
                    "UPDATE sessions SET timestamp = ?, sessionType = ?, sessionTask = ?, timeSpent = ?, completion=? WHERE id = ?"
            );
            updateSession.setTimestamp(1, session.getTimestamp());
            updateSession.setString(2, session.getSessionType());
            updateSession.setString(3, session.getSessionTask());
            updateSession.setString(4, session.getTimeSpent());
            updateSession.setBoolean(5, session.getCompletion());
            updateSession.setInt(6, session.getId());
            updateSession.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<Session> getAll() {
        List<Session> sessions = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet resultset = getAll.executeQuery("SELECT * FROM sessions");
            while (resultset.next()) {
                sessions.add(
                        new Session(
                                resultset.getInt("Id"),
                                resultset.getTimestamp("timestamp"),
                                resultset.getString("sessionType"),
                                resultset.getString("sessionTask"),
                                resultset.getString("timeSpent"),
                                resultset.getBoolean("completion")
                        )
                );
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return sessions;
    }

    public Session getByTimestamp(Timestamp timestamp) {
        try {
            PreparedStatement getSession = connection.prepareStatement("SELECT * FROM sessions WHERE timestamp = ?");
            getSession.setTimestamp(1, timestamp);
            ResultSet resultset = getSession.executeQuery();
            if (resultset.next()) {
                return new Session(
                        resultset.getInt("Id"),
                        resultset.getTimestamp("timestamp"),
                        resultset.getString("sessionType"),
                        resultset.getString("sessionTask"),
                        resultset.getString("timeSpent"),
                        resultset.getBoolean("completion")
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }
}




