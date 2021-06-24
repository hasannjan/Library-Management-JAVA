package com.company;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class Worker {

    public void addNewBook(String[] loggedin){

        try {

            Scanner in = new Scanner(System.in);
            Boolean alreadyIn = false;

            DBreadNwrite dbRW = new DBreadNwrite();
            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData("bookcollection");

            if (loggedin[1].equals("true") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {

                System.out.println("Enter book details: ");
                System.out.println("Enter title: ");
                String title = in.nextLine();
                in.nextLine();
                System.out.println("Enter author: ");
                String author = in.nextLine();
                in.nextLine();
                System.out.println("Enter ISBN: ");
                String isbn = in.nextLine();
                in.nextLine();
                for (ArrayList<String> record : allrecords) {

                    if (record.get(2).equals(title.toLowerCase()) & record.get(3).equals(author.toLowerCase())) {
                        System.out.println("A book with the same title and author is already in the collection.");
                        alreadyIn = true;
                        break;
                    }

                }
                if (alreadyIn == false){
                    dbRW.enterNewBook(title,author,isbn,"bookcollection");
                    System.out.println("New book added to the collection.");

                }

            }
            else if (loggedin[1].equals("false") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("workerdetails")) | !(loggedin[2].equals("admindetails"))) {
                System.out.println("You are a customer, only staff are allowed to to do exercise this feature.");
            }

        }catch (Exception err) {
            err.printStackTrace();
        }

    }
    public void deleteBook(String[] loggedin) {
        try {

            Scanner in = new Scanner(System.in);

            String delete = "";
            String confirmed = "",IDcolumnname = "", confirm = "";
            boolean invalid = false;
            int records = 0;
            int id;

            DBreadNwrite dbRW = new DBreadNwrite();
            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData("bookcollection");


            final char[] delimiters = {' ', '_'};

            if (loggedin[1].equals("true") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {

                System.out.println(capitalizeFully(loggedin[0], delimiters) + ", what do you want to delete from the DB?");
                System.out.println("Book from collection(1), Book from dues(2)");
                delete = in.nextLine();
                in.nextLine();

                if        (delete.equals("1")) {
                    confirmed = "bookcollection";
                    IDcolumnname = "bookid";
                } else if (delete.equals("2")) {
                    confirmed = "dues";
                    IDcolumnname = "dueid";
                } else {
                    System.out.println("Sorry, " + capitalizeFully(loggedin[0], delimiters) + " but you have entered a invalid option. ");
                    invalid = true;
                }

                if (invalid == false) {

                    allrecords = dbRW.readAllData(confirmed);

                    System.out.println("These are all the records of " + confirmed);

                    for (ArrayList<String> record : allrecords) {
                        System.out.println(record);
                        ++records;
                    }

                    System.out.println("Enter the ID number of the record you want to delete from " + confirmed);
                    id = in.nextInt();
                    in.nextLine();

//                    if (id > records){
//                        System.out.println("Sorry, you have entered a out of range ID.");
//                    }
       //             else {
                        System.out.println("Are you sure you want to delete " + id + " from " + confirmed + " (y/n): ");
                        confirm = in.nextLine();
                        in.nextLine();
                        if (confirm.equals("y")) {

                            dbRW.deleteOnelne(id, confirmed, IDcolumnname);
                            //System.out.println();
                            System.out.println(id + " is deleted from " + confirmed);
                        }
                        else {
                            System.out.println("Okay, It is not deleted.");
                        }
                   // }
                }

            }
            else if (loggedin[1].equals("false") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("workerdetails")) | !(loggedin[2].equals("admindetails"))) {
                System.out.println("You are a customer, only staff are allowed to to do exercise this feature.");
            }

        }catch (Exception err) {
            err.printStackTrace();
        }

    }

    public void changeISBN(String[] loggedin) {
        try {

            Scanner in = new Scanner(System.in);

            boolean alreadyasigned = false;
            int id;
            String newisbn;

            BookCollection a1 = new BookCollection();
            DBreadNwrite dbRW = new DBreadNwrite();
            int bokColorDues = 0;

            ArrayList<ArrayList<String>> allrecords;
            String tableDueorBokCol =  "", idColumnName = "";
            while (bokColorDues != 1 & bokColorDues != 2 ) {

                System.out.println("From book collection(1) or from dues(2)? ");
                bokColorDues = in.nextInt();
                in.nextLine();

                if (bokColorDues == 1) {

                    System.out.println("Book collection selected..\n");
                    tableDueorBokCol = "bookcollection";
                    idColumnName = "bookid";

                }
                else if (bokColorDues == 2) {

                    System.out.println("Dues selected..\n");
                    tableDueorBokCol = "dues";
                    idColumnName = "dueid";
                }
                else {

                    System.out.println("Invalid option... Try again...");
                }
            }

            allrecords = dbRW.readAllData(tableDueorBokCol);

            if (loggedin[1].equals("true") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {

                System.out.println("Using the search function find the book you want change the isbn of: ");

                a1.search(tableDueorBokCol);

                do {
                    
                    System.out.println("Enter the ID assigned to the book: ");
                    id = in.nextInt();
                    in.nextLine();

                    System.out.println("Enter the new ISBN/barcode you want to assign to the book: ");
                    newisbn = in.nextLine();
                    in.nextLine();

                    for (ArrayList<String> record : allrecords) {

                        //System.out.println(Integer.parseInt(record.get(0)) + " : " + Integer.parseInt(record.get(0)) == id + " : " + record.get(4));

                        if (Integer.parseInt(record.get(0)) != id && record.get(4).equals(newisbn)) {
                            System.out.println("ISBN is already assigned to another book.");
                            alreadyasigned = true;
                        }
                        else if (Integer.parseInt(record.get(0)) == id && record.get(4).equals(newisbn)) {
                            System.out.println("That book with the " + id + " already has the that ISBN assgined to it.");
                            alreadyasigned = true;
                        }
                    }

                    if (alreadyasigned == false) {

                        dbRW.changeISBN(id,newisbn,tableDueorBokCol,idColumnName);
                        System.out.println("ISBN successfully changed.");
                        break;
                    }

                    alreadyasigned = false;
                } while(!alreadyasigned);
            }
            else if (loggedin[1].equals("false") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("workerdetails")) | !(loggedin[2].equals("admindetails"))) {
                System.out.println("You are a customer, only staff are allowed to to do exercise this feature.");
            }

        }catch (Exception err) {
            err.printStackTrace();
        }

    }

    public static void search(String table) {
        try {
            Boolean correct = false;
            Boolean matchedBol = false;
            int search = -99;
            String searchVariable = "";
            int matchedCount = 0;
            Scanner in = new Scanner(System.in);

            DBreadNwrite dbRW = new DBreadNwrite();

            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData(table);

            ArrayList<ArrayList<String>> matched = new ArrayList<ArrayList<String>>();

            final char[] delimiters = { ' ', '_' };

            do {
                System.out.println("By which type do you want to search: Title(1), Author(2), ISBN(3), Patron name(4): ");
                int type = in.nextInt();
                in.nextLine();
                if (type == 1) {
                    search = 2;
                    correct = true;
                    System.out.println("Searching by title name.");
                    System.out.println("Enter title name: ");
                    searchVariable = in.nextLine();
                } else if (type == 2) {
                    search = 3;
                    correct = true;
                    System.out.println("Searching by author name.");
                    System.out.println("Enter author name: ");
                    searchVariable = in.nextLine();
                } else if (type == 3) {
                    search = 4;
                    correct = true;
                    System.out.println("Searching by ISBN.");
                    System.out.println("Enter ISBN: ");
                    searchVariable = in.nextLine();
                } else if (type == 4) {
                    search = 5;
                    correct = true;
                    System.out.println("Searching by patron name.");
                    System.out.println("Enter patron name: ");
                    searchVariable = in.nextLine();
                } else {
                    System.out.println("Invalid option selected, try again.");
                }
            } while (correct == false);

            for (ArrayList<String> record : allrecords) {
                if (record.get(search).matches("(.*)" + searchVariable + "(.*)" )) {
                    matched.add(record);
                    matchedBol = true;
                    ++matchedCount;
                }
            }
            if (matchedBol == false) {
                System.out.println("Sorry, nothing matched with " + searchVariable);
            }
            else {
                System.out.println(matchedCount + " have been found.");
                for (ArrayList<String> record : matched) {

                    System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters) + ", " + capitalizeFully(record.get(5), delimiters) + ", " + capitalizeFully(record.get(6), delimiters) + ", " + capitalizeFully(record.get(7), delimiters)+ ", " + capitalizeFully(record.get(8), delimiters));

                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void createAcc(String[] loggedin, String table) {

        try {

            String username , password1,password2, question, answer;
            boolean correct = true;

            Scanner in = new Scanner(System.in);
            DBreadNwrite dbRW = new DBreadNwrite();

            ArrayList<ArrayList<String>> questions = dbRW.readAllData("securityquestions");

            if (loggedin[1].equals("true") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {

                System.out.println("Enter username: ");
                username = in.nextLine();
                in.nextLine();

                do {
                    if (correct == false) {
                        System.out.println("Passwords do not match, try again.");

                    }
                    correct = false;
                    System.out.println("New password: ");
                    password1 = in.nextLine();
                    in.nextLine();

                    System.out.println("Re-confirm password: ");
                    password2 = in.nextLine();
                    in.nextLine();

                    if (password1.equals(password2)) {
                        correct = true;
                    }
                } while (!(password1.equals(password2)));

                int randomInt = (int)(10.0 * Math.random());
                question = questions.get(randomInt).get(1);

                System.out.println(question + " ");
                answer = in.nextLine();
                in.nextLine();

                int maxID = dbRW.createData(username,password1,"false","0",question,answer,table);

                if ( table.equals("workerdetails")) {

                    dbRW.enterNewEmp4Attendance(maxID,"attendence");
                    System.out.println("New entry is made for " + username + " in the attendance table...");
                }
                System.out.println("New account added in " + table + ". ");
            }
            else if (loggedin[1].equals("false") & ((loggedin[2].equals("workerdetails")) | (loggedin[2].equals("admindetails")))) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("workerdetails")) | !(loggedin[2].equals("admindetails"))) {
                System.out.println("You are a customer, only staff are allowed to to do exercise this feature.");
            }
        }catch (Exception err) {
            err.printStackTrace();
        }

    }

    public void checkSalary(String[] loggedin) throws SQLException, ClassNotFoundException {

        DBreadNwrite dbRW = new DBreadNwrite();

        if (loggedin[1].equals("true") & (loggedin[2].equals("admindetails") || loggedin[2].equals("workerdetails"))) {

            ArrayList<String> record = dbRW.readOnerecord(Integer.parseInt(loggedin[3]),"salary", "id");

            String salary = record.get(5), workingdays = record.get(2), deductions = record.get(4);
            System.out.println("Your total pay is " + salary + " PKR for " + workingdays + " days of work after deductions of " + deductions + " PKR...\n") ;
        }
    }
}

