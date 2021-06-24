package com.company;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class BookCollection {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        dueCollection(true);
    }
    public static ArrayList<ArrayList<String>> dueCollection(boolean emp) {
        try {
            int fineperday = 5;
            int totalCount = 0;
            int countDue = 0;
            final char[] delimiters = { ' ', '_' };

            LocalDate current = LocalDate.now();

            DBreadNwrite dbRW = new DBreadNwrite();

            ArrayList<ArrayList<String>> allrecords = dbRW.readAllData("dues");
            ArrayList<ArrayList<String>> passDueDate = new ArrayList<ArrayList<String>>();

            for (ArrayList<String> record : allrecords) {
                ++totalCount;
                //System.out.println(record.get(4));

                LocalDate dueDate = LocalDate.parse(record.get(6).substring(0,10));
                //System.out.println(dueDate);
                if (current.compareTo(dueDate) > 0) {
                    ++countDue;
                    Long fine = dueDate.until(current, ChronoUnit.DAYS) * fineperday;
                    //System.out.println( dueDate + " occurs after " + current);
                    record.add(Long.toString(dueDate.until(current, ChronoUnit.DAYS)));
                    record.add(Long.toString(fine));
                    passDueDate.add(record);

                }
            }
            if (emp) {
                System.out.println(countDue + " out of " + totalCount + " books are late.");
                for (ArrayList<String> record : passDueDate) {

                    System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters) + ", " + capitalizeFully(record.get(5), delimiters) + ", " + capitalizeFully(record.get(6), delimiters) + ", " + capitalizeFully(record.get(7), delimiters));

                }
            }

            return passDueDate;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
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
                System.out.println("By which type do you want to search: Title(1), Author(2), ISBN(3): ");
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
                    if (table.equals("bookcollection")) {
                        System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters));
                    }
                    else if (table.equals(("dues"))){
                        System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters) + ", " + capitalizeFully(record.get(5), delimiters) + ", " + capitalizeFully(record.get(6), delimiters) + ", " + capitalizeFully(record.get(7), delimiters) );
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void search(String table, String username) {
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
                System.out.println("By which type do you want to search: Title(1), Author(2), ISBN(3): ");
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
                } else {
                    System.out.println("Invalid option selected, try again.");
                }
            } while (correct == false);

            for (ArrayList<String> record : allrecords) {
                if (record.get(search).matches("(.*)" + searchVariable + "(.*)") && record.get(5).equals(username) ) {
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
                    if (table.equals("bookcollection")) {
                        System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters));
                    }
                    else if (table.equals(("dues"))){
                        System.out.println(record.get(0) + ", " + capitalizeFully(record.get(2), delimiters) + ", " + capitalizeFully(record.get(3), delimiters) + ", " + capitalizeFully(record.get(4), delimiters) + ", " + capitalizeFully(record.get(5), delimiters) + ", " + capitalizeFully(record.get(6), delimiters) + ", " + capitalizeFully(record.get(7), delimiters) );
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
