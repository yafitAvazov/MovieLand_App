package Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.HandleRequest;
import server.Request;
import server.Response;
import services.MovieCatalogService;
import services.MovieService;
import dm.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieController implements Controller {
    private final MovieCatalogService movieCatalogService;

    public MovieController(MovieCatalogService movieCatalogService) {
        this.movieCatalogService = movieCatalogService;
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            String action = request.getHeaders().get("action");
            Gson gson = new Gson();

            switch (action) {
                case "movie/add":
                    Movie newMovie = gson.fromJson(gson.toJson(request.getBody()), Movie.class);
                    if (newMovie.getTitle() == null || newMovie.getTitle().isEmpty()) {
                        return new Response("failure", "Movie title cannot be empty");
                    }

                    if (newMovie.getActors() == null) {
                        newMovie.setActors(new ArrayList<>());
                    }

                    System.out.println("New Movie: " + newMovie.toString());
                    if (movieCatalogService.addMovie(newMovie))
                    {
                        return new Response("success", "Movie added successfully");
                    }
                    else {
                        return new Response("failure", "Cant add movie");
                    }


                case "movie/search":
                    Object body = request.getBody();

                    if (!(body instanceof Map)) {
                        return new Response("failure", "Invalid request format");
                    }

                    Map<String, Object> requestBody = (Map<String, Object>) body;

                    if (!requestBody.containsKey("query") || !(requestBody.get("query") instanceof String)) {
                        return new Response("failure", "Invalid search query format");
                    }

                    String searchQuery = (String) requestBody.get("query");
                    Map<String, Movie> searchResults = movieCatalogService.searchMovies(searchQuery);

                    if (searchResults.isEmpty()) {
                        return new Response("failure", "No movies found");
                    }
                    return new Response("success", searchResults);


                case "movie/searchbytype":
                    if (!(request.getBody() instanceof Map)) {
                        return new Response("failure", "Invalid request format");
                    }

                    Map<String, Object> searchRequestBody = (Map<String, Object>) request.getBody();
                    System.out.println(" Received request body: " + searchRequestBody);

                    if (!searchRequestBody.containsKey("query") || !(searchRequestBody.get("query") instanceof String)) {
                        return new Response("failure", "Invalid search query format");
                    }

                    if (!searchRequestBody.containsKey("type") || !(searchRequestBody.get("type") instanceof String)) {
                        return new Response("failure", "Invalid search type format");
                    }

                    String searchTerm = (String) searchRequestBody.get("query");
                    String searchCategory = (String) searchRequestBody.get("type");
                    System.out.println(" Searching for '" + searchTerm + "' in category: " + searchCategory);

                    Map<String, Movie> filteredSearchResults = movieCatalogService.searchMoviesByType(searchTerm, searchCategory);

                    if (filteredSearchResults.isEmpty()) {
                        System.out.println(" No results found.");
                        return new Response("failure", "No movies found");
                    }

                    System.out.println("Found movies: " + filteredSearchResults.keySet());
                    return new Response("success", filteredSearchResults);



                case "movie/update":
                    Movie updateMovie = gson.fromJson(gson.toJson(request.getBody()), Movie.class);
                    if (!movieCatalogService.updateMovie(updateMovie.getTitle(), updateMovie)) {
                        return new Response("failure", "Movie not found");
                    }
                    return new Response("success", "Movie updated successfully");


                case "movie/remove":
                    String MovieTitleToRemove = (String) request.getBody();
                    if (!movieCatalogService.deleteMovie(MovieTitleToRemove)) {
                        return new Response("failure", "Movie not found");
                    }
                    return new Response("success", "Movie removed successfully");

                case "movie/get":
                    String MovieTitleToGet = (String) request.getBody();
                    Map<String, Movie> fetchedMovies = movieCatalogService.searchMovies(MovieTitleToGet);

                    if (fetchedMovies.isEmpty()) {
                        return new Response("failure", "Movie not found");
                    }

                    return new Response("success", fetchedMovies);


                case "movie/getall":
                    Map<String, Movie> allMovies = movieCatalogService.getMovies();
                    System.out.println("Sending Movies: " + gson.toJson(allMovies));
                    if (allMovies == null || allMovies.isEmpty()) {
                        return new Response("failure", "No movies found");
                    }
                    return new Response("success", allMovies);



                default:
                    return new Response("failure", "Invalid action for movie");
            }
        } catch (Exception e) {
            return new Response("failure", "Error processing request: " + e.getMessage());
        }
    }
}


