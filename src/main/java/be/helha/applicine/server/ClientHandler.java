package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.common.network.ObjectSocket;
import be.helha.applicine.server.dao.*;
import be.helha.applicine.server.dao.impl.*;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread implements RequestVisitor {
    private ObjectSocket clientSocket;
    private MovieDAO movieDAO;
    private ClientsDAO clientsDAO;
    private TicketDAO ticketDAO;
    private RoomDAO roomDAO;

    private ViewableDAO viewableDAO;
    private SessionDAO sessionDAO;

    public ClientHandler(ObjectSocket socket) throws IOException, SQLException {
        this.clientSocket = socket;
        this.movieDAO = new MovieDAOImpl();
        this.clientsDAO = new ClientsDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.viewableDAO = new ViewableDAOImpl();
    }

    public void run() {
        try {
            ClientEvent event;
            while ((event = clientSocket.read()) != null) {
                event.dispatchOn(this);
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
        }
    }

    public void writeToClient(Object object){
        try {
            this.clientSocket.write(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //effectue la requete de récupération des sagas liées à un film
    @Override
    public void visit(GetSagasLinkedToMovieRequest getSagasLinkedToMovieRequest) {
        int movieId = getSagasLinkedToMovieRequest.getMovieId();
        int amountSagas = viewableDAO.sagasLinkedToMovie(movieId);
        getSagasLinkedToMovieRequest.setAmountSagas(amountSagas);
        writeToClient(getSagasLinkedToMovieRequest);
    }

    @Override
    public void visit(UpdateMovieRequest updateMovieRequest) {
        Movie movie = updateMovieRequest.getMovie();
        try {
            if (movie.getImage() != null) {
                movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
                FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
            }
            movieDAO.update(movie);
            updateMovieRequest.setStatus(true);
            writeToClient(updateMovieRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetRoomByIdRequest getRoomByIdRequest) {
        int id = getRoomByIdRequest.getRoomId();
        try {
            Room room = roomDAO.get(id);
            getRoomByIdRequest.setRoom(room);
            writeToClient(getRoomByIdRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(AddSessionRequest addSessionRequest) {
        MovieSession session = addSessionRequest.getSession();
        try {
            sessionDAO.create(session);
            addSessionRequest.setSuccess(true);
            writeToClient(addSessionRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(UpdateSessionRequest updateSessionRequest) {
        MovieSession session = updateSessionRequest.getSession();
        try {
            sessionDAO.update(session);
            updateSessionRequest.setSuccess(true);
            writeToClient(updateSessionRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetRoomsRequest getRoomsRequest) {
        try {
            getRoomsRequest.setRooms(roomDAO.getAll());
            System.out.println("Rooms: " + getRoomsRequest.getRooms());
            writeToClient(getRoomsRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void visit(DeleteViewableRequest deleteViewableRequest) {
        int viewableId = deleteViewableRequest.getViewableId();
        if (viewableDAO.removeViewable(viewableId)) {
            deleteViewableRequest.setSuccess(true);
            writeToClient(deleteViewableRequest);
        } else {
            deleteViewableRequest.setSuccess(false);
            writeToClient(deleteViewableRequest);
        }
    }

    @Override
    public void visit(GetViewablesRequest getViewablesRequest) {
        List<Viewable> viewables = viewableDAO.getAllViewables();
        getViewablesRequest.setViewables(viewables);
        writeToClient(getViewablesRequest);
    }

    @Override
    public void visit(AddViewableRequest addViewableRequest) {
        Saga saga = (Saga) addViewableRequest.getViewable();
        ArrayList<Movie> viewables = saga.getMovies();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Movie viewable : viewables) {
            ids.add(viewable.getId());
        }
        viewableDAO.addViewable(saga.getTitle(), "Saga", ids);
        addViewableRequest.setSuccess(true);
        writeToClient(addViewableRequest);
    }

    @Override
    public void visit(UpdateViewableRequest updateViewableRequest) {
        Saga saga = updateViewableRequest.getSaga();
        ArrayList<Movie> sagaMovies = saga.getMovies();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Movie movie : sagaMovies) {
            ids.add(movie.getId());
        }
        viewableDAO.updateViewable(saga.getId(), saga.getTitle(), "Saga", ids);
        updateViewableRequest.setSuccess(true);
        writeToClient(updateViewableRequest);
    }

    @Override
    public void visit(GetSessionsLinkedToMovieRequest getSessionsLinkedToMovieRequest) {
        int movieId = getSessionsLinkedToMovieRequest.getMovieId();
        int amountSessions = movieDAO.getSessionLinkedToMovie(movieId);
        getSessionsLinkedToMovieRequest.setAmountSessions(amountSessions);
        writeToClient(getSessionsLinkedToMovieRequest);
    }

    @Override
    public void visit(CreateMovieRequest createMovieRequest) {
        try {
            Movie movie = createMovieRequest.getMovie();
            movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
            FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
            movieDAO.create(movie);
            createMovieRequest.setStatus(true);
            writeToClient(createMovieRequest);
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    @Override
    public void visit(ClientRegistrationRequest clientRegistrationRequest) {
        Client client = clientRegistrationRequest.getClient();
        try {
            String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
            Client registeredClient = clientsDAO.create(new Client(client.getName(), client.getEmail(), client.getUsername(), hashedPassword));
            if (registeredClient != null) {
                clientRegistrationRequest.setSuccess(true);
            } else {
                clientRegistrationRequest.setSuccess(false);
            }
            writeToClient(clientRegistrationRequest);
        } catch (IOException e) {
            writeToClient("Error during registration: " + e.getMessage());
        }
    }

    @Override
    public void visit(DeleteMoviesRequest deleteMoviesRequest) {
        try {
            movieDAO.delete(deleteMoviesRequest.getId());
            deleteMoviesRequest.setStatus(true);
            writeToClient(deleteMoviesRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetAllSessionRequest getAllSessionRequest) {
        try {
            List<MovieSession> sessions = sessionDAO.getAll();
            getAllSessionRequest.setSessions(sessions);
            writeToClient(getAllSessionRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CheckLoginRequest checkLoginRequest) {
        String username = checkLoginRequest.getUsername();
        String password = checkLoginRequest.getPassword();
        Client client = clientsDAO.getClientByUsername(username);
        if (client != null && HashedPassword.checkPassword(password, client.getPassword())) {
            checkLoginRequest.setClient(client);
            writeToClient(checkLoginRequest);
        } else {
            checkLoginRequest.setClient(null);
            writeToClient(checkLoginRequest);
        }
    }

    @Override
    public void visit(GetMoviesRequest getMoviesRequest) {
        try {
            List<Movie> movies = movieDAO.getAll();
            for (Viewable movie : movies) {
                movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
            }
            getMoviesRequest.setMovies(movies);
            writeToClient(getMoviesRequest);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSessionByMovieId getSessionByMovieId) {
        int movieId = getSessionByMovieId.getMovieId();
        try {
            List<MovieSession> sessions = sessionDAO.getSessionsForMovie(viewableDAO.getViewableById(movieId));
            getSessionByMovieId.setSessions(sessions);
            writeToClient(getSessionByMovieId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetTicketByClientRequest getTicketByClientRequest) {
        int clientId = getTicketByClientRequest.getClientId();
        List<Ticket> tickets = ticketDAO.getTicketsByClient(clientId);
        getTicketByClientRequest.setTickets(tickets);
        writeToClient(getTicketByClientRequest);
    }

    @Override
    public void visit(GetSessionByIdRequest getSessionByIdRequest) {
        int sessionId = getSessionByIdRequest.getSessionId();
        try {
            MovieSession session = sessionDAO.get(sessionId);
            getSessionByIdRequest.setSession(session);
            writeToClient(getSessionByIdRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetMovieByIdRequest getMovieByIdRequest) {
        int id = getMovieByIdRequest.getMovieId();
        try {
            Movie movie = movieDAO.get(id);
            movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
            getMovieByIdRequest.setMovie(movie);
            writeToClient(getMovieByIdRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CreateTicketRequest createTicketRequest) {
        Ticket ticket = createTicketRequest.getTicket();
        ticketDAO.create(ticket);
        createTicketRequest.setStatus(true);
        writeToClient(createTicketRequest);
    }

    @Override
    public void visit(DeleteSessionRequest deleteSessionRequest) {
        try {
            int sessionId = deleteSessionRequest.getSessionId();
            sessionDAO.delete(sessionId);
            deleteSessionRequest.setSuccess(true);
            writeToClient(deleteSessionRequest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}