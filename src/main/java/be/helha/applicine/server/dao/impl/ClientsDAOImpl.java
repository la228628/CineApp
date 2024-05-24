package be.helha.applicine.server.dao.impl;

import be.helha.applicine.common.models.exceptions.DaoException;
import be.helha.applicine.server.dao.ClientsDAO;
import be.helha.applicine.server.database.DatabaseConnection;
import be.helha.applicine.common.models.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientsDAOImpl implements ClientsDAO {

    private final Connection connection;

    public ClientsDAOImpl(){
        this.connection = DatabaseConnection.getConnection();
    }

    //constructor pour les tests
    public ClientsDAOImpl(Connection connection) {
        this.connection = connection;
    }

    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM clients";
    private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM clients WHERE id = ?";
    private static final String INSERT_CLIENT = "INSERT INTO clients (name, email, username, hashedpassword) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CLIENT = "UPDATE clients SET name = ?, email = ?, username = ?, hashedpassword = ? WHERE id = ?";
    private static final String DELETE_CLIENT = "DELETE FROM clients WHERE id = ?";
    private static final String GET_CLIENT_BY_USERNAME = "SELECT * FROM clients WHERE username = ?";
    private static final String GET_CLIENT_BY_EMAIL = "SELECT * FROM clients WHERE email = ?";

    /**
     * Create a client from the given parameters.
     *
     * @param client the client to create
     * @return the created client
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
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating client" + " "+ e.getMessage());
        }
    }

    @Override
    public void delete(int clientId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT)) {
            preparedStatement.setInt(1, clientId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }

    /**
     * Update a client with the given parameters.
     *
     * @param client the client to update
     */
    @Override
    public void update(Client client) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getUsername());
            preparedStatement.setString(4, client.getPassword());
            preparedStatement.setInt(5, client.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du client : " + e.getMessage());
        } catch (SQLException e) {
            throw new DaoException("Erreur lors de la création du client");
        }
    }

    /**
     * Get a client by its id.
     *
     * @param clientId the id of the client
     * @return the client
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
        return null;
    }

    /**
     * Get all clients.
     *
     * @return the list of clients
     */
    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> clients = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_CLIENTS);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("password")));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la liste des clients : " + e.getMessage());
        }
        return clients;
    }

    /**
     * Get a client by its username.
     *
     * @param username the username of the client
     * @return the client
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
     * @param email the email of the client
     * @return the client
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

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM clients")) {
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de tous les clients : " + e.getMessage());
        }
    }
}
