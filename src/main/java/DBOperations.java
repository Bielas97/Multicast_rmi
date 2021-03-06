import com.assignmentRmiServer.Message;
import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

/**
 *Class for the operations with database
 */
public class DBOperations {
    public DBOperations() {

    }

    /**
     * Method, which inserts a song into the database.
     * @param title Title of the song
     * @param albumname Album, to which the song belongs (must exist already in database)
     * @return Returns object Message, which contains a return message for the client.
     */
    public synchronized Message insertSong(String title, String albumname) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        Statement stmt3;
        ResultSet rs2;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM SONGS WHERE TITLE = '" + title + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "Song " + title + " already exists in the database.";
                return m;
            }
            stmt3 = MulticastServer.conn.createStatement();
            rs2 =stmt3.executeQuery("select count(*) from album where name='"+albumname+"'");
            rs2.next();
            if (rs2.getInt(1) == 0) {
                m.msg = "Such album doesn't exist.";
                return m;
            }
            stmt = MulticastServer.conn.prepareStatement("INSERT INTO SONGS (TITLE,ID_ALBUM) VALUES " +
                    "('" + title + "'," +
                    "(SELECT ID_ALBUM FROM ALBUM WHERE ALBUM.NAME='" + albumname + "'));");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "Song successfully inserted.";
                return m;
            } else {
                m.msg = "Problem inserting " + title + ". Server side error";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which inserts a new user into the database.
     * @param user new username
     * @param pass password
     * @param type type of the user (admin,publisher,user)
     * @return Returns object Message, which contains a return message for the client.
     */
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
                m.msg = "User " + user + " already exists in the database.";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO USERS (USERNAME,PASSWORD,TYPE ) " +
                    "VALUES ('" + user + "','" + pass + "','" + type + "');");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "User successfully inserted.";
                return m;
            } else {
                m.msg = "Problem inserting " + user + ". Server side error";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which inserts a new album into the database.
     * @param name album name
     * @param genre album genre
     * @param descr album description
     * @param artist Name of the artist, which released the album (must already exist in database
     * @return Returns object Message, which contains a return message for the client.
     */
    public synchronized Message insertAlbum(String name, String genre, String descr, String artist) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        Statement stmt3;
        ResultSet rs = null;
        ResultSet rs2;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM ALBUM WHERE NAME = '" + name + "'");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "Album " + name + " already exists in the database.";
                return m;
            }
            stmt3 = MulticastServer.conn.createStatement();
            rs2 =stmt3.executeQuery("select count(*) from artists where name='"+artist+"'");
            rs2.next();
            if (rs2.getInt(1) == 0) {
                m.msg = "Such artist doesn't exist.";
                return m;
            }
            stmt = MulticastServer.conn.prepareStatement("INSERT INTO ALBUM (NAME,GENRE,DESCR,ID_ARTIST) VALUES " +
                    "('" + name + "','" + genre + "','" + descr + "'," +
                    "(SELECT ID_ARTIST FROM ARTISTS WHERE ARTISTS.NAME='" + artist + "') );");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "Album successfully inserted.";
                return m;
            } else {
                m.msg = "Problem inserting " + name + ". Server side error";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which inserts a new artist into the database.
     * @param name artist name
     * @param bio short description
     * @return Returns object Message, which contains a return message for the client.
     */
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
                m.msg = "Artist " + name + " already exists in the database.";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO ARTISTS (NAME,BIO) VALUES " +
                    "('" + name + "','" + bio + "');");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "Artist successfully inserted.";
                return m;
            } else {
                m.msg = "Problem inserting " + name + ". Server side error";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which deletes records in specific table, column.
     * @param ids name of deleted item
     * @param table
     * @param column
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message delete(String ids, String table, String column) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2;
        ResultSet rs;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*)FROM "+table+" WHERE "+column+" = '" + ids + "'");
            rs.next();
            if (rs.getInt(1) == 0) {
                m.msg = "This entity doesn't exist in database";
                return m;
            }
            stmt = MulticastServer.conn.prepareStatement("DELETE FROM " + table + " WHERE " + column + " = '" + ids + "';");
            stmt.executeUpdate();

        } catch (SQLException e) {
            m.msg = "Could not delete " + ids;
            return m;
        }
        m.msg = "Successfully deleted " + ids;
        return m;
    }

    /**
     * Method, which authenticates the user.
     * @param user
     * @param pass
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message authenticateUser(String user, String pass) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;

        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM USERS where '" + user + "' = USERNAME AND '"
                    + pass + "' = PASSWORD");

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    m.msg = "Authentication successful";
                    return m;
                } else {
                    m.msg = "Wrong username or password";
                    return m;
                }
            }
            stmt.close();
        } catch (SQLException e) {
            m.msg = "Error server side";
            return m;
        }
        return m;

    }

    /**
     * Method, which returns all artists from the database
     * @return Returns object Message, which contains a list of artists.
     */
    public synchronized Message getAllArtists() {
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
    /**
     * Method, which returns all users from the database
     * @return Returns object Message, which contains a list of users.
     */
    public synchronized Message getAllUsers() {
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
                m.userList.add(new User(id, username, password, type));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    /**
     * Method, which returns all albums from the database
     * @return Returns object Message, which contains a list of albums.
     */
    public synchronized Message getAllAlbums() {
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
                m.albumList.add(new Album(id, name, genre, descr, id_art));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    /**
     * Method, which returns all songs from the database
     * @return Returns object Message, which contains a list of songs.
     */
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

                m.songList.add(new Song(id, id_alb, title));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which updates records in database
     * @param replacement new value
     * @param ids value to identify updated record
     * @param table table, where value is updated
     * @param column column name for the query condition
     * @param repColumn column name where the data is replaced
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message updateRecord(String replacement, String ids, String table, String column, String repColumn) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2;
        ResultSet rs = null;

        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*) FROM " + table + " WHERE " + column + " ='" + ids + "';");
            System.out.println(stmt2);
            rs.next();
            if (rs.getInt(1) == 0) {
                m.msg = "No record with given data found.";
                return m;
            }
            stmt = MulticastServer.conn.prepareStatement("update " + table + " set " + repColumn + " = '" + replacement + "' where " + column + " = '" + ids + "';");
            stmt.executeUpdate();
            System.out.println(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            m.msg = "Error updating data";
            return m;
        }
        m.msg = "Data updated.";
        return m;
    }

    /**
     * Method, which searches songs created by artists with given phrase
     * @param name name of artist
     * @return Returns object Message, which contains list of songs by artists.
     */
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
                    "AND ARTISTS.NAME LIKE ('%" + name + "%')");
            while (rs.next()) {

                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);

                m.songList.add(new Song(id, id_alb, title));

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    /**
     * Method, which searches songs contained by albums with given phrase
     * @param name name of album
     * @return Returns object Message, which contains list of songs.
     */
    public synchronized Message searchSongsByAlbum(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT SONGS.TITLE,SONGS.ID_SONG,SONGS.ID_ALBUM " +
                    "FROM SONGS,ALBUM " +
                    "WHERE ALBUM.ID_ALBUM = SONGS.ID_ALBUM " +
                    "AND ALBUM.NAME LIKE ('%" + name + "%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id, id_alb, title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    /**
     * Method, which searches songs with given genre
     * @param name name of artist
     * @return Returns object Message, which contains list of songs.
     */
    public synchronized Message searchSongsByGenre(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT SONGS.TITLE,SONGS.ID_SONG,SONGS.ID_ALBUM " +
                    "FROM SONGS,ALBUM " +
                    "WHERE ALBUM.ID_ALBUM = SONGS.ID_ALBUM " +
                    "AND ALBUM.GENRE LIKE ('%" + name + "%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id, id_alb, title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
    /**
     * Method, which searches searches songs with given phrase
     * @param name name of artist
     * @return Returns object Message, which contains list of songs.
     */
    public synchronized Message searchSongsByName(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT TITLE,ID_SONG,ID_ALBUM " +
                    "FROM SONGS " +
                    "WHERE TITLE LIKE ('%" + name + "%')");
            while (rs.next()) {
                int id = rs.getInt(2);
                int id_alb = rs.getInt(3);
                String title = rs.getString(1);
                m.songList.add(new Song(id, id_alb, title));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method, which checks if the file to upload exists in database
     * @param filename
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message checkIfFileCorresponds(String filename) {
        Message m = new Message();
        filename = filename.substring(0, filename.length() - 4);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT TITLE FROM SONGS WHERE NAME ='" + filename + "';");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "ok";
                return m;
            } else {
                m.msg = "notexists";
                return m;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.msg = "servererror";
        return m;
    }

    /**
     * Method, which lets user download song shared by another user
     * @param user
     * @param title
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message insertSharedUser(String user, String title) {
        Message m = new Message();
        PreparedStatement stmt;
        title = title.substring(0, title.length() - 4);
        try {
            stmt = MulticastServer.conn.prepareStatement("INSERT INTO USERFILES(USERNAME,TITLE) VALUES ('" + user + "','" + title + "')");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "User + " + user + "can now download song: " + title;
                return m;
            } else {
                m.msg = "Wrong user or song title";
                return m;
            }
        } catch (SQLException e) {
            m.msg = "Error sharing music";
            return m;
        }
    }

    /**
     * Method, which checks whether the user can download given file
     * @param username
     * @param title
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message checkIfUserCanDownload(String username, String title) {
        Message m = new Message();
        title = title.substring(0, title.length() - 4);
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM USERFILES WHERE USERNAME ='" + username + "' AND TITLE = '" + title + "';");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "ok";
                return m;
            } else {
                m.msg = "no";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.msg = "error";
        return m;
    }

    /**
     * Method, which inserts a favorite song for given user
     * @param user
     * @param song
     * @return Returns object Message, which contains a message for the client.
     */
    public synchronized Message insertFavSong(String user, String song) {
        Message m = new Message();
        PreparedStatement stmt;
        Statement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt2 = MulticastServer.conn.createStatement();
            rs = stmt2.executeQuery("SELECT COUNT(*) FROM FAVORITES,users,songs WHERE '" + user + "'=users.username " +
                    "and '" + song + "'=songs.title and songs.id_song = favorites.id_song and favorites.id_user = users.id_user");
            rs.next();
            if (rs.getInt(1) == 1) {
                m.msg = "Song already in favorites.";
                return m;
            }

            stmt = MulticastServer.conn.prepareStatement("INSERT INTO FAVORITES (ID_USER,ID_SONG) VALUES (" +
                    "(SELECT ID_USER FROM USERS WHERE USERS.USERNAME='" + user + "')," +
                    "(SELECT ID_SONG FROM SONGS WHERE SONGS.TITLE='" + song + "'));");
            int i = stmt.executeUpdate();
            if (i > 0) {
                m.msg = "Song successfully inserted";
                return m;
            } else {
                m.msg = "Error inserting song.";
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * Method returning a list of users with given phrase
     * @param name
     * @return Returns object Message, which contains a list of users for the client.
     */
    public synchronized Message searchUsersByName(String name) {

        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();
            rs = stmt.executeQuery("SELECT ID_USER,USERNAME,PASSWORD,TYPE " +
                    "FROM USERS " +
                    "WHERE USERNAME LIKE ('%" + name + "%')");
            while (rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                String type = rs.getString(4);
                m.userList.add(new User(id, username, password, type));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;

    }

    /**
     * Method, which returns all songs shared by given user
     * @param user
     * @return
     */
    public synchronized Message getAllSharedSongs(String user) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();

            rs = stmt.executeQuery("select id_song,id_album,songs.title from userfiles,songs " +
                    "where username = '" + user + "' and userfiles.title=songs.title");
            if (rs.next() == false) {
                m.msg = "User has no shered songs or doesn't exist.";
                return m;
            } else {
                do {
                    int id = rs.getInt(1);
                    int id_alb = rs.getInt(2);
                    String title = rs.getString(3);
                    m.songList.add(new Song(id, id_alb, title));
                } while (rs.next());
            }

            stmt.close();
        } catch (SQLException e) {
            m.msg = "User has no shared song files.";
            return m;
        }
        return m;
    }

    /**
     * Method, which returns all favorite songs of a given user.
     * @param name
     * @return
     */
    public synchronized Message getAllFavSongs(String name) {
        Message m = new Message();
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = MulticastServer.conn.createStatement();


            rs = stmt.executeQuery("select songs.id_song,songs.id_album,songs.title " +
                    "from favorites,users,songs " +
                    "where users.username='" + name + "' " +
                    "and users.id_user=favorites.id_user " +
                    "and favorites.id_song=songs.id_song");
            if (rs.next() == false) {
                m.msg = "User has no favorites or doesn't exist.";
                return m;
            } else {
                do {
                    int id = rs.getInt(1);
                    int id_alb = rs.getInt(2);
                    String title = rs.getString(3);
                    m.songList.add(new Song(id, id_alb, title));
                } while (rs.next());
            }
            stmt.close();
        } catch (SQLException e) {
            m.msg = "Some fucking error or something lmao";
            return m;
        }
        return m;
    }
}
