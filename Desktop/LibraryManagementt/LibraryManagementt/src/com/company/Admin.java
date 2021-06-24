package com.company;


import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

public class Admin extends Worker {

    public void deleteAnything(String[] loggedin) {

        try {

            String delete = "";
            String confirmed = "", IDcolumnname = "id", confirm = "";
            boolean invalid = false;
            int records = 0;
            int id;

            Scanner in = new Scanner(System.in);
            DBreadNwrite dbRW = new DBreadNwrite();

            ArrayList<ArrayList<String>> allrecords;

            final char[] delimiters = {' ', '_'};


            if (loggedin[1].equals("true") & loggedin[2].equals("admindetails")) {

                System.out.println(capitalizeFully(loggedin[0], delimiters) + ", what do you want to delete from the DB?");
                System.out.println("Worker(1), Customer(2)");
                delete = in.nextLine();
                in.nextLine();

                if        (delete.equals("1")) {
                    confirmed = "workerdetails";

                } else if (delete.equals("2")) {
                    confirmed = "customerdetails";

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

                    int maxID = dbRW.returnMaxID(confirmed,IDcolumnname);
                    int minID = dbRW.returnMinID(confirmed,IDcolumnname);

                    //System.out.println(minID + " : " + maxID);
                    if (id < minID || maxID < id ){

                        System.out.println("Sorry, you have entered a out of range ID.");
                    }
                    else {
                        System.out.println("Are you sure you want to delete " + id + " from " + confirmed + " (y/n): ");
                        confirm = in.nextLine();
                        in.nextLine();
                        if (confirm.equals("y")) {
                            dbRW.deleteOnelne(id, confirmed, IDcolumnname);
                            System.out.println(id + " is deleted from " + confirmed);
                        }
                        else {
                            System.out.println("Okay, It is not deleted.");
                        }
                    }
                } else {
                    System.out.println("Invalid option, please enter a valid option. ");
                    deleteAnything(loggedin);
                }

            }
            else if (loggedin[1].equals("false") & loggedin[2].equals("admindetails")) {
                System.out.println("You have entered the wrong credentials.");
            }
            else if (!(loggedin[2].equals("admindetails"))) {
                System.out.println("You are not a admin, only customer are allowed to check-in/check-out.");
            }


        }catch (Exception err) {
            err.printStackTrace();
        }

    }

    public ArrayList<ArrayList<String>> printAttendance(String[] loggedin, Boolean print) throws SQLException, ClassNotFoundException {

        DBreadNwrite dbRW = new DBreadNwrite();
        ArrayList<ArrayList<String>> allAttendance = new ArrayList<ArrayList<String>>();

        if (loggedin[1].equals("true") & loggedin[2].equals("admindetails")) {

            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd");
            LocalDate current = LocalDate.now();
            String currDay = current.format(format1);

            int currDayInt = Integer.parseInt(currDay);
           // System.out.println("CurrDayInt: " + currDayInt);

            ArrayList<ArrayList<String>> allRecords = dbRW.readAllData("attendence");

            for (ArrayList<String> record : allRecords) {

                ArrayList<String> attendance = new ArrayList<String>();
                attendance.add(0,record.get(0));

                int presentCount = 0, absentCount = 0, totalCount = 0;
                for (int i = 2; i < currDayInt+2; i++) {

                    if (record.get(i).equals("p")) {

                        ++presentCount;
                    }
                    if (record.get(i).equals("u")) {

                        ++absentCount;
                    }
                    ++totalCount;

                }
                attendance.add( 1,Integer.toString(presentCount) );
                attendance.add( 2,Integer.toString(absentCount)  );
                if (print)
                    System.out.println(record.get(0) + " has been marked present for " + presentCount + " days and marked absent for " + absentCount + " days out of a total of  " + totalCount + " days. ");
                allAttendance.add(attendance);
            }
            System.out.println();
        }
        return allAttendance;
    }

    public void resetAttendence(String[] loggedin) throws SQLException, ClassNotFoundException {

        DBreadNwrite dbRW = new DBreadNwrite();

        if (loggedin[1].equals("true") & loggedin[2].equals("admindetails")) {

            int[] numDayofMonths = {31,28,31,30,31,30,31,31,30,31,30,31};
            int currMonth = Integer.parseInt( LocalDate.now().format(DateTimeFormatter.ofPattern("MM")) );
            int currDay   = Integer.parseInt( LocalDate.now().format(DateTimeFormatter.ofPattern("dd")) );
            String confirm = "u";
            Scanner in = new Scanner(System.in);

            if (currDay < numDayofMonths[currMonth-1]) {

                System.out.println("Are you sure you want to do this? The current month has not ended... (y/n)");
                confirm = in.nextLine();
                in.nextLine();
            }
            if ( confirm.equals("y") || confirm.equals("u") ) {

                ArrayList<ArrayList<String>> allRecords = dbRW.readAllData("workerdetails");
                for (ArrayList<String> record : allRecords) {

                    dbRW.enterNewEmp4Attendance(Integer.parseInt(record.get(0)),"attendence");
                }

                allRecords = dbRW.readAllData("admindetails");
                for (ArrayList<String> record : allRecords) {

                    dbRW.enterNewEmp4Attendance(Integer.parseInt(record.get(0)),"attendence");
                }

                System.out.println("The reset has been completed.");
            }
            else {
                System.out.println("Okay, the reset has been halted.");
            }
        }
    }

    public void calculateSalaries(String[] loggedin, int workerSalary, int adminSalary) throws SQLException, ClassNotFoundException {

        DBreadNwrite dbRW = new DBreadNwrite();

        if (loggedin[1].equals("true") & loggedin[2].equals("admindetails")) {

            Scanner in = new Scanner(System.in);
            Boolean  backUP = false;
            int deductionPerAbsentee = 0;

            System.out.println("Is there a absentee deduction from salary? Press 'y' for yes or anything else besides 'y' for no...");
            String absenteeBol = in.nextLine();
            in.nextLine();
            //System.out.println(absenteeBol + " : " + absenteeBol.equals('y') );
            if ( absenteeBol.equals("y") == true ) {

                //System.out.println("X");
                do {
                    System.out.println("How much is the absentee deduction per day? max is 200.");
                    deductionPerAbsentee = in.nextInt();
                    in.nextLine();
                    backUP = false;

                    if (deductionPerAbsentee > 200) {

                        System.out.println("Maximum is 200 PKR per day");
                        backUP = true;
                    }
                } while (backUP == true);
            }
            ArrayList<ArrayList<String>> allAttendance = printAttendance(loggedin,false);


            for (ArrayList<String> attendance : allAttendance) {

                //System.out.println( "l: " + attendance.get(0) );
                if ( attendance.get(0).matches("22(.*)") ) {

                    //System.out.println("2");
                    dbRW.updateSalary( Integer.parseInt(attendance.get(0)), Integer.parseInt(attendance.get(1)), Integer.parseInt(attendance.get(1)) * workerSalary, Integer.parseInt(attendance.get(2)) * deductionPerAbsentee, "salary");
                }
                if (attendance.get(0).matches("11(.*)")) {

                    //System.out.println("3");
                    dbRW.updateSalary(Integer.parseInt(attendance.get(0)), Integer.parseInt(attendance.get(1)), Integer.parseInt(attendance.get(1)) * adminSalary  , Integer.parseInt(attendance.get(2)) * deductionPerAbsentee, "salary");
                }
            }
            System.out.println("Salaries of all the employees has been updated...");
        }

//        for (ArrayList<String> record : dbRW.readAllData("salary")) {
//
//            System.out.println(record);
//        }
    }
}
