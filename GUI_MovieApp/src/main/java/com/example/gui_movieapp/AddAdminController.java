package com.example.gui_movieapp;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;

public class AddAdminController {
    private Stage stage;
    private Client client;

    @FXML
    private TextField nameField;
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
    protected void addAdminClicked() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Sending Admin Data: " + name + ", " + username + ", " + password);

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: #f66b6b;");
            errorLabel.setText("Please fill all fields");
            errorLabel.setVisible(true);
            return;
        }

        Map<String, Object> response = client.sendRequest("admin/add", name, username, password);
        System.out.println(" Server Response: " + response);

        if (response != null && response.containsKey("status")) {
            String status = response.get("status").toString();
            if ("success".equalsIgnoreCase(status)) {
                errorLabel.setStyle("-fx-text-fill: #6ee66e;");
                errorLabel.setText("Admin added successfully");
                nameField.clear();
                usernameField.clear();
                passwordField.clear();
            } else {
                Object body = response.get("body");
                errorLabel.setStyle("-fx-text-fill: #f66b6b;");
                errorLabel.setText("Error: " + body);
            }
            errorLabel.setVisible(true);

        }
    }


    @FXML
    private void goBackToSuperAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/superAdmin-view.fxml"));
        Parent root = loader.load();
        SuperAdminController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Super-Admin Panel");
    }
}
