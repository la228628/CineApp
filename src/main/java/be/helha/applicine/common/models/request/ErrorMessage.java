package be.helha.applicine.common.models.request;

public class ErrorMessage {
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
