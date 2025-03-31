package server;
import Controllers.Controller;
import Controllers.InitController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleRequest implements Runnable {
    private Socket clientSocket;

    public HandleRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String jsonRequest;

            while ((jsonRequest = reader.readLine()) != null) {
                Request request = Request.fromJson(jsonRequest);
                System.out.println(request.toJson().toString());

                Controller controller = InitController.getController(request.getHeaders().get("action"));
                if (controller != null) {
                    Response response = controller.handleRequest(request);
                    writer.println(response.toJson());
                } else {
                    writer.println(new Response("error", "Invalid action").toJson());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
