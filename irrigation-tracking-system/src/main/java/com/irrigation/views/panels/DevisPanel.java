package com.irrigation.views.panels;

import com.irrigation.models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class DevisPanel extends JPanel implements MainFrame.RefreshablePanel {
    private Utilisateur currentUser;
    
    public DevisPanel(Utilisateur user) {
        this.currentUser = user;
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel label = new JLabel("Gestion des Devis - En cours de développement");
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
            "Fonctionnalité d'ajout de devis à implémenter.", 
            "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
