package be.helha.applicine.common.models.responses;

import be.helha.applicine.common.models.event.Event;

import java.io.Serializable;

public abstract class ServerEvent extends Event implements Serializable {
    public abstract void dispatchOn(ResponseVisitor visitor);
}
