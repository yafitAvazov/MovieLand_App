package com.example.gui_movieapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class LoginController {
    private Stage stage;
    private Client client;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        client = Client.getInstance();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("Please enter a username");
            errorLabel.setVisible(true);
            return;
        }

        System.out.println("📌 Debug - Sending username: " + username);

        Map<String, Object> response = client.sendRequest("admin/get", username);

        if (response != null && "success".equals(response.get("status"))) {
            Map<String, Object> adminData = (Map<String, Object>) response.get("body");

            if (adminData != null) {
                String storedPassword = (String) adminData.get("password");
                System.out.println("📌 Debug - Stored password: " + storedPassword);

                if (password.equals(storedPassword)) {
                    loadAdminView();
                } else {
                    errorLabel.setText("Incorrect username or password");
                    errorLabel.setVisible(true);
                }
            } else {
                errorLabel.setText("User not found");
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("User not found");
            errorLabel.setVisible(true);
        }
    }


    private void loadAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/admin-view.fxml"));
            Parent root = loader.load();
            AdminController controller = loader.getController();
            controller.setStage(stage);
            controller.setLoggedInAdmin(usernameField.getText());
            stage.setScene(new Scene(root, 600, 700));
            stage.setTitle("Admin Panel");
        } catch (IOException e) {
            errorLabel.setText("Error loading admin panel");
            errorLabel.setVisible(true);
        }
    }



    @FXML
    private void goBackToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/hello-view.fxml"));
        Parent root = loader.load();
        HelloController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("MovieLand - Home");
    }
}