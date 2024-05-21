package be.helha.applicine.common.models.request;

import java.io.IOException;

public class GetRoomByIdRequest extends ClientEvent {
    private int roomId;

    public GetRoomByIdRequest(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
