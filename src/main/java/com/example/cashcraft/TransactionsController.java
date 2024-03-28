package com.example.cashcraft;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable
{
    String selected_type;
    String[] types = {"All","Income","Expense","Transfer"};
    String query;
    ResultSet resultset;
    Statement statement;
    Connection connection;
    @FXML
    TableView<ObservableList<String>> info_box;

    @FXML
    TableColumn<ObservableList<String>, String> amount_column;
    @FXML
    TableColumn<ObservableList<String>, String> people_column;
    @FXML
    TableColumn<ObservableList<String>, String> place_column;
    @FXML
    TableColumn<ObservableList<String>, String> cat_column;
    @FXML
    TableColumn<ObservableList<String>, String> note_column;
    @FXML
    TableColumn<ObservableList<String>, String> desc_column;
    @FXML
    TableColumn<ObservableList<String>, String> date_column;
    @FXML
    TableColumn<ObservableList<String>, String> src_column;
    @FXML
    TableColumn<ObservableList<String>, String> dest_column;
    @FXML
    ComboBox<String> type_combo;
    @FXML
    ComboBox<String> sort_combo;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        type_combo.getItems().addAll(types);
        selected_type="All";
        try
        {
            connection =Makeconnection.makeconnection();
            statement=connection.createStatement();
            statement.setQueryTimeout(30);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void on_type_selected()throws SQLException
    {
        info_box.getItems().clear();
        if(type_combo.getValue()!=null)
        {selected_type=type_combo.getValue();}

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
            System.out.println("Selected all");
        }

        resultset=statement.executeQuery(query);
        if(selected_type.equals("Transfer")) {
            amount_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
            people_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
            place_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            cat_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
            note_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
            desc_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
            date_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
            src_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(7)));
            dest_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(8)));
            while (resultset.next()) {
                double val = resultset.getDouble("amount");
                String desc = resultset.getString("desc");
                String timing = resultset.getString("date");
                String to_wallet = resultset.getString("to_wallet_name");
                String from_wallet = resultset.getString("from_wallet_name");
                String people = resultset.getString("people_name");
                String place = resultset.getString("place_name");
                String note = resultset.getString("notes");
                String category = resultset.getString("category_name");

                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(val));
                row.add(people);
                row.add(place);
                row.add(category);
                row.add(note);
                row.add(desc);
                row.add(timing);
                row.add(from_wallet);
                row.add(to_wallet);
                info_box.getItems().add(row);
            }
        }
        else
        {
            amount_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
            people_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
            place_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            cat_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
            note_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
            desc_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
            date_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
            src_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(7)));
            dest_column.setCellValueFactory(null);
            while(resultset.next())
            {
                double val=resultset.getDouble("amount");
                String desc=resultset.getString("desc");
                String timing=resultset.getString("date");
                String wallet=resultset.getString("wallet_name");
                String people=resultset.getString("people_name");
                String place=resultset.getString("place_name");
                String note=resultset.getString("notes");
                String category=resultset.getString("category_name");

                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(val));
                row.add(people);
                row.add(place);
                row.add(category);
                row.add(note);
                row.add(desc);
                row.add(timing);
                row.add(wallet);
                info_box.getItems().add(row);
            }
        }
    }
}
