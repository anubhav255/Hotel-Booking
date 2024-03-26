package com.library.dl;


// Basic Exception class for handeling all the Data access layer exceptions
public class DAOException extends Exception {
    public DAOException(String message) {
        super(message);
    }
}
