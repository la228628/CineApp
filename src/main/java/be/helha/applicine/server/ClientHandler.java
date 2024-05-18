package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.request.*;
import be.helha.applicine.server.dao.*;
import be.helha.applicine.server.dao.impl.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread implements RequestVisitor {
    private Socket clientSocket;
    private MovieDAO movieDAO;
    private ClientsDAO clientsDAO;
    private TicketDAO ticketDAO;
    private RoomDAO roomDAO;

    private ViewableDAO viewableDAO;
    private SessionDAO sessionDAO;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket) throws IOException, SQLException {
        this.clientSocket = socket;
        this.movieDAO = new MovieDAOImpl();
        this.clientsDAO = new ClientsDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.viewableDAO = new ViewableDAOImpl();
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void run() {
        try {
            ClientEvent event;
            while ((event = (ClientEvent) in.readObject()) != null) {
                event.dispatchOn(this);
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSagasLinkedToMovieRequest getSagasLinkedToMovieRequest) {
        int movieId = getSagasLinkedToMovieRequest.getMovieId();
        try {
            out.writeObject(viewableDAO.getSeancesLinkedToViewable(movieId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(UpdateMovieRequest updateMovieRequest) {
        Movie movie = updateMovieRequest.getMovie();
        try {
            movieDAO.updateMovie(movie);
            out.writeObject("MOVIE_UPDATED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetRoomByIdRequest getRoomByIdRequest) {
        int id = getRoomByIdRequest.getRoomId();
        try {
            Room room = roomDAO.getRoomById(id);
            out.writeObject(room);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(AddSessionRequest addSessionRequest) {
        MovieSession session = addSessionRequest.getSession();
        try {
            sessionDAO.addSession(session.getViewable().getId(), session.getRoom().getNumber(), session.getTime(), session.getVersion());
            out.writeObject("SESSION_ADDED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(UpdateSessionRequest updateSessionRequest) {
        MovieSession session = updateSessionRequest.getSession();
        try {
            sessionDAO.updateSession(session.getId(), session.getViewable().getId(), session.getRoom().getNumber(), session.getTime(), session.getVersion());
            out.writeObject("SESSION_UPDATED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetRoomsRequest getRoomsRequest) {
        try {
            out.writeObject(roomDAO.getAllRooms());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImageAsBytes(String imagePath) throws IOException {
        if (imagePath.startsWith("file:")) {
            imagePath = imagePath.substring(5); // Remove the "file:" scheme
        }
        return Files.readAllBytes(Paths.get(imagePath));
    }


    @Override
    public void visit(DeleteViewableRequest deleteViewableRequest) {
        int viewableId = deleteViewableRequest.getViewableId();
        if (viewableDAO.removeViewable(viewableId)) {
            try {
                out.writeObject("VIEWABLE_DELETED");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                out.writeObject("VIEWABLE_NOT_DELETED");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void visit(GetViewablesRequest getViewablesRequest) throws IOException {
        try {
            out.writeObject(viewableDAO.getAllViewables());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            out.writeObject("VIEWABLE_ADDED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(UpdateViewableRequest updateViewableRequest) {
        Saga saga = updateViewableRequest.getSaga();
        viewableDAO.updateViewable(saga.getId(), saga.getTitle(), "Saga", new ArrayList<>());
        try {
            out.writeObject("VIEWABLE_UPDATED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSessionsLinkedToMovieRequest getSessionsLinkedToMovieRequest) {
        int movieId = getSessionsLinkedToMovieRequest.getMovieId();
        int amountSessions = movieDAO.sessionLinkedToMovie(movieId);
        try {
            out.writeObject(amountSessions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CreateMovieRequest createMovieRequest) throws IOException {
        Movie movie = createMovieRequest.getMovie();
        movieDAO.addMovie(movie);
        out.writeObject("MOVIE_ADDED");
    }

    @Override
    public void visit(ClientRegistrationRequest clientRegistrationRequest) throws IOException {
        Client client = clientRegistrationRequest.getClient();
        String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
        Client registeredClient = clientsDAO.createClient(client.getName(), client.getEmail(), client.getUsername(), hashedPassword);
        if (registeredClient != null) {
            out.writeObject("Registration successful");
        } else {
            out.writeObject("Registration failed");
        }
    }

    @Override
    public void visit(DeleteMoviesRequest deleteMoviesRequest) {
        try {
            movieDAO.removeMovie(deleteMoviesRequest.getId());
            out.writeObject("MOVIE_DELETED");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetAllSessionRequest getAllSessionRequest) {
        try {
            out.writeObject(sessionDAO.getAllSessions());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CheckLoginRequest checkLoginRequest) {
        String username = checkLoginRequest.getUsername();
        String password = checkLoginRequest.getPassword();
        Client client = clientsDAO.getClientByUsername(username);
        if (client != null && HashedPassword.checkPassword(password, client.getPassword())) {
            try {
                out.writeObject(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                out.writeObject(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void visit(GetMoviesRequest getMoviesRequest) {
        try {
            List<Movie> movies = movieDAO.getAllMovies();
            for (Viewable movie : movies) {
                movie.setImage(getImageAsBytes(movie.getImagePath()));
            }
            out.writeObject(movies);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSessionByMovieId getSessionByMovieId) {
        int movieId = getSessionByMovieId.getMovieId();
        try {
            out.writeObject(sessionDAO.getSessionsForMovie(movieDAO.getMovieById(movieId)));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetTicketByClientRequest getTicketByClientRequest) {
        int clientId = getTicketByClientRequest.getClientId();
        try {
            out.writeObject(ticketDAO.getTicketsByClient(clientId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSessionByIdRequest getSessionByIdRequest) {
        int sessionId = getSessionByIdRequest.getSessionId();
        try {
            out.writeObject(sessionDAO.getSessionById(sessionId));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetMovieByIdRequest getMovieByIdRequest) {
        int id = getMovieByIdRequest.getMovieId();
        try {
            Movie movie = movieDAO.getMovieById(id);
            movie.setImage(getImageAsBytes(movie.getImagePath()));
            out.writeObject(movie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CreateTicketRequest createTicketRequest) throws IOException {
        int clientId = createTicketRequest.getClientId();
        int sessionId = createTicketRequest.getSessionId();
        String ticketType = createTicketRequest.getTicketType();
        double price = createTicketRequest.getPrice();
        String seatCode = "A1"; // This can be changed to a dynamic value if needed
        String verificationCode = "123456789"; // This can be changed to a dynamic value if needed
        ticketDAO.addTicket(clientId, sessionId, ticketType, seatCode, price, verificationCode);
        out.writeObject("TICKET_CREATED");
    }

    @Override
    public void visit(DeleteSessionRequest deleteSessionRequest) throws IOException, SQLException {
        int sessionId = deleteSessionRequest.getSessionId();
        sessionDAO.removeSession(sessionId);
        out.writeObject("SESSION_DELETED");
    }
}