package server;

public class ServerDriver {
    public static void main(String[] args) {
        Server server = new Server(12345);
        new Thread(server).start();
    }
}
