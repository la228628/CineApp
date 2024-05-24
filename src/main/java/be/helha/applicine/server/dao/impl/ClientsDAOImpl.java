package be.helha.applicine.server.dao.impl;

import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.Client;

import java.sql.*;

/**
 * ClientsDAOImpl is the implementation of the ClientsDAO interface.
 * It is used to interact with the database and perform CRUD operations on the clients table.
 */
public class ClientsDAOImpl implements ClientsDAO {

    private final Connection connection;
    /**
     * Constructor used to create a connection to the database.
     */
    public ClientsDAOImpl(){
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Constructor used for testing purposes.
     * @param connection The connection to the database.
     */
    public ClientsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM clients WHERE id = ?";
    private static final String INSERT_CLIENT = "INSERT INTO clients (name, email, username, hashedpassword) VALUES (?, ?, ?, ?)";
    private static final String GET_CLIENT_BY_USERNAME = "SELECT * FROM clients WHERE username = ?";
    private static final String GET_CLIENT_BY_EMAIL = "SELECT * FROM clients WHERE email = ?";

    /**
     * Create a client from the given parameters.
     * The client is then added to the database.
     * If a client with the same username or email already exists, a DaoException is thrown.
     * The client is then returned.
     * @param client The client created
     */

    @Override
    public Client create(Client client) throws DaoException {
        try {
            if (getClientByUsername(client.getUsername()) != null) {
                throw new DaoException("Un client avec le même nom d'utilisateur existe déjà");
            }
            if (getClientByEmail(client.getEmail()) != null) {
                throw new DaoException("Un client avec le même email existe déjà");
            }

            try (PreparedStatement statement = connection.prepareStatement(INSERT_CLIENT)) {
                statement.setString(1, client.getName());
                statement.setString(2, client.getEmail());
                statement.setString(3, client.getUsername());
                statement.setString(4, client.getPassword());
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                }
            }
            return getClientByUsername(client.getUsername());
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la création du client");
        }
    }

    /**
     * Get a client by its id.
     *
     * @param clientId The id of the client to get.
     * @return Client
     */

    @Override
    public Client get(int clientId) throws DaoException {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_CLIENT_BY_ID)) {
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du client");
        }
        //si le client n'existe pas, on retourne null pour éviter de renvoyer un objet vide
        return null;
    }

    /**
     * Get a client by its username.
     *
     * @param username The username of the client to get.
     * @return Client
     */

    @Override
    public Client getClientByUsername(String username) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(GET_CLIENT_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du client par nom d'utilisateur");
        }
        return null;
    }

    /**
     * Get a client by its email.
     *
     * @param email The email of the client to get.
     * @return Client
     */
    @Override
    public Client getClientByEmail(String email) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(GET_CLIENT_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la récupération du client par email");
        }
        return null;
    }

    /**
     * Delete all clients. This method is used for testing purposes.
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM clients")) {
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de tous les clients : " + e.getMessage());
        }
    }
}
