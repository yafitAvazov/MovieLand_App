// Refactored MovieService.java

package services;
import dao.MovieDaoImpl;
import dao.IDao;
import dm.Movie;
import java.util.List;
import java.util.Map;

public class MovieService {
    private final IDao<String, Movie> movieDAO;

    public MovieService() {this.movieDAO = new MovieDaoImpl();}

    public void addMovie(String id,Movie movie) {movieDAO.add(id, movie);}

    public void deleteMovie(String id) {movieDAO.delete(id);}

    public void updateMovieData(String id,Movie movie ) {movieDAO.update(id,movie);}

    public Movie getAllMovies(String id) {return movieDAO.get(id);}

    public Map<String, Movie> getAllMoviesMap() {
        return movieDAO.getAll();
    }}
