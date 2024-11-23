package com.example.project162.Domain;

import java.util.List;

public class Commande {
    private String id_user;
    private String ville;
    private String rue;
    private String nomComplet;
    private String dateCommande;
    private double prixTotal;
    private List<ProduitCommande> listeProduits;

    public Commande() {
        // Constructeur vide nécessaire pour Firebase
    }

    public Commande(String id_user, String ville, String rue, String nomComplet, String dateCommande, double prixTotal, List<ProduitCommande> listeProduits) {
        this.id_user = id_user;
        this.ville = ville;
        this.rue = rue;
        this.nomComplet = nomComplet;
        this.dateCommande = dateCommande;
        this.prixTotal = prixTotal;
        this.listeProduits = listeProduits;
    }

    // Getters et setters pour tous les champs

    public String getId_user() {
        return id_user;
    }

    public String getVille() {
        return ville;
    }

    public String getRue() {
        return rue;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public String getDateCommande() {
        return dateCommande;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public List<ProduitCommande> getListeProduits() {
        return listeProduits;
    }

    public void setListeProduits(List<ProduitCommande> listeProduits) {
        this.listeProduits = listeProduits;
    }
}
