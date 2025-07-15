package com.irrigation.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Devis {
    private int id;
    private String numero;
    private int clientId;
    private String clientNom;
    private String titre;
    private String description;
    private LocalDate dateCreation;
    private LocalDate dateValidite;
    private String statut; // BROUILLON, ENVOYE, ACCEPTE, REFUSE, EXPIRE
    private List<LigneDevis> lignes;
    private double montantHT;
    private double tauxTVA;
    private double montantTVA;
    private double montantTTC;
    private String conditions;
    private String notes;
    private LocalDateTime dateModification;
    
    // Constructeurs
    public Devis() {
        this.dateCreation = LocalDate.now();
        this.dateValidite = LocalDate.now().plusDays(30);
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
        this.tauxTVA = 20.0; // 20% par défaut
        this.dateModification = LocalDateTime.now();
    }
    
    public Devis(int clientId, String clientNom, String titre) {
        this();
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.titre = titre;
        this.numero = genererNumero();
    }
    
    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    
    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
    
    public LocalDate getDateValidite() { return dateValidite; }
    public void setDateValidite(LocalDate dateValidite) { this.dateValidite = dateValidite; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public List<LigneDevis> getLignes() { return lignes; }
    public void setLignes(List<LigneDevis> lignes) { this.lignes = lignes; }
    
    public double getMontantHT() { return montantHT; }
    public void setMontantHT(double montantHT) { this.montantHT = montantHT; }
    
    public double getTauxTVA() { return tauxTVA; }
    public void setTauxTVA(double tauxTVA) { this.tauxTVA = tauxTVA; }
    
    public double getMontantTVA() { return montantTVA; }
    public void setMontantTVA(double montantTVA) { this.montantTVA = montantTVA; }
    
    public double getMontantTTC() { return montantTTC; }
    public void setMontantTTC(double montantTTC) { this.montantTTC = montantTTC; }
    
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    
    // Méthodes utilitaires
    public void ajouterLigne(LigneDevis ligne) {
        lignes.add(ligne);
        calculerMontants();
    }
    
    public void supprimerLigne(int index) {
        if (index >= 0 && index < lignes.size()) {
            lignes.remove(index);
            calculerMontants();
        }
    }
    
    public void calculerMontants() {
        montantHT = lignes.stream().mapToDouble(LigneDevis::getTotal).sum();
        montantTVA = montantHT * tauxTVA / 100;
        montantTTC = montantHT + montantTVA;
        dateModification = LocalDateTime.now();
    }
    
    private String genererNumero() {
        return "DEV-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis() % 10000;
    }
    
    public boolean isExpire() {
        return LocalDate.now().isAfter(dateValidite);
    }
    
    @Override
    public String toString() {
        return numero + " - " + clientNom + " (" + montantTTC + "€)";
    }
    
    // Classe interne pour les lignes de devis
    public static class LigneDevis {
        private String designation;
        private String description;
        private double quantite;
        private String unite;
        private double prixUnitaire;
        private double total;
        
        public LigneDevis() {}
        
        public LigneDevis(String designation, double quantite, String unite, double prixUnitaire) {
            this.designation = designation;
            this.quantite = quantite;
            this.unite = unite;
            this.prixUnitaire = prixUnitaire;
            this.total = quantite * prixUnitaire;
        }
        
        // Getters et Setters
        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public double getQuantite() { return quantite; }
        public void setQuantite(double quantite) { 
            this.quantite = quantite;
            this.total = quantite * prixUnitaire;
        }
        
        public String getUnite() { return unite; }
        public void setUnite(String unite) { this.unite = unite; }
        
        public double getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(double prixUnitaire) { 
            this.prixUnitaire = prixUnitaire;
            this.total = quantite * prixUnitaire;
        }
        
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
    }
}
