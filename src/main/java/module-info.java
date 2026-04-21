module org.example.desktoppet302 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.example.desktoppet302 to javafx.fxml;
    exports org.example.desktoppet302;
}