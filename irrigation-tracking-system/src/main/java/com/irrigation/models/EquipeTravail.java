package com.irrigation.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class EquipeTravail {
    private int id;
    private String nom;
    private String description;
    private String chef; // Chef d'équipe
    private String telephone;
    private String email;
    private String specialite; // Installation, Maintenance, Réparation, etc.
    private List<String> membres; // Liste des membres de l'équipe
    private boolean disponible;
    private LocalDateTime dateCreation;
    private boolean actif;
    
    // Constructeurs
    public EquipeTravail() {
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
        this.disponible = true;
        this.membres = new ArrayList<>();
    }
    
    public EquipeTravail(String nom, String chef, String specialite) {
        this();
        this.nom = nom;
        this.chef = chef;
        this.specialite = specialite;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getChef() { return chef; }
    public void setChef(String chef) { this.chef = chef; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public List<String> getMembres() { return membres; }
    public void setMembres(List<String> membres) { this.membres = membres; }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    // Méthodes utilitaires
    public void ajouterMembre(String membre) {
        if (!membres.contains(membre)) {
            membres.add(membre);
        }
    }
    
    public void supprimerMembre(String membre) {
        membres.remove(membre);
    }
    
    public int getNombreMembres() {
        return membres.size();
    }
    
    public String getMembresString() {
        return String.join(", ", membres);
    }
    
    @Override
    public String toString() {
        return nom + " - " + chef + " (" + specialite + ")";
    }
}
