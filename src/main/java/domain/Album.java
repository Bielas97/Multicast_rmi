package domain;

import java.io.Serializable;
import java.util.Objects;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;

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
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", descr='" + descr + '\'' +
                ", id_artist=" + id_artist +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return id == album.id &&
                id_artist == album.id_artist &&
                Objects.equals(name, album.name) &&
                Objects.equals(genre, album.genre) &&
                Objects.equals(descr, album.descr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, genre, descr, id_artist);
    }
}
