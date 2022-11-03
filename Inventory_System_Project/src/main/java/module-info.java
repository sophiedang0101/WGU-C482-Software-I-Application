module com.example.c482project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.c482project to javafx.fxml;
    exports com.example.c482project;
}