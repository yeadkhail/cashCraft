package com.example.cashcraft;

import java.io.*;
import java.sql.*;
import java.util.Collections;

public class ImportExportHandler {

    public boolean exportAllTablesToCSV(File directory) {
        String[] tableNames = {"place", "wallet", "category", "expense", "Income", "people", "transfer"};
        boolean success = true;

        for (String tableName : tableNames) {
            try {
                String csvFilePath = new File(directory, tableName + "_export.csv").getAbsolutePath();
                exportTableToCSV(tableName, csvFilePath);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                success = false; // Set success to false if any export fails
            }
        }
        return success;
    }

    public boolean importAllTablesFromCSV(File directory) {
        String[] tableNames = {"place", "wallet", "category", "expense", "Income", "people", "transfer"};
        boolean success = true;

        for (String tableName : tableNames) {
            try {
                String csvFilePath = new File(directory, tableName + "_export.csv").getAbsolutePath();
                importCSVToTable(tableName, csvFilePath);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                success = false; // Set success to false if any import fails
            }
        }
        return success;
    }

    private void exportTableToCSV(String tableName, String csvFilePath) throws SQLException, IOException {
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = Makeconnection.makeconnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Write CSV header
            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Write CSV data
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    if (value == null) {
                        value = "";  // Replace null with empty string
                    }
                    writer.write(value);
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        }
    }

    private void importCSVToTable(String tableName, String csvFilePath) throws SQLException, IOException {
        String query = "INSERT INTO " + tableName + " VALUES (";
        int columnCount = getColumnCount(tableName);
        String placeholder = String.join(",", Collections.nCopies(columnCount, "?"));
        query += placeholder + ")";

        try (Connection connection = Makeconnection.makeconnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {

            // Read CSV header (optional)
            String headerLine = reader.readLine();
            // Assuming the first line is header and it's being skipped here

            // Read CSV data
            String dataLine;
            while ((dataLine = reader.readLine()) != null) {
                String[] data = dataLine.split(",");

                // Check if the data length matches the column count
                if (data.length != columnCount) {
                    // Handle mismatched column count here
                    System.err.println("Mismatched column count for table " + tableName);
                    continue;
                }

                // Set values for PreparedStatement
                for (int i = 0; i < data.length; i++) {
                    preparedStatement.setString(i + 1, data[i]); // Adjust index to start from 1
                }

                try {
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    // Handle constraint violation (duplicate entry)
                    if (e.getMessage().contains("UNIQUE constraint")) {
                        System.err.println("Skipping duplicate record: " + e.getMessage());
                    } else {
                        // Rethrow other SQL exceptions
                        throw e;
                    }
                }
            }
        }
    }





    private int getColumnCount(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + " LIMIT 1";

        try (Connection connection = Makeconnection.makeconnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            return metaData.getColumnCount();
        }
    }
}
