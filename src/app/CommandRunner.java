package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int CASE = 3;

    private CommandRunner() {
    }

    /**
     * gets online users.
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = Admin.getOnlineUsers();

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", "getOnlineUsers");
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
        for (String onlineUser : onlineUsers) {
            resultArray.add(onlineUser);
        }

        objectNode.set("result", resultArray);

        return objectNode;
    }

    /**
     * gets all users.
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> allUsers = Admin.getAllUsers();

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", "getAllUsers");
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
        for (String all : allUsers) {
            resultArray.add(all);
        }

        objectNode.set("result", resultArray);

        return objectNode;
    }

    /**
     * deletes a user
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        List<String> allUsers = Admin.getAllUsers();

        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", "deleteUser");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User toDelete = Admin.getUser(commandInput.getUsername());
        boolean okToDelete = true;
        for (String name : allUsers) {
            User user = Admin.getUser(name);
            if (user != null && toDelete != null
                    && toDelete.getType() != null && toDelete.getType().equals("artist")
                    && user.getPlayer().getCurrentAudioFile() != null) {
                String song = user.getPlayer().getCurrentAudioFile().getName();
                Song aux = Admin.getSongDetails(song);
                if (aux != null && aux.getArtist().equals(commandInput.getUsername())) {
                    okToDelete = false;
                }
            }
            if (user != null && toDelete != null
                    && toDelete.getType() != null && toDelete.getType().equals("host")
                    && user.getPlayer().getCurrentAudioFile() != null) {
                if (user.getPlayer().getType().equals("podcast")) {
                    Episode episode = (Episode) user.getPlayer().getCurrentAudioFile();
                    for (Podcast podcast : Admin.getPodcasts()) {
                        if (podcast.getOwner().equals(commandInput.getUsername())) {
                            for (Episode episode1 : podcast.getEpisodes()) {
                                if (episode1.getName().equals(episode.getName())) {
                                    okToDelete = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (okToDelete) {
            objectNode.put("message", commandInput.getUsername() + " was successfully deleted.");
            Admin.deleteUser(toDelete, commandInput);
        } else {
            objectNode.put("message", commandInput.getUsername() + " can't be deleted.");
        }

        return objectNode;
    }

    /**
     * list all albums
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", "showAlbums");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
        Artist artist = Artist.getArtist(commandInput.getUsername());
        if (artist != null) {
            List<Album> albums = Artist.getAlbums();
            for (Album album : albums) {
                ObjectNode albumNode = JsonNodeFactory.instance.objectNode();
                albumNode.put("name", album.getName());

                ArrayNode songsArray = JsonNodeFactory.instance.arrayNode();
                for (SongInput song : album.getSongs()) {
                    songsArray.add(song.getName());
                }

                albumNode.set("songs", songsArray);
                resultArray.add(albumNode);
            }
        }

        objectNode.set("result", resultArray);

        return objectNode;
    }

    /**
     * list all podcasts
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("command", "showPodcasts");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
        List<Podcast> podcasts = Admin.getPodcasts();
        for (Podcast podcast : podcasts) {
            if (podcast.getOwner().equals(commandInput.getUsername())) {
                ObjectNode node = JsonNodeFactory.instance.objectNode();
                node.put("name", podcast.getName());

                ArrayNode episodes = JsonNodeFactory.instance.arrayNode();
                for (Episode episode : podcast.getEpisodes()) {
                    episodes.add(episode.getName());
                }

                node.set("episodes", episodes);
                resultArray.add(node);
            }
        }

        objectNode.set("result", resultArray);

        return objectNode;
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user != null && user.isOnline()) {
            if (user.getPage().equals("home")) {
                StringBuilder message = new StringBuilder("Liked songs:\n");
                liked(message, user.getLikedSongs());

                message.append("\n\nFollowed playlists:\n");
                followed(message, user.getFollowedPlaylists());

                objectNode.put("message", message.toString());
            } else if (user.getPage().equals("liked")) {
                StringBuilder message = new StringBuilder("Liked songs:\n");
                liked1(message, user.getLikedSongs());

                message.append("\n\nFollowed playlists:\n");
                followed1(message, user.getFollowedPlaylists());
                objectNode.put("message", message.toString());
            }
        } else {
            objectNode.put("message", commandInput.getUsername() + " is offline.");
        }

        return objectNode;
    }

    /**
     * gets online users.
     */
    private static void liked1(final StringBuilder message, final ArrayList<Song> items) {
        message.append("\t[");
        if (!items.isEmpty()) {
            message.append(items.get(0).getName());
            message.append(" - ").append(items.get(0).getArtist());
            for (int i = 1; i < items.size(); i++) {
                message.append(", ").append(items.get(i).getName());
                message.append(" - ").append(items.get(i).getArtist());
            }
        }
        message.append("]");
    }

    /**
     * gets online users.
     */
    private static void liked(final StringBuilder message, final ArrayList<Song> items) {
        message.append("\t[");
        if (!items.isEmpty()) {
            message.append(items.get(0).getName());
            for (int i = 1; i < items.size(); i++) {
                message.append(", ").append(items.get(i).getName());
            }
        }
        message.append("]");
    }

    /**
     * gets online users.
     */
    private static void followed(final StringBuilder message, final ArrayList<Playlist> items) {
        message.append("\t[");
        if (!items.isEmpty()) {
            message.append(items.get(0).getName());
            for (int i = 1; i < items.size(); i++) {
                message.append(", ").append(items.get(i).getName());
            }
        }
        message.append("]");
    }

    /**
     * gets online users.
     */
    private static void followed1(final StringBuilder message, final ArrayList<Playlist> items) {
        message.append("\t[");
        if (!items.isEmpty()) {
            message.append(items.get(0).getName());
            message.append(" - ").append(items.get(0).getOwner());
            for (int i = 1; i < items.size(); i++) {
                message.append(", ").append(items.get(i).getName());
                message.append(" - ").append(items.get(i).getOwner());
            }
        }
        message.append("]");
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        int ok = 0;
        if (user != null && user.getType() != null && user.getType().equals("host")) {
            PodcastInput newPodcast = new PodcastInput();
            newPodcast.setEpisodes(commandInput.getEpisodes());
            newPodcast.setName(commandInput.getName());
            newPodcast.setOwner(commandInput.getUsername());
            List<Podcast> podcasts = Admin.getPodcasts();
            for (Podcast podcast : podcasts) {
                if (podcast.getName().equals(commandInput.getName())) {
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                Admin.addPodcast(newPodcast);
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user != null && user.getType() != null && user.getType().equals("host") && ok != 1) {
            objectNode.put("message",
                    String.format("%s has added new podcast successfully.",
                            commandInput.getUsername()));
        } else if (ok == 0) {
            objectNode.put("message", commandInput.getUsername() + " is not a host.");
        } else {
            objectNode.put("message",
                    commandInput.getUsername() + " has another podcast with the same name.");
        }

        return objectNode;
    }

    /**
     * Remove podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        Podcast podcast = Admin.getPodcastDetails(commandInput.getName());
        List<User> users = Admin.getUsers();
        boolean verify = true;
        for (User user : users) {
            verify = user.verifyRemove(podcast);
            if (!verify) {
                break;
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (verify) {
            objectNode.put("message",
                    String.format("%s deleted the podcast successfully.",
                            commandInput.getUsername()));
            Admin.removePodcast(commandInput);

        } else if (podcast == null || !podcast.getOwner().equals(commandInput.getUsername())) {
            objectNode.put("message",
                    String.format("%s doesn't have a podcast with the given name.",
                            commandInput.getUsername()));
        } else {
            objectNode.put("message",
                    String.format("%s can't delete this podcast.",
                            commandInput.getUsername()));
        }

        return objectNode;
    }


    /**
     * add album
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        Album newAlbum = new Album(commandInput.getName(), commandInput.getUsername());
        newAlbum.setDescription(commandInput.getDescription());
        newAlbum.setSongs(commandInput.getSongs());
        newAlbum.setReleaseYear(commandInput.getReleaseYear());
        Artist artist = new Artist();
        artist.setName(commandInput.getUsername());
        Artist.addArtist(artist);
        int ok = 0;
        if (Artist.getAlbums() != null) {
            for (Album album : Artist.getAlbums()) {
                if (album.getOwner().equals(commandInput.getUsername())
                        && album.getName().equals(commandInput.getName())) {
                    ok = 1;
                    break;
                }
            }
        }
        for (SongInput song1 : commandInput.getSongs()) {
            for (Song song2 : Admin.getSongs()) {
                if (song1.getName().equals(song2.getName())) {
                    ok = CASE;
                    break;
                }
            }
        }
        int n = commandInput.getSongs().size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (commandInput.getSongs().get(i).getName()
                        .equals(commandInput.getSongs().get(j).getName())) {
                    ok = CASE;
                    break;
                }
            }
        }
        if (ok == 0) {
            List<SongInput> songs = commandInput.getSongs();
            for (SongInput song : songs) {
                Admin.addSong(song);
            }
            Artist.addAlbum(newAlbum);
        }
        String message = Artist.addAlbum(commandInput.getUsername(), ok);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    /**
     * add user
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        UserInput newUserInput = new UserInput();
        newUserInput.setUsername(commandInput.getUsername());
        newUserInput.setAge(commandInput.getAge());
        newUserInput.setCity(commandInput.getCity());
        List<User> users = Admin.getUsers();
        int ok = 0;
        for (User user : users) {
            if (user.getUsername().equals(commandInput.getUsername())) {
                ok = 1;
                break;
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (ok == 0) {
            Admin.addUser(newUserInput, commandInput);
            User user = Admin.getUser(commandInput.getUsername());
            String message = user.addUser(commandInput.getUsername());

            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " is already taken.");
        }

        return objectNode;
    }


    /**
     * switch connection status
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.switchConnectionStatus(user, commandInput.getUsername());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }


    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        assert user != null;
        if (user.isOnline()) {
            Filters filters = new Filters(commandInput.getFilters());
            String type = commandInput.getType();
            ArrayList<String> results = user.search(filters, type);
            String message = "Search returned " + results.size() + " results";

            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
            objectNode.put("results", objectMapper.valueToTree(results));
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", String.format("%s is offline.", commandInput.getUsername()));
            ArrayList<String> results = new ArrayList<>();
            objectNode.put("results", objectMapper.valueToTree(results));
        }

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        assert user != null;
        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        assert user != null;
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (user.isOnline()) {
            String message = user.like();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);
        } else {
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", String.format("%s is offline.", commandInput.getUsername()));
        }

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                                             commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        PlayerStats stats = user.getPlayerStats();
        if (!user.isOnline()) {
            stats.setPaused(false);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        if (!user.isOnline()) {
            stats.setPaused(true);
        }

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Gets top 5 albums.
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Artist.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * change page
     *
     * @param commandInput the command input
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            switch (commandInput.getNextPage()) {
                case "Home" -> user.setPage("home");
                case "LikedContent" -> user.setPage("liked");
                case "Artist" -> user.setPage("artist");
                default -> user.setPage("host");
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", commandInput.getUsername() + " accessed "
                + commandInput.getNextPage() + " successfully.");

        return objectNode;
    }

    /**
     * removes album
     *
     * @param commandInput the command input
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        Album album = Artist.getAlbumDetails(commandInput.getName());
        if (album != null && (!album.getOwner()
                .equals(commandInput.getUsername()) || album.isSelected())) {
            objectNode.put("message", commandInput.getUsername() + " can't delete this album.");
        }

        return objectNode;
    }
}
