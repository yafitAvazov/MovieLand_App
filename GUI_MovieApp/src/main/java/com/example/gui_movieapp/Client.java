package com.example.gui_movieapp;
import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static Client instance;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Gson gson;
    private boolean isConnected;

    private Client() {
        this.gson = new Gson();
        connect();

    }


    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }


    private void connect() {
        try {
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.isConnected = true;
            System.out.println("Connected to server.");
        } catch (IOException e) {
            this.isConnected = false;
            System.err.println("Connection failed: " + e.getMessage());
        }
    }


    private void ensureConnection() {
        if (!isConnected || socket == null || socket.isClosed()) {
            System.out.println("Reconnecting...");
            connect();
        }
    }
    private String loggedInUser;

    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
    }

    public String getLoggedInUser() {
        return this.loggedInUser;
    }



//פונקציה זו יוצרת בקשת JSON ושולחת אותה לשרת
    public Map<String, Object> sendRequest(String action, Object... params) {
        ensureConnection();
        if (!isConnected) {
            System.err.println("Cannot send request, not connected.");
            return null;
        }

        try {
            Object body = buildBody(action, params);

            Map<String, Object> requestMap = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            headers.put("action", action);

            requestMap.put("headers", headers);
            requestMap.put("body", body != null ? body : new HashMap<>());

            // Convert request to JSON and send
            String jsonRequest = gson.toJson(requestMap);
            writer.println(jsonRequest);
            System.out.println("Sent: " + jsonRequest);

            // Read and parse response (as a generic JSON object)
            String jsonResponse = reader.readLine();
            if (jsonResponse != null) {
                Map<String, Object> responseMap = gson.fromJson(jsonResponse, Map.class);
                System.out.println(" Recieved: " + gson.toJson(responseMap));
                return responseMap;
            }
        } catch (IOException e) {
            isConnected = false;
            System.err.println(" Error communicating with server: " + e.getMessage());
        }
        return null;
    }


    // Method to build request body dynamically
    private Object buildBody(String action, Object... params) {
        if (params == null || params.length == 0) {
            System.out.println(" ERROR: No parameters provided for action: " + action);
            return new HashMap<>(); // מחזיר מפה ריקה כדי למנוע שליחה לא נכונה
        }

        Map<String, Object> body = new HashMap<>();


        switch (action.toLowerCase()) {
            case "movie/add":
                if (params.length >= 6) {
                    body.put("title", params[0]);
                    body.put("genre", params[1]);
                    body.put("director", params[2]);
                    body.put("actors", params[3]);
                    body.put("releaseYear", params[4]);
                    body.put("description", params[5]);
                }
                break;

            case "movie/update":
                if (params.length >= 7) {
                    body.put("id", params[0]);
                    body.put("title", params[1]);
                    body.put("genre", params[2]);
                    body.put("director", params[3]);
                    body.put("actors", ((List<String>) params[4]).toArray(new String[0]));
                    body.put("releaseYear", Integer.parseInt(params[5].toString()));
                    body.put("description", params[6].toString());
                }
                break;

            case "movie/search":
                if (params.length >= 1) {
                    Object queryParam = params[0];
                    if (queryParam instanceof Map) {
                        queryParam = ((Map<?, ?>) queryParam).get("query");
                    }
                    return Map.of("query", queryParam);
                }
                break;

            case "movie/searchbytype":
                if (params.length >= 2) {
                    body.put("query", params[0]);
                    body.put("type", params[1]);
                    System.out.println("Building request body: " + body);
                    return body;
                } else {
                    System.out.println(" ERROR: Not enough parameters for searchbytype request.");
                }
                break;



            case "admin/remove":


            case "admin/get":
                if (params.length >= 1 && params[0] instanceof String) {
                    body.put("username", params[0]);
                    System.out.println(" Built admin/get request body: " + body);
                    return new HashMap<>(body);
                } else {
                    System.out.println("ERROR: Missing username parameter for admin/get request.");
                    return new HashMap<>();
                }


            case "admin/search":
                if (params.length >= 1) {
                    Object queryParam = params[0];
                    if (queryParam instanceof Map) {
                        queryParam = ((Map<?, ?>) queryParam).get("query");
                    }
                    return Map.of("query", queryParam);
                }
                break;


            case "movie/remove":

            case "movie/get":
                if (params.length >= 1) {
                    return params[0];
                }
                break;


            case "admin/add":
                if (params.length >= 3) {
                    body.put("name", params[0]);
                    body.put("username", params[1]);
                    body.put("password", params[2]);
                    System.out.println(" Built admin/add request body: " + body);
                } else {
                    System.out.println(" Not enough parameters for admin/add request.");
                };

            break;

            case "admin/getall":

            case "movie/getall":
                break;


            default:
                System.err.println("Unknown action: " + action);
                return null;
        }

        return body;
    }


}

