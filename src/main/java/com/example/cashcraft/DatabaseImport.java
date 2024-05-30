package com.example.cashcraft;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseImport {

    public static void importCSVToTable(String tableName, String csvFilePath) throws SQLException, IOException {
        try (Connection conn = Makeconnection.makeconnection();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String line;
            String[] columns = br.readLine().split(","); // Read column names
            StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
            for (String column : columns) {
                sql.append(column).append(",");
            }
            sql.setLength(sql.length() - 1); // Remove last comma
            sql.append(") VALUES (");
            for (int i = 0; i < columns.length; i++) {
                sql.append("?,");
            }
            sql.setLength(sql.length() - 1); // Remove last comma
            sql.append(")");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    pstmt.setString(i + 1, values[i]);
                }
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
}

