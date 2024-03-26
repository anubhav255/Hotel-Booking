**Basic Hotel Booking app in Java that uses database as MySQL.**

It have 3 basic methods of CRUD operation
1. Add reservation -> an application can send data in JSON format consisting of 2 input fields (customerCode and hotelCode).

2. Cancel reservation -> an application can cancel the reservation by send the bookingCode.

3. Get reservation -> an application can send the customerCode to fetch their reservation as a list of JavaScript Objects, with 3 fields (bookingCode, customerCode and hotelCode).

Note:- Used Gson for String to JSON conversion and vice-versa.
