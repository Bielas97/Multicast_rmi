package domain;

import java.io.Serializable;

public class Album implements Serializable {
    private int id;
    private String name;
    private String genre;
    private String descr;
    private int id_artist;

    public Album(int id, String name, String genre, String descr, int id_artist) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.descr = descr;
        this.id_artist = id_artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getId_artist() {
        return id_artist;
    }

    public void setId_artist(int id_artist) {
        this.id_artist = id_artist;
    }

    @Override
    public String toString() {
        return "domain.Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", descr='" + descr + '\'' +
                ", id_artist=" + id_artist +
                '}';
    }
}
