# ModifierOuSupprimerProduit.java

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
