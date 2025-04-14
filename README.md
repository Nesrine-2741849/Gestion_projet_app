# MouvementDAO Class

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

```java
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
