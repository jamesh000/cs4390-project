import java.net.*;
import java.util.concurrent.*;

class MathServer {

    public static void main(String argv[]) throws Exception {
        // Take port argument if given
        int port;
        if (argv.length < 1) {
            port = 6789;
        } else {
            port = Integer.parseInt(argv[0]);
        }

        // Server connection socket
        ServerSocket welcomeSocket = new ServerSocket(port);

        // Thread pool for client handling threads
        ExecutorService executor = Executors.newCachedThreadPool();

        // Logger and connection time tracker
        ServerLogger logService = new ServerLogger();
        ConnectionHandler connectionHandler = new ConnectionHandler();

        logService.start();

        System.out.println("Server is UP and running!");

        // Loop forever accepting connections
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            executor.execute(new ClientHandler(connectionSocket, logService, connectionHandler));
        }
    }
}
