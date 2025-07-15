package com.irrigation.dao;

import com.irrigation.models.Client;
import com.irrigation.utils.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    public boolean create(Client client) {
        String sql = """
            INSERT INTO clients (nom, prenom, entreprise, telephone, email, adresse, ville, code_postal, pays) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEntreprise());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getVille());
            stmt.setString(8, client.getCodePostal());
            stmt.setString(9, client.getPays());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public Client read(int id) {
        String sql = """
            SELECT id, nom, prenom, entreprise, telephone, email, adresse, ville, code_postal, pays, date_creation, actif 
            FROM clients WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Client> readAll() {
        List<Client> clients = new ArrayList<>();
        String sql = """
            SELECT id, nom, prenom, entreprise, telephone, email, adresse, ville, code_postal, pays, date_creation, actif 
            FROM clients WHERE actif = TRUE ORDER BY nom, prenom
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
    
    public boolean update(Client client) {
        String sql = """
            UPDATE clients SET nom = ?, prenom = ?, entreprise = ?, telephone = ?, 
            email = ?, adresse = ?, ville = ?, code_postal = ?, pays = ? 
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEntreprise());
            stmt.setString(4, client.getTelephone());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getAdresse());
            stmt.setString(7, client.getVille());
            stmt.setString(8, client.getCodePostal());
            stmt.setString(9, client.getPays());
            stmt.setInt(10, client.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean delete(int id) {
        String sql = "UPDATE clients SET actif = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<Client> search(String keyword) {
        List<Client> clients = new ArrayList<>();
        String sql = """
            SELECT id, nom, prenom, entreprise, telephone, email, adresse, ville, code_postal, pays, date_creation, actif 
            FROM clients WHERE actif = TRUE AND 
            (nom LIKE ? OR prenom LIKE ? OR entreprise LIKE ? OR email LIKE ? OR telephone LIKE ?)
            ORDER BY nom, prenom
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
    
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEntreprise(rs.getString("entreprise"));
        client.setTelephone(rs.getString("telephone"));
        client.setEmail(rs.getString("email"));
        client.setAdresse(rs.getString("adresse"));
        client.setVille(rs.getString("ville"));
        client.setCodePostal(rs.getString("code_postal"));
        client.setPays(rs.getString("pays"));
        
        if (rs.getTimestamp("date_creation") != null) {
            client.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        }
        
        client.setActif(rs.getBoolean("actif"));
        
        return client;
    }
}
