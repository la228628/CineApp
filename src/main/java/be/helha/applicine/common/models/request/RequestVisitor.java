package be.helha.applicine.common.models.request;

public interface RequestVisitor {
    void visit(CheckLoginRequest checkLoginRequest);
    void visit(ClientRegistrationRequest clientRegistrationRequest);
    void visit(DeleteMoviesRequest deleteMoviesRequest);
    void visit(GetAllSessionRequest getAllSessionRequest);
    void visit(GetMovieByIdRequest getMovieByIdRequest);
    void visit(GetMoviesRequest getMoviesRequest);
    void visit(GetSessionByIdRequest getSessionByIdRequest);
    void visit(GetSessionByMovieId getSessionByMovieId);
    void visit(GetTicketByClientRequest getTicketByClientRequest);
    void visit(CreateTicketRequest createTicketRequest);
    void visit(DeleteSessionRequest deleteSessionRequest);
    void visit(CreateMovieRequest createMovieRequest);
    void visit(GetSessionsLinkedToMovieRequest getSessionsLinkedToMovieRequest);
    void visit(UpdateViewableRequest updateViewableRequest);
    void visit(AddViewableRequest addViewableRequest);

    void visit(GetViewablesRequest getViewablesRequest);

    void visit(DeleteViewableRequest deleteViewableRequest);

    void visit(GetRoomsRequest getRoomsRequest);

    void visit(UpdateSessionRequest updateSessionRequest);

    void visit(AddSessionRequest addSessionRequest);

    void visit(GetRoomByIdRequest getRoomByIdRequest);

    void visit(UpdateMovieRequest updateMovieRequest);

    void visit(GetSagasLinkedToMovieRequest getSagasLinkedToMovieRequest);

}
