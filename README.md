# Classe AjouterProduitSwing

package ui;

import DAO.ProduitDAO; // Import de la classe DAO pour accéder à la base de données

import javax.swing.*; // Import des composants Swing pour l'interface graphique

public class AjouterProduitSwing extends JFrame { // La classe hérite de JFrame (fenêtre)
    public AjouterProduitSwing() { // Constructeur de la fenêtre
        setTitle("Ajouter un produit"); // Titre affiché en haut de la fenêtre
        setSize(300, 250); // Taille de la fenêtre (largeur, hauteur)
        setLayout(null); // Placement manuel des éléments (pas de layout manager)

        // Déclaration des composants : étiquettes et champs de saisie.
        // Ces composants sont tous des objets Java Swing utilisés pour créer l’interface graphique
        JLabel nomLabel = new JLabel("Nom :"); // Étiquette pour le nom
        JTextField nomField = new JTextField(); // Champ de saisie du nom

        JLabel qteLabel = new JLabel("Quantité :"); // Étiquette pour la quantité
        JTextField qteField = new JTextField(); // Champ de saisie de la quantité

        JLabel prixLabel = new JLabel("Prix :"); // Étiquette pour le prix
        JTextField prixField = new JTextField(); // Champ de saisie du prix

        JButton ajouterBtn = new JButton("Ajouter"); // Bouton pour valider l'ajout

        // Positionnement des composants dans la fenêtre
        nomLabel.setBounds(20, 20, 80, 25);
        nomField.setBounds(100, 20, 150, 25);

        qteLabel.setBounds(20, 60, 80, 25);
        qteField.setBounds(100, 60, 150, 25);

        prixLabel.setBounds(20, 100, 80, 25);
        prixField.setBounds(100, 100, 150, 25);

        ajouterBtn.setBounds(90, 150, 100, 30);

        // Ajout des composants à la fenêtre
        add(nomLabel); add(nomField);
        add(qteLabel); add(qteField);
        add(prixLabel); add(prixField);
        add(ajouterBtn);

        // Action déclenchée lorsqu'on clique sur le bouton "Ajouter"
        ajouterBtn.addActionListener(e -> {   // ActionListener: C’est une interface de Java (du package java.awt.event) qui sert à détecter les actions de l'utilisateur,
            // e: est une variable qui représente l’événement (ici le clic)/C’est ce qu’on appelle une lambda expression (forme raccourcie de code).
            String nom = nomField.getText().trim(); // Récupère le texte du champ nom
            String qteStr = qteField.getText().trim(); // Récupère le texte du champ quantité
            String prixStr = prixField.getText().trim(); // Récupère le texte du champ prix

            // Vérifie si un champ est vide
            if (nom.isEmpty() || qteStr.isEmpty() || prixStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.");
                return; // Arrête l'exécution si un champ est vide
            }

            try {
                int qte = Integer.parseInt(qteStr); // Convertit la quantité en entier
                double prix = Double.parseDouble(prixStr); // Convertit le prix en nombre décimal

                // Vérifie que les valeurs sont positives
                if (qte < 0 || prix < 0) {
                    JOptionPane.showMessageDialog(this, "Quantité et prix doivent être positifs.");
                    return; // Arrête si l’un des deux est négatif
                }

                // Ajoute le produit en base de données via ProduitDAO
                new ProduitDAO().ajouterProduit(nom, qte, prix);

                // Message de confirmation
                JOptionPane.showMessageDialog(this, "Produit ajouté !");
                dispose(); // Ferme la fenêtre après l’ajout

            } catch (NumberFormatException ex) {
                // En cas de saisie non valide (ex : texte au lieu de nombre)
                JOptionPane.showMessageDialog(this, "Quantité et prix doivent être des nombres valides.");
            }
        });

        setVisible(true); // Rend la fenêtre visible
    }
}

# Explication de la classe
La classe AjouterProduitSwing permet à l’utilisateur d’ajouter un nouveau produit dans la base de données via une interface graphique. Elle affiche une fenêtre avec trois champs de saisie : Nom, Quantité, et Prix, ainsi qu’un bouton Ajouter.
Lorsque l’utilisateur clique sur ce bouton :
* Le programme vérifie que tous les champs sont remplis.
* Il convertit les saisies en types numériques (int pour la quantité et double pour le prix).
* Si les données sont valides (non négatives), elles sont transmises à la méthode ajouterProduit du ProduitDAO pour les enregistrer en base.
* Un message de confirmation s’affiche, et la fenêtre se ferme.
L’interface est construite avec Java Swing, en positionnant manuellement les composants avec setBounds(...)


