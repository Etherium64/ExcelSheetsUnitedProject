module project.pomodoro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;
    opens project.pomodoro.controller to javafx.fxml;
    exports project.pomodoro.controller;
    opens project.pomodoro.model to javafx.fxml;
    exports project.pomodoro.model;
}