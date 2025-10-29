package model;

public class Books {
    private String title;
    private String author;
    private int ID;
    private boolean is_available;

    public Books(String title, String author, int ID) {
        this.title = title;
        this.author = author;
        this.ID = ID;
        this.is_available = true;
    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean getIsAvailable() {
        return is_available;
    }


    public void setIsAvailable(boolean is_available) {
        this.is_available = is_available;
    }

    public String getAuthor() {
        return author;
    }
public void setAuthor(String author) {
        this.author = author;
}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
       return "Book: {ID = " + ID + ", title = " + title + ", author = " + author + " Status" + is_available + "}";

    }
}