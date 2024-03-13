package com.example.cashcraft;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Makeconnection {
    private static final String databaselocation = HelloApplication.class.getResource("database.db").toExternalForm();
    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }
//    public static void main(String[] args){
//        checkDrivers();    //check for driver errors
//        Connection connection = connect(databaselocation);
//
//    }
    private static Connection connect(String databaselocation) {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + databaselocation);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            databaselocation);
            return null;
        }
        return connection;
    }
    public static Connection makeconnection(){
        checkDrivers();
        return connect(databaselocation);
    }
}
