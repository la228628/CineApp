package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

public interface SessionRequest {
    MovieSession getSession();
    void setSuccess(boolean success);
    boolean getSuccess();
    void setMessage(String message);
    String getMessage();

    void setConflictedSessions(List<Integer> conflictedSessions);
}
