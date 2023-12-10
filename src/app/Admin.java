package app;

import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private static final int LIMIT = 5;

    private Admin() {
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
        timestamp = 0;
    }
}
