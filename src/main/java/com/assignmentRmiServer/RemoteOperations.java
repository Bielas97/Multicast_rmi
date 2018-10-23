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
        Message m = mc.SendReceive("insert|user|" + user + "|" + pass + "|" + type);
        return m.msg;
    }

    public String insertArtist(String name, String bio) {
        Message m = mc.SendReceive("insert|artist|" + name + "|" + bio);
        return m.msg;
    }

    public String insertSong(String title, String albumname) {
        Message m = mc.SendReceive("insert|song|" + title + "|" + albumname);
        return m.msg;
    }

    public String insertAlbum(String name, String genre, String descr, String artistname) {
        Message m = mc.SendReceive("insert|album|" + name + "|" + genre + "|" + artistname + "|" + descr);
        return m.msg;
    }

    public String deleteUser(String id) {
        Message m = mc.SendReceive("delete|user|" + id);
        return m.msg;
    }

    public String deleteArtist(String id) {
        Message m = mc.SendReceive("delete|artist|" + id);
        return m.msg;
    }

    public String deleteAlbum(String id) {
        Message m = mc.SendReceive("delete|album|" + id);
        return m.msg;
    }

    public String deleteSong(String id) {
        Message m = mc.SendReceive("delete|song|" + id);
        return m.msg;
    }

    public List<Artist> getAllArtists() {
        Message m = mc.SendReceive("getall|artists");
        return m.artistList;
    }

    public List<User> getAllUsers() {
        Message m = mc.SendReceive("getall|users");
        return m.userList;
    }

    public List<Album> getAllAlbums() {
        Message m = mc.SendReceive("getall|albums");
        return m.albumList;
    }

    public List<Song> getAllSongs() {
        Message m = mc.SendReceive("getall|songs");
        return m.songList;
    }

    public String updateSong(String newtitle, String oldtitle) {
        Message m = mc.SendReceive("update|song|" + newtitle + "|" + oldtitle);
        return m.msg;
    }

    public String updateArtist(String newname, String oldname) {
        Message m = mc.SendReceive("update|artist|" + newname + "|" + oldname);
        return m.msg;
    }

    public String updateAlbum(String newname, String oldname) {
        Message m = mc.SendReceive("update|album|" + newname + "|" + oldname);
        return m.msg;
    }

    public List<Song> searchSongByArtist(String name) {
        Message m = mc.SendReceive("search|byartist|" + name);
        return m.songList;
    }

    public List<Song> searchSongByAlbum(String name) {
        Message m = mc.SendReceive("search|byalbum|" + name);
        return m.songList;
    }

    public List<Song> searchSongByGenre(String name) {
        Message m = mc.SendReceive("search|bygenre|" + name);
        return m.songList;
    }

    public List<Song> searchSongByName(String name) {
        Message m = mc.SendReceive("search|byname|" + name);
        return m.songList;
    }

    public String checkIfFileCorresponds(String filename) {
        Message m = mc.SendReceive("check|file|" + filename);
        return m.msg;
    }

    public String checkIfUserCanDownload(String username, String title) {
        Message m = mc.SendReceive("check|user" + "|" + username + "|" + title);
        return m.msg;
    }

    public String shareSong(String user, String filename) {
        Message m = mc.SendReceive("insert|share|" + user + "|" + filename);
        return m.msg;
    }

    public String writeDescriptionToAlbum(String album, String desc) {
        Message m = mc.SendReceive("update|descalbum|" + desc + "|" + album);
        return m.msg;
    }
    public String changeRoleOfAUser(String user,String role){
        Message m = mc.SendReceive("update|role|" + role + "|" + user);
        return m.msg;
    }
    public String insertFavoriteSong(String user,String song){
        Message m = mc.SendReceive("insert|fav|" + user + "|" + song);
        return m.msg;
    }
}
