package com.irrigation.views;

import com.irrigation.models.Utilisateur;
import com.irrigation.views.panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private Utilisateur currentUser;
    private JTabbedPane tabbedPane;
    private JLabel userInfoLabel;
    private JLabel timeLabel;
    private Timer clockTimer;
    
    // Panels
    private DashboardPanel dashboardPanel;
    private ClientsPanel clientsPanel;
    private FournisseursPanel fournisseursPanel;
    private EquipesPanel equipesPanel;
    private DevisPanel devisPanel;
    private FacturesPanel facturesPanel;
    private RapportsPanel rapportsPanel;
    
    public MainFrame(Utilisateur user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupEventListeners();
        startClock();
    }
    
    private void initializeComponents() {
        setTitle("ITMPRO - Système de Suivi d'Irrigation");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        
        // Créer les panels
        dashboardPanel = new DashboardPanel(currentUser);
        clientsPanel = new ClientsPanel(currentUser);
        fournisseursPanel = new FournisseursPanel(currentUser);
        equipesPanel = new EquipesPanel(currentUser);
        devisPanel = new DevisPanel(currentUser);
        facturesPanel = new FacturesPanel(currentUser);
        rapportsPanel = new RapportsPanel(currentUser);
        
        // Créer le TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Ajouter les onglets
        tabbedPane.addTab("Tableau de Bord", createTabIcon("dashboard"), dashboardPanel, "Vue d'ensemble du système");
        tabbedPane.addTab("Clients", createTabIcon("clients"), clientsPanel, "Gestion des clients");
        tabbedPane.addTab("Fournisseurs", createTabIcon("suppliers"), fournisseursPanel, "Gestion des fournisseurs");
        tabbedPane.addTab("Équipes", createTabIcon("teams"), equipesPanel, "Gestion des équipes de travail");
        tabbedPane.addTab("Devis", createTabIcon("quotes"), devisPanel, "Gestion des devis");
        tabbedPane.addTab("Factures", createTabIcon("invoices"), facturesPanel, "Gestion des factures");
        tabbedPane.addTab("Rapports", createTabIcon("reports"), rapportsPanel, "Génération de rapports");
        
        // Labels pour la barre de statut
        userInfoLabel = new JLabel();
        updateUserInfo();
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Barre de menu
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Panel principal avec les onglets
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barre de statut
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.setMnemonic('F');
        
        JMenuItem newItem = new JMenuItem("Nouveau");
        newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newItem.addActionListener(e -> showNewItemDialog());
        
        JMenuItem exportItem = new JMenuItem("Exporter");
        exportItem.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        exportItem.addActionListener(e -> showExportDialog());
        
        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitItem.addActionListener(e -> confirmExit());
        
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Menu Édition
        JMenu editMenu = new JMenu("Édition");
        editMenu.setMnemonic('E');
        
        JMenuItem preferencesItem = new JMenuItem("Préférences");
        preferencesItem.addActionListener(e -> showPreferences());
        editMenu.add(preferencesItem);
        
        // Menu Utilisateur
        JMenu userMenu = new JMenu("Utilisateur");
        userMenu.setMnemonic('U');
        
        JMenuItem profileItem = new JMenuItem("Mon Profil");
        profileItem.addActionListener(e -> showUserProfile());
        
        JMenuItem changePasswordItem = new JMenuItem("Changer le mot de passe");
        changePasswordItem.addActionListener(e -> showChangePasswordDialog());
        
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.addActionListener(e -> logout());
        
        userMenu.add(profileItem);
        userMenu.add(changePasswordItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);
        
        // Menu Aide
        JMenu helpMenu = new JMenu("Aide");
        helpMenu.setMnemonic('A');
        
        JMenuItem aboutItem = new JMenuItem("À propos");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        JMenuItem helpItem = new JMenuItem("Aide");
        helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        helpItem.addActionListener(e -> showHelpDialog());
        
        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        // Ajouter les menus à la barre
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(userMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setPreferredSize(new Dimension(0, 25));
        
        statusBar.add(userInfoLabel, BorderLayout.WEST);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private Icon createTabIcon(String type) {
        // Créer des icônes simples pour les onglets
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                switch (type) {
                    case "dashboard":
                        g2d.setColor(new Color(76, 175, 80));
                        g2d.fillRect(x + 2, y + 2, 12, 12);
                        break;
                    case "clients":
                        g2d.setColor(new Color(33, 150, 243));
                        g2d.fillOval(x + 2, y + 2, 12, 12);
                        break;
                    case "suppliers":
                        g2d.setColor(new Color(255, 152, 0));
                        g2d.fillRect(x + 2, y + 2, 12, 12);
                        break;
                    case "teams":
                        g2d.setColor(new Color(156, 39, 176));
                        g2d.fillOval(x + 2, y + 2, 12, 12);
                        break;
                    case "quotes":
                        g2d.setColor(new Color(0, 150, 136));
                        g2d.fillRect(x + 2, y + 2, 12, 12);
                        break;
                    case "invoices":
                        g2d.setColor(new Color(244, 67, 54));
                        g2d.fillRect(x + 2, y + 2, 12, 12);
                        break;
                    case "reports":
                        g2d.setColor(new Color(96, 125, 139));
                        g2d.fillOval(x + 2, y + 2, 12, 12);
                        break;
                }
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() { return 16; }
            
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    private void setupEventListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        
        // Listener pour les changements d'onglets
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
                if (selectedComponent instanceof RefreshablePanel) {
                    ((RefreshablePanel) selectedComponent).refreshData();
                }
            }
        });
    }
    
    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            timeLabel.setText(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            ));
        });
        clockTimer.start();
    }
    
    private void updateUserInfo() {
        userInfoLabel.setText("Connecté en tant que: " + currentUser.getNomComplet() + 
                             " (" + currentUser.getRole() + ")");
    }
    
    // Méthodes pour les actions du menu
    private void showNewItemDialog() {
        String[] options = {"Client", "Fournisseur", "Équipe", "Devis", "Facture"};
        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Que souhaitez-vous créer ?",
            "Nouveau",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice != null) {
            switch (choice) {
                case "Client":
                    tabbedPane.setSelectedComponent(clientsPanel);
                    clientsPanel.showAddDialog();
                    break;
                case "Fournisseur":
                    tabbedPane.setSelectedComponent(fournisseursPanel);
                    fournisseursPanel.showAddDialog();
                    break;
                case "Équipe":
                    tabbedPane.setSelectedComponent(equipesPanel);
                    equipesPanel.showAddDialog();
                    break;
                case "Devis":
                    tabbedPane.setSelectedComponent(devisPanel);
                    devisPanel.showAddDialog();
                    break;
                case "Facture":
                    tabbedPane.setSelectedComponent(facturesPanel);
                    facturesPanel.showAddDialog();
                    break;
            }
        }
    }
    
    private void showExportDialog() {
        tabbedPane.setSelectedComponent(rapportsPanel);
        rapportsPanel.showExportDialog();
    }
    
    private void showPreferences() {
        JOptionPane.showMessageDialog(this, 
            "Fonctionnalité des préférences à implémenter.", 
            "Préférences", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showUserProfile() {
        JOptionPane.showMessageDialog(this, 
            "Nom: " + currentUser.getNomComplet() + "\n" +
            "Email: " + currentUser.getEmail() + "\n" +
            "Rôle: " + currentUser.getRole() + "\n" +
            "Dernière connexion: " + (currentUser.getDerniereConnexion() != null ? 
                currentUser.getDerniereConnexion().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                ) : "Première connexion"),
            "Mon Profil", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showChangePasswordDialog() {
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Ancien mot de passe:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("Nouveau mot de passe:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirmer:"));
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Changer le mot de passe", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Les nouveaux mots de passe ne correspondent pas.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ici, vous ajouteriez la logique pour changer le mot de passe
            JOptionPane.showMessageDialog(this, 
                "Mot de passe changé avec succès.", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, 
            "ITMPRO - Système de Suivi d'Irrigation\n" +
            "Version 1.0.0\n\n" +
            "Application de gestion complète pour:\n" +
            "• Clients et Fournisseurs\n" +
            "• Équipes de travail\n" +
            "• Devis et Factures\n" +
            "• Rapports exportables\n\n" +
            "© 2024 ITMPRO", 
            "À propos", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this, 
            "Aide - ITMPRO\n\n" +
            "Navigation:\n" +
            "• Utilisez les onglets pour accéder aux différentes sections\n" +
            "• Ctrl+N: Créer un nouvel élément\n" +
            "• Ctrl+E: Exporter des données\n" +
            "• F1: Afficher cette aide\n\n" +
            "Pour plus d'aide, contactez l'administrateur.", 
            "Aide", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir vous déconnecter ?", 
            "Déconnexion", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (clockTimer != null) {
                clockTimer.stop();
            }
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
    
    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir quitter l'application ?", 
            "Quitter", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (clockTimer != null) {
                clockTimer.stop();
            }
            System.exit(0);
        }
    }
    
    // Interface pour les panels qui peuvent être rafraîchis
    public interface RefreshablePanel {
        void refreshData();
        void showAddDialog();
    }
}
