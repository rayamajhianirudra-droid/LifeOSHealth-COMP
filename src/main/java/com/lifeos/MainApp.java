package com.lifeos;

import com.lifeos.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/lifeos/login.fxml"));
        Scene scene = new Scene(loader.load(), 500, 600);
        scene.getStylesheets().add(
                getClass().getResource("/com/lifeos/style.css").toExternalForm());
        LoginController controller = loader.getController();
        controller.setStage(stage);
        stage.setTitle("LifeOS Health");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}