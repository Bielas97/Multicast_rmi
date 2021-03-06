
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class, which handles incoming TCP connections from multiple clients.
 */
public class TCPConnectionHandler extends Thread {

    static final int PORT = 5000;

    public void run() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        while (FailoverHandler.maintenance)
        if (FailoverHandler.respond) {
            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {

                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                // new thread for a client
                new Connection(socket).start();

            }
        }
    }
}
