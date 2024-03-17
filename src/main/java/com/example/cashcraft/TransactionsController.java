package com.example.cashcraft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable
{
    String Type;
    String Source;
    int Amount;
    String Amount_choice;
    String Time;
    ResultSet rs;
    String query;
    Statement statement;
    PreparedStatement PS;
    Connection connection;
    private String[] choices={"Arbitrary","Income", "Expense", "Savings"};
    private String[] time_choices={"Arbitrary","Recent","Oldest"};
    private String[] amount_choices={"Arbitrary","Decreasing","Increasing"};
    @FXML
    private ComboBox<String> CB;
    @FXML
    private ComboBox<String> TB;
    @FXML
    private ComboBox<String> AB;
    @FXML
    private TextArea TA;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        CB.getItems().addAll(choices);
        TB.getItems().addAll(time_choices);
        AB.getItems().addAll(amount_choices);
        Type="Arbitrary";
        Time="Arbitrary";
        Amount_choice="Arbitrary";
        try {
            connection = Makeconnection.makeconnection();
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            Date date= Date.valueOf("2024-03-17");
            statement.executeUpdate("drop table if exists transactions");
            statement.executeUpdate("create table transactions (amount int, type string,source string,timing date)");
            date= Date.valueOf("2024-04-12");
            statement.executeUpdate("insert into transactions values(15000, 'Income', 'Tuition',' "+ date +" ' )");
            date= Date.valueOf("2023-11-12");
            statement.executeUpdate("insert into transactions values(15000, 'Income', 'Rent',' "+ date +" ' )");
            date= Date.valueOf("2023-12-25");
            statement.executeUpdate("insert into transactions values(150, 'Expense', 'Food CDS',' "+ date +" ' )");
            date= Date.valueOf("2023-12-31");
            statement.executeUpdate("insert into transactions values(200, 'Expense', 'Copy buying',' "+ date +" ' )");
            date= Date.valueOf("2023-09-18");
            statement.executeUpdate("insert into transactions values(1500, 'Savings', 'Salami',' "+ date +" ' )");
            date= Date.valueOf("2022-04-18");
            statement.executeUpdate("insert into transactions values(500, 'Savings', 'Mother gift',' "+ date +" ' )");
            rs = statement.executeQuery("select * from transactions");

            while (rs.next()) {
                Amount = rs.getInt("amount");
                Type = rs.getString("type");
                Source = rs.getString("source");
                Time = rs.getString("timing");
                // Do something with id and name
                TA.appendText("Amount: "+Amount+"   Type: "+Type+"   Cause: "+Source+"   Date: "+Time+"\n");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void on_select_CB(ActionEvent event) throws SQLException {
        Type=CB.getValue();
        TA.clear();
        if(Type.equals("Arbitrary"))
        {
            query="select * from transactions";
            rs = statement.executeQuery(query);
            while (rs.next()) {
                Amount = rs.getInt("amount");
                Type = rs.getString("type");
                Source = rs.getString("source");
                Time = rs.getString("timing");
                // Do something with id and name
                TA.appendText("Amount: "+Amount+"   Type: "+Type+"   Cause: "+Source+"   Date: "+Time+"\n");
            }
            return;
        }
        query="select * from transactions where type=?";
        PS = connection.prepareStatement(query);
        PS.setString(1,Type);
        rs = PS.executeQuery();
        while (rs.next()) {
            Amount = rs.getInt("amount");
            Type = rs.getString("type");
            Source = rs.getString("source");
            Time = rs.getString("timing");
            // Do something with id and name
            TA.appendText("Amount: "+Amount+"   Type: "+Type+"   Cause: "+Source+"   Date: "+Time+"\n");
        }
    }
    @FXML
    void on_select_TB(ActionEvent event) throws SQLException {
        //TA.clear();
        Time=TB.getValue();
        query="select * from transactions where type=?";
        PS = connection.prepareStatement(query);
        PS.setString(1,Type);
        rs = PS.executeQuery(query);
        TA.clear();
        while (rs.next()) {
            Amount = rs.getInt("amount");
            Type = rs.getString("type");
            Source = rs.getString("source");
            Time = rs.getString("timing");
            // Do something with id and name
            TA.appendText("Amount: "+Amount+"   Type: "+Type+"   Cause: "+Source+"   Date: "+Time+"\n");
        }
    }
}
