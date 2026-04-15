import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        DataOutputStream  outToClient =
                new DataOutputStream(socket.getOutputStream());
        ) {
            String clientSentence = inFromClient.readLine();

            System.out.println("Server received message!" + clientSentence);
            String capitalizedSentence = clientSentence.toUpperCase() + '\n';

            outToClient.writeBytes(capitalizedSentence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
