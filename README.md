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
La classe AjouterProduitSwing permet à l’utilisateur d’ajouter un nouveau produit dans la base de données via une interface graphique.
Elle affiche une fenêtre avec trois champs de saisie : Nom, Quantité, et Prix, ainsi qu’un bouton Ajouter.
Lorsque l’utilisateur clique sur ce bouton (ajouterBtn.addActionListener(...)) :
* Le programme vérifie que tous les champs sont remplis (isEmpty() sur les champs texte).
* Il convertit les saisies en types numériques :
     *Integer.parseInt(...) pour la quantité,
     * Double.parseDouble(...) pour le prix.
* Si les données sont valides (non négatives), elles sont transmises à la méthode ajouterProduit(...) du ProduitDAO pour les enregistrer dans la base.
* Un message de confirmation s’affiche (JOptionPane.showMessageDialog(...)) et la fenêtre se ferme (dispose()).
L’interface est construite avec Java Swing, en positionnant manuellement les composants avec la méthode setBounds(...),
et sans gestionnaire de mise en page grâce à setLayout(null).

# Classe AjouterProduitSwing

package ui;

import DAO.ProduitDAO; // Import de la classe d'accès aux données des produits
import javax.swing.*; // Import des composants Swing
import javax.swing.table.DefaultTableModel; // Pour gérer les lignes/colonnes de la JTable
import java.util.List;
import java.util.stream.Collectors; // Pour filtrer la liste avec des expressions lambda

public class AfficherProduitsSwing extends JFrame {
//  déclaration des variables de classe nécessaires à l’affichage, la recherche, et la gestion dynamique du tableau.
    DefaultTableModel model; // Modèle de données pour le tableau
    JTextField searchField; // Attribut objet graphique (Swing) /Champ de texte
    JTable table; // Tableau d'affichage
    List<String[]> tousLesProduits; //Attribut de type liste/ Liste complète des produits (non filtrée)
// Constructeur : initialise les composants de la fenêtre
    public AfficherProduitsSwing() {
        setTitle("Liste des produits"); // Titre de la fenêtre
        setSize(550, 350); // Taille de la fenêtre
        setLayout(null); // Positionnement manuel des composants

        JLabel searchLabel = new JLabel("Rechercher (ID ou Nom) :"); // Étiquette du champ de recherche
        searchLabel.setBounds(20, 10, 200, 25); // Position et taille
        add(searchLabel); // Ajout à la fenêtre

        searchField = new JTextField(); // Champ de saisie pour la recherche
        searchField.setBounds(200, 10, 200, 25); // Position
        add(searchField); // Ajout

        String[] colonnes = {"ID", "Nom", "Quantité", "Prix"}; // En-têtes du tableau
        model = new DefaultTableModel(colonnes, 0); // Création du modèle vide
        table = new JTable(model); // Création de la table avec le modèle
        JScrollPane scroll = new JScrollPane(table); // Ajout du scroll si beaucoup de lignes
        scroll.setBounds(20, 50, 500, 200); // Position du tableau
        add(scroll); // Ajout à la fenêtre

        // Récupération des produits depuis la base de données
        tousLesProduits = new ProduitDAO().getTousLesProduits();
        afficherProduits(tousLesProduits); // Affichage initial

        // Mise à jour automatique en fonction de la recherche
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        // Action lorsqu'on clique sur une ligne du tableau
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow(); // Ligne sélectionnée
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                String nom = table.getValueAt(row, 1).toString();
                int quantite = Integer.parseInt(table.getValueAt(row, 2).toString());
                double prix = Double.parseDouble(table.getValueAt(row, 3).toString());

                // Ouvre la fenêtre de modification/suppression avec les valeurs
                new ModifierOuSupprimerProduit(id, nom, quantite, prix);
            }
        });
        setVisible(true); // Affiche la fenêtre
    }
    // Méthode pour afficher tous les produits dans le tableau
    private void afficherProduits(List<String[]> produits) {
        model.setRowCount(0); // Vide le tableau
        for (String[] ligne : produits) {
            model.addRow(ligne); // Ajoute chaque ligne
        }
    }

    // Méthode pour filtrer les produits selon ce que l’utilisateur tape
    private void filtrer() {
        String texte = searchField.getText().trim().toLowerCase(); // Texte entré par l’utilisateur
        if (texte.isEmpty()) {
            afficherProduits(tousLesProduits); // Si vide, on affiche tout
            return;
        }

        // Filtrage avec Java Streams : on garde les produits dont le nom ou l’ID contient le texte
        List<String[]> filtres = tousLesProduits.stream()
                .filter(p -> p[0].contains(texte) || p[1].toLowerCase().contains(texte))
                .collect(Collectors.toList());

        afficherProduits(filtres); // Affiche la liste filtrée
    }
}
# Explication de la classe
La classe AfficherProduitsSwing permet d’afficher la liste des produits enregistrés dans la base de données sous forme de tableau. Elle utilise Java Swing pour créer une interface graphique avec un champ de recherche, un tableau (JTable) et un modèle de données (DefaultTableModel). Au démarrage, la fenêtre charge tous les produits à l’aide de ProduitDAO et les affiche dans le tableau.

L’utilisateur peut ensuite rechercher un produit par nom ou par ID grâce au champ de recherche. La recherche s’effectue en temps réel grâce à un écouteur (DocumentListener) qui filtre les données à chaque modification. Si l’utilisateur clique sur une ligne du tableau, une nouvelle fenêtre s’ouvre (avec la classe ModifierOuSupprimerProduit) pour modifier ou supprimer le produit sélectionné.
