package com.example.project162.Domain;

public class ProduitCommande {
    private String titre;
    private double quantite;

    public ProduitCommande() {
        // Constructeur vide nécessaire pour Firebase
    }

    public ProduitCommande(String titre, double quantite) {
        this.titre = titre;
        this.quantite = quantite;
    }

    // Getters et setters pour tous les champs

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
