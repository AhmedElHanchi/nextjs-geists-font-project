package com.irrigation.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/database/database.properties";
    private static Connection connection = null;
    private static String url;
    private static String username;
    private static String password;
    
    static {
        loadDatabaseConfig();
    }
    
    private static void loadDatabaseConfig() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
                url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/irrigation_db");
                username = props.getProperty("db.username", "root");
                password = props.getProperty("db.password", "");
            } else {
                // Configuration par défaut
                url = "jdbc:mysql://localhost:3306/irrigation_db";
                username = "root";
                password = "";
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la configuration de base de données: " + e.getMessage());
            // Utiliser les valeurs par défaut
            url = "jdbc:mysql://localhost:3306/irrigation_db";
            username = "root";
            password = "";
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connexion à la base de données établie avec succès.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL non trouvé: " + e.getMessage());
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection testConn = getConnection()) {
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            System.err.println("Test de connexion échoué: " + e.getMessage());
            return false;
        }
    }
    
    public static void createDatabaseIfNotExists() {
        String createDbUrl = url.substring(0, url.lastIndexOf('/'));
        String dbName = url.substring(url.lastIndexOf('/') + 1);
        
        try (Connection conn = DriverManager.getConnection(createDbUrl, username, password);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("Base de données créée ou vérifiée: " + dbName);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la base de données: " + e.getMessage());
        }
    }
    
    public static void initializeTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Table utilisateurs
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS utilisateurs (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom_utilisateur VARCHAR(50) UNIQUE NOT NULL,
                    mot_de_passe VARCHAR(255) NOT NULL,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    email VARCHAR(150) UNIQUE NOT NULL,
                    role ENUM('ADMIN', 'MANAGER', 'EMPLOYE') DEFAULT 'EMPLOYE',
                    actif BOOLEAN DEFAULT TRUE,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    derniere_connexion TIMESTAMP NULL,
                    permissions TEXT
                )
            """);
            
            // Table clients
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS clients (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    entreprise VARCHAR(150),
                    telephone VARCHAR(20),
                    email VARCHAR(150),
                    adresse TEXT,
                    ville VARCHAR(100),
                    code_postal VARCHAR(10),
                    pays VARCHAR(50) DEFAULT 'France',
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    actif BOOLEAN DEFAULT TRUE
                )
            """);
            
            // Table fournisseurs
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS fournisseurs (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    entreprise VARCHAR(150) NOT NULL,
                    telephone VARCHAR(20),
                    email VARCHAR(150),
                    adresse TEXT,
                    ville VARCHAR(100),
                    code_postal VARCHAR(10),
                    pays VARCHAR(50) DEFAULT 'France',
                    numero_tva VARCHAR(50),
                    type_service VARCHAR(100),
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    actif BOOLEAN DEFAULT TRUE
                )
            """);
            
            // Table équipes de travail
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS equipes_travail (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    description TEXT,
                    chef VARCHAR(100) NOT NULL,
                    telephone VARCHAR(20),
                    email VARCHAR(150),
                    specialite VARCHAR(100),
                    membres TEXT,
                    disponible BOOLEAN DEFAULT TRUE,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    actif BOOLEAN DEFAULT TRUE
                )
            """);
            
            // Table devis
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS devis (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    numero VARCHAR(50) UNIQUE NOT NULL,
                    client_id INT NOT NULL,
                    client_nom VARCHAR(200),
                    titre VARCHAR(200) NOT NULL,
                    description TEXT,
                    date_creation DATE NOT NULL,
                    date_validite DATE NOT NULL,
                    statut ENUM('BROUILLON', 'ENVOYE', 'ACCEPTE', 'REFUSE', 'EXPIRE') DEFAULT 'BROUILLON',
                    montant_ht DECIMAL(10,2) DEFAULT 0,
                    taux_tva DECIMAL(5,2) DEFAULT 20.00,
                    montant_tva DECIMAL(10,2) DEFAULT 0,
                    montant_ttc DECIMAL(10,2) DEFAULT 0,
                    conditions TEXT,
                    notes TEXT,
                    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (client_id) REFERENCES clients(id)
                )
            """);
            
            // Table lignes de devis
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS lignes_devis (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    devis_id INT NOT NULL,
                    designation VARCHAR(200) NOT NULL,
                    description TEXT,
                    quantite DECIMAL(10,2) NOT NULL,
                    unite VARCHAR(20),
                    prix_unitaire DECIMAL(10,2) NOT NULL,
                    total DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (devis_id) REFERENCES devis(id) ON DELETE CASCADE
                )
            """);
            
            // Table factures
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS factures (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    numero VARCHAR(50) UNIQUE NOT NULL,
                    client_id INT NOT NULL,
                    client_nom VARCHAR(200),
                    devis_id INT NULL,
                    titre VARCHAR(200) NOT NULL,
                    description TEXT,
                    date_facture DATE NOT NULL,
                    date_echeance DATE NOT NULL,
                    statut ENUM('BROUILLON', 'ENVOYEE', 'PAYEE', 'EN_RETARD', 'ANNULEE') DEFAULT 'BROUILLON',
                    montant_ht DECIMAL(10,2) DEFAULT 0,
                    taux_tva DECIMAL(5,2) DEFAULT 20.00,
                    montant_tva DECIMAL(10,2) DEFAULT 0,
                    montant_ttc DECIMAL(10,2) DEFAULT 0,
                    montant_paye DECIMAL(10,2) DEFAULT 0,
                    montant_restant DECIMAL(10,2) DEFAULT 0,
                    mode_paiement ENUM('ESPECES', 'CHEQUE', 'VIREMENT', 'CARTE'),
                    conditions TEXT,
                    notes TEXT,
                    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    date_paiement DATE NULL,
                    FOREIGN KEY (client_id) REFERENCES clients(id),
                    FOREIGN KEY (devis_id) REFERENCES devis(id)
                )
            """);
            
            // Table lignes de factures
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS lignes_factures (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    facture_id INT NOT NULL,
                    designation VARCHAR(200) NOT NULL,
                    description TEXT,
                    quantite DECIMAL(10,2) NOT NULL,
                    unite VARCHAR(20),
                    prix_unitaire DECIMAL(10,2) NOT NULL,
                    total DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (facture_id) REFERENCES factures(id) ON DELETE CASCADE
                )
            """);
            
            System.out.println("Tables de base de données initialisées avec succès.");
            
            // Créer un utilisateur admin par défaut
            createDefaultAdmin();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation des tables: " + e.getMessage());
        }
    }
    
    private static void createDefaultAdmin() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Vérifier si l'admin existe déjà
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM utilisateurs WHERE nom_utilisateur = 'admin'");
            rs.next();
            if (rs.getInt(1) == 0) {
                // Créer l'utilisateur admin par défaut
                stmt.executeUpdate("""
                    INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, nom, prenom, email, role) 
                    VALUES ('admin', SHA2('admin123', 256), 'Administrateur', 'Système', 'admin@irrigation.com', 'ADMIN')
                """);
                System.out.println("Utilisateur admin créé (login: admin, mot de passe: admin123)");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur admin: " + e.getMessage());
        }
    }
}
