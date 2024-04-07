package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddWallet{



    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    public void initialize() {
        nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Name text field gained focus");
            } else {
                System.out.println("Name text field lost focus");
            }
        });

        descriptionTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("Description text field gained focus");
            } else {
                System.out.println("Description text field lost focus");
            }
        });
    }

    public void handleFinishButton() {
        String name = nameTextField.getText();
        String description = descriptionTextField.getText();


        if (walletExists(name)) {
            System.out.println("Wallet with name '" + name + "' already exists.");
            return;
        }

        PersonClasses.Wallet wallet = new PersonClasses.Wallet(name, description);
        try {
            PersonDao.addWallet(wallet);
            System.out.println("Wallet added successfully");
        } catch (Exception e) {
            System.err.println("Error adding category: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean walletExists(String name) {
        return PersonDao.isPlaceExist(name);
    }

}
