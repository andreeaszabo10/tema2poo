package app;

public class Event {
    private String name;
    private String owner;
    private String description;
    private String date;

    public Event() {
    }

    public Event(final String name, final String owner,
                 final String description, final String date) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }
}
