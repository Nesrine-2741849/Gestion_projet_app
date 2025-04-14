package ui;

import DAO.ProduitDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

// Classe Swing qui affiche les statistiques globales sur les produits en stock.
// Statistiques affichées : total de produits, quantité totale, prix moyen, valeur totale du stock.
public class FenetreStatistiques extends JFrame {

    // Constructeur qui initialise la fenêtre et y affiche les statistiques
    public FenetreStatistiques() {
        setTitle("Statistiques du Stock"); // Titre de la fenêtre
        setSize(400, 300); // Taille de la fenêtre
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        setLayout(new GridLayout(5, 1)); // Utiliser un GridLayout : 5 lignes, 1 colonne (4 stats + 1 bouton)

        // Récupérer les statistiques depuis la base via la classe ProduitDAO
        // La méthode retourne une Map (paires clé-valeur) avec des clés comme "total_produits", "valeur_stock", etc.
        Map<String, Double> stats = new ProduitDAO().calculerStatistiques();

        // Création des étiquettes (JLabel) pour afficher chaque statistique
        JLabel totalProduits = new JLabel("Total des produits : " + stats.get("total_produits").intValue());
        JLabel totalQuantite = new JLabel("Quantité totale : " + stats.get("total_quantite").intValue());
        JLabel moyennePrix = new JLabel("Prix moyen : " + String.format("%.2f", stats.get("moyenne_prix")) + " $");
        JLabel valeurStock = new JLabel("Valeur totale du stock : " + String.format("%.2f", stats.get("valeur_stock")) + " $");

        // Centrer horizontalement le texte de chaque label
        totalProduits.setHorizontalAlignment(SwingConstants.CENTER);
        totalQuantite.setHorizontalAlignment(SwingConstants.CENTER);
        moyennePrix.setHorizontalAlignment(SwingConstants.CENTER);
        valeurStock.setHorizontalAlignment(SwingConstants.CENTER);

        // Ajouter les labels à la fenêtre (1 par ligne dans le GridLayout)
        add(totalProduits);
        add(totalQuantite);
        add(moyennePrix);
        add(valeurStock);

        // Bouton pour fermer la fenêtre
        JButton fermerBtn = new JButton("Fermer");
        fermerBtn.addActionListener(e -> dispose());
        add(fermerBtn);

        // Rendre la fenêtre visible
        setVisible(true);
    }
}
