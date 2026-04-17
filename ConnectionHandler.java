import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    // Holds client connection times
    private final ConcurrentHashMap<String, LocalDateTime> connections = new ConcurrentHashMap<String, LocalDateTime>();

    // Add a connection time for a client
    public void addConnection(String name) {
        connections.put(name, LocalDateTime.now());
    }

    // Remove a client and return its connection time
    public LocalDateTime disconnect(String name) {
        LocalDateTime connectionTime = connections.get(name);
        connections.remove(name);

        return connectionTime;
    }
}
