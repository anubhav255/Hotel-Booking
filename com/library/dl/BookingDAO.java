package com.library.dl;
import java.util.*;
import java.sql.*;


// BookingDAO consists of the operations need to be performed and providing a Connectivity between the database and server class
public class BookingDAO implements BookingDAOInterface {

    public void add(BookingInterface booking) throws DAOException {
        try {
            Connection connection = DAOConnection.getConnection();

            PreparedStatement preparedStatement;            
            preparedStatement = connection.prepareStatement("insert into booking (customerCode, hotelCode) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, booking.getCustomerCode());
            preparedStatement.setInt(2, booking.getHotelCode());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            // Fetching and setting the automated generated key by database from resultSet to Booking object 
            booking.setBookingCode(resultSet.getInt(1));
            
            resultSet.close();  
            preparedStatement.close();
            connection.close();

        } catch(SQLException sqlException) {
            throw new DAOException("Unable to add : " + sqlException.getMessage());
        }
    }
    public List<BookingInterface> getByCustomerCode(int customerCode) throws DAOException {
        List<BookingInterface> bookings;
        try {
            Connection connection = DAOConnection.getConnection();
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("select * from booking where customerCode = ?");
            preparedStatement.setInt(1, customerCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            // No bookings found scenario
            if(resultSet.next() == false) {
                resultSet.close();
                preparedStatement.close();
                connection.close();
                throw new DAOException("No Bookings");
            }  

            bookings = new ArrayList<BookingInterface>();
            Booking b;
            
            do {
                b = new Booking();
                b.setBookingCode(resultSet.getInt("bookingCode"));
                b.setCustomerCode(resultSet.getInt("customerCode"));
                b.setHotelCode(resultSet.getInt("hotelCode"));
                bookings.add(b);
            } while(resultSet.next());

            resultSet.close();
            preparedStatement.close();
            connection.close();
            
            return bookings;
        } catch(SQLException sqlException){
            throw new DAOException("Unable to fetch : "+ sqlException.getMessage());
        }
    }
    public void remove(int bookingCode) throws DAOException {
        try {
            Connection connection = DAOConnection.getConnection();
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("delete from booking where bookingCode=?");
            preparedStatement.setInt(1, bookingCode);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch(SQLException sqlException) {
            throw new DAOException("Unable to delete : "+ sqlException.getMessage());
        }
    }
}
