#!/bin/bash

echo "============================================"
echo "ITMPRO - Système de Suivi d'Irrigation"
echo "Script d'installation et de configuration"
echo "============================================"

# Vérifier Java
echo "Vérification de Java..."
if ! command -v java &> /dev/null; then
    echo "Java n'est pas installé. Veuillez installer Java 11 ou supérieur."
    exit 1
fi

java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "Java version: $java_version"

# Créer le dossier lib s'il n'existe pas
mkdir -p lib

echo ""
echo "============================================"
echo "Instructions pour les bibliothèques:"
echo "1. Téléchargez mysql-connector-java.jar depuis:"
echo "   https://dev.mysql.com/downloads/connector/j/"
echo "2. Téléchargez les bibliothèques PDF/Excel:"
echo "   - itextpdf-5.5.13.3.jar"
echo "   - poi-5.2.3.jar"
echo "   - poi-ooxml-5.2.3.jar"
echo "3. Placez tous les fichiers .jar dans le dossier lib/"
echo "============================================"

# Créer la base de données MySQL
echo ""
echo "Configuration de la base de données MySQL..."
echo "Assurez-vous que MySQL est en cours d'exécution"
echo "et que vous avez les droits d'administration."

# Vérifier MySQL
if ! command -v mysql &> /dev/null; then
    echo "MySQL n'est pas installé ou n'est pas dans le PATH."
    echo "Veuillez installer MySQL et l'ajouter au PATH."
else
    echo "MySQL est disponible."
fi

echo ""
echo "============================================"
echo "Pour configurer la base de données:"
echo "1. Connectez-vous à MySQL: mysql -u root -p"
echo "2. Créez la base de données: CREATE DATABASE irrigation_db;"
echo "3. Configurez les paramètres dans: src/main/resources/database/database.properties"
echo "============================================"

# Compiler le projet
echo ""
echo "Compilation du projet..."
if command -v ant &> /dev/null; then
    echo "Apache Ant est disponible."
    echo "Vous pouvez compiler avec: ant clean build"
else
    echo "Apache Ant n'est pas installé."
    echo "Utilisez NetBeans ou installez Apache Ant."
fi

echo ""
echo "============================================"
echo "Installation terminée!"
echo ""
echo "Pour lancer l'application:"
echo "1. Avec NetBeans: Ouvrez le projet et cliquez sur Run"
echo "2. Avec la ligne de commande: ant run"
echo ""
echo "Identifiants par défaut:"
echo "Email: admin"
echo "Mot de passe: admin123"
echo "============================================"
