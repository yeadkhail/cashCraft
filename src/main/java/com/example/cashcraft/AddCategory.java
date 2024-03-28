package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class AddCategory {

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ButtonType finishButton;

    @FXML
    private ButtonType cancelButton;

    @FXML
    private void addCategory() {
        String name = nameField.getText();
        String description = descriptionField.getText();

        // Create a new Category object
        PersonClasses.Category category = new PersonClasses.Category();
        category.setName(name);
        category.setDescription(description);

        // Use the PersonDao to add the category to the database
        PersonDao personDao = new PersonDao();
        personDao.addCategory(category);
    }

    @FXML
    private void finishButtonAction() {
        // Code to execute when the finish button is clicked
    }

    @FXML
    private void cancelButtonAction() {
        // Code to execute when the cancel button is clicked
    }
}