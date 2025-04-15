# MouvementDAO

La classe `MouvementDAO` gère les opérations liées aux mouvements de stock dans une base de données. Elle permet d'enregistrer de nouveaux mouvements (entrées ou sorties) et de récupérer l'historique des mouvements.

## Fonctionnalités
  **1. Enregistrement d'un mouvement**
La méthode enregistrerMouvement(int produitId, String type, int quantite) permet d'enregistrer un mouvement de stock.
Elle insère un enregistrement dans la table mouvements :

String sql = "INSERT INTO mouvements (produit_id, type_mouvement, quantite) VALUES (?, ?, ?)";

Ensuite, elle met à jour la quantité du produit en fonction du type de mouvement (ENTREE ou SORTIE).

  **2. Récupération de l'historique des mouvements**
La méthode getHistoriqueMouvements() récupère l'historique des mouvements en effectuant une jointure entre les tables mouvements et produits :

String sql = "SELECT m.id, p.nom, m.type_mouvement, m.quantite, m.date_mouvement " +
              "FROM mouvements m JOIN produits p ON m.produit_id = p.id " +
              "ORDER BY m.date_mouvement DESC";
## Instructions
Assurez-vous que la classe DatabaseConnection est correctement configurée et érifiez que les tables mouvements et produits existent dans votre base de données.

## Code

```
package DAO;

import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouvementDAO {

    // 🔹 Enregistre un mouvement (ENTREE ou SORTIE) et met à jour le stock du produit
    public void enregistrerMouvement(int produitId, String type, int quantite) {
        // SQL pour insérer un nouveau mouvement
        String sql = "INSERT INTO mouvements (produit_id, type_mouvement, quantite) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Définition des paramètres pour l'insertion
            stmt.setInt(1, produitId);
            stmt.setString(2, type); // Type de mouvement : "ENTREE" ou "SORTIE"
            stmt.setInt(3, quantite);
            stmt.executeUpdate(); // Exécution de l'insertion

            // ✅ Mise à jour du stock dans la table produits
            String updateSql = type.equalsIgnoreCase("ENTREE") ?
                    "UPDATE produits SET quantite = quantite + ? WHERE id = ?" :
                    "UPDATE produits SET quantite = quantite - ? WHERE id = ?";

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                // Définition des paramètres pour la mise à jour du stock
                updateStmt.setInt(1, quantite);
                updateStmt.setInt(2, produitId);
                updateStmt.executeUpdate(); // Exécution de la mise à jour
            }

        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }
    }

    // 🔹 Récupère l’historique des mouvements enregistrés
    public List<String[]> getHistoriqueMouvements() {
        List<String[]> liste = new ArrayList<>(); // Liste pour stocker l'historique
        // SQL pour récupérer les mouvements avec les noms des produits
        String sql = "SELECT m.id, p.nom, m.type_mouvement, m.quantite, m.date_mouvement " +
                     "FROM mouvements m JOIN produits p ON m.produit_id = p.id " +
                     "ORDER BY m.date_mouvement DESC";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Parcours des résultats et ajout à la liste
            while (rs.next()) {
                liste.add(new String[]{
                        String.valueOf(rs.getInt("id")), // ID du mouvement
                        rs.getString("nom"), // Nom du produit
                        rs.getString("type_mouvement"), // Type de mouvement
                        String.valueOf(rs.getInt("quantite")), // Quantité
                        rs.getString("date_mouvement") // Date du mouvement
                });
            }

        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }

        return liste; // Retourne l'historique des mouvements
    }
}
```
# DatabaseConnection

La classe `DatabaseConnection` permet d'établir une connexion à une base de données MySQL.

## Fonctionnalités : 
Établissement de la connexion : La méthode connect() initialise la connexion à la base de données en utilisant l'URL, le nom d'utilisateur et le mot de passe fournis.
Gestion des erreurs : En cas d'échec de la connexion, un message d'erreur est affiché.

## Instructions : 
Remplacez le mot de passe par votre mot de passe réel avant d'exécuter le code et assurez-vous que le serveur MySQL est en cours d'exécution et accessible.

## Code

```java
package utils;

import java.sql.*;

public class DatabaseConnection {

    // 🔹 Établit une connexion à la base de données MySQL
    public static Connection connect() {
        try {
            // URL de connexion à la base de données
            String url = "jdbc:mysql://localhost:3306/stockdb";
            String user = "root"; // Nom d'utilisateur pour la connexion
            String password = "Azerazer7"; // Remplace par ton mot de passe

            // Retourne une connexion à la base de données
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Gestion des erreurs de connexion
            System.out.println("Erreur de connexion : " + e.getMessage());
            return null; // Retourne null en cas d'erreur
        }
    }
}
```
# ProduitDAO

La classe `ProduitDAO` gère les opérations liées aux produits dans une base de données. Elle permet d'ajouter, modifier, supprimer des produits et de récupérer la liste des produits.

## Fonctionnalités

1. **Ajouter un produit**
   La méthode `ajouterProduit(String nom, int quantite, double prix)` permet d'ajouter un produit à la base de données. 
   Elle insère un enregistrement dans la table `produits` :
   String sql = "INSERT INTO produits (nom, quantite, prix) VALUES (?, ?, ?)";

2. **Récupérer tous les produits**
La méthode getTousLesProduits() récupère tous les produits de la base de données :
  String sql = "SELECT * FROM produits";

3. **Modifier un produit existant**
La méthode modifierProduit(int id, String nom, int quantite, double prix) permet de modifier les informations d'un produit existant :
  String sql = "UPDATE produits SET nom=?, quantite=?, prix=? WHERE id=?";

4. **Supprimer un produit**
La méthode supprimerProduit(int id) supprime un produit de la base de données :
  String sql = "DELETE FROM produits WHERE id=?";

5. **Calculer des statistiques simples**
La méthode calculerStatistiques() calcule des statistiques simples sur les produits (total, moyenne, valeur du stock) :
  String sql = "SELECT COUNT(*) AS total_produits, SUM(quantite) AS total_quantite, " +
             "AVG(prix) AS moyenne_prix, SUM(quantite * prix) AS valeur_stock FROM produits";

## Instructions
Assurez-vous que la classe DatabaseConnection est correctement configurée et vérifiez que la table produits existe dans votre base de données.

# Code
```java
package DAO;

import utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ProduitDAO {

    // 🔹 Ajouter un nouveau produit dans la base de données
    public void ajouterProduit(String nom, int quantite, double prix) {
        // SQL pour insérer un nouveau produit
        String sql = "INSERT INTO produits (nom, quantite, prix) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Définition des paramètres pour l'insertion
            stmt.setString(1, nom);
            stmt.setInt(2, quantite);
            stmt.setDouble(3, prix);
            stmt.executeUpdate(); // Exécution de l'insertion
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }
    }

    // 🔹 Récupérer tous les produits de la base de données
    public List<String[]> getTousLesProduits() {
        List<String[]> liste = new ArrayList<>(); // Liste pour stocker les produits
        // SQL pour sélectionner tous les produits
        String sql = "SELECT * FROM produits";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Parcours des résultats et ajout à la liste
            while (rs.next()) {
                liste.add(new String[]{
                        String.valueOf(rs.getInt("id")), // ID du produit
                        rs.getString("nom"), // Nom du produit
                        String.valueOf(rs.getInt("quantite")), // Quantité du produit
                        String.valueOf(rs.getDouble("prix")) // Prix du produit
                });
            }
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }
        return liste; // Retourne la liste des produits
    }

    // 🔹 Modifier les détails d'un produit existant
    public void modifierProduit(int id, String nom, int quantite, double prix) {
        // SQL pour mettre à jour un produit
        String sql = "UPDATE produits SET nom=?, quantite=?, prix=? WHERE id=?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Définition des paramètres pour la mise à jour
            stmt.setString(1, nom);
            stmt.setInt(2, quantite);
            stmt.setDouble(3, prix);
            stmt.setInt(4, id);
            stmt.executeUpdate(); // Exécution de la mise à jour
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }
    }

    // 🔹 Supprimer un produit de la base de données
    public void supprimerProduit(int id) {
        // SQL pour supprimer un produit
        String sql = "DELETE FROM produits WHERE id=?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Définition des paramètres pour la suppression
            stmt.setInt(1, id);
            stmt.executeUpdate(); // Exécution de la suppression
        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }
    }

    // ✅ Nouvelle méthode : calcul des statistiques simples sur les produits
    public Map<String, Double> calculerStatistiques() {
        Map<String, Double> stats = new HashMap<>(); // Map pour stocker les statistiques
        // SQL pour calculer les statistiques des produits
        String sql = "SELECT COUNT(*) AS total_produits, SUM(quantite) AS total_quantite, " +
                     "AVG(prix) AS moyenne_prix, SUM(quantite * prix) AS valeur_stock FROM produits";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                // Ajout des statistiques à la map
                stats.put("total_produits", rs.getDouble("total_produits"));
                stats.put("total_quantite", rs.getDouble("total_quantite"));
                stats.put("moyenne_prix", rs.getDouble("moyenne_prix"));
                stats.put("valeur_stock", rs.getDouble("valeur_stock"));
            }

        } catch (SQLException e) {
            // Gestion des erreurs SQL
            e.printStackTrace();
        }

        return stats; // Retourne les statistiques calculées
    }
}
