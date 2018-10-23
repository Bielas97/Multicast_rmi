import com.assignmentRmiServer.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Task implements Runnable {
    public String message;
    public Message returnMessage;
    public DBOperations dbOperations;
    public MulticastSocket socket;

    public Task(String message) throws IOException {
        this.message = message;
        this.socket = new MulticastSocket(MulticastServer.PORT);
        this.dbOperations = new DBOperations();
    }

    public void run() {
        String[] tokens = this.message.split("[|]");

        switch (tokens[0]) {
            case "insert":
                switch (tokens[1]) {
                    case "user":
                        returnMessage = dbOperations.insertUser(tokens[2], tokens[3], tokens[4]);
                        break;
                    case "artist":
                        returnMessage = dbOperations.insertArtist(tokens[2], tokens[3]);
                        break;
                    case "album":
                        returnMessage = dbOperations.insertAlbum(tokens[2], tokens[3], tokens[5], tokens[4]);
                        break;
                    case "song":
                        returnMessage = dbOperations.insertSong(tokens[2], tokens[3]);
                        break;
                    case "share":
                        returnMessage = dbOperations.insertSharedUser(tokens[2], tokens[3]);
                        break;
                    case "fav":
                        returnMessage = dbOperations.insertFavSong(tokens[2], tokens[3]);
                        break;
                }
                break;
            case "delete":
                switch (tokens[1]) {
                    case "user":
                        returnMessage = dbOperations.deleteUser(tokens[2], "USERS", "ID_USER");
                        break;
                    case "artist":
                        returnMessage = dbOperations.deleteUser(tokens[2], "ARTISTS", "ID_ARTIST");
                        break;
                    case "album":
                        returnMessage = dbOperations.deleteUser(tokens[2], "ALBUM", "ID_ALBUM");
                        break;
                    case "song":
                        returnMessage = dbOperations.deleteUser(tokens[2], "SONGS", "ID_SONG");
                        break;
                }
                break;
            case "getall":
                switch (tokens[1]) {
                    case "artists":
                        returnMessage = dbOperations.getAllArtists();
                        break;
                    case "users":
                        returnMessage = dbOperations.getAllUsers();
                        break;
                    case "albums":
                        returnMessage = dbOperations.getAllAlbums();
                        break;
                    case "songs":
                        returnMessage = dbOperations.getAllSongs();
                        break;
                }
                break;
            case "update":
                switch (tokens[1]) {
                    case "song":
                        returnMessage = dbOperations.updateRecord(tokens[2], tokens[3], "SONGS", "TITLE", "TITLE");
                        break;
                    case "album":
                        returnMessage = dbOperations.updateRecord(tokens[2], tokens[3], "ALBUM", "NAME", "NAME");
                        break;
                    case "artist":
                        returnMessage = dbOperations.updateRecord(tokens[2], tokens[3], "ARTISTS", "NAME", "NAME");
                        break;
                    case "descalbum":
                        returnMessage = dbOperations.updateRecord(tokens[2], tokens[3],"ALBUM","NAME","DESCR");
                        break;
                    case "role":
                        returnMessage = dbOperations.updateRecord(tokens[2], tokens[3],"USERS","USERNAME","TYPE");
                        break;
                }
                break;
            case "search":
                switch (tokens[1]) {
                    case "byartist":
                        returnMessage = dbOperations.searchSongsByArtist(tokens[2]);
                        break;
                    case "byalbum":
                        returnMessage = dbOperations.searchSongsByAlbum(tokens[2]);
                        break;
                    case "bygenre":
                        returnMessage = dbOperations.searchSongsByGenre(tokens[2]);
                        break;
                    case "byname":
                        returnMessage = dbOperations.searchSongsByName(tokens[2]);
                        break;
                }
            case "check": {
                switch (tokens[1]) {
                    case "file":
                        returnMessage = dbOperations.checkIfFileCorresponds(tokens[2]);
                        break;
                    case "user":
                        returnMessage = dbOperations.checkIfUserCanDownload(tokens[2],tokens[3]);
                        break;
                }
            }
            break;
            default:
                returnMessage.msg = "ERROR - WRONG REQUEST SYNTAX";
                break;
        }


        InetAddress group = null;
        while (FailoverHandler.maintenance) {
            System.out.println("xd");
        }
        if (FailoverHandler.respond) {
            try {
                group = InetAddress.getByName(MulticastServer.MULTICAST_ADDRESS);
                socket.joinGroup(group);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(returnMessage);
                byte[] buffer = baos.toByteArray();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MulticastServer.PORT + 1);
                System.out.println(returnMessage.msg + " sysout");
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
