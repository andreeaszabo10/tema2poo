package app;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * get artist
     */
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

    /**
     * add artist
     */
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

    /**
     * add album message
     */
    public static String addAlbum(String username, int ok) {
        if (ok == 1) {
            return username + " has another album with the same name.";
        }
        if (ok == 3) {
            return username + " has the same song at least twice in this album.";
        }
        return username + " has added new album successfully.";
    }

    /**
     * add album
     */
    public static void addAlbum(Album album) {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        albums.add(album);
    }

    /**
     * gets an album by its name
     */
    public static Album getAlbumDetails(final String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Gets top 5 albums.
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<String> topAlbums = new ArrayList<>();

        if (albums != null && !albums.isEmpty()) {
            albums.sort(Comparator.comparingInt(Album::getLikes).reversed().thenComparing(Album::getName));

            int count = 0;
            for (Album album : albums) {
                if (count >= 5) {
                    break;
                }
                topAlbums.add(album.getName());
                count++;
            }
        }

        return topAlbums;
    }
}
