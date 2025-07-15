# ITMPRO - Système de Suivi d'Irrigation

Application de bureau Java complète pour la gestion des systèmes d'irrigation, développée avec NetBeans.

## Fonctionnalités

### Gestion des Clients
- Ajout, modification et suppression de clients
- Recherche avancée
- Historique des interactions

### Gestion des Fournisseurs
- Gestion complète des fournisseurs
- Suivi des services et produits

### Gestion des Équipes de Travail
- Création et gestion des équipes
- Attribution des tâches
- Suivi des disponibilités

### Gestion des Devis
- Création de devis personnalisés
- Génération automatique de numéros
- Suivi des statuts (Brouillon, Envoyé, Accepté, Refusé, Expiré)

### Gestion des Factures
- Création de factures à partir des devis
- Suivi des paiements
- Gestion des retards
- Export en PDF/Excel

### Rapports
- Export vers PDF et Excel
- Statistiques détaillées
- Tableau de bord interactif

## Prérequis

- Java 11 ou supérieur
- MySQL Server
- NetBeans IDE (recommandé)

## Installation

### 1. Configuration de la base de données

1. Installez MySQL Server
2. Créez une base de données nommée `irrigation_db`
3. Configurez les paramètres de connexion dans `src/main/resources/database/database.properties`

### 2. Installation des dépendances

Placez les fichiers JAR suivants dans le dossier `lib/` :
- mysql-connector-java.jar
- itext-pdf.jar (pour l'export PDF)
- poi-ooxml.jar (pour l'export Excel)
- poi.jar

### 3. Compilation et exécution

#### Avec NetBeans
1. Ouvrez le projet dans NetBeans
2. Cliquez sur "Run Project" (F6)

#### Avec la ligne de commande
```bash
cd irrigation-tracking-system
ant run
```

## Configuration initiale

### Utilisateur par défaut
- **Email/Login**: admin
- **Mot de passe**: admin123

### Base de données
L'application crée automatiquement toutes les tables nécessaires au premier lancement.

## Structure du projet

```
irrigation-tracking-system/
├── src/main/java/com/irrigation/
│   ├── models/          # Classes de modèles
│   ├── views/           # Interfaces utilisateur
│   ├── controllers/     # Logique métier
│   ├── dao/            # Accès aux données
│   ├── services/       # Services métier
│   ├── utils/          # Utilitaires
│   └── reports/        # Génération de rapports
├── lib/                # Bibliothèques externes
├── resources/          # Fichiers de configuration
└── nbproject/          # Configuration NetBeans
```

## Utilisation

### Connexion
1. Lancez l'application
2. Utilisez les identifiants par défaut ou créez un nouvel utilisateur
3. Accédez aux différentes sections via les onglets

### Navigation
- **Tableau de bord**: Vue d'ensemble des statistiques
- **Clients**: Gestion des clients
- **Fournisseurs**: Gestion des fournisseurs
- **Équipes**: Gestion des équipes de travail
- **Devis**: Création et gestion des devis
- **Factures**: Gestion des factures
- **Rapports**: Export des données

## Sécurité

- Mots de passe hachés avec SHA-256
- Gestion des rôles (Admin, Manager, Employé)
- Sessions utilisateur sécurisées

## Support

Pour toute question ou problème, contactez l'administrateur système.

## Licence

© 2024 ITMPRO - Tous droits réservés
