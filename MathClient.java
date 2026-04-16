import java.io.*;
import java.net.*;

class MathClient {

    public static void main(String argv[]) throws Exception {
        System.out.println("Client is running: ");

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

        // Send name to server
        outToServer.writeObject(new Message("Test", false, ""));

        // Wait for Ack
        inFromServer.readObject();

        // Input loop
        while (true) {
            // Read user input
            String input = inFromUser.readLine();

            // Check for exit command
            if (input.equals("/exit")) {
                outToServer.writeObject(new Message("", true, ""));
                break;
            }

            // Send the input to the server
            outToServer.writeObject(new Message("", false, input));

            // Read response and print it out
            Message serverResponse = (Message) inFromServer.readObject();
            System.out.println("FROM SERVER: " + serverResponse.data);
        }

        clientSocket.close();
    }
}

