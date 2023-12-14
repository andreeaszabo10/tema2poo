package app;

import app.audio.Collections.Album;
import lombok.Getter;

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
    @Getter
    private static List<Event> events;
    @Getter
    private static List<Merch> merch;
    private static final int LIMIT = 5;
    private static final int CASE = 3;

    /**
     * add event
     */
    public static void addEvent(final Event event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
    }

    public static void setEvents(List<Event> events) {
        Artist.events = events;
    }

    public static void setMerch(List<Merch> merch) {
        Artist.merch = merch;
    }

    public static void removeEvent(Event event) {
        events.remove(event);
    }
    /**
     * add merch
     */
    public static void addMerch(final Merch merchandise) {
        if (merch == null) {
            merch = new ArrayList<>();
        }
        merch.add(merchandise);
    }

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
    public static void addArtist(final Artist artist) {
        if (artists == null) {
            artists = new ArrayList<>();
        }
        artists.add(artist);
    }

    public static void setArtists(final List<Artist> artists) {
        Artist.artists = artists;
    }

    /**
     * sets name
     */
    public void setName(final String name) {
        this.name = name;
    }

    public static void setAlbums(final List<Album> albums) {
        Artist.albums = albums;
    }

    /**
     * add album message
     */
    public static String addAlbum(final String username, final int ok) {
        if (ok == 1) {
            return username + " has another album with the same name.";
        }
        if (ok == CASE) {
            return username + " has the same song at least twice in this album.";
        }
        return username + " has added new album successfully.";
    }

    /**
     * add album
     */
    public static void addAlbum(final Album album) {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        albums.add(album);
    }

    /**
     * remove album
     */
    public static void removeAlbum(final Album album) {
        albums.remove(album);
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
            albums.sort(Comparator.comparingInt(Album::getLikes)
                    .reversed().thenComparing(Album::getName));

            int count = 0;
            for (Album album : albums) {
                if (count >= LIMIT) {
                    break;
                }
                topAlbums.add(album.getName());
                count++;
            }
        }

        return topAlbums;
    }
}
