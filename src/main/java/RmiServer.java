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

public class RmiServer extends Thread{
    private static String MULTICAST_ADDRESS = "224.3.3.1";
    private static final int PORT = 4320;

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(7000);
            RemoteInterfaceImpl server = new RemoteInterfaceImpl(0);
            Naming.rebind("rmi://localhost:7000/siemka", server);
            System.out.println("Server ready to use...");

            RemoteOperations rmo = new RemoteOperations(new SendReceiveConnection());
           // System.out.println(rmo.getAllAlbums().get(0).getDescr());
            //System.out.println(rmo.getAllUsers());
            System.out.println(rmo.updateSong("Big in Japan","Big in Chia"));


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            MulticastSocket socket = null;
            try {

                socket = new MulticastSocket(PORT + 1);  // create socket and bind it
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                socket.joinGroup(group);
                byte[] buffer = new byte[256];
                String message = scanner.nextLine();
                buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
                byte[] incomingData = new byte[1024];


                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length, group, PORT + 1);
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
    }
}
