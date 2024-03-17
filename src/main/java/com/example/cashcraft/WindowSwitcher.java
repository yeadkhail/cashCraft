package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class WindowSwitcher
{
    @FXML
    private PasswordField passfield;
    private Scene scene;
    private Stage stage;
    private Parent root;
    public void log_in(ActionEvent event)throws IOException
    {
        try
        {
            if(passfield.getText().isEmpty()) {throw new ExceptionCatcher("Invalid input!");}
            else
            {
                String pass="";
                Connection connection = Makeconnection.makeconnection();
                Statement statement = connection.createStatement();
                ResultSet rs=statement.executeQuery("select password from user where id=701");
                if(rs.next()) {pass = rs.getString("password");}
                if(passfield.getText().equals(pass))
                {
                    connection.close();
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else throw new ExceptionCatcher("Wrong input!");
            }
        } catch(ExceptionCatcher ex)
        {
            passfield.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input is EMPTY!");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        } catch (SQLException e) {
            passfield.clear();
            throw new RuntimeException(e);
        }
    }
}
