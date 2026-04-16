import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private String clientName;
    private ClientLogger logService;

    public ClientHandler(Socket socket, ClientLogger logService) {
        this.socket = socket;
        this.logService = logService;
    }

    public void run() {
        try (ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

             ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());) {

            // Receive client name before proceeding
            Message nameMessage = (Message) inFromClient.readObject();
            clientName = nameMessage.name;
            logService.log("Client " + clientName + " connected");

            // Send Ack of name
            outToClient.writeObject(new Message("", false, ""));

            // Loop until close is requested (or error)
            while (true) {
                // Read in client message
                Message clientMessage = (Message) inFromClient.readObject();

                // Check for close request
                if (clientMessage.close) {
                    logService.log("Client " + clientName + " DCed");
                    break;
                }

                // Process client input
                String result = process(clientMessage.data);

                // Send back results
                outToClient.writeObject(new Message("", false, result));
            }
        } catch (Exception e) {
            // Should be better handling
            logService.log(e.getMessage());
        }
    }

    private String process(String input) {
        // Placeholder operation
        return input.toUpperCase();
    }
}
