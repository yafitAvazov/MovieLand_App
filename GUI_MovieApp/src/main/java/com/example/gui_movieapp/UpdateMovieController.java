package com.example.gui_movieapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UpdateMovieController {
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


    private AllMoviesController allMoviesController;
    private Client client;
    private Movie currentMovie;
    private Stage stage;
    @FXML
    public void initialize() {
        client = Client.getInstance();
    }

    public void setAllMoviesController(AllMoviesController controller) {
        this.allMoviesController = controller;
    }

    public void loadMovieData(Movie movie) {
        if (movie != null) {
            this.currentMovie = movie;

            if (movie.getId() == null) {
                System.out.println("Error: Movie ID is null!");
            } else {
                System.out.println("Movie ID Loaded: " + movie.getId());
            }
            titleField.setText(movie.getTitle());
            titleField.setEditable(false);
            genreField.setText(movie.getGenre());
            directorField.setText(movie.getDirector());
            actorsField.setText(movie.getActors() != null ? movie.getActors() : "");
            releaseYearField.setText(String.valueOf(movie.getReleaseYear()));
            descriptionField.setText(movie.getDescription());

        }
    }

    @FXML
    protected void saveMovieChanges() {
        if (currentMovie == null) return;

        String newTitle = titleField.getText();
        String genre = genreField.getText();
        String director = directorField.getText();
        String actorsText = actorsField.getText();
        String releaseYear = releaseYearField.getText();
        String description = descriptionField.getText();

        if (newTitle.isEmpty() || genre.isEmpty() || director.isEmpty() || actorsText.isEmpty() || releaseYear.isEmpty() || description.isEmpty()) {
            System.out.println("Please fill all fields");
            return;
        }

        String[] actorsArray = actorsText.split(",");
        for (int i = 0; i < actorsArray.length; i++) {
            actorsArray[i] = actorsArray[i].trim();
        }
        List<String> actorsList = Arrays.asList(actorsArray);

        Map<String, Object> response = client.sendRequest("movie/update",
                currentMovie.getId(),
                newTitle,
                genre,
                director,
                actorsList,
                Integer.parseInt(releaseYear),
                description
        );


        if (response != null && response.containsKey("status")) {
            String status = response.get("status").toString();
            if ("success".equalsIgnoreCase(status)) {
                System.out.println("Movie updated successfully");


                currentMovie.setTitle(newTitle);
                currentMovie.setGenre(genre);
                currentMovie.setDirector(director);
                currentMovie.setActors(String.join(", ", actorsList));
                currentMovie.setReleaseYear(Integer.parseInt(releaseYear));
                currentMovie.setDescription(description);


                if (allMoviesController != null) {
                    allMoviesController.refreshTable();
                }
                showAlert("הצלחה", "הסרט עודכן בהצלחה!", Alert.AlertType.INFORMATION);

                Stage stage = (Stage) titleField.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("Error: " + response.get("body"));
            }
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
