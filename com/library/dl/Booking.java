package com.library.dl;

public class Booking implements BookingInterface{
    private int bookingCode;
    private int customerCode;
    private int hotelCode;

    Booking() {
        bookingCode = 0;
        customerCode = 0;
        hotelCode = 0;
    }

    public int getBookingCode() {
        return this.bookingCode;
    }

    public void setBookingCode(int bookingCode) {
        this.bookingCode = bookingCode;
    }
    
    public int getCustomerCode() {
        return this.customerCode;
    }
    public void setCustomerCode(int customerCode) {
        this.customerCode = customerCode;
    }
    public int getHotelCode() {
        return this.hotelCode;
    }
    public void setHotelCode(int hotelCode) {
        this.hotelCode = hotelCode;
    }
}
