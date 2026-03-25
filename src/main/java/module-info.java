module project.esu {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.esu to javafx.fxml;
    exports project.esu;
}