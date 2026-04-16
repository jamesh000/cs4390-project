import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private String clientName;
    private ServerLogger logService;
    private ConnectionHandler connectionHandler;

    public ClientHandler(Socket socket, ServerLogger logService, ConnectionHandler connectionHandler) {
        this.socket = socket;
        this.logService = logService;
        this.connectionHandler = connectionHandler;
    }

    public void run() {
        try (ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

             ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());) {

            // Receive client name before proceeding
            Message nameMessage = (Message) inFromClient.readObject();
            clientName = nameMessage.name;
            logService.log(LocalDateTime.now() + ": Client " + clientName + " connected");

            // Send Ack of name
            outToClient.writeObject(new Message("", false, ""));

            // add this connection into the handler
            connectionHandler.addConnection(clientName);

            // Loop until close is requested (or error)
            while (true) {
                // Read in client message
                Message clientMessage = (Message) inFromClient.readObject();

                // Check for close request
                if (clientMessage.close) {
                    closeConnection();
                    break;
                }

                // Process client input
                String result = process(clientMessage.data);

                // Send back results
                outToClient.writeObject(new Message("", false, result));
            }
        } catch (Exception e) {
            // Should be better handling
            closeConnection();
        }
    }

    private void closeConnection() {
        LocalDateTime dcTime = LocalDateTime.now();
        LocalDateTime connTime = connectionHandler.disconnect(clientName);
        Duration timeConnected = Duration.between(connTime, dcTime);

        logService.log(dcTime + ": Client " + clientName + " disconnected after " + timeConnected.getSeconds() + " seconds");
    }

    private String process(String input) {
        // Data is processed in the format as "integer operation integer"
        // The string won't be read correctly without all this terribleness! Too bad!
        try {
            // Remove whitespace
            input = input.replaceAll("\\s+","");

            // Naive string reading because Java sux
            int i = 0;
            char c;
            int sign;
            int num1 = 0;
            int num2 = 0;
            char operation;
            int result;

            sign = 1;

            if (input.charAt(i) == '-') {
                sign = -1;
                i++;
            }

            for (; Character.isDigit(c = input.charAt(i)); i++) {
                num1 *= 10;
                num1 += Character.getNumericValue(c);
            }

            num1 *= sign;

            operation = input.charAt(i);
            i++;

            sign = 1;

            if (input.charAt(i) == '-') {
                sign = -1;
                i++;
            }

            for (; i < input.length() && Character.isDigit(c = input.charAt(i)); i++) {
                num2 *= 10;
                num2 += Character.getNumericValue(c);
            }

            num2 *= sign;

            switch (operation) {
                case '*':
                    result = num1 * num2;
                    break;
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '/':
                    result = num1 / num2;
                    break;
                default:
                    throw new Exception();
            }

            return Integer.toString(result);
        } catch (Exception e) {
            logService.log(LocalDateTime.now() + ": Bad format from client " + clientName + ": " + input);
            return "Bad format!";
        }

    }
}
