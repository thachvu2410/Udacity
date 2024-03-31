package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Date;

public class HotelResource {
    private static final HotelResource HOTEL_RESOURCE = new HotelResource();
    private CustomerService customerService = CustomerService.getCustomerService();
    private ReservationService reservationService = ReservationService.getReservationService();

    private HotelResource(){

    }

    public static HotelResource getHotelResource(){
        return HOTEL_RESOURCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        return reservationService.reserveARoom(customerService.getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public ArrayList<Reservation> getCustomerReservations(String customerEmail){
        return reservationService.getCustomerReservation(customerService.getCustomer(customerEmail));
    }

    public ArrayList<IRoom> findAvailableRoom(Date checkIn, Date checkOut){
        return reservationService.findRooms(checkIn, checkOut);
    }

    public IRoom findRoomByRoomNumber(String roomNum){
        return reservationService.getARoom(roomNum);
    }
}
