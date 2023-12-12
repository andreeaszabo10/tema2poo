package app;

public class Announcement {
    private String name;
    private String owner;
    private String description;

    public Announcement() {
    }

    public Announcement(final String name, final String owner, final String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
