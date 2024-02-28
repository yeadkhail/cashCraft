package com.example.cashcraft;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.*;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
             Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                // Do something with id and name
                System.out.println("ID: " + id + ", Name: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}