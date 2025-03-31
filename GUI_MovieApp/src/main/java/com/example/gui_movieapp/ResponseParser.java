package com.example.gui_movieapp;

import com.example.gui_movieapp.Client;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

public final class ResponseParser {


    public static ObservableList<String> parseGetMovies(Map<String, Object> responseMap) {
        ObservableList<String> movieList = FXCollections.observableArrayList();

        try {
            if (responseMap.containsKey("status")) {
                String status = (String) responseMap.get("status");
                if ("error".equalsIgnoreCase(status)) {
                    String errorMessage = (String) responseMap.get("body");
                    movieList.add("Error: " + errorMessage);
                    return movieList;
                }
            }

            if (responseMap.containsKey("body")) {
                Map<String, Object> body = (Map<String, Object>) responseMap.get("body");

                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    Map<String, Object> movieData = (Map<String, Object>) entry.getValue();
                    movieList.add(formatMovie(movieData));
                }
            }
        } catch (Exception e) {
            movieList.add("Error parsing response: " + e.getMessage());
        }

        return movieList;
    }

    public static ObservableList<String> parseGetAdmins(Map<String, Object> responseMap) {
        ObservableList<String> adminList = FXCollections.observableArrayList();

        try {
            if (responseMap.containsKey("body")) {
                Map<String, Object> body = (Map<String, Object>) responseMap.get("body");

                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    Map<String, Object> adminData = (Map<String, Object>) entry.getValue();
                    adminList.add(formatAdmin(adminData));  // שימוש בפורמט החדש
                }
            }
        } catch (Exception e) {
            adminList.add("Error parsing response: " + e.getMessage());
        }

        return adminList;
    }

    private static String formatAdmin(Map<String, Object> adminMap) {
        return String.format("Name: %s | Username: %s",
                adminMap.getOrDefault("name", "Unknown"),
                adminMap.getOrDefault("username", "Unknown"));
    }

    public static ObservableList<Admin> getAllAdminsAsObjects(Map<String, Object> responseMap) {
        ObservableList<Admin> adminList = FXCollections.observableArrayList();

        try {
            if (responseMap.containsKey("body")) {
                Map<String, Object> body = (Map<String, Object>) responseMap.get("body");
                System.out.println("Admin data received from server: " + body);

                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    Map<String, Object> adminData = (Map<String, Object>) entry.getValue();
                    String name = (String) adminData.get("name");
                    String username = (String) adminData.get("username");

                    Admin admin = new Admin(name, username, "");
                    adminList.add(admin);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing admins: " + e.getMessage());
        }

        return adminList;
    }



    public static ObservableList<Movie> getAllMoviesAsObjects(Map<String, Object> responseMap) {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();

        try {
            if (responseMap.containsKey("body")) {
                Map<String, Object> body = (Map<String, Object>) responseMap.get("body");

                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    Map<String, Object> movieData = (Map<String, Object>) entry.getValue();
                    String title = (String) movieData.get("title");
                    String genre = (String) movieData.get("genre");
                    String description = (String) movieData.get("description");


                    String director;
                    Object directorObj = movieData.get("director");
                    if (directorObj instanceof String) {
                        director = (String) directorObj;
                    } else if (directorObj instanceof List) {
                        List<String> directorList = (List<String>) directorObj;
                        director = String.join(", ", directorList);
                    } else {
                        director = "Unknown";
                    }
                    String actors;
                    Object actorsObj = movieData.get("actors");
                    if (actorsObj instanceof String) {
                        actors = (String) actorsObj;
                    } else if (actorsObj instanceof List) {
                        List<String> actorsList = (List<String>) actorsObj;
                        actors = String.join(", ", actorsList);
                    } else {
                        actors = "Unknown";
                    }

                    int releaseYear;
                    Object releaseYearObj = movieData.get("releaseYear");
                    if (releaseYearObj instanceof Double) {
                        releaseYear = ((Double) releaseYearObj).intValue();
                    } else if (releaseYearObj instanceof Integer) {
                        releaseYear = (int) releaseYearObj;
                    } else {
                        releaseYear = 0;
                    }


                    Movie movie = new Movie(title, genre, director, releaseYear, actors,description);
                    movieList.add(movie);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing movies: " + e.getMessage());
        }

        return movieList;
    }



    private static String formatMovie(Map<String, Object> movieMap) {
        return String.format("{title=%s, genre=%s, director=%s, releaseYear=%s, actors=%s, description=%s}",
                movieMap.getOrDefault("title", "Unknown"),
                movieMap.getOrDefault("genre", "Unknown"),
                movieMap.getOrDefault("director", "Unknown"),
                movieMap.getOrDefault("releaseYear", "Unknown"),
        movieMap.getOrDefault("actors", "Unknown"),
        movieMap.getOrDefault("description", "Unknown"));

    }

}

