package be.helha.applicine.dao.impl;

import be.helha.applicine.dao.ClientsDAO;
import be.helha.applicine.database.DatabaseConnection;
import be.helha.applicine.models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
    private static final String INSERT_CLIENT = "INSERT INTO clients (name, email, username, password) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CLIENT = "UPDATE clients SET name = ?, email = ?, username = ?, password = ? WHERE id = ?";
    private static final String DELETE_CLIENT = "DELETE FROM clients WHERE id = ?";
    private static final String GET_CLIENT_BY_USERNAME = "SELECT * FROM clients WHERE username = ?";
    private static final String GET_CLIENT_BY_EMAIL = "SELECT * FROM clients WHERE email = ?";

    @Override
    public void createClient(String name, String email, String username, String password) {

    }

    @Override
    public void deleteClient(int clientId) {

    }

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

    @Override
    public Client getClient(int clientId) {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_CLIENT_BY_ID)){
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()){
                    return new Client(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void getAllClients() {

    }

    @Override
    public void getClientByUsername(String username) {

    }

    @Override
    public void getClientByEmail(String email) {

    }
}
