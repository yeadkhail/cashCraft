package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class WindowSwitcher
{
    @FXML
    private TextField passfield;
    private Scene scene;
    private Stage stage;
    private Parent root;
    @FXML
    public void log_in(ActionEvent event)throws IOException
    {
        try
        {
            if(passfield.getText().isEmpty()) {throw new ExceptionCatcher("Invalid input!");}
            else
            {
                String pass="";
                DatabaseManager db= new DatabaseManager();
                Statement statement = db.getConnection().createStatement();
                ResultSet rs=statement.executeQuery("select password from user where id=701");
                if(rs.next()) {pass = rs.getString("password");}
                if(passfield.getText().equals(pass))
                {
                    db.close();
                    root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
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
