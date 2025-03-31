package Controllers;

import services.MovieCatalogService;

import java.util.HashMap;
import java.util.Map;

public class InitController {
    private static final Map<String, Controller> controllers = new HashMap<>();
    static {
        MovieCatalogService movieCatalogService = new MovieCatalogService();

        controllers.put("movie/add", new MovieController(movieCatalogService));
        controllers.put("movie/update", new MovieController(movieCatalogService));
        controllers.put("movie/remove", new MovieController(movieCatalogService));
        controllers.put("movie/get", new MovieController(movieCatalogService));
        controllers.put("movie/getall", new MovieController(movieCatalogService));
        controllers.put("movie/search", new MovieController(movieCatalogService));
        controllers.put("movie/searchbytype", new MovieController(movieCatalogService));

        controllers.put("admin/add", new AdminController(movieCatalogService));
        controllers.put("admin/update", new AdminController(movieCatalogService));
        controllers.put("admin/remove", new AdminController(movieCatalogService));
        controllers.put("admin/get", new AdminController(movieCatalogService));
        controllers.put("admin/search", new AdminController(movieCatalogService));
        controllers.put("admin/getall", new AdminController(movieCatalogService));

    }

    public static Controller getController(String action) {
        return controllers.get(action.toLowerCase());
    }
}
