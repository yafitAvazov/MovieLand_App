package Controllers;

import com.google.gson.Gson;
import dm.Movie;
import server.Request;
import server.Response;
import services.AdminService;
import dm.Admin;
import services.MovieCatalogService;

import java.util.HashMap;
import java.util.Map;

public class AdminController implements Controller {
    private final MovieCatalogService movieCatalogService;

    public AdminController(MovieCatalogService movieCatalogService) {
        this.movieCatalogService = movieCatalogService;
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            String action = request.getHeaders().get("action");
            Gson gson = new Gson();

            switch (action) {
                case "admin/add":
                    Admin newAdmin = gson.fromJson(gson.toJson(request.getBody()), Admin.class);
                    System.out.println("Received admin: " + newAdmin);

                    if (newAdmin.getUsername() == null || newAdmin.getUsername().isEmpty()) {
                        return new Response("failure", "Admin username cannot be empty");
                    }

                    boolean success = movieCatalogService.addAdmin(newAdmin);
                    if (success) {
                        return new Response("success", "Admin added successfully");
                    } else {
                        return new Response("failure", "Cannot add admin (Possible duplicate or DB issue)");
                    }




                case "admin/search":
                    Object body = request.getBody();

                    if (!(body instanceof Map)) {
                        return new Response("failure", "Invalid request format");
                    }

                    Map<String, Object> requestBody1 = (Map<String, Object>) body;

                    if (!requestBody1.containsKey("query") || !(requestBody1.get("query") instanceof String)) {
                        return new Response("failure", "Invalid search query format");
                    }

                    String searchQuery = (String) requestBody1.get("query");
                    Map<String, Admin> searchResults = movieCatalogService.searchAdmins(searchQuery);

                    if (searchResults.isEmpty()) {
                        return new Response("failure", "No movies found");
                    }
                    return new Response("success", searchResults);


                case "admin/remove":
                    Object body1 = request.getBody();
                    if (!(body1 instanceof Map)) {
                        System.out.println(" Body is not a Map. Received: " + body1);
                        return new Response("failure", "Invalid request format");
                    }

                    Map<String, Object> requestBody2 = (Map<String, Object>) body1;

                    if (!requestBody2.containsKey("username") || !(requestBody2.get("username") instanceof String)) {
                        System.out.println("Missing or invalid 'username' parameter.");
                        return new Response("failure", "Invalid remove request format");
                    }

                    String username1 = (String) requestBody2.get("username");
                    System.out.println("Attempting to remove admin: " + username1);

                    boolean removed = movieCatalogService.deleteAdmin(username1);

                    if (!removed) {
                        return new Response("failure", "Admin not found or could not be removed");
                    }
                    return new Response("success", "Admin removed successfully");



                case "admin/get":
                    Map<String, Object> requestBody = (Map<String, Object>) request.getBody();
                    System.out.println(" Received request body in admin/get: " + requestBody);

                    if (requestBody == null || !requestBody.containsKey("username")) {
                        return new Response("failure", "Invalid request format: missing username");
                    }

                    String username = (String) requestBody.get("username");
                    System.out.println(" Looking for admin: " + username);

                    Admin fetchedAdmin = movieCatalogService.getAdmins().get(username);

                    if (fetchedAdmin == null) {
                        return new Response("failure", "Admin not found");
                    }

                    System.out.println(" Found Admin: " + fetchedAdmin);
                    return new Response("success", fetchedAdmin);


                case "admin/getall":
                    Map<String, Admin> allAdmins = movieCatalogService.getAdmins();
                    String jsonResponse = gson.toJson(allAdmins);

                    System.out.println("📌 Debug - JSON Response Length: " + jsonResponse.length());
                    return new Response("success", allAdmins);


                default:
                    return new Response("failure", "Invalid action for admin");
            }
        } catch (Exception e) {
            return new Response("failure", "Error processing request: " + e.getMessage());
        }
    }
}
