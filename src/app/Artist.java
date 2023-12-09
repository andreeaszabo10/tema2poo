package app;

import app.audio.Collections.Album;
import app.user.User;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    public Artist() {
    }
    @Getter
    private static List<Artist> artists;
    @Getter
    private String name;
    @Getter
    private static List<Album> albums;

    public static Artist getArtist(final String username) {
        if (artists != null) {
            for (Artist artist : artists) {
                if (artist.getName().equals(username)) {
                    return artist;
                }
            }
        }
        return null;
    }

    public static void addArtist(Artist artist) {
        if (artists == null) {
            artists = new ArrayList<>();
        }
        artists.add(artist);
    }

    public static void setArtists(List<Artist> artists) {
        Artist.artists = artists;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setAlbums(List<Album> albums) {
        Artist.albums = albums;
    }

    public static String addAlbum(String username, int ok) {
        if (ok == 1) {
            return username + " has another album with the same name.";
        }
        if (ok == 3) {
            return username + " has the same song at least twice in this album.";
        }
        return username + " has added new album successfully.";
    }
    public static void addAlbum(Album album) {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        albums.add(album);
    }
}
