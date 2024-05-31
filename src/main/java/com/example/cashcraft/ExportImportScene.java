package com.example.cashcraft;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.io.File;

public class ExportImportScene {

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Database Export/Import");
        ImportExportHandler dbHandler = new ImportExportHandler();

        Button exportButton = new Button("Export All Tables to CSV");
        exportButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Export Directory");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                boolean success = dbHandler.exportAllTablesToCSV(selectedDirectory);
                if (success) {
                    showAlert(AlertType.INFORMATION, "Export Successful", "Tables exported successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Export Failed", "Failed to export tables.");
                }
            }
        });

        Button importButton = new Button("Import All Tables from CSV");
        importButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Import Directory");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                boolean success = dbHandler.importAllTablesFromCSV(selectedDirectory);
                if (success) {
                    showAlert(AlertType.INFORMATION, "Import Successful", "Tables imported successfully.");
                } else {
                    showAlert(AlertType.ERROR, "Import Failed", "Failed to import tables.");
                }
            }
        });

        // Apply inline CSS styles to buttons
        String buttonStyle = "-fx-background-color: #000133; "
                + "-fx-background-insets: 0, 1; "
                + "-fx-background-radius: 20; "
                + "-fx-padding: 7 22 7 22; "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 13px; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: transparent; "
                + "-fx-border-radius: 10; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 5); "
                + "-fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #cc5500;";
        String pressedStyle = "-fx-background-color: #002b4d;";

        exportButton.setStyle(buttonStyle);
        importButton.setStyle(buttonStyle);

        // Add hover effect
        exportButton.setOnMouseEntered(e -> exportButton.setStyle(buttonStyle + hoverStyle));
        exportButton.setOnMouseExited(e -> exportButton.setStyle(buttonStyle));
        exportButton.setOnMousePressed(e -> exportButton.setStyle(buttonStyle + pressedStyle));
        exportButton.setOnMouseReleased(e -> exportButton.setStyle(buttonStyle + hoverStyle));

        importButton.setOnMouseEntered(e -> importButton.setStyle(buttonStyle + hoverStyle));
        importButton.setOnMouseExited(e -> importButton.setStyle(buttonStyle));
        importButton.setOnMousePressed(e -> importButton.setStyle(buttonStyle + pressedStyle));
        importButton.setOnMouseReleased(e -> importButton.setStyle(buttonStyle + hoverStyle));

        VBox vbox = new VBox(20, exportButton, importButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #48DBFB;");
        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
