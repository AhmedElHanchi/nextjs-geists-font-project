package com.irrigation.models;

import java.time.LocalDateTime;

public class Fournisseur {
    private int id;
    private String nom;
    private String entreprise;
    private String telephone;
    private String email;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
    private String numeroTVA;
    private String typeService; // Équipements, Matériaux, Services, etc.
    private LocalDateTime dateCreation;
    private boolean actif;
    
    // Constructeurs
    public Fournisseur() {
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
    }
    
    public Fournisseur(String nom, String entreprise, String telephone, String email, String typeService) {
        this();
        this.nom = nom;
        this.entreprise = entreprise;
        this.telephone = telephone;
        this.email = email;
        this.typeService = typeService;
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    
    public String getNumeroTVA() { return numeroTVA; }
    public void setNumeroTVA(String numeroTVA) { this.numeroTVA = numeroTVA; }
    
    public String getTypeService() { return typeService; }
    public void setTypeService(String typeService) { this.typeService = typeService; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    
    @Override
    public String toString() {
        return entreprise + " (" + nom + ")";
    }
}
