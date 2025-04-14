# Classe ModifierOuSupprimerProduit

package ui;

import DAO.ProduitDAO;
import javax.swing.*;
import java.util.List;

// Classe représentant une fenêtre permettant de modifier ou supprimer un produit
public class ModifierOuSupprimerProduit extends JFrame {

    // Constructeur 1 : mode manuel (avec bouton "Charger")
    public ModifierOuSupprimerProduit(String mode) {
        //Titre dynamique selon le mode
        setTitle((mode.equals("modifier") ? "Modifier" : "Supprimer") + " un produit");
        setSize(350, 300);  // Taille de la fenêtre
        setLayout(null); //Pas de layout automatique : positionnement manuel avec setBounds()

        // Déclaration des champs et boutons
        JLabel idLabel = new JLabel("ID :");
        JTextField idField = new JTextField();
        JButton chargerBtn = new JButton("Charger");

        JLabel nomLabel = new JLabel("Nom :");
        JTextField nomField = new JTextField();
        JLabel qteLabel = new JLabel("Quantité :");
        JTextField qteField = new JTextField();
        JLabel prixLabel = new JLabel("Prix :");
        JTextField prixField = new JTextField();

        // crée un bouton selon le mode choisi modifier ou supprimer.
        JButton actionBtn = new JButton(mode.equals("modifier") ? "Modifier" : "Supprimer");

        // Positionnement des composants
        idLabel.setBounds(20, 20, 80, 25); idField.setBounds(100, 20, 150, 25);
        chargerBtn.setBounds(100, 50, 150, 25);

        nomLabel.setBounds(20, 90, 80, 25); nomField.setBounds(100, 90, 150, 25);
        qteLabel.setBounds(20, 130, 80, 25); qteField.setBounds(100, 130, 150, 25);
        prixLabel.setBounds(20, 170, 80, 25); prixField.setBounds(100, 170, 150, 25);
        actionBtn.setBounds(100, 220, 120, 30);

        // Ajout des composants à la fenêtre
        add(idLabel); add(idField); add(chargerBtn);
        add(nomLabel); add(nomField);
        add(qteLabel); add(qteField);
        add(prixLabel); add(prixField);
        add(actionBtn);

        // Champs et bouton désactivés au départ
        nomField.setEnabled(false);
        qteField.setEnabled(false);
        prixField.setEnabled(false);
        actionBtn.setEnabled(false);

        // Action du bouton "Charger" : récupérer les données du produit selon l'ID
        chargerBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                ProduitDAO dao = new ProduitDAO();
                List<String[]> produits = dao.getTousLesProduits();

                boolean trouve = false;
                for (String[] p : produits) {
                    if (Integer.parseInt(p[0]) == id) {
                        // Remplir les champs avec les données trouvées
                        nomField.setText(p[1]);
                        qteField.setText(p[2]);
                        prixField.setText(p[3]);

                        // Si on modifie, activer les champs
                        if (mode.equals("modifier")) {
                            nomField.setEnabled(true);
                            qteField.setEnabled(true);
                            prixField.setEnabled(true);
                        }

                        actionBtn.setEnabled(true); // Activer le bouton principal
                        trouve = true;
                        break;
                    }
                }

                if (!trouve) {
                    JOptionPane.showMessageDialog(this, "Produit introuvable.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID invalide.");
            }
        });

        // Action du bouton principal (Modifier ou Supprimer)
        actionBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());

                // Si mode suppression
                if (mode.equals("supprimer")) {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Voulez-vous vraiment supprimer ce produit ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        new ProduitDAO().supprimerProduit(id);
                        JOptionPane.showMessageDialog(this, "Produit supprimé !");
                        dispose();
                    }
                    return;
                }

                // Si mode modification récupérer les nouvelles valeurs
                String nom = nomField.getText().trim();
                int qte = Integer.parseInt(qteField.getText().trim());
                double prix = Double.parseDouble(prixField.getText().trim());

                // Vérification des champs
                if (nom.isEmpty() || qte < 0 || prix < 0) {
                    JOptionPane.showMessageDialog(this, "Vérifiez les champs.");
                    return;
                }

                new ProduitDAO().modifierProduit(id, nom, qte, prix);
                JOptionPane.showMessageDialog(this, "Produit modifié !");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Champs invalides.");
            }
        });

        setVisible(true); // Afficher la fenêtre
    }

    // Constructeur 2 : mode automatique depuis la liste
    public ModifierOuSupprimerProduit(int id, String nom, int quantite, double prix) {
        //Titre dynamique selon le mode
        setTitle("Modifier ou Supprimer un produit");
        setSize(350, 300);
        setLayout(null);

        // Définition des composants
        JLabel nomLabel = new JLabel("Nom :");
        JTextField nomField = new JTextField(nom);
        JLabel qteLabel = new JLabel("Quantité :");
        JTextField qteField = new JTextField(String.valueOf(quantite));
        JLabel prixLabel = new JLabel("Prix :");
        JTextField prixField = new JTextField(String.valueOf(prix));

        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        // Positionnement
        nomLabel.setBounds(20, 30, 80, 25); nomField.setBounds(100, 30, 150, 25);
        qteLabel.setBounds(20, 70, 80, 25); qteField.setBounds(100, 70, 150, 25);
        prixLabel.setBounds(20, 110, 80, 25); prixField.setBounds(100, 110, 150, 25);
        btnModifier.setBounds(40, 170, 100, 30);
        btnSupprimer.setBounds(160, 170, 120, 30);

        // Ajout des composants
        add(nomLabel); add(nomField);
        add(qteLabel); add(qteField);
        add(prixLabel); add(prixField);
        add(btnModifier); add(btnSupprimer);

        // Action du bouton Modifier
        btnModifier.addActionListener(e -> {
            try {
                String newNom = nomField.getText().trim();
                int newQte = Integer.parseInt(qteField.getText().trim());
                double newPrix = Double.parseDouble(prixField.getText().trim());

                if (newNom.isEmpty() || newQte < 0 || newPrix < 0) {
                    JOptionPane.showMessageDialog(this, "Vérifiez les champs.");
                    return;
                }

                new ProduitDAO().modifierProduit(id, newNom, newQte, newPrix);
                JOptionPane.showMessageDialog(this, "Produit modifié !");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Champs invalides.");
            }
        });

        // Action du bouton Supprimer
        btnSupprimer.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous vraiment supprimer ce produit ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new ProduitDAO().supprimerProduit(id);
                JOptionPane.showMessageDialog(this, "Produit supprimé !");
                dispose();
            }
        });

        setVisible(true);
    }
}

# Explication de la classe ModifierOuSupprimerProduit
Fenêtre Swing permettant de **modifier ou supprimer un produit**.

## Deux constructeurs :
- `ModifierOuSupprimerProduit(String mode)` : Mode manuel (saisie de l'ID + bouton "Charger")
- `ModifierOuSupprimerProduit(int id, String nom, int qte, double prix)` : Appelé depuis la table avec sélection automatique

## Fonctionnalités
- Recherche produit par ID (manuel)
- Affichage automatique via sélection
- Modification de nom, quantité, prix
- Suppression avec confirmation
- Validation des champs (nombre > 0, nom non vide)

## Technologies
- `JTextField`, `JButton`, `JOptionPane`
- Utilisation de `ProduitDAO` pour interaction avec la base

# Classe MouvementStockSwing

package ui;

import DAO.MouvementDAO;
import DAO.ProduitDAO;

import javax.swing.*;
import java.util.List;

public class MouvementStockSwing extends JFrame {
    private JComboBox<String> produitCombo; // Liste déroulante des produits (affiche "id - nom")
    private JTextField qteField; // Champ de saisie pour la quantité

    public MouvementStockSwing() {
        setTitle("Entrée / Sortie de Stock"); // Titre de la fenêtre
        setSize(420, 250); // Taille de la fenêtre
        setLayout(null); // Positionnement manuel
        setLocationRelativeTo(null); // Centrer la fenêtre

        initUI(); // Crée et positionne les composants graphiques
        chargerProduits(); // Remplit la comboBox avec les produits
        setVisible(true); // Rend la fenêtre visible
    }
    //Méthode qui nitialise les composants de l'interface utilisateur
    private void initUI() {
        JLabel produitLabel = new JLabel("Produit :");
        produitCombo = new JComboBox<>(); // liste déroulante
        JLabel qteLabel = new JLabel("Quantité :");
        qteField = new JTextField(); // champ de saisie texte

        JButton entreeBtn = new JButton("Entrée");
        JButton sortieBtn = new JButton("Sortie");

        // Positionnement des composants
        produitLabel.setBounds(30, 30, 100, 25);
        produitCombo.setBounds(130, 30, 220, 25);
        qteLabel.setBounds(30, 70, 100, 25);
        qteField.setBounds(130, 70, 220, 25);
        entreeBtn.setBounds(90, 130, 100, 30);
        sortieBtn.setBounds(210, 130, 100, 30);

        // Ajout des composants graphiques à la fenêtre
        add(produitLabel); add(produitCombo);
        add(qteLabel); add(qteField);
        add(entreeBtn); add(sortieBtn);

        // Attacher un comportement aux boutons "Entrée" et "Sortie"
        entreeBtn.addActionListener(e -> enregistrerMouvement("ENTREE"));
        sortieBtn.addActionListener(e -> enregistrerMouvement("SORTIE"));
    }

    // Charge tous les produits disponibles dans la base de données et les ajoute à la comboBox.
    private void chargerProduits() {
        List<String[]> produits = new ProduitDAO().getTousLesProduits();
        // Supprimer tous les éléments qui se trouvaient dans la JComboBox
        produitCombo.removeAllItems();

        if (produits.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun produit disponible.");
            dispose();
            return;
        }

        // Ajouter chaque produit sous forme "id - nom"
        for (String[] p : produits) {
            produitCombo.addItem(p[0] + " - " + p[1]); // "id - nom"
        }
    }

    // Enregistre un mouvement de stock (entrée ou sortie)
    private void enregistrerMouvement(String type) {
        try {
            // Vérifier la sélection et la quantité
            String selection = (String) produitCombo.getSelectedItem();
            if (selection == null || qteField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit et une quantité.");
                return;
            }

            // Récupérer l'ID du produit et la quantité saisie
            int produitId = Integer.parseInt(selection.split(" - ")[0]);
            int quantite = Integer.parseInt(qteField.getText().trim());

            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, "Quantité invalide.");
                return;
            }

            // Si c'est une sortie, vérifier le stock disponible
            if (type.equals("SORTIE")) {
                List<String[]> produits = new ProduitDAO().getTousLesProduits();
                for (String[] p : produits) {
                    if (Integer.parseInt(p[0]) == produitId) {
                        int stockActuel = Integer.parseInt(p[2]); // Quantité actuelle en stock
                        if (quantite > stockActuel) {
                            // Afficher une erreur si quantité demandée > stock
                            JOptionPane.showMessageDialog(this,
                                    "Stock insuffisant. Quantité disponible : " + stockActuel,
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
            // Enregistrement du mouvement dans la base
            new MouvementDAO().enregistrerMouvement(produitId, type, quantite);
            JOptionPane.showMessageDialog(this, "Mouvement enregistré !");
            dispose(); // Fermer la fenêtre

        } catch (NumberFormatException ex) {
            // Saisie non numérique pour la quantité
            JOptionPane.showMessageDialog(this, "Quantité invalide : doit être un nombre.");
        } catch (Exception ex) {
            // Toute autre erreur
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}

# Explication de la classe MouvementStockSwing

Interface graphique Swing pour enregistrer les **entrées et sorties de stock**.

## Fonctionnalités
- Liste déroulante des produits (`JComboBox`)
- Champ de quantité (`JTextField`)
- Boutons "Entrée" et "Sortie"
- Mise à jour du stock automatique dans la base
- Vérification du stock disponible en cas de sortie

## Comportement
- En cas de stock insuffisant → erreur bloquante
- Enregistrement dans la base via `MouvementDAO.enregistrerMouvement`

## Technologies
- Swing (`JLabel`, `JTextField`, `JComboBox`, `JButton`)
- `ProduitDAO`, `MouvementDAO`

# Classe FenetreStatistiques

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

# Explication de la classe FenetreStatistiques
Cette classe affiche les **statistiques globales** des produits en stock dans une interface Swing.

## Fonctionnalité
- Récupération des données via `ProduitDAO.calculerStatistiques()`
- Affiche :
  - Total des produits
  - Quantité totale
  - Prix moyen
  - Valeur totale du stock
- Fermeture de la fenêtre avec un bouton

## Technologies utilisées
- `GridLayout` (5 lignes × 1 colonne)
- `JLabel` pour afficher les valeurs
- `Map<String, Double>` pour la récupération des statistiques

