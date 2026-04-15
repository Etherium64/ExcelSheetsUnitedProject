module project.pomodoro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;
}