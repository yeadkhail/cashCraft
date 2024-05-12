package com.example.cashcraft;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionsController implements Initializable {
    EdittransactionsController controller;

    AddTransactionController addController;
    private Scene scene;
    private Stage stage;
    private Parent root;
    String selected_type;
    String[] types = {"All", "Income", "Expense", "Transfer"};
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
    TableView.TableViewSelectionModel<ObservableList<String>> selectionModel;

    @FXML
    TabPane main_tab;
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
    @FXML
    Button edit_button;
    @FXML
    PieChart pie_chart;

    @FXML
    StackPane graph_stack;

    @FXML
    ComboBox<String> graph_combo;

    @FXML
    ScrollPane graph_scroll;

    String[] graph_box = {"Transaction types", "Wallets"};

    @FXML
    private ListView<PersonClasses.Wallet> ZakwalletListView;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descField;
    @FXML
    private TextField zakField;
    @FXML
    private DropShadow dropShadow;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            dropShadow = new DropShadow();
            dropShadow.setRadius(15);
            dropShadow.setOffsetX(5);
            dropShadow.setOffsetY(5);



        graph_scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        selectionModel = info_box.getSelectionModel();
        delete_button.disableProperty().bind(info_box.getSelectionModel().selectedItemProperty().isNull());
        edit_button.disableProperty().bind(info_box.getSelectionModel().selectedItemProperty().isNull());
        type_combo.getItems().addAll(types);
        graph_combo.getItems().addAll(graph_box);
        selected_type = "All";
        try {
            connection = Makeconnection.makeconnection();
            statement = connection.createStatement();
            delete_income_statement = connection.prepareStatement("DELETE FROM Income WHERE income_id=?");
            delete_expense_statement = connection.prepareStatement("DELETE FROM expense WHERE transaction_id=?");
            delete_transfer_statement = connection.prepareStatement("DELETE FROM transfer WHERE transfer_id=?");
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void on_type_selected() throws SQLException {

        if (connection.isClosed())//connection to be closed
        {
            connection = Makeconnection.makeconnection();
            statement = connection.createStatement();
            delete_income_statement = connection.prepareStatement("DELETE FROM Income WHERE income_id=?");
            delete_expense_statement = connection.prepareStatement("DELETE FROM expense WHERE transaction_id=?");
            delete_transfer_statement = connection.prepareStatement("DELETE FROM transfer WHERE transfer_id=?");
            statement.setQueryTimeout(30);
        }

        info_box.getItems().clear();
        if (type_combo.getValue() != null) {
            selected_type = type_combo.getValue();
        }

        //handle type first
        if (selected_type.equals("Income"))
            query = "SELECT i.income_id as ID,i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM income i " +
                    "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id";
        else if (selected_type.equals("Expense"))
            query = "SELECT i.transaction_id as ID,i.amount, i.desc, i.date, w.wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM expense i " +
                    "LEFT JOIN wallet w ON i.wallet = w.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id";
        else if (selected_type.equals("Transfer"))
            query = "SELECT i.transfer_id,i.amount, i.desc, i.date, fw.wallet_name as from_wallet_name, tw.wallet_name as to_wallet_name, c.category_name, p.people_name, pl.place_name, i.notes " +
                    "FROM transfer i " +
                    "LEFT JOIN wallet fw ON i.from_wallet = fw.wallet_id " +
                    "LEFT JOIN people p ON i.people = p.people_id " +
                    "LEFT JOIN category c ON i.category = c.category_id " +
                    "LEFT JOIN place pl ON i.place = pl.place_id " +
                    "LEFT JOIN wallet tw on i.to_wallet = tw.wallet_id";
        else if (selected_type.equals("All")) {
            System.out.println("Selected all");
        }

        resultset = statement.executeQuery(query);
        if (selected_type.equals("Transfer")) {
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
                String val = resultset.getString("amount");
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
                row.add(val);
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
        } else {
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
            while (resultset.next()) {
                String val = resultset.getString("amount");
                String desc = resultset.getString("desc");
                String timing = resultset.getString("date");
                String wallet = resultset.getString("wallet_name");
                String people = resultset.getString("people_name");
                String place = resultset.getString("place_name");
                String note = resultset.getString("notes");
                String category = resultset.getString("category_name");
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
            if (response == ButtonType.OK) {
                if (selected_type.equals("Transfer")) {
                    int lastColumnIndex = info_box.getColumns().size() - 1;
                    ObservableList<String> selectedRow = info_box.getSelectionModel().getSelectedItem();
                    String ID = selectedRow.get(lastColumnIndex);
                    //System.out.println(ID+"\n");
                    try {
                        delete_transfer_statement.setString(1, ID);
                        delete_transfer_statement.executeUpdate();
                        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                        confirm.setTitle("Success");
                        confirm.setHeaderText("Transfer transaction");
                        confirm.setContentText("Transfer transaction removed");
                        confirm.showAndWait();
                        on_type_selected();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    int lastColumnIndex = info_box.getColumns().size() - 2;
                    ObservableList<String> selectedRow = info_box.getSelectionModel().getSelectedItem();
                    String ID = selectedRow.get(lastColumnIndex);
                    //System.out.println(ID+"\n");
                    if (selected_type.equals("Income")) {
                        try {
                            delete_income_statement.setString(1, ID);
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
                    } else if (selected_type.equals("Expense")) {
                        try {
                            delete_expense_statement.setString(1, ID);
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
            } else {
                // User clicked Cancel or closed the dialog, do nothing
            }
        });
    }

    @FXML
    void on_edit_clicked(ActionEvent event) throws IOException, SQLException {
        // Get the value from the specific column directly
        amount_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(0);
        people_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(1);
        place_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(2);
        cat_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(3);
        note_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(4);
        desc_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(5);
        date_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(6);
        src_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(7);
        dest_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(8);
        trans_column = (TableColumn<ObservableList<String>, String>) info_box.getColumns().get(9);

        String amount = amount_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String people = people_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String place = place_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String cat = cat_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String note = note_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String desc = desc_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String date = date_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String src = src_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String dest = dest_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        String id = trans_column.getCellData(info_box.getSelectionModel().getSelectedIndex());
        selected_type = type_combo.getValue();
        //System.out.println(amount+people+place+cat+note+desc+date+src+dest);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-transactions.fxml"));
        root = loader.load();
        controller = loader.getController();
        if (dest == null)
            controller.others_initialize(connection, amount, people, place, cat, note, desc, date, src, id, selected_type);
        else
            controller.transfer_initialize(connection, amount, people, place, cat, note, desc, date, src, dest, id, selected_type);
        //connection.close();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        popupStage.setScene(new Scene(root));
        popupStage.setResizable(false);
        popupStage.show();
        popupStage.setOnHidden(e -> {
            try {
                on_type_selected();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    Button add_category_button;

    @FXML
    private void handleAddCategoryButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-category-dialouge.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Category");
            Optional<ButtonType> clickedbutton = dialog.showAndWait();

            dialog.setOnCloseRequest(EVENT -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            if (clickedbutton.get() == ButtonType.FINISH) {
                AddCategory controller = fxmlLoader.getController();
                controller.handleFinishButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddPersonButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-people-dialouge.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add People");
            Optional<ButtonType> clickedbutton = dialog.showAndWait();

            dialog.setOnCloseRequest(EVENT -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            if (clickedbutton.get() == ButtonType.FINISH) {
                AddPeople controller = fxmlLoader.getController();
                controller.handleFinishButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Button add_people_button;

    @FXML
    private void handleAddPlaceButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-place-dialouge.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Place");
            Optional<ButtonType> clickedbutton = dialog.showAndWait();

            dialog.setOnCloseRequest(EVENT -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            if (clickedbutton.get() == ButtonType.FINISH) {
                AddPlace controller = fxmlLoader.getController();
                controller.handleFinishButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddWalletButton(ActionEvent event) {
        try {
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-wallet-dialouge.fxml"));
            DialogPane dialogPane = fxmlLoader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add Wallet");
            Optional<ButtonType> clickedbutton = dialog.showAndWait();

            dialog.setOnCloseRequest(EVENT -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            if (clickedbutton.get() == ButtonType.FINISH) {
                AddWallet controller = fxmlLoader.getController();
                controller.handleFinishButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleEditCategoryButton(ActionEvent event) {
        try {
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-category-dialouge-box.fxml"));
            Parent editCategoryParent = fxmlLoader.load();

            Scene editCategoryScene = new Scene(editCategoryParent);
            Stage editCategoryStage = new Stage();

            editCategoryStage.setScene(editCategoryScene);

            editCategoryStage.setTitle("Edit Category");
            editCategoryStage.initModality(Modality.APPLICATION_MODAL);

            editCategoryStage.show();
            editCategoryStage.setOnHidden(e -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleEditPeopleButton(ActionEvent event) {
        try {
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-people-dialouge-box.fxml"));
            Parent editPeopleParent = fxmlLoader.load();

            Scene editPeopleScene = new Scene(editPeopleParent);
            Stage editPeopleStage = new Stage();

            editPeopleStage.setScene(editPeopleScene);

            editPeopleStage.setTitle("Edit People");
            editPeopleStage.initModality(Modality.APPLICATION_MODAL);

            editPeopleStage.show();
            editPeopleStage.setOnHidden(e -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleEditPlaceButton(ActionEvent event) {
        try {
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-place-dialouge-box.fxml"));
            Parent editPlaceParent = fxmlLoader.load();

            Scene editPlaceScene = new Scene(editPlaceParent);
            Stage editPlaceStage = new Stage();

            editPlaceStage.setScene(editPlaceScene);

            editPlaceStage.setTitle("Edit Place");
            editPlaceStage.initModality(Modality.APPLICATION_MODAL);

            editPlaceStage.show();
            editPlaceStage.setOnHidden(e -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleEditWalletButton(ActionEvent event) {
        try {
            connection.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-wallet-dialouge-box.fxml"));
            Parent editWalletParent = fxmlLoader.load();

            Scene editWalletScene = new Scene(editWalletParent);
            Stage editWalletStage = new Stage();

            editWalletStage.setScene(editWalletScene);

            editWalletStage.setTitle("Edit Wallet");
            editWalletStage.initModality(Modality.APPLICATION_MODAL);

            editWalletStage.show();
            editWalletStage.setOnHidden(e -> {
                try {
                    on_type_selected();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void zakInit() {
        List<PersonClasses.Wallet> wallets = PersonDao.getWallets();
        ObservableList<PersonClasses.Wallet> observableList = FXCollections.observableArrayList(wallets);

        ZakwalletListView.setCellFactory(param -> new ListCell<PersonClasses.Wallet>() {
            @Override
            protected void updateItem(PersonClasses.Wallet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });


        ZakwalletListView.setItems(observableList);
        ZakwalletListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String walName;
                ResultSet tot_Amount;
                walName = newValue.getName();
                nameField.setText(newValue.getName());
                descField.setText(newValue.getDescription());
                try (Connection connection = Makeconnection.makeconnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("select current_balance from wallet_balance_view where wallet_name = ?");
                    preparedStatement.setString(1, walName);
                    tot_Amount = preparedStatement.executeQuery();
                    int val = tot_Amount.getInt("current_balance");
                    zakField.setText(String.valueOf(val * .025));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @FXML
    public void on_add_transaction_clicked(ActionEvent event) throws IOException, SQLException {
        connection.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addTransactionWindow.fxml"));
        root = loader.load();
        addController = loader.getController();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        popupStage.setScene(new Scene(root));
        popupStage.setResizable(false);
        popupStage.show();
        popupStage.setOnHidden(e -> {
            try {
                on_type_selected();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    public void on_graphs_clicked() {
        try {
            double totalIncome = getTotalAmount("income");
            double totalExpense = getTotalAmount("expense");
            double totalTransfers = getTotalAmount("transfer");

            double total = totalIncome + totalExpense + totalTransfers;
            double incomePercentage = (totalIncome / total) * 100;
            double expensePercentage = (totalExpense / total) * 100;
            double transfersPercentage = (totalTransfers / total) * 100;

            PieChart pieChart = new PieChart();
            pieChart.setMaxWidth(1000);
            pieChart.setMaxHeight(500);
            pieChart.setPadding(new Insets(50,200,0,0));
            PieChart.Data incomeData = new PieChart.Data("Income: " + String.format("%.2f", incomePercentage) + "%", incomePercentage);
            PieChart.Data expenseData = new PieChart.Data("Expense: " + String.format("%.2f", expensePercentage) + "%", expensePercentage);
            PieChart.Data transfersData = new PieChart.Data("Transfer: " + String.format("%.2f", transfersPercentage) + "%", transfersPercentage);
            pieChart.getData().addAll(incomeData, expenseData, transfersData);
            if (graph_stack.getChildren().size() > 1) {
                graph_stack.getChildren().remove(1, graph_stack.getChildren().size());
            }
            graph_stack.getChildren().add(pieChart);
            incomeData.getNode().setStyle("-fx-pie-color: #2ca02c;");//green
            expenseData.getNode().setStyle("-fx-pie-color: #ff0000;");//red
            transfersData.getNode().setStyle("-fx-pie-color: #0000FF;");//blue

            graph_stack.setAlignment(pieChart, Pos.TOP_LEFT);





            pieChart.setEffect(dropShadow);
            FadeTransition fadeInTransition = new FadeTransition(Duration.millis(500));
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(500));
            AtomicReference<PieChart> subPieChartRef = new AtomicReference<>(null);
            for (PieChart.Data data : pieChart.getData()) {
                data.getNode().setOnMouseEntered(e -> {
                    data.getNode().setScaleX(1.05);
                    data.getNode().setScaleY(1.05);

                    String nodeName = data.getName();
                    String[] parts = nodeName.split(":");
                    String type = parts[0].trim();

                    try {
                        ResultSet values = getCategoryAmount(type);

                        // Create a new pie chart for displaying category breakdown
                        PieChart subPieChart = new PieChart();
                        subPieChart.setMaxWidth(600);
                        subPieChart.setMaxHeight(300);
                        subPieChart.setEffect(dropShadow);
                        fadeInTransition.setNode(subPieChart); // Set the node for fadeInTransition
                        fadeInTransition.setFromValue(0.0);
                        fadeInTransition.setToValue(1.0);
                        fadeInTransition.play();

                        //subPieChart.setPadding(new Insets(100,0,0,0));
                        while (values.next()) {
                            double totalAmount = values.getDouble("total_amount");
                            String categoryName = values.getString("category_name");
                            double val;
                            if (type.equals("Income")) val = totalIncome;
                            else if (type.equals("Expense")) val = totalExpense;
                            else val = totalTransfers;
                            // Add a new slice to the sub pie chart
                            PieChart.Data categoryData = new PieChart.Data(categoryName + ": " + String.format("%.2f", (totalAmount / val) * 100) + "%", (totalAmount / val) * 100);
                            subPieChart.getData().add(categoryData);

                        }

                        // Add the sub pie chart to the graph stack and set the reference atomically
                        graph_stack.getChildren().add(subPieChart);
                        graph_stack.setAlignment(subPieChart, Pos.TOP_RIGHT);
                        subPieChartRef.set(subPieChart);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                data.getNode().setOnMouseExited(e -> {
                    data.getNode().setScaleX(1.0);
                    data.getNode().setScaleY(1.0);

                    fadeOutTransition.setNode(subPieChartRef.get()); // Set the node for fadeOutTransition
                    fadeOutTransition.setFromValue(1.0);
                    fadeOutTransition.setToValue(0.0);
                    fadeOutTransition.play();

                    // Remove the sub pie chart from the graph_stack atomically
                    PieChart subPieChartToRemove = subPieChartRef.getAndSet(null);
                    if (subPieChartToRemove != null) {
                        graph_stack.getChildren().remove(subPieChartToRemove);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getTotalAmount(String tableName) {
        double total = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(amount) FROM " + tableName);
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public ResultSet getCategoryAmount(String tableName) {
        double total = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT SUM(income.amount) AS total_amount, category.category_name " +
                    "FROM " + tableName + " AS income " +
                    "JOIN category ON income.category = category.category_id " +
                    "GROUP BY income.category";
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

/* SELECT SUM(i.amount) AS total_amount, c.category_name as name FROM income i JOIN category c ON i.category = c.category_id GROUP BY i.category ;*/