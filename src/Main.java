import model.Books;
import model.Members;

import service.Library;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try {  Library libraryv = new Library();
            libraryv.addBook(new Books("el34k el mamno3", "Khaled", 1));
            libraryv.addBook(new Books("kwa3ed el 34k el 40", "Gamal", 2));
            libraryv.addBook(new Books("hibta", "ahmed khaled tawfik", 3));
            libraryv.registerMember(new Members("Khaled", 1));
            libraryv.registerMember(new Members("Gamal", 2));

            while(true) {


                System.out.print("Choose num to make action : ");
                System.out.println("1 : borrow book \n 2 : list availble books \n 3 : list borrowed books by user \n 4: add book \n 5: return book \n 6:register new member \n 7: Exit ");
                int num = input.nextInt(), bookid, memberid;
                String btitle, bauther, namem;
                switch (num) {
                    case 1:
                        System.out.print("please enter book ID : ");
                        bookid = input.nextInt();
                        System.out.println("please enter member ID : ");
                        memberid = input.nextInt();

    libraryv.borrowbook(bookid, memberid);

                        break;
                    case 2:
                        libraryv.list_available_books();
                        break;
                    case 3:
                        System.out.print("please enter member ID : ");
                        memberid = input.nextInt();

                          libraryv.listborrowed_books(memberid);

                      break;
                    case 4:
                        System.out.print("please enter Book ID : ");
                        bookid = input.nextInt();
                        System.out.println("please enter the auther name");
                        bauther = input.nextLine();
                        System.out.println("please enter the book title");
                        btitle = input.nextLine();
                        
                            libraryv.addBook(new Books(btitle, bauther, bookid));
                        
                        break;
                    case 5:
                        System.out.println("please enter member ID : ");
                        memberid = input.nextInt();
                        System.out.println("please enter book ID  : ");
                        bookid = input.nextInt();

                            libraryv.returnbook(bookid, memberid);

                        break;
                    case 6:
                        System.out.println("please enter member ID : ");
                        memberid = input.nextInt();
                        System.out.println("please enter member name : ");
                        namem = input.next();
                        libraryv.registerMember(new Members(namem, memberid));
                        break;
                    case 7:
                        return ;
                        default:
                }
            }
        }catch (Exception e){
            System.err.println("Error");
        }
        }
    }
