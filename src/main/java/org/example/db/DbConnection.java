package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USER = "root"; // Replace with your DB user
    private static final String PASSWORD = "mysql"; // Replace with your DB password


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
