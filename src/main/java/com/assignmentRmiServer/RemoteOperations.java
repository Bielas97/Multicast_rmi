package com.assignmentRmiServer;

import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;

import java.util.List;


public class RemoteOperations {
    SendReceiveConnection mc;

    public RemoteOperations(SendReceiveConnection mc) {
        this.mc = mc;
    }

    public String insertUser(String user, String pass, String type) {
        Message m = mc.sendReceive("insert|user|" + user + "|" + pass + "|" + type);
        return m.msg;
    }

    public String insertArtist(String name, String bio) {
        Message m = mc.sendReceive("insert|artist|" + name + "|" + bio);
        return m.msg;
    }

    public String insertSong(String title, String albumname) {
        Message m = mc.sendReceive("insert|song|" + title + "|" + albumname);
        return m.msg;
    }

    public String insertAlbum(String name, String genre, String descr, String artistname) {
        Message m = mc.sendReceive("insert|album|" + name + "|" + genre + "|" + artistname + "|" + descr);
        return m.msg;
    }

    public String deleteUser(String user) {
        Message m = mc.sendReceive("delete|user|" + user);
        return m.msg;
    }

    public String deleteArtist(String artist) {
        Message m = mc.sendReceive("delete|artist|" + artist);
        return m.msg;
    }

    public String deleteAlbum(String album) {
        Message m = mc.sendReceive("delete|album|" + album);
        return m.msg;
    }

    public String deleteSong(String song) {
        Message m = mc.sendReceive("delete|song|" + song);
        return m.msg;
    }

    public List<Artist> getAllArtists() {
        Message m = mc.sendReceive("getall|artists");
        return m.artistList;
    }

    public List<User> getAllUsers() {
        Message m = mc.sendReceive("getall|users");
        return m.userList;
    }

    public List<Album> getAllAlbums() {
        Message m = mc.sendReceive("getall|albums");
        return m.albumList;
    }

    public List<Song> getAllSongs() {
        Message m = mc.sendReceive("getall|songs");
        return m.songList;
    }

    public String updateSong(String newtitle, String oldtitle) {
        Message m = mc.sendReceive("update|song|" + newtitle + "|" + oldtitle);
        return m.msg;
    }

    public String updateArtist(String newname, String oldname) {
        Message m = mc.sendReceive("update|artist|" + newname + "|" + oldname);
        return m.msg;
    }

    public String updateAlbum(String newname, String oldname) {
        Message m = mc.sendReceive("update|album|" + newname + "|" + oldname);
        return m.msg;
    }

    public List<Song> searchSongByArtist(String name) {
        Message m = mc.sendReceive("search|byartist|" + name);
        return m.songList;
    }

    public List<Song> searchSongByAlbum(String name) {
        Message m = mc.sendReceive("search|byalbum|" + name);
        return m.songList;
    }

    public List<Song> searchSongByGenre(String name) {
        Message m = mc.sendReceive("search|bygenre|" + name);
        return m.songList;
    }

    public List<Song> searchSongByName(String name) {
        Message m = mc.sendReceive("search|byname|" + name);
        return m.songList;
    }

    public String checkIfFileCorresponds(String filename) {
        Message m = mc.sendReceive("check|file|" + filename);
        return m.msg;
    }

    public String checkIfUserCanDownload(String username, String title) {
        Message m = mc.sendReceive("check|user" + "|" + username + "|" + title);
        return m.msg;
    }

    public String shareSong(String user, String filename) {
        Message m = mc.sendReceive("insert|share|" + user + "|" + filename);
        return m.msg;
    }

    public String writeDescriptionToAlbum(String album, String desc) {
        Message m = mc.sendReceive("update|descalbum|" + desc + "|" + album);
        return m.msg;
    }

    public String changeRoleOfAUser(String user, String role) {
        Message m = mc.sendReceive("update|role|" + role + "|" + user);
        return m.msg;
    }

    public String insertFavoriteSong(String user, String song) {
        Message m = mc.sendReceive("insert|fav|" + user + "|" + song);
        return m.msg;
    }

    public String authenicateUser(String user, String pass) {
        Message m = mc.sendReceive("check|auth|" + user + "|" + pass);
        return m.msg;
    }

    public List<User> searchUser(String name) {
        Message m = mc.sendReceive("search|byuser|" + name);
        return m.userList;
    }

    public String updateArtistBio(String newbio, String name) {
        Message m = mc.sendReceive("update|bio|" + newbio + "|" + name);
        return m.msg;
    }
}
