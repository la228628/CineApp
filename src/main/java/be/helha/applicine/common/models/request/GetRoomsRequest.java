package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.Room;

import java.util.List;

/**
 * Request to get all the rooms
 */
public class GetRoomsRequest extends ClientEvent {
    private List<Room> rooms;

    /**
     * Constructor of the request
     */
    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }

    /**
     * Setter of the rooms
     * @param all the rooms
     */
    public void setRooms(List<Room> all) {
        this.rooms = all;
    }

    /**
     * Getter of the rooms
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }
}
