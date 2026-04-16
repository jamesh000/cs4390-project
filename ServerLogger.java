import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerLogger {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final Thread thread;

    public ServerLogger() {
        // Thread that receives logs from clientHandler threads and writes to the log
        thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String logMessage = queue.take();

                    writeLog(logMessage);
                }
            } catch (InterruptedException e) {}
        });
    }

    // Start the logger service
    public void start() {
        thread.start();
    }

    // Log a given string
    public void log(String logMessage) {
        queue.offer(logMessage);
    }

    // Function that handles the direct logging
    private void writeLog(String msg) {
        System.out.println(msg);
    }
}
