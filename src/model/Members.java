package model;

import java.util.ArrayList;

public class Members {
    private String Name;
    private int ID;
    private ArrayList<Books> borrowedBooks;

    public Members(String name, int id) {
        Name = name;
        ID = id;
        borrowedBooks = new ArrayList<>(); // مهم جداً عشان نتفادى NullPointerException
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Books> getBorrowedBooks() {
        return borrowedBooks; // بيرجع الكتب المستعارة بشكل مباشر
    }

    public void borrowBook(Books book) {
        borrowedBooks.add(book);
        book.setIsAvailable(false);
    }

    public void returnBook(Books book) {
        borrowedBooks.remove(book);
        book.setIsAvailable(true);
    }

    @Override
    public String toString() {
        return "Member: { ID = " + ID + ", Name = " + Name + ", BorrowedBooks = " + borrowedBooks + " }";
    }
}
