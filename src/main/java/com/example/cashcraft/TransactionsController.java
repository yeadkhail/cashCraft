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
    String selected_type;
    String selected_sort;
    String[] types = {"All","Income","Expense","Transfer"};
    String[] sorting = {"Random","Newest","Oldest","Highest","Lowest"};
    String query;
    ResultSet resultset;
    PreparedStatement preparedstatement;
    Statement statement;
    Connection connection;
    @FXML
    TextArea info_box;
    @FXML
    ComboBox<String> type_combo;
    @FXML
    ComboBox<String> sort_combo;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        type_combo.getItems().addAll(types);
        sort_combo.getItems().addAll(sorting);
        selected_type="All";
        selected_sort="Random";
        try
        {
            connection =Makeconnection.makeconnection();
            statement=connection.createStatement();
            statement.setQueryTimeout(30);
            query = "SELECT i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM income i " +
                    "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id";

            resultset = statement.executeQuery(query);
            while(resultset.next())
            {
                int val=resultset.getInt("amount");
                String desc=resultset.getString("desc");
                String timing=resultset.getString("date");
                String wallet=resultset.getString("wallet_name");
                String people=resultset.getString("people_name");
                String place=resultset.getString("place_name");
                String note=resultset.getString("notes");
                String category=resultset.getString("category_name");
                info_box.appendText(("Amount: "+val+"     Description: "+desc+"     Date: "+timing+"     Wallet source: "+wallet+"     Category: "+category+"     People: "+people+"     Place: "+place+"     Note: "+note+"\n\n"));
            }

            query = "SELECT i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM expense i " +
                    "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id";

            resultset = statement.executeQuery(query);
            while(resultset.next())
            {
                int val=resultset.getInt("amount");
                String desc=resultset.getString("desc");
                String timing=resultset.getString("date");
                String wallet=resultset.getString("wallet_name");
                String people=resultset.getString("people_name");
                String place=resultset.getString("place_name");
                String note=resultset.getString("notes");
                String category=resultset.getString("category_name");
                info_box.appendText(("Amount: "+val+"     Description: "+desc+"     Date: "+timing+"     Wallet source: "+wallet+"     Category: "+category+"     People: "+people+"     Place: "+place+"     Note: "+note+"\n\n"));
            }

            query = "SELECT i.amount, i.desc, i.date, fw.wallet_name as from_wallet_name, tw.wallet_name as to_wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM transfer i " +
                    "LEFT JOIN wallet fw ON i.from_wallet = fw.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id " +
                    "LEFT JOIN wallet tw on i.to_wallet = tw.wallet_id";
            resultset = statement.executeQuery(query);
            while (resultset.next()) {
                int val = resultset.getInt("amount");
                String desc = resultset.getString("desc");
                String timing = resultset.getString("date");
                String to_wallet = resultset.getString("to_wallet_name");
                String from_wallet = resultset.getString("from_wallet_name");
                String people = resultset.getString("people_name");
                String place = resultset.getString("place_name");
                String note = resultset.getString("notes");
                String category = resultset.getString("category_name");
                info_box.appendText("Amount: " + val + "     Description: " + desc + "     Date: " + timing + "     From: " + from_wallet + "     To: " + to_wallet + "     Category: " + category + "     People: " + people + "     Place: " + place + "     Note: " + note + "\n\n");
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void on_type_selected()throws SQLException
    {
        info_box.clear();
        if(type_combo.getValue()!=null)
        {selected_type=type_combo.getValue();}
        if(sort_combo.getValue()!=null)
        {selected_sort=sort_combo.getValue();}

        //handle type first
        if(selected_type.equals("Income"))query = "SELECT i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                "FROM income i " +
                "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                "LEFT JOIN people p ON i.people = p.people_id " +
                "LEFT JOIN category c ON i.category = c.category_id " +
                "LEFT JOIN place pl ON i.place = pl.place_id";
        else if(selected_type.equals("Expense"))query = "SELECT i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                "FROM expense i " +
                "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                "LEFT JOIN people p ON i.people = p.people_id " +
                "LEFT JOIN category c ON i.category = c.category_id " +
                "LEFT JOIN place pl ON i.place = pl.place_id";
        else if(selected_type.equals("Transfer"))query = "SELECT i.amount, i.desc, i.date, fw.wallet_name as from_wallet_name, tw.wallet_name as to_wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                "FROM transfer i " +
                "LEFT JOIN wallet fw ON i.from_wallet = fw.wallet_id " +
                "LEFT JOIN people p ON i.people = p.people_id " +
                "LEFT JOIN category c ON i.category = c.category_id " +
                "LEFT JOIN place pl ON i.place = pl.place_id " +
                "LEFT JOIN wallet tw on i.to_wallet = tw.wallet_id";
        else if(selected_type.equals("All"))
        {

        }

        //handle time second
        if(selected_sort.equals("Newest"))query += " ORDER BY i.date DESC";
        else if(selected_sort.equals("Oldest"))query += " ORDER BY i.date ASC";
        else if(selected_sort.equals("Highest"))query += " ORDER BY i.amount DESC";
        else if(selected_sort.equals("Lowest"))query += " ORDER BY i.amount ASC";
        else if(selected_sort.equals("Random"))query+=" ORDER BY RANDOM()";

        resultset=statement.executeQuery(query);
        if(selected_type.equals("Transfer")) {

            while (resultset.next()) {
                int val = resultset.getInt("amount");
                String desc = resultset.getString("desc");
                String timing = resultset.getString("date");
                String to_wallet = resultset.getString("to_wallet_name");
                String from_wallet = resultset.getString("from_wallet_name");
                String people = resultset.getString("people_name");
                String place = resultset.getString("place_name");
                String note = resultset.getString("notes");
                String category = resultset.getString("category_name");
                info_box.appendText("Amount: " + val + "     Description: " + desc + "     Date: " + timing + "     From: " + from_wallet + "     To: " + to_wallet + "     Category: " + category + "     People: " + people + "     Place: " + place + "     Note: " + note + "\n\n");
            }
        }
        else
        {
            while(resultset.next())
            {
                int val=resultset.getInt("amount");
                String desc=resultset.getString("desc");
                String timing=resultset.getString("date");
                String wallet=resultset.getString("wallet_name");
                String people=resultset.getString("people_name");
                String place=resultset.getString("place_name");
                String note=resultset.getString("notes");
                String category=resultset.getString("category_name");
                info_box.appendText(("Amount: "+val+"     Description: "+desc+"     Date: "+timing+"     Wallet source: "+wallet+"     Category: "+category+"     People: "+people+"     Place: "+place+"     Note: "+note+"\n\n"));
            }
        }
    }
}
