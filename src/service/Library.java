package service;

import exceptions.*;
import model.Books;
import model.Members;

import java.util.ArrayList;

public class Library {
private ArrayList<Books> books;
private ArrayList<Members> members;

public Library()
{
books=new ArrayList<>();
members=new ArrayList<>();
}
    public ArrayList<Books> getBooks() {
        return books;
    }


    public void addBook(Books book)
{
books.add(book);
}
public void registerMember(Members member)
{
members.add(member);
}
public Members find_member_by_id(int id)
{
for(Members member:members)
{
    if(member.getID()==id)return member;
}
return null;
}
public Books  find_book_by_id(int id)
{
    for(Books b : books){
        if(b.getID()==id)return b;
    }return null;
}

    public void borrowbook(int bookid,int memberid)throws Exception{
    Books book=find_book_by_id(bookid);
    Members member=find_member_by_id(memberid);
    if(book==null)
        throw new booknotfound();
    if (!book.getIsAvailable())
        throw new bookisalreadybrowed();
    if(member==null)
        throw new membernotfound();
         book.setIsAvailable(false);
         member.borrowBook(book);
        System.out.println("member " + member.getName() + " borrowed book"+book.getTitle());
    }
    public void returnbook(int bookid,int memberid)throws Exception{
    Books book=find_book_by_id(bookid);
    Members member=find_member_by_id(memberid);
        if(book==null)
            throw new booknotfound();
        if (!member.getBorrowedBooks().contains(book))
            throw new bookisnotborrowedforthisuser();
        if(member==null)
            throw new membernotfound();
     member.returnBook(book);
     book.setIsAvailable(true);
     System.out.println("member " + member.getName() + " returned book"+book.getTitle());
    }
    public void list_available_books(){
    for (Books b : books){
        if(b.getIsAvailable())
            System.out.println(b);
    }
    }
    public void listborrowed_books(int memberid)throws Exception{
    Members member=find_member_by_id(memberid);
    if(member==null)
        throw new membernotfound();
    if(member.getBorrowedBooks().size()==0)
        throw new memberisnotborrwedanybooks("No books borrowed");
        System.out.println("borrowed books by " + member.getName());
    for (Books b : member.getBorrowedBooks()){
        System.out.println(b);
    }

    }
}
