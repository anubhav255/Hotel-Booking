import com.sun.net.httpserver.*;
import java.util.List;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import com.library.dl.*;
import com.google.gson.Gson;

// Booking Server provides access to the bookings by 3 methods
//      Add -> lets end customer to add reservation
//      Cancel -> lets end user to cancel the reservation 
//      Get -> lets end user to fetch their all reservations 
public class BookingServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        System.out.println("Server listening on port: " + port);

        server.createContext("/booking/create", new AddBooking());
        server.createContext("/booking/get", new GetBookingByCustomerCode());
        server.createContext("/booking/cancel", new CancelBookingByBookingId());
        server.setExecutor(null);
        server.start();
    }

    
    static class AddBooking implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("POST")) {
                StringBuilder requestBody = new StringBuilder();
                try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        requestBody.append(line);
                    }
                    System.out.println(requestBody);
                    Gson gson = new Gson();
                    BookingInterface booking = gson.fromJson(requestBody.toString(), Booking.class);
                    try {
                        BookingDAO bookingDAO = new BookingDAO();
                        bookingDAO.add(booking);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream os = exchange.getResponseBody();
                        os.write("{\"msg:\": \"Hotel Booking Confirmed\"}".getBytes());
                        os.close();
                        exchange.close();
                    } catch (DAOException daoException) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        exchange.close();
                    }
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.close();
            }
        }
    }

    static class GetBookingByCustomerCode implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String uri = exchange.getRequestURI().toString();
                Pattern pattern = Pattern.compile("/booking/get/(\\d+)");
                Matcher matcher = pattern.matcher(uri);
                if (matcher.find()) {
                    int customerCode = Integer.parseInt(matcher.group(1));
                    System.out.println(customerCode);
                    try {
                        BookingDAO bookingDAO = new BookingDAO();
                        List<BookingInterface> bookingList = bookingDAO.getByCustomerCode(customerCode);
                        System.out.println(bookingList.size());
                        Gson gson = new Gson();
                        String bookingJson = gson.toJson(bookingList);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, bookingJson.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(bookingJson.getBytes());
                        os.close();
                        exchange.close();
                    } catch (DAOException daoException) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        exchange.getResponseBody().write(("{\"msg\": \"No Bookings Found\"}").getBytes());
                        exchange.close();
                    }

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    exchange.close();
                }
            }
        }
    }

    static class CancelBookingByBookingId implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("DELETE")) {
                String uri = exchange.getRequestURI().toString();
                Pattern pattern = Pattern.compile("/booking/cancel/(\\d+)");
                Matcher matcher = pattern.matcher(uri);
                if (matcher.find()) {
                    int bookingCode = Integer.parseInt(matcher.group(1));
                    String response = ("{\"msg\" : \"Cancelled Booking with code: "+ bookingCode + "\"}");
                    try {
                        new BookingDAO().delete(bookingCode);
                    } catch (DAOException daoException) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        exchange.close();
                    }
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    exchange.close();
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    exchange.close();
                }
            }
        }
    }

}
