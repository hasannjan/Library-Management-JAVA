package com.company;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

class Config {

    protected String dbUsername="dbmanager";
    protected String dbPassword="szabist";
}

class DBHandler extends Config{

    Connection dbConnection;

    public Connection getDbConnection() throws SQLException {

        String URL=               "jdbc:oracle:thin:@//localhost:1521/xepdb1    ";

        dbConnection = DriverManager.getConnection(URL,dbUsername,dbPassword);

        return dbConnection;
    }
}

public class DBreadNwrite {

    private static DBHandler dbHandler;
    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public  int returnMaxID(String table,String idColumnName) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        int maxID = 0;

        String readQuery = "SELECT " + idColumnName + " FROM " + table ;

        PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
        //Special kind of data type to store data from databases
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {

            if (resultSet.getInt(1) > maxID ) {

                maxID = resultSet.getInt((1));
            }
        }
        preparedStatement.close();
        return maxID;
    }

    public  int returnMinID(String table,String idColumnName) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();


        String readQuery = "SELECT " + idColumnName + " FROM " + table ;

        PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
        //Special kind of data type to store data from databases
        ResultSet resultSet = preparedStatement.executeQuery();

        int minID = 999999999;

        while(resultSet.next()) {

            if (resultSet.getInt(1) < minID ) {

                minID = resultSet.getInt((1));
            }
        }
        preparedStatement.close();
        return minID;
    }

    public  ArrayList<ArrayList<String>> readAllData(String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        ArrayList<ArrayList<String>> allrecords = new ArrayList<ArrayList<String>>();
        ArrayList<String> record = new ArrayList<String>();

        String readQuery = "SELECT * FROM " + table ;

        PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
        //Special kind of data type to store data from databases
        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData rsmd = resultSet.getMetaData();

        int count = rsmd.getColumnCount();


        while(resultSet.next()) {

            record = new ArrayList<String>();

            for (int i=1; i <= count; i++) {

                if (resultSet.getString(i) == null ) {

                    record.add("NULL");
                } else {

                    record.add(resultSet.getString(i).toLowerCase());
                }
            }
            //System.out.println(record);
            allrecords.add(record);

        }
        preparedStatement.close();
        return allrecords;
    }

    public ArrayList<String> readOnerecord(String username, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        ArrayList<String> record = new ArrayList<String>();

        String readQuery = "SELECT * FROM " + table;

        PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
        //Special kind of data type to store data from databases
        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData rsmd = resultSet.getMetaData();

        int count = rsmd.getColumnCount();

        while(resultSet.next()) {

            //System.out.println(resultSet.getString(3));
            if (resultSet.getString(3).toLowerCase().equals(username)) {

                for (int i = 1; i <= count; i++) {

                    record.add(resultSet.getString(i).toLowerCase());
                }
                //System.out.println(record);
                break;
            }
        }
        preparedStatement.close();
        return record;
    }

    public ArrayList<String> readOnerecord(int id, String table, String IDcolumnName) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        ArrayList<String> record = new ArrayList<String>();

        String readQuery = "SELECT * FROM " + table + " WHERE " + IDcolumnName + " = " + id;

        PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        int count = resultSet.getMetaData().getColumnCount();
        while(resultSet.next()) {

            for (int i = 1; i <= count; i++) {

                record.add(resultSet.getString(i).toLowerCase());
            }
        }
        preparedStatement.close();
        return record;
    }

    public boolean deleteOnelne(int id,String table, String idColumnName) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String deleteQuery = "DELETE FROM " + table + " WHERE " + idColumnName + "= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1,id);
        return preparedStatement.execute();
        //System.out.println("Users data is deleted successfully...");
    }

    public void updateOneLine_TriesNLocked(String username, String locked, String tries, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String updateQuery = "UPDATE " + table + " SET tries = ?, locked = ?" +
                " WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1,tries.toLowerCase());
        preparedStatement.setString(2,locked.toLowerCase());
        preparedStatement.setString(3,username.toLowerCase());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("Users data is updated successfully...");
    }
    public void updateOneLine_All(String id,String password, String locked, String tries, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String updateQuery = "UPDATE " + table + " SET password = ?, tries = ?, locked = ?" +
                " WHERE id = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1,password);
        preparedStatement.setInt(2,Integer.parseInt(tries));
        preparedStatement.setString(3,locked.toLowerCase());
        preparedStatement.setInt(4,Integer.parseInt(id));

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("Users data is updated successfully...");
    }

    public int createData(String username, String password, String locked, String tries, String question, String answer, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String insertQuery = "INSERT INTO " + table + "(id,timestamp,username,password,locked,tries,question,answer)" + "Values(?,LOCALTIMESTAMP(2),?,?,?,?,?,?)";

        int maxID = returnMaxID(table,"id");

        preparedStatement = connection.prepareStatement((insertQuery));

        preparedStatement.setInt(1,++maxID);
        preparedStatement.setString(2,username.toLowerCase());
        preparedStatement.setString(3,password);
        preparedStatement.setString(4,locked.toLowerCase());
        preparedStatement.setString(5,tries.toLowerCase());
        preparedStatement.setString(6,question.toLowerCase());
        preparedStatement.setString(7,answer.toLowerCase());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("\nNew account is inserted successfully in to the database!");
        return maxID;
    }
    public void enterNewBook(String title, String author, String isbn, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String insertQuery = "INSERT INTO " + table + "(bookid,timestamp,title,aurthor,isbn)" + "Values(?,LOCALTIMESTAMP(2),?,?,?)";

        int maxID = returnMaxID(table,"bookid");
        preparedStatement = connection.prepareStatement((insertQuery));
        preparedStatement.setInt( 1,++maxID);
        preparedStatement.setString(2,title.toLowerCase());
        preparedStatement.setString(3,author.toLowerCase());
        preparedStatement.setString(4,isbn.toLowerCase());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("\nNew book is inserted successfully in to the data base!");
    }
    public void enterNewDue(String title, String author, String isbn, String customer, String currdate, String duedate, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        int maxID = returnMaxID(table,"dueid");

        String insertQuery = "INSERT INTO " + table + "(dueid,timestamp,title,aurthor,isbn,customer,curdate,duedate)" + "Values(?,LOCALTIMESTAMP(2),?,?,?,?,to_date(?,'dd-MM-yyyy'),to_date(?,'dd-MM-yyyy'))";

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd-MM-uuuu").toFormatter();

        LocalDate currdateLD = LocalDate.parse(currdate, formatter);
        String currdateF = formatter.format(currdateLD);

        LocalDate duedateLD = LocalDate.parse(duedate,formatter);
        String duedateF = formatter.format(duedateLD);


        preparedStatement = connection.prepareStatement((insertQuery));
        preparedStatement.setInt(1,++maxID);
        preparedStatement.setString(2,title.toLowerCase());
        preparedStatement.setString(3,author.toLowerCase());
        preparedStatement.setString(4,isbn.toLowerCase());
        preparedStatement.setString(5,customer.toLowerCase());
        preparedStatement.setString(6,currdateF);
        preparedStatement.setString(7,duedateF);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("\nNew due is inserted successfully in to the data base!");
    }

    public void enterNewPatron(String title, String author, String isbn, String patron, String currdate, String duedate, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        int maxID = returnMaxID(table,"patid");

        String insertQuery = "INSERT INTO " + table + "(patid,title,timestamp,aurthor,isbn,patron,curdate,duedate,returndate)" + "Values(?,?,LOCALTIMESTAMP(2),?,?,?,to_date(?,'dd-MM-yyyy'),to_date(?,'dd-MM-yyyy'),to_date(?,'dd-MM-yyyy'))";

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("dd-MM-uuuu").toFormatter();

        LocalDate currdateLD = LocalDate.parse(currdate, formatter);
        String currdateF = formatter.format(currdateLD);

        LocalDate duedateLD = LocalDate.parse(duedate,formatter);
        String duedateF = formatter.format(duedateLD);

        preparedStatement = connection.prepareStatement((insertQuery));

        preparedStatement.setInt(1,++maxID);
        preparedStatement.setString(2,title.toLowerCase());
        preparedStatement.setString(3,author.toLowerCase());
        preparedStatement.setString(4,isbn.toLowerCase());
        preparedStatement.setString(5,patron.toLowerCase());
        preparedStatement.setString(6,currdateF);
        preparedStatement.setString(7,duedateF);
        preparedStatement.setString(8,"");

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("\nNew due is inserted successfully in to the data base!");
    }

    public void updateReturnDatePatron(int patid, String returndate, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();


        LocalDate returndateLD = LocalDate.parse(returndate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String returndateF = formatter.format(returndateLD);

        //System.out.println("RD: " + returndate + "RD-LD: " + returndateLD +  " RD-F:- " + returndateF + " patron: "+ patid);

        String updateQuery = "UPDATE " + table + " SET returndate = to_date(?,'dd-MM-yyyy')" +
                " WHERE patid = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1,returndateF);
        preparedStatement.setInt(2,patid);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("Users data is updated successfully...");
    }

    public void changeISBN(int id, String isbn, String table, String idColumnName) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String updateQuery = "UPDATE " + table + " SET isbn = ?" +
                " WHERE " + idColumnName + " = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1,isbn);
        preparedStatement.setInt(2,id);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("Users data is updated successfully...");

    }
    public void enterSecurityQuestion(String question, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String insertQuery = "INSERT INTO " + table + "(question)" + "Values(?)";

        preparedStatement = connection.prepareStatement((insertQuery));
        preparedStatement.setString(1,question);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        //System.out.println("\nNew due is inserted successfully in to the data base!");
    }

    public void updateAttendence(int id,String dayNum, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String updateQuery = "UPDATE " + table + " SET \"" + dayNum + "\" = 'P' WHERE id = ?";

        preparedStatement = connection.prepareStatement((updateQuery));
        //preparedStatement.setString(1,dayNum);
        preparedStatement.setInt(1,id);

        preparedStatement.executeUpdate();
        preparedStatement.close();

//        for (ArrayList<String> record : readAllData("attendence")) {
//
//            System.out.println(record);
//        }
    }

    public void enterNewEmp4Attendance(int id, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String deleteQuery = "DELETE FROM " + table + " WHERE id = " + id;
        preparedStatement = connection.prepareStatement((deleteQuery));
        preparedStatement.executeUpdate();

        String updateQuery = "INSERT INTO " + table + "(id,timestamp) values(?,LOCALTIMESTAMP(2))";


        preparedStatement = connection.prepareStatement((updateQuery));
        preparedStatement.setInt(1,id);

        preparedStatement.executeUpdate();
        preparedStatement.close();

//        for (ArrayList<String> record : readAllData("attendence")) {
//
//            System.out.println(record);
//        }
    }

    public void updateSalary(int id, int presentDays, int salary, int deduction, String table) throws SQLException, ClassNotFoundException {

        dbHandler = new DBHandler();
        connection = dbHandler.getDbConnection();

        String updateQuery = "UPDATE " + table + " SET workingdays = " + presentDays + ", totalSalary = " + salary + ", deduction = " + deduction + ", givenSalary = ? WHERE id = " + id;

        preparedStatement = connection.prepareStatement((updateQuery));
        preparedStatement.setInt(1,salary - deduction);
        preparedStatement.executeUpdate();
        preparedStatement.close();

//        for (ArrayList<String> record : readAllData(table)) {
//
//            System.out.println(record);
//        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // write your code here

        DBreadNwrite dbRW = new DBreadNwrite();

        dbRW.updateReturnDatePatron(88102,"2021-06-16","patrons");
    }
}
