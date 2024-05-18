package be.helha.applicine.common.models.request;

import be.helha.applicine.server.ClientHandler;
import org.controlsfx.control.spreadsheet.GridBase;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public abstract class ClientEvent implements Serializable {
    public abstract void dispatchOn(ClientHandler clientHandler) throws IOException, SQLException;
}
