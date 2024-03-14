/**
 * This hello-application.java file implements the application of cashCraft.
 * It holds the main function for the project and holds the application class from javafx application
 * @ author: Md. Yead Khail Shovo , Muhammad Tausif Ul Islam , Shahir Awlad
 * The view-only link for the E-R diagram for the sqlite-database : https://lucid.app/lucidchart/95af82fb-b0e0-4918-9560-080446c1473c/edit?viewport_loc=-602%2C-186%2C2834%2C1218%2C0_0&invitationId=inv_66e7b2eb-a3c9-4cc4-be92-b9020dd5d2c9
 * Database structure is changed only on request-only basis.
 *
 */

package com.example.cashcraft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CashCraft");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This is the main method for the application
     * @param args not used
     */
    public static void main(String[] args) {
        launch();
    }
}