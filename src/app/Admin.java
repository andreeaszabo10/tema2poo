package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.*;
import lombok.Getter;

import java.util.*;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static int timestamp = 0;
    static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * removes a song from the list
     */
    public static void removeSongByName(final List<Song> songs, final String songName) {
        Iterator<Song> iterator = songs.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getName().equals(songName)) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * removes a podcast from the list
     */
    public static void deletePodcast(final List<Podcast> podcasts, final String name) {
        Iterator<Podcast> iterator = podcasts.iterator();
        while (iterator.hasNext()) {
            Podcast podcast = iterator.next();
            if (podcast.getName().equals(name)) {
                iterator.remove();
                return;
            }
        }
    }

    /**
     * removes a podcast
     */
    public static void removePodcast(final CommandInput commandInput) {
        deletePodcast(podcasts, commandInput.getName());
    }

    /**
     * deletes user and all of its playlists/albums
     */
    public static void deleteUser(final User toDelete, final CommandInput commandInput) {
        if (toDelete != null && toDelete.getType() != null && toDelete.getType().equals("artist")) {
            for (Album album : Artist.getAlbums()) {
                if (album.getOwner().equals(commandInput.getUsername())) {
                    for (Song song : album.getSongs()) {
                        removeSongByName(songs, song.getName());
                        for (User user : users) {
                            removeSongByName(user.getLikedSongs(), song.getName());
                        }
                    }
                }
            }
            Artist.getAlbums().removeIf(album -> album.getOwner()
                    .equals(commandInput.getUsername()));
        }
        if (toDelete != null && (toDelete.getType() == null || toDelete.getType().equals("user"))) {
            for (Playlist playlist : toDelete.getFollowedPlaylists()) {
                playlist.decreaseFollowers();
            }
            for (Playlist playlist : toDelete.getPlaylists()) {
                for (User user : users) {
                    user.getFollowedPlaylists().remove(playlist);
                }
            }
        }
        users.remove(toDelete);
    }

    /**
     * gets a song by its name
     */
    public static Song getSongDetails(final String name) {
        for (Song song : songs) {
            if (song.getName().equals(name)) {
                return song;
            }
        }
        return null;
    }

    /**
     * gets a podcast by its name
     */
    public static Podcast getPodcastDetails(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     * add song.
     */
    public static void addSong(final SongInput songInput) {
        songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                songInput.getReleaseYear(), songInput.getArtist()));
    }


    /**
     * add podcast.
     */
    public static void addPodcast(final PodcastInput podcastInput) {
        function(podcastInput);
    }

    /**
     * add a new podcast.
     */
    private static void function(final PodcastInput podcastInput) {
        List<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput.getName(),
                    episodeInput.getDuration(),
                    episodeInput.getDescription()));
        }
        podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
    }

    /**
     * gets online users.
     */
    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isOnline() && (user.getType() == null ||  user.getType().equals("user"))) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * gets all users.
     *
     */
    public static List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == null || user.getType().equals("user")) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() != null && user.getType().equals("artist")) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() != null && user.getType().equals("host")) {
                allUsers.add(user.getUsername());
            }
        }
        return allUsers;
    }

    /**
     * adds users.
     *
     * @param userInput the user that needs to be added
     */
    public static void addUser(final UserInput userInput, final CommandInput commandInput) {
        User newUser = new User(userInput.getUsername(), userInput.getAge(), userInput.getCity());
        newUser.setType(commandInput.getType());
        users.add(newUser);
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            function(podcastInput);
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        for (Song song : songs) {
            int likes = 0;
            for (User user : users) {
                for (Song song1 : user.getLikedSongs()) {
                    if (song1.getName().equals(song.getName())) {
                        likes++;
                    }
                }
            }
            song.setLikes(likes);
        }
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(Artist.getArtists());
        for (Artist artist : Artist.getArtists()) {
            int likes = 0;
            for (User user : users) {
                for (Song song1 : user.getLikedSongs()) {
                    if (song1.getArtist().equals(artist.getName())) {
                        likes++;
                    }
                }
            }
            artist.setLikes(likes);
        }
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getName());
            count++;
        }
        return topArtists;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        Artist.setAlbums(null);
        Artist.setArtists(null);
        Host.setAnnouncements(null);
        Artist.setEvents(null);
        Artist.setMerch(null);
        timestamp = 0;
    }
}
