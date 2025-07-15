package com.irrigation.views.panels;

import com.irrigation.models.Utilisateur;
import com.irrigation.utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private Utilisateur currentUser;
    private JLabel totalClientsLabel;
    private JLabel totalFournisseursLabel;
    private JLabel totalDevisLabel;
    private JLabel totalFacturesLabel;
    private JLabel totalRevenusLabel;
    private JLabel facturesEnRetardLabel;
    
    public DashboardPanel(Utilisateur user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        loadDashboardData();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Créer les labels de statistiques
        totalClientsLabel = createStatLabel("0", "Clients", new Color(33, 150, 243));
        totalFournisseursLabel = createStatLabel("0", "Fournisseurs", new Color(255, 152, 0));
        totalDevisLabel = createStatLabel("0", "Devis", new Color(0, 150, 136));
        totalFacturesLabel = createStatLabel("0", "Factures", new Color(156, 39, 176));
        totalRevenusLabel = createStatLabel("0 €", "Revenus Totaux", new Color(76, 175, 80));
        facturesEnRetardLabel = createStatLabel("0", "Factures en Retard", new Color(244, 67, 54));
    }
    
    private void setupLayout() {
        // Panel principal avec GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Titre
        JLabel titleLabel = new JLabel("Tableau de Bord");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.1;
        mainPanel.add(titleLabel, gbc);
        
        // Réinitialiser pour les statistiques
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        
        // Première ligne de statistiques
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(totalClientsLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(totalFournisseursLabel, gbc);
        
        gbc.gridx = 2;
        mainPanel.add(totalDevisLabel, gbc);
        
        // Deuxième ligne de statistiques
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(totalFacturesLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(totalRevenusLabel, gbc);
        
        gbc.gridx = 2;
        mainPanel.add(facturesEnRetardLabel, gbc);
        
        // Panel d'informations rapides
        JPanel infoPanel = createInfoPanel();
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.5;
        mainPanel.add(infoPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel de bienvenue
        JPanel welcomePanel = createWelcomePanel();
        add(welcomePanel, BorderLayout.NORTH);
    }
    
    private JLabel createStatLabel(String value, String label, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        labelLabel.setForeground(Color.DARK_GRAY);
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.add(labelLabel, BorderLayout.SOUTH);
        
        return new JLabel() {
            @Override
            public Component getUI() {
                return panel;
            }
        };
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "Informations Rapides"
        ));
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 12));
        infoArea.setText(getQuickInfo());
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(139, 195, 74));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel welcomeLabel = new JLabel("Bienvenue, " + currentUser.getNomComplet() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        panel.add(welcomeLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private String getQuickInfo() {
        return String.format(
            "Système de suivi d'irrigation ITMPRO\n\n" +
            "Utilisateur connecté: %s (%s)\n" +
            "Date: %s\n" +
            "Heure: %s\n\n" +
            "Pour commencer:\n" +
            "• Ajoutez des clients via l'onglet 'Clients'\n" +
            "• Gérez vos fournisseurs dans l'onglet 'Fournisseurs'\n" +
            "• Créez des équipes de travail\n" +
            "• Générez des devis et factures\n" +
            "• Exportez vos rapports en PDF ou Excel",
            currentUser.getNomComplet(),
            currentUser.getRole(),
            java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
    
    public void loadDashboardData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loadStats();
                return null;
            }
            
            @Override
            protected void done() {
                repaint();
            }
        };
        worker.execute();
    }
    
    private void loadStats() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Total clients
            totalClientsLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM clients WHERE actif = TRUE")));
            
            // Total fournisseurs
            totalFournisseursLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM fournisseurs WHERE actif = TRUE")));
            
            // Total devis
            totalDevisLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM devis")));
            
            // Total factures
            totalFacturesLabel.setText(String.valueOf(getCount(conn, "SELECT COUNT(*) FROM factures")));
            
            // Revenus totaux
            totalRevenusLabel.setText(String.format("%.2f €", getSum(conn, "SELECT SUM(montant_ttc) FROM factures WHERE statut = 'PAYEE'")));
            
            // Factures en retard
            facturesEnRetardLabel.setText(String.valueOf(getCount(conn, 
                "SELECT COUNT(*) FROM factures WHERE statut = 'EN_RETARD' OR (statut != 'PAYEE' AND date_echeance < CURDATE())")));
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des statistiques: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int getCount(Connection conn, String query) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private double getSum(Connection conn, String query) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }
}
