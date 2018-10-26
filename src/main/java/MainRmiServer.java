public class MainRmiServer {
    private static String MULTICAST_ADDRESS = "224.3.3.1";
    private static final int PORT = 4320;

    public static void main(String[] args) {
        RmiServer rmiServer = new RmiServer(MULTICAST_ADDRESS, PORT);
        Thread t1 = new Thread(rmiServer::startServer);
        t1.start();
    }
}
