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
