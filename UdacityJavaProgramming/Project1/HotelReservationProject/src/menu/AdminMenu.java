package menu;


import api.AdminResource;
import api.HotelResource;
import model.FreeRoom;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource ADMIN_RESOURCE = AdminResource.getAdminResource();
    private static final HotelResource HOTEL_RESOURCE = HotelResource.getHotelResource();
    private Scanner scanner = new Scanner(System.in);

    // option 1
    public void seeAllCustomers(){
        System.out.println(ADMIN_RESOURCE.getAllCustomers());
    }

    // option 2
    public void seeAllRooms(){
        System.out.println(ADMIN_RESOURCE.getAllRooms());
    }

    // option 3
    public void seeAllReservations(){
        ADMIN_RESOURCE.displayAllReservations();
    }

    // option 4
    public void addARoom(){
        List<IRoom> rooms = new ArrayList<>();
        boolean isContinue = true;
        while (isContinue){
            System.out.println("Please add your room number: ");
            String roomNum = scanner.nextLine();
            System.out.println("Which type of this room - SINGLE(1) | DOUBLE(2): ");
            int roomTypeOption = scanner.nextInt();
            System.out.println("Please input the price of the room: ");
            Double price = scanner.nextDouble();

            if (price == 0){
                if (roomTypeOption == 1){
                    FreeRoom freeRoom = new FreeRoom(roomNum, RoomType.SINGLE);
                    rooms.add(freeRoom);
                }else {
                    FreeRoom freeRoom = new FreeRoom(roomNum, RoomType.DOUBLE);
                    rooms.add(freeRoom);
                }
            }else {
                if (roomTypeOption == 1){
                    Room room = new Room(roomNum, price, RoomType.SINGLE);
                    rooms.add(room);
                }else {
                    Room room = new Room(roomNum, price, RoomType.DOUBLE);
                    rooms.add(room);
                }
            }
            System.out.println("You want to add more room? - (Y)es/(N)o: ");
            scanner.nextLine();
            String myChoice = scanner.nextLine();
            if (myChoice.equals("N")){
                isContinue = false;
            }
        }

        ADMIN_RESOURCE.addNewRoom(rooms);
    }


    public void optionhandler(){
        boolean isContinue = true;
        while (isContinue){
            displayAdminMenu();
            System.out.println("Input you option: ");
            String option = scanner.nextLine();
            switch (option){
                case "1":
                    seeAllCustomers();
                    break;
                case "2":
                    seeAllRooms();
                    break;
                case "3":
                    seeAllReservations();
                    break;
                case "4":
                    addARoom();
                    break;
                case "5":
                    isContinue = false;
                    break;
                default:
                    System.out.println("Invalid option! Try again");
                    break;
            }
        }
    }

    public void displayAdminMenu() {
        System.out.println("admin menu");
        System.out.println("*********************************");
        System.out.println("1.  See all customers");
        System.out.println("2.  See all rooms");
        System.out.println("3.  See all reservations");
        System.out.println("4.  Add a room");
        System.out.println("5.  Back to Main Menu");
        System.out.println("*********************************");

    }
}