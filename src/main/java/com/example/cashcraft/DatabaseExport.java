package com.example.cashcraft;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseExport {

    public static void exportTableToCSV(String tableName, String csvFilePath) throws SQLException, IOException {
        try (Connection conn = Makeconnection.makeconnection() ;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
             FileWriter fw = new FileWriter(csvFilePath)) {

            int columnCount = rs.getMetaData().getColumnCount();
            // Write column names
            for (int i = 1; i <= columnCount; i++) {
                fw.append(rs.getMetaData().getColumnName(i));
                if (i < columnCount) fw.append(',');
            }
            fw.append('\n');

            // Write rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fw.append(rs.getString(i));
                    if (i < columnCount) fw.append(',');
                }
                fw.append('\n');
            }
        }
    }
}
