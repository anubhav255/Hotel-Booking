package com.library.dl;

import java.sql.*;


// Reusable class for Database Connectivity
public class DAOConnection {
    private DAOConnection() {
    }

    public static Connection getConnection() throws DAOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb2017", "root", "maheshwari");
            return connection;
        } catch (ClassNotFoundException classNotFoundException) {
            throw new DAOException("Driver class : com.mysql.jdbc.Driver missing in classpath");
        } catch (SQLException sqlException) {
            throw new DAOException("Unable to connect : " + sqlException.getMessage());
        }
    }
}