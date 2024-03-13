module com.example.cashcraft {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.cashcraft to javafx.fxml;
    exports com.example.cashcraft;
}