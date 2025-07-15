package com.irrigation.views.panels;

import com.irrigation.models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class RapportsPanel extends JPanel implements MainFrame.RefreshablePanel {
    private Utilisateur currentUser;
    
    public RapportsPanel(Utilisateur user) {
        this.currentUser = user;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Génération de Rapports - En cours de développement");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        
        add(label, BorderLayout.CENTER);
    }
    
    @Override
    public void refreshData() {
        // À implémenter
    }
    
    @Override
    public void showAddDialog() {
        showExportDialog();
    }
    
    public void showExportDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        JComboBox<String> reportType = new JComboBox<>(new String[]{
            "Clients", "Fournisseurs", "Devis", "Factures", "Revenus"
        });
        
        JComboBox<String> exportFormat = new JComboBox<>(new String[]{
            "PDF", "Excel", "CSV"
        });
        
        JComboBox<String> dateRange = new JComboBox<>(new String[]{
            "Toutes les données", "Ce mois", "Ce trimestre", "Cette année"
        });
        
        panel.add(new JLabel("Type de rapport:"));
        panel.add(reportType);
        panel.add(new JLabel("Format d'export:"));
        panel.add(exportFormat);
        panel.add(new JLabel("Période:"));
        panel.add(dateRange);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Exporter des rapports", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, 
                "Fonctionnalité d'export à implémenter.\n" +
                "Type: " + reportType.getSelectedItem() + "\n" +
                "Format: " + exportFormat.getSelectedItem() + "\n" +
                "Période: " + dateRange.getSelectedItem(), 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
