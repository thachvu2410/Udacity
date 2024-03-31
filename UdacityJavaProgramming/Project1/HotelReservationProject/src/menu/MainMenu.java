package menu;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {

    private static final HotelResource HOTEL_RESOURCE = HotelResource.getHotelResource();
    private static final CustomerService CUSTOMER_SERVICE = CustomerService.getCustomerService();
    private static final AdminMenu ADMIN_MENU = new AdminMenu();
    private Scanner scanner = new Scanner(System.in);


    // option 1
    public void findAndReserveARoom() {
        System.out.println("Please input your check in date (MM/dd/yyyy): ");
        String checkInStr = scanner.nextLine();

        while (parseDateFromString(checkInStr) == null){
            System.out.println("Invalid date! Please input your check in date (MM/dd/yyyy): ");
            checkInStr = scanner.nextLine();
        }

        Date checkIn = parseDateFromString(checkInStr);

        System.out.println("Please input your check out date (MM/dd/yyyy): ");
        String checkOutStr = scanner.nextLine();



        while (parseDateFromString(checkOutStr) == null){
            System.out.println("Invalid date! Please input your check in date (MM/dd/yyyy): ");
            checkOutStr = scanner.nextLine();
        }
        Date checkOut = parseDateFromString(checkOutStr);

        ArrayList<IRoom> availableRooms = HOTEL_RESOURCE.findAvailableRoom(checkIn, checkOut);


        // if no room is available at that moment, please suggest empty room in next 7 days
        if (availableRooms.isEmpty()){
            System.out.println("There is no available room in your expected time range!");
            System.out.println("Please consider these rooms from " + addDays(checkIn, 7)
                    + " to " + addDays(checkOut, 7) +": ");
            System.out.println(HOTEL_RESOURCE.findAvailableRoom(addDays(checkIn, 7), addDays(checkOut, 7)));

            System.out.println("Do you have an account? Please choose answer (Y)es/(N)o");
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("N")){
                System.out.println("Let create an account first!");
                String email = createAccount();

                System.out.println("Please choose room number you want to book: ");
                String roomNum = scanner.nextLine();

                Reservation reservation = HOTEL_RESOURCE.bookARoom(email,HOTEL_RESOURCE.findRoomByRoomNumber(roomNum),addDays(checkIn, 7), addDays(checkOut, 7));
                System.out.println("Your reservations is: " + reservation.toString());
            }else if (option.equalsIgnoreCase("Y")){
                boolean isContinue = true;
                String email = "";
                while (isContinue){
                    System.out.println("Please input your email: ");
                    email = scanner.nextLine();
                    for (Customer customer : CUSTOMER_SERVICE.getAllCustomers()) {
                        if (customer.getEmail().equals(email)) {
                            isContinue = false;
                            break;
                        }
                    }
                }
                System.out.println("Please choose room number you want to book: ");
                String roomNum = scanner.nextLine();

                Reservation reservation = HOTEL_RESOURCE.bookARoom(email,HOTEL_RESOURCE.findRoomByRoomNumber(roomNum),addDays(checkIn, 7), addDays(checkOut, 7));
                System.out.println("Your reservations is: " + reservation.toString());
            }else {
                System.out.println("invalid option!");
            }
        }else {
            System.out.println("Please check available rooms below:");
            System.out.println(availableRooms);

            System.out.println("Do you have an account? Please choose answer (Y)es/(N)o");
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("N")){
                System.out.println("Let create an account first!");
                String email = createAccount();

                System.out.println("Please choose room number you want to book: ");
                String roomNum = scanner.nextLine();

                Reservation reservation = HOTEL_RESOURCE.bookARoom(email,HOTEL_RESOURCE.findRoomByRoomNumber(roomNum),checkIn,checkOut);
                System.out.println("Your reservations is: " + reservation.toString());
            }else if (option.equalsIgnoreCase("Y")){
                boolean isContinue = true;
                String email = "";
                while (isContinue){
                    System.out.println("Please input your email: ");
                    email = scanner.nextLine();
                    for (Customer customer : CUSTOMER_SERVICE.getAllCustomers()) {
                        if (customer.getEmail().equals(email)) {
                            isContinue = false;
                            break;
                        }
                    }
                }

                System.out.println("Please choose room number you want to book: ");
                String roomNum = scanner.nextLine();

                Reservation reservation = HOTEL_RESOURCE.bookARoom(email,HOTEL_RESOURCE.findRoomByRoomNumber(roomNum),checkIn,checkOut);
                System.out.println("Your reservations is: " + reservation.toString());
            }else {
                System.out.println("invalid option!");
            }
        }

    }

    // option 3
    public String createAccount() {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        System.out.println("Please provide your email: ");
        String email = scanner.nextLine();
        boolean isValidEmail = false;

        while (!isValidEmail){
            // validate email
            if (email == null || !pattern.matcher(email).matches()) {
                System.out.println("Invalid email format. Please try again: ");
                email = scanner.nextLine();
                continue;
            } else {
                // check duplicate email
                for (Customer customer : CUSTOMER_SERVICE.getAllCustomers()) {
                    if (customer.getEmail().equals(email)) {
                        System.out.println("This account is already existed. Please try again: ");
                        email = scanner.nextLine();
                        break;
                    }
                }
                break;
            }
        }


        // create new account
        System.out.println("Please input your first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Please input your last name: ");
        String lastName = scanner.nextLine();
        HOTEL_RESOURCE.createACustomer(email, firstName, lastName);
        System.out.println("Your account is created! Enjoy!");
        return email;
    }

    // option 2
    public void checkReservationsOfCustomer() {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        System.out.println("Please input your email: ");
        String email = scanner.nextLine();
        boolean isEmailValid = false;
        while (!isEmailValid){

            if (email == null || !pattern.matcher(email).matches()) {
                System.out.println("Invalid email format. Please try again: ");
                email = scanner.nextLine();
            }else isEmailValid = true;
        }

        if (HOTEL_RESOURCE.getCustomer(email) == null) {
            System.out.println("Account does not exist!!!");
        } else {
            System.out.println(HOTEL_RESOURCE.getCustomerReservations(email));
        }
    }

    // option 4
    public void displayAdminMenu() {
        ADMIN_MENU.displayAdminMenu();
    }

    public void optionhandler() {
        boolean isContinue = true;
        while (isContinue) {
            displayMainMenu();
            System.out.println("Input you option: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    findAndReserveARoom();
                    break;
                case "2":
                    checkReservationsOfCustomer();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    ADMIN_MENU.optionhandler();
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

    public void displayMainMenu() {
        System.out.println("main menu");
        System.out.println("*********************************");
        System.out.println("1.  Find and reserve a room");
        System.out.println("2.  See my reservations");
        System.out.println("3.  Create an account");
        System.out.println("4.  Admin");
        System.out.println("5.  Exit");
        System.out.println("*********************************");
    }


    private Date parseDateFromString(String input) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            dateFormat.setLenient(false);
            Date dateFormatted = dateFormat.parse(input);
            return dateFormatted;
        } catch (ParseException e) {
            System.out.println("Wrong date format!!");
            return null;
        }
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}