package com.example.cashcraft;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class AddPeople {



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

        if (PersonDao.isPeopleExist(name)) {
            System.out.println("Person with name '" + name + "' already exists.");
            return;
        }

        PersonClasses.People people = new PersonClasses.People(name, description);
        try {
            PersonDao.addPeople(people);
            System.out.println("Category added successfully");
        } catch (Exception e) {
            System.err.println("Error adding category: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean peopleExists(String name) {
        return PersonDao.isPeopleExist(name);
    }

}
