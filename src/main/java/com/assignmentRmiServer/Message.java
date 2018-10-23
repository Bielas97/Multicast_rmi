package com.assignmentRmiServer;

import domain.Album;
import domain.Artist;
import domain.Song;
import domain.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    public String msg;
    public List<User> userList = new ArrayList<>();
    public List<Song> songList = new ArrayList<>();
    public User user;
    public Artist artist;
    public List<Artist> artistList = new ArrayList<>();
    public Album album;
    public List<Album> albumList = new ArrayList<>();

    public Message() {
    }
}
