package com.assignmentRmiServer;


import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;
import enums.Role;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * To ma byc tez klientem UDP
 */
public class RemoteInterfaceImpl extends UnicastRemoteObject implements RemoteInterface {
    private static final long serialVersionUID = 1L;
    private List<User> users = new ArrayList<>();
    private List<Song> songs = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();

    private RemoteOperations remoteOperations = new RemoteOperations(new SendReceiveConnection());

    protected RemoteInterfaceImpl() throws RemoteException {
        super();
    }

    public RemoteInterfaceImpl(int port) throws RemoteException {
        super(port);
    }

    protected RemoteInterfaceImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }


    @Override
    public void printMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public String getValue() throws RemoteException {
        return "example";
    }

    @Override
    public String register(String username, String password, String role) throws RemoteException {
        System.out.println("attempt to register...");
        //send the credentials to multicast server
        //String credentials = username + " | " + password + " | " + email;
        //logic
        //users.add(new User(1, username, password, "kupa"));
        //test
        return remoteOperations.insertUser(username, password, role);
    }

    /*@Override
    public boolean isLoginOk(String username, String password) {
        //check in DB if there is this USER
       *//* if(ok){
            return true;
        }
        else return false;*//*
        System.out.println("attempt to login...");
        System.out.println("checking for user with username '" + username + "'...");
        if (users.isEmpty()) {
            System.out.println("user not found!");
        }
        boolean result = false;
        for (User user : users) {
            if (username.equals(username)) {
                System.out.println("found '" + username + "'...");
                System.out.println("checking password...");
                if (password.equals(user.getPassword())) {
                    result = true;
                    System.out.println("PASSWORD CORRECT!");
                } else {
                    System.out.println("PASSWORD INCORRECT!");
                }
            } else {
                System.out.println("user not found!");
            }
        }


        return result;
    }*/


    @Override
    public String login(String username, String password) {
        return remoteOperations.authenicateUser(username, password);
    }

    @Override
    public Role whoIsLoggedIn(String username) throws RemoteException {
        /*Role role = null;
        for (User user : users) {
            if (username.equals(username)) {
                role = user.getRole();
            }
        }*/
        return Role.ADMIN;
    }

    @Override
    public void logout() throws RemoteException {
        System.out.println("logged out.");
    }

    @Override
    public List<Song> searchSongByName(String songName) throws RemoteException {
        return remoteOperations.searchSongByName(songName);
    }

    @Override
    public List<Song> searchSongByArtist(String name){
        return remoteOperations.searchSongByArtist(name);
    }

    @Override
    public List<Song> searchSongByAlbum(String name){
        return remoteOperations.searchSongByAlbum(name);
    }

    @Override
    public List<Song> searchSongByGenre(String name){
        return remoteOperations.searchSongByGenre(name);
    }



    @Override
    public List<User> findByUsername(String username) throws RemoteException {
        return remoteOperations.searchUser(username);
    }

    @Override
    public List<Song> getAllSongs() throws RemoteException {
        return remoteOperations.getAllSongs();
    }

    @Override
    public List<Album> getAllAlbums() throws RemoteException {
        return remoteOperations.getAllAlbums();
    }

    @Override
    public List<Artist> getAllArtists() throws RemoteException {
       return remoteOperations.getAllArtists();
    }

   /* @Override
    public Album getOneAlbum(String albumName) throws RemoteException {
        return new Album(1, "name", "gente", "das", 3);

    }*/

    /*@Override
    public Artist getOneArtist(String artistName) throws RemoteException {
        return new Artist(1, "name1", "bio");
    }*/

    @Override
    public String insertSong(String title, String albumname) throws RemoteException {
        return remoteOperations.insertSong(title, albumname);
    }

    @Override
    public String updateSong(String newTitle, String oldTitle) throws RemoteException {
        return remoteOperations.updateSong(newTitle, oldTitle);
    }

    @Override
    public String deleteSong(String id) throws RemoteException {
        return remoteOperations.deleteSong(id);
    }

    @Override
    public String deleteArtist(String id) throws RemoteException {
        return remoteOperations.deleteArtist(id);
    }

    @Override
    public String deleteAlbum(String id) throws RemoteException {
        return remoteOperations.deleteAlbum(id);
    }

    @Override
    public String updateAlbum(String newName, String oldName) throws RemoteException {
        return remoteOperations.updateAlbum(newName, oldName);
    }

    @Override
    public String updateArtist(String newName, String oldName) throws RemoteException {
        return remoteOperations.updateArtist(newName, oldName);
    }

    @Override
    public String promoteUser(String username, String role) throws RemoteException {
        return remoteOperations.changeRoleOfAUser(username, role);
    }

    @Override
    public List<Song> getSharedSongs(String username) throws RemoteException {
        return remoteOperations.getSharedSongs(String username);
    }

    @Override
    public List<Song> getFavouriteSongs(String username) throws RemoteException {
        return remoteOperations.getFavouriteSongs(username);
    }

    @Override
    public List<Song> searchSongsByGenre(final String genre) throws RemoteException {
        return remoteOperations.searchSongByGenre(genre);
    }

    @Override
    public List<Song> searchSongsByArtist(String name) throws RemoteException {
        return remoteOperations.searchSongByArtist(name);
    }

    @Override
    public List<Song> searchSongsByAlbum(String name) throws RemoteException {
        return remoteOperations.searchSongByAlbum(name);
    }

    @Override
    public String writeDescriptonToAlbum(String nameOfAlbum, String newDesc) throws RemoteException {
        return remoteOperations.writeDescriptionToAlbum(nameOfAlbum, newDesc);
    }

    @Override
    public String changeRoleOfAUser(String username, String role) throws RemoteException {
        return remoteOperations.changeRoleOfAUser(username, role);
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        System.out.println("proba wywolania getAllUsers");
        return remoteOperations.getAllUsers();
    }

    @Override
    public String insertAlbum(String name, String genre, String desc, String artistName) throws RemoteException {
        return remoteOperations.insertAlbum(name, genre, desc, artistName);
    }

    @Override
    public String insertArtist(String name, String bio) throws RemoteException {
        return remoteOperations.insertArtist(name, bio);
    }

    @Override
    public String deleteUser(String id) throws RemoteException {
        return remoteOperations.deleteUser(id);
    }

}
