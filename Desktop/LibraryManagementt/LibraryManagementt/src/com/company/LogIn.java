package com.company;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

// Java program to create a blank text field with a
// given initial text and given number of columns


public class LogIn{


    public void FirstMethod() throws SQLException, ClassNotFoundException {

        String[] loggedin = login();
        final char[] delimiters = { ' ', '_' };
        String usernameTC = capitalizeFully(loggedin[0], delimiters);

        Scanner in = new Scanner(System.in);
        String type = "";
        BookCollection bookCol;

        if (loggedin[1].equals("true") & loggedin[2].equals("admindetails")) {

            Admin admin = new Admin();
            bookCol = new BookCollection();

            do {

                System.out.println("What do you want to do, " + usernameTC + "?");
                System.out.println(" Delete an account(1)," +
                        "\n Add new book(2)," +
                        "\n Delete book(3)," +
                        "\n Change ISBN(4)," +
                        "\n Search bookcollection(5)," +
                        "\n Search dues(6)," +
                        "\n Search patrons(7)," +
                        "\n Create worker account(8)," +
                        "\n Create customer account(9)," +
                        "\n Due collection(10)," +
                        "\n Check attendance(11)," +
                        "\n Reset attendance sheet(12)," +
                        "\n Update salaries(13)," +
                        "\n Check your own salary(14)," +
                        "\n Exit(15): \n");
                type = in.nextLine();

                in.nextLine();

                switch (type) {

                    case "1":
                        admin.deleteAnything(loggedin);
                        break;
                    case "2":
                        admin.addNewBook(loggedin);
                        break;
                    case "3":
                        admin.deleteBook(loggedin);
                        break;
                    case "4":
                        admin.changeISBN(loggedin);
                        break;
                    case "5":
                        bookCol.search("bookcollection");
                        break;
                    case "6":
                        bookCol.search("dues");
                        break;
                    case "7":
                        admin.search("patrons");
                        break;
                    case "8":
                        admin.createAcc(loggedin, "workerdetails");
                        break;
                    case "9":
                        admin.createAcc(loggedin, "customerdetails");
                        break;
                    case "10":
                        bookCol.dueCollection(true);
                        break;
                    case "11":
                        admin.printAttendance(loggedin,true);
                        break;
                    case "12":
                        admin.resetAttendence(loggedin);
                        break;
                    case "13":
                        int workerSalary = 54;
                        int adminSalary = 100;
                        admin.calculateSalaries(loggedin,workerSalary,adminSalary);
                        break;
                    case "14":
                        admin.checkSalary(loggedin);
                        break;
                    case "15":
                        System.out.println("Program shutting down... ");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice, please enter a valid choice. ");
                        break;
                }
            } while(true);
        }
        else if (loggedin[1].equals("true") & loggedin[2].equals("workerdetails")) {

            Worker worker = new Worker();
            bookCol = new BookCollection();

            do {

                System.out.println("What do you want to do, " + usernameTC + "?");
                System.out.println(" Add a new book(1), " +
                        "\n Delete book(2)," +
                        "\n Change ISBN(3)," +
                        "\n Search bookcollection(4)," +
                        "\n Search dues(5)," +
                        "\n Search patrons(6)," +
                        "\n Create customer account(7)," +
                        "\n Due collection(8)," +
                        "\n Check your own salary(9)," +
                        "\n Exit(10): ");
                type = in.nextLine();

                in.nextLine();

                switch (type) {

                    case "1":
                        worker.addNewBook(loggedin);
                        break;
                    case "2":
                        worker.deleteBook(loggedin);
                        break;
                    case "3":
                        worker.changeISBN(loggedin);
                        break;
                    case "4":
                        bookCol.search("bookcollection");
                        break;
                    case "5":
                        bookCol.search("dues");
                        break;
                    case "6":
                        worker.search("patrons");
                        break;
                    case "7":
                        worker.createAcc(loggedin, "customerdetails");
                        break;
                    case "8":
                        bookCol.dueCollection(true);
                        break;
                    case "9":
                        worker.checkSalary(loggedin);
                        break;
                    case "10":
                        System.out.println("Program shutting down... ");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice, please enter a valid choice. ");
                        break;
                }
            } while(true);
        }
        else if (loggedin[1].equals("true") & loggedin[2].equals("customerdetails")) {

            Customer customer = new Customer();
            bookCol = new BookCollection();
            do {

                System.out.println("What do you want to do, " + usernameTC + "?");
                System.out.println(" Check in (1)" +
                        "\n Search (2)" +
                        "\n Borrow book (3)" +
                        "\n Return book (4)" +
                        "\n Check out (5) \n");
                type = in.nextLine();

                in.nextLine();

                switch (type) {

                    case "1":
                        customer.checkIn(loggedin);
                        break;
                    case "2":
                        bookCol.search("bookcollection");
                        break;
                    case "3":
                        Boolean overDue = customer.checkIn(loggedin);
                        customer.borrowBook(loggedin,overDue);
                        break;
                    case "4":
                        customer.returnBook(loggedin);
                        break;
                    case "5":
                        customer.checkOut(loggedin);
                        break;
                    default:
                        System.out.println("Invalid choice, please enter a valid choice. ");
                        break;
                }

            } while(true);
        }
        else {

            Boolean invalidoption = false;

            do {
                System.out.println("Do you want to try to login again(1), verify account(2) or shutdown the program(3): ");
                type = in.nextLine();

                in.nextLine();
                if (type.equals("1")) {
                    System.out.println("Restarting the program.");
                    FirstMethod();
                } else if (type.equals("2")) {
                    System.out.println("Starting verify account: ");
                    verifyAccount();
                } else if (type.equals("3")) {
                    System.out.println("Program shutting down. ");
                    System.exit(0);
                } else {
                    System.out.println("Invalid choice. Please enter a valid option. ");
                    invalidoption = true;
                }
            } while (invalidoption);
        }

    }

    public String[] login() {
        try {

            DBreadNwrite dbRW = new DBreadNwrite();
            Scanner in = new Scanner(System.in);
            String type = "";

            //default type --> customer
            String table = "customerdetails";



            System.out.println("Login process: ");
            System.out.println("Are you a admin(1), a worker(2) or a customer(3): ");
            type = in.nextLine();

            in.nextLine();

            if (type.equals("1")) {
                table = "admindetails";
                System.out.println("Please enter your admin credentials.");
            }
            else if (type.equals("2")) {
                table = "workerdetails";
                System.out.println("Please enter your worker credentials.");
            }
            else {
                System.out.println("Please enter your customer credentials.");
            }

            System.out.println("Enter username: ");
            String username = in.nextLine();
            in.nextLine();
            System.out.println("Enter password: ");
            String password = in.nextLine();
            in.nextLine();

            Boolean loggedin = false;
            int id = 0;

            ArrayList<String> record = dbRW.readOnerecord(username,table);

            final char[] delimiters = { ' ', '_' };
            String usernameTC = capitalizeFully(username, delimiters);

            if (record.isEmpty() == true ) {
                System.out.println("Incorrect username/password, please try again.");
            }
            else {
                //System.out.println(record.get(4));
                int tries = Integer.parseInt(record.get(5));

                if ((record.get(2).equals(username)) & (record.get(3).equals(password)) & (record.get(4).equals("false"))) {
                    record.set(5, "0");
                    loggedin = true;
                    System.out.println("Hello, " + usernameTC);
                    id = Integer.parseInt(record.get(0));

                    if (type.equals("1") || type.equals("2")) {

                        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd");
                        LocalDate current = LocalDate.now();
                        String currDay = current.format(format1);
                        //System.out.println("currDay: " + currDay);

                        dbRW.updateAttendence(id, currDay, "attendence");
                        System.out.println("Your attendance has been marked present for today, " + usernameTC);
                    }
                } else if ((record.get(2).equals(username)) & (!record.get(3).equals(password)) & (record.get(4).equals("false"))) {
                    record.set(5, Integer.toString(++tries));
                    System.out.println(usernameTC + " ," + (4 - tries) + " try/tries left after which your account will be locked.");

                } if ((record.get(2).equals(username)) & (!record.get(3).equals(password)) & (record.get(4).equals("false")) & (parseInt(record.get(5))>=4)) {
                    record.set(4, "true");
                    record.set(5, "0");
                    System.out.println(usernameTC + ", your account is now locked. Please verify your account to unlock your account.");

                }
                else if ((record.get(2).equals(username)) & (record.get(4).equals("true"))) {
                    System.out.println();
                    System.out.println(usernameTC + ", your account is locked. Please verify your account to unlock your account.");

                }

                dbRW.updateOneLine_TriesNLocked(record.get(2), record.get(4), record.get(5), table);
            }
            //System.out.println(loggedin);
            String[] returnVariable = {username, Boolean.toString(loggedin), table, Integer.toString(id)};
            return returnVariable;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void verifyAccount() {
        try {
            DBreadNwrite dbRW = new DBreadNwrite();
            Scanner in = new Scanner(System.in);

            //default type --> customer
            String table = "customerdetails";

            String type = "";

            System.out.println("Verification process: ");
            System.out.println("Are you a admin(1), a worker(2) or a customer(3): ");
            type = in.nextLine();
            in.nextLine();

            if (type.equals("1")) {
                table = "admindetails";
                System.out.println("Please enter your admin credentials.");
            }
            else if (type.equals("2")) {
                table = "workerdetails";
                System.out.println("Please enter your worker credentials.");
            }
            else {
                System.out.println("Please enter your customer credentials.");
            }

            System.out.println("Enter username: ");
            String username = in.nextLine();
            in.nextLine();

            Boolean correct2 = true;
            ArrayList<String> record = dbRW.readOnerecord(username,table);

            final char[] delimiters = { ' ', '_' };
            String usernameTC = capitalizeFully(username, delimiters);

            //System.out.println("record: "+ record);

            if (record.isEmpty() == true ) {
                System.out.println("Sorry but it seems like there is no account with the username: " + usernameTC);
            }
            else {

                if ( (record.get(2).equals(username)) && (record.get(4).equals("false")) ) {

                    System.out.println(usernameTC + ", your account is already unlocked.");
                }
                else if ( (record.get(2).equals(username)) && (record.get(4).equals("true")) ) {
                    System.out.println("You will have to answer the security question correctly to reset your password.");

                    String answer;
                    do {

                        System.out.println(record.get(6));
                        answer = in.nextLine();
                        if (answer.equals(record.get(7))) {
                            String newpassword1;
                            String newpassword2;

                            System.out.println("Security question answered correctly. Now reset your password.");

                            System.out.println();
                            do {
                                if (correct2 == false) {
                                    System.out.println("Passwords do not match, try again.");

                                }
                                correct2 = false;
                                System.out.println("New password: ");


                                newpassword1 = in.nextLine();
                                in.nextLine();

                                System.out.println("Re-confirm password: ");


                                newpassword2 = in.nextLine();
                                if (newpassword1.equals(newpassword2)) {
                                    correct2 = true;
                                    record.set(3,newpassword1);
                                    record.set(4, "false");

                                    //System.out.println("record: "+ record);
                                    dbRW.updateOneLine_All(record.get(0),record.get(3),record.get(4),record.get(5),table);
                                    System.out.println("Password successfully reset.");
                                }
                            } while (!(newpassword1.equals(newpassword2)));

                            //System.out.println("Password successfully reset.");
                            break;
                        }
                        else {
                            System.out.println("Answer incorrect, try again.");

                        }
                    }
                    while (!(answer.equals(record.get(5)))) ;

                }
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
