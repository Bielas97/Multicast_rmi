import com.assignmentRmiServer.RemoteInterfaceImpl;
import com.assignmentRmiServer.RemoteOperations;
import com.assignmentRmiServer.SendReceiveConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * creates Rmi server
 */
public class RmiServer /*extends Thread*/{
    /*private static String MULTICAST_ADDRESS = "224.3.3.1";
    private static final int PORT = 4320;*/
    private String multiCastAddress;
    private int port;

    public RmiServer(String multiCastAddress, int port) {
        this.multiCastAddress = multiCastAddress;
        this.port = port;
    }

    public void startServer(){
        try {
            LocateRegistry.createRegistry(7000);
            RemoteInterfaceImpl server = new RemoteInterfaceImpl(0);
            Naming.rebind("rmi://localhost:7000/siemka", server);
            System.out.println("Server ready to use...");

            RemoteOperations rmo = new RemoteOperations(new SendReceiveConnection());
            //System.out.println(rmo.getAllAlbums().get(0).getDescr());
            System.out.println(rmo.insertSong("Big in Japans","Debil"));


            Scanner scanner = new Scanner(System.in);
            while (true) {
                MulticastSocket socket = null;
                try {

                    socket = new MulticastSocket(port + 1);  // create socket and bind it
                    InetAddress group = InetAddress.getByName(multiCastAddress);
                    socket.joinGroup(group);
                    byte[] buffer = new byte[256];
                    String message = scanner.nextLine();
                    buffer = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);
                    socket.send(packet);
                    byte[] incomingData = new byte[1024];


                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length, group, port + 1);
                    socket.receive(incomingPacket);
                    byte[] data = incomingPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        Object o = (Object) is.readObject();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    System.out.println("server not responding... try again");
                } finally {
                    socket.close();
                }
            }


        } catch (RemoteException e) {
            System.out.println("check if server is Alive...");
            try {
                Thread.sleep(5000);
                startServer();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
