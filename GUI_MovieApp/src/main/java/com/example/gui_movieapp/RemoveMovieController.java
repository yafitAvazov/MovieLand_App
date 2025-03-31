package com.example.gui_movieapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveMovieController {
    private Stage stage;
    private Client client;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, String> directorColumn;
    @FXML
    private TableColumn<Movie, Integer> releaseYearColumn;
    @FXML
    private TableColumn<Movie, String> actorsColumn;
    @FXML
    private TableColumn<Movie, Void> deleteColumn;

    @FXML
    public void initialize() {
        client = Client.getInstance();

        // הגדרת קישור בין העמודות לערכים מה- Movie.java
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        genreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGenre()));
        directorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDirector()));
        releaseYearColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReleaseYear()));
        actorsColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.join(", ", cellData.getValue().getActors())));
        movieTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addDeleteButton();
    }
    @FXML
    private void searchMovies() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            movieTable.setItems(FXCollections.observableArrayList());
            return;
        }

        // שליחת הבקשה עם גוף מתאים
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Map<String, Object> response = client.sendRequest("movie/search", requestBody);

        System.out.println("Sent request: " + requestBody);
        System.out.println("Response from server: " + response);

        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            Map<String, Object> body = (Map<String, Object>) response.get("body");
            ObservableList<Movie> movies = FXCollections.observableArrayList();

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
                if (actorsObj instanceof List) {
                    List<String> actorsList = (List<String>) actorsObj;
                    actors = String.join(", ", actorsList);
                } else {
                    actors = "Unknown";
                }

                Movie movie = new Movie(title, genre, director, releaseYear, actors);
                movies.add(movie);
            }

            movieTable.setItems(movies);
        } else {
            movieTable.setItems(FXCollections.observableArrayList());
            System.out.println("No movies found.");
        }
    }


    private void addDeleteButton() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    removeMovie(movie.getTitle());
                });
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }
    private void removeMovie(String title) {
        Map<String, Object> response = client.sendRequest("movie/remove", title);
        if (response != null && response.containsKey("status") && "success".equalsIgnoreCase(response.get("status").toString())) {
            System.out.println("Movie removed: " + title);
            searchMovies(); // חיפוש מחדש כדי לעדכן את הטבלה
        } else {
            System.out.println("Failed to remove movie: " + title);
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
