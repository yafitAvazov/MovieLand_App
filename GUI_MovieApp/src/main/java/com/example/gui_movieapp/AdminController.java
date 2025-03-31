package com.example.gui_movieapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public class AdminController {
    private Stage stage;
    private Client client;
    private String loggedInUser;


    @FXML
    public void initialize() {
        client = Client.getInstance();
    }


    public void setLoggedInAdmin(String username) {
        this.loggedInUser = username;
        client.setLoggedInUser(username);
    }

    @FXML
    private void viewAdmins() throws IOException {
        String currentUser = client.getLoggedInUser();

        if (!"admin".equals(currentUser)) {
            showNoPermissionPopup();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/superAdmin-view.fxml"));
        Parent root = loader.load();
        SuperAdminController controller = loader.getController();
        controller.setStage(stage);
        controller.setLoggedInAdmin(currentUser);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("Super-Admin Panel");
    }

    private void showNoPermissionPopup() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "You do not have permission to access this section.", ButtonType.OK);
        alert.setTitle("Access Denied");
        alert.setHeaderText(null);
        alert.showAndWait();
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
    @FXML
    private void AddMovie() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/Add_movie.fxml"));
        Parent root = loader.load();
        AddMovieController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("MovieLand - Add Movie");
    }

    @FXML
    private void AllMovies() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/all_movies.fxml"));
        Parent root = loader.load();
        AllMoviesController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 860, 700));
        stage.setTitle("MovieLand - All Movies");
    }
    @FXML
    private void RemoveMovie() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/remove_movie.fxml"));
        Parent root = loader.load();
        RemoveMovieController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 600, 700));
        stage.setTitle("MovieLand - Remove Movie");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
