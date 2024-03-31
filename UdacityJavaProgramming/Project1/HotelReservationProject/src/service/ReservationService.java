package service;

import model.*;
import java.util.*;

public class ReservationService {
    private static final ReservationService RESERVATION_SERVICE = new ReservationService();
    private static HashMap<String, IRoom> roomCollection = new HashMap<>();
    private static Set<Reservation> reservations = new HashSet<>();

    private ReservationService() {

    }

    public static ReservationService getReservationService() {
        return RESERVATION_SERVICE;
    }

    public void addRoom(IRoom room) {
        roomCollection.put(room.getRoomNumber(), room);
        System.out.println("Add room successfully!!!");
    }

    public IRoom getARoom(String roomId) {
        return roomCollection.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public ArrayList<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        ArrayList<IRoom> availableRoom = new ArrayList<>();

        // find unavailable room
        Set<String> unavailableRoomNumSet = new HashSet<>();

        for (Reservation reservation :
                reservations) {
            if (reservation.getCheckInDate().before(checkOutDate) && reservation.getCheckOutDate().after(checkInDate)) {
                unavailableRoomNumSet.add(reservation.getRoom().getRoomNumber());
            }
        }

        for (String roomNum :
                roomCollection.keySet()) {
            if (!unavailableRoomNumSet.contains(roomNum)) {
                availableRoom.add(roomCollection.get(roomNum));
            }
        }

        return availableRoom;
    }

    public ArrayList<Reservation> getCustomerReservation(Customer customer) {
        ArrayList<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation :
                reservations) {
            if (reservation.getCustomer().getEmail().equals(customer.getEmail())) {
                customerReservations.add(reservation);
            }
        }

        return customerReservations;
    }

    public void printAllReservation() {
            System.out.println(reservations.toString());
    }

    public ArrayList<IRoom> getAllRooms(){
        return new ArrayList<IRoom> (roomCollection.values());
    }
}
