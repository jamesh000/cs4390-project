import java.io.*;
import java.net.*;

class MathClient {

    public static void main(String argv[]) throws Exception {
        System.out.println("Client is running: ");

        String name;
        String serverAddr;
        int serverPort;

        if (argv.length < 1) {
            throw new IllegalArgumentException("Too few arguments");
        }

        // Set name of client, must be provided
        name = argv[0];

        // Set server address, default is localhost
        if (argv.length > 1) {
            serverAddr = argv[1];
        } else {
            serverAddr = "127.0.0.1";
        }

        // Set server port, default is 6789
        if (argv.length > 2) {
            serverPort = Integer.parseInt(argv[3]);
        } else {
            serverPort = 6789;
        }

        // Open connection to server
        Socket clientSocket = new Socket(serverAddr, serverPort);

        // Open user input
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        // Open serialized object streams
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

        // Send name to server
        outToServer.writeObject(new Message(name, false, ""));

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

        // Close socket and exit
        clientSocket.close();
    }
}

