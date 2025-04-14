# MouvementDAO Class

La classe `MouvementDAO` gère les opérations liées aux mouvements de stock dans une base de données. Elle permet d'enregistrer de nouveaux mouvements (entrées ou sorties) et de récupérer l'historique des mouvements.

## Fonctionnalités
  1. Enregistrement d'un mouvement
La méthode enregistrerMouvement(int produitId, String type, int quantite) permet d'enregistrer un mouvement de stock.
Elle insère un enregistrement dans la table mouvements :

String sql = "INSERT INTO mouvements (produit_id, type_mouvement, quantite) VALUES (?, ?, ?)";

Ensuite, elle met à jour la quantité du produit en fonction du type de mouvement (ENTREE ou SORTIE).

  2. Récupération de l'historique des mouvements
La méthode getHistoriqueMouvements() récupère l'historique des mouvements en effectuant une jointure entre les tables mouvements et produits :

String sql = "SELECT m.id, p.nom, m.type_mouvement, m.quantite, m.date_mouvement " +
              "FROM mouvements m JOIN produits p ON m.produit_id = p.id " +
              "ORDER BY m.date_mouvement DESC";
## Instructions
Assurez-vous que la classe DatabaseConnection est correctement configurée.
Vérifiez que les tables mouvements et produits existent dans votre base de données.

## Code

```java
package utils;

package DAO;

import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MouvementDAO {

    // 🔹 Ajouter un mouvement (ENTREE ou SORTIE) et mettre à jour le stock
    public void enregistrerMouvement(int produitId, String type, int quantite) {
        // Définition de la requête SQL pour insérer un mouvement
        String sql = "INSERT INTO mouvements (produit_id, type_mouvement, quantite) VALUES (?, ?, ?)";

        // Bloc try-with-resources pour gérer la connexion et le PreparedStatement
        try (Connection conn = DatabaseConnection.connect(); // Connexion à la base de données
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Préparation de la requête

            // Définition des paramètres de la requête
            stmt.setInt(1, produitId); // ID du produit
            stmt.setString(2, type); // Type de mouvement : "ENTREE" ou "SORTIE"
            stmt.setInt(3, quantite); // Quantité du mouvement
            stmt.executeUpdate(); // Exécution de l'insertion

            // ✅ Mise à jour du stock dans la table produits
            String updateSql = type.equalsIgnoreCase("ENTREE") ?
                    "UPDATE produits SET quantite = quantite + ? WHERE id = ?" : // Incrémenter la quantité
                    "UPDATE produits SET quantite = quantite - ? WHERE id = ?"; // Décrémenter la quantité

            // Préparation de la requête de mise à jour
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantite); // Définition de la quantité à ajouter ou retirer
                updateStmt.setInt(2, produitId); // ID du produit à mettre à jour
                updateStmt.executeUpdate(); // Exécution de la mise à jour
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Gestion des exceptions SQL
        }
    }

    // 🔹 Récupérer l’historique des mouvements
    public List<String[]> getHistoriqueMouvements() {
        List<String[]> liste = new ArrayList<>(); // Liste pour stocker les résultats

        // Requête SQL pour récupérer l'historique des mouvements
        String sql = "SELECT m.id, p.nom, m.type_mouvement, m.quantite, m.date_mouvement " +
                "FROM mouvements m JOIN produits p ON m.produit_id = p.id " +
                "ORDER BY m.date_mouvement DESC";

        // Bloc try-with-resources pour gérer la connexion, le Statement et le ResultSet
        try (Connection conn = DatabaseConnection.connect(); // Connexion à la base de données
             Statement stmt = conn.createStatement(); // Création d'un Statement
             ResultSet rs = stmt.executeQuery(sql)) { // Exécution de la requête

            // Parcours des résultats de la requête
            while (rs.next()) {
                liste.add(new String[]{
                        String.valueOf(rs.getInt("id")), // ID du mouvement
                        rs.getString("nom"), // Nom du produit
                        rs.getString("type_mouvement"), // Type de mouvement
                        String.valueOf(rs.getInt("quantite")), // Quantité du mouvement
                        rs.getString("date_mouvement") // Date du mouvement
                });
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Gestion des exceptions SQL
        }

        return liste; // Retourne la liste des mouvements
    }
}
