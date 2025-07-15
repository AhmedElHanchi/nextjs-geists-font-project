package com.irrigation.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Facture {
    private int id;
    private String numero;
    private int clientId;
    private String clientNom;
    private int devisId; // Référence au devis si applicable
    private String titre;
    private String description;
    private LocalDate dateFacture;
    private LocalDate dateEcheance;
    private String statut; // BROUILLON, ENVOYEE, PAYEE, EN_RETARD, ANNULEE
    private List<LigneFacture> lignes;
    private double montantHT;
    private double tauxTVA;
    private double montantTVA;
    private double montantTTC;
    private double montantPaye;
    private double montantRestant;
    private String modePaiement; // ESPECES, CHEQUE, VIREMENT, CARTE
    private String conditions;
    private String notes;
    private LocalDateTime dateModification;
    private LocalDate datePaiement;
    
    // Constructeurs
    public Facture() {
        this.dateFacture = LocalDate.now();
        this.dateEcheance = LocalDate.now().plusDays(30);
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
        this.tauxTVA = 20.0; // 20% par défaut
        this.montantPaye = 0.0;
        this.dateModification = LocalDateTime.now();
    }
    
    public Facture(int clientId, String clientNom, String titre) {
        this();
        this.clientId = clientId;
        this.clientNom = clientNom;
        this.titre = titre;
        this.numero = genererNumero();
    }
    
    // Constructeur à partir d'un devis
    public Facture(Devis devis) {
        this();
        this.clientId = devis.getClientId();
        this.clientNom = devis.getClientNom();
        this.devisId = devis.getId();
        this.titre = devis.getTitre();
        this.description = devis.getDescription();
        this.tauxTVA = devis.getTauxTVA();
        this.numero = genererNumero();
        
        // Copier les lignes du devis
        for (Devis.LigneDevis ligneDevis : devis.getLignes()) {
            LigneFacture ligneFacture = new LigneFacture(
                ligneDevis.getDesignation(),
                ligneDevis.getQuantite(),
                ligneDevis.getUnite(),
                ligneDevis.getPrixUnitaire()
            );
            ligneFacture.setDescription(ligneDevis.getDescription());
            this.lignes.add(ligneFacture);
        }
        calculerMontants();
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
    
    public int getDevisId() { return devisId; }
    public void setDevisId(int devisId) { this.devisId = devisId; }
    
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }
    
    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public List<LigneFacture> getLignes() { return lignes; }
    public void setLignes(List<LigneFacture> lignes) { this.lignes = lignes; }
    
    public double getMontantHT() { return montantHT; }
    public void setMontantHT(double montantHT) { this.montantHT = montantHT; }
    
    public double getTauxTVA() { return tauxTVA; }
    public void setTauxTVA(double tauxTVA) { this.tauxTVA = tauxTVA; }
    
    public double getMontantTVA() { return montantTVA; }
    public void setMontantTVA(double montantTVA) { this.montantTVA = montantTVA; }
    
    public double getMontantTTC() { return montantTTC; }
    public void setMontantTTC(double montantTTC) { this.montantTTC = montantTTC; }
    
    public double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(double montantPaye) { 
        this.montantPaye = montantPaye;
        this.montantRestant = montantTTC - montantPaye;
        if (montantPaye >= montantTTC) {
            this.statut = "PAYEE";
            this.datePaiement = LocalDate.now();
        }
    }
    
    public double getMontantRestant() { return montantRestant; }
    public void setMontantRestant(double montantRestant) { this.montantRestant = montantRestant; }
    
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
    
    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    
    // Méthodes utilitaires
    public void ajouterLigne(LigneFacture ligne) {
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
        montantHT = lignes.stream().mapToDouble(LigneFacture::getTotal).sum();
        montantTVA = montantHT * tauxTVA / 100;
        montantTTC = montantHT + montantTVA;
        montantRestant = montantTTC - montantPaye;
        dateModification = LocalDateTime.now();
    }
    
    private String genererNumero() {
        return "FACT-" + LocalDate.now().getYear() + "-" + System.currentTimeMillis() % 10000;
    }
    
    public boolean isEnRetard() {
        return LocalDate.now().isAfter(dateEcheance) && !statut.equals("PAYEE");
    }
    
    public long getJoursRetard() {
        if (isEnRetard()) {
            return LocalDate.now().toEpochDay() - dateEcheance.toEpochDay();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return numero + " - " + clientNom + " (" + montantTTC + "€) - " + statut;
    }
    
    // Classe interne pour les lignes de facture
    public static class LigneFacture {
        private String designation;
        private String description;
        private double quantite;
        private String unite;
        private double prixUnitaire;
        private double total;
        
        public LigneFacture() {}
        
        public LigneFacture(String designation, double quantite, String unite, double prixUnitaire) {
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
