package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import java.sql.*;

public class passwordController {

    @FXML
    private PasswordField new_pass;

    @FXML
    private PasswordField confirm_pass;

    @FXML
    private Button confirm_pass_button;

    @FXML
    private PasswordField old_pass;

    @FXML
    public void on_confirm_clicked()
    {
        if(new_pass.getText().isEmpty() || confirm_pass.getText().isEmpty() || old_pass.getText().isEmpty())
        {
            confirm_pass.clear();
            old_pass.clear();
            new_pass.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input is EMPTY!");
            alert.setHeaderText(null);
            alert.setContentText("PLEASE ENSURE ALL INPUTS ARE FILLED!");
            alert.showAndWait();
        }
        else
        {
            try(Connection connection = Makeconnection.makeconnection()) {
                Statement statement = connection.createStatement();
                ResultSet rs=statement.executeQuery("select password from user where id=701");

                if (rs.next()) {
                    String storedPassword = rs.getString("password");  // Retrieve the password from the result set
                    if (old_pass.getText().equals(storedPassword) && confirm_pass.getText().equals(new_pass.getText())) {

                        String updateQuery = "update user set password=? where id=701";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, new_pass.getText());
                            updateStatement.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Password status");
                        alert.setHeaderText(null);
                        alert.setContentText("Password has been updated!");
                        alert.showAndWait();
                    } else {

                        confirm_pass.clear();
                        old_pass.clear();
                        new_pass.clear();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Input is EMPTY!");
                        alert.setHeaderText(null);
                        alert.setContentText("CONFIRMATION PASSWORD DOESN'T MATCH OR OLD PASSWORD IS INCORRECT");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
