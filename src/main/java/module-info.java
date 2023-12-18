module com.example.triviagameproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.triviagameproject to javafx.fxml;
    exports com.example.triviagameproject;
}