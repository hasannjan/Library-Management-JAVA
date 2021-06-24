package com.company;


import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // write your code here

        DBreadNwrite a = new DBreadNwrite();

        System.out.println(a.readAllData("admindetails"));
        System.out.println();

        System.out.println(a.readAllData("workerdetails"));
        System.out.println();

        System.out.println(a.readAllData("customerdetails"));
        System.out.println();

        LogIn log = new LogIn();

        log.FirstMethod();

            /*System.out.println(a.readAllData("customerdetails"));
            System.out.println();
            System.out.println(a.readAllData("bookcollection"));
            System.out.println();
            System.out.println(a.readAllData("dues"));
            System.out.println();
            System.out.println(a.readAllData("patrons"));
            System.out.println();
            */
    }
}
