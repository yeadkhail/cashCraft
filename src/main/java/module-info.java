module com.example.cashcraft {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cashcraft to javafx.fxml;
    exports com.example.cashcraft;
}