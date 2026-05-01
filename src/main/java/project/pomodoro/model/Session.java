package project.pomodoro.model;

import java.sql.Timestamp;

public class Session {
    private int id;
    private Timestamp timestamp;
    private String sessionType;
    private String sessionTask;
    private String timespent;
    private boolean completion;

    public Session(int id, Timestamp timestamp, String sessionType, String sessionTask, String timeSpent, boolean completion) {
        this.id = id;
        this.timestamp = timestamp;
        this.sessionType = sessionType;
        this.sessionTask = sessionTask;
        this.timespent = timeSpent;
        this.completion = completion;
    }

    public Session(Timestamp timestamp, String sessionType, String sessionTask, String timespent, boolean completion) {
        this.timestamp = timestamp;
        this.sessionType = sessionType;
        this.sessionTask = sessionTask;
        this.timespent = timespent;
        this.completion = completion;
    }

    public Session() {}

    public void Completed() {
        completion = true;
    }

    public void setId(int id) { this.id = id; }

    public void setTimestamp(Timestamp timestamp) {this.timestamp = timestamp; }

    public void setSessionType(String sessionType) {this.sessionType = sessionType; }

    public void setSessionTask(String sessionTask) {this.sessionTask = sessionTask; }

    public void setTimespent(String timespent) {this.timespent = timespent; }

    public void setCompletion(Boolean completion) {this.completion = completion; }

    public int getId() {return id;}

    public Timestamp getTimestamp() {return timestamp;}

    public String getSessionType() {
        return sessionType;
    }

    public String getSessionTask() { return sessionTask; }

    public String getTimespent() { return timespent; }

    public boolean getCompletion() {return completion;}

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", sessionTask='" + sessionTask + '\'' +
                ", sessionType=" + sessionType + '\'' +
                ", timespent='" + timespent + '\'' +
                ", completion=" + completion +
                '}';
    }
}
