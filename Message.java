import java.io.Serializable;

public class Message implements Serializable {
    String name;
    boolean close;
    String data;

    public Message(String name, boolean close, String data) {
        this.name = name;
        this.close = close;
        this.data = data;
    }
}
