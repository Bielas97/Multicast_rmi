package domain;

import java.io.Serializable;

public class Song implements Serializable {
    private int id;
    private int id_album;
    private String title;

    public Song(int id, int id_album, String title) {
        this.id = id;
        this.id_album = id_album;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "domain.Song{" +
                "id=" + id +
                ", id_album=" + id_album +
                ", title='" + title + '\'' +
                '}';
    }
}
