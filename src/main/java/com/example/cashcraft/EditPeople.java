package com.example.cashcraft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

import java.util.List;

public class EditPeople {
    @FXML
    private ListView<PersonClasses.People> peopleListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    public void initialize(){
        List<PersonClasses.People> people = PersonDao.getPeople();
        ObservableList<PersonClasses.People> observableList = FXCollections.observableArrayList(people);

        // Set a cell factory to show only the people name
        peopleListView.setCellFactory(param -> new ListCell<PersonClasses.People>() {
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

        peopleListView.setItems(observableList);
        peopleListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                descriptionTextField.setText(newValue.getDescription());
            }
        });
    }

    public void handleEditButton() {
        PersonClasses.People selectedPerson = peopleListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            PersonClasses.People newPerson = new PersonClasses.People(nameTextField.getText(), descriptionTextField.getText());
            PersonDao.editPerson(selectedPerson, newPerson);
            peopleListView.getItems().set(peopleListView.getSelectionModel().getSelectedIndex(), newPerson);
        }
    }

    public void handleDeleteButton() {
        PersonClasses.People selectedPerson = peopleListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            PersonDao.deletePerson(selectedPerson);
            peopleListView.getItems().remove(selectedPerson);
        }
    }
    public void handleCancelButton() {
        nameTextField.clear();
        descriptionTextField.clear();
        peopleListView.getSelectionModel().clearSelection();
    }
    public void handleCloseButton(){
        Stage stage = (Stage) peopleListView.getScene().getWindow();
        stage.close();
    }
}