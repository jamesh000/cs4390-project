import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

             ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());) {
            while (true) {
                Message clientMessage = (Message) inFromClient.readObject();

                System.out.println("Received client message object " + clientMessage);

                String result = process(clientMessage.data);

                outToClient.writeObject(new Message("", false, result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String process(String input) {
        return input.toUpperCase();
    }
}
