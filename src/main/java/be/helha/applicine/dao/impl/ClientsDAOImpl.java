package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientsDAOImpl implements ClientsDAO {

    private final Connection connection;

    public ClientsDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }

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
     * @param name
     * @param email
     * @param username
     * @param password
     */

    @Override
    public void createClient(String name, String email, String username, String password) {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_CLIENT)){
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()){
                if (rs.next()){
                    System.out.println("Client créé avec l'id : " + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du client : " + e.getMessage());
        }

    }

    @Override
    public void deleteClient(int clientId) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT)){
            preparedStatement.setInt(1, clientId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }

    /**
     * Update a client with the given parameters.
     * @param clientId
     * @param name
     * @param email
     * @param username
     * @param password
     */
    @Override
    public void updateClient(int clientId, String name, String email, String username, String password) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT)){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, clientId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du client : " + e.getMessage());
        }
    }
    /**
     * Get a client by its id.
     * @param clientId
     * @return
     */

    @Override
    public Client getClient(int clientId) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_CLIENT_BY_ID)){
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        //si le client n'existe pas, on retourne null pour éviter de renvoyer un objet vide
        return null;
    }

    /**
     * Get all clients.
     * @return
     */
    @Override
    public ArrayList<Client> getAllClients() {
        ArrayList<Client> clients = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_CLIENTS);
             ResultSet rs = pstmt.executeQuery()){
            while (rs.next()){
                clients.add(new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("password")));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la liste des clients : " + e.getMessage());
        }
        return clients;
    }

    /**
     * Get a client by its username.
     * @param username
     * @return
     */

    @Override
    public Client getClientByUsername(String username) {
        try(PreparedStatement statement = connection.prepareStatement(GET_CLIENT_BY_USERNAME)){
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client par username : " + e.getMessage());
        }
        return null;
    }

    /**
     * Get a client by its email.
     * @param email
     * @return
     */
    @Override
    public Client getClientByEmail(String email) {
        try (PreparedStatement statement = connection.prepareStatement(GET_CLIENT_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("hashedpassword"));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client par mail : " + e.getMessage());
        }
        return null;
    }
}
