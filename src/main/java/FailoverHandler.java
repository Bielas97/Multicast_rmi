import java.io.IOException;
import java.net.*;
import java.text.Collator;
import java.util.*;

/**
 * Arbiter class, which makes inner multicast connection between running database servers and
 * automatically chooses one to respond to the RMI server.
 *
 */
public class FailoverHandler implements Runnable {
    public static boolean respond = false;
    public static boolean maintenance = false;

    public FailoverHandler() {
    }


    @Override
    public void run() {
        int port = 4330;
        String address = "224.3.3.0";
        MulticastSocket socket;
        InetAddress group = null;
        while (true) {

            try {
                System.out.println("Chcecking if there is a main server");
                group = InetAddress.getByName(address);
                socket = new MulticastSocket(port + 1);
                socket.joinGroup(group);

                socket.setSoTimeout(1000);
                listen(group, socket);
                socket.setSoTimeout(0);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                System.out.println("No main server active. Failover procedure initializing...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Creating new ID...");
            maintenance = true;
            byte[] array = new byte[7]; //nowe ID, tworzenie listy ID
            new Random().nextBytes(array);
            String instanceID = UUID.randomUUID().toString();
            System.out.println(instanceID);
            List<String> IDs = new ArrayList<>();
            IDs.add(instanceID);

            String s;
            MulticastSocket sock = null;
            try {
                sock = new MulticastSocket(port);
                sock.joinGroup(group);
                byte[] buffer;
                DatagramPacket packet;

                System.out.println("sending and receiving ID to other servers...");
                long endTime = System.currentTimeMillis() + 3000;
                while (System.currentTimeMillis() < endTime) {
                    buffer = new byte[256];
                    buffer = instanceID.getBytes();
                    packet = new DatagramPacket(buffer, buffer.length, group, port);
                    sock.send(packet);

                    packet = new DatagramPacket(buffer, buffer.length, group, port);
                    sock.setSoTimeout(200);
                    sock.receive(packet);
                    s = new String(packet.getData(), 0, packet.getLength());
                    // System.out.println(s);

                    if (!IDs.contains(s)) IDs.add(s);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            IDs.sort(Collator.getInstance());
            if (instanceID.equals(IDs.get(0))) {
                respond = true;
            }
            maintenance = false;
            if (respond) {
                System.out.println("I am the sender");
                while (true) {

                    byte[] buffer = "listening".getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port + 1);
                    // System.out.println("sd");
                    try {
                        sock.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * Method, in which other servers listen to the sender.
     * @param group multicast group
     * @param socket multicast socket
     */
    private void listen(InetAddress group, MulticastSocket socket) {
        byte[] buffer = new byte[256];
        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 4331);
                socket.receive(packet);
                String s = new String(packet.getData(), 0, packet.getLength());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            System.out.println("No main server active. Failover procedure initializing...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
