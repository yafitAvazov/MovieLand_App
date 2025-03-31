package services;

import dao.AdminDaoImpl;
import dao.MovieDaoImpl;

import dm.Admin;
import dm.Movie;

import java.util.HashMap;
import java.util.Map;

public class MovieCatalogService {
    private Map<String, Movie> movies;
    private Map<String, Admin> admins;
    private MovieService movieService;
    private AdminService adminService;

    private SearchService searchService;
    public MovieCatalogService() {
        searchService = new SearchService();
        movieService = new MovieService();
        adminService = new AdminService();
        movies =movieService.getAllMoviesMap();
        admins = adminService.getAllAdminsMap();
    }
    public Map<String, Movie> searchMovies(String query) {
        Map<String, Movie> result = new HashMap<>();
        String lowerCaseQuery = query.toLowerCase();

        for (Map.Entry<String, Movie> entry : movies.entrySet()) {
            Movie movie = entry.getValue();

            String title = (movie.getTitle() != null) ? movie.getTitle().toLowerCase() : "";
            String genre = (movie.getGenre() != null) ? movie.getGenre().toLowerCase() : "";
            String director = (movie.getDirector() != null) ? movie.getDirector().toLowerCase() : "";
            String description = (movie.getDescription() != null) ? movie.getDescription().toLowerCase() : "";
            String releaseYear = String.valueOf(movie.getReleaseYear());

            boolean containsQuery = title.contains(lowerCaseQuery) ||
                    genre.contains(lowerCaseQuery) ||
                    director.contains(lowerCaseQuery) ||
                    description.contains(lowerCaseQuery) ||
                    releaseYear.contains(lowerCaseQuery) ||
                    (movie.getActors() != null && movie.getActors().stream().anyMatch(actor -> actor.toLowerCase().contains(lowerCaseQuery)));

            if (containsQuery) {
                result.put(entry.getKey(), movie);
            }
        }
        return result;
    }
    public Map<String, Movie> searchMoviesByType(String query, String type) {
        Map<String, Movie> result = new HashMap<>();
        String lowerCaseQuery = query.toLowerCase();

        for (Map.Entry<String, Movie> entry : movies.entrySet()) {
            Movie movie = entry.getValue();

            boolean containsQuery = false;
            switch (type) {
                case "title":
                    containsQuery = movie.getTitle() != null && movie.getTitle().toLowerCase().contains(lowerCaseQuery);
                    break;
                case "genre":
                    containsQuery = movie.getGenre() != null && movie.getGenre().toLowerCase().contains(lowerCaseQuery);
                    break;
                case "director":
                    containsQuery = movie.getDirector() != null && movie.getDirector().toLowerCase().contains(lowerCaseQuery);
                    break;
                case "actor":
                    containsQuery = movie.getActors() != null && movie.getActors().stream().anyMatch(actor -> actor.toLowerCase().contains(lowerCaseQuery));
                    break;
            }
            if (containsQuery) {
                result.put(entry.getKey(), movie);
            }
        }
        return result;
    }

    public Map<String, Admin> searchAdmins(String query) {
        Map<String, Admin> result = new HashMap<>();
        String lowerCaseQuery = query.toLowerCase();

        System.out.println(" Searching for admins matching: " + query);

        for (Map.Entry<String, Admin> entry : admins.entrySet()) {
            Admin admin = entry.getValue();

            String name = (admin.getName() != null) ? admin.getName().toLowerCase() : "";
            String username = (admin.getUsername() != null) ? admin.getUsername().toLowerCase() : "";

            boolean containsQuery = name.contains(lowerCaseQuery) || username.contains(lowerCaseQuery);

            if (containsQuery) {
                result.put(entry.getKey(), admin);
            }
        }
        System.out.println(" Found admins: " + result);
        return result;
    }



    public Map<String, Movie> getMovies() {
        System.out.println("=== getMovies ===");
        System.out.println("Movies in Catalog: " + movies.toString());
        return movies;
    }



    public Map<String, Admin> getAdmins() {
        return admins;
    }
    public boolean addAdmin(Admin admin) {
        System.out.println("=== addAdmin ===");
        System.out.println("Admin to Add: " + admin.toString());

        if (admins.containsKey(admin.getUsername())) {
            System.out.println("Admin already exists with username: " + admin.getUsername());
            return false;
        }
        admins.put(admin.getUsername(), admin);
        AdminDaoImpl adminDao = new AdminDaoImpl();
        adminDao.add(admin.getUsername(), admin);

        System.out.println("Admins After Adding: " + admins.toString());
        return true;
    }


    public boolean addMovie(Movie movie) {
        System.out.println("=== addMovie ===");
        System.out.println("Movie to Add: " + movie.toString());
        if (movies.containsKey(movie.getTitle())) {
            System.out.println("Movie already exists with title: " + movie.getTitle());
            return false;
        }
        movies.put(movie.getTitle(), movie);
        MovieDaoImpl movieDao = new MovieDaoImpl();
        movieDao.add(movie.getTitle(), movie);

        System.out.println("Movies After Adding: " + movies.toString());
        return true;
    }


    public boolean updateMovie(String title, Movie newMovie) {
        boolean success = false;
        if (movies.containsKey(title)) {
            movieService.updateMovieData(title, newMovie);
            movies.put(title, newMovie);
            success = true;
        }
        return success;
    }

    public boolean deleteMovie(String title) {
        boolean success = false;
        if (movies.remove(title) != null){
            movieService.deleteMovie(title);
            success = true;
        }
        return success;
    }

    public boolean deleteAdmin(String username) {
        boolean success = false;
        if (admins.remove(username) != null) {
            adminService.deleteAdmin(username);
            success = true;
        }
        return success;
    }


}
