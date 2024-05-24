package be.helha.applicine.common.models.request;

/**
 * Interface to visit the requests
 * @see be.helha.applicine.server.ClientHandler for the implementation
 */
public interface RequestVisitor {
    default void visit(CheckLoginRequest checkLoginRequest){

    }

    default void visit(ClientRegistrationRequest clientRegistrationRequest) {

    }

    default void visit(DeleteMoviesRequest deleteMoviesRequest) {

    }

    default void visit(GetAllSessionRequest getAllSessionRequest) {

    }

     default void visit(GetMoviesRequest getMoviesRequest){

     }

    default void visit(GetSessionByIdRequest getSessionByIdRequest) {

    }

    default void visit(GetSessionByMovieId getSessionByMovieId) {

    }

    default void visit(GetTicketByClientRequest getTicketByClientRequest) {

    }

    default void visit(CreateTicketRequest createTicketRequest) {

    }

    default void visit(DeleteSessionRequest deleteSessionRequest) {

    }

    default void visit(CreateMovieRequest createMovieRequest) {

    }

    default void visit(UpdateViewableRequest updateViewableRequest) {

    }

    default void visit(AddViewableRequest addViewableRequest) {

    }

    default void visit(GetViewablesRequest getViewablesRequest) {

    }

    default void visit(DeleteViewableRequest deleteViewableRequest) {

    }

    default void visit(GetRoomsRequest getRoomsRequest) {

    }

    default void visit(UpdateSessionRequest updateSessionRequest) {

    }

    default void visit(AddSessionRequest addSessionRequest) {

    }

    default void visit(PingServer pingServer) {

    }

    default void visit(UpdateMovieRequest updateMovieRequest) {

    }

    default void visit(ErrorMessage errorMessage) {

    }
}
