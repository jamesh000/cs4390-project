import java.io.Serializable;

public class Message implements Serializable {
    // Used for client to give name
    String name;

    // Used for client to request disconnection
    boolean close;

    // Used for string requests from client and responses from server
    String data;

    // Basic constructor
    public Message(String name, boolean close, String data) {
        this.name = name;
        this.close = close;
        this.data = data;
    }
}
