package be.helha.applicine.common.models.request;

import java.io.Serializable;

public abstract class ClientEvent implements Serializable {
    public abstract void dispatchOn(RequestVisitor requestVisitor);
}
