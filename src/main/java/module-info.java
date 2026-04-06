module project.Trivia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens project.Trivia.view to javafx.fxml;
    exports project.Trivia;
}