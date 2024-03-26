package com.library.dl;

import java.util.*;
public interface BookingDAOInterface {
    public void add(BookingInterface booking) throws DAOException;
    public List<BookingInterface> getByCustomerCode(int customerCode) throws DAOException;
    public void remove(int bookingCode) throws DAOException;
}
