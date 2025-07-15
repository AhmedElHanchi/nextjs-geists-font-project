package com.irrigation.views.panels;

import com.irrigation.models.Client;
import com.irrigation.models.Utilisateur;
import com.irrigation.dao.ClientDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientsPanel extends JPanel implements MainFrame.RefreshablePanel {
    private Utilisateur currentUser;
    private ClientDAO clientDAO;
    
    private JTable clientsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    public ClientsPanel(Utilisateur user) {
        this.currentUser = user;
        this.clientDAO = new ClientDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadClients();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Créer le modèle de table
        String[] columnNames = {"ID", "Nom", "Prénom", "Entreprise", "Téléphone", "Email", "Ville"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Créer la table
        clientsTable = new JTable(tableModel);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientsTable.getTableHeader().setReorderingAllowed(false);
        clientsTable.setRowHeight(25);
        
        // Configurer les colonnes
        clientsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        clientsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        clientsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        clientsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        clientsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        clientsTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        clientsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Barre de recherche
        searchField = new JTextField(20);
        searchField.setToolTipText("Rechercher un client...");
        
        // Boutons
        addButton = new JButton("Ajouter");
        addButton.setIcon(new ImageIcon(getClass().getResource("/images/add.png")));
        
        editButton = new JButton("Modifier");
        editButton.setIcon(new ImageIcon(getClass().getResource("/images/edit.png")));
        editButton.setEnabled(false);
        
        deleteButton = new JButton("Supprimer");
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/images/delete.png")));
        deleteButton.setEnabled(false);
        
        refreshButton = new JButton("Actualiser");
        refreshButton.setIcon(new ImageIcon(getClass().getResource("/images/refresh.png")));
    }
    
    private void setupLayout() {
        // Panel nord avec recherche et boutons
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        northPanel.add(searchPanel, BorderLayout.WEST);
        northPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Panel central avec la table
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        // Recherche
        searchField.addActionListener(e -> searchClients());
        
        // Boutons
        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteClient());
        refreshButton.addActionListener(e -> loadClients());
        
        // Sélection dans la table
        clientsTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = clientsTable.getSelectedRow() != -1;
            editButton.setEnabled(hasSelection);
            deleteButton.setEnabled(hasSelection && currentUser.isManager());
        });
        
        // Double-clic pour éditer
        clientsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showEditDialog();
                }
            }
        });
    }
    
    @Override
    public void refreshData() {
        loadClients();
    }
    
    @Override
    public void showAddDialog() {
        showClientDialog(null);
    }
    
    private void loadClients() {
        SwingWorker<List<Client>, Void> worker = new SwingWorker<List<Client>, Void>() {
            @Override
            protected List<Client> doInBackground() throws Exception {
                return clientDAO.readAll();
            }
            
            @Override
            protected void done() {
                try {
                    List<Client> clients = get();
                    updateTable(clients);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ClientsPanel.this, 
                        "Erreur lors du chargement des clients: " + e.getMessage(), 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void searchClients() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadClients();
            return;
        }
        
        SwingWorker<List<Client>, Void> worker = new SwingWorker<List<Client>, Void>() {
            @Override
            protected List<Client> doInBackground() throws Exception {
                return clientDAO.search(keyword);
            }
            
            @Override
            protected void done() {
                try {
                    List<Client> clients = get();
                    updateTable(clients);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(ClientsPanel.this, 
                        "Erreur lors de la recherche: " + e.getMessage(), 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void showClientDialog(Client client) {
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField entrepriseField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField adresseField = new JTextField();
        JTextField villeField = new JTextField();
        JTextField codePostalField = new JTextField();
        JTextField paysField = new JTextField("France");
        
        if (client != null) {
            nomField.setText(client.getNom());
            prenomField.setText(client.getPrenom());
            entrepriseField.setText(client.getEntreprise());
            telephoneField.setText(client.getTelephone());
            emailField.setText(client.getEmail());
            adresseField.setText(client.getAdresse());
            villeField.setText(client.getVille());
            codePostalField.setText(client.getCodePostal());
            paysField.setText(client.getPays());
        }
        
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Entreprise:"));
        panel.add(entrepriseField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telephoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Adresse:"));
        panel.add(adresseField);
        panel.add(new JLabel("Ville:"));
        panel.add(villeField);
        panel.add(new JLabel("Code Postal:"));
        panel.add(codePostalField);
        panel.add(new JLabel("Pays:"));
        panel.add(paysField);
        
        String title = client == null ? "Ajouter un client" : "Modifier le client";
        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            Client newClient = new Client();
            if (client != null) {
                newClient.setId(client.getId());
            }
            newClient.setNom(nomField.getText().trim());
            newClient.setPrenom(prenomField.getText().trim());
            newClient.setEntreprise(entrepriseField.getText().trim());
            newClient.setTelephone(telephoneField.getText().trim());
            newClient.setEmail(emailField.getText().trim());
            newClient.setAdresse(adresseField.getText().trim());
            newClient.setVille(villeField.getText().trim());
            newClient.setCodePostal(codePostalField.getText().trim());
            newClient.setPays(paysField.getText().trim());
            
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    if (client == null) {
                        return clientDAO.create(newClient);
                    } else {
                        return clientDAO.update(newClient);
                    }
                }
                
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            loadClients();
                            JOptionPane.showMessageDialog(ClientsPanel.this, 
                                client == null ? "Client ajouté avec succès." : "Client modifié avec succès.",
                                "Succès", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ClientsPanel.this, 
                                "Erreur lors de l'enregistrement.", 
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(ClientsPanel.this, 
                            "Erreur: " + e.getMessage(), 
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void showEditDialog() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow != -1) {
            int clientId = (int) clientsTable.getValueAt(selectedRow, 0);
            Client client = clientDAO.read(clientId);
            if (client != null) {
                showClientDialog(client);
            }
        }
    }
    
    private void deleteClient() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow != -1) {
            int clientId = (int) clientsTable.getValueAt(selectedRow, 0);
            String clientName = (String) clientsTable.getValueAt(selectedRow, 1) + " " + 
                               (String) clientsTable.getValueAt(selectedRow, 2);
            
            int result = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer le client '" + clientName + "' ?", 
                "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        return clientDAO.delete(clientId);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            boolean success = get();
                            if (success) {
                                loadClients();
                                JOptionPane.showMessageDialog(ClientsPanel.this, 
                                    "Client supprimé avec succès.", 
                                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(ClientsPanel.this, 
                                    "Erreur lors de la suppression.", 
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(ClientsPanel.this, 
                                "Erreur: " + e.getMessage(), 
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.execute();
            }
        }
    }
    
    private void updateTable(List<Client> clients) {
        tableModel.setRowCount(0);
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getEntreprise() != null ? client.getEntreprise() : "",
                client.getTelephone() != null ? client.getTelephone() : "",
                client.getEmail() != null ? client.getEmail() : "",
                client.getVille() != null ? client.getVille() : ""
            });
        }
    }
}
