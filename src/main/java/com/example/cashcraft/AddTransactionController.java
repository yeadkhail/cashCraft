package com.example.cashcraft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.util.StringConverter;


public class AddTransactionController {

    @FXML
    private TextField amount_box_add;

    @FXML
    private TextArea note_box_add;

    @FXML
    private TextArea description_box_add;

    @FXML
    private Button cancel_edit;

    @FXML
    private Button confirm_transaction;

    @FXML
    private DatePicker date_box_add;

    @FXML
    private ComboBox<PersonClasses.People> people_box_add;

    @FXML
    private ComboBox<PersonClasses.Wallet> endwallet_box_add;

    @FXML
    private ComboBox<PersonClasses.Category> category_box_add;

    @FXML
    private ComboBox<PersonClasses.Wallet> mainwallet_box_add;

    @FXML
    private ComboBox<PersonClasses.Place> place_box_add;

    @FXML
    private ComboBox<String> type_transaction_add;

    String[] type_list = {"Income", "Expense", "Transfer"};
    private PersonClasses.Wallet selectedMainWallet;
    private PersonClasses.Wallet selectedEndWallet;
    private PersonClasses.Category selectedCategory;
    private PersonClasses.People selectedPeople;
    private PersonClasses.Place selectedPlace;
    private String selectedType;

    public void initialize() {
        amount_box_add.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9.]")) {
                event.consume();
            }
        });
        type_transaction_add.getItems().addAll(type_list);
        endwallet_box_add.setDisable(true);
        type_transaction_add.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // Enable comboBox2
            endwallet_box_add.setDisable(!(newVal != null && newVal.equals("Transfer")));
        });

        List<PersonClasses.Wallet> wallets = PersonDao.getWallets();
        ObservableList<PersonClasses.Wallet> walletList = FXCollections.observableArrayList(wallets);
        List<PersonClasses.Category> categories = PersonDao.getCategories();
        ObservableList<PersonClasses.Category> categoryList = FXCollections.observableArrayList(categories);
        List<PersonClasses.Place> places = PersonDao.getPlaces();
        ObservableList<PersonClasses.Place> placeList = FXCollections.observableArrayList(places);
        List<PersonClasses.People> people = PersonDao.getPeople();
        ObservableList<PersonClasses.People> peopleList = FXCollections.observableArrayList(people);

        StringConverter<PersonClasses.Wallet> walletStringConverter = new StringConverter<PersonClasses.Wallet>() {
            @Override
            public String toString(PersonClasses.Wallet wallet) {
                return wallet == null ? "" : wallet.getName();
            }

            @Override
            public PersonClasses.Wallet fromString(String string) {
                // Implement if needed, but usually not necessary for ComboBox
                return null;
            }
        };

        mainwallet_box_add.setCellFactory(param -> new ListCell<>() {
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

        endwallet_box_add.setCellFactory(param -> new ListCell<>() {
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

        category_box_add.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PersonClasses.Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        people_box_add.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PersonClasses.People item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        place_box_add.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PersonClasses.Place item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        mainwallet_box_add.setItems(walletList);
        mainwallet_box_add.setConverter(walletStringConverter);

        endwallet_box_add.setItems(walletList);
        endwallet_box_add.setConverter(walletStringConverter);

        category_box_add.setItems(categoryList);
        category_box_add.setConverter(new StringConverter<PersonClasses.Category>() {
            @Override
            public String toString(PersonClasses.Category category) {
                return category == null ? "" : category.getName();
            }

            @Override
            public PersonClasses.Category fromString(String string) {
                return null;
            }
        });

        people_box_add.setItems(peopleList);
        people_box_add.setConverter(new StringConverter<PersonClasses.People>() {
            @Override
            public String toString(PersonClasses.People person) {
                return person == null ? "" : person.getName();
            }

            @Override
            public PersonClasses.People fromString(String string) {
                return null;
            }
        });

        place_box_add.setItems(placeList);
        place_box_add.setConverter(new StringConverter<PersonClasses.Place>() {
            @Override
            public String toString(PersonClasses.Place place) {
                return place == null ? "" : place.getName();
            }

            @Override
            public PersonClasses.Place fromString(String string) {
                return null;
            }
        });



        mainwallet_box_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedMainWallet = newValue;
            }
        });

        // End wallet ComboBox
        endwallet_box_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedEndWallet = newValue;
            }
        });

        // Category ComboBox
        category_box_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCategory = newValue;
            }
        });

        // People ComboBox
        people_box_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPeople = newValue;
            }
        });

        // Place ComboBox
        place_box_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedPlace = newValue;
                System.out.println("Printing" +selectedPlace.getUuid());
            }
        });

        // Type ComboBox
        type_transaction_add.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedType = newValue;
            }
        });

    }

    @FXML
    public void on_confirm_add_transaction_clicked()
    {
        if(type_transaction_add.getValue() ==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a type of transaction");
            alert.showAndWait();
        }
        else if(endwallet_box_add.isDisabled())
        {
            if(amount_box_add.getText().isEmpty() || date_box_add.getValue()==null || category_box_add.getValue()==null || mainwallet_box_add.getValue()==null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("A possible MANDATORY tab has been left empty!");
                alert.showAndWait();
            }
            else
            {
                String amount = amount_box_add.getText();
                String category = selectedCategory.getUuid();
                String people;
                String place;
                if(selectedPeople==null || selectedPeople.getUuid().isEmpty())people="";
                else people = selectedPeople.getUuid();
                if(selectedPlace==null || selectedPlace.getUuid().isEmpty())place="";
                else place = selectedPlace.getUuid();
                String main_wallet = selectedMainWallet.getUuid();
                LocalDate selectedDate = date_box_add.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateString = selectedDate.format(formatter);
                String note = note_box_add.getText();
                String desc = description_box_add.getText();
                PersonClasses.Income_and_expense_String obj = new PersonClasses.Income_and_expense_String(amount,people,place,category,note,desc,main_wallet,dateString);
                //System.out.println("Printing "+category+" "+place+" "+people);
                PersonDao.addTransaction(obj, false, type_transaction_add.getValue());

                Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                confirm.setTitle("Success");
                confirm.setContentText("Your transaction has been added!");
                confirm.showAndWait();
            }
        }
        else if(amount_box_add.getText().isEmpty() || date_box_add.getValue()==null || category_box_add.getValue()==null || mainwallet_box_add.getValue()==null || endwallet_box_add.getValue()==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("A possible MANDATORY tab has been left EMPTY!");
            alert.showAndWait();
        }
        else // ALL OK
        {
            if(type_transaction_add.getValue().equals("Transfer")) {
                if (selectedMainWallet.getName() == selectedEndWallet.getName()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText(null);
                    alert.setContentText("Money cannot be transferred from a wallet to itself!");
                    alert.showAndWait();
                } else {
                    String amount = amount_box_add.getText();
                    String category = selectedCategory.getUuid();
                    String people;
                    String place;
                    if (selectedPeople == null || selectedPeople.getUuid().isEmpty()) people = "";
                    else people = selectedPeople.getUuid();
                    if (selectedPlace == null || selectedPlace.getUuid().isEmpty()) place = "";
                    else place = selectedPlace.getUuid();
                    String main_wallet = selectedMainWallet.getUuid();
                    String end_wallet = selectedEndWallet.getUuid();
                    LocalDate selectedDate = date_box_add.getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String dateString = selectedDate.format(formatter);
                    String note = note_box_add.getText();
                    String desc = description_box_add.getText();
                    PersonClasses.Income_and_expense_String obj = new PersonClasses.Income_and_expense_String(amount, people, place, category, note, desc, main_wallet, end_wallet, dateString);
                    //System.out.println(desc+note);
                    PersonDao.addTransaction(obj, true, type_transaction_add.getValue());

                    Alert confirm = new Alert(Alert.AlertType.INFORMATION);
                    confirm.setTitle("Success");
                    confirm.setContentText("Your transaction has been added!");
                    confirm.showAndWait();
                }
            }
        }
    }
}


