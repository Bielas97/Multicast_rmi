/*
package com.assignmentRmiServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.3.3.1";
    private int PORT = 4320;

    public static void main(String[] args) throws IOException {
        RemoteOperations rmo = new RemoteOperations(new MulticastClient());
        //System.out.println(rmo.insertUser("razll","dwa","user"));
        //System.out.println(rmo.deleteUser("6"));
        //System.out.println(rmo.searchSongByArtist("ril").get(1).getTitle());
        //System.out.println(rmo.searchSongByAlbum("bi").get(0).getTitle());
        //System.out.println(rmo.searchSongByGenre("Rock").get(0).getTitle());
        //System.out.println(rmo.searchSongByName("in").get(0).getTitle());
        //System.out.println(rmo.checkIfUserCanDownload("dupnij","Wub dub.mp3"));
        //System.out.println(rmo.shareSong("kuba","Wub dub.mp3"));
        //System.out.println(rmo.writeDescriptionToAlbum("Musztarda","Czwarty album znanego zespołu..."));


        System.out.println(rmo.insertUser("pawel", "1234", "admin"));


        System.out.println(rmo.getAllAlbums().get(0).getDescr());
        //System.out.println(rmo.insertFavoriteSong("blancior","Medium in Japan"));
        //TCPConnectionHandlerClient tcphandle = new TCPConnectionHandlerClient();

         //   tcphandle.sendFile("Scenariusz.mp3");

        //System.out.println(rmo.insertAlbum("Debil2","Rock","Czwarty album znanego zespołu :)","Kabanos"));
        //System.out.println(rmo.insertSong("RUŻOWA PANTERA","Debil"));
        */
/*com.assignmentRmiServer.MulticastClient client = new com.assignmentRmiServer.MulticastClient();
        client.start();*//*

    }

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



*/
