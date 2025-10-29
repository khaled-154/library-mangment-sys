package service;

import database.DatabaseConnection;
import exceptions.*;
import model.Books;
import model.Members;

import java.sql.*;
import java.util.ArrayList;

public class Library {

    public Library() {
        DatabaseConnection.initializeDatabase();
    }

    // ---------------- ADD METHODS ----------------
    public void addBook(Books book) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Books (id, title, author, isAvailable) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, book.getID());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setBoolean(4, book.getIsAvailable());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerMember(Members member) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Members (id, name) VALUES (?, ?)")) {
            ps.setInt(1, member.getID());
            ps.setString(2, member.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- FIND METHODS ----------------
    public Books find_book_by_id(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Books WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Books b = new Books(rs.getString("title"), rs.getString("author"), rs.getInt("id"));
                b.setIsAvailable(rs.getBoolean("isAvailable"));
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Members find_member_by_id(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Members WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Members(rs.getString("name"), rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- BORROW / RETURN ----------------
    public void borrowbook(int bookid, int memberid) throws Exception {
        Books book = find_book_by_id(bookid);
        Members member = find_member_by_id(memberid);

        if (book == null) throw new booknotfound();
        if (member == null) throw new membernotfound();
        if (!book.getIsAvailable()) throw new bookisalreadybrowed();

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement updateBook = conn.prepareStatement("UPDATE Books SET isAvailable = FALSE WHERE id = ?");
            updateBook.setInt(1, bookid);
            updateBook.executeUpdate();

            PreparedStatement insertBorrow = conn.prepareStatement("INSERT INTO Borrowed (memberId, bookId) VALUES (?, ?)");
            insertBorrow.setInt(1, memberid);
            insertBorrow.setInt(2, bookid);
            insertBorrow.executeUpdate();

            conn.commit();
            System.out.println("✅ member " + member.getName() + " borrowed book " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error borrowing book");
        }
    }

    public void returnbook(int bookid, int memberid) throws Exception {
        Books book = find_book_by_id(bookid);
        Members member = find_member_by_id(memberid);

        if (book == null) throw new booknotfound();
        if (member == null) throw new membernotfound();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM Borrowed WHERE memberId=? AND bookId=?");
            check.setInt(1, memberid);
            check.setInt(2, bookid);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) throw new bookisnotborrowedforthisuser();

            conn.setAutoCommit(false);

            PreparedStatement deleteBorrow = conn.prepareStatement("DELETE FROM Borrowed WHERE memberId=? AND bookId=?");
            deleteBorrow.setInt(1, memberid);
            deleteBorrow.setInt(2, bookid);
            deleteBorrow.executeUpdate();

            PreparedStatement updateBook = conn.prepareStatement("UPDATE Books SET isAvailable = TRUE WHERE id = ?");
            updateBook.setInt(1, bookid);
            updateBook.executeUpdate();

            conn.commit();
            System.out.println("✅ member " + member.getName() + " returned book " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error returning book");
        }
    }

    // ---------------- LIST METHODS ----------------
    public void list_available_books() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE isAvailable = TRUE")) {

            System.out.println("📚 Available Books:");
            while (rs.next()) {
                System.out.println("Book { ID=" + rs.getInt("id") +
                        ", Title=" + rs.getString("title") +
                        ", Author=" + rs.getString("author") + " }");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listborrowed_books(int memberid) throws Exception {
        Members member = find_member_by_id(memberid);
        if (member == null) throw new membernotfound();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT b.id, b.title, b.author FROM Borrowed br JOIN Books b ON br.bookId = b.id WHERE br.memberId = ?")) {
            ps.setInt(1, memberid);
            ResultSet rs = ps.executeQuery();

            boolean hasBooks = false;
            System.out.println("📖 Borrowed Books by " + member.getName() + ":");
            while (rs.next()) {
                hasBooks = true;
                System.out.println("Book { ID=" + rs.getInt("id") +
                        ", Title=" + rs.getString("title") +
                        ", Author=" + rs.getString("author") + " }");
            }
            if (!hasBooks) throw new memberisnotborrwedanybooks("No books borrowed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Books> getBooks() {
        ArrayList<Books> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Books")) {
            while (rs.next()) {
                Books b = new Books(rs.getString("title"), rs.getString("author"), rs.getInt("id"));
                b.setIsAvailable(rs.getBoolean("isAvailable"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
