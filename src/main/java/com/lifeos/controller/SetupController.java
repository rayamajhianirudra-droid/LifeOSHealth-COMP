package com.lifeos.controller;

import com.lifeos.model.User;
import com.lifeos.service.DataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SetupController {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField weightField;
    @FXML private TextField heightFeetField;
    @FXML private TextField heightInchesField;
    @FXML private ComboBox<String> goalBox;
    @FXML private ComboBox<String> activityBox;
    @FXML private ComboBox<String> sexBox;
    @FXML private Label errorLabel;

    private Stage stage;

    @FXML
    public void initialize() {
        goalBox.getItems().addAll("lose_fat", "build_muscle", "maintain");
        activityBox.getItems().addAll("sedentary", "light", "moderate", "active", "very_active");
        sexBox.getItems().addAll("male", "female");
        goalBox.setValue("maintain");
        activityBox.setValue("moderate");
        sexBox.setValue("male");
        errorLabel.setText("");
    }

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    private void handleSave() {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
            int age          = Integer.parseInt(ageField.getText().trim());
            double weightLbs = Double.parseDouble(weightField.getText().trim());
            int feet         = Integer.parseInt(heightFeetField.getText().trim());
            int inches       = Integer.parseInt(heightInchesField.getText().trim());
            if (age <= 0 || weightLbs <= 0 || feet <= 0)
                throw new IllegalArgumentException("Please enter valid positive values.");
            if (inches < 0 || inches > 11)
                throw new IllegalArgumentException("Inches must be between 0 and 11.");

            User user = new User(name, age, weightLbs, feet, inches,
                    goalBox.getValue(), activityBox.getValue(), sexBox.getValue());
            new DataService().saveUser(user);
            goToLogin();

        } catch (NumberFormatException e) {
            errorLabel.setText("Please enter valid numbers in all fields.");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() { goToLogin(); }

    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/lifeos/login.fxml"));
            Scene scene = new Scene(loader.load(), 500, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/com/lifeos/style.css").toExternalForm());
            LoginController controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("LifeOS Health — Login");
            stage.setScene(scene);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }
}