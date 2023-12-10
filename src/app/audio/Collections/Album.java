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
    @Getter
    private boolean selected = false;

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public int getLikes() {
        int likes = 0;

        if (songs != null) {
            for (SongInput songInput : songs) {
                Song aux = new Song(songInput.getName(), songInput.getDuration(),
                        songInput.getAlbum(),
                        songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                        songInput.getReleaseYear(), songInput.getArtist());
                likes = likes + (aux.getLikes());
            }
        }

        return likes;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setSongs(final List<SongInput> songs) {
        this.songs = songs;
    }

    /**
     * Instantiates a new Audio collection.
     *
     * @param name  the name
     * @param owner the owner
     */
    public Album(final String name, final String owner) {
        super(name, owner);
    }

    public Album(final String name, final String owner, final Integer releaseYear,
                 final String description, final List<SongInput> songs) {
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
    public AudioFile getTrackByIndex(final int index) {
        SongInput song = songs.get(index);
        return new Song(song.getName(), song.getDuration(),
                song.getAlbum(), song.getTags(), song.getLyrics(),
                song.getGenre(), song.getReleaseYear(), song.getArtist());
    }
}
