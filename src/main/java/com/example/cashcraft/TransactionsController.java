package com.example.cashcraft;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable
{
    String selected_type;
    String[] types = {"All","Income","Expense","Transfer"};
    String query;
    ResultSet resultset;
    Statement statement;
    PreparedStatement delete_income_statement;
    PreparedStatement delete_expense_statement;
    PreparedStatement delete_transfer_statement;
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
    TableColumn<ObservableList<String>, String> trans_column;
    @FXML
    ComboBox<String> type_combo;
    @FXML
    ComboBox<String> sort_combo;
    @FXML
    Button delete_button;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        delete_button.disableProperty().bind(info_box.getSelectionModel().selectedItemProperty().isNull());
        type_combo.getItems().addAll(types);
        selected_type="All";
        try
        {
            connection =Makeconnection.makeconnection();
            statement=connection.createStatement();
            delete_income_statement=connection.prepareStatement("DELETE FROM Income WHERE income_id=?");
            delete_expense_statement=connection.prepareStatement("DELETE FROM expense WHERE transaction_id=?");
            delete_transfer_statement=connection.prepareStatement("DELETE FROM transfer WHERE transfer_id=?");
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
        if(selected_type.equals("Income"))query = "SELECT i.income_id as ID,i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                "FROM income i " +
                "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                "LEFT JOIN people p ON i.people = p.people_id " +
                "LEFT JOIN category c ON i.category = c.category_id " +
                "LEFT JOIN place pl ON i.place = pl.place_id";
        else if(selected_type.equals("Expense"))query = "SELECT i.transaction_id as ID,i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                "FROM expense i " +
                "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                "LEFT JOIN people p ON i.people = p.people_id " +
                "LEFT JOIN category c ON i.category = c.category_id " +
                "LEFT JOIN place pl ON i.place = pl.place_id";
        else if(selected_type.equals("Transfer"))query = "SELECT i.transfer_id,i.amount, i.desc, i.date, fw.wallet_name as from_wallet_name, tw.wallet_name as to_wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
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
            trans_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(9)));
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
                String id = resultset.getString("transfer_id");

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
                row.add(id);
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
            trans_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(8)));
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
                String id = resultset.getString("ID");

                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(val));
                row.add(people);
                row.add(place);
                row.add(category);
                row.add(note);
                row.add(desc);
                row.add(timing);
                row.add(wallet);
                row.add(id);
                info_box.getItems().add(row);
            }
        }
    }
    @FXML
    void on_deleteButton_clicked() {
        // Check if a row is selected in the TableView
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete confirmation");
            alert.setHeaderText("Deleting transaction");
            alert.setContentText("Are you sure you want to delete this item?");

            alert.initModality(Modality.APPLICATION_MODAL);//blocking other window events

            // Show the confirmation dialog and wait for the user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK)
                {
                    if(selected_type.equals("Transfer"))
                    {
                        int lastColumnIndex = info_box.getColumns().size() - 1;
                        ObservableList<String> selectedRow = info_box.getSelectionModel().getSelectedItem();
                        String ID = selectedRow.get(lastColumnIndex);
                        //System.out.println(ID+"\n");
                        try {
                            delete_transfer_statement.setString(1,ID);
                            delete_transfer_statement.executeUpdate();
                            Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText("Transfer transaction");
                            alert.setContentText("Transfer transaction removed");
                            alert.showAndWait();
                            on_type_selected();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else
                    {
                        int lastColumnIndex = info_box.getColumns().size() - 2;
                        ObservableList<String> selectedRow = info_box.getSelectionModel().getSelectedItem();
                        String ID = selectedRow.get(lastColumnIndex);
                        //System.out.println(ID+"\n");
                        if(selected_type.equals("Income"))
                        {
                            try {
                                delete_income_statement.setString(1,ID);
                                delete_income_statement.executeUpdate();
                                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText("Income transaction");
                                alert.setContentText("Transaction deleted successfully");
                                alert.showAndWait();
                                on_type_selected();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if(selected_type.equals("Expense"))
                        {
                            try {
                                delete_expense_statement.setString(1,ID);
                                delete_expense_statement.executeUpdate();
                                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText("Expense transaction");
                                alert.setContentText("Transaction deleted successfully");
                                alert.showAndWait();
                                on_type_selected();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                }
                else
                {
                    // User clicked Cancel or closed the dialog, do nothing
                }
            });
    }
}
