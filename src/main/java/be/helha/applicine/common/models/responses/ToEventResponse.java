package be.helha.applicine.common.models.responses;

import be.helha.applicine.client.network.ServerRequestHandler;
import be.helha.applicine.common.models.event.Event;

public class ToEventResponse extends ServerEvent {
    private Event event;

    public ToEventResponse(Event ev){
        this.event = ev;
    }

    public Event getEvent(){
        return this.event;
    }

    @Override
    public void dispatchOn( ResponseVisitor visitor ){
        visitor.visit(this);
    }
}
