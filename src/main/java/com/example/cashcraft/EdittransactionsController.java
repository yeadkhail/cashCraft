package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    PreparedStatement peoplestatement;
    PreparedStatement placestatement;
    PreparedStatement categorystatement;
    PreparedStatement mainwalletstatement;
    PreparedStatement endwalletstatement;
    PreparedStatement preparedstatement;
    Statement statement;
    String query;
    String trans_id;
    String selected_type;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        amount_box.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9.]")) {
                event.consume();
            }
        });
    }
    @FXML
    public void others_initialize(Connection conn,String amount, String people, String place,String cat, String note, String desc,String date, String src, String id,String type) throws SQLException//selected either income or expense
    {
        connection=conn;
        statement=connection.createStatement();
        statement.setQueryTimeout(30);
        peoplestatement= connection.prepareStatement("SELECT people_id from people where people_name=?");
        placestatement= connection.prepareStatement("SELECT place_id from place where place_name=?");
        categorystatement= connection.prepareStatement("SELECT category_id from category where category_name=?");
        mainwalletstatement= connection.prepareStatement("SELECT wallet_id from wallet where wallet_name=?");
        endwalletstatement= connection.prepareStatement("SELECT wallet_id from wallet where wallet_name=?");

        trans_id=id;
        selected_type=type;
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
        place_box.getItems().add("");
        items.clear();

        query="SELECT category_name from category";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("category_name");
            items.add(item);
        }
        category_box.getItems().addAll(items);
        category_box.getItems().add("");
        items.clear();

        query="SELECT people_name from people";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("people_name");
            items.add(item);
        }
        people_box.getItems().addAll(items);
        people_box.getItems().add("");
        items.clear();

        amount_box.setText(amount);
        note_box.setText(note);
        description_box.setText(desc);
        people_box.setValue(people);
        category_box.setValue(cat);
        place_box.setValue(place);
        mainwallet_box.setValue(src);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        date_box.setValue(parsedDate);
    }
    @FXML
    public void transfer_initialize(Connection conn,String amount, String people, String place,String cat, String note, String desc,String date, String src, String dest, String id,String type) throws SQLException//selected either income or expense
    {
        connection=conn;
        statement=connection.createStatement();
        statement.setQueryTimeout(30);
        peoplestatement= connection.prepareStatement("SELECT people_id from people where people_name=?");
        placestatement= connection.prepareStatement("SELECT place_id from place where place_name=?");
        categorystatement= connection.prepareStatement("SELECT category_id from category where category_name=?");
        mainwalletstatement= connection.prepareStatement("SELECT wallet_id from wallet where wallet_name=?");
        //endwalletstatement= connection.prepareStatement("SELECT wallet_id from wallet where wallet_name=?");

        trans_id=id;
        selected_type=type;
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
        place_box.getItems().add("");
        items.clear();

        query="SELECT category_name from category";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("category_name");
            items.add(item);
        }
        category_box.getItems().addAll(items);
        category_box.getItems().add("");
        items.clear();

        query="SELECT people_name from people";
        resultset=statement.executeQuery(query);
        while (resultset.next()) {
            String item = resultset.getString("people_name");
            items.add(item);
        }
        people_box.getItems().addAll(items);
        people_box.getItems().add("");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        date_box.setValue(parsedDate);
    }
    @FXML
    void on_confirm_edits_clicked()
    {
        try
        {
            String amount=amount_box.getText();
            String note=note_box.getText();
            String desc=description_box.getText();
            LocalDate selectedDate = date_box.getValue();
            System.out.println(selectedDate);
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String people=people_box.getValue();
            String place=place_box.getValue();
            String category=category_box.getValue();
            String mainwallet=mainwallet_box.getValue();
            String endwallet;
            peoplestatement.setString(1,people);
            placestatement.setString(1,place);
            categorystatement.setString(1,category);
            mainwalletstatement.setString(1,mainwallet);
            ResultSet temp_result=peoplestatement.executeQuery();
            if(temp_result.next())people=temp_result.getString("people_id");
            temp_result=placestatement.executeQuery();
            if(temp_result.next())place=temp_result.getString("place_id");
            temp_result=categorystatement.executeQuery();
            if(temp_result.next())category=temp_result.getString("category_id");
            temp_result=mainwalletstatement.executeQuery();
            if(temp_result.next())mainwallet=temp_result.getString("wallet_id");
            if(!endwallet_box.isDisabled())//transfer update wanted
            {
                    endwallet = endwallet_box.getValue();
                    mainwalletstatement.setString(1, endwallet);
                if(endwallet_box.getValue().equals(mainwallet_box.getValue()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText(null);
                    alert.setContentText("Money cannot be transferred from a wallet to itself!");
                    alert.showAndWait();
                }
                else {
                    temp_result = mainwalletstatement.executeQuery();
                    if (temp_result.next()) endwallet = temp_result.getString("wallet_id");
                    System.out.println("passed " + endwallet +" "+ mainwallet);

                    PersonClasses.Income_and_expense_String obj = new PersonClasses.Income_and_expense_String(amount, people, place, category, note, desc, mainwallet, endwallet, formattedDate);
                    PersonDao.editTransaction(obj, connection, trans_id, selected_type);
                }
            }
            else
            {
                PersonClasses.Income_and_expense_String obj= new PersonClasses.Income_and_expense_String(amount,people,place,category,note,desc,mainwallet,formattedDate);
                PersonDao.editTransaction(obj, connection, trans_id,selected_type);
            }
            //System.out.println(trans_id);
            //            System.out.println(people+place+category+mainwallet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
