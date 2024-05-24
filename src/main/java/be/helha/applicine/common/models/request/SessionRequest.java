package be.helha.applicine.common.models.request;

import be.helha.applicine.common.models.MovieSession;

import java.util.List;

/**
 * Request to get a session by its movie id
 */
public interface SessionRequest {
    MovieSession getSession();
    void setSuccess(boolean success);
    boolean getSuccess();
    void setMessage(String message);
    String getMessage();

    /**
     * Getter of the movie id
     * @param conflictedSessions the movie id
     */
    void setConflictedSessions(List<Integer> conflictedSessions);
}
