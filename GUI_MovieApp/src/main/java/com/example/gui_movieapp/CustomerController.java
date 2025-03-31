package com.example.gui_movieapp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomerController {
    private Client client;
    private Stage stage;
    @FXML
    private ToggleGroup searchToggleGroup;
    @FXML
    private ToggleButton allButton;
    @FXML
    private ToggleButton titleButton;
    @FXML
    private ToggleButton genreButton;
    @FXML
    private ToggleButton actorButton;
    @FXML
    private ToggleButton directorButton;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<Movie> movieListView;
    @FXML
    private Label noMoviesLabel;

    @FXML
    public void initialize() {
        client = Client.getInstance();


        movieListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox movieBox = new VBox();
                    movieBox.setSpacing(8);
                    movieBox.setPadding(new Insets(12));
                    movieBox.getStyleClass().add("movie-item");

                    Label titleLabel = new Label(movie.getTitle());
                    titleLabel.getStyleClass().add("movie-title");

                    Label genreLabel = new Label("🎭 Genre: " + movie.getGenre());
                    genreLabel.getStyleClass().add("movie-details");

                    Label directorLabel = new Label("🎬 Director: " + movie.getDirector());
                    directorLabel.getStyleClass().add("movie-details");

                    Label releaseYearLabel = new Label("📅 Year: " + movie.getReleaseYear());
                    releaseYearLabel.getStyleClass().add("movie-details");

                    Label actorsLabel = new Label("⭐ Starring: " + movie.getActors());
                    actorsLabel.getStyleClass().add("movie-details");

                    movieBox.getChildren().addAll(titleLabel, genreLabel, directorLabel, releaseYearLabel, actorsLabel);
                    setGraphic(movieBox);
                }
            }
        });
        searchToggleGroup = new ToggleGroup();
        allButton.setToggleGroup(searchToggleGroup);
        titleButton.setToggleGroup(searchToggleGroup);
        genreButton.setToggleGroup(searchToggleGroup);
        actorButton.setToggleGroup(searchToggleGroup);
        directorButton.setToggleGroup(searchToggleGroup);

        setButtonStyle(allButton);
        setButtonStyle(titleButton);
        setButtonStyle(genreButton);
        setButtonStyle(actorButton);
        setButtonStyle(directorButton);

        allButton.setOnAction(event -> {
            resetMovieList();
            loadAllMovies();
            updateButtonStyles(allButton);
        });

    }


    private void setButtonStyle(ToggleButton button) {
        button.setOnAction(event -> {
            allButton.getStyleClass().remove("selected-toggle");
            titleButton.getStyleClass().remove("selected-toggle");
            genreButton.getStyleClass().remove("selected-toggle");
            actorButton.getStyleClass().remove("selected-toggle");
            directorButton.getStyleClass().remove("selected-toggle");
                updateButtonStyles(button);
                resetMovieList();
            movieListView.setItems(FXCollections.observableArrayList());
            if (button.isSelected()) {
                button.getStyleClass().add("selected-toggle");
            }
        });


    }

    private void updateButtonStyles(ToggleButton selectedButton) {
        allButton.getStyleClass().remove("selected-toggle");
        titleButton.getStyleClass().remove("selected-toggle");
        genreButton.getStyleClass().remove("selected-toggle");
        actorButton.getStyleClass().remove("selected-toggle");
        directorButton.getStyleClass().remove("selected-toggle");

        if (selectedButton != null) {
            selectedButton.getStyleClass().add("selected-toggle");
        }
    }

    private void resetMovieList() {
        movieListView.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void searchMovies() {
        String query = searchField.getText().trim();
        if (allButton.isSelected()) {
            loadAllMovies();
            return;
        }
        if (query.isEmpty()) {
            movieListView.setItems(FXCollections.observableArrayList());
            noMoviesLabel.setVisible(true);
            return;
        }

        String searchType = "";
        if (titleButton.isSelected()) {
            searchType = "title";
        } else if (genreButton.isSelected()) {
            searchType = "genre";
        } else if (actorButton.isSelected()) {
            searchType = "actor";
        } else if (directorButton.isSelected()) {
            searchType = "director";
        }

        if (searchType.isEmpty()) {
            System.out.println("No search type selected.");
            return;
        }

        Map<String, Object> response = client.sendRequest("movie/searchbytype", query, searchType);

        ObservableList<Movie> movies = FXCollections.observableArrayList();
        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            for (Map.Entry<String, Object> entry : body.entrySet()) {
                Map<String, Object> movieData = (Map<String, Object>) entry.getValue();
                String title = (String) movieData.get("title");
                String genre = (String) movieData.get("genre");
                String director = (String) movieData.get("director");

                int releaseYear;
                Object releaseYearObj = movieData.get("releaseYear");
                if (releaseYearObj instanceof Double) {
                    releaseYear = ((Double) releaseYearObj).intValue();
                } else if (releaseYearObj instanceof Integer) {
                    releaseYear = (int) releaseYearObj;
                } else {
                    releaseYear = 0;
                }

                String actors;
                Object actorsObj = movieData.get("actors");
                if (actorsObj instanceof java.util.List) {
                    actors = String.join(", ", (java.util.List<String>) actorsObj);
                } else {
                    actors = "Unknown";
                }

                Movie movie = new Movie(title, genre, director, releaseYear, actors);
                movies.add(movie);
            }
        }

        movieListView.setItems(movies);

        if (movies.isEmpty()) {
            noMoviesLabel.setVisible(true);
        } else {
            noMoviesLabel.setVisible(false);
        }

        searchField.clear();
    }

    private void loadAllMovies() {
        Map<String, Object> response = client.sendRequest("movie/getall");

        ObservableList<Movie> movies = FXCollections.observableArrayList();
        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            for (Map.Entry<String, Object> entry : body.entrySet()) {
                Map<String, Object> movieData = (Map<String, Object>) entry.getValue();
                String title = (String) movieData.get("title");
                String genre = (String) movieData.get("genre");
                String director = (String) movieData.get("director");

                int releaseYear;
                Object releaseYearObj = movieData.get("releaseYear");
                if (releaseYearObj instanceof Double) {
                    releaseYear = ((Double) releaseYearObj).intValue();
                } else if (releaseYearObj instanceof Integer) {
                    releaseYear = (int) releaseYearObj;
                } else {
                    releaseYear = 0;
                }

                String actors;
                Object actorsObj = movieData.get("actors");
                if (actorsObj instanceof java.util.List) {
                    actors = String.join(", ", (java.util.List<String>) actorsObj);
                } else {
                    actors = "Unknown";
                }

                Movie movie = new Movie(title, genre, director, releaseYear, actors);
                movies.add(movie);
            }
        }

        movieListView.setItems(movies);
        if (movies.isEmpty()) {
            noMoviesLabel.setVisible(true);
        } else {
            noMoviesLabel.setVisible(false);
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
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
