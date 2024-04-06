package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EdittransactionsController implements Initializable {

    @FXML
    private TextField amount_box;

    @FXML
    private TextArea note_box;

    @FXML
    private TextArea description_box;

    @FXML
    private Button cancel_edit;

    @FXML
    private Button confirm_edit;

    @FXML
    private DatePicker date_box;

    @FXML
    private ComboBox<String> people_box;

    @FXML
    private ComboBox<String> endwallet_box;

    @FXML
    private ComboBox<String> category_box;

    @FXML
    private ComboBox<String> mainwallet_box;

    @FXML
    private ComboBox<String> place_box;
    Connection connection;
    ResultSet resultset;
    Statement statement;
    String query;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try {
            connection=Makeconnection.makeconnection();
            statement=connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void others_initialize(String amount, String people, String place,String cat, String note, String desc,String date, String src) throws SQLException//selected either income or expense
    {
        List<String> items = new ArrayList<>();
        endwallet_box.setDisable(true);
        query="SELECT wallet_name from wallet";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("wallet_name");
            items.add(item);
        }
        mainwallet_box.getItems().addAll(items);
        items.clear();

        query="SELECT place_name from place";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("place_name");
            items.add(item);
        }
        place_box.getItems().addAll(items);
        items.clear();

        query="SELECT category_name from category";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("category_name");
            items.add(item);
        }
        category_box.getItems().addAll(items);
        items.clear();

        query="SELECT people_name from people";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("people_name");
            items.add(item);
        }
        people_box.getItems().addAll(items);
        items.clear();

        amount_box.setText(amount);
        note_box.setText(note);
        description_box.setText(desc);
        people_box.setValue(people);
        category_box.setValue(cat);
        place_box.setValue(place);
        mainwallet_box.setValue(src);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Assuming 'date' parameter is in this format
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        date_box.setValue(parsedDate);
    }
    @FXML
    public void transfer_initialize(String amount, String people, String place,String cat, String note, String desc,String date, String src, String dest) throws SQLException//selected either income or expense
    {
        List<String> items = new ArrayList<>();
        query="SELECT wallet_name from wallet";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("wallet_name");
            items.add(item);
        }
        mainwallet_box.getItems().addAll(items);
        items.clear();

        query="SELECT place_name from place";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("place_name");
            items.add(item);
        }
        place_box.getItems().addAll(items);
        items.clear();

        query="SELECT category_name from category";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("category_name");
            items.add(item);
        }
        category_box.getItems().addAll(items);
        items.clear();

        query="SELECT people_name from people";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("people_name");
            items.add(item);
        }
        people_box.getItems().addAll(items);
        items.clear();

        query="SELECT wallet_name from wallet";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("wallet_name");
            items.add(item);
        }
        endwallet_box.getItems().addAll(items);
        items.clear();

        amount_box.setText(amount);
        note_box.setText(note);
        description_box.setText(desc);
        people_box.setValue(people);
        category_box.setValue(cat);
        place_box.setValue(place);
        mainwallet_box.setValue(src);
        endwallet_box.setValue(dest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Assuming 'date' parameter is in this format
        LocalDate parsedDate = LocalDate.parse(date, formatter);
    }
}
