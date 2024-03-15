package com.example.cashcraft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

public class TransactionsController implements Initializable
{
    ResultSet rs;
    Statement statement;
    public String[] choices={"Income", "Expense", "Savings"};
    @FXML
    private ComboBox<String> CB;
    @FXML
    private TextArea TA;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        CB.getItems().addAll(choices);
        try {
            Connection connection = Makeconnection.makeconnection();
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists transactions");
            statement.executeUpdate("create table transactions (amount int, type string)");
            statement.executeUpdate("insert into transactions values(15000, 'Income')");
            statement.executeUpdate("insert into transactions values(145000, 'Income')");
            statement.executeUpdate("insert into transactions values(15000, 'Expense')");
            statement.executeUpdate("insert into transactions values(25000, 'Expense')");
            statement.executeUpdate("insert into transactions values(25000, 'Savings')");
            statement.executeUpdate("insert into transactions values(25000, 'Savings')");
            rs = statement.executeQuery("select * from transactions");

            while (rs.next()) {
                int val = rs.getInt("amount");
                String tipe = rs.getString("type");
                // Do something with id and name
                TA.appendText("Amount: "+val+" Type: "+tipe+"\n");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void on_select_CB(ActionEvent event) throws SQLException {
        //TA.clear();
        String s=CB.getValue();
        if(s.equals("Income"))
        {
            rs = statement.executeQuery("select * from transactions where type='Income'");
            TA.clear();
            while (rs.next()) {
                int val = rs.getInt("amount");
                String tipe = rs.getString("type");
                // Do something with id and name
                TA.appendText("Amount: "+val+" Type: "+tipe+"\n");
            }
        }
        else if(s.equals("Expense"))
        {
            rs = statement.executeQuery("select * from transactions where type='Expense'");
            TA.clear();
            while (rs.next()) {
                int val = rs.getInt("amount");
                String tipe = rs.getString("type");
                // Do something with id and name
                TA.appendText("Amount: "+val+" Type: "+tipe+"\n");
            }
        }
        else if(s.equals("Savings"))
        {
            rs = statement.executeQuery("select * from transactions where type='Savings'");
            TA.clear();
            while (rs.next()) {
                int val = rs.getInt("amount");
                String tipe = rs.getString("type");
                // Do something with id and name
                TA.appendText("Amount: "+val+" Type: "+tipe+"\n");
            }
        }
    }

}
