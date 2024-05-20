package be.helha.applicine.common.models.event;

import java.io.Serializable;

public class Event implements Serializable {
    //le type de l'event
    private final String type;
    //si je veux envoyer des données avec l'event
    private final Object data;

    public Event(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

}
