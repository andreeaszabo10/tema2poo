package app;

public class Merch {
    private String name;
    private String owner;
    private String description;
    private int price;

    public Merch() {
    }

    public Merch(final String name, final String owner,
                 final String description, final int price) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }
}
