package com.example.cashcraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseManager implements AutoCloseable
{

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database.db");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
    public Connection getConnection() {
        return connection;
    }
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
