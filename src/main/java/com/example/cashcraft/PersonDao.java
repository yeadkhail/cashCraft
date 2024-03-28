package com.example.cashcraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PersonDao {
    public static void addCategory(PersonClasses.Category category){
        try {
            Connection connection = Makeconnection.makeconnection();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into category values(?,?)");
            preparedStatement.setString(1, category.name);
            preparedStatement.setString(2, category.desc);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
public static void addTransaction(PersonClasses.Expense transaction){
    try {
        Connection connection = Makeconnection.makeconnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into expense values(?,?,?,?,?,?)");

        // Convert date to SQL date
        java.sql.Date sqlDate = new java.sql.Date(transaction.date.getTime());
        preparedStatement.setDate(1, sqlDate);

        // Insert category UUID
        preparedStatement.setString(2, transaction.category.uuid);

        preparedStatement.setString(3, transaction.desc);
        preparedStatement.setDouble(4, transaction.amount);

        // Insert people UUID
        preparedStatement.setString(5, transaction.people.uuid);

        // Insert wallet UUID
        preparedStatement.setString(6, transaction.wallet.uuid);

        preparedStatement.executeUpdate();
        connection.close();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
public static void addPerson(PersonClasses.People people) {
    try {
        Connection connection = Makeconnection.makeconnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into people values(?,?)");
        preparedStatement.setString(1, people.name);
        preparedStatement.setString(2, people.desc);
        preparedStatement.executeUpdate();
        connection.close();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
}
