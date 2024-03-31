package model;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType roomType) {
        super(roomNumber, 0.0, roomType);
    }

    @Override
    public String toString() {
        return "FreeRoom{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=0" +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public boolean isFree() {
        return true;
    }
}
