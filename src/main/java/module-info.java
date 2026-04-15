module project.Trivia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens project.Trivia.view to javafx.fxml;
    exports project.Trivia;
    exports project.Trivia.dao;
}