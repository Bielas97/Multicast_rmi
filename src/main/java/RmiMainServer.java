/**
 * main class for java rmi server
 */
public class RmiMainServer {
    private static String MULTICAST_ADDRESS = "224.3.3.1";
    private static final int PORT = 4320;

    public static void main(String[] args) {
        RmiServer rmiServerPrimary = new RmiServer(MULTICAST_ADDRESS, PORT);
        RmiServer rmiServerSecondary;
        if(args[0].equalsIgnoreCase("primary")){
            Thread t1 = new Thread(rmiServerPrimary::startServer);
            t1.start();
        }
        else {
            rmiServerSecondary = rmiServerPrimary;
            Thread t2 = new Thread(rmiServerSecondary::startServer);
            t2.start();
        }
    }
}
