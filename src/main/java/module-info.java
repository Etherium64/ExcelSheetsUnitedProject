module project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.example.desktoppet302 to javafx.fxml;
    exports org.example.desktoppet302;
    opens project.pomodoro to javafx.fxml;
    exports project.pomodoro;
}