package app;

import lombok.Getter;

@Getter
public class Announcement {
    private String name;
    private String owner;
    private String description;

    public Announcement(final String name, final String owner, final String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    /**
     * sets name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * sets owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * sets description
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
