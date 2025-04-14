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
