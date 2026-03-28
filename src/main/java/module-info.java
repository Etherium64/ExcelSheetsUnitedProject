module project.petdog {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.petdog to javafx.fxml;
    exports project.petdog;
}