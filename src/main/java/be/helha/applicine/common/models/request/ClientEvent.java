package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class ClientEvent implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public abstract void dispatchOn(RequestVisitor requestVisitor);
}
