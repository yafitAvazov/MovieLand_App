package services;

import dm.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi, Action");
        movie.setDirector("Christopher Nolan");
        movie.setReleaseYear(2010);
        movie.setActors(Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page")); // ✅ לוודא רשימה
        movie.setDescription("A thief with the ability to enter people's dreams and steal secrets gets a chance at redemption.");

        movieService.addMovie("1", movie);

        Movie retrievedMovie = movieService.getAllMovies("1");
        assertNotNull(retrievedMovie);
        assertEquals("Inception", retrievedMovie.getTitle());
        assertEquals("Sci-Fi, Action", retrievedMovie.getGenre());
        assertEquals("Christopher Nolan", retrievedMovie.getDirector());
        assertEquals(2010, retrievedMovie.getReleaseYear());
        assertEquals(Arrays.asList("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page"), retrievedMovie.getActors());
        assertEquals("A thief with the ability to enter people's dreams and steal secrets gets a chance at redemption.", retrievedMovie.getDescription());
    }

    @Test
    public void testUpdateMovieData() {
        Movie movie = new Movie();
        movie.setTitle("Titanic");
        movie.setGenre("Romance, Drama");
        movie.setDirector("James Cameron");
        movie.setReleaseYear(1997);
        movie.setActors(Arrays.asList("Leonardo DiCaprio", "Kate Winslet")); // ✅ רשימה ולא String
        movie.setDescription("A young couple from different social classes fall in love aboard the ill-fated Titanic.");

        movieService.addMovie("2", movie);

        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("The Matrix Reloaded");
        updatedMovie.setGenre("Sci-Fi, Action");
        updatedMovie.setDirector("Lana Wachowski, Lilly Wachowski");
        updatedMovie.setReleaseYear(2003);
        updatedMovie.setActors(Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss")); // ✅ רשימה
        updatedMovie.setDescription("Neo and the rebel leaders continue their fight against the machines in a world where reality is an illusion.");

        movieService.updateMovieData("2", updatedMovie);

        Movie retrievedMovie = movieService.getAllMovies("2");
        assertNotNull(retrievedMovie);
        assertEquals("The Matrix Reloaded", retrievedMovie.getTitle());
        assertEquals("Sci-Fi, Action", retrievedMovie.getGenre());
        assertEquals("Lana Wachowski, Lilly Wachowski", retrievedMovie.getDirector());
        assertEquals(2003, retrievedMovie.getReleaseYear());
        assertEquals(Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"), retrievedMovie.getActors());
        assertEquals("Neo and the rebel leaders continue their fight against the machines in a world where reality is an illusion.", retrievedMovie.getDescription());
    }

    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setTitle("Pulp Fiction");
        movie.setGenre("Crime, Drama");
        movie.setDirector("Quentin Tarantino");
        movie.setReleaseYear(1994);
        movie.setActors(Arrays.asList("John Travolta", "Uma Thurman", "Samuel L. Jackson")); // ✅ רשימה ולא String
        movie.setDescription("The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.");

        movieService.addMovie("3", movie);

        movieService.deleteMovie("3");

        Movie retrievedMovie = movieService.getAllMovies("3");
        assertNull(retrievedMovie);
    }

    @Test
    public void testGetAllMoviesMap() {
        Movie movie1 = new Movie();
        movie1.setTitle("The Shawshank Redemption");
        movie1.setGenre("Drama");
        movie1.setDirector("Frank Darabont");
        movie1.setReleaseYear(1994);
        movie1.setActors(Arrays.asList("Tim Robbins", "Morgan Freeman")); // ✅ רשימה ולא String
        movie1.setDescription("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        movieService.addMovie("4", movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight");
        movie2.setGenre("Action, Crime, Drama");
        movie2.setDirector("Christopher Nolan");
        movie2.setReleaseYear(2008);
        movie2.setActors(Arrays.asList("Christian Bale", "Heath Ledger", "Aaron Eckhart")); // ✅ רשימה
        movie2.setDescription("When the menace known as The Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.");
        movieService.addMovie("5", movie2);

        Map<String, Movie> allMovies = movieService.getAllMoviesMap();
        assertNotNull(allMovies);
        assertEquals(allMovies.size(), allMovies.size());
        assertTrue(allMovies.containsKey("4"));
        assertTrue(allMovies.containsKey("5"));

        Movie retrievedMovie1 = allMovies.get("4");
        assertEquals("The Shawshank Redemption", retrievedMovie1.getTitle());
        assertEquals("Drama", retrievedMovie1.getGenre());
        assertEquals("Frank Darabont", retrievedMovie1.getDirector());
        assertEquals(1994, retrievedMovie1.getReleaseYear());
        assertEquals(Arrays.asList("Tim Robbins", "Morgan Freeman"), retrievedMovie1.getActors());
        assertEquals("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", retrievedMovie1.getDescription());

        Movie retrievedMovie2 = allMovies.get("5");
        assertEquals("The Dark Knight", retrievedMovie2.getTitle());
        assertEquals("Action, Crime, Drama", retrievedMovie2.getGenre());
        assertEquals("Christopher Nolan", retrievedMovie2.getDirector());
        assertEquals(2008, retrievedMovie2.getReleaseYear());
        assertEquals(Arrays.asList("Christian Bale", "Heath Ledger", "Aaron Eckhart"), retrievedMovie2.getActors());
        assertEquals("When the menace known as The Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham.", retrievedMovie2.getDescription());
    }
}
