package be.helha.applicine.common.models.request;

import java.io.IOException;

public interface RequestVisitor {
    void visit(CheckLoginRequest checkLoginRequest);
    void visit(ClientRegistrationRequest clientRegistrationRequest) throws IOException;
    void visit(DeleteMoviesRequest deleteMoviesRequest);
    void visit(GetAllSessionRequest getAllSessionRequest);
    void visit(GetMovieByIdRequest getMovieByIdRequest);
    void visit(GetMoviesRequest getMoviesRequest);
    void visit(GetSessionByIdRequest getSessionByIdRequest);
    void visit(GetSessionByMovieId getSessionByMovieId);
    void visit(GetTicketByClientRequest getTicketByClientRequest);
}
