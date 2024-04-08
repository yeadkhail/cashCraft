package com.example.cashcraft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

import java.util.List;

public class EditWallet {
    @FXML
    private ListView<PersonClasses.Wallet> walletListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    public void initialize(){
        List<PersonClasses.Wallet> wallets = PersonDao.getWallets();
        ObservableList<PersonClasses.Wallet> observableList = FXCollections.observableArrayList(wallets);

        walletListView.setCellFactory(param -> new ListCell<PersonClasses.Wallet>() {
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

        walletListView.setItems(observableList);
        walletListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                descriptionTextField.setText(newValue.getDescription());
            }
        });
    }

    public void handleEditButton() {
        PersonClasses.Wallet selectedWallet = walletListView.getSelectionModel().getSelectedItem();
        if (selectedWallet != null) {
            PersonClasses.Wallet newWallet = new PersonClasses.Wallet(nameTextField.getText(), descriptionTextField.getText());
            PersonDao.editWallet(selectedWallet, newWallet);
            walletListView.getItems().set(walletListView.getSelectionModel().getSelectedIndex(), newWallet);
        }
    }

    public void handleDeleteButton() {
        PersonClasses.Wallet selectedWallet = walletListView.getSelectionModel().getSelectedItem();
        if (selectedWallet != null) {
            PersonDao.deleteWallet(selectedWallet);
            walletListView.getItems().remove(selectedWallet);
        }
    }
    public void handleCancelButton() {
        nameTextField.clear();
        descriptionTextField.clear();
        walletListView.getSelectionModel().clearSelection();
    }
    public void handleCloseButton(){
        Stage stage = (Stage) walletListView.getScene().getWindow();
        stage.close();
    }
}