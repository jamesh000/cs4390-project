import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientLogger {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final Thread thread;

    public ClientLogger() {
        thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String logMessage = queue.take();

                    writeLog(logMessage);
                }
            } catch (InterruptedException e) {}
        });
    }

    public void start() {
        thread.start();
    }

    public void log(String logMessage) {
        queue.offer(logMessage);
    }

    private void writeLog(String msg) {
        System.out.println(msg);
    }
}
