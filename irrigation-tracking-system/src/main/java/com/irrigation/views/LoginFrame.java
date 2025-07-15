package com.irrigation.views;

import com.irrigation.controllers.AuthController;
import com.irrigation.models.Utilisateur;
import com.irrigation.utils.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton closeButton;
    private JLabel forgotPasswordLabel;
    private JLabel registerLabel;
    private AuthController authController;
    
    public LoginFrame() {
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        // Initialiser la base de données
        DatabaseConnection.createDatabaseIfNotExists();
        DatabaseConnection.initializeTables();
    }
    
    private void initializeComponents() {
        setTitle("ITMPRO - Système d'Irrigation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 600);
        setLocationRelativeTo(null);
        
        // Définir l'icône de l'application
        try {
            setIconImage(createLogoImage());
        } catch (Exception e) {
            System.err.println("Impossible de charger l'icône: " + e.getMessage());
        }
        
        // Champs de saisie
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Boutons
        loginButton = new JButton("Se connecter");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton = new JButton("Fermer");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(244, 67, 54));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Labels
        forgotPasswordLabel = new JLabel("Mot de passe / Nom d'utilisateur oublié ?");
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(new Color(33, 150, 243));
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        registerLabel = new JLabel("Vous n'avez pas de compte ?");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLabel.setForeground(Color.GRAY);
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel registerLinkLabel = new JLabel("Inscrivez-vous");
        registerLinkLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLinkLabel.setForeground(new Color(33, 150, 243));
        registerLinkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLinkLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Header avec titre
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(139, 195, 74));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("ITMPRO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // Logo panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(createLogoImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        logoPanel.add(logoLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Email field
        JLabel emailLabel = new JLabel("Adresse e-mail");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Password field
        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        buttonPanel.add(loginButton);
        buttonPanel.add(closeButton);
        
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(forgotPasswordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(registerLabel);
        
        // Ajouter tous les panels
        add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(logoPanel);
        mainPanel.add(formPanel);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Permettre la connexion avec Enter
        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        };
        
        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showForgotPasswordDialog();
            }
        });
    }
    
    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir votre email et mot de passe.", 
                "Champs requis", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Désactiver le bouton pendant la vérification
        loginButton.setEnabled(false);
        loginButton.setText("Connexion...");
        
        SwingWorker<Utilisateur, Void> worker = new SwingWorker<Utilisateur, Void>() {
            @Override
            protected Utilisateur doInBackground() throws Exception {
                return authController.authenticate(email, password);
            }
            
            @Override
            protected void done() {
                try {
                    Utilisateur user = get();
                    if (user != null) {
                        // Connexion réussie
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            new MainFrame(user).setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, 
                            "Email ou mot de passe incorrect.", 
                            "Erreur de connexion", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Erreur lors de la connexion: " + e.getMessage(), 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                    loginButton.setEnabled(true);
                    loginButton.setText("Se connecter");
                    passwordField.setText("");
                }
            }
        };
        
        worker.execute();
    }
    
    private void showForgotPasswordDialog() {
        JOptionPane.showMessageDialog(this, 
            "Contactez l'administrateur système pour réinitialiser votre mot de passe.\n" +
            "Email: admin@irrigation.com", 
            "Mot de passe oublié", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Image createLogoImage() {
        // Créer un logo simple avec du texte
        BufferedImage image = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Activer l'antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dessiner un cercle de fond
        g2d.setColor(new Color(139, 195, 74));
        g2d.fillOval(10, 10, 100, 100);
        
        // Dessiner le contour
        g2d.setColor(new Color(76, 175, 80));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(10, 10, 100, 100);
        
        // Dessiner le texte "IRRIGATION TMPRO"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        
        String text1 = "IRRIGATION";
        String text2 = "TMPRO";
        
        int x1 = (120 - fm.stringWidth(text1)) / 2;
        int x2 = (120 - fm.stringWidth(text2)) / 2;
        
        g2d.drawString(text1, x1, 50);
        g2d.drawString(text2, x2, 70);
        
        // Dessiner des éléments décoratifs (gouttes d'eau)
        g2d.setColor(new Color(33, 150, 243));
        for (int i = 0; i < 3; i++) {
            int x = 30 + i * 20;
            int y = 80;
            g2d.fillOval(x, y, 8, 12);
        }
        
        g2d.dispose();
        return image;
    }
    
    public static void main(String[] args) {
        // Définir le Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
