package project.model;

import java.sql.Timestamp;

/**
 * Custom data model class for a Pomodoro Session.
 * Represents each Pomodoro session that the User initiates. .
 * Data is stored in session.db for persistence.
 * TableviewFXML represents Session Data as an editable GUI
 * SessionDAO allows users to perform CRUD operations on Session Data
 *
 * @author Minhman Do
 */
public class Session {
    /**
     * Session id which identifies the SQL row. Autoincrements.
     */
    private int session_id;
    /**
     * Session Timestamp, the exact time and date a Session is created.
     */
    private Timestamp timestamp;
    /**
     * Type of Pomodoro Session, Work vs Rest.
     */
    private String sessionType;
    /**
     * Task of the Pomodoro Session. E.g. Work Session where the Task is Programming.
     */
    private String sessionTask;
    /**
     * Total time spent on the session, whether full time elapsed or partial.
     */
    private String timespent;
    /**
     * Whether User completed the full Pomodoro session or not.
     */
    private boolean completion;

    private int user_id;

    /**
     * Session Public Constructor
     */
    public Session(int session_id, Timestamp timestamp, String sessionType, String sessionTask, String timeSpent, boolean completion, int user_id) {
        this.session_id = session_id;
        this.timestamp = timestamp;
        this.sessionType = sessionType;
        this.sessionTask = sessionTask;
        this.timespent = timeSpent;
        this.completion = completion;
        this.user_id = user_id;
    }

    /**
     * Session Public Constructor without id field.
     * For inserting new Sessions after the first, as id will auto increment in SQLite.
     */
    public Session(Timestamp timestamp, String sessionType, String sessionTask, String timespent, boolean completion, int user_id) {
        this.timestamp = timestamp;
        this.sessionType = sessionType;
        this.sessionTask = sessionTask;
        this.timespent = timespent;
        this.completion = completion;
        this.user_id = user_id;
    }

    public int getSession_Id() {
        return session_id;
    }

    /**
     * All Session field setters and getters for use in the DAO
     */
    public void setSession_Id(int session_id) {
        this.session_id = session_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionTask() {
        return sessionTask;
    }

    public void setSessionTask(String sessionTask) {
        this.sessionTask = sessionTask;
    }

    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }

    public boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * Represents all of a Session field values as a concatenated string
     */
    @Override
    public String toString() {
        return "Session{" +
                "session_id=" + session_id +
                ", timestamp='" + timestamp + '\'' +
                ", sessionTask='" + sessionTask + '\'' +
                ", sessionType=" + sessionType + '\'' +
                ", timespent='" + timespent + '\'' +
                ", completion=" + completion + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
