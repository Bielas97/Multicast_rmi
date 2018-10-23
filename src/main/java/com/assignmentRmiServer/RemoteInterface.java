package com.assignmentRmiServer;

import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;
import enums.Role;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteInterface extends Remote {
    //tescik
    void printMessage(String message) throws RemoteException;

    String getValue() throws RemoteException;

    //real
    String register(String username, String password, String role) throws RemoteException;

    //boolean isLoginOk(String username, String password) throws RemoteException;
    void login(String username, String password) throws RemoteException;

    Role whoIsLoggedIn(String username) throws RemoteException;

    void logout() throws RemoteException;


//    Song getOneSong(String songName) throws RemoteException;

    User findByUsername(String username) throws RemoteException;

    List<Song> getAllSongs() throws RemoteException;

    List<Album> getAllAlbums() throws RemoteException;

    List<Artist> getAllArtists() throws RemoteException;

    Album getOneAlbum(String albumName) throws RemoteException;

    Artist getOneArtist(String artistName) throws RemoteException;

    String insertSong(String title, String albumname) throws RemoteException;

    //JAK TO MA DZIALAC W BAZIE?
    //przykladowe rozwiazanie:
    //1. wyciagasz id z "song" podanej w argumecnie
    //2. robisz update - UPDATE song SET title = :song.title, albumId = :song.albumID, artistId = :song.artistID WHERE id = :song.id
    //void updateSong(Song song) throws RemoteException;

    //JAK TO MA DZIALAC W BAZIE:
    //przykladowe rozwiazanie:
    //1. najpierwsz wyszukujesz w bazie piosenki o title = :name - podane w argumencie, zapisujesz sb ta piosenke np. "moja"
    //2. SQL - DELETE song WHERE id = :moja.id
    String deleteSong(String id) throws RemoteException;

    //analogicznie
    String deleteArtist(String id) throws RemoteException;

    //analogicznie
    String deleteAlbum(String id) throws RemoteException;

   /* //analogicznie
    void updateAlbum(Album album) throws RemoteException;

    //analogicznie
    void updateArtist(Artist artist) throws RemoteException;*/

    List<Song> searchSongsByGenre(String genre) throws RemoteException;

    List<Song> searchSongsByArtist(String name) throws RemoteException;

    List<Song> searchSongsByAlbum(String name) throws RemoteException;

    void writeDescriptonToAlbum(String nameOfAlbum, String newDesc) throws RemoteException;

    void changeRoleOfAUser(String username, Role role) throws RemoteException;

    List<User> getAllUsers() throws RemoteException;

    String insertAlbum(String name, String genre, String desc, String artistName) throws RemoteException;

    String deleteUser(String id) throws RemoteException;

    List<Song> searchSongByName(String songName) throws RemoteException;

    List<Song> searchSongByArtist(String name) throws RemoteException;

    List<Song> searchSongByAlbum(String name) throws RemoteException;

    List<Song> searchSongByGenre(String name) throws RemoteException;

    String updateSong(String newTitle, String oldTitle) throws RemoteException;

    String updateAlbum(String newName, String oldName) throws RemoteException;

    String updateArtist(String newName, String oldName) throws RemoteException;
}
