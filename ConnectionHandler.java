import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    private final ConcurrentHashMap<String, LocalDateTime> connections = new ConcurrentHashMap<String, LocalDateTime>();

    public void addConnection(String name) {
        connections.put(name, LocalDateTime.now());
    }

    public LocalDateTime disconnect(String name) {
        LocalDateTime connectionTime = connections.get(name);
        connections.remove(name);

        return connectionTime;
    }
}
