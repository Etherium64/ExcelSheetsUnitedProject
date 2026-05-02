import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.Trivia.dao.DatabaseConnection;
import project.Trivia.dao.DatabaseInitialiser;
import project.Trivia.dao.ScoreDAO;
import project.pomodoro.controller.PomodoroController;
import project.pomodoro.model.Session;
import project.pomodoro.model.SessionDAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SessionDAOTest {
    private SessionDAO sessionDAO;

    private LocalDateTime a = LocalDateTime.now();
    private Timestamp A = Timestamp.valueOf(a);

    private LocalDateTime b = a.plusMinutes(25);
    private Timestamp B = Timestamp.valueOf(b);


    @BeforeEach
    public void setUp() {
        sessionDAO = new SessionDAO();
        sessionDAO.dropTable();
        sessionDAO.createTable();

        sessionDAO.insert(new Session(A, "work", "Test 1", "25:00", true ));
        sessionDAO.insert(new Session(B, "rest", "Test 2", "02:00", false));
    }

    @AfterEach
    public void closeConnection() {
        sessionDAO.close();
    }

    @Test
    public void testOne() {
        assertFalse(sessionDAO.getAll().isEmpty());

        assertEquals(1, sessionDAO.getByTimestamp(A).getId());
        assertEquals(2, sessionDAO.getByTimestamp(B).getId());

        assertNotEquals(sessionDAO.getById(1).getSessionType(), sessionDAO.getById(2).getSessionType());
        assertNotEquals(sessionDAO.getById(1).getSessionTask(), sessionDAO.getById(2).getSessionTask());
        assertNotEquals(sessionDAO.getById(1).getTimespent(), sessionDAO.getById(2).getTimespent());
        assertNotEquals(sessionDAO.getById(1).getCompletion(), sessionDAO.getById(2).getCompletion());
    }

    @Test
    public void testTwo() {
        sessionDAO.delete(sessionDAO.getById(1));
        assertEquals(1, sessionDAO.getAll().size());

        sessionDAO.delete(sessionDAO.getByTimestamp(B));
        assert(sessionDAO.getAll().isEmpty());

    }

    @Test
    public void testThree() {
        LocalDateTime c = a.plusHours(3);
        Timestamp C = Timestamp.valueOf(c);

        Session sessionOne = sessionDAO.getById(2);
        sessionOne.setTimestamp(C);
        sessionOne.setTimespent("05:00");
        sessionOne.setCompletion(true);
        sessionDAO.update(sessionOne);

        assertNotEquals(B, sessionOne.getTimestamp());
        assertEquals("05:00", sessionOne.getTimespent());
        assert(sessionOne.getCompletion());

        Session sessionTwo = sessionDAO.getByTimestamp(A);
        sessionTwo.setId(3);
        sessionTwo.setSessionType("rest");
        sessionTwo.setSessionTask("Test 3");
        sessionDAO.update(sessionTwo);

        assertNotEquals(1, sessionTwo.getId());
        assertEquals("rest", sessionTwo.getSessionType());
        assertEquals("Test 3", sessionTwo.getSessionTask());
    }
}