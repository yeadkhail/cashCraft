package com.example.cashcraft;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

import java.util.List;

public class EditPlace {
    @FXML
    private ListView<PersonClasses.Place> placeListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    public void initialize(){
        List<PersonClasses.Place> places = PersonDao.getPlaces();
        ObservableList<PersonClasses.Place> observableList = FXCollections.observableArrayList(places);

        placeListView.setCellFactory(param -> new ListCell<PersonClasses.Place>() {
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

        placeListView.setItems(observableList);
        placeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameTextField.setText(newValue.getName());
                descriptionTextField.setText(newValue.getDescription());
            }
        });
    }

    public void handleEditButton() {
        PersonClasses.Place selectedPlace = placeListView.getSelectionModel().getSelectedItem();
        if (selectedPlace != null) {
            PersonClasses.Place newPlace = new PersonClasses.Place(nameTextField.getText(), descriptionTextField.getText());
            PersonDao.editPlace(selectedPlace, newPlace);
            placeListView.getItems().set(placeListView.getSelectionModel().getSelectedIndex(), newPlace);
        }
    }

    public void handleDeleteButton() {
        PersonClasses.Place selectedPlace = placeListView.getSelectionModel().getSelectedItem();
        if (selectedPlace != null) {
            PersonDao.deletePlace(selectedPlace);
            placeListView.getItems().remove(selectedPlace);
        }
    }
    public void handleCancelButton() {
        nameTextField.clear();
        descriptionTextField.clear();
        placeListView.getSelectionModel().clearSelection();
    }
    public void handleCloseButton(){
        Stage stage = (Stage) placeListView.getScene().getWindow();
        stage.close();
    }
}