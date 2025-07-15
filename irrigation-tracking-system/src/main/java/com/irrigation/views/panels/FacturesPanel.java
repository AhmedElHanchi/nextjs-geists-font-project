package com.irrigation.views.panels;

import com.irrigation.models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class FacturesPanel extends JPanel implements MainFrame.RefreshablePanel {
    private Utilisateur currentUser;
    
    public FacturesPanel(Utilisateur user) {
        this.currentUser = user;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Gestion des Factures - En cours de développement");
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
        JOptionPane.showMessageDialog(this, 
            "Fonctionnalité d'ajout de factures à implémenter.", 
            "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
