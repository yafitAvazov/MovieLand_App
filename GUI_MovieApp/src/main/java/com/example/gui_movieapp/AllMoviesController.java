package com.example.gui_movieapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class AllMoviesController {
    private Stage stage;
    private Client client;

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
    private TableColumn<Movie, String> descriptionColumn;
    @FXML
    private TableColumn<Movie, Void> editColumn;
    @FXML
    private TableColumn<Movie, Void> deleteColumn;

    @FXML
    public void initialize() {
        client = Client.getInstance();

        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        genreColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGenre()));
        directorColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDirector()));
        releaseYearColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReleaseYear()));
        actorsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getActorsAsString()));
        descriptionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));


        addEditButtonToTable();
        addDeleteButton();
        movieTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadMovies();
    }

    public void refreshTable() {
        movieTable.refresh();
    }

    private void loadMovies() {
        Map<String, Object> response = client.sendRequest("movie/getall");
        System.out.println("Response from server: " + response);
        ObservableList<Movie> movies = ResponseParser.getAllMoviesAsObjects(response);
        if (!movies.isEmpty()) {
            movieTable.setItems(movies);
        } else {
            movieTable.setPlaceholder(new Label("No movies available."));
        }
    }

    private void addEditButtonToTable() {
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    openEditMovieWindow(movie);
                });
                editButton.setStyle("-fx-background-color: #78c189; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }

    private void openEditMovieWindow(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gui_movieapp/update_movie.fxml"));
            Parent root = loader.load();
            UpdateMovieController controller = loader.getController();
            controller.setAllMoviesController(this);
            controller.loadMovieData(movie);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Movie - " + movie.getTitle());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
                deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the movie: " + title + "?");

        ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                Map<String, Object> serverResponse = client.sendRequest("movie/remove", title);
                if (serverResponse != null && "success".equalsIgnoreCase(serverResponse.get("status").toString())) {
                    System.out.println("Movie removed: " + title);
                    ObservableList<Movie> movies = movieTable.getItems();
                    movies.removeIf(movie -> movie.getTitle().equals(title));
                    movieTable.refresh();
                } else {
                    System.out.println("Failed to remove movie: " + title);
                }
            }
        });
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
