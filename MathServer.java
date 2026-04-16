import java.net.*;
import java.util.concurrent.*;

class MathServer {

    public static void main(String argv[]) throws Exception {
        int port;
        if (argv.length < 1) {
            port = 6789;
        } else {
            port = Integer.parseInt(argv[0]);
        }

        ServerSocket welcomeSocket = new ServerSocket(port);
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerLogger logService = new ServerLogger();
        ConnectionHandler connectionHandler = new ConnectionHandler();

        logService.start();

        System.out.println("Server is UP and running!");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            executor.execute(new ClientHandler(connectionSocket, logService, connectionHandler));
        }
    }
}
