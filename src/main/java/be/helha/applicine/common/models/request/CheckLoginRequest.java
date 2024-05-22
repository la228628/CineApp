package be.helha.applicine.common.models.request;

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
