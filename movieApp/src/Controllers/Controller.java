package Controllers;

import server.Request;
import server.Response;

public interface Controller {
    Response handleRequest(Request request);
}

