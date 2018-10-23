import com.assignmentRmiServer.Message;
import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBOperations {
    public DBOperations() {

    }
    public synchronized Message insertSong(String title, String albumname) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM SONGS WHERE TITLE = '" + title + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "insertionsong|exists";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO SONGS (TITLE,ID_ALBUM) VALUES " +
                    "('" + title + "'," +
                    "(SELECT ID_ALBUM FROM ALBUM WHERE ALBUM.NAME='"+albumname+"'));");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionsong|ok";
                return m;
            } else {
                m.msg = "insertionsong|bad";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    public synchronized Message insertUser(String user, String pass, String type) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM USERS WHERE USERNAME = '" + user + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "insertionuser|userexists";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO USERS (USERNAME,PASSWORD,TYPE ) " +
                    "VALUES ('" + user + "','" + pass + "','" + type + "');");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionuser|ok";
                return m;
            } else {
                m.msg = "insertionuser|bad";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message insertAlbum(String name, String genre, String descr, String artist) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM ALBUM WHERE NAME = '" + name + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "insertionalb| exists";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO ALBUM (NAME,GENRE,DESCR,ID_ARTIST) VALUES " +
                    "('" + name + "','" + genre+ "','" +descr+ "'," +
                    "(SELECT ID_ARTIST FROM ARTISTS WHERE ARTISTS.NAME='"+artist+"') );");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionalb|ok";
                return m;
            } else {
                m.msg = "insertionalb|bad";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message insertArtist(String name, String bio) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM ARTISTS WHERE NAME = '" + name + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "insertionart|exists";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO ARTISTS (NAME,BIO) VALUES " +
                    "('" + name + "','" + bio+"');");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionart|ok";
                return m;
            } else {
                m.msg = "insertionart|bad";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message deleteUser(String ids, String who, String what) {
        Message m = new Message();
        PreparedStatement stmt;
        ResultSet rs = null;
        int id = Integer.parseInt(ids);
        try {

            stmt = MulticastServer.conn.prepareStatement("DELETE FROM "+who+" WHERE "+what+" = '" + id + "';");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();


        }
        m.msg = "deletion"+who.toLowerCase()+"|ok";
        return m;
    }

    public synchronized String authenticateUser(String user, String pass) throws SQLException {
        Statement stmt;
        ResultSet rs = null;

        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM USERS where '" + user + "' = USERNAME AND '"
                    + pass + "' = PASSWORD");

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return "authentication|ok";
                } else {
                    return "authentication|bad";
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "authentication|bad";
    }

    public Message getAllArtists() {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

        rs = stmt.executeQuery("SELECT * FROM ARTISTS");
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String bio = rs.getString(3);
            m.artistList.add(new Artist(id, name, bio));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public Message getAllUsers() {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

            rs = stmt.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {

                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                String type = rs.getString(4);
                m.userList.add(new User(id, username,password,type));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public Message getAllAlbums() {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

            rs = stmt.executeQuery("SELECT * FROM ALBUM");
            while (rs.next()) {

                int id = rs.getInt(1);
                String name = rs.getString(2);
                String genre = rs.getString(3);
                String descr = rs.getString(4);
                int id_art = rs.getInt(5);
                m.albumList.add(new Album(id, name,genre,descr,id_art));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }


    public synchronized Message getAllSongs() {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

            rs = stmt.executeQuery("SELECT * FROM SONGS");
            while (rs.next()) {

                int id = rs.getInt(1);
                int id_alb = rs.getInt(2);
                String title = rs.getString(3);

                m.songList.add(new Song(id,id_alb,title));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message updateRecord(String replacement, String ids, String table, String column, String repColumn) {
        Message m = new Message();
        PreparedStatement stmt;
        ResultSet rs = null;

        try {
            stmt = MulticastServer.conn.prepareStatement("update "+table+" set "+repColumn+" = '"+replacement+"' where "+column+" = '"+ids+"';");
            stmt.executeUpdate();
            System.out.println(stmt);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        m.msg="update|ok";
        return m;
    }


    public synchronized Message searchSongsByArtist(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

            rs = stmt.executeQuery("SELECT SONGS.TITLE,SONGS.ID_SONG,SONGS.ID_ALBUM " +
                    "FROM SONGS,ALBUM,ARTISTS " +
                    "WHERE ARTISTS.ID_ARTIST = ALBUM.ID_ARTIST " +
                    "AND ALBUM.ID_ALBUM = SONGS.ID_ALBUM " +
                    "AND ARTISTS.NAME LIKE ('%"+name+"%')");
            while (rs.next()) {

                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);

                m.songList.add(new Song(id,id_alb,title));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message searchSongsByAlbum(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT SONGS.TITLE,SONGS.ID_SONG,SONGS.ID_ALBUM " +
                    "FROM SONGS,ALBUM " +
                    "WHERE ALBUM.ID_ALBUM = SONGS.ID_ALBUM " +
                    "AND ALBUM.NAME LIKE ('%"+name+"%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id,id_alb,title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message searchSongsByGenre(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT SONGS.TITLE,SONGS.ID_SONG,SONGS.ID_ALBUM " +
                    "FROM SONGS,ALBUM " +
                    "WHERE ALBUM.ID_ALBUM = SONGS.ID_ALBUM " +
                    "AND ALBUM.GENRE LIKE ('%"+name+"%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id,id_alb,title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message searchSongsByName(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT TITLE,ID_SONG,ID_ALBUM " +
                    "FROM SONGS " +
                    "WHERE TITLE LIKE ('%"+name+"%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id,id_alb,title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public synchronized Message checkIfFileCorresponds(String filename) {
        Message m = new Message();
        filename = filename.substring(0,filename.length()-4);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT TITLE FROM SONGS WHERE NAME ='"+filename+"';");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "checkfile|ok";
               return m;
            }else{
                m.msg = "checkfile|notexists";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.msg = "checkfile|servererror";
        return m;
    }

    public synchronized Message insertSharedUser(String user, String title) {
        Message m = new Message();
        PreparedStatement stmt;
        title = title.substring(0,title.length()-4);
        try {
            stmt = MulticastServer.conn.prepareStatement("INSERT INTO USERFILES(USERNAME,TITLE) VALUES ('"+user+"','"+title+"')");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionsu|ok";
                return m;
            } else {
                m.msg = "insertionsu|bad";
                return m;
            }
        } catch (SQLException e) {
            m.msg = "insertionsu|bad";
            return m;
        }
    }

    public synchronized Message checkIfUserCanDownload(String username, String title) {
        Message m = new Message();
        title = title.substring(0,title.length()-4);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM USERFILES WHERE USERNAME ='"+username+"' AND TITLE = '"+title+"';");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "checkuser|ok";
                return m;
            }else{
                m.msg = "checkuser|notexists";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.msg = "checkuser|servererror";
        return m;
    }

    public synchronized Message insertFavSong(String user, String song) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*) FROM FAVORITES,users,songs WHERE '"+user+"'=users.username " +
                    "and '"+song+"'=songs.title and songs.id_song = favorites.id_song and favorites.id_user = users.id_user");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "insertionfav|exists";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO FAVORITES (ID_USER,ID_SONG) VALUES (" +
                    "(SELECT ID_USER FROM USERS WHERE USERS.USERNAME='"+user+"')," +
                    "(SELECT ID_SONG FROM SONGS WHERE SONGS.TITLE='"+song+"'));");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "insertionfav|ok";
                return m;
            } else {
                m.msg = "insertionfav|bad";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
}
