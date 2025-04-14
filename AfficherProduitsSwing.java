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
        // Série d’instructions situées dans le constructeur
        setTitle("Liste des produits"); // définit le titre de la fenêtre
        setSize(550, 350); //  fixe la taille de la fenêtre
        setLayout(null); // permet un placement manuel des composants avec setBounds(...) au lieu d’utiliser un gestionnaire automatique

        // Série d’instructions situées dans le constructeur
        JLabel searchLabel = new JLabel("Rechercher (ID ou Nom) :");// Ce bloc crée une étiquette "Rechercher (ID ou Nom)" pour guider l’utilisateur.
        searchLabel.setBounds(20, 10, 200, 25); // Il positionne cette étiquette à un endroit précis dans la fenêtre.
        add(searchLabel); // Enfin, il ajoute cette étiquette à l’interface graphique.

        // Série d’instructions situées dans le constructeur
        searchField = new JTextField(); // Ce bloc crée un champ de saisie (JTextField) pour taper un mot-clé de recherche
        searchField.setBounds(200, 10, 200, 25); // Il place ce champ à une position précise dans la fenêtre avec setBounds
        add(searchField); // Il ajoute le champ de recherche à l’interface graphique

        //  Bloc sert à créer et afficher un tableau de produits dans la fenêtre
        String[] colonnes = {"ID", "Nom", "Quantité", "Prix"}; // En-têtes du tableau
        model = new DefaultTableModel(colonnes, 0); // Crée un modèle de tableau vide (sans lignes, mais avec ces colonnes).
        table = new JTable(model); // Crée un tableau graphique (JTable) basé sur le modèle.
        JScrollPane scroll = new JScrollPane(table); // Entoure le tableau d’un composant de défilement pour pouvoir scroller si le tableau devient grand.
        scroll.setBounds(20, 50, 500, 200); // Positionne la zone du tableau dans la fenêtre
        add(scroll); // Ajoute ce composant à la fenêtre.

        // Récupération des produits depuis la base de données
        tousLesProduits = new ProduitDAO().getTousLesProduits();
        afficherProduits(tousLesProduits); // Affichage initial

        //  Bloc permet de filtrer automatiquement la liste des produits affichés dès que l'utilisateur
        //  tape quelque chose dans le champ de recherch
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() { // On écoute les changements de texte dans searchField
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); } // Methode Appelé quand un changement de style est effectué (Jamais appelee dans notre cas)
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); } // Methode appelée quand du texte est supprimé → on met à jour l'affichage.
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }}); // Methode Appelé quand du texte est ajouté

        //  Bloc sert à ouvrir automatiquement une fenêtre de modification/suppression
        //  lorsqu’un utilisateur clique sur une ligne du tableau.
        table.getSelectionModel().addListSelectionListener(event -> { // On ajoute un écouteur pour détecter la sélection d’une ligne dans le tableau table
            if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {//  s’assurer que l’action n’est pas en cours de modification et qu’une ligne a bien été sélectionnée
                int row = table.getSelectedRow(); // On récupère l’indice de la ligne sélectionnée.
                // On extrait les valeurs de la ligne sélectionnée (id, nom, quantité, prix)
                // en les convertissant si nécessaire (car elles sont récupérées comme objets).
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
    // Méthode privee pour afficher tous les produits dans le tableau
    private void afficherProduits(List<String[]> produits) {
        model.setRowCount(0); // vide toutes les lignes du tableau pour repartir à zéro.
        for (String[] ligne : produits) { //La boucle for parcourt chaque produit et l’ajoute à la table avec model.addRow(ligne)
            model.addRow(ligne);}
    }

    // Méthode pour filtrer les produits selon ce que l’utilisateur tape
    private void filtrer() {
        String texte = searchField.getText().trim().toLowerCase(); // récupère le texte saisi par l’utilisateur, sans espaces, en minuscules.
        if (texte.isEmpty()) { // si l'utilisateur n'a rien écrit,
            afficherProduits(tousLesProduits); // Si vide, on affiche tous les produits (tousLesProduits).
            return; // on sort de la méthode sans faire de filtrage si le champ est vide.
        }
        // Filtrage avec Java Streams : on garde les produits dont le nom ou l’ID contient le texte
        List<String[]> filtres = tousLesProduits.stream() //transforme la liste en flux de données.
                .filter(p -> p[0].contains(texte) || p[1].toLowerCase().contains(texte)) // l’ID contient le texte saisi et le nom (en minuscules) contient le texte saisi.
                .collect(Collectors.toList()); // rassemble les produits filtrés dans une nouvelle liste filtres

        afficherProduits(filtres); // Affiche la liste filtrée
    }
}
