import org.h2.jdbc.JdbcSQLException;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MulticastServer {
    public static String MULTICAST_ADDRESS = "224.3.3.1";
    public static int PORT = 4320;
    private static int MAX_THREADS = 5;
    public static Connection conn;

    public MulticastServer() {

    }

    public static void main(String[] args) throws IOException{


        FailoverHandler fhandler = new FailoverHandler();
        Thread t = new Thread(fhandler);
        TCPConnectionHandler ch = new TCPConnectionHandler();
        ch.start();
        t.start();
        File f = new File("test/.././file.txt");
        String s = "jdbc:h2:file:" + f.getCanonicalPath().replace('\\', '/');
        s = s.substring(0, s.length() - 8) + "target/db0";
        handleDBConnection(s, -1);


        MulticastSocket socket = null;
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        try {
            socket = new MulticastSocket(PORT);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        String message;
        while (true) {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            message = new String(packet.getData(), 0, packet.getLength());
            System.out.println(message);

            Task task = new Task(message);
            //buffer = message.getBytes();
            pool.execute(task);
        }

    }

    private static void handleDBConnection(String s, int i) {
        s = s.substring(0, s.length() - 1);
        s += ++i;
        try {

            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(s, "root", "root");
            System.out.println(s);
        } catch (JdbcSQLException e) {
            handleDBConnection(s,i);
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}