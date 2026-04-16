import java.io.*;
import java.net.*;
import java.util.concurrent.*;

class MathServer {

    public static void main(String argv[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(argv[0]));
        ExecutorService executor = Executors.newCachedThreadPool();
        ClientLogger logService = new ClientLogger();

        logService.start();

        System.out.println("Server is UP and running!");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            executor.execute(new ClientHandler(connectionSocket, logService));
        }
    }
}
