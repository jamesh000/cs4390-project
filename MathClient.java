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

        while (true) {
            String input = inFromUser.readLine();

            outToServer.writeObject(new Message("", false, input));

            Message serverResponse = (Message) inFromServer.readObject();

            System.out.println("FROM SERVER: " + serverResponse.data);
        }
    }
}

