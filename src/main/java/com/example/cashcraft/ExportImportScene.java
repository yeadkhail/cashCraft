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

        VBox vbox = new VBox(20, exportButton, importButton);
        vbox.setAlignment(Pos.CENTER);

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
