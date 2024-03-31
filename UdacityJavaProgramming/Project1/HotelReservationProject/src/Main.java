import api.AdminResource;
import api.HotelResource;
import model.FreeRoom;
import model.Room;
import model.RoomType;
import model.IRoom;
import menu.MainMenu;
import service.ReservationService;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private static MainMenu mainMenu = new MainMenu();
    public static void main(String[] args) {

        List<IRoom> roomList = new ArrayList<>();
        roomList.add(new FreeRoom("2",  RoomType.DOUBLE));
//        roomList.add(new FreeRoom("3",  RoomType.SINGLE));
//
//        roomList.add(new Room("1", 20.0, RoomType.SINGLE));
//        roomList.add(new Room("4", 100.0, RoomType.DOUBLE));


        AdminResource.getAdminResource().addNewRoom(roomList);
        HotelResource.getHotelResource().createACustomer("thachvu@gmail.com", "Thach", "Vu");

        mainMenu.optionhandler();
    }
}
