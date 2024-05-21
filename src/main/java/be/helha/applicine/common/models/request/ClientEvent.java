package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class ClientEvent implements Serializable {
    public abstract void dispatchOn(RequestVisitor requestVisitor);
}
