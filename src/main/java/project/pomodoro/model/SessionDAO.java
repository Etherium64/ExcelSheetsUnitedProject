package project.pomodoro.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Data Access Object for managing the CRUD operations of Session Data
 * Connects to the Sqlite database vis DBConnection Class
 * Allows for persistent storage of Pomodoro Session Data
 */
public class SessionDAO {
    private Connection connection;

    /**
     * Public constructor that instantiates connection by calling DBConnection method getConnection
     */
    public SessionDAO() {
        connection = DBConnection.getConnection();
    }

    /**
     * Create an SQL Table sessions in the Database if none yet exist
     */
    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS sessions ("
                            + "id INTEGER PRIMARY KEY,"
                            + "timestamp TIMESTAMP NOT NULL,"
                            + "sessionType VARCHAR NOT NULL, "
                            + "sessionTask VARCHAR NOT NULL,"
                            + "timespent VARCHAR NOT NULL,"
                            + "completion BIT NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void dropTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "DROP TABLE sessions";
            statement.execute(query);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /**
     * CRUD Operation - Create Operation
     * Insert a new Session to the database, assigning the new Session fields
     */
    public void insert(Session session) {
        try {
            PreparedStatement insertSession = connection.prepareStatement(
                    "INSERT INTO sessions (timestamp, sessionType, sessionTask, timespent, completion) VALUES (?, ?, ?, ?, ?)"
            );
            insertSession.setTimestamp(1, session.getTimestamp());
            insertSession.setString(2, session.getSessionType());
            insertSession.setString(3, session.getSessionTask());
            insertSession.setString(4, session.getTimespent());
            insertSession.setBoolean(5, session.getCompletion());
            insertSession.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /**
     * CRUD Operation - Update  Operation
     * Update a currently existing Session in the database
     * Perform this after calling Session setter methods
     */
    public void update(Session session) {
        try {
            PreparedStatement updateSession = connection.prepareStatement(
                    "UPDATE sessions SET timestamp = ?, sessionType = ?, sessionTask = ?, timespent = ?, completion=? WHERE id = ?"
            );
            updateSession.setTimestamp(1, session.getTimestamp());
            updateSession.setString(2, session.getSessionType());
            updateSession.setString(3, session.getSessionTask());
            updateSession.setString(4, session.getTimespent());
            updateSession.setBoolean(5, session.getCompletion());
            updateSession.setInt(6, session.getId());
            updateSession.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }


    /**
     * CRUD Operation - Delete Operation
     * Delete a currently existing Session in the database
     */
    public void delete(Session session) {
        try {
            PreparedStatement deleteSession = connection.prepareStatement("DELETE FROM sessions WHERE id = ?");
            deleteSession.setInt(1, session.getId());
            deleteSession.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    /**
     * CRUD Operation - Read Operation
     * Returns a list of all the Session data to be read
     * List is moved to an ObservableList which is then used to populate the TableView
     */
    public List<Session> getAll() {
        List<Session> sessions = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet resultset = getAll.executeQuery("SELECT * FROM sessions");
            while (resultset.next()) {
                sessions.add(
                        new Session(
                                resultset.getInt("id"),
                                resultset.getTimestamp("timestamp"),
                                resultset.getString("sessionType"),
                                resultset.getString("sessionTask"),
                                resultset.getString("timespent"),
                                resultset.getBoolean("completion")
                        )
                );
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return sessions;
    }

    /**
     * Retrieve the session via timestamp rather than id. Used before updating the session.
     * Easier for the current instance of the Pomodoro Controller / Session DAO to find the timestamp of the latest session rather than the id
     * As id is always auto-incrementing
     */
    public Session getByTimestamp(Timestamp timestamp) {
        try {
            PreparedStatement getSession = connection.prepareStatement("SELECT * FROM sessions WHERE timestamp = ?");
            getSession.setTimestamp(1, timestamp);
            ResultSet resultset = getSession.executeQuery();
            if (resultset.next()) {
                return new Session(
                        resultset.getInt("id"),
                        resultset.getTimestamp("timestamp"),
                        resultset.getString("sessionType"),
                        resultset.getString("sessionTask"),
                        resultset.getString("timespent"),
                        resultset.getBoolean("completion")
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public Session getById(int id) {
        try {
            PreparedStatement getMockSession = connection.prepareStatement("SELECT * FROM sessions WHERE id = ?");
            getMockSession.setInt(1, id);
            ResultSet resultset = getMockSession.executeQuery();
            if (resultset.next()) {
                return new Session(
                        resultset.getInt("id"),
                        resultset.getTimestamp("timestamp"),
                        resultset.getString("sessionType"),
                        resultset.getString("sessionTask"),
                        resultset.getString("timespent"),
                        resultset.getBoolean("completion")
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }


    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}




