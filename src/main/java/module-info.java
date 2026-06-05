module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens project.authentication to javafx.fxml;
    exports project.authentication;

    opens project.desktoppet302 to javafx.fxml;
    exports project.desktoppet302;

    opens project.model to javafx.fxml;
    exports project.model;

    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;

    opens project.Trivia to javafx.fxml;
    exports project.Trivia;

    requires javafx.base;
    requires java.net.http;

    opens AnimationStates;
    exports AnimationStates;



}