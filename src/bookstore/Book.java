package bookstore;

public class Book {
    private int id;
    private String title;
    private int price;
    private String author;
    private String description;
    private String genre;
    private String publishDate;

    public Book(int id, String title, int price, String author, String description, String genre, String publishDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.publishDate = publishDate;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + price + "원 | " + author + " | " + description + " | " + genre + " | " + publishDate;
    }
}
