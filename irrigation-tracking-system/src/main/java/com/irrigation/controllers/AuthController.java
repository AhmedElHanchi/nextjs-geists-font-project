package com.irrigation.controllers;

import com.irrigation.models.Utilisateur;
import com.irrigation.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthController {
    
    public Utilisateur authenticate(String emailOrUsername, String password) {
        String hashedPassword = hashPassword(password);
        String sql = """
            SELECT id, nom_utilisateur, mot_de_passe, nom, prenom, email, role, actif, 
                   date_creation, derniere_connexion, permissions 
            FROM utilisateurs 
            WHERE (email = ? OR nom_utilisateur = ?) AND mot_de_passe = ? AND actif = TRUE
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            stmt.setString(3, hashedPassword);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setNomUtilisateur(rs.getString("nom_utilisateur"));
                user.setMotDePasse(rs.getString("mot_de_passe"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setActif(rs.getBoolean("actif"));
                
                // Convertir les timestamps
                if (rs.getTimestamp("date_creation") != null) {
                    user.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                }
                if (rs.getTimestamp("derniere_connexion") != null) {
                    user.setDerniereConnexion(rs.getTimestamp("derniere_connexion").toLocalDateTime());
                }
                
                user.setPermissions(rs.getString("permissions"));
                
                // Mettre à jour la dernière connexion
                updateLastLogin(user.getId());
                user.setDerniereConnexion(LocalDateTime.now());
                
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean createUser(String nomUtilisateur, String password, String nom, String prenom, String email, String role) {
        // Vérifier si l'utilisateur existe déjà
        if (userExists(nomUtilisateur, email)) {
            return false;
        }
        
        String hashedPassword = hashPassword(password);
        String sql = """
            INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, nom, prenom, email, role) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, nom);
            stmt.setString(4, prenom);
            stmt.setString(5, email);
            stmt.setString(6, role);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // Vérifier l'ancien mot de passe
        String checkSql = "SELECT mot_de_passe FROM utilisateurs WHERE id = ?";
        String updateSql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Vérifier l'ancien mot de passe
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    String currentHashedPassword = rs.getString("mot_de_passe");
                    String oldHashedPassword = hashPassword(oldPassword);
                    
                    if (!currentHashedPassword.equals(oldHashedPassword)) {
                        return false; // Ancien mot de passe incorrect
                    }
                } else {
                    return false; // Utilisateur non trouvé
                }
            }
            
            // Mettre à jour avec le nouveau mot de passe
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                String newHashedPassword = hashPassword(newPassword);
                updateStmt.setString(1, newHashedPassword);
                updateStmt.setInt(2, userId);
                
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du changement de mot de passe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean resetPassword(String emailOrUsername, String newPassword) {
        String sql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE (email = ? OR nom_utilisateur = ?) AND actif = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = hashPassword(newPassword);
            stmt.setString(1, hashedPassword);
            stmt.setString(2, emailOrUsername);
            stmt.setString(3, emailOrUsername);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la réinitialisation du mot de passe: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deactivateUser(int userId) {
        String sql = "UPDATE utilisateurs SET actif = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désactivation de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean activateUser(int userId) {
        String sql = "UPDATE utilisateurs SET actif = TRUE WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean userExists(String nomUtilisateur, String email) {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE nom_utilisateur = ? OR email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, email);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'existence de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    private void updateLastLogin(int userId) {
        String sql = "UPDATE utilisateurs SET derniere_connexion = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la dernière connexion: " + e.getMessage());
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }
    
    public boolean isValidPassword(String password) {
        // Validation basique du mot de passe
        return password != null && password.length() >= 6;
    }
    
    public boolean isValidEmail(String email) {
        // Validation basique de l'email
        return email != null && email.contains("@") && email.contains(".");
    }
}
