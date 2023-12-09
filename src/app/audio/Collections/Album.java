package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.List;

@Getter
public final class Album extends AudioCollection {

    private Integer releaseYear;
    private String description;
    private List<SongInput> songs;

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSongs(List<SongInput> songs) {
        this.songs = songs;
    }

    /**
     * Instantiates a new Audio collection.
     *
     * @param name  the name
     * @param owner the owner
     */
    public Album(String name, String owner) {
        super(name, owner);
    }

    public Album(String name, String owner, Integer releaseYear, String description, List<SongInput> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return null;
    }
}
