package com.example.cashcraft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

import java.util.List;

public class EditCategory {
    @FXML
    private ListView<PersonClasses.Category> categoryListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    public void initialize(){
        List<PersonClasses.Category> categories = PersonDao.getCategories();
        ObservableList<PersonClasses.Category> observableList = FXCollections.observableArrayList(categories);

        // Set a cell factory to show only the category name
        categoryListView.setCellFactory(param -> new ListCell<PersonClasses.Category>() {
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

        categoryListView.setItems(observableList);
        categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                descriptionTextField.setText(newValue.getDescription());
            }
        });
    }

    public void handleEditButton() {
        PersonClasses.Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            PersonClasses.Category newCategory = new PersonClasses.Category(nameTextField.getText(), descriptionTextField.getText());
            PersonDao.editCategory(selectedCategory, newCategory);
            categoryListView.getItems().set(categoryListView.getSelectionModel().getSelectedIndex(), newCategory);
        }
    }

    public void handleDeleteButton() {
        PersonClasses.Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            PersonDao.deleteCategory(selectedCategory);
            categoryListView.getItems().remove(selectedCategory);
        }
    }
    public void handleCancelButton() {
        nameTextField.clear();
        descriptionTextField.clear();
        categoryListView.getSelectionModel().clearSelection();
    }
    public void handleCloseButton(){
        Stage stage = (Stage) categoryListView.getScene().getWindow();
        stage.close();
    }
}