package be.helha.applicine.server;

import be.helha.applicine.common.models.*;
import be.helha.applicine.common.models.event.Event;
import be.helha.applicine.common.models.exceptions.AdminIsAlreadyLoggedException;
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
    private Server server;

    public ClientHandler(Socket socket) throws IOException, SQLException {
        this.clientSocket = socket;
        //ajout du client dans la liste des clients connectés
        Server.clientsConnected.add(this);
        this.movieDAO = new MovieDAOImpl();
        this.clientsDAO = new ClientsDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.sessionDAO = new SessionDAOImpl();
        this.ticketDAO = new TicketDAOImpl();
        this.viewableDAO = new ViewableDAOImpl();
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.server = new Server();
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
        } finally {
            //suppression du client de la liste des clients connectés
            Server.clientsConnected.remove(this);
            System.out.println("Client disconnected");
            System.out.println("Number of clients connected: " + Server.clientsConnected.size());
        }
    }

    //effectue la requete de récupération des sagas liées à un film
    @Override
    public void visit(GetSagasLinkedToMovieRequest getSagasLinkedToMovieRequest) {
        int movieId = getSagasLinkedToMovieRequest.getMovieId();
        try {
            out.writeObject(viewableDAO.sagasLinkedToMovie(movieId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //effectue la requete de modification d'un film déjà existant
    @Override
    public void visit(UpdateMovieRequest updateMovieRequest) {
        //envoie d'une notification a tout les clients connectés pour les informer de la modification
        Movie movie = updateMovieRequest.getMovie();
        try {
            if (movie.getImage() != null) {
                movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
                FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
            }
            movieDAO.update(movie);
            out.writeObject("MOVIE_UPDATED");
            Event event = new Event("EVENT: UPDATE_MOVIE", movie);
            for(ClientHandler client : Server.clientsConnected) {
                client.out.writeObject(event);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //effectue la requete de récupération d'une salle par son id
    @Override
    public void visit(GetRoomByIdRequest getRoomByIdRequest) {
        int id = getRoomByIdRequest.getRoomId();
        try {
            Room room = roomDAO.get(id);
            out.writeObject(room);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(AddSessionRequest addSessionRequest) {
        MovieSession session = addSessionRequest.getSession();
        try {
            sessionDAO.create(session);
            out.writeObject("SESSION_ADDED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(UpdateSessionRequest updateSessionRequest) {
        MovieSession session = updateSessionRequest.getSession();
        try {
            sessionDAO.update(session);
            out.writeObject("SESSION_UPDATED");
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetRoomsRequest getRoomsRequest) {
        try {
            out.writeObject(roomDAO.getAll());
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
        ArrayList<Movie> sagaMovies = saga.getMovies();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Movie movie : sagaMovies) {
            ids.add(movie.getId());
        }
        viewableDAO.updateViewable(saga.getId(), saga.getTitle(), "Saga", ids);
        try {
            out.writeObject("VIEWABLE_UPDATED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetSessionsLinkedToMovieRequest getSessionsLinkedToMovieRequest) {
        int movieId = getSessionsLinkedToMovieRequest.getMovieId();
        int amountSessions = movieDAO.getSessionLinkedToMovie(movieId);
        try {
            out.writeObject(amountSessions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CreateMovieRequest createMovieRequest) throws IOException {
        Movie movie = createMovieRequest.getMovie();
        movie.setImagePath(FileManager.createPath(removeSpecialCharacters(movie.getTitle()) + ".jpg"));
        FileManager.createImageFromBytes(movie.getImage(), movie.getImagePath());
        movieDAO.create(movie);
        out.writeObject("MOVIE_ADDED");
    }

    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    @Override
    public void visit(ClientRegistrationRequest clientRegistrationRequest) throws IOException {
        Client client = clientRegistrationRequest.getClient();
        String hashedPassword = HashedPassword.getHashedPassword(client.getPassword());
        Client registeredClient = clientsDAO.create(new Client(client.getName(), client.getEmail(), client.getUsername(), hashedPassword));
        if (registeredClient != null) {
            out.writeObject("Registration successful");
        } else {
            out.writeObject("Registration failed");
        }
    }

    @Override
    public void visit(DeleteMoviesRequest deleteMoviesRequest) {
        try {
            movieDAO.delete(deleteMoviesRequest.getId());
            out.writeObject("MOVIE_DELETED");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetAllSessionRequest getAllSessionRequest) {
        try {
            out.writeObject(sessionDAO.getAll());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CheckLoginRequest checkLoginRequest) {
        String username = checkLoginRequest.getUsername();
        String password = checkLoginRequest.getPassword();
        //si l'utilisateur est un admin et qu'il n'y a pas de session admin active alors on crée une session admin
        if(username.equals("admin") && password.equals("admin") && !server.getAdminSession()){
            try {
                //un admin est connecté
                server.setAdminSession(true);
                System.out.println("Admin logged in");
                out.writeObject(new Client("admin", "admin", "admin", "admin"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        } else if(server.getAdminSession()){
            try {
                System.out.println("An admin is already logged in");
                out.writeObject(null);
                throw new AdminIsAlreadyLoggedException("An admin is already logged in");
            } catch (IOException | AdminIsAlreadyLoggedException e) {
                //je renvoie une exception pour informer le client qu'un admin est déjà connecté
                throw new RuntimeException(e);
            }
        }
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
            List<Movie> movies = movieDAO.getAll();
            for (Viewable movie : movies) {
                movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
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
            out.writeObject(sessionDAO.getSessionsForMovie(viewableDAO.getViewableById(movieId)));
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
            out.writeObject(sessionDAO.get(sessionId));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(GetMovieByIdRequest getMovieByIdRequest) {
        int id = getMovieByIdRequest.getMovieId();
        try {
            Movie movie = movieDAO.get(id);
            movie.setImage(FileManager.getImageAsBytes(movie.getImagePath()));
            out.writeObject(movie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visit(CreateTicketRequest createTicketRequest) throws IOException {
        Ticket ticket = createTicketRequest.getTicket();
        ticketDAO.create(ticket);
        out.writeObject("TICKET_CREATED");
    }

    @Override
    public void visit(DeleteSessionRequest deleteSessionRequest) throws IOException, SQLException {
        int sessionId = deleteSessionRequest.getSessionId();
        sessionDAO.delete(sessionId);
        out.writeObject("SESSION_DELETED");
    }
}