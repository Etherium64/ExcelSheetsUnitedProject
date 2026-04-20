module project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens project.desktoppet302 to javafx.fxml;
    exports project.desktoppet302;

    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;
}