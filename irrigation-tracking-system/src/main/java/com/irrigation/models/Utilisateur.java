package com.irrigation.models;

import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse; // Stocké en hash
    private String nom;
    private String prenom;
    private String email;
    private String role; // ADMIN, MANAGER, EMPLOYE
    private boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime derniereConnexion;
    private String permissions; // JSON ou string séparée par virgules
    
    // Constructeurs
    public Utilisateur() {
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
        this.role = "EMPLOYE";
    }
    
    public Utilisateur(String nomUtilisateur, String motDePasse, String nom, String prenom, String email) {
        this();
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = hashMotDePasse(motDePasse);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public LocalDateTime getDerniereConnexion() { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime derniereConnexion) { this.derniereConnexion = derniereConnexion; }
    
    public String getPermissions() { return permissions; }
    public void setPermissions(String permissions) { this.permissions = permissions; }
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public void changerMotDePasse(String nouveauMotDePasse) {
        this.motDePasse = hashMotDePasse(nouveauMotDePasse);
    }
    
    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(hashMotDePasse(motDePasse));
    }
    
    public void mettreAJourDerniereConnexion() {
        this.derniereConnexion = LocalDateTime.now();
    }
    
    public boolean aPermission(String permission) {
        if (permissions == null) return false;
        return permissions.contains(permission) || role.equals("ADMIN");
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isManager() {
        return "MANAGER".equals(role) || isAdmin();
    }
    
    // Méthode pour hasher le mot de passe
    private String hashMotDePasse(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(motDePasse.getBytes());
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
    
    @Override
    public String toString() {
        return getNomComplet() + " (" + nomUtilisateur + ") - " + role;
    }
}
