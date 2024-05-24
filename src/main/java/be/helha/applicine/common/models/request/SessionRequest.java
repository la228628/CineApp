package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

public interface SessionRequest {
    MovieSession getSession();
    void setSuccess(boolean success);
    boolean getSuccess();
    void setMessage(String message);
    String getMessage();
}
