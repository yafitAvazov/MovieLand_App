// Refactored MovieDaoImpl.java

package dao;

import dm.Movie;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDaoImpl implements IDao<String, Movie> {
    private final String FILE_PATH = "src/resources/Movies.dat";
    private Map<String, Movie> moviesMap;

    public MovieDaoImpl() {
        moviesMap = new HashMap<>();
        loadDataFromFile();
    }

    @Override
    public void add(String id, Movie movie) {
        moviesMap.put(id, movie);
        saveDataToFile();
    }

    @Override
    public void delete(String id) {
        moviesMap.remove(id);
        saveDataToFile();
    }

    @Override
    public void update(String id,Movie movie) {
        if (moviesMap.containsKey(id)) {
            moviesMap.put(id, movie);
            saveDataToFile();
        }
    }

    @Override
    public Movie get(String id) {
        return moviesMap.get(id);
    }

    @Override
    public Map<String, Movie> getAll() {
        return moviesMap;
    }


    private void loadDataFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            moviesMap = (Map<String, Movie>) in.readObject();
        } catch (FileNotFoundException e) {
            moviesMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(moviesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
