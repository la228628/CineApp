package be.helha.applicine.common.models.request;

import java.io.IOException;
import java.sql.SQLException;

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
    void visit(CreateTicketRequest createTicketRequest) throws IOException;
    void visit(DeleteSessionRequest deleteSessionRequest) throws IOException, SQLException;
    void visit(CreateMovieRequest createMovieRequest) throws IOException;
    void visit(GetSessionsLinkedToMovieRequest getSessionsLinkedToMovieRequest);
    void visit(UpdateViewableRequest updateViewableRequest) throws IOException;
    void visit(AddViewableRequest addViewableRequest);

    void visit(GetViewablesRequest getViewablesRequest) throws IOException;

    void visit(DeleteViewableRequest deleteViewableRequest);

    void visit(GetRoomsRequest getRoomsRequest);

    void visit(UpdateSessionRequest updateSessionRequest);

    void visit(AddSessionRequest addSessionRequest);

    void visit(GetRoomByIdRequest getRoomByIdRequest);

    void visit(UpdateMovieRequest updateMovieRequest);

    void visit(GetSagasLinkedToMovieRequest getSagasLinkedToMovieRequest);

}
