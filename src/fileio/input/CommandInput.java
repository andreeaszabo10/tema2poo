package fileio.input;

import app.audio.Files.Episode;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class CommandInput {
    @Getter
    private String command;
    @Getter
    private String username;
    @Getter
    private Integer timestamp;
    @Getter
    private String type; // song / playlist / podcast
    @Getter
    private FiltersInput filters; // pentru search
    @Getter
    private Integer itemNumber; // pentru select
    @Getter
    private Integer repeatMode; // pentru repeat
    @Getter
    private Integer playlistId; // pentru add/remove song
    @Getter
    private String playlistName; // pentru create playlist
    @Getter
    private Integer seed; // pentru shuffle
    @Getter
    private Integer age;
    @Getter
    private String nextPage;
    @Getter
    private String city;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String date;
    @Getter
    private Integer price;
    @Getter
    private Integer releaseYear;
    @Getter
    private List<SongInput> songs;
    @Getter
    private ArrayList<EpisodeInput> episodes;

    public CommandInput() {
    }

    public void setEpisodes(ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    public void setSongs(List<SongInput> songs) {
        this.songs = songs;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setRepeatMode(final Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
