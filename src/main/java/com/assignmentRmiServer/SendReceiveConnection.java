package com.assignmentRmiServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendReceiveConnection {
    private static String MULTICAST_ADDRESS = "224.3.3.1";
    private static final int PORT = 4320;

    public Message SendReceive(String msg) {

        MulticastSocket socket = null;
        try {

            socket = new MulticastSocket(PORT + 1);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            byte[] buffer;

            buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
            byte[] incomingData = new byte[1024];


            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length, group, PORT + 1);
            socket.receive(incomingPacket);
            byte[] data = incomingPacket.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);

            return (Message) is.readObject();



        } catch (IOException e) {
            System.out.println("server not responding... try again");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }

        return new Message();
    }
}
