module project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens project.desktoppet302 to javafx.fxml;
    exports project.desktoppet302;

    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;
    opens project.pomodoro.controller to javafx.fxml;
    exports project.pomodoro.controller;
    opens project.pomodoro.model to javafx.fxml;
    exports project.pomodoro.model;

    opens project.Trivia.view to javafx.fxml;
    exports project.Trivia;
    exports project.Trivia.dao;
    exports project.Trivia.view;

    requires javafx.base;
    requires java.net.http;

    opens AnimationStates;
    exports AnimationStates;


}