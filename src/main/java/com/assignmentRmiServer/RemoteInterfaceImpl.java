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
    public void login(String username, String password) {
        //send the credentials to multicast server
        String credentials = username + " | " + password;
        //logic

        //test
        System.out.println("successful logged in with crentials: " + credentials);
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
    public Song getOneSong(String songName) throws RemoteException {
        Song s = null;
        for (Song song : songs) {
            if (song.getTitle().equals(songName)) {
                s = song;
            }
        }
        return s;
    }

    @Override
    public User findByUsername(String username) throws RemoteException {
        System.out.println("*****************************************");
        return new User(2, username, "pass", "czesc");
    }

    @Override
    public List<Song> getAllSongs() throws RemoteException {
        /*Song s1 = new Song(1L, "title1", 2L, 3L);
        Song s2 = new Song(2L, "title2", 3L, 4L);
        songs.add(s1);
        songs.add(s2);*/
        return songs;
    }

    @Override
    public List<Album> getAllAlbums() throws RemoteException {
        /*Album a1 = new Album(1L, "name1", "genre1", 2l, "desc");
        Album a2 = new Album(2L, "name2", "genre2", 3l, "desc");
        albums.add(a1);
        albums.add(a2);*/
        return albums;
    }

    @Override
    public List<Artist> getAllArtists() throws RemoteException {
       /* Artist a1 = new Artist(1L, "name1");
        Artist a2 = new Artist(2L, "name2");
        artists.add(a1);
        artists.add(a2);*/
        return artists;
    }

    @Override
    public Album getOneAlbum(String albumName) throws RemoteException {
        return new Album(1, "name", "gente", "das", 3);
    }

    @Override
    public Artist getOneArtist(String artistName) throws RemoteException {
        return new Artist(1, "name1", "bio");
    }

    @Override
    public void insertSong(Song song) throws RemoteException {
        songs.add(song);
    }

    @Override
    public void updateSong(Song song) throws RemoteException {
        deleteSong(song.getTitle());
        songs.add(song);
    }

    @Override
    public void deleteSong(String name) throws RemoteException {
        Song song = getOneSong(name);
        songs.remove(song);
    }

    @Override
    public void deleteArtist(String name) throws RemoteException {
        artists.remove(getOneArtist(name));
    }

    @Override
    public void deleteAlbum(String name) throws RemoteException {
        albums.remove(getOneAlbum(name));
    }

    @Override
    public void updateAlbum(Album album) throws RemoteException {
        deleteAlbum(album.getName());
        albums.add(album);
    }

    @Override
    public void updateArtist(Artist artist) throws RemoteException {
        deleteArtist(artist.getName());
        artists.add(artist);
    }

    @Override
    public List<Song> searchSongsByGenre(final String genre) throws RemoteException {
        List<Album> albumsWithGenre = albums.stream().filter(album -> album.getGenre().equals(genre)).collect(Collectors.toList());
        List<Song> result = new ArrayList<>();

        /*songs.forEach(song -> {
            for (Album al : albumsWithGenre){
                if(song.getAlbumId().equals(al.getId())){
                    result.add(song);
                }
            }
        });*/

        return result;
    }

    @Override
    public List<Song> searchSongsByArtist(String name) throws RemoteException {
        List<Artist> artistsWithGivenName = artists.stream().filter(artist -> artist.getName().equals(name)).collect(Collectors.toList());
        List<Song> result = new ArrayList<>();

       /* songs.forEach(song -> {
            for (Artist ar : artistsWithGivenName){
                if(song.getArtistId().equals(ar.getId())){
                    result.add(song);
                }
            }
        });*/

        return result;
    }

    @Override
    public List<Song> searchSongsByAlbum(String name) throws RemoteException {
        List<Album> albumsWithName = albums.stream().filter(album -> album.getName().equals(name)).collect(Collectors.toList());
        List<Song> result = new ArrayList<>();

        /*songs.forEach(song -> {
            for (Album al : albumsWithName){
                if(song.getAlbumId().equals(al.getId())){
                    result.add(song);
                }
            }
        });*/

        return result;
    }

    @Override
    public void writeDescriptonToAlbum(String nameOfAlbum, String newDesc) throws RemoteException {
        Album album = getOneAlbum(nameOfAlbum);
        albums.remove(album);
        /* album.setDescription(newDesc);*/
        albums.add(album);
    }

    @Override
    public void changeRoleOfAUser(String username, Role role) throws RemoteException {
        /*List<User> userList = users.stream().filter(u -> u.getUsername().equals(username)).collect(Collectors.toList());
        userList.forEach(us -> us.setRole(role));*/
    }

    @Override
    public List<User> getAllUsers() throws RemoteException {
        System.out.println("proba wywolania getAllUsers");
        return remoteOperations.getAllUsers();
    }

}
