package ui;

// Importation des bibliothèques nécessaires
import net.sf.jasperreports.engine.*; // Bibliothèque JasperReports pour les rapports PDF
import utils.DatabaseConnection; // Classe utilitaire pour la connexion à la base de données
import javax.swing.*; // Bibliothèque pour l'interface graphique Swing
import java.awt.*; // Pour gérer les couleurs, polices, images, etc.
import java.sql.Connection; // Pour gérer les connexions SQL

// Classe principale qui représente la fenêtre principale de l’application
public class MenuPrincipal extends JFrame {

    // Constructeur : ce bloc s’exécute à la création de la fenêtre
    public MenuPrincipal() {
        setTitle("Menu Principal"); // Titre de la fenêtre
        setSize(450, 650); // Taille de la fenêtre
        setLayout(null); // Positionnement manuel
        getContentPane().setBackground(new Color(230, 245, 255)); // Couleur pastel pour le fond
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran

        // Chargement et affichage du logo
        ImageIcon logo = new ImageIcon("C:\\Users\\aicha\\Desktop\\2 eme session\\Programmation avancee\\UA3\\logo.png");
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Redimensionne le logo
        JLabel logoLabel = new JLabel(new ImageIcon(img)); // Création du composant d’image
        logoLabel.setBounds(170, 10, 100, 100); // Position du logo
        add(logoLabel); // Ajout du logo à la fenêtre

        // Création et configuration du titre
        JLabel titre = new JLabel("Application de Gestion de Stock", SwingConstants.CENTER); // Texte centré
        titre.setBounds(30, 120, 390, 30); // Position et taille
        titre.setFont(new Font("Arial", Font.BOLD, 18)); // Style du texte
        titre.setForeground(new Color(220, 115, 150)); // Couleur du texte (rose pastel)
        add(titre); // Ajout du titre à la fenêtre

        // Couleur utilisée pour les boutons
        Color boutonCouleur = new Color(181, 234, 215); // Vert pastel

        //  Bloc crée un tableau de chaînes (String[]) qui contient les noms de tous
        //  les boutons qui seront affichés dans l’interface du menu principal.
        String[] nomsBoutons = {
                "Ajouter Produit", "Afficher Produits", "Modifier Produit", "Supprimer Produit",
                "Mouvement Stock", "Historique Mouvements", "Exporter PDF", "Statistiques"
        };

        // Création des boutons à partir du tableau de noms
        JButton[] boutons = new JButton[nomsBoutons.length]; // Crée un tableau de boutons de la même taille que nomsBoutons, pour pouvoir ensuite les manipuler individuellement.
        int y = 170; // Position verticale initiale

        // Boucle pour créer chaque bouton et l’ajouter à la fenêtre
        for (int i = 0; i < nomsBoutons.length; i++) {
            boutons[i] = new JButton(nomsBoutons[i]); // Création du bouton
            boutons[i].setBounds(120, y, 200, 35); // Position et taille du bouton
            boutons[i].setBackground(boutonCouleur); // Appliquer la couleur pastel
            boutons[i].setFocusPainted(false); // Enlever le contour de sélection
            add(boutons[i]); // Ajouter le bouton à la fenêtre
            y += 45; // Décalage vertical pour le prochain bouton
        }

        // Association de chaque bouton à une action
        boutons[0].addActionListener(e -> new AjouterProduitSwing()); // Ouvre la fenêtre d’ajout
        boutons[1].addActionListener(e -> new AfficherProduitsSwing()); // Ouvre la liste des produits
        boutons[2].addActionListener(e -> new ModifierOuSupprimerProduit("modifier")); // Fenêtre modification
        boutons[3].addActionListener(e -> new ModifierOuSupprimerProduit("supprimer")); // Fenêtre suppression
        boutons[4].addActionListener(e -> new MouvementStockSwing()); // Fenêtre pour mouvements de stock
        boutons[5].addActionListener(e -> new HistoriqueMouvementsSwing()); // Affiche l’historique
        boutons[6].addActionListener(e -> exporterPDF()); // Génère le rapport PDF
        boutons[7].addActionListener(e -> new FenetreStatistiques()); // Fenêtre des statistiques

        setDefaultCloseOperation(EXIT_ON_CLOSE); // Fermer l’application en fermant la fenêtre
        setVisible(true); // Affiche la fenêtre
    }

    // Méthode privée pour générer le rapport PDF avec JasperReports
    private void exporterPDF() {
        try {
            Connection conn = DatabaseConnection.connect(); // Connexion à la base de données

            // Chemin vers le fichier .jrxml du rapport Jasper
            String jrxmlPath = getClass().getClassLoader().getResource("reports/rapport_stock.jrxml").getPath();

            JasperReport report = JasperCompileManager.compileReport(jrxmlPath); // Compilation du modèle
            JasperPrint print = JasperFillManager.fillReport(report, null, conn); // Remplissage avec les données

            // Chemin de sortie du fichier PDF
            String chemin = "C:\\Users\\aicha\\Desktop\\2 eme session\\Programmation avancee\\UA3\\rapport_stock.pdf";

            JasperExportManager.exportReportToPdfFile(print, chemin); // Export en PDF

            JOptionPane.showMessageDialog(this, "PDF généré avec succès !"); // Message de succès

        } catch (Exception ex) {
            ex.printStackTrace(); // Affichage de l’erreur dans la console
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du PDF"); // Message d’erreur
        }
    }
    // Point d’entrée principal de l’application
    public static void main(String[] args) {
        new MenuPrincipal(); // Lance la fenêtre principale
    }
}
