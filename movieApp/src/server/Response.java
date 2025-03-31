package server;

import com.google.gson.Gson;

public class Response {
    private String status;
    private Object body;


    public Response(String status, Object body) {
        this.status = status;
        this.body = body;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }


    public static Response fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Response.class);
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}