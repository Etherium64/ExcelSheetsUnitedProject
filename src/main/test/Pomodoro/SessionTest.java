package Pomodoro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing Setters and Getters of Session Data Model
 */


public class SessionTest {
    private Session session;
    LocalDateTime nowSession = LocalDateTime.now();
    private Timestamp Now = Timestamp.valueOf(nowSession);
    LocalDateTime laterSession = nowSession.plusHours(1);
    private Timestamp Later = Timestamp.valueOf(laterSession);

    @BeforeEach
    public void setUp() {
        session = new Session(Now, "work", "Test Driven Development", "60:00", true);
    }

    @Test
    public void testGetId() {
        session.setId(302);
        assertEquals(302, session.getId());
    }

    @Test
    public void testGetTimestamp() {
        assertEquals(Now, session.getTimestamp());
    }

    @Test
    public void testGetSessionType() {
        assertEquals("work", session.getSessionType());
    }

    @Test
    public void testGetSessionTask() {
        assertEquals("Test Driven Development", session.getSessionTask());
    }

    @Test
    public void testGetTimespent() {
        assertEquals("60:00", session.getTimespent());
    }

    @Test
    public void testGetCompletion() {
        assertEquals(true, session.getCompletion());
    }

    @Test
    public void testSetTimestamp() {
        session.setTimestamp(Later);
        assertEquals(Later, session.getTimestamp());
    }

    @Test
    public void testSetSessionType() {
        session.setSessionType("rest");
        assertEquals("rest", session.getSessionType());
    }

    @Test
    public void testSetSessionTask() {
        session.setSessionTask("Having a warm meal");
        assertEquals("Having a warm meal", session.getSessionTask());
    }

    @Test
    public void testSetTimespent() {
        session.setTimespent("30:00");
        assertEquals("30:00", session.getTimespent());
    }

    @Test
    public void testSetCompletion() {
        session.setCompletion(false);
        assertEquals(false, session.getCompletion());
    }
}



