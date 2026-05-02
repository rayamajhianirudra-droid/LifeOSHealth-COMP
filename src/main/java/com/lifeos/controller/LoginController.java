package com.lifeos.controller;

import com.lifeos.model.User;
import com.lifeos.service.DataService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class LoginController {

    @FXML private ListView<User> userListView;
    @FXML private Label statusLabel;

    private Stage stage;
    private final DataService dataService = new DataService();

    @FXML
    public void initialize() {
        refreshUserList();
        statusLabel.setText("");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void refreshUserList() {
        List<User> users = dataService.loadAllUsers();
        userListView.getItems().setAll(users);
    }

    @FXML
    private void handleLogin() {
        User selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a profile to continue.");
            return;
        }
        openDashboard(selected);
    }

    @FXML
    private void handleNewProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/lifeos/setup.fxml"));
            Scene scene = new Scene(loader.load(), 500, 640);
            scene.getStylesheets().add(
                    getClass().getResource("/com/lifeos/style.css").toExternalForm());
            SetupController controller = loader.getController();
            controller.setStage(stage);
            stage.setScene(scene);
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteProfile() {
        User selected = userListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a profile to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + selected.getName() + "? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                dataService.deleteUser(selected);
                refreshUserList();
                statusLabel.setText("Profile deleted.");
            }
        });
    }

    private void openDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/lifeos/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 960, 700);
            scene.getStylesheets().add(
                    getClass().getResource("/com/lifeos/style.css").toExternalForm());
            DashboardController controller = loader.getController();
            controller.setStage(stage);
            controller.initData(user);
            stage.setTitle("LifeOS Health — " + user.getName());
            stage.setScene(scene);
        } catch (Exception e) {
            // Show full error details
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            statusLabel.setText("Error: " + cause.getMessage());
            e.printStackTrace();
        }
    }
}