package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class AdminResource {
    private static final AdminResource ADMIN_RESOURCE = new AdminResource();
    private CustomerService customerService = CustomerService.getCustomerService();
    private ReservationService reservationService = ReservationService.getReservationService();

    private AdminResource(){

    }

    public static AdminResource getAdminResource(){
        return ADMIN_RESOURCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void addNewRoom(List<IRoom> rooms){
        rooms.forEach(reservationService::addRoom);
    }

    public ArrayList<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    public ArrayList<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservation();
    }
}
