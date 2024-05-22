package be.helha.applicine.common.models.responses;

import java.io.Serializable;

public abstract class ServerEvent implements Serializable {
    public abstract void dispatchOn(ResponseVisitor visitor);
}
