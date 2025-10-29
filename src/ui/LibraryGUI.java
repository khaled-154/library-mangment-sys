package ui;

import javax.swing.*;
import service.Library;
import model.Books;
import model.Members;
import exceptions.*;

public class LibraryGUI {
    private Library library;

    public LibraryGUI() {
        library = new Library();

        library.addBook(new Books("Clean Code", "Robert Martin", 1));
        library.addBook(new Books("Effective Java", "Joshua Bloch", 2));
        library.registerMember(new Members("Khaled", 1));
        library.registerMember(new Members("Gamal", 2));

        JFrame frame = new JFrame("Library Management System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton addBookBtn = new JButton("Add Book");
        JButton addMemberBtn = new JButton("Register Member");
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton listBooksBtn = new JButton("List Available Books");
        JButton listBorrowedBtn = new JButton("List Borrowed Books");

        panel.add(addBookBtn);
        panel.add(addMemberBtn);
        panel.add(borrowBtn);
        panel.add(returnBtn);
        panel.add(listBooksBtn);
        panel.add(listBorrowedBtn);

        addBookBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter Book Title:");
            String author = JOptionPane.showInputDialog("Enter Author:");
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));

            library.addBook(new Books(title, author, id));
            JOptionPane.showMessageDialog(frame, "Book added successfully!");
        });

        // زرار إضافة عضو
        addMemberBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Member Name:");
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID:"));

            library.registerMember(new Members(name, id));
            JOptionPane.showMessageDialog(frame, "Member registered successfully!");
        });

        // زرار استلاف كتاب
        borrowBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID:"));
                library.borrowbook(bookId, memberId);
                JOptionPane.showMessageDialog(frame, "Book borrowed successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // زرار إرجاع كتاب
        returnBtn.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(JOptionPane.showInputDialog("Enter Book ID:"));
                int memberId = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID:"));
                library.returnbook(bookId, memberId);
                JOptionPane.showMessageDialog(frame, "Book returned successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // زرار عرض الكتب المتاحة
        listBooksBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Available Books:\n");
            for (Books b : library.getBooks()) {
                if (b.getIsAvailable()) {
                    sb.append(b.toString()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(frame, sb.toString());
        });

        // زرار عرض الكتب المستلفة لعضو
        listBorrowedBtn.addActionListener(e -> {
            try {
                int memberId = Integer.parseInt(JOptionPane.showInputDialog("Enter Member ID:"));
                Members member = library.find_member_by_id(memberId);

                if (member == null) throw new membernotfound();

                if (member.getBorrowedBooks().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No books borrowed by this member.");
                    return;
                }

                StringBuilder sb = new StringBuilder("Borrowed Books by " + member.getName() + ":\n");
                for (Books b : member.getBorrowedBooks()) {
                    sb.append(b.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, sb.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LibraryGUI();
    }
}
