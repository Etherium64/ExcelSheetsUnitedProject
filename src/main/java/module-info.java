module com.esuproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.esuproject to javafx.fxml;
    exports com.esuproject;
}