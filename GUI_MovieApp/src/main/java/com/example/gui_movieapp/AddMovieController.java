package com.example.gui_movieapp;

import com.example.gui_movieapp.AdminController;
import com.example.gui_movieapp.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;

public class AddMovieController {
    private Stage stage;
    private Client client;

    @FXML
    private TextField titleField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField directorField;
    @FXML
    private TextField actorsField;
    @FXML
    private TextField releaseYearField;
    @FXML
    private TextField descriptionField;
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
    protected void addMovieClicked() {
        String title = titleField.getText();
        String genre = genreField.getText();
        String director = directorField.getText();
        String actorsText = actorsField.getText();
        String releaseYear = releaseYearField.getText();
        String description = descriptionField.getText();

        if (title.isEmpty() || genre.isEmpty() || director.isEmpty() || actorsText.isEmpty() || releaseYear.isEmpty() || description.isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: #f66b6b;");
            errorLabel.setText("Please fill all the fields");
            errorLabel.setVisible(true);
            return;
        }

        String[] actorsArray = actorsText.split(",");
        for (int i = 0; i < actorsArray.length; i++) {
            actorsArray[i] = actorsArray[i].trim();
        }

        Map<String, Object> response = client.sendRequest("movie/add", title, genre, director, actorsArray, releaseYear, description);
        if (response != null && response.containsKey("status")) {
            String status = response.get("status").toString();
            if ("success".equalsIgnoreCase(status)) {
                errorLabel.setStyle("-fx-text-fill: #6ee66e;");
                errorLabel.setText("Movie added successfully!");
                titleField.clear();
                genreField.clear();
                directorField.clear();
                actorsField.clear();
                releaseYearField.clear();
                descriptionField.clear();

            } else {
                Object body = response.get("body");
                errorLabel.setStyle("-fx-text-fill: #f66b6b;");
                errorLabel.setText("Error: " + body);
            }
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void goBackToAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/admin-view.fxml"));
        Parent root = loader.load();
        AdminController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Admin Panel");
    }
}
