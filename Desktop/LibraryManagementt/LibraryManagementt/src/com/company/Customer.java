package com.company;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class Customer {

    public boolean checkIn(String[] loggedin){
        try {
            int totalFine = 0;
            Boolean overDueBooks = false;

            ArrayList<ArrayList<String>> allrecords = BookCollection.dueCollection(false);
            ArrayList<ArrayList<String>> customDues = new ArrayList<ArrayList<String>>();

            final char[] delimiters = {' ', '_'};

            if (loggedin[1].equals("true") & loggedin[2].equals("customerdetails")) {

                for (ArrayList<String> record : allrecords) {
                    if (loggedin[0].equals(record.get(5))) {
                        totalFine += parseInt(record.get(8));
                        customDues.add(record);
                        overDueBooks = true;
                    }
                }
                if (overDueBooks == true) {
                    System.out.println(capitalizeFully(loggedin[0], delimiters) + ", you have not returned these books to the library and also not paid the fines, so if you want borrow more you have to return all these first.");
                    for (ArrayList<String> record : customDues) {
                        System.out.println(capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters));
                    }
                    System.out.println("Your total fine is " + totalFine + " PKR.");
                }
                else {
                    System.out.println(capitalizeFully(loggedin[0], delimiters) + ", you have no overdues. ");

                }
            }
            else if (loggedin[1].equals("false") & loggedin[2].equals("customerdetails")) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("customerdetails"))) {
                System.out.println("You are not a customer, only customer are allowed to check-in/check-out.");
            }
            return overDueBooks;

        }catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }

    public void checkOut(String[] loggedin) {

        try {
            final char[] delimiters = { ' ', '_' };
            System.out.println("Thank you for choosing to do business with us, " + capitalizeFully(loggedin[0], delimiters) + ".");
            System.out.println("We hope that you come again.");
            System.exit(0);

        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    public void borrowBook(String[] loggedin, Boolean overDue) {

        try {

            if (overDue) {

                System.out.println("You have over due book/books, return those first so you can borrow more. ");
                return;
            }

            final char[] delimiters = { ' ', '_' };
            String bookID = "", weeksBorrowed;
            Boolean invalidoption = true, notfound = true;

            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate current = LocalDate.now();
            String currdate = current.format(format1);


            BookCollection bookCol = new BookCollection();
            DBreadNwrite dbRW = new DBreadNwrite();
            Scanner in = new Scanner(System.in);

            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData("bookcollection");
            if (overDue == false) {

                System.out.println("Search the book you to borrow from the library. ");
                bookCol.search("bookcollection");

                System.out.println("Enter the ID number of the book you want to borrow: ");
                bookID = in.nextLine();
            }
            for (ArrayList<String> record: allrecords) {

                if(record.get(0).equals(bookID) & overDue == false) {

                    notfound = false;
                    System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters));
                    System.out.println("Book is found.");
                    do {
                        System.out.println("How many weeks do you want to borrow it for? ");
                        weeksBorrowed = in.nextLine();
                        in.nextLine();

                        if (parseInt(weeksBorrowed) > 24) {
                            System.out.println("Books can not be borrowed for more than 6 months(24 weeks). ");
                        } else if (parseInt(weeksBorrowed) < 1) {
                            System.out.println("Books can not be borrowed for less than 1 week. ");

                        } else {
                            dbRW.deleteOnelne(parseInt(bookID), "bookCollection", "bookid");
                            String duedate = current.plusWeeks(parseInt(weeksBorrowed)).format(format1);

                            //System.out.println(currdate + " " + duedate);

                            dbRW.enterNewDue(record.get(2), record.get(3), record.get(4), loggedin[0], currdate, duedate, "dues");
                            dbRW.enterNewPatron(record.get(2), record.get(3), record.get(4), loggedin[0], currdate, duedate, "patrons");
                            System.out.println("You will have to return the book by " + duedate + " and pay a fees of 2PKR for each day borrowed. ");
                            invalidoption = false;
                        }
                    } while (invalidoption);

                }
            }
            if (notfound && overDue == false ) {

                System.out.println("There is no book with that ID, sorry. ");
            }



        }catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void returnBook(String[] loggedin) {

        try {

            int fineperday0 = 2, fineperday = 5;

            final char[] delimiters = { ' ', '_' };
            String bookID;
            Boolean notfound = true;

            LocalDate currdate = LocalDate.now();
            //String currdate = current.format(format1);

            BookCollection bookCol = new BookCollection();
            DBreadNwrite dbRW = new DBreadNwrite();
            Scanner in = new Scanner(System.in);
            LocalDate dueDate, borrowDate;
            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData("dues");



                System.out.println("Search the book you want to return. ");
                bookCol.search("dues",loggedin[0]);

                System.out.println("Enter the ID number of the book you want to return: ");
                bookID = in.nextLine();

                in.nextLine();

                for (ArrayList<String> record: allrecords) {

                    if (loggedin[0].equals(record.get(5)) & record.get(0).equals(bookID)) {
                        notfound = false;
                        borrowDate = LocalDate.parse(record.get(6).substring(0,10));
                        dueDate = LocalDate.parse(record.get(7).substring(0,10));

                        String title = record.get(2);
                        String author = record.get(3);
                        //String isbn = record.get(4);

                        System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters));
                        if (currdate.compareTo(dueDate) > 0) {

                            Long fine1 = borrowDate.until(dueDate, ChronoUnit.DAYS) * fineperday0;
                            Long fine2 = dueDate.until(currdate, ChronoUnit.DAYS) * fineperday + fine1;
                            System.out.println("This book is over due, you will pay: " + fine2 + " PKR");

                            dbRW.enterNewBook(record.get(2), record.get(3), record.get(4), "bookcollection");
                            dbRW.deleteOnelne(parseInt(bookID), "dues", "dueID");
                        } else if (currdate.compareTo(dueDate) <= 0) {

                            Long fine1 = borrowDate.until(currdate, ChronoUnit.DAYS) * fineperday0;
                            System.out.println("This book is not overdue, you will pay: " + fine1 + " PKR");

                        }

                        ArrayList<ArrayList<String>> allrecordsPatrons = dbRW.readAllData("patrons");
                        for (ArrayList<String> recordPatron : allrecordsPatrons) {

                            if (loggedin[0].equals(record.get(5)) & recordPatron.get(2).equals(title) & recordPatron.get(3).equals(author) ) {

                                dbRW.updateReturnDatePatron(parseInt(recordPatron.get(0)), currdate.toString(), "patrons");

                            }
                        }

                        //dbRW.enterNewBook(record.get(2), record.get(3), record.get(4), "bookcollection");
                        //dbRW.deleteOnelne(parseInt(bookID), "dues", "dueid");
                    }

                }
                if (notfound) {

                    System.out.println("There is no book with that ID, sorry. ");
                }
        }catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // write your code here

        //Customer a = new Customer();
        //a.borrowBook(new String[]{"gol"},false);
    }
}
