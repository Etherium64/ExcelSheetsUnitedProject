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

    opens project.Trivia.view to javafx.fxml;
    exports project.Trivia;
    exports project.Trivia.dao;

    requires javafx.base;

    opens AnimationStates;
    exports AnimationStates;
}