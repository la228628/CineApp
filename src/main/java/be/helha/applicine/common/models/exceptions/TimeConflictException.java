package be.helha.applicine.common.models.exceptions;

import java.util.List;

public class TimeConflictException extends Throwable {

    public List<Integer> conflictingSessionsIds;
    public TimeConflictException(String message, List<Integer> conflictingSessionsIds) {
        super(message);
        this.conflictingSessionsIds = conflictingSessionsIds;

    }
    public List<Integer> getConflictingSessionsIds() {
        return conflictingSessionsIds;
    }

}
