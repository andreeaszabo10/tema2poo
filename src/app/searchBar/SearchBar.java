package app.searchBar;


import app.Admin;
import app.Artist;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> results;
    private List<User> result;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;
    @Getter
    private User lastSelectedUser;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;
        Admin admin = Admin.getInstance();

        switch (type) {
            case "song":
                entries = new ArrayList<>(admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>();
                if (Artist.getAlbums() != null) {
                    entries = new ArrayList<>(Artist.getAlbums());

                    if (filters.getName() != null) {
                        entries = filterByName(entries, filters.getName());
                    }

                    if (filters.getOwner() != null) {
                        entries = filterByOwner(entries, filters.getOwner());
                    }
                }
                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<User> search(final Filters filters, final String type, final SearchBar searchBar) {
        List<User> entries;
        Admin admin = Admin.getInstance();
        switch (type) {
            case "artist":
                entries = new ArrayList<>();
                for (User user : admin.getUsers()) {
                    if (user.getType() != null && user.getType().equals("artist")) {
                        entries.add(user);
                    }
                }

                if (filters.getName() != null) {
                    entries = filterName(entries, filters.getName());
                }
                break;
            case "host":
                entries = new ArrayList<>();
                for (User user : admin.getUsers()) {
                    if (user.getType() != null && user.getType().equals("host")) {
                        entries.add(user);
                    }
                }

                if (filters.getName() != null) {
                    entries = filterName(entries, filters.getName());
                }
                break;

            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }
        this.result = entries;
        this.lastSearchType = type;
        return this.result;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    /**
     * Select user page.
     *
     * @param itemNumber the item number
     * @return the user
     */
    public User selects(final Integer itemNumber) {
        if (this.result.size() < itemNumber) {
            result.clear();

            return null;
        } else {
            lastSelectedUser =  this.result.get(itemNumber - 1);
            result.clear();

            return lastSelectedUser;
        }
    }
}
