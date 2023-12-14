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

    /**
     * gets name
     */
    public String getName() {
        return name;
    }

    /**
     * sets name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * gets owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * sets owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * gets description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * gets date
     */
    public String getDate() {
        return date;
    }
}
