package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

public class CheckLoginRequest extends ClientEvent{

    private String username;
    private String password;

    public CheckLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void dispatchOn(RequestVisitor requestVisitor) {
        requestVisitor.visit(this);
    }
}
